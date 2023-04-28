package com.sheetsquad.app.web.rest;

import com.sheetsquad.app.domain.Archetype;
import com.sheetsquad.app.repository.ArchetypeRepository;
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
 * REST controller for managing {@link com.sheetsquad.app.domain.Archetype}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArchetypeResource {

    private final Logger log = LoggerFactory.getLogger(ArchetypeResource.class);

    private static final String ENTITY_NAME = "archetype";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArchetypeRepository archetypeRepository;

    public ArchetypeResource(ArchetypeRepository archetypeRepository) {
        this.archetypeRepository = archetypeRepository;
    }

    /**
     * {@code POST  /archetypes} : Create a new archetype.
     *
     * @param archetype the archetype to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new archetype, or with status {@code 400 (Bad Request)} if the archetype has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/archetypes")
    public ResponseEntity<Archetype> createArchetype(@Valid @RequestBody Archetype archetype) throws URISyntaxException {
        log.debug("REST request to save Archetype : {}", archetype);
        if (archetype.getId() != null) {
            throw new BadRequestAlertException("A new archetype cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Archetype result = archetypeRepository.save(archetype);
        return ResponseEntity
            .created(new URI("/api/archetypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /archetypes/:id} : Updates an existing archetype.
     *
     * @param id the id of the archetype to save.
     * @param archetype the archetype to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archetype,
     * or with status {@code 400 (Bad Request)} if the archetype is not valid,
     * or with status {@code 500 (Internal Server Error)} if the archetype couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/archetypes/{id}")
    public ResponseEntity<Archetype> updateArchetype(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Archetype archetype
    ) throws URISyntaxException {
        log.debug("REST request to update Archetype : {}, {}", id, archetype);
        if (archetype.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archetype.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archetypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Archetype result = archetypeRepository.save(archetype);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, archetype.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /archetypes/:id} : Partial updates given fields of an existing archetype, field will ignore if it is null
     *
     * @param id the id of the archetype to save.
     * @param archetype the archetype to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archetype,
     * or with status {@code 400 (Bad Request)} if the archetype is not valid,
     * or with status {@code 404 (Not Found)} if the archetype is not found,
     * or with status {@code 500 (Internal Server Error)} if the archetype couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/archetypes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Archetype> partialUpdateArchetype(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Archetype archetype
    ) throws URISyntaxException {
        log.debug("REST request to partial update Archetype partially : {}, {}", id, archetype);
        if (archetype.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archetype.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archetypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Archetype> result = archetypeRepository
            .findById(archetype.getId())
            .map(existingArchetype -> {
                if (archetype.getName() != null) {
                    existingArchetype.setName(archetype.getName());
                }
                if (archetype.getCost() != null) {
                    existingArchetype.setCost(archetype.getCost());
                }
                if (archetype.getNotes() != null) {
                    existingArchetype.setNotes(archetype.getNotes());
                }

                return existingArchetype;
            })
            .map(archetypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, archetype.getId().toString())
        );
    }

    /**
     * {@code GET  /archetypes} : get all the archetypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of archetypes in body.
     */
    @GetMapping("/archetypes")
    public List<Archetype> getAllArchetypes() {
        log.debug("REST request to get all Archetypes");
        return archetypeRepository.findAll();
    }

    /**
     * {@code GET  /archetypes/:id} : get the "id" archetype.
     *
     * @param id the id of the archetype to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the archetype, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/archetypes/{id}")
    public ResponseEntity<Archetype> getArchetype(@PathVariable Long id) {
        log.debug("REST request to get Archetype : {}", id);
        Optional<Archetype> archetype = archetypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(archetype);
    }

    /**
     * {@code DELETE  /archetypes/:id} : delete the "id" archetype.
     *
     * @param id the id of the archetype to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/archetypes/{id}")
    public ResponseEntity<Void> deleteArchetype(@PathVariable Long id) {
        log.debug("REST request to delete Archetype : {}", id);
        archetypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
