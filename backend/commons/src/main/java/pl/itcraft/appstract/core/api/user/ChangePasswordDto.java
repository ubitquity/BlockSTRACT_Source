package pl.itcraft.appstract.core.api.user;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import pl.itcraft.appstract.core.validation.CurrentPassword;
import pl.itcraft.appstract.core.validation.StrongPassword;

public class ChangePasswordDto {

	@NotEmpty
	@CurrentPassword
	private String currentPassword;
	
	@NotEmpty
	@StrongPassword
	private String newPassword;
	
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = StringUtils.trim(currentPassword);
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = StringUtils.trim(newPassword);
	}
	
}
