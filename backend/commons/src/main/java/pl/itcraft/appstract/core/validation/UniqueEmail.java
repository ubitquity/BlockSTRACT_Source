package pl.itcraft.appstract.core.validation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { UniqueEmailConstraintValidator.class })
@Target({METHOD})
@Retention(RUNTIME)
public @interface UniqueEmail {

	String fieldOverride() default "";
	String message() default CoreValidationMessages.INVALID_UNIQUE_EMAIL;
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };

	@Target({METHOD})
	@Retention(RUNTIME)
	public @interface List {
		UniqueEmail[] value();
	}
}
