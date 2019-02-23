package pl.itcraft.appstract.admin.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
import pl.itcraft.appstract.core.dto.ValidationErrorDto;
import pl.itcraft.appstract.core.utils.UtilsBean;
import pl.itcraft.appstract.core.validation.AppValidator;

@RestController
@Api(tags = {"adminProfile"}, produces = "application/json")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminProfileController {
	
	@Autowired
	private AppValidator validator;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private AdminProfileService service;

	@GetMapping("admin")
	@ApiOperation(value = "Get admin profile", notes = "Returns currently logged in admin's profile data")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success"),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "Admin not found", response = ErrorDto.class)
	})
	public AdminProfileDto getDetails() {
		return new AdminProfileDto(utilsBean.getCurrentUser());
	}
	
	@PutMapping("admin")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Update admin profile", notes = "Update currently logged in admin's profile data")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success"),
		@ApiResponse(code = RC.VALIDATION_ERROR, message = "Form validation failed", response = ValidationErrorDto.class),
	})
	public void updateAdmin(@RequestBody AdminProfileUpdateDto dto) {
		dto.setAdminId(utilsBean.getCurrentUser().getId());
		validator.validate(dto);
		service.update(utilsBean.getCurrentUser().getId(), dto);
	}
	
}
