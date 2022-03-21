package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.util.StaticHelperClass;

public class TaskCenterTasksDetails implements Serializable, Cloneable {
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private static FastDateFormat dateFormat = FastDateFormat.getInstance("MMM dd, yyyy"); // RO format
	
	private String name = null; // for web display
	private String lastName; // for download
	private String middleInitial; // for download
	private String firstName; // for download
	private String ssn = null;
	private String division = null;
	private long createdTS;
	private String details;
	private String anniversaryDate;
	private long anniversaryDateRaw; 
	private String profileId;
	private int counter;
	private BigDecimal contribOldAmt;
	private BigDecimal contribOldPct;
	private BigDecimal contribAmt;
	private BigDecimal contribPct;
	private BigDecimal contribIncAmt;
	private BigDecimal contribIncPct;
	private String employeeId; // for download
	private String ssnRaw; // for download
	private String status;
	private int instructionNo; // park of pk
	private boolean isAccountHolder;
	private boolean isRoth;
	private boolean isADHoc;
	private String createdUserIdType; // CREATED_USER_ID_TYPE COLUMN varchar(3)
	private String createdFirstName;
	private String createdLastName;
	
	// support user actions from web page(checkbox)
	private boolean approve = false;
	private boolean decline = false;
	
	// mouse over values
	private String initiatedByInternal; // for internal users
	private String initiatedByExternal; // for external[tpa, ps]
	private String initiatedSource;
	
	// alerts/warnings
	private boolean noDeferralOnFileAlertForAuto = false; 
	private boolean noDeferralOnFileAlertForSignup = false; 
	private boolean multipleOverdueWarningForAuto = false;
	private boolean multipleOverdueWarningForSignup = false;
	private boolean overdueAlert = false;
	private boolean overdueWarningForAuto = false;
	private boolean overdueWarningForSignup = false;
	
	// support error reporting
	private List<String> fieldsInError = null;
	
	// Auto or SignUp
	private String autoOrSignup;
	
	private Date effectiveDate;

	public TaskCenterTasksDetails(
			String profileId,
			String lastName,
			String firstName,
			String middleInitial,
			String ssn,
			String division,
			long created,
			String contribSrcCode,
			BigDecimal contribAmt,
			BigDecimal contribPct,
			BigDecimal contribOldAmt,
			BigDecimal contribOldPct,
			BigDecimal contribIncAmt,
			BigDecimal contribIncPct,
			String moneyTypeCode,
			long anvDate,
			String employeeId,
			String status,
			int instructionNo,
			int counter,
			String participantStatus,
			BigDecimal participantBalance,
			String createdUserIdType,
			String createdFirstName,
			String createdLastName,
			String createdFirstName2,
			String createdLastName2,
			String createdName3,
			String autoOrSignup,
			Date effectiveDate) { 
		
		this.createdUserIdType = createdUserIdType;

		// 3 possible sources supported:
		//  - id was 9 char data from employee_contract
		//  - id was 7 char data from user_profile
		//  - id was 8 char data from client_account_rep (car with no csdb entry)
		if (createdLastName !=null && createdLastName.trim().length() >0) {
			this.createdFirstName = createdFirstName;
			this.createdLastName = createdLastName;
		} else if (createdLastName2 !=null && createdLastName2.trim().length() > 0){
			this.createdFirstName = createdFirstName2;
			this.createdLastName = createdLastName2;			
		} else if (createdName3 !=null) { // last case is car with no csdb entry, name is char 30 all in one field
			this.createdFirstName = null;
			this.createdLastName = createdName3.trim();			
		}
		
		this.profileId = profileId;		
		this.lastName = lastName;
		this.middleInitial = middleInitial;
		this.firstName = firstName;
	    this.contribOldAmt = contribOldAmt;
	    this.contribOldPct = contribOldPct;
	    this.contribAmt = contribAmt;
	    this.contribPct = contribPct;
	    this.contribIncAmt = contribIncAmt;
	    this.contribIncPct = contribIncPct;
	    this.employeeId = (employeeId == null ? "" : employeeId.trim());
	    this.anniversaryDateRaw = anvDate;
	    this.counter = counter;
	    this.status = status;
	    this.instructionNo = instructionNo;
		
		if (middleInitial !=null && middleInitial.trim().length()>0) {
			this.name = lastName.trim()+", "+firstName.trim()+" "+middleInitial.trim()+".";
		} else {
			this.name = lastName.trim()+", "+firstName.trim()+".";
		}
	
		this.ssnRaw = ssn;
		this.ssn = encodeSSN(ssn);
		this.division = encodeDivision(division);		
		this.isADHoc = encodeIsAdHoc(contribSrcCode);
	    this.createdTS = created;
	    this.anniversaryDate = encodeDate(anvDate);
	    
	    this.details = encodeDetailType(moneyTypeCode) + 
	                   encodeDetailsRequested(contribAmt, contribPct, contribOldAmt, contribOldPct, 
	    		                              contribIncAmt, contribIncPct);
	    
	    this.autoOrSignup = autoOrSignup;
	    
	    if (isADHocRequest() == false) evaluateAlertWarnings();
	    
	    this.isAccountHolder = ("TT".equalsIgnoreCase(participantStatus) == false) ||
	                           (participantBalance != null && participantBalance.doubleValue() > 0.0);
	    
	    
	    this.evaluateInitiatedBy();
	    this.effectiveDate = effectiveDate; 
	}

	
	// Note this logic is duplicated for the most part in the stored proc
	// in order to do the sorting.
	private void evaluateAlertWarnings() {		
		// 5.12
		if ((this.contribOldAmt == null) && (this.contribOldPct == null)) {
			if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(this.getAutoOrSignup())) {
		    	this.noDeferralOnFileAlertForAuto = true;
			} else if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(this.getAutoOrSignup())) {
				this.noDeferralOnFileAlertForSignup = true;
			}
		}
		
		boolean overdue = currentDate() > this.anniversaryDateRaw;
		
		// 5.13-5.15
		if (overdue) {
			if (this.counter > 1) {
				if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(this.getAutoOrSignup())) {
					this.multipleOverdueWarningForAuto = true; // 5.15 
				} else if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(this.getAutoOrSignup())) {
					this.multipleOverdueWarningForSignup = true; // 5.24 
				}
			} else {
				if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(this.getAutoOrSignup())) {
					this.overdueWarningForAuto = true; // 5.14 
				} else if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(this.getAutoOrSignup())) {
					this.overdueWarningForSignup = true; // 5.22 
				}
			}
		} else {
			if (this.counter > 1) {
			    this.overdueAlert = true; // 5.13	
			} 
		}		
	}

	
	// As per 1.20, using Appendix D of Task Center Deferral Requests DFS
	private void evaluateInitiatedBy() {
		if (this.isADHoc) {
			this.initiatedSource = "Participant Website";
			String createName = null;
			if (createdFirstName == null && createdLastName !=null) {
				createName = createdLastName;
			} else {
				createName = createdLastName+","+createdFirstName;				
			}
			if (this.createdUserIdType.equals("PAR")) { // participant
				this.initiatedByExternal = createName;
				this.initiatedByInternal = createName;				
			} else { // PS,TPA, or internal [not participant]
				this.initiatedByExternal = "John Hancock representative";
				this.initiatedByInternal = "John Hancock representative("+createName+")";				
			}
		} else {
			this.initiatedByExternal = "Automated";
			this.initiatedByInternal = "Automated";
			this.initiatedSource = "Automated";
		}
	}
	
	
	public String hasOldContribution() {
		if ((this.contribOldAmt == null) && (this.contribOldPct == null)) {
			return "true";
		} else {
			return "false";
		}
	}
	
	private long currentDate() { // current date as long
		Calendar cal = Calendar.getInstance(); // set to start of day, better way to do this?
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	
	// called by form-reset in struts to deal with checkbox/radio buttons.
	public void resetOptions() {
		this.approve = false;
		this.decline = false;
	}
	
		
	// see spec 3.14
	private String encodeDetailType(String moneyTypeCode) {
		if ("EEROT".equalsIgnoreCase(moneyTypeCode)) {
			this.isRoth = true;
			
			return "Roth (401k): ";
		} else {
			this.isRoth = false;
			
			return "Before tax: ";
		}
	}
	
	// see spec, 5.16-5.18 
	private String encodeDetailsRequested(BigDecimal contribAmt, BigDecimal contribPct,
			                     BigDecimal contribOldAmt, BigDecimal contribOldPct,
			                     BigDecimal contribIncAmt, BigDecimal contribIncPct) {
		
		boolean hasOld = ((contribOldAmt != null) || (contribOldPct != null ));
		boolean hasNew = ((contribAmt != null) || (contribPct != null ));
		boolean hasInc = ((contribIncAmt != null) || (contribIncPct != null));
		
		if (hasOld && hasNew) { // content id 5
			if (contribAmt !=null) {
				if (contribOldAmt != null) {
					return "From "+encodeAmt(contribOldAmt)+" to "+encodeAmt(contribAmt);
				} else {
					return "From "+encodePct(contribOldPct)+" to "+encodeAmt(contribAmt);
				}
			} else {
				if (contribOldPct !=null) {
					return "From "+encodePct(contribOldPct)+" to "+encodePct(contribPct);
				} else {
					return "From "+encodeAmt(contribOldAmt)+" to "+encodePct(contribPct);					
				}
			}
			
		} else if (!hasOld && hasNew) { // content id 6
			if (contribAmt !=null) {
				return encodeAmt(contribAmt)+ " requested";
			} else {
				return encodePct(contribPct)+ " requested";
			}
		} else if (hasInc && !hasOld) { // content id 7
			if (contribIncAmt != null) {
				return "Increase by " + encodeAmt(contribIncAmt);
			} else {
				return "Increase by " + encodePct(contribIncPct);
			}
		} else {
			return ""; // exception?
		}
	}
	
	private static final DecimalFormat amountFormat = new DecimalFormat("###,##0.00");
	private String encodeAmt(BigDecimal amount) {
		if (amount == null) return ""; 
		return "$"+formatAmountFormatter(amount.doubleValue());
	}
	
	private static final DecimalFormat percentFormat = new DecimalFormat("##0.###");
	private String encodePct(BigDecimal percent) {
		if (percent == null) return "";
		return formatPercentFormatter(percent.doubleValue())+"%";
	}
	
	//synchronizes this method to avoid race condition.
    public static synchronized String formatAmountFormatter(double value) { 
        return amountFormat.format(value); 
    }
    public static synchronized String formatPercentFormatter(double value) { 
        return percentFormat.format(value); 
    }
	private String encodeDivision(String division) {
		if (division == null) {
			return "";
		} else {
			return division.trim();
		}
	}
	
    private String encodeDate(long theDate) {
    	if (theDate == 0) return "";
    	
    	Date initiated = new Date(theDate);
    	return dateFormat.format(initiated);
    }
	
	
	private String encodeSSN(String rawSSN) {
		if (rawSSN !=null) {
			return "xxx-xx-"+rawSSN.substring(rawSSN.length()-4);
		}	
		return "";
	}

	private boolean encodeIsAdHoc(String contribSrcCode) {
		if ("CI".equalsIgnoreCase(contribSrcCode.trim())) {
			return false;
		} else { // AC or JH
			return true;
		}
	}
	
	// **** getters ****/
	
	
	public boolean isADHocRequest() {
		return this.isADHoc;
	}
	
	public String getDivision() {
		return division;
	}


	public String getName() {
		return name;
	}


	public String getSsn() {
		return ssn;
	}

	public String getDetails() {
		return details;
	}

	public String getInitiated() {
		return encodeDate(this.createdTS);
	}

	public String getType() {
		String type = "";
		if (isADHocRequest()) {
			type = "Deferral change";
		} else if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(this.getAutoOrSignup())
				|| ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(this.getAutoOrSignup())) {
			type = "Scheduled deferral increase";
		}
		return type;
	}

	public String getAnniversaryDate() {
		return anniversaryDate;
	}

	public String getProfileId() {
		return profileId;
	}

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}

	public boolean isDecline() {
		return decline;
	}

	public void setDecline(boolean decline) {
		this.decline = decline;
	}
	
	public void clearFieldsInError() {
		if (this.fieldsInError !=null) {
			this.fieldsInError.clear();
		}
	}
	
	public boolean hasOverdueWarningForAuto() {
		return this.overdueWarningForAuto;
	}
	
	public boolean hasOverdueWarningForSignup() {
		return this.overdueWarningForSignup;
	}
	
	public boolean hasMulitipleOutstandingRequestsWarningForAuto() {
		return multipleOverdueWarningForAuto;
	}
	
	public boolean hasMulitipleOutstandingRequestsWarningForSignup() {
		return multipleOverdueWarningForSignup;
	}

	public boolean hasNoDeferralOnFileAlertForAuto() {
		return noDeferralOnFileAlertForAuto;
	}
	
	public boolean hasNoDeferralOnFileAlertForSignup() {
		return noDeferralOnFileAlertForSignup;
	}

	public boolean hasOverdueAlert() {
		return overdueAlert;
	}

	public boolean hasAlert() { 
		return noDeferralOnFileAlertForAuto || noDeferralOnFileAlertForSignup || overdueAlert;
	}
	
	public boolean hasWarning() {
		return this.multipleOverdueWarningForAuto || this.overdueWarningForAuto ||
		this.multipleOverdueWarningForSignup || this.overdueWarningForSignup;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}
	
	public long getCreatedTS() {
		return this.createdTS;
	}

	public String getStatusForDisplay() {
		if ("PA".equalsIgnoreCase(this.status)) {
			return "Pending approval";
		} else if ("AP".equalsIgnoreCase(this.status)) {
			return "Approved";
		} else if ("DC".equalsIgnoreCase(this.status)) {
			return "Declined";
		}
		
		return ""; // not good. 
	}
	
	public String getStatusCode() {
		return this.status;
	}
	
	// error stuff
	public boolean isFieldInError(String fieldName) {
		if ((this.fieldsInError == null) || (this.fieldsInError.size() == 0)) {
			return false;
		}
		
		return this.fieldsInError.contains(fieldName);
	}
	
	public void setFieldInError(String fieldName) {
		if (this.fieldsInError == null) {
			this.fieldsInError = new LinkedList<String>();
		}
		this.fieldsInError.add(fieldName);
	}
	
	public BigDecimal getContribAmt() {
		return contribAmt;
	}

	public BigDecimal getContribIncAmt() {
		return contribIncAmt;
	}

	public BigDecimal getContribIncPct() {
		return contribIncPct;
	}

	public BigDecimal getContribOldAmt() {
		return contribOldAmt;
	}

	public BigDecimal getContribOldPct() {
		return contribOldPct;
	}

	public BigDecimal getContribPct() {
		return contribPct;
	}

	public String getSSNRaw() {
		return this.ssnRaw;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}	
	
	public int getInstructionNo() {
		return instructionNo;
	}
	
	public boolean isAccountHolder() {
		return isAccountHolder;
	}
	
	public boolean isRoth() {
		return this.isRoth;
	}
	
	public String getInitiatedByExternalView() {
		return initiatedByExternal;
	}

	public String getInitiatedByInternalView() {
		return initiatedByInternal;
	}	

	public String getInitiatedSource() {
		return initiatedSource;
	}
	
	public void setAutoOrSignup(String autoOrSignup) {
		this.autoOrSignup = autoOrSignup;
	}

	public String getAutoOrSignup() {
		return autoOrSignup;
	}
	
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	// support stuff
	public String toString() {
		return StaticHelperClass.toString(this);
	}

}
