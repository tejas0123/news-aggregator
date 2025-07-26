package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import constants.Messages;
import dto.NewsArticleData;
import exception.DAOException;
import model.NewsArticle;
import util.DBConnection;

public class NewsAPIDAO implements NewsProviderDAO{

	@Override
	public Instant getArticleLastFetchedAt() {
		final String LATEST_FETCH_DATE_QUERY = "SELECT MAX(fetched_at) as fetched_at FROM articles;";
		
		try {
			Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(LATEST_FETCH_DATE_QUERY);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(!resultSet.next()) {
				throw new DAOException(Messages.LAST_FETCHED_NOT_FOUND);
			}
			return resultSet.getTimestamp("fetched_at").toInstant();
		} catch (SQLException sqlException) {
			throw new DAOException(sqlException.getMessage(), sqlException.getCause());
		}
	}

	@Override
	public void insertNewsArticles(List<NewsArticle> articles, Map<String, Integer> headlinesCategories) {
		final String INSERT_ARTICLES_QUERY = "INSERT INTO articles (title, source_id, url, published_at, description, category_id, article_body) values(?, ?, ?, ?, ?, ?, ?) ON CONFLICT (url) DO NOTHING;";
	
		if(!articles.isEmpty()) {
			try {
				Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARTICLES_QUERY);
				
				for(NewsArticle article: articles) {
					preparedStatement.setString(1, article.getTitle());
					preparedStatement.setString(2, article.getSourceId());
					preparedStatement.setString(3, article.getUrl());
					preparedStatement.setTimestamp(4, Timestamp.from(article.getPublishedAt()));
					preparedStatement.setString(5, article.getDescription());
					preparedStatement.setInt(6, headlinesCategories.get(article.getCategory()));
					preparedStatement.setString(7, article.getArticle_body());
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
				throw new DAOException(sqlException.getMessage(), sqlException.getCause());
			}
		}
	}

	@Override
	public Map<String, Integer> getNewsCategories() {
		final String GET_NEWS_CATEGORIES_QUERY = "SELECT category_id, name, is_active FROM news_categories WHERE is_active = true";
		
		try {
			Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(GET_NEWS_CATEGORIES_QUERY);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			Map<String, Integer> categories = new HashMap<String, Integer>();
			
			if(resultSet.isBeforeFirst()) {
				while(resultSet.next()) {
					categories.put(resultSet.getString("name"), resultSet.getInt("category_id"));
				}
			}
			return categories;
		} catch(SQLException sqlException) {
			sqlException.printStackTrace();
			throw new DAOException(sqlException.getMessage(), sqlException.getCause());
		}
	}
	
	public List<NewsArticle> getLatestAddedArticles() {
	    final String LATEST_ADDED_ARTICLES = """
	        SELECT 
			    a.article_id,
			    a.title,
			    a.url,
			    a.description,
			    a.article_body,
			    a.published_at,
			    a.source_id,
			    nc.name AS category
			FROM articles a
			JOIN news_categories nc ON a.category_id = nc.category_id
			WHERE a.fetched_at >= NOW() - INTERVAL '1 hour';
	    """;

	    List<NewsArticle> articles = new ArrayList<>();

	    try {
	        Connection connection = DBConnection.getConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement(LATEST_ADDED_ARTICLES);
	        
	        ResultSet resultSet = preparedStatement.executeQuery();
	     
	        while (resultSet.next()) {
	            NewsArticle article = new NewsArticle();
	            article.setArticleId(resultSet.getInt("article_id"));
	            article.setTitle(resultSet.getString("title"));
	            article.setUrl(resultSet.getString("url"));
	            article.setDescription(resultSet.getString("description"));
	            article.setArticle_body(resultSet.getString("article_body"));
	            article.setPublishedAt(resultSet.getTimestamp("published_at").toInstant());
	            article.setSourceId(resultSet.getString("source_id"));
	            article.setCategory(resultSet.getString("category"));
	            articles.add(article);
	        }
	        
	    } catch (SQLException sqlException) {
	        sqlException.printStackTrace();
	        throw new DAOException(sqlException.getMessage(), sqlException.getCause());
	    }

	    return articles;
	}

		
}
