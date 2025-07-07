package com.itt.newsaggregatorclient.dto;

import com.itt.newsaggregatorclient.dto.Gender;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public record UserDetails(
        String firstName,
        String lastName,
        String email,
        Gender gender,
        String password
) {

}
