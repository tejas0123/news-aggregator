package dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NewsCategoriesDAO {
	public boolean addCategory(Map<String, List<String>> categoriesMap);
	public boolean disableNewsCategory(Set<Integer> categoryIds);
}
