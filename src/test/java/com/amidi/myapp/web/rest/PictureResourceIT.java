package com.amidi.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.amidi.myapp.IntegrationTest;
import com.amidi.myapp.domain.Picture;
import com.amidi.myapp.repository.PictureRepository;
import com.amidi.myapp.repository.search.PictureSearchRepository;
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
 * Integration tests for the {@link PictureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PictureResourceIT {

    private static final String DEFAULT_PICTURE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE_ALT = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_ALT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LOGO = false;
    private static final Boolean UPDATED_IS_LOGO = true;

    private static final Boolean DEFAULT_IS_DISPLAYED = false;
    private static final Boolean UPDATED_IS_DISPLAYED = true;

    private static final String ENTITY_API_URL = "/api/pictures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/pictures";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PictureRepository pictureRepository;

    /**
     * This repository is mocked in the com.amidi.myapp.repository.search test package.
     *
     * @see com.amidi.myapp.repository.search.PictureSearchRepositoryMockConfiguration
     */
    @Autowired
    private PictureSearchRepository mockPictureSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPictureMockMvc;

    private Picture picture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Picture createEntity(EntityManager em) {
        Picture picture = new Picture()
            .pictureName(DEFAULT_PICTURE_NAME)
            .pictureUrl(DEFAULT_PICTURE_URL)
            .pictureAlt(DEFAULT_PICTURE_ALT)
            .isLogo(DEFAULT_IS_LOGO)
            .isDisplayed(DEFAULT_IS_DISPLAYED);
        return picture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Picture createUpdatedEntity(EntityManager em) {
        Picture picture = new Picture()
            .pictureName(UPDATED_PICTURE_NAME)
            .pictureUrl(UPDATED_PICTURE_URL)
            .pictureAlt(UPDATED_PICTURE_ALT)
            .isLogo(UPDATED_IS_LOGO)
            .isDisplayed(UPDATED_IS_DISPLAYED);
        return picture;
    }

    @BeforeEach
    public void initTest() {
        picture = createEntity(em);
    }

    @Test
    @Transactional
    void createPicture() throws Exception {
        int databaseSizeBeforeCreate = pictureRepository.findAll().size();
        // Create the Picture
        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isCreated());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeCreate + 1);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getPictureName()).isEqualTo(DEFAULT_PICTURE_NAME);
        assertThat(testPicture.getPictureUrl()).isEqualTo(DEFAULT_PICTURE_URL);
        assertThat(testPicture.getPictureAlt()).isEqualTo(DEFAULT_PICTURE_ALT);
        assertThat(testPicture.getIsLogo()).isEqualTo(DEFAULT_IS_LOGO);
        assertThat(testPicture.getIsDisplayed()).isEqualTo(DEFAULT_IS_DISPLAYED);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(1)).save(testPicture);
    }

    @Test
    @Transactional
    void createPictureWithExistingId() throws Exception {
        // Create the Picture with an existing ID
        picture.setId(1L);

        int databaseSizeBeforeCreate = pictureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeCreate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }

    @Test
    @Transactional
    void checkPictureNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureRepository.findAll().size();
        // set the field null
        picture.setPictureName(null);

        // Create the Picture, which fails.

        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPictureUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureRepository.findAll().size();
        // set the field null
        picture.setPictureUrl(null);

        // Create the Picture, which fails.

        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPictureAltIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureRepository.findAll().size();
        // set the field null
        picture.setPictureAlt(null);

        // Create the Picture, which fails.

        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsLogoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureRepository.findAll().size();
        // set the field null
        picture.setIsLogo(null);

        // Create the Picture, which fails.

        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDisplayedIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureRepository.findAll().size();
        // set the field null
        picture.setIsDisplayed(null);

        // Create the Picture, which fails.

        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPictures() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        // Get all the pictureList
        restPictureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(picture.getId().intValue())))
            .andExpect(jsonPath("$.[*].pictureName").value(hasItem(DEFAULT_PICTURE_NAME)))
            .andExpect(jsonPath("$.[*].pictureUrl").value(hasItem(DEFAULT_PICTURE_URL)))
            .andExpect(jsonPath("$.[*].pictureAlt").value(hasItem(DEFAULT_PICTURE_ALT)))
            .andExpect(jsonPath("$.[*].isLogo").value(hasItem(DEFAULT_IS_LOGO.booleanValue())))
            .andExpect(jsonPath("$.[*].isDisplayed").value(hasItem(DEFAULT_IS_DISPLAYED.booleanValue())));
    }

    @Test
    @Transactional
    void getPicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        // Get the picture
        restPictureMockMvc
            .perform(get(ENTITY_API_URL_ID, picture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(picture.getId().intValue()))
            .andExpect(jsonPath("$.pictureName").value(DEFAULT_PICTURE_NAME))
            .andExpect(jsonPath("$.pictureUrl").value(DEFAULT_PICTURE_URL))
            .andExpect(jsonPath("$.pictureAlt").value(DEFAULT_PICTURE_ALT))
            .andExpect(jsonPath("$.isLogo").value(DEFAULT_IS_LOGO.booleanValue()))
            .andExpect(jsonPath("$.isDisplayed").value(DEFAULT_IS_DISPLAYED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPicture() throws Exception {
        // Get the picture
        restPictureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture
        Picture updatedPicture = pictureRepository.findById(picture.getId()).get();
        // Disconnect from session so that the updates on updatedPicture are not directly saved in db
        em.detach(updatedPicture);
        updatedPicture
            .pictureName(UPDATED_PICTURE_NAME)
            .pictureUrl(UPDATED_PICTURE_URL)
            .pictureAlt(UPDATED_PICTURE_ALT)
            .isLogo(UPDATED_IS_LOGO)
            .isDisplayed(UPDATED_IS_DISPLAYED);

        restPictureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPicture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPicture))
            )
            .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getPictureName()).isEqualTo(UPDATED_PICTURE_NAME);
        assertThat(testPicture.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testPicture.getPictureAlt()).isEqualTo(UPDATED_PICTURE_ALT);
        assertThat(testPicture.getIsLogo()).isEqualTo(UPDATED_IS_LOGO);
        assertThat(testPicture.getIsDisplayed()).isEqualTo(UPDATED_IS_DISPLAYED);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository).save(testPicture);
    }

    @Test
    @Transactional
    void putNonExistingPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, picture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(picture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }

    @Test
    @Transactional
    void putWithIdMismatchPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(picture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }

    @Test
    @Transactional
    void partialUpdatePictureWithPatch() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture using partial update
        Picture partialUpdatedPicture = new Picture();
        partialUpdatedPicture.setId(picture.getId());

        partialUpdatedPicture.isLogo(UPDATED_IS_LOGO);

        restPictureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPicture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPicture))
            )
            .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getPictureName()).isEqualTo(DEFAULT_PICTURE_NAME);
        assertThat(testPicture.getPictureUrl()).isEqualTo(DEFAULT_PICTURE_URL);
        assertThat(testPicture.getPictureAlt()).isEqualTo(DEFAULT_PICTURE_ALT);
        assertThat(testPicture.getIsLogo()).isEqualTo(UPDATED_IS_LOGO);
        assertThat(testPicture.getIsDisplayed()).isEqualTo(DEFAULT_IS_DISPLAYED);
    }

    @Test
    @Transactional
    void fullUpdatePictureWithPatch() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture using partial update
        Picture partialUpdatedPicture = new Picture();
        partialUpdatedPicture.setId(picture.getId());

        partialUpdatedPicture
            .pictureName(UPDATED_PICTURE_NAME)
            .pictureUrl(UPDATED_PICTURE_URL)
            .pictureAlt(UPDATED_PICTURE_ALT)
            .isLogo(UPDATED_IS_LOGO)
            .isDisplayed(UPDATED_IS_DISPLAYED);

        restPictureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPicture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPicture))
            )
            .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getPictureName()).isEqualTo(UPDATED_PICTURE_NAME);
        assertThat(testPicture.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testPicture.getPictureAlt()).isEqualTo(UPDATED_PICTURE_ALT);
        assertThat(testPicture.getIsLogo()).isEqualTo(UPDATED_IS_LOGO);
        assertThat(testPicture.getIsDisplayed()).isEqualTo(UPDATED_IS_DISPLAYED);
    }

    @Test
    @Transactional
    void patchNonExistingPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, picture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(picture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(picture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(0)).save(picture);
    }

    @Test
    @Transactional
    void deletePicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        int databaseSizeBeforeDelete = pictureRepository.findAll().size();

        // Delete the picture
        restPictureMockMvc
            .perform(delete(ENTITY_API_URL_ID, picture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Picture in Elasticsearch
        verify(mockPictureSearchRepository, times(1)).deleteById(picture.getId());
    }

    @Test
    @Transactional
    void searchPicture() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        pictureRepository.saveAndFlush(picture);
        when(mockPictureSearchRepository.search("id:" + picture.getId())).thenReturn(Stream.of(picture));

        // Search the picture
        restPictureMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + picture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(picture.getId().intValue())))
            .andExpect(jsonPath("$.[*].pictureName").value(hasItem(DEFAULT_PICTURE_NAME)))
            .andExpect(jsonPath("$.[*].pictureUrl").value(hasItem(DEFAULT_PICTURE_URL)))
            .andExpect(jsonPath("$.[*].pictureAlt").value(hasItem(DEFAULT_PICTURE_ALT)))
            .andExpect(jsonPath("$.[*].isLogo").value(hasItem(DEFAULT_IS_LOGO.booleanValue())))
            .andExpect(jsonPath("$.[*].isDisplayed").value(hasItem(DEFAULT_IS_DISPLAYED.booleanValue())));
    }
}
