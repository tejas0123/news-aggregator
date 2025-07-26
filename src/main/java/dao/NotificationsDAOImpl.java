package dao;

import model.Notification;
import dto.NotificationDTO;
import exception.DAOException;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDAOImpl implements NotificationsDAO {

	@Override
	public List<NotificationDTO> getUserNotifcations(int userId) {
	    List<NotificationDTO> notifications = new ArrayList<>();

	    final String query = """
	        SELECT n.notification_id, n.article_id, a.title, a.url, n.is_read
	        FROM notifications n
	        JOIN articles a ON n.article_id = a.article_id
	        WHERE n.user_id = ?
	        ORDER BY n.notification_id DESC
	    """;

	    try {
	    	Connection connection = DBConnection.getConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        
	        preparedStatement.setInt(1, userId);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            notifications.add(new NotificationDTO(
	                    resultSet.getInt("notification_id"),
	                    resultSet.getInt("article_id"),
	                    resultSet.getString("title"),
	                    resultSet.getString("url"),
	                    resultSet.getBoolean("is_read")
	            ));
	        }

	    } catch (SQLException sqlException) {
	        sqlException.printStackTrace();
	        throw new DAOException(sqlException.getMessage(), sqlException.getCause());
	    }

	    return notifications;
	}

	@Override
	public boolean insertNotifications(List<Notification> notifications) {
	    String query = "INSERT INTO notifications (user_id, article_id) VALUES (?, ?)";

	    try (Connection connection = DBConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        for (Notification notification : notifications) {
	            preparedStatement.setInt(1, notification.userId());
	            preparedStatement.setInt(2, notification.articleId());
	            preparedStatement.addBatch();
	        }

	        int[] batchResults = preparedStatement.executeBatch();
	        boolean isCreated = batchResults.length == notifications.size();
	        if(isCreated) {
	        	System.out.println("Notifications created successfully");
	        }
	        return isCreated;

	    } catch (SQLException sqlException) {
	    	sqlException.printStackTrace();
	    	throw new DAOException(sqlException.getMessage());
	    }
	}
}

