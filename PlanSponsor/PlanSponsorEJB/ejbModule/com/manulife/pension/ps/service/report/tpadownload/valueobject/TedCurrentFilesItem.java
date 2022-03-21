package com.manulife.pension.ps.service.report.tpadownload.valueobject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.FastDateFormat;

public class TedCurrentFilesItem implements Serializable {
	public final static String DOWNLOAD_STATUS_NEW = "Y";
	public final static String DOWNLOAD_STATUS_DOWNLOADED = "D";
	public final static String DOWNLOAD_STATUS_NOT_AVAILABLE = "N";
	public final static String CORRECTED_STATUS_YES = "Y";
	public final static String CORRECTED_STATUS_NO = "N";
	
	public static final String DOWNLOAD_STATUS_NEW_DESC = "New";
	public static final String DOWNLOAD_STATUS_DOWNLOADED_DESC = "Downloaded";
	public static final String DOWNLOAD_STATUS_DOWNLOADED_TODAY_DESC = "Downloaded<b>**</b>";
	public static final String DOWNLOAD_STATUS_NOT_AVAILABLE_DESC = "Not Available";	
	public static final String CORRECTED_STATUS_YES_DESC = "Yes";
	public static final String CORRECTED_STATUS_NO_DESC = "No";
	
	public static final String SORT_FIELD_LAST_RUN_DATE = "lastRunDate";
	public static final String SORT_FIELD_QUARTER_END_DATE = "quarterEndDate";
	public static final String SORT_FIELD_CONTRACT_NAME = "contractName";
	public static final String SORT_FIELD_CONTRACT_NUMBER = "contractNumber";
	public static final String SORT_FIELD_DOWNLOAD_STATUS = "downloadStatus";
	public static final String SORT_FIELD_CORRECTED_IND = "correctedIndicator";
	public static final String SORT_FIELD_LAST_DOWNLOAD_DATE = "lastDownloadDate";
	public static final String SORT_FIELD_YEAR_END_IND = "yearEnd";
		
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	public static final FastDateFormat sdf = FastDateFormat.getInstance("MM/dd/yyyy");
	
	private String contractNumber;
	private String contractName;
	private String tpaId;
	private GregorianCalendar quarterEndDate;
	private GregorianCalendar lastRunDate;
	private GregorianCalendar lastDownloadDate;	
	private String correctedIndicator;
	private String downloadStatus;
	private String downloadStatusCode;
	private String fileSize;
	private String fileName = "";
	private int quarter=-1;
	private String identityString=null;
	private String quarterEndLink="";
	private GregorianCalendar today = new GregorianCalendar();
    private String yearEnd;
    
	public String getYearEnd() {
		return yearEnd;
	}
	public void setYearEnd(String yearEnd) {
        if (yearEnd != null && !yearEnd.trim().equals("")
                && yearEnd.equalsIgnoreCase(CORRECTED_STATUS_YES)) {
            this.yearEnd = CORRECTED_STATUS_YES_DESC;
        } else {
            this.yearEnd = CORRECTED_STATUS_NO_DESC;
        }	
	}
	/**
	 * @return Returns the contractName.
	 */
	public String getContractName() {
		return contractName;
	}
	/**
	 * @param contractName The contractName to set.
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	/**
	 * @return Returns the contractNumber.
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param contractNumber The contractNumber to set.
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	/**
	 * @return Returns the correctedIndicator.
	 */
	public String getCorrectedIndicator() {
		return correctedIndicator;
	}
	/**
	 * @param correctedIndicator The correctedIndicator to set.
	 */
	public void setCorrectedIndicator(String correctedIndicator) {
		if (correctedIndicator.equalsIgnoreCase(CORRECTED_STATUS_YES)){
			this.correctedIndicator = CORRECTED_STATUS_YES_DESC;
		} else if (correctedIndicator.equalsIgnoreCase(CORRECTED_STATUS_NO)){
			this.correctedIndicator = CORRECTED_STATUS_NO_DESC;
		} else {
			this.correctedIndicator = correctedIndicator;			
		}
	}
	/**
	 * @return Returns the downloadStatus.
	 */
	public String getDownloadStatus() {
		return downloadStatus;
	}
	/**
	 * @param downloadStatus The downloadStatus to set.
	 */
	public void setDownloadStatusCode(String downloadStatus) {
		downloadStatusCode = downloadStatus;
		if (downloadStatus.equalsIgnoreCase(DOWNLOAD_STATUS_DOWNLOADED)) {
			// If downloaded today use a different description.
			if ((this.getLastDownloadDate().get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) 
				&& (this.getLastDownloadDate().get(Calendar.YEAR) == today.get(Calendar.YEAR)) ) {

				this.downloadStatus = DOWNLOAD_STATUS_DOWNLOADED_TODAY_DESC;
			} else {
				this.downloadStatus = DOWNLOAD_STATUS_DOWNLOADED_DESC;
			}
		} else if (downloadStatus.equalsIgnoreCase(DOWNLOAD_STATUS_NEW)) {
			this.downloadStatus = DOWNLOAD_STATUS_NEW_DESC;			
		} else if (downloadStatus.equalsIgnoreCase(DOWNLOAD_STATUS_NOT_AVAILABLE)) {
			this.downloadStatus = DOWNLOAD_STATUS_NOT_AVAILABLE_DESC;			
		} else {
			this.downloadStatus = downloadStatus;
		}
	}
	/**
	 * @return Returns the lastRunDate.
	 */
	public GregorianCalendar getLastRunDate() {
		return lastRunDate;
	}
	/**
	 * @param lastRunDate The lastRunDate to set.
	 */
	public void setLastRunDate(GregorianCalendar lastRunDate) {
		this.lastRunDate = lastRunDate;
	}
	/**
	 * @return Returns the quarterEndDate.
	 */
	public GregorianCalendar getQuarterEndDate() {
		return quarterEndDate;
	}
	/**
	 * @param quarterEndDate The quarterEndDate to set.
	 */
	public void setQuarterEndDate(GregorianCalendar quarterEndDate) {
		this.quarterEndDate = quarterEndDate;
	}
	/**
	 * @return Returns the fileSize.
	 */
	public String getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize The fileSize to set.
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getFileName() {
		return getFileName(true);
	}
	
	public String getIdentityString() {
		if (identityString == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String quarterEnd = sdf.format(getQuarterEndDate().getTime());
            String yearEndInd = this.yearEnd.equalsIgnoreCase(CORRECTED_STATUS_YES_DESC) ? "Y"
                    : "N";
			setIdentityString(getTpaId() + getContractNumber() + quarterEnd + yearEndInd + getFileName());
		}
		return identityString;
	}
	public void setIdentityString(String value) {
		identityString = value;
	}
	
	public String getFileName(boolean withExtension) {
		if (fileName.equals("")) {		
			int year = quarterEndDate.get(Calendar.YEAR);
			fileName = getContractNumber() + String.valueOf(year) + String.valueOf(getQuarter()) + ".ZIP";
		}
		return fileName;
	}
	
	/**
	 * Returns the name of the current ManuMerge file from the TPA directory on the FTP server.
	 *
	 * @param withExtension - if true the appropriate file extension will be included in the file name (ie. 73083090.ZIP).
	 *						  Otherwise it won't. 
	 * @return java.lang.String		Format: cccccmmy.xxx
	 *								where	ccccc = contract number
	 *										mm    = month of quarterEndDate
	 *										y     = last digit of year of quarterEndDate
	 *										xxx   = BAK, if downloadStatus = "D"
	 *												ZIP, otherwise
	 */	
	public int getQuarter() {
		if (quarter == -1) {
			int month = quarterEndDate.get(Calendar.MONTH);			
			if (month <= 2) {
				this.quarter = 1;
			} else if (month >= 3 && month <= 5) {
				this.quarter = 2;
			} else if (month >= 6 && month <= 8) {
				this.quarter = 3;
			} else {
				this.quarter = 4;
			}
		}
		return quarter;
	}
	
	/**
	 * @return Returns the tpaId.
	 */
	public String getTpaId() {
		return tpaId;
	}
	/**
	 * @param tpaId The tpaId to set.
	 */
	public void setTpaId(String tpaId) {
		this.tpaId = tpaId;
	}
	public String getDownloadStatusCode() {
		return downloadStatusCode;
	}
	public void setDownloadStatus(String downloadStatus) {
		this.downloadStatus = downloadStatus;
	}
	
	public String getQuarterEndLink() {
		if (quarterEndLink == null || quarterEndLink.equals("")) {
			quarterEndLink = "<A href=\"javascript:doDownloadSingleFile('" + getIdentityString() + "')\">" + getFormattedQuarterEndDate() + "</A>";
		}
		return quarterEndLink;
	}
	public void setQuarterEndLink(String quarterEndLink) {
		this.quarterEndLink = quarterEndLink;
	}
	
	private String getFormattedQuarterEndDate() {
		return sdf.format(getQuarterEndDate().getTime());
	}
	public Date getQuarterEndAsDate() {
		return quarterEndDate.getTime();
	}
	public Date getLastRunAsDate(){
		return lastRunDate.getTime();
	}
	public Date getLastDownloadAsDate() {
		return lastDownloadDate.getTime();
	}
	public void setLastDownloadDate(GregorianCalendar lastDownloadDate) {
		this.lastDownloadDate = lastDownloadDate;
	}
	public GregorianCalendar getLastDownloadDate() {
		return this.lastDownloadDate;
	}
	public boolean showLastDownloadDate() {
		if (lastDownloadDate != null) {
			if (lastDownloadDate.get(Calendar.YEAR)>= 9999) {
				return false;
			}
		}
		return true;
	}
}

