package com.jpa;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jpa.builder.query.QueryBuilder;
import com.jpa.model.User;
import com.jpa.search.SearchCriteria;

@Service
public class UserService {

	@Autowired
	private SessionFactory sessionFactory;

	public ResponseEntity<Object> searchUser(SearchCriteria searchCriteria) {
		try {
		QueryBuilder<User> qb = new QueryBuilder<User>(User.class, searchCriteria);
		return new ResponseEntity<Object>(qb.getQuery(this.sessionFactory.openSession()).getResultList(), HttpStatus.OK);
		}
		catch(IllegalArgumentException ex) {
			return new ResponseEntity<Object>("UnSupported Operator "+searchCriteria.getOp(), HttpStatus.BAD_REQUEST);
		}
	}
}
