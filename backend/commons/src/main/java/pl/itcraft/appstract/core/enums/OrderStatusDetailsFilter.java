package pl.itcraft.appstract.core.enums;

public enum OrderStatusDetailsFilter {

	OPEN,
	CLOSED,
	CANCELLED,
	DECLINED,
	ALL;
	
	public String getFieldName() {
		if (this == CLOSED) {
			return "completed";
		}
		return this.name().toLowerCase();
	}
	
}
