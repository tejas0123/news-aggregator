package com.itt.newsaggregatorclient.api.notifications;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsaggregatorclient.api.GetRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.NotificationDTO;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;
import com.itt.newsaggregatorclient.util.SingletonObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GetUserNotificationsHandler implements GetRequestsHandler<List<NotificationDTO>> {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(Constants.BASE_URL + uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object uriEndpoint) {
        URI getUserNotificationsUri = buildUri(uriEndpoint.toString());
        Map<String, String> headers = Map.of(Constants.AUTHORIZATION, Constants.BEARER+ JwtUtil.getToken().get());
        HttpRequest httpRequest = buildHttpRequest(getUserNotificationsUri, headers);
        return sendHttpRequest(httpRequest, new TypeReference<Response<List<NotificationDTO>>>(){});
    }

    @Override
    public Optional<List<NotificationDTO>> extractResponseData(String body) {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();

        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode dataNode = root.get("data");

            if (dataNode != null && dataNode.isArray()) {
                List<NotificationDTO> notifications = new ArrayList<>();

                for (JsonNode node : dataNode) {
                    NotificationDTO notification = objectMapper.treeToValue(node, NotificationDTO.class);
                    notifications.add(notification);
                }

                return Optional.of(notifications);
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return Optional.empty();
    }
}
