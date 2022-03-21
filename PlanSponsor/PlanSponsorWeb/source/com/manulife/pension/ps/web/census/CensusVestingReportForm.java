package com.manulife.pension.ps.web.census;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.vesting.VestingConstants;

public class CensusVestingReportForm extends ReportForm implements AllowedToEdit {

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

    private String asOfDate;

    private String division;

    private String segment;

    private boolean allowedToEdit;
    
    private boolean allowedToViewVestingInformation;

    private boolean allowedToAccessEligibTab;

    private boolean allowedToDownloadCensus;

    private boolean allowedToDownloadVesting;

    private boolean allowedToAdd;

    private boolean allowedToAutoEnrollment;

    private String dirty = "false";
    
    private String vestingServiceFeature;

    private boolean allowedToAccessDeferralTab;

    private List statusList = new ArrayList();

    private List segmentList = new ArrayList();

    private List<Date> asOfDateList = new ArrayList<Date>();

    private boolean isEditMode;

    private String serviceCreditingMethod;

    private CensusVestingReportDataUi censusVestingReportDataUi;
    
    private String task;

    public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	private boolean displayEditButton;
	private boolean displaySaveButton;
	private boolean displayCancelButton;
	

	public void setDisplayEditButton(boolean displayEditButton) {
		this.displayEditButton = displayEditButton;
	}

	public void setDisplaySaveButton(boolean displaySaveButton) {
		this.displaySaveButton = displaySaveButton;
	}

	public void setDisplayCancelButton(boolean displayCancelButton) {
		this.displayCancelButton = displayCancelButton;
	}

	/**
     * Constructor for CensusVestingReportForm.
     */
    public CensusVestingReportForm() {
        super();
        this.censusVestingReportDataUi = new CensusVestingReportDataUi();
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
     * @param contractDates The contractDates to set
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
     * @param namePhrase The namePhrase to set
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
     * @param ssnOne The ssnOne to set
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
     * @param ssnTwo The ssnTwo to set
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
     * @param ssnThree The ssnThree to set
     */
    public void setSsnThree(String ssnThree) {
        this.ssnThree = ssnThree;
    }

    public String getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(String asOfDate) {
        this.asOfDate = asOfDate;
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

    public boolean isAllowedToDownloadCensus() {
        return allowedToDownloadCensus;
    }

    public void setAllowedToDownloadCensus(boolean allowedToDownloadCensus) {
        this.allowedToDownloadCensus = allowedToDownloadCensus;
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

    public boolean isAllowedToDownloadVesting() {
        return allowedToDownloadVesting;
    }

    public void setAllowedToDownloadVesting(boolean allowedToDownloadVesting) {
        this.allowedToDownloadVesting = allowedToDownloadVesting;
    }

    public List<Date> getAsOfDateList() {
        return asOfDateList;
    }

    public void setAsOfDateList(List<Date> asOfDateList) {
        this.asOfDateList = asOfDateList;
    }

    public String getVestingServiceFeature() {
        return vestingServiceFeature;
    }

    public void setVestingServiceFeature(String vestingServiceFeature) {
        this.vestingServiceFeature = vestingServiceFeature;
    }

    /**
     * Returns true if the CSF is calculate, false otherwise.
     * 
     * @return boolean - True if the vesting service feature is set to calculate, false otherwise.
     */
    public boolean getIsVestingServiceFeatureCalculate() {
        return StringUtils.equals(vestingServiceFeature, VestingConstants.VestingServiceFeature.CALCULATION);
    }

    /**
     * Returns true if the CSF is collection, false otherwise.
     * 
     * @return boolean - True if the vesting service feature is set to collection, false otherwise.
     */
    public boolean getIsVestingServiceFeatureCollection() {
        return StringUtils.equals(vestingServiceFeature, VestingConstants.VestingServiceFeature.COLLECTION);
    }

    /**
     * Logical test to display the info icon (used in the legend).
     * 
     * @return boolean - True if the icon is to be displayed, false otherwise.
     */
    public boolean getDisplayInfoIcon() {
        return ((isAllowedToViewVestingInformation()) && (!(getIsEditMode()))
        );
    }
    
    /**
     * Determines if the page is in edit mode or not.
     * 
     * @return boolean - True if the page is in edit mode, false otherwise.
     */
    public boolean getIsEditMode() {
        return isEditMode;
    }
    

    /**
     * @param isEditMode - The isEditMode to set.
     */
    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public boolean isAllowedToAccessDeferralTab() {
        return allowedToAccessDeferralTab;
    }

    public void setAllowedToAccessDeferralTab(boolean allowedToAccessDeferralTab) {
        this.allowedToAccessDeferralTab = allowedToAccessDeferralTab;
    }

    /**
     * Determines if the edit button should be displayed or not.
     * 
     * @return boolean - True if the button should be displayed, false otherwise.
     */
    public boolean getDisplayEditButton() {
        return getCensusVestingReportDataUi().getDisplayEditButton();
    }

    /**
     * Determines if the save button should be displayed or not.
     * 
     * @return boolean - True if the button should be displayed, false otherwise.
     */
    public boolean getDisplaySaveButton() {
        return getCensusVestingReportDataUi().getDisplaySaveButton();
    }
    
    /**
     * Determines if the cancel button should be displayed or not.
     * 
     * @return boolean - True if the button should be displayed, false otherwise.
     */
    public boolean getDisplayCancelButton() {
        return getCensusVestingReportDataUi().getDisplayCancelButton();
    }

    /**
     * Determines if the mouse over history should be displayed or not.
     * 
     * @return boolean - True if the content should be displayed, false otherwise.
     */
    public boolean getDisplayMouseOverHistory() {
        return (!(isEditMode));
    }
    
    /**
     * @return returns "true" if the form is dirty
     */
    public String getDirty() {
        return dirty;
    }

    /**
     * @param dirty
     */
    public void setDirty(final String dirty) {
        this.dirty = Boolean.parseBoolean(dirty) ? Boolean.TRUE.toString() : Boolean.FALSE
                .toString();
    }

    public boolean isAllowedToViewVesting() {
        return (!(getIsEditMode()));
    }

    public boolean isAllowedToViewVestingInformation() {
        return allowedToViewVestingInformation;
    }

    public void setAllowedToViewVestingInformation(boolean allowedToViewVestingInformation) {
        this.allowedToViewVestingInformation = allowedToViewVestingInformation;
    }

    /**
     * @return CensusVestingReportDataUi - The censusVestingReportDataUi.
     */
    public CensusVestingReportDataUi getCensusVestingReportDataUi() {
        return censusVestingReportDataUi;
    }

    /**
     * @param censusVestingReportDataUi - The censusVestingReportDataUi to set.
     */
    public void setCensusVestingReportDataUi(final CensusVestingReportDataUi censusVestingReportDataUi) {
        this.censusVestingReportDataUi = censusVestingReportDataUi;
    }

    /**
     * Determines if the crediting method has been set to either Elapsed Time or Hours of Service.
     * 
     * @return boolean - True if it's set, false if it's null or unspecified.
     */
    public boolean getIsCreditingMethodSpecified() {
        return StringUtils.equals(VestingConstants.CreditingMethod.ELAPSED_TIME,
                serviceCreditingMethod)
                || StringUtils.equals(VestingConstants.CreditingMethod.HOURS_OF_SERVICE,
                        serviceCreditingMethod);
    }

    /**
     * @return String - The serviceCreditingMethod.
     */
    public String getServiceCreditingMethod() {
        return serviceCreditingMethod;
    }

    /**
     * @param serviceCreditingMethod - The serviceCreditingMethod to set.
     */
    public void setServiceCreditingMethod(final String serviceCreditingMethod) {
        this.serviceCreditingMethod = serviceCreditingMethod;
    }
}
