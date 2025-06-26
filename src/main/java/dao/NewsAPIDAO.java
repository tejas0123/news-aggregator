package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import constants.Messages;
import exception.DAOException;
import model.NewsArticle;
import util.DBConnection;

public class NewsAPIDAO implements NewsProviderDAO{

	@Override
	public Instant getArticleLastFetchedAt() {
		String LATEST_FETCH_DATE_QUERY = "SELECT MAX(fetched_at) FROM articles;";
		
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
	public void insertNewsArticles(List<NewsArticle> articles) {
		String INSERT_ARTICLES_QUERY = "INSERT INTO articles values(title, source_id, url, published_at, description, category, article_body) values(?, ?, ?, ?, ?, ?, ?::category, ?) ON CONFLICT (url) DO NOTHING;";
	
		try {
			Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARTICLES_QUERY);
			for(NewsArticle article: articles) {
				preparedStatement.setString(1, article.getTitle());
				preparedStatement.setString(2, article.getSourceId());
				preparedStatement.setString(3, article.getUrl());
				preparedStatement.setTimestamp(4, Timestamp.from(article.getPublishedAt()));
				preparedStatement.setString(5, article.getDescription());
				preparedStatement.setString(6, article.getCategory());
				preparedStatement.setString(7, article.getArticle_body());
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			throw new DAOException(sqlException.getMessage(), sqlException.getCause());
		}
	}

	@Override
	public List<String> getNewsCategories() {
		String GET_NEWS_CATEGORIES_QUERY = "SELECT category_id, name, is_active FROM news_categories WHERE is_active = true LIMIT 1";
		
		try {
			Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(GET_NEWS_CATEGORIES_QUERY);
			ResultSet resultSet = preparedStatement.executeQuery();
			List<String> categories = new ArrayList<String>();
			
			if(resultSet.isBeforeFirst()) {
				while(resultSet.next()) {
					categories.add(resultSet.getString("name"));
				}
			}
			return categories;
		} catch(SQLException sqlException) {
			sqlException.getStackTrace();
			throw new DAOException(sqlException.getMessage(), sqlException.getCause());
		}
	}

}
