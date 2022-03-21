/*
 * Created on May 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.tpadownload;

/**
 * @author eldrima
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TedRequestedFile {

	private String filename;
	private String contractNumber;
	private String tpaId;
	private String quarterEndDate;
	private boolean isCurrentFile=true;
    private String yearEndInd;
	
	public TedRequestedFile() {		
	}
	
	public String getYearEndInd() {
		return yearEndInd;
	}
	public void setYearEndInd(String yearEndInd) {
		this.yearEndInd = yearEndInd;
	}
	public String getQuarterEndDate() {
		return quarterEndDate;
	}
	public void setQuarterEndDate(String quarterEndDate) {
		this.quarterEndDate = quarterEndDate;
	}
	public boolean isCurrentFile() {
		return isCurrentFile;
	}
	public void setCurrentFile(boolean isCurrentFile) {
		this.isCurrentFile = isCurrentFile;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getTpaId() {
		return tpaId;
	}
	public void setTpaId(String tpaId) {
		this.tpaId = tpaId;
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
