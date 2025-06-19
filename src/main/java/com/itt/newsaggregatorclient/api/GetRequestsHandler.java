package com.itt.newsaggregatorclient.api;

import java.util.Optional;

public interface GetRequestsHandler<T> extends APIHandler{
    Optional<T> extractResponseData(String body);
}
