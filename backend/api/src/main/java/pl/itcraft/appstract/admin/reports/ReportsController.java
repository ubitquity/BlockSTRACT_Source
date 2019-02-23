package pl.itcraft.appstract.admin.reports;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ValueDto;

@RestController
@Api(tags = {"reports"}, produces = "application/json")
public class ReportsController {

	@Autowired
	private ReportsService service;
	
	@GetMapping("/most-responsive")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Get most responsive abstractor", notes = "Returns most responsive abstractor's data")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public MostResponsiveAbstractorDto getMostResponsiveAbstractor() {
		return service.getMostResponsiveAbstractor();
	}
	
	@GetMapping("/cheapest")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Get cheapest abstractor", notes = "Returns cheapest abstractor's data")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public AbstractorWithAverageCostDto getCheapestAbstractor() {
		return service.getCheapestAbstractor();
	}
	
	@GetMapping("/most-expensive")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Get most expensive abstractor", notes = "Returns most expensive abstractor's data")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public AbstractorWithAverageCostDto getMostExpensiveAbstractor() {
		return service.getMostExpensiveAbstractor();
	}
	
	@GetMapping("/average-cost/{startDate}/{months}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Get average cost", notes = "Returns average cost for orders in the gicen time period")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public ValueDto<BigDecimal> getAverageCost(
			@PathVariable("startDate")
			@DateTimeFormat(pattern = "yyyy-MM-dd")
			@ApiParam(value = "Date in yyyy-MM-dd format", required = true)
			Date startDate,
			@PathVariable("months") int months) {
		ValueDto<BigDecimal> dto = new ValueDto<>();
		dto.setValue(service.getAverageCost(startDate, months));
		return dto;
	}
	
	@GetMapping("/total-income")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Get total income", notes = "Returns total income for current abstractor")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public ValueDto<BigDecimal> getTotalIncome() {
		ValueDto<BigDecimal> dto = new ValueDto<>();
		dto.setValue(service.getTotalIncome());
		return dto;
	}
	
	@GetMapping("/projects-won")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Get projects won", notes = "Returns number of projects won for current abstractor")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public ValueDto<Integer> getProjectsWon() {
		ValueDto<Integer> dto = new ValueDto<>();
		dto.setValue(service.getProjectsWon());
		return dto;
	}
	
	@GetMapping("/projects-declined")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Get projects declined", notes = "Returns number of projects declined for current abstractor")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public ValueDto<Integer> getProjectsDeclined() {
		ValueDto<Integer> dto = new ValueDto<>();
		dto.setValue(service.getProjectsDeclinedOrRecalled(false));
		return dto;
	}
	
	@GetMapping("/projects-recalled")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Get projects recalled", notes = "Returns number of projects recalled for current abstractor")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public ValueDto<Integer> getProjectsRecalled() {
		ValueDto<Integer> dto = new ValueDto<>();
		dto.setValue(service.getProjectsDeclinedOrRecalled(true));
		return dto;
	}
	
}
