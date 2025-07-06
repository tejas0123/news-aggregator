package service;

import java.util.List;

import dao.SavedArticleDAO;
import exception.DAOException;
import model.NewsArticle;

public class SavedArticlesServiceImpl implements SavedArticlesService{

	private SavedArticleDAO savedArticlesDAO;
	
	public SavedArticlesServiceImpl(SavedArticleDAO savedArticlesDAO) {
		this.savedArticlesDAO = savedArticlesDAO;
	}

	@Override
	public List<NewsArticle> getSavedArticlesByUser(int userId) {
		return savedArticlesDAO.getSavedArticlesByUser(userId);
	}

	@Override
	public void saveArticle(int userId, int articleId) {
		savedArticlesDAO.saveArticle(userId, articleId);
	}
	
	@Override
	public boolean deleteArticles(int userId, List<Integer> articleIds) {
		return savedArticlesDAO.deleteArticles(userId, articleIds);
	}

	@Override
	public void saveArticle(int userId, List<Integer> articleIds) {
		savedArticlesDAO.saveArticle(userId, articleIds);
	}

}
