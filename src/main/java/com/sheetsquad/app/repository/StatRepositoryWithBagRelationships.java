package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Stat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StatRepositoryWithBagRelationships {
    Optional<Stat> fetchBagRelationships(Optional<Stat> stat);

    List<Stat> fetchBagRelationships(List<Stat> stats);

    Page<Stat> fetchBagRelationships(Page<Stat> stats);
}
