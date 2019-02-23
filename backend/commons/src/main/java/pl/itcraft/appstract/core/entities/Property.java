package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "properties")
public class Property extends BaseEntity {

	@Column(name = "flat_address")
	private String flatAddress;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "county_id")
	private County county;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	protected Property() {
		// JPA
	}
	
	public Property(Order order, String flatAddress, County county) {
		this.createdAt = new Date();
		this.order = order;
		this.flatAddress = flatAddress;
		this.county = county;
	}

	public String getFlatAddress() {
		return flatAddress;
	}

	public Order getOrder() {
		return order;
	}

	public County getCounty() {
		return county;
	}
	
}
