package service;

import java.util.List;

import model.NewsArticle;

public interface SavedArticlesService {
	List<NewsArticle> getSavedArticlesByUser(int userId);
    void saveArticle(int userId, int articleId);
    void saveArticle(int userId, List<Integer> articleIds);
    boolean deleteArticles(int userId, List<Integer> articleIds);
}
