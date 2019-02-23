package pl.itcraft.appstract.admin.notifications;

import pl.itcraft.appstract.core.entities.Notification;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.enums.NotificationType;

public class NotificationDto {

	private Long id;
	private String content;
	private NotificationType type;
	private Long orderId;
	
	public NotificationDto(Notification notification) {
		this.id = notification.getId();
		this.content = notification.getContent();
		this.type = notification.getType();
		Order order = notification.getOrder();
		if (order != null) {
			this.orderId = order.getId();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}
