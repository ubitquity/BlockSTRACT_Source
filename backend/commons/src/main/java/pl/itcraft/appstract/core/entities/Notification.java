package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.itcraft.appstract.core.enums.NotificationType;

@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "seen")
	private boolean seen;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private NotificationType type;
	
	protected Notification() {
		// JPA
	}
	
	public Notification(User user, Order order, String content, NotificationType type) {
		this.createdAt = new Date();
		this.seen = false;
		this.user = user;
		this.order = order;
		this.content = content;
		this.type = type;
	}
	
	public static Notification makeNewMessageNotification(Order order, User sender, User recipient) {
		String content = sender.getFirstName() + " " + sender.getLastName() + " sent you a message";
		return new Notification(recipient, order, content, NotificationType.MESSAGE);
	}
	
	public static Notification makeOrderAcceptedByAbstractorNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " was accepted by an abstractor";
		return new Notification(recipient, order, content, NotificationType.ORDER_ACCEPTED_BY_ABSTRACTOR);
	}
	
	public static Notification makeOrderAcceptedByAdminNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " was accepted";
		return new Notification(recipient, order, content, NotificationType.ORDER_ACCEPTED_BY_ADMIN);
	}
	
	public static Notification makeOrderSubmittedForApprovalNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " was submitted for approval";
		return new Notification(recipient, order, content, NotificationType.ORDER_SUBMITTED_FOR_APPROVAL);
	}
	
	public static Notification makeOrderDeclinedNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " was declined";
		return new Notification(recipient, order, content, NotificationType.ORDER_DECLINED);
	}
	
	public static Notification makeOrderCancelledNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " was cancelled";
		return new Notification(recipient, order, content, NotificationType.ORDER_CANCELLED);
	}
	
	public static Notification makeOrderRecalledNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " was recalled";
		return new Notification(recipient, order, content, NotificationType.ORDER_RECALLED);
	}

	public static Notification makeOrderOverdueNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " is overdue";
		return new Notification(recipient, order, content, NotificationType.ORDER_OVERDUE);
	}
	
	public static Notification makeOrderInProgressNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " is in progress";
		return new Notification(recipient, order, content, NotificationType.ORDER_IN_PROGRESS);
	}

	public static Notification makeOrderIncompleteNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " is incomplete";
		return new Notification(recipient, order, content, NotificationType.ORDER_INCOMPLETE);
	}

	public static Notification makeOrderPaidNotification(Order order, User recipient) {
		String content = "Order " + order.getOrderNumber() + " is paid";
		return new Notification(recipient, order, content, NotificationType.ORDER_PAID);
	}
	
	public static Notification makeNewOrderNotification(Order order, User recipient) {
		String content = "A new order (" + order.getOrderNumber() + ") has appeared in appstract";
		return new Notification(recipient, order, content, NotificationType.NEW_ORDER);
	}

	public User getUser() {
		return user;
	}

	public Order getOrder() {
		return order;
	}

	public String getContent() {
		return content;
	}

	public boolean isSeen() {
		return seen;
	}

	public NotificationType getType() {
		return type;
	}
	
}
