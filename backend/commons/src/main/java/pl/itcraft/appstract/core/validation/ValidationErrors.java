package pl.itcraft.appstract.core.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.itcraft.appstract.core.api.error.BeanValidationException;
import pl.itcraft.appstract.core.dto.ErrorMsgDto;
import pl.itcraft.appstract.core.dto.ValidationErrorMsgDto;

public class ValidationErrors {
	
	private List<ErrorMsgDto> globalErrors = new ArrayList<>();
	
	private List<ValidationErrorMsgDto> fieldErrors = new ArrayList<>();
	
	private Set<String> fields = new HashSet<>();
	
	public void throwIfNotEmpty() {
		if (hasErrors()) {
			throw new BeanValidationException(this);
		}
	}
	
	public void reject(String msg, Object... args) {
		globalErrors.add(new ErrorMsgDto(msg, stringifyArgs(args)));
	}
	
	public void rejectValue(String field, String msg, Object... args) {
		fields.add(field);
		fieldErrors.add(new ValidationErrorMsgDto(field, msg, stringifyArgs(args)));
	}
	
	private String[] stringifyArgs(Object[] args) {
		String[] stringArgs = new String[0];
		if (args != null) {
			stringArgs = new String[args.length];
			for (int i = 0; i < args.length; i++) {
				stringArgs[i] = args[i] == null ? "" : args[i].toString();
			}
		}
		return stringArgs;
	}
	
	public boolean hasErrors() {
		return !fieldErrors.isEmpty();
	}
	
	public boolean hasFieldErrors(String field) {
		return fields.contains(field);
	}
	
	public boolean noFieldErrors(String field) {
		return !fields.contains(field);
	}
	
	public List<ErrorMsgDto> getGlobalErrors() {
		return globalErrors;
	}
	
	public List<ValidationErrorMsgDto> getFieldErrors() {
		return fieldErrors;
	}
	
}
