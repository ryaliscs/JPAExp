package com.jpa.search;

import java.io.Serializable;
import java.util.List;

/**
 * Search Criteria : Creates a search criteria with search nodes and search
 * operator.
 * 
 * <pre>
 * example
	  	{
		   "searchNodes":[
		      {
		         "name":"lastName",
		         "value":"five",
		         "op":"LIKE"
		      },
		      {
		         "name":"firstName",
		         "value":"test5",
		         "op":"EQUAL"
		      }
		   ],
		   "scOp":"OR"
		}
		when scOp = OR:
		select * from <entitiy> where lastName LIKE '%five%' OR firstName='test5';
		when scOp = AND:
		select * from <entitiy> where lastName LIKE '%five%' AND firstName='test5';
 * </pre>
 * 
 * @author saryal
 *
 */
public class SearchCriteria implements Serializable {

	private static final long serialVersionUID = 2836053449503939573L;

	private List<SearchNode> searchNodes;
	private SCOperator scOp;

	public SearchCriteria() {
		this.scOp = SCOperator.AND;
	}

	/**
	 * creates a SearchCriteria object
	 * 
	 * @param searchNodes nodes to be searched
	 * @param scOperator  operator joing the nodes
	 */
	public SearchCriteria(List<SearchNode> searchNodes, SCOperator scOperator) {
		this.searchNodes = searchNodes;
		this.scOp = scOperator;
	}

	/**
	 * @return the searchNodes
	 */
	public List<SearchNode> getSearchNodes() {
		return searchNodes;
	}

	/**
	 * @return the scOperator
	 */
	public SCOperator getScOp() {
		return scOp;
	}
}
