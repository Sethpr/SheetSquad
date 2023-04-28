package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Quality;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface QualityRepositoryWithBagRelationships {
    Optional<Quality> fetchBagRelationships(Optional<Quality> quality);

    List<Quality> fetchBagRelationships(List<Quality> qualities);

    Page<Quality> fetchBagRelationships(Page<Quality> qualities);
}
