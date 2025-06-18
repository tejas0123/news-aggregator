package dao;

import java.util.Optional;
import dto.UserCredentials;

public interface UserAuthenticationDAO {
	Optional<UserCredentials> fetchUserCredentials(UserCredentials loginCredentials);
}
