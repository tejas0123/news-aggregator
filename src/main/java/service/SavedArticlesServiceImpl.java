package service;

import java.util.List;
import java.util.Set;
import dao.SavedArticleDAO;
import dto.NewsArticleData;


public class SavedArticlesServiceImpl implements SavedArticlesService{

	private SavedArticleDAO savedArticlesDAO;
	
	public SavedArticlesServiceImpl(SavedArticleDAO savedArticlesDAO) {
		this.savedArticlesDAO = savedArticlesDAO;
	}

	@Override
	public List<NewsArticleData> getSavedArticlesByUser(int userId) {
		return savedArticlesDAO.getSavedArticlesByUser(userId);
	}

	@Override
	public void saveArticle(int userId, int articleId) {
		savedArticlesDAO.saveArticle(userId, articleId);
	}
	
	@Override
	public boolean deleteArticles(int userId, Set<Integer> articleIds) {
		return savedArticlesDAO.deleteArticles(userId, articleIds);
	}

	@Override
	public void saveArticle(int userId, Set<Integer> articleIds) {
		savedArticlesDAO.saveArticle(userId, articleIds);
	}

}
