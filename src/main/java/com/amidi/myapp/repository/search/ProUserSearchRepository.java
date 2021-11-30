package com.amidi.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.amidi.myapp.domain.ProUser;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProUser} entity.
 */
public interface ProUserSearchRepository extends ElasticsearchRepository<ProUser, Long>, ProUserSearchRepositoryInternal {}

interface ProUserSearchRepositoryInternal {
    Stream<ProUser> search(String query);
}

class ProUserSearchRepositoryInternalImpl implements ProUserSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ProUserSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<ProUser> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, ProUser.class).map(SearchHit::getContent).stream();
    }
}
