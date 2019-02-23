package pl.itcraft.appstract.core.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import pl.itcraft.appstract.core.api.error.RC;

/**
 * Simple error (only code and details)
 */
public class ErrorDto {
	
	public static final String ERROR_REQUEST_ATTR = ErrorDto.class.getName();
	
	@ApiModelProperty(value = "HTTP Code", name = "_code", required = true)
	@JsonProperty(value = "_code")
	protected int code;
	
	@ApiModelProperty(value = "Timestamp", name = "_timestamp", required = true)
	@JsonProperty(value = "_timestamp")
	protected String timestamp;
	
	@ApiModelProperty(value = "Optional error details", name = "_errorDetails")
	@JsonProperty(value = "_errorDetails")
	protected String errorDetails = null;
	
	public ErrorDto(int errorCode) {
		initTimestamp();
		this.code = errorCode;
		this.errorDetails = RC.codes.containsKey(errorCode)
			? RC.codes.get(errorCode).toString()
			: HttpStatus.valueOf(errorCode).name();
	}
	
	public ErrorDto(int errorCode, Throwable e) {
		this(errorCode);
		this.errorDetails += ": " + e.getClass().getSimpleName();
		if (StringUtils.hasLength(e.getMessage())) {
			this.errorDetails += ": " + e.getMessage();
		}
	}
	
	public ErrorDto(int errorCode, String errorDetails) {
		this(errorCode);
		this.errorDetails += ": " + errorDetails;
	}
	
	private void initTimestamp() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		timestamp = sdf.format(now);
	}
	
	public int getCode() {
		return code;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public String getErrorDetails() {
		return errorDetails;
	}

	@Override
	public String toString() {
		return "ErrorDto [code = " + code + ", timestamp = " + timestamp + ", errorDetails = " + errorDetails + "]";
	}

}
