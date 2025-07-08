package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NotificationsService;
import util.HttpServletResponseHelper;
import util.JwtUtil;
import util.SingletonObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.AppConfig;
import dto.NotificationDTO;
import dto.Response;
import io.jsonwebtoken.Claims;

public class Notifications extends HttpServlet {
	private NotificationsService notificationsService = AppConfig.getNotificationsServiceInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
        Response<List<NotificationDTO>> notificationResponse;
        String jwtToken = request.getHeader("Authorization").substring(7);
        Claims claims = JwtUtil.validateTokenAndGetSubject(jwtToken);
        int userId = claims.get("userId", Integer.class);
        
        try {
        	List<NotificationDTO> notifications = notificationsService.getUserNotifcations(userId);
        	notificationResponse = new Response<>(true, "Notifications fetched successfully", Optional.of(notifications));
        } catch(RuntimeException runtimeException) {
        	runtimeException.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            notificationResponse = new Response<>(false, runtimeException.getMessage(), Optional.empty());
        }
        buildResponse(response, notificationResponse);
	}
	
	private <T> void buildResponse(HttpServletResponse response, Response<T> notificationPreferenceResponse) {
		HttpServletResponseHelper.buildHttpServletResponse(response, notificationPreferenceResponse)
        .orElseGet(() -> {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        }); 
	}

}
