package pl.itcraft.appstract.admin.qualia;

public class QualiaOrderIdDto {

	private String orderId;
	
	public QualiaOrderIdDto(String orderId) {
		this.orderId = orderId;
	}
	
	@Override
	public String toString() {
		return "{ order_id: \"" + orderId + "\" }";
	}
	
}
