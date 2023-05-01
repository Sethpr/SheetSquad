package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.PowerCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PowerCategory entity.
 */
@Repository
public interface PowerCategoryRepository extends JpaRepository<PowerCategory, Long> {
    default Optional<PowerCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PowerCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PowerCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct powerCategory from PowerCategory powerCategory left join fetch powerCategory.owner",
        countQuery = "select count(distinct powerCategory) from PowerCategory powerCategory"
    )
    Page<PowerCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct powerCategory from PowerCategory powerCategory left join fetch powerCategory.owner")
    List<PowerCategory> findAllWithToOneRelationships();

    @Query("select powerCategory from PowerCategory powerCategory left join fetch powerCategory.owner where powerCategory.id =:id")
    Optional<PowerCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
