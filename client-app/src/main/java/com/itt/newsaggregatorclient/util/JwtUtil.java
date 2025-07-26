package com.itt.newsaggregatorclient.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class JwtUtil {
    private static final String TOKEN_FILE_PATH = "token_store.txt";
    private static final String SECRET = "S3cUr3JwT_K3y@1234567890123456";
    private static final long EXPIRATION_TIME_MS = 86400000;

    public static void saveToken(String token) {
        try (FileWriter writer = new FileWriter(TOKEN_FILE_PATH)) {
            writer.write(token);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static Optional<String> getToken() {
        try {
            String token = Files.readString(Paths.get(TOKEN_FILE_PATH)).trim();
            return Optional.of(token);
        } catch (IOException noSuchFileException) {
            return Optional.empty();
        }
    }

    public static Claims validateTokenAndGetSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
}
