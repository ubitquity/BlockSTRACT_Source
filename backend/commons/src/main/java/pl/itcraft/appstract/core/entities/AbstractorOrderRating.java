package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "abstractor_order_ratings")
public class AbstractorOrderRating extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "abstractor_id")
	private User abstractor;
	
	@Column(name = "rate")
	private int rate;

	protected AbstractorOrderRating() {
		// JPA
	}
	
	public AbstractorOrderRating(Order order, int rate) {
		this.createdAt = new Date();
		this.order = order;
		this.abstractor = order.getAbstractor();
		this.rate = rate;
	}
	
	public Order getOrder() {
		return order;
	}

	public User getAbstractor() {
		return abstractor;
	}

	public int getRate() {
		return rate;
	}

}
