package pl.itcraft.appstract.admin.order;

import pl.itcraft.appstract.core.entities.Activity;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.ActivityType;

public class OrderActivityListRowDto {

	private String name;
	private String user;
	private ActivityType type;
	private Long entityId;
	private Long createdAt;
	
	public OrderActivityListRowDto(Activity activity) {
		this.name = activity.getName();
		User user = activity.getUser();
		if (user != null) {
			this.user = user.getFirstName() + " " + user.getLastName();
		}
		this.type = activity.getType();
		this.entityId = activity.getEntityId();
		this.createdAt = activity.getCreatedAt().getTime();
	}
	
	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String abstractor) {
		this.user = abstractor;
	}
	public Long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}
	
}
