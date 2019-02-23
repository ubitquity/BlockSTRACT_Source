package pl.itcraft.appstract.admin.qualia;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import pl.itcraft.appstract.core.enums.OrderStatus;

@ApiModel(description = "Single row of orders' list")
public class QualiaOrderDetailsDto {

	// field names are like this because they need to be the same as in Qualia's GraphQL api
	private String _id;
	private String order_number;
	private OrderStatus status;
	private String customer_name;
	private String product_name;
	private BigDecimal quoted_price;
	private BigDecimal price;
	private Long due_date;
	private Long projected_close_date;
	private boolean pay_on_close;
	private boolean charged_at_beginning;
	private String purpose;
	private String customer_order_number;
	private String product_description;
	private QualiaName customer_contact;
	private QualiaStatusDetails status_details;
	private List<QualiaPerson> sellers;
	private List<QualiaPerson> borrowers;
	private List<QualiaProperty> properties;

	public String getId() {
		return _id;
	}

	public String getOrderNumber() {
		return order_number;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public void setStatus(String status) {
		this.status = OrderStatus.fromString(status);
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public void setQuoted_price(BigDecimal quoted_price) {
		this.quoted_price = quoted_price;
	}

	public void setDue_date(Long due_date) {
		this.due_date = due_date;
	}
	
	public Long getProjectedCloseDate() {
		return projected_close_date;
	}

	public void setProjected_close_date(Long projected_close_date) {
		this.projected_close_date = projected_close_date;
	}

	public void setStatus_details(QualiaStatusDetails status_details) {
		this.status_details = status_details;
	}

	public void setProperties(List<QualiaProperty> properties) {
		this.properties = properties;
	}
	
	public OrderStatus getStatus() {
		return status;
	}
	
	public String getCustomerName() {
		return customer_name;
	}
	
	public String getProductName() {
		return product_name;
	}
	
	public BigDecimal getQuotedPrice() {
		return quoted_price;
	}
	
	public Long getDueDate() {
		return due_date;
	}
	
	public QualiaStatusDetails getStatusDetails() {
		return status_details;
	}
	
	public List<QualiaProperty> getProperties() {
		return properties;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isPayOnClose() {
		return pay_on_close;
	}

	public void setPay_on_close(boolean pay_on_close) {
		this.pay_on_close = pay_on_close;
	}

	public boolean isChargedAtBeginning() {
		return charged_at_beginning;
	}

	public void setCharged_at_beginning(boolean charged_at_beginning) {
		this.charged_at_beginning = charged_at_beginning;
	}

	public List<QualiaPerson> getSellers() {
		return sellers;
	}

	public void setSellers(List<QualiaPerson> sellers) {
		this.sellers = sellers;
	}

	public List<QualiaPerson> getBorrowers() {
		return borrowers;
	}

	public void setBorrowers(List<QualiaPerson> borrowers) {
		this.borrowers = borrowers;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getCustomerOrderNumber() {
		return customer_order_number;
	}

	public void setCustomer_order_number(String customer_order_number) {
		this.customer_order_number = customer_order_number;
	}

	public QualiaName getCustomerContact() {
		return customer_contact;
	}

	public void setCustomer_contact(QualiaName customer_contact) {
		this.customer_contact = customer_contact;
	}

	public String getProductDescription() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public static class QualiaStatusDetails {
		
		private boolean open;
		private boolean completed;
		private boolean cancelled;
		private Long created_date;
		
		public boolean isOpen() {
			return open;
		}
		public void setOpen(boolean open) {
			this.open = open;
		}
		public boolean isCancelled() {
			return cancelled;
		}
		public void setCancelled(boolean cancelled) {
			this.cancelled = cancelled;
		}
		public boolean isCompleted() {
			return completed;
		}
		public void setCompleted(boolean completed) {
			this.completed = completed;
		}
		public Long getCreatedDate() {
			return created_date;
		}
		public void setCreated_date(Long created_date) {
			this.created_date = created_date;
		}
		
	}
	
	public static class QualiaProperty {
		
		private String flat_address;
		private String county;
		private String[] parcel_ids;
		
		public void setFlat_address(String flat_address) {
			this.flat_address = flat_address;
		}
		
		public String getFlatAddress() {
			return flat_address;
		}

		public String getCounty() {
			return county;
		}

		public void setCounty(String county) {
			this.county = county;
		}

		public String[] getParcelIds() {
			return parcel_ids;
		}

		public void setParcel_ids(String[] parcel_ids) {
			this.parcel_ids = parcel_ids;
		}
		
	}
	
	public static class QualiaPerson {
		
		private String full_name;

		public void setFull_name(String full_name) {
			this.full_name = full_name;
		}
		
		public String getFullName() {
			return full_name;
		}
		
	}
	
	public static class QualiaName {
		
		private String name;
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
}
