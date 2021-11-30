package com.amidi.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.Dish;
import com.amidi.myapp.repository.DishRepository;
import com.amidi.myapp.repository.search.DishSearchRepository;
import com.amidi.myapp.service.DishService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dish}.
 */
@Service
@Transactional
public class DishServiceImpl implements DishService {

    private final Logger log = LoggerFactory.getLogger(DishServiceImpl.class);

    private final DishRepository dishRepository;

    private final DishSearchRepository dishSearchRepository;

    public DishServiceImpl(DishRepository dishRepository, DishSearchRepository dishSearchRepository) {
        this.dishRepository = dishRepository;
        this.dishSearchRepository = dishSearchRepository;
    }

    @Override
    public Dish save(Dish dish) {
        log.debug("Request to save Dish : {}", dish);
        Dish result = dishRepository.save(dish);
        dishSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Dish> partialUpdate(Dish dish) {
        log.debug("Request to partially update Dish : {}", dish);

        return dishRepository
            .findById(dish.getId())
            .map(existingDish -> {
                if (dish.getDishName() != null) {
                    existingDish.setDishName(dish.getDishName());
                }
                if (dish.getDishDescription() != null) {
                    existingDish.setDishDescription(dish.getDishDescription());
                }
                if (dish.getDishPrice() != null) {
                    existingDish.setDishPrice(dish.getDishPrice());
                }
                if (dish.getDishDate() != null) {
                    existingDish.setDishDate(dish.getDishDate());
                }
                if (dish.getDishPictureName() != null) {
                    existingDish.setDishPictureName(dish.getDishPictureName());
                }
                if (dish.getDishPictureUrl() != null) {
                    existingDish.setDishPictureUrl(dish.getDishPictureUrl());
                }
                if (dish.getDishPictureAlt() != null) {
                    existingDish.setDishPictureAlt(dish.getDishPictureAlt());
                }
                if (dish.getIsDailyDish() != null) {
                    existingDish.setIsDailyDish(dish.getIsDailyDish());
                }
                if (dish.getIsAvailable() != null) {
                    existingDish.setIsAvailable(dish.getIsAvailable());
                }

                return existingDish;
            })
            .map(dishRepository::save)
            .map(savedDish -> {
                dishSearchRepository.save(savedDish);

                return savedDish;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dish> findAll(Pageable pageable) {
        log.debug("Request to get all Dishes");
        return dishRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dish> findOne(Long id) {
        log.debug("Request to get Dish : {}", id);
        return dishRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dish : {}", id);
        dishRepository.deleteById(id);
        dishSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dish> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Dishes for query {}", query);
        return dishSearchRepository.search(query, pageable);
    }
}
