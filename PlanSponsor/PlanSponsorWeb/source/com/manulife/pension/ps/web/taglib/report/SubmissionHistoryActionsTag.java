package com.manulife.pension.ps.web.taglib.report;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.platform.web.taglib.report.AbstractReportTag;
import com.manulife.pension.ps.service.report.census.valueobject.Details;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistory;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.AllowedToEdit;
import com.manulife.pension.ps.web.census.CensusSummaryReportForm;
import com.manulife.pension.ps.web.census.CensusVestingReportForm;
import com.manulife.pension.ps.web.census.DeferralReportForm;
import com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.participant.ParticipantAddressesReportForm;
import com.manulife.pension.ps.web.tools.util.CensusDetailsHelper;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.service.psr.valueobject.PasswordResetRequestEntity.RequestRestriction;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.BundledGaApprover;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.BasicInternalUser;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * @author parkand
 *
 * This tag renders the available action icons for a particular action type and item
 */
public class SubmissionHistoryActionsTag extends AbstractReportTag {

	private static final String PAGE_SCOPE = "page";

	private static final String TABLE_DATA_START = "<td>";
	private static final String TABLE_DATA_END = "</td>";
	private static final String TABLE_ROW_START = "<tr>";
	private static final String TABLE_ROW_END = "</tr>";
	private static final String TABLE_START = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"1\">";
	private static final String TABLE_END = "</table>";
    private static final String BREAK = "<br/>";

	private static final String ANCHOR_START_PRE_URL = "<a href=\"";
	private static final String ANCHOR_START_POST_URL = "\">";
	private static final String ANCHOR_END = "</a>";
	private static final String SPACER = "<img src=\"/assets/unmanaged/images/s.gif\" width=\"12\" height=\"12\"\" />";
    private static final String SPACER_SMALL = "<img src=\"/assets/unmanaged/images/s.gif\" width=\"3\" height=\"12\"\" />";
    private static final String SPACER_SMALL_VERTICAL = "<img src=\"/assets/unmanaged/images/s.gif\" width=\"27\" height=\"3\"\" />";
    private static final String RESET_PASSWORD_ICON = "<img src=\"/assets/unmanaged/images/rIcon.gif\" alt=\"Reset\" title=\"Reset\" border=\"0\"/>";
    private static final String RESET_PASSWORD_LABEL = "Reset password";
    private static final String LEGEND_RESET_PASSWORD = "legendResetPassword";
    private static final String SPACE = "&nbsp;";
	private static final String VIEW_ICON = "<img src=\"/assets/unmanaged/images/view_icon.gif\" alt=\"View\" title=\"View\" border=\"0\"/>";
	private static final String COPY_ICON = "<img src=\"/assets/unmanaged/images/copy_icon.gif\" alt=\"Copy\" title=\"Copy\" border=\"0\"/>";
	private static final String EDIT_ICON = "<img src=\"/assets/unmanaged/images/edit_icon.gif\" alt=\"Edit\" title=\"Edit\" border=\"0\"/>";
    private static final String INFO_ICON_ELIGIBILITY = "<img src=\"/assets/unmanaged/images/info_icon.gif\" alt=\"Eligibility Information\" title=\"Eligibility Information\" border=\"0\"/>";
    private static final String INFO_ICON_VESTING = "<img src=\"/assets/unmanaged/images/info_icon.gif\" alt=\"Vesting Information\" title=\"Vesting Information\" border=\"0\"/>";
    private static final String DELETE_ICON = "<img src=\"/assets/unmanaged/images/delete_icon.gif\" alt=\"Delete\" title=\"Delete\" border=\"0\"/>";
    private static final String HISTORY_ICON_START = "<img src=\"/assets/unmanaged/images/history_icon.gif\" alt=\"";
    private static final String HISTORY_ICON_END =	"\" border=\"0\"/>";

	private static final String DELETE_ANCHOR_CONFIRM_JS = "\" onClick=\"return confirmDelete('";
	private static final String DELETE_ANCHOR_POST_CONFIRM_JS = "')\">";
	private static final String ACTION_ANCHOR_CONFIRM_JS = "\" onClick=\"return confirmAction()";

	private static final String ICONS="icons";
	private static final String VIEW="view";
	private static final String EDIT_SUBMITTED_CENSUS="editSubmittedCensus";
	private static final String EDIT_PARTICIPANT_ADDRESS="editParticipantAddress";
	private static final String EDIT_EMPLOYEE_SUMMARY="editEmployeeSummary";
    private static final String EDIT_VIEW_HISTORY_DEFERRAL="evhDeferral";
    private static final String EDIT_EMPLOYEE_VESTING="editEmployeeVesting";
    


	private String item;
	private String profile;
	private String action;

	public int doStartTag() throws JspException {

		UserProfile userProfile = (UserProfile)TagUtils.getInstance().lookup(pageContext,profile,PAGE_SCOPE);
		StringBuffer sb = new StringBuffer();

		if (EDIT_SUBMITTED_CENSUS.equals(action)) {
			sb.append(generateCensusEditIcon(userProfile,item));
		} else if (EDIT_PARTICIPANT_ADDRESS.equals(action)) {
			sb.append(generateCSDBAddressIcons(userProfile,item));
        } else if (EDIT_EMPLOYEE_SUMMARY.equals(action)) {           
				sb.append(generateEmployeeSummaryIcons(userProfile,item));			
		} else if (EDIT_VIEW_HISTORY_DEFERRAL.equals(action)) {
            sb.append(generateEVHIcons(userProfile,item));
        } else if (EDIT_EMPLOYEE_VESTING.equals(action)) {
            sb.append(generateEmployeeVestingIcons(userProfile,item));
		}else if(LEGEND_RESET_PASSWORD.equals(action)){
			sb.append(generateLegendResetPasswordIcon(userProfile,item));
		} else {
			SubmissionHistoryItem subHistItem = (SubmissionHistoryItem)TagUtils.getInstance().lookup(pageContext,item,PAGE_SCOPE);
			if (ICONS.equals(action)) {
				sb.append(generateIcons(userProfile,subHistItem));
			} else if (VIEW.equals(action)) {
				sb.append(generateViewAnchor(subHistItem));
			}
		}

		try {
			pageContext.getOut().print(sb.toString());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_BODY_TAG;
	}

	private String generateViewAnchor(SubmissionHistoryItem item) {
		StringBuffer sb = new StringBuffer();
		sb.append(ANCHOR_START_PRE_URL);
		sb.append(SubmissionActionURLGenerator.getViewURL(item));
		sb.append(ACTION_ANCHOR_CONFIRM_JS);
		sb.append(ANCHOR_START_POST_URL);
		return sb.toString();
	}

	private String generateIcons(UserProfile userProfile, SubmissionHistoryItem subHistItem) {

		String subName=(String)subHistItem.getSubmitterName();
		if(subName==null)subName="";

		SubmissionHistoryItemActionHelper helper = SubmissionHistoryItemActionHelper.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(TABLE_START);
		sb.append(TABLE_ROW_START);

		// view icon
		sb.append(TABLE_DATA_START);
		if (helper.isViewAllowed(subHistItem,userProfile)) {
			sb.append(generateViewAnchor(subHistItem));
			sb.append(VIEW_ICON);
			sb.append(ANCHOR_END);
		} else {
			sb.append(SPACER);
		}
		sb.append(TABLE_DATA_END);

		// copy icon
		sb.append(TABLE_DATA_START);
		if (helper.isCopyAllowed(subHistItem,userProfile)&&
				!(subName.equals(Constants.AWAITING_PAYMENT_PAYROLL_COMPANY))) {
			sb.append(ANCHOR_START_PRE_URL);
			sb.append(SubmissionActionURLGenerator.getCopyURL(subHistItem));
			sb.append(ACTION_ANCHOR_CONFIRM_JS);
			sb.append(ANCHOR_START_POST_URL);
			sb.append(COPY_ICON);
			sb.append(ANCHOR_END);
		} else {
			sb.append(SPACER);
		}
		sb.append(TABLE_DATA_END);

		sb.append(TABLE_ROW_END);
		sb.append(TABLE_ROW_START);

		// edit icon
		sb.append(TABLE_DATA_START);
		if (helper.isEditAllowed(subHistItem,userProfile)&&
				!(subName.equals(Constants.AWAITING_PAYMENT_PAYROLL_COMPANY))) {
			sb.append(ANCHOR_START_PRE_URL);
			sb.append(SubmissionActionURLGenerator.getEditURL(subHistItem));
			sb.append(ACTION_ANCHOR_CONFIRM_JS);
			sb.append(ANCHOR_START_POST_URL);
			sb.append(EDIT_ICON);
			sb.append(ANCHOR_END);
		} else {
			sb.append(SPACER);
		}
		sb.append(TABLE_DATA_END);

		// delete icon
		sb.append(TABLE_DATA_START);
		if (helper.isDeleteAllowed(subHistItem,userProfile)&&
				!(subName.equals(Constants.AWAITING_PAYMENT_PAYROLL_COMPANY))) {
			sb.append(ANCHOR_START_PRE_URL);
			sb.append(SubmissionActionURLGenerator.getDeleteURL(subHistItem));
			sb.append(DELETE_ANCHOR_CONFIRM_JS);
			sb.append(SubmissionHistoryItemActionHelper.getInstance().getDisplayType(subHistItem));
			sb.append(DELETE_ANCHOR_POST_CONFIRM_JS);
			sb.append(DELETE_ICON);
			sb.append(ANCHOR_END);
		} else {
			sb.append(SPACER);
		}
		sb.append(TABLE_DATA_END);

		sb.append(TABLE_ROW_END);
		sb.append(TABLE_END);

		return sb.toString();
	}

	private String generateCSDBAddressIcons(UserProfile profile, String item) throws JspException {

        StringBuffer sb = new StringBuffer();

		// CensusDetailsHelper helper = (CensusDetailsHelper)RequestUtils.lookup(pageContext,SecurityConstants.CENSUS_DETAILS_HELPER,PAGE_SCOPE);
        Object itemObject = TagUtils.getInstance().lookup(pageContext,item,PAGE_SCOPE);

        if (itemObject instanceof Details) {

            ParticipantAddressesReportForm form = (ParticipantAddressesReportForm) TagUtils.getInstance().lookup(pageContext,Constants.CENSUS_ADDRESS_FORM,PAGE_SCOPE);

            // view icon - ParticipantAddress
            sb.append(ANCHOR_START_PRE_URL);
            sb.append(SubmissionActionURLGenerator.getAddressViewURL((Details)itemObject));
            sb.append(ANCHOR_START_POST_URL);
            sb.append(VIEW_ICON);
            sb.append(ANCHOR_END);

            // edit icon - ParticipantAddress
            if (form.isAllowedToEdit()) {
                sb.append(SPACER_SMALL);
                sb.append(ANCHOR_START_PRE_URL);
                sb.append(SubmissionActionURLGenerator.getAddressEditURL((Details)itemObject));
                sb.append(ANCHOR_START_POST_URL);
                sb.append(EDIT_ICON);
                sb.append(ANCHOR_END);
            } else {
                sb.append(SPACER);
            }

        } else if (itemObject instanceof ParticipantAddressHistory) {
            // edit icon - ParticipantAddressHistory
            sb.append(ANCHOR_START_PRE_URL);
            sb.append(SubmissionActionURLGenerator.getAddressEditURL((ParticipantAddressHistory)itemObject));
            sb.append(ANCHOR_START_POST_URL);
            sb.append(EDIT_ICON);
            sb.append(ANCHOR_END);
        }

		return sb.toString();
	}

    private String generateEVHIcons(UserProfile profile, String item) throws JspException {
        StringBuffer sb = new StringBuffer();


        Details censusItem = (Details)TagUtils.getInstance().lookup(pageContext,item,PAGE_SCOPE);
        AllowedToEdit form = (DeferralReportForm) TagUtils.getInstance().lookup(pageContext,Constants.DEFERRAL_REPORT_FORM,PAGE_SCOPE);

        if(form == null) {
            form = (EmployeeEnrollmentSummaryReportForm) TagUtils.getInstance().lookup(pageContext,Constants.EMPLOYEE_ENROLLMENT_SUMMARY_FORM,PAGE_SCOPE);
        }
        
        String historyMouseOver = "";
        try{
        	Miscellaneous hmo = (Miscellaneous)ContentCacheManager.getInstance().getContentById(ContentConstants.ELIGIBILITY_MOUSE_OVER_HISTORY_ICON, ContentTypeManager.instance().MISCELLANEOUS);
        	historyMouseOver = hmo.getText();
       
        }catch(ContentException ex){
        	historyMouseOver = "Participant transaction history";
        }
        // view icon
        sb.append(ANCHOR_START_PRE_URL);
        sb.append(SubmissionActionURLGenerator.getCensusSummaryViewURL(censusItem));
        sb.append(ANCHOR_START_POST_URL);
        sb.append(VIEW_ICON);
        sb.append(ANCHOR_END);

        // edit icon
        if (form.isAllowedToEdit()) {
            sb.append(SPACER_SMALL);
            sb.append(ANCHOR_START_PRE_URL);
            sb.append(SubmissionActionURLGenerator.getCensusSummaryEditURL(censusItem));
            sb.append(ANCHOR_START_POST_URL);
            sb.append(EDIT_ICON);
            sb.append(ANCHOR_END);
        } else {
            sb.append(SPACER);
            sb.append(SPACER_SMALL);
        }

        // history icon
        
        
        sb.append(BREAK);
        sb.append(SPACER_SMALL_VERTICAL);
        sb.append(BREAK);
        sb.append(ANCHOR_START_PRE_URL);
        sb.append(SubmissionActionURLGenerator.getHistoryURL(censusItem));
        sb.append(ANCHOR_START_POST_URL);
        sb.append(HISTORY_ICON_START);
        sb.append(historyMouseOver);
        sb.append(HISTORY_ICON_END);
        sb.append(ANCHOR_END);
        
        // information icon
        if(isEligibilityInfoPageAccesible(profile)){
            sb.append(SPACER_SMALL);
            sb.append(ANCHOR_START_PRE_URL);
            sb.append(SubmissionActionURLGenerator.getEligibilityInformationURL(censusItem));
            sb.append(ANCHOR_START_POST_URL);
            sb.append(INFO_ICON_ELIGIBILITY);
            sb.append(ANCHOR_END);
        }else {
            sb.append(SPACER);
            sb.append(SPACER_SMALL);
        }

        return sb.toString();
    }

    private String generateEmployeeSummaryIcons(UserProfile profile, String item) throws JspException {
        StringBuffer sb = new StringBuffer();


        Details censusItem = (Details)TagUtils.getInstance().lookup(pageContext,item,PAGE_SCOPE);
        AllowedToEdit form = (CensusSummaryReportForm) TagUtils.getInstance().lookup(pageContext,Constants.CENSUS_SUMMARY_FORM,PAGE_SCOPE);
        int contractId=profile.getCurrentContract().getContractNumber();
        // If is not Census Summary it should be Eligiblity or Deferral
        if(form == null) {
            form = (EmployeeEnrollmentSummaryReportForm) TagUtils.getInstance().lookup(pageContext,Constants.EMPLOYEE_ENROLLMENT_SUMMARY_FORM,PAGE_SCOPE);
        }
        
        // view icon
        sb.append(SPACER_SMALL);
        sb.append(ANCHOR_START_PRE_URL);
        sb.append(SubmissionActionURLGenerator.getCensusSummaryViewURL(censusItem));
        sb.append(ANCHOR_START_POST_URL);
        sb.append(VIEW_ICON);
        sb.append(ANCHOR_END);
        sb.append(SPACER_SMALL);

        // edit icon
        if (form.isAllowedToEdit()) {
            sb.append(ANCHOR_START_PRE_URL);
            sb.append(SubmissionActionURLGenerator.getCensusSummaryEditURL(censusItem));
            sb.append(ANCHOR_START_POST_URL);
            sb.append(EDIT_ICON);
            sb.append(ANCHOR_END);
        }
      //Reset Password Icon
        try {
			if (displayResetPasswordLegend(profile, contractId)&& isValidUserRole(profile) ) {
	    		// Action Icon display code
				sb.append(BREAK);
				sb.append(SPACER_SMALL_VERTICAL);
		     	sb.append(BREAK);
				sb.append(SPACER_SMALL);				
		     	sb.append(ANCHOR_START_PRE_URL);
			    sb.append(SubmissionActionURLGenerator.getResetPasswordURL(censusItem));
			    sb.append(ANCHOR_START_POST_URL);
			    sb.append(RESET_PASSWORD_ICON);
			    sb.append(ANCHOR_END);
			}else{
				sb.append(BREAK);
	     	   	sb.append(SPACER_SMALL_VERTICAL);
	     	   	sb.append(BREAK);
	     	   sb.append(SPACER);
			}
			
		} catch (Exception exception) {
			throw new JspException(exception.getMessage());
		}
		
        // info icon
        if(isEligibilityInfoPageAccesible(profile)){
           
     	   
     	   sb.append(SPACER_SMALL);
           sb.append(ANCHOR_START_PRE_URL);
           sb.append(SubmissionActionURLGenerator.getEligibilityInformationURL(censusItem));
           sb.append(ANCHOR_START_POST_URL);
           sb.append(INFO_ICON_ELIGIBILITY);
           sb.append(ANCHOR_END);
        }

        return sb.toString();
    }

    private String generateLegendResetPasswordIcon(UserProfile profile, String item) throws JspException {
    	StringBuffer sb = new StringBuffer();
		try {
			int contractId = profile.getCurrentContract().getContractNumber();
			if (displayResetPasswordLegend(profile, contractId) && isValidUserRole(profile)) {
				// Legend Icon display code
				sb.append(SPACE);
				sb.append(RESET_PASSWORD_ICON);
				sb.append(SPACE);
				sb.append(RESET_PASSWORD_LABEL);
			}
		} catch (Exception exception) {
			throw new JspException(exception.getMessage(), exception);
		}
    	return sb.toString();
    }

    private String generateEmployeeVestingIcons(UserProfile profile, String item) throws JspException {
        StringBuffer sb = new StringBuffer();

        Details censusItem = (Details)TagUtils.getInstance().lookup(pageContext,item,PAGE_SCOPE);
        CensusVestingReportForm form = (CensusVestingReportForm) TagUtils.getInstance().lookup(pageContext,Constants.CENSUS_VESTING_FORM,PAGE_SCOPE);

        // view icon
        if (form.isAllowedToViewVesting()) {
            sb.append(ANCHOR_START_PRE_URL);
            sb.append(SubmissionActionURLGenerator.getCensusVestingViewURL(censusItem));
            sb.append(ANCHOR_START_POST_URL);
            sb.append(VIEW_ICON);
            sb.append(ANCHOR_END);
        } else {
            sb.append(SPACER);
        }

        // view vesting info icon
        if (form.isAllowedToViewVestingInformation()) {
            sb.append(SPACER_SMALL);
            sb.append(ANCHOR_START_PRE_URL);
            sb.append(SubmissionActionURLGenerator.getCensusVestingInformationURL(censusItem));
            sb.append(ANCHOR_START_POST_URL);
            sb.append(INFO_ICON_VESTING);
            sb.append(ANCHOR_END);
        } else {
            sb.append(SPACER);
        }

        return sb.toString();
    }

	private String generateCensusEditIcon(UserProfile profile, String item) throws JspException {

        StringBuffer sb = new StringBuffer();
        if (! (profile.getRole() instanceof RelationshipManager 
               || profile.getRole() instanceof BasicInternalUser
               || profile.getRole() instanceof BundledGaApprover)) {
    		CensusDetailsHelper helper = (CensusDetailsHelper)TagUtils.getInstance().lookup(pageContext,Constants.CENSUS_DETAILS_HELPER,PAGE_SCOPE);
    		CensusSubmissionItem censusItem = (CensusSubmissionItem)TagUtils.getInstance().lookup(pageContext,item,PAGE_SCOPE);

    		if (helper.isCensusEditable(censusItem)) {
    			sb.append(ANCHOR_START_PRE_URL);
    			sb.append(SubmissionActionURLGenerator.getCensusEditURL(censusItem));
    			sb.append(ANCHOR_START_POST_URL);
    			sb.append(EDIT_ICON);
    			sb.append(ANCHOR_END);
    		} else {
    			sb.append(SPACER);
    		}
        }
		return sb.toString();
	}

	/**
	 * @return Returns the item.
	 */
	public String getItem() {
		return item;
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(String item) {
		this.item = item;
	}
	/**
	 * @return Returns the profile.
	 */
	public String getProfile() {
		return profile;
	}
	/**
	 * @param profile The profile to set.
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	private boolean isEligibilityInfoPageAccesible(UserProfile profile) throws JspException {
		boolean hasAccess = SecurityManager.getInstance().isUserAuthorized(profile, SubmissionActionURLGenerator.ELIGIBILITY_INFO_ACTION);
		if(hasAccess){
		EmployeeEnrollmentSummaryReportForm form = (EmployeeEnrollmentSummaryReportForm) TagUtils.getInstance().lookup(pageContext,Constants.EMPLOYEE_ENROLLMENT_SUMMARY_FORM,PAGE_SCOPE);
		if (form != null) {
				return form.isEligibiltyCalcOn();
			}
		}
		return false;
	}
	
	/**
	 * To check whether to display Reset Password Legend icon
	 * 
	 * @param contractId
	 * @param profule UserProfile
	 * @return boolean
	 * @throws Exception
	 */
	private boolean displayResetPasswordLegend(UserProfile profile, int contractId)
			throws Exception {
		String displayResetIconInd = (String) pageContext.getAttribute("displayResetIconInd");

			if (displayResetIconInd != null) {
				return Constants.YES.equals(displayResetIconInd);

			} else if (SecurityManager.getInstance().isUserAuthorized(profile,
					SubmissionActionURLGenerator.RESET_PWD_EMAIL_ACTION)) {
				
				Set<RequestRestriction> pwdResetEligibilityViolations = new HashSet<RequestRestriction>();
				
				pwdResetEligibilityViolations = SecurityServiceDelegate.getInstance()
						.validateResetPasswordRequest(contractId, profile.getPrincipal().getProfileId(), pwdResetEligibilityViolations);
								
				if (pwdResetEligibilityViolations == null
						|| pwdResetEligibilityViolations.isEmpty()) {
					
					pageContext.setAttribute("displayResetIconInd", Constants.YES);
					return true;
				}
			}
			
			pageContext.setAttribute("displayResetIconInd", Constants.NO);
			return false;
	}
	
	//CSP.164.4	If the user has one of the following roles: The Contract plan has at least one web registered Plan Sponsor user with one of following roles
	//1.	Trustee
	//2.	Authorized Signor
	//3.	Administrative Contact

	/**
	 * Displays the 'R' candy button in Census Summary page for a valid user role as per PAR.
	 * @param profile
	 * @return
	 */
	private boolean isValidUserRole(UserProfile profile){
		return (profile.getRole().getRoleId() == AuthorizedSignor.ID
				|| profile.getRole().getRoleId() == Trustee.ID
				|| profile.getRole().getRoleId() == AdministrativeContact.stringID) ;
	}
	
}

