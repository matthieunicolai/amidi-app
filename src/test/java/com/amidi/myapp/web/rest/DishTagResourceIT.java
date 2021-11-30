package com.amidi.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.amidi.myapp.IntegrationTest;
import com.amidi.myapp.domain.DishTag;
import com.amidi.myapp.domain.enumeration.FoodType;
import com.amidi.myapp.repository.DishTagRepository;
import com.amidi.myapp.repository.search.DishTagSearchRepository;
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
 * Integration tests for the {@link DishTagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DishTagResourceIT {

    private static final FoodType DEFAULT_DISH_TAG = FoodType.VEGAN;
    private static final FoodType UPDATED_DISH_TAG = FoodType.VEGETARIEN;

    private static final String ENTITY_API_URL = "/api/dish-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/dish-tags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DishTagRepository dishTagRepository;

    /**
     * This repository is mocked in the com.amidi.myapp.repository.search test package.
     *
     * @see com.amidi.myapp.repository.search.DishTagSearchRepositoryMockConfiguration
     */
    @Autowired
    private DishTagSearchRepository mockDishTagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDishTagMockMvc;

    private DishTag dishTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DishTag createEntity(EntityManager em) {
        DishTag dishTag = new DishTag().dishTag(DEFAULT_DISH_TAG);
        return dishTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DishTag createUpdatedEntity(EntityManager em) {
        DishTag dishTag = new DishTag().dishTag(UPDATED_DISH_TAG);
        return dishTag;
    }

    @BeforeEach
    public void initTest() {
        dishTag = createEntity(em);
    }

    @Test
    @Transactional
    void createDishTag() throws Exception {
        int databaseSizeBeforeCreate = dishTagRepository.findAll().size();
        // Create the DishTag
        restDishTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishTag)))
            .andExpect(status().isCreated());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeCreate + 1);
        DishTag testDishTag = dishTagList.get(dishTagList.size() - 1);
        assertThat(testDishTag.getDishTag()).isEqualTo(DEFAULT_DISH_TAG);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(1)).save(testDishTag);
    }

    @Test
    @Transactional
    void createDishTagWithExistingId() throws Exception {
        // Create the DishTag with an existing ID
        dishTag.setId(1L);

        int databaseSizeBeforeCreate = dishTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDishTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishTag)))
            .andExpect(status().isBadRequest());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeCreate);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(0)).save(dishTag);
    }

    @Test
    @Transactional
    void checkDishTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishTagRepository.findAll().size();
        // set the field null
        dishTag.setDishTag(null);

        // Create the DishTag, which fails.

        restDishTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishTag)))
            .andExpect(status().isBadRequest());

        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDishTags() throws Exception {
        // Initialize the database
        dishTagRepository.saveAndFlush(dishTag);

        // Get all the dishTagList
        restDishTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dishTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].dishTag").value(hasItem(DEFAULT_DISH_TAG.toString())));
    }

    @Test
    @Transactional
    void getDishTag() throws Exception {
        // Initialize the database
        dishTagRepository.saveAndFlush(dishTag);

        // Get the dishTag
        restDishTagMockMvc
            .perform(get(ENTITY_API_URL_ID, dishTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dishTag.getId().intValue()))
            .andExpect(jsonPath("$.dishTag").value(DEFAULT_DISH_TAG.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDishTag() throws Exception {
        // Get the dishTag
        restDishTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDishTag() throws Exception {
        // Initialize the database
        dishTagRepository.saveAndFlush(dishTag);

        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();

        // Update the dishTag
        DishTag updatedDishTag = dishTagRepository.findById(dishTag.getId()).get();
        // Disconnect from session so that the updates on updatedDishTag are not directly saved in db
        em.detach(updatedDishTag);
        updatedDishTag.dishTag(UPDATED_DISH_TAG);

        restDishTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDishTag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDishTag))
            )
            .andExpect(status().isOk());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);
        DishTag testDishTag = dishTagList.get(dishTagList.size() - 1);
        assertThat(testDishTag.getDishTag()).isEqualTo(UPDATED_DISH_TAG);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository).save(testDishTag);
    }

    @Test
    @Transactional
    void putNonExistingDishTag() throws Exception {
        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();
        dishTag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dishTag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dishTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(0)).save(dishTag);
    }

    @Test
    @Transactional
    void putWithIdMismatchDishTag() throws Exception {
        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();
        dishTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dishTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(0)).save(dishTag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDishTag() throws Exception {
        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();
        dishTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishTag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(0)).save(dishTag);
    }

    @Test
    @Transactional
    void partialUpdateDishTagWithPatch() throws Exception {
        // Initialize the database
        dishTagRepository.saveAndFlush(dishTag);

        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();

        // Update the dishTag using partial update
        DishTag partialUpdatedDishTag = new DishTag();
        partialUpdatedDishTag.setId(dishTag.getId());

        partialUpdatedDishTag.dishTag(UPDATED_DISH_TAG);

        restDishTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDishTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDishTag))
            )
            .andExpect(status().isOk());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);
        DishTag testDishTag = dishTagList.get(dishTagList.size() - 1);
        assertThat(testDishTag.getDishTag()).isEqualTo(UPDATED_DISH_TAG);
    }

    @Test
    @Transactional
    void fullUpdateDishTagWithPatch() throws Exception {
        // Initialize the database
        dishTagRepository.saveAndFlush(dishTag);

        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();

        // Update the dishTag using partial update
        DishTag partialUpdatedDishTag = new DishTag();
        partialUpdatedDishTag.setId(dishTag.getId());

        partialUpdatedDishTag.dishTag(UPDATED_DISH_TAG);

        restDishTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDishTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDishTag))
            )
            .andExpect(status().isOk());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);
        DishTag testDishTag = dishTagList.get(dishTagList.size() - 1);
        assertThat(testDishTag.getDishTag()).isEqualTo(UPDATED_DISH_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingDishTag() throws Exception {
        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();
        dishTag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dishTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dishTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(0)).save(dishTag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDishTag() throws Exception {
        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();
        dishTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dishTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(0)).save(dishTag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDishTag() throws Exception {
        int databaseSizeBeforeUpdate = dishTagRepository.findAll().size();
        dishTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dishTag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DishTag in the database
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(0)).save(dishTag);
    }

    @Test
    @Transactional
    void deleteDishTag() throws Exception {
        // Initialize the database
        dishTagRepository.saveAndFlush(dishTag);

        int databaseSizeBeforeDelete = dishTagRepository.findAll().size();

        // Delete the dishTag
        restDishTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, dishTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DishTag> dishTagList = dishTagRepository.findAll();
        assertThat(dishTagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DishTag in Elasticsearch
        verify(mockDishTagSearchRepository, times(1)).deleteById(dishTag.getId());
    }

    @Test
    @Transactional
    void searchDishTag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dishTagRepository.saveAndFlush(dishTag);
        when(mockDishTagSearchRepository.search("id:" + dishTag.getId())).thenReturn(Stream.of(dishTag));

        // Search the dishTag
        restDishTagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + dishTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dishTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].dishTag").value(hasItem(DEFAULT_DISH_TAG.toString())));
    }
}
