package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Extra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Extra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtraRepository extends JpaRepository<Extra, Long> {}
