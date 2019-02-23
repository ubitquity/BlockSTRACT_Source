package pl.itcraft.appstract.core;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AppFlyway extends Flyway {
	
	private static final Logger LOG = LoggerFactory.getLogger(AppFlyway.class);

	@Autowired
	private BaseAppModuleConfig appModuleConfig;
	
	@Override
	public int migrate() throws FlywayException {
		if (appModuleConfig.migrateDbOnStartup()) {
			LOG.info("Flyway migration in module '{}' - ON", appModuleConfig.getModule());
			return super.migrate();
		} else {
			LOG.info("Flyway migration in module '{}' - OFF", appModuleConfig.getModule());
			return 0;
		}
	}
}
