package com.itt.newsaggregatorclient.api.articles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.api.GetRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.ArticleFilterParams;
import com.itt.newsaggregatorclient.dto.NewsArticleData;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import com.itt.newsaggregatorclient.util.SingletonObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class GetHeadlinesHandler implements GetRequestsHandler<List<NewsArticleData>> {

    @Override
    public URI buildUri(String uriString) {
        return URI.create(uriString);
    }

    public APIResponse sendAPIRequest(Object params) {
        ArticleFilterParams articleFilterParams = (ArticleFilterParams) params;

        StringBuilder uriBuilder = new StringBuilder(Constants.BASE_URL)
                .append("/api/v1/articles?")
                .append("from=").append(articleFilterParams.from().get())
                .append("&to=").append(articleFilterParams.to().get());

        articleFilterParams.category().ifPresent(category ->
                uriBuilder.append("&category=").append(category)
        );

        articleFilterParams.keyword().ifPresent(keyword ->
            uriBuilder.append("&keyword=").append(keyword)
        );

        URI uri = buildUri(uriBuilder.toString());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + JwtUtil.getToken());
        HttpRequest request = buildHttpRequest(uri, headers);

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(httpResponse);
            ObjectMapper mapper = SingletonObjectMapper.getInstance();

            Response<List<NewsArticleData>> response = mapper.readValue(
                    httpResponse.body(), new TypeReference<Response<List<NewsArticleData>>>() {}
            );

            return new APIResponse(
                    httpResponse.statusCode(),
                    httpResponse.body(),
                    response.isOperationSuccessful(),
                    response.message()
            );

        } catch (IOException | InterruptedException exception) {
            return new APIResponse(
                    500,
                    Arrays.toString(exception.getStackTrace()),
                    false,
                    exception.getMessage()
            );
        }
    }


    @Override
    public Optional<List<NewsArticleData>> extractResponseData(String body) {
        ObjectMapper mapper = SingletonObjectMapper.getInstance();

        try {
            JsonNode root = mapper.readTree(body);
            JsonNode dataNode = root.path("data");

            if (!dataNode.isArray()) return Optional.empty();

            List<NewsArticleData> articles = new ArrayList<>();

            for (JsonNode articleNode : dataNode) {
                NewsArticleData articleData = new NewsArticleData();
                articleData.setArticle_id(articleNode.path("article_id").asInt());
                articleData.setTitle(articleNode.path("title").asText(null));
                articleData.setDescription(articleNode.path("description").asText(null));
                articleData.setUrl(articleNode.path("url").asText(null));
                articleData.setLikes(articleNode.path("likes").asInt());
                articleData.setDislikes(articleNode.path("dislikes").asInt());
                articles.add(articleData);
            }

            return Optional.of(articles);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
