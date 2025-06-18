package service;

import java.util.Optional;
import constants.Messages;
import dao.UserAuthenticationDAO;
import dto.Response;
import dto.UserCredentials;
import exception.UserNotFoundException;

public class UserAuthenticationServiceImpl implements UserAuthenticationService{
	private UserAuthenticationDAO userAuthDAO;
	
	public UserAuthenticationServiceImpl(UserAuthenticationDAO userAuthDAO) {
		this.userAuthDAO = userAuthDAO;
	}

	@Override
    public Response<Void> login(UserCredentials loginCredentials) {
        Optional<UserCredentials> userCredentialsOptional = userAuthDAO.fetchUserCredentials(loginCredentials);

        UserCredentials userCredentials = userCredentialsOptional.orElseThrow(() -> 
        	new UserNotFoundException(Messages.USER_NOT_FOUND));

        if (!userCredentials.isPasswordCorrect(loginCredentials.password())) {
            throw new UserNotFoundException(Messages.INCORRECT_CREDENTIALS);
        }
        
        return new Response<>(true, Messages.LOGIN_SUCCESSFUL, Optional.empty());
    }
}
