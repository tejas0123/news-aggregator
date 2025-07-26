package com.itt.newsaggregatorclient.api.articles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.api.GetRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.NewsArticleData;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import com.itt.newsaggregatorclient.util.SingletonObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GetSavedArticlesHandler implements  GetRequestsHandler<List<NewsArticleData>> {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(Constants.BASE_URL + uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        URI getSavedArticlesUri = buildUri( "/api/v1/articles/saved");
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, Constants.BEARER + JwtUtil.getToken().get());
        HttpRequest httpRequest = buildHttpRequest(getSavedArticlesUri, headers);

        return sendHttpRequest(httpRequest, new TypeReference<Response<List<NewsArticleData>>>() {});
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
        } catch (IOException ioException){
            ioException.printStackTrace();
            return Optional.empty();
        }
    }
}
