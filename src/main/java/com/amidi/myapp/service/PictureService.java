package com.amidi.myapp.service;

import com.amidi.myapp.domain.Picture;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Picture}.
 */
public interface PictureService {
    /**
     * Save a picture.
     *
     * @param picture the entity to save.
     * @return the persisted entity.
     */
    Picture save(Picture picture);

    /**
     * Partially updates a picture.
     *
     * @param picture the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Picture> partialUpdate(Picture picture);

    /**
     * Get all the pictures.
     *
     * @return the list of entities.
     */
    List<Picture> findAll();

    /**
     * Get the "id" picture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Picture> findOne(Long id);

    /**
     * Delete the "id" picture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the picture corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Picture> search(String query);
}
