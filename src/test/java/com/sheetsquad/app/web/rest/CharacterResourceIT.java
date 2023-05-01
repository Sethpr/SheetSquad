package com.sheetsquad.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheetsquad.app.IntegrationTest;
import com.sheetsquad.app.domain.Character;
import com.sheetsquad.app.repository.CharacterRepository;
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
 * Integration tests for the {@link CharacterResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CharacterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TALENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TALENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOYALTY = "AAAAAAAAAA";
    private static final String UPDATED_LOYALTY = "BBBBBBBBBB";

    private static final String DEFAULT_PASSION = "AAAAAAAAAA";
    private static final String UPDATED_PASSION = "BBBBBBBBBB";

    private static final String DEFAULT_INVENTORY = "AAAAAAAAAA";
    private static final String UPDATED_INVENTORY = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINT_TOTAL = 1;
    private static final Integer UPDATED_POINT_TOTAL = 2;

    private static final Integer DEFAULT_SPENT_POINTS = 1;
    private static final Integer UPDATED_SPENT_POINTS = 2;

    private static final String ENTITY_API_URL = "/api/characters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CharacterRepository characterRepository;

    @Mock
    private CharacterRepository characterRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCharacterMockMvc;

    private Character character;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Character createEntity(EntityManager em) {
        Character character = new Character()
            .name(DEFAULT_NAME)
            .talentName(DEFAULT_TALENT_NAME)
            .loyalty(DEFAULT_LOYALTY)
            .passion(DEFAULT_PASSION)
            .inventory(DEFAULT_INVENTORY)
            .pointTotal(DEFAULT_POINT_TOTAL)
            .spentPoints(DEFAULT_SPENT_POINTS);
        return character;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Character createUpdatedEntity(EntityManager em) {
        Character character = new Character()
            .name(UPDATED_NAME)
            .talentName(UPDATED_TALENT_NAME)
            .loyalty(UPDATED_LOYALTY)
            .passion(UPDATED_PASSION)
            .inventory(UPDATED_INVENTORY)
            .pointTotal(UPDATED_POINT_TOTAL)
            .spentPoints(UPDATED_SPENT_POINTS);
        return character;
    }

    @BeforeEach
    public void initTest() {
        character = createEntity(em);
    }

    @Test
    @Transactional
    void createCharacter() throws Exception {
        int databaseSizeBeforeCreate = characterRepository.findAll().size();
        // Create the Character
        restCharacterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isCreated());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeCreate + 1);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacter.getTalentName()).isEqualTo(DEFAULT_TALENT_NAME);
        assertThat(testCharacter.getLoyalty()).isEqualTo(DEFAULT_LOYALTY);
        assertThat(testCharacter.getPassion()).isEqualTo(DEFAULT_PASSION);
        assertThat(testCharacter.getInventory()).isEqualTo(DEFAULT_INVENTORY);
        assertThat(testCharacter.getPointTotal()).isEqualTo(DEFAULT_POINT_TOTAL);
        assertThat(testCharacter.getSpentPoints()).isEqualTo(DEFAULT_SPENT_POINTS);
    }

    @Test
    @Transactional
    void createCharacterWithExistingId() throws Exception {
        // Create the Character with an existing ID
        character.setId(1L);

        int databaseSizeBeforeCreate = characterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().size();
        // set the field null
        character.setName(null);

        // Create the Character, which fails.

        restCharacterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isBadRequest());

        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().size();
        // set the field null
        character.setPointTotal(null);

        // Create the Character, which fails.

        restCharacterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isBadRequest());

        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpentPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().size();
        // set the field null
        character.setSpentPoints(null);

        // Create the Character, which fails.

        restCharacterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isBadRequest());

        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCharacters() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        // Get all the characterList
        restCharacterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(character.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].talentName").value(hasItem(DEFAULT_TALENT_NAME)))
            .andExpect(jsonPath("$.[*].loyalty").value(hasItem(DEFAULT_LOYALTY)))
            .andExpect(jsonPath("$.[*].passion").value(hasItem(DEFAULT_PASSION)))
            .andExpect(jsonPath("$.[*].inventory").value(hasItem(DEFAULT_INVENTORY)))
            .andExpect(jsonPath("$.[*].pointTotal").value(hasItem(DEFAULT_POINT_TOTAL)))
            .andExpect(jsonPath("$.[*].spentPoints").value(hasItem(DEFAULT_SPENT_POINTS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCharactersWithEagerRelationshipsIsEnabled() throws Exception {
        when(characterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCharacterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(characterRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCharactersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(characterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCharacterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(characterRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        // Get the character
        restCharacterMockMvc
            .perform(get(ENTITY_API_URL_ID, character.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(character.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.talentName").value(DEFAULT_TALENT_NAME))
            .andExpect(jsonPath("$.loyalty").value(DEFAULT_LOYALTY))
            .andExpect(jsonPath("$.passion").value(DEFAULT_PASSION))
            .andExpect(jsonPath("$.inventory").value(DEFAULT_INVENTORY))
            .andExpect(jsonPath("$.pointTotal").value(DEFAULT_POINT_TOTAL))
            .andExpect(jsonPath("$.spentPoints").value(DEFAULT_SPENT_POINTS));
    }

    @Test
    @Transactional
    void getNonExistingCharacter() throws Exception {
        // Get the character
        restCharacterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character
        Character updatedCharacter = characterRepository.findById(character.getId()).get();
        // Disconnect from session so that the updates on updatedCharacter are not directly saved in db
        em.detach(updatedCharacter);
        updatedCharacter
            .name(UPDATED_NAME)
            .talentName(UPDATED_TALENT_NAME)
            .loyalty(UPDATED_LOYALTY)
            .passion(UPDATED_PASSION)
            .inventory(UPDATED_INVENTORY)
            .pointTotal(UPDATED_POINT_TOTAL)
            .spentPoints(UPDATED_SPENT_POINTS);

        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCharacter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCharacter))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacter.getTalentName()).isEqualTo(UPDATED_TALENT_NAME);
        assertThat(testCharacter.getLoyalty()).isEqualTo(UPDATED_LOYALTY);
        assertThat(testCharacter.getPassion()).isEqualTo(UPDATED_PASSION);
        assertThat(testCharacter.getInventory()).isEqualTo(UPDATED_INVENTORY);
        assertThat(testCharacter.getPointTotal()).isEqualTo(UPDATED_POINT_TOTAL);
        assertThat(testCharacter.getSpentPoints()).isEqualTo(UPDATED_SPENT_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, character.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(character)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacterWithPatch() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character using partial update
        Character partialUpdatedCharacter = new Character();
        partialUpdatedCharacter.setId(character.getId());

        partialUpdatedCharacter.pointTotal(UPDATED_POINT_TOTAL).spentPoints(UPDATED_SPENT_POINTS);

        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacter))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacter.getTalentName()).isEqualTo(DEFAULT_TALENT_NAME);
        assertThat(testCharacter.getLoyalty()).isEqualTo(DEFAULT_LOYALTY);
        assertThat(testCharacter.getPassion()).isEqualTo(DEFAULT_PASSION);
        assertThat(testCharacter.getInventory()).isEqualTo(DEFAULT_INVENTORY);
        assertThat(testCharacter.getPointTotal()).isEqualTo(UPDATED_POINT_TOTAL);
        assertThat(testCharacter.getSpentPoints()).isEqualTo(UPDATED_SPENT_POINTS);
    }

    @Test
    @Transactional
    void fullUpdateCharacterWithPatch() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character using partial update
        Character partialUpdatedCharacter = new Character();
        partialUpdatedCharacter.setId(character.getId());

        partialUpdatedCharacter
            .name(UPDATED_NAME)
            .talentName(UPDATED_TALENT_NAME)
            .loyalty(UPDATED_LOYALTY)
            .passion(UPDATED_PASSION)
            .inventory(UPDATED_INVENTORY)
            .pointTotal(UPDATED_POINT_TOTAL)
            .spentPoints(UPDATED_SPENT_POINTS);

        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacter))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacter.getTalentName()).isEqualTo(UPDATED_TALENT_NAME);
        assertThat(testCharacter.getLoyalty()).isEqualTo(UPDATED_LOYALTY);
        assertThat(testCharacter.getPassion()).isEqualTo(UPDATED_PASSION);
        assertThat(testCharacter.getInventory()).isEqualTo(UPDATED_INVENTORY);
        assertThat(testCharacter.getPointTotal()).isEqualTo(UPDATED_POINT_TOTAL);
        assertThat(testCharacter.getSpentPoints()).isEqualTo(UPDATED_SPENT_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, character.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(character))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeDelete = characterRepository.findAll().size();

        // Delete the character
        restCharacterMockMvc
            .perform(delete(ENTITY_API_URL_ID, character.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
