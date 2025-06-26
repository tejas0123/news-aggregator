package dao;

import java.time.Instant;
import java.util.List;

import model.NewsArticle;

public interface NewsProviderDAO {
	Instant getArticleLastFetchedAt();
	void insertNewsArticles(List<NewsArticle> articles);
	List<String> getNewsCategories();
}
