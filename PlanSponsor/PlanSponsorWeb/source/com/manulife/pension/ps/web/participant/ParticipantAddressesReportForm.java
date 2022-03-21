package com.manulife.pension.ps.web.participant;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData;
import com.manulife.pension.ps.web.report.ReportForm;

/**
 * @author kumarye
 *
 */
public class ParticipantAddressesReportForm extends ReportForm {
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_LAST_NAME = "lastName";
	
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
	private String namePhrase = null;
	private ParticipantAddressesReportData report = null;
    
	private boolean allowedToEdit;
    private boolean allowedToAccessEligibTab;
    private boolean allowedToAccessVestingTab;
    private boolean allowedToDownload;
    private boolean allowedToAdd;
    private boolean allowedToAutoEnrollment;
    private boolean allowedToAccessDeferralTab;
    
    private String task = "default";

	private String path = null;
	private String division;
    private String segment;
    private String status;
    
    private List statusList = new ArrayList();
    private List segmentList = new ArrayList();
	

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public ParticipantAddressesReportForm() {
		super();
	}

	/**
	 * Gets the namePhrase
	 * @return Returns a String
	 */
	public String getNamePhrase() {
		return trim(namePhrase);
	}
	/**
	 * Sets the namePhrase
	 * @param namePhrase The namePhrase to set
	 */
	public void setNamePhrase(String namePhrase) {
		this.namePhrase = namePhrase;
	}


	public ParticipantAddressesReportData getReport(){
		return this.report;
	}	


	public void setReport(ParticipantAddressesReportData report){
		this.report = report;
	}		
	/**
	 * Gets the ssn
	 * @return Returns a Ssn
	 */
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,ssnOne);
		ssnTemp.setDigits(1,ssnTwo);
		ssnTemp.setDigits(2,ssnThree);
		return ssnTemp;
	}
	/**
	 * Gets the ssnOne
	 * @return Returns a String
	 */
	public String getSsnOne() {
		return ssnOne;
	}
	/**
	 * Sets the ssnOne
	 * @param ssnOne The ssnOne to set
	 */
	public void setSsnOne(String ssnOne) {
		this.ssnOne = ssnOne;
	}


	/**
	 * Gets the ssnTwo
	 * @return Returns a String
	 */
	public String getSsnTwo() {
		return ssnTwo;
	}
	/**
	 * Sets the ssnTwo
	 * @param ssnTwo The ssnTwo to set
	 */
	public void setSsnTwo(String ssnTwo) {
		this.ssnTwo = ssnTwo;
	}


	/**
	 * Gets the ssnThree
	 * @return Returns a String
	 */
	public String getSsnThree() {
		return ssnThree;
	}
	/**
	 * Sets the ssnThree
	 * @param ssnThree The ssnThree to set
	 */
	public void setSsnThree(String ssnThree) {
		this.ssnThree = ssnThree;
	}


	/**
	 * @return Returns the allowedToEdit.
	 */
	public boolean isAllowedToEdit() {
		return allowedToEdit;
	}
	/**
	 * @param allowedToEdit The allowedToEdit to set.
	 */
	public void setAllowedToEdit(boolean allowedToEdit) {
		this.allowedToEdit = allowedToEdit;
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getStatusList() {
        return statusList;
    }

    public void setStatusList(List statusList) {
        this.statusList = statusList;
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
    
    
}
