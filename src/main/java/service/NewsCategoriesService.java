package service;

import java.util.List;
import java.util.Map;

public interface NewsCategoriesService {
	public Map<String, Integer> getAllNewsCategories();
	public void addCategory(Map<String, List<String>> categoryWithKeywordsMap);
}
