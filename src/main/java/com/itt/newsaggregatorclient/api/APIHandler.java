package com.itt.newsaggregatorclient.api;

import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;

public interface APIHandler{
    URI buildUri();

    APIResponse sendAPIRequest(Object data);

    default void addHeaders(HttpRequest.Builder httpBuilder, Map<String, String> headers) {
        headers.forEach(httpBuilder::header);
    }

    default APIResponse sendHttpRequest(HttpRequest httpRequest){
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<Response<Object>> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return new APIResponse(
                    httpResponse.statusCode(),
                    httpResponse.body().message(),
                    httpResponse.body().isOperationSuccessful(),
                    httpResponse.body().
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
}
