package com.itt.newsaggregatorclient.api.notifications;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.api.GetRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class GetUserPreferencesHandler implements GetRequestsHandler<Set<String>> {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(Constants.BASE_URL + uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object uriEndpoint) {
        URI getUserPreferencesUri = buildUri(uriEndpoint.toString());
        Map<String, String> headers = Map.of(Constants.AUTHORIZATION, Constants.BEARER+ JwtUtil.getToken().get());
        HttpRequest httpRequest = buildHttpRequest(getUserPreferencesUri, headers);
        return sendHttpRequest(httpRequest, new TypeReference<Response<Set<String>>>(){});
    }

    @Override
    public Optional<Set<String>> extractResponseData(String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(body);

            JsonNode dataNode = rootNode.get("data");
            if (dataNode != null && dataNode.isArray()) {
                Set<String> preferences = new HashSet<>();
                for (JsonNode node : dataNode) {
                    preferences.add(node.asText());
                }
                return Optional.of(preferences);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }
}
