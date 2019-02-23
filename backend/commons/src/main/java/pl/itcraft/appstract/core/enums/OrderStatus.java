package pl.itcraft.appstract.core.enums;

import org.apache.commons.lang.StringUtils;

public enum OrderStatus {

	OPEN,
	PENDING,
	ACCEPTED,
	DECLINED,
	CANCELLED,
	SUBMITTED,
	REVISION_REQUIRED,
	COMPLETED,
	RESUBMITTED,
	RESUBMISSION_ACCEPTED,
	PREORDER;
	
	public static OrderStatus fromString(String s) {
		return OrderStatus.valueOf(StringUtils.upperCase(s));
	}
	
}
