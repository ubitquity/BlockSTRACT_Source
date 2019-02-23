package pl.itcraft.appstract.core.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { StrongPasswordConstraintValidator.class })
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface StrongPassword {
	
	String message() default CoreValidationMessages.PASSWORD_TOO_WEAK;
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };

	@Target({METHOD, FIELD})
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		StrongPassword[] value();
	}
}
