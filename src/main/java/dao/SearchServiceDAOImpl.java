package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import exception.DAOException;
import model.NewsArticle;
import model.SearchParams;
import util.DBConnection;

public class SearchServiceDAOImpl implements SearchServiceDAO {

    @Override
    public List<NewsArticle> searchArticles(SearchParams searchParams) {
        List<Object> parameters = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT article_id, title, url, description FROM articles WHERE article_id IS NOT NULL");
        String searchQuery = buildSearchQuery(queryBuilder, searchParams, parameters);
        List<NewsArticle> newsArticles = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(searchQuery)) {

            for (int i = 0; i < parameters.size(); i++) {
            	Object param = parameters.get(i);
            	if (param instanceof java.time.Instant) {
            	    preparedStatement.setTimestamp(i + 1, java.sql.Timestamp.from((java.time.Instant) param));
            	} else {
            	    preparedStatement.setObject(i + 1, param);
            	}
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                NewsArticle article = new NewsArticle();
                article.setArticleId(resultSet.getInt("article_id"));
                article.setTitle(resultSet.getString("title"));
                article.setUrl(resultSet.getString("url"));
                article.setDescription(resultSet.getString("description"));
                newsArticles.add(article);
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

        return searchQuery.toString();
    }
}
