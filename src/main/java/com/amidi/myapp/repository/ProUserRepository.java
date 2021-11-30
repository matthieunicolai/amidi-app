package com.amidi.myapp.repository;

import com.amidi.myapp.domain.ProUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProUserRepository extends JpaRepository<ProUser, Long> {}
