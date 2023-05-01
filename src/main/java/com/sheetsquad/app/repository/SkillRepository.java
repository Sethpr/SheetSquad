package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Skill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Skill entity.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    default Optional<Skill> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Skill> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Skill> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct skill from Skill skill left join fetch skill.owner",
        countQuery = "select count(distinct skill) from Skill skill"
    )
    Page<Skill> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct skill from Skill skill left join fetch skill.owner")
    List<Skill> findAllWithToOneRelationships();

    @Query("select skill from Skill skill left join fetch skill.owner where skill.id =:id")
    Optional<Skill> findOneWithToOneRelationships(@Param("id") Long id);
}
