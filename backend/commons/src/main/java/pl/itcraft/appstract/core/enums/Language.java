package pl.itcraft.appstract.core.enums;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum Language {
	PL("PL"),
	EN("US");
	
	public static Language defaultLang = Language.EN;
	
	private static Map<String,Language> ACCEPT_LANGUAGE_MAP = new HashMap<>();
	static {
		for (Language lang : values()) {
			ACCEPT_LANGUAGE_MAP.put(lang.getLcName(), lang);
		}
	}
	
	private Locale locale;
	
	public static Language fromLocale(Locale locale) {
		return Language.valueOf(locale.getLanguage().toUpperCase());
	}
	
	public static Language fromAcceptLanguage(String acceptLanguage) {
		return ACCEPT_LANGUAGE_MAP.containsKey(acceptLanguage) ? ACCEPT_LANGUAGE_MAP.get(acceptLanguage) : defaultLang;
	}
	
	private Language(String country) {
		locale = new Locale(name().toLowerCase(), country);
	}
	
	public String getName() {
		return name();
	}
	
	public String getLcName() {
		return name().toLowerCase();
	}
	
	public Locale getLocale() {
		return locale;
	}
	
}
