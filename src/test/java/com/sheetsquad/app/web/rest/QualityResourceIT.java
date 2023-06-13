package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.Quality;
import com.sheetsquad.app.domain.enumeration.Capacity;
import com.sheetsquad.app.domain.enumeration.Capacity;
import com.sheetsquad.app.domain.enumeration.Capacity;
import com.sheetsquad.app.domain.enumeration.QualityType;
import com.sheetsquad.app.repository.QualityRepository;
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
 * Integration tests for the {@link QualityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QualityResourceIT {

    private static final QualityType DEFAULT_QUALITY_TYPE = QualityType.ATTACK;
    private static final QualityType UPDATED_QUALITY_TYPE = QualityType.USEFUL;

    private static final Capacity DEFAULT_CAPACITY_1 = Capacity.SPEED;
    private static final Capacity UPDATED_CAPACITY_1 = Capacity.RANGE;

    private static final Capacity DEFAULT_CAPACITY_2 = Capacity.SPEED;
    private static final Capacity UPDATED_CAPACITY_2 = Capacity.RANGE;

    private static final Capacity DEFAULT_CAPACITY_3 = Capacity.SPEED;
    private static final Capacity UPDATED_CAPACITY_3 = Capacity.RANGE;

    private static final Integer DEFAULT_COST = 1;
    private static final Integer UPDATED_COST = 2;

    private static final String ENTITY_API_URL = "/api/qualities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QualityRepository qualityRepository;

    @Mock
    private QualityRepository qualityRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQualityMockMvc;

    private Quality quality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quality createEntity(EntityManager em) {
        Quality quality = new Quality()
            .qualityType(DEFAULT_QUALITY_TYPE)
            .capacity1(DEFAULT_CAPACITY_1)
            .capacity2(DEFAULT_CAPACITY_2)
            .capacity3(DEFAULT_CAPACITY_3)
            .cost(DEFAULT_COST);
        return quality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quality createUpdatedEntity(EntityManager em) {
        Quality quality = new Quality()
            .qualityType(UPDATED_QUALITY_TYPE)
            .capacity1(UPDATED_CAPACITY_1)
            .capacity2(UPDATED_CAPACITY_2)
            .capacity3(UPDATED_CAPACITY_3)
            .cost(UPDATED_COST);
        return quality;
    }

    @BeforeEach
    public void initTest() {
        quality = createEntity(em);
    }

    @Test
    @Transactional
    void createQuality() throws Exception {
        int databaseSizeBeforeCreate = qualityRepository.findAll().size();
        // Create the Quality
        restQualityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quality)))
            .andExpect(status().isCreated());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeCreate + 1);
        Quality testQuality = qualityList.get(qualityList.size() - 1);
        assertThat(testQuality.getQualityType()).isEqualTo(DEFAULT_QUALITY_TYPE);
        assertThat(testQuality.getCapacity1()).isEqualTo(DEFAULT_CAPACITY_1);
        assertThat(testQuality.getCapacity2()).isEqualTo(DEFAULT_CAPACITY_2);
        assertThat(testQuality.getCapacity3()).isEqualTo(DEFAULT_CAPACITY_3);
        assertThat(testQuality.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void createQualityWithExistingId() throws Exception {
        // Create the Quality with an existing ID
        quality.setId(1L);

        int databaseSizeBeforeCreate = qualityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQualityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quality)))
            .andExpect(status().isBadRequest());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQualityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = qualityRepository.findAll().size();
        // set the field null
        quality.setQualityType(null);

        // Create the Quality, which fails.

        restQualityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quality)))
            .andExpect(status().isBadRequest());

        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacity1IsRequired() throws Exception {
        int databaseSizeBeforeTest = qualityRepository.findAll().size();
        // set the field null
        quality.setCapacity1(null);

        // Create the Quality, which fails.

        restQualityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quality)))
            .andExpect(status().isBadRequest());

        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = qualityRepository.findAll().size();
        // set the field null
        quality.setCost(null);

        // Create the Quality, which fails.

        restQualityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quality)))
            .andExpect(status().isBadRequest());

        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQualities() throws Exception {
        // Initialize the database
        qualityRepository.saveAndFlush(quality);

        // Get all the qualityList
        restQualityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quality.getId().intValue())))
            .andExpect(jsonPath("$.[*].qualityType").value(hasItem(DEFAULT_QUALITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].capacity1").value(hasItem(DEFAULT_CAPACITY_1.toString())))
            .andExpect(jsonPath("$.[*].capacity2").value(hasItem(DEFAULT_CAPACITY_2.toString())))
            .andExpect(jsonPath("$.[*].capacity3").value(hasItem(DEFAULT_CAPACITY_3.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQualitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(qualityRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQualityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(qualityRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQualitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(qualityRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQualityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(qualityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuality() throws Exception {
        // Initialize the database
        qualityRepository.saveAndFlush(quality);

        // Get the quality
        restQualityMockMvc
            .perform(get(ENTITY_API_URL_ID, quality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quality.getId().intValue()))
            .andExpect(jsonPath("$.qualityType").value(DEFAULT_QUALITY_TYPE.toString()))
            .andExpect(jsonPath("$.capacity1").value(DEFAULT_CAPACITY_1.toString()))
            .andExpect(jsonPath("$.capacity2").value(DEFAULT_CAPACITY_2.toString()))
            .andExpect(jsonPath("$.capacity3").value(DEFAULT_CAPACITY_3.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST));
    }

    @Test
    @Transactional
    void getNonExistingQuality() throws Exception {
        // Get the quality
        restQualityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuality() throws Exception {
        // Initialize the database
        qualityRepository.saveAndFlush(quality);

        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();

        // Update the quality
        Quality updatedQuality = qualityRepository.findById(quality.getId()).get();
        // Disconnect from session so that the updates on updatedQuality are not directly saved in db
        em.detach(updatedQuality);
        updatedQuality
            .qualityType(UPDATED_QUALITY_TYPE)
            .capacity1(UPDATED_CAPACITY_1)
            .capacity2(UPDATED_CAPACITY_2)
            .capacity3(UPDATED_CAPACITY_3)
            .cost(UPDATED_COST);

        restQualityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuality.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuality))
            )
            .andExpect(status().isOk());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
        Quality testQuality = qualityList.get(qualityList.size() - 1);
        assertThat(testQuality.getQualityType()).isEqualTo(UPDATED_QUALITY_TYPE);
        assertThat(testQuality.getCapacity1()).isEqualTo(UPDATED_CAPACITY_1);
        assertThat(testQuality.getCapacity2()).isEqualTo(UPDATED_CAPACITY_2);
        assertThat(testQuality.getCapacity3()).isEqualTo(UPDATED_CAPACITY_3);
        assertThat(testQuality.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void putNonExistingQuality() throws Exception {
        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();
        quality.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQualityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quality.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuality() throws Exception {
        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();
        quality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQualityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuality() throws Exception {
        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();
        quality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQualityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quality)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQualityWithPatch() throws Exception {
        // Initialize the database
        qualityRepository.saveAndFlush(quality);

        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();

        // Update the quality using partial update
        Quality partialUpdatedQuality = new Quality();
        partialUpdatedQuality.setId(quality.getId());

        partialUpdatedQuality.qualityType(UPDATED_QUALITY_TYPE).capacity1(UPDATED_CAPACITY_1).capacity3(UPDATED_CAPACITY_3);

        restQualityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuality))
            )
            .andExpect(status().isOk());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
        Quality testQuality = qualityList.get(qualityList.size() - 1);
        assertThat(testQuality.getQualityType()).isEqualTo(UPDATED_QUALITY_TYPE);
        assertThat(testQuality.getCapacity1()).isEqualTo(UPDATED_CAPACITY_1);
        assertThat(testQuality.getCapacity2()).isEqualTo(DEFAULT_CAPACITY_2);
        assertThat(testQuality.getCapacity3()).isEqualTo(UPDATED_CAPACITY_3);
        assertThat(testQuality.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void fullUpdateQualityWithPatch() throws Exception {
        // Initialize the database
        qualityRepository.saveAndFlush(quality);

        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();

        // Update the quality using partial update
        Quality partialUpdatedQuality = new Quality();
        partialUpdatedQuality.setId(quality.getId());

        partialUpdatedQuality
            .qualityType(UPDATED_QUALITY_TYPE)
            .capacity1(UPDATED_CAPACITY_1)
            .capacity2(UPDATED_CAPACITY_2)
            .capacity3(UPDATED_CAPACITY_3)
            .cost(UPDATED_COST);

        restQualityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuality))
            )
            .andExpect(status().isOk());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
        Quality testQuality = qualityList.get(qualityList.size() - 1);
        assertThat(testQuality.getQualityType()).isEqualTo(UPDATED_QUALITY_TYPE);
        assertThat(testQuality.getCapacity1()).isEqualTo(UPDATED_CAPACITY_1);
        assertThat(testQuality.getCapacity2()).isEqualTo(UPDATED_CAPACITY_2);
        assertThat(testQuality.getCapacity3()).isEqualTo(UPDATED_CAPACITY_3);
        assertThat(testQuality.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void patchNonExistingQuality() throws Exception {
        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();
        quality.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQualityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuality() throws Exception {
        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();
        quality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQualityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuality() throws Exception {
        int databaseSizeBeforeUpdate = qualityRepository.findAll().size();
        quality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQualityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quality)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quality in the database
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuality() throws Exception {
        // Initialize the database
        qualityRepository.saveAndFlush(quality);

        int databaseSizeBeforeDelete = qualityRepository.findAll().size();

        // Delete the quality
        restQualityMockMvc
            .perform(delete(ENTITY_API_URL_ID, quality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quality> qualityList = qualityRepository.findAll();
        assertThat(qualityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
