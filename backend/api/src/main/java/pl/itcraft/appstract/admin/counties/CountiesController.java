package pl.itcraft.appstract.admin.counties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ListDto;
import pl.itcraft.appstract.core.dto.NamedIdDto;

@RestController
@RequestMapping("counties")
@Api(tags = {"counties"}, produces = "application/json")
public class CountiesController {

	@Autowired
	private CountiesService countiesService;

	@GetMapping("/")
	@ApiOperation(value = "Get all counties", notes = "Returns all counties")
	@ApiResponses({
			@ApiResponse(code = RC.OK, message = "Success")
	})
	public ListDto<NamedIdDto> getAllCounties() {
		return new ListDto<>(countiesService.getAll());
	}

}
