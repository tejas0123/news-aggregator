package dao;

import java.util.List;
import java.util.Set;

import model.ArticleMetadata;

public interface ArticlesMetadataDAO {
    void updateArticlesMetadata(List<ArticleMetadata> metadataList);
    List<ArticleMetadata> getArticlesMetadata(Set<Integer> articleIds);
}
