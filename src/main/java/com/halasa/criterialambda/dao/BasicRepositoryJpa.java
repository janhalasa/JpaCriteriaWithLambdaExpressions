package com.halasa.criterialambda.dao;

import com.halasa.criterialambda.dao.builder.OrderBy;
import com.halasa.criterialambda.dao.builder.PredicateAndOrder;
import com.halasa.criterialambda.dao.builder.PredicateAndOrderBuilder;
import com.halasa.criterialambda.dao.builder.PredicateBuilder;
import com.halasa.criterialambda.dao.builder.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implements support for lambda queries. The most important class of the project.
 * 
 * @param <T> Entity class
 * @author janhalasa
 */
public abstract class BasicRepositoryJpa<T> {

	public static final String JAVAX_PERSISTENCE_LOADGRAPH = "javax.persistence.loadgraph";

	private final Class<T> entityClass;
	private final String entityName;
	private final SingularAttribute<?, ?> pkField;
	private final Logger logger;

	private final EntityManager em;

	protected BasicRepositoryJpa(
			EntityManager em,
			Class<? extends BasicRepositoryJpa<T>> repositoryClass,
			Class<T> entityClass,
			SingularAttribute<?, ?> pkField) {
		this.em = em;
		this.entityClass = entityClass;
		this.pkField = pkField;
		this.entityName = JpaUtils.getEntityName(entityClass);
		this.logger = LoggerFactory.getLogger(repositoryClass.getName());
	}

	protected Logger logger() {
		return this.logger;
	}

	protected EntityManager em() {
		return this.em;
	}

	protected Class<T> entityClass() {
		return this.entityClass;
	}

	protected String entityName() {
		return this.entityName;
	}

	protected SingularAttribute<?, ?> pkField() {
		return this.pkField;
	}

	/**
	 * Creates a TypedQuery which can be further customized by calling its methods such as setMaxResults() or
	 * setFirstResult. To get results, call its getResultList() or getSingleResult() method. Method is private, so it
	 * cannot be overridden - it's used by other methods.
	 */
	private TypedQuery<T> createTypedQuery(
			QueryBuilder<T> queryBuilder,
			EntityGraph<T> entityLoadGraph) {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(entityClass);
		Root<T> root = q.from(entityClass);
		CriteriaQuery<T> criteriaQuery = q.select(root);
		criteriaQuery = queryBuilder.build(cb, root, criteriaQuery);

		final TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);

		if (entityLoadGraph != null) {
			typedQuery.setHint(JAVAX_PERSISTENCE_LOADGRAPH, entityLoadGraph);
		}

		return typedQuery;
	}

	private TypedQuery<T> createTypedQuery(QueryBuilder<T> queryBuilder) {
		return this.createTypedQuery(queryBuilder, null);
	}

	private List<Order> buildOrderBy(List<OrderBy> orderByList, CriteriaBuilder cb) {
		return orderByList.stream()
				.map(orderBy -> orderBy.toJpa(cb))
				.collect(Collectors.toList());
	}

	protected TypedQuery<T> createQuery(QueryBuilder<T> queryBuilder) {
		return createTypedQuery(queryBuilder);
	}

	public void persist(T entity) {
		this.em().persist(entity);
	}

	public List<T> findAll() {
		return this.find((cb, root, criteriaQuery) -> criteriaQuery);
	}

	public List<T> find(QueryBuilder<T> queryBuilder) {
		return createQuery(queryBuilder).getResultList();
	}

	protected List<T> findWhere(PredicateBuilder<T> predicateBuilder) {
		return this.findWhereOrdered(
				PredicateAndOrderBuilder.of(predicateBuilder),
				null);
	}

	protected List<T> findWhereOrdered(
			PredicateAndOrderBuilder<T> predicateAndOrderBuilder,
			EntityGraph<T> entityLoadGraph) {
		return createTypedQuery(
				(cb, root, query) -> {
					final PredicateAndOrder predicateAndOrder = predicateAndOrderBuilder.build(cb, root);
					if (predicateAndOrder.getPredicate() != null) {
						query.where(predicateAndOrder.getPredicate());
					}
					if (predicateAndOrder.getOrders() != null) {
						query.orderBy(this.buildOrderBy(predicateAndOrder.getOrders(), cb));
					}
					return query;
				},
				entityLoadGraph
		).getResultList();
	}

	protected List<T> findWhereOrdered(PredicateAndOrderBuilder<T> predicateAndOrderBuilder) {
		return createTypedQuery(
				(cb, root, query) -> {
					final PredicateAndOrder predicateAndOrder = predicateAndOrderBuilder.build(cb, root);
					if (predicateAndOrder.getPredicate() != null) {
						query.where(predicateAndOrder.getPredicate());
					}
					if (predicateAndOrder.getOrders() != null) {
						query.orderBy(this.buildOrderBy(predicateAndOrder.getOrders(), cb));
					}
					return query;
				},
				null
		).getResultList();
	}

	/**
	 * Finds a single entity matching the predicate.
	 *
	 * @param predicateBuilders Restricting query conditions. If you supply more than one predicate, they will be joined
	 *     by conjunction.
	 */
	@SuppressWarnings("unchecked")
	protected T loadWhere(List<PredicateBuilder<T>> predicateBuilders) {
		return createTypedQuery(
				(cb, root, query) -> (query
						.where(buildPredicates(
								cb,
								root,
								predicateBuilders.toArray(new PredicateBuilder[0])))))
				.getSingleResult();
	}

	protected T loadWhere(PredicateBuilder<T> predicateBuilder) {
		return loadWhere(Collections.singletonList(predicateBuilder));
	}

	protected T loadWhere(PredicateBuilder<T> predicateBuilder1, PredicateBuilder<T> predicateBuilder2) {
		return loadWhere(Arrays.asList(predicateBuilder1, predicateBuilder2));
	}

	protected Optional<T> getWhere(List<PredicateBuilder<T>> predicateBuilders) {
		@SuppressWarnings("unchecked")
		final List<T> results = createTypedQuery(
				(cb, root, query) -> (query.where(buildPredicates(
						cb,
						root,
						predicateBuilders.toArray(new PredicateBuilder[0])))))
				.getResultList();
		if (results.isEmpty()) {
			return Optional.empty();
		}
		if (results.size() > 1) {
			throw new NonUniqueResultException("There were " + results.size() + " results");
		}
		return Optional.of(results.get(0));
	}

	protected Optional<T> getWhere(PredicateBuilder<T> predicateBuilder) {
		return getWhere(Collections.singletonList(predicateBuilder));
	}

	protected Optional<T> getWhere(PredicateBuilder<T> predicateBuilder1, PredicateBuilder<T> predicateBuilder2) {
		return getWhere(Arrays.asList(predicateBuilder1, predicateBuilder2));
	}

	protected Predicate[] buildPredicates(CriteriaBuilder cb, Root<T> root, PredicateBuilder<T>[] predicateBuilders) {
		final List<Predicate> predicates = new ArrayList<>();
		if (predicateBuilders != null && predicateBuilders.length > 0) {
			for (PredicateBuilder<T> builder : predicateBuilders) {
				predicates.add(builder.build(cb, root));
			}
		}
		return predicates.toArray(new Predicate[0]);
	}

	@SuppressWarnings("unchecked")
	private PredicateBuilder<T>[] toPredicateBuilderArray(PredicateBuilder<T> predicateBuilder) {
		return predicateBuilder != null
				? new PredicateBuilder[] { predicateBuilder }
				: new PredicateBuilder[] {};
	}

	protected long countWhere(PredicateBuilder<T> predicateBuilder) {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<T> root = q.from(entityClass);
		CriteriaQuery<Long> criteriaQuery = q.select(cb.count(root));
		final Predicate predicate = predicateBuilder.build(cb, root);
		if (predicate != null) {
			criteriaQuery = criteriaQuery.where(predicate);
		}
		return em.createQuery(criteriaQuery)
				.getSingleResult();
	}

	protected ResultPage<T> pageWhere(
			PredicateAndOrderBuilder<T> predicateAndOrderBuilder,
			int pageNumber,
			int pageSize,
			EntityGraph<T> entityLoadGraph) {

		if (predicateAndOrderBuilder == null) {
			throw new IllegalArgumentException("Paging requires at least some order.");
		}
		if (pageNumber < 1) {
			throw new IllegalArgumentException("Page number must be 1 or higher: " + pageNumber);
		}
		if (pageSize < 1) {
			throw new IllegalArgumentException("Page size must be 1 or higher: " + pageNumber);
		}

		final TypedQuery<T> typedQuery = createTypedQuery(
				(cb, root, query) -> {
					final PredicateAndOrder predicateAndOrder = predicateAndOrderBuilder.build(cb, root);
					if (predicateAndOrder.getPredicate() != null) {
						query.where(predicateAndOrder.getPredicate());
					}
					if (predicateAndOrder.getOrders() == null) {
						throw new IllegalArgumentException("Paging requires ordering.");
					}
					query.orderBy(this.buildOrderBy(predicateAndOrder.getOrders(), cb));
					return query;
				},
				entityLoadGraph);

		final List<T> resultList = typedQuery
				.setFirstResult((pageNumber - 1) * pageSize)
				.setMaxResults(pageSize)
				.getResultList();

		final long totalCount = this.countWhere((cb, root) -> predicateAndOrderBuilder.build(cb, root).getPredicate());

		return new ResultPage<>(totalCount, pageNumber, pageSize, resultList);
	}

	protected ResultPage<T> pageWhere(
			PredicateAndOrderBuilder<T> predicateAndOrderBuilder,
			int pageNumber,
			int pageSize) {
		return pageWhere(
				predicateAndOrderBuilder,
				pageNumber,
				pageSize,
				null);
	}

	protected void deleteWhere(PredicateBuilder<T> predicateBuilder) {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaDelete<T> delete = cb.createCriteriaDelete(entityClass);
		Root<T> root = delete.from(entityClass);
		delete.where(predicateBuilder.build(cb, root));
		em().createQuery(delete).executeUpdate();
	}
}
