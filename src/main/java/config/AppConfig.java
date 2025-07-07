package config;

import dao.ArticlesMetadataDAO;
import dao.ArticlesMetadataDAOImpl;
import dao.NewsAPIDAO;
import dao.NotificationPreferenceDAO;
import dao.NotificationPreferenceDAOImpl;
import dao.SavedArticleDAO;
import dao.SavedArticleDAOImpl;
import dao.SearchServiceDAO;
import dao.SearchServiceDAOImpl;
import dao.UserAuthenticationDAO;
import dao.UserAuthenticationDAOImpl;
import newsprovider.NewsAPIRequest;
import newsprovider.NewsAPIRequestImpl;
import service.ArticlesMetadataService;
import service.ArticlesMetadataServiceImpl;
import service.NotificationPreferenceService;
import service.NotificationPreferenceServiceImpl;
import service.SavedArticlesService;
import service.SavedArticlesServiceImpl;
import service.SearchService;
import service.SearchServiceImpl;
import service.UserAuthenticationService;
import service.UserAuthenticationServiceImpl;

public class AppConfig {
	private static UserAuthenticationDAO userAuthenticationDAO = null;
	private static UserAuthenticationService userAuthenticationService = null;
    private static NewsAPIDAO newsAPIDAO = null;
    private static NewsAPIRequest newsAPIRequest = null;
    private static SearchService searchService = null;
    private static SearchServiceDAO searchServiceDAO = null;
    private static SavedArticlesService savedArticlesService = null;
    private static SavedArticleDAO savedArticleDAO = null;
    private static ArticlesMetadataService articlesMetadataService = null;
    private static ArticlesMetadataDAO articlesMetadataDAO = null;
    private static NotificationPreferenceDAO notificationPreferenceDAO = null;
    private static NotificationPreferenceService notificationPreferenceService = null;
    
    private AppConfig() {
    	
    }
	
	public static UserAuthenticationService getUserAuthServiceInstance(){
        if(userAuthenticationDAO == null) {
        	userAuthenticationDAO = new UserAuthenticationDAOImpl();
        }
        if(userAuthenticationService == null) {
        	userAuthenticationService = new UserAuthenticationServiceImpl(userAuthenticationDAO);
        }
        return userAuthenticationService;
    };

    public static NewsAPIDAO getNewsAPIDAOInstance(){
    	if(newsAPIDAO == null) {
    		newsAPIDAO = new NewsAPIDAO();
    	}
    	return newsAPIDAO;
    }
    
    public static NewsAPIRequest getNewsAPIRequestInstance() {
    	if(newsAPIDAO == null) {
    		newsAPIDAO = new NewsAPIDAO();
    	}
    	if(newsAPIRequest == null) {
    		newsAPIRequest = new NewsAPIRequestImpl(newsAPIDAO);
    	}
    	return newsAPIRequest;
    }
    
    public static SearchService getSearchServiceInstance() {
    	if(searchServiceDAO == null) {
    		searchServiceDAO = new SearchServiceDAOImpl();
    	}
    	if(searchService == null) {
    		searchService = new SearchServiceImpl(searchServiceDAO);
    	}
    	return searchService;
    }
    
    public static SavedArticlesService getSavedArticlesServiceInstance() {
    	if(savedArticleDAO == null) {
    		savedArticleDAO = new SavedArticleDAOImpl();
    	}
    	
    	if(savedArticlesService == null) {
    		savedArticlesService = new SavedArticlesServiceImpl(savedArticleDAO);
    	}
    	return savedArticlesService;
    }
    
    public static ArticlesMetadataService getArticlesMetadataServiceInstance() {
    	if(articlesMetadataDAO == null) {
    		articlesMetadataDAO = new ArticlesMetadataDAOImpl();
    	}
    	
    	if(articlesMetadataService == null) {
    		articlesMetadataService = new ArticlesMetadataServiceImpl(articlesMetadataDAO);
    	}
    	return articlesMetadataService;
    }
    
    public static NotificationPreferenceService getNotificationPreferenceServiceInstance() {
    	if(notificationPreferenceDAO == null) {
    		notificationPreferenceDAO = new NotificationPreferenceDAOImpl();
    	}
    	
    	if(notificationPreferenceService == null) {
    		notificationPreferenceService = new NotificationPreferenceServiceImpl(notificationPreferenceDAO);
    	}
    	return notificationPreferenceService;
    }
}
