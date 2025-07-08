package service;

import java.util.List;
import java.util.Set;

import dao.NotificationPreferenceDAO;
import dto.CategoryPreference;

public class NotificationPreferenceServiceImpl implements NotificationPreferenceService{
	private NotificationPreferenceDAO notificationPreferenceDAO;

	public NotificationPreferenceServiceImpl(NotificationPreferenceDAO notificationPreferenceDAO) {
		this.notificationPreferenceDAO = notificationPreferenceDAO;
	}

	@Override
	public void saveCategoryPreferences(int userId, List<String> preferences) {
		notificationPreferenceDAO.insertCategoryPreferences(userId, preferences);
	}

	@Override
	public List<Integer> getUserIdsByCategory(String categoryName) {
		 return notificationPreferenceDAO.getUserIdsByCategoryName(categoryName);
	}

	@Override
	public void removeCategoryPreference(int userId, Set<String> categories) {
		notificationPreferenceDAO.deleteCategoryPreference(userId, categories);
	}

	@Override
	public Set<String> getUserPreferences(int userId) {
		return notificationPreferenceDAO.getUserPreferences(userId);
	}
}
