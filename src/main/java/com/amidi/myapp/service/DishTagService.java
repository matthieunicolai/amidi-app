package com.amidi.myapp.service;

import com.amidi.myapp.domain.DishTag;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DishTag}.
 */
public interface DishTagService {
    /**
     * Save a dishTag.
     *
     * @param dishTag the entity to save.
     * @return the persisted entity.
     */
    DishTag save(DishTag dishTag);

    /**
     * Partially updates a dishTag.
     *
     * @param dishTag the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DishTag> partialUpdate(DishTag dishTag);

    /**
     * Get all the dishTags.
     *
     * @return the list of entities.
     */
    List<DishTag> findAll();

    /**
     * Get the "id" dishTag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DishTag> findOne(Long id);

    /**
     * Delete the "id" dishTag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the dishTag corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<DishTag> search(String query);
}
