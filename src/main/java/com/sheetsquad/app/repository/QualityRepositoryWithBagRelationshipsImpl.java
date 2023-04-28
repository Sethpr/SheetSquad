package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Quality;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class QualityRepositoryWithBagRelationshipsImpl implements QualityRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Quality> fetchBagRelationships(Optional<Quality> quality) {
        return quality.map(this::fetchExtras);
    }

    @Override
    public Page<Quality> fetchBagRelationships(Page<Quality> qualities) {
        return new PageImpl<>(fetchBagRelationships(qualities.getContent()), qualities.getPageable(), qualities.getTotalElements());
    }

    @Override
    public List<Quality> fetchBagRelationships(List<Quality> qualities) {
        return Optional.of(qualities).map(this::fetchExtras).orElse(Collections.emptyList());
    }

    Quality fetchExtras(Quality result) {
        return entityManager
            .createQuery("select quality from Quality quality left join fetch quality.extras where quality is :quality", Quality.class)
            .setParameter("quality", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Quality> fetchExtras(List<Quality> qualities) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, qualities.size()).forEach(index -> order.put(qualities.get(index).getId(), index));
        List<Quality> result = entityManager
            .createQuery(
                "select distinct quality from Quality quality left join fetch quality.extras where quality in :qualities",
                Quality.class
            )
            .setParameter("qualities", qualities)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
