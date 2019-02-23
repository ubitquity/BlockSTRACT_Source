package pl.itcraft.appstract.core.api.error;

public class BusinessException extends ApiException {
	
	private static final long serialVersionUID = 1L;
	
	private Enum<?> errorType;
	
	public BusinessException(Enum<?> errorType) {
		super(RC.BUSINESS_ERROR, errorType.name());
		this.errorType = errorType;
	}
	
	public Enum<?> getErrorType() {
		return errorType;
	}
	
}
