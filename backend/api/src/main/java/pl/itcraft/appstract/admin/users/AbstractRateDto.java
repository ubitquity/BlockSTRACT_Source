package pl.itcraft.appstract.admin.users;

import java.math.BigDecimal;

import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.entities.AbstractRate;
import pl.itcraft.appstract.core.entities.OrderAbstractRate;
import pl.itcraft.appstract.core.entities.ServiceType;

public class AbstractRateDto {
	
	private NamedIdDto serviceType;
	private BigDecimal rate;

	public AbstractRateDto(AbstractRate ar) {
		ServiceType st = ar.getServiceType();
		this.serviceType = new NamedIdDto(st.getId(), st.getName());
		this.rate = ar.getRate();
	}
	
	public AbstractRateDto(OrderAbstractRate oar) {
		ServiceType st = oar.getServiceType();
		this.serviceType = new NamedIdDto(oar.getId(), st.getName());
		this.rate = oar.getRate();
	}

	public NamedIdDto getServiceType() {
		return serviceType;
	}

	public void setServiceType(NamedIdDto serviceType) {
		this.serviceType = serviceType;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	
}
