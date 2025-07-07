package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dto.CategoryPreference;
import exception.DAOException;
import util.DBConnection;

public class NotificationPreferenceDAOImpl implements NotificationPreferenceDAO{

	@Override
	public void insertCategoryPreferences(int userId, List<String> preferences) {
		String insertQuery = """
		        INSERT INTO category_preferences (user_id, category_id)
				VALUES (?, (SELECT category_id FROM news_categories WHERE name = ?))
				ON CONFLICT DO NOTHING;
		    """;

        try {
        	Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            for (String preference : preferences) {
            	preparedStatement.setInt(1, userId);
            	preparedStatement.setString(2, preference.toUpperCase());
            	preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException sqlException) {
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
	}

	@Override
	public List<Integer> getUserIdsByCategoryName(String categoryName) {
		String getUserswithPreferredCategoryQuery = """
	            SELECT cp.user_id
	            FROM category_preferences cp
	            JOIN news_categories nc ON cp.category_id = nc.category_id
	            WHERE LOWER(nc.name) = LOWER(?)
	        """;

        List<Integer> userIds = new ArrayList<>();

        try {
        	Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getUserswithPreferredCategoryQuery);
            preparedStatement.setString(1, categoryName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userIds.add(resultSet.getInt("user_id"));
            }

            return userIds;
        } catch (SQLException sqlException) {
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
	}

	@Override
	public void deleteCategoryPreference(int userId, Set<String> categories) {
		String deleteCategoryPreferenceQuery = """
		        DELETE FROM category_preferences
		        WHERE user_id = ?
		        AND category_id = (
		            SELECT category_id FROM news_categories WHERE LOWER(name) = LOWER(?)
		        )
		    """;

	    try {
	    	Connection connection = DBConnection.getConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement(deleteCategoryPreferenceQuery);
	        
	        for(String category : categories) {
	        	preparedStatement.setInt(1, userId);
		        preparedStatement.setString(2, category);
		        preparedStatement.addBatch();
	        }
	        
	        preparedStatement.executeBatch();
	    } catch (SQLException sqlException) {
	        throw new DAOException(sqlException.getMessage(), sqlException.getCause());
	    }
	}
	
}
