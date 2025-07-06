package dao;

import java.util.List;

import model.NewsArticle;

public interface SavedArticleDAO {
	List<NewsArticle> getSavedArticlesByUser(int userId);
    void saveArticle(int userId, int articleId);
    boolean deleteArticles(int userId, List<Integer> articleIds);
    void saveArticle(int userId, List<Integer> articleIds);
}
