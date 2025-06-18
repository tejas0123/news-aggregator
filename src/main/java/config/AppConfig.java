package config;

import dao.UserAuthenticationDAOImpl;
import service.UserAuthenticationServiceImpl;
import service.UserAuthenticationService;
import dao.UserAuthenticationDAO;

public class AppConfig {
	private static UserAuthenticationDAO userAuthenticationDAO = null;
	private static UserAuthenticationService userAuthenticationService = null;
	
	public static UserAuthenticationService getUserAuthServiceInstance(){
        if(userAuthenticationDAO == null) {
        	userAuthenticationDAO = new UserAuthenticationDAOImpl();
        }
        if(userAuthenticationService == null) {
        	userAuthenticationService = new UserAuthenticationServiceImpl(userAuthenticationDAO);
        }
        return userAuthenticationService;
    };
}
