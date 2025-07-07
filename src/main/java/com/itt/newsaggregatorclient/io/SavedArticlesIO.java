package com.itt.newsaggregatorclient.io;

import com.itt.newsaggregatorclient.dto.ArticleMetadata;
import com.itt.newsaggregatorclient.dto.NewsArticleData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SavedArticlesIO extends ArticleUserInteractionIO{
    private Map<Integer, ArticleMetadata> articleMetadataMap;
    private Set<Integer> articleIdsToUnsave;

    public SavedArticlesIO() {
        articleMetadataMap = new HashMap<>();
        articleIdsToUnsave = new HashSet<>();
    }

    public void printSavedArticles(int index, List<NewsArticleData> articleDataList){
        if (articleDataList == null || articleDataList.isEmpty()) {
            System.out.println("No articles to display.");
            return;
        }

        NewsArticleData articleData = articleDataList.get(index);
        System.out.println();
        System.out.println("Article " + (index + 1) + " of " + articleDataList.size());
        printArticleContents(articleData);

        System.out.print("Enter [rm=remove from saved articles, l=like, d=dislike, r=report, n=next, p=previous, e=exit]: ");
        String input = scanner.nextLine().trim().toLowerCase();

        int articleId = articleData.getArticle_id();

        switch (input) {
            case "rm" -> {
                if (articleIdsToUnsave.add(articleId)) {
                    System.out.println("Article removed");
                } else {
                    System.out.println("Article already removed");
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

        printSavedArticles(index, articleDataList);
    }

    public Set<Integer> getSavedArticlesToRemove(){
        return articleIdsToUnsave;
    }

    public Map<Integer, ArticleMetadata> getUpdatedMetadataMap(){
        return articleMetadataMap;
    }
}
