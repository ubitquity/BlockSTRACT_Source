package pl.itcraft.appstract.admin.users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.entities.User;

@ApiModel(description = "Single row of abstractors' list")
public class AbstractorsListRowDto {

	@ApiModelProperty(required = true, readOnly = true)
	private Long id;

	@ApiModelProperty(required = true, readOnly = true)
	private String firstName;

	@ApiModelProperty(required = true, readOnly = true)
	private String lastName;

	@ApiModelProperty(required = true, readOnly = true)
	private String email;
	
	@ApiModelProperty(required = true, readOnly = true)
	private String companyName;
	
	@ApiModelProperty(required = true, readOnly = true)
	private String phoneNumber;
	
	@ApiModelProperty(required = true, readOnly = true)
	private Integer weeklyHoursAvailability;
	
	@ApiModelProperty(required = true, readOnly = true)
	private boolean enabled;

	public AbstractorsListRowDto(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.companyName = user.getCompanyName();
		this.phoneNumber = user.getPhoneNumber();
		this.weeklyHoursAvailability = user.getWeeklyHoursAvailability();
		this.enabled = user.isEnabled();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
