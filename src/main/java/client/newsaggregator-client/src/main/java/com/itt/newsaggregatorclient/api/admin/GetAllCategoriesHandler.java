package com.itt.newsaggregatorclient.api.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.api.GetRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import com.itt.newsaggregatorclient.util.SingletonObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.itt.newsaggregatorclient.constants.Constants.BASE_URL;

public class GetAllCategoriesHandler implements GetRequestsHandler<Map<String, Integer>> {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(BASE_URL + uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object uriEndpointString) {
        URI getAllCategoriesUri = buildUri(uriEndpointString.toString());
        Map<String, String> headers = Map.of(Constants.AUTHORIZATION, Constants.BEARER+ JwtUtil.getToken().get());

        HttpRequest httpRequest = buildHttpRequest(getAllCategoriesUri, headers);
        return sendHttpRequest(httpRequest, new TypeReference<Response<Map<String, Integer>>>() {});
    }

    @Override
    public Optional<Map<String, Integer>> extractResponseData(String body) {
        try {
            ObjectMapper mapper = SingletonObjectMapper.getInstance();
            JsonNode rootNode = mapper.readTree(body);

            if (rootNode.has("data") && rootNode.get("data").isObject()) {
                JsonNode dataNode = rootNode.get("data");
                Map<String, Integer> result = mapper.convertValue(
                        dataNode,
                        new TypeReference<Map<String, Integer>>() {}
                );
                return Optional.of(result);
            } else {
                return Optional.empty();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return Optional.empty();
        }
    }

}
