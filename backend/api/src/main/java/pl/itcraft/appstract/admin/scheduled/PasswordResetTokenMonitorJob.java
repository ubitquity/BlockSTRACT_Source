package pl.itcraft.appstract.admin.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.admin.password.PasswordResetService;

@Service
public class PasswordResetTokenMonitorJob implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetTokenMonitorJob.class);
	
	@Autowired
	private PasswordResetService passwordResetService;
	
	private boolean started;
	
	@Scheduled(cron = "${app.scheduled.passwordResetTokenMonitor}")
	public void monitorPasswordResetTonens() {
		if (started) {
			int deletedTokens = passwordResetService.deleteExpiredTokens();
			LOGGER.info("Automatically removed " + deletedTokens + " expired password reset tokens.");
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() != null) {
			started = true;
			LOGGER.info("Job initialized");
		}
	}
	
}
