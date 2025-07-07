package com.itt.newsaggregatorclient;

import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.api.admin.AddNewCategoryHandler;
import com.itt.newsaggregatorclient.api.admin.DisableCategoryHandler;
import com.itt.newsaggregatorclient.api.articles.ArticlesMetadataUpdateHandler;
import com.itt.newsaggregatorclient.api.articles.GetSavedArticlesHandler;
import com.itt.newsaggregatorclient.api.articles.PostUserSavedArticlesHandler;
import com.itt.newsaggregatorclient.api.articles.UnsaveArticlesHandler;
import com.itt.newsaggregatorclient.api.auth.LoginHandler;
import com.itt.newsaggregatorclient.api.auth.SignupHandler;
import com.itt.newsaggregatorclient.io.AdminIO;
import com.itt.newsaggregatorclient.io.ArticlesIO;
import com.itt.newsaggregatorclient.io.MainMenu;
import com.itt.newsaggregatorclient.io.SavedArticlesIO;

public class AppConfig {
    private static PostRequestsHandler loginHandler = null;
    private static MainMenu mainMenu = null;
    private static ArticlesIO articlesIO = null;
    private static PostUserSavedArticlesHandler postUserSavedArticlesHandler = null;
    private static ArticlesMetadataUpdateHandler articlesMetadataUpdateHandler = null;
    private static SignupHandler signupHandler = null;
    private static GetSavedArticlesHandler savedArticlesHandler = null;
    private static SavedArticlesIO savedArticlesIO = null;
    private static UnsaveArticlesHandler unsaveArticlesHandler = null;
    private static AdminIO adminIO = null;
    private static AddNewCategoryHandler addNewCategoryHandler = null;
    private static DisableCategoryHandler disableCategoryHandler = null;

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

    public static PostUserSavedArticlesHandler getUserSavedArticlesHandler(){
        if(postUserSavedArticlesHandler == null){
            postUserSavedArticlesHandler = new PostUserSavedArticlesHandler();
        }
        return postUserSavedArticlesHandler;
    }

    public static ArticlesMetadataUpdateHandler getMetadataUpdateHandlerInstance(){
        if(articlesMetadataUpdateHandler == null){
            articlesMetadataUpdateHandler = new ArticlesMetadataUpdateHandler();
        }
        return articlesMetadataUpdateHandler;
    }

    public static GetSavedArticlesHandler getSavedArticlesHandlerInstance(){
        if(savedArticlesHandler == null){
            savedArticlesHandler = new GetSavedArticlesHandler();
        }
        return savedArticlesHandler;
    }

    public static SavedArticlesIO getSavedArticlesIOInstance(){
        if (savedArticlesIO == null) {
            savedArticlesIO = new SavedArticlesIO();
        }
        return savedArticlesIO;
    }

    public static UnsaveArticlesHandler getUnsaveArticlesHandlerInstance(){
        if(unsaveArticlesHandler == null){
            unsaveArticlesHandler = new UnsaveArticlesHandler();
        }
        return unsaveArticlesHandler;
    }

    public static AdminIO getAdminIOInstance(){
        if(adminIO == null){
            adminIO = new AdminIO();
        }
        return adminIO;
    }

    public static AddNewCategoryHandler getAddNewCategoryHandlerInstance(){
        if(addNewCategoryHandler == null){
            addNewCategoryHandler = new AddNewCategoryHandler();
        }
        return addNewCategoryHandler;
    }

    public static DisableCategoryHandler getDisableCategoryHandlerInstance(){
        if(disableCategoryHandler == null){
            disableCategoryHandler = new DisableCategoryHandler();
        }
        return disableCategoryHandler;
    }
}
