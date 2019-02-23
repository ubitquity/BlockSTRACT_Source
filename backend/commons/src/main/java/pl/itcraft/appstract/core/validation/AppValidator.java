package pl.itcraft.appstract.core.validation;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppValidator {

	@Autowired
	private Validator javaxValidator;
	
	private static final Object[] EMPTY_PARAMS = new Object[0];
	
	public void validate(Object target, Class<?>... groups) {
		Set<ConstraintViolation<Object>> violations = javaxValidator.validate(target, groups);
		ValidationErrors ve = translateConstraintViolations(violations);
		ve.throwIfNotEmpty();
	}
	
	public ValidationErrors getValidationErrors(Object target, Class<?>... groups) {
		Set<ConstraintViolation<Object>> violations = javaxValidator.validate(target, groups);
		return translateConstraintViolations(violations);
	}
	
	private ValidationErrors translateConstraintViolations(Set<ConstraintViolation<Object>> violations) {
		ValidationErrors ve = new ValidationErrors();
		
		for (ConstraintViolation<Object> violation : violations) {
			RejectedValue msg = createMsg(violation);
			if ("".equals(msg.path)) {
				// Global error
				ve.reject(msg.message, msg.params);
			} else {
				// Field error
				ve.rejectValue(msg.path, msg.message, msg.params);
			}
		}
		return ve;
	}
	
	private RejectedValue createMsg(ConstraintViolation<Object> violation) {
		String msg = violation.getMessageTemplate();
		Annotation a = violation.getConstraintDescriptor().getAnnotation();
		Map<String, Object> attr = violation.getConstraintDescriptor().getAttributes();
		String path = resolvePath( violation.getPropertyPath().toString(), attr);
		
		if (a instanceof NotNull || a instanceof NotBlank || a instanceof NotEmpty) {
			return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.REQUIRED), EMPTY_PARAMS);
		}
		if (a instanceof Size || a instanceof Length || a instanceof Range) {
			return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.INVALID_SIZE), new Object[] { attr.get("min"), attr.get("max") });
		}
		if (a instanceof Min || a instanceof DecimalMin) {
			return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.INVALID_MIN_VALUE), new Object[] { attr.get("value") });
		}
		if (a instanceof Max || a instanceof DecimalMax) {
			return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.INVALID_MAX_VALUE), new Object[] { attr.get("value") });
		}
		if (a instanceof Pattern) {
			return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.INVALID_PATTERN), new Object[] { attr.get("regexp") });
		}
		if (a instanceof Email) {
			return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.INVALID_EMAIL), EMPTY_PARAMS);
		}
		if (a instanceof Digits) {
			return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.INVALID_DIGITS), new Object[] { attr.get("integer"), attr.get("fraction") });
		}
		if (a instanceof SafeHtml) {
			SafeHtml safeHtml = (SafeHtml) a;
			return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.INVALID_SAFE_HTML), new Object[] { safeHtml.whitelistType().name() });
		}
		
		return new RejectedValue(path, resolveMessage(msg, CoreValidationMessages.INVALID_VALUE), EMPTY_PARAMS);
	}
	
	private String resolveMessage(String template, String fallback) {
		return (template.startsWith("{") && template.endsWith(".message}")) ? fallback : template;
	}
	
	private String resolvePath(String path, Map<String, Object> attr) {
		if (attr.containsKey("fieldOverride")) {
			String fieldOverride = attr.get("fieldOverride").toString();
			return path.replaceFirst("[^\\.]+$", fieldOverride);
		} else {
			return path;
		}
	}
	
	private class RejectedValue {
		String path;
		String message;
		Object[] params;
		
		RejectedValue(String path, String message, Object[] params) {
			this.path = path;
			this.message = message;
			this.params = params;
		}
	}
}
