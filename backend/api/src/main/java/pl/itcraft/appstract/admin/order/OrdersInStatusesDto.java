package pl.itcraft.appstract.admin.order;

public class OrdersInStatusesDto {

	private int inOrderAcceptance;
	private int inTitleProcessing;
	private int inClosingPrep;
	private int inPostClosing;
	
	public OrdersInStatusesDto(int inOrderAcceptance, int inTitleProcessing, int inClosingPrep, int inPostClosing) {
		this.inOrderAcceptance = inOrderAcceptance;
		this.inTitleProcessing = inTitleProcessing;
		this.inClosingPrep = inClosingPrep;
		this.inPostClosing = inPostClosing;
	}
	
	public OrdersInStatusesDto() {}

	public int getInOrderAcceptance() {
		return inOrderAcceptance;
	}

	public void setInOrderAcceptance(int inOrderAcceptance) {
		this.inOrderAcceptance = inOrderAcceptance;
	}

	public int getInTitleProcessing() {
		return inTitleProcessing;
	}

	public void setInTitleProcessing(int inTitleProcessing) {
		this.inTitleProcessing = inTitleProcessing;
	}

	public int getInClosingPrep() {
		return inClosingPrep;
	}

	public void setInClosingPrep(int inClosingPrep) {
		this.inClosingPrep = inClosingPrep;
	}

	public int getInPostClosing() {
		return inPostClosing;
	}

	public void setInPostClosing(int inPostClosing) {
		this.inPostClosing = inPostClosing;
	}
	
}
