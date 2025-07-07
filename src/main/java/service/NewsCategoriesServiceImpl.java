package service;

import java.util.List;
import java.util.Map;
import dao.NewsAPIDAO;
import dao.NewsCategoriesDAO;

public class NewsCategoriesServiceImpl implements NewsCategoriesService{
	private NewsAPIDAO newsAPIDAO;
	private NewsCategoriesDAO newsCategoriesDAO;

	public NewsCategoriesServiceImpl(NewsAPIDAO newsAPIDAO) {
		this.newsAPIDAO = newsAPIDAO;
	}
	
	public NewsCategoriesServiceImpl(NewsCategoriesDAO newsCategoriesDAO) {
		this.newsCategoriesDAO = newsCategoriesDAO;
	}

	@Override
	public Map<String, Integer> getAllNewsCategories() {
		return newsAPIDAO.getNewsCategories();
	}

	@Override
	public void addCategory(Map<String, List<String>> categoryWithKeywordsMap) {
		newsCategoriesDAO.addCategory(categoryWithKeywordsMap);
	}
	
}
