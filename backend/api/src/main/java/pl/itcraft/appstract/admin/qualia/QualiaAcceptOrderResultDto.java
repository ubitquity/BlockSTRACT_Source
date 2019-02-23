package pl.itcraft.appstract.admin.qualia;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;

public class QualiaAcceptOrderResultDto {
	
	@GraphQLArgument(name = "input")
	private QualiaOrderStatus acceptOrder;
	
	public QualiaOrderStatus getAcceptOrder() {
		return acceptOrder;
	}

	public void setAcceptOrder(QualiaOrderStatus acceptOrder) {
		this.acceptOrder = acceptOrder;
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
