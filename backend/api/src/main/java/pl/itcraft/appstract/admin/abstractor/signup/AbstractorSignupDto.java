package pl.itcraft.appstract.admin.abstractor.signup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.validation.StrongPassword;
import pl.itcraft.appstract.core.validation.UniqueEmail;

public class AbstractorSignupDto {

	@ApiModelProperty(required = true)
	@NotNull
	@Email
	private String email;
	
	@ApiModelProperty(required = true)
	@NotNull
	@StrongPassword
	private String password;

	@ApiModelProperty(required = false)
	@NotNull
	@Length(min = 2, max = 100)
	private String firstName;
	
	@ApiModelProperty(required = false)
	@NotNull
	@Length(min = 2, max = 100)
	private String lastName;

	@ApiModelProperty(required = true)
	@NotNull
	@Length(min = 2, max = 255)
	private String companyName;

	@ApiModelProperty(required = true)
	@NotNull
	@Length(min = 2, max = 20)
	private String phoneNumber;

	@ApiModelProperty(required = true)
	@NotNull
	@Min(0)
	@Max(100)
	private Integer weeklyHoursAvailability;
	
	@UniqueEmail(fieldOverride = "email")
	public NamedIdDto getUniqueEmail() {
		return new NamedIdDto(0L, email);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getWeeklyHoursAvailability() {
		return weeklyHoursAvailability;
	}

	public void setWeeklyHoursAvailability(Integer weeklyHoursAvailability) {
		this.weeklyHoursAvailability = weeklyHoursAvailability;
	}
}
