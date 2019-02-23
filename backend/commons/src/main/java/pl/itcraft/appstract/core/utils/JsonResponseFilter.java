package pl.itcraft.appstract.core.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import pl.itcraft.appstract.core.BaseAppModuleConfig;

public class JsonResponseFilter extends GenericFilterBean {
	
	@Autowired
	private BaseAppModuleConfig config;

	private static final Logger LOG = LoggerFactory.getLogger(JsonResponseFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String servletPath = httpRequest.getServletPath();
		
		LOG.debug("Request: {} {}", httpRequest.getMethod(), servletPath);
		
		if (!servletPath.startsWith("/resources/") && !servletPath.startsWith("/resources-commons/") && !servletPath.contains("swagger-")) {
			// REST
			httpResponse.setHeader("Access-Control-Allow-Origin", config.getCurrentApiUrl());
			httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept-Language, Accept");
		}
		// Tutaj nie lapiemy wyjatkow.
		// Wszystko bedzie obsluzone w RestErrorHandler lub ErrorController
		chain.doFilter(request, response);
	}
}
