package pl.itcraft.appstract.core.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { CurrentPasswordConstraintValidator.class })
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface CurrentPassword {

	String message() default CoreValidationMessages.INVALID_CURRENT_PASSWORD;
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };

	@Target({METHOD, FIELD})
	@Retention(RUNTIME)
	public @interface List {
		CurrentPassword[] value();
	}
}
