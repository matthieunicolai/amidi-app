package com.amidi.myapp.repository;

import com.amidi.myapp.domain.Hedgeprod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hedgeprod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HedgeprodRepository extends JpaRepository<Hedgeprod, Long> {}
