package service;

import java.util.List;

import dto.NotificationDTO;
import model.Notification;

public interface NotificationsService {
	List<NotificationDTO> getUserNotifcations(int userId);
}
