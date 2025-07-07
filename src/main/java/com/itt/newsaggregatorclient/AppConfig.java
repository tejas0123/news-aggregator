package com.itt.newsaggregatorclient;

import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.api.articles.ArticlesMetadataUpdateHandler;
import com.itt.newsaggregatorclient.api.articles.GetSavedArticlesHandler;
import com.itt.newsaggregatorclient.api.auth.LoginHandler;
import com.itt.newsaggregatorclient.api.auth.SignupHandler;
import com.itt.newsaggregatorclient.io.ArticlesIO;
import com.itt.newsaggregatorclient.io.MainMenu;

public class AppConfig {
    private static PostRequestsHandler loginHandler = null;
    private static MainMenu mainMenu = null;
    private static ArticlesIO articlesIO = null;
    private static GetSavedArticlesHandler getSavedArticlesHandler = null;
    private static ArticlesMetadataUpdateHandler articlesMetadataUpdateHandler = null;
    private static SignupHandler signupHandler = null;

    public static PostRequestsHandler getLoginHandlerInstance(){
        if(loginHandler == null){
            loginHandler = new LoginHandler();
        }
        return loginHandler;
    }

    public static SignupHandler getSignupHandlerInstance(){
        if(signupHandler == null){
            signupHandler = new SignupHandler();
        }
        return signupHandler;
    }

    public static MainMenu getMainMenuInstance(){
        if(mainMenu == null){
            mainMenu = new MainMenu();
        }
        return mainMenu;
    }

    public static ArticlesIO getArticlesIOInstance() {
        if(articlesIO == null){
            articlesIO = new ArticlesIO();
        }
        return articlesIO;
    }

    public static GetSavedArticlesHandler getSaveArticlesHandlerInstance(){
        if(getSavedArticlesHandler == null){
            getSavedArticlesHandler = new GetSavedArticlesHandler();
        }
        return getSavedArticlesHandler;
    }

    public static ArticlesMetadataUpdateHandler getMetadataUpdateHandlerInstance(){
        if(articlesMetadataUpdateHandler == null){
            articlesMetadataUpdateHandler = new ArticlesMetadataUpdateHandler();
        }
        return articlesMetadataUpdateHandler;
    }
}
