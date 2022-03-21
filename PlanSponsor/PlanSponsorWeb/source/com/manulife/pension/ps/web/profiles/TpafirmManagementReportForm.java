package com.manulife.pension.ps.web.profiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * @author marcest
 *
 */
public class TpafirmManagementReportForm extends ReportForm {
	private static final long serialVersionUID = 4104735685695589996L;
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
    public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy");
	
	private String changedBy;
	private String contractNumber;
	private String action;
	private String teamCode;
	private String userType;
	private String fromDate;
	private String toDate;
	private boolean trusteeSelected;
	private boolean authorizedSignorSelected;
	private boolean administrativeContactSelected;
	private boolean payrollAdministratorSelected;
	private boolean intermediaryContactSelected;
	private boolean tpaSelected;
	private boolean planSponsorUserSelected;
	private boolean externalReport;

    // private static Map<String, String> permissionMap = new HashMap<String, String>();
	
	
    private List<LabelValueBean> teamCodeList = new ArrayList<LabelValueBean>();

	
	private String [] userTypeList = { "All" , "Internal", "External" };

    // static {
    // permissionMap.put("REDO", "Download reports - full SSN");
    // permissionMap.put("SUVI", "Submit/Update Vesting");
    // permissionMap.put("AEEM", "Auto enrollment administration emails");
    // permissionMap.put("ANEM", "Annual reminder notification email");
    // permissionMap.put("PAEM", "Payroll Path email");
    // permissionMap.put("SBAC", "View Submissions");
    // permissionMap.put("SBUC", "Create/upload submissions");
    // permissionMap.put("VWAS", "View all user's submissions");
    // permissionMap.put("CAAC", "Cash Account");
    // permissionMap.put("LNIN", "Initiate Loans");
    // permissionMap.put("LNRW", "Review Loans");
    // permissionMap.put("WDIN", "Initiate i:withdrawals");
    // permissionMap.put("WDRW", "Review i:withdrawals");
    // permissionMap.put("WDAP", "Signing Authority");
    // permissionMap.put("CDUP", "Update census data");
    // permissionMap.put("SYVW", "View salary");
    // permissionMap.put("DEEM", "Deferral email");
    // permissionMap.put("ENEM", "Enrollment email");
    // permissionMap.put("ACEM", "Address Change email");
    //		
    // }

	public void reset( HttpServletRequest httpservletrequest) {
		// reset checkboxes to false
		trusteeSelected = false;
		authorizedSignorSelected = false;
		administrativeContactSelected = false;
		payrollAdministratorSelected = false;
		intermediaryContactSelected = false;
		planSponsorUserSelected = false;
		tpaSelected = false;
		super.reset( httpservletrequest);
    }	
	/**
	 * @return the contractNumber
	 */
	public String getContractNumber() {
		return contractNumber;
	}

	/**
	 * @param contractNumber the contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}

	public TpafirmManagementReportForm() {
		super();
		setUserType("Internal");
		Calendar yesterday = new GregorianCalendar();
		yesterday.add(Calendar.DATE, -1);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		setFromDate(dateFormat.format(yesterday.getTime()));
		setToDate(dateFormat.format(yesterday.getTime()));
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the teamCode
	 */
	public String getTeamCode() {
		return teamCode;
	}

	/**
	 * @param teamCode the teamCode to set
	 */
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	/**
	 * @return the contractList
	 */
	public List<LabelValueBean> getTeamCodeList() {
		return teamCodeList;
	}
    
    public void setTeamCodeList(List<LabelValueBean> teamCodeList) {
        this.teamCodeList = teamCodeList;
    }
	
	public String[] getUserTypeList() {
		return userTypeList;
	}
	

	/**
	 * @return the trusteeSelected
	 */
	public boolean isTrusteeSelected() {
		return trusteeSelected;
	}

	/**
	 * @param trusteeSelected the trusteeSelected to set
	 */
	public void setTrusteeSelected(boolean trusteeSelected) {
		this.trusteeSelected = trusteeSelected;
	}
	/**
	 * @return the administrativeContactSelected
	 */
	public boolean isAdministrativeContactSelected() {
		return administrativeContactSelected;
	}
	/**
	 * @param administrativeContactSelected the administrativeContactSelected to set
	 */
	public void setAdministrativeContactSelected(
			boolean administrativeContactSelected) {
		this.administrativeContactSelected = administrativeContactSelected;
	}
	/**
	 * @return the authorizedSignorSelected
	 */
	public boolean isAuthorizedSignorSelected() {
		return authorizedSignorSelected;
	}
	/**
	 * @param authorizedSignorSelected the authorizedSignorSelected to set
	 */
	public void setAuthorizedSignorSelected(boolean authorizedSignorSelected) {
		this.authorizedSignorSelected = authorizedSignorSelected;
	}
	/**
	 * @return the intermediaryContactSelected
	 */
	public boolean isIntermediaryContactSelected() {
		return intermediaryContactSelected;
	}
	/**
	 * @param intermediaryContactSelected the intermediaryContactSelected to set
	 */
	public void setIntermediaryContactSelected(boolean intermediaryContactSelected) {
		this.intermediaryContactSelected = intermediaryContactSelected;
	}
	/**
	 * @return the payrollAdministratorSelected
	 */
	public boolean isPayrollAdministratorSelected() {
		return payrollAdministratorSelected;
	}
	/**
	 * @param payrollAdministratorSelected the payrollAdministratorSelected to set
	 */
	public void setPayrollAdministratorSelected(boolean payrollAdministratorSelected) {
		this.payrollAdministratorSelected = payrollAdministratorSelected;
	}
	/**
	 * @return the tpaSelected
	 */
	public boolean isTpaSelected() {
		return tpaSelected;
	}
	/**
	 * @param tpaSelected the tpaSelected to set
	 */
	public void setTpaSelected(boolean tpaSelected) {
		this.tpaSelected = tpaSelected;
	}
	/**
	 * @return the externalReport
	 */
	public boolean isExternalReport() {
		return externalReport;
	}
	/**
	 * @param externalReport the externalReport to set
	 */
	public void setExternalReport(boolean externalReport) {
		this.externalReport = externalReport;
	}
	/**
	 * @return the changedBy
	 */
	public String getChangedBy() {
		return changedBy;
	}
	/**
	 * @param changedBy the changedBy to set
	 */
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}
	/**
	 * @return the planSponsorUserSelected
	 */
	public boolean isPlanSponsorUserSelected() {
		return planSponsorUserSelected;
	}
	/**
	 * @param planSponsorUserSelected the planSponsorUserSelected to set
	 */
	public void setPlanSponsorUserSelected(boolean planSponsorUserSelected) {
		this.planSponsorUserSelected = planSponsorUserSelected;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
    private String getCurrentDay() {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());
        return DATE_FORMATTER.format(new Date(cl.getTimeInMillis()));
    }
}
