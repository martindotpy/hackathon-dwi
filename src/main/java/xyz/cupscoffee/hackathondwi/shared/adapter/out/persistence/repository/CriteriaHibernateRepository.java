package xyz.cupscoffee.hackathondwi.shared.adapter.out.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.application.util.StringUtils;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Filter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;

/**
 * Repository for paginated entities using Hibernate.
 */
@Slf4j
public abstract class CriteriaHibernateRepository<T> {
    @Autowired
    protected EntityManager entityManager;

    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public CriteriaHibernateRepository() {
        this.clazz = (Class<T>) ((java.lang.reflect.ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Matches the criteria with the entity manager and the class.
     *
     * @param criteria The criteria to match by.
     * @return The paginated response
     * @throws IllegalArgumentException If the class is not an entity.
     */
    public Paginated<T> match(Criteria criteria) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Class must be an entity");
        }

        CriteriaBuilder criteriaCountBuilder = entityManager.getCriteriaBuilder();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        CriteriaQuery<Long> countQuery = criteriaCountBuilder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(clazz);
        countQuery.select(criteriaCountBuilder.count(countRoot));

        List<Predicate> predicatesCount = new ArrayList<>();
        List<Predicate> predicates = new ArrayList<>();
        for (Filter filter : criteria.getFilters()) {
            addFilter(filter, criteriaBuilder, root, predicates);
            addFilter(filter, criteriaBuilder, countRoot, predicatesCount);
        }
        countQuery.where(predicatesCount.toArray(Predicate[]::new));
        long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        criteriaQuery.select(root).where(predicates.toArray(Predicate[]::new));

        switch (criteria.getOrder().getType()) {
            case ASC -> criteriaQuery
                    .orderBy(criteriaBuilder.asc(root.get(StringUtils.toCamelCase(criteria.getOrder().getField()))));
            case DESC -> criteriaQuery
                    .orderBy(criteriaBuilder.desc(root.get(StringUtils.toCamelCase(criteria.getOrder().getField()))));
            case NONE -> {
                // Do nothing
            }
        }

        long page = criteria.getPage();
        long size = criteria.getSize();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<T> elements = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) ((page - 1) * size))
                .setMaxResults((int) size)
                .getResultList();

        return Paginated.<T>builder()
                .content(elements)
                .page((int) page)
                .size((int) size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    /**
     * Adds a filter to the query.
     *
     * @param filter          The filter to add.
     * @param criteriaBuilder The criteria builder to use.
     * @param root            The root to use.
     * @param predicates      The predicates to add to.
     */
    private void addFilter(Filter filter, CriteriaBuilder criteriaBuilder, Root<T> root, List<Predicate> predicates) {
        if (filter.getValue() == null || filter.getValue().isBlank()) {
            return;
        }

        String[] fieldParts = filter.getField().split("\\.");
        Path<?> path = root;

        for (String part : fieldParts) {
            path = path.get(part);
        }

        Predicate predicate = switch (filter.getOperator()) {
            case LIKE -> criteriaBuilder.like(path.as(String.class), "%" + filter.getValue() + "%");
            case EQUAL -> criteriaBuilder.equal(path, filter.getValue());
            default -> throw new IllegalArgumentException("Unsupported filter operator: " + filter.getOperator());
        };

        predicates.add(predicate);
    }

    /**
     * Matches one entity with the criteria.
     *
     * @param criteria The criteria to match by.
     * @return The entity, if found
     * @throws IllegalArgumentException If the class is not an entity.
     * @throws IllegalStateException    If multiple entities are found
     */
    public Optional<T> matchOne(Criteria criteria) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Class must be an entity");
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Filter filter : criteria.getFilters()) {
            addFilter(filter, criteriaBuilder, root, predicates);
        }
        criteriaQuery.select(root).where(predicates.toArray(Predicate[]::new));

        List<T> results = entityManager.createQuery(criteriaQuery)
                .setMaxResults(2)
                .getResultList();

        if (results.isEmpty()) {
            return Optional.empty();
        } else if (results.size() > 1) {
            throw new IllegalStateException("Expected one result but found multiple");
        }

        return Optional.of(results.get(0));
    }
}