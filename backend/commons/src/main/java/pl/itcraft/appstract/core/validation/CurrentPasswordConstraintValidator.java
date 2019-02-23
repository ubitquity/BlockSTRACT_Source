package pl.itcraft.appstract.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import pl.itcraft.appstract.core.utils.UtilsBean;

public class CurrentPasswordConstraintValidator implements ConstraintValidator<CurrentPassword, String>  {

	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void initialize(CurrentPassword constraintAnnotation) {
		// nothing to do here
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (!StringUtils.hasLength(value)) {
			return true;
		}
		return passwordEncoder.matches(value, utilsBean.getCurrentUser().getPassword());
	}
	
}
