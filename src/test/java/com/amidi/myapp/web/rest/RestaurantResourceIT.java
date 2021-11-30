package com.amidi.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.amidi.myapp.IntegrationTest;
import com.amidi.myapp.domain.Restaurant;
import com.amidi.myapp.domain.enumeration.RestaurationType;
import com.amidi.myapp.domain.enumeration.SubscriptionType;
import com.amidi.myapp.repository.RestaurantRepository;
import com.amidi.myapp.repository.search.RestaurantSearchRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link RestaurantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RestaurantResourceIT {

    private static final String DEFAULT_RESTAURANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURANT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURANT_ADDRESS_CMP = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_ADDRESS_CMP = "BBBBBBBBBB";

    private static final RestaurationType DEFAULT_RESTAURANT_TYPE = RestaurationType.BRASSERIE;
    private static final RestaurationType UPDATED_RESTAURANT_TYPE = RestaurationType.FASTFOOD;

    private static final Boolean DEFAULT_IS_SUB = false;
    private static final Boolean UPDATED_IS_SUB = true;

    private static final SubscriptionType DEFAULT_RESTAURANT_SUBSCRIPTION = SubscriptionType.SILVER;
    private static final SubscriptionType UPDATED_RESTAURANT_SUBSCRIPTION = SubscriptionType.GOLD;

    private static final Instant DEFAULT_RESTAURANT_SUB_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESTAURANT_SUB_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RESTAURANT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURANT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURANT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURANT_WEB_SITE = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_WEB_SITE = "BBBBBBBBBB";

    private static final Float DEFAULT_RESTAURANT_LATITUDE = 1F;
    private static final Float UPDATED_RESTAURANT_LATITUDE = 2F;

    private static final Float DEFAULT_RESTAURANT_LONGITUDE = 1F;
    private static final Float UPDATED_RESTAURANT_LONGITUDE = 2F;

    private static final Boolean DEFAULT_IS_ACTIVATED = false;
    private static final Boolean UPDATED_IS_ACTIVATED = true;

    private static final String ENTITY_API_URL = "/api/restaurants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/restaurants";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * This repository is mocked in the com.amidi.myapp.repository.search test package.
     *
     * @see com.amidi.myapp.repository.search.RestaurantSearchRepositoryMockConfiguration
     */
    @Autowired
    private RestaurantSearchRepository mockRestaurantSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantMockMvc;

    private Restaurant restaurant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant()
            .restaurantName(DEFAULT_RESTAURANT_NAME)
            .restaurantAddress(DEFAULT_RESTAURANT_ADDRESS)
            .restaurantAddressCmp(DEFAULT_RESTAURANT_ADDRESS_CMP)
            .restaurantType(DEFAULT_RESTAURANT_TYPE)
            .isSub(DEFAULT_IS_SUB)
            .restaurantSubscription(DEFAULT_RESTAURANT_SUBSCRIPTION)
            .restaurantSubDate(DEFAULT_RESTAURANT_SUB_DATE)
            .restaurantDescription(DEFAULT_RESTAURANT_DESCRIPTION)
            .restaurantPhoneNumber(DEFAULT_RESTAURANT_PHONE_NUMBER)
            .restaurantEmail(DEFAULT_RESTAURANT_EMAIL)
            .restaurantWebSite(DEFAULT_RESTAURANT_WEB_SITE)
            .restaurantLatitude(DEFAULT_RESTAURANT_LATITUDE)
            .restaurantLongitude(DEFAULT_RESTAURANT_LONGITUDE)
            .isActivated(DEFAULT_IS_ACTIVATED);
        return restaurant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createUpdatedEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant()
            .restaurantName(UPDATED_RESTAURANT_NAME)
            .restaurantAddress(UPDATED_RESTAURANT_ADDRESS)
            .restaurantAddressCmp(UPDATED_RESTAURANT_ADDRESS_CMP)
            .restaurantType(UPDATED_RESTAURANT_TYPE)
            .isSub(UPDATED_IS_SUB)
            .restaurantSubscription(UPDATED_RESTAURANT_SUBSCRIPTION)
            .restaurantSubDate(UPDATED_RESTAURANT_SUB_DATE)
            .restaurantDescription(UPDATED_RESTAURANT_DESCRIPTION)
            .restaurantPhoneNumber(UPDATED_RESTAURANT_PHONE_NUMBER)
            .restaurantEmail(UPDATED_RESTAURANT_EMAIL)
            .restaurantWebSite(UPDATED_RESTAURANT_WEB_SITE)
            .restaurantLatitude(UPDATED_RESTAURANT_LATITUDE)
            .restaurantLongitude(UPDATED_RESTAURANT_LONGITUDE)
            .isActivated(UPDATED_IS_ACTIVATED);
        return restaurant;
    }

    @BeforeEach
    public void initTest() {
        restaurant = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurant() throws Exception {
        int databaseSizeBeforeCreate = restaurantRepository.findAll().size();
        // Create the Restaurant
        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isCreated());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getRestaurantName()).isEqualTo(DEFAULT_RESTAURANT_NAME);
        assertThat(testRestaurant.getRestaurantAddress()).isEqualTo(DEFAULT_RESTAURANT_ADDRESS);
        assertThat(testRestaurant.getRestaurantAddressCmp()).isEqualTo(DEFAULT_RESTAURANT_ADDRESS_CMP);
        assertThat(testRestaurant.getRestaurantType()).isEqualTo(DEFAULT_RESTAURANT_TYPE);
        assertThat(testRestaurant.getIsSub()).isEqualTo(DEFAULT_IS_SUB);
        assertThat(testRestaurant.getRestaurantSubscription()).isEqualTo(DEFAULT_RESTAURANT_SUBSCRIPTION);
        assertThat(testRestaurant.getRestaurantSubDate()).isEqualTo(DEFAULT_RESTAURANT_SUB_DATE);
        assertThat(testRestaurant.getRestaurantDescription()).isEqualTo(DEFAULT_RESTAURANT_DESCRIPTION);
        assertThat(testRestaurant.getRestaurantPhoneNumber()).isEqualTo(DEFAULT_RESTAURANT_PHONE_NUMBER);
        assertThat(testRestaurant.getRestaurantEmail()).isEqualTo(DEFAULT_RESTAURANT_EMAIL);
        assertThat(testRestaurant.getRestaurantWebSite()).isEqualTo(DEFAULT_RESTAURANT_WEB_SITE);
        assertThat(testRestaurant.getRestaurantLatitude()).isEqualTo(DEFAULT_RESTAURANT_LATITUDE);
        assertThat(testRestaurant.getRestaurantLongitude()).isEqualTo(DEFAULT_RESTAURANT_LONGITUDE);
        assertThat(testRestaurant.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(1)).save(testRestaurant);
    }

    @Test
    @Transactional
    void createRestaurantWithExistingId() throws Exception {
        // Create the Restaurant with an existing ID
        restaurant.setId(1L);

        int databaseSizeBeforeCreate = restaurantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant);
    }

    @Test
    @Transactional
    void checkRestaurantNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setRestaurantName(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRestaurantAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setRestaurantAddress(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRestaurantAddressCmpIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setRestaurantAddressCmp(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSubIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setIsSub(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRestaurantPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setRestaurantPhoneNumber(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRestaurantEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setRestaurantEmail(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRestaurantLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setRestaurantLatitude(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRestaurantLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setRestaurantLongitude(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setIsActivated(null);

        // Create the Restaurant, which fails.

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRestaurants() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.getId().intValue())))
            .andExpect(jsonPath("$.[*].restaurantName").value(hasItem(DEFAULT_RESTAURANT_NAME)))
            .andExpect(jsonPath("$.[*].restaurantAddress").value(hasItem(DEFAULT_RESTAURANT_ADDRESS)))
            .andExpect(jsonPath("$.[*].restaurantAddressCmp").value(hasItem(DEFAULT_RESTAURANT_ADDRESS_CMP)))
            .andExpect(jsonPath("$.[*].restaurantType").value(hasItem(DEFAULT_RESTAURANT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isSub").value(hasItem(DEFAULT_IS_SUB.booleanValue())))
            .andExpect(jsonPath("$.[*].restaurantSubscription").value(hasItem(DEFAULT_RESTAURANT_SUBSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].restaurantSubDate").value(hasItem(DEFAULT_RESTAURANT_SUB_DATE.toString())))
            .andExpect(jsonPath("$.[*].restaurantDescription").value(hasItem(DEFAULT_RESTAURANT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].restaurantPhoneNumber").value(hasItem(DEFAULT_RESTAURANT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].restaurantEmail").value(hasItem(DEFAULT_RESTAURANT_EMAIL)))
            .andExpect(jsonPath("$.[*].restaurantWebSite").value(hasItem(DEFAULT_RESTAURANT_WEB_SITE)))
            .andExpect(jsonPath("$.[*].restaurantLatitude").value(hasItem(DEFAULT_RESTAURANT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].restaurantLongitude").value(hasItem(DEFAULT_RESTAURANT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    void getRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get the restaurant
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurant.getId().intValue()))
            .andExpect(jsonPath("$.restaurantName").value(DEFAULT_RESTAURANT_NAME))
            .andExpect(jsonPath("$.restaurantAddress").value(DEFAULT_RESTAURANT_ADDRESS))
            .andExpect(jsonPath("$.restaurantAddressCmp").value(DEFAULT_RESTAURANT_ADDRESS_CMP))
            .andExpect(jsonPath("$.restaurantType").value(DEFAULT_RESTAURANT_TYPE.toString()))
            .andExpect(jsonPath("$.isSub").value(DEFAULT_IS_SUB.booleanValue()))
            .andExpect(jsonPath("$.restaurantSubscription").value(DEFAULT_RESTAURANT_SUBSCRIPTION.toString()))
            .andExpect(jsonPath("$.restaurantSubDate").value(DEFAULT_RESTAURANT_SUB_DATE.toString()))
            .andExpect(jsonPath("$.restaurantDescription").value(DEFAULT_RESTAURANT_DESCRIPTION))
            .andExpect(jsonPath("$.restaurantPhoneNumber").value(DEFAULT_RESTAURANT_PHONE_NUMBER))
            .andExpect(jsonPath("$.restaurantEmail").value(DEFAULT_RESTAURANT_EMAIL))
            .andExpect(jsonPath("$.restaurantWebSite").value(DEFAULT_RESTAURANT_WEB_SITE))
            .andExpect(jsonPath("$.restaurantLatitude").value(DEFAULT_RESTAURANT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.restaurantLongitude").value(DEFAULT_RESTAURANT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.isActivated").value(DEFAULT_IS_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingRestaurant() throws Exception {
        // Get the restaurant
        restRestaurantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Update the restaurant
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurant are not directly saved in db
        em.detach(updatedRestaurant);
        updatedRestaurant
            .restaurantName(UPDATED_RESTAURANT_NAME)
            .restaurantAddress(UPDATED_RESTAURANT_ADDRESS)
            .restaurantAddressCmp(UPDATED_RESTAURANT_ADDRESS_CMP)
            .restaurantType(UPDATED_RESTAURANT_TYPE)
            .isSub(UPDATED_IS_SUB)
            .restaurantSubscription(UPDATED_RESTAURANT_SUBSCRIPTION)
            .restaurantSubDate(UPDATED_RESTAURANT_SUB_DATE)
            .restaurantDescription(UPDATED_RESTAURANT_DESCRIPTION)
            .restaurantPhoneNumber(UPDATED_RESTAURANT_PHONE_NUMBER)
            .restaurantEmail(UPDATED_RESTAURANT_EMAIL)
            .restaurantWebSite(UPDATED_RESTAURANT_WEB_SITE)
            .restaurantLatitude(UPDATED_RESTAURANT_LATITUDE)
            .restaurantLongitude(UPDATED_RESTAURANT_LONGITUDE)
            .isActivated(UPDATED_IS_ACTIVATED);

        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRestaurant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRestaurant))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getRestaurantName()).isEqualTo(UPDATED_RESTAURANT_NAME);
        assertThat(testRestaurant.getRestaurantAddress()).isEqualTo(UPDATED_RESTAURANT_ADDRESS);
        assertThat(testRestaurant.getRestaurantAddressCmp()).isEqualTo(UPDATED_RESTAURANT_ADDRESS_CMP);
        assertThat(testRestaurant.getRestaurantType()).isEqualTo(UPDATED_RESTAURANT_TYPE);
        assertThat(testRestaurant.getIsSub()).isEqualTo(UPDATED_IS_SUB);
        assertThat(testRestaurant.getRestaurantSubscription()).isEqualTo(UPDATED_RESTAURANT_SUBSCRIPTION);
        assertThat(testRestaurant.getRestaurantSubDate()).isEqualTo(UPDATED_RESTAURANT_SUB_DATE);
        assertThat(testRestaurant.getRestaurantDescription()).isEqualTo(UPDATED_RESTAURANT_DESCRIPTION);
        assertThat(testRestaurant.getRestaurantPhoneNumber()).isEqualTo(UPDATED_RESTAURANT_PHONE_NUMBER);
        assertThat(testRestaurant.getRestaurantEmail()).isEqualTo(UPDATED_RESTAURANT_EMAIL);
        assertThat(testRestaurant.getRestaurantWebSite()).isEqualTo(UPDATED_RESTAURANT_WEB_SITE);
        assertThat(testRestaurant.getRestaurantLatitude()).isEqualTo(UPDATED_RESTAURANT_LATITUDE);
        assertThat(testRestaurant.getRestaurantLongitude()).isEqualTo(UPDATED_RESTAURANT_LONGITUDE);
        assertThat(testRestaurant.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository).save(testRestaurant);
    }

    @Test
    @Transactional
    void putNonExistingRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        partialUpdatedRestaurant
            .restaurantSubscription(UPDATED_RESTAURANT_SUBSCRIPTION)
            .restaurantSubDate(UPDATED_RESTAURANT_SUB_DATE)
            .restaurantPhoneNumber(UPDATED_RESTAURANT_PHONE_NUMBER)
            .restaurantEmail(UPDATED_RESTAURANT_EMAIL)
            .restaurantWebSite(UPDATED_RESTAURANT_WEB_SITE)
            .restaurantLatitude(UPDATED_RESTAURANT_LATITUDE)
            .restaurantLongitude(UPDATED_RESTAURANT_LONGITUDE);

        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurant))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getRestaurantName()).isEqualTo(DEFAULT_RESTAURANT_NAME);
        assertThat(testRestaurant.getRestaurantAddress()).isEqualTo(DEFAULT_RESTAURANT_ADDRESS);
        assertThat(testRestaurant.getRestaurantAddressCmp()).isEqualTo(DEFAULT_RESTAURANT_ADDRESS_CMP);
        assertThat(testRestaurant.getRestaurantType()).isEqualTo(DEFAULT_RESTAURANT_TYPE);
        assertThat(testRestaurant.getIsSub()).isEqualTo(DEFAULT_IS_SUB);
        assertThat(testRestaurant.getRestaurantSubscription()).isEqualTo(UPDATED_RESTAURANT_SUBSCRIPTION);
        assertThat(testRestaurant.getRestaurantSubDate()).isEqualTo(UPDATED_RESTAURANT_SUB_DATE);
        assertThat(testRestaurant.getRestaurantDescription()).isEqualTo(DEFAULT_RESTAURANT_DESCRIPTION);
        assertThat(testRestaurant.getRestaurantPhoneNumber()).isEqualTo(UPDATED_RESTAURANT_PHONE_NUMBER);
        assertThat(testRestaurant.getRestaurantEmail()).isEqualTo(UPDATED_RESTAURANT_EMAIL);
        assertThat(testRestaurant.getRestaurantWebSite()).isEqualTo(UPDATED_RESTAURANT_WEB_SITE);
        assertThat(testRestaurant.getRestaurantLatitude()).isEqualTo(UPDATED_RESTAURANT_LATITUDE);
        assertThat(testRestaurant.getRestaurantLongitude()).isEqualTo(UPDATED_RESTAURANT_LONGITUDE);
        assertThat(testRestaurant.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        partialUpdatedRestaurant
            .restaurantName(UPDATED_RESTAURANT_NAME)
            .restaurantAddress(UPDATED_RESTAURANT_ADDRESS)
            .restaurantAddressCmp(UPDATED_RESTAURANT_ADDRESS_CMP)
            .restaurantType(UPDATED_RESTAURANT_TYPE)
            .isSub(UPDATED_IS_SUB)
            .restaurantSubscription(UPDATED_RESTAURANT_SUBSCRIPTION)
            .restaurantSubDate(UPDATED_RESTAURANT_SUB_DATE)
            .restaurantDescription(UPDATED_RESTAURANT_DESCRIPTION)
            .restaurantPhoneNumber(UPDATED_RESTAURANT_PHONE_NUMBER)
            .restaurantEmail(UPDATED_RESTAURANT_EMAIL)
            .restaurantWebSite(UPDATED_RESTAURANT_WEB_SITE)
            .restaurantLatitude(UPDATED_RESTAURANT_LATITUDE)
            .restaurantLongitude(UPDATED_RESTAURANT_LONGITUDE)
            .isActivated(UPDATED_IS_ACTIVATED);

        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurant))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getRestaurantName()).isEqualTo(UPDATED_RESTAURANT_NAME);
        assertThat(testRestaurant.getRestaurantAddress()).isEqualTo(UPDATED_RESTAURANT_ADDRESS);
        assertThat(testRestaurant.getRestaurantAddressCmp()).isEqualTo(UPDATED_RESTAURANT_ADDRESS_CMP);
        assertThat(testRestaurant.getRestaurantType()).isEqualTo(UPDATED_RESTAURANT_TYPE);
        assertThat(testRestaurant.getIsSub()).isEqualTo(UPDATED_IS_SUB);
        assertThat(testRestaurant.getRestaurantSubscription()).isEqualTo(UPDATED_RESTAURANT_SUBSCRIPTION);
        assertThat(testRestaurant.getRestaurantSubDate()).isEqualTo(UPDATED_RESTAURANT_SUB_DATE);
        assertThat(testRestaurant.getRestaurantDescription()).isEqualTo(UPDATED_RESTAURANT_DESCRIPTION);
        assertThat(testRestaurant.getRestaurantPhoneNumber()).isEqualTo(UPDATED_RESTAURANT_PHONE_NUMBER);
        assertThat(testRestaurant.getRestaurantEmail()).isEqualTo(UPDATED_RESTAURANT_EMAIL);
        assertThat(testRestaurant.getRestaurantWebSite()).isEqualTo(UPDATED_RESTAURANT_WEB_SITE);
        assertThat(testRestaurant.getRestaurantLatitude()).isEqualTo(UPDATED_RESTAURANT_LATITUDE);
        assertThat(testRestaurant.getRestaurantLongitude()).isEqualTo(UPDATED_RESTAURANT_LONGITUDE);
        assertThat(testRestaurant.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(restaurant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(0)).save(restaurant);
    }

    @Test
    @Transactional
    void deleteRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        int databaseSizeBeforeDelete = restaurantRepository.findAll().size();

        // Delete the restaurant
        restRestaurantMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Restaurant in Elasticsearch
        verify(mockRestaurantSearchRepository, times(1)).deleteById(restaurant.getId());
    }

    @Test
    @Transactional
    void searchRestaurant() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);
        when(mockRestaurantSearchRepository.search("id:" + restaurant.getId())).thenReturn(Stream.of(restaurant));

        // Search the restaurant
        restRestaurantMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + restaurant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.getId().intValue())))
            .andExpect(jsonPath("$.[*].restaurantName").value(hasItem(DEFAULT_RESTAURANT_NAME)))
            .andExpect(jsonPath("$.[*].restaurantAddress").value(hasItem(DEFAULT_RESTAURANT_ADDRESS)))
            .andExpect(jsonPath("$.[*].restaurantAddressCmp").value(hasItem(DEFAULT_RESTAURANT_ADDRESS_CMP)))
            .andExpect(jsonPath("$.[*].restaurantType").value(hasItem(DEFAULT_RESTAURANT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isSub").value(hasItem(DEFAULT_IS_SUB.booleanValue())))
            .andExpect(jsonPath("$.[*].restaurantSubscription").value(hasItem(DEFAULT_RESTAURANT_SUBSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].restaurantSubDate").value(hasItem(DEFAULT_RESTAURANT_SUB_DATE.toString())))
            .andExpect(jsonPath("$.[*].restaurantDescription").value(hasItem(DEFAULT_RESTAURANT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].restaurantPhoneNumber").value(hasItem(DEFAULT_RESTAURANT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].restaurantEmail").value(hasItem(DEFAULT_RESTAURANT_EMAIL)))
            .andExpect(jsonPath("$.[*].restaurantWebSite").value(hasItem(DEFAULT_RESTAURANT_WEB_SITE)))
            .andExpect(jsonPath("$.[*].restaurantLatitude").value(hasItem(DEFAULT_RESTAURANT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].restaurantLongitude").value(hasItem(DEFAULT_RESTAURANT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())));
    }
}
