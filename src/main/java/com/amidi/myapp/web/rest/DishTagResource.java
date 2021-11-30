package com.amidi.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.DishTag;
import com.amidi.myapp.repository.DishTagRepository;
import com.amidi.myapp.service.DishTagService;
import com.amidi.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.amidi.myapp.domain.DishTag}.
 */
@RestController
@RequestMapping("/api")
public class DishTagResource {

    private final Logger log = LoggerFactory.getLogger(DishTagResource.class);

    private static final String ENTITY_NAME = "dishTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DishTagService dishTagService;

    private final DishTagRepository dishTagRepository;

    public DishTagResource(DishTagService dishTagService, DishTagRepository dishTagRepository) {
        this.dishTagService = dishTagService;
        this.dishTagRepository = dishTagRepository;
    }

    /**
     * {@code POST  /dish-tags} : Create a new dishTag.
     *
     * @param dishTag the dishTag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dishTag, or with status {@code 400 (Bad Request)} if the dishTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dish-tags")
    public ResponseEntity<DishTag> createDishTag(@Valid @RequestBody DishTag dishTag) throws URISyntaxException {
        log.debug("REST request to save DishTag : {}", dishTag);
        if (dishTag.getId() != null) {
            throw new BadRequestAlertException("A new dishTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DishTag result = dishTagService.save(dishTag);
        return ResponseEntity
            .created(new URI("/api/dish-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dish-tags/:id} : Updates an existing dishTag.
     *
     * @param id the id of the dishTag to save.
     * @param dishTag the dishTag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dishTag,
     * or with status {@code 400 (Bad Request)} if the dishTag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dishTag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dish-tags/{id}")
    public ResponseEntity<DishTag> updateDishTag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DishTag dishTag
    ) throws URISyntaxException {
        log.debug("REST request to update DishTag : {}, {}", id, dishTag);
        if (dishTag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dishTag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dishTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DishTag result = dishTagService.save(dishTag);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dishTag.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dish-tags/:id} : Partial updates given fields of an existing dishTag, field will ignore if it is null
     *
     * @param id the id of the dishTag to save.
     * @param dishTag the dishTag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dishTag,
     * or with status {@code 400 (Bad Request)} if the dishTag is not valid,
     * or with status {@code 404 (Not Found)} if the dishTag is not found,
     * or with status {@code 500 (Internal Server Error)} if the dishTag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dish-tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DishTag> partialUpdateDishTag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DishTag dishTag
    ) throws URISyntaxException {
        log.debug("REST request to partial update DishTag partially : {}, {}", id, dishTag);
        if (dishTag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dishTag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dishTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DishTag> result = dishTagService.partialUpdate(dishTag);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dishTag.getId().toString())
        );
    }

    /**
     * {@code GET  /dish-tags} : get all the dishTags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dishTags in body.
     */
    @GetMapping("/dish-tags")
    public List<DishTag> getAllDishTags() {
        log.debug("REST request to get all DishTags");
        return dishTagService.findAll();
    }

    /**
     * {@code GET  /dish-tags/:id} : get the "id" dishTag.
     *
     * @param id the id of the dishTag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dishTag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dish-tags/{id}")
    public ResponseEntity<DishTag> getDishTag(@PathVariable Long id) {
        log.debug("REST request to get DishTag : {}", id);
        Optional<DishTag> dishTag = dishTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dishTag);
    }

    /**
     * {@code DELETE  /dish-tags/:id} : delete the "id" dishTag.
     *
     * @param id the id of the dishTag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dish-tags/{id}")
    public ResponseEntity<Void> deleteDishTag(@PathVariable Long id) {
        log.debug("REST request to delete DishTag : {}", id);
        dishTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/dish-tags?query=:query} : search for the dishTag corresponding
     * to the query.
     *
     * @param query the query of the dishTag search.
     * @return the result of the search.
     */
    @GetMapping("/_search/dish-tags")
    public List<DishTag> searchDishTags(@RequestParam String query) {
        log.debug("REST request to search DishTags for query {}", query);
        return dishTagService.search(query);
    }
}
