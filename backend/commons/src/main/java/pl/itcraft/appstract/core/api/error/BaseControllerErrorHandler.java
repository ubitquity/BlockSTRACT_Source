package pl.itcraft.appstract.core.api.error;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import pl.itcraft.appstract.core.security.UserAccount;

public abstract class BaseControllerErrorHandler {

	protected Logger logger;
	
	protected void init(Logger logger) {
		this.logger = logger;
	}

	protected void logWarn(Exception e, HttpServletRequest httpRequest, Authentication auth) {
		String user = getAuthenticatedUser(auth);
		logger.warn("{} at {} {} (user: {})", errorToString(e), httpRequest.getMethod(), httpRequest.getServletPath(), user);
	}

	protected void logError(Exception e, HttpServletRequest httpRequest, Authentication auth) {
		String user = getAuthenticatedUser(auth);
		String msg = String.format("%s at %s %s (user: %s)", errorToString(e), httpRequest.getMethod(), httpRequest.getServletPath(), user);
		logger.error(msg, e);
	}
	
	private String errorToString(Exception e) {
		if (e instanceof ApiException) {
			ApiException ae = (ApiException) e;
			return ae.getClass().getSimpleName() + ":" + ae.getErrorCode();
		} else {
			return e.getClass().getSimpleName();
		}
	}
	
	private String getAuthenticatedUser(Authentication auth) {
		if (auth != null && auth.getPrincipal() instanceof UserAccount) {
			return ((UserAccount) auth.getPrincipal()).getUser().getEmail();
		} else {
			return "NONE";
		}
	}
	
}
