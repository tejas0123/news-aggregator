package com.itt.newsaggregatorclient.dto;

public class NewsArticleData {

	private int article_id;
	private String title;
	private String description;
	private String url;
	private int likes;
	private int dislikes;
	
	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislkies) {
		this.dislikes = dislkies;
	}
	
	public NewsArticleData() {
		
	}

}
