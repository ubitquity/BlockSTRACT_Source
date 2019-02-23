package pl.itcraft.appstract.admin.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.admin.order.OrdersInStatusesDto;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ListDto;
import pl.itcraft.appstract.core.dto.NamedIdDto;
import pl.itcraft.appstract.core.dto.PageableDto;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.utils.UtilsBean;

@RestController
@RequestMapping("orders")
@Api(tags = {"orders"}, produces = "application/json")
public class OrdersController {
	
	@Autowired
	private OrdersService service;

	@Autowired
	private UtilsBean utilsBean;

	@GetMapping("")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Get orders list", notes = "Returns all orders as list")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public Page<OrderListRowDto> getList(PageableDto pageableDto, OrdersListFilterDto filter) {
		return service.getOrders(pageableDto, filter);
	}
	
	@GetMapping("/abstractor-list")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Get orders list for abstractor", notes = "Returns all orders as list for abstractor")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public Page<AbstractorOrderListRowDto> getAbstractorList(PageableDto pageableDto, OrdersListFilterDto filter) {
		return service.getOrdersForAbstractor(pageableDto, filter);
	}

	@GetMapping("/active")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Get abstractor active orders list", notes = "Returns all active orders")
	@ApiResponses({
			@ApiResponse(code = RC.OK, message = "Success")
	})
	public ListDto<NamedIdDto> getActiveOrders() {
		User loggedUser = utilsBean.getCurrentUser();
		return new ListDto<>(service.getUserActiveOrders(loggedUser));
	}

	@GetMapping("/list-with-abstractors")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Get order list with abstractors")
	@ApiResponses({
			@ApiResponse(code = RC.OK, message = "Success")
	})
	public ListDto<NamedIdDto> getOrdersWithAbstractors() {
		return new ListDto<>(service.getOrdersWithAbstractors());
	}
	
	@GetMapping("/orders-in-statuses")
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Get orders in statuses counters")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public OrdersInStatusesDto getOrdersInStatuses() {
		return service.getOrdersInStatuses();
	}
	
}
