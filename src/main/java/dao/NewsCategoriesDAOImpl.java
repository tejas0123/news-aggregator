package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import exception.DAOException;
import util.DBConnection;

public class NewsCategoriesDAOImpl implements NewsCategoriesDAO{

	@Override
	public boolean addCategory(Map<String, List<String>> categoryWithKeywordsMap) {
	    final String insertCategoryQuery = "INSERT INTO news_categories (name) VALUES (?) ON CONFLICT DO NOTHING RETURNING category_id";
	    final String insertWordMapQuery = "INSERT INTO word_map (word, news_category) VALUES (?, ?) ON CONFLICT DO NOTHING";

	    try{
	    	Connection connection = DBConnection.getConnection();
	        connection.setAutoCommit(false);

	        for (Map.Entry<String, List<String>> entry : categoryWithKeywordsMap.entrySet()) {
	            String category = entry.getKey();
	            List<String> keywords = entry.getValue();

	            int categoryId = -1;
	            
	            try (PreparedStatement categoryStatement = connection.prepareStatement(insertCategoryQuery)) {
	            	categoryStatement.setString(1, category.toUpperCase());
	                ResultSet resultSet = categoryStatement.executeQuery();
	                if (resultSet.next()) {
	                    categoryId = resultSet.getInt("category_id");
	                }
	            }

	            try {
	            	PreparedStatement wordMapStatement = connection.prepareStatement(insertWordMapQuery);
	                for (String word : keywords) {
	                	wordMapStatement.setString(1, word);
	                	wordMapStatement.setString(2, category.toUpperCase());
	                	wordMapStatement.addBatch();
	                }
	                wordMapStatement.executeBatch();
	            } catch(SQLException sqlException) {
	            	sqlException.printStackTrace();
	    	        throw new DAOException(sqlException.getMessage(), sqlException.getCause());
	            }
	        }

	        connection.commit();
	        return true;
	    } catch (SQLException sqlException) {
	    	sqlException.printStackTrace();
	        throw new DAOException(sqlException.getMessage(), sqlException.getCause());
	    }	
	}

	@Override
	public boolean disableNewsCategory(Set<Integer> categoryIds) {
		if (categoryIds == null || categoryIds.isEmpty()) {
	        return false; 
	    }

	    String disableCategoryQuery = "UPDATE news_categories SET is_active = false WHERE category_id = ?";

	    try {
	    	Connection connection = DBConnection.getConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement(disableCategoryQuery);
	        for (Integer categoryId : categoryIds) {
	            preparedStatement.setInt(1, categoryId);
	            preparedStatement.addBatch();
	        }

	        int[] rowsAffected = preparedStatement.executeBatch();
	       
	        return Arrays.stream(rowsAffected).anyMatch(count -> count >= 1);

	    } catch (SQLException sqlException) {
	        sqlException.printStackTrace();
	        throw new DAOException(sqlException.getMessage(), sqlException.getCause());
	    }
	}

	@Override
	public boolean addWordsToBlock(Set<String> wordsToBlock) {
		if (wordsToBlock == null || wordsToBlock.isEmpty()) {
	        return false; 
	    }
		
		String addWordsToBlockQuery = "INSERT into blocked_words (word) VALUES(?) ON CONFLICT DO NOTHING";
		
		try {
			Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(addWordsToBlockQuery);
			for(String word : wordsToBlock) {
				preparedStatement.setString(1, word);
				preparedStatement.addBatch();
			}
			
			int[] rowsAffected = preparedStatement.executeBatch();
			return Arrays.stream(rowsAffected).anyMatch(count -> count >= 1);
		} catch (SQLException sqlException) {
	        sqlException.printStackTrace();
	        throw new DAOException(sqlException.getMessage(), sqlException.getCause());
	    }
	}	
}
