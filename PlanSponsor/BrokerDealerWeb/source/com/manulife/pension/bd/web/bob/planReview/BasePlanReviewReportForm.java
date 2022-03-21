package com.manulife.pension.bd.web.bob.planReview;

import com.manulife.pension.bd.web.bob.planReview.util.PersistStep1SortDetails;
import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * @author R VaniKishore
 * 
 *         This is the Base form class used for Irregular navigation fields for
 *         all Plan review Report forms.Every action form should extend this
 *         form so that the irregular navigation fields would be available
 * 
 */
public class BasePlanReviewReportForm extends BaseReportForm {

	private static final long serialVersionUID = -7249401450752755857L;

	private PersistStep1SortDetails step1SortDetails;
	private String pageRegularlyNavigated;

	public String getPageRegularlyNavigated() {
		return pageRegularlyNavigated;
	}

	public void setPageRegularlyNavigated(String pageRegularlyNavigated) {
		this.pageRegularlyNavigated = pageRegularlyNavigated;
	}
	
	public PersistStep1SortDetails getStep1SortDetails() {
		return step1SortDetails;
	}

	public void setStep1SortDetails(PersistStep1SortDetails step1SortDetails) {
		this.step1SortDetails = step1SortDetails;
	}


}
