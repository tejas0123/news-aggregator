package com.itt.newsaggregatorclient.api.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.api.GetRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.dto.Server;
import com.itt.newsaggregatorclient.util.JwtUtil;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GetAllServersHandler implements GetRequestsHandler<List<Server>> {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(Constants.BASE_URL + uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object uriEndpoint) {
        URI getAllServersUri = buildUri(uriEndpoint.toString());
        Map<String, String> headers = Map.of(Constants.AUTHORIZATION, Constants.BEARER + JwtUtil.getToken().get());
        HttpRequest httpRequest = buildHttpRequest(getAllServersUri, headers);

        return sendHttpRequest(httpRequest,  new TypeReference<Response<List<Server>>>() {});
    }

    @Override
    public Optional<List<Server>> extractResponseData(String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(body);
            JsonNode dataNode = root.get("data");
            if (dataNode != null && dataNode.isArray()) {
                List<Server> servers = mapper.readValue(
                        dataNode.toString(),
                        new TypeReference<List<Server>>() {}
                );
                return Optional.of(servers);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

}
