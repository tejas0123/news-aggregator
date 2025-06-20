package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

import dto.UserCredentials;
 
public class JwtUtil {
	private static final String SECRET = "S3cUr3JwT_K3y@1234567890123456";
	private static final long EXPIRATION_TIME_MS = 86400000;
 
	public static String generateToken(UserCredentials credentials) {
		return Jwts.builder()
				.setSubject(String.valueOf(credentials.userId()))
				.claim("userId", credentials.userId())
				.claim("role", credentials.role())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
				.signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
				.compact();
	}
	public static Claims validateTokenAndGetSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
 
        return claims;
    }
}