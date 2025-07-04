package config;

import dao.NewsAPIDAO;
import dao.SearchServiceDAO;
import dao.SearchServiceDAOImpl;
import dao.UserAuthenticationDAO;
import dao.UserAuthenticationDAOImpl;
import newsprovider.NewsAPIRequest;
import newsprovider.NewsAPIRequestImpl;
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
}
