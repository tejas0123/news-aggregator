package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NotificationPreferenceService;
import util.HttpServletResponseHelper;
import util.JwtUtil;
import util.SingletonObjectMapper;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;
import dto.Response;
import io.jsonwebtoken.Claims;

public class NotificationPreferences extends HttpServlet {
	private NotificationPreferenceService notificationPreferenceService = AppConfig.getNotificationPreferenceServiceInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
        Response<List<Integer>> getUserByCategoryResponse;

        String categoryName = request.getParameter("category");
        if (categoryName == null || categoryName.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            getUserByCategoryResponse = new Response<>(false, "Missing 'category' query parameter", Optional.empty());
        }
        else {
            try {
                List<Integer> userIds = notificationPreferenceService.getUserIdsByCategory(categoryName);
                response.setStatus(HttpServletResponse.SC_OK);
                getUserByCategoryResponse = new Response<>(true, "User IDs fetched successfully", Optional.of(userIds));
            } catch (RuntimeException runTimeException) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                getUserByCategoryResponse = new Response<>(false, runTimeException.getMessage(), Optional.empty());
            }
        }
        
        buildResponse(response, getUserByCategoryResponse);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
        Response<Void> notificationPreferenceResponse;
        String jwtToken = request.getHeader("Authorization").substring(7);
        Claims claims = JwtUtil.validateTokenAndGetSubject(jwtToken);
        int userId = claims.get("userId", Integer.class);
        
        try {
            List<String> preferences = mapper.readValue(request.getInputStream(),new TypeReference<>() {});

            notificationPreferenceService.saveCategoryPreferences(userId, preferences);
            response.setStatus(HttpServletResponse.SC_CREATED);
            notificationPreferenceResponse = new Response<>(true, "Preferences saved successfully", Optional.empty());
        } catch (RuntimeException runTimeException) {
        	runTimeException.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            notificationPreferenceResponse = new Response<>(false, runTimeException.getMessage(), Optional.empty());
        }
        
        buildResponse(response, notificationPreferenceResponse);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
        Response<Void> deletePreferencesResponse;

        String jwtToken = request.getHeader("Authorization").substring(7);
	    Claims claims = JwtUtil.validateTokenAndGetSubject(jwtToken);
	    int userId = claims.get("userId", Integer.class);
        
        try {
        	Set<String> categories = getCategoryIds(request);
    		if(categories.isEmpty()) {
    			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    			deletePreferencesResponse = new Response<>(false, "Invalid request, category not found in params", Optional.empty());
                buildResponse(response, deletePreferencesResponse);
                return;
    		}
    		
            notificationPreferenceService.removeCategoryPreference(userId, categories);
            response.setStatus(HttpServletResponse.SC_OK);
            deletePreferencesResponse = new Response<>(true, "Category preference deleted successfully", Optional.empty());
        } catch (RuntimeException runTimeException) {
        	runTimeException.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            deletePreferencesResponse = new Response<>(false, "Failed to delete category preference", Optional.empty());
        }
        
        buildResponse(response, deletePreferencesResponse);
	}
	
	private Set<String> getCategoryIds(HttpServletRequest request){
		String[] categoryParams = request.getParameterValues("category");
		Set<String> categories = new HashSet<>();

        if (categoryParams == null || categoryParams.length == 0) {
            return categories;
        }

        for (String category : categoryParams) {
            try {
            	categories.add(category);
            } catch (NumberFormatException numberFormatException) {
                return new HashSet<>();
            }
        }
        return categories;
	}

	private <T> void buildResponse(HttpServletResponse response, Response<T> notificationPreferenceResponse) {
		HttpServletResponseHelper.buildHttpServletResponse(response, notificationPreferenceResponse)
        .orElseGet(() -> {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        }); 
	}
}
