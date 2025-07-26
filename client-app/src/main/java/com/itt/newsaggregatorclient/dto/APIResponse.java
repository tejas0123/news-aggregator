package com.itt.newsaggregatorclient.dto;

public record APIResponse(
        int statusCode,
        String body,
        boolean success,
        String message
) {}
