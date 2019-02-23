package pl.itcraft.appstract.core.dto;

import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.validation.ValidationErrors;

public class ValidationErrorDto extends ErrorDto {
	
	@ApiModelProperty(value = "Global errors", required = false)
	private List<ErrorMsgDto> globalErrors;
	
	@ApiModelProperty(value = "Errors for specific fields", required = true)
	private List<ValidationErrorMsgDto> fieldErrors;

	public ValidationErrorDto(ValidationErrors ve) {
		super(RC.VALIDATION_ERROR, "");
		globalErrors = ve.getGlobalErrors();
		fieldErrors = ve.getFieldErrors();
	}

	public List<ErrorMsgDto> getGlobalErrors() {
		return globalErrors;
	}
	
	public List<ValidationErrorMsgDto> getFieldErrors() {
		return fieldErrors;
	}

	@Override
	public String toString() {
		return "ValidationErrorDto"
			+ "[ globalErrors = " + Arrays.toString(globalErrors.toArray())
			+ ", fieldErrors = " + Arrays.toString(fieldErrors.toArray()) + " ]";
	}

}
