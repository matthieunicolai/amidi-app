package com.amidi.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.amidi.myapp.IntegrationTest;
import com.amidi.myapp.domain.Dish;
import com.amidi.myapp.repository.DishRepository;
import com.amidi.myapp.repository.search.DishSearchRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DishResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DishResourceIT {

    private static final String DEFAULT_DISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISH_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DISH_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_DISH_PRICE = 1F;
    private static final Float UPDATED_DISH_PRICE = 2F;

    private static final Instant DEFAULT_DISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DISH_PICTURE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISH_PICTURE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISH_PICTURE_URL = "AAAAAAAAAA";
    private static final String UPDATED_DISH_PICTURE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DISH_PICTURE_ALT = "AAAAAAAAAA";
    private static final String UPDATED_DISH_PICTURE_ALT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DAILY_DISH = false;
    private static final Boolean UPDATED_IS_DAILY_DISH = true;

    private static final Boolean DEFAULT_IS_AVAILABLE = false;
    private static final Boolean UPDATED_IS_AVAILABLE = true;

    private static final String ENTITY_API_URL = "/api/dishes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/dishes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DishRepository dishRepository;

    /**
     * This repository is mocked in the com.amidi.myapp.repository.search test package.
     *
     * @see com.amidi.myapp.repository.search.DishSearchRepositoryMockConfiguration
     */
    @Autowired
    private DishSearchRepository mockDishSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDishMockMvc;

    private Dish dish;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dish createEntity(EntityManager em) {
        Dish dish = new Dish()
            .dishName(DEFAULT_DISH_NAME)
            .dishDescription(DEFAULT_DISH_DESCRIPTION)
            .dishPrice(DEFAULT_DISH_PRICE)
            .dishDate(DEFAULT_DISH_DATE)
            .dishPictureName(DEFAULT_DISH_PICTURE_NAME)
            .dishPictureUrl(DEFAULT_DISH_PICTURE_URL)
            .dishPictureAlt(DEFAULT_DISH_PICTURE_ALT)
            .isDailyDish(DEFAULT_IS_DAILY_DISH)
            .isAvailable(DEFAULT_IS_AVAILABLE);
        return dish;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dish createUpdatedEntity(EntityManager em) {
        Dish dish = new Dish()
            .dishName(UPDATED_DISH_NAME)
            .dishDescription(UPDATED_DISH_DESCRIPTION)
            .dishPrice(UPDATED_DISH_PRICE)
            .dishDate(UPDATED_DISH_DATE)
            .dishPictureName(UPDATED_DISH_PICTURE_NAME)
            .dishPictureUrl(UPDATED_DISH_PICTURE_URL)
            .dishPictureAlt(UPDATED_DISH_PICTURE_ALT)
            .isDailyDish(UPDATED_IS_DAILY_DISH)
            .isAvailable(UPDATED_IS_AVAILABLE);
        return dish;
    }

    @BeforeEach
    public void initTest() {
        dish = createEntity(em);
    }

    @Test
    @Transactional
    void createDish() throws Exception {
        int databaseSizeBeforeCreate = dishRepository.findAll().size();
        // Create the Dish
        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isCreated());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeCreate + 1);
        Dish testDish = dishList.get(dishList.size() - 1);
        assertThat(testDish.getDishName()).isEqualTo(DEFAULT_DISH_NAME);
        assertThat(testDish.getDishDescription()).isEqualTo(DEFAULT_DISH_DESCRIPTION);
        assertThat(testDish.getDishPrice()).isEqualTo(DEFAULT_DISH_PRICE);
        assertThat(testDish.getDishDate()).isEqualTo(DEFAULT_DISH_DATE);
        assertThat(testDish.getDishPictureName()).isEqualTo(DEFAULT_DISH_PICTURE_NAME);
        assertThat(testDish.getDishPictureUrl()).isEqualTo(DEFAULT_DISH_PICTURE_URL);
        assertThat(testDish.getDishPictureAlt()).isEqualTo(DEFAULT_DISH_PICTURE_ALT);
        assertThat(testDish.getIsDailyDish()).isEqualTo(DEFAULT_IS_DAILY_DISH);
        assertThat(testDish.getIsAvailable()).isEqualTo(DEFAULT_IS_AVAILABLE);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(1)).save(testDish);
    }

    @Test
    @Transactional
    void createDishWithExistingId() throws Exception {
        // Create the Dish with an existing ID
        dish.setId(1L);

        int databaseSizeBeforeCreate = dishRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(0)).save(dish);
    }

    @Test
    @Transactional
    void checkDishNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setDishName(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDishPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setDishPrice(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setDishDate(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDishPictureNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setDishPictureName(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDishPictureUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setDishPictureUrl(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDailyDishIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setIsDailyDish(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setIsAvailable(null);

        // Create the Dish, which fails.

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDishes() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList
        restDishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dish.getId().intValue())))
            .andExpect(jsonPath("$.[*].dishName").value(hasItem(DEFAULT_DISH_NAME)))
            .andExpect(jsonPath("$.[*].dishDescription").value(hasItem(DEFAULT_DISH_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dishPrice").value(hasItem(DEFAULT_DISH_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].dishDate").value(hasItem(DEFAULT_DISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].dishPictureName").value(hasItem(DEFAULT_DISH_PICTURE_NAME)))
            .andExpect(jsonPath("$.[*].dishPictureUrl").value(hasItem(DEFAULT_DISH_PICTURE_URL)))
            .andExpect(jsonPath("$.[*].dishPictureAlt").value(hasItem(DEFAULT_DISH_PICTURE_ALT)))
            .andExpect(jsonPath("$.[*].isDailyDish").value(hasItem(DEFAULT_IS_DAILY_DISH.booleanValue())))
            .andExpect(jsonPath("$.[*].isAvailable").value(hasItem(DEFAULT_IS_AVAILABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getDish() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get the dish
        restDishMockMvc
            .perform(get(ENTITY_API_URL_ID, dish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dish.getId().intValue()))
            .andExpect(jsonPath("$.dishName").value(DEFAULT_DISH_NAME))
            .andExpect(jsonPath("$.dishDescription").value(DEFAULT_DISH_DESCRIPTION))
            .andExpect(jsonPath("$.dishPrice").value(DEFAULT_DISH_PRICE.doubleValue()))
            .andExpect(jsonPath("$.dishDate").value(DEFAULT_DISH_DATE.toString()))
            .andExpect(jsonPath("$.dishPictureName").value(DEFAULT_DISH_PICTURE_NAME))
            .andExpect(jsonPath("$.dishPictureUrl").value(DEFAULT_DISH_PICTURE_URL))
            .andExpect(jsonPath("$.dishPictureAlt").value(DEFAULT_DISH_PICTURE_ALT))
            .andExpect(jsonPath("$.isDailyDish").value(DEFAULT_IS_DAILY_DISH.booleanValue()))
            .andExpect(jsonPath("$.isAvailable").value(DEFAULT_IS_AVAILABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDish() throws Exception {
        // Get the dish
        restDishMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDish() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        int databaseSizeBeforeUpdate = dishRepository.findAll().size();

        // Update the dish
        Dish updatedDish = dishRepository.findById(dish.getId()).get();
        // Disconnect from session so that the updates on updatedDish are not directly saved in db
        em.detach(updatedDish);
        updatedDish
            .dishName(UPDATED_DISH_NAME)
            .dishDescription(UPDATED_DISH_DESCRIPTION)
            .dishPrice(UPDATED_DISH_PRICE)
            .dishDate(UPDATED_DISH_DATE)
            .dishPictureName(UPDATED_DISH_PICTURE_NAME)
            .dishPictureUrl(UPDATED_DISH_PICTURE_URL)
            .dishPictureAlt(UPDATED_DISH_PICTURE_ALT)
            .isDailyDish(UPDATED_IS_DAILY_DISH)
            .isAvailable(UPDATED_IS_AVAILABLE);

        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
        Dish testDish = dishList.get(dishList.size() - 1);
        assertThat(testDish.getDishName()).isEqualTo(UPDATED_DISH_NAME);
        assertThat(testDish.getDishDescription()).isEqualTo(UPDATED_DISH_DESCRIPTION);
        assertThat(testDish.getDishPrice()).isEqualTo(UPDATED_DISH_PRICE);
        assertThat(testDish.getDishDate()).isEqualTo(UPDATED_DISH_DATE);
        assertThat(testDish.getDishPictureName()).isEqualTo(UPDATED_DISH_PICTURE_NAME);
        assertThat(testDish.getDishPictureUrl()).isEqualTo(UPDATED_DISH_PICTURE_URL);
        assertThat(testDish.getDishPictureAlt()).isEqualTo(UPDATED_DISH_PICTURE_ALT);
        assertThat(testDish.getIsDailyDish()).isEqualTo(UPDATED_IS_DAILY_DISH);
        assertThat(testDish.getIsAvailable()).isEqualTo(UPDATED_IS_AVAILABLE);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository).save(testDish);
    }

    @Test
    @Transactional
    void putNonExistingDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(0)).save(dish);
    }

    @Test
    @Transactional
    void putWithIdMismatchDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(0)).save(dish);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(0)).save(dish);
    }

    @Test
    @Transactional
    void partialUpdateDishWithPatch() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        int databaseSizeBeforeUpdate = dishRepository.findAll().size();

        // Update the dish using partial update
        Dish partialUpdatedDish = new Dish();
        partialUpdatedDish.setId(dish.getId());

        partialUpdatedDish
            .dishPrice(UPDATED_DISH_PRICE)
            .dishDate(UPDATED_DISH_DATE)
            .dishPictureName(UPDATED_DISH_PICTURE_NAME)
            .isDailyDish(UPDATED_IS_DAILY_DISH);

        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
        Dish testDish = dishList.get(dishList.size() - 1);
        assertThat(testDish.getDishName()).isEqualTo(DEFAULT_DISH_NAME);
        assertThat(testDish.getDishDescription()).isEqualTo(DEFAULT_DISH_DESCRIPTION);
        assertThat(testDish.getDishPrice()).isEqualTo(UPDATED_DISH_PRICE);
        assertThat(testDish.getDishDate()).isEqualTo(UPDATED_DISH_DATE);
        assertThat(testDish.getDishPictureName()).isEqualTo(UPDATED_DISH_PICTURE_NAME);
        assertThat(testDish.getDishPictureUrl()).isEqualTo(DEFAULT_DISH_PICTURE_URL);
        assertThat(testDish.getDishPictureAlt()).isEqualTo(DEFAULT_DISH_PICTURE_ALT);
        assertThat(testDish.getIsDailyDish()).isEqualTo(UPDATED_IS_DAILY_DISH);
        assertThat(testDish.getIsAvailable()).isEqualTo(DEFAULT_IS_AVAILABLE);
    }

    @Test
    @Transactional
    void fullUpdateDishWithPatch() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        int databaseSizeBeforeUpdate = dishRepository.findAll().size();

        // Update the dish using partial update
        Dish partialUpdatedDish = new Dish();
        partialUpdatedDish.setId(dish.getId());

        partialUpdatedDish
            .dishName(UPDATED_DISH_NAME)
            .dishDescription(UPDATED_DISH_DESCRIPTION)
            .dishPrice(UPDATED_DISH_PRICE)
            .dishDate(UPDATED_DISH_DATE)
            .dishPictureName(UPDATED_DISH_PICTURE_NAME)
            .dishPictureUrl(UPDATED_DISH_PICTURE_URL)
            .dishPictureAlt(UPDATED_DISH_PICTURE_ALT)
            .isDailyDish(UPDATED_IS_DAILY_DISH)
            .isAvailable(UPDATED_IS_AVAILABLE);

        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
        Dish testDish = dishList.get(dishList.size() - 1);
        assertThat(testDish.getDishName()).isEqualTo(UPDATED_DISH_NAME);
        assertThat(testDish.getDishDescription()).isEqualTo(UPDATED_DISH_DESCRIPTION);
        assertThat(testDish.getDishPrice()).isEqualTo(UPDATED_DISH_PRICE);
        assertThat(testDish.getDishDate()).isEqualTo(UPDATED_DISH_DATE);
        assertThat(testDish.getDishPictureName()).isEqualTo(UPDATED_DISH_PICTURE_NAME);
        assertThat(testDish.getDishPictureUrl()).isEqualTo(UPDATED_DISH_PICTURE_URL);
        assertThat(testDish.getDishPictureAlt()).isEqualTo(UPDATED_DISH_PICTURE_ALT);
        assertThat(testDish.getIsDailyDish()).isEqualTo(UPDATED_IS_DAILY_DISH);
        assertThat(testDish.getIsAvailable()).isEqualTo(UPDATED_IS_AVAILABLE);
    }

    @Test
    @Transactional
    void patchNonExistingDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(0)).save(dish);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(0)).save(dish);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(0)).save(dish);
    }

    @Test
    @Transactional
    void deleteDish() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        int databaseSizeBeforeDelete = dishRepository.findAll().size();

        // Delete the dish
        restDishMockMvc
            .perform(delete(ENTITY_API_URL_ID, dish.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dish in Elasticsearch
        verify(mockDishSearchRepository, times(1)).deleteById(dish.getId());
    }

    @Test
    @Transactional
    void searchDish() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dishRepository.saveAndFlush(dish);
        when(mockDishSearchRepository.search("id:" + dish.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dish), PageRequest.of(0, 1), 1));

        // Search the dish
        restDishMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + dish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dish.getId().intValue())))
            .andExpect(jsonPath("$.[*].dishName").value(hasItem(DEFAULT_DISH_NAME)))
            .andExpect(jsonPath("$.[*].dishDescription").value(hasItem(DEFAULT_DISH_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dishPrice").value(hasItem(DEFAULT_DISH_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].dishDate").value(hasItem(DEFAULT_DISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].dishPictureName").value(hasItem(DEFAULT_DISH_PICTURE_NAME)))
            .andExpect(jsonPath("$.[*].dishPictureUrl").value(hasItem(DEFAULT_DISH_PICTURE_URL)))
            .andExpect(jsonPath("$.[*].dishPictureAlt").value(hasItem(DEFAULT_DISH_PICTURE_ALT)))
            .andExpect(jsonPath("$.[*].isDailyDish").value(hasItem(DEFAULT_IS_DAILY_DISH.booleanValue())))
            .andExpect(jsonPath("$.[*].isAvailable").value(hasItem(DEFAULT_IS_AVAILABLE.booleanValue())));
    }
}
