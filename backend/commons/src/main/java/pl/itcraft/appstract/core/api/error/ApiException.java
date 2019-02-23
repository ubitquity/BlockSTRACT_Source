package pl.itcraft.appstract.core.api.error;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String errorDetails;
	
	public ApiException(int errorCode) {
		super("");
		this.errorCode = errorCode;
	}
	
	public ApiException(int errorCode, Object errorDetails) {
		super(errorDetails.toString());
		this.errorCode = errorCode;
	}
	
	public ApiException(int errorCode, Throwable errorDetails) {
		super(errorDetails.toString());
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorDetails() {
		return errorDetails;
	}
	
}
