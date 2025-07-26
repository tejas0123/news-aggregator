package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Role;
import service.NewsCategoriesService;
import service.NewsCategoriesServiceImpl;
import dao.NewsCategoriesDAOImpl;
import util.HttpServletResponseHelper;
import util.JwtUtil;
import util.SingletonObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;
import constants.Messages;
import dto.Response;
import io.jsonwebtoken.Claims;

public class NewsCategories extends HttpServlet {
	private NewsCategoriesService newsCategoriesService = AppConfig.getNewsCategoriesServiceInstance();
	private NewsCategoriesService newsCategoriesServiceWithDAO = new NewsCategoriesServiceImpl(new NewsCategoriesDAOImpl());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Response<Map<String, Integer>> getAllCategoriesResponse = null;
		
		try {
			Map<String, Integer> newsCategories = newsCategoriesService.getAllNewsCategories();
			response.setStatus(HttpServletResponse.SC_OK);
			getAllCategoriesResponse = new Response<>(true, "News Categories fetched successfully", Optional.of(newsCategories));
		} catch(RuntimeException runTimeException) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			getAllCategoriesResponse = new Response<>(false, runTimeException.getMessage(), Optional.empty());
		}
		
		buildResponse(response, getAllCategoriesResponse);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Response<Void> apiResponse;
		
		if(!isUserAuthorized(request)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			apiResponse = new Response<>(false, Messages.UNAUTHORIZED_OPERATION,Optional.empty());
			buildResponse(response, apiResponse);
			return;
		}
		
		try {
            ObjectMapper mapper = SingletonObjectMapper.getInstance();
            if(request.getRequestURI().endsWith("/blocked")) {
            	Set<String> wordsToBlock = mapper.readValue(
            			request.getInputStream(), new TypeReference<Set<String>>() {}
            	);
            	newsCategoriesServiceWithDAO.addWordsToBlock(wordsToBlock);
            	response.setStatus(HttpServletResponse.SC_CREATED);
            	apiResponse = new Response<>(true, "Words Added successfully", Optional.empty());
            }
            else {
            	Map<String, List<String>> categoryWithKeywords = mapper.readValue(
                        request.getInputStream(), new TypeReference<Map<String, List<String>>>() {}
                );
            	newsCategoriesServiceWithDAO.addCategory(categoryWithKeywords);
                response.setStatus(HttpServletResponse.SC_CREATED);
                apiResponse = new Response<>(true, "Category added successfully", Optional.empty());
            }
            
        } catch (RuntimeException runTimeException) {
        	runTimeException.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            apiResponse = new Response<>(false, runTimeException.getMessage(), Optional.empty());
        }
		buildResponse(response, apiResponse);
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    if ("PATCH".equalsIgnoreCase(request.getMethod())) {
	        doPatch(request, response);
	    } else {
	        super.service(request, response);
	    }
	}

	protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Response<Void> disableNewsCategoryResponse;
		if(!isUserAuthorized(request)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			disableNewsCategoryResponse = new Response<>(false, Messages.UNAUTHORIZED_OPERATION,Optional.empty());
			buildResponse(response, disableNewsCategoryResponse);
			return;
		}
		
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
		try {
			Set<Integer> categoryIds = mapper.readValue(
                    request.getInputStream(), new TypeReference<Set<Integer>>() {}
            );
			
			boolean isDisabled = newsCategoriesServiceWithDAO.disableCategory(categoryIds);
			if(isDisabled) {
				response.setStatus(HttpServletResponse.SC_OK);
				disableNewsCategoryResponse = new Response<>(true, "News category disabled successfully", Optional.empty());
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
				disableNewsCategoryResponse = new Response<>(true, "News category not found", Optional.empty());
			}
		} catch(RuntimeException runtimeException) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			disableNewsCategoryResponse = new Response<>(false, runtimeException.getMessage() ,Optional.empty());
		}
		
		buildResponse(response, disableNewsCategoryResponse);
	}
	
	private boolean isUserAuthorized(HttpServletRequest request) {
		String jwtToken = request.getHeader("Authorization").substring(7);
        Claims claims = JwtUtil.validateTokenAndGetSubject(jwtToken);
        Role role = Role.valueOf(claims.get("role", String.class));
        return role.equals(Role.ADMIN);
	}
	
	private <T> void buildResponse(HttpServletResponse response, Response<T> apiResponse) {
		HttpServletResponseHelper.buildHttpServletResponse(response, apiResponse)
        .orElseGet(() -> {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        });
	}
}
