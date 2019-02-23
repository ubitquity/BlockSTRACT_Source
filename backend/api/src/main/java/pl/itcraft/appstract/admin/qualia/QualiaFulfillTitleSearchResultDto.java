package pl.itcraft.appstract.admin.qualia;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;

public class QualiaFulfillTitleSearchResultDto {

	@GraphQLArgument(name = "input")
	private FulfillOrderResultDto fulfillTitleSearchPlus;

	public FulfillOrderResultDto getFulfillTitleSearchPlus() {
		return fulfillTitleSearchPlus;
	}

	public void setFulfillTitleSearchPlus(FulfillOrderResultDto fulfillTitleSearchPlus) {
		this.fulfillTitleSearchPlus = fulfillTitleSearchPlus;
	}
	
	public class FulfillOrderResultDto {
		private QualiaOrderStatus order;

		public QualiaOrderStatus getOrder() {
			return order;
		}
		public void setOrder(QualiaOrderStatus order) {
			this.order = order;
		}
	}
	
}
