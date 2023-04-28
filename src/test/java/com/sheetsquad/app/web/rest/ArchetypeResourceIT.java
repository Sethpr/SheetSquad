package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.Archetype;
import com.sheetsquad.app.repository.ArchetypeRepository;
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
 * Integration tests for the {@link ArchetypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArchetypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COST = 1;
    private static final Integer UPDATED_COST = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/archetypes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArchetypeRepository archetypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArchetypeMockMvc;

    private Archetype archetype;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Archetype createEntity(EntityManager em) {
        Archetype archetype = new Archetype().name(DEFAULT_NAME).cost(DEFAULT_COST).notes(DEFAULT_NOTES);
        return archetype;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Archetype createUpdatedEntity(EntityManager em) {
        Archetype archetype = new Archetype().name(UPDATED_NAME).cost(UPDATED_COST).notes(UPDATED_NOTES);
        return archetype;
    }

    @BeforeEach
    public void initTest() {
        archetype = createEntity(em);
    }

    @Test
    @Transactional
    void createArchetype() throws Exception {
        int databaseSizeBeforeCreate = archetypeRepository.findAll().size();
        // Create the Archetype
        restArchetypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archetype)))
            .andExpect(status().isCreated());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeCreate + 1);
        Archetype testArchetype = archetypeList.get(archetypeList.size() - 1);
        assertThat(testArchetype.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArchetype.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testArchetype.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createArchetypeWithExistingId() throws Exception {
        // Create the Archetype with an existing ID
        archetype.setId(1L);

        int databaseSizeBeforeCreate = archetypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArchetypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archetype)))
            .andExpect(status().isBadRequest());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = archetypeRepository.findAll().size();
        // set the field null
        archetype.setCost(null);

        // Create the Archetype, which fails.

        restArchetypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archetype)))
            .andExpect(status().isBadRequest());

        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArchetypes() throws Exception {
        // Initialize the database
        archetypeRepository.saveAndFlush(archetype);

        // Get all the archetypeList
        restArchetypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archetype.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getArchetype() throws Exception {
        // Initialize the database
        archetypeRepository.saveAndFlush(archetype);

        // Get the archetype
        restArchetypeMockMvc
            .perform(get(ENTITY_API_URL_ID, archetype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(archetype.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingArchetype() throws Exception {
        // Get the archetype
        restArchetypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArchetype() throws Exception {
        // Initialize the database
        archetypeRepository.saveAndFlush(archetype);

        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();

        // Update the archetype
        Archetype updatedArchetype = archetypeRepository.findById(archetype.getId()).get();
        // Disconnect from session so that the updates on updatedArchetype are not directly saved in db
        em.detach(updatedArchetype);
        updatedArchetype.name(UPDATED_NAME).cost(UPDATED_COST).notes(UPDATED_NOTES);

        restArchetypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArchetype.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArchetype))
            )
            .andExpect(status().isOk());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
        Archetype testArchetype = archetypeList.get(archetypeList.size() - 1);
        assertThat(testArchetype.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArchetype.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testArchetype.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingArchetype() throws Exception {
        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();
        archetype.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchetypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archetype.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(archetype))
            )
            .andExpect(status().isBadRequest());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArchetype() throws Exception {
        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();
        archetype.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchetypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(archetype))
            )
            .andExpect(status().isBadRequest());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArchetype() throws Exception {
        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();
        archetype.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchetypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archetype)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArchetypeWithPatch() throws Exception {
        // Initialize the database
        archetypeRepository.saveAndFlush(archetype);

        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();

        // Update the archetype using partial update
        Archetype partialUpdatedArchetype = new Archetype();
        partialUpdatedArchetype.setId(archetype.getId());

        partialUpdatedArchetype.cost(UPDATED_COST);

        restArchetypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchetype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArchetype))
            )
            .andExpect(status().isOk());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
        Archetype testArchetype = archetypeList.get(archetypeList.size() - 1);
        assertThat(testArchetype.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArchetype.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testArchetype.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateArchetypeWithPatch() throws Exception {
        // Initialize the database
        archetypeRepository.saveAndFlush(archetype);

        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();

        // Update the archetype using partial update
        Archetype partialUpdatedArchetype = new Archetype();
        partialUpdatedArchetype.setId(archetype.getId());

        partialUpdatedArchetype.name(UPDATED_NAME).cost(UPDATED_COST).notes(UPDATED_NOTES);

        restArchetypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchetype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArchetype))
            )
            .andExpect(status().isOk());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
        Archetype testArchetype = archetypeList.get(archetypeList.size() - 1);
        assertThat(testArchetype.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArchetype.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testArchetype.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingArchetype() throws Exception {
        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();
        archetype.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchetypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, archetype.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(archetype))
            )
            .andExpect(status().isBadRequest());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArchetype() throws Exception {
        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();
        archetype.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchetypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(archetype))
            )
            .andExpect(status().isBadRequest());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArchetype() throws Exception {
        int databaseSizeBeforeUpdate = archetypeRepository.findAll().size();
        archetype.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchetypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(archetype))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Archetype in the database
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArchetype() throws Exception {
        // Initialize the database
        archetypeRepository.saveAndFlush(archetype);

        int databaseSizeBeforeDelete = archetypeRepository.findAll().size();

        // Delete the archetype
        restArchetypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, archetype.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Archetype> archetypeList = archetypeRepository.findAll();
        assertThat(archetypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
