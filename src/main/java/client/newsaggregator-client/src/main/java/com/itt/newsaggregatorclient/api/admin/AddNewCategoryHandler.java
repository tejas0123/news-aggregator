package com.itt.newsaggregatorclient.api.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

public class AddNewCategoryHandler implements PostRequestsHandler {

    @Override
    public URI buildUri(String uriString) {
        return URI.create(Constants.BASE_URL + uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        URI uri = buildUri("/api/v1/admin/categories");

        Map<String, List<String>> categoryWithKeyWordsMap = (Map<String, List<String>>) data;
        Map<String, String> headers = Map.of(Constants.AUTHORIZATION, Constants.BEARER + JwtUtil.getToken().get());

        HttpRequest httpRequest = buildHttpRequest(categoryWithKeyWordsMap, uri, headers);
        return sendHttpRequest(httpRequest, new TypeReference<Response<Void>>() {});
    }
}
