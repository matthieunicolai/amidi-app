package com.amidi.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.Hedgeprod;
import com.amidi.myapp.repository.HedgeprodRepository;
import com.amidi.myapp.service.HedgeprodService;
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
 * REST controller for managing {@link com.amidi.myapp.domain.Hedgeprod}.
 */
@RestController
@RequestMapping("/api")
public class HedgeprodResource {

    private final Logger log = LoggerFactory.getLogger(HedgeprodResource.class);

    private static final String ENTITY_NAME = "hedgeprod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HedgeprodService hedgeprodService;

    private final HedgeprodRepository hedgeprodRepository;

    public HedgeprodResource(HedgeprodService hedgeprodService, HedgeprodRepository hedgeprodRepository) {
        this.hedgeprodService = hedgeprodService;
        this.hedgeprodRepository = hedgeprodRepository;
    }

    /**
     * {@code POST  /hedgeprods} : Create a new hedgeprod.
     *
     * @param hedgeprod the hedgeprod to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hedgeprod, or with status {@code 400 (Bad Request)} if the hedgeprod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hedgeprods")
    public ResponseEntity<Hedgeprod> createHedgeprod(@Valid @RequestBody Hedgeprod hedgeprod) throws URISyntaxException {
        log.debug("REST request to save Hedgeprod : {}", hedgeprod);
        if (hedgeprod.getId() != null) {
            throw new BadRequestAlertException("A new hedgeprod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hedgeprod result = hedgeprodService.save(hedgeprod);
        return ResponseEntity
            .created(new URI("/api/hedgeprods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hedgeprods/:id} : Updates an existing hedgeprod.
     *
     * @param id the id of the hedgeprod to save.
     * @param hedgeprod the hedgeprod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hedgeprod,
     * or with status {@code 400 (Bad Request)} if the hedgeprod is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hedgeprod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hedgeprods/{id}")
    public ResponseEntity<Hedgeprod> updateHedgeprod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Hedgeprod hedgeprod
    ) throws URISyntaxException {
        log.debug("REST request to update Hedgeprod : {}, {}", id, hedgeprod);
        if (hedgeprod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hedgeprod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hedgeprodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Hedgeprod result = hedgeprodService.save(hedgeprod);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hedgeprod.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hedgeprods/:id} : Partial updates given fields of an existing hedgeprod, field will ignore if it is null
     *
     * @param id the id of the hedgeprod to save.
     * @param hedgeprod the hedgeprod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hedgeprod,
     * or with status {@code 400 (Bad Request)} if the hedgeprod is not valid,
     * or with status {@code 404 (Not Found)} if the hedgeprod is not found,
     * or with status {@code 500 (Internal Server Error)} if the hedgeprod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hedgeprods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hedgeprod> partialUpdateHedgeprod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Hedgeprod hedgeprod
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hedgeprod partially : {}, {}", id, hedgeprod);
        if (hedgeprod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hedgeprod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hedgeprodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Hedgeprod> result = hedgeprodService.partialUpdate(hedgeprod);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hedgeprod.getId().toString())
        );
    }

    /**
     * {@code GET  /hedgeprods} : get all the hedgeprods.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hedgeprods in body.
     */
    @GetMapping("/hedgeprods")
    public List<Hedgeprod> getAllHedgeprods() {
        log.debug("REST request to get all Hedgeprods");
        return hedgeprodService.findAll();
    }

    /**
     * {@code GET  /hedgeprods/:id} : get the "id" hedgeprod.
     *
     * @param id the id of the hedgeprod to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hedgeprod, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hedgeprods/{id}")
    public ResponseEntity<Hedgeprod> getHedgeprod(@PathVariable Long id) {
        log.debug("REST request to get Hedgeprod : {}", id);
        Optional<Hedgeprod> hedgeprod = hedgeprodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hedgeprod);
    }

    /**
     * {@code DELETE  /hedgeprods/:id} : delete the "id" hedgeprod.
     *
     * @param id the id of the hedgeprod to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hedgeprods/{id}")
    public ResponseEntity<Void> deleteHedgeprod(@PathVariable Long id) {
        log.debug("REST request to delete Hedgeprod : {}", id);
        hedgeprodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/hedgeprods?query=:query} : search for the hedgeprod corresponding
     * to the query.
     *
     * @param query the query of the hedgeprod search.
     * @return the result of the search.
     */
    @GetMapping("/_search/hedgeprods")
    public List<Hedgeprod> searchHedgeprods(@RequestParam String query) {
        log.debug("REST request to search Hedgeprods for query {}", query);
        return hedgeprodService.search(query);
    }
}
