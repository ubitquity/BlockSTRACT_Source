package pl.itcraft.appstract.admin.qualia;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Single row of orders' list")
public class QualiaOrderDto {

	// field names are like this because they need to be the same as in Qualia's GraphQL api
	private String _id;

	public String getId() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

}
