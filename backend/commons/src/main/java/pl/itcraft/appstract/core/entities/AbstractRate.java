package pl.itcraft.appstract.core.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "abstract_rates")
public class AbstractRate extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "service_type_id")
	private ServiceType serviceType;
	
	@Column(name = "rate")
	private BigDecimal rate;
	
	protected AbstractRate() {
		// JPA
	}

	public AbstractRate(User user, ServiceType serviceType, BigDecimal rate) {
		this.createdAt = new Date();
		this.user = user;
		this.serviceType = serviceType;
		this.rate = rate;
	}

	public User getUser() {
		return user;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public BigDecimal getRate() {
		return rate;
	}
	
}
