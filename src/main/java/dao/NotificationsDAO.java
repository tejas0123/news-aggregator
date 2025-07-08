package dao;

import java.util.List;

import model.Notification;

import dto.NotificationDTO;

public interface NotificationsDAO {
	List<NotificationDTO> getUserNotifcations(int userId);
	boolean insertNotifications(List<Notification> notifications);
}
