package com.amidi.myapp.service;

import com.amidi.myapp.domain.Restaurant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Restaurant}.
 */
public interface RestaurantService {
    /**
     * Save a restaurant.
     *
     * @param restaurant the entity to save.
     * @return the persisted entity.
     */
    Restaurant save(Restaurant restaurant);

    /**
     * Partially updates a restaurant.
     *
     * @param restaurant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Restaurant> partialUpdate(Restaurant restaurant);

    /**
     * Get all the restaurants.
     *
     * @return the list of entities.
     */
    List<Restaurant> findAll();

    /**
     * Get the "id" restaurant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Restaurant> findOne(Long id);

    /**
     * Delete the "id" restaurant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the restaurant corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Restaurant> search(String query);
}
