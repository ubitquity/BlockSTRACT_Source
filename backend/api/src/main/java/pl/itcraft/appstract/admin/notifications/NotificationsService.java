package pl.itcraft.appstract.admin.notifications;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.core.entities.Notification;
import pl.itcraft.appstract.core.utils.UtilsBean;

@Service
@Transactional
public class NotificationsService {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private NotificationsEmailService notificationsEmailService;
	
	public void sendNotification(Notification notification) {
		em.persist(notification);
		if (notification.getUser().isNotifications()) {
			notificationsEmailService.sendNotificationEmailAsync(notification);
		}
	}
	
	public List<NotificationDto> getNewNotifications() {
		return em.createQuery("SELECT n FROM Notification n WHERE n.user = :user AND n.seen = FALSE ORDER BY n.id DESC", Notification.class)
			.setParameter("user", utilsBean.getCurrentUser())
			.getResultList()
			.stream()
			.map(NotificationDto::new)
			.collect(Collectors.toList());
	}

	public void setNotificationsAsSeenUpToId(long notificationId) {
		em.createQuery("UPDATE Notification n SET n.seen = TRUE WHERE n.id <= :notificationId AND n.user = :user")
			.setParameter("notificationId", notificationId)
			.setParameter("user", utilsBean.getCurrentUser())
			.executeUpdate();
	}
	
}
