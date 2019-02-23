package pl.itcraft.appstract.admin.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.admin.Constants;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;

@RestController
@RequestMapping("order")
@Api(tags = {"order"}, produces = "application/json")
public class OrderDetailsController {
	
	@Autowired
	private OrderDetailsService service;

	@GetMapping("/details/{orderId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Get order details", notes = "Returns order details data")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public OrderDetailsDto getOrderDetails(@PathVariable("orderId") long orderId) {
		return service.getOrderDetails(orderId);
	}
	
	@GetMapping("/details/{orderId}/activities")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Get order activities", notes = "Returns order activities")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public List<OrderActivityListRowDto> getOrderActivities(@PathVariable("orderId") long orderId) {
		return service.getOrderActivities(orderId);
	}
	
	@GetMapping("/details/{orderId}/files")
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Get order files", notes = "Returns order files")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public List<OrderFileDto> getOrderFiles(@PathVariable("orderId") long orderId) {
		return service.getOrderFiles(orderId);
	}
	
	@PostMapping("/details/{orderId}/files")
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Add order files", notes = "Add order files")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public List<OrderFileDto> addOrderFiles(@PathVariable("orderId") long orderId, @RequestParam(value = "files", required = false) MultipartFile[] files) {
		for (MultipartFile file : files) {
			if (!Constants.ALLOWED_FILE_TYPES.contains(file.getContentType())) {
				throw new ApiException(RC.FORBIDDEN, "Unsupported file type, supported file types are: pdf, zip and jpg.");
			}
		}
		return service.addOrderFiles(orderId, files);
	}
	
	@DeleteMapping("/details/{orderId}/file/{orderFileId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Delete order file", notes = "Delete order file")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void deleteOrderFile(@PathVariable("orderId") long orderId, @PathVariable("orderFileId") long orderFileId) {
		service.deleteOrderFile(orderFileId);
	}
	
	@PutMapping("/accept/{orderId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Accept order", notes = "Accept order")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success"),
		@ApiResponse(code = RC.CONFLICT, message = "Order already taken")
	})
	public void acceptOrder(@PathVariable("orderId") long orderId) {
		service.acceptOrder(orderId);
	}
	
	@PutMapping("/decline/{orderId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Decline order", notes = "Decline order")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void declineOrder(@PathVariable("orderId") long orderId) {
		service.declineOrderByAbstractor(orderId);
	}
	
	@PutMapping("/cancel/{orderId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value = "Cancel order", notes = "Cancels order in qualia")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void cancelOrder(@PathVariable("orderId") long orderId) {
		service.cancelOrder(orderId);
	}
	
}
