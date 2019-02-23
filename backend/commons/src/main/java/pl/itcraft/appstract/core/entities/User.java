package pl.itcraft.appstract.core.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import pl.itcraft.appstract.core.CoreConstants;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.enums.UserRole;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
	
	public static final User SYSTEM;
	static {
		SYSTEM = new User();
		SYSTEM.id = 1L;
		SYSTEM.role = UserRole.SYSTEM;
	}

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "password")
	private String password;

	@Column(name = "activation_token")
	private String activationToken;

	@Column(name = "account_activated")
	private boolean accountActivated;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "deleted")
	private boolean deleted;
	
	@Column(name = "account_locked")
	private boolean accountLocked;

	@ManyToOne
	@JoinColumn(name = "avatar_id")
	private UploadedFile avatar;

	@Column(name = "incorrect_logins_count")
	private int incorrectLoginsCount;

	@Column(name = "password_expiration_date")
	private Date passwordExpirationDate;

	@Column(name = "needs_password_reset")
	private boolean needsPasswordReset;

	@Column(name = "last_login_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginAt;
	
	@Column(name = "company_name")
	private String companyName;

	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "weekly_hours_availability")
	private Integer weeklyHoursAvailability;
	
	@Column(name = "bank_account")
	private String bankAccount;
	
	@Column(name = "notifications")
	private boolean notifications;
	
	@OneToMany(mappedBy = "user")
	@OrderBy("id ASC")
	private List<AbstractRate> abstractRates;
	
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(
		name = "abstractor_declined_orders", 
		joinColumns = { @JoinColumn(name = "abstractor_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "order_id") }
	)
	private List<Order> declinedOrders;
	
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(
		name = "abstractor_counties", 
		joinColumns = { @JoinColumn(name = "abstractor_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "county_id") }
	)
	private List<County> counties;
	
	@Transient
	private String currentAccessToken;

	@Transient
	private String currentUserAgent;

	public User() {
		// jpa
	}
	
	public User(UserRole role, String password, String email, String firstName, String lastName) {
		this.createdAt = new Date();
		this.password = password;
		update(role, email, firstName, lastName);
	}
	
	public void update(UserRole role, String email, String firstName, String lastName) {
		this.role = role;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public void updateAdmin(String email, String bankAccount, boolean notifications) {
		if (this.role != UserRole.ADMIN) {
			throw new ApiException(RC.FORBIDDEN);
		}
		this.email = email;
		this.bankAccount = bankAccount;
		this.notifications = notifications;
	}
	
	public void updateAbstractor(String firstName, String lastName, String email, String bankAccount, String companyName, Integer weeklyAvailability, boolean notifications) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.bankAccount = bankAccount;
		this.companyName = companyName;
		this.weeklyHoursAvailability = weeklyAvailability;
		this.notifications = notifications;
	}
	
	public void incrementIncorrectLoginsCount(int max) {
		incorrectLoginsCount++;
		if (max > 0 && incorrectLoginsCount > max) {
			accountLocked = true;
		}
	}

	// Return FALSE when user is not activated and token does not match.
	public boolean activate(String token) {
		if (accountActivated) {
			return true;
		}
		if (StringUtils.isEmpty(token)) {
			return false;
		}
		if (token.equals(activationToken)) {
			accountActivated = true;
			activationToken = null;
			return true;
		}
		return false;
	}
	
	public void markAsDeleted() {
		deleted = true;
	}
	
	public void activate() {
		enabled = true;
	}
	
	public void deactivate() {
		enabled = false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isDeleted() {
		return deleted;
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

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public int getIncorrectLoginsCount() {
		return incorrectLoginsCount;
	}

	public void setIncorrectLoginsCount(Integer incorrectLoginsCount) {
		this.incorrectLoginsCount = incorrectLoginsCount;
	}

	public Date getPasswordExpirationDate() {
		return passwordExpirationDate;
	}

	public void setPasswordExpirationDate(Date passwordExpirationDate) {
		this.passwordExpirationDate = passwordExpirationDate;
	}

	public boolean isNeedsPasswordReset() {
		return needsPasswordReset;
	}

	public void setNeedsPasswordReset(boolean needsPasswordReset) {
		this.needsPasswordReset = needsPasswordReset;
	}

	public UploadedFile getAvatar() {
		return avatar;
	}

	public void setAvatar(UploadedFile avatar) {
		this.avatar = avatar;
	}

	public String getRealOrDummyAvatarPath() {
		if (avatar != null) {
			return avatar.getPath();
		} else {
			return CoreConstants.DUMMY_AVATAR;
		}
	}

	public String getCurrentAccessToken() {
		return currentAccessToken;
	}

	public void setCurrentAccessToken(String currentAccessToken) {
		this.currentAccessToken = currentAccessToken;
	}

	public String getCurrentUserAgent() {
		return currentUserAgent;
	}

	public void setCurrentUserAgent(String currentUserAgent) {
		this.currentUserAgent = currentUserAgent;
	}

	public Date getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(Date lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public String getActivationToken() {
		return activationToken;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
	}

	public boolean isAccountActivated() {
		return accountActivated;
	}

	public void setAccountActivated(boolean accountActivated) {
		this.accountActivated = accountActivated;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Integer getWeeklyHoursAvailability() {
		return weeklyHoursAvailability;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public boolean isNotifications() {
		return notifications;
	}

	public void setNotifications(boolean notifications) {
		this.notifications = notifications;
	}

	public List<AbstractRate> getAbstractRates() {
		return abstractRates;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setWeeklyHoursAvailability(Integer weeklyHoursAvailability) {
		this.weeklyHoursAvailability = weeklyHoursAvailability;
	}

	public List<Order> getDeclinedOrders() {
		return declinedOrders;
	}

	public List<County> getCounties() {
		return counties;
	}
}
