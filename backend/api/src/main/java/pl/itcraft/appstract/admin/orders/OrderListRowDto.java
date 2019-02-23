package pl.itcraft.appstract.admin.orders;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.enums.OrderStatus;

@ApiModel(description = "Single row of orders' list")
public class OrderListRowDto {

	private Long id;
	private String qualiaId;
	private String orderNumber;
	private OrderStatus status;
	private String customerName;
	private String productName;
	private BigDecimal quotedPrice;
	private Long dueDate;
	private String flatAddress;
	
	public OrderListRowDto(Order o) {
		this.id = o.getId();
		this.qualiaId = o.getQualiaId();
		this.orderNumber = o.getOrderNumber();
		this.status = o.getStatus();
		this.customerName = o.getCustomerName();
		this.productName = o.getProductName();
		this.quotedPrice = o.getPrice();
		if (o.getDueDate() != null) {
			this.dueDate = o.getDueDate().getTime();
		}
		if (o.getProperties() != null && o.getProperties().size() > 0) {
			this.flatAddress = o.getProperties().get(0).getFlatAddress();
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQualiaId() {
		return qualiaId;
	}
	public void setQualiaId(String qualiaId) {
		this.qualiaId = qualiaId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus orderStatus) {
		this.status = orderStatus;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getQuotedPrice() {
		return quotedPrice;
	}
	public void setQuotedPrice(BigDecimal quotedPrice) {
		this.quotedPrice = quotedPrice;
	}
	public Long getDueDate() {
		return dueDate;
	}
	public void setDueDate(Long dueDate) {
		this.dueDate = dueDate;
	}
	public String getFlatAddress() {
		return flatAddress;
	}
	public void setFlatAddress(String flatAddress) {
		this.flatAddress = flatAddress;
	}

}
