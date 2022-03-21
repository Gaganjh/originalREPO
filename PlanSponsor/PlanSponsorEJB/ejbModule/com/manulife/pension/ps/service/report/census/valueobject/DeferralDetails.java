package com.manulife.pension.ps.service.report.census.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;

/**
 * Data Value Object
 * 
 * It adds utilities for data conversion and implementations for clone(), equals() and hashCode() 
 * needed for processing the form 
 * 
 * @author patuadr
 *
 */
public class DeferralDetails implements Details, Serializable, Cloneable {
    private static final long serialVersionUID = -8425072860372721066L;
    
    public static final int OK = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    private int autoIncreaseFlagStatus;
    private int nextADIYearStatus;
    private int typeStatus;
    private int increaseStatus;
    private int limitStatus;
    
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String ssn;
    private Date dateNextADI;
    private String nextADIYear; // year only
    private String nextAD; // holds user free form input from screen field 
    private String nextADIMonthDay;
    private String employmentStatus;
    private String profileId;
    private int participantInd;
    private String division;
    private String enrollmentStatus;
    private String employerDesignatedID;
    private String autoEnrollOptOutInd;
    private String beforeTaxDeferralPct;
    private String beforeTaxDeferralAmt;
    private String designatedRothDeferralPct;
    private String designatedRothDeferralAmt;
    private String increaseAmt;
    private String limitAmt;
    private String increasePct;
    private String limitPct;
    private String increase;
    private String limit;
    private String aciSettingsInd;
    private String autoIncreaseLimitAlert;
    private String increaseType;
    private EmployeeChangeHistoryVO autoIncreaseHistory;
    private EmployeeChangeHistoryVO increasePctHistory;
    private EmployeeChangeHistoryVO increaseAmtHistory;
    private EmployeeChangeHistoryVO limitPctHistory;
    private EmployeeChangeHistoryVO limitAmtHistory;
    private String increaseMouseOver;
    private boolean alert;
    private String adHoc401kDeferralChangeReqAmt;
    private String adHoc401kDeferralChangeReqPct;
    private Date aciReqWaitingApprovalAnniversaryDate;
    private boolean autoIncreaseChanged;
    private List<String> warnings;
    private String erEeLimitMessage;
    private String lastDeferralUpdatedTs;
    private String instructSrcCode;
    private FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("MM/dd/yyyy");
    private FastDateFormat DATE_FORMAT_MONTH_DAY = FastDateFormat.getInstance("MM/dd");
    private String outstandingEziRequest;
    private String participantStatusCode;
    
    
    public String getParticipantStatusCode() {
		return participantStatusCode;
	}


	public void setParticipantStatusCode(String participantStatusCode) {
		this.participantStatusCode = participantStatusCode;
	}


	/**
     * Default ctor
     *
     */
    public DeferralDetails() {
        enrollmentStatus = "";
        autoIncreaseHistory = new EmployeeChangeHistoryVO();
        autoIncreaseHistory.setCurrentUser(autoIncreaseHistory.new ChangeUserInfo());                
        increasePctHistory = new EmployeeChangeHistoryVO();
        increasePctHistory.setCurrentUser(increasePctHistory.new ChangeUserInfo());
        limitPctHistory = new EmployeeChangeHistoryVO();
        limitPctHistory.setCurrentUser(limitPctHistory.new ChangeUserInfo()); 
        increaseAmtHistory = new EmployeeChangeHistoryVO();
        increaseAmtHistory.setCurrentUser(increaseAmtHistory.new ChangeUserInfo());
        limitAmtHistory = new EmployeeChangeHistoryVO();
        limitAmtHistory.setCurrentUser(limitAmtHistory.new ChangeUserInfo());   
        warnings = new ArrayList<String>();
    }


    /**
     * Uses just the fields that can be updated from CensusSummaryDetails
     * 
     * @Override 
     */    
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        final DeferralDetails other = (DeferralDetails) obj;
        
        if (this.getProfileId() == null) {
            if (other.getProfileId() != null)
                return false;
        } else if (!this.getProfileId().equals(other.getProfileId()))
            return false;         
        
        if (this.getAciSettingsInd() == null) {
            if (other.getAciSettingsInd() != null)
                return false;
        } else  if ("".equals(this.getAciSettingsInd().trim())) { 
            if(other.getAciSettingsInd() != null) {
                if( !"".equals(other.getAciSettingsInd().trim())) {
                    return false;
                }
            } 
        } else if (!this.getAciSettingsInd().equals(other.getAciSettingsInd())) // check both ON or OFF
            return false;
        
        if (this.getDateNextADI() == null) {
            if (other.getDateNextADI() != null)
                return false;
        } else if (other.getDateNextADI() == null) {
            return false;
        } else if(!matchingDate(other.getDateNextADI()))
            return false;
        
        if (this.getNextAD() == null) {
        	if (other.getNextAD() != null) return false;
        } else if (this.getNextAD().equals(other.getNextAD()) == false) {
        	return false;
        }

        // data gets set on the page so it is available when user selects to turn on aci, this
        // make it look like a change, when it is not, if they don't turn it on. Thus if it's off
        // just ignore the settings. 
        if ("N".equals(this.getAciSettingsInd())==false) { // already know the 2 are the same(see above)
	        if (this.getNextADIYear() == null) {
	            if (other.getNextADIYear() != null)
	                return false;
	        } else if (other.getNextADIYear() == null) {
	            return false;
	        } else if(!this.getNextADIYear().equals(other.getNextADIYear()))
	            return false;
	        
	        if (this.getNextADIMonthDay() == null) {
	        	if (other.getNextADIMonthDay() != null) return false;
	        } else if (this.getNextADIMonthDay().equals(other.getNextADIMonthDay()) == false) {
	        	return false;
	        }
        }
        
        if (this.getIncreaseType() == null) {
            if (other.getIncreaseType() != null)
                return false;
        } else  if ("".equals(this.getIncreaseType())) { 
            if(other.getIncreaseType() != null) {
                if( !"".equals(other.getIncreaseType())) {
                    return false;
                }
            } 
        } else if (!this.getIncreaseType().equals(other.getIncreaseType()))
            return false;
                
        if (this.getIncreaseAmt() == null) {
            if (other.getIncreaseAmt() != null)
                return false;
        } else  if ("".equals(this.getIncreaseAmt())) { 
            if(other.getIncreaseAmt() != null) {
                if( !"".equals(other.getIncreaseAmt())) {
                    return false;
                }
            } 
        } else if (!this.getIncreaseAmt().equals(other.getIncreaseAmt()))
            return false;
        
        if (this.getIncreasePct() == null) {
            if (other.getIncreasePct() != null)
                return false;
        } else  if ("".equals(this.getIncreasePct())) { 
            if(other.getIncreasePct() != null) {
                if( !"".equals(other.getIncreasePct())) {
                    return false;
                }
            } 
        } else if (!this.getIncreasePct().equals(other.getIncreasePct()))
            return false;
        
        if (this.getLimitAmt() == null) {
            if (other.getLimitAmt() != null)
                return false;
        } else  if ("".equals(this.getLimitAmt())) { 
            if(other.getLimitAmt() != null) {
                if( !"".equals(other.getLimitAmt())) {
                    return false;
                }
            } 
        } else if (!this.getLimitAmt().equals(other.getLimitAmt()))
            return false;
        
        if (this.getLimitPct() == null) {
            if (other.getLimitPct() != null)
                return false;
        } else  if ("".equals(this.getLimitPct())) { 
            if(other.getLimitPct() != null) {
                if( !"".equals(other.getLimitPct())) {
                    return false;
                }
            } 
        } else if (!this.getLimitPct().equals(other.getLimitPct()))
            return false;
        
        if (this.getIncrease() == null) {
            if (other.getIncrease() != null)
                return false;
        } else  if ("".equals(this.getIncrease())) { 
            if(other.getIncrease() != null) {
                if( !"".equals(other.getIncrease())) {
                    return false;
                }
            } 
        } else if (!this.getIncrease().equals(other.getIncrease()))
            return false;

        if (this.getLimit() == null) {
            if (other.getLimit() != null)
                return false;
        } else  if ("".equals(this.getLimit())) { 
            if(other.getLimit() != null) {
                if( !"".equals(other.getLimit())) {
                    return false;
                }
            } 
        } else if (!this.getLimit().equals(other.getLimit()))
            return false;
     
        if (this.getInstructSrcCode() == null) {
        	if (other.getInstructSrcCode() != null) 
        		return false;
        } else if (!this.getInstructSrcCode().equals(other.getInstructSrcCode())) {
        	return false;
        }   
        
        return true;
    }

    /**
     * Takes into account just valid dates and sets all time fields to 0 before comparison 
     * 
     * @param otherNextADI
     * @return
     */
    private boolean matchingDate(Date otherNextADI) {
        Calendar cal = new GregorianCalendar();
        Calendar otherCal = new GregorianCalendar();
        
        cal.setTime(getDateNextADI());
        otherCal.setTime(otherNextADI);
        
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
    
    /**
     * Uses just the fields that can be updated from CensusSummaryDetails
     * 
     * @Override 
     */ 
    public int hashCode() {
        final int PRIME = 391;
        int result = 1;
        
        result = PRIME * result + ((getProfileId() == null) ? 0 : getProfileId().hashCode());
        result = PRIME * result + ((getIncreaseType() == null) ? 0 : getIncreaseType().hashCode());
        result = PRIME * result + ((getDateNextADI() == null) ? 0 : getDateNextADI().hashCode());
        result = PRIME * result + ((getIncreasePct() == null) ? 0 : getIncreasePct().hashCode());
        result = PRIME * result + ((getIncreaseAmt() == null) ? 0 : getIncreaseAmt().hashCode());
        result = PRIME * result + ((getLimitPct() == null) ? 0 : getLimitPct().hashCode());
        result = PRIME * result + ((getLimitAmt() == null) ? 0 : getLimitAmt().hashCode());
        result = PRIME * result + ((getLimit() == null) ? 0 : getLimit().hashCode());
        result = PRIME * result + ((getIncrease() == null) ? 0 : getIncrease().hashCode());
        result = PRIME * result + ((getAciSettingsInd() == null) ? 0 : getAciSettingsInd().hashCode());
        result = PRIME * result + ((getInstructSrcCode() == null) ? 0 : getInstructSrcCode().hashCode());
        result = PRIME * result + ((getNextAD() == null) ? 0 : getNextAD().hashCode());
        if ("Y".equals(getAciSettingsInd())) { // don't consider if off.
        	result = PRIME * result + ((getNextADIMonthDay() == null) ? 0 : getNextADIMonthDay().hashCode());
        	result = PRIME * result + ((getNextADIYear() == null) ? 0 : getNextADIYear().hashCode());
        }
        
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DeferralDetails details = new DeferralDetails();
        details.setProfileId(this.profileId);
        details.setDateNextADI(this.dateNextADI==null?null:new Date(getDateNextADI().getTime()));
        details.setIncreaseType(this.increaseType);
        details.setIncreaseAmt(this.increaseAmt);        
        details.setIncreasePct(this.increasePct);
        details.setLimitAmt(this.limitAmt);
        details.setLimitPct(this.limitPct);
        details.setIncrease(this.increase);   
        details.setLimit(this.limit);
        details.setAciSettingsInd(this.aciSettingsInd);
        details.setNextADIYear(this.nextADIYear);
        details.setNextAD(this.nextAD);
        details.setInstructSrcCode(this.instructSrcCode);
        
        return details;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getSsn() {
        return ssn;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
    
    public boolean isEmploymentStatusActiveOrBlank() {
        if(employmentStatus == null || 
                "A".equalsIgnoreCase(employmentStatus) ||
                "".equals(employmentStatus.trim())) {
            return true;
        } else {
            return false;
        }
    }

    public String getDivision() {
        return division;
    }

    public String getEmployerDesignatedID() {
        return employerDesignatedID;
    }

    public void setEmployerDesignatedID(String employerDesignatedID) {
        this.employerDesignatedID = employerDesignatedID;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDesignatedRothDeferralAmt() {
        return designatedRothDeferralAmt;
    }

    public void setDesignatedRothDeferralAmt(String afterTaxDeferralAmt) {
        this.designatedRothDeferralAmt = afterTaxDeferralAmt;
    }

    public String getDesignatedRothDeferralPct() {
        return designatedRothDeferralPct;
    }

    public void setDesignatedRothDeferralPct(String afterTaxDeferralPct) {
        this.designatedRothDeferralPct = afterTaxDeferralPct;
    }

    public String getBeforeTaxDeferralAmt() {
        return beforeTaxDeferralAmt;
    }

    public void setBeforeTaxDeferralAmt(String beforeTaxDeferralAmt) {
        this.beforeTaxDeferralAmt = beforeTaxDeferralAmt;
    }

    public String getBeforeTaxDeferralPct() {
        return beforeTaxDeferralPct;
    }

    public void setBeforeTaxDeferralPct(String beforeTaxDeferralPct) {
        this.beforeTaxDeferralPct = beforeTaxDeferralPct;
    }

    public String getAutoEnrollOptOutInd() {
        return autoEnrollOptOutInd;
    }

    public void setAutoEnrollOptOutInd(String autoEnrollOptOutInd) {
        this.autoEnrollOptOutInd = autoEnrollOptOutInd;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }
    
    public static class EmployeeComparator implements Comparator<DeferralDetails>, Serializable {
        private static final long serialVersionUID = 3848281624303984832L;

        /**
         * Compare just the names
         */
        public int compare(DeferralDetails esd1, DeferralDetails esd2) {
            String lastName1 = "Z";
            String firstName1 = "Z";
            String middleInitial1= "A";
            String lastName2 = "Z";
            String firstName2 = "Z";
            String middleInitial2 = "A";
            
            int result = 0;
            
            if(esd1.getLastName() != null && esd1.getLastName().length() > 0) {
                lastName1 = esd1.getLastName(); 
            } 
              
            if(esd2.getLastName() != null && esd2.getLastName().length() > 0) {
                lastName2 = esd2.getLastName(); 
            } 
            
            if(esd1.getFirstName() != null && esd1.getFirstName().length() > 0) {
                firstName1 = esd1.getFirstName(); 
            } 
            
            if(esd2.getFirstName() != null && esd2.getFirstName().length() > 0) {
                firstName2 = esd2.getFirstName(); 
            } 
            
            if(esd1.getMiddleInitial() != null && esd1.getMiddleInitial().length() > 0) {
                middleInitial1 = esd1.getMiddleInitial(); 
            } 
            
            if(esd2.getMiddleInitial() != null && esd2.getMiddleInitial().length() > 0) {
                middleInitial2 = esd2.getMiddleInitial(); 
            } 
            
            result = lastName1.compareTo(lastName2);
            
            // Test if the last name is the same
            if(result == 0) {
                result = firstName1.compareTo(firstName2);
                // Test if the first name is the same 
                if(result == 0) {
                    // The result will the comparison of middle initals,
                    // because the last and first names are the same
                    result = middleInitial1.compareTo(middleInitial2);
                }
            } 
                
            return result;
        }
        
    }
    
    /**
     * Utility method that returns the name as expected 
     * to be displayed on the screen
     * 
     * @return
     */
    public String getFullName() {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(processString(getLastName()));
        buffer.append(", ");
        buffer.append(processString(getFirstName()));
        if(getMiddleInitial() != null &&
            !"".equals(getMiddleInitial().trim())) {
            buffer.append(" ");
            buffer.append(processString(getMiddleInitial()));
        }     
        
        return buffer.toString();
    }
    
    /**
     * Utility method that prepares strings to be displayed 
     * 
     * @param field
     * @return
     */
    public static String processString(String field) {
        if(field == null) {
            return "";
        } else {
            return field.trim();
        }
    }

    /**
     * Method exists just for compliance with <code>Details</code> interface 
     */
    public Date getBirthDate() {
        return null;
    }

    /**
     * Method exists just for compliance with <code>Details</code> interface 
     */
    public String getEnrollmentMethod() {
        return null;
    }

    /**
     * Method exists just for compliance with <code>Details</code> interface 
     */
    public Date getEnrollmentProcessedDate() {
        return null;
    }

    /**
     * Method exists just for compliance with <code>Details</code> interface 
     */
    public Date getHireDate() {
        return null;
    }

    /**
     * Method exists just for compliance with <code>Details</code> interface 
     */
    public void setEnrollmentProcessedDate(Date enrollmentProcessedDate) {        
    }

    public Date getDateNextADI() {
        return dateNextADI;
    }

    public void setDateNextADI(Date dateNextADI) {
        this.dateNextADI = dateNextADI;
        if (dateNextADI == null) {
        	this.nextAD = null;
        } else {
        	this.nextAD = DATE_FORMAT.format(dateNextADI);
        	this.nextADIMonthDay = DATE_FORMAT_MONTH_DAY.format(dateNextADI);
        }
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getNextADIYear() {
        return nextADIYear;
    }

    public void setNextADIYear(String nextADIYear) {
        this.nextADIYear = nextADIYear;
    }

    public String getAciSettingsInd() {
        return aciSettingsInd;
    }

    public void setAciSettingsInd(String autoIncreaseFlag) {
        this.aciSettingsInd = autoIncreaseFlag;
    }

    public String getIncreaseType() {
        return increaseType;
    }

    public void setIncreaseType(String increaseType) {
        this.increaseType = increaseType;
    }

    public String getAutoIncreaseLimitAlert() {
        return autoIncreaseLimitAlert;
    }

    public void setAutoIncreaseLimitAlert(String autoIncreaseFlagRoth) {
        this.autoIncreaseLimitAlert = autoIncreaseFlagRoth;
    }
    
    public String getIncreaseAmt() {
        return increaseAmt;
    }

    public void setIncreaseAmt(String increaseAmount) {
        this.increaseAmt = increaseAmount;
    }

    public String getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(String limit) {
        this.limitAmt = limit;
    }

    public String getIncreasePct() {
        return increasePct;
    }

    public void setIncreasePct(String increasePct) {
        this.increasePct = increasePct;
    }

    public String getLimitPct() {
        return limitPct;
    }

    public void setLimitPct(String limitPct) {
        this.limitPct = limitPct;
    }

    public String getIncrease() {
        return increase;
    }

    public void setIncrease(String increase) {
        this.increase = increase;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public EmployeeChangeHistoryVO getAutoIncreaseHistory() {
        return autoIncreaseHistory;
    }

    public void setAutoIncreaseHistory(EmployeeChangeHistoryVO autoIncreaseHistory) {
        this.autoIncreaseHistory = autoIncreaseHistory;
    }

    public EmployeeChangeHistoryVO getIncreasePctHistory() {
        return increasePctHistory;
    }

    public void setIncreasePctHistory(EmployeeChangeHistoryVO increaseHistory) {
        this.increasePctHistory = increaseHistory;
    }

    public EmployeeChangeHistoryVO getLimitPctHistory() {
        return limitPctHistory;
    }

    public void setLimitPctHistory(EmployeeChangeHistoryVO limitHistory) {
        this.limitPctHistory = limitHistory;
    }

    public String getIncreaseMouseOver() {
        return increaseMouseOver;
    }

    public void setIncreaseMouseOver(String increaseMouseOver) {
        this.increaseMouseOver = increaseMouseOver;
    }

    public boolean isParticipantInd() {
        if(participantInd == 1){
            return true;
        } else {
            return false;
        }
    }

    public int getParticipantInd() {
        return participantInd;
    }

    public void setParticipantInd(int participantInd) {
        this.participantInd = participantInd;
    }

    public int getAutoIncreaseFlagStatus() {
        return autoIncreaseFlagStatus;
    }

    public void setAutoIncreaseFlagStatus(int autoIncreaseFlagStatus) {
        this.autoIncreaseFlagStatus = autoIncreaseFlagStatus;
    }

    public int getIncreaseStatus() {
        return increaseStatus;
    }

    public void setIncreaseStatus(int increaseStatus) {
        this.increaseStatus = increaseStatus;
    }

    public int getLimitStatus() {
        return limitStatus;
    }

    public void setLimitStatus(int limitStatus) {
        this.limitStatus = limitStatus;
    }

    public int getNextADIYearStatus() {
        return nextADIYearStatus;
    }

    public void setNextADIYearStatus(int nextADIYearStatus) {
        this.nextADIYearStatus = nextADIYearStatus;
    }

    public int getTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(int typeStatus) {
        this.typeStatus = typeStatus;
    }
    
    
    public String getInstructSrcCode() {
		return instructSrcCode;
	}


	public void setInstructSrcCode(String instructSrcCode) {
		this.instructSrcCode = instructSrcCode;
	}

	// adHoc vs EZi, JH/AC vs CI 
	public boolean hasOutstandingEZiRequest() {
		if("Y".equals(outstandingEziRequest))
			return true;
		else
			return false;
	}

	/**
     * Tests if any error exist
     * 
     * @return
     */
    public boolean hasErrors() {
        if(getAutoIncreaseFlagStatus() == ERROR || 
                getIncreaseStatus() == ERROR ||
                getLimitStatus() == ERROR ||
                getNextADIYearStatus() == ERROR ||
                getTypeStatus() == ERROR) {
            return true;
        } 
        
        return false;
    }

    /**
     * Tests if any warning exist
     * 
     * @return
     */
    public boolean hasWarnings() {
        if(getWarnings() != null && getWarnings().size() > 0) {
            return true;
        } 
        
        return false;
    }

    public boolean hasAlert() {
        return alert;
    }


    public void setAlert(boolean alert) {
        this.alert = alert;
    }


    public EmployeeChangeHistoryVO getIncreaseAmtHistory() {
        return increaseAmtHistory;
    }


    public void setIncreaseAmtHistory(EmployeeChangeHistoryVO increaseAmtHistory) {
        this.increaseAmtHistory = increaseAmtHistory;
    }


    public EmployeeChangeHistoryVO getLimitAmtHistory() {
        return limitAmtHistory;
    }


    public void setLimitAmtHistory(EmployeeChangeHistoryVO limitAmtHistory) {
        this.limitAmtHistory = limitAmtHistory;
    }


    public Date getAciReqWaitingApprovalAnniversaryDate() {
        return aciReqWaitingApprovalAnniversaryDate;
    }


    public void setAciReqWaitingApprovalAnniversaryDate(Date aciReqWaitingApprovalAnniversaryDate) {
        this.aciReqWaitingApprovalAnniversaryDate = aciReqWaitingApprovalAnniversaryDate;
    }


    public String getAdHoc401kDeferralChangeReqAmt() {
        return adHoc401kDeferralChangeReqAmt;
    }


    public void setAdHoc401kDeferralChangeReqAmt(String adHoc401kDeferralChangeReqAmt) {
        this.adHoc401kDeferralChangeReqAmt = adHoc401kDeferralChangeReqAmt;
    }


    public String getAdHoc401kDeferralChangeReqPct() {
        return adHoc401kDeferralChangeReqPct;
    }


    public void setAdHoc401kDeferralChangeReqPct(String adHoc401kDeferralChangeReqPct) {
        this.adHoc401kDeferralChangeReqPct = adHoc401kDeferralChangeReqPct;
    }


    public boolean isAutoIncreaseChanged() {
        return autoIncreaseChanged;
    }


    public void setAutoIncreaseChanged(boolean autoIncreaseChanged) {
        this.autoIncreaseChanged = autoIncreaseChanged;
    }


    public List<String> getWarnings() {
        return warnings;
    }


    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

	public String getErEeLimitMessage() {
		return erEeLimitMessage;
	}

	public void setErEeLimitMessage(String erEeLimitMessage) {
		this.erEeLimitMessage = erEeLimitMessage;
	}

	public String getLastDeferralUpdatedTs() {
		return lastDeferralUpdatedTs;
	}


	public void setLastDeferralUpdatedTs(String lastDeferralUpdatedTs) {
		this.lastDeferralUpdatedTs = lastDeferralUpdatedTs;
	}


	public String getNextAD() {
		return nextAD;
	}

	public void setNextAD(String nextAD) {
		this.nextAD = nextAD;
	}


	public String getNextADIMonthDay() {
		return nextADIMonthDay;
	}


	public void setNextADIMonthDay(String nextADIMonthDay) {
		this.nextADIMonthDay = nextADIMonthDay;
	}


	public String getOutstandingEziRequest() {
		return outstandingEziRequest;
	}


	public void setOutstandingEziRequest(String outstandingEziRequest) {
		this.outstandingEziRequest = outstandingEziRequest;
	}
	
	
	    
}
              
