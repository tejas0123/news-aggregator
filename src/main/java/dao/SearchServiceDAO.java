package dao;

import java.util.List;
import model.NewsArticle;
import model.SearchParams;

public interface SearchServiceDAO {
	List<NewsArticle> searchArticles(SearchParams searchParams);
}
