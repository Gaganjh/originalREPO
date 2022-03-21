package com.manulife.pension.ps.web.taglib.census;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.manulife.pension.ps.web.census.EmployeeSnapshotForm;
import com.manulife.pension.ps.web.census.util.EmployeeStatusHistoryReportHelper;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.ps.web.taglib.csf.DmPEDWarningTag;
import com.manulife.pension.ps.web.util.SessionHelper;

public class EmployeeStatusHistoryLinkTag extends TagSupport {
    private static final Logger logger = Logger.getLogger(DmPEDWarningTag.class);


	private static final String ANCHOR_START_PRE_URL = "<a href=\"";
	private static final String DESCRIPTION = "Edit";
	private static final String ANCHOR_START_POST_URL = "\">";
	private static final String ANCHOR_END = "</a>";
	private static final String POST_END = "</a>";
	private static final String STATUS_HISTORY_ACTION = "/do/census/employeeStatusHistory/";
	private static final String DELETE_ANCHOR_POST_CONFIRM_JS = "')\">";
	private static final String ANCHOR_CONFIRM_JS = "\" onClick=\"return doConfirmAndSubmit('employeeStatusHistory')\">";
	private static final String PARAM_START = "?";
	private String formName = "actionForm";
    /**
     * Constructor.
     */
    public EmployeeStatusHistoryLinkTag () {
        super();
    }
    
    
	public String getFormName() {
		return formName;
	}


	public void setFormName(String formName) {
		this.formName = formName;
	}


	/**
	 * Writes the end tag. 	 * 
	 */
	public int doEndTag() throws JspException {

		super.doEndTag();
        UserProfile userProfile = (UserProfile) SessionHelper.getUserProfile((HttpServletRequest) pageContext.getRequest());
        int contractNumber =userProfile.getCurrentContract().getContractNumber();
        String basePlanEntryDate = null;
        String planFrequency =null;
        String initialEnrollmentDate =null;
        String InitialEnrollmentDate =null;
        try {
          // get form and profileId
		EmployeeSnapshotForm form = (EmployeeSnapshotForm) BaseTagHelper.getSpringForm(pageContext,formName);;
    		
		if (form!=null){
			String profileId =form.getProfileId();
			if (doShowLink (profileId, userProfile, contractNumber)){
			
    			StringBuffer sb = new StringBuffer();
		
    			sb.append(ANCHOR_START_PRE_URL);
    			sb.append(getURL(profileId));
    			sb.append(ANCHOR_START_POST_URL);
    			sb.append(DESCRIPTION);
    			sb.append(ANCHOR_END);
    			sb.toString();
                pageContext.getOut().print(sb);
			}
			
			}
    } catch (Exception e) {
        logger.error("Exception calculating nextPED for contract " + contractNumber+ ", initialEnrollmentDate " + initialEnrollmentDate
                + " basePlanEntryDate " + basePlanEntryDate +  e);
    }
		return (EVAL_PAGE);
	}

	public static String getURL(String profileId) {
		StringBuffer url = new StringBuffer("/do/census/employeeStatusHistory/");
		url.append(PARAM_START);
		url.append("profileId=");
		url.append(profileId);
		return url.toString();
	}
	/**
	 * calls RmployeeHistoryDAO to get the # records
	 * 6.1.3.1 	Internal user is accessing an employee record which has at least 
	 * one history record with employment status equal to Cancelled
	 * 6.1.3.2 	External user is accessing an employee record which has at least
	 *  one history record with employment status equal to Cancelled
	 *  6.1.4	Even if the employee record only has one employment status history record 
	 *  (i.e. current status) saved in CSDB the link will be presented in the edit mode of the Employee Snapshot page.
	 * @param profileId
	 * @param userProfile
	 * @param contractId
	 * @return
	 */
	private static boolean doShowLink(String profileId, UserProfile userProfile, int contractId){
		int count = 0;
		int countCancelled =0;
		// count only non-cancelled records
		count = EmployeeStatusHistoryReportHelper.getStatusHistoryCountExt(contractId, profileId);
		//	count = EmployeeStatusHistoryReportHelper.getStatusHistoryCount(contractId, profileId);
        // show the link if there is at least one record and no cancelled
		countCancelled = EmployeeStatusHistoryReportHelper.getStatusHistoryCountCancelled(contractId, profileId);
		return (count > 0 && countCancelled==0);
	}
}

