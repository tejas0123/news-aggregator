package dao;

import java.util.Optional;
import dto.UserCredentials;
import dto.UserDetails;

public interface UserAuthenticationDAO {
	Optional<UserCredentials> fetchUserCredentials(UserCredentials loginCredentials);
	int addNewUser(UserDetails userDetails);
}
