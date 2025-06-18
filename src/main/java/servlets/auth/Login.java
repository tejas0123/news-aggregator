package servlets.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserAuthenticationService;
import util.HttpServletResponseHelper;
import util.SingletonObjectMapper;
import java.io.IOException;
import java.util.Optional;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;
import constants.Messages;
import dto.Response;
import dto.UserCredentials;
import exception.BadRequestException;
import exception.DAOException;
import exception.UserNotFoundException;
	
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleLogin(request, response);
	}
	
	private HttpServletResponse handleLogin(HttpServletRequest request, HttpServletResponse response){
		Response<Void> loginResponse;
		UserAuthenticationService userAuthService = AppConfig.getUserAuthServiceInstance();
		
		Optional<UserCredentials> userCredentialsOptional = HttpServletResponseHelper.getRequestBody(request, UserCredentials.class);
        UserCredentials userCredentials = userCredentialsOptional.orElseThrow(() -> new BadRequestException(Messages.INVALID_REQUEST_BODY));
        
		try {
			loginResponse = userAuthService.login(userCredentials);
			response.setStatus(200);
		} catch(UserNotFoundException userNotFoundException) {
			loginResponse = new Response<>(false, Messages.INCORRECT_CREDENTIALS, Optional.empty());
			response.setStatus(401);
		} catch(DAOException daoException) {
			loginResponse = new Response<>(false, daoException.getMessage(), Optional.empty());
			response.setStatus(500);
		}
		
		return HttpServletResponseHelper.buildHttpServletResponse(response, loginResponse)
	        .orElseGet(() -> {
	            response.setStatus(500);
	            return response;
	        });
	};
}
