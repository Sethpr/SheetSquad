package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.BaseExtra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BaseExtra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseExtraRepository extends JpaRepository<BaseExtra, Long> {}
