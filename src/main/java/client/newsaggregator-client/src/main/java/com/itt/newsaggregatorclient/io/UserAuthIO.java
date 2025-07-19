package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.AppConfig;
import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.api.auth.SignupHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.constants.Messages;
import com.itt.newsaggregatorclient.constants.Prompts;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Gender;
import com.itt.newsaggregatorclient.dto.UserCredentials;
import com.itt.newsaggregatorclient.dto.UserDetails;
import com.itt.newsaggregatorclient.util.SingletonScanner;
import com.itt.newsaggregatorclient.validator.InputValidator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class UserAuthIO {
    private static final String TOKEN_FILE_PATH = "token_store.txt";
    Scanner inputScanner = SingletonScanner.getScannerInstance();
    MainMenu mainMenu = AppConfig.getMainMenuInstance();

    public void startApplication(){
        boolean isLoggedIn = false;
        System.out.println("Welcome to the News Aggregator application.");
        System.out.println("______________________________________________________________");

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
                    isLoggedIn = mainMenu.displayMenu();
                }
            } else if(inputCommand.equalsIgnoreCase(Constants.SIGNUP)){
                initiateSignup();
            } else if(inputCommand.equalsIgnoreCase(Constants.EXIT)){
                System.out.println("Exiting");
                System.exit(0);
            } else{
                System.out.println(Messages.INVALID_INPUT);
            }
        }
    }

    private boolean initiateLogin(){
        UserCredentials userCredentials = getLoginCredentials();
        PostRequestsHandler loginAPIHandler = AppConfig.getLoginHandlerInstance();
        APIResponse loginResponse = loginAPIHandler.sendAPIRequest(userCredentials);
        System.out.println(loginResponse.message());
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

    private void initiateSignup() {
        UserDetails userDetails = getUserDetails();
        SignupHandler signupHandler = AppConfig.getSignupHandlerInstance();
        APIResponse response = signupHandler.sendAPIRequest(userDetails);
        if(response.success()){
            System.out.println();
            System.out.println("Signup successful. Continue to login");
            System.out.println("__________________________________________________________");
        } else{
            System.out.println(response.message());
        }
    }

    public static void logout() {
        try {
            Files.deleteIfExists(Paths.get(TOKEN_FILE_PATH));
            System.out.println("Logged out successfully.");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private UserDetails getUserDetails() {
        System.out.println(Prompts.FIRST_NAME_PROMPT);
        String firstName = inputScanner.nextLine().trim();

        System.out.println(Prompts.LAST_NAME_PROMPT);
        String lastName = inputScanner.nextLine().trim();

        String email;
        while (true) {
            System.out.println(Prompts.EMAIL_PROMPT);
            email = inputScanner.nextLine().trim();
            if (InputValidator.isEmailValid(email)) break;
            System.out.println(Messages.INVALID_EMAIL);
        }

        Gender gender = getGenderChoice();

        System.out.println(Prompts.PASSWORD_PROMPT);
        String password = inputScanner.nextLine();

        return new UserDetails(firstName, lastName, email, gender, password);
    }

    private Gender getGenderChoice() {
        while (true) {
            System.out.println("Select Gender:");
            System.out.println("1. MALE");
            System.out.println("2. FEMALE");
            System.out.println("3. OTHER");
            System.out.print("Enter choice (1-3): ");
            String choice = inputScanner.nextLine().trim();

            switch (choice) {
                case "1" -> { return Gender.MALE; }
                case "2" -> { return Gender.FEMALE; }
                case "3" -> { return Gender.OTHER; }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }
}
