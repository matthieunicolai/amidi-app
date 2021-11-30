package com.amidi.myapp.repository;

import com.amidi.myapp.domain.DishTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DishTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DishTagRepository extends JpaRepository<DishTag, Long> {}
