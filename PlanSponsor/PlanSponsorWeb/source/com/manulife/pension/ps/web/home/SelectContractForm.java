package com.manulife.pension.ps.web.home;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.report.valueobject.ReportSort;

public class SelectContractForm extends ReportForm {

	private String contractNumber;

	public SelectContractForm() {
	}

	/**
	 * Gets the contractNumber
	 * 
	 * @return Returns a String
	 */
	public String getContractNumber() {
		return contractNumber == null ? "" : contractNumber.trim();
	}

	/**
	 * Sets the contractNumber
	 * 
	 * @param contractNumber
	 *            The contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public void reset( HttpServletRequest arg1) {
		//super.reset(arg0, arg1);
		setSortDirection(ReportSort.ASC_DIRECTION);
		setSortField("contractName");
	}
}