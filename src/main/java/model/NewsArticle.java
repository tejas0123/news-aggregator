package model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class NewsArticle {
    private int articleId;
    private String title;

    @JsonProperty("content")
    private String article_body;

    private String description;
    private String url;

    @JsonProperty("publishedAt")
    private Instant publishedAt;

    private String sourceId;

    private String category;
    
    public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArticle_body(String article_body) {
		this.article_body = article_body;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

    @JsonProperty("source")
    public void unpackSource(JsonNode source) {
        this.sourceId = source.get("name").asText();
    }
	
	public int getArticleId() {
		return articleId;
	}

	public String getTitle() {
		return title;
	}

	public String getArticle_body() {
		return article_body;
	}

	public String getDescription() {
		return description;
	}
	
	public String getUrl() {
		return url;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public String getSourceId() {
		return sourceId;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
}
