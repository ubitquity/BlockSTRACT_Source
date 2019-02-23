package pl.itcraft.appstract.core.dto;

import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.api.error.RC;

public class GeneralErrorDto extends ErrorDto {
	
	@ApiModelProperty(value = "Error message", required = true)
	private ErrorMsgDto errorMessage;
	
	public GeneralErrorDto(ErrorMsgDto errorMessage) {
		super(RC.GENERAL_ERROR, "");
		this.errorMessage = errorMessage;
	}
	
	public ErrorMsgDto getErrorMessage() {
		return errorMessage;
	}
	
}
