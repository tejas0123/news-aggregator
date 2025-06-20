package dto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;

import model.Gender;

public record UserDetails(
		String firstName,
	    String lastName,
	    String email,
	    Gender gender,
	    LocalDate dateOfBirth,
	    String phone,
	    String password
) {
	public String getPasswordHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(this.password.getBytes(StandardCharsets.UTF_8));

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
