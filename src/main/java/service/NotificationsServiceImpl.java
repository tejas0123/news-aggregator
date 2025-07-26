package service;

import java.util.List;

import dao.NotificationsDAO;
import dto.NotificationDTO;

public class NotificationsServiceImpl implements NotificationsService{
	private NotificationsDAO notificationsDAO;
	
	public NotificationsServiceImpl(NotificationsDAO notificationsDAO) {
		this.notificationsDAO = notificationsDAO;
	}

	@Override
	public List<NotificationDTO> getUserNotifcations(int userId) {
		return notificationsDAO.getUserNotifcations(userId);
	}
}
