package service;

import dto.Response;
import dto.UserCredentials;
import dto.UserDetails;
import jakarta.servlet.http.HttpServletResponse;

public interface UserAuthenticationService {
	Response<Void> login(UserCredentials userCredentials, HttpServletResponse response);
	Response<Void> signup(UserDetails userDetails);
}
