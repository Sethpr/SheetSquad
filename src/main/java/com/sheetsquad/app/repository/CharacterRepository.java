package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Character;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Character entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Query("select character from Character character where character.owner.login = ?#{principal.username}")
    List<Character> findByOwnerIsCurrentUser();
}
