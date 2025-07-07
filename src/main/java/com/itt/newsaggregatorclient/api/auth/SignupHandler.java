package com.itt.newsaggregatorclient.api.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itt.newsaggregatorclient.api.PostRequestsHandler;
import com.itt.newsaggregatorclient.constants.Constants;
import com.itt.newsaggregatorclient.dto.APIResponse;
import com.itt.newsaggregatorclient.dto.Response;
import com.itt.newsaggregatorclient.dto.UserDetails;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;

public class SignupHandler implements PostRequestsHandler {
    @Override
    public URI buildUri(String uriString) {
        return URI.create(uriString + "/api/v1/auth/signup");
    }

    @Override
    public APIResponse sendAPIRequest(Object data) {
        UserDetails userDetails = (UserDetails)data;
        URI signupUri = buildUri(Constants.BASE_URL);

        HttpRequest httpRequest = buildHttpRequest(userDetails, signupUri, new HashMap<>());
        return sendHttpRequest(httpRequest, new TypeReference<Response<Void>>() {});
    }
}
