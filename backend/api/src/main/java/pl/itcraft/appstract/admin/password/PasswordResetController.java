package pl.itcraft.appstract.admin.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.api.auth.AccountErrorDto;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ErrorDto;
import pl.itcraft.appstract.core.dto.ValidationErrorDto;
import pl.itcraft.appstract.core.entities.PasswordReset;
import pl.itcraft.appstract.core.utils.UtilsBean;
import pl.itcraft.appstract.core.validation.AppValidator;

@RestController
@Api(tags={"password"}, produces = "application/json")
public class PasswordResetController {

	@Autowired
	private PasswordResetService service;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private AppValidator validator;

	@PostMapping("password/reset")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Send link via e-mail", notes = "Finds user by email and send link to reset new password")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "Account not found", response = ErrorDto.class),
		@ApiResponse(code = RC.BUSINESS_ERROR, message = "Valid credentials but account status forbids user to login", response = AccountErrorDto.class)
	})
	public void resetPassword(@RequestBody ResetPasswordDto dto) {
		validator.validate(dto);
		String token = service.reset(dto);
		service.sendEmailWithinResetLink(utilsBean.getCurrentLocale(), dto.getEmail(), dto.getRelativePath(), token);
	}

	@PostMapping("password/set-new")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Set new password", notes = "Finds user by e-mail and send link to reset new password")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
		@ApiResponse(code = RC.BAD_REQUEST, message = "Invalid or expired token", response = ErrorDto.class),
		@ApiResponse(code = RC.VALIDATION_ERROR, message = "Form validation failed", response = ValidationErrorDto.class)
	})
	public void resetUserPassword(@RequestBody ResetUserPasswordDto dto) throws Exception {
		validator.validate(dto);
		PasswordReset pr = service.findByToken(dto.getToken());
		if (pr == null) {
			throw new ApiException(RC.FORBIDDEN, "Token expired");
		}
		service.resetPassword(pr.getUser(), dto.getNewPassword());
	}

}
