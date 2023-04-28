package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.Pool;
import com.sheetsquad.app.repository.PoolRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PoolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoolResourceIT {

    private static final Integer DEFAULT_NORMAL = 1;
    private static final Integer UPDATED_NORMAL = 2;

    private static final Integer DEFAULT_HARD = 1;
    private static final Integer UPDATED_HARD = 2;

    private static final Integer DEFAULT_WIGGLE = 1;
    private static final Integer UPDATED_WIGGLE = 2;

    private static final Integer DEFAULT_EXPERT = 1;
    private static final Integer UPDATED_EXPERT = 2;

    private static final String ENTITY_API_URL = "/api/pools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoolRepository poolRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoolMockMvc;

    private Pool pool;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pool createEntity(EntityManager em) {
        Pool pool = new Pool().normal(DEFAULT_NORMAL).hard(DEFAULT_HARD).wiggle(DEFAULT_WIGGLE).expert(DEFAULT_EXPERT);
        return pool;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pool createUpdatedEntity(EntityManager em) {
        Pool pool = new Pool().normal(UPDATED_NORMAL).hard(UPDATED_HARD).wiggle(UPDATED_WIGGLE).expert(UPDATED_EXPERT);
        return pool;
    }

    @BeforeEach
    public void initTest() {
        pool = createEntity(em);
    }

    @Test
    @Transactional
    void createPool() throws Exception {
        int databaseSizeBeforeCreate = poolRepository.findAll().size();
        // Create the Pool
        restPoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pool)))
            .andExpect(status().isCreated());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeCreate + 1);
        Pool testPool = poolList.get(poolList.size() - 1);
        assertThat(testPool.getNormal()).isEqualTo(DEFAULT_NORMAL);
        assertThat(testPool.getHard()).isEqualTo(DEFAULT_HARD);
        assertThat(testPool.getWiggle()).isEqualTo(DEFAULT_WIGGLE);
        assertThat(testPool.getExpert()).isEqualTo(DEFAULT_EXPERT);
    }

    @Test
    @Transactional
    void createPoolWithExistingId() throws Exception {
        // Create the Pool with an existing ID
        pool.setId(1L);

        int databaseSizeBeforeCreate = poolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pool)))
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPools() throws Exception {
        // Initialize the database
        poolRepository.saveAndFlush(pool);

        // Get all the poolList
        restPoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pool.getId().intValue())))
            .andExpect(jsonPath("$.[*].normal").value(hasItem(DEFAULT_NORMAL)))
            .andExpect(jsonPath("$.[*].hard").value(hasItem(DEFAULT_HARD)))
            .andExpect(jsonPath("$.[*].wiggle").value(hasItem(DEFAULT_WIGGLE)))
            .andExpect(jsonPath("$.[*].expert").value(hasItem(DEFAULT_EXPERT)));
    }

    @Test
    @Transactional
    void getPool() throws Exception {
        // Initialize the database
        poolRepository.saveAndFlush(pool);

        // Get the pool
        restPoolMockMvc
            .perform(get(ENTITY_API_URL_ID, pool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pool.getId().intValue()))
            .andExpect(jsonPath("$.normal").value(DEFAULT_NORMAL))
            .andExpect(jsonPath("$.hard").value(DEFAULT_HARD))
            .andExpect(jsonPath("$.wiggle").value(DEFAULT_WIGGLE))
            .andExpect(jsonPath("$.expert").value(DEFAULT_EXPERT));
    }

    @Test
    @Transactional
    void getNonExistingPool() throws Exception {
        // Get the pool
        restPoolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPool() throws Exception {
        // Initialize the database
        poolRepository.saveAndFlush(pool);

        int databaseSizeBeforeUpdate = poolRepository.findAll().size();

        // Update the pool
        Pool updatedPool = poolRepository.findById(pool.getId()).get();
        // Disconnect from session so that the updates on updatedPool are not directly saved in db
        em.detach(updatedPool);
        updatedPool.normal(UPDATED_NORMAL).hard(UPDATED_HARD).wiggle(UPDATED_WIGGLE).expert(UPDATED_EXPERT);

        restPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPool.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPool))
            )
            .andExpect(status().isOk());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
        Pool testPool = poolList.get(poolList.size() - 1);
        assertThat(testPool.getNormal()).isEqualTo(UPDATED_NORMAL);
        assertThat(testPool.getHard()).isEqualTo(UPDATED_HARD);
        assertThat(testPool.getWiggle()).isEqualTo(UPDATED_WIGGLE);
        assertThat(testPool.getExpert()).isEqualTo(UPDATED_EXPERT);
    }

    @Test
    @Transactional
    void putNonExistingPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pool.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pool))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pool))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pool)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoolWithPatch() throws Exception {
        // Initialize the database
        poolRepository.saveAndFlush(pool);

        int databaseSizeBeforeUpdate = poolRepository.findAll().size();

        // Update the pool using partial update
        Pool partialUpdatedPool = new Pool();
        partialUpdatedPool.setId(pool.getId());

        partialUpdatedPool.normal(UPDATED_NORMAL).hard(UPDATED_HARD).expert(UPDATED_EXPERT);

        restPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPool))
            )
            .andExpect(status().isOk());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
        Pool testPool = poolList.get(poolList.size() - 1);
        assertThat(testPool.getNormal()).isEqualTo(UPDATED_NORMAL);
        assertThat(testPool.getHard()).isEqualTo(UPDATED_HARD);
        assertThat(testPool.getWiggle()).isEqualTo(DEFAULT_WIGGLE);
        assertThat(testPool.getExpert()).isEqualTo(UPDATED_EXPERT);
    }

    @Test
    @Transactional
    void fullUpdatePoolWithPatch() throws Exception {
        // Initialize the database
        poolRepository.saveAndFlush(pool);

        int databaseSizeBeforeUpdate = poolRepository.findAll().size();

        // Update the pool using partial update
        Pool partialUpdatedPool = new Pool();
        partialUpdatedPool.setId(pool.getId());

        partialUpdatedPool.normal(UPDATED_NORMAL).hard(UPDATED_HARD).wiggle(UPDATED_WIGGLE).expert(UPDATED_EXPERT);

        restPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPool))
            )
            .andExpect(status().isOk());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
        Pool testPool = poolList.get(poolList.size() - 1);
        assertThat(testPool.getNormal()).isEqualTo(UPDATED_NORMAL);
        assertThat(testPool.getHard()).isEqualTo(UPDATED_HARD);
        assertThat(testPool.getWiggle()).isEqualTo(UPDATED_WIGGLE);
        assertThat(testPool.getExpert()).isEqualTo(UPDATED_EXPERT);
    }

    @Test
    @Transactional
    void patchNonExistingPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pool))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pool))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pool)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePool() throws Exception {
        // Initialize the database
        poolRepository.saveAndFlush(pool);

        int databaseSizeBeforeDelete = poolRepository.findAll().size();

        // Delete the pool
        restPoolMockMvc
            .perform(delete(ENTITY_API_URL_ID, pool.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
