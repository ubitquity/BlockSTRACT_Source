package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_charges")
public class OrderCharge extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "order_abstract_rate_id")
	private OrderAbstractRate orderAbstractRate;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@Column(name = "units")
	private int units;
	
	protected OrderCharge() {
		// JPA
	}
	
	public OrderCharge(Order order, OrderAbstractRate orderAbstractRate, int units) {
		this.createdAt = new Date();
		this.order = order;
		this.orderAbstractRate = orderAbstractRate;
		this.units = units;
	}

	public OrderAbstractRate getOrderAbstractRate() {
		return orderAbstractRate;
	}

	public Order getOrder() {
		return order;
	}

	public int getUnits() {
		return units;
	}
	
}
