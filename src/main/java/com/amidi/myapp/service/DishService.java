package com.amidi.myapp.service;

import com.amidi.myapp.domain.Dish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Dish}.
 */
public interface DishService {
    /**
     * Save a dish.
     *
     * @param dish the entity to save.
     * @return the persisted entity.
     */
    Dish save(Dish dish);

    /**
     * Partially updates a dish.
     *
     * @param dish the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dish> partialUpdate(Dish dish);

    /**
     * Get all the dishes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dish> findAll(Pageable pageable);

    /**
     * Get the "id" dish.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dish> findOne(Long id);

    /**
     * Delete the "id" dish.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the dish corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dish> search(String query, Pageable pageable);
}
