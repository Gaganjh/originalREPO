package com.manulife.pension.bd.web.bob.planReview.util;

import java.io.Serializable;

public class PersistStep1SortDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sortField;
	private String sortDirection;
	
	
	public PersistStep1SortDetails(String sortField, String sortDirection ) {
		this.sortField = sortField;
		this.sortDirection = sortDirection;
	}
	
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
}
