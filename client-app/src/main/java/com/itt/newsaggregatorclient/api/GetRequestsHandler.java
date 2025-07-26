package com.itt.newsaggregatorclient.api;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;
import java.util.Optional;

public interface GetRequestsHandler<T> extends APIHandler{
    Optional<T> extractResponseData(String body);
    default HttpRequest buildHttpRequest(URI uri, Map<String, String> headers){
        HttpRequest.Builder httpBuilder = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET();
        addHeaders(httpBuilder, headers);
        return httpBuilder.build();
    }
}
