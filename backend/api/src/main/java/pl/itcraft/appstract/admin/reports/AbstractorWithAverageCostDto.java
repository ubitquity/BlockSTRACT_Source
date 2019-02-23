package pl.itcraft.appstract.admin.reports;

import java.math.BigDecimal;

public class AbstractorWithAverageCostDto {
	
	private Long id;
	private String name;
	private BigDecimal averageCost;
	
	public AbstractorWithAverageCostDto() {}
	
	public AbstractorWithAverageCostDto(Long id, String name, BigDecimal averageCost) {
		this.id = id;
		this.name = name;
		this.averageCost = averageCost;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getAverageCost() {
		return averageCost;
	}
	public void setAverageCost(BigDecimal averageCost) {
		this.averageCost = averageCost;
	}
	
}
