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
import java.util.stream.Collectors;

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
	private final String ARTICLES_ARRAY_NODE = "articles";
	private final String X_API_KEY = "X-Api-Key";
	private final String API_KEY = "53451ca6dd194f9e9f33a220b806d584";
	private NewsAPIDAO newsAPIDAO;
	private Map<String, Integer> headlinesCategories;
	
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
		List<NewsArticle> articlesToCategorise = new ArrayList<>();
		Map<String, String> headers = Map.of(X_API_KEY, API_KEY);
		
		headlinesCategories = newsAPIDAO.getNewsCategories();
		Instant lastFetchedAt = getLatestFetchDate();
		
		System.out.println(headlinesCategories.keySet());
		for(String headLineCategory : headlinesCategories.keySet()) {
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
			System.out.println("Json responses size: " + jsonResponses.size());
		}
		
		if(!jsonResponses.isEmpty()) {
			for(String category: jsonResponses.keySet()) {
				List<NewsArticle> newsHeadLinesList = processAPIResponse(category, jsonResponses.get(category));
				List<NewsArticle> latestPublishedHeadlines = filterArticlesWithMissingData(newsHeadLinesList, lastFetchedAt);
				if(category == "general") {
					articlesToCategorise.addAll(latestPublishedHeadlines);
				}
				newsHeadLines.addAll(latestPublishedHeadlines);
			}
		}
		
		//newsAPIDAO.insertNewsArticles(newsHeadLines, headlinesCategories);
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
	            articles.add(buildNewsArticle(node, headLineCategory));
	        }

	        System.out.println("Total articles parsed: " + articles.size());
	        return articles;

	    } catch (JsonProcessingException exception) {
	        System.out.println("JSON parsing error");
	        exception.printStackTrace();
	        return articles;
	    }
	}
	
	private NewsArticle buildNewsArticle(JsonNode node, String headLineCategory) {
		NewsArticle article = new NewsArticle();
		
		try {
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
                } catch (Exception exception) {
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
            return article;

        } catch (Exception exception) {
            System.out.println("Failed to parse article node: " + node);
            exception.printStackTrace();
            return article;
        }
	}
	
	private List<NewsArticle> filterArticlesWithMissingData(List<NewsArticle> articles, Instant lastFetchedAt){
		return articles.stream()
	    .filter(article -> article.getTitle() != null
	                    && article.getDescription() != null
	                    && article.getUrl() != null
	                    && article.getPublishedAt().isAfter(lastFetchedAt))
	    .collect(Collectors.toList());
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
