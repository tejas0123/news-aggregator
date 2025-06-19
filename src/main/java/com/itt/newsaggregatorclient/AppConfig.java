package com.itt.newsaggregatorclient;

import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.api.auth.LoginHandler;

public class AppConfig {
    private static PostRequestsHandler loginHandler = null;

    public static PostRequestsHandler getLoginHandler(){
        if(loginHandler == null){
            loginHandler = new LoginHandler();
        }
        return loginHandler;
    }
}
