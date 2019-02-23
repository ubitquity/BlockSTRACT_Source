package pl.itcraft.appstract.admin.qualia;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;

public class QualiaOrderRequestDto {

	@GraphQLArgument(name = "_id")
	private QualiaOrderModelDto order;
	
	public QualiaOrderDetailsDto getOrder() {
		return order.getOrder();
	}
	
}
