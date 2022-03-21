package com.manulife.pension.bd.web.estatement;

import java.io.Serializable;
import java.util.Date;

/**
 * Value Object to hold the statement related details which would be used in front end
 * @author raoprer
 *
 */
public class RiaStatementVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4308353435755385000L;
	private String pdfDocId = null;
	private String csvDocId = null;
	private String firmName = null;
	private String firmId = null;
	private String genDateStr = null;
	private Date genDate = null;

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getPdfDocId() {
		return pdfDocId;
	}

	public void setPdfDocId(String pdfDocId) {
		this.pdfDocId = pdfDocId;
	}

	public String getCsvDocId() {
		return csvDocId;
	}

	public void setCsvDocId(String csvDocId) {
		this.csvDocId = csvDocId;
	}

	public Date getGenDate() {
		return genDate;
	}

	public void setGenDate(Date gendate) {
		this.genDate = gendate;
	}

	public String getFirmId() {
		return firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	public String getGenDateStr() {
		return genDateStr;
	}

	public void setGenDateStr(String genDateStr) {
		this.genDateStr = genDateStr;
	}

}