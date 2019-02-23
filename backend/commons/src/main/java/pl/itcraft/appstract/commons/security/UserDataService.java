package pl.itcraft.appstract.commons.security;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.security.UserAccount;
import pl.itcraft.appstract.core.user.UserService;

public class UserDataService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserDataService.class);

	@Autowired
	private UserService userService;
	
	private UserRole[] supportedRoles;

	public UserDataService(String... supportedRoles) {
		this.supportedRoles = new UserRole[supportedRoles.length];
		for (int i = 0; i < supportedRoles.length; i++) {
			this.supportedRoles[i] = UserRole.valueOf(supportedRoles[i]);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, NotActivatedException {
		User user = userService.findForEmailAuthentication(username);
		if (user == null) {
			throw createError("User [" + username + "] not found.");
		}
		if (user.getRole().equals(UserRole.SYSTEM)) {
			throw createError("You can`t log in as system user.");
		}
		UserRole userRole = user.getRole();
		for (UserRole supportedRole : supportedRoles) {
			if (userRole.equals(supportedRole)) {
				if (!user.isAccountActivated()) {
					throw createNotActivatedError("User [" + username + " : " + userRole + "] is not activated");
				}
				return new UserAccount(user);
			}
		}
		throw createError("User [" + username + " : " + userRole + "] has no supported role " + Arrays.toString(supportedRoles));
	}
	
	private UsernameNotFoundException createError(String msg) {
		logger.warn(msg);
		return new UsernameNotFoundException(msg);
	}
	
	private NotActivatedException createNotActivatedError(String msg) {
		logger.warn(msg);
		return new NotActivatedException(msg);
	}
	
}
