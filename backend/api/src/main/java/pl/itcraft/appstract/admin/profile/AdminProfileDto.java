package pl.itcraft.appstract.admin.profile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.entities.User;

@ApiModel(description = "Admin profile data")
public class AdminProfileDto {
	
	@ApiModelProperty(required = true)
	private Long id;
	
	@ApiModelProperty(required = true)
	private String email;
	
	@ApiModelProperty(required = true)
	private String bankAccount;
	
	@ApiModelProperty(required = true)
	private boolean notifications;
	
	public AdminProfileDto(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.bankAccount = user.getBankAccount();
		this.notifications = user.isNotifications();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public boolean isNotifications() {
		return notifications;
	}

	public void setNotifications(boolean notifications) {
		this.notifications = notifications;
	}

}
