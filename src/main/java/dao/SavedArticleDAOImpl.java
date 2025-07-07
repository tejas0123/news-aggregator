package dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import dto.NewsArticleData;
import exception.DAOException;
import util.DBConnection;

public class SavedArticleDAOImpl implements SavedArticleDAO{
	
	
	@Override
    public List<NewsArticleData> getSavedArticlesByUser(int userId) {
        List<NewsArticleData> articles = new ArrayList<>();
        String getArticlesQuery = """
            SELECT 
		    a.article_id, 
		    a.title, 
		    a.url, 
		    a.description,
		    COALESCE(am.likes, 0) AS likes,
		    COALESCE(am.dislikes, 0) AS dislikes
			FROM articles a
			JOIN saved_articles sa ON a.article_id = sa.article_id
			LEFT JOIN article_metadata am ON a.article_id = am.article_id
			WHERE sa.user_id = ?
			ORDER BY sa.saved_at DESC;""";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getArticlesQuery)) {

        	preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                NewsArticleData article = new NewsArticleData();
                article.setArticle_id(resultSet.getInt("article_id"));
                article.setTitle(resultSet.getString("title"));
                article.setUrl(resultSet.getString("url"));
                article.setDescription(resultSet.getString("description"));
                article.setLikes(resultSet.getInt("likes"));
                article.setDislikes(resultSet.getInt("dislikes"));
                articles.add(article);
            }
        } catch (SQLException sqlException) {
        	sqlException.printStackTrace();
        	throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }

        return articles;
    }
	 
    @Override
    public void saveArticle(int userId, int articleId) {
        String saveArticleQuery = "INSERT INTO saved_articles(user_id, article_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(saveArticleQuery)) {

        	preparedStatement.setInt(1, userId);
        	preparedStatement.setInt(2, articleId);
        	preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
        	sqlException.printStackTrace();
        	throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
    }

    @Override
    public boolean deleteArticles(int userId, Set<Integer> articleIds) {
        String deleteArticleQuery = "DELETE FROM saved_articles WHERE user_id = ? AND article_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(deleteArticleQuery)) {

            for (Integer articleId : articleIds) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, articleId);
                preparedStatement.addBatch();
            }

            int[] rowsAffected = preparedStatement.executeBatch();
            return Arrays.stream(rowsAffected).anyMatch(rows -> rows > 0);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
    }


	@Override
	public void saveArticle(int userId, Set<Integer> articleIds) {
		String saveArticleQuery = "INSERT INTO saved_articles(user_id, article_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(saveArticleQuery)) {

        	for(int articleId: articleIds) {
        		preparedStatement.setInt(1, userId);
            	preparedStatement.setInt(2, articleId);
            	preparedStatement.addBatch();
        	}
        	preparedStatement.executeBatch();
        } catch (BatchUpdateException batchUpdateException) {
        	batchUpdateException.printStackTrace();
        	throw new DAOException(batchUpdateException.getMessage(), batchUpdateException.getCause());
        } catch (SQLException sqlException) {
        	sqlException.printStackTrace();
        	throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }	
	}
}
