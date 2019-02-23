package pl.itcraft.appstract.admin.qualia;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;

public class QualiaDeclineOrderResultDto {
	
	@GraphQLArgument(name = "input")
	private QualiaOrderStatus declineOrder;
	
	public QualiaOrderStatus getDeclineOrder() {
		return declineOrder;
	}

	public void setDeclineOrder(QualiaOrderStatus declineOrder) {
		this.declineOrder = declineOrder;
	}

	public class QualiaOrderStatus {
		private String status;

		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
	}
	
}
