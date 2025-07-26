package com.itt.newsaggregatorclient.api.notifications;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itt.newsaggregatorclient.api.DeleteRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.util.JwtUtil;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;

public class DeletePreferenceHandler implements DeleteRequestsHandler {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(Constants.BASE_URL + uriString);
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        URI deletePreferencesUri = buildUri("/api/v1/notification/preference/category?category=" + data.toString().toLowerCase());
        Map<String, String> headers = Map.of(Constants.AUTHORIZATION, Constants.BEARER+ JwtUtil.getToken().get());

        HttpRequest httpRequest = buildHttpRequest(deletePreferencesUri, headers);
        return sendHttpRequest(httpRequest, new TypeReference<Response<Void>>() {});
    }
}
