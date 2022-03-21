package com.manulife.pension.ps.web.census;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.report.ReportForm;

public class CensusSummaryReportForm extends ReportForm implements AllowedToEdit {


	private static final long serialVersionUID = 1L;

	public static final String FORMAT_DATE_LONG = "MMMM d, yyyy";
	public static final String FORMAT_DATE_SHORT_YMD = "yyyy-MM-dd";

	public static final String ALL = "All";
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_LAST_NAME = "lastName";

	private String status = null;
	private String namePhrase = null;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
    private String todayDate;
    private String division;
    private String segment;
    private boolean allowedToEdit;
    private boolean allowedToAccessEligibTab;
    private boolean allowedToAccessVestingTab;
    private boolean allowedToDownload;
    private boolean allowedToAdd;
    private boolean allowedToAutoEnrollment;
    private boolean allowedToAccessDeferralTab;
    private boolean allowedToDownloadBeneficiaryReport;
    
    private List statusList = new ArrayList();
    private List segmentList = new ArrayList();

	/**
	 * Constructor for CensusSummaryReportForm.
	 */
	public CensusSummaryReportForm() {
		super();
	}

	/**
	 * Gets the status
	 * 
	 * @return Returns a string
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Sets the Status
	 * 
	 * @param contractDates
	 *            The contractDates to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the namePhrase
	 * 
	 * @return Returns a String
	 */
	public String getNamePhrase() {
		return namePhrase;
	}

	/**
	 * Sets the namePhrase
	 * 
	 * @param namePhrase
	 *            The namePhrase to set
	 */
	public void setNamePhrase(String namePhrase) {
		this.namePhrase = namePhrase;
	}

	/**
	 * Gets the ssn
	 * 
	 * @return Returns a Ssn
	 */
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0, ssnOne);
		ssnTemp.setDigits(1, ssnTwo);
		ssnTemp.setDigits(2, ssnThree);
		return ssnTemp;
	}

	/**
	 * Gets the ssnOne
	 * 
	 * @return Returns a String
	 */
	public String getSsnOne() {
		return ssnOne;
	}

	/**
	 * Sets the ssnOne
	 * 
	 * @param ssnOne
	 *            The ssnOne to set
	 */
	public void setSsnOne(String ssnOne) {
		this.ssnOne = ssnOne;
	}

	/**
	 * Gets the ssnTwo
	 * 
	 * @return Returns a String
	 */
	public String getSsnTwo() {
		return ssnTwo;
	}

	/**
	 * Sets the ssnTwo
	 * 
	 * @param ssnTwo
	 *            The ssnTwo to set
	 */
	public void setSsnTwo(String ssnTwo) {
		this.ssnTwo = ssnTwo;
	}

	/**
	 * Gets the ssnThree
	 * 
	 * @return Returns a String
	 */
	public String getSsnThree() {
		return ssnThree;
	}

	/**
	 * Sets the ssnThree
	 * 
	 * @param ssnThree
	 *            The ssnThree to set
	 */
	public void setSsnThree(String ssnThree) {
		this.ssnThree = ssnThree;
	}

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public List getStatusList() {
        return statusList;
    }

    public void setStatusList(List statusList) {
        this.statusList = statusList;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public boolean isAllowedToEdit() {
        return allowedToEdit;
    }

    public void setAllowedToEdit(boolean allowedToEdit) {
        this.allowedToEdit = allowedToEdit;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }


    public List getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(List segmentList) {
        this.segmentList = segmentList;
    }

    public boolean isAllowedToAccessEligibTab() {
        return allowedToAccessEligibTab;
    }

    public void setAllowedToAccessEligibTab(boolean allowedToAccessEligibTab) {
        this.allowedToAccessEligibTab = allowedToAccessEligibTab;
    }

    public boolean isAllowedToDownload() {
        return allowedToDownload;
    }

    public void setAllowedToDownload(boolean allowedToDownload) {
        this.allowedToDownload = allowedToDownload;
    }

    public boolean isAllowedToAdd() {
        return allowedToAdd;
    }

    public void setAllowedToAdd(boolean allowedToAdd) {
        this.allowedToAdd = allowedToAdd;
    }

    public boolean isAllowedToAutoEnrollment() {
        return allowedToAutoEnrollment;
    }

    public void setAllowedToAutoEnrollment(boolean allowedToAutoEnrollment) {
        this.allowedToAutoEnrollment = allowedToAutoEnrollment;
    }

    public boolean isAllowedToAccessVestingTab() {
        return allowedToAccessVestingTab;
    }

    public void setAllowedToAccessVestingTab(boolean allowedToAccessVestingTab) {
        this.allowedToAccessVestingTab = allowedToAccessVestingTab;
    }
    
       
    public boolean isAllowedToAccessDeferralTab() {
		return allowedToAccessDeferralTab;
	}

	public void setAllowedToAccessDeferralTab(boolean allowedToAccessDeferralTab) {
		this.allowedToAccessDeferralTab = allowedToAccessDeferralTab;
	}

	/**
     * Check if the action source is from contract home
     * if it is, reset all the search criteria except for
     * name search criteria.
     * 
     * This is to resolve the problem, the search parameter
     * is retained 
     */
	public void resetSearchParameterFromContractHome() {
			setSsnOne(null);
			setSsnTwo(null);
			setSsnThree(null);
			setDivision(null);
			setStatus(null);
			setSegment(null);
	}

	/**
	 * Returns allowedToDownloadBeneficiaryReport value.
	 * 
	 * @return allowedToDownloadBeneficiaryReport
	 */
	public boolean isAllowedToDownloadBeneficiaryReport() {
		return allowedToDownloadBeneficiaryReport;
	}

	/**
	 * Set allowedToDownloadBeneficiaryReport value
	 * 
	 * @param allowedToDownloadBeneficiaryReport
	 */
	public void setAllowedToDownloadBeneficiaryReport(
			boolean allowedToDownloadBeneficiaryReport) {
		this.allowedToDownloadBeneficiaryReport = allowedToDownloadBeneficiaryReport;
	}
}
