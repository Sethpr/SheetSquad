package com.sheetsquad.app.web.rest;

import com.sheetsquad.app.domain.Refrence;
import com.sheetsquad.app.repository.RefrenceRepository;
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
 * REST controller for managing {@link com.sheetsquad.app.domain.Refrence}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RefrenceResource {

    private final Logger log = LoggerFactory.getLogger(RefrenceResource.class);

    private static final String ENTITY_NAME = "refrence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RefrenceRepository refrenceRepository;

    public RefrenceResource(RefrenceRepository refrenceRepository) {
        this.refrenceRepository = refrenceRepository;
    }

    /**
     * {@code POST  /refrences} : Create a new refrence.
     *
     * @param refrence the refrence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new refrence, or with status {@code 400 (Bad Request)} if the refrence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/refrences")
    public ResponseEntity<Refrence> createRefrence(@RequestBody Refrence refrence) throws URISyntaxException {
        log.debug("REST request to save Refrence : {}", refrence);
        if (refrence.getId() != null) {
            throw new BadRequestAlertException("A new refrence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Refrence result = refrenceRepository.save(refrence);
        return ResponseEntity
            .created(new URI("/api/refrences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /refrences/:id} : Updates an existing refrence.
     *
     * @param id the id of the refrence to save.
     * @param refrence the refrence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refrence,
     * or with status {@code 400 (Bad Request)} if the refrence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the refrence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/refrences/{id}")
    public ResponseEntity<Refrence> updateRefrence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Refrence refrence
    ) throws URISyntaxException {
        log.debug("REST request to update Refrence : {}, {}", id, refrence);
        if (refrence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refrence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!refrenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Refrence result = refrenceRepository.save(refrence);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, refrence.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /refrences/:id} : Partial updates given fields of an existing refrence, field will ignore if it is null
     *
     * @param id the id of the refrence to save.
     * @param refrence the refrence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refrence,
     * or with status {@code 400 (Bad Request)} if the refrence is not valid,
     * or with status {@code 404 (Not Found)} if the refrence is not found,
     * or with status {@code 500 (Internal Server Error)} if the refrence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/refrences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Refrence> partialUpdateRefrence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Refrence refrence
    ) throws URISyntaxException {
        log.debug("REST request to partial update Refrence partially : {}, {}", id, refrence);
        if (refrence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refrence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!refrenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Refrence> result = refrenceRepository
            .findById(refrence.getId())
            .map(existingRefrence -> {
                if (refrence.getTitle() != null) {
                    existingRefrence.setTitle(refrence.getTitle());
                }
                if (refrence.getInfo() != null) {
                    existingRefrence.setInfo(refrence.getInfo());
                }

                return existingRefrence;
            })
            .map(refrenceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, refrence.getId().toString())
        );
    }

    /**
     * {@code GET  /refrences} : get all the refrences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of refrences in body.
     */
    @GetMapping("/refrences")
    public List<Refrence> getAllRefrences() {
        log.debug("REST request to get all Refrences");
        return refrenceRepository.findAll();
    }

    /**
     * {@code GET  /refrences/:id} : get the "id" refrence.
     *
     * @param id the id of the refrence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the refrence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/refrences/{id}")
    public ResponseEntity<Refrence> getRefrence(@PathVariable Long id) {
        log.debug("REST request to get Refrence : {}", id);
        Optional<Refrence> refrence = refrenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(refrence);
    }

    /**
     * {@code DELETE  /refrences/:id} : delete the "id" refrence.
     *
     * @param id the id of the refrence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/refrences/{id}")
    public ResponseEntity<Void> deleteRefrence(@PathVariable Long id) {
        log.debug("REST request to delete Refrence : {}", id);
        refrenceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
