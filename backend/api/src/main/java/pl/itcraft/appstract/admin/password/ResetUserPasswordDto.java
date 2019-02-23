package pl.itcraft.appstract.admin.password;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.validation.StrongPassword;

public class ResetUserPasswordDto {

	@ApiModelProperty(value = "Disposable token")
	private String token;
	
	@ApiModelProperty(value = "New password")
	@NotNull
	@StrongPassword
	private String newPassword;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
