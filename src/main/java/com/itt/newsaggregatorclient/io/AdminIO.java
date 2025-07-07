package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.AppConfig;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.util.SingletonScanner;
import java.util.*;

public class AdminIO {
    Scanner inputScanner = SingletonScanner.getScannerInstance();
    public void printAdminMenu(){
        System.out.println("1. View the list of external servers and status");
        System.out.println("2. Update/Edit the external server’s details");
        System.out.println("3. Add new news category");
        System.out.println("4. Logout");

        int input = getValidChoice(1, 4);

        switch (input){
            case 1 -> getExternalServers();
            case 2 -> updateServerdetails();
            case 3 -> addCategory();
            case 4 -> disableCategory();
            case 5 -> {
                return;
            }
        }
    }

    private void getExternalServers() {
    }

    private void updateServerdetails() {
    }

    private void addCategory() {
        List<String> keywords = new ArrayList<>();
        System.out.println("Enter category name");
        String category = inputScanner.nextLine();

        while(true){
            System.out.println("Enter keywords related to category. Enter 'exit' to end");
            String keyword = inputScanner.nextLine();
            if(keyword.equalsIgnoreCase("exit")){
                break;
            }
            keywords.add(keyword);
        }

        Map<String, List<String>> categoryWithKeywordsMap = new HashMap<>();
        categoryWithKeywordsMap.put(category, keywords);

        APIResponse apiResponse = AppConfig.getAddNewCategoryHandlerInstance().sendAPIRequest(categoryWithKeywordsMap);
        System.out.println(apiResponse.message());
    }

    private void disableCategory(){
        Map<String, Integer> newsCategories = getAllNewsCategories();
        System.out.println("Choose category id to disable: ");

        for(Map.Entry<String, Integer> entry : newsCategories.entrySet()){
            System.out.println(entry.getValue() + ". " + entry.getKey());
        }

        int input = getValidChoice(1, newsCategories.size());
        APIResponse apiResponse = AppConfig.getDisableCategoryHandlerInstance().sendAPIRequest(input);
        System.out.println(apiResponse);
    }

    private Map<String, Integer> getAllNewsCategories(){

    }

    private int getValidChoice(int min, int max) {
        while (true) {
            System.out.print("Enter your choice: ");
            String input = inputScanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
            }
        }
    }
}
