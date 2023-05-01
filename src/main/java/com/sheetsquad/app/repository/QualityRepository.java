package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Quality;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quality entity.
 */
@Repository
public interface QualityRepository extends JpaRepository<Quality, Long> {
    default Optional<Quality> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Quality> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Quality> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct quality from Quality quality left join fetch quality.owner",
        countQuery = "select count(distinct quality) from Quality quality"
    )
    Page<Quality> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct quality from Quality quality left join fetch quality.owner")
    List<Quality> findAllWithToOneRelationships();

    @Query("select quality from Quality quality left join fetch quality.owner where quality.id =:id")
    Optional<Quality> findOneWithToOneRelationships(@Param("id") Long id);
}
