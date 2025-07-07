package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NewsCategoriesService;
import service.NewsCategoriesServiceImpl;
import util.HttpServletResponseHelper;
import util.SingletonObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;
import dto.Response;

public class NewsCategories extends HttpServlet {
	private NewsCategoriesService newsCategoriesService = AppConfig.getNewsCategoriesServiceInstance();
	private NewsCategoriesService newsCategoriesWithDAO = new NewsCategoriesServiceImpl(new NewsCategoriesDAOImpl());

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
		Response<Void> addCategoryResponse;
		try {
            ObjectMapper mapper = SingletonObjectMapper.getInstance();
            Map<String, List<String>> categoryWithKeywords = mapper.readValue(
                    request.getInputStream(), new TypeReference<Map<String, List<String>>>() {}
            );

            newsCategoriesWithDAO.addCategory(categoryWithKeywords);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            addCategoryResponse = new Response<>(true, "Category added successfully", Optional.empty());
        } catch (RuntimeException runTimeException) {
        	runTimeException.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            addCategoryResponse = new Response<>(false, runTimeException.getMessage(), Optional.empty());
        }
		buildResponse(response, addCategoryResponse);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private <T> void buildResponse(HttpServletResponse response, Response<T> apiResponse) {
		HttpServletResponseHelper.buildHttpServletResponse(response, apiResponse)
        .orElseGet(() -> {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        });
	}
}
