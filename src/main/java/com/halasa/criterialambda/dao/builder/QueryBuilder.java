package com.halasa.criterialambda.dao.builder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jan
 */
public interface QueryBuilder<T> {

	CriteriaQuery<T> build(CriteriaBuilder criteriaBuilder, Root<T> root, CriteriaQuery<T> criteriaQuery);
}
