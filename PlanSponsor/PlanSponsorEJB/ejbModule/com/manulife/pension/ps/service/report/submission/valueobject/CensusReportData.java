package com.manulife.pension.ps.service.report.submission.valueobject;

import java.io.Serializable;

import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author parkand
 */
public abstract class CensusReportData extends ReportData implements Serializable {

	public static final String SORT_NAME = "name";
	public static final String SORT_CITY = "city";
	public static final String SORT_STATE = "state";
	public static final String SORT_SSN = "ssn"; 
    public static final String SORT_ZIP = "zip";
    public static final String SORT_COUNTRY = "country";
	
	public static final String FILTER_CONTRACT_NO = "contractNumber";

	protected int contractNumber;
	
	protected String sortCode;
	
	public CensusReportData() {
		super();
	}

	/**
	 * @param criteria
	 * @param totalCount
	 */
	public CensusReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * @return Returns the sortCode.
	 */
	public String getSortCode() {
		return sortCode;
	}
	/**
	 * @param sortCode The sortCode to set.
	 */
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}
	/**
	 * @return Returns the contractNumber.
	 */
	public int getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param contractNumber The contractNumber to set.
	 */
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}

}
