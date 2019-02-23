package pl.itcraft.appstract.admin.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.core.user.UserService;

@Service
public class ApiLoginEntryMonitorJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiLoginEntryMonitorJob.class);
	
	@Autowired
	private UserService service;
	
	private boolean started;
	
	@Scheduled(cron="${app.scheduled.apiLoginEntryMonitor}")
	public void logoutExpired() {
		if (started) {
			int updatedCount = service.logoutExpired();
			LOGGER.info("Automatically logout " + updatedCount + " expired auth tokens.");
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
