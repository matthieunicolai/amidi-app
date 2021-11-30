package com.amidi.myapp.service;

import com.amidi.myapp.domain.Hedgeprod;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Hedgeprod}.
 */
public interface HedgeprodService {
    /**
     * Save a hedgeprod.
     *
     * @param hedgeprod the entity to save.
     * @return the persisted entity.
     */
    Hedgeprod save(Hedgeprod hedgeprod);

    /**
     * Partially updates a hedgeprod.
     *
     * @param hedgeprod the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Hedgeprod> partialUpdate(Hedgeprod hedgeprod);

    /**
     * Get all the hedgeprods.
     *
     * @return the list of entities.
     */
    List<Hedgeprod> findAll();

    /**
     * Get the "id" hedgeprod.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Hedgeprod> findOne(Long id);

    /**
     * Delete the "id" hedgeprod.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the hedgeprod corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Hedgeprod> search(String query);
}
