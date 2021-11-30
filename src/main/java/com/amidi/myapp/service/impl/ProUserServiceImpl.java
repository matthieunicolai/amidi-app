package com.amidi.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.ProUser;
import com.amidi.myapp.repository.ProUserRepository;
import com.amidi.myapp.repository.search.ProUserSearchRepository;
import com.amidi.myapp.service.ProUserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProUser}.
 */
@Service
@Transactional
public class ProUserServiceImpl implements ProUserService {

    private final Logger log = LoggerFactory.getLogger(ProUserServiceImpl.class);

    private final ProUserRepository proUserRepository;

    private final ProUserSearchRepository proUserSearchRepository;

    public ProUserServiceImpl(ProUserRepository proUserRepository, ProUserSearchRepository proUserSearchRepository) {
        this.proUserRepository = proUserRepository;
        this.proUserSearchRepository = proUserSearchRepository;
    }

    @Override
    public ProUser save(ProUser proUser) {
        log.debug("Request to save ProUser : {}", proUser);
        ProUser result = proUserRepository.save(proUser);
        proUserSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<ProUser> partialUpdate(ProUser proUser) {
        log.debug("Request to partially update ProUser : {}", proUser);

        return proUserRepository
            .findById(proUser.getId())
            .map(existingProUser -> {
                if (proUser.getProUserName() != null) {
                    existingProUser.setProUserName(proUser.getProUserName());
                }
                if (proUser.getProUserSurname() != null) {
                    existingProUser.setProUserSurname(proUser.getProUserSurname());
                }
                if (proUser.getProUserRole() != null) {
                    existingProUser.setProUserRole(proUser.getProUserRole());
                }
                if (proUser.getProUserLogin() != null) {
                    existingProUser.setProUserLogin(proUser.getProUserLogin());
                }
                if (proUser.getProUserPwd() != null) {
                    existingProUser.setProUserPwd(proUser.getProUserPwd());
                }
                if (proUser.getProUserEmail() != null) {
                    existingProUser.setProUserEmail(proUser.getProUserEmail());
                }
                if (proUser.getProUserPhoneNumber() != null) {
                    existingProUser.setProUserPhoneNumber(proUser.getProUserPhoneNumber());
                }
                if (proUser.getIsActivated() != null) {
                    existingProUser.setIsActivated(proUser.getIsActivated());
                }

                return existingProUser;
            })
            .map(proUserRepository::save)
            .map(savedProUser -> {
                proUserSearchRepository.save(savedProUser);

                return savedProUser;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProUser> findAll() {
        log.debug("Request to get all ProUsers");
        return proUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProUser> findOne(Long id) {
        log.debug("Request to get ProUser : {}", id);
        return proUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProUser : {}", id);
        proUserRepository.deleteById(id);
        proUserSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProUser> search(String query) {
        log.debug("Request to search ProUsers for query {}", query);
        return StreamSupport.stream(proUserSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
