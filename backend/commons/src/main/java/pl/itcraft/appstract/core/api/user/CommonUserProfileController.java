package pl.itcraft.appstract.core.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.UserDto;
import pl.itcraft.appstract.core.dto.ValidationErrorDto;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.user.UserService;
import pl.itcraft.appstract.core.utils.UtilsBean;
import pl.itcraft.appstract.core.validation.AppValidator;

@RestController
@Api(tags = {"userProfile"}, produces = "application/json")
public class CommonUserProfileController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AppValidator validator;

	@Autowired
	private UtilsBean utilsBean;

	@GetMapping("user/profile")
	@ApiOperation(value = "Get user profile", notes = "Returns current user profile details")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public UserDto getUserProfile() {
		User user = utilsBean.getCurrentUser();
		return userService.getUserProfile( user.getId() );
	}

	@PostMapping("user/change-password")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Set new password", notes = "Set new password for current user")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
		@ApiResponse(code = RC.VALIDATION_ERROR, message = "Form validation failed", response = ValidationErrorDto.class)
	})
	public void changePassword(@RequestBody ChangePasswordDto dto) throws Exception {
		validator.validate(dto);
		User user = utilsBean.getCurrentUser();
		userService.changeUserPassword(user.getId(), dto.getNewPassword(), false);
	}
	
	@PostMapping("user/upload-avatar")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Set new avatar", notes = "Set new avatar for current user")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void uploadAvatar(@RequestParam(value = "avatar", required = false) MultipartFile avatar) {
		userService.uploadAvatar(utilsBean.getCurrentUser().getId(), avatar);
	}
	
}
