package com.itt.newsaggregatorclient.dto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public record UserCredentials(String email, String password) {
    public boolean isPasswordCorrect(String enteredPassword){
        return this.password().equals(getPasswordHash(enteredPassword));
    }

    private String getPasswordHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte dataByte : hash) {
                String hex = Integer.toHexString(0xff & dataByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return "";
        }
    }
}
