package newsprovider;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.Response;
import model.NewsArticle;
import model.NewsCategory;

public class NewsAPIRequestImpl implements NewsAPIRequest{
	
//	List<String> sources = new List.of("espn", "medical-news-today", "entertainment-weekly", "new-scientist", "the-wall-street-journal", "the-times-of-india", "");
//
//	Map<String, NewsCategory> sourceCategoryMappings = Map.of(
//		"espn", NewsCategory.SPORTS,
//		"the-wall-street-journal", 
//	)
			
	@Override
	public URI buildUri(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<Object> sendRequest(HttpRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NewsArticle> processAPIResponse(Response<Object> response) {
		// TODO Auto-generated method stub
		return null;
	}

}
