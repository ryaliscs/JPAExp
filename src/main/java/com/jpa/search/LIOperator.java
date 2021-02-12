package com.jpa.search;

public enum LIOperator {

	EQUAL("=="), LIKE("=");

	private String op;

	LIOperator(String op) {
		this.op = op;
	}

	public String getOperator() {
		return this.op;
	}
}
