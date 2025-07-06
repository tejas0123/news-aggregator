package service;

import java.util.ArrayList;
import java.util.List;

import dao.SearchServiceDAO;
import dto.NewsArticleData;
import dto.SearchParams;
import exception.DAOException;
import model.NewsArticle;

public class SearchServiceImpl implements SearchService{
	private SearchServiceDAO searchServiceDAO;
	
	public SearchServiceImpl(SearchServiceDAO searchServiceDAO) {
		this.searchServiceDAO = searchServiceDAO;
	}

	@Override
	public List<NewsArticleData> getArticles(SearchParams searchParams) {
		List<NewsArticleData> newsArticles = new ArrayList<>();
		
		try {
			newsArticles = searchServiceDAO.searchArticles(searchParams);
			return newsArticles;
		} catch(DAOException daoException) {
			daoException.printStackTrace();
			return newsArticles;
		}
	}

}
