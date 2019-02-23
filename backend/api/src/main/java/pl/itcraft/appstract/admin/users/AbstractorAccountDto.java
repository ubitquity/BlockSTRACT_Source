package pl.itcraft.appstract.admin.users;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.validation.StrongPassword;
import pl.itcraft.appstract.core.validation.UniqueEmail;

@ApiModel(description = "User's account data managed by admin")
public class AbstractorAccountDto {
	
	public interface OnCreate extends Default {}
	public interface OnUpdate extends Default {}
	
	@ApiModelProperty
	@Null(groups = OnCreate.class)
	@NotNull(groups = OnUpdate.class)
	private Long id;

	@ApiModelProperty(required = true)
	@NotNull
	@Length(min = 2, max = 100)
	private String firstName;

	@ApiModelProperty(required = true)
	@NotNull
	@Length(min = 2, max = 100)
	private String lastName;

	@ApiModelProperty(required = false, notes = "Required for creating. Optional for updating")
	@NotNull(groups = OnCreate.class)
	@StrongPassword(groups = OnCreate.class)
	private String password;

	@ApiModelProperty(required = true)
	@NotNull
	private UserRole role;

	@ApiModelProperty(required = true)
	@NotNull
	@Email
	private String email;
	
	@UniqueEmail(fieldOverride = "email")
	public NamedIdDto getUniqueEmail() {
		return new NamedIdDto(id==null ? 0L : id, email);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
