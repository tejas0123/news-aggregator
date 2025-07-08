package newsprovider;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import config.AppConfig;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import service.NotificationCreationService;

public class NewsAPIScheduler implements ServletContextListener{

	private ScheduledExecutorService scheduler;
	private NewsAPIRequest newsAPIRequest = AppConfig.getNewsAPIRequestInstance();
	private NotificationCreationService notificationCreationService = new NotificationCreationService();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {	            
        //newsAPIRequest.fetchNewsHeadlines();
        //notificationCreationService.createNotifications();
        }, 0, 4, TimeUnit.HOURS);

        System.out.println("Scheduler started at " + Instant.now());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Scheduler stopped.");
        }
    }
}


