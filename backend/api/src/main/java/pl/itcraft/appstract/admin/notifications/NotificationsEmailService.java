package pl.itcraft.appstract.admin.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.core.entities.Notification;
import pl.itcraft.appstract.core.mail.MailDto;
import pl.itcraft.appstract.core.mail.MailerService;

@Service
public class NotificationsEmailService {

	@Autowired
	private MailerService mailerService;
	
	@Async("NOTIFICATION_EMAIL_EXECUTOR")
	public void sendNotificationEmailAsync(Notification notification) {
		MailDto mailDto = new MailDto()
			.withBodyTemplate("notification.html")
			.asHtml(true)
			.withSubject("Appstract - " + notification.getContent())
			.withRecipient(notification.getUser().getEmail())
			.withModel("message", notification.getContent());
		mailerService.sendMailMessage(mailDto);
	}
	
}
