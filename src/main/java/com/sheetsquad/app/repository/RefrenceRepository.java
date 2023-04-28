package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Refrence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Refrence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefrenceRepository extends JpaRepository<Refrence, Long> {}
