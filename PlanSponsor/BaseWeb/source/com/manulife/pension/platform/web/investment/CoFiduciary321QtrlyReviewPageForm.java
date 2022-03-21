package com.manulife.pension.platform.web.investment;


import java.util.Date;
import java.util.List;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.investment.valueobject.CoFidContractDocuments;


/**
 * Form Bean for CoFiduciary321 Quarterly Review Details
 * 
 * @author Sreenivasa Koppula
 *
 */

public class CoFiduciary321QtrlyReviewPageForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date currentDate = null;
	private List<CoFidContractDocuments> coFidContractDocList = null;

	
	public List<CoFidContractDocuments> getCoFidContractDocList() {
		return coFidContractDocList;
	}

	public void setCoFidContractDocList(
			List<CoFidContractDocuments> coFidContractDocList) {
		this.coFidContractDocList = coFidContractDocList;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	public void resetData() {
		this.currentDate = null;
		this.coFidContractDocList = null;
	}

}
