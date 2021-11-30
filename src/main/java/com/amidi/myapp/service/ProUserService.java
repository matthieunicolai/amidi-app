package com.amidi.myapp.service;

import com.amidi.myapp.domain.ProUser;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ProUser}.
 */
public interface ProUserService {
    /**
     * Save a proUser.
     *
     * @param proUser the entity to save.
     * @return the persisted entity.
     */
    ProUser save(ProUser proUser);

    /**
     * Partially updates a proUser.
     *
     * @param proUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProUser> partialUpdate(ProUser proUser);

    /**
     * Get all the proUsers.
     *
     * @return the list of entities.
     */
    List<ProUser> findAll();

    /**
     * Get the "id" proUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProUser> findOne(Long id);

    /**
     * Delete the "id" proUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the proUser corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<ProUser> search(String query);
}
