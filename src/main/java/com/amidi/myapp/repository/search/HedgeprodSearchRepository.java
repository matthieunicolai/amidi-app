package com.amidi.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.amidi.myapp.domain.Hedgeprod;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Hedgeprod} entity.
 */
public interface HedgeprodSearchRepository extends ElasticsearchRepository<Hedgeprod, Long>, HedgeprodSearchRepositoryInternal {}

interface HedgeprodSearchRepositoryInternal {
    Stream<Hedgeprod> search(String query);
}

class HedgeprodSearchRepositoryInternalImpl implements HedgeprodSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    HedgeprodSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Hedgeprod> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Hedgeprod.class).map(SearchHit::getContent).stream();
    }
}
