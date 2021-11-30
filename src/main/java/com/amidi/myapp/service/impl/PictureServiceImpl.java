package com.amidi.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.Picture;
import com.amidi.myapp.repository.PictureRepository;
import com.amidi.myapp.repository.search.PictureSearchRepository;
import com.amidi.myapp.service.PictureService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Picture}.
 */
@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    private final PictureRepository pictureRepository;

    private final PictureSearchRepository pictureSearchRepository;

    public PictureServiceImpl(PictureRepository pictureRepository, PictureSearchRepository pictureSearchRepository) {
        this.pictureRepository = pictureRepository;
        this.pictureSearchRepository = pictureSearchRepository;
    }

    @Override
    public Picture save(Picture picture) {
        log.debug("Request to save Picture : {}", picture);
        Picture result = pictureRepository.save(picture);
        pictureSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Picture> partialUpdate(Picture picture) {
        log.debug("Request to partially update Picture : {}", picture);

        return pictureRepository
            .findById(picture.getId())
            .map(existingPicture -> {
                if (picture.getPictureName() != null) {
                    existingPicture.setPictureName(picture.getPictureName());
                }
                if (picture.getPictureUrl() != null) {
                    existingPicture.setPictureUrl(picture.getPictureUrl());
                }
                if (picture.getPictureAlt() != null) {
                    existingPicture.setPictureAlt(picture.getPictureAlt());
                }
                if (picture.getIsLogo() != null) {
                    existingPicture.setIsLogo(picture.getIsLogo());
                }
                if (picture.getIsDisplayed() != null) {
                    existingPicture.setIsDisplayed(picture.getIsDisplayed());
                }

                return existingPicture;
            })
            .map(pictureRepository::save)
            .map(savedPicture -> {
                pictureSearchRepository.save(savedPicture);

                return savedPicture;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Picture> findAll() {
        log.debug("Request to get all Pictures");
        return pictureRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Picture> findOne(Long id) {
        log.debug("Request to get Picture : {}", id);
        return pictureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureRepository.deleteById(id);
        pictureSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Picture> search(String query) {
        log.debug("Request to search Pictures for query {}", query);
        return StreamSupport.stream(pictureSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
