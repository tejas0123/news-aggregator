package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.AppConfig;
import com.itt.newsaggregatorclient.api.articles.GetSavedArticlesHandler;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.util.JwtUtil;
import com.itt.newsaggregatorclient.util.SingletonScanner;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu {
    Scanner inputScanner = SingletonScanner.getScannerInstance();
    ArticlesIO articlesIO = AppConfig.getArticlesIOInstance();
    public void displayMenu(){
        try{
            Map<String, String> userDetails = getUserDetails();
            if(userDetails.isEmpty()){
                System.out.println("Token expired. Please login again");
                return;
            }

            System.out.println("Welcome to the News Aggregator application " + LocalDate.now());

            if(userDetails.get("role").equals("USER")){
                userMenu(userDetails);
            } else{
                adminMenu(userDetails);
            }
        } catch (ExpiredJwtException expiredJwtException){
            System.out.println("Token expired. Please login again");
        }
    }

    private void userMenu(Map<String, String> userDetails){
        while (true) {
            System.out.println("\nPlease choose from the options below:");
            System.out.println("1. Headlines");
            System.out.println("2. Saved Articles");
            System.out.println("3. Search");
            System.out.println("4. Notifications");
            System.out.println("5. Logout");

            try {
                int input = Integer.parseInt(inputScanner.nextLine().trim());
                if (handleUserChoice(input, userDetails)) {
                    break;
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
            }
        }
    }

    private boolean handleUserChoice(int input, Map<String, String> userDetails) {
        switch (input) {
            case 1:
                articlesIO.searchArticles("headlines");
                break;
            case 2:
                break;
            case 3:
                articlesIO.searchArticles("keywordSearch");
                break;
            case 4:
                articlesIO.getUserSavedArticles();
                break;
            case 5:
                return true;
            default:
                System.out.println("Invalid choice. Please select a valid option (1-5).");
        }
        return false;
    }

    private void adminMenu(Map<String, String> userDetails){

    }

    private Map<String, String> getUserDetails(){
        Optional<String> token = JwtUtil.getToken();
        if(token.isPresent()){
            Claims claims = JwtUtil.validateTokenAndGetSubject(token.get());
            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("userId", claims.get("userId", Integer.class).toString());
            userDetails.put("role", claims.get("role", String.class));
            return userDetails;
        } else {
            return new HashMap<>();
        }
    }
}
