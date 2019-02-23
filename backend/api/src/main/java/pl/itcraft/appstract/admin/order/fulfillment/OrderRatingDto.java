package pl.itcraft.appstract.admin.order.fulfillment;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class OrderRatingDto {

	@Min(1)
	@Max(5)
	private int rate;

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	
}
