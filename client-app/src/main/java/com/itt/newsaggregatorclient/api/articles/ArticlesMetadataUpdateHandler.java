package com.itt.newsaggregatorclient.api.articles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itt.newsaggregatorclient.api.PutRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.ArticleMetadata;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

public class ArticlesMetadataUpdateHandler implements PutRequestsHandler {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(Constants.BASE_URL + uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        List<ArticleMetadata> metadataList = (List<ArticleMetadata>) data;
        URI updateMetadataUri = buildUri("/api/v1/articles/metadata");
        Map<String, String> headers =  Map.of(Constants.AUTHORIZATION, Constants.BEARER + JwtUtil.getToken().get());
        HttpRequest httpRequest = buildHttpRequest(metadataList, updateMetadataUri, headers);
        return sendHttpRequest(httpRequest, new TypeReference<Response<Void>>() {});
    }
}
