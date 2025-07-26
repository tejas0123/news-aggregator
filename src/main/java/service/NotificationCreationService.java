package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import config.AppConfig;
import dao.NewsAPIDAO;
import io.jsonwebtoken.lang.Collections;
import model.NewsArticle;
import model.Notification;
import dao.NotificationsDAO;
import dao.NotificationsDAOImpl;

public class NotificationCreationService {
	NewsAPIDAO newsAPIDAO = AppConfig.getNewsAPIDAOInstance();
	NotificationPreferenceService notificationPreferenceService = AppConfig
			.getNotificationPreferenceServiceInstance();
	NotificationsService notificationsService = AppConfig.getNotificationsServiceInstance();
	NotificationsDAO notificationsDAO = new NotificationsDAOImpl();
	
	public void createNotifications() {
	    List<NewsArticle> articles = newsAPIDAO.getLatestAddedArticles();
	    Map<String, List<Integer>> categoryWiseSubscriberIds = new HashMap<>();
	    Map<String, Integer> categories = newsAPIDAO.getNewsCategories();
	    
	    for (String category : categories.keySet()) {
	        categoryWiseSubscriberIds.put(category, notificationPreferenceService.getUserIdsByCategory(category));
	    }

	    Map<String, List<NewsArticle>> categoryWiseArticles = new HashMap<>();
	    for (NewsArticle article : articles) {
	        categoryWiseArticles
	            .computeIfAbsent(article.getCategory(), k -> new ArrayList<>())
	            .add(article);
	    }

	    List<Notification> notifications = new ArrayList<>();
	    
	    for (Map.Entry<String, List<Integer>> entry : categoryWiseSubscriberIds.entrySet()) {
	        String category = entry.getKey();
	        List<Integer> userIds = entry.getValue();
	        List<NewsArticle> categoryArticles = categoryWiseArticles.getOrDefault(category, new ArrayList<>());

	        for (NewsArticle article : categoryArticles) {
	            for (Integer userId : userIds) {
	                notifications.add(new Notification(article.getArticleId(), userId));
	            }
	        }
	    }

	    if (!notifications.isEmpty()) {
	        notificationsDAO.insertNotifications(notifications);
	    }
	}

}
