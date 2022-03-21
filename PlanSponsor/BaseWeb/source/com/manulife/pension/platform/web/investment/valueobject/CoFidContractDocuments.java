package com.manulife.pension.platform.web.investment.valueobject;

import java.io.Serializable;
import java.util.Date;

/**
 * Value Object for Wilshire 3(21) Adviser Service Review Report Details
 * 
 * @author Sreenivasa Koppula
 * 
 */

public class CoFidContractDocuments implements Serializable {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	private String contractDocName;
	private Date documentCreatedDate;
	private Date quarterStartDate;
	private String documentCurrentQuarter;
	private String providerDisplayName;
	
	
	
	/**
	 * @return the contractDocName
	 */
	public String getContractDocName() {
		return contractDocName;
	}
	/**
	 * @param contractDocName the contractDocName to set
	 */
	public void setContractDocName(String contractDocName) {
		this.contractDocName = contractDocName;
	}
	/**
	 * @return the documentCreatedDate
	 */
	public Date getDocumentCreatedDate() {
		return documentCreatedDate;
	}
	/**
	 * @param documentCreatedDate the documentCreatedDate to set
	 */
	public void setDocumentCreatedDate(Date documentCreatedDate) {
		this.documentCreatedDate = documentCreatedDate;
	}
	/**
	 * @return the quarterStartDate
	 */
	public Date getQuarterStartDate() {
		return quarterStartDate;
	}
	/**
	 * @param quarterStartDate the quarterStartDate to set
	 */
	public void setQuarterStartDate(Date quarterStartDate) {
		this.quarterStartDate = quarterStartDate;
	}
	/**
	 * @return the documentCurrentQuarter
	 */
	public String getDocumentCurrentQuarter() {
		return documentCurrentQuarter;
	}
	/**
	 * @param documentCurrentQuarter the documentCurrentQuarter to set
	 */
	public void setDocumentCurrentQuarter(String documentCurrentQuarter) {
		this.documentCurrentQuarter = documentCurrentQuarter;
	}
	public String getProviderDisplayName() {
		return providerDisplayName;
	}
	public void setProviderDisplayName(String providerDisplayName) {
		this.providerDisplayName = providerDisplayName;
	}
	
	
}
