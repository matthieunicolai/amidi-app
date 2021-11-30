package com.amidi.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.Hedgeprod;
import com.amidi.myapp.repository.HedgeprodRepository;
import com.amidi.myapp.repository.search.HedgeprodSearchRepository;
import com.amidi.myapp.service.HedgeprodService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Hedgeprod}.
 */
@Service
@Transactional
public class HedgeprodServiceImpl implements HedgeprodService {

    private final Logger log = LoggerFactory.getLogger(HedgeprodServiceImpl.class);

    private final HedgeprodRepository hedgeprodRepository;

    private final HedgeprodSearchRepository hedgeprodSearchRepository;

    public HedgeprodServiceImpl(HedgeprodRepository hedgeprodRepository, HedgeprodSearchRepository hedgeprodSearchRepository) {
        this.hedgeprodRepository = hedgeprodRepository;
        this.hedgeprodSearchRepository = hedgeprodSearchRepository;
    }

    @Override
    public Hedgeprod save(Hedgeprod hedgeprod) {
        log.debug("Request to save Hedgeprod : {}", hedgeprod);
        Hedgeprod result = hedgeprodRepository.save(hedgeprod);
        hedgeprodSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Hedgeprod> partialUpdate(Hedgeprod hedgeprod) {
        log.debug("Request to partially update Hedgeprod : {}", hedgeprod);

        return hedgeprodRepository
            .findById(hedgeprod.getId())
            .map(existingHedgeprod -> {
                if (hedgeprod.gethName() != null) {
                    existingHedgeprod.sethName(hedgeprod.gethName());
                }
                if (hedgeprod.gethSurname() != null) {
                    existingHedgeprod.sethSurname(hedgeprod.gethSurname());
                }
                if (hedgeprod.gethRole() != null) {
                    existingHedgeprod.sethRole(hedgeprod.gethRole());
                }
                if (hedgeprod.gethEmail() != null) {
                    existingHedgeprod.sethEmail(hedgeprod.gethEmail());
                }
                if (hedgeprod.gethPhoneNumber() != null) {
                    existingHedgeprod.sethPhoneNumber(hedgeprod.gethPhoneNumber());
                }
                if (hedgeprod.getIsActivated() != null) {
                    existingHedgeprod.setIsActivated(hedgeprod.getIsActivated());
                }

                return existingHedgeprod;
            })
            .map(hedgeprodRepository::save)
            .map(savedHedgeprod -> {
                hedgeprodSearchRepository.save(savedHedgeprod);

                return savedHedgeprod;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hedgeprod> findAll() {
        log.debug("Request to get all Hedgeprods");
        return hedgeprodRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hedgeprod> findOne(Long id) {
        log.debug("Request to get Hedgeprod : {}", id);
        return hedgeprodRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Hedgeprod : {}", id);
        hedgeprodRepository.deleteById(id);
        hedgeprodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hedgeprod> search(String query) {
        log.debug("Request to search Hedgeprods for query {}", query);
        return StreamSupport.stream(hedgeprodSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
