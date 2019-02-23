package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sellers")
public class Seller extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@Column(name = "name")
	private String name;
	
	protected Seller() {
		// JPA
	}
	
	public Seller(Order order, String name) {
		this.createdAt = new Date();
		this.order = order;
		this.name = name;
	}

	public Order getOrder() {
		return order;
	}

	public String getName() {
		return name;
	}
	
}
