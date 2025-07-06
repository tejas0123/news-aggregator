package service;

import java.util.List;
import java.util.Set;
import dao.ArticlesMetadataDAO;
import model.ArticleMetadata;

public class ArticlesMetadataServiceImpl implements ArticlesMetadataService{
	private ArticlesMetadataDAO articlesMetadataDAO;

	public ArticlesMetadataServiceImpl(ArticlesMetadataDAO articlesMetadataDAO) {
		this.articlesMetadataDAO = articlesMetadataDAO;
	}

	@Override
	public List<ArticleMetadata> getArticlesMetadata(Set<Integer> articleIds) {
		return articlesMetadataDAO.getArticlesMetadata(articleIds);
	}

	@Override
	public void updateArticlesMetadata(List<ArticleMetadata> articlesMetadata) {
		articlesMetadataDAO.updateArticlesMetadata(articlesMetadata);
	}
}
