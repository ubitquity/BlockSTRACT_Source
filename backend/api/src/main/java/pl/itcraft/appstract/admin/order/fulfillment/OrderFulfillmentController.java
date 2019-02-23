package pl.itcraft.appstract.admin.order.fulfillment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import pl.itcraft.appstract.core.validation.AppValidator;

@RestController
@RequestMapping("order/fulfillment")
@Api(tags = {"order fulfillment"}, produces = "application/json")
public class OrderFulfillmentController {
	
	@Autowired
	private OrderFulfillmentService service;
	
	@Autowired
	private AppValidator validator;

	@GetMapping("/{orderId}")
	@PreAuthorize("hasAnyRole('ABSTRACTOR', 'ADMIN')")
	@ApiOperation(value = "Get order fulfillment data", notes = "Returns order fulfillment data")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public OrderFulfillmentDataDto getOrderFulfillmentData(@PathVariable("orderId") long orderId) {
		return service.getOrderFulfillmentData(orderId);
	}
	
	@PutMapping("/{orderId}")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Fill order fulfillment data", notes = "Adds order fulfillment data")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void fillOrderFulfillmentData(@PathVariable("orderId") long orderId, @RequestBody OrderFulfillmentFormDto dto) {
		validator.validate(dto);
		service.fillOrderFulfillmentData(orderId, dto);
	}
	
	@PostMapping("/{orderId}/deed-files")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Add order deed files", notes = "Uploads new deed files")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public List<DeedFileDto> addOrderDeedFiles(@PathVariable("orderId") long orderId, @RequestParam(value = "deedFiles", required = true) MultipartFile[] deedFiles) {
		for (MultipartFile deedFile : deedFiles) {
			if (!Constants.ALLOWED_FILE_TYPES.contains(deedFile.getContentType())) {
				throw new ApiException(RC.FORBIDDEN, "Unsupported file type, supported file types are: pdf, zip and jpg.");
			}
		}
		return service.addOrderDeedFiles(orderId, deedFiles);
	}

	@DeleteMapping("/{orderId}/deed-file/{deedFileId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Delete deed file", notes = "Deletes an existing deed file")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public void deleteDeedFile(@PathVariable("orderId") long orderId, @PathVariable("deedFileId") long deedFileId) {
		service.deleteDeedFile(orderId, deedFileId);
	}
	
	@PostMapping("/{orderId}/title-search-document-file")
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Change order title search document file", notes = "Uploads a new title search document file and removes the existing one")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public TitleSearchDocumentDto changeTitleSearchDocumentFile(@PathVariable("orderId") long orderId, @RequestParam(value = "titleSearchDocumentFile", required = true) MultipartFile titleSearchDocumentFile) {
		if (!Constants.ALLOWED_FILE_TYPES.contains(titleSearchDocumentFile.getContentType())) {
			throw new ApiException(RC.FORBIDDEN, "Unsupported file type, supported file types are: pdf, zip and jpg.");
		}
		return service.changeTitleSearchDocumentFile(orderId, titleSearchDocumentFile);
	}
	
	@DeleteMapping("/{orderId}/title-search-document-file")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ApiOperation(value = "Delete order title search document file", notes = "Deletes the existing title search document file")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public void deleteTitleSearchDocumentFile(@PathVariable("orderId") long orderId) {
		service.deleteTitleSearchDocumentFile(orderId);
	}
	
	@PreAuthorize("hasAnyRole('ABSTRACTOR')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PutMapping("/{orderId}/submit-for-approval")
	@ApiOperation(value = "Submit order for approval", notes = "Submits the order for admin approval")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void submitForApproval(@PathVariable("orderId") long orderId) {
		service.submitForApproval(orderId);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PutMapping("/{orderId}/set-as-incomplete")
	@ApiOperation(value = "Sets order as incomplete", notes = "Sets the order as incomplete")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void setAsIncomplete(@PathVariable("orderId") long orderId) {
		service.setAsIncomplete(orderId);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PutMapping("/{orderId}/set-as-accepted")
	@ApiOperation(value = "Sets order as accepted", notes = "Sets the order as accepted and rates the abstractor")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void setAsAccepted(@PathVariable("orderId") long orderId, @RequestBody OrderRatingDto dto) {
		validator.validate(dto);
		service.setAsAccepted(orderId, dto.getRate());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PutMapping("/{orderId}/set-as-paid")
	@ApiOperation(value = "Sets order as paid", notes = "Sets the order as paid")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void setAsPaid(@PathVariable("orderId") long orderId) {
		service.setAsPaid(orderId);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PutMapping("/{orderId}/recall")
	@ApiOperation(value = "Recall order from abstractor", notes = "Recalls order from abstractor and puts it back in open orders list and rates the abstractor")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void recall(@PathVariable("orderId") long orderId, @RequestBody OrderRatingDto dto) {
		validator.validate(dto);
		service.recall(orderId, dto.getRate());
	}
	
}
