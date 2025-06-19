package newsprovider;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import dto.Response;
import model.NewsArticle;

public interface NewsAPIRequest {
	URI buildUri(String uri);
	Response<Object> sendRequest(HttpRequest request);
	List<NewsArticle> processAPIResponse(Response<Object> response);	
}
