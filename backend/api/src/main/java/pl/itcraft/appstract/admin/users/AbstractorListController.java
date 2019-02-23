package pl.itcraft.appstract.admin.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ErrorDto;
import pl.itcraft.appstract.core.dto.PageableDto;
import pl.itcraft.appstract.core.utils.UtilsBean;

@RestController
@Api(tags = {"abstractors"}, produces = "application/json")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AbstractorListController {
	
	@Autowired
	private AbstractorsListService service;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@GetMapping("abstractors")
	@ApiOperation(value = "Get abstractors accounts list", notes = "Returns all abstractors as list")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public Page<AbstractorsListRowDto> getList(PageableDto pageableDto) {
		return service.getList(pageableDto);
	}

	@GetMapping("abstractors/{abstractorId}")
	@ApiOperation(value = "Get abstractor", notes = "Returns abstractor details")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success"),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "Abstractor not found", response = ErrorDto.class)
	})
	public AbstractorListDetailsDto getDetails(@PathVariable("abstractorId") Long abstractorId) {
		return service.getDetails(abstractorId);
	}
	
	@GetMapping("abstractor-profile")
	@ApiOperation(value = "Get abstractor profile", notes = "Returns abstractor details")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public AbstractorListDetailsDto getAbstractorProfile() {
		return service.getDetails(utilsBean.getCurrentUser().getId());
	}
	
	@PutMapping("abstractor-profile")
	@ApiOperation(value = "Update abstractor profile", notes = "Updates abstractor details")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public AbstractorListDetailsDto updateAbstractorProfile(@RequestBody AbstractorProfileUpdateDto dto) {
		return service.updateDetails(dto);
	}
}
