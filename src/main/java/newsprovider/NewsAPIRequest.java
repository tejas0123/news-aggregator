package newsprovider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dto.Response;
import model.NewsArticle;

public interface NewsAPIRequest {
	URI buildUri(String uri);
	
	void fetchNewsHeadlines();
	
	List<NewsArticle> processAPIResponse(String jsonResponseData, String newsHeadlinesCategory);
	
	HttpClient httpClient = HttpClient.newHttpClient();
	
	default void addHeaders(HttpRequest.Builder httpBuilder, Map<String, String> headers) {
	        headers.forEach(httpBuilder::header);
	}
	
	default Optional<String> sendNewsArticlesRequest(URI uri, Map<String, String> headers){
		HttpRequest.Builder httpBuilder = HttpRequest.newBuilder()
                .uri(uri)
                .GET();

        addHeaders(httpBuilder, headers);
        HttpRequest request = httpBuilder.build();

        try {
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
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
