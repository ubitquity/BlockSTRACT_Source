package pl.itcraft.appstract.core.enums;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public enum OrderInternalStatus {

	UNCLAIMED,
	UNDER_REVIEW,
	IN_PROGRESS,
	OVERDUE,
	PENDING_APPROVAL,
	ABSTRACT_INCOMPLETE,
	ACCEPTED,
	PAID,
	CANCELLED;
	
	public boolean isEditable() {
		return Arrays.asList(UNDER_REVIEW, IN_PROGRESS, OVERDUE, ABSTRACT_INCOMPLETE).contains(this);
	}
	
	public boolean isNotCancellable() {
		return Arrays.asList(PAID, CANCELLED).contains(this);
	}
	
	public String getStatusName() {
		return StringUtils.capitalize(name().toLowerCase().replace('_', ' '));
	}
	
}
