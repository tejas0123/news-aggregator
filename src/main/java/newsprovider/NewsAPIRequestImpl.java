package newsprovider;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
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

import dto.Response;
import model.NewsArticle;
import model.NewsCategory;
import util.SingletonObjectMapper;

public class NewsAPIRequestImpl implements NewsAPIRequest{
	
//	List<String> sources = new List.of("espn", "medical-news-today", "entertainment-weekly", "new-scientist", "the-wall-street-journal", "the-times-of-india", "");
//
//	Map<String, NewsCategory> sourceCategoryMappings = Map.of(
//		"espn", NewsCategory.SPORTS,
//		"the-wall-street-journal", 
//	)
	private final List<String> headlinesCategories = Arrays.asList("business", "entertainment", "sports", "technology");
	private final String ARTICLES_ARRAY_NODE = "articles";
	@Override
	public URI buildUri(String uri) {
		return URI.create(uri);
	}

	@Override
	public void fetchNewsHeadlines() {
		String BASE_URL = "https://newsapi.org/v2/top-headlines?apiKey=53451ca6dd194f9e9f33a220b806d584&category=";
		List<NewsArticle> newsHeadLines = new ArrayList<>();
		
		for(String headLineCategory : headlinesCategories) {
			URI requestUri = buildUri(BASE_URL + headLineCategory + URLEncoder.encode(headLineCategory, StandardCharsets.UTF_8));
			Optional<String> jsonResponseOptional = sendNewsArticlesRequest(requestUri, new HashMap<String, String>());
			if(!jsonResponseOptional.isPresent()) {
				continue;
			}
			String jsonResponseString = jsonResponseOptional.get();
			List<NewsArticle> newsHeadLinesList = processAPIResponse(jsonResponseString, headLineCategory);
			newsHeadLines.addAll(newsHeadLinesList);
		}
	}

	@Override
	public List<NewsArticle> processAPIResponse(String jsonResponseString, String headLineCategory) {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
		List<NewsArticle> articles = new ArrayList<>();
		
		try {
			JsonNode rootNode = mapper.readTree(jsonResponseString);
			ArrayNode articlesArray = (ArrayNode) rootNode.get(ARTICLES_ARRAY_NODE);
			
			for(JsonNode node: articlesArray) {
				NewsArticle article = mapper.treeToValue(node, NewsArticle.class);
				
	            articles.add(article);
			}
			
			return articles;
		} catch (JsonProcessingException jsonProcessingException) {
			jsonProcessingException.printStackTrace();
			return articles;
		}
	}
	
	private Instant getLatestFetchDate() {
		
	}

}
