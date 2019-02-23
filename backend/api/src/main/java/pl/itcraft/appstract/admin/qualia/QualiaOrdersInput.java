package pl.itcraft.appstract.admin.qualia;

public class QualiaOrdersInput {

	private String status;
	
	public QualiaOrdersInput(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	public String toString() {
		return "{ status: " + status + " }";
	}
	
}
