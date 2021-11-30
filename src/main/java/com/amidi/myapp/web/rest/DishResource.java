package com.amidi.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.Dish;
import com.amidi.myapp.repository.DishRepository;
import com.amidi.myapp.service.DishService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.amidi.myapp.domain.Dish}.
 */
@RestController
@RequestMapping("/api")
public class DishResource {

    private final Logger log = LoggerFactory.getLogger(DishResource.class);

    private static final String ENTITY_NAME = "dish";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DishService dishService;

    private final DishRepository dishRepository;

    public DishResource(DishService dishService, DishRepository dishRepository) {
        this.dishService = dishService;
        this.dishRepository = dishRepository;
    }

    /**
     * {@code POST  /dishes} : Create a new dish.
     *
     * @param dish the dish to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dish, or with status {@code 400 (Bad Request)} if the dish has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dishes")
    public ResponseEntity<Dish> createDish(@Valid @RequestBody Dish dish) throws URISyntaxException {
        log.debug("REST request to save Dish : {}", dish);
        if (dish.getId() != null) {
            throw new BadRequestAlertException("A new dish cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dish result = dishService.save(dish);
        return ResponseEntity
            .created(new URI("/api/dishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dishes/:id} : Updates an existing dish.
     *
     * @param id the id of the dish to save.
     * @param dish the dish to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dish,
     * or with status {@code 400 (Bad Request)} if the dish is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dishes/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Dish dish)
        throws URISyntaxException {
        log.debug("REST request to update Dish : {}, {}", id, dish);
        if (dish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dish.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dish result = dishService.save(dish);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dish.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dishes/:id} : Partial updates given fields of an existing dish, field will ignore if it is null
     *
     * @param id the id of the dish to save.
     * @param dish the dish to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dish,
     * or with status {@code 400 (Bad Request)} if the dish is not valid,
     * or with status {@code 404 (Not Found)} if the dish is not found,
     * or with status {@code 500 (Internal Server Error)} if the dish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dishes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dish> partialUpdateDish(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Dish dish
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dish partially : {}, {}", id, dish);
        if (dish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dish.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dish> result = dishService.partialUpdate(dish);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dish.getId().toString())
        );
    }

    /**
     * {@code GET  /dishes} : get all the dishes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dishes in body.
     */
    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getAllDishes(Pageable pageable) {
        log.debug("REST request to get a page of Dishes");
        Page<Dish> page = dishService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dishes/:id} : get the "id" dish.
     *
     * @param id the id of the dish to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dish, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dishes/{id}")
    public ResponseEntity<Dish> getDish(@PathVariable Long id) {
        log.debug("REST request to get Dish : {}", id);
        Optional<Dish> dish = dishService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dish);
    }

    /**
     * {@code DELETE  /dishes/:id} : delete the "id" dish.
     *
     * @param id the id of the dish to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dishes/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        log.debug("REST request to delete Dish : {}", id);
        dishService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/dishes?query=:query} : search for the dish corresponding
     * to the query.
     *
     * @param query the query of the dish search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/dishes")
    public ResponseEntity<List<Dish>> searchDishes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Dishes for query {}", query);
        Page<Dish> page = dishService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
