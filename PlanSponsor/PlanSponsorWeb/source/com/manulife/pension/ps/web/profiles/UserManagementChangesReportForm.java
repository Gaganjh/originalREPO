package com.manulife.pension.ps.web.profiles;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.PlanSponsorUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.Trustee;

/**
 * @author marcest
 *
 */
public class UserManagementChangesReportForm extends ReportForm {
	private static final long serialVersionUID = 4104735685695589996L;
	
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
    static {
        DATE_FORMATTER.setLenient(false);
    }
	
    //Date Formatter variable is added to a synchronized block to make it thread safe
    protected static synchronized String  dateFormatter(Date inputDate){ 
        return DATE_FORMATTER.format(inputDate); 
    }
    
    protected static synchronized Date  dateParser(String value) throws ParseException{ 
        return DATE_FORMATTER.parse(value); 
    }
    
	private String changedBy;
	private String contractNumber;
	private String action;
	private String teamCode; 
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
	private int totalRecordsInCSV;
	
	private List<LabelValueBean> teamCodeList = new ArrayList<LabelValueBean>();
    private List<LabelValueBean> internalActionList = new ArrayList<LabelValueBean>();
    private List<LabelValueBean> externalActionList = new ArrayList<LabelValueBean>();

	public void reset( HttpServletRequest httpservletrequest) {
		// reset checkboxes to false
//		trusteeSelected = false;
//		authorizedSignorSelected = false;
//		administrativeContactSelected = false;
//		payrollAdministratorSelected = false;
//		intermediaryContactSelected = false;
//		planSponsorUserSelected = false;
//		tpaSelected = false;
		super.reset(httpservletrequest);
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
		if (fromDate == null)
			return getDefaultDate();
		return fromDate;
	}

	private String getDefaultDate() {
		GregorianCalendar defaultDate = new GregorianCalendar(); 
		defaultDate.add(GregorianCalendar.DATE, -1);
		return dateFormatter(defaultDate.getTime());
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
        try {
            this.fromDate = dateFormatter(dateParser(fromDate));
        } catch (ParseException pe) {
		this.fromDate = fromDate;
	}
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
		if (toDate == null)
			return getDefaultDate();
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
        try {
            this.toDate = dateFormatter(dateParser(toDate));
        } catch (ParseException pe) {
		this.toDate = toDate;
	}
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
	
	public String getSelectedRoles() {
        StringBuffer roleCodes = new StringBuffer();
        for (String roleCode : getSelectedRoleList()) {
            if (roleCodes.length() > 0) {
                roleCodes.append(", ");
            }
            roleCodes.append("'").append(roleCode).append("'");
        }
        return roleCodes.toString();
    }

    public List<String> getSelectedRoleList() {
        List<String> roleList = new ArrayList<String>();
        if (isAdministrativeContactSelected()) {
            roleList.add(AdministrativeContact.stringID);
        }
        if (isAuthorizedSignorSelected()) {
            roleList.add(AuthorizedSignor.ID);
        }
        if (isIntermediaryContactSelected()) {
            roleList.add(IntermediaryContact.ID);
        }
        if (isPayrollAdministratorSelected()) {
            roleList.add(PayrollAdministrator.ID);
        }
        if (isPlanSponsorUserSelected()) {
            roleList.add(PlanSponsorUser.ID);
        }
        if (isTpaSelected()) {
            roleList.add(ThirdPartyAdministrator.ID);
        }
        if (isTrusteeSelected()) {
            roleList.add(Trustee.ID);
        }
        return roleList;
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
	
	public int getTotalRecordsInCSV() {
		return totalRecordsInCSV;
	}

	public void setTotalRecordsInCSV(int totalRecordsInCSV) {
		this.totalRecordsInCSV = totalRecordsInCSV;
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
	
    /**
     * @return the teamCodeList
     */
    public List<LabelValueBean> getTeamCodeList() {
        return teamCodeList;
    }

    /**
     * @param teamCodeList the teamCodeList to set
     */
    public void setTeamCodeList(List<LabelValueBean> teamCodeList) {
        this.teamCodeList = teamCodeList;
    }

    /**
     * @param externalActionList the externalActionList to set
     */
    public void setExternalActionList(List<LabelValueBean> externalActionList) {
        this.externalActionList = externalActionList;
    }

    /**
     * @param internalActionList the internalActionList to set
     */
    public void setInternalActionList(List<LabelValueBean> internalActionList) {
        this.internalActionList = internalActionList;
    }
    
    public List<LabelValueBean> getActionList() {
		if (isExternalReport())
			return externalActionList;
		else
			return internalActionList;
	}
}
