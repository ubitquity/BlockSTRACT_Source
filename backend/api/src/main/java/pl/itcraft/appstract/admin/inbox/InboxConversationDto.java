package pl.itcraft.appstract.admin.inbox;

import pl.itcraft.appstract.core.entities.InboxConversation;
import pl.itcraft.appstract.core.entities.InboxMessage;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.UserRole;

public class InboxConversationDto {

	private long id;
	private long orderId;
	private String user;
	private String orderNumber;
	private Long lastMessageTime;
	private String lastMessageText;
	private boolean hasNewMessages;
	
	public InboxConversationDto(InboxConversation ic, UserRole role) {
		this(ic.getOrder());
		this.id = ic.getId();
		InboxMessage lastMessage = ic.getLastMessage();
		this.lastMessageTime = lastMessage.getCreatedAt().getTime();
		this.lastMessageText = lastMessage.getContent();
		this.hasNewMessages = role == UserRole.ADMIN ? ic.isAdminNewMessage() : ic.isAbstractorNewMessage();
	}
	
	public InboxConversationDto(Order order) {
		this.orderId = order.getId();
		User abstractor = order.getAbstractor();
		if (abstractor != null) {
			this.user = abstractor.getFirstName() + " " + abstractor.getLastName();
		}
		this.orderNumber = order.getOrderNumber();
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Long getLastMessageTime() {
		return lastMessageTime;
	}
	public void setLastMessageTime(Long lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}
	public String getLastMessageText() {
		return lastMessageText;
	}
	public void setLastMessageText(String lastMessageText) {
		this.lastMessageText = lastMessageText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public boolean isHasNewMessages() {
		return hasNewMessages;
	}
	public void setHasNewMessages(boolean hasNewMessages) {
		this.hasNewMessages = hasNewMessages;
	}
	
}
