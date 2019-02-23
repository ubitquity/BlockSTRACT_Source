package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_archive_passwords")
public class UserArchivePassword extends BaseEntity {
		
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "password")
	private String password;
	
	protected UserArchivePassword() {
		// JPA
	}
	
	public UserArchivePassword(User user, Date createdAt, String password) {
		this.user = user;
		this.createdAt = createdAt;
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
