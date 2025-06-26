package newsprovider;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import config.AppConfig;
import dao.NewsAPIDAO;
import dto.Response;
import exception.DAOException;
import model.NewsArticle;
import model.NewsCategory;
import util.SingletonObjectMapper;

public class NewsAPIRequestImpl implements NewsAPIRequest{

	private List<String> headlinesCategories;
	private final String ARTICLES_ARRAY_NODE = "articles";
	private final String X_API_KEY = "X-Api-Key";
	private final String API_KEY = "53451ca6dd194f9e9f33a220b806d584";
	private NewsAPIDAO newsAPIDAO;
	
	public NewsAPIRequestImpl(NewsAPIDAO newsAPIDAO) {
		this.newsAPIDAO = newsAPIDAO;
	}
	
	@Override
	public HttpRequest buildRequest(String uri, Map<String, String> headers) {
		URI requestUri = URI.create(uri);
		HttpRequest.Builder httpBuilder = HttpRequest.newBuilder()
                .uri(requestUri)
                .GET();

        addHeaders(httpBuilder, headers);
        return httpBuilder.build();
	}

	@Override
	public void fetchNewsHeadlines() {
		String BASE_URL = "https://newsapi.org/v2/top-headlines?category=";
		Map<String, String> jsonResponses = new HashMap<String, String>();
		List<NewsArticle> newsHeadLines = new ArrayList<>();
		Map<String, String> headers = Map.of(X_API_KEY, API_KEY);
		
		headlinesCategories = newsAPIDAO.getNewsCategories();
		System.out.println(headlinesCategories);
		for(String headLineCategory : headlinesCategories) {
			String uriString = BASE_URL + URLEncoder.encode(headLineCategory.toLowerCase(), StandardCharsets.UTF_8);
			HttpRequest request = buildRequest(uriString, headers);
			Optional<String> jsonResponseOptional = sendNewsArticlesRequest(request);

			if(!jsonResponseOptional.isPresent()) {
				System.out.println("Not present");
				continue;
			}

			String jsonResponseString = jsonResponseOptional.get();
			System.out.println("This is the jsonResponse string: " + jsonResponseString);
			jsonResponses.put(headLineCategory, jsonResponseString);
		}
		
		if(!jsonResponses.isEmpty()) {
			for(String category: jsonResponses.keySet()) {
				List<NewsArticle> newsHeadLinesList = processAPIResponse(category, jsonResponses.get(category));
				newsHeadLines.addAll(newsHeadLinesList);
			}
		}
		System.out.println("Printing news headlines: " + newsHeadLines.size());
		System.out.println("Printing news headlines: " + newsHeadLines);
	}

	@Override
	public List<NewsArticle> processAPIResponse(String headLineCategory, String jsonResponseString) {
	    System.out.println("Entered process method");
	    ObjectMapper mapper = SingletonObjectMapper.getInstance();
	    List<NewsArticle> articles = new ArrayList<>();

	    try {
	        JsonNode rootNode = mapper.readTree(jsonResponseString);
	        ArrayNode articlesArray = (ArrayNode) rootNode.get("articles");

	        for (JsonNode node : articlesArray) {
	            try {
	                NewsArticle article = new NewsArticle();

	                JsonNode titleNode = node.get("title");
	                if (titleNode != null && !titleNode.isNull()) {
	                    article.setTitle(titleNode.asText());
	                }

	                JsonNode contentNode = node.get("content");
	                if (contentNode != null && !contentNode.isNull()) {
	                    article.setArticle_body(contentNode.asText());
	                }

	                JsonNode descriptionNode = node.get("description");
	                if (descriptionNode != null && !descriptionNode.isNull()) {
	                    article.setDescription(descriptionNode.asText());
	                }

	                JsonNode urlNode = node.get("url");
	                if (urlNode != null && !urlNode.isNull()) {
	                    article.setUrl(urlNode.asText());
	                }

	                JsonNode publishedAtNode = node.get("publishedAt");
	                if (publishedAtNode != null && !publishedAtNode.isNull()) {
	                    try {
	                        article.setPublishedAt(Instant.parse(publishedAtNode.asText()));
	                    } catch (Exception e) {
	                        System.out.println("Failed to parse publishedAt: " + publishedAtNode.asText());
	                    }
	                }

	                JsonNode sourceNode = node.get("source");
	                if (sourceNode != null && !sourceNode.isNull()) {
	                    JsonNode nameNode = sourceNode.get("name");
	                    if (nameNode != null && !nameNode.isNull()) {
	                        article.setSourceId(nameNode.asText());
	                    }
	                }

	                article.setCategory(headLineCategory);
	                articles.add(article);

	            } catch (Exception e) {
	                System.out.println("Failed to parse article node: " + node);
	                e.printStackTrace();
	            }
	        }

	        System.out.println("Total articles parsed: " + articles.size());
	        return articles;

	    } catch (JsonProcessingException e) {
	        System.out.println("JSON parsing error");
	        e.printStackTrace();
	        return articles;
	    }
	}

    private Instant getLatestFetchDate() {
    	try {
    		Instant lastFetchedAt = newsAPIDAO.getArticleLastFetchedAt();
    		return lastFetchedAt;
    	} catch(DAOException daoException) {
    		daoException.printStackTrace();
    		return Instant.now();
    	}
    }

}
