package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.PowerCategory;
import com.sheetsquad.app.repository.PowerCategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PowerCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PowerCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final Integer DEFAULT_COST = 1;
    private static final Integer UPDATED_COST = 2;

    private static final String ENTITY_API_URL = "/api/power-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PowerCategoryRepository powerCategoryRepository;

    @Mock
    private PowerCategoryRepository powerCategoryRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPowerCategoryMockMvc;

    private PowerCategory powerCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PowerCategory createEntity(EntityManager em) {
        PowerCategory powerCategory = new PowerCategory().name(DEFAULT_NAME).priority(DEFAULT_PRIORITY).cost(DEFAULT_COST);
        return powerCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PowerCategory createUpdatedEntity(EntityManager em) {
        PowerCategory powerCategory = new PowerCategory().name(UPDATED_NAME).priority(UPDATED_PRIORITY).cost(UPDATED_COST);
        return powerCategory;
    }

    @BeforeEach
    public void initTest() {
        powerCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createPowerCategory() throws Exception {
        int databaseSizeBeforeCreate = powerCategoryRepository.findAll().size();
        // Create the PowerCategory
        restPowerCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(powerCategory)))
            .andExpect(status().isCreated());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        PowerCategory testPowerCategory = powerCategoryList.get(powerCategoryList.size() - 1);
        assertThat(testPowerCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPowerCategory.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testPowerCategory.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void createPowerCategoryWithExistingId() throws Exception {
        // Create the PowerCategory with an existing ID
        powerCategory.setId(1L);

        int databaseSizeBeforeCreate = powerCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPowerCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(powerCategory)))
            .andExpect(status().isBadRequest());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerCategoryRepository.findAll().size();
        // set the field null
        powerCategory.setName(null);

        // Create the PowerCategory, which fails.

        restPowerCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(powerCategory)))
            .andExpect(status().isBadRequest());

        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerCategoryRepository.findAll().size();
        // set the field null
        powerCategory.setCost(null);

        // Create the PowerCategory, which fails.

        restPowerCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(powerCategory)))
            .andExpect(status().isBadRequest());

        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPowerCategories() throws Exception {
        // Initialize the database
        powerCategoryRepository.saveAndFlush(powerCategory);

        // Get all the powerCategoryList
        restPowerCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(powerCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPowerCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(powerCategoryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPowerCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(powerCategoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPowerCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(powerCategoryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPowerCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(powerCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPowerCategory() throws Exception {
        // Initialize the database
        powerCategoryRepository.saveAndFlush(powerCategory);

        // Get the powerCategory
        restPowerCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, powerCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(powerCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST));
    }

    @Test
    @Transactional
    void getNonExistingPowerCategory() throws Exception {
        // Get the powerCategory
        restPowerCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPowerCategory() throws Exception {
        // Initialize the database
        powerCategoryRepository.saveAndFlush(powerCategory);

        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();

        // Update the powerCategory
        PowerCategory updatedPowerCategory = powerCategoryRepository.findById(powerCategory.getId()).get();
        // Disconnect from session so that the updates on updatedPowerCategory are not directly saved in db
        em.detach(updatedPowerCategory);
        updatedPowerCategory.name(UPDATED_NAME).priority(UPDATED_PRIORITY).cost(UPDATED_COST);

        restPowerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPowerCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPowerCategory))
            )
            .andExpect(status().isOk());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
        PowerCategory testPowerCategory = powerCategoryList.get(powerCategoryList.size() - 1);
        assertThat(testPowerCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPowerCategory.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testPowerCategory.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void putNonExistingPowerCategory() throws Exception {
        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();
        powerCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPowerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, powerCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(powerCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPowerCategory() throws Exception {
        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();
        powerCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(powerCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPowerCategory() throws Exception {
        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();
        powerCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(powerCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePowerCategoryWithPatch() throws Exception {
        // Initialize the database
        powerCategoryRepository.saveAndFlush(powerCategory);

        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();

        // Update the powerCategory using partial update
        PowerCategory partialUpdatedPowerCategory = new PowerCategory();
        partialUpdatedPowerCategory.setId(powerCategory.getId());

        partialUpdatedPowerCategory.cost(UPDATED_COST);

        restPowerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPowerCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPowerCategory))
            )
            .andExpect(status().isOk());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
        PowerCategory testPowerCategory = powerCategoryList.get(powerCategoryList.size() - 1);
        assertThat(testPowerCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPowerCategory.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testPowerCategory.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void fullUpdatePowerCategoryWithPatch() throws Exception {
        // Initialize the database
        powerCategoryRepository.saveAndFlush(powerCategory);

        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();

        // Update the powerCategory using partial update
        PowerCategory partialUpdatedPowerCategory = new PowerCategory();
        partialUpdatedPowerCategory.setId(powerCategory.getId());

        partialUpdatedPowerCategory.name(UPDATED_NAME).priority(UPDATED_PRIORITY).cost(UPDATED_COST);

        restPowerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPowerCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPowerCategory))
            )
            .andExpect(status().isOk());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
        PowerCategory testPowerCategory = powerCategoryList.get(powerCategoryList.size() - 1);
        assertThat(testPowerCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPowerCategory.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testPowerCategory.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void patchNonExistingPowerCategory() throws Exception {
        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();
        powerCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPowerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, powerCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(powerCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPowerCategory() throws Exception {
        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();
        powerCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(powerCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPowerCategory() throws Exception {
        int databaseSizeBeforeUpdate = powerCategoryRepository.findAll().size();
        powerCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPowerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(powerCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PowerCategory in the database
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePowerCategory() throws Exception {
        // Initialize the database
        powerCategoryRepository.saveAndFlush(powerCategory);

        int databaseSizeBeforeDelete = powerCategoryRepository.findAll().size();

        // Delete the powerCategory
        restPowerCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, powerCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PowerCategory> powerCategoryList = powerCategoryRepository.findAll();
        assertThat(powerCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
