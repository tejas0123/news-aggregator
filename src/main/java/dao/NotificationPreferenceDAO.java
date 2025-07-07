package dao;

import java.util.List;
import java.util.Set;
import dto.CategoryPreference;

public interface NotificationPreferenceDAO {
	void insertCategoryPreferences(int userId, List<String> preferences);
	List<Integer> getUserIdsByCategoryName(String categoryName);
	void deleteCategoryPreference(int userId, Set<String> categories);
}
