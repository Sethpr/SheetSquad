package com.sheetsquad.app.repository;

import com.sheetsquad.app.domain.Stat;
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
public class StatRepositoryWithBagRelationshipsImpl implements StatRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Stat> fetchBagRelationships(Optional<Stat> stat) {
        return stat.map(this::fetchExtras).map(this::fetchOwners);
    }

    @Override
    public Page<Stat> fetchBagRelationships(Page<Stat> stats) {
        return new PageImpl<>(fetchBagRelationships(stats.getContent()), stats.getPageable(), stats.getTotalElements());
    }

    @Override
    public List<Stat> fetchBagRelationships(List<Stat> stats) {
        return Optional.of(stats).map(this::fetchExtras).map(this::fetchOwners).orElse(Collections.emptyList());
    }

    Stat fetchExtras(Stat result) {
        return entityManager
            .createQuery("select stat from Stat stat left join fetch stat.extras where stat is :stat", Stat.class)
            .setParameter("stat", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Stat> fetchExtras(List<Stat> stats) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, stats.size()).forEach(index -> order.put(stats.get(index).getId(), index));
        List<Stat> result = entityManager
            .createQuery("select distinct stat from Stat stat left join fetch stat.extras where stat in :stats", Stat.class)
            .setParameter("stats", stats)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Stat fetchOwners(Stat result) {
        return entityManager
            .createQuery("select stat from Stat stat left join fetch stat.owners where stat is :stat", Stat.class)
            .setParameter("stat", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Stat> fetchOwners(List<Stat> stats) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, stats.size()).forEach(index -> order.put(stats.get(index).getId(), index));
        List<Stat> result = entityManager
            .createQuery("select distinct stat from Stat stat left join fetch stat.owners where stat in :stats", Stat.class)
            .setParameter("stats", stats)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
