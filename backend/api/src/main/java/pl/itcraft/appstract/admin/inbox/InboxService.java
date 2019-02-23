package pl.itcraft.appstract.admin.inbox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pl.itcraft.appstract.admin.notifications.NotificationsService;
import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.commons.orders.OrderRepository;
import pl.itcraft.appstract.commons.users.UserRepository;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.Activity;
import pl.itcraft.appstract.core.entities.InboxConversation;
import pl.itcraft.appstract.core.entities.InboxMessage;
import pl.itcraft.appstract.core.entities.InboxMessageAttachment;
import pl.itcraft.appstract.core.entities.Notification;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.UploadedFile;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.file.UploadedFileDescriptor;
import pl.itcraft.appstract.core.file.UploadedFilesWriterService;
import pl.itcraft.appstract.core.utils.UtilsBean;

@Service
@Transactional
public class InboxService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private AdminAppModuleConfig config;
	
	@Autowired
	private UploadedFilesWriterService filesWriterService;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotificationsService notificationsService;

	public List<InboxConversationDto> getConversations() {
		return em.createQuery(
				"SELECT ic FROM InboxConversation ic "
				+ " JOIN FETCH ic.order o "
				+ " JOIN FETCH o.abstractor a "
				+ " JOIN FETCH ic.lastMessage lm "
				+ " ORDER BY lm.createdAt DESC", InboxConversation.class)
			.getResultList()
			.stream()
			.map(ic -> new InboxConversationDto(ic, utilsBean.getCurrentUser().getRole()))
			.collect(Collectors.toList());
	}

	public List<InboxConversationDto> getConversationsByAbstractor(User abstractor) {
		return em.createQuery(
				"SELECT ic FROM InboxConversation ic "
						+ " JOIN FETCH ic.order o "
						+ " JOIN FETCH o.abstractor a "
						+ " JOIN FETCH ic.lastMessage lm "
						+ " WHERE o.abstractor = :abstractor "
						+ " ORDER BY lm.createdAt DESC", InboxConversation.class)
				.setParameter("abstractor", abstractor)
				.getResultList()
				.stream()
				.filter(ic -> abstractor.equals(ic.getLastMessage().getAssignedAbstractor()))
				.map(ic -> new InboxConversationDto(ic, utilsBean.getCurrentUser().getRole()))
				.collect(Collectors.toList());
	}
	
	public InboxConversationWithMessagesDto getConversationWithMessages(Long orderId) {
		List<InboxConversation> result = em.createQuery(
				"SELECT ic FROM InboxConversation ic "
				+ " JOIN FETCH ic.order o "
				+ " LEFT JOIN FETCH o.abstractor a "
				+ " WHERE o.id = :orderId", InboxConversation.class)
			.setParameter("orderId", orderId)
			.getResultList();
		if (result.isEmpty()) {
			Order order = orderRepository.findOne(orderId);
			if (order == null) {
				throw new ApiException(RC.NOT_FOUND, "Order doesn't exist");
			}
			return new InboxConversationWithMessagesDto(order);
		}
		InboxConversation inboxConversation = result.get(0);
		User currentUser = utilsBean.getCurrentUser();
		InboxConversationWithMessagesDto dto = new InboxConversationWithMessagesDto(inboxConversation, currentUser, findInboxMessages(orderId), config);
		inboxConversation.setMessagesAsRead(currentUser.getRole());
		return dto;
	}
	
	private List<InboxMessage> findInboxMessages(long orderId) {
		User currentUser = utilsBean.getCurrentUser();
		final String assignedAbstractorQueryPart = currentUser.getRole() == UserRole.ABSTRACTOR ? " AND im.assignedAbstractor = :user " : "";
		TypedQuery<InboxMessage> q = em.createQuery("SELECT im FROM InboxConversation ic JOIN ic.inboxMessages im WHERE ic.order.id = :orderId " + assignedAbstractorQueryPart + " ORDER BY im.id ASC", InboxMessage.class)
				.setParameter("orderId", orderId);
		if (currentUser.getRole() == UserRole.ABSTRACTOR) {
			q.setParameter("user", currentUser);
		}
		return q.getResultList();
	}
	
	public InboxMessageDto sendMessage(long orderId, String content, MultipartFile[] multipartAttachments) {
		InboxConversation inboxConversation = findOrCreateForOrder(orderId);
		User sender = utilsBean.getCurrentUser();
		Order order = inboxConversation.getOrder();
		User assignedAbstractor = order.getAbstractor();
		List<InboxMessageAttachment> messageAttachments = new ArrayList<>();
		InboxMessage inboxMessage = new InboxMessage(inboxConversation, sender, assignedAbstractor, content, messageAttachments);
		if (multipartAttachments != null) {
			UploadedFile[] uploadedAttachments = filesWriterService.uploadAllFiles(createDescriptors(multipartAttachments));
			for (UploadedFile file : uploadedAttachments) {
				messageAttachments.add(new InboxMessageAttachment(inboxMessage, file));
			}
		}
		em.persist(inboxMessage);
		inboxMessage.setAsLastMessageInConversation();
		em.persist(Activity.makeNewMessageActivity(order, sender));
		if (sender.getRole() == UserRole.ABSTRACTOR) {
			User admin = userRepository.findFirstByRole(UserRole.ADMIN).get();
			notificationsService.sendNotification(Notification.makeNewMessageNotification(order, sender, admin));
		} else if (assignedAbstractor != null) {
			notificationsService.sendNotification(Notification.makeNewMessageNotification(order, sender, assignedAbstractor));
		}
		return new InboxMessageDto(inboxMessage, config);
	}
	
	private UploadedFileDescriptor[] createDescriptors(MultipartFile[] files) {
		UploadedFileDescriptor[] result = new UploadedFileDescriptor[files.length];
		int i = 0;
		for (MultipartFile file : files) {
			result[i] = UploadedFileDescriptor.create().withMultipartFile(file);
			i++;
		}
		return result;
	}
	
	private InboxConversation findOrCreateForOrder(long orderId) {
		List<InboxConversation> results = em.createQuery("SELECT ic FROM InboxConversation ic WHERE ic.order.id = :orderId", InboxConversation.class)
			.setParameter("orderId", orderId)
			.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		}
		InboxConversation newConversation = new InboxConversation(em.find(Order.class, orderId));
		em.persist(newConversation);
		return newConversation;
	}
	
}
