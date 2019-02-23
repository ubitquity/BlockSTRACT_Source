package pl.itcraft.appstract.admin.users;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.entities.User;

@ApiModel(description = "Abstractor's details")
public class AbstractorListDetailsDto {

	@ApiModelProperty(required = true)
	private Long id;

	@ApiModelProperty(required = true)
	private String firstName;

	@ApiModelProperty(required = true)
	private String lastName;

	@ApiModelProperty(required = true)
	private String email;
	
	@ApiModelProperty(required = true)
	private String bankAccount;

	@ApiModelProperty(required = true)
	private String companyName;
	
	@ApiModelProperty(required = true)
	private String[] countries;
	
	@ApiModelProperty(required = true)
	private double averageRate;
	
	@ApiModelProperty(required = true)
	private int weeklyAvailability;
	
	@ApiModelProperty(required = true)
	private boolean enabled;
	
	@ApiModelProperty(required = true)
	private boolean notifications;
	
	@ApiModelProperty(required = true)
	private List<AbstractRateDto> abstractRates;
	
	@ApiModelProperty(required = true)
	private List<NamedIdDto> counties;

	public AbstractorListDetailsDto(User user, double averageRate) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.bankAccount = user.getBankAccount();
		this.companyName = user.getCompanyName();
		this.countries = new String[]{"USA"};
		this.weeklyAvailability = user.getWeeklyHoursAvailability();
		this.averageRate = averageRate;
		this.enabled = user.isEnabled();
		this.notifications = user.isNotifications();
		this.abstractRates = user.getAbstractRates().stream().map(AbstractRateDto::new).collect(Collectors.toList());
		this.counties = user.getCounties().stream().map(c -> new NamedIdDto(c.getId(), c.getName())).collect(Collectors.toList());
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

	public double getAverageRate() {
		return averageRate;
	}

	public void setAverageRate(double averageRate) {
		this.averageRate = averageRate;
	}

	public List<AbstractRateDto> getAbstractRates() {
		return abstractRates;
	}

	public void setAbstractRates(List<AbstractRateDto> rates) {
		this.abstractRates = rates;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isNotifications() {
		return notifications;
	}

	public void setNotifications(boolean notifications) {
		this.notifications = notifications;
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

	public String[] getCountries() {
		return countries;
	}

	public void setCountries(String[] countries) {
		this.countries = countries;
	}

	public int getWeeklyAvailability() {
		return weeklyAvailability;
	}

	public void setWeeklyAvailability(int weeklyAvailability) {
		this.weeklyAvailability = weeklyAvailability;
	}

	public List<NamedIdDto> getCounties() {
		return counties;
	}

	public void setCounties(List<NamedIdDto> counties) {
		this.counties = counties;
	}

}
