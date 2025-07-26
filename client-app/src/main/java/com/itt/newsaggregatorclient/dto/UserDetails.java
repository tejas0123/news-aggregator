package com.itt.newsaggregatorclient.dto;

public record UserDetails(
        String firstName,
        String lastName,
        String email,
        Gender gender,
        String password
) {

}
