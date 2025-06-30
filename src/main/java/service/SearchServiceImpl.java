package service;

import java.util.ArrayList;
import java.util.List;

import dao.SearchServiceDAO;
import exception.DAOException;
import model.NewsArticle;
import model.SearchParams;

public class SearchServiceImpl implements SearchService{
	private SearchServiceDAO searchServiceDAO;
	
	public SearchServiceImpl(SearchServiceDAO searchServiceDAO) {
		this.searchServiceDAO = searchServiceDAO;
	}

	@Override
	public List<NewsArticle> getArticles(SearchParams searchParams) {
		List<NewsArticle> newsArticles = new ArrayList<>();
		
		try {
			newsArticles = searchServiceDAO.searchArticles(searchParams);
			return newsArticles;
		} catch(DAOException daoException) {
			daoException.printStackTrace();
			return newsArticles;
		}
	}

}
