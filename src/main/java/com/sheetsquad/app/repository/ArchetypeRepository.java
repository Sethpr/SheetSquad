package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Archetype;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Archetype entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArchetypeRepository extends JpaRepository<Archetype, Long> {}
