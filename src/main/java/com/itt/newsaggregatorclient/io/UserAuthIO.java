package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.AppConfig;
import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.constants.Messages;
import com.itt.newsaggregatorclient.constants.Prompts;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.UserCredentials;
import com.itt.newsaggregatorclient.util.SingletonScanner;
import com.itt.newsaggregatorclient.validator.InputValidator;

import java.util.Scanner;

public class UserAuthIO {
    Scanner inputScanner = SingletonScanner.scannerSupplier.get();

    public void startApplication(){
        boolean isLoggedIn = false;

        while(!isLoggedIn){
            System.out.println(Prompts.USER_AUTH_PROMPT);
            String inputCommand = inputScanner.nextLine();

            if(inputCommand.equalsIgnoreCase(Constants.LOGIN)){
                byte attempts = 3;
                while(attempts > 0 && !isLoggedIn){
                    boolean isLoginSuccessful = initiateLogin();
                    if(isLoginSuccessful){
                        isLoggedIn = true;
                    } else{
                        attempts--;
                    }
                }
                if(!isLoggedIn){
                    System.out.println(Messages.NO_MORE_ATTEMPTS);
                    System.exit(0);
                } else{
                    System.out.println(Messages.LOGIN_SUCCESSFUL);
                }
            } else if(inputCommand.equalsIgnoreCase(Constants.SIGNUP)){
                initiateSignup();
            } else{
                System.out.println(Messages.INVALID_INPUT);
            }
        }
    }

    private boolean initiateLogin(){
        UserCredentials userCredentials = getLoginCredentials();
        PostRequestsHandler loginAPIHandler = AppConfig.getLoginHandler();
        APIResponse loginResponse = loginAPIHandler.sendAPIRequest(userCredentials);
        return loginResponse.success();
    }

    private UserCredentials getLoginCredentials(){
        boolean isLoginCredentialValid = false;
        String email;
        String password;

        do{
            System.out.println(Prompts.EMAIL_PROMPT);
            email = inputScanner.nextLine();

            System.out.println(Prompts.PASSWORD_PROMPT);
            password = inputScanner.nextLine();

            if(InputValidator.isEmailValid(email)){
                isLoginCredentialValid = true;
            } else{
                System.out.println(Messages.INVALID_EMAIL);
            }

        } while(!isLoginCredentialValid);

        return new UserCredentials(email, password);
    }

    private void initiateSignup(){
        System.out.println("Starting signup");
    }
}
