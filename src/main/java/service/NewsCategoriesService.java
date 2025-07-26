package service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NewsCategoriesService {
	public Map<String, Integer> getAllNewsCategories();
	public void addCategory(Map<String, List<String>> categoryWithKeywordsMap);
	public boolean disableCategory(Set<Integer> categoryIds);
	public boolean addWordsToBlock(Set<String> wordsToBlock);
}
