package com.itt.newsaggregatorclient;

import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.api.articles.ArticlesMetadataUpdateHandler;
import com.itt.newsaggregatorclient.api.articles.GetSavedArticlesHandler;
import com.itt.newsaggregatorclient.api.auth.LoginHandler;
import com.itt.newsaggregatorclient.io.ArticlesIO;
import com.itt.newsaggregatorclient.io.MainMenu;

public class AppConfig {
    private static PostRequestsHandler loginHandler = null;
    private static MainMenu mainMenu = null;
    private static ArticlesIO articlesIO = null;
    private static GetSavedArticlesHandler getSavedArticlesHandler = null;
    private static ArticlesMetadataUpdateHandler articlesMetadataUpdateHandler = null;

    public static PostRequestsHandler getLoginHandler(){
        if(loginHandler == null){
            loginHandler = new LoginHandler();
        }
        return loginHandler;
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
