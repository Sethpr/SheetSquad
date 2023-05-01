package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Power;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Power entity.
 */
@Repository
public interface PowerRepository extends JpaRepository<Power, Long> {
    default Optional<Power> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Power> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Power> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct power from Power power left join fetch power.owner",
        countQuery = "select count(distinct power) from Power power"
    )
    Page<Power> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct power from Power power left join fetch power.owner")
    List<Power> findAllWithToOneRelationships();

    @Query("select power from Power power left join fetch power.owner where power.id =:id")
    Optional<Power> findOneWithToOneRelationships(@Param("id") Long id);
}
