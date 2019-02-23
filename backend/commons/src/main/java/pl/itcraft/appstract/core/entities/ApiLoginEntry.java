package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "api_login_entries")
public class ApiLoginEntry extends BaseEntity {
	
	private static final int ACCESS_TOKEN_LENGTH = 50;

	@Column(name = "logout_time", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date logoutTime;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "access_token")
	private String accessToken;
	
	@Column(name = "token_expiration_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tokenExpirationDate;
	
	@Column(name = "registration_id")
	private String registrationId;
	
	@Column(name = "remote_host")
	private String remoteHost;
	
	@Column(name = "user_agent")
	private String userAgent;
	
	protected ApiLoginEntry() {
		// jpa
	}
	
	public ApiLoginEntry(Date createdAt, User user, Date tokenExpirationDate) {
		this.createdAt = createdAt;
		this.accessToken = RandomStringUtils.random(ACCESS_TOKEN_LENGTH, true, true);
		this.user = user;
		this.tokenExpirationDate = tokenExpirationDate;
	}
	
	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public User getUser() {
		return user;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public Date getTokenExpirationDate() {
		return tokenExpirationDate;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = StringUtils.hasText(registrationId) ? registrationId : null;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}	

}
