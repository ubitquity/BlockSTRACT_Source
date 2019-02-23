package pl.itcraft.appstract.core.dto;

import java.util.Arrays;

import io.swagger.annotations.ApiModelProperty;

public class ErrorMsgDto {
	
	@ApiModelProperty(value = "Message key", required = true)
	protected String message;
	
	@ApiModelProperty(value = "Message parameters", required = false)
	protected String[] parameters;
	
	public ErrorMsgDto(String msgKey, String[] parameters) {
		this.message = msgKey;
		this.parameters = parameters;
	}
	
	public String getMessage() {
		return message;
	}

	public String[] getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return "ErrorMsgDto[ message = " + message + ", parameters = " + Arrays.toString(parameters) + " ]";
	}
	
}
