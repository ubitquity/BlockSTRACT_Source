package pl.itcraft.appstract.core.api.error;

import pl.itcraft.appstract.core.validation.ValidationErrors;

public class BeanValidationException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	private ValidationErrors validationErrors;
	
	public BeanValidationException(ValidationErrors validationErrors) {
		super(RC.VALIDATION_ERROR);
		this.validationErrors = validationErrors;
	}
	
	public ValidationErrors getValidationErrors() {
		return validationErrors;
	}

}
