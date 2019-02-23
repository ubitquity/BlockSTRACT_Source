package pl.itcraft.appstract.admin.order.fulfillment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pl.itcraft.appstract.admin.Constants;
import pl.itcraft.appstract.admin.notifications.NotificationsService;
import pl.itcraft.appstract.admin.order.OrderDetailsService;
import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.commons.orders.OrderRepository;
import pl.itcraft.appstract.commons.users.UserRepository;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.AbstractorOrderRating;
import pl.itcraft.appstract.core.entities.Activity;
import pl.itcraft.appstract.core.entities.Notification;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.OrderAbstractRate;
import pl.itcraft.appstract.core.entities.OrderCharge;
import pl.itcraft.appstract.core.entities.OrderDeedFile;
import pl.itcraft.appstract.core.entities.UploadedFile;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.OrderInternalStatus;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.file.UploadedFileDescriptor;
import pl.itcraft.appstract.core.file.UploadedFilesWriterService;
import pl.itcraft.appstract.core.utils.UtilsBean;
import pl.itcraft.appstract.core.validation.CoreValidationMessages;
import pl.itcraft.appstract.core.validation.ValidationErrors;

@Service
@Transactional
public class OrderFulfillmentService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private AdminAppModuleConfig config;
	
	@Autowired
	private UploadedFilesWriterService filesWriterService;
	
	@Autowired
	private OrderDetailsService orderDetailsService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotificationsService notificationsService;
	
	public OrderFulfillmentDataDto getOrderFulfillmentData(long orderId) {
		Order order = null;
		if (utilsBean.getCurrentUser().getRole() == UserRole.ADMIN) {
			order = repository.getOne(orderId);
		} else {
			Optional<Order> optional = repository.findFirstByIdAndAbstractor(orderId, utilsBean.getCurrentUser());
			if (!optional.isPresent()) {
				throw new ApiException(RC.NOT_FOUND, "Order not found");
			}
			order = optional.get();
		}
		return new OrderFulfillmentDataDto(order, config);
	}
	
	public void fillOrderFulfillmentData(long orderId, OrderFulfillmentFormDto dto) {
		Order order = findEditableOrder(orderId);
		em.createQuery("DELETE FROM OrderFulfillmentParcelId p WHERE p.order.id = :orderId").setParameter("orderId", orderId).executeUpdate();
		em.createQuery("DELETE FROM OrderCharge c WHERE c.order.id = :orderId").setParameter("orderId", orderId).executeUpdate();
		List<OrderCharge> charges = dto.getCharges() != null ?
			Arrays.stream(dto.getCharges()).map(
				c -> new OrderCharge(
					order,
					em.getReference(OrderAbstractRate.class, c.getOrderAbstractRateId()),
					c.getUnits()
				)
			).collect(Collectors.toList())
			: null;
		order.fillFulfillmentData(dto.getParcelIds(), dto.getStartDate(), dto.getEndDate(), dto.getTitleVesting(), dto.getLegalDescription(), dto.getEstateType(), dto.getOutstandingMortgage(), dto.getCommitmentRequirements(), dto.getCommitmentExceptions(), dto.getCopyCostPerUnit(), dto.getCopyUnits(), charges);
	}

	public List<DeedFileDto> addOrderDeedFiles(long orderId, MultipartFile[] deedFiles) {
		Order order = findEditableOrder(orderId);
		List<DeedFileDto> result = new ArrayList<>();
		for (MultipartFile deedFile : deedFiles) {
			UploadedFileDescriptor fileDescriptor = UploadedFileDescriptor.create().withMultipartFile(deedFile);
			UploadedFile uploadedFile = filesWriterService.uploadFile(fileDescriptor);
			OrderDeedFile orderDeedFile = new OrderDeedFile(order, uploadedFile);
			em.persist(orderDeedFile);
			result.add(new DeedFileDto(orderDeedFile, config));
		}
		return result;
	}
	
	public void deleteDeedFile(long orderId, long deedFileId) {
		Order order = findEditableOrder(orderId);
		OrderDeedFile orderDeedFile = em.find(OrderDeedFile.class, deedFileId);
		if (!order.equals(orderDeedFile.getOrder())) {
			throw new ApiException(RC.NOT_FOUND, "Deed file not found");
		}
		orderDeedFile.getUploadedFile().markAsToDelete();
		em.remove(orderDeedFile);
	}
	
	public TitleSearchDocumentDto changeTitleSearchDocumentFile(long orderId, MultipartFile titleSearchDocumentFile) {
		Order order = findEditableOrder(orderId);
		UploadedFileDescriptor fileDescriptor = UploadedFileDescriptor.create().withMultipartFile(titleSearchDocumentFile);
		UploadedFile uploadedFile = filesWriterService.uploadFile(fileDescriptor);
		order.changeTitleSearchDocumentFile(uploadedFile);
		return new TitleSearchDocumentDto(uploadedFile, config);
	}
	
	public void deleteTitleSearchDocumentFile(long orderId) {
		Order order = findEditableOrder(orderId);
		order.changeTitleSearchDocumentFile(null);
	}

	public void submitForApproval(long orderId) {
		Order order = findEditableOrder(orderId);
		validateOrderForApproval(order);
		order.submitForApproval();
		em.persist(Activity.makeOrderStatusChangeActivity(order, utilsBean.getCurrentUser()));
		User admin = userRepository.findFirstByRole(UserRole.ADMIN).get();
		notificationsService.sendNotification(Notification.makeOrderSubmittedForApprovalNotification(order, admin));
	}
	
	private void validateOrderForApproval(Order order) {
		ValidationErrors errors = new ValidationErrors();
		if (order.getParcelIds().isEmpty()) {
			errors.rejectValue("parcelIds", CoreValidationMessages.REQUIRED);
		}
		if (order.getStartDate() == null) {
			errors.rejectValue("startDate", CoreValidationMessages.REQUIRED);
		}
		if (order.getEndDate() == null) {
			errors.rejectValue("endDate", CoreValidationMessages.REQUIRED);
		}
		if (StringUtils.isBlank(order.getTitleVesting())) {
			errors.rejectValue("titleVesting", CoreValidationMessages.REQUIRED);
		}
		if (StringUtils.isBlank(order.getLegalDescription())) {
			errors.rejectValue("legalDescription", CoreValidationMessages.REQUIRED);
		}
		if (StringUtils.isBlank(order.getEstateType())) {
			errors.rejectValue("estateType", CoreValidationMessages.REQUIRED);
		}
		if (StringUtils.isBlank(order.getOutstandingMortgage())) {
			errors.rejectValue("outstandingMortgage", CoreValidationMessages.REQUIRED);
		}
		if (StringUtils.isBlank(order.getCommitmentRequirements())) {
			errors.rejectValue("commitmentRequirements", CoreValidationMessages.REQUIRED);
		}
		if (StringUtils.isBlank(order.getCommitmentExceptions())) {
			errors.rejectValue("commitmentExceptions", CoreValidationMessages.REQUIRED);
		}
		if (order.getCopyCostPerUnit() != null && order.getCopyCostPerUnit().compareTo(BigDecimal.ZERO) < 0) {
			errors.rejectValue("copyCostPerUnit", CoreValidationMessages.INVALID_MIN_VALUE, 0);
		}
		if (order.getCopyUnits() != null && order.getCopyUnits() <= 0) {
			errors.rejectValue("copyUnits", CoreValidationMessages.INVALID_MIN_VALUE, 1);
		}
		if (order.getCharges().isEmpty()) {
			errors.rejectValue("charges", CoreValidationMessages.REQUIRED);
		}
		if (order.getDeedFiles().isEmpty()) {
			errors.rejectValue("deedFiles", CoreValidationMessages.REQUIRED);
		}
		if (order.getAbstractRates().isEmpty()) {
			errors.rejectValue("abstractRates", CoreValidationMessages.REQUIRED);
		}
		if (order.getTitleSearchDocumentFile() == null) {
			errors.rejectValue("titleSearchDocumentFile", CoreValidationMessages.REQUIRED);
		}
		errors.throwIfNotEmpty();
	}
	
	public void setAsIncomplete(long orderId) {
		Order order = findPendingForApprovalOrder(orderId);
		order.setAsIncomplete();
		em.persist(Activity.makeOrderStatusChangeActivity(order, utilsBean.getCurrentUser()));
		notificationsService.sendNotification(Notification.makeOrderIncompleteNotification(order, order.getAbstractor()));
	}
	
	public void setAsAccepted(long orderId, int rate) {
		Order order = findPendingForApprovalOrder(orderId);
		em.persist(new AbstractorOrderRating(order, rate));
		order.setAsAccepted();
		em.persist(Activity.makeOrderStatusChangeActivity(order, utilsBean.getCurrentUser()));
		notificationsService.sendNotification(Notification.makeOrderAcceptedByAdminNotification(order, order.getAbstractor()));
	}
	
	public void setAsPaid(long orderId) {
		Order order = findAcceptedOrder(orderId);
		order.setAsPaid();
		em.persist(Activity.makeOrderStatusChangeActivity(order, utilsBean.getCurrentUser()));
		notificationsService.sendNotification(Notification.makeOrderPaidNotification(order, order.getAbstractor()));
	}
	
	public void recall(long orderId, int rate) {
		Order order = findAssignedOrder(orderId);
		em.persist(new AbstractorOrderRating(order, rate));
		orderDetailsService.declineOrder(order, order.getAbstractor());
	}
	
	public int setUnderReviewOrdersAsInProgress() {
		Date timeLimit = new DateTime().minusHours(Constants.ORDER_UNDER_REVIEW_TIME_PERIOD_IN_HOURS).toDate();
		OrderInternalStatus oldStatus = OrderInternalStatus.UNDER_REVIEW;
		List<Order> orders = findOrdersInStatusAfterTimeLimit(oldStatus, timeLimit);
		User admin = userRepository.findFirstByRole(UserRole.ADMIN).get();
		for (Order order : orders) {
			em.persist(Activity.makeOrderInProgressActivity(order));
			notificationsService.sendNotification(Notification.makeOrderInProgressNotification(order, admin));
			notificationsService.sendNotification(Notification.makeOrderInProgressNotification(order, order.getAbstractor()));
		}
		return updateOrderStatusesAfterTimeLimit(oldStatus, OrderInternalStatus.IN_PROGRESS, timeLimit);
	}
	
	public int setInProgressOrdersAsOverdue() {
		Date timeLimit = new DateTime().minusHours(Constants.ORDER_OVERDUE_TIME_LIMIT_IN_HOURS).toDate();
		OrderInternalStatus oldStatus = OrderInternalStatus.IN_PROGRESS;
		List<Order> orders = findOrdersInStatusAfterTimeLimit(oldStatus, timeLimit);
		User admin = userRepository.findFirstByRole(UserRole.ADMIN).get();
		for (Order order : orders) {
			em.persist(Activity.makeOrderOverdueActivity(order));
			notificationsService.sendNotification(Notification.makeOrderOverdueNotification(order, admin));
			notificationsService.sendNotification(Notification.makeOrderOverdueNotification(order, order.getAbstractor()));
		}
		return updateOrderStatusesAfterTimeLimit(oldStatus, OrderInternalStatus.OVERDUE, timeLimit);
	}
	
	private List<Order> findOrdersInStatusAfterTimeLimit(OrderInternalStatus status, Date timeLimit) {
		return em.createQuery("SELECT o FROM Order o WHERE o.internalStatus = :status AND o.acceptedByAbstractorTime < :timeLimit", Order.class)
			.setParameter("status", status)
			.setParameter("timeLimit", timeLimit)
			.getResultList();
	}
	
	private int updateOrderStatusesAfterTimeLimit(OrderInternalStatus oldStatus, OrderInternalStatus newStatus, Date timeLimit) {
		return em.createQuery("UPDATE Order o SET o.internalStatus = :newStatus WHERE o.internalStatus = :oldStatus AND o.acceptedByAbstractorTime < :timeLimit")
			.setParameter("newStatus", newStatus)
			.setParameter("oldStatus", oldStatus)
			.setParameter("timeLimit", timeLimit)
			.executeUpdate();
	}
	
	private Order findEditableOrder(long orderId) {
		Optional<Order> optional = repository.findFirstByIdAndAbstractor(orderId, utilsBean.getCurrentUser());
		if (!optional.isPresent()) {
			throw new ApiException(RC.NOT_FOUND, "Order not found");
		}
		Order order = optional.get();
		if (!order.getInternalStatus().isEditable()) {
			throw new ApiException(RC.FORBIDDEN, "Order can not be edited in the current state");
		}
		return order;
	}
	
	private Order findPendingForApprovalOrder(long orderId) {
		Order order = repository.findOne(orderId);
		if (order.getInternalStatus() != OrderInternalStatus.PENDING_APPROVAL) {
			throw new ApiException(RC.FORBIDDEN, "Order is not pending for approval");
		}
		return order;
	}
	
	private Order findAcceptedOrder(long orderId) {
		Order order = repository.findOne(orderId);
		if (order.getInternalStatus() != OrderInternalStatus.ACCEPTED) {
			throw new ApiException(RC.FORBIDDEN, "Order is not accepted");
		}
		return order;
	}
	
	private Order findAssignedOrder(long orderId) {
		Order order = repository.findOne(orderId);
		if (order.getAbstractor() == null) {
			throw new ApiException(RC.FORBIDDEN, "Order is not accepted by any abstractor");
		}
		return order;
	}
	
}
