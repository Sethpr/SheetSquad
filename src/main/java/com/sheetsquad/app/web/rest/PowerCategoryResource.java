package com.sheetsquad.app.web.rest;

import com.sheetsquad.app.domain.PowerCategory;
import com.sheetsquad.app.repository.PowerCategoryRepository;
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
 * REST controller for managing {@link com.sheetsquad.app.domain.PowerCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PowerCategoryResource {

    private final Logger log = LoggerFactory.getLogger(PowerCategoryResource.class);

    private static final String ENTITY_NAME = "powerCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PowerCategoryRepository powerCategoryRepository;

    public PowerCategoryResource(PowerCategoryRepository powerCategoryRepository) {
        this.powerCategoryRepository = powerCategoryRepository;
    }

    /**
     * {@code POST  /power-categories} : Create a new powerCategory.
     *
     * @param powerCategory the powerCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new powerCategory, or with status {@code 400 (Bad Request)} if the powerCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/power-categories")
    public ResponseEntity<PowerCategory> createPowerCategory(@Valid @RequestBody PowerCategory powerCategory) throws URISyntaxException {
        log.debug("REST request to save PowerCategory : {}", powerCategory);
        if (powerCategory.getId() != null) {
            throw new BadRequestAlertException("A new powerCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PowerCategory result = powerCategoryRepository.save(powerCategory);
        return ResponseEntity
            .created(new URI("/api/power-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /power-categories/:id} : Updates an existing powerCategory.
     *
     * @param id the id of the powerCategory to save.
     * @param powerCategory the powerCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated powerCategory,
     * or with status {@code 400 (Bad Request)} if the powerCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the powerCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/power-categories/{id}")
    public ResponseEntity<PowerCategory> updatePowerCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PowerCategory powerCategory
    ) throws URISyntaxException {
        log.debug("REST request to update PowerCategory : {}, {}", id, powerCategory);
        if (powerCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, powerCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!powerCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PowerCategory result = powerCategoryRepository.save(powerCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, powerCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /power-categories/:id} : Partial updates given fields of an existing powerCategory, field will ignore if it is null
     *
     * @param id the id of the powerCategory to save.
     * @param powerCategory the powerCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated powerCategory,
     * or with status {@code 400 (Bad Request)} if the powerCategory is not valid,
     * or with status {@code 404 (Not Found)} if the powerCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the powerCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/power-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PowerCategory> partialUpdatePowerCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PowerCategory powerCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update PowerCategory partially : {}, {}", id, powerCategory);
        if (powerCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, powerCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!powerCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PowerCategory> result = powerCategoryRepository
            .findById(powerCategory.getId())
            .map(existingPowerCategory -> {
                if (powerCategory.getName() != null) {
                    existingPowerCategory.setName(powerCategory.getName());
                }
                if (powerCategory.getPriority() != null) {
                    existingPowerCategory.setPriority(powerCategory.getPriority());
                }
                if (powerCategory.getCost() != null) {
                    existingPowerCategory.setCost(powerCategory.getCost());
                }

                return existingPowerCategory;
            })
            .map(powerCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, powerCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /power-categories} : get all the powerCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of powerCategories in body.
     */
    @GetMapping("/power-categories")
    public List<PowerCategory> getAllPowerCategories() {
        log.debug("REST request to get all PowerCategories");
        return powerCategoryRepository.findAll();
    }

    /**
     * {@code GET  /power-categories/:id} : get the "id" powerCategory.
     *
     * @param id the id of the powerCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the powerCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/power-categories/{id}")
    public ResponseEntity<PowerCategory> getPowerCategory(@PathVariable Long id) {
        log.debug("REST request to get PowerCategory : {}", id);
        Optional<PowerCategory> powerCategory = powerCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(powerCategory);
    }

    /**
     * {@code DELETE  /power-categories/:id} : delete the "id" powerCategory.
     *
     * @param id the id of the powerCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/power-categories/{id}")
    public ResponseEntity<Void> deletePowerCategory(@PathVariable Long id) {
        log.debug("REST request to delete PowerCategory : {}", id);
        powerCategoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
