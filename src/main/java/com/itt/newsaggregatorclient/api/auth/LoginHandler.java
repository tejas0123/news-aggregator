package com.itt.newsaggregatorclient.api.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import com.itt.newsaggregatorclient.util.SingletonObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class LoginHandler implements PostRequestsHandler {

    @Override
    public URI buildUri(String uriString) {
        String LOGIN_ENDPOINT = "/login";
        return URI.create(uriString + LOGIN_ENDPOINT);
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        URI uri = buildUri(Constants.BASE_URL);
        HttpRequest httpRequest = buildHttpRequest(data, uri, new HashMap<>());
        return sendHttpRequest(httpRequest, new TypeReference<Response<Void>>() {});
    }

    @Override
    public <T> APIResponse sendHttpRequest(HttpRequest httpRequest, TypeReference<Response<T>> typeReference){
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = SingletonObjectMapper.getInstance();
            String jsonString = httpResponse.body();
            HttpHeaders headers = httpResponse.headers();
            Optional<String> authHeader = headers.firstValue("Authorization");

            if (authHeader.isPresent()) {
                String token = authHeader.get().substring(7);
                System.out.println("Printing token: " + token);
                JwtUtil.saveToken(token);
            }

            System.out.println(httpResponse);
            Response<T> response = mapper.readValue(jsonString, typeReference);

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
