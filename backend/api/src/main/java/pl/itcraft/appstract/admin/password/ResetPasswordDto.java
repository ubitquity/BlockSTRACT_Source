package pl.itcraft.appstract.admin.password;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ResetPasswordDto {
	
	@ApiModelProperty(value = "E-mail address to send reset link", required = true)
	@NotEmpty
	@Email
	private String email;
	
	@ApiModelProperty(value = "Relative path of reset link. IE.: /some/path", required = true)
	private String relativePath = "/password/reset";

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	
}
