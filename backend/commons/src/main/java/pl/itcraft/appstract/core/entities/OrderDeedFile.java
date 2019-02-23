package pl.itcraft.appstract.core.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_deed_files")
public class OrderDeedFile extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "uploaded_file_id")
	private UploadedFile uploadedFile;
	
	protected OrderDeedFile() {
		// JPA
	}
	
	public OrderDeedFile(Order order, UploadedFile uploadedFile) {
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
