package pl.itcraft.appstract.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ListDto;
import pl.itcraft.appstract.core.dto.NamedIdDto;

@RestController
@RequestMapping("services")
@Api(tags = {"services"}, produces = "application/json")
public class ServicesController {

	@Autowired
	private ServicesService servicesService;

	@GetMapping("/")
	@ApiOperation(value = "Get all services", notes = "Returns all services")
	@ApiResponses({
			@ApiResponse(code = RC.OK, message = "Success")
	})
	public ListDto<NamedIdDto> getAllServices() {
		return new ListDto<>(servicesService.getAll());
	}
	
}
