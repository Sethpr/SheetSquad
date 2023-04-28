package com.sheetsquad.app.web.rest;

import com.sheetsquad.app.domain.Power;
import com.sheetsquad.app.repository.PowerRepository;
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
 * REST controller for managing {@link com.sheetsquad.app.domain.Power}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PowerResource {

    private final Logger log = LoggerFactory.getLogger(PowerResource.class);

    private static final String ENTITY_NAME = "power";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PowerRepository powerRepository;

    public PowerResource(PowerRepository powerRepository) {
        this.powerRepository = powerRepository;
    }

    /**
     * {@code POST  /powers} : Create a new power.
     *
     * @param power the power to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new power, or with status {@code 400 (Bad Request)} if the power has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/powers")
    public ResponseEntity<Power> createPower(@Valid @RequestBody Power power) throws URISyntaxException {
        log.debug("REST request to save Power : {}", power);
        if (power.getId() != null) {
            throw new BadRequestAlertException("A new power cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Power result = powerRepository.save(power);
        return ResponseEntity
            .created(new URI("/api/powers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /powers/:id} : Updates an existing power.
     *
     * @param id the id of the power to save.
     * @param power the power to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated power,
     * or with status {@code 400 (Bad Request)} if the power is not valid,
     * or with status {@code 500 (Internal Server Error)} if the power couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/powers/{id}")
    public ResponseEntity<Power> updatePower(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Power power)
        throws URISyntaxException {
        log.debug("REST request to update Power : {}, {}", id, power);
        if (power.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, power.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!powerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Power result = powerRepository.save(power);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, power.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /powers/:id} : Partial updates given fields of an existing power, field will ignore if it is null
     *
     * @param id the id of the power to save.
     * @param power the power to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated power,
     * or with status {@code 400 (Bad Request)} if the power is not valid,
     * or with status {@code 404 (Not Found)} if the power is not found,
     * or with status {@code 500 (Internal Server Error)} if the power couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/powers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Power> partialUpdatePower(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Power power
    ) throws URISyntaxException {
        log.debug("REST request to partial update Power partially : {}, {}", id, power);
        if (power.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, power.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!powerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Power> result = powerRepository
            .findById(power.getId())
            .map(existingPower -> {
                if (power.getName() != null) {
                    existingPower.setName(power.getName());
                }
                if (power.getCost() != null) {
                    existingPower.setCost(power.getCost());
                }
                if (power.getNotes() != null) {
                    existingPower.setNotes(power.getNotes());
                }

                return existingPower;
            })
            .map(powerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, power.getId().toString())
        );
    }

    /**
     * {@code GET  /powers} : get all the powers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of powers in body.
     */
    @GetMapping("/powers")
    public List<Power> getAllPowers() {
        log.debug("REST request to get all Powers");
        return powerRepository.findAll();
    }

    /**
     * {@code GET  /powers/:id} : get the "id" power.
     *
     * @param id the id of the power to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the power, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/powers/{id}")
    public ResponseEntity<Power> getPower(@PathVariable Long id) {
        log.debug("REST request to get Power : {}", id);
        Optional<Power> power = powerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(power);
    }

    /**
     * {@code DELETE  /powers/:id} : delete the "id" power.
     *
     * @param id the id of the power to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/powers/{id}")
    public ResponseEntity<Void> deletePower(@PathVariable Long id) {
        log.debug("REST request to delete Power : {}", id);
        powerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
