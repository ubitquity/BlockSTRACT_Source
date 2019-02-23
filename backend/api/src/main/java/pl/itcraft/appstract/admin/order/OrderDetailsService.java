package pl.itcraft.appstract.admin.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import pl.itcraft.appstract.admin.Constants;
import pl.itcraft.appstract.admin.notifications.NotificationsService;
import pl.itcraft.appstract.admin.qualia.QualiaService;
import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.commons.orders.AbstractorOrderRatingRepository;
import pl.itcraft.appstract.commons.orders.OrderRepository;
import pl.itcraft.appstract.commons.users.UserRepository;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.AbstractorOrderRating;
import pl.itcraft.appstract.core.entities.Activity;
import pl.itcraft.appstract.core.entities.Notification;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.OrderFile;
import pl.itcraft.appstract.core.entities.UploadedFile;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.OrderInternalStatus;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.file.UploadedFileDescriptor;
import pl.itcraft.appstract.core.file.UploadedFilesWriterService;
import pl.itcraft.appstract.core.utils.UtilsBean;

@Service
@Transactional
public class OrderDetailsService {
	
	@Autowired
	private OrderRepository repository;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private AdminAppModuleConfig config;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private UploadedFilesWriterService filesWriterService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotificationsService notificationsService;
	
	@Autowired
	private QualiaService qualiaService;
	
	@Autowired
	private AbstractorOrderRatingRepository abstractorOrderRatingRepository;
	
	public OrderDetailsDto getOrderDetails(long orderId) {
		checkIsMyOrUnclaimedOrder(orderId);
		Order order = repository.findOne(orderId);
		User user = userRepository.findOne(utilsBean.getCurrentUser().getId());
		Optional<AbstractorOrderRating> rating = abstractorOrderRatingRepository.findFirstByAbstractorAndOrder(order.getAbstractor(), order);
		int rate = rating.isPresent() ? rating.get().getRate() : 0;
		return new OrderDetailsDto(order, rate, user.getAbstractRates().isEmpty());
	}
	
	public List<OrderActivityListRowDto> getOrderActivities(long orderId) {
		return em.createQuery("SELECT a FROM Activity a WHERE a.order.id = :orderId ORDER BY a.createdAt DESC", Activity.class)
			.setParameter("orderId", orderId)
			.getResultList()
			.stream()
			.map(OrderActivityListRowDto::new)
			.collect(Collectors.toList());
	}
	
	public List<OrderFileDto> getOrderFiles(long orderId) {
		checkIsMyOrder(orderId);
		return em.createQuery("SELECT f FROM OrderFile f WHERE f.order.id = :orderId ORDER BY f.id ASC", OrderFile.class)
			.setParameter("orderId", orderId)
			.getResultList()
			.stream()
			.map(f -> new OrderFileDto(f, config))
			.collect(Collectors.toList());
	}
	
	public List<OrderFileDto> addOrderFiles(long orderId, MultipartFile[] files) {
		checkIsMyOrder(orderId);
		Order order = em.getReference(Order.class, orderId);
		List<OrderFileDto> results = new ArrayList<>();
		for (MultipartFile file : files) {
			UploadedFile uploadedFile = filesWriterService.uploadFile(UploadedFileDescriptor.create().withMultipartFile(file));
			OrderFile orderFile = new OrderFile(order, uploadedFile);
			em.persist(orderFile);
			results.add(new OrderFileDto(orderFile, config));
		}
		return results;
	}
	
	public void deleteOrderFile(long orderFileId) {
		OrderFile file = em.find(OrderFile.class, orderFileId);
		checkIsMyOrder(file.getOrder().getId());
		file.getUploadedFile().markAsToDelete();
		em.remove(file);
	}
	
	public void acceptOrder(long orderId) {
		User abstractor = em.find(User.class, utilsBean.getCurrentUser().getId());
		if (abstractor.getAbstractRates().isEmpty()) {
			throw new ApiException(RC.FORBIDDEN, "You need to fill your rates in profle before accepting an order");
		}
		Order order = repository.getOne(orderId);
		if (!isOrderAcceptableByAbstractor(order)) {
			throw new ApiException(RC.FORBIDDEN, "You cannot accept this order yet");
		}
		order.acceptOrder(abstractor);
		em.persist(Activity.makeOrderStatusChangeActivity(order, abstractor));
		User admin = userRepository.findFirstByRole(UserRole.ADMIN).get();
		notificationsService.sendNotification(Notification.makeOrderAcceptedByAbstractorNotification(order, admin));
	}
	
	public void declineOrderByAbstractor(long orderId) {
		User abstractor = utilsBean.getCurrentUser();
		Order order = repository.getOne(orderId);
		if (abstractor.equals(order.getAbstractor()) && order.getInternalStatus() != OrderInternalStatus.UNDER_REVIEW) {
			throw new ApiException(RC.FORBIDDEN, "Order can not be declined anymore");
		}
		declineOrder(order, abstractor);
	}

	public void declineOrder(Order order, User abstractor) {
		boolean recalled = false;
		if (abstractor.equals(order.getAbstractor())) {
			order.recall();
			em.persist(Activity.makeOrderStatusChangeActivity(order, utilsBean.getCurrentUser()));
			if (utilsBean.getCurrentUser().getRole() == UserRole.ADMIN) {
				recalled = true;
				notificationsService.sendNotification(Notification.makeOrderRecalledNotification(order, abstractor));
			} else {
				User admin = userRepository.findFirstByRole(UserRole.ADMIN).get();
				notificationsService.sendNotification(Notification.makeOrderDeclinedNotification(order, admin));
			}
			deleteDataRelatedToOrder(order.getId());
		}
		boolean orderNotDeclinedYet = em.createNativeQuery("SELECT 1 FROM abstractor_declined_orders WHERE abstractor_id = :abstractorId AND order_id = :orderId")
			.setParameter("abstractorId", abstractor.getId())
			.setParameter("orderId", order.getId())
			.getResultList()
			.isEmpty();
		if (orderNotDeclinedYet) {
			em.createNativeQuery("INSERT INTO abstractor_declined_orders(abstractor_id, order_id, recalled) VALUES(:abstractorId, :orderId, :recalled)")
				.setParameter("abstractorId", abstractor.getId())
				.setParameter("orderId", order.getId())
				.setParameter("recalled", recalled)
				.executeUpdate();
		}
	}
	
	public void cancelOrder(long orderId) {
		Order order = repository.getOne(orderId);
		if (order.getInternalStatus().isNotCancellable()) {
			throw new ApiException(RC.FORBIDDEN, "Order can not be cancelled in the current state");
		}
		User abstractor = order.getAbstractor();
		order.setAsCancelled();
		deleteDataRelatedToOrder(orderId);
		qualiaService.declineOrder(order.getQualiaId());
		em.persist(Activity.makeOrderStatusChangeActivity(order, utilsBean.getCurrentUser()));
		if (abstractor != null) {
			notificationsService.sendNotification(Notification.makeOrderCancelledNotification(order, abstractor));
		}
	}

	private void deleteDataRelatedToOrder(long orderId) {
		em.createQuery("DELETE FROM OrderFulfillmentParcelId p WHERE p.order.id = :orderId").setParameter("orderId", orderId).executeUpdate();
		em.createQuery("DELETE FROM OrderCharge c WHERE c.order.id = :orderId").setParameter("orderId", orderId).executeUpdate();
		em.createQuery("DELETE FROM OrderAbstractRate oar WHERE oar.order.id = :orderId").setParameter("orderId", orderId).executeUpdate();
		em.createQuery("DELETE FROM OrderDeedFile odf WHERE odf.order.id = :orderId").setParameter("orderId", orderId).executeUpdate();
		em.createQuery("UPDATE InboxConversation ic SET ic.abstractorNewMessage = FALSE WHERE ic.order.id = :orderId").setParameter("orderId", orderId).executeUpdate();
	}

	private void checkIsMyOrder(long orderId) {
		User user = utilsBean.getCurrentUser();
		if (user.getRole() != UserRole.ADMIN && !repository.findFirstByIdAndAbstractor(orderId, user).isPresent()) {
			throw new ApiException(RC.NOT_FOUND, "Order not found");
		}
	}
	
	private void checkIsMyOrUnclaimedOrder(long orderId) {
		User user = utilsBean.getCurrentUser();
		if (user.getRole() == UserRole.ABSTRACTOR) {
			Order order = repository.getOne(orderId);
			if (order.getAbstractor() != null && !user.equals(order.getAbstractor())) {
				throw new ApiException(RC.FORBIDDEN, "No access");
			}
			if (order.getAbstractor() == null && order.getDeclinedAbstractors().contains(user)) {
				throw new ApiException(RC.FORBIDDEN, "No access");
			}
		}
	}
	
	private boolean isOrderAcceptableByAbstractor(Order order) {
		if (new DateTime(order.getCreatedAt()).plusMinutes(Constants.FIRST_ABSTRACTORS_TIME_LIMIT_IN_MINUTES).isBeforeNow()) {
			return true;
		}
		return qualiaService.findBestAbstractorsForOrder(order).contains(utilsBean.getCurrentUser().getId());
	}
	
}
