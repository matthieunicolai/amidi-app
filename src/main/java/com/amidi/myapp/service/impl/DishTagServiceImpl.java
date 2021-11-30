package com.amidi.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.DishTag;
import com.amidi.myapp.repository.DishTagRepository;
import com.amidi.myapp.repository.search.DishTagSearchRepository;
import com.amidi.myapp.service.DishTagService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DishTag}.
 */
@Service
@Transactional
public class DishTagServiceImpl implements DishTagService {

    private final Logger log = LoggerFactory.getLogger(DishTagServiceImpl.class);

    private final DishTagRepository dishTagRepository;

    private final DishTagSearchRepository dishTagSearchRepository;

    public DishTagServiceImpl(DishTagRepository dishTagRepository, DishTagSearchRepository dishTagSearchRepository) {
        this.dishTagRepository = dishTagRepository;
        this.dishTagSearchRepository = dishTagSearchRepository;
    }

    @Override
    public DishTag save(DishTag dishTag) {
        log.debug("Request to save DishTag : {}", dishTag);
        DishTag result = dishTagRepository.save(dishTag);
        dishTagSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<DishTag> partialUpdate(DishTag dishTag) {
        log.debug("Request to partially update DishTag : {}", dishTag);

        return dishTagRepository
            .findById(dishTag.getId())
            .map(existingDishTag -> {
                if (dishTag.getDishTag() != null) {
                    existingDishTag.setDishTag(dishTag.getDishTag());
                }

                return existingDishTag;
            })
            .map(dishTagRepository::save)
            .map(savedDishTag -> {
                dishTagSearchRepository.save(savedDishTag);

                return savedDishTag;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishTag> findAll() {
        log.debug("Request to get all DishTags");
        return dishTagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DishTag> findOne(Long id) {
        log.debug("Request to get DishTag : {}", id);
        return dishTagRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DishTag : {}", id);
        dishTagRepository.deleteById(id);
        dishTagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishTag> search(String query) {
        log.debug("Request to search DishTags for query {}", query);
        return StreamSupport.stream(dishTagSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
