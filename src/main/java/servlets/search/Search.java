package servlets.search;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.NewsArticle;
import model.SearchParams;
import service.SearchService;
import util.HttpServletResponseHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import config.AppConfig;
import dto.Response;

public class Search extends HttpServlet {
	private SearchService searchService = AppConfig.getSearchServiceInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchParams searchParams = getSearchParams(request);
		List<NewsArticle> articles = searchService.getArticles(searchParams);
		Response<List<NewsArticle>> searchResponse;
		
		if(articles.isEmpty()) {
			searchResponse = new Response<>(true, "No articles found", Optional.empty());
		}
		searchResponse = new Response<>(true, "", Optional.of(articles));
		response.setStatus(200);
		
		HttpServletResponseHelper.buildHttpServletResponse(response, searchResponse)
        .orElseGet(() -> {
            response.setStatus(500);
            return response;
        });
	}
	
	private SearchParams getSearchParams(HttpServletRequest request) {
		Map<String, String[]>params = request.getParameterMap();
		
		Optional<String> category = getOptionalParam(request, "category");
		Optional<String> keyword = getOptionalParam(request, "keyword");
		Optional<LocalDate> from = getOptionalDateParam(request, "from");
		Optional<LocalDate> to = getOptionalDateParam(request, "to");
		
		return new SearchParams(category, keyword, from, to);
	}
	
	private Optional<String> getOptionalParam(HttpServletRequest request, String paramName) {
	    String value = request.getParameter(paramName);
	    return (value != null && !value.isBlank()) ? Optional.of(value) : Optional.empty();
	}
	
	private Optional<LocalDate> getOptionalDateParam(HttpServletRequest request, String paramName){
		String value = request.getParameter(paramName);
		return (value != null && !value.isBlank()) ? Optional.of(LocalDate.parse(value)) : Optional.empty();
	}
}
