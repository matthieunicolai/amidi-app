package com.amidi.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.amidi.myapp.domain.Dish;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Dish} entity.
 */
public interface DishSearchRepository extends ElasticsearchRepository<Dish, Long>, DishSearchRepositoryInternal {}

interface DishSearchRepositoryInternal {
    Page<Dish> search(String query, Pageable pageable);
}

class DishSearchRepositoryInternalImpl implements DishSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    DishSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Dish> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Dish> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Dish.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
