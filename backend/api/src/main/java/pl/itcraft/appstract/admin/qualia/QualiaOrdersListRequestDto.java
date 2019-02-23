package pl.itcraft.appstract.admin.qualia;

import java.util.List;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;

public class QualiaOrdersListRequestDto {

	@GraphQLArgument(name = "input")
	private QualiaOrdersListModelDto orders;
	
	public List<QualiaOrderDto> getOrders() {
		return orders.getOrders();
	}
	
}
