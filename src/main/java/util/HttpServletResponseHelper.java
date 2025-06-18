package util;

import java.io.IOException;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpServletResponseHelper {
	public static Optional<HttpServletResponse> buildHttpServletResponse(HttpServletResponse httpResponse, Response<Void> response) {
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
		String jsonResponseString;
		httpResponse.setContentType("application/json");
		httpResponse.setCharacterEncoding("UTF-8");
		
		try {
			jsonResponseString = mapper.writeValueAsString(response);
			httpResponse.getWriter().write(jsonResponseString);
			return Optional.of(httpResponse);
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return Optional.empty();
		}
	}
	
	public static <T> Optional<T> getRequestBody(HttpServletRequest request, Class<T> clazz){
		ObjectMapper mapper = SingletonObjectMapper.getInstance();
		
		try {
			return Optional.of(mapper.readValue(request.getReader(), clazz));
		} catch(IOException ioException) {
			ioException.printStackTrace();
			return Optional.empty();
		}
	}
}
