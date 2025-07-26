package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Server;
import service.ServerService;
import util.HttpServletResponseHelper;
import util.SingletonObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.AppConfig;
import dto.Response;
import dto.ServerDetails;

public class ServerOperations extends HttpServlet {
	
	ServerService serverService = AppConfig.getServerServiceInstance();
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Server> servers = serverService.getAllServers();
        Response<List<Server>> apiResponse = new Response<>(true, "Server details fetched successfully", Optional.of(servers));
        buildResponse(response, apiResponse);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = SingletonObjectMapper.getInstance();
        ServerDetails serverDetails = mapper.readValue(request.getInputStream(), ServerDetails.class);
        boolean updated = serverService.updateApiKey(serverDetails.server_id(), serverDetails.api_key());
        Response<Void> apiResponse = new Response<>(updated, updated ? "API key updated" : "Update failed", Optional.empty());
        response.setStatus(updated ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
        buildResponse(response, apiResponse);
    }

	private <T> void buildResponse(HttpServletResponse response, Response<T> serverResponse) {
		HttpServletResponseHelper.buildHttpServletResponse(response, serverResponse)
        .orElseGet(() -> {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return response;
        }); 
	}
}
