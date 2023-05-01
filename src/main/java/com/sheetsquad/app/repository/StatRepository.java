package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Stat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Stat entity.
 */
@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
    default Optional<Stat> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Stat> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Stat> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct stat from Stat stat left join fetch stat.owner",
        countQuery = "select count(distinct stat) from Stat stat"
    )
    Page<Stat> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct stat from Stat stat left join fetch stat.owner")
    List<Stat> findAllWithToOneRelationships();

    @Query("select stat from Stat stat left join fetch stat.owner where stat.id =:id")
    Optional<Stat> findOneWithToOneRelationships(@Param("id") Long id);
}
