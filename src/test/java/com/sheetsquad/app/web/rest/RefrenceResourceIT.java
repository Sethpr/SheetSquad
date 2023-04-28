package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.Refrence;
import com.sheetsquad.app.repository.RefrenceRepository;
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
 * Integration tests for the {@link RefrenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RefrenceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/refrences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RefrenceRepository refrenceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRefrenceMockMvc;

    private Refrence refrence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refrence createEntity(EntityManager em) {
        Refrence refrence = new Refrence().title(DEFAULT_TITLE).info(DEFAULT_INFO);
        return refrence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refrence createUpdatedEntity(EntityManager em) {
        Refrence refrence = new Refrence().title(UPDATED_TITLE).info(UPDATED_INFO);
        return refrence;
    }

    @BeforeEach
    public void initTest() {
        refrence = createEntity(em);
    }

    @Test
    @Transactional
    void createRefrence() throws Exception {
        int databaseSizeBeforeCreate = refrenceRepository.findAll().size();
        // Create the Refrence
        restRefrenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refrence)))
            .andExpect(status().isCreated());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeCreate + 1);
        Refrence testRefrence = refrenceList.get(refrenceList.size() - 1);
        assertThat(testRefrence.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRefrence.getInfo()).isEqualTo(DEFAULT_INFO);
    }

    @Test
    @Transactional
    void createRefrenceWithExistingId() throws Exception {
        // Create the Refrence with an existing ID
        refrence.setId(1L);

        int databaseSizeBeforeCreate = refrenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefrenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refrence)))
            .andExpect(status().isBadRequest());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRefrences() throws Exception {
        // Initialize the database
        refrenceRepository.saveAndFlush(refrence);

        // Get all the refrenceList
        restRefrenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refrence.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)));
    }

    @Test
    @Transactional
    void getRefrence() throws Exception {
        // Initialize the database
        refrenceRepository.saveAndFlush(refrence);

        // Get the refrence
        restRefrenceMockMvc
            .perform(get(ENTITY_API_URL_ID, refrence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(refrence.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO));
    }

    @Test
    @Transactional
    void getNonExistingRefrence() throws Exception {
        // Get the refrence
        restRefrenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRefrence() throws Exception {
        // Initialize the database
        refrenceRepository.saveAndFlush(refrence);

        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();

        // Update the refrence
        Refrence updatedRefrence = refrenceRepository.findById(refrence.getId()).get();
        // Disconnect from session so that the updates on updatedRefrence are not directly saved in db
        em.detach(updatedRefrence);
        updatedRefrence.title(UPDATED_TITLE).info(UPDATED_INFO);

        restRefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRefrence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRefrence))
            )
            .andExpect(status().isOk());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
        Refrence testRefrence = refrenceList.get(refrenceList.size() - 1);
        assertThat(testRefrence.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRefrence.getInfo()).isEqualTo(UPDATED_INFO);
    }

    @Test
    @Transactional
    void putNonExistingRefrence() throws Exception {
        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();
        refrence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refrence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRefrence() throws Exception {
        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();
        refrence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRefrence() throws Exception {
        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();
        refrence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefrenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refrence)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRefrenceWithPatch() throws Exception {
        // Initialize the database
        refrenceRepository.saveAndFlush(refrence);

        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();

        // Update the refrence using partial update
        Refrence partialUpdatedRefrence = new Refrence();
        partialUpdatedRefrence.setId(refrence.getId());

        restRefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefrence))
            )
            .andExpect(status().isOk());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
        Refrence testRefrence = refrenceList.get(refrenceList.size() - 1);
        assertThat(testRefrence.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRefrence.getInfo()).isEqualTo(DEFAULT_INFO);
    }

    @Test
    @Transactional
    void fullUpdateRefrenceWithPatch() throws Exception {
        // Initialize the database
        refrenceRepository.saveAndFlush(refrence);

        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();

        // Update the refrence using partial update
        Refrence partialUpdatedRefrence = new Refrence();
        partialUpdatedRefrence.setId(refrence.getId());

        partialUpdatedRefrence.title(UPDATED_TITLE).info(UPDATED_INFO);

        restRefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefrence))
            )
            .andExpect(status().isOk());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
        Refrence testRefrence = refrenceList.get(refrenceList.size() - 1);
        assertThat(testRefrence.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRefrence.getInfo()).isEqualTo(UPDATED_INFO);
    }

    @Test
    @Transactional
    void patchNonExistingRefrence() throws Exception {
        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();
        refrence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, refrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRefrence() throws Exception {
        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();
        refrence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRefrence() throws Exception {
        int databaseSizeBeforeUpdate = refrenceRepository.findAll().size();
        refrence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefrenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(refrence)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refrence in the database
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRefrence() throws Exception {
        // Initialize the database
        refrenceRepository.saveAndFlush(refrence);

        int databaseSizeBeforeDelete = refrenceRepository.findAll().size();

        // Delete the refrence
        restRefrenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, refrence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Refrence> refrenceList = refrenceRepository.findAll();
        assertThat(refrenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
