package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import dto.NewsArticleData;
import dto.SearchParams;
import exception.DAOException;
import model.NewsArticle;
import util.DBConnection;

public class SearchServiceDAOImpl implements SearchServiceDAO {

    @Override
    public List<NewsArticleData> searchArticles(SearchParams searchParams) {
        StringBuilder getArticlesQuery = new StringBuilder("""
                SELECT 
                    a.article_id, a.title, a.url, a.description,
                    COALESCE(m.likes, 0) AS likes,
                    COALESCE(m.dislikes, 0) AS dislikes
                FROM articles a
                LEFT JOIN article_metadata m ON a.article_id = m.article_id
                WHERE (m.is_hidden = FALSE OR m.is_hidden IS NULL)
            """);
        
        List<Object> parameters = new ArrayList<>();
        
        String searchQuery = buildSearchQuery(getArticlesQuery, searchParams, parameters);
        List<NewsArticleData> newsArticles = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(searchQuery)) {

            for (int index = 0; index < parameters.size(); index++) {
            	Object param = parameters.get(index);
            	if (param instanceof java.time.Instant) {
            	    preparedStatement.setTimestamp(index + 1, java.sql.Timestamp.from((java.time.Instant) param));
            	} else {
            	    preparedStatement.setObject(index + 1, param);
            	}
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	NewsArticleData articleData = new NewsArticleData();
            	articleData.setArticle_id(resultSet.getInt("article_id"));
            	articleData.setTitle(resultSet.getString("title"));
            	articleData.setUrl(resultSet.getString("url"));
            	articleData.setDescription(resultSet.getString("description"));
            	articleData.setLikes(resultSet.getInt("likes"));
            	articleData.setDislikes(resultSet.getInt("dislikes"));
                newsArticles.add(articleData);
            }

            return newsArticles;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new DAOException(sqlException.getMessage(), sqlException.getCause());
        }
    }

    private String buildSearchQuery(StringBuilder searchQuery, SearchParams searchParams, List<Object> parameters) {
        searchParams.category().ifPresent(category -> {
            searchQuery.append(" AND category_id = (SELECT category_id FROM news_categories WHERE LOWER(name) = LOWER(?))");
            parameters.add(category);
        });

        searchParams.keyword().ifPresent(keyword -> {
            searchQuery.append(" AND (title ILIKE ? OR description ILIKE ? OR article_body ILIKE ?)");
            String likePattern = "%" + keyword.toLowerCase() + "%";
            parameters.add(likePattern);
            parameters.add(likePattern);
            parameters.add(likePattern);
        });

        searchParams.from().ifPresent(from -> {
            searchQuery.append(" AND published_at >= ?");
            parameters.add(from.atStartOfDay(ZoneOffset.UTC).toInstant());
        });

        searchParams.to().ifPresent(to -> {
            searchQuery.append(" AND published_at < ?");
            parameters.add(to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant());
        });

        System.out.println(searchQuery.toString());
        return searchQuery.toString();
    }
}
