package com.manulife.pension.ps.web.tpafeeschedule;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.report.ReportForm;

public class TPAFeeScheduleContractSearchForm extends ReportForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String contractName;
	private String contractNumber;
	private Collection details=new ArrayList();
	private int tpaId;
	private int totalCount;
	private boolean enableContractSearch;
	private String selectedContractNumber;

	
	/**
	 * @return the tpaId
	 */
	public int getTpaId() {
		return tpaId;
	}

	/**
	 * @param tpaId the tpaId to set
	 */
	public void setTpaId(int tpaId) {
		this.tpaId = tpaId;
	}

	public TPAFeeScheduleContractSearchForm() {
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

	/**
	 * @return the contractName
	 */
	public String getContractName() {
		return contractName;
	}

	/**
	 * @param contractName the contractName to set
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	/**
	 * @return the details
	 */
	public Collection getDetails() {
		return details;
	}
   /**
	 * @param details the details to set
	 */
	public void setDetails(Collection details) {
		this.details = details;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public boolean isEnableContractSearch() {
		return enableContractSearch;
	}

	public void setEnableContractSearch(boolean enableContractSearch) {
		this.enableContractSearch = enableContractSearch;
	}

	public void reset( HttpServletRequest arg1) {
		//super.reset(arg0, arg1);
	//	setSortDirection(ReportSort.ASC_DIRECTION);
	//	setSortField("contractName");
	}

	public void resetSearchFilters() {
		setContractName(StringUtils.EMPTY);
		setContractNumber(StringUtils.EMPTY);
	}

	public String getSelectedContractNumber() {
		return selectedContractNumber;
	}

	public void setSelectedContractNumber(String selectedContractNumber) {
		this.selectedContractNumber = selectedContractNumber;
	}
}
