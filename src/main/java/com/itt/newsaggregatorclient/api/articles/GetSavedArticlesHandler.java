package com.itt.newsaggregatorclient.api.articles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetSavedArticlesHandler implements PostRequestsHandler {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(uriString + "/api/v1/articles/saved");
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        Set<Integer> userSavedArticleIds = (Set<Integer>)data;
        URI saveArticlesUri = buildUri(Constants.BASE_URL);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + JwtUtil.getToken().get());
        HttpRequest httpRequest = buildHttpRequest(userSavedArticleIds, saveArticlesUri, headers);
        return sendHttpRequest(httpRequest, new TypeReference<Response<Void>>() {});
    }
}
