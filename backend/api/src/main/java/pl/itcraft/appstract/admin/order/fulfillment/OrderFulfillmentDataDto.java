package pl.itcraft.appstract.admin.order.fulfillment;

import java.util.List;
import java.util.stream.Collectors;

import pl.itcraft.appstract.admin.users.AbstractRateDto;
import pl.itcraft.appstract.admin.utils.AdminAppModuleConfig;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.OrderCharge;
import pl.itcraft.appstract.core.entities.OrderFulfillmentParcelId;

public class OrderFulfillmentDataDto extends OrderFulfillmentFormDto {

	private List<DeedFileDto> deedFiles;
	private TitleSearchDocumentDto titleSearchDocumentFile;
	private List<AbstractRateDto> abstractRates;
	
	public OrderFulfillmentDataDto(Order order, AdminAppModuleConfig config) {
		List<OrderFulfillmentParcelId> orderParcelIds = order.getParcelIds();
		setParcelIds(orderParcelIds.stream()
			.map(p -> p.getParcelId()).collect(Collectors.toList())
			.toArray(new String[orderParcelIds.size()])
		);
		if (order.getStartDate() != null) {
			setStartDate(order.getStartDate().getTime());
		}
		if (order.getEndDate() != null) {
			setEndDate(order.getEndDate().getTime());
		}
		setTitleVesting(order.getTitleVesting());
		setLegalDescription(order.getLegalDescription());
		setEstateType(order.getEstateType());
		setOutstandingMortgage(order.getOutstandingMortgage());
		setCommitmentRequirements(order.getCommitmentRequirements());
		setCommitmentExceptions(order.getCommitmentExceptions());
		setCopyCostPerUnit(order.getCopyCostPerUnit());
		setCopyUnits(order.getCopyUnits());
		List<OrderCharge> orderCharges = order.getCharges();
		setCharges(orderCharges.stream()
			.map(OrderFulfillmentChargeDto::new).collect(Collectors.toList())
			.toArray(new OrderFulfillmentChargeDto[orderCharges.size()])
		);
		
		if (order.getDeedFiles() != null) {
			this.deedFiles = order.getDeedFiles().stream().map(df -> new DeedFileDto(df, config)).collect(Collectors.toList());
		}
		if (order.getTitleSearchDocumentFile() != null) {
			this.titleSearchDocumentFile = new TitleSearchDocumentDto(order.getTitleSearchDocumentFile(), config);
		}
		this.abstractRates = order.getAbstractRates().stream().map(AbstractRateDto::new).collect(Collectors.toList());
	}
	
	public List<DeedFileDto> getDeedFiles() {
		return deedFiles;
	}
	public void setDeedFiles(List<DeedFileDto> deedFiles) {
		this.deedFiles = deedFiles;
	}
	public TitleSearchDocumentDto getTitleSearchDocumentFile() {
		return titleSearchDocumentFile;
	}
	public void setTitleSearchDocumentFile(TitleSearchDocumentDto titleSearchDocumentFile) {
		this.titleSearchDocumentFile = titleSearchDocumentFile;
	}
	public List<AbstractRateDto> getAbstractRates() {
		return abstractRates;
	}
	public void setAbstractRates(List<AbstractRateDto> abstractRates) {
		this.abstractRates = abstractRates;
	}
	
}
