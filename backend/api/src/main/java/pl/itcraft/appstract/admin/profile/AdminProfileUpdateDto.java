package pl.itcraft.appstract.admin.profile;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.validation.UniqueEmail;

@ApiModel(description = "Admin profile update data")
public class AdminProfileUpdateDto {
	
	@ApiModelProperty(required = true)
	@NotEmpty
	@Email
	private String email;
	
	@ApiModelProperty(required = true)
	@Length(max = 255)
	private String bankAccount;
	
	@ApiModelProperty(required = true)
	private boolean notifications;
	
	@ApiModelProperty(hidden = true)
	private Long adminId;
	
	@UniqueEmail(fieldOverride = "email")
	public NamedIdDto getUniqueEmail() {
		return new NamedIdDto(adminId != null ? adminId : 0, email);
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

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

}
