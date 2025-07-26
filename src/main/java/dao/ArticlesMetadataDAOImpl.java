package dao;

import exception.DAOException;
import model.ArticleMetadata;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticlesMetadataDAOImpl implements ArticlesMetadataDAO {

    @Override
    public void updateArticlesMetadata(List<ArticleMetadata> metadataList) {
    	final String updateMetadataQuery = """
		        INSERT INTO article_metadata (article_id, likes, dislikes, reports)
		        VALUES (?, ?, ?, ?)
		        ON CONFLICT (article_id) DO UPDATE
		        SET likes = article_metadata.likes + EXCLUDED.likes,
		            dislikes = article_metadata.dislikes + EXCLUDED.dislikes,
		            reports = article_metadata.reports + EXCLUDED.reports
	        """;

        try {
        	PreparedStatement preparedStatement = getPreparedStatement(updateMetadataQuery);
            
            for (ArticleMetadata metadata : metadataList) {
            	preparedStatement.setInt(1, metadata.getArticle_id());
            	preparedStatement.setInt(2, metadata.getLikes());
            	preparedStatement.setInt(3, metadata.getDislikes());
            	preparedStatement.setInt(4, metadata.getReports());
            	preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException sqlException) {
        	sqlException.printStackTrace();
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
    }

    @Override
    public List<ArticleMetadata> getArticlesMetadata(Set<Integer> articleIds) {

        StringBuilder queryBuilder = buildArticlesMetadataQuery(articleIds);

        List<ArticleMetadata> articlesMetadata = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = getPreparedStatement(queryBuilder.toString());

            int index = 1;
            for (Integer id : articleIds) {
                preparedStatement.setInt(index++, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ArticleMetadata metadata = new ArticleMetadata();
                metadata.setArticle_id(resultSet.getInt("article_id"));
                metadata.setLikes(resultSet.getInt("likes"));
                metadata.setDislikes(resultSet.getInt("dislikes"));
                metadata.setReports(resultSet.getInt("reports"));
                articlesMetadata.add(metadata);
            }

            return articlesMetadata;
        } catch (SQLException sqlException) {
        	sqlException.printStackTrace();
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
    }

	private PreparedStatement getPreparedStatement(String query) throws SQLException{
		Connection connection = DBConnection.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		return preparedStatement;
	}
	
	private StringBuilder buildArticlesMetadataQuery(Set<Integer> articleIds) {
		StringBuilder queryBuilder = new StringBuilder("""
	            SELECT article_id, likes, dislikes, reports 
	            FROM article_metadata
	            WHERE article_id IN (
	        """);

        String placeholders = articleIds
        		.stream()
        		.map(id -> "?")
        		.collect(Collectors.joining(", "));
        return queryBuilder.append(placeholders).append(") ORDER BY reports DESC");
	}
}

