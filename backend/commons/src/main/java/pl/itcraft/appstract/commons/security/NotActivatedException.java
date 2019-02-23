package pl.itcraft.appstract.commons.security;

import org.springframework.security.authentication.AccountStatusException;

public class NotActivatedException extends AccountStatusException {

	private static final long serialVersionUID = -7985155534176421027L;
	
	public NotActivatedException(String msg) {
		super(msg);
	}
	
	public NotActivatedException(String msg, Throwable t) {
		super(msg, t);
	}

}
