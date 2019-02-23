package pl.itcraft.appstract.core.api.error;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pl.itcraft.appstract.core.dto.BusinessErrorDto;
import pl.itcraft.appstract.core.dto.ErrorDto;
import pl.itcraft.appstract.core.dto.ValidationErrorDto;

/**
 * Przechwytuje wyjatki rzucone w kontrolerach @RestController
 */
@RestControllerAdvice(
	annotations = RestController.class
)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestControllerErrorHandler extends BaseControllerErrorHandler {
	
	@PostConstruct
	protected void postConstruct() {
		init( LoggerFactory.getLogger(RestControllerErrorHandler.class) );
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDto handleHttpMessageNotReadableException(HttpServletRequest request, Exception e, Authentication auth) throws IOException {
		logWarn(e, request, auth);
		ErrorDto dto = new ErrorDto(RC.BAD_REQUEST, e.getCause());
		request.setAttribute(ErrorDto.ERROR_REQUEST_ATTR, dto);
		return dto;
	}

	@ExceptionHandler(DataAccessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDto handleDataAccessException(HttpServletRequest request, HttpServletResponse response, Exception e, Authentication auth) throws IOException {
		logError(e, request, auth);
		ErrorDto dto = new ErrorDto(RC.INTERNAL_SERVER_ERROR, e);
		request.setAttribute(ErrorDto.ERROR_REQUEST_ATTR, dto);
		return dto;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e, Authentication auth) throws IOException {
		logger.error("Annotations @Valid @Validated aren`t supported on controller params.", e);
	}
	
	@ExceptionHandler(BeanValidationException.class)
	public ErrorDto handleValidationException(HttpServletRequest request, HttpServletResponse response, BeanValidationException e, Authentication auth) throws IOException {
		logWarn(e, request, auth);
		response.sendError( e.getErrorCode() );
		ErrorDto dto = new ValidationErrorDto( e.getValidationErrors() );
		request.setAttribute(ErrorDto.ERROR_REQUEST_ATTR, dto);
		return dto;
	}
	
	@ExceptionHandler(BusinessException.class)
	public ErrorDto handleBusinessException(HttpServletRequest request, HttpServletResponse response, BusinessException e, Authentication auth) throws IOException {
		logWarn(e, request, auth);
		response.sendError( e.getErrorCode() );
		ErrorDto dto = new BusinessErrorDto<>(e.getErrorType());
		request.setAttribute(ErrorDto.ERROR_REQUEST_ATTR, dto);
		return dto;
	}

	@ExceptionHandler(ApiException.class)
	public ErrorDto handleApiException(HttpServletRequest request, HttpServletResponse response, ApiException e, Authentication auth) throws IOException {
		if (e.getErrorCode() == RC.INTERNAL_SERVER_ERROR) {
			logError(e, request, auth);
		} else {
			logWarn(e, request, auth);
		}
		response.sendError( e.getErrorCode() );
		
		Throwable exception = null;
		Throwable cause = e.getCause();
		if (cause != null) {
			exception = cause;
		} else {
			exception = e;
		}
		ErrorDto dto = new ErrorDto(e.getErrorCode(), exception);
		request.setAttribute(ErrorDto.ERROR_REQUEST_ATTR, dto);
		return dto;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDto handleException(HttpServletRequest request, Exception e, Authentication auth) throws Exception {
		logError(e, request, auth);
		ErrorDto dto = new ErrorDto(RC.INTERNAL_SERVER_ERROR, e);
		request.setAttribute(ErrorDto.ERROR_REQUEST_ATTR, dto);
		return dto;
	}
	
}
