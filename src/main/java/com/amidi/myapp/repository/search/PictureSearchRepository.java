package com.amidi.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.amidi.myapp.domain.Picture;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Picture} entity.
 */
public interface PictureSearchRepository extends ElasticsearchRepository<Picture, Long>, PictureSearchRepositoryInternal {}

interface PictureSearchRepositoryInternal {
    Stream<Picture> search(String query);
}

class PictureSearchRepositoryInternalImpl implements PictureSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PictureSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Picture> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Picture.class).map(SearchHit::getContent).stream();
    }
}
