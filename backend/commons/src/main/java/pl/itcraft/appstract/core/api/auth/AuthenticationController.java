package pl.itcraft.appstract.core.api.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.commons.security.NotActivatedException;
import pl.itcraft.appstract.core.CoreConstants;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.BusinessException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ErrorDto;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.security.UserAccount;
import pl.itcraft.appstract.core.user.UserService;
import pl.itcraft.appstract.core.utils.UtilsBean;

@RestController
@Api(tags = {"auth"}, produces = "application/json")
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private ApplicationContext ctx;
	
	@PostMapping("login")
	@ApiOperation(value = "Login user", notes = "Logins user when valid credentials")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Successfully logged in"),
		@ApiResponse(code = RC.UNAUTHORIZED, message = "Bad credentials", response = ErrorDto.class),
		@ApiResponse(code = RC.BUSINESS_ERROR, message = "Valid credentials but account status forbids user to login", response = AccountErrorDto.class)
	})
	public AuthenticationResultDto login(@RequestBody(required=true) LoginDto dto, HttpServletRequest request, HttpServletResponse response) {
		String username = dto.getEmail();
		final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, dto.getPassword());	
		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(authRequest);
		} catch (BadCredentialsException e) {
			userService.onBadCredentialsError(username);
			throw new ApiException(RC.UNAUTHORIZED, e);
		} catch (LockedException e) {
			throw new BusinessException( AccountErrorType.LOCKED );
		} catch (DisabledException e) {
			throw new BusinessException( AccountErrorType.DISABLED );
		} catch (AuthenticationException e) {
			if (e.getCause() instanceof NotActivatedException) {
				throw new BusinessException( AccountErrorType.NOT_ACTIVATED );
			}
			throw new ApiException(RC.INTERNAL_SERVER_ERROR, e);
		}
		
		User user = ((UserAccount) authentication.getPrincipal()).getUser();
		AuthenticationResultDto result = userService.login(user, request.getRemoteHost(), request.getHeader("User-Agent"));
		logger.info("Login success: {}", username);
		setCookieWithToken(response, result.getAccessToken());
		return result;
	}
	
	@PostMapping("logout")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Logout user", notes = "Logins user when valid credentials")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Successfully logged out")
	})
	public void logout(HttpServletResponse response)	{
		User currentUser = utilsBean.getCurrentUser();
		logger.info("Logout user: {}", currentUser.getEmail());
		userService.logout( currentUser.getCurrentAccessToken() );
		removeCookieWithToken(response);
	}
	
	@GetMapping("resend-cookie")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Resend cookie", notes = "Resends auth cookie based on auth token")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void resendCookie(HttpServletRequest request, HttpServletResponse response) {
		if (WebUtils.getCookie(request, CoreConstants.AUTH_TOKEN_COOKIE) == null) {
			User user = utilsBean.getCurrentUser();
			if (user != null) {
				setCookieWithToken(response, user.getCurrentAccessToken());
			}
		}
	}
	
	private void setCookieWithToken(HttpServletResponse response, String value) {
		Cookie cookie = new Cookie(CoreConstants.AUTH_TOKEN_COOKIE, value);
		cookie.setPath(ctx.getApplicationName());
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
	
	private void removeCookieWithToken(HttpServletResponse response) {
		Cookie cookie = new Cookie(CoreConstants.AUTH_TOKEN_COOKIE, "none");
		cookie.setMaxAge(0);
		cookie.setPath(ctx.getApplicationName());
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
	
}
