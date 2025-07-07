package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.AppConfig;
import com.itt.newsaggregatorclient.api.articles.ArticlesMetadataUpdateHandler;
import com.itt.newsaggregatorclient.api.articles.GetHeadlinesHandler;
import com.itt.newsaggregatorclient.api.articles.GetSavedArticlesHandler;
import com.itt.newsaggregatorclient.api.articles.PostUserSavedArticlesHandler;
import com.itt.newsaggregatorclient.api.articles.UnsaveArticlesHandler;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.ArticleFilterParams;
import com.itt.newsaggregatorclient.dto.ArticleMetadata;
import com.itt.newsaggregatorclient.dto.NewsArticleData;
import com.itt.newsaggregatorclient.util.SingletonScanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ArticlesIO {
    Scanner inputScanner = SingletonScanner.getScannerInstance();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    ArticleUserInteractionIO userInteractionIO = new ArticleUserInteractionIO();
    PostUserSavedArticlesHandler postUserSavedArticlesHandler = AppConfig.getUserSavedArticlesHandler();
    ArticlesMetadataUpdateHandler articlesMetadataUpdateHandler = AppConfig.getMetadataUpdateHandlerInstance();
    GetSavedArticlesHandler getSavedArticlesHandler = AppConfig.getSavedArticlesHandlerInstance();
    SavedArticlesIO savedArticlesIO = AppConfig.getSavedArticlesIOInstance();
    UnsaveArticlesHandler unsaveArticlesHandler = AppConfig.getUnsaveArticlesHandlerInstance();

    public void searchArticles(String searchEntity) {
        LocalDate fromDate = null;
        LocalDate toDate = null;
        String category = "";
        Optional<String> keyword = Optional.empty();

        if (searchEntity.equalsIgnoreCase("headlines")) {
            System.out.println("Please choose from the options below");
            System.out.println("1. Today");
            System.out.println("2. Date Range");
            System.out.println("3. Go Back");

            int choice = getValidChoice(1, 3);

            switch (choice) {
                case 1 -> {
                    fromDate = LocalDate.now().minusDays(1);
                    toDate = LocalDate.now();
                }
                case 2 -> {
                    fromDate = readDate("Enter start date in (yyyy-MM-dd) format: ");
                    toDate = readDate("Enter end date in (yyyy-MM-dd) format: ");
                }
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("\nPlease choose the category for Headlines:");
            System.out.println("1. All");
            System.out.println("2. Business");
            System.out.println("3. Entertainment");
            System.out.println("4. Sports");
            System.out.println("5. Technology");

            int categoryChoice = getValidChoice(1, 5);

            category = switch (categoryChoice) {
                case 1 -> "business,entertainment,sports,technology";
                case 2 -> "business";
                case 3 -> "entertainment";
                case 4 -> "sports";
                case 5 -> "technology";
                default -> "business,entertainment,sports,technology";
            };
        } else if (searchEntity.equalsIgnoreCase("keywordSearch")) {
            fromDate = readDate("Enter start date in (yyyy-MM-dd) format: ");
            toDate = readDate("Enter end date in (yyyy-MM-dd) format: ");

            System.out.print("Enter keyword to search articles: ");
            String keywordInput = inputScanner.nextLine().trim();
            if (!keywordInput.isEmpty()) {
                keyword = Optional.of(keywordInput);
            }
        } else {
            System.out.println("Invalid mode. Exiting...");
            return;
        }

        ArticleFilterParams params = new ArticleFilterParams(
                Optional.ofNullable(fromDate),
                Optional.ofNullable(toDate),
                searchEntity.equalsIgnoreCase("headlines") ? Optional.of(category) : Optional.empty(),
                keyword
        );

        GetHeadlinesHandler handler = new GetHeadlinesHandler();
        APIResponse response = handler.sendAPIRequest(params);
        System.out.println(response);

        if (response.success()) {
            Optional<List<NewsArticleData>> articlesOpt = handler.extractResponseData(response.body());

            if (articlesOpt.isPresent()) {
                List<NewsArticleData> articles = articlesOpt.get();
                if (articles.isEmpty()) {
                    System.out.println("No articles found.");
                } else {
                    userInteractionIO.printArticles(0, articles);
                    updateArticlesData();
                }
            } else {
                System.out.println("Failed to parse article data. No articles found");
            }
        } else {
            System.out.println(response.message());
        }
    }

    public void getUserSavedArticles(){
        APIResponse apiResponse = getSavedArticlesHandler.sendAPIRequest("");
        if (apiResponse.success()) {
            getSavedArticlesHandler.extractResponseData(apiResponse.body())
                .ifPresentOrElse(articles -> {
                    if (articles.isEmpty()) {
                        System.out.println("No articles found.");
                    } else {
                        savedArticlesIO.printSavedArticles(0, articles);
                        unsaveArticles();
                    }
                }, () -> System.out.println("Failed to parse article data. No articles found"));
        } else {
            System.out.println(apiResponse.message());
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
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
            }
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = inputScanner.nextLine();
            try {
                return LocalDate.parse(input, dateFormatter);
            } catch (Exception exception) {
                System.out.println("Invalid date format. Please enter in yyyy-MM-dd format.");
            }
        }
    }

    private void updateArticlesData(){
        Set<Integer> userSavedArticleIds = userInteractionIO.getUserSavedArticleIds();
        if(!userSavedArticleIds.isEmpty()){
            APIResponse saveArticlesResponse = postUserSavedArticlesHandler.sendAPIRequest(userSavedArticleIds);
            System.out.println(saveArticlesResponse.message());
        }

        updateArticlesMetadata(userInteractionIO.getModifiedMetadata());
    }

    private void unsaveArticles(){
        Set<Integer> unsavedArticleIds = savedArticlesIO.getSavedArticlesToRemove();
        if(!unsavedArticleIds.isEmpty()){
            APIResponse unsaveArticlesResponse = unsaveArticlesHandler.sendAPIRequest(unsavedArticleIds);
            System.out.println(unsaveArticlesResponse.message());
        }

        updateArticlesMetadata(savedArticlesIO.getModifiedMetadata());
    }

    private void updateArticlesMetadata(List<ArticleMetadata> updatedArticlesMetadata){
        if(!updatedArticlesMetadata.isEmpty()){
            APIResponse metadataUpdateResponse = articlesMetadataUpdateHandler.sendAPIRequest(updatedArticlesMetadata);
            System.out.println(metadataUpdateResponse.message());
        }
    }
}
