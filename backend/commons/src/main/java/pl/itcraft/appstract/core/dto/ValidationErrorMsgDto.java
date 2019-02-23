package pl.itcraft.appstract.core.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({"field", "msgKey", "msgArgs"})
public class ValidationErrorMsgDto extends ErrorMsgDto {
	
	@ApiModelProperty(value = "Field name", required = true, position = 1)
	private String field;
	
	public ValidationErrorMsgDto(String field, String message, String[] parameters) {
		super(message, parameters);
		this.field = field;
	}
	
	public String getField() {
		return field;
	}

	@Override
	public String toString() {
		return "ValidationErrorMsgDto[ field = " + field + ", message = " + message + ", parameters = " + Arrays.toString(parameters) + " ]";
	}
	
}
