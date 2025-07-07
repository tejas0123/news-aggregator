package com.itt.newsaggregatorclient.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.util.SingletonObjectMapper;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;

public interface DeleteRequestsHandler extends APIHandler {
    default HttpRequest buildHttpRequest(URI uri, Map<String, String> headers){
        HttpRequest.Builder httpBuilder = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .DELETE();
        addHeaders(httpBuilder, headers);
        return httpBuilder.build();
    }

    default String prepareRequestBody(Object body) {
        ObjectMapper mapper = SingletonObjectMapper.getInstance();
        try {
            System.out.println("The json string is: " + mapper.writeValueAsString(body));
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
            return "";
        }
    }
}
