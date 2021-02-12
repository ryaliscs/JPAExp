package com.jpa.search;

import java.io.Serializable;

public class SearchCriteria implements Serializable {

	private static final long serialVersionUID = 3464623786364675396L;

	private String name;
	private String value;
	private String op;

	public SearchCriteria() {
	}

	public SearchCriteria(String name, String value, String op) {
		super();
		this.name = name;
		this.value = value;
		this.op = op;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public String getOp() {
		return this.op;
	}

	public LIOperator getOperator() {
		return LIOperator.valueOf(this.op);
	}

}
