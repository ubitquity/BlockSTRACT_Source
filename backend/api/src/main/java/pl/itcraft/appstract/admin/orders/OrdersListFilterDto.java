package pl.itcraft.appstract.admin.orders;

import pl.itcraft.appstract.core.enums.OrderStatusDetailsFilter;

public class OrdersListFilterDto {

	private OrderStatusDetailsFilter status;
	private String search;

	public OrderStatusDetailsFilter getStatus() {
		return status;
	}

	public void setStatus(OrderStatusDetailsFilter status) {
		this.status = status;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
}
