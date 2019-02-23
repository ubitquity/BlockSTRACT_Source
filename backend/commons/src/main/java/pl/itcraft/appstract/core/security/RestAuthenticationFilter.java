package pl.itcraft.appstract.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import pl.itcraft.appstract.core.CoreConstants;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.user.UserService;

public class RestAuthenticationFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationFilter.class);
	
	@Autowired
	private UserService userService;

	private AuthenticationManager authenticationManager;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsr = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String token = findTokenInRequest(hsr);
		String userAgent = StringUtils.isEmpty(hsr.getHeader("User-Agent")) ? "UNKNOWN" : hsr.getHeader("User-Agent");
		
		User user = StringUtils.isBlank(token) ? null : userService.findForTokenAuthentication(token);
		if (user != null) {
			user.setCurrentAccessToken(token);
			user.setCurrentUserAgent(userAgent);
			UserAccount userAccount = new UserAccount(user);
			PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userAccount, token, userAccount.getAuthorities());
			
			Authentication a = authenticationManager.authenticate(authentication);
			SecurityContextHolder.getContext().setAuthentication(a);
			extendTokenExpiration(hsr, token);
		} else {
			logger.info("Invalid token [{}]. User-Agent [{}]", token, userAgent);
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		chain.doFilter(request, response);
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	private void extendTokenExpiration(HttpServletRequest hsr, String token) {
		// TODO: Jakis sensowny i wydajny sposob na przedluzanie terminu waznosci tokena
		if (!hsr.getServletPath().startsWith("/file/")) {
			userService.extendTokenExpiration(token);
		}
	}
	
	private String findTokenInRequest(HttpServletRequest hsr) {
		String token = hsr.getHeader(CoreConstants.AUTH_TOKEN_HEADER);
		if (StringUtils.isBlank(token)) {
			Cookie cookie = WebUtils.getCookie(hsr, CoreConstants.AUTH_TOKEN_COOKIE);
			if (cookie != null) {
				token = cookie.getValue();
			}
		}
		return token;
	}
	
}
