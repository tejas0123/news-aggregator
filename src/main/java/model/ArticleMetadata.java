package model;

public class ArticleMetadata {
	
	private int article_id;
	private int likes;
	private int dislikes;
	private int reports;
	
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
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
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public int getReports() {
		return reports;
	}
	public void setReports(int reports) {
		this.reports = reports;
	}

}
