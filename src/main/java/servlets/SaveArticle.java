package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.NewsArticle;
import service.SavedArticlesService;
import util.HttpServletResponseHelper;
import util.JwtUtil;
import util.SingletonObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;
import dto.NewsArticleData;
import dto.Response;
import exception.DAOException;
import io.jsonwebtoken.Claims;

public class SaveArticle extends HttpServlet {        
	
	private SavedArticlesService savedArticlesService = AppConfig.getSavedArticlesServiceInstance();
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jwtToken = request.getHeader("Authorization").substring(7);
		
        Claims claims = JwtUtil.validateTokenAndGetSubject(jwtToken);
        int userId = claims.get("userId", Integer.class);
        Response<List<NewsArticleData>> getUserSavedArticlesResponse;
        
        try {
        	List<NewsArticleData> savedArticles = savedArticlesService.getSavedArticlesByUser(userId);
        	response.setStatus(HttpServletResponse.SC_OK);
        	String responseMessage = "Found " + savedArticles.size() + " articles";
        	getUserSavedArticlesResponse = new Response<>(true, responseMessage, Optional.of(savedArticles));
        	
        } catch(RuntimeException runtimeException) {
        	runtimeException.printStackTrace();
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	getUserSavedArticlesResponse = new Response<>(false, runtimeException.getMessage(), Optional.empty());
        }
        
        HttpServletResponseHelper.buildHttpServletResponse(response, getUserSavedArticlesResponse)
        .orElseGet(() -> {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        });
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    Response<Void> saveArticleResponse;
	    String jwtToken = request.getHeader("Authorization").substring(7);

	    Claims claims = JwtUtil.validateTokenAndGetSubject(jwtToken);
	    int userId = claims.get("userId", Integer.class);

	    try {
	        ObjectMapper mapper = SingletonObjectMapper.getInstance();
	        Set<Integer> articleIds = mapper.readValue(request.getInputStream(), new TypeReference<Set<Integer>>() {});

	        savedArticlesService.saveArticle(userId, articleIds);

	        response.setStatus(HttpServletResponse.SC_CREATED);
	        saveArticleResponse = new Response<>(true, "Articles saved successfully", Optional.empty());
	    } catch (RuntimeException runtimeException) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        saveArticleResponse = new Response<>(false, runtimeException.getMessage(), Optional.empty());
	    }

	    HttpServletResponseHelper.buildHttpServletResponse(response, saveArticleResponse)
	        .orElseGet(() -> {
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            return response;
	        });
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Response<Void> deleteArticleResponse = null;
		String jwtToken = request.getHeader("Authorization").substring(7);

	    Claims claims = JwtUtil.validateTokenAndGetSubject(jwtToken);
	    int userId = claims.get("userId", Integer.class);
	    
	    try {
	    	ObjectMapper mapper = SingletonObjectMapper.getInstance();
	        Set<Integer> articleIds = getArticleIds(request);
	        
	        boolean isDeleted = savedArticlesService.deleteArticles(userId, articleIds);
	        if(isDeleted) {
	        	response.setStatus(HttpServletResponse.SC_OK);
	        	deleteArticleResponse = new Response<>(true, "Articles removed from saved artices", Optional.empty());
	        }
	    } catch(RuntimeException runtimeException) {
	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    	deleteArticleResponse = new Response<>(false, runtimeException.getMessage(), Optional.empty());
	    }
	    
	    HttpServletResponseHelper.buildHttpServletResponse(response, deleteArticleResponse)
        .orElseGet(() -> {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        }); 
	}
	
	private Set<Integer> getArticleIds(HttpServletRequest request){
		String[] articleIdParams = request.getParameterValues("articleId");
		Set<Integer> articleIds = new HashSet<>();

        if (articleIdParams == null || articleIdParams.length == 0) {
            return articleIds;
        }

        for (String idString : articleIdParams) {
            try {
                articleIds.add(Integer.parseInt(idString));
            } catch (NumberFormatException numberFormatException) {
            	System.out.println(numberFormatException.getStackTrace());
                return new HashSet<>();
            }
        }
        return articleIds;
	}
}
