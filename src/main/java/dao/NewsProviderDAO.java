package dao;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import model.NewsArticle;

public interface NewsProviderDAO {
	Instant getArticleLastFetchedAt();
	void insertNewsArticles(List<NewsArticle> articles, Map<String, Integer> headlinesCategories);
	Map<String, Integer> getNewsCategories();
}

