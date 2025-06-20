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
import java.sql.ResultSet;
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
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = SingletonObjectMapper.getInstance();
            String jsonString = httpResponse.body();
            System.out.println(httpResponse);
            Response<Void> response = mapper.readValue(
                    jsonString,
                    new TypeReference<Response<Void>>() {}
            );

            System.out.println(response);

            return new APIResponse(
                    httpResponse.statusCode(),
                    httpResponse.body(),
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
