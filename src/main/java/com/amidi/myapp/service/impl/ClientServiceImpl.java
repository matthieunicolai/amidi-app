package com.amidi.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.amidi.myapp.domain.Client;
import com.amidi.myapp.repository.ClientRepository;
import com.amidi.myapp.repository.search.ClientSearchRepository;
import com.amidi.myapp.service.ClientService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Client}.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientSearchRepository clientSearchRepository;

    public ClientServiceImpl(ClientRepository clientRepository, ClientSearchRepository clientSearchRepository) {
        this.clientRepository = clientRepository;
        this.clientSearchRepository = clientSearchRepository;
    }

    @Override
    public Client save(Client client) {
        log.debug("Request to save Client : {}", client);
        Client result = clientRepository.save(client);
        clientSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Client> partialUpdate(Client client) {
        log.debug("Request to partially update Client : {}", client);

        return clientRepository
            .findById(client.getId())
            .map(existingClient -> {
                if (client.getName() != null) {
                    existingClient.setName(client.getName());
                }
                if (client.getSurname() != null) {
                    existingClient.setSurname(client.getSurname());
                }
                if (client.getBirthDate() != null) {
                    existingClient.setBirthDate(client.getBirthDate());
                }
                if (client.getEmail() != null) {
                    existingClient.setEmail(client.getEmail());
                }
                if (client.getPhoneNumber() != null) {
                    existingClient.setPhoneNumber(client.getPhoneNumber());
                }
                if (client.getClientLogin() != null) {
                    existingClient.setClientLogin(client.getClientLogin());
                }
                if (client.getClientPwd() != null) {
                    existingClient.setClientPwd(client.getClientPwd());
                }
                if (client.getIsActivated() != null) {
                    existingClient.setIsActivated(client.getIsActivated());
                }

                return existingClient;
            })
            .map(clientRepository::save)
            .map(savedClient -> {
                clientSearchRepository.save(savedClient);

                return savedClient;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        log.debug("Request to get all Clients");
        return clientRepository.findAllWithEagerRelationships();
    }

    public Page<Client> findAllWithEagerRelationships(Pageable pageable) {
        return clientRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
        clientSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> search(String query) {
        log.debug("Request to search Clients for query {}", query);
        return StreamSupport.stream(clientSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
