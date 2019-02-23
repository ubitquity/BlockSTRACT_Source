package pl.itcraft.appstract.admin.order.fulfillment;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import pl.itcraft.appstract.core.entities.OrderCharge;

public class OrderFulfillmentFormDto {

	private String[] parcelIds;
	private Long startDate;
	private Long endDate;
	private String titleVesting;
	private String legalDescription;
	private String estateType;
	private String outstandingMortgage;
	private String commitmentRequirements;
	private String commitmentExceptions;
	private BigDecimal copyCostPerUnit;
	@Min(1)
	private Integer copyUnits;
	private OrderFulfillmentChargeDto[] charges;

	public String[] getParcelIds() {
		return parcelIds;
	}

	public void setParcelIds(String[] parcelIds) {
		this.parcelIds = parcelIds;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getTitleVesting() {
		return titleVesting;
	}

	public void setTitleVesting(String titleVesting) {
		this.titleVesting = titleVesting;
	}

	public String getLegalDescription() {
		return legalDescription;
	}

	public void setLegalDescription(String legalDescription) {
		this.legalDescription = legalDescription;
	}

	public String getEstateType() {
		return estateType;
	}

	public void setEstateType(String estateType) {
		this.estateType = estateType;
	}

	public String getOutstandingMortgage() {
		return outstandingMortgage;
	}

	public void setOutstandingMortgage(String outstandingMortgage) {
		this.outstandingMortgage = outstandingMortgage;
	}

	public String getCommitmentRequirements() {
		return commitmentRequirements;
	}

	public void setCommitmentRequirements(String commitmentRequirements) {
		this.commitmentRequirements = commitmentRequirements;
	}

	public String getCommitmentExceptions() {
		return commitmentExceptions;
	}

	public void setCommitmentExceptions(String commitmentExceptions) {
		this.commitmentExceptions = commitmentExceptions;
	}

	public BigDecimal getCopyCostPerUnit() {
		return copyCostPerUnit;
	}

	public void setCopyCostPerUnit(BigDecimal copyCostPerUnit) {
		this.copyCostPerUnit = copyCostPerUnit;
	}

	public Integer getCopyUnits() {
		return copyUnits;
	}

	public void setCopyUnits(Integer copyUnits) {
		this.copyUnits = copyUnits;
	}

	public OrderFulfillmentChargeDto[] getCharges() {
		return charges;
	}

	public void setCharges(OrderFulfillmentChargeDto[] charges) {
		this.charges = charges;
	}

	public static class OrderFulfillmentChargeDto {
		private long orderAbstractRateId;
		private int units;
		
		public OrderFulfillmentChargeDto() {}
		
		public OrderFulfillmentChargeDto(OrderCharge oc) {
			this.orderAbstractRateId = oc.getOrderAbstractRate().getId();
			this.units = oc.getUnits();
		}
		
		public long getOrderAbstractRateId() {
			return orderAbstractRateId;
		}
		public void setOrderAbstractRateId(long orderAbstractRateId) {
			this.orderAbstractRateId = orderAbstractRateId;
		}
		public int getUnits() {
			return units;
		}
		public void setUnits(int units) {
			this.units = units;
		}
	}
	
}
