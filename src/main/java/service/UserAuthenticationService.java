package service;

import dto.Response;
import dto.UserCredentials;
import dto.UserDetails;

public interface UserAuthenticationService {
	Response<Void> login(UserCredentials userCredentials);
	Response<Void> signup(UserDetails userDetails);
}
