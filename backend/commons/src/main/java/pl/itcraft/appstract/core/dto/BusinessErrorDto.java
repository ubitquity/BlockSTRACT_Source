package pl.itcraft.appstract.core.dto;

import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.api.error.RC;

public class BusinessErrorDto<T extends Enum<?>> extends ErrorDto {
	
	@ApiModelProperty(value = "Business error type", required = true)
	private T errorType;
	
	public BusinessErrorDto(T errorType) {
		super(RC.BUSINESS_ERROR, errorType.name());
		this.errorType = errorType;
	}
	
	public T getErrorType() {
		return errorType;
	}
	
}
