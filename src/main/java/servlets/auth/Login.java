package servlets.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserAuthenticationService;
import util.HttpServletResponseHelper;
import util.JwtUtil;

import java.io.IOException;
import java.util.Optional;
import config.AppConfig;
import constants.Messages;
import dto.Response;
import dto.UserCredentials;
import exception.BadRequestException;
import exception.DAOException;
import exception.UserNotFoundException;
	
public class Login extends HttpServlet {
	private final String AUTHORIZATION = "Authorization";
	private final String BEARER = "Bearer ";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleLogin(request, response);
	}
	
	private void handleLogin(HttpServletRequest request, HttpServletResponse response){
		Response<Void> loginResponse;
		UserAuthenticationService userAuthService = AppConfig.getUserAuthServiceInstance();
		
		Optional<UserCredentials> userCredentialsOptional = HttpServletResponseHelper.getRequestBody(request, UserCredentials.class);
        UserCredentials userCredentials = userCredentialsOptional.orElseThrow(() -> new BadRequestException(Messages.INVALID_REQUEST_BODY));
        System.out.println(userCredentials);
		try {
			loginResponse = userAuthService.login(userCredentials);
			String jwtToken = JwtUtil.generateToken(userCredentials);
			response.setHeader(AUTHORIZATION, BEARER + jwtToken);
			response.setStatus(200);
		} catch(UserNotFoundException userNotFoundException) {
			loginResponse = new Response<>(false, Messages.INCORRECT_CREDENTIALS, Optional.empty());
			response.setStatus(401);
		} catch(DAOException daoException) {
			loginResponse = new Response<>(false, daoException.getMessage(), Optional.empty());
			response.setStatus(500);
		}
		
		HttpServletResponseHelper.buildHttpServletResponse(response, loginResponse)
	        .orElseGet(() -> {
	            response.setStatus(500);
	            return response;
	        });
	};
}
