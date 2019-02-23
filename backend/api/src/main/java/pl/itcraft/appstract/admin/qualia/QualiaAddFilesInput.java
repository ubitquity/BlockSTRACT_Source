package pl.itcraft.appstract.admin.qualia;

import org.apache.commons.lang.StringEscapeUtils;

import pl.itcraft.appstract.core.entities.Order;

public class QualiaAddFilesInput {
	
	private String orderId;
	private String name;
	private String base64;
	
	public QualiaAddFilesInput(Order order, String base64) {
		this.orderId = order.getQualiaId();
		this.name = order.getTitleSearchDocumentFile().getSourceFileName();
		this.base64 = base64;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(" order_id: \"" + orderId + "\" \n");
		builder.append(" files: [{\n");
		builder.append("  name: \"" + StringEscapeUtils.escapeJava(name) + "\"\n");
		builder.append("  base_64: \"" + base64 + "\"\n");
		builder.append("  is_primary: true\n");
		builder.append(" }]\n");
		builder.append("}\n");
		return builder.toString();
	}

}
