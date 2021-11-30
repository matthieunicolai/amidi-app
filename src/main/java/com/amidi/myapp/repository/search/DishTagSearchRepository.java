package com.amidi.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.amidi.myapp.domain.DishTag;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DishTag} entity.
 */
public interface DishTagSearchRepository extends ElasticsearchRepository<DishTag, Long>, DishTagSearchRepositoryInternal {}

interface DishTagSearchRepositoryInternal {
    Stream<DishTag> search(String query);
}

class DishTagSearchRepositoryInternalImpl implements DishTagSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    DishTagSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<DishTag> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, DishTag.class).map(SearchHit::getContent).stream();
    }
}
