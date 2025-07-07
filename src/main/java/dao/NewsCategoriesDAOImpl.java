package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import exception.DAOException;
import util.DBConnection;

public class NewsCategoriesDAOImpl implements NewsCategoriesDAO{

	@Override
	public boolean addCategory(Map<String, List<String>> categoryWithKeywordsMap) {
	    String insertCategoryQuery = "INSERT INTO news_categories (category_name) VALUES (?) ON CONFLICT DO NOTHING RETURNING category_id";
	    String insertWordMapQuery = "INSERT INTO word_map (word, category) VALUES (?, ?) ON CONFLICT DO NOTHING";

	    try (Connection connection = DBConnection.getConnection()) {
	        connection.setAutoCommit(false);

	        for (Map.Entry<String, List<String>> entry : categoryWithKeywordsMap.entrySet()) {
	            String category = entry.getKey();
	            List<String> keywords = entry.getValue();

	            int categoryId = -1;
	            try (PreparedStatement categoryStatement = connection.prepareStatement(insertCategoryQuery)) {
	            	categoryStatement.setString(1, category);
	                ResultSet resultSet = categoryStatement.executeQuery();
	                if (resultSet.next()) {
	                    categoryId = resultSet.getInt("category_id");
	                }
	            }

	            try (PreparedStatement wordMapStatement = connection.prepareStatement(insertWordMapQuery)) {
	                for (String word : keywords) {
	                	wordMapStatement.setString(1, word);
	                	wordMapStatement.setString(2, category);
	                	wordMapStatement.addBatch();
	                }
	                wordMapStatement.executeBatch();
	            }
	        }

	        connection.commit();
	        return true;
	    } catch (SQLException sqlException) {
	    	sqlException.printStackTrace();
	        throw new DAOException("Error inserting category and keywords", sqlException);
	    }
	}
}
