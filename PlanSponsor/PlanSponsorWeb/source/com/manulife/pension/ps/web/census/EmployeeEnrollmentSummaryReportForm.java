package com.manulife.pension.ps.web.census;
 
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.EmployeeEligibilitySummaryValidationRules;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportActionCloneableForm;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Form that works in tandem with <code>EmployeeEnrollmentSummaryReportAction</code>
 * 
 * @author patuadr
 *
 */
public class EmployeeEnrollmentSummaryReportForm extends ReportActionCloneableForm implements AllowedToEdit {

    private static final long serialVersionUID = -1867592717268166579L;
    public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";    
	public static final String FULL_COLUMNS = "full";
	public static final String HIDE_DIVISION = "hideDivision";
	public static final String HIDE_PAYROLL = "hidePayroll";
	public static final String HIDE_BOTH = "hideBoth";
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_LAST_NAME = "namePhrase";
    public static final String FIELD_SEGMENT = "segment";
    public static final String FIELD_DIVISION = "division";
	
//	public static final int NUMBER_FULL_COLUMNS_WITH_WEB = 26;
//	public static final int NUMBER_HIDE_ONE_COLUMN_WITH_WEB = 24;
//	public static final int NUMBER_HIDE_TWO_COLUMN_WITH_WEB = 22;	

	public static final int NUMBER_FULL_COLUMNS_WITH_WEB = 28;
	public static final int NUMBER_HIDE_ONE_COLUMN_WITH_WEB = 26;
	public static final int NUMBER_HIDE_TWO_COLUMN_WITH_WEB = 24;	
	public static final int NUMBER_HIDE_THREE_COLUMN_WITH_WEB = 22;
	public static final int NUMBER_HIDE_FOUR_COLUMN_WITH_WEB = 20;

    private boolean isInitialSearch = true;
    private boolean isSearch = false;
    protected String profileId;
 	private boolean hasDivisionFeature = true;
 	private boolean hasPayrollNumberFeature = true;
    private String segment = null;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;	
    private String status = null;    
    private List statusList = new ArrayList();
    private String namePhrase = null;    
    private String todayDate;    
    private String lastFeedDate;    
    private String division;
    private Date nextOptOut;
    private Date nextPED;
    private List segmentList = new ArrayList();
    private int frequency;
    private int oOD;
    private String empStatus;
    private String enrolledFrom=""; // ACI2
    private String enrolledTo=""; // ACI2
    private String mailingDate;
    private String languageInd;
    private boolean allowedToEdit;
    private boolean showOptOutReport;
    private boolean allowedViewEnrollmentStats;
    private boolean autoEnrollmentEnabled;
    private boolean allowedToDownloadCensus;
    private boolean allowedToAccessEligibTab;
    private boolean allowedToDownload;
    private boolean allowedToAccessVestingTab;
    private boolean allowedToAccessDeferralTab;
    private boolean isEZstartOn;
    private boolean showDM =false;
    private HashSet removedDeferrals = new HashSet();
    
    // Added For EC project. -- START --
    private boolean showEligibilitySection = false;
    private boolean isEligibiltyCalcOn ;
    private String fromPED = "";
    private String toPED = "";
    private boolean pendingEligibilityCalculationRequest ;
    private boolean showCalculateButton;
    private List<LabelValueBean> moneyTypes ;
    private String moneyTypeSelected;
    private boolean fromCalculateButton = false;
    private boolean showSaveAndCancelButtons = false;
    private String eedefShortName;
    //  Added For EC project. -- END --
    
    /**
     * A list of <code>EmployeeSummaryDetails</code> beans that contain data to be saved
     * The data for display comes from the report and later the data is captured in this collection
     * part of the Form
     * 
     */
    public List<EmployeeSummaryDetails> theItem = new ArrayList<EmployeeSummaryDetails>();

    	
	/**
	 * Constructor for ParticipantEnrollmentSummaryReportForm
	 */
	public EmployeeEnrollmentSummaryReportForm() {
		super();        
	}

	/**
	 * Gets the hasDivisionFeature
	 * @return Returns a boolean
	 */
	public boolean getHasDivisionFeature() {
		return hasDivisionFeature;
	}
    
	/**
	 * Sets the hasDivisionFeature
	 * @param hasDivisionFeature The hasDivisionFeature to set
	 */
	public void setHasDivisionFeature(boolean hasDivisionFeature) {
		this.hasDivisionFeature = hasDivisionFeature;
	}
	
	/**
	 * Gets the hasPayrollNumberFeature
	 * @return Returns a boolean
	 */
	public boolean getHasPayrollNumberFeature() {
		return hasPayrollNumberFeature;
	}
    
	/**
	 * Sets the hasPayrollNumberFeature
	 * @param hasPayrollNumberFeature The hasPayrollNumberFeature to set
	 */
	public void setHasPayrollNumberFeature(boolean hasPayrollNumberFeature) {
		this.hasPayrollNumberFeature = hasPayrollNumberFeature;
	}

    /**
     * Determines and returns the number of columns required by the 
     * report table that is going to be displayed
     *  
     * @return
     */
	public String getTypeOfPageLayout(){		
		String result = null;
        
        if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == true)) {
            result = FULL_COLUMNS;
        } else if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == false)) {
            result = HIDE_PAYROLL;  
        } else if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == true)) {
            result = HIDE_DIVISION; 
        } else if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == false)) {
            result = HIDE_BOTH;
        } 
        	
        return result;
	}	

	 /* Gets the numberOfDisplayColumns
	 * @return Returns a int
	 */
	public int getNumberOfDisplayColumns() {
		int optOut = 0;
		if (this.isEZstartOn()==false) optOut = -2; // don't show it and the divider column
		
        if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == true)&& (this.showDM == true)){
            return NUMBER_FULL_COLUMNS_WITH_WEB +optOut;  
        } else if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == false)&& (this.showDM == true)) {
            return NUMBER_HIDE_ONE_COLUMN_WITH_WEB +optOut;
        } else if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == true)&& (this.showDM == true)) {
            return NUMBER_HIDE_ONE_COLUMN_WITH_WEB +optOut;
        } else if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == true)&& (this.showDM == false)) {
            return NUMBER_HIDE_TWO_COLUMN_WITH_WEB +optOut;
        } else if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == false)&& (this.showDM == true)) {
            return NUMBER_HIDE_TWO_COLUMN_WITH_WEB +optOut;
        } else if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == false)&& (this.showDM == false)) {
            return NUMBER_HIDE_THREE_COLUMN_WITH_WEB +optOut;
        } else if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == true)&& (this.showDM == false)) {
            return NUMBER_HIDE_THREE_COLUMN_WITH_WEB +optOut;
        } else if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == false)&& (this.showDM == false)) {
            return NUMBER_HIDE_FOUR_COLUMN_WITH_WEB +optOut;
        } else {
            // unreacheable
            return 0;
        }
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
     * 
     * @return
     */
	public String getSegment() {
        return segment;
    }

    /**
     * 
     * @param segment
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    /**
	 * Gets the isSearch
	 * @return Returns a boolean
	 */
	public boolean getIsSearch() {
		return isSearch;
	}

	/**
	 * Sets the isSearch
	 * @param isSearch The isSearch to set
	 */
	public void setIsSearch(boolean isSearch) {
		this.isSearch = isSearch; 
	}

	/**
	 * Gets the isInitialSearch
	 * @return Returns a boolean
	 */
	public boolean getIsInitialSearch() {
		return isInitialSearch;
	}

	/**
	 * Sets the isInitialSearch
	 * @param isInitialSearch The isInitialSearch to set
	 */
	public void setIsInitialSearch(boolean isInitialSearch) {
		this.isInitialSearch = isInitialSearch; 
	}
	
	/**
	 * resets the form
	 */
	public void clear() {
 		hasDivisionFeature = true;
 		hasPayrollNumberFeature = true; 		 
		namePhrase = null;
		ssnOne = null;
		ssnTwo = null;
		ssnThree = null;
        segment = null;
		isInitialSearch = true;
	 	isSearch = false;
	 	empStatus = null;
        theItem = new ArrayList<EmployeeSummaryDetails>();
	}
    
    /**
     * The while construct is used to create objects as necessary in order to return 
     * to the Struts populate method. 
     * 
     * @param index
     * @return
     */
    public EmployeeSummaryDetails getTheItem(int index) {
        while( theItem.size() <= index )
                theItem.add( new EmployeeSummaryDetails() );
            
        return( (EmployeeSummaryDetails) theItem.get( index ) );
        
    }

    /**
     * Setter for the list used to pre-populate the form
     * 
     * @param theItemList
     */
    public void setTheItemList(List<EmployeeSummaryDetails> theItemList) {
        if(theItemList == null) {
            theItemList = new ArrayList<EmployeeSummaryDetails>(); 
        } 
        
        this.theItem = theItemList;
    }
    
    /**
     * Utility method
     */
    public List<EmployeeSummaryDetails> getTheItemList() {
        return this.theItem;
    }
    
    /**
     * Overrides the one from base
     */
    public void reset( HttpServletRequest arg1) {

        //super.reset(arg0, arg1);
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getLastFeedDate() {
        return lastFeedDate;
    }

    public void setLastFeedDate(String lastFeedDate) {
        this.lastFeedDate = lastFeedDate;
    }

    public String getNamePhrase() {
        return namePhrase;
    }

    public void setNamePhrase(String namePhrase) {
        this.namePhrase = namePhrase;
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

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }
    
    /**
     * Form validation
     * 
     * @param errors
     * @return
     */
    public boolean validate(List<ValidationError> errors, EmployeeSummaryDetails element) {           
       // boolean hasNoIndicatorErrors = EmployeeEligibilitySummaryValidationRules.validateEligibilityIndicator(errors, element);        
        //boolean hasNoDateErrors = EmployeeEligibilitySummaryValidationRules.validateEligibilityDate(errors, element, nextPED);        
        boolean hasNoDeferralErrors = EmployeeEligibilitySummaryValidationRules.validateDeferralPct(errors, element);        
        boolean hasNoOptOutErrors = EmployeeEligibilitySummaryValidationRules.validateOptOutIndicator(errors, element);
        
        return hasNoDeferralErrors && hasNoOptOutErrors;
    }
    
    /**
     * Using the form's input value to update the Employee object.
     * 
     * @param employee The Employee object to be updated.
     */
    public void updateEmployee(final Employee employee, 
            final EmployeeSummaryDetails element, final UserProfile user) {
        // need to get the nested object ready for receiving values
        if (employee.getEmployeeDetailVO() == null) {
            employee.setEmployeeDetailVO(new EmployeeDetailVO());
        }
     
        if (employee.getEmployeeVestingVO() == null) {
            employee.setEmployeeVestingVO(new EmployeeVestingVO());
        }
        
        // added for AEE1 - if employee opts out delete dererrals and display info icon
        boolean hasInfo = false;
        if("Y".equalsIgnoreCase(element.getAutoEnrollOptOutInd()) &&
           !"Y".equals(employee.getEmployeeDetailVO().getAutoEnrollOptOutInd())) {
        	
        	if(employee.getEmployeeDetailVO().getBeforeTaxDeferralPct() != null)
        	{
        		if(employee.getEmployeeDetailVO().getBeforeTaxDeferralPct().intValue() > 0)
        			hasInfo = true;
        		employee.getEmployeeDetailVO().setBeforeTaxDeferralPct(null);
        		
        	}
        	if(employee.getEmployeeDetailVO().getBeforeTaxFlatDollarDeferral() != null)
        	{
        		if(employee.getEmployeeDetailVO().getBeforeTaxFlatDollarDeferral().intValue() > 0)
        			hasInfo = true;
        		employee.getEmployeeDetailVO().setBeforeTaxFlatDollarDeferral(null);
        		
        	}
        	if(employee.getEmployeeDetailVO().getDesignatedRothDeferralPct() != null)
        	{
        		if(employee.getEmployeeDetailVO().getDesignatedRothDeferralPct().intValue() > 0)
        			hasInfo = true;
        		employee.getEmployeeDetailVO().setDesignatedRothDeferralPct(null);
           	}
        	
        	if(employee.getEmployeeDetailVO().getDesignatedRothDeferralAmt() != null)
        	{
        		if(employee.getEmployeeDetailVO().getDesignatedRothDeferralAmt().intValue() > 0)
        			hasInfo = true;
        		employee.getEmployeeDetailVO().setDesignatedRothDeferralAmt(null);
          	}
        	
        }
        
        // AEE 1 - if employee opt back in, and 90 day opt out indicator was set, 
        // then set the 90 days withdrawal indicator to space
        if("Y".equalsIgnoreCase(employee.getEmployeeDetailVO().getAutoEnrollOptOutInd()) &&
                !"Y".equals(element.getAutoEnrollOptOutInd()))
        {
        	if (null != employee.getEmployeeDetailVO().getAe90DaysOptOutInd()) {
        		employee.getEmployeeDetailVO().setAe90DaysOptOutInd(" ");
        	}	
        }
        
        if(hasInfo)
        	removedDeferrals.add(element.getProfileId());
        
       // commented out since contribution pct was removed from the eligibility page for ACI1     
       /*  BigDecimal beforeTaxDeferralPct = null;
        
        if(element.getContributionPct()!= null && !"".equals(element.getContributionPct().trim())) {
            try {
                beforeTaxDeferralPct = new BigDecimal(element.getContributionPct());
            } catch (NumberFormatException e) {
                // It is passed validation, but to make it saffer catch all the possibile exceptions
                beforeTaxDeferralPct = null;
            } 
        }                
        
        employee.getEmployeeDetailVO().setBeforeTaxDeferralPct(beforeTaxDeferralPct); */
        
        // Special handling of opt-out indicator (check box)
        if(employee.getEmployeeDetailVO().getAutoEnrollOptOutInd() == null && 
           "N".equalsIgnoreCase(element.getAutoEnrollOptOutInd())) {
            // Do nothing
            // The same treatment in employee snapshot pages
            // The business does not want to see changes from NULL to 'N'
        } else {
            employee.getEmployeeDetailVO().setAutoEnrollOptOutInd(element.getAutoEnrollOptOutInd());
        }
                
        try {
            employee.getEmployeeVestingVO().setEligibilityDate(
                    element.getEligibilityDateAsDate());
        } catch (ParseException e) {
            // It is after validation, it has to be unreacheable
        }
        
        String eligibleToEnroll = element.getEligibleToEnroll();
        if(eligibleToEnroll != null){
            if("Yes".equalsIgnoreCase(element.getEligibleToEnroll())){
        	eligibleToEnroll = "Y";
            }else if("No".equalsIgnoreCase(element.getEligibleToEnroll())){
        	eligibleToEnroll = "N";
            }
        }
        employee.getEmployeeVestingVO().setPlanEligibleInd(eligibleToEnroll != null ? 
        					eligibleToEnroll.trim():eligibleToEnroll); 
        // DM. set language indicator if a valid option is selected from the drop down menu ("SP" or "EN")
        // otherwise, keep what is currently on the database
          if(element.getLanguageInd()!=null&& element.getLanguageInd().length()>0)
        {
        	employee.getEmployeeDetailVO().setLanguageInd(
                element.getLanguageInd()); 
         }
        
        // Set up the created timestamp and created user id if it is null
        Principal principal = user.getPrincipal();

        // Set up the last updated user profile id
        employee.setUserId(Long.toString(principal.getProfileId()));
        employee.setUserIdType(user.isInternalUser() ? UserIdType.UP_INTERNAL
                : UserIdType.UP_EXTERNAL);
        employee.getEmployeeDetailVO().setSourceChannelCode(Constants.PS_APPLICATION_ID);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public Object clone() {
        EmployeeEnrollmentSummaryReportForm newForm = new EmployeeEnrollmentSummaryReportForm();
        
        for (EmployeeSummaryDetails element : theItem) {
            try {
                newForm.getTheItemList().add((EmployeeSummaryDetails)element.clone());
            } catch (CloneNotSupportedException e) {
                // Never reacheable
                e.printStackTrace();
            }
        }
        
        return newForm;
    }

    public String getCurrentDate() {
        return DateRender.formatByPattern(new Date(), "", RenderConstants.MEDIUM_MDY_SLASHED);
    }

  
    public Date getNextOptOut() {
        return nextOptOut;
    }

    public void setNextOptOut(Date nextOptOut) {
        this.nextOptOut = nextOptOut;
    }

    public Date getNextPED() {
        return nextPED;
    }

    public void setNextPED(Date nextPED) {
        this.nextPED = nextPED;
    }

    public boolean isAllowedToEdit() {
        return allowedToEdit;
    }    
    
    public void setAllowedToEdit(boolean allowedToEdit) {
        this.allowedToEdit = allowedToEdit;
    }

    public boolean isShowOptOutReport() {
        return showOptOutReport;
    }

    public void setShowOptOutReport(boolean showOptOutReport) {
        this.showOptOutReport = showOptOutReport;
    }

    public boolean isAllowedViewEnrollmentStats() {
        return allowedViewEnrollmentStats && this.isEZstartOn() && this.isEligibiltyCalcOn();
    }

    public void setAllowedViewEnrollmentStats(boolean allowedViewEnrollmentStats) {
        this.allowedViewEnrollmentStats = allowedViewEnrollmentStats;
    }

    public boolean isAutoEnrollmentEnabled() {
        return autoEnrollmentEnabled;
    }

    public void setAutoEnrollmentEnabled(boolean autoEnrollmentEnabled) {
        this.autoEnrollmentEnabled = autoEnrollmentEnabled;
    }

    public boolean isAllowedToDownloadCensus() {
        return allowedToDownloadCensus;
    }

    public void setAllowedToDownloadCensus(boolean allowedToDownloadCensus) {
        this.allowedToDownloadCensus = allowedToDownloadCensus;
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

    public void setAllowedToDownload(boolean setAllowedToDownload) {
        this.allowedToDownload = setAllowedToDownload;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getOOD() {
        return oOD;
    }

    public void setOOD(int ood) {
        oOD = ood;
    }

    public boolean isAllowedToAccessVestingTab() {
        return allowedToAccessVestingTab;
    }

    public void setAllowedToAccessVestingTab(boolean allowedToAccessVestingTab) {
        this.allowedToAccessVestingTab = allowedToAccessVestingTab;
    }

 	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	public boolean isAllowedToAccessDeferralTab() {
		return allowedToAccessDeferralTab;
	}

	public void setAllowedToAccessDeferralTab(boolean allowedToAccessDeferralTab) {
		this.allowedToAccessDeferralTab = allowedToAccessDeferralTab;
	}

	public boolean hasInfo(String profileId)
	{
		if(removedDeferrals.contains(profileId))
			return true;
		else
			return false;
	}

	public boolean isEZstartOn() {
		return isEZstartOn;
	}

	public void setEZstartOn(boolean isEZstartOn) {
		this.isEZstartOn = isEZstartOn;
	}

	public String getEnrolledFrom() {
		return enrolledFrom;
	}

	public void setEnrolledFrom(String enrolledFrom) {
		this.enrolledFrom = enrolledFrom;
	}

	public String getEnrolledTo() {
		return enrolledTo;
	}

	public void setEnrolledTo(String enrolledTo) {
		this.enrolledTo = enrolledTo;
	}

	public void setMailingDate(String mailingDate) {
		this.mailingDate = mailingDate;
	}

	public String getMailingDate() {
		return mailingDate;
	}

	private void setLanguageInd(String languageInd) {
		this.languageInd = languageInd;
	}

	private String getLanguageInd() {
		return languageInd;
	}


	public void setShowDM(boolean showDM) {
		this.showDM = showDM;
	}


	public boolean isShowDM() {
		return showDM;
	}
	 // Added For EC project. -- START --
	public boolean isShowEligibilitySection() {
		return showEligibilitySection;
	}

	public void setShowEligibilitySection(boolean showEligibilitySection) {
		this.showEligibilitySection = showEligibilitySection;
	}	
  

	public boolean isEligibiltyCalcOn() {
		return isEligibiltyCalcOn;
	}

	public void setEligibiltyCalcOn(boolean isEligibiltyCalcOn) {
		this.isEligibiltyCalcOn = isEligibiltyCalcOn;
	}

	public void setFromPED(String fromPED) {
		this.fromPED = fromPED;
	}

	public void setToPED(String toPED) {
		this.toPED = toPED;
	}
	

	public String getFromPED() {
		return fromPED;
	}

	public String getToPED() {
		return toPED;
	}
	 // Added For EC project. -- START --

	public boolean isPendingEligibilityCalculationRequest() {
		return pendingEligibilityCalculationRequest;
	}

	public void setPendingEligibilityCalculationRequest(
			boolean pendingEligibilityCalculationRequest) {
		this.pendingEligibilityCalculationRequest = pendingEligibilityCalculationRequest;
	}

	public boolean isShowCalculateButton() {
		return showCalculateButton;
	}

	public void setShowCalculateButton(boolean showCalculateButton) {
		this.showCalculateButton = showCalculateButton;
	}

	public List<LabelValueBean> getMoneyTypes() {
		return moneyTypes;
	}

	public void setMoneyTypes(List<LabelValueBean> moneyTypes) {
		this.moneyTypes = moneyTypes;
	}

	public String getMoneyTypeSelected() {
		return moneyTypeSelected;
	}

	public void setMoneyTypeSelected(String moneyTypeSelected) {
		this.moneyTypeSelected = moneyTypeSelected;
	}

	public boolean isShowSaveAndCancelButtons() {
		return showSaveAndCancelButtons;
	}

	public void setShowSaveAndCancelButtons(boolean showSaveAndCancelButtons) {
		this.showSaveAndCancelButtons = showSaveAndCancelButtons;
	}

	public boolean isFromCalculateButton() {
	    return fromCalculateButton;
	}

	public void setFromCalculateButton(boolean fromCalculateButton) {
	    this.fromCalculateButton = fromCalculateButton;
	}

	public String getEedefShortName() {
	    return eedefShortName;
	}

	public void setEedefShortName(String eedefShortName) {
	    this.eedefShortName = eedefShortName;
	}

	
}

