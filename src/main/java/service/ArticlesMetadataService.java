package service;

import java.util.List;
import java.util.Set;
import model.ArticleMetadata;

public interface ArticlesMetadataService {
	List<ArticleMetadata> getArticlesMetadata(Set<Integer> articleIds);
	void updateArticlesMetadata(List<ArticleMetadata> articlesMetadata);
}
