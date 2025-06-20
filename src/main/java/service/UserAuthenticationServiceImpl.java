package service;

import java.util.Optional;
import constants.Messages;
import dao.UserAuthenticationDAO;
import dto.Response;
import dto.UserCredentials;
import dto.UserDetails;
import exception.DAOException;
import exception.DuplicateUserException;
import exception.UserNotFoundException;

public class UserAuthenticationServiceImpl implements UserAuthenticationService{
	private UserAuthenticationDAO userAuthDAO;
	
	private static final String UNIQUE_CONSTRAINT_VIOLATION_STATE = "23505";
	
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


	@Override
	public Response<Void> signup(UserDetails userDetails) {
		try {
			int rowsInserted = userAuthDAO.addNewUser(userDetails);
			if(rowsInserted <= 0) {
				return new Response<>(false, Messages.USER_NOT_ADDED, Optional.empty());
			}
			return new Response<>(true, Messages.USER_SIGNUP_SUCCESSFUL, Optional.empty());
		} catch(DAOException daoException) {
			daoException.printStackTrace();
			if(daoException.getSqlState().equals(UNIQUE_CONSTRAINT_VIOLATION_STATE)) {
				throw new DuplicateUserException(Messages.USERNAME_EXISTS);
			} else {
				throw new RuntimeException(daoException.getMessage(), daoException.getCause());
			}
		}
	}
}
