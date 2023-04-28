package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.Power;
import com.sheetsquad.app.repository.PowerRepository;
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
 * Integration tests for the {@link PowerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PowerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COST = 1;
    private static final Integer UPDATED_COST = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/powers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PowerRepository powerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPowerMockMvc;

    private Power power;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Power createEntity(EntityManager em) {
        Power power = new Power().name(DEFAULT_NAME).cost(DEFAULT_COST).notes(DEFAULT_NOTES);
        return power;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Power createUpdatedEntity(EntityManager em) {
        Power power = new Power().name(UPDATED_NAME).cost(UPDATED_COST).notes(UPDATED_NOTES);
        return power;
    }

    @BeforeEach
    public void initTest() {
        power = createEntity(em);
    }

    @Test
    @Transactional
    void createPower() throws Exception {
        int databaseSizeBeforeCreate = powerRepository.findAll().size();
        // Create the Power
        restPowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(power)))
            .andExpect(status().isCreated());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeCreate + 1);
        Power testPower = powerList.get(powerList.size() - 1);
        assertThat(testPower.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPower.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testPower.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createPowerWithExistingId() throws Exception {
        // Create the Power with an existing ID
        power.setId(1L);

        int databaseSizeBeforeCreate = powerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(power)))
            .andExpect(status().isBadRequest());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerRepository.findAll().size();
        // set the field null
        power.setName(null);

        // Create the Power, which fails.

        restPowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(power)))
            .andExpect(status().isBadRequest());

        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerRepository.findAll().size();
        // set the field null
        power.setCost(null);

        // Create the Power, which fails.

        restPowerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(power)))
            .andExpect(status().isBadRequest());

        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPowers() throws Exception {
        // Initialize the database
        powerRepository.saveAndFlush(power);

        // Get all the powerList
        restPowerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(power.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getPower() throws Exception {
        // Initialize the database
        powerRepository.saveAndFlush(power);

        // Get the power
        restPowerMockMvc
            .perform(get(ENTITY_API_URL_ID, power.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(power.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingPower() throws Exception {
        // Get the power
        restPowerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPower() throws Exception {
        // Initialize the database
        powerRepository.saveAndFlush(power);

        int databaseSizeBeforeUpdate = powerRepository.findAll().size();

        // Update the power
        Power updatedPower = powerRepository.findById(power.getId()).get();
        // Disconnect from session so that the updates on updatedPower are not directly saved in db
        em.detach(updatedPower);
        updatedPower.name(UPDATED_NAME).cost(UPDATED_COST).notes(UPDATED_NOTES);

        restPowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPower.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPower))
            )
            .andExpect(status().isOk());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
        Power testPower = powerList.get(powerList.size() - 1);
        assertThat(testPower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPower.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testPower.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingPower() throws Exception {
        int databaseSizeBeforeUpdate = powerRepository.findAll().size();
        power.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, power.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(power))
            )
            .andExpect(status().isBadRequest());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPower() throws Exception {
        int databaseSizeBeforeUpdate = powerRepository.findAll().size();
        power.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(power))
            )
            .andExpect(status().isBadRequest());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPower() throws Exception {
        int databaseSizeBeforeUpdate = powerRepository.findAll().size();
        power.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(power)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePowerWithPatch() throws Exception {
        // Initialize the database
        powerRepository.saveAndFlush(power);

        int databaseSizeBeforeUpdate = powerRepository.findAll().size();

        // Update the power using partial update
        Power partialUpdatedPower = new Power();
        partialUpdatedPower.setId(power.getId());

        restPowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPower.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPower))
            )
            .andExpect(status().isOk());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
        Power testPower = powerList.get(powerList.size() - 1);
        assertThat(testPower.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPower.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testPower.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdatePowerWithPatch() throws Exception {
        // Initialize the database
        powerRepository.saveAndFlush(power);

        int databaseSizeBeforeUpdate = powerRepository.findAll().size();

        // Update the power using partial update
        Power partialUpdatedPower = new Power();
        partialUpdatedPower.setId(power.getId());

        partialUpdatedPower.name(UPDATED_NAME).cost(UPDATED_COST).notes(UPDATED_NOTES);

        restPowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPower.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPower))
            )
            .andExpect(status().isOk());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
        Power testPower = powerList.get(powerList.size() - 1);
        assertThat(testPower.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPower.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testPower.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingPower() throws Exception {
        int databaseSizeBeforeUpdate = powerRepository.findAll().size();
        power.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, power.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(power))
            )
            .andExpect(status().isBadRequest());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPower() throws Exception {
        int databaseSizeBeforeUpdate = powerRepository.findAll().size();
        power.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(power))
            )
            .andExpect(status().isBadRequest());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPower() throws Exception {
        int databaseSizeBeforeUpdate = powerRepository.findAll().size();
        power.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(power)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Power in the database
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePower() throws Exception {
        // Initialize the database
        powerRepository.saveAndFlush(power);

        int databaseSizeBeforeDelete = powerRepository.findAll().size();

        // Delete the power
        restPowerMockMvc
            .perform(delete(ENTITY_API_URL_ID, power.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Power> powerList = powerRepository.findAll();
        assertThat(powerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
