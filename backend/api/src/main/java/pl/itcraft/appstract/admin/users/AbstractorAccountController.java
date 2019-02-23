package pl.itcraft.appstract.admin.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ErrorDto;
import pl.itcraft.appstract.core.dto.LongDto;
import pl.itcraft.appstract.core.dto.ValidationErrorDto;
import pl.itcraft.appstract.core.validation.AppValidator;

@RestController
@Api(tags = {"abstractors"}, produces = "application/json")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AbstractorAccountController {

	@Autowired
	private AbstractorAccountsService service;
	
	@Autowired
	private AppValidator validator;

	@DeleteMapping("abstractors/{abstractorId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete abstractor", notes = "Set deleted = false")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "Abstractor not found")
	})
	public void deleteUser(@PathVariable("abstractorId") Long abstractorId) {
		service.delete(abstractorId);
	}
	
	@PutMapping("abstractors/{abstractorId}/activate")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Activate abstractor", notes = "Set enabled = true")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "Abstractor not found")
	})
	public void activateUser(@PathVariable("abstractorId") Long abstractorId) {
		service.activate(abstractorId);
	}
	
	@PutMapping("abstractors/{abstractorId}/deactivate")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Deactivate abstractor", notes = "Set enabled = false")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "Abstractor not found")
	})
	public void deactivateUser(@PathVariable("abstractorId") Long abstractorId) {
		service.deactivate(abstractorId);
	}

	@PostMapping("abstractors")
	@ApiOperation(value = "Create new abstractor", notes = "Create new abstractor. Returns its ID.")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success", response = LongDto.class),
		@ApiResponse(code = RC.VALIDATION_ERROR, message = "Form validation failed", response = ValidationErrorDto.class),
	})
	public LongDto addUser(@RequestBody AbstractorAccountDto dto) {
		validator.validate(dto, AbstractorAccountDto.OnCreate.class);
		Long id = service.create(dto);
		return new LongDto(id);
	}
	
	@PutMapping("abstractors")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Update abstractor account", notes = "Update abstractor account")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success", response = LongDto.class),
		@ApiResponse(code = RC.VALIDATION_ERROR, message = "Form validation failed", response = ValidationErrorDto.class),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "User not found", response = ErrorDto.class)
	})
	public void updateUser(@RequestBody AbstractorAccountDto dto) {
		validator.validate(dto, AbstractorAccountDto.OnUpdate.class);
		service.update(dto);
	}
	
}