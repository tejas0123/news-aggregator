package dao;

import java.util.List;
import java.util.Map;

public interface NewsCategoriesDAO {
	public boolean addCategory(Map<String, List<String>> categoriesMap);
}
