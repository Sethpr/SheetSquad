package com.sheetsquad.app.web.rest;

import com.sheetsquad.app.domain.Quality;
import com.sheetsquad.app.repository.QualityRepository;
import com.sheetsquad.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sheetsquad.app.domain.Quality}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QualityResource {

    private final Logger log = LoggerFactory.getLogger(QualityResource.class);

    private static final String ENTITY_NAME = "quality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QualityRepository qualityRepository;

    public QualityResource(QualityRepository qualityRepository) {
        this.qualityRepository = qualityRepository;
    }

    /**
     * {@code POST  /qualities} : Create a new quality.
     *
     * @param quality the quality to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quality, or with status {@code 400 (Bad Request)} if the quality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/qualities")
    public ResponseEntity<Quality> createQuality(@Valid @RequestBody Quality quality) throws URISyntaxException {
        log.debug("REST request to save Quality : {}", quality);
        if (quality.getId() != null) {
            throw new BadRequestAlertException("A new quality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Quality result = qualityRepository.save(quality);
        return ResponseEntity
            .created(new URI("/api/qualities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /qualities/:id} : Updates an existing quality.
     *
     * @param id the id of the quality to save.
     * @param quality the quality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quality,
     * or with status {@code 400 (Bad Request)} if the quality is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/qualities/{id}")
    public ResponseEntity<Quality> updateQuality(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Quality quality
    ) throws URISyntaxException {
        log.debug("REST request to update Quality : {}, {}", id, quality);
        if (quality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qualityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Quality result = qualityRepository.save(quality);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quality.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /qualities/:id} : Partial updates given fields of an existing quality, field will ignore if it is null
     *
     * @param id the id of the quality to save.
     * @param quality the quality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quality,
     * or with status {@code 400 (Bad Request)} if the quality is not valid,
     * or with status {@code 404 (Not Found)} if the quality is not found,
     * or with status {@code 500 (Internal Server Error)} if the quality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/qualities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Quality> partialUpdateQuality(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Quality quality
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quality partially : {}, {}", id, quality);
        if (quality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qualityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Quality> result = qualityRepository
            .findById(quality.getId())
            .map(existingQuality -> {
                if (quality.getType() != null) {
                    existingQuality.setType(quality.getType());
                }
                if (quality.getCapacity1() != null) {
                    existingQuality.setCapacity1(quality.getCapacity1());
                }
                if (quality.getCapacity2() != null) {
                    existingQuality.setCapacity2(quality.getCapacity2());
                }
                if (quality.getCapacity3() != null) {
                    existingQuality.setCapacity3(quality.getCapacity3());
                }
                if (quality.getCost() != null) {
                    existingQuality.setCost(quality.getCost());
                }

                return existingQuality;
            })
            .map(qualityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quality.getId().toString())
        );
    }

    /**
     * {@code GET  /qualities} : get all the qualities.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qualities in body.
     */
    @GetMapping("/qualities")
    public List<Quality> getAllQualities(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Qualities");
        if (eagerload) {
            return qualityRepository.findAllWithEagerRelationships();
        } else {
            return qualityRepository.findAll();
        }
    }

    /**
     * {@code GET  /qualities/:id} : get the "id" quality.
     *
     * @param id the id of the quality to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quality, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/qualities/{id}")
    public ResponseEntity<Quality> getQuality(@PathVariable Long id) {
        log.debug("REST request to get Quality : {}", id);
        Optional<Quality> quality = qualityRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(quality);
    }

    /**
     * {@code DELETE  /qualities/:id} : delete the "id" quality.
     *
     * @param id the id of the quality to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/qualities/{id}")
    public ResponseEntity<Void> deleteQuality(@PathVariable Long id) {
        log.debug("REST request to delete Quality : {}", id);
        qualityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
