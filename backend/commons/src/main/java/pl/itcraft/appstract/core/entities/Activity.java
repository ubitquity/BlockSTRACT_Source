package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.itcraft.appstract.core.enums.ActivityType;
import pl.itcraft.appstract.core.enums.OrderInternalStatus;

@Entity
@Table(name = "activities")
public class Activity extends BaseEntity {

	@Column(name = "name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private ActivityType type;
	
	@Column(name = "entity_id")
	private Long entityId;
	
	protected Activity() {
		// JPA
	}
	
	public Activity(User user, Order order, String name, ActivityType type, Long entityId) {
		this.createdAt = new Date();
		this.user = user;
		this.order = order;
		this.name = name;
		this.type = type;
		this.entityId = entityId;
	}
	
	public Activity(Order order, String name, ActivityType type) {
		this(order.getAbstractor(), order, name, type, order.getId());
	}
	
	public Activity(User user, Order order, String name, ActivityType type) {
		this(user, order, name, type, order.getId());
	}
	
	public static Activity makeOrderOverdueActivity(Order order) {
		return new Activity(order, "Order status changed to \"" + OrderInternalStatus.OVERDUE.getStatusName() + "\"", ActivityType.STATUS_CHANGE);
	}
	
	public static Activity makeOrderInProgressActivity(Order order) {
		return new Activity(order, "Order status changed to \"" + OrderInternalStatus.IN_PROGRESS.getStatusName() + "\"", ActivityType.STATUS_CHANGE);
	}
	
	public static Activity makeOrderStatusChangeActivity(Order order, User user) {
		return new Activity(user, order, "Order status changed to \"" + order.getInternalStatus().getStatusName() + "\"", ActivityType.STATUS_CHANGE, order.getId());
	}
	
	public static Activity makeNewMessageActivity(Order order, User user) {
		return new Activity(user, order, "New message in the order's inbox", ActivityType.MESSAGE);
	}

	public Order getOrder() {
		return order;
	}

	public String getName() {
		return name;
	}

	public User getUser() {
		return user;
	}

	public ActivityType getType() {
		return type;
	}

	public Long getEntityId() {
		return entityId;
	}

}
