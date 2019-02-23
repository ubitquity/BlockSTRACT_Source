package pl.itcraft.appstract.core.fcm;

import java.io.Serializable;
import java.util.List;

public final class FcmResult implements Serializable {
	private static final long serialVersionUID = 6074839281821457284L;
	private final String messageId;
	private final String canonicalRegistrationId;
	private final String errorCode;
	private final Integer success;
	private final Integer failure;
	private final List<String> failedRegistrationIds;

	public static final class Builder {

		// optional parameters
		private String messageId;
		private String canonicalRegistrationId;
		private String errorCode;
		private Integer success;
		private Integer failure;
		private List<String> failedRegistrationIds;

		public Builder canonicalRegistrationId(String value) {
			canonicalRegistrationId = value;
			return this;
		}

		public Builder messageId(String value) {
			messageId = value;
			return this;
		}

		public Builder errorCode(String value) {
			errorCode = value;
			return this;
		}

		public Builder success(Integer value) {
			success = value;
			return this;
		}

		public Builder failure(Integer value) {
			failure = value;
			return this;
		}

		public Builder failedRegistrationIds(List<String> value) {
			failedRegistrationIds = value;
			return this;
		}

		public FcmResult build() {
			return new FcmResult(this);
		}
	}

	private FcmResult(Builder builder) {
		canonicalRegistrationId = builder.canonicalRegistrationId;
		messageId = builder.messageId;
		errorCode = builder.errorCode;
		success = builder.success;
		failure = builder.failure;
		failedRegistrationIds = builder.failedRegistrationIds;
	}

	/**
	 * Gets the message id, if any.
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Gets the canonical registration id, if any.
	 */
	public String getCanonicalRegistrationId() {
		return canonicalRegistrationId;
	}

	/**
	 * Gets the error code, if any.
	 */
	public String getErrorCodeName() {
		return errorCode;
	}

	public Integer getSuccess() {
		return success;
	}

	public Integer getFailure() {
		return failure;
	}

	public List<String> getFailedRegistrationIds() {
		return failedRegistrationIds;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		if (messageId != null) { 
			builder.append(" messageId=").append(messageId);
		}
		if (canonicalRegistrationId != null) {
			builder.append(" canonicalRegistrationId=")
					.append(canonicalRegistrationId);
		}
		if (errorCode != null) { 
			builder.append(" errorCode=").append(errorCode);
		}
		if (success != null) {
			builder.append(" groupSuccess=").append(success);
		}
		if (failure != null) {
			builder.append(" groupFailure=").append(failure);
		}
		if (failedRegistrationIds != null) {
			builder.append(" failedRegistrationIds=").append(failedRegistrationIds);
		}
		return builder.append(" ]").toString();
	}

}
