package com.manulife.pension.ps.web.census;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;


import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.DeferralReportValidationRules;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportActionCloneableForm;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Form that works in tandem with <code>DeferralReportAction</code>
 *
 * @author patuadr
 *
 */
public class DeferralReportForm extends ReportActionCloneableForm implements AllowedToEdit {
    private static final long serialVersionUID = 3807168635095804152L;
    public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_LAST_NAME = "namePhrase";
    public static final String FIELD_SEGMENT = "segment";
    public static final String FIELD_DIVISION = "division";
    private static final DecimalFormat PERCENTAGE_FORMATTER = new DecimalFormat("##0");
	private static final DecimalFormat DOLLAR_FORMATTER = new DecimalFormat("##,###.##");
	public static final char PLAN_EMP_DEF_ELEC_CODE_EACH_PAYROLL = 'P';
	public static final char PLAN_EMP_DEF_ELEC_CODE_EVERY_MONTH = 'M';
	public static final char PLAN_EMP_DEF_ELEC_CODE_EACH_QUATER = 'Q';
	public static final char PLAN_EMP_DEF_ELEC_CODE_FIRST_SEVENTH_OF_PLAN_YEAR = 'S';
	public static final char PLAN_EMP_DEF_ELEC_CODE_FIRST_OF_PLAN_YEAR = 'Y';
	public static final char PLAN_EMP_DEF_ELEC_CODE_OTHERS = 'O';
	public static final String SHORT_YMD_DASHED = "yyyy-MM-dd";
	// dniEditStates
	public static final int DNI_EDIT_STATE_YEAR_ONLY = 1; // change year only
	public static final int DNI_EDIT_STATE_FREE_FORM = 2; // change full date(entry box)
	public static final int DNI_EDIT_STATE_MULTI_SELECT = 3; // combo box for month/day and another for year.

    private boolean ACIOff;
    protected String profileId;
    private String segment = null;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
    private String employmentStatus = null;
    private String enrollmentStatus = null;
    private List employmentStatusList = new ArrayList();
    private List enrollmentStatusList = new ArrayList();
    private String namePhrase = null;
    private String todayDate;
    private String division;
    private List segmentList = new ArrayList();
    private List statusList = new ArrayList();

    private boolean allowedToEdit;
    private boolean allowedToDownload;
    private boolean autoEnrollmentEnabled;
    private boolean allowedToDownloadCensus;
    private boolean hasDivisionFeature = true;
    private boolean hasPayrollNumberFeature = true;
    private String aciDefaultDeferralLimitByAmount;
    private String aciDefaultDeferralLimitByPercent;
    private String aciDefaultDeferralIncreaseByAmount;
    private String aciDefaultDeferralIncreaseByPercent;
    private String aciDefaultAnniversaryDate;
    private String aciSignupMethod;
    private int optOutDays;
    private String planDeferralType;
    private String planLimitAmount;
    private String planLimitPercent;
    private boolean initialSearch;
    private boolean loadedOnce;
    private boolean roth;
    private boolean ezStartOn;
    
    private List<DayOfYear> allowedAnniversaryMonthDay;
    private List<String> allowedAnniversaryYear;
    private Date nextAnniversaryDate; 
    private static FastDateFormat EDIT_TOTAL_DATE_FORMAT = ContractDateHelper.getDateFormatter("MM/dd/yyyy");
    private static FastDateFormat EDIT_YEAR_DATE_FORMAT = ContractDateHelper.getDateFormatter("yyyy");
    private static FastDateFormat MONTH_DAY_DATE_FORMAT = ContractDateHelper.getDateFormatter("MM/dd");
    private int dniEditState = -1; // (see above) 3 possible forms, 1 - year only, 2 - free form, 3 - multi select
    
    private boolean allowedToAccessEligibTab;
    private boolean allowedToAccessVestingTab;
    
    private boolean termsAndConditionsAccepted;
    
    private PlanEmployeeDeferralElection planEmpDefElection;
    private DayOfYear planYearEnd;
    private DayOfYear planAciAnnualApplyDate;
    private BigDecimal catchupContriAmt;
	/**
     * A list of <code>DeferralDetails</code> beans that contain data to be saved
     * The data for display comes from the report and later the data is captured in this collection
     * part of the Form
     *
     */
    public List<DeferralDetails> theItem = new ArrayList<DeferralDetails>();
    public List<DeferralDetails> theItem1 = new ArrayList<DeferralDetails>();


	public List<DeferralDetails> getTheItem1() {
		return theItem;
	}
	public void setTheItem1(List<DeferralDetails> theItem) {
		this.theItem = theItem;
	}
	/**
	 * Constructor for ParticipantEnrollmentSummaryReportForm
	 */
	public DeferralReportForm() {
		super();
        initialSearch = true;
        loadedOnce = false;
	}
	public synchronized String formatPercentageFormatter(String value) throws ParseException {
        return PERCENTAGE_FORMATTER.parse(value).toString();
    }
	
	public synchronized String decimalFormatter(String value) throws ParseException {
        return DOLLAR_FORMATTER.parse(value).toString();
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
	public static boolean isOptOutPeriod(String annivDateasString,int optOutDays){
		Date annivDate=null;
		try {
			if(annivDateasString!=null)
			annivDate = new SimpleDateFormat("MM/dd/yyyy").parse(annivDateasString);
		} catch (ParseException e) {
			return false;
		}
		//this.setOptOutDays(String.valueOf(optOutDays));
		if (annivDate != null && optOutDays != 0){
			Calendar currentDate = Calendar.getInstance();
			clearTimeOnCalendar(currentDate);
			
			Calendar annivBeginDate = Calendar.getInstance();
			annivBeginDate.setTime(annivDate);
			clearTimeOnCalendar(annivBeginDate);
			annivBeginDate.add(Calendar.DATE, -optOutDays);
			//this.setOptOutDate(annivBeginDate.getTime());
			
			Calendar annivEndDate = Calendar.getInstance();
			annivEndDate.setTime(annivDate) ;
			clearTimeOnCalendar(annivEndDate);
			
			if ( currentDate.compareTo(annivEndDate) == 0 || 
					(currentDate.compareTo(annivBeginDate) > 0 && currentDate.compareTo(annivEndDate) < 0)){
				return true;
			} else {
				return false;
			}
		}		
		return false;
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
	 * resets the form
	 */
	public void clear() {
        ACIOff = true;
		namePhrase = null;
		ssnOne = null;
		ssnTwo = null;
		ssnThree = null;
        segment = null;
        theItem = new ArrayList<DeferralDetails>();
	}

    /**
     * The while construct is used to create objects as necessary in order to return
     * to the Struts populate method.
     *
     * @param index
     * @return
     */
    public DeferralDetails getTheItem(int index) {
        while( theItem.size() <= index )
                theItem.add( new DeferralDetails() );

        return( (DeferralDetails) theItem.get( index ) );

    }

    /**
     * Setter for the list used to pre-populate the form
     *
     * @param theItemList
     */
    public void setTheItemList(List<DeferralDetails> theItemList) {
        if(theItemList == null) {
            theItemList = new ArrayList<DeferralDetails>();
        }

        this.theItem = theItemList;
    }

    /**
     * Utility method
     */
    public List<DeferralDetails> getTheItemList() {
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

    public String getNamePhrase() {
//    	if (namePhrase == null || namePhrase.length() == 0 ) 
//    		return "M"; // FIXME: hack to speedup page; return namePhrase;
    	return namePhrase;
    }

    public void setNamePhrase(String namePhrase) {
        this.namePhrase = namePhrase;
    }
    
    public DayOfYear getPlanAciAnnualApplyDate() {
		return planAciAnnualApplyDate;
	}

	public void setPlanAciAnnualApplyDate(DayOfYear planAciAnnualApplyDate) {
		this.planAciAnnualApplyDate = planAciAnnualApplyDate;
	}
	
    public PlanEmployeeDeferralElection getPlanEmpDefElection() {
		return planEmpDefElection;
	}

	public void setPlanEmpDefElection(PlanEmployeeDeferralElection planEmpDefElection) {
		this.planEmpDefElection = planEmpDefElection;
	}
	
	public BigDecimal getCatchupContriAmt() {
		return catchupContriAmt;
	}

	public void setCatchupContriAmt(BigDecimal catchupContriAmt) {
		this.catchupContriAmt = catchupContriAmt;
	}
	
    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String status) {
        this.employmentStatus = status;
    }

    public List getEmploymentStatusList() {
        return employmentStatusList;
    }

    public void setEmploymentStatusList(List statusList) {
        this.employmentStatusList = statusList;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    /**
     * Form validation, note element values are also updated in the validations.
     *
     */
    public boolean validate(List<ValidationError> errors, DeferralDetails element) {
        boolean nextADIErrors = DeferralReportValidationRules.validateNextADI(errors, element, this);
        boolean nextADIYearErrors = DeferralReportValidationRules.validateNextADIYear(
                errors, element,
                getOptOutDays(),
                DeferralUtils.getDateNextADIFromClonedElement(element.getProfileId(), this));
        boolean increaseErrors = DeferralReportValidationRules.validateIncrease(errors, element, this);
        boolean limitErrors = DeferralReportValidationRules.validateLimit(errors, element, this);

        return nextADIErrors && nextADIYearErrors && increaseErrors && limitErrors;
    }

    /**
     * Using the form's input value to update the Employee object.
     *
     * @param employee The Employee object to be updated.
     */
    public void updateEmployee(final Employee employee,
            final DeferralDetails element,
            final DeferralDetails initialElement,
            final UserProfile user) {
    	boolean aciSettingsUpdated = false;
    	
        // need to get the nested object ready for receiving values
        if (employee.getEmployeeDetailVO() == null) {
            employee.setEmployeeDetailVO(new EmployeeDetailVO());
        }

        if (employee.getEmployeeVestingVO() == null) {
            employee.setEmployeeVestingVO(new EmployeeVestingVO());
        }

        BigDecimal increasePercent = null;
        BigDecimal increaseAmount = null;
        BigDecimal limitPercent = null;
        BigDecimal limitAmount = null;
        
        if(fieldHasChanged(initialElement.getAciSettingsInd(),element.getAciSettingsInd()) && "N".equals(element.getAciSettingsInd()))
        {
        	// if ACI was turned off - set the personalized limit and increase to null - CIS144
        	 employee.getEmployeeDetailVO().setDeferralIncreasePct(increasePercent);
             employee.getEmployeeDetailVO().setDeferralMaxLimitPct(limitPercent);
             employee.getEmployeeDetailVO().setDeferralIncreaseAmt(increaseAmount);
             employee.getEmployeeDetailVO().setDeferralMaxLimitAmt(limitAmount);
             employee.getEmployeeDetailVO().setAciAnniversaryDate(null);
             aciSettingsUpdated = true;
        }

        if(Constants.DEFERRAL_TYPE_DOLLAR.equals(element.getIncreaseType())) {
            if(element.getIncrease()!= null && !"".equals(element.getIncrease().trim())) {
                try {
                    increaseAmount = new BigDecimal(parseAmount(element.getIncrease()));
                } catch (NumberFormatException e) {
                    // It is passed validation, but to make it saffer catch all the possibile exceptions
                    increaseAmount = null;
                }
            }

            if(element.getLimit()!= null && !"".equals(element.getLimit().trim())) {
                try {
                    limitAmount = new BigDecimal(parseAmount(element.getLimit()));
                } catch (NumberFormatException e) {
                    // It is passed validation, but to make it saffer catch all the possibile exceptions
                    limitAmount = null;
                }
            }
            if(fieldHasChanged(initialElement.getIncreaseAmt(), increaseAmount)) {
                employee.getEmployeeDetailVO().setDeferralIncreaseAmt(increaseAmount);
                aciSettingsUpdated = true;
            }
            if(fieldHasChanged(initialElement.getLimitAmt(), limitAmount)) {
                employee.getEmployeeDetailVO().setDeferralMaxLimitAmt(limitAmount);
                aciSettingsUpdated = true;
            }
            
            // if type = $ clear the % values for the participant
            employee.getEmployeeDetailVO().setDeferralIncreasePct(increasePercent);
            employee.getEmployeeDetailVO().setDeferralMaxLimitPct(limitPercent);
            
        } else if(Constants.DEFERRAL_TYPE_PERCENT.equals(element.getIncreaseType())) {
            if(element.getIncrease()!= null && !"".equals(element.getIncrease().trim())) {
                try {
                    increasePercent = new BigDecimal(element.getIncrease());
                } catch (NumberFormatException e) {
                    // It is passed validation, but to make it saffer catch all the possibile exceptions
                    increasePercent = null;
                }
            }

            if(element.getLimit()!= null && !"".equals(element.getLimit().trim())) {
                try {
                    limitPercent = new BigDecimal(element.getLimit());
                } catch (NumberFormatException e) {
                    // It is passed validation, but to make it saffer catch all the possibile exceptions
                    limitPercent = null;
                }
            }
            if(fieldHasChanged(initialElement.getIncreasePct(), increasePercent)) {
                employee.getEmployeeDetailVO().setDeferralIncreasePct(increasePercent);
                aciSettingsUpdated = true;
            }
            if(fieldHasChanged(initialElement.getLimitPct(), limitPercent)) {
                employee.getEmployeeDetailVO().setDeferralMaxLimitPct(limitPercent);
                aciSettingsUpdated = true;
            }
            
            // if type = % clear the $ values for the participant
            employee.getEmployeeDetailVO().setDeferralIncreaseAmt(increaseAmount);
            employee.getEmployeeDetailVO().setDeferralMaxLimitAmt(limitAmount);

        }

        if(!"N".equals(element.getAciSettingsInd()) && fieldHasChanged(initialElement.getDateNextADI(), element.getDateNextADI())) {
            employee.getEmployeeDetailVO().setAciAnniversaryDate(element.getDateNextADI());
            aciSettingsUpdated = true;
        }
        if(fieldHasChanged(initialElement.getAciSettingsInd(), element.getAciSettingsInd())) {
            employee.getEmployeeDetailVO().setAciSettingInd(element.getAciSettingsInd());
            aciSettingsUpdated = true;
        }

        /*
         * If Increase amount or Limit dollar or percent fields are available and
         * the employee ACI override indicator is Yes then change the override indicator = No, when ACI 
         * has been updated.
         */
        if(aciSettingsUpdated)
        {
	        if(increasePercent != null || increaseAmount != null ||
	                limitAmount != null || limitPercent != null) {
	            if(employee.getEmployeeDetailVO().getPsACIOverrideInd() != null &&
	               Constants.YES.equalsIgnoreCase(employee.getEmployeeDetailVO().getPsACIOverrideInd().trim())) 
	            		employee.getEmployeeDetailVO().setPsACIOverrideInd(Constants.NO);
	            
	        }else{
	        	// if all are null and the override indicator is No change it to Yes
	           if(employee.getEmployeeDetailVO().getPsACIOverrideInd() != null &&
	             Constants.NO.equalsIgnoreCase(employee.getEmployeeDetailVO().getPsACIOverrideInd().trim())) 
	             employee.getEmployeeDetailVO().setPsACIOverrideInd(Constants.YES);
	        }
        }

        // Set up the created timestamp and created user id if it is null
        Principal principal = user.getPrincipal();

        // Set up the last updated user profile id
        employee.setUserId(Long.toString(principal.getProfileId()));
        employee.setUserIdType(user.isInternalUser() ? UserIdType.UP_INTERNAL: UserIdType.UP_EXTERNAL);
        employee.getEmployeeDetailVO().setSourceChannelCode(Constants.PS_APPLICATION_ID);
        
        if (aciSettingsUpdated) {
        	Timestamp now = new Timestamp(System.currentTimeMillis());
        	employee.getEmployeeDetailVO().setAciSettingLastUpdatedTS(now);
        }
    }

    
    private boolean fieldHasChanged(String initialValue, BigDecimal currentValue) {
    	if ((initialValue == null) && (currentValue ==null)) return false;
    	if ((initialValue != null) && (currentValue ==null)) return true;
    	if ((initialValue == null) && (currentValue !=null)) return true;
    	
        return currentValue.doubleValue() !=  Double.valueOf(parseAmount(initialValue));
    }
    
    private static String parseAmount(String amount) throws NumberFormatException
	{
		String amt = amount.trim();
		int index = amt.indexOf(',');
		if(index != -1)
		{
			if(index < 1 || index > 2)
				return null;
			else
				amt = amt.substring(0,index) + amt.substring(index+1);
		}
		return amt;
  	}
    
    /**
     * Checks if the field is different than before
     * Empty strings are treated as NULL values
     *
     * @param initialValue
     * @param currentValue
     * @return
     */
    private boolean fieldHasChanged(String initialValue, String currentValue) {
        if (initialValue == currentValue) {
            return false;
        }

        if (initialValue == null) {
            if (currentValue != null) {
                return true;
            }
        } else  if ("".equals(initialValue.trim())) {
            if(currentValue != null) {
                if( !"".equals(currentValue.trim())) {
                    return true;
                }
            }
        } else if (!initialValue.trim().equals(currentValue.trim())) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the field is different than before
     * Empty strings are treated as NULL values
     *
     * @param initialValue
     * @param currentValue
     * @return
     */
    private boolean fieldHasChanged(Date initialValue, Date currentValue) {
        if (initialValue == currentValue) {
            return false;
        }

        if (initialValue == null) {
            if (currentValue != null) {
                return true;
            }
        } else if(!matchingDate(initialValue, currentValue)) {
            return true;
        }

        return false;
    }

    /**
     * Takes into account just valid dates and sets all time fields to 0 before comparison
     *
     * @param currentValue
     * @return
     */
    private boolean matchingDate(Date initialValue, Date currentValue) {
    	if(currentValue == null){
    		return false;
    	}
        Calendar cal = new GregorianCalendar();
        Calendar otherCal = new GregorianCalendar();

        cal.setTime(initialValue);
        otherCal.setTime(currentValue);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        otherCal.set(Calendar.HOUR_OF_DAY, 0);
        otherCal.set(Calendar.MINUTE, 0);
        otherCal.set(Calendar.SECOND, 0);
        otherCal.set(Calendar.MILLISECOND, 0);

        return cal.equals(otherCal);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public Object clone() {
        DeferralReportForm newForm = new DeferralReportForm();

        for (DeferralDetails element : theItem) {
            try {
                newForm.getTheItemList().add((DeferralDetails)element.clone());
            } catch (CloneNotSupportedException e) {
                // Never reacheable
                e.printStackTrace();
            }
        }

        return newForm;
    }
    
    /**
     * Derived fields are calculated and copied over to the list that is going to be cloned
     *
     */
    public void applyBusinessRulesForDerivedData() {
        //DecimalFormat df = new DecimalFormat("###.###");
        
        for (DeferralDetails element : theItem) {
        	if(!element.hasErrors()){
	            element.setAutoIncreaseChanged(
	                    DeferralUtils.isAutoIncreaseChanged(this, element));
	            
	                if(getPlanDeferralType() == null ||
	                    Constants.DEFERRAL_TYPE_EITHER.equalsIgnoreCase(getPlanDeferralType())) {
	                if(element.getLimitAmt() != null || element.getIncreaseAmt() != null) {
	                    element.setIncreaseType(Constants.DEFERRAL_TYPE_DOLLAR);
	                } else if(element.getLimitPct() != null || element.getIncreasePct() != null) {
	                    element.setIncreaseType(Constants.DEFERRAL_TYPE_PERCENT);
	                } else if(element.hasAlert() &&
	                        (element.getAdHoc401kDeferralChangeReqAmt() != null ||
	                                element.getAdHoc401kDeferralChangeReqPct() != null)) {
	                    // if most recently created ad-hoc 401k deferral change req is outstanding
	                    if(element.getAdHoc401kDeferralChangeReqAmt() != null) {
	                        element.setIncreaseType(Constants.DEFERRAL_TYPE_DOLLAR);
	                    } else {
	                        element.setIncreaseType(Constants.DEFERRAL_TYPE_PERCENT);
	                    }
	                } else if(element.getBeforeTaxDeferralAmt() != null && !"".equals(element.getBeforeTaxDeferralAmt().trim())) {
	                    element.setIncreaseType(Constants.DEFERRAL_TYPE_DOLLAR);
	                } else if(element.getBeforeTaxDeferralPct() != null && !"".equals(element.getBeforeTaxDeferralPct().trim())) {
	                    element.setIncreaseType(Constants.DEFERRAL_TYPE_PERCENT);
	                } else if(element.getDesignatedRothDeferralAmt() != null && !"".equals(element.getDesignatedRothDeferralAmt().trim())) {
	                    element.setIncreaseType(Constants.DEFERRAL_TYPE_DOLLAR);
	                } else if(element.getDesignatedRothDeferralPct() != null && !"".equals(element.getDesignatedRothDeferralPct().trim())) {
	                    element.setIncreaseType(Constants.DEFERRAL_TYPE_PERCENT);
	                } else {
	                    element.setIncreaseType(Constants.DEFERRAL_TYPE_PERCENT);
	                }
	            } else {
	                element.setIncreaseType(getPlanDeferralType());
	            }
	
	            DeferralUtils.calculateAutoIncreaseLimitAlert(element,
	                    aciDefaultDeferralLimitByAmount, aciDefaultDeferralLimitByPercent,
	                    planLimitAmount,planLimitPercent);
	            if(element.getAciSettingsInd() == null) {
	                element.setAciSettingsInd(" ");
	            }
	
	            if(Constants.DEFERRAL_TYPE_DOLLAR.equals(element.getIncreaseType())) {
	                String lim = null;
	                String inc = null;
	                try {
	                    if(element.getLimitAmt()!= null && !"".equals(element.getLimitAmt())) {
	                        lim = decimalFormatter(element.getLimitAmt());
	                    }
	                    if(element.getIncreaseAmt()!= null && !"".equals(element.getIncreaseAmt())) {
	                        inc = decimalFormatter(element.getIncreaseAmt());
	                    }
	                } catch (ParseException e) {
	                    // It should not happen, because it passed validation when it was saved on the first place
	                }
	                element.setLimit(lim);
	                element.setIncrease(inc);
	            } else {
	                String lim = null;
	                String inc = null;
	                try {
	                    if(element.getLimitPct()!= null && !"".equals(element.getLimitPct())) {
	                        lim = formatPercentageFormatter(element.getLimitPct());
	                    }
	                    if(element.getIncreasePct()!= null && !"".equals(element.getIncreasePct())) {
	                        inc = formatPercentageFormatter(element.getIncreasePct());
	                    }
	                } catch (ParseException e) {
	                    // It should not happen, because it passed validation when it was saved on the first place
	                }
	                element.setLimit(lim);
	                element.setIncrease(inc);
	            }
	
	            if(element.getDateNextADI() != null) {
	                Calendar cal = new GregorianCalendar();
	                cal.setTime(element.getDateNextADI());
	                element.setNextADIYear(Integer.toString(cal.get(Calendar.YEAR)));
	            }
	
	            if(element.getDateNextADI() == null || isACIOff()) {
	                element.setDateNextADI(null);
	                element.setNextADIYear(null);
	            } else {
	                Calendar cal = new GregorianCalendar();
	                Calendar currentCal = new GregorianCalendar();
	                currentCal.setTime(element.getDateNextADI());
	                element.setNextADIYear(Integer.toString(currentCal.get(Calendar.YEAR)));
	            }
	            element.setErEeLimitMessage(DeferralUtils.getNextADIMessage(this,element));
        	}
        }   
    }
    
    
    // do stuff in support of ACI2, deferral page Date of next Increase column editing
    public void calculateDNI() throws Exception{ // CIS.376, CIS.378
		calculateNextAnniversaryDate();
		if(this.getPlanEmpDefElection() != null && this.getPlanEmpDefElection().getEmployeeDeferralElectionCode() != null && 
				StringUtils.isNotBlank(this.getPlanEmpDefElection().getEmployeeDeferralElectionCode())){
			if (PLAN_EMP_DEF_ELEC_CODE_EACH_PAYROLL == this.getPlanEmpDefElection().getEmployeeDeferralElectionCode().toCharArray()[0]) {
				this.dniEditState = 2; // edit entire field
			} else {
				this.dniEditState = 3; // restricted date listing
				this.allowedAnniversaryYear = new ArrayList<String>(3);
				int year = GregorianCalendar.getInstance().get(Calendar.YEAR);
				
				this.allowedAnniversaryYear.add(StringUtils.EMPTY+year);
				this.allowedAnniversaryYear.add(StringUtils.EMPTY+(year+1));
				this.allowedAnniversaryYear.add(StringUtils.EMPTY+(year+2));
			}
		}
    }    

	private static Calendar clearTimeOnCalendar(Calendar cal){
		if(cal != null){
			cal.clear(Calendar.HOUR_OF_DAY);
			cal.clear(Calendar.HOUR);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			cal.clear(Calendar.MILLISECOND);
		}
		return cal;
	}
	
	 // CIS.384
	public void calculateNextAnniversaryDate() throws Exception {
		Date contractAnniversaryDate = null;
		if (this.getAciDefaultAnniversaryDate() != null
				&& (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(this.getAciSignupMethod())
				|| ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(this.getAciSignupMethod()))) {

			Calendar cal = Calendar.getInstance();
			clearTimeOnCalendar(cal);
			Date sysDate = cal.getTime();

			int curntMonth = cal.get(Calendar.MONTH) + 1;
			int curntDay = cal.get(Calendar.DAY_OF_MONTH);
			int curntYear = cal.get(Calendar.YEAR);
			
			DayOfYear dayOfYear = this.getPlanAciAnnualApplyDate();
			int planMonth = dayOfYear.getMonth();
			int planDay = dayOfYear.getDay();
			
			if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(this.getAciSignupMethod())) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(SHORT_YMD_DASHED);
				Date conAnnDate = dateFormat.parse(this.getAciDefaultAnniversaryDate());
				cal.set(dateFormat.getCalendar().get(Calendar.YEAR), planMonth -1, planDay);

				if (cal.getTime().compareTo(sysDate) >= 0) {
					cal.setTime(conAnnDate);
				} else if ((cal.getTime().compareTo(sysDate) < 0) && ((planMonth < curntMonth) || ((planMonth == curntMonth) && (planDay < curntDay)))) {
					cal.set(Calendar.YEAR, curntYear + 1);
				} else if ((cal.getTime().compareTo(sysDate) < 0) && ((planMonth > curntMonth) || ((planMonth == curntMonth) && (planDay >= curntDay)))) {
					cal.set(Calendar.YEAR, curntYear);
				}
				contractAnniversaryDate = cal.getTime();
			} else if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(this.getAciSignupMethod())) {
				cal.set(curntYear, planMonth -1, planDay + 1);

				if (cal.getTime().compareTo(sysDate) >= 0) {
					contractAnniversaryDate = cal.getTime();
				} else {
					cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
					contractAnniversaryDate = cal.getTime();
				}
			}
		}
		this.nextAnniversaryDate = contractAnniversaryDate;
	}
    
    public String getCurrentDate() {
        return DateRender.formatByPattern(new Date(), "", RenderConstants.MEDIUM_MDY_SLASHED);
    }

    public boolean isACIOff() {
        return ACIOff;
    }

    public void setACIOff(boolean off) {
        ACIOff = off;
    }

    public boolean isAllowedToEdit() {
        return allowedToEdit;
    }

    public void setAllowedToEdit(boolean allowedToEdit) {
        this.allowedToEdit = allowedToEdit;
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

    public boolean isAllowedToDownload() {
        return allowedToDownload;
    }

    public void setAllowedToDownload(boolean setAllowedToDownload) {
        this.allowedToDownload = setAllowedToDownload;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public List getEnrollmentStatusList() {
        return enrollmentStatusList;
    }

    public void setEnrollmentStatusList(List enrollmentStatusList) {
        this.enrollmentStatusList = enrollmentStatusList;
    }

    public List getStatusList() {
        return statusList;
    }

    public void setStatusList(List statusList) {
        this.statusList = statusList;
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

    public boolean isAutoEnrollmentEnabled() {
        return autoEnrollmentEnabled;
    }

    public void setAutoEnrollmentEnabled(boolean autoEnrollmentEnabled) {
        this.autoEnrollmentEnabled = autoEnrollmentEnabled;
    }

    public String getAciDefaultAnniversaryDate() {
        return aciDefaultAnniversaryDate;
    }

    public void setAciDefaultAnniversaryDate(String aciDefaultAnniversaryDate) {
        this.aciDefaultAnniversaryDate = aciDefaultAnniversaryDate;
    }

    public String getAciDefaultDeferralLimitByAmount() {
        return aciDefaultDeferralLimitByAmount;
    }

    public void setAciDefaultDeferralLimitByAmount(String aciDefaultDeferralLimitByAmount) {
        this.aciDefaultDeferralLimitByAmount = aciDefaultDeferralLimitByAmount;
    }

    public String getAciDefaultDeferralLimitByPercent() {
        return aciDefaultDeferralLimitByPercent;
    }

    public void setAciDefaultDeferralLimitByPercent(String aciDefaultDeferralLimitByPercent) {
        this.aciDefaultDeferralLimitByPercent = aciDefaultDeferralLimitByPercent;
    }

    public String getPlanDeferralType() {
        return planDeferralType;
    }

    public void setPlanDeferralType(String planDeferralType) {
        this.planDeferralType = planDeferralType;
    }

    public String getPlanLimitAmount() {
        return planLimitAmount;
    }

    public void setPlanLimitAmount(String planLimitAmount) {
        this.planLimitAmount = planLimitAmount;
    }

    public String getPlanLimitPercent() {
        return planLimitPercent;
    }

    public void setPlanLimitPercent(String planLimitPercent) {
        this.planLimitPercent = planLimitPercent;
    }

    public String getAciSignupMethod() {
        return aciSignupMethod;
    }

    public void setAciSignupMethod(String aciSignupMethod) {
        this.aciSignupMethod = aciSignupMethod;
    }

    public String getAciDefaultDeferralIncreaseByAmount() {
        return aciDefaultDeferralIncreaseByAmount;
    }

    public void setAciDefaultDeferralIncreaseByAmount(String aciDefaultDeferralIncreaseByAmount) {
        this.aciDefaultDeferralIncreaseByAmount = aciDefaultDeferralIncreaseByAmount;
    }

    public String getAciDefaultDeferralIncreaseByPercent() {
        return aciDefaultDeferralIncreaseByPercent;
    }

    public void setAciDefaultDeferralIncreaseByPercent(String aciDefaultDeferralIncreaseByPercent) {
        this.aciDefaultDeferralIncreaseByPercent = aciDefaultDeferralIncreaseByPercent;
    }

    public int getOptOutDays() {

    	if(this.getPlanEmpDefElection()!= null && this.getPlanEmpDefElection().getEmployeeDeferralElectionCode() != null &&
    			StringUtils.isNotBlank(this.getPlanEmpDefElection().getEmployeeDeferralElectionCode())){
    		return (this.getPlanEmpDefElection()!= null && this.getPlanEmpDefElection().getEmployeeDeferralElectionCode() != null &&
    				PLAN_EMP_DEF_ELEC_CODE_EACH_PAYROLL == this.getPlanEmpDefElection().getEmployeeDeferralElectionCode().trim().toCharArray()[0])? 0 : optOutDays;
    	}else{
    		return optOutDays;
    	}
    	
    }

    public void setOptOutDays(int optOutDays) {
        this.optOutDays = optOutDays;
    }

    public boolean isInitialSearch() {
        return initialSearch;
    }

    public void setInitialSearch(boolean initialSearch) {
        this.initialSearch = initialSearch;
    }

    public boolean hasRoth() {
        return roth;
    }

    public void setRoth(boolean roth) {
        this.roth = roth;
    }

    public boolean isLoadedOnce() {
        return loadedOnce;
    }

    public void setLoadedOnce(boolean loadedOnce) {
        this.loadedOnce = loadedOnce;
    }

	public boolean isAllowedToAccessEligibTab() {
		return allowedToAccessEligibTab;
	}

	public void setAllowedToAccessEligibTab(boolean allowedToAccessEligibTab) {
		this.allowedToAccessEligibTab = allowedToAccessEligibTab;
	}

	public boolean isAllowedToAccessVestingTab() {
		return allowedToAccessVestingTab;
	}

	public void setAllowedToAccessVestingTab(boolean allowedToAccessVestingTab) {
		this.allowedToAccessVestingTab = allowedToAccessVestingTab;
	}

	public boolean isTermsAndConditionsAccepted() {
		return termsAndConditionsAccepted;
	}

	public void setTermsAndConditionsAccepted(boolean termsAndConditionsAccepted) {
		this.termsAndConditionsAccepted = termsAndConditionsAccepted;
	}

	public int getDniEditState() {
		return dniEditState;
	}
    
	public String getNextAnniversaryDateYear() {
		return EDIT_YEAR_DATE_FORMAT.format(this.nextAnniversaryDate);
	}
	
	public String getNextAnniversaryMonthDay() {
		return MONTH_DAY_DATE_FORMAT.format(this.nextAnniversaryDate);
	}
	
	public String getNextAnniversaryDate() {
		return EDIT_TOTAL_DATE_FORMAT.format(this.nextAnniversaryDate);
	}
	
	public Date getNextAnniversaryDateAsDate() {
		return this.nextAnniversaryDate;
	}

	public String getAllowedAnniversaryYear(int i) {
		return this.allowedAnniversaryYear.get(i);
	}
	
	public List<DayOfYear> getAllowedAnniversaryMonthDay() {
		return this.allowedAnniversaryMonthDay;
	}
	
	public void setAllowedAnniversaryMonthDay(List<DayOfYear> allowedAnniversaryMonthDay) {
		this.allowedAnniversaryMonthDay = allowedAnniversaryMonthDay;
	}
	
	public DayOfYear getPlanYearEnd() {
		return planYearEnd;
	}

	public void setPlanYearEnd(DayOfYear planYearEnd) {
		this.planYearEnd = planYearEnd;
	}

	public boolean isEzStartOn() {
		return ezStartOn;
	}

	public void setEzStartOn(boolean ezStartOn) {
		this.ezStartOn = ezStartOn;
	}

}

