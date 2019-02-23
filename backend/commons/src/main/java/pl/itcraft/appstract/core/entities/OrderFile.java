package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_files")
public class OrderFile extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "uploaded_file_id")
	private UploadedFile uploadedFile;
	
	protected OrderFile() {
		// JPA
	}
	
	public OrderFile(Order order, UploadedFile uploadedFile) {
		this.createdAt = new Date();
		this.order = order;
		this.uploadedFile = uploadedFile;
	}

	public Order getOrder() {
		return order;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	
}
