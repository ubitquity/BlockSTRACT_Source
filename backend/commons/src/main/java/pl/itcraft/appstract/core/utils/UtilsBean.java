package pl.itcraft.appstract.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.env.Environment;

import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.Language;
import pl.itcraft.appstract.core.security.UserAccount;

public class UtilsBean {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private Environment env;
	
	private String applicationVersion;
	private String profile;
	
	public UtilsBean(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}
	
	protected void init() {
		String[] activeProfiles = env.getActiveProfiles();
		if (activeProfiles != null && activeProfiles.length == 1) {
			this.profile = activeProfiles[0];
			return;
		}
		throw new RuntimeException("Unexpected profiles configuration: " + Arrays.toString(activeProfiles));
	}
	
	public Locale getCurrentLocale() {
		LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
		if (localeContext != null) {
			Locale locale = localeContext.getLocale();
			if (locale != null) {
				return locale;
			}
		}
		throw new IllegalStateException("LocaleContext is undefined. If you are not in servlet dispatcher thread you should explicite pass Locale to method is dependent on Locale");
	}
	
	public String getEnumMessage(Enum<?> instance) {
		if (instance == null) {
			return null;
		}
		return instance.name();
	}
	
	public String getMessage(String code, Object... params) {
		return messageSource.getMessage(code, params, getCurrentLocale());
	}

	public String getMessage(Locale locale, String code, Object... params) {
		return messageSource.getMessage(code, params, locale);
	}
	
	public String getApplicationVersion() {
		return applicationVersion;
	}
	
	public String getProfile() {
		return profile;
	}
	
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserAccount) {
				User user = ((UserAccount) principal).getUser();
				return user;
			}
		}
		return null;
	}

	public List<String> getWarningsFromKeys(List<String> warningKeys){
		List<String> warnings = new ArrayList<String>();
		for(String warningKey : warningKeys){
			warnings.add(getMessage(warningKey, null, getCurrentLocale()));
		}
		return warnings;
	}
	
	public Map<String, String> getChangeLanguageOptions() {
		Map<String, String> languages = new LinkedHashMap<>();
		for(Language l : Language.values()) {
			languages.put(l.getLcName(), getEnumMessage(l));
		}
		return languages;
	}

	public String getGeocodingApiKey() {
		return "";
	}
	
}
