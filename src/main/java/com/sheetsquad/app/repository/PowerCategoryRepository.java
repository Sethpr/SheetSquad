package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.PowerCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PowerCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerCategoryRepository extends JpaRepository<PowerCategory, Long> {}
