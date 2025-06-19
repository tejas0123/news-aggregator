package servlets.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserAuthenticationService;
import util.HttpServletResponseHelper;

import java.io.IOException;
import java.util.Optional;

import config.AppConfig;
import constants.Messages;
import dto.Response;
import dto.UserCredentials;
import dto.UserDetails;
import exception.BadRequestException;
import exception.DuplicateUserException;

public class Signup extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleSignup(request, response);
	}

	private HttpServletResponse handleSignup(HttpServletRequest request, HttpServletResponse response){
		Response<Void> loginResponse;
		UserAuthenticationService userAuthService = AppConfig.getUserAuthServiceInstance();
		
		Optional<UserDetails> userDetailsOptional = HttpServletResponseHelper.getRequestBody(request, UserDetails.class);
		UserDetails userDetails = userDetailsOptional.orElseThrow(() -> new BadRequestException(Messages.INVALID_REQUEST_BODY));
		
		try {
			loginResponse = userAuthService.signup(userDetails);
			response.setStatus(201);
		} catch(DuplicateUserException exception) {
			loginResponse = new Response<>(false, Messages.USERNAME_EXISTS, Optional.empty());
			response.setStatus(401);
		} catch(RuntimeException exception) {
			response.setStatus(403);
			loginResponse = new Response<>(false, exception.getMessage(), Optional.empty());
		}
		
		return HttpServletResponseHelper.buildHttpServletResponse(response, loginResponse)
		        .orElseGet(() -> {
		            response.setStatus(500);
		            return response;
		        });
	}
}
