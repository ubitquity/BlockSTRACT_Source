package pl.itcraft.appstract.admin.inbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.admin.Constants;
import pl.itcraft.appstract.admin.orders.OrdersService;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ListDto;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.utils.UtilsBean;

@RestController
@Api(tags = {"inbox"}, produces = "application/json")
public class InboxController {

	@Autowired
	private InboxService service;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private UtilsBean utilsBean;
	
	@GetMapping("inbox/conversations")
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Get conversations list", notes = "Returns all conversations as list")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public ListDto<InboxConversationDto> getConversations() {
		User loggedUser = utilsBean.getCurrentUser();
		if(loggedUser.getRole().equals(UserRole.ABSTRACTOR)) {
			return new ListDto<>(service.getConversationsByAbstractor(loggedUser));
		}
		return new ListDto<>(service.getConversations());
	}
	
	@GetMapping("inbox/conversation/{orderId}/messages")
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Get conversation messages", notes = "Returns all messages for conversation")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public InboxConversationWithMessagesDto getConversationMessages(@PathVariable("orderId") Long orderId) {
		User loggedUser = utilsBean.getCurrentUser();
		if(loggedUser.getRole().equals(UserRole.ABSTRACTOR)) {
			ordersService.getOne(orderId, loggedUser);
		}
		return service.getConversationWithMessages(orderId);
	}
	
	@PostMapping("inbox/conversation/{orderId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Send message in a conversation", notes = "Returns new message")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public InboxMessageDto sendMessage(@PathVariable("orderId") Long orderId, @RequestParam(value = "content", required = true) String content, @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
		if (attachments != null) {
			for (MultipartFile attachment : attachments) {
				if (!Constants.ALLOWED_FILE_TYPES.contains(attachment.getContentType())) {
					throw new ApiException(RC.FORBIDDEN, "Unsupported file type, supported file types are: pdf, zip and jpg.");
				}
			}
		}
		return service.sendMessage(orderId, content, attachments);
	}
	
}
