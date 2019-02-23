package pl.itcraft.appstract.core.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.LocaleResolver;

import pl.itcraft.appstract.core.enums.Language;

public class ApiClientLocaleResolver implements LocaleResolver {
	
	private static final Logger LOG = LoggerFactory.getLogger(ApiClientLocaleResolver.class);
	
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String acceptLanguage = request.getHeader("Accept-Language");
		Locale resolvedLocale = Language.fromAcceptLanguage(acceptLanguage).getLocale();
		LOG.debug("Locale '{}' resolved form '{}'", resolvedLocale, acceptLanguage);
		return resolvedLocale;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		throw new UnsupportedOperationException("Cannot change HTTP accept header");
	}

}
