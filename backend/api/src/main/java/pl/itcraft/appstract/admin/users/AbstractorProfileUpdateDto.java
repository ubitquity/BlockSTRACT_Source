package pl.itcraft.appstract.admin.users;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.validation.UniqueEmail;

public class AbstractorProfileUpdateDto {

	@ApiModelProperty(required = true)
	@NotNull
	@Length(min = 2, max = 100)
	private String firstName;
	
	@ApiModelProperty(required = true)
	@NotNull
	@Length(min = 2, max = 100)
	private String lastName;
	
	@ApiModelProperty(required = true)
	@NotNull
	@Email
	private String email;
	
	@ApiModelProperty(required = false)
	@Length(min = 2, max = 100)
	private String bankAccount;
	
	@ApiModelProperty(required = false)
	@Length(min = 2, max = 100)
	private String companyName;
	
	@ApiModelProperty(required = true)
	@NotNull
	@Min(0)
	@Max(100)
	private Integer weeklyAvailability;
	
	@ApiModelProperty(required = true)
	private boolean notifications;
	
	@ApiModelProperty(required = true)
	@Valid
	private List<AbstractorProfileAbstractRateDto> abstractRates;
	
	@ApiModelProperty(required = true)
	private Long[] countyIds;
	
	@ApiModelProperty(hidden = true)
	private Long userId;
	
	@UniqueEmail(fieldOverride = "email")
	public NamedIdDto getUniqueEmail() {
		return new NamedIdDto(userId, email);
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
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getWeeklyAvailability() {
		return weeklyAvailability;
	}
	public void setWeeklyAvailability(Integer weeklyAvailability) {
		this.weeklyAvailability = weeklyAvailability;
	}
	public List<AbstractorProfileAbstractRateDto> getAbstractRates() {
		return abstractRates;
	}
	public Long[] getCountyIds() {
		return countyIds;
	}

	public boolean isNotifications() {
		return notifications;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public static class AbstractorProfileAbstractRateDto {
		
		private long serviceTypeId;
		
		@NotNull
		@Min(0)
		@Max(value = 99999999)
		private BigDecimal rate;
		
		public long getServiceTypeId() {
			return serviceTypeId;
		}
		public void setServiceTypeId(long serviceTypeId) {
			this.serviceTypeId = serviceTypeId;
		}
		public void setRate(BigDecimal rate) {
			this.rate = rate;
		}
		public BigDecimal getRate() {
			return rate;
		}
	}
	
}
