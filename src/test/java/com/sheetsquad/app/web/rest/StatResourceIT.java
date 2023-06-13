package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.Stat;
import com.sheetsquad.app.domain.enumeration.StatType;
import com.sheetsquad.app.repository.StatRepository;
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
 * Integration tests for the {@link StatResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StatResourceIT {

    private static final StatType DEFAULT_STAT_TYPE = StatType.BODY;
    private static final StatType UPDATED_STAT_TYPE = StatType.COORDINATION;

    private static final String ENTITY_API_URL = "/api/stats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatRepository statRepository;

    @Mock
    private StatRepository statRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatMockMvc;

    private Stat stat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stat createEntity(EntityManager em) {
        Stat stat = new Stat().statType(DEFAULT_STAT_TYPE);
        return stat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stat createUpdatedEntity(EntityManager em) {
        Stat stat = new Stat().statType(UPDATED_STAT_TYPE);
        return stat;
    }

    @BeforeEach
    public void initTest() {
        stat = createEntity(em);
    }

    @Test
    @Transactional
    void createStat() throws Exception {
        int databaseSizeBeforeCreate = statRepository.findAll().size();
        // Create the Stat
        restStatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stat)))
            .andExpect(status().isCreated());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeCreate + 1);
        Stat testStat = statList.get(statList.size() - 1);
        assertThat(testStat.getStatType()).isEqualTo(DEFAULT_STAT_TYPE);
    }

    @Test
    @Transactional
    void createStatWithExistingId() throws Exception {
        // Create the Stat with an existing ID
        stat.setId(1L);

        int databaseSizeBeforeCreate = statRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stat)))
            .andExpect(status().isBadRequest());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = statRepository.findAll().size();
        // set the field null
        stat.setStatType(null);

        // Create the Stat, which fails.

        restStatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stat)))
            .andExpect(status().isBadRequest());

        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStats() throws Exception {
        // Initialize the database
        statRepository.saveAndFlush(stat);

        // Get all the statList
        restStatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stat.getId().intValue())))
            .andExpect(jsonPath("$.[*].statType").value(hasItem(DEFAULT_STAT_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatsWithEagerRelationshipsIsEnabled() throws Exception {
        when(statRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(statRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(statRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStat() throws Exception {
        // Initialize the database
        statRepository.saveAndFlush(stat);

        // Get the stat
        restStatMockMvc
            .perform(get(ENTITY_API_URL_ID, stat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stat.getId().intValue()))
            .andExpect(jsonPath("$.statType").value(DEFAULT_STAT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStat() throws Exception {
        // Get the stat
        restStatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStat() throws Exception {
        // Initialize the database
        statRepository.saveAndFlush(stat);

        int databaseSizeBeforeUpdate = statRepository.findAll().size();

        // Update the stat
        Stat updatedStat = statRepository.findById(stat.getId()).get();
        // Disconnect from session so that the updates on updatedStat are not directly saved in db
        em.detach(updatedStat);
        updatedStat.statType(UPDATED_STAT_TYPE);

        restStatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStat))
            )
            .andExpect(status().isOk());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
        Stat testStat = statList.get(statList.size() - 1);
        assertThat(testStat.getStatType()).isEqualTo(UPDATED_STAT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingStat() throws Exception {
        int databaseSizeBeforeUpdate = statRepository.findAll().size();
        stat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStat() throws Exception {
        int databaseSizeBeforeUpdate = statRepository.findAll().size();
        stat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStat() throws Exception {
        int databaseSizeBeforeUpdate = statRepository.findAll().size();
        stat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatWithPatch() throws Exception {
        // Initialize the database
        statRepository.saveAndFlush(stat);

        int databaseSizeBeforeUpdate = statRepository.findAll().size();

        // Update the stat using partial update
        Stat partialUpdatedStat = new Stat();
        partialUpdatedStat.setId(stat.getId());

        restStatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStat))
            )
            .andExpect(status().isOk());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
        Stat testStat = statList.get(statList.size() - 1);
        assertThat(testStat.getStatType()).isEqualTo(DEFAULT_STAT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateStatWithPatch() throws Exception {
        // Initialize the database
        statRepository.saveAndFlush(stat);

        int databaseSizeBeforeUpdate = statRepository.findAll().size();

        // Update the stat using partial update
        Stat partialUpdatedStat = new Stat();
        partialUpdatedStat.setId(stat.getId());

        partialUpdatedStat.statType(UPDATED_STAT_TYPE);

        restStatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStat))
            )
            .andExpect(status().isOk());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
        Stat testStat = statList.get(statList.size() - 1);
        assertThat(testStat.getStatType()).isEqualTo(UPDATED_STAT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingStat() throws Exception {
        int databaseSizeBeforeUpdate = statRepository.findAll().size();
        stat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStat() throws Exception {
        int databaseSizeBeforeUpdate = statRepository.findAll().size();
        stat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStat() throws Exception {
        int databaseSizeBeforeUpdate = statRepository.findAll().size();
        stat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stat in the database
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStat() throws Exception {
        // Initialize the database
        statRepository.saveAndFlush(stat);

        int databaseSizeBeforeDelete = statRepository.findAll().size();

        // Delete the stat
        restStatMockMvc
            .perform(delete(ENTITY_API_URL_ID, stat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stat> statList = statRepository.findAll();
        assertThat(statList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
