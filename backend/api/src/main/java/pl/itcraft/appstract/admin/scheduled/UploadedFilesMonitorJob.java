package pl.itcraft.appstract.admin.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.core.file.UploadedFilesWriterService;

@Service
public class UploadedFilesMonitorJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadedFilesMonitorJob.class);
	
	@Autowired
	private UploadedFilesWriterService filesService;
	
	private boolean started;
	
	@Scheduled(cron = "${app.scheduled.uploadedFilesMonitor}")
	public void deleteOldTempPhotos() {
		if (started) {
			int deletedCount = filesService.cleanupFiles();
			LOGGER.info("Automatically removed " + deletedCount + " old temp files.");
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
