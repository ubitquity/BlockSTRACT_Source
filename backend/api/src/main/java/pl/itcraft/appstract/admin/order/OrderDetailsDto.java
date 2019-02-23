package pl.itcraft.appstract.admin.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import pl.itcraft.appstract.core.entities.Borrower;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.Property;
import pl.itcraft.appstract.core.entities.Seller;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.OrderInternalStatus;
import pl.itcraft.appstract.core.enums.OrderStatus;

public class OrderDetailsDto {

	private long id;
	private String orderNumber;
	private OrderStatus status;
	private OrderInternalStatus internalStatus;
	private Long abstractorId;
	private String abstractor;
	private String qualiaId;
	private String purpose;
	private Long createdDate;
	private BigDecimal price;
	private BigDecimal quotedPrice;
	private Long dueDate;
	private Long projectedCloseDate;
	private String customerName;
	private String customerOrderNumber;
	private String customerContact;
	private String productDescription;
	private List<OrderPropertyDto> properties;
	private List<OrderPersonDto> sellers;
	private List<OrderPersonDto> borrowers;
	private int rate;
	private boolean ratesEmpty;
	
	public OrderDetailsDto(Order order, Integer rate, boolean ratesEmpty) {
		this.id = order.getId();
		this.orderNumber = order.getOrderNumber();
		this.status = order.getStatus();
		this.internalStatus = order.getInternalStatus();
		User abstractor = order.getAbstractor();
		if (abstractor != null) {
			this.abstractorId = abstractor.getId();
			this.abstractor = abstractor.getFirstName() + " " + abstractor.getLastName();
		}
		this.qualiaId = order.getQualiaId();
		this.purpose = order.getPurpose();
		this.createdDate = order.getCreatedDate() != null ? order.getCreatedDate().getTime() : null;
		this.price = order.getPrice();
		this.quotedPrice = order.getQuotedPrice();
		this.dueDate = order.getDueDate() != null ? order.getDueDate().getTime() : null;
		this.projectedCloseDate = order.getProjectedCloseDate() != null ? order.getProjectedCloseDate().getTime() : null;
		this.customerName = order.getCustomerName();
		this.customerOrderNumber = order.getCustomerOrderNumber();
		this.customerContact = order.getCustomerContact();
		this.productDescription = order.getProductDescription();
		this.properties = order.getProperties().stream().map(OrderPropertyDto::new).collect(Collectors.toList());
		this.sellers = order.getSellers().stream().map(OrderPersonDto::new).collect(Collectors.toList());
		this.borrowers = order.getBorrowers().stream().map(OrderPersonDto::new).collect(Collectors.toList());
		this.rate = rate;
		this.ratesEmpty = ratesEmpty;
	}

	public OrderInternalStatus getInternalStatus() {
		return internalStatus;
	}

	public void setInternalStatus(OrderInternalStatus internalStatus) {
		this.internalStatus = internalStatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getAbstractor() {
		return abstractor;
	}

	public void setAbstractor(String abstractor) {
		this.abstractor = abstractor;
	}

	public String getCustomerContact() {
		return customerContact;
	}

	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public List<OrderPersonDto> getSellers() {
		return sellers;
	}

	public void setSellers(List<OrderPersonDto> sellers) {
		this.sellers = sellers;
	}

	public List<OrderPersonDto> getBorrowers() {
		return borrowers;
	}

	public void setBorrowers(List<OrderPersonDto> borrowers) {
		this.borrowers = borrowers;
	}

	public String getQualiaId() {
		return qualiaId;
	}

	public void setQualiaId(String qualiaId) {
		this.qualiaId = qualiaId;
	}
	
	public List<OrderPropertyDto> getProperties() {
		return properties;
	}

	public void setProperties(List<OrderPropertyDto> properties) {
		this.properties = properties;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public Long getProjectedCloseDate() {
		return projectedCloseDate;
	}

	public void setProjectedCloseDate(Long projectedCloseDate) {
		this.projectedCloseDate = projectedCloseDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}

	public void setCustomerOrderNumber(String customerOrderNumber) {
		this.customerOrderNumber = customerOrderNumber;
	}

	public Long getAbstractorId() {
		return abstractorId;
	}

	public void setAbstractorId(Long abstractorId) {
		this.abstractorId = abstractorId;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public boolean isRatesEmpty() {
		return ratesEmpty;
	}

	public void setRatesEmpty(boolean ratesEmpty) {
		this.ratesEmpty = ratesEmpty;
	}

	public static class OrderPropertyDto {
		
		private String flatAddress;
		private String county;
		
		public OrderPropertyDto(Property p) {
			this.flatAddress = p.getFlatAddress();
			this.county = p.getCounty() != null ? p.getCounty().getName() : null;
		}
		
		public String getFlatAddress() {
			return flatAddress;
		}
		
		public void setFlatAddress(String flatAddress) {
			this.flatAddress = flatAddress;
		}
		
		public String getCounty() {
			return county;
		}
		
		public void setCounty(String county) {
			this.county = county;
		}
		
	}
	
	public static class OrderPersonDto {
		
		private String name;
		
		public OrderPersonDto(Seller seller) {
			this.name = seller.getName();
		}
		
		public OrderPersonDto(Borrower borrower) {
			this.name = borrower.getName();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
}

