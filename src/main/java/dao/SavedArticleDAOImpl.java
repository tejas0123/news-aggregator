package dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exception.DAOException;
import model.NewsArticle;
import util.DBConnection;

public class SavedArticleDAOImpl implements SavedArticleDAO{
	
	
	@Override
    public List<NewsArticle> getSavedArticlesByUser(int userId) {
        List<NewsArticle> articles = new ArrayList<>();
        String getArticlesQuery = """
            SELECT a.article_id, a.title, a.url, a.description
            FROM articles a
            JOIN saved_articles sa ON a.article_id = sa.article_id
            WHERE sa.user_id = ?
            ORDER BY sa.saved_at DESC""";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(getArticlesQuery)) {

            ps.setInt(1, userId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                NewsArticle article = new NewsArticle();
                article.setArticleId(resultSet.getInt("article_id"));
                article.setTitle(resultSet.getString("title"));
                article.setUrl(resultSet.getString("url"));
                article.setDescription(resultSet.getString("description"));
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
    public boolean deleteArticles(int userId, List<Integer> articleIds) {
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
	public void saveArticle(int userId, List<Integer> articleIds) {
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
