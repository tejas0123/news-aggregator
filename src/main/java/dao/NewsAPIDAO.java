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
import exception.DAOException;
import model.NewsArticle;
import util.DBConnection;

public class NewsAPIDAO implements NewsProviderDAO{

	@Override
	public Instant getArticleLastFetchedAt() {
		String LATEST_FETCH_DATE_QUERY = "SELECT MAX(fetched_at) as fetched_at FROM articles;";
		
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
		String INSERT_ARTICLES_QUERY = "INSERT INTO articles (title, source_id, url, published_at, description, category_id, article_body) values(?, ?, ?, ?, ?, ?, ?) ON CONFLICT (url) DO NOTHING;";
	
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
		String GET_NEWS_CATEGORIES_QUERY = "SELECT category_id, name, is_active FROM news_categories WHERE is_active = true LIMIT 2";
		
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
			sqlException.getStackTrace();
			throw new DAOException(sqlException.getMessage(), sqlException.getCause());
		}
	}

}
