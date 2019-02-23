package pl.itcraft.appstract.core.entities;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.enums.OrderInternalStatus;
import pl.itcraft.appstract.core.enums.OrderStatus;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

	@Column(name = "qualia_id")
	private String qualiaId;
	
	@Column(name = "order_number")
	private String orderNumber;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "quoted_price")
	private BigDecimal quotedPrice;
	
	@Column(name = "due_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;
	
	@Column(name = "projected_close_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date projectedCloseDate;
	
	@Column(name = "pay_on_close")
	private boolean payOnClose;
	
	@Column(name = "charged_at_beginning")
	private boolean chargedAtBeginning;
	
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "open")
	private boolean open;
	
	@Column(name = "completed")
	private boolean completed;
	
	@Column(name = "cancelled")
	private boolean cancelled;
	
	@ManyToOne
	@JoinColumn(name = "abstractor_id")
	private User abstractor;
	
	@Column(name = "purpose")
	private String purpose;

	@Column(name = "customer_order_number")
	private String customerOrderNumber;

	@Column(name = "product_description")
	private String productDescription;

	@Column(name = "customer_contact")
	private String customerContact;
	
	@Column(name = "internal_status")
	@Enumerated(EnumType.STRING)
	private OrderInternalStatus internalStatus;
	
	@Column(name = "accepted_by_abstractor_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date acceptedByAbstractorTime;
	
	@OneToMany(mappedBy = "order")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<Seller> sellers;
	
	@OneToMany(mappedBy = "order")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<Borrower> borrowers;
	
	@OneToMany(mappedBy = "order")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<Property> properties;
	
	@ManyToMany(mappedBy = "declinedOrders")
	private List<User> declinedAbstractors;
	
	// Order fulfillment
	
	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	@Column(name = "title_vesting")
	private String titleVesting;
	
	@Column(name = "legal_description")
	private String legalDescription;
	
	@Column(name = "estate_type")
	private String estateType;
	
	@Column(name = "outstanding_mortgage")
	private String outstandingMortgage;

	@Column(name = "commitment_requirements")
	private String commitmentRequirements;

	@Column(name = "commitment_exceptions")
	private String commitmentExceptions;
	
	@Column(name = "copy_cost_per_unit")
	private BigDecimal copyCostPerUnit;
	
	@Column(name = "copy_units")
	private Integer copyUnits;
	
	@OneToMany(mappedBy = "order")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<OrderDeedFile> deedFiles;
	
	@ManyToOne
	@JoinColumn(name = "title_search_document_file_id")
	private UploadedFile titleSearchDocumentFile;
	
	@OneToMany(mappedBy = "order")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<OrderAbstractRate> abstractRates;
	
	@OneToMany(mappedBy = "order")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<OrderFulfillmentParcelId> parcelIds;
	
	@OneToMany(mappedBy = "order")
	@OrderBy("id ASC")
	@Cascade(value = CascadeType.ALL)
	private List<OrderCharge> charges;
	
	// helper column for reports
	@Column(name = "final_cost")
	private BigDecimal finalCost;
	
	protected Order() {
		// JPA
	}
	
	public Order(
			String qualiaId, String orderNumber, OrderStatus status, String customerName, String productName, 
			BigDecimal quotedPrice, BigDecimal price, Long dueDate, Long projectedCloseDate, 
			boolean payOnClose, boolean chargedAtBeginning, Long createdDate, 
			boolean open, boolean completed, boolean cancelled, 
			String purpose, String customerOrderNumber, String productDescription, String customerContact,
			List<Seller> sellers, List<Borrower> borrowers,
			List<Property> properties) {
		this.createdAt = new Date();
		this.internalStatus = OrderInternalStatus.UNCLAIMED;
		this.qualiaId = qualiaId;
		this.orderNumber = orderNumber;
		this.status = status;
		this.customerName = customerName;
		this.productName = productName;
		this.price = price;
		this.quotedPrice = quotedPrice;
		this.dueDate = dueDate != null ? new Date(dueDate) : null;
		this.projectedCloseDate = projectedCloseDate != null ? new Date(projectedCloseDate) : null;
		this.createdDate = createdDate != null ? new Date(createdDate) : null;
		this.payOnClose = payOnClose;
		this.chargedAtBeginning = chargedAtBeginning;
		this.open = open;
		this.completed = completed;
		this.cancelled = cancelled;
		this.purpose = purpose;
		this.customerOrderNumber = customerOrderNumber;
		this.productDescription = productDescription;
		this.customerContact = customerContact;
		this.sellers = sellers;
		this.borrowers = borrowers;
		this.properties = properties;
	}
	
	public void fillFulfillmentData(
		String[] parcelIds,
		Long startDate,
		Long endDate,
		String titleVesting,
		String legalDescription,
		String estateType,
		String outstandingMortgage,
		String commitmentRequirements,
		String commitmentExceptions,
		BigDecimal copyCostPerUnit,
		Integer copyUnits,
		List<OrderCharge> charges
	) {
		this.parcelIds = parcelIds != null ? Arrays.stream(parcelIds).map(p -> new OrderFulfillmentParcelId(this, p)).collect(Collectors.toList()) : null;
		this.startDate = startDate != null ? new Date(startDate) : null;
		this.endDate = endDate != null ? new Date(endDate) : null;
		this.titleVesting = titleVesting;
		this.legalDescription = legalDescription;
		this.estateType = estateType;
		this.outstandingMortgage = outstandingMortgage;
		this.commitmentRequirements = commitmentRequirements;
		this.commitmentExceptions = commitmentExceptions;
		this.copyCostPerUnit = copyCostPerUnit;
		this.copyUnits = copyUnits;
		this.charges = charges;
	}
	
	public void changeTitleSearchDocumentFile(UploadedFile titleSearchDocumentFile) {
		if (this.titleSearchDocumentFile != null) {
			this.titleSearchDocumentFile.markAsToDelete();
		}
		this.titleSearchDocumentFile = titleSearchDocumentFile;
	}

	public String getPurpose() {
		return purpose;
	}

	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public String getCustomerContact() {
		return customerContact;
	}

	public List<Seller> getSellers() {
		return sellers;
	}

	public List<Borrower> getBorrowers() {
		return borrowers;
	}

	public String getQualiaId() {
		return qualiaId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getProductName() {
		return productName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public boolean isOpen() {
		return open;
	}

	public boolean isCompleted() {
		return completed;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public User getAbstractor() {
		return abstractor;
	}

	public void setAbstractor(User abstractor) {
		this.abstractor = abstractor;
	}

	public BigDecimal getQuotedPrice() {
		return quotedPrice;
	}

	public Date getProjectedCloseDate() {
		return projectedCloseDate;
	}

	public boolean isPayOnClose() {
		return payOnClose;
	}

	public boolean isChargedAtBeginning() {
		return chargedAtBeginning;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public OrderInternalStatus getInternalStatus() {
		return internalStatus;
	}

	public List<User> getDeclinedAbstractors() {
		return declinedAbstractors;
	}
	
	public void acceptOrder(User abstractor) {
		if (this.abstractor != null) {
			throw new ApiException(RC.CONFLICT, "Order already taken");
		}
		if (declinedAbstractors.contains(abstractor)) {
			throw new ApiException(RC.CONFLICT, "Order already declined");
		}
		this.abstractor = abstractor;
		this.internalStatus = OrderInternalStatus.UNDER_REVIEW;
		this.acceptedByAbstractorTime = new Date();
		this.abstractRates = abstractor.getAbstractRates().stream().map(ar -> new OrderAbstractRate(this, ar)).collect(Collectors.toList());
	}
	
	public void submitForApproval() {
		this.internalStatus = OrderInternalStatus.PENDING_APPROVAL;
	}
	
	public void setAsIncomplete() {
		this.internalStatus = OrderInternalStatus.ABSTRACT_INCOMPLETE;
	}
	
	public void setAsAccepted() {
		this.internalStatus = OrderInternalStatus.ACCEPTED;
		this.open = false;
		this.completed = true;
	}
	
	public void setAsPaid() {
		this.internalStatus = OrderInternalStatus.PAID;
		this.finalCost = this.copyCostPerUnit.multiply(BigDecimal.valueOf(this.copyUnits)).add(
			this.charges.stream()
				.map(c -> c.getOrderAbstractRate().getRate().multiply(BigDecimal.valueOf(c.getUnits())))
				.reduce(BigDecimal.ZERO, BigDecimal::add)
		);
	}
	
	public void setAsCancelled() {
		this.internalStatus = OrderInternalStatus.CANCELLED;
		this.status = OrderStatus.DECLINED;
		this.open = false;
		this.completed = false;
		this.cancelled = true;
	}
	
	public void recall() {
		this.abstractor = null;
		this.internalStatus = OrderInternalStatus.UNCLAIMED;
		this.acceptedByAbstractorTime = null;
		this.startDate = null;
		this.endDate = null;
		this.titleVesting = null;
		this.legalDescription = null;
		this.estateType = null;
		this.outstandingMortgage = null;
		this.commitmentRequirements = null;
		this.commitmentExceptions = null;
		this.copyCostPerUnit = null;
		this.copyUnits = null;
		
		if (this.titleSearchDocumentFile != null) {
			this.titleSearchDocumentFile.markAsToDelete();
			this.titleSearchDocumentFile = null;
		}
		for (OrderDeedFile deedFile : this.deedFiles) {
			deedFile.getUploadedFile().markAsToDelete();
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getTitleVesting() {
		return titleVesting;
	}

	public String getLegalDescription() {
		return legalDescription;
	}

	public String getEstateType() {
		return estateType;
	}

	public String getOutstandingMortgage() {
		return outstandingMortgage;
	}

	public String getCommitmentRequirements() {
		return commitmentRequirements;
	}

	public String getCommitmentExceptions() {
		return commitmentExceptions;
	}

	public BigDecimal getCopyCostPerUnit() {
		return copyCostPerUnit;
	}

	public Integer getCopyUnits() {
		return copyUnits;
	}

	public UploadedFile getTitleSearchDocumentFile() {
		return titleSearchDocumentFile;
	}

	public List<OrderAbstractRate> getAbstractRates() {
		return abstractRates;
	}

	public List<OrderFulfillmentParcelId> getParcelIds() {
		return parcelIds;
	}

	public List<OrderCharge> getCharges() {
		return charges;
	}

	public BigDecimal getFinalCost() {
		return finalCost;
	}

	public List<OrderDeedFile> getDeedFiles() {
		return deedFiles;
	}

	public Date getAcceptedByAbstractorTime() {
		return acceptedByAbstractorTime;
	}

}
