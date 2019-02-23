package pl.itcraft.appstract.admin.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.itcraft.appstract.admin.Constants;
import pl.itcraft.appstract.admin.order.OrdersInStatusesDto;
import pl.itcraft.appstract.admin.qualia.QualiaService;
import pl.itcraft.appstract.commons.orders.OrderRepository;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.dto.PageableDto;
import pl.itcraft.appstract.core.entities.County;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.OrderInternalStatus;
import pl.itcraft.appstract.core.enums.OrderStatusDetailsFilter;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.utils.UtilsBean;

@Service
@Transactional
public class OrdersService {
	
	private static final String ORDER_NOT_DECLINED_QUERY_PART = "(da IS NULL OR :abstractor NOT IN (SELECT das FROM Order os LEFT JOIN os.declinedAbstractors das WHERE os = o))";

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private QualiaService qualiaService;

	public Order getOne(Long orderId, User abstractor) {
		Optional<Order> optional = orderRepository.findFirstByIdAndAbstractor(orderId, abstractor);
		if(!optional.isPresent()) {
			throw new ApiException(RC.NOT_FOUND, "Order not found");
		}
		return optional.get();
	}
	
	public Page<OrderListRowDto> getOrders(PageableDto pageableDto, OrdersListFilterDto filter) {
		Pageable pageable = pageableDto.getPageable(Constants.DEFAULT_PAGE_SIZE);
		StringBuilder whereBuilder = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		User user = utilsBean.getCurrentUser();
		filterToWhereWithParameters(filter, whereBuilder, params, user);
		
		TypedQuery<Order> q = em.createQuery(
			"SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.properties p " + whereBuilder.toString() + " ORDER BY " + mapSortToQuery(pageable.getSort()) + " o.createdAt DESC", Order.class
		).setFirstResult(pageable.getOffset())
		.setMaxResults(pageable.getPageSize());
		
		params.forEach(q::setParameter);
		
		List<OrderListRowDto> list = q.getResultList()
			.stream()
			.map(OrderListRowDto::new)
			.collect(Collectors.toList());
		
		long count = getOrdersCount(filter, user);
		return new PageImpl<OrderListRowDto>(list, pageable, count);
	}
	
	public Page<AbstractorOrderListRowDto> getOrdersForAbstractor(PageableDto pageableDto, OrdersListFilterDto filter) {
		Pageable pageable = pageableDto.getPageable(Constants.DEFAULT_PAGE_SIZE);
		StringBuilder whereBuilder = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		User user = utilsBean.getCurrentUser();
		filterToWhereWithParameters(filter, whereBuilder, params, user);
		
		Query q = em.createQuery(
			"SELECT DISTINCT o, "
			+ " CASE WHEN "
			+ ORDER_NOT_DECLINED_QUERY_PART
			+ " THEN TRUE ELSE FALSE END AS notDeclined "
			+ " FROM Order o LEFT JOIN FETCH o.properties p LEFT JOIN o.declinedAbstractors da " + whereBuilder.toString() + " ORDER BY " + mapSortToQuery(pageable.getSort()) + " o.createdAt DESC"
		).setFirstResult(pageable.getOffset())
		.setMaxResults(pageable.getPageSize());
		
		params.forEach(q::setParameter);
		
		Map<County, List<Long>> bestAbstractorsForCounty = new HashMap<>();
		List<AbstractorOrderListRowDto> list = new ArrayList<>();
		for (Object obj : q.getResultList()) {
			Object[] arr = (Object[]) obj;
			Order order = (Order) arr[0];
			boolean available = getAvailabilityForAbstractor(user, bestAbstractorsForCounty, order, (Boolean) arr[1]);
			list.add(new AbstractorOrderListRowDto(order, available));
		}
		
		long count = getOrdersCount(filter, user);
		return new PageImpl<AbstractorOrderListRowDto>(list, pageable, count);
	}

	private boolean getAvailabilityForAbstractor(User user, Map<County, List<Long>> bestAbstractorsForCounty, Order order, boolean notDeclined) {
		if (notDeclined && !user.equals(order.getAbstractor()) && !new DateTime(order.getCreatedAt()).plusMinutes(Constants.FIRST_ABSTRACTORS_TIME_LIMIT_IN_MINUTES).isBeforeNow()) {
			County county = order.getProperties().get(0).getCounty();
			if (!bestAbstractorsForCounty.containsKey(county)) {
				bestAbstractorsForCounty.put(county, qualiaService.findBestAbstractorsForCounty(county));
			}
			notDeclined = bestAbstractorsForCounty.get(county).contains(user.getId());
		}
		return notDeclined;
	}
	
	private long getOrdersCount(OrdersListFilterDto filter, User user) {
		StringBuilder whereBuilder = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		filterToWhereWithParameters(filter, whereBuilder, params, utilsBean.getCurrentUser());
		String declinedAbstractorsQueryPart = utilsBean.getCurrentUser().getRole() == UserRole.ABSTRACTOR ? " LEFT JOIN o.declinedAbstractors da " : "";
		TypedQuery<Long> q = em.createQuery("SELECT COUNT(DISTINCT o) FROM Order o LEFT JOIN o.properties p " + declinedAbstractorsQueryPart + whereBuilder.toString(), Long.class);
		
		params.forEach(q::setParameter);
		
		List<Long> results = q.getResultList();
		if (results.isEmpty()) {
			return 0;
		}
		return results.get(0);
	}
	
	private String mapSortToQuery(Sort sort) {
		if (sort == null) {
			return "";
		}
		StringBuilder orderBy = new StringBuilder();
		sort.forEach(o -> {
			orderBy.append(getSortColumn(o.getProperty()) + " " + o.getDirection().name() + ", ");
		});
		return orderBy.toString();
	}
	
	private String getSortColumn(String sortProperty) {
		switch (sortProperty) {
		case "quotedPrice": return "o.price";
		case "flatAddress": return "p.flatAddress";
		default: return "o." + sortProperty;
		}
	}
	
	private void filterToWhereWithParameters(OrdersListFilterDto filter, StringBuilder whereBuilder, Map<String, Object> params, User user) {
		if (filter == null) {
			return;
		}
		List<String> conditions = new ArrayList<>();
		if (filter.getStatus() != null && filter.getStatus() != OrderStatusDetailsFilter.ALL) {
			if (user.getRole() != UserRole.ABSTRACTOR || filter.getStatus() == OrderStatusDetailsFilter.OPEN) {
				conditions.add("o." + filter.getStatus().getFieldName() + " = TRUE");
			} else if (filter.getStatus() == OrderStatusDetailsFilter.DECLINED) {
				conditions.add("(da = :abstractor)");
				params.put("abstractor", user);
			} else if (filter.getStatus() == OrderStatusDetailsFilter.CLOSED) {
				conditions.add("(o.abstractor.id = :abstractorId AND o.completed = TRUE)");
				params.put("abstractorId", user.getId());
			} else if (filter.getStatus() == OrderStatusDetailsFilter.CANCELLED) {
				conditions.add("(o.abstractor.id = :abstractorId AND o.cancelled = TRUE)");
				params.put("abstractorId", user.getId());
			}
			if (user.getRole() == UserRole.ABSTRACTOR && filter.getStatus() == OrderStatusDetailsFilter.OPEN) {
				conditions.add(ORDER_NOT_DECLINED_QUERY_PART);
				params.put("abstractor", user);
			}
		}
		if (user.getRole() == UserRole.ABSTRACTOR) {
			conditions.add("(o.abstractor IS NULL OR o.abstractor = :abstractor)");
			params.put("abstractor", user);
		}
		if (StringUtils.isNotBlank(filter.getSearch())) {
			conditions.add("(LOWER(p.flatAddress) LIKE LOWER(:search) OR LOWER(o.orderNumber) LIKE LOWER(:search))");
			params.put("search", '%'+filter.getSearch()+'%');
		}
		if (!conditions.isEmpty()) {
			whereBuilder.append("WHERE " + StringUtils.join(conditions, " AND "));
		}
	}

	public List<NamedIdDto> getUserActiveOrders(User user) {
		return orderRepository.findAllActiveByAbstractor(user).stream().map(order -> new NamedIdDto(order.getId(), order.getOrderNumber())).collect(Collectors.toList());
	}

	public List<NamedIdDto> getOrdersWithAbstractors() {
		return orderRepository.findAllWithAssignedAbstractor().stream().map(order -> new NamedIdDto(order.getId(), order.getAbstractor().getFirstName() +
				" " + order.getAbstractor().getLastName() + " - " + order.getOrderNumber())).collect(Collectors.toList());
	}
	
	public boolean abstractorHasAnyOrders(User user) {
		return !em.createQuery("SELECT 1 FROM Order o WHERE o.abstractor = :abstractor")
				.setParameter("abstractor", user)
				.setMaxResults(1)
				.getResultList()
				.isEmpty();
	}
	
	public OrdersInStatusesDto getOrdersInStatuses() {
		User currentUser = utilsBean.getCurrentUser();
		String where = currentUser.getRole() == UserRole.ABSTRACTOR ? " WHERE o.abstractor_id = :abstractorId " : "";
		Query q = em.createNativeQuery("SELECT "
				+ " COALESCE(SUM(CASE WHEN o.internal_status IN (:unclaimed, :underReview) THEN 1 ELSE 0 END), 0) AS inOrderAcceptance, "
				+ " COALESCE(SUM(CASE WHEN o.internal_status IN (:inProgress, :overdue) THEN 1 ELSE 0 END), 0) AS inTitleProcessing, "
				+ " COALESCE(SUM(CASE WHEN o.internal_status IN (:pendingApproval, :abstractIncomplete) THEN 1 ELSE 0 END), 0) AS inClosingPrep, "
				+ " COALESCE(SUM(CASE WHEN o.internal_status = :accepted THEN 1 ELSE 0 END), 0) AS postClosing "
				+ " FROM orders o " + where)
			.setParameter("unclaimed", OrderInternalStatus.UNCLAIMED.name())
			.setParameter("underReview", OrderInternalStatus.UNDER_REVIEW.name())
			.setParameter("inProgress", OrderInternalStatus.IN_PROGRESS.name())
			.setParameter("overdue", OrderInternalStatus.OVERDUE.name())
			.setParameter("pendingApproval", OrderInternalStatus.PENDING_APPROVAL.name())
			.setParameter("abstractIncomplete", OrderInternalStatus.ABSTRACT_INCOMPLETE.name())
			.setParameter("accepted", OrderInternalStatus.ACCEPTED.name());
		
		if (currentUser.getRole() == UserRole.ABSTRACTOR) {
			q.setParameter("abstractorId", currentUser.getId());
		}
		
		List<?> results = q.getResultList();
		if (results.isEmpty()) {
			return new OrdersInStatusesDto();
		}
		Object[] result = (Object[])results.get(0);
		return new OrdersInStatusesDto(
			((Number) result[0]).intValue(),
			((Number) result[1]).intValue(),
			((Number) result[2]).intValue(),
			((Number) result[3]).intValue()
		);
	}
	
}
