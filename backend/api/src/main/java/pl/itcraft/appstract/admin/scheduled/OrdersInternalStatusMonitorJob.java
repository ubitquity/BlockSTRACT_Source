package pl.itcraft.appstract.admin.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.admin.order.fulfillment.OrderFulfillmentService;
import pl.itcraft.appstract.core.enums.OrderInternalStatus;

@Service
public class OrdersInternalStatusMonitorJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersInternalStatusMonitorJob.class);
	
	@Autowired
	private OrderFulfillmentService orderFulfillmentService;
	
	private boolean started;
	
	@Scheduled(cron = "${app.scheduled.ordersInternalStatusMonitor}")
	public void adjustOrdersInternalStatuses() {
		if (started) {
			int updatedCount = orderFulfillmentService.setUnderReviewOrdersAsInProgress();
			LOGGER.info("Automatically set " + updatedCount + " orders to " + OrderInternalStatus.IN_PROGRESS.name() + " status.");
			
			updatedCount = orderFulfillmentService.setInProgressOrdersAsOverdue();
			LOGGER.info("Automatically set " + updatedCount + " orders to " + OrderInternalStatus.OVERDUE.name() + " status.");
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
