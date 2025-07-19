package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.AppConfig;
import com.itt.newsaggregatorclient.api.admin.BlockWordsHandler;
import com.itt.newsaggregatorclient.api.admin.DisableCategoryHandler;
import com.itt.newsaggregatorclient.api.admin.GetAllCategoriesHandler;
import com.itt.newsaggregatorclient.api.admin.GetAllServersHandler;
import com.itt.newsaggregatorclient.api.admin.UpdateServerDetailsHandler;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Server;
import com.itt.newsaggregatorclient.dto.ServerDetails;
import com.itt.newsaggregatorclient.util.SingletonScanner;
import java.util.*;

public class AdminIO {
    Scanner inputScanner = SingletonScanner.getScannerInstance();

    public void printAdminMenu(){
        while (true){
            System.out.println("___________________________________________________________________");
            System.out.println("1. View / Update the list of external servers and status");
            System.out.println("2. Add new news category");
            System.out.println("3. Disable a news category");
            System.out.println("4. Hide articles based on words");
            System.out.println("5. Logout");
            System.out.println();

            int input = getValidChoice(1, 5);

            switch (input){
                case 1 -> updateServerDetails();
                case 2 -> addCategory();
                case 3 -> disableCategory();
                case 4 -> getWordsToBlock();
                case 5 -> {
                    UserAuthIO.logout();
                    return;
                }
            }
        }
    }

    private void updateServerDetails() {
        List<Server> servers = getExternalServers();
        if(!servers.isEmpty()){
            for(Server server : servers){
                System.out.println("Server ID: " + server.getServerId() + " | " + "Name: " + server.getName() + " | " + "api-key: " + server.getApiKey());
            }

            System.out.println();
            System.out.println("Enter 1 to update server details, or 0 to exit");
            int input = getValidChoice(0, 1);
            if(input == 0){
                return;
            } else{
                System.out.println("Enter the id of the server to update");
                int serverId = inputScanner.nextInt();
                inputScanner.nextLine();
                System.out.println("Enter updated api-key");
                String updatedApiKey = inputScanner.nextLine();

                ServerDetails updatedDetails = new ServerDetails(serverId, updatedApiKey);
                APIResponse apiResponse = new UpdateServerDetailsHandler().sendAPIRequest(updatedDetails);
                System.out.println(apiResponse.message());
                System.out.println();
            }
        }
    }

    private List<Server> getExternalServers() {
        List<Server> servers = new ArrayList<>();
        GetAllServersHandler getAllServersHandler = AppConfig.getAllServersHandler();
        APIResponse getServersResponse = getAllServersHandler.sendAPIRequest("/api/v1/admin/server");
        System.out.println(getServersResponse.message());
        System.out.println();
        if(getServersResponse.success()){
            System.out.println();
            Optional<List<Server>> serversOptional = getAllServersHandler.extractResponseData(getServersResponse.body());
            if(serversOptional.isPresent() && !serversOptional.get().isEmpty()){
                servers = serversOptional.get();
            }
        }
        return servers;
    }

    private void addCategory() {
        List<String> keywords = new ArrayList<>();
        System.out.println("Enter category name");
        String category = inputScanner.nextLine();

        while(true){
            System.out.println();
            System.out.println("Enter keywords related to category. Enter 'exit' to end");
            String keyword = inputScanner.nextLine();
            if(keyword.equalsIgnoreCase("exit")){
                break;
            }
            keywords.add(keyword);
        }

        Map<String, List<String>> categoryWithKeywordsMap = new HashMap<>();
        categoryWithKeywordsMap.put(category, keywords);

        APIResponse apiResponse = AppConfig.getAddNewCategoryHandlerInstance()
                .sendAPIRequest(categoryWithKeywordsMap);
        System.out.println(apiResponse.message());
    }

    private void disableCategory(){
        Map<Integer, String> newsCategories = getAllNewsCategories();
        if(!newsCategories.isEmpty()){
            System.out.println();
            System.out.println("Choose category id to disable: ");
            System.out.println();

            newsCategories.forEach((id, category) ->
                    System.out.println(id + ". " + category)
            );

            int input = inputScanner.nextInt();
            DisableCategoryHandler disableCategoryHandler = AppConfig.getDisableCategoryHandlerInstance();
            APIResponse apiResponse = disableCategoryHandler.sendAPIRequest(Set.of(input));
            System.out.println(apiResponse.message());
        } else{
            System.out.println("No categories found");
        }
    }

    private void getWordsToBlock() {
        Set<String> wordsToBlock = new HashSet<>();

        while(true){
            System.out.println();
            System.out.println("Enter words to block. Enter 'exit' to end");
            String keyword = inputScanner.nextLine();
            if(keyword.equalsIgnoreCase("exit")){
                break;
            }
            wordsToBlock.add(keyword);
        }

        BlockWordsHandler blockWordsHandler = AppConfig.getBlockWordsHandlerInstance();
        APIResponse apiResponse = blockWordsHandler.sendAPIRequest(wordsToBlock);
        System.out.println(apiResponse.message());
        System.out.println();
    }

    public Map<Integer, String> getAllNewsCategories(){
        GetAllCategoriesHandler getAllCategoriesHandler = AppConfig.getAllCategoriesHandlerInstance();
        APIResponse apiResponse = getAllCategoriesHandler.sendAPIRequest("/api/v1/admin/categories");
        Optional<Map<String, Integer>> categoriesMapOptional = getAllCategoriesHandler.extractResponseData(apiResponse.body());
        Map<String, Integer> categoriesMap = categoriesMapOptional.get();
        return (categoriesMap.isEmpty()) ? new HashMap<>() : sortNewsCategoriesById(categoriesMap);
    }

    private Map<Integer, String> sortNewsCategoriesById(Map<String, Integer> categoriesMap){
        return categoriesMap
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(
                    LinkedHashMap::new,
                    (map, entry) -> map.put(entry.getValue(), entry.getKey()),
                    LinkedHashMap::putAll
            );
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
