package com.jpa.builder.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.jpa.search.SCOperator;
import com.jpa.search.SearchCriteria;
import com.jpa.search.SearchNode;

public class QueryBuilder<T> {

	private final Class<T> entity;
	private final SearchCriteria searchCriteria;

	public QueryBuilder(Class<T> entity, SearchCriteria searchCriteria) {
		this.entity = entity;
		this.searchCriteria = searchCriteria;
	}

	public List<T> getResult(EntityManager entityManager) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(this.entity);
		Root<T> root = cq.from(this.entity);
		Predicate finalPredicate = addPredicates(cb, root);

		cq.select(root).where(finalPredicate);
		return entityManager.createQuery(cq).getResultList();
	}

	/**
	 * AddPredicates: Add all predicates {@link Predicate} as per the
	 * {@link SearchCriteria} nodes and {@link SCOperator}.
	 * 
	 * @param criteriaBuilder {@link CriteriaBuilder} to generate query
	 * @param root            query root
	 * @return Predicate added to the given criteriaBilder
	 */
	private Predicate addPredicates(CriteriaBuilder criteriaBuilder, Root<T> root) {
		Predicate finalPredicate;

		List<Predicate> predicates = new ArrayList<Predicate>(this.searchCriteria.getSearchNodes().size());
		for (SearchNode node : this.searchCriteria.getSearchNodes()) {
			predicates.add(getPredicate(node, criteriaBuilder, root));
		}

		switch (searchCriteria.getScOp()) {
		case AND:
			finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			break;
		case OR:
			finalPredicate = criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
			break;
		default: // Error
			throw new IllegalArgumentException("Unsupported operator " + searchCriteria.getScOp().name());
		}
		return finalPredicate;
	}

	/**
	 * Creates the predicate and adds it to the criteria builder
	 * 
	 * @param searchNode      Search node to create the predicate
	 * @param criteriaBuilder {@link CriteriaBuilder} to add the predicate
	 * @param root            query root
	 * @return {@link Predicate}
	 */
	private Predicate getPredicate(SearchNode searchNode, CriteriaBuilder criteriaBuilder, Root<T> root) {
		Predicate p;

		switch (searchNode.getOperator()) {
		case LIKE:
			p = criteriaBuilder.like(root.get(searchNode.getName()), "%" + searchNode.getValue() + "%");
			break;

		case EQUAL:
			p = criteriaBuilder.equal(root.get(searchNode.getName()), searchNode.getValue());
			break;

		case GREATER_THAN:
			p = criteriaBuilder.greaterThan(root.get(searchNode.getName()), searchNode.getValue());
			break;

		case GREATER_THAN_OR_EQUAL_TO:
			p = criteriaBuilder.greaterThanOrEqualTo(root.get(searchNode.getName()), searchNode.getValue());
			break;

		case LESS_THAN:
			p = criteriaBuilder.lessThan(root.get(searchNode.getName()), searchNode.getValue());
			break;

		case LESS_THAN_OR_EQUAL_TO:
			p = criteriaBuilder.lessThanOrEqualTo(root.get(searchNode.getName()), searchNode.getValue());
			break;

		case NOT_EQUAL:
			p = criteriaBuilder.notEqual(root.get(searchNode.getName()), searchNode.getValue());
			break;
			
		case NOT_NULL:
			p = criteriaBuilder.isNotNull(root.get(searchNode.getName()));
			break;
			
		case IS_NULL:
			p = criteriaBuilder.isNull(root.get(searchNode.getName()));
			break;
			
//		case IS_EMPTY:
//			p = criteriaBuilder.isEmpty(root.get(searchNode.getName()));
//			break;
//		case NOT_EMPTY:
//			Path<Collection<?>> collection = root.get(searchNode.getName());
//			p = criteriaBuilder.isNotEmpty(collection);
//			break;
			
		default: // Error
			throw new IllegalArgumentException("Unsupported operator " + searchNode.getOp());
		}
		return p;
	}
}
