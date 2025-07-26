package service;

import java.util.List;
import java.util.Set;

public interface NotificationPreferenceService {
    void saveCategoryPreferences(int userId, List<String> preferences);
    List<Integer> getUserIdsByCategory(String categoryName);
    void removeCategoryPreference(int userId, Set<String> categories);
    Set<String> getUserPreferences(int userId);
}
