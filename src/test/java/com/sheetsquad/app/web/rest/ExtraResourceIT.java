package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.Extra;
import com.sheetsquad.app.domain.enumeration.Capacity;
import com.sheetsquad.app.repository.ExtraRepository;
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
 * Integration tests for the {@link ExtraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtraResourceIT {

    private static final Integer DEFAULT_MULTIPLIER = 1;
    private static final Integer UPDATED_MULTIPLIER = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Capacity DEFAULT_CAPACITY = Capacity.SPEED;
    private static final Capacity UPDATED_CAPACITY = Capacity.RANGE;

    private static final String ENTITY_API_URL = "/api/extras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExtraRepository extraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtraMockMvc;

    private Extra extra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extra createEntity(EntityManager em) {
        Extra extra = new Extra().multiplier(DEFAULT_MULTIPLIER).notes(DEFAULT_NOTES).capacity(DEFAULT_CAPACITY);
        return extra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extra createUpdatedEntity(EntityManager em) {
        Extra extra = new Extra().multiplier(UPDATED_MULTIPLIER).notes(UPDATED_NOTES).capacity(UPDATED_CAPACITY);
        return extra;
    }

    @BeforeEach
    public void initTest() {
        extra = createEntity(em);
    }

    @Test
    @Transactional
    void createExtra() throws Exception {
        int databaseSizeBeforeCreate = extraRepository.findAll().size();
        // Create the Extra
        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extra)))
            .andExpect(status().isCreated());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeCreate + 1);
        Extra testExtra = extraList.get(extraList.size() - 1);
        assertThat(testExtra.getMultiplier()).isEqualTo(DEFAULT_MULTIPLIER);
        assertThat(testExtra.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testExtra.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    void createExtraWithExistingId() throws Exception {
        // Create the Extra with an existing ID
        extra.setId(1L);

        int databaseSizeBeforeCreate = extraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extra)))
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMultiplierIsRequired() throws Exception {
        int databaseSizeBeforeTest = extraRepository.findAll().size();
        // set the field null
        extra.setMultiplier(null);

        // Create the Extra, which fails.

        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extra)))
            .andExpect(status().isBadRequest());

        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExtras() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get all the extraList
        restExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extra.getId().intValue())))
            .andExpect(jsonPath("$.[*].multiplier").value(hasItem(DEFAULT_MULTIPLIER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY.toString())));
    }

    @Test
    @Transactional
    void getExtra() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get the extra
        restExtraMockMvc
            .perform(get(ENTITY_API_URL_ID, extra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extra.getId().intValue()))
            .andExpect(jsonPath("$.multiplier").value(DEFAULT_MULTIPLIER))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExtra() throws Exception {
        // Get the extra
        restExtraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExtra() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        int databaseSizeBeforeUpdate = extraRepository.findAll().size();

        // Update the extra
        Extra updatedExtra = extraRepository.findById(extra.getId()).get();
        // Disconnect from session so that the updates on updatedExtra are not directly saved in db
        em.detach(updatedExtra);
        updatedExtra.multiplier(UPDATED_MULTIPLIER).notes(UPDATED_NOTES).capacity(UPDATED_CAPACITY);

        restExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExtra))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
        Extra testExtra = extraList.get(extraList.size() - 1);
        assertThat(testExtra.getMultiplier()).isEqualTo(UPDATED_MULTIPLIER);
        assertThat(testExtra.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testExtra.getCapacity()).isEqualTo(UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void putNonExistingExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtraWithPatch() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        int databaseSizeBeforeUpdate = extraRepository.findAll().size();

        // Update the extra using partial update
        Extra partialUpdatedExtra = new Extra();
        partialUpdatedExtra.setId(extra.getId());

        partialUpdatedExtra.capacity(UPDATED_CAPACITY);

        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtra))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
        Extra testExtra = extraList.get(extraList.size() - 1);
        assertThat(testExtra.getMultiplier()).isEqualTo(DEFAULT_MULTIPLIER);
        assertThat(testExtra.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testExtra.getCapacity()).isEqualTo(UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void fullUpdateExtraWithPatch() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        int databaseSizeBeforeUpdate = extraRepository.findAll().size();

        // Update the extra using partial update
        Extra partialUpdatedExtra = new Extra();
        partialUpdatedExtra.setId(extra.getId());

        partialUpdatedExtra.multiplier(UPDATED_MULTIPLIER).notes(UPDATED_NOTES).capacity(UPDATED_CAPACITY);

        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtra))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
        Extra testExtra = extraList.get(extraList.size() - 1);
        assertThat(testExtra.getMultiplier()).isEqualTo(UPDATED_MULTIPLIER);
        assertThat(testExtra.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testExtra.getCapacity()).isEqualTo(UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void patchNonExistingExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(extra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtra() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        int databaseSizeBeforeDelete = extraRepository.findAll().size();

        // Delete the extra
        restExtraMockMvc
            .perform(delete(ENTITY_API_URL_ID, extra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
