package com.jpa;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.builder.query.QueryBuilder;
import com.jpa.model.User;
import com.jpa.search.SearchCriteria;

@Service
public class UserService {

	@Autowired
	private SessionFactory sessionFactory;

	public List<User> searchUser(SearchCriteria searchCriteria) {
		QueryBuilder<User> qb = new QueryBuilder<User>(User.class, searchCriteria);
		return qb.getQuery(this.sessionFactory.openSession()).getResultList();
	}
}
