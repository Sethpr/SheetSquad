package com.sheetsquad.app.web.rest;

import com.sheetsquad.app.domain.Pool;
import com.sheetsquad.app.repository.PoolRepository;
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
 * REST controller for managing {@link com.sheetsquad.app.domain.Pool}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PoolResource {

    private final Logger log = LoggerFactory.getLogger(PoolResource.class);

    private static final String ENTITY_NAME = "pool";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoolRepository poolRepository;

    public PoolResource(PoolRepository poolRepository) {
        this.poolRepository = poolRepository;
    }

    /**
     * {@code POST  /pools} : Create a new pool.
     *
     * @param pool the pool to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pool, or with status {@code 400 (Bad Request)} if the pool has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pools")
    public ResponseEntity<Pool> createPool(@RequestBody Pool pool) throws URISyntaxException {
        log.debug("REST request to save Pool : {}", pool);
        if (pool.getId() != null) {
            throw new BadRequestAlertException("A new pool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pool result = poolRepository.save(pool);
        return ResponseEntity
            .created(new URI("/api/pools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pools/:id} : Updates an existing pool.
     *
     * @param id the id of the pool to save.
     * @param pool the pool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pool,
     * or with status {@code 400 (Bad Request)} if the pool is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pools/{id}")
    public ResponseEntity<Pool> updatePool(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pool pool)
        throws URISyntaxException {
        log.debug("REST request to update Pool : {}, {}", id, pool);
        if (pool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pool result = poolRepository.save(pool);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pool.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pools/:id} : Partial updates given fields of an existing pool, field will ignore if it is null
     *
     * @param id the id of the pool to save.
     * @param pool the pool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pool,
     * or with status {@code 400 (Bad Request)} if the pool is not valid,
     * or with status {@code 404 (Not Found)} if the pool is not found,
     * or with status {@code 500 (Internal Server Error)} if the pool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pools/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pool> partialUpdatePool(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pool pool)
        throws URISyntaxException {
        log.debug("REST request to partial update Pool partially : {}, {}", id, pool);
        if (pool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pool> result = poolRepository
            .findById(pool.getId())
            .map(existingPool -> {
                if (pool.getNormal() != null) {
                    existingPool.setNormal(pool.getNormal());
                }
                if (pool.getHard() != null) {
                    existingPool.setHard(pool.getHard());
                }
                if (pool.getWiggle() != null) {
                    existingPool.setWiggle(pool.getWiggle());
                }
                if (pool.getExpert() != null) {
                    existingPool.setExpert(pool.getExpert());
                }

                return existingPool;
            })
            .map(poolRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pool.getId().toString())
        );
    }

    /**
     * {@code GET  /pools} : get all the pools.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pools in body.
     */
    @GetMapping("/pools")
    public List<Pool> getAllPools() {
        log.debug("REST request to get all Pools");
        return poolRepository.findAll();
    }

    /**
     * {@code GET  /pools/:id} : get the "id" pool.
     *
     * @param id the id of the pool to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pool, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pools/{id}")
    public ResponseEntity<Pool> getPool(@PathVariable Long id) {
        log.debug("REST request to get Pool : {}", id);
        Optional<Pool> pool = poolRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pool);
    }

    /**
     * {@code DELETE  /pools/:id} : delete the "id" pool.
     *
     * @param id the id of the pool to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pools/{id}")
    public ResponseEntity<Void> deletePool(@PathVariable Long id) {
        log.debug("REST request to delete Pool : {}", id);
        poolRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
