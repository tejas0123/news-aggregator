package service;

import dto.Response;
import dto.UserCredentials;

public interface UserAuthenticationService {
	Response<Void> login(UserCredentials userCredentials);
}
