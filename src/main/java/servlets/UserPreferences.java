package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NotificationPreferenceService;
import util.HttpServletResponseHelper;
import util.JwtUtil;
import util.SingletonObjectMapper;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.AppConfig;
import dto.Response;
import io.jsonwebtoken.Claims;

public class UserPreferences extends HttpServlet {
	private NotificationPreferenceService notificationPreferenceService = AppConfig.getNotificationPreferenceServiceInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
        Response<Void> notificationPreferenceResponse;
        String jwtToken = request.getHeader("Authorization").substring(7);
        Claims claims = JwtUtil.validateTokenAndGetSubject(jwtToken);
        int userId = claims.get("userId", Integer.class);
        Response<Set<String>> userPreferencesResponse;
        
        try {
        	Set<String> userPreferences = notificationPreferenceService.getUserPreferences(userId);
        	response.setStatus(HttpServletResponse.SC_OK);
        	userPreferencesResponse = new Response<>(true, "User preferences fetched successfully", Optional.of(userPreferences));
        	
        } catch(RuntimeException runTimeException) {
        	runTimeException.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            userPreferencesResponse = new Response<>(false, runTimeException.getMessage(), Optional.empty());
        }
        
        buildResponse(response, userPreferencesResponse);
	}

	private <T> void buildResponse(HttpServletResponse response, Response<T> notificationPreferenceResponse) {
		HttpServletResponseHelper.buildHttpServletResponse(response, notificationPreferenceResponse)
        .orElseGet(() -> {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        }); 
	}
}
