package com.amidi.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.amidi.myapp.IntegrationTest;
import com.amidi.myapp.domain.ProUser;
import com.amidi.myapp.domain.enumeration.UserRole;
import com.amidi.myapp.repository.ProUserRepository;
import com.amidi.myapp.repository.search.ProUserSearchRepository;
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
 * Integration tests for the {@link ProUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProUserResourceIT {

    private static final String DEFAULT_PRO_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRO_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRO_USER_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_PRO_USER_SURNAME = "BBBBBBBBBB";

    private static final UserRole DEFAULT_PRO_USER_ROLE = UserRole.OWNER;
    private static final UserRole UPDATED_PRO_USER_ROLE = UserRole.USER;

    private static final String DEFAULT_PRO_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_PRO_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PRO_USER_PWD = "AAAAAAAAAA";
    private static final String UPDATED_PRO_USER_PWD = "BBBBBBBBBB";

    private static final String DEFAULT_PRO_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PRO_USER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PRO_USER_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PRO_USER_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVATED = false;
    private static final Boolean UPDATED_IS_ACTIVATED = true;

    private static final String ENTITY_API_URL = "/api/pro-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/pro-users";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProUserRepository proUserRepository;

    /**
     * This repository is mocked in the com.amidi.myapp.repository.search test package.
     *
     * @see com.amidi.myapp.repository.search.ProUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProUserSearchRepository mockProUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProUserMockMvc;

    private ProUser proUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProUser createEntity(EntityManager em) {
        ProUser proUser = new ProUser()
            .proUserName(DEFAULT_PRO_USER_NAME)
            .proUserSurname(DEFAULT_PRO_USER_SURNAME)
            .proUserRole(DEFAULT_PRO_USER_ROLE)
            .proUserLogin(DEFAULT_PRO_USER_LOGIN)
            .proUserPwd(DEFAULT_PRO_USER_PWD)
            .proUserEmail(DEFAULT_PRO_USER_EMAIL)
            .proUserPhoneNumber(DEFAULT_PRO_USER_PHONE_NUMBER)
            .isActivated(DEFAULT_IS_ACTIVATED);
        return proUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProUser createUpdatedEntity(EntityManager em) {
        ProUser proUser = new ProUser()
            .proUserName(UPDATED_PRO_USER_NAME)
            .proUserSurname(UPDATED_PRO_USER_SURNAME)
            .proUserRole(UPDATED_PRO_USER_ROLE)
            .proUserLogin(UPDATED_PRO_USER_LOGIN)
            .proUserPwd(UPDATED_PRO_USER_PWD)
            .proUserEmail(UPDATED_PRO_USER_EMAIL)
            .proUserPhoneNumber(UPDATED_PRO_USER_PHONE_NUMBER)
            .isActivated(UPDATED_IS_ACTIVATED);
        return proUser;
    }

    @BeforeEach
    public void initTest() {
        proUser = createEntity(em);
    }

    @Test
    @Transactional
    void createProUser() throws Exception {
        int databaseSizeBeforeCreate = proUserRepository.findAll().size();
        // Create the ProUser
        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isCreated());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeCreate + 1);
        ProUser testProUser = proUserList.get(proUserList.size() - 1);
        assertThat(testProUser.getProUserName()).isEqualTo(DEFAULT_PRO_USER_NAME);
        assertThat(testProUser.getProUserSurname()).isEqualTo(DEFAULT_PRO_USER_SURNAME);
        assertThat(testProUser.getProUserRole()).isEqualTo(DEFAULT_PRO_USER_ROLE);
        assertThat(testProUser.getProUserLogin()).isEqualTo(DEFAULT_PRO_USER_LOGIN);
        assertThat(testProUser.getProUserPwd()).isEqualTo(DEFAULT_PRO_USER_PWD);
        assertThat(testProUser.getProUserEmail()).isEqualTo(DEFAULT_PRO_USER_EMAIL);
        assertThat(testProUser.getProUserPhoneNumber()).isEqualTo(DEFAULT_PRO_USER_PHONE_NUMBER);
        assertThat(testProUser.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(1)).save(testProUser);
    }

    @Test
    @Transactional
    void createProUserWithExistingId() throws Exception {
        // Create the ProUser with an existing ID
        proUser.setId(1L);

        int databaseSizeBeforeCreate = proUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(0)).save(proUser);
    }

    @Test
    @Transactional
    void checkProUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = proUserRepository.findAll().size();
        // set the field null
        proUser.setProUserName(null);

        // Create the ProUser, which fails.

        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProUserSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = proUserRepository.findAll().size();
        // set the field null
        proUser.setProUserSurname(null);

        // Create the ProUser, which fails.

        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProUserRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = proUserRepository.findAll().size();
        // set the field null
        proUser.setProUserRole(null);

        // Create the ProUser, which fails.

        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = proUserRepository.findAll().size();
        // set the field null
        proUser.setProUserLogin(null);

        // Create the ProUser, which fails.

        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProUserPwdIsRequired() throws Exception {
        int databaseSizeBeforeTest = proUserRepository.findAll().size();
        // set the field null
        proUser.setProUserPwd(null);

        // Create the ProUser, which fails.

        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProUserEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = proUserRepository.findAll().size();
        // set the field null
        proUser.setProUserEmail(null);

        // Create the ProUser, which fails.

        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProUserPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = proUserRepository.findAll().size();
        // set the field null
        proUser.setProUserPhoneNumber(null);

        // Create the ProUser, which fails.

        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = proUserRepository.findAll().size();
        // set the field null
        proUser.setIsActivated(null);

        // Create the ProUser, which fails.

        restProUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isBadRequest());

        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProUsers() throws Exception {
        // Initialize the database
        proUserRepository.saveAndFlush(proUser);

        // Get all the proUserList
        restProUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].proUserName").value(hasItem(DEFAULT_PRO_USER_NAME)))
            .andExpect(jsonPath("$.[*].proUserSurname").value(hasItem(DEFAULT_PRO_USER_SURNAME)))
            .andExpect(jsonPath("$.[*].proUserRole").value(hasItem(DEFAULT_PRO_USER_ROLE.toString())))
            .andExpect(jsonPath("$.[*].proUserLogin").value(hasItem(DEFAULT_PRO_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].proUserPwd").value(hasItem(DEFAULT_PRO_USER_PWD)))
            .andExpect(jsonPath("$.[*].proUserEmail").value(hasItem(DEFAULT_PRO_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].proUserPhoneNumber").value(hasItem(DEFAULT_PRO_USER_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    void getProUser() throws Exception {
        // Initialize the database
        proUserRepository.saveAndFlush(proUser);

        // Get the proUser
        restProUserMockMvc
            .perform(get(ENTITY_API_URL_ID, proUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proUser.getId().intValue()))
            .andExpect(jsonPath("$.proUserName").value(DEFAULT_PRO_USER_NAME))
            .andExpect(jsonPath("$.proUserSurname").value(DEFAULT_PRO_USER_SURNAME))
            .andExpect(jsonPath("$.proUserRole").value(DEFAULT_PRO_USER_ROLE.toString()))
            .andExpect(jsonPath("$.proUserLogin").value(DEFAULT_PRO_USER_LOGIN))
            .andExpect(jsonPath("$.proUserPwd").value(DEFAULT_PRO_USER_PWD))
            .andExpect(jsonPath("$.proUserEmail").value(DEFAULT_PRO_USER_EMAIL))
            .andExpect(jsonPath("$.proUserPhoneNumber").value(DEFAULT_PRO_USER_PHONE_NUMBER))
            .andExpect(jsonPath("$.isActivated").value(DEFAULT_IS_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingProUser() throws Exception {
        // Get the proUser
        restProUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProUser() throws Exception {
        // Initialize the database
        proUserRepository.saveAndFlush(proUser);

        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();

        // Update the proUser
        ProUser updatedProUser = proUserRepository.findById(proUser.getId()).get();
        // Disconnect from session so that the updates on updatedProUser are not directly saved in db
        em.detach(updatedProUser);
        updatedProUser
            .proUserName(UPDATED_PRO_USER_NAME)
            .proUserSurname(UPDATED_PRO_USER_SURNAME)
            .proUserRole(UPDATED_PRO_USER_ROLE)
            .proUserLogin(UPDATED_PRO_USER_LOGIN)
            .proUserPwd(UPDATED_PRO_USER_PWD)
            .proUserEmail(UPDATED_PRO_USER_EMAIL)
            .proUserPhoneNumber(UPDATED_PRO_USER_PHONE_NUMBER)
            .isActivated(UPDATED_IS_ACTIVATED);

        restProUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProUser))
            )
            .andExpect(status().isOk());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);
        ProUser testProUser = proUserList.get(proUserList.size() - 1);
        assertThat(testProUser.getProUserName()).isEqualTo(UPDATED_PRO_USER_NAME);
        assertThat(testProUser.getProUserSurname()).isEqualTo(UPDATED_PRO_USER_SURNAME);
        assertThat(testProUser.getProUserRole()).isEqualTo(UPDATED_PRO_USER_ROLE);
        assertThat(testProUser.getProUserLogin()).isEqualTo(UPDATED_PRO_USER_LOGIN);
        assertThat(testProUser.getProUserPwd()).isEqualTo(UPDATED_PRO_USER_PWD);
        assertThat(testProUser.getProUserEmail()).isEqualTo(UPDATED_PRO_USER_EMAIL);
        assertThat(testProUser.getProUserPhoneNumber()).isEqualTo(UPDATED_PRO_USER_PHONE_NUMBER);
        assertThat(testProUser.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository).save(testProUser);
    }

    @Test
    @Transactional
    void putNonExistingProUser() throws Exception {
        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();
        proUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(0)).save(proUser);
    }

    @Test
    @Transactional
    void putWithIdMismatchProUser() throws Exception {
        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();
        proUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(0)).save(proUser);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProUser() throws Exception {
        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();
        proUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(0)).save(proUser);
    }

    @Test
    @Transactional
    void partialUpdateProUserWithPatch() throws Exception {
        // Initialize the database
        proUserRepository.saveAndFlush(proUser);

        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();

        // Update the proUser using partial update
        ProUser partialUpdatedProUser = new ProUser();
        partialUpdatedProUser.setId(proUser.getId());

        partialUpdatedProUser.proUserPwd(UPDATED_PRO_USER_PWD);

        restProUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProUser))
            )
            .andExpect(status().isOk());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);
        ProUser testProUser = proUserList.get(proUserList.size() - 1);
        assertThat(testProUser.getProUserName()).isEqualTo(DEFAULT_PRO_USER_NAME);
        assertThat(testProUser.getProUserSurname()).isEqualTo(DEFAULT_PRO_USER_SURNAME);
        assertThat(testProUser.getProUserRole()).isEqualTo(DEFAULT_PRO_USER_ROLE);
        assertThat(testProUser.getProUserLogin()).isEqualTo(DEFAULT_PRO_USER_LOGIN);
        assertThat(testProUser.getProUserPwd()).isEqualTo(UPDATED_PRO_USER_PWD);
        assertThat(testProUser.getProUserEmail()).isEqualTo(DEFAULT_PRO_USER_EMAIL);
        assertThat(testProUser.getProUserPhoneNumber()).isEqualTo(DEFAULT_PRO_USER_PHONE_NUMBER);
        assertThat(testProUser.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);
    }

    @Test
    @Transactional
    void fullUpdateProUserWithPatch() throws Exception {
        // Initialize the database
        proUserRepository.saveAndFlush(proUser);

        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();

        // Update the proUser using partial update
        ProUser partialUpdatedProUser = new ProUser();
        partialUpdatedProUser.setId(proUser.getId());

        partialUpdatedProUser
            .proUserName(UPDATED_PRO_USER_NAME)
            .proUserSurname(UPDATED_PRO_USER_SURNAME)
            .proUserRole(UPDATED_PRO_USER_ROLE)
            .proUserLogin(UPDATED_PRO_USER_LOGIN)
            .proUserPwd(UPDATED_PRO_USER_PWD)
            .proUserEmail(UPDATED_PRO_USER_EMAIL)
            .proUserPhoneNumber(UPDATED_PRO_USER_PHONE_NUMBER)
            .isActivated(UPDATED_IS_ACTIVATED);

        restProUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProUser))
            )
            .andExpect(status().isOk());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);
        ProUser testProUser = proUserList.get(proUserList.size() - 1);
        assertThat(testProUser.getProUserName()).isEqualTo(UPDATED_PRO_USER_NAME);
        assertThat(testProUser.getProUserSurname()).isEqualTo(UPDATED_PRO_USER_SURNAME);
        assertThat(testProUser.getProUserRole()).isEqualTo(UPDATED_PRO_USER_ROLE);
        assertThat(testProUser.getProUserLogin()).isEqualTo(UPDATED_PRO_USER_LOGIN);
        assertThat(testProUser.getProUserPwd()).isEqualTo(UPDATED_PRO_USER_PWD);
        assertThat(testProUser.getProUserEmail()).isEqualTo(UPDATED_PRO_USER_EMAIL);
        assertThat(testProUser.getProUserPhoneNumber()).isEqualTo(UPDATED_PRO_USER_PHONE_NUMBER);
        assertThat(testProUser.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
    }

    @Test
    @Transactional
    void patchNonExistingProUser() throws Exception {
        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();
        proUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(0)).save(proUser);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProUser() throws Exception {
        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();
        proUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(0)).save(proUser);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProUser() throws Exception {
        int databaseSizeBeforeUpdate = proUserRepository.findAll().size();
        proUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProUser in the database
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(0)).save(proUser);
    }

    @Test
    @Transactional
    void deleteProUser() throws Exception {
        // Initialize the database
        proUserRepository.saveAndFlush(proUser);

        int databaseSizeBeforeDelete = proUserRepository.findAll().size();

        // Delete the proUser
        restProUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, proUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProUser> proUserList = proUserRepository.findAll();
        assertThat(proUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProUser in Elasticsearch
        verify(mockProUserSearchRepository, times(1)).deleteById(proUser.getId());
    }

    @Test
    @Transactional
    void searchProUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        proUserRepository.saveAndFlush(proUser);
        when(mockProUserSearchRepository.search("id:" + proUser.getId())).thenReturn(Stream.of(proUser));

        // Search the proUser
        restProUserMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + proUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].proUserName").value(hasItem(DEFAULT_PRO_USER_NAME)))
            .andExpect(jsonPath("$.[*].proUserSurname").value(hasItem(DEFAULT_PRO_USER_SURNAME)))
            .andExpect(jsonPath("$.[*].proUserRole").value(hasItem(DEFAULT_PRO_USER_ROLE.toString())))
            .andExpect(jsonPath("$.[*].proUserLogin").value(hasItem(DEFAULT_PRO_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].proUserPwd").value(hasItem(DEFAULT_PRO_USER_PWD)))
            .andExpect(jsonPath("$.[*].proUserEmail").value(hasItem(DEFAULT_PRO_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].proUserPhoneNumber").value(hasItem(DEFAULT_PRO_USER_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())));
    }
}
