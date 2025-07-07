package com.itt.newsaggregatorclient.api.articles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itt.newsaggregatorclient.api.DeleteRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UnsaveArticlesHandler implements DeleteRequestsHandler {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        Set<Integer> articleIds = (Set<Integer>) data;
        String uriString = Constants.BASE_URL + "/api/v1/articles/saved";

        String paramString = articleIds.stream()
                .map(id -> "articleId=" + id)
                .collect(Collectors.joining("&"));

        String finalUri = uriString + "?" + paramString;
        URI unsaveArticlesUri = buildUri(finalUri);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + JwtUtil.getToken().get());

        HttpRequest httpRequest = buildHttpRequest(unsaveArticlesUri, headers);
        return sendHttpRequest(httpRequest,  new TypeReference<Response<Void>>() {});
    }
}
