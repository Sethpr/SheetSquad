package com.sheetsquad.app.web.rest;

import com.sheetsquad.app.domain.Extra;
import com.sheetsquad.app.repository.ExtraRepository;
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
 * REST controller for managing {@link com.sheetsquad.app.domain.Extra}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExtraResource {

    private final Logger log = LoggerFactory.getLogger(ExtraResource.class);

    private static final String ENTITY_NAME = "extra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtraRepository extraRepository;

    public ExtraResource(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }

    /**
     * {@code POST  /extras} : Create a new extra.
     *
     * @param extra the extra to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extra, or with status {@code 400 (Bad Request)} if the extra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extras")
    public ResponseEntity<Extra> createExtra(@Valid @RequestBody Extra extra) throws URISyntaxException {
        log.debug("REST request to save Extra : {}", extra);
        if (extra.getId() != null) {
            throw new BadRequestAlertException("A new extra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Extra result = extraRepository.save(extra);
        return ResponseEntity
            .created(new URI("/api/extras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extras/:id} : Updates an existing extra.
     *
     * @param id the id of the extra to save.
     * @param extra the extra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extra,
     * or with status {@code 400 (Bad Request)} if the extra is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extras/{id}")
    public ResponseEntity<Extra> updateExtra(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Extra extra)
        throws URISyntaxException {
        log.debug("REST request to update Extra : {}, {}", id, extra);
        if (extra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Extra result = extraRepository.save(extra);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extra.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /extras/:id} : Partial updates given fields of an existing extra, field will ignore if it is null
     *
     * @param id the id of the extra to save.
     * @param extra the extra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extra,
     * or with status {@code 400 (Bad Request)} if the extra is not valid,
     * or with status {@code 404 (Not Found)} if the extra is not found,
     * or with status {@code 500 (Internal Server Error)} if the extra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/extras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Extra> partialUpdateExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Extra extra
    ) throws URISyntaxException {
        log.debug("REST request to partial update Extra partially : {}, {}", id, extra);
        if (extra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Extra> result = extraRepository
            .findById(extra.getId())
            .map(existingExtra -> {
                if (extra.getMultiplier() != null) {
                    existingExtra.setMultiplier(extra.getMultiplier());
                }
                if (extra.getNotes() != null) {
                    existingExtra.setNotes(extra.getNotes());
                }
                if (extra.getCapacity() != null) {
                    existingExtra.setCapacity(extra.getCapacity());
                }

                return existingExtra;
            })
            .map(extraRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extra.getId().toString())
        );
    }

    /**
     * {@code GET  /extras} : get all the extras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extras in body.
     */
    @GetMapping("/extras")
    public List<Extra> getAllExtras() {
        log.debug("REST request to get all Extras");
        return extraRepository.findAll();
    }

    /**
     * {@code GET  /extras/:id} : get the "id" extra.
     *
     * @param id the id of the extra to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extra, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extras/{id}")
    public ResponseEntity<Extra> getExtra(@PathVariable Long id) {
        log.debug("REST request to get Extra : {}", id);
        Optional<Extra> extra = extraRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(extra);
    }

    /**
     * {@code DELETE  /extras/:id} : delete the "id" extra.
     *
     * @param id the id of the extra to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extras/{id}")
    public ResponseEntity<Void> deleteExtra(@PathVariable Long id) {
        log.debug("REST request to delete Extra : {}", id);
        extraRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
