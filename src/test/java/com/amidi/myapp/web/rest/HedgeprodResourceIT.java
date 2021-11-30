package com.amidi.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.amidi.myapp.IntegrationTest;
import com.amidi.myapp.domain.Hedgeprod;
import com.amidi.myapp.domain.enumeration.HedgeprodRole;
import com.amidi.myapp.repository.HedgeprodRepository;
import com.amidi.myapp.repository.search.HedgeprodSearchRepository;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HedgeprodResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HedgeprodResourceIT {

    private static final String DEFAULT_H_NAME = "AAAAAAAAAA";
    private static final String UPDATED_H_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_H_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_H_SURNAME = "BBBBBBBBBB";

    private static final HedgeprodRole DEFAULT_H_ROLE = HedgeprodRole.ADMIN;
    private static final HedgeprodRole UPDATED_H_ROLE = HedgeprodRole.DEV;

    private static final String DEFAULT_H_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_H_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_H_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_H_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVATED = false;
    private static final Boolean UPDATED_IS_ACTIVATED = true;

    private static final String ENTITY_API_URL = "/api/hedgeprods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/hedgeprods";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HedgeprodRepository hedgeprodRepository;

    /**
     * This repository is mocked in the com.amidi.myapp.repository.search test package.
     *
     * @see com.amidi.myapp.repository.search.HedgeprodSearchRepositoryMockConfiguration
     */
    @Autowired
    private HedgeprodSearchRepository mockHedgeprodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHedgeprodMockMvc;

    private Hedgeprod hedgeprod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hedgeprod createEntity(EntityManager em) {
        Hedgeprod hedgeprod = new Hedgeprod()
            .hName(DEFAULT_H_NAME)
            .hSurname(DEFAULT_H_SURNAME)
            .hRole(DEFAULT_H_ROLE)
            .hEmail(DEFAULT_H_EMAIL)
            .hPhoneNumber(DEFAULT_H_PHONE_NUMBER)
            .isActivated(DEFAULT_IS_ACTIVATED);
        return hedgeprod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hedgeprod createUpdatedEntity(EntityManager em) {
        Hedgeprod hedgeprod = new Hedgeprod()
            .hName(UPDATED_H_NAME)
            .hSurname(UPDATED_H_SURNAME)
            .hRole(UPDATED_H_ROLE)
            .hEmail(UPDATED_H_EMAIL)
            .hPhoneNumber(UPDATED_H_PHONE_NUMBER)
            .isActivated(UPDATED_IS_ACTIVATED);
        return hedgeprod;
    }

    @BeforeEach
    public void initTest() {
        hedgeprod = createEntity(em);
    }

    @Test
    @Transactional
    void createHedgeprod() throws Exception {
        int databaseSizeBeforeCreate = hedgeprodRepository.findAll().size();
        // Create the Hedgeprod
        restHedgeprodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hedgeprod)))
            .andExpect(status().isCreated());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeCreate + 1);
        Hedgeprod testHedgeprod = hedgeprodList.get(hedgeprodList.size() - 1);
        assertThat(testHedgeprod.gethName()).isEqualTo(DEFAULT_H_NAME);
        assertThat(testHedgeprod.gethSurname()).isEqualTo(DEFAULT_H_SURNAME);
        assertThat(testHedgeprod.gethRole()).isEqualTo(DEFAULT_H_ROLE);
        assertThat(testHedgeprod.gethEmail()).isEqualTo(DEFAULT_H_EMAIL);
        assertThat(testHedgeprod.gethPhoneNumber()).isEqualTo(DEFAULT_H_PHONE_NUMBER);
        assertThat(testHedgeprod.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(1)).save(testHedgeprod);
    }

    @Test
    @Transactional
    void createHedgeprodWithExistingId() throws Exception {
        // Create the Hedgeprod with an existing ID
        hedgeprod.setId(1L);

        int databaseSizeBeforeCreate = hedgeprodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHedgeprodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hedgeprod)))
            .andExpect(status().isBadRequest());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeCreate);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(0)).save(hedgeprod);
    }

    @Test
    @Transactional
    void checkhNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hedgeprodRepository.findAll().size();
        // set the field null
        hedgeprod.sethName(null);

        // Create the Hedgeprod, which fails.

        restHedgeprodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hedgeprod)))
            .andExpect(status().isBadRequest());

        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkhSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hedgeprodRepository.findAll().size();
        // set the field null
        hedgeprod.sethSurname(null);

        // Create the Hedgeprod, which fails.

        restHedgeprodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hedgeprod)))
            .andExpect(status().isBadRequest());

        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkhRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = hedgeprodRepository.findAll().size();
        // set the field null
        hedgeprod.sethRole(null);

        // Create the Hedgeprod, which fails.

        restHedgeprodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hedgeprod)))
            .andExpect(status().isBadRequest());

        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkhEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = hedgeprodRepository.findAll().size();
        // set the field null
        hedgeprod.sethEmail(null);

        // Create the Hedgeprod, which fails.

        restHedgeprodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hedgeprod)))
            .andExpect(status().isBadRequest());

        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = hedgeprodRepository.findAll().size();
        // set the field null
        hedgeprod.setIsActivated(null);

        // Create the Hedgeprod, which fails.

        restHedgeprodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hedgeprod)))
            .andExpect(status().isBadRequest());

        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHedgeprods() throws Exception {
        // Initialize the database
        hedgeprodRepository.saveAndFlush(hedgeprod);

        // Get all the hedgeprodList
        restHedgeprodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hedgeprod.getId().intValue())))
            .andExpect(jsonPath("$.[*].hName").value(hasItem(DEFAULT_H_NAME)))
            .andExpect(jsonPath("$.[*].hSurname").value(hasItem(DEFAULT_H_SURNAME)))
            .andExpect(jsonPath("$.[*].hRole").value(hasItem(DEFAULT_H_ROLE.toString())))
            .andExpect(jsonPath("$.[*].hEmail").value(hasItem(DEFAULT_H_EMAIL)))
            .andExpect(jsonPath("$.[*].hPhoneNumber").value(hasItem(DEFAULT_H_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    void getHedgeprod() throws Exception {
        // Initialize the database
        hedgeprodRepository.saveAndFlush(hedgeprod);

        // Get the hedgeprod
        restHedgeprodMockMvc
            .perform(get(ENTITY_API_URL_ID, hedgeprod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hedgeprod.getId().intValue()))
            .andExpect(jsonPath("$.hName").value(DEFAULT_H_NAME))
            .andExpect(jsonPath("$.hSurname").value(DEFAULT_H_SURNAME))
            .andExpect(jsonPath("$.hRole").value(DEFAULT_H_ROLE.toString()))
            .andExpect(jsonPath("$.hEmail").value(DEFAULT_H_EMAIL))
            .andExpect(jsonPath("$.hPhoneNumber").value(DEFAULT_H_PHONE_NUMBER))
            .andExpect(jsonPath("$.isActivated").value(DEFAULT_IS_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHedgeprod() throws Exception {
        // Get the hedgeprod
        restHedgeprodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHedgeprod() throws Exception {
        // Initialize the database
        hedgeprodRepository.saveAndFlush(hedgeprod);

        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();

        // Update the hedgeprod
        Hedgeprod updatedHedgeprod = hedgeprodRepository.findById(hedgeprod.getId()).get();
        // Disconnect from session so that the updates on updatedHedgeprod are not directly saved in db
        em.detach(updatedHedgeprod);
        updatedHedgeprod
            .hName(UPDATED_H_NAME)
            .hSurname(UPDATED_H_SURNAME)
            .hRole(UPDATED_H_ROLE)
            .hEmail(UPDATED_H_EMAIL)
            .hPhoneNumber(UPDATED_H_PHONE_NUMBER)
            .isActivated(UPDATED_IS_ACTIVATED);

        restHedgeprodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHedgeprod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHedgeprod))
            )
            .andExpect(status().isOk());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);
        Hedgeprod testHedgeprod = hedgeprodList.get(hedgeprodList.size() - 1);
        assertThat(testHedgeprod.gethName()).isEqualTo(UPDATED_H_NAME);
        assertThat(testHedgeprod.gethSurname()).isEqualTo(UPDATED_H_SURNAME);
        assertThat(testHedgeprod.gethRole()).isEqualTo(UPDATED_H_ROLE);
        assertThat(testHedgeprod.gethEmail()).isEqualTo(UPDATED_H_EMAIL);
        assertThat(testHedgeprod.gethPhoneNumber()).isEqualTo(UPDATED_H_PHONE_NUMBER);
        assertThat(testHedgeprod.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository).save(testHedgeprod);
    }

    @Test
    @Transactional
    void putNonExistingHedgeprod() throws Exception {
        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();
        hedgeprod.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHedgeprodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hedgeprod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hedgeprod))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(0)).save(hedgeprod);
    }

    @Test
    @Transactional
    void putWithIdMismatchHedgeprod() throws Exception {
        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();
        hedgeprod.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHedgeprodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hedgeprod))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(0)).save(hedgeprod);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHedgeprod() throws Exception {
        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();
        hedgeprod.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHedgeprodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hedgeprod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(0)).save(hedgeprod);
    }

    @Test
    @Transactional
    void partialUpdateHedgeprodWithPatch() throws Exception {
        // Initialize the database
        hedgeprodRepository.saveAndFlush(hedgeprod);

        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();

        // Update the hedgeprod using partial update
        Hedgeprod partialUpdatedHedgeprod = new Hedgeprod();
        partialUpdatedHedgeprod.setId(hedgeprod.getId());

        partialUpdatedHedgeprod.hName(UPDATED_H_NAME).hSurname(UPDATED_H_SURNAME).hRole(UPDATED_H_ROLE);

        restHedgeprodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHedgeprod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHedgeprod))
            )
            .andExpect(status().isOk());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);
        Hedgeprod testHedgeprod = hedgeprodList.get(hedgeprodList.size() - 1);
        assertThat(testHedgeprod.gethName()).isEqualTo(UPDATED_H_NAME);
        assertThat(testHedgeprod.gethSurname()).isEqualTo(UPDATED_H_SURNAME);
        assertThat(testHedgeprod.gethRole()).isEqualTo(UPDATED_H_ROLE);
        assertThat(testHedgeprod.gethEmail()).isEqualTo(DEFAULT_H_EMAIL);
        assertThat(testHedgeprod.gethPhoneNumber()).isEqualTo(DEFAULT_H_PHONE_NUMBER);
        assertThat(testHedgeprod.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);
    }

    @Test
    @Transactional
    void fullUpdateHedgeprodWithPatch() throws Exception {
        // Initialize the database
        hedgeprodRepository.saveAndFlush(hedgeprod);

        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();

        // Update the hedgeprod using partial update
        Hedgeprod partialUpdatedHedgeprod = new Hedgeprod();
        partialUpdatedHedgeprod.setId(hedgeprod.getId());

        partialUpdatedHedgeprod
            .hName(UPDATED_H_NAME)
            .hSurname(UPDATED_H_SURNAME)
            .hRole(UPDATED_H_ROLE)
            .hEmail(UPDATED_H_EMAIL)
            .hPhoneNumber(UPDATED_H_PHONE_NUMBER)
            .isActivated(UPDATED_IS_ACTIVATED);

        restHedgeprodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHedgeprod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHedgeprod))
            )
            .andExpect(status().isOk());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);
        Hedgeprod testHedgeprod = hedgeprodList.get(hedgeprodList.size() - 1);
        assertThat(testHedgeprod.gethName()).isEqualTo(UPDATED_H_NAME);
        assertThat(testHedgeprod.gethSurname()).isEqualTo(UPDATED_H_SURNAME);
        assertThat(testHedgeprod.gethRole()).isEqualTo(UPDATED_H_ROLE);
        assertThat(testHedgeprod.gethEmail()).isEqualTo(UPDATED_H_EMAIL);
        assertThat(testHedgeprod.gethPhoneNumber()).isEqualTo(UPDATED_H_PHONE_NUMBER);
        assertThat(testHedgeprod.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
    }

    @Test
    @Transactional
    void patchNonExistingHedgeprod() throws Exception {
        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();
        hedgeprod.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHedgeprodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hedgeprod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hedgeprod))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(0)).save(hedgeprod);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHedgeprod() throws Exception {
        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();
        hedgeprod.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHedgeprodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hedgeprod))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(0)).save(hedgeprod);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHedgeprod() throws Exception {
        int databaseSizeBeforeUpdate = hedgeprodRepository.findAll().size();
        hedgeprod.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHedgeprodMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hedgeprod))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hedgeprod in the database
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(0)).save(hedgeprod);
    }

    @Test
    @Transactional
    void deleteHedgeprod() throws Exception {
        // Initialize the database
        hedgeprodRepository.saveAndFlush(hedgeprod);

        int databaseSizeBeforeDelete = hedgeprodRepository.findAll().size();

        // Delete the hedgeprod
        restHedgeprodMockMvc
            .perform(delete(ENTITY_API_URL_ID, hedgeprod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hedgeprod> hedgeprodList = hedgeprodRepository.findAll();
        assertThat(hedgeprodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Hedgeprod in Elasticsearch
        verify(mockHedgeprodSearchRepository, times(1)).deleteById(hedgeprod.getId());
    }

    @Test
    @Transactional
    void searchHedgeprod() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        hedgeprodRepository.saveAndFlush(hedgeprod);
        when(mockHedgeprodSearchRepository.search("id:" + hedgeprod.getId())).thenReturn(Stream.of(hedgeprod));

        // Search the hedgeprod
        restHedgeprodMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + hedgeprod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hedgeprod.getId().intValue())))
            .andExpect(jsonPath("$.[*].hName").value(hasItem(DEFAULT_H_NAME)))
            .andExpect(jsonPath("$.[*].hSurname").value(hasItem(DEFAULT_H_SURNAME)))
            .andExpect(jsonPath("$.[*].hRole").value(hasItem(DEFAULT_H_ROLE.toString())))
            .andExpect(jsonPath("$.[*].hEmail").value(hasItem(DEFAULT_H_EMAIL)))
            .andExpect(jsonPath("$.[*].hPhoneNumber").value(hasItem(DEFAULT_H_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())));
    }
}
