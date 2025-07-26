package service;

import java.util.List;
import java.util.Set;
import dto.NewsArticleData;

public interface SavedArticlesService {
	List<NewsArticleData> getSavedArticlesByUser(int userId);
    void saveArticle(int userId, int articleId);
    void saveArticle(int userId, Set<Integer> articleIds);
    boolean deleteArticles(int userId, Set<Integer> articleIds);
}
