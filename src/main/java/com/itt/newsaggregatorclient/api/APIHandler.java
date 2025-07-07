package com.itt.newsaggregatorclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.SingletonObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;

public interface APIHandler{
    URI buildUri(String uriString);

    APIResponse sendAPIRequest(Object data);

    default void addHeaders(HttpRequest.Builder httpBuilder, Map<String, String> headers) {
        headers.forEach(httpBuilder::header);
    }

    default <T> APIResponse sendHttpRequest(HttpRequest httpRequest, TypeReference<Response<T>> typeReference) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(httpResponse);
            ObjectMapper mapper = SingletonObjectMapper.getInstance();
            String jsonString = httpResponse.body();

            Response<T> response = mapper.readValue(jsonString, typeReference);

            return new APIResponse(
                    httpResponse.statusCode(),
                    jsonString,
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
}
