package pl.itcraft.appstract.admin.notifications;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.api.error.RC;

@RestController
@RequestMapping("notifications")
@Api(tags = {"notifications"}, produces = "application/json")
public class NotificationsController {

	@Autowired
	private NotificationsService service;
	
	@GetMapping("")
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Get new notifications", notes = "Returns new notifications")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public List<NotificationDto> getNewNotifications() {
		return service.getNewNotifications();
	}
	
	@PutMapping("/set-as-seen-up-to/{notificationId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyRole('ADMIN', 'ABSTRACTOR')")
	@ApiOperation(value = "Sets notifications as seen", notes = "Sets notifications as seen up to the given id")
	@ApiResponses({
		@ApiResponse(code = RC.NO_CONTENT, message = "Success")
	})
	public void setAsSeenUpToId(@PathVariable("notificationId") long notificationId) {
		service.setNotificationsAsSeenUpToId(notificationId);
	}
	
}
