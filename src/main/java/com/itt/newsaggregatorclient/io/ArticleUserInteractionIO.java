package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.dto.ArticleMetadata;
import com.itt.newsaggregatorclient.dto.NewsArticleData;
import com.itt.newsaggregatorclient.util.SingletonScanner;
import java.util.*;

public class ArticleUserInteractionIO {
    Scanner scanner = SingletonScanner.getScannerInstance();
    private final Set<Integer> userSavedArticleIds;
    private final Map<Integer, ArticleMetadata> articleMetadataMap;

    public ArticleUserInteractionIO() {
        userSavedArticleIds = new HashSet<>();
        articleMetadataMap = new HashMap<>();
    }

    public void printArticles(int index, List<NewsArticleData> articleDataList) {
        if (articleDataList == null || articleDataList.isEmpty()) {
            System.out.println("No articles to display.");
            return;
        }

        NewsArticleData articleData = articleDataList.get(index);
        System.out.println();
        System.out.println("Article " + (index + 1) + " of " + articleDataList.size());
        printArticleContents(articleData);

        System.out.print("Enter [s=save, l=like, d=dislike, r=report, n=next, p=previous, e=exit]: ");
        String input = scanner.nextLine().trim().toLowerCase();

        int articleId = articleData.getArticle_id();

        switch (input) {
            case "s" -> {
                if (userSavedArticleIds.add(articleId)) {
                    System.out.println("Article saved.");
                } else {
                    System.out.println("Article already saved.");
                }
            }
            case "l" -> {
                likeArticle(articleId);
                articleData.setLikes(articleData.getLikes() + 1);
            }

            case "d" -> {
                dislikeArticle(articleId);
                articleData.setDislikes(articleData.getDislikes() + 1);
            }

            case "r" -> reportArticle(articleId);
            case "n" -> {
                index = (index + 1) % articleDataList.size();
            }
            case "p" -> {
                index = (index - 1 + articleDataList.size()) % articleDataList.size();
            }
            case "e" -> {
                System.out.println("Exiting");
                return;
            }
            default -> System.out.println("Invalid input");
        }

        printArticles(index, articleDataList);
    }

    private void printArticleContents(NewsArticleData articleData) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nTitle: " + articleData.getTitle());
        System.out.println("Description: " + articleData.getDescription());
        System.out.println("URL: " + articleData.getUrl());
        System.out.println("Likes: " + articleData.getLikes() + "     " + "Dislikes: " + articleData.getDislikes());
        System.out.println("____________________________________________________________________________");
    }

    private void likeArticle(int articleId) {
        ArticleMetadata metadata = articleMetadataMap.getOrDefault(articleId, new ArticleMetadata(articleId, 0, 0, 0));
        metadata = new ArticleMetadata(articleId, 1, 0, metadata.getReports());
        articleMetadataMap.put(articleId, metadata);
        System.out.println("Liked the article.");
    }

    private void dislikeArticle(int articleId) {
        ArticleMetadata metadata = articleMetadataMap.getOrDefault(articleId, new ArticleMetadata(articleId, 0, 0, 0));
        metadata = new ArticleMetadata(articleId, 0, 1, metadata.getReports());
        articleMetadataMap.put(articleId, metadata);
        System.out.println("Disliked the article.");
    }

    private void reportArticle(int articleId) {
        ArticleMetadata metadata = articleMetadataMap.getOrDefault(articleId, new ArticleMetadata(articleId, 0, 0, 0));
        metadata = new ArticleMetadata(articleId, metadata.getLikes(), metadata.getDislikes(), 1);
        articleMetadataMap.put(articleId, metadata);
        System.out.println("Reported the article.");
    }

    public Set<Integer> getUserSavedArticleIds() {
        return userSavedArticleIds;
    }

    public List<ArticleMetadata> getModifiedMetadata() {
        return new ArrayList<>(articleMetadataMap.values());
    }
}
