package com.manulife.pension.bd.web.bob.planReview.util;

import java.io.Serializable;

/**
 * @author R VaniKishore
 * 
 *         This class is used to hold values for Irregular navigation
 * 
 */
public class PlanReviewPageNavigationInfo implements Serializable {

	private static final long serialVersionUID = -8070333755117921636L;
	private String currentPage;
	private String nextPage;

	public PlanReviewPageNavigationInfo(String currentPage, String nextPage) {
		this.currentPage = currentPage;
		this.nextPage = nextPage;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

}
