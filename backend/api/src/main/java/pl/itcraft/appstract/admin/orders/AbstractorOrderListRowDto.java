package pl.itcraft.appstract.admin.orders;

import pl.itcraft.appstract.core.entities.Order;

public class AbstractorOrderListRowDto extends OrderListRowDto {

	private boolean available;
	
	public AbstractorOrderListRowDto(Order o, boolean available) {
		super(o);
		this.available = available;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
	
}
