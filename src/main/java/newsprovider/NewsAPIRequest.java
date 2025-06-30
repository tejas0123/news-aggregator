package newsprovider;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.NewsArticle;

public interface NewsAPIRequest {
	HttpClient httpClient = HttpClient.newHttpClient();
	
	HttpRequest buildRequest(String uri, Map<String, String> headers);
	
	void fetchNewsHeadlines();
	
	List<NewsArticle> processAPIResponse(String jsonResponseData, String newsHeadlinesCategory);
	
	default void addHeaders(HttpRequest.Builder httpBuilder, Map<String, String> headers) {
	        headers.forEach(httpBuilder::header);
	}
	
	default Optional<String> sendNewsArticlesRequest(HttpRequest request){
        try {
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			System.out.println(response);
			if(response.statusCode() == 200) {
				return Optional.of(response.body());
			} else {
				return Optional.empty();
			}
		} catch (IOException | InterruptedException exception) {
			exception.printStackTrace();
			return Optional.empty();
		} 
	}
}
