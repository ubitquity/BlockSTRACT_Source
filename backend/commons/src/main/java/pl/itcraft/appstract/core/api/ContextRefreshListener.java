package pl.itcraft.appstract.core.api;

import java.util.Locale;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.support.XmlWebApplicationContext;

import pl.itcraft.appstract.core.BaseAppModuleConfig;
import pl.itcraft.appstract.core.file.UploadedFilesReaderService;
import pl.itcraft.appstract.core.utils.UtilsBean;

public class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ContextRefreshListener.class);
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private UploadedFilesReaderService filesService;
	
	@Autowired
	private BaseAppModuleConfig config;
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		logger.info("Context refreshed: {}", contextSting(context));
		for (String configLocation : ((XmlWebApplicationContext) context).getConfigLocations()) {
			logger.info(" config: {}", configLocation);
		}
		
		if (context.getParent() != null) {
			String contextPath = context.getApplicationName();
			logger.info("App module: {}:{}", config.getModule(), contextPath);
			logger.info("Profile: {}", utilsBean.getProfile());
			logger.info("Version: {}", utilsBean.getApplicationVersion());
			logger.info("Upload dir: {}", filesService.getUploadDir());
			logger.info("JVM locale: '{}'", Locale.getDefault());
			logger.info("JVM timezone: '{}'", TimeZone.getDefault().getID());
			final String[] dbOptions = {
				"timezone",
				//"max_connections",
				//"shared_buffers",
				//"temp_buffers",
				"client_encoding",
				//"lc_ctype",
				//"lc_collate"
			};
			for (String dbo : dbOptions) {
				logger.info("DB {}: '{}'", dbo, em.createNativeQuery("SHOW "+dbo).getSingleResult());
			}
			logger.info("===");
			logger.info("=== App module {}:{} ready!", config.getModule(), contextPath);
			logger.info("===");
		}
	}
	
	private String contextSting(ApplicationContext context) {
		if (context == null) {
			return "null";
		}
		return String.format(
			"%s:/%s (parent %s)",
			context.getClass().getSimpleName(),
			context.getId().replaceAll("[.a-zA-Z0-9]+\\:/", ""),
			context.getParent() != null ? contextSting(context.getParent()) : "null"
		);
	}

}

