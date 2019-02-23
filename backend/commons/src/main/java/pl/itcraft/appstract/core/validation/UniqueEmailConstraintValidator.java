package pl.itcraft.appstract.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.user.UserService;

public class UniqueEmailConstraintValidator implements ConstraintValidator<UniqueEmail, NamedIdDto>  {

	@Autowired
	private UserService service;
	
	@Override
	public void initialize(UniqueEmail constraintAnnotation) {
		// nothing to do here
	}

	@Override
	public boolean isValid(NamedIdDto value, ConstraintValidatorContext context) {
		if (!StringUtils.hasLength(value.getName())) {
			return true;
		}
		return service.isEmailUnique(value.getName(), value.getId());
	}
	
}
