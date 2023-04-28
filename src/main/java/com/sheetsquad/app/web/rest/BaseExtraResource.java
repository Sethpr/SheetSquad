package com.sheetsquad.app.web.rest;

import com.sheetsquad.app.domain.BaseExtra;
import com.sheetsquad.app.repository.BaseExtraRepository;
import com.sheetsquad.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sheetsquad.app.domain.BaseExtra}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BaseExtraResource {

    private final Logger log = LoggerFactory.getLogger(BaseExtraResource.class);

    private static final String ENTITY_NAME = "baseExtra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BaseExtraRepository baseExtraRepository;

    public BaseExtraResource(BaseExtraRepository baseExtraRepository) {
        this.baseExtraRepository = baseExtraRepository;
    }

    /**
     * {@code POST  /base-extras} : Create a new baseExtra.
     *
     * @param baseExtra the baseExtra to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baseExtra, or with status {@code 400 (Bad Request)} if the baseExtra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/base-extras")
    public ResponseEntity<BaseExtra> createBaseExtra(@RequestBody BaseExtra baseExtra) throws URISyntaxException {
        log.debug("REST request to save BaseExtra : {}", baseExtra);
        if (baseExtra.getId() != null) {
            throw new BadRequestAlertException("A new baseExtra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BaseExtra result = baseExtraRepository.save(baseExtra);
        return ResponseEntity
            .created(new URI("/api/base-extras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /base-extras/:id} : Updates an existing baseExtra.
     *
     * @param id the id of the baseExtra to save.
     * @param baseExtra the baseExtra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baseExtra,
     * or with status {@code 400 (Bad Request)} if the baseExtra is not valid,
     * or with status {@code 500 (Internal Server Error)} if the baseExtra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/base-extras/{id}")
    public ResponseEntity<BaseExtra> updateBaseExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BaseExtra baseExtra
    ) throws URISyntaxException {
        log.debug("REST request to update BaseExtra : {}, {}", id, baseExtra);
        if (baseExtra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baseExtra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baseExtraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BaseExtra result = baseExtraRepository.save(baseExtra);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baseExtra.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /base-extras/:id} : Partial updates given fields of an existing baseExtra, field will ignore if it is null
     *
     * @param id the id of the baseExtra to save.
     * @param baseExtra the baseExtra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baseExtra,
     * or with status {@code 400 (Bad Request)} if the baseExtra is not valid,
     * or with status {@code 404 (Not Found)} if the baseExtra is not found,
     * or with status {@code 500 (Internal Server Error)} if the baseExtra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/base-extras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BaseExtra> partialUpdateBaseExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BaseExtra baseExtra
    ) throws URISyntaxException {
        log.debug("REST request to partial update BaseExtra partially : {}, {}", id, baseExtra);
        if (baseExtra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baseExtra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baseExtraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BaseExtra> result = baseExtraRepository
            .findById(baseExtra.getId())
            .map(existingBaseExtra -> {
                if (baseExtra.getName() != null) {
                    existingBaseExtra.setName(baseExtra.getName());
                }
                if (baseExtra.getValue() != null) {
                    existingBaseExtra.setValue(baseExtra.getValue());
                }

                return existingBaseExtra;
            })
            .map(baseExtraRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baseExtra.getId().toString())
        );
    }

    /**
     * {@code GET  /base-extras} : get all the baseExtras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baseExtras in body.
     */
    @GetMapping("/base-extras")
    public List<BaseExtra> getAllBaseExtras() {
        log.debug("REST request to get all BaseExtras");
        return baseExtraRepository.findAll();
    }

    /**
     * {@code GET  /base-extras/:id} : get the "id" baseExtra.
     *
     * @param id the id of the baseExtra to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baseExtra, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/base-extras/{id}")
    public ResponseEntity<BaseExtra> getBaseExtra(@PathVariable Long id) {
        log.debug("REST request to get BaseExtra : {}", id);
        Optional<BaseExtra> baseExtra = baseExtraRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(baseExtra);
    }

    /**
     * {@code DELETE  /base-extras/:id} : delete the "id" baseExtra.
     *
     * @param id the id of the baseExtra to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/base-extras/{id}")
    public ResponseEntity<Void> deleteBaseExtra(@PathVariable Long id) {
        log.debug("REST request to delete BaseExtra : {}", id);
        baseExtraRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
