package pl.itcraft.appstract.core.validation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class CoreValidationMessages {
	
	public static final String REQUIRED = "REQUIRED";
	
	public static final String INVALID_EMAIL = "INVALID_EMAIL";
	public static final String INVALID_UNIQUE_EMAIL = "INVALID_UNIQUE_EMAIL";
	public static final String INVALID_UNIQUE_USERNAME = "INVALID_UNIQUE_USERNAME";
	public static final String INVALID_USERNAME = "INVALID_USERNAME";
	public static final String HTML_NOT_ALLOWED = "HTML_NOT_ALLOWED";
	
	public static final String INVALID_SIZE = "INVALID_SIZE";
	public static final String INVALID_LENGTH = "INVALID_LENGTH";
	public static final String INVALID_RANGE = "INVALID_RANGE";
	
	public static final String INVALID_MIN_VALUE = "INVALID_MIN_VALUE";
	public static final String INVALID_MAX_VALUE = "INVALID_MAX_VALUE";
	
	public static final String INVALID_PATTERN = "INVALID_PATTERN";
	public static final String INVALID_DIGITS = "INVALID_DIGITS";
	public static final String INVALID_SAFE_HTML = "INVALID_SAFE_HTML";
	public static final String INVALID_VALUE = "INVALID_VALUE";
	
	public static final String PASSWORD_TOO_WEAK = "PASSWORD_TOO_WEAK";
	public static final String INVALID_CURRENT_PASSWORD = "INVALID_CURRENT_PASSWORD";
	
	public static final String USERNAME_ALREADY_EXISTS = "USERNAME_ALREADY_EXISTS";
	public static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
	
	
	@PostConstruct
	protected void checkValuesVsNames() {
		List<String> mismatches = new ArrayList<>();
		for (Field field : CoreValidationMessages.class.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				Object value = null;
				String name = field.getName();
				try {
					value = field.get(null);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (!name.equals(value)) {
					mismatches.add( CoreValidationMessages.class.getSimpleName() + "." + name );
				}
			}
		}
		if (!mismatches.isEmpty()) {
			throw new RuntimeException("Mismatched key(s) " + Arrays.toString(mismatches.toArray()));
		}
	}
	
}
