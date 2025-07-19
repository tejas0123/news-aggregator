package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.AppConfig;
import com.itt.newsaggregatorclient.api.notifications.AddPreferenceHandler;
import com.itt.newsaggregatorclient.api.notifications.DeletePreferenceHandler;
import com.itt.newsaggregatorclient.api.notifications.GetUserNotificationsHandler;
import com.itt.newsaggregatorclient.api.notifications.GetUserPreferencesHandler;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.NotificationDTO;
import com.itt.newsaggregatorclient.util.SingletonScanner;

import java.util.*;

public class NotificationsIO {
    Scanner inputScanner = SingletonScanner.getScannerInstance();
    AdminIO adminIO = AppConfig.getAdminIOInstance();
    AddPreferenceHandler addPreferenceHandler = AppConfig.getAddPreferenceHandlerInstance();
    GetUserPreferencesHandler getUserPreferencesHandler = AppConfig.getUserPreferencesHandlerInstance();
    DeletePreferenceHandler deletePreferenceHandler = AppConfig.getDeletePreferenceHandlerInstance();
    GetUserNotificationsHandler getUserNotificationsHandler = AppConfig.getUserNotificationsHandlerInstance();

    public void displayNotificationsMenu(){
        System.out.println("Choose from the options below: ");
        System.out.println("1. View notifications");
        System.out.println("2. My notification preferences");
        System.out.println("3. Add new notification preference");
        System.out.println("4. Disable current notification preference");
        System.out.println("5. Go back");

        int input = getValidChoice(1, 5);

        switch (input){
            case 1 -> displayNotifications();
            case 2 -> displayNotificationPreferences();
            case 3 -> addNotificationPreference();
            case 4 -> disableNotificationPreference();
            case 5 -> {return;}
        }
    }

    private void displayNotifications() {
        APIResponse getNotificationsResponse = getUserNotificationsHandler.sendAPIRequest("/api/v1/notification");
        System.out.println(getNotificationsResponse.message());
        if(getNotificationsResponse.success()){
            Optional<List<NotificationDTO>> notificationsOptional = getUserNotificationsHandler
                    .extractResponseData(getNotificationsResponse.body());

            if(notificationsOptional.isEmpty()){
                System.out.println("Notifications not fetched");
            } else{
                List<NotificationDTO> notifications = notificationsOptional.get();
                if(notifications.isEmpty()){
                    System.out.println("No notifications to view");
                } else{
                    printNotifications(notifications);
                }
            }
        }
    }

    private void printNotifications(List<NotificationDTO> notifications){
        for(NotificationDTO notification : notifications){
            System.out.println("_____________________________________________________________________________________");
            System.out.println(notification.title());
            System.out.println(notification.url());
            System.out.println("_____________________________________________________________________________________");
        }
    }

    private void displayNotificationPreferences() {
        Map<Integer, String> userPreferencesMap = getUserPreferences();
        printUserPreferences(userPreferencesMap);
    }

    public void addNotificationPreference(){
        Map<Integer, String> newsCategories = adminIO.getAllNewsCategories();
        System.out.println();
        if(!newsCategories.isEmpty()){
            newsCategories.forEach((category, id) ->
                    System.out.println(id + ". " + category)
            );
        }

        System.out.println();
        int categoryId;

        while(true){
            System.out.println("Choose category id to enable notifications: ");
            categoryId = inputScanner.nextInt();
            inputScanner.nextLine();
            if(!newsCategories.containsKey(categoryId)){
                System.out.println("Invalid category Id. Select from list");
            } else{
                break;
            }
        }
        Set<String> newPreferences = Set.of(newsCategories.get(categoryId));
        APIResponse apiResponse = addPreferenceHandler.sendAPIRequest(newPreferences);
        System.out.println(apiResponse.message());
    }

    private void disableNotificationPreference() {
        Map<Integer, String> userPreferencesMap = getUserPreferences();
        printUserPreferences(userPreferencesMap);

        int categoryId;
        while(true){
            System.out.println("Select the category to disable notifications for: ");
            categoryId = inputScanner.nextInt();
            inputScanner.nextLine();
            if(!userPreferencesMap.containsKey(categoryId)){
                System.out.println("Invalid categoryId");
            } else {
                break;
            }
        }

        APIResponse deletePreferenceResponse = deletePreferenceHandler.sendAPIRequest(userPreferencesMap.get(categoryId));
        System.out.println(deletePreferenceResponse.message());
    }

    private Map<Integer, String> getUserPreferences(){
        Set<String> userPreferences = new HashSet<>();
        APIResponse getUserPreferencesResponse = getUserPreferencesHandler.sendAPIRequest("/api/v1/notification/preference");
        System.out.println(getUserPreferencesResponse.message());
        if(getUserPreferencesResponse.success()){
            Optional<Set<String>> userPreferencesOptional = getUserPreferencesHandler.extractResponseData(getUserPreferencesResponse.body());
            if(userPreferencesOptional.isEmpty()){
                System.out.println("No preferences fetched");
                return new HashMap<>();
            } else{
                userPreferences = userPreferencesOptional.get();
                Map<Integer, String> userPreferencesMap = new HashMap<>();
                int categoryId = 1;
                for(String category : userPreferences){
                    userPreferencesMap.put(categoryId++, category);
                }
                return userPreferencesMap;
            }
        } else{
            return new HashMap<>();
        }
    }

    private void printUserPreferences(Map<Integer, String> userPreferencesMap){
        if(userPreferencesMap.isEmpty()){
            System.out.println("No preferences set");
        } else{
            System.out.println();
            System.out.println("Notifications set for these categories");
            userPreferencesMap.forEach((id, category) ->
                    System.out.println(id + ". " + category)
            );
        }
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
