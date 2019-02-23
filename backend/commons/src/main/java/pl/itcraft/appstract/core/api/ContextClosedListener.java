package pl.itcraft.appstract.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ContextClosedListener.class);
	
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		logger.info("Context closed {}", context.getId());
	}

}

