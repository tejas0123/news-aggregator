package com.itt.newsaggregatorclient.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import com.itt.newsaggregatorclient.util.SingletonObjectMapper;
import java.net.http.HttpRequest;
import java.util.Map;

public interface PatchRequestsHandler extends APIHandler {
    default HttpRequest buildHttpRequest(Object data, URI uri, Map<String, String> headers) {
        String jsonRequestString = prepareRequestBody(data);
        HttpRequest.Builder httpBuilder = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonRequestString));
        addHeaders(httpBuilder, headers);
        return httpBuilder.build();
    }

    default String prepareRequestBody(Object body) {
        ObjectMapper mapper = SingletonObjectMapper.getInstance();
        try {
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
            return "";
        }
    }
}
