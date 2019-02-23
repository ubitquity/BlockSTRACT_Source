package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_fulfillment_parcel_ids")
public class OrderFulfillmentParcelId extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "parcel_id")
	private String parcelId;
	
	protected OrderFulfillmentParcelId() {
		// JPA
	}

	public OrderFulfillmentParcelId(Order order, String parcelId) {
		this.createdAt = new Date();
		this.order = order;
		this.parcelId = parcelId;
	}

	public Order getOrder() {
		return order;
	}

	public String getParcelId() {
		return parcelId;
	}
	
}
