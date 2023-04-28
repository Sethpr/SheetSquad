package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Pool;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pool entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoolRepository extends JpaRepository<Pool, Long> {}
