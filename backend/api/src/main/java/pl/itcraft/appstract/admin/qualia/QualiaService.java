package pl.itcraft.appstract.admin.qualia;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.GraphQLRequestEntity;
import io.aexp.nodes.graphql.GraphQLResponseEntity;
import io.aexp.nodes.graphql.GraphQLTemplate;
import io.aexp.nodes.graphql.GraphQLTemplate.GraphQLMethod;
import pl.itcraft.appstract.admin.notifications.NotificationsService;
import pl.itcraft.appstract.admin.qualia.QualiaOrderDetailsDto.QualiaPerson;
import pl.itcraft.appstract.admin.qualia.QualiaOrderDetailsDto.QualiaProperty;
import pl.itcraft.appstract.admin.qualia.QualiaOrderDetailsDto.QualiaStatusDetails;
import pl.itcraft.appstract.commons.counties.CountyRepository;
import pl.itcraft.appstract.commons.users.UserRepository;
import pl.itcraft.appstract.core.CoreConstants;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.Borrower;
import pl.itcraft.appstract.core.entities.County;
import pl.itcraft.appstract.core.entities.Notification;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.Property;
import pl.itcraft.appstract.core.entities.Seller;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.OrderStatus;

@Service
@Transactional
public class QualiaService {
	
	private final static Logger logger = LoggerFactory.getLogger(QualiaService.class);
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CountyRepository countyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotificationsService notificationsService;
	
	@Value("${app.qualia.api_url}")
	private String qualiaApiUrl;
	
	@Value("${app.qualia.api_key}")
	private String qualiaApiKey;
	
	@Value("${app.upload_dir}")
	protected String uploadDir;

	public void importData() {
		GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
		try {
			GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
				.url(qualiaApiUrl)
				.headers(createRequestHeaders())
				.requestMethod(GraphQLMethod.QUERY)
				.request(QualiaOrdersListRequestDto.class)
				.arguments(new Arguments("orders", new Argument<QualiaOrdersInput>("input", new QualiaOrdersInput("PENDING"))))
				.scalars(BigDecimal.class)
				.build();
			GraphQLResponseEntity<QualiaOrdersListRequestDto> responseEntity = graphQLTemplate.query(requestEntity, QualiaOrdersListRequestDto.class);
			saveOrders(responseEntity.getResponse().getOrders());
		} catch (IllegalStateException | MalformedURLException e) {
			logger.error("Error in retrieving orders list from Qualia");
			throw new RuntimeException(e);
		}
	}
	
	private void saveOrders(List<QualiaOrderDto> orders) {
		Set<String> existingOrdersQualiaIds = new HashSet<>(em.createQuery("SELECT o.qualiaId FROM Order o WHERE o.status = :pending", String.class)
			.setParameter("pending", OrderStatus.PENDING)
			.getResultList());
		for (QualiaOrderDto dto : orders) {
			if (existingOrdersQualiaIds.contains(dto.getId())) {
				continue;
			}
			QualiaOrderDetailsDto dt = importOrderData(dto.getId());
			QualiaStatusDetails details = dt.getStatusDetails();
			List<Property> properties = new ArrayList<>();
			List<Seller> sellers = new ArrayList<>();
			List<Borrower> borrowers = new ArrayList<>();
			Order order = new Order(
				dt.getId(), dt.getOrderNumber(), dt.getStatus(), dt.getCustomerName(), dt.getProductName(),
				dt.getQuotedPrice(), dt.getPrice(), dt.getDueDate(), dt.getProjectedCloseDate(),
				dt.isPayOnClose(), dt.isChargedAtBeginning(), details.getCreatedDate(),
				details.isOpen(), details.isCompleted(), details.isCancelled(),
				dt.getPurpose(), dt.getCustomerOrderNumber(), dt.getProductDescription(), dt.getCustomerContact() != null ? dt.getCustomerContact().getName() : null,
				sellers, borrowers,
				properties);
			for (QualiaProperty qp : dt.getProperties()) {
				Optional<County> optional = countyRepository.findFirstByName(qp.getCounty());
				if(optional.isPresent()) {
					properties.add(new Property(order, qp.getFlatAddress(), optional.get()));
				} else {
					County newCounty = new County(qp.getCounty());
					countyRepository.save(newCounty);
					properties.add(new Property(order, qp.getFlatAddress(), newCounty));
				}
			}
			for (QualiaPerson qp : dt.getBorrowers()) {
				if (StringUtils.isNotBlank(qp.getFullName())) {
					borrowers.add(new Borrower(order, qp.getFullName()));
				}
			}
			for (QualiaPerson qp : dt.getSellers()) {
				if (StringUtils.isNotBlank(qp.getFullName())) {
					sellers.add(new Seller(order, qp.getFullName()));
				}
			}
			em.persist(order);
			List<User> abstractors = userRepository.findAbstractorsForNotifications();
			for (User abstractor : abstractors) {
				notificationsService.sendNotification(Notification.makeNewOrderNotification(order, abstractor));
			}
		}
	}
	
	private QualiaOrderDetailsDto importOrderData(String qualiaId) {
		final String REQUEST_STRING = "query {\n"
			+ " order(_id: \""+qualiaId+"\") {\n"
			+ "  order {\n"
			+ "   _id\n"
			+ "   order_number\n"
			+ "   customer_name\n"
			+ "   product_name\n"
			+ "   quoted_price\n"
			+ "   price\n"
			+ "   status\n"
			+ "   due_date\n"
			+ "   projected_close_date\n"
			+ "   pay_on_close\n"
			+ "   charged_at_beginning\n"
			+ "   purpose\n"
			+ "   customer_order_number\n"
			+ "   customer_contact {\n"
			+ "    name\n"
			+ "   }\n"
			+ "   product_description\n"
			+ "   status_details {\n"
			+ "    open\n"
			+ "    completed\n"
			+ "    cancelled\n"
			+ "    created_date\n"
			+ "   }\n"
			+ "   borrowers {\n"
			+ "    full_name\n"
			+ "   }\n"
			+ "   sellers {\n"
			+ "    full_name\n"
			+ "   }\n"
			+ "   ... on Title {\n"
			+ "    properties {\n"
			+ "     county\n"
			+ "     flat_address\n"
			+ "    }\n"
			+ "   }\n"
			+ "  }\n"
			+ " }\n"
			+ "}";
		
		GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
		try {
			GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
				.url(qualiaApiUrl)
				.headers(createRequestHeaders())
				.requestMethod(GraphQLMethod.QUERY)
				.request(REQUEST_STRING)
				.scalars(BigDecimal.class)
				.build();
			GraphQLResponseEntity<QualiaOrderRequestDto> responseEntity = graphQLTemplate.query(requestEntity, QualiaOrderRequestDto.class);
			return responseEntity.getResponse().getOrder();
		} catch (IllegalStateException | MalformedURLException e) {
			logger.error("Error in retrieving orders list from Qualia");
			throw new RuntimeException(e);
		}
	}
	
	public void declineOrder(String qualiaId) {
		GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
		try {
			GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
				.url(qualiaApiUrl)
				.headers(createRequestHeaders())
				.requestMethod(GraphQLMethod.MUTATE)
				.request(QualiaDeclineOrderResultDto.class)
				.arguments(new Arguments("declineOrder", new Argument<QualiaOrderIdDto>("input", new QualiaOrderIdDto(qualiaId))))
				.scalars(BigDecimal.class)
				.build();
			GraphQLResponseEntity<QualiaDeclineOrderResultDto> responseEntity = graphQLTemplate.mutate(requestEntity, QualiaDeclineOrderResultDto.class);
			if (responseEntity.getErrors() != null) {
				logger.error("Qualia error in declining order");
				for (io.aexp.nodes.graphql.internal.Error e : responseEntity.getErrors()) {
					logger.error(e.getMessage());
				}
				throw new ApiException(RC.GENERAL_ERROR, "Order can not be declined in qualia");
			}
			String status = responseEntity.getResponse().getDeclineOrder().getStatus();
			if (!"Declined".equals(status)) {
				throw new ApiException(RC.FORBIDDEN, "Order in status " + status + " can not be declined in qualia");
			}
		} catch (IllegalStateException | MalformedURLException e) {
			logger.error("Error in declining order in Qualia");
			throw new RuntimeException(e);
		}
	}
	
	// TODO remove, only for testing
	@Deprecated
	public void acceptOrderTest(Long orderId) {
		Order o = em.find(Order.class, orderId);
		acceptOrder(o);
	}
	
	public void acceptOrder(Order order) {
		acceptOrderInQualia(order.getQualiaId());
		fillOrderFulfillmentInQualia(order);
		addTitleSearchDocumentInQualia(order);
	}
	
	private void acceptOrderInQualia(String qualiaId) {
		GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
		try {
			GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
				.url(qualiaApiUrl)
				.headers(createRequestHeaders())
				.requestMethod(GraphQLMethod.MUTATE)
				.request(QualiaAcceptOrderResultDto.class)
				.arguments(new Arguments("acceptOrder", new Argument<QualiaOrderIdDto>("input", new QualiaOrderIdDto(qualiaId))))
				.scalars(BigDecimal.class)
				.build();
			GraphQLResponseEntity<QualiaAcceptOrderResultDto> responseEntity = graphQLTemplate.mutate(requestEntity, QualiaAcceptOrderResultDto.class);
			if (responseEntity.getErrors() != null) {
				logger.error("Qualia error in accepting order");
				for (io.aexp.nodes.graphql.internal.Error e : responseEntity.getErrors()) {
					logger.error(e.getMessage());
				}
				throw new ApiException(RC.GENERAL_ERROR, "Order can not be accepted in qualia");
			}
			String status = responseEntity.getResponse().getAcceptOrder().getStatus();
			if (!"Open".equals(status)) {
				throw new ApiException(RC.FORBIDDEN, "Order in status " + status + " can not be accepted in qualia");
			}
		} catch (IllegalStateException | MalformedURLException e) {
			logger.error("Error in accepting order in Qualia");
			throw new RuntimeException(e);
		}
	}
	
	private void fillOrderFulfillmentInQualia(Order order) {
		GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
		try {
			GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
				.url(qualiaApiUrl)
				.headers(createRequestHeaders())
				.requestMethod(GraphQLMethod.MUTATE)
				.request(QualiaFulfillTitleSearchResultDto.class)
				.arguments(new Arguments("fulfillTitleSearchPlus", new Argument<QualiaTitleSearchPlusInput>("input", new QualiaTitleSearchPlusInput(order))))
				.build();
			System.out.println(requestEntity.getRequest());
			GraphQLResponseEntity<QualiaFulfillTitleSearchResultDto> responseEntity = graphQLTemplate.mutate(requestEntity, QualiaFulfillTitleSearchResultDto.class);
			if (responseEntity.getErrors() != null) {
				logger.error("Qualia error in accepting order");
				for (io.aexp.nodes.graphql.internal.Error e : responseEntity.getErrors()) {
					logger.error(e.getMessage());
				}
				throw new ApiException(RC.GENERAL_ERROR, "Order can not be fulfilled in qualia");
			}
		} catch (IllegalStateException | MalformedURLException e) {
			logger.error("Error in accepting order in Qualia");
			throw new RuntimeException(e);
		}
	}
	
	private void addTitleSearchDocumentInQualia(Order order) {
		GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
		try {
			byte[] fileBytes = Files.readAllBytes(Paths.get(uploadDir, order.getTitleSearchDocumentFile().getPath().replace("_", CoreConstants.DS)));
			String base64 = Base64.encodeBase64String(fileBytes);
			GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
					.url(qualiaApiUrl)
					.headers(createRequestHeaders())
					.requestMethod(GraphQLMethod.MUTATE)
					.request(QualiaAddFilesResultDto.class)
					.arguments(new Arguments("addFiles", new Argument<QualiaAddFilesInput>("input", new QualiaAddFilesInput(order, base64))))
					.build();
			System.out.println(requestEntity.getRequest());
			GraphQLResponseEntity<QualiaAddFilesResultDto> responseEntity = graphQLTemplate.mutate(requestEntity, QualiaAddFilesResultDto.class);
			if (responseEntity.getErrors() != null) {
				logger.error("Qualia error in accepting order");
				for (io.aexp.nodes.graphql.internal.Error e : responseEntity.getErrors()) {
					logger.error(e.getMessage());
				}
				throw new ApiException(RC.GENERAL_ERROR, "File can not be uploaded to qualia");
			}
		} catch (IllegalStateException | MalformedURLException e) {
			logger.error("Error in uploading file to Qualia");
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error("Error in reading order title search document file");
			throw new RuntimeException(e);
		}
	}

	private Map<String, String> createRequestHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Basic " + qualiaApiKey);
		return headers;
	}
	
	public List<Long> findBestAbstractorsForOrder(Order o) {
		return findBestAbstractorsForCounty(o.getProperties().get(0).getCounty());
	}
	
	public List<Long> findBestAbstractorsForCounty(County c) {
		List<?> result = em.createNativeQuery(
				"SELECT u.id FROM users u LEFT JOIN abstractor_order_ratings aor ON aor.abstractor_id = u.id WHERE u.id IN "
						+ " (SELECT uac.id FROM users uac LEFT JOIN orders o ON o.abstractor_id = uac.id WHERE uac.id IN "
						+ "  (SELECT uc.id FROM users uc JOIN abstractor_counties ac ON ac.abstractor_id = uc.id WHERE ac.county_id = :orderCountyId) "
						+ " GROUP BY uac.id ORDER BY AVG(o.final_cost) ASC LIMIT 20) "
						+ "GROUP BY u.id ORDER BY AVG(aor.rate) DESC NULLS LAST LIMIT 10")
				.setParameter("orderCountyId", c.getId())
				.getResultList();
		
		List<Long> ids = new ArrayList<>();
		for (Object id : result) {
			ids.add(((Number) id).longValue());
		}
		return ids;
	}
	
}
