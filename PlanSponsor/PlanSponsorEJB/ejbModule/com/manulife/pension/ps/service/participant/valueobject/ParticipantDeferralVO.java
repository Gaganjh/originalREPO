package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.util.StaticHelperClass;

// collect data needed for the Participant Account, net EE deferrals tab
public class ParticipantDeferralVO implements Serializable {
	
    public static final String DEFERRAL_TYPE_PERCENT="%";
    public static final String DEFERRAL_TYPE_DOLLAR="$";
    public static final String DEFERRAL_TYPE_EITHER="E";
    
  //DateFormat is converted to FastDateFormat to make it thread safe
    private static final FastDateFormat dateFormat = FastDateFormat.getInstance("MMMMM dd, yyyy");
	private static FastDateFormat aciDBDateFormat = FastDateFormat.getInstance(
			"yyyy-MM-dd", Locale.US);
	
	private Date aciAnniversaryDate;
	
    private Double beforeTaxDeferralAmount;
    private Double beforeTaxDeferralPercent;
    private Double rothPercent;
    private Double rothAmount;
    private Double maxLimitPercent;
    private Double maxLimitAmount;
    private Double increaseAmount;
    private Double increasePercent;
	
	private int outstandingRequests = 0;
	
	private boolean CSFACIValue;
	
	private boolean planACIValue;
	
	private String deferralType;
	
	private int optOutDays;
	
	private String participantACISetting;
	
	private Date dateOfNextIncrease;
	
	private String contractDeferralType;
	
	// If outstanding requests > 0 then POSSIBLY this will hold data
	// Most recent, ad-hoc, before tax values
	private Double pendingBeforeTaxAmount;
	private Double pendingBeforeTaxPercent;
	
	private Date mostRecentACIProcessedDate; // for most recently created only
	private String mostRecentACIProcessedStatusCode;
	private Date mostRecentACIAnniversaryDate;
	
	private String contractDefaultIncreasePercent;
	private String contractDefaultIncreaseAmount;	
	private String contractDefaultLimitPercent;
	private String contractDefaultLimitAmount;
	
	private boolean signUp = false;
	private boolean auto = false;
	private DayOfYear annualApplyDate;
	private DayOfYear planYearEndDate;
	private String contractAciAnniversaryDate;
	
	public ParticipantDeferralVO() {
	}
	
	private void calculateDeferralType()  {
		this.deferralType = internalCalculateDeferralType(this.getContractDeferralType());
	}
	
	// leave with returning string so it can be moved if needed
	private String internalCalculateDeferralType(String planDeferralType)  {
        
        // CIS.181 Census Deferral DFS
        if(planDeferralType == null || 
        		ParticipantDeferralVO.DEFERRAL_TYPE_EITHER.equalsIgnoreCase(planDeferralType)) {
            if(getMaxLimitAmount() != null && getIncreaseAmount() != null) {
                return  ParticipantDeferralVO.DEFERRAL_TYPE_DOLLAR;
            } else if(getMaxLimitPercent() != null && getIncreasePercent() != null) {
                return ParticipantDeferralVO.DEFERRAL_TYPE_PERCENT;
            } else if (getOutstandingRequests() > 0 &&
            		   ((getPendingBeforeTaxAmount() != null &&
            		     getPendingBeforeTaxAmount() != 0.0) ||
            		    (getPendingBeforeTaxPercent() != null &&
            		     getPendingBeforeTaxPercent() != 0.0))) {

                // if most recently created ad-hoc deferral change req is outstanding
            	if (getPendingBeforeTaxAmount() != null &&
            		getPendingBeforeTaxAmount() != 0.0) {
                    return ParticipantDeferralVO.DEFERRAL_TYPE_DOLLAR;
                } else {
                    return ParticipantDeferralVO.DEFERRAL_TYPE_PERCENT;
                }
            } else if(getBeforeTaxDeferralAmount() != null) { 
                return ParticipantDeferralVO.DEFERRAL_TYPE_DOLLAR;
            } else if(getBeforeTaxDeferralPercent() != null) { 
                return ParticipantDeferralVO.DEFERRAL_TYPE_PERCENT;
            } else if(getRothAmount() != null) { 
                return ParticipantDeferralVO.DEFERRAL_TYPE_DOLLAR;
            } else if(getRothPercent() != null) { 
                return ParticipantDeferralVO.DEFERRAL_TYPE_PERCENT;
            } else {
                return ParticipantDeferralVO.DEFERRAL_TYPE_PERCENT;
            }
        } else {
            return planDeferralType; 
        }
	}
	
	
	// PPC.20.1
	private void determineDateOfNextIncrease() {
		Date nextIncreaseDate = null;
		
		if (this.getAciAnniversaryDate() == null) {
			Date nextIncreaseDateIfNoPptAnnivDate = null;
			if (this.isAuto()) {
				// PPC.20.1 
				nextIncreaseDateIfNoPptAnnivDate = getDateOfNextIncreaseForAuto();
			} else if (this.isSignUp()) {
				// PPC.20.2
				nextIncreaseDateIfNoPptAnnivDate = getDateOfNextIncreaseForSignUp();
			}
			// PPC.20.3 & PPC.20.4
			if (ContractServiceFeatureUtil.isFreezePeriod(this.getOptOutDays(), 
					nextIncreaseDateIfNoPptAnnivDate, false)) {
				Calendar dateInQuestionCalendar = new GregorianCalendar();
				dateInQuestionCalendar.setTime(nextIncreaseDateIfNoPptAnnivDate);
				dateInQuestionCalendar.add(Calendar.YEAR, 1);
				nextIncreaseDateIfNoPptAnnivDate = dateInQuestionCalendar.getTime();
			}
			this.setDateOfNextIncrease(nextIncreaseDateIfNoPptAnnivDate);
		} else if (this.getOutstandingRequests() > 0 &&
				   "PA".equals(this.getMostRecentACIProcessedStatusCode())) {
			nextIncreaseDate = this.mostRecentACIAnniversaryDate;
		} else if (onOrAfterToday(this.getAciAnniversaryDate())) {
			nextIncreaseDate = this.getAciAnniversaryDate();
		} else if (lessThanTodayUsingMonthDay(this.getAciAnniversaryDate())) {
			nextIncreaseDate = genNextYearDate(this.getAciAnniversaryDate());
		} else  {
			nextIncreaseDate = genThisYearDate(this.getAciAnniversaryDate());
		}
		
		if (nextIncreaseDate != null) {
			// 20.2 rule
			if (withinOptOutPeriod(null) &&
				(this.getMostRecentACIProcessedStatusCode() != null) &&
				("PA".equals(this.getMostRecentACIProcessedStatusCode())==false) &&  // approve or declined
			    withinOptOutPeriod(this.mostRecentACIProcessedDate)) {
				Calendar dateInQuestionCalendar = new GregorianCalendar();
				dateInQuestionCalendar.setTime(nextIncreaseDate);
				dateInQuestionCalendar.set(Calendar.YEAR, dateInQuestionCalendar.get(Calendar.YEAR)+1);
				
				this.setDateOfNextIncrease(dateInQuestionCalendar.getTime());
			} else {
				this.setDateOfNextIncrease(nextIncreaseDate);
			}
		}	
	}
	
	
	private boolean withinOptOutPeriod(Date theDate) {
		// null means today.
		Calendar anniversaryDate = new GregorianCalendar();
		anniversaryDate.setTime(this.aciAnniversaryDate);
		int anniversaryDay = anniversaryDate.get(Calendar.DAY_OF_YEAR);
		
		Calendar targetDate = null;
		if (theDate == null) { // today
			targetDate = new GregorianCalendar().getInstance();
		} else {
			targetDate = new GregorianCalendar();
			targetDate.setTime(theDate);
		}
		
		int targetDay = targetDate.get(Calendar.DAY_OF_YEAR);
		
		if ((targetDay<=anniversaryDay) &&
			(targetDay>=(anniversaryDay-optOutDays)))
			return true;
		
		return false;
	}
	
	
	private boolean onOrAfterToday(Date theDate) {
		return !theDate.before(new Date(System.currentTimeMillis()));
	}
	
	// is month/day before today
	private boolean lessThanTodayUsingMonthDay(Date theDate) {
		Calendar dateInQuestionCalendar = new GregorianCalendar();
		dateInQuestionCalendar.setTime(theDate);
		
		int todayDayInYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

		return dateInQuestionCalendar.get(Calendar.DAY_OF_YEAR) < todayDayInYear;
	}
	
	
	private Date genNextYearDate(Date theDate) {
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		
		Calendar calendarDate = new GregorianCalendar();
		calendarDate.setTime(theDate);
        calendarDate.set(Calendar.YEAR, thisYear+1);		
        
        return calendarDate.getTime();
	}
	
	
	private Date genThisYearDate(Date theDate) {
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		
		Calendar calendarDate = new GregorianCalendar();
		calendarDate.setTime(theDate);
        calendarDate.set(Calendar.YEAR, thisYear);
        
        return calendarDate.getTime();
	}
	
	
    public void calculateDerrivedFields() {
        calculateDeferralType();
        determineDateOfNextIncrease();
    }
	
	
	// **** Getters and setters
	
	public Date getAciAnniversaryDate() {
		return aciAnniversaryDate;
	}

	public void setAciAnniversaryDate(Date aciAnniversaryDate) {
		this.aciAnniversaryDate = aciAnniversaryDate;
	}
	
	public String getContractAciAnniversaryDate() {
		return contractAciAnniversaryDate;
	}

	public void setContractAciAnniversaryDate(String contractAciAnniversaryDate) {
		this.contractAciAnniversaryDate = contractAciAnniversaryDate;
	}
	
	public DayOfYear getAnnualApplyDate() {
		return annualApplyDate;
	}

	public void setAnnualApplyDate(DayOfYear annualApplyDate) {
		this.annualApplyDate = annualApplyDate;
	}
	
	public DayOfYear getPlanYearEndDate() {
		return planYearEndDate;
	}

	public void setPlanYearEndDate(DayOfYear planYearEndDate) {
		this.planYearEndDate = planYearEndDate;
	}

	public Double getBeforeTaxDeferralAmount() {
		return beforeTaxDeferralAmount;
	}

	public void setBeforeTaxDeferralAmount(Double beforeTaxDeferralAmount) {
		this.beforeTaxDeferralAmount = beforeTaxDeferralAmount;
	}

	public Double getBeforeTaxDeferralPercent() {
		return beforeTaxDeferralPercent;
	}

	public void setBeforeTaxDeferralPercent(Double beforeTaxDeferralPercent) {
		this.beforeTaxDeferralPercent = beforeTaxDeferralPercent;
	}

	public int getOutstandingRequests() {
		return outstandingRequests;
	}

	public void setOutstandingRequests(int outstandingRequests) {
		this.outstandingRequests = outstandingRequests;
	}

	public Double getRothAmount() {
		return rothAmount;
	}

	public void setRothAmount(Double rothAmount) {
		this.rothAmount = rothAmount;
	}

	public Double getRothPercent() {
		return rothPercent;
	}

	public void setRothPercent(Double rothPercent) {
		this.rothPercent = rothPercent;
	}
	
	public Double getMaxLimitAmount() {
		return maxLimitAmount;
	}

	public void setMaxLimitAmount(Double maxLimitAmount) {
		this.maxLimitAmount = maxLimitAmount;
	}

	public Double getMaxLimitPercent() {
		return maxLimitPercent;
	}

	public void setMaxLimitPercent(Double maxLimitPercent) {
		this.maxLimitPercent = maxLimitPercent;
	}

	public Double getIncreaseAmount() {
		return increaseAmount;
	}

	public void setIncreaseAmount(Double increaseAmount) {
		this.increaseAmount = increaseAmount;
	}

	public Double getIncreasePercent() {
		return increasePercent;
	}

	public void setIncreasePercent(Double increasePercent) {
		this.increasePercent = increasePercent;
	}

	public Double getPendingBeforeTaxAmount() {
		return pendingBeforeTaxAmount;
	}

	public void setPendingBeforeTaxAmount(Double pendingBeforeTaxAmount) {
		this.pendingBeforeTaxAmount = pendingBeforeTaxAmount;
	}

	public Double getPendingBeforeTaxPercent() {
		return pendingBeforeTaxPercent;
	}

	public void setPendingBeforeTaxPercent(Double pendingBeforeTaxPercent) {
		this.pendingBeforeTaxPercent = pendingBeforeTaxPercent;
	}
	
	public String getDeferralType() {
		return deferralType;
	}

	public void setDeferralType(String deferralType) {
		this.deferralType = deferralType;
	}
	

	public int getOptOutDays() {
		return optOutDays;
	}

	public void setOptOutDays(int optOutDays) {
		this.optOutDays = optOutDays;
	}

	public boolean getCSFACIValue() {
		return CSFACIValue;
	}

	public void setCSFACIValue(boolean value) {
		CSFACIValue = value;
	}

	public boolean isCSFACIOn() {
		return CSFACIValue;
	}
	
	public boolean getPlanACIValue() {
		return planACIValue;
	}

	public void setPlanACIValue(boolean value) {
		planACIValue = value;
	}

	public boolean isPlanACIOn() {
		return planACIValue;
	}
	
	public boolean isParticipantACIOn() {
		return "Y".equalsIgnoreCase(participantACISetting);
	}
	
	public Date getMostRecentACIAnniversaryDate() {
		return mostRecentACIAnniversaryDate;
	}

	public void setMostRecentACIAnniversaryDate(Date mostRecentACIAnniversaryDate) {
		this.mostRecentACIAnniversaryDate = mostRecentACIAnniversaryDate;
	}

	public Date getMostRecentACIProcessedDate() {
		return mostRecentACIProcessedDate;
	}

	public void setMostRecentACIProcessedDate(Date mostRecentACIProcessedDate) {
		this.mostRecentACIProcessedDate = mostRecentACIProcessedDate;
	}

	public String getMostRecentACIProcessedStatusCode() {
		return mostRecentACIProcessedStatusCode;
	}

	public void setMostRecentACIProcessedStatusCode(
			String mostRecentACIProcessedStatusCode) {
		this.mostRecentACIProcessedStatusCode = mostRecentACIProcessedStatusCode;
	}
	
	public String getParticipantACISetting() {
		return participantACISetting;
	}

	public void setParticipantACISetting(String participantACISetting) {
		this.participantACISetting = participantACISetting;
	}

	public String getContractDeferralType() {
		return contractDeferralType;
	}

	public void setContractDeferralType(String contractDeferralType) {
		this.contractDeferralType = contractDeferralType;
	}

	public Date getDateOfNextIncreaseAsDate() {
		return this.dateOfNextIncrease;
	}
	
	public String getDateOfNextIncrease() {
		if (this.dateOfNextIncrease == null) 
			return "";
		else
			return dateFormat.format(this.dateOfNextIncrease);
	}

	public void setDateOfNextIncrease(Date dateOfNextIncrease) {
		this.dateOfNextIncrease = dateOfNextIncrease;
	}

	
	public String getContractDefaultIncreaseAmount() {
		return contractDefaultIncreaseAmount;
	}

	public void setContractDefaultIncreaseAmount(
			String contractDefaultIncreaseAmount) {
		this.contractDefaultIncreaseAmount = contractDefaultIncreaseAmount;
	}

	public String getContractDefaultIncreasePercent() {
		return contractDefaultIncreasePercent;
	}

	public void setContractDefaultIncreasePercent(
			String contractDefaultIncreasePercent) {
		this.contractDefaultIncreasePercent = contractDefaultIncreasePercent;
	}

	public String getContractDefaultLimitAmount() {
		return contractDefaultLimitAmount;
	}

	public void setContractDefaultLimitAmount(String contractDefaultLimitAmount) {
		this.contractDefaultLimitAmount = contractDefaultLimitAmount;
	}

	public String getContractDefaultLimitPercent() {
		return contractDefaultLimitPercent;
	}

	public void setContractDefaultLimitPercent(String contractDefaultLimitPercent) {
		this.contractDefaultLimitPercent = contractDefaultLimitPercent;
	}

	// support stuff
	public String toString() {
		return StaticHelperClass.toString(this);
	}

	public void setSignUp(boolean signUp) {
		this.signUp = signUp;
	}

	public boolean isSignUp() {
		return signUp;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public boolean isAuto() {
		return auto;
	}	
	
	private Date getDateOfNextIncreaseForAuto() {
		Date nextAnnivDate = null;
		if (this.getContractAciAnniversaryDate() != null) {
			try {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(aciDBDateFormat.parse(this.getContractAciAnniversaryDate()));
				int annivYear = gc.get(Calendar.YEAR);
				Date annivDate = aciDBDateFormat.parse(Integer.toString(annivYear) + "-" +this.getAnnualApplyDate().getMonth()
						+ "-" +this.getAnnualApplyDate().getDay());
				nextAnnivDate = ContractServiceFeatureUtil.getContractNextAnniversary(annivDate);
			} catch(ParseException pe) {
				// do nothing
			}
		}
		return nextAnnivDate;
	}
	
	private Date getDateOfNextIncreaseForSignUp() {
		Date nextAnnivDate = null;
		if (this.getContractAciAnniversaryDate() != null) {
			try {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(aciDBDateFormat.parse(this.getContractAciAnniversaryDate()));
				int currentYear = ContractServiceFeatureUtil.getCurrentYear();
				this.getPlanYearEndDate().incrementDayOfYear(1);
				Date annivDate = aciDBDateFormat.parse(currentYear + "-" +this.getPlanYearEndDate().getMonth()
						+ "-" +this.getPlanYearEndDate().getDay());
				nextAnnivDate = ContractServiceFeatureUtil.getContractNextAnniversary(annivDate);
			} catch(ParseException pe) {
				// do nothing
			}
		}
		return nextAnnivDate;
	}
	
}
