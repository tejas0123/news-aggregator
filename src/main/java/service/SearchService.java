package service;

import java.util.List;
import model.NewsArticle;
import model.SearchParams;

public interface SearchService {
	List<NewsArticle> getArticles(SearchParams searchParams);
}
