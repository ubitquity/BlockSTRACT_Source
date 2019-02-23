package pl.itcraft.appstract.core.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordConstraintValidator implements ConstraintValidator<StrongPassword, String>  {

	@Override
	public void initialize(StrongPassword constraintAnnotation) {
		// nothing to do here
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		if (value.length() < 4 || value.length() > 50) {
			return false;
		}
		int matches = 0;
		if (LOWER_CASE_PATTERN.matcher(value).find()) {
			matches++;
		}
		if (UPPER_CASE_PATTERN.matcher(value).find()) {
			matches++;
		}
		if (DIGITS_PATTERN.matcher(value).find()) {
			matches++;
		}
		if (SPECIAL_PATTERN.matcher(value).find()) {
			matches++;
		}
		return (matches >= MIN_MATCHING_GROUPS);
	}
	
	private static final int MIN_MATCHING_GROUPS = 3;
	
	private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("([a-z])+");
	private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("([A-Z])+");
	private static final Pattern DIGITS_PATTERN = Pattern.compile("([0-9])+");
	private static final Pattern SPECIAL_PATTERN = Pattern.compile("(\\W|_)+");

}
