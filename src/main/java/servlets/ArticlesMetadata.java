package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ArticleMetadata;
import service.ArticlesMetadataService;
import util.HttpServletResponseHelper;
import util.SingletonObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;
import dto.Response;

public class ArticlesMetadata extends HttpServlet {
	private ArticlesMetadataService articlesMetadataService = AppConfig.getArticlesMetadataServiceInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
		Response<List<ArticleMetadata>> getArticleMetadataResponse;
	
		Set<Integer> articleIds = mapper.readValue(
				request.getInputStream(), 
				new TypeReference<Set<Integer>>() {}
		);
		
		try {
			List<ArticleMetadata> articlesMetadata = articlesMetadataService.getArticlesMetadata(articleIds);
			response.setStatus(HttpServletResponse.SC_OK);
			getArticleMetadataResponse = new Response<>(true, "Metadata fetched successfully", Optional.of(articlesMetadata));
			
		} catch(RuntimeException runTimeException) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			getArticleMetadataResponse = new Response<>(false, runTimeException.getMessage(), Optional.empty());
		}
		
		buildResponse(response, getArticleMetadataResponse);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
		Response<Void> updateArticlesMetadataResponse;
		
		List<ArticleMetadata> metadata = mapper.readValue(
				request.getInputStream(), 
				new TypeReference<List<ArticleMetadata>>() {}
		);
		
		try {
			articlesMetadataService.updateArticlesMetadata(metadata);
			response.setStatus(HttpServletResponse.SC_CREATED);
			updateArticlesMetadataResponse = new Response<>(true, "Updated Successfully", Optional.empty());
		} catch(RuntimeException runTimeException) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			updateArticlesMetadataResponse = new Response<>(false, runTimeException.getMessage(), Optional.empty());
		}
		
		buildResponse(response, updateArticlesMetadataResponse);
	}
	
	private <T> void buildResponse(HttpServletResponse response, Response<T> apiResponse) {
		HttpServletResponseHelper.buildHttpServletResponse(response, apiResponse)
        .orElseGet(() -> {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        });
	}

}
