package dao;

import java.util.List;
import java.util.Set;
import dto.NewsArticleData;

public interface SavedArticleDAO {
	List<NewsArticleData> getSavedArticlesByUser(int userId);
    void saveArticle(int userId, int articleId);
    boolean deleteArticles(int userId, Set<Integer> articleIds);
    void saveArticle(int userId, Set<Integer> articleIds);
}
