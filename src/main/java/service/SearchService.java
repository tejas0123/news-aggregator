package service;

import java.util.List;

import dto.NewsArticleData;
import dto.SearchParams;
import model.NewsArticle;

public interface SearchService {
	List<NewsArticleData> getArticles(SearchParams searchParams);
}
