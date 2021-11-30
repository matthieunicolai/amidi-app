package com.amidi.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.ProUser;
import com.amidi.myapp.repository.ProUserRepository;
import com.amidi.myapp.service.ProUserService;
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
 * REST controller for managing {@link com.amidi.myapp.domain.ProUser}.
 */
@RestController
@RequestMapping("/api")
public class ProUserResource {

    private final Logger log = LoggerFactory.getLogger(ProUserResource.class);

    private static final String ENTITY_NAME = "proUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProUserService proUserService;

    private final ProUserRepository proUserRepository;

    public ProUserResource(ProUserService proUserService, ProUserRepository proUserRepository) {
        this.proUserService = proUserService;
        this.proUserRepository = proUserRepository;
    }

    /**
     * {@code POST  /pro-users} : Create a new proUser.
     *
     * @param proUser the proUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proUser, or with status {@code 400 (Bad Request)} if the proUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pro-users")
    public ResponseEntity<ProUser> createProUser(@Valid @RequestBody ProUser proUser) throws URISyntaxException {
        log.debug("REST request to save ProUser : {}", proUser);
        if (proUser.getId() != null) {
            throw new BadRequestAlertException("A new proUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProUser result = proUserService.save(proUser);
        return ResponseEntity
            .created(new URI("/api/pro-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pro-users/:id} : Updates an existing proUser.
     *
     * @param id the id of the proUser to save.
     * @param proUser the proUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proUser,
     * or with status {@code 400 (Bad Request)} if the proUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pro-users/{id}")
    public ResponseEntity<ProUser> updateProUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProUser proUser
    ) throws URISyntaxException {
        log.debug("REST request to update ProUser : {}, {}", id, proUser);
        if (proUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProUser result = proUserService.save(proUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pro-users/:id} : Partial updates given fields of an existing proUser, field will ignore if it is null
     *
     * @param id the id of the proUser to save.
     * @param proUser the proUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proUser,
     * or with status {@code 400 (Bad Request)} if the proUser is not valid,
     * or with status {@code 404 (Not Found)} if the proUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the proUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pro-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProUser> partialUpdateProUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProUser proUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProUser partially : {}, {}", id, proUser);
        if (proUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProUser> result = proUserService.partialUpdate(proUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proUser.getId().toString())
        );
    }

    /**
     * {@code GET  /pro-users} : get all the proUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proUsers in body.
     */
    @GetMapping("/pro-users")
    public List<ProUser> getAllProUsers() {
        log.debug("REST request to get all ProUsers");
        return proUserService.findAll();
    }

    /**
     * {@code GET  /pro-users/:id} : get the "id" proUser.
     *
     * @param id the id of the proUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pro-users/{id}")
    public ResponseEntity<ProUser> getProUser(@PathVariable Long id) {
        log.debug("REST request to get ProUser : {}", id);
        Optional<ProUser> proUser = proUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proUser);
    }

    /**
     * {@code DELETE  /pro-users/:id} : delete the "id" proUser.
     *
     * @param id the id of the proUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pro-users/{id}")
    public ResponseEntity<Void> deleteProUser(@PathVariable Long id) {
        log.debug("REST request to delete ProUser : {}", id);
        proUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/pro-users?query=:query} : search for the proUser corresponding
     * to the query.
     *
     * @param query the query of the proUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search/pro-users")
    public List<ProUser> searchProUsers(@RequestParam String query) {
        log.debug("REST request to search ProUsers for query {}", query);
        return proUserService.search(query);
    }
}
