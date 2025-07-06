package dao;

import java.util.List;

import dto.NewsArticleData;
import dto.SearchParams;
import model.NewsArticle;

public interface SearchServiceDAO {
	List<NewsArticleData> searchArticles(SearchParams searchParams);
}
