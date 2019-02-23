package pl.itcraft.appstract.admin.qualia;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.OrderCharge;

public class QualiaTitleSearchPlusInput {
	
	private String orderId;
	private TitleFormSearchPlusInput form;
	
	public QualiaTitleSearchPlusInput(Order order) {
		this.orderId = order.getQualiaId();
		this.form = new TitleFormSearchPlusInput(order);
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public TitleFormSearchPlusInput getForm() {
		return form;
	}

	public void setForm(TitleFormSearchPlusInput form) {
		this.form = form;
	}

	public class TitleFormSearchPlusInput {
		private Date searchCompletedDate;
		private String searchCompletedTime;
		private String legalDescription;
		private String estateType;
		private String titleVesting;
		private String exceptions;
		private String requirements;
		private List<String> parcelIds;
		private List<QualiaOrderCharge> additionalCosts;
		
		public TitleFormSearchPlusInput(Order order) {
			this.searchCompletedDate = order.getEndDate();
			this.searchCompletedTime = "12:34 PM";
			this.legalDescription = order.getLegalDescription();
			this.estateType = order.getEstateType();
			this.titleVesting = order.getTitleVesting();
			this.exceptions = order.getCommitmentExceptions();
			this.requirements = order.getCommitmentRequirements();
			this.parcelIds = order.getParcelIds().stream().map(p -> p.getParcelId()).collect(Collectors.toList());
			this.additionalCosts = order.getCharges().stream().map(QualiaOrderCharge::new).collect(Collectors.toList());
			if (order.getCopyUnits() != null && order.getCopyUnits() > 0 && order.getCopyCostPerUnit() != null) {
				this.additionalCosts.add(new QualiaOrderCharge(order.getCopyCostPerUnit(), order.getCopyUnits()));
			}
		}
		
		public class QualiaOrderCharge {
			private String name;
			private BigDecimal costPerUnit;
			private int units;
			
			public QualiaOrderCharge(OrderCharge oc) {
				this.name = oc.getOrderAbstractRate().getServiceType().getName();
				this.costPerUnit = oc.getOrderAbstractRate().getRate();
				this.units = oc.getUnits();
			}
			
			public QualiaOrderCharge(BigDecimal costPerUnit, int units) {
				this.name = "Copy Costs";
				this.costPerUnit = costPerUnit;
				this.units = units;
			}
			
			@Override
			public String toString() {
				return "{\n"
						+ " name: \"" + StringEscapeUtils.escapeJava(name) + "\"\n"
						+ " cost_per_unit: " + costPerUnit + "\n"
						+ " units: " + units + "\n"
						+ "}\n";
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(" order_id: \"" + orderId + "\" \n");
		builder.append(" form: {\n");
		builder.append("  search_completed_date: " + form.searchCompletedDate.getTime() + "\n");
		builder.append("  search_completed_time: \"" + StringEscapeUtils.escapeJava(form.searchCompletedTime) + "\"\n");
		builder.append("  additional_costs: [\n");
		builder.append(StringUtils.join(
			form.additionalCosts.stream().map(c -> c.toString()).collect(Collectors.toList()),
			",\n"
		));
		builder.append("  ]\n");
		builder.append("  exceptions: [{\n");
		builder.append("   text: \"" + StringEscapeUtils.escapeJava(form.exceptions) + "\"\n");
		builder.append("  }]\n");
		builder.append("  requirements: [{\n");
		builder.append("   text: \"" + StringEscapeUtils.escapeJava(form.requirements) + "\"\n");
		builder.append("  }]\n");
		builder.append("  properties: [{\n");
		builder.append("   legal_description: \"" + StringEscapeUtils.escapeJava(form.legalDescription) + "\"\n");
		builder.append("   estate_type: \"" + StringEscapeUtils.escapeJava(form.estateType) + "\"\n");
		builder.append("   title_vesting: \"" + StringEscapeUtils.escapeJava(form.titleVesting) + "\"\n");
		builder.append("   parcel_ids: [ " +
				StringUtils.join(
					form.parcelIds.stream()
						.map(StringEscapeUtils::escapeJava)
						.map(pid -> "\"" + pid + "\"")
						.collect(Collectors.toList())
					, ", "
				)
		+ " ]\n");
		builder.append("  }]\n");
		builder.append(" }\n");
		builder.append("}\n");
		return builder.toString();
	}

}
