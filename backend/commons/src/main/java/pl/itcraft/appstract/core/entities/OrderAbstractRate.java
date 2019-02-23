package pl.itcraft.appstract.core.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_abstract_rates")
public class OrderAbstractRate extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "service_type_id")
	private ServiceType serviceType;
	
	@Column(name = "rate")
	private BigDecimal rate;
	
	protected OrderAbstractRate() {
		// JPA
	}
	
	public OrderAbstractRate(Order order, AbstractRate ar) {
		this.createdAt = new Date();
		this.order = order;
		this.serviceType = ar.getServiceType();
		this.rate = ar.getRate();
	}

	public Order getOrder() {
		return order;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public BigDecimal getRate() {
		return rate;
	}
	
}
