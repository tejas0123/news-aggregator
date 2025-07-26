package com.itt.newsaggregatorclient.dto;

public record NotificationDTO(
		int notificationId,
		int articleId,
		String title,
		String url,
		boolean isRead
) {

}
