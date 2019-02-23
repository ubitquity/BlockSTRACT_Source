package pl.itcraft.appstract.core.api.error;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Przechwytuje wyjatki rzucone w kontrolerach @Controller
 */
@ControllerAdvice(
	annotations = Controller.class
)
@Order(Ordered.LOWEST_PRECEDENCE)
public class ControllerErrorHandler extends BaseControllerErrorHandler {

	@PostConstruct
	protected void postConstruct() {
		init( LoggerFactory.getLogger(ControllerErrorHandler.class) );
	}

	@ExceptionHandler(ApiException.class)
	public void handleApiException(HttpServletRequest request, HttpServletResponse response, ApiException e, Authentication auth) throws IOException {
		if (e.getErrorCode() == RC.INTERNAL_SERVER_ERROR) {
			logError(e, request, auth);
		} else {
			logWarn(e, request, auth);
		}
		response.sendError( e.getErrorCode() );
	}
	
	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e, Authentication auth) throws Exception {
		logError(e, request, auth);
		response.sendError(RC.INTERNAL_SERVER_ERROR);
		return;
	}
	
}
