package com.jpa.builder.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.jpa.search.SearchCriteria;

public class QueryBuilder<T> {


	private final Class<T> entity;
	private final SearchCriteria searchCriteria;

	public QueryBuilder(Class<T> entity, SearchCriteria searchCriteria) {
		this.entity = entity;
		this.searchCriteria = searchCriteria;
	}

	public Query<T> getQuery(Session session) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(this.entity);
		Root<T> root = cq.from(this.entity);
		Predicate p = getPredicate(this.searchCriteria, cb, root);
		cq.select(root).where(p);
		return session.createQuery(cq);
	}

	private Predicate getPredicate(SearchCriteria searchCriteria, CriteriaBuilder cb, Root<T> root) {
		Predicate p;

		switch (searchCriteria.getOperator()) {
		case LIKE:
			p = cb.like(root.get(searchCriteria.getName()), "%" + searchCriteria.getValue() + "%");
			break;

		case EQUAL:
			p = cb.equal(root.get(searchCriteria.getName()), searchCriteria.getValue());
			break;
		default: // Error
			 throw new IllegalArgumentException(searchCriteria.getOp());			
		}
		return p;
	}
}
