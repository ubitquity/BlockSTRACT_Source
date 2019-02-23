package pl.itcraft.appstract.admin.abstractor.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ValidationErrorDto;
import pl.itcraft.appstract.core.validation.AppValidator;

@RestController
@Api(tags = "abstractor")
@RequestMapping(value = "/abstractor/")
public class AbstractorSignupController {

	@Autowired
	private AppValidator validator;
	
	@Autowired
	private AbstractorSignupService service;
	
	@PostMapping("signup")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Register abstractor", notes = "Create new abstractor account")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
		@ApiResponse(code = RC.VALIDATION_ERROR, message = "Form validation failed", response = ValidationErrorDto.class)
	})
	public void registerUser(@RequestBody AbstractorSignupDto dto) {
		validator.validate(dto);
		service.signupAbstractor(dto);
	}

	@GetMapping("activate/{token}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Activate abstractor", notes = "Activate abstractor account")
	@ApiResponses({
			@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
			@ApiResponse(code = RC.VALIDATION_ERROR, message = "Form validation failed", response = ValidationErrorDto.class)
	})
	public void activateAbstractor(@PathVariable("token") String token) {
		service.activate(token);

	}

}
