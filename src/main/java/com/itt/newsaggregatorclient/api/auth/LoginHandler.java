package com.itt.newsaggregatorclient.api.auth;

import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;

public class LoginHandler implements PostRequestsHandler {

    @Override
    public URI buildUri() {
        String LOGIN_ENDPOINT = "/login";
        return URI.create(Constants.BASE_URL + LOGIN_ENDPOINT);
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        URI uri = buildUri();
        HttpRequest httpRequest = buildHttpRequest(data, uri, new HashMap<>());
        return sendHttpRequest(httpRequest);
    }
}
