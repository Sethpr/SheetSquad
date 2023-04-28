package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.BaseExtra;
import com.sheetsquad.app.repository.BaseExtraRepository;
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
 * Integration tests for the {@link BaseExtraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BaseExtraResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final String ENTITY_API_URL = "/api/base-extras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BaseExtraRepository baseExtraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaseExtraMockMvc;

    private BaseExtra baseExtra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaseExtra createEntity(EntityManager em) {
        BaseExtra baseExtra = new BaseExtra().name(DEFAULT_NAME).value(DEFAULT_VALUE);
        return baseExtra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaseExtra createUpdatedEntity(EntityManager em) {
        BaseExtra baseExtra = new BaseExtra().name(UPDATED_NAME).value(UPDATED_VALUE);
        return baseExtra;
    }

    @BeforeEach
    public void initTest() {
        baseExtra = createEntity(em);
    }

    @Test
    @Transactional
    void createBaseExtra() throws Exception {
        int databaseSizeBeforeCreate = baseExtraRepository.findAll().size();
        // Create the BaseExtra
        restBaseExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baseExtra)))
            .andExpect(status().isCreated());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeCreate + 1);
        BaseExtra testBaseExtra = baseExtraList.get(baseExtraList.size() - 1);
        assertThat(testBaseExtra.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBaseExtra.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createBaseExtraWithExistingId() throws Exception {
        // Create the BaseExtra with an existing ID
        baseExtra.setId(1L);

        int databaseSizeBeforeCreate = baseExtraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaseExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baseExtra)))
            .andExpect(status().isBadRequest());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBaseExtras() throws Exception {
        // Initialize the database
        baseExtraRepository.saveAndFlush(baseExtra);

        // Get all the baseExtraList
        restBaseExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baseExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getBaseExtra() throws Exception {
        // Initialize the database
        baseExtraRepository.saveAndFlush(baseExtra);

        // Get the baseExtra
        restBaseExtraMockMvc
            .perform(get(ENTITY_API_URL_ID, baseExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(baseExtra.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingBaseExtra() throws Exception {
        // Get the baseExtra
        restBaseExtraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBaseExtra() throws Exception {
        // Initialize the database
        baseExtraRepository.saveAndFlush(baseExtra);

        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();

        // Update the baseExtra
        BaseExtra updatedBaseExtra = baseExtraRepository.findById(baseExtra.getId()).get();
        // Disconnect from session so that the updates on updatedBaseExtra are not directly saved in db
        em.detach(updatedBaseExtra);
        updatedBaseExtra.name(UPDATED_NAME).value(UPDATED_VALUE);

        restBaseExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBaseExtra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBaseExtra))
            )
            .andExpect(status().isOk());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
        BaseExtra testBaseExtra = baseExtraList.get(baseExtraList.size() - 1);
        assertThat(testBaseExtra.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBaseExtra.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingBaseExtra() throws Exception {
        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();
        baseExtra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaseExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baseExtra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseExtra))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaseExtra() throws Exception {
        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();
        baseExtra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baseExtra))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaseExtra() throws Exception {
        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();
        baseExtra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseExtraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baseExtra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaseExtraWithPatch() throws Exception {
        // Initialize the database
        baseExtraRepository.saveAndFlush(baseExtra);

        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();

        // Update the baseExtra using partial update
        BaseExtra partialUpdatedBaseExtra = new BaseExtra();
        partialUpdatedBaseExtra.setId(baseExtra.getId());

        partialUpdatedBaseExtra.name(UPDATED_NAME).value(UPDATED_VALUE);

        restBaseExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaseExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaseExtra))
            )
            .andExpect(status().isOk());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
        BaseExtra testBaseExtra = baseExtraList.get(baseExtraList.size() - 1);
        assertThat(testBaseExtra.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBaseExtra.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateBaseExtraWithPatch() throws Exception {
        // Initialize the database
        baseExtraRepository.saveAndFlush(baseExtra);

        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();

        // Update the baseExtra using partial update
        BaseExtra partialUpdatedBaseExtra = new BaseExtra();
        partialUpdatedBaseExtra.setId(baseExtra.getId());

        partialUpdatedBaseExtra.name(UPDATED_NAME).value(UPDATED_VALUE);

        restBaseExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaseExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaseExtra))
            )
            .andExpect(status().isOk());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
        BaseExtra testBaseExtra = baseExtraList.get(baseExtraList.size() - 1);
        assertThat(testBaseExtra.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBaseExtra.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingBaseExtra() throws Exception {
        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();
        baseExtra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaseExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baseExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baseExtra))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBaseExtra() throws Exception {
        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();
        baseExtra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baseExtra))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBaseExtra() throws Exception {
        int databaseSizeBeforeUpdate = baseExtraRepository.findAll().size();
        baseExtra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaseExtraMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(baseExtra))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaseExtra in the database
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaseExtra() throws Exception {
        // Initialize the database
        baseExtraRepository.saveAndFlush(baseExtra);

        int databaseSizeBeforeDelete = baseExtraRepository.findAll().size();

        // Delete the baseExtra
        restBaseExtraMockMvc
            .perform(delete(ENTITY_API_URL_ID, baseExtra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BaseExtra> baseExtraList = baseExtraRepository.findAll();
        assertThat(baseExtraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
