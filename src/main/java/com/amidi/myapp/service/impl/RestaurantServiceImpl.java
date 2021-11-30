package com.amidi.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.Restaurant;
import com.amidi.myapp.repository.RestaurantRepository;
import com.amidi.myapp.repository.search.RestaurantSearchRepository;
import com.amidi.myapp.service.RestaurantService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurant}.
 */
@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantSearchRepository restaurantSearchRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantSearchRepository restaurantSearchRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantSearchRepository = restaurantSearchRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        log.debug("Request to save Restaurant : {}", restaurant);
        Restaurant result = restaurantRepository.save(restaurant);
        restaurantSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Restaurant> partialUpdate(Restaurant restaurant) {
        log.debug("Request to partially update Restaurant : {}", restaurant);

        return restaurantRepository
            .findById(restaurant.getId())
            .map(existingRestaurant -> {
                if (restaurant.getRestaurantName() != null) {
                    existingRestaurant.setRestaurantName(restaurant.getRestaurantName());
                }
                if (restaurant.getRestaurantAddress() != null) {
                    existingRestaurant.setRestaurantAddress(restaurant.getRestaurantAddress());
                }
                if (restaurant.getRestaurantAddressCmp() != null) {
                    existingRestaurant.setRestaurantAddressCmp(restaurant.getRestaurantAddressCmp());
                }
                if (restaurant.getRestaurantType() != null) {
                    existingRestaurant.setRestaurantType(restaurant.getRestaurantType());
                }
                if (restaurant.getIsSub() != null) {
                    existingRestaurant.setIsSub(restaurant.getIsSub());
                }
                if (restaurant.getRestaurantSubscription() != null) {
                    existingRestaurant.setRestaurantSubscription(restaurant.getRestaurantSubscription());
                }
                if (restaurant.getRestaurantSubDate() != null) {
                    existingRestaurant.setRestaurantSubDate(restaurant.getRestaurantSubDate());
                }
                if (restaurant.getRestaurantDescription() != null) {
                    existingRestaurant.setRestaurantDescription(restaurant.getRestaurantDescription());
                }
                if (restaurant.getRestaurantPhoneNumber() != null) {
                    existingRestaurant.setRestaurantPhoneNumber(restaurant.getRestaurantPhoneNumber());
                }
                if (restaurant.getRestaurantEmail() != null) {
                    existingRestaurant.setRestaurantEmail(restaurant.getRestaurantEmail());
                }
                if (restaurant.getRestaurantWebSite() != null) {
                    existingRestaurant.setRestaurantWebSite(restaurant.getRestaurantWebSite());
                }
                if (restaurant.getRestaurantLatitude() != null) {
                    existingRestaurant.setRestaurantLatitude(restaurant.getRestaurantLatitude());
                }
                if (restaurant.getRestaurantLongitude() != null) {
                    existingRestaurant.setRestaurantLongitude(restaurant.getRestaurantLongitude());
                }
                if (restaurant.getIsActivated() != null) {
                    existingRestaurant.setIsActivated(restaurant.getIsActivated());
                }

                return existingRestaurant;
            })
            .map(restaurantRepository::save)
            .map(savedRestaurant -> {
                restaurantSearchRepository.save(savedRestaurant);

                return savedRestaurant;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> findAll() {
        log.debug("Request to get all Restaurants");
        return restaurantRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurant> findOne(Long id) {
        log.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
        restaurantSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> search(String query) {
        log.debug("Request to search Restaurants for query {}", query);
        return StreamSupport.stream(restaurantSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
