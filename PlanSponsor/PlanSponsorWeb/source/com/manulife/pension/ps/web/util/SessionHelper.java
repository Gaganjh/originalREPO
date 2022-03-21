package com.manulife.pension.ps.web.util;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.heartbeat.util.memory.Sizeof;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.tools.EditContributionDetailBean;
import com.manulife.pension.ps.web.tools.EditLongTermPartTimeDetailBean;
import com.manulife.pension.ps.web.tools.EditVestingDetailBean;
import com.manulife.pension.ps.web.tools.FileUploadDetailBean;
import com.manulife.pension.ps.web.tools.SubmissionUploadDetailBean;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author Charles Chan
 */
public class SessionHelper extends BaseSessionHelper {

	private final static String LINE_BREAK = System.getProperty("line.separator");

	private final static Logger logger = Logger.getLogger(SessionHelper.class);
	private final static String BINDING_LISTENER = "BindingListener";

	/**
	 * Constructor.
	 *  
	 */
	private SessionHelper() {
		super();
	}

	public static Set<Integer> getAccessibleContractMessageTemplates(final ServletContext servlet,
			final HttpServletRequest request) throws SystemException {

		Set<Integer> returnSet = null;
		Integer contractId = null;  
		UserProfile userProfile = getUserProfile(request);
		if (userProfile.getCurrentContract() == null) {
			throw new SystemException(
					"Can not retrieve accessible message templates without selecting contract");
		}
		Map<Integer, Set<Integer>> cachedMap = (Map<Integer, Set<Integer>>) request
				.getSession(false).getAttribute(
						Constants.CONTRACT_MESSAGE_ACCESSIBLE_TEMPLATES);
		if ( cachedMap == null ) {
			cachedMap = new HashMap<Integer, Set<Integer>>();
			request.getSession(false).setAttribute(Constants.CONTRACT_MESSAGE_ACCESSIBLE_TEMPLATES, cachedMap);
		}
		contractId = userProfile.getCurrentContract().getContractNumber();
		returnSet = cachedMap.get(contractId);
		if ( returnSet == null ) {
			returnSet = MessageServiceFacadeFactory.getInstance(servlet).getAccessibleContractMessageTemplates(userProfile);
			cachedMap.put(contractId, returnSet);
		}
		return returnSet;
	}

	public static Set<Integer> getAccessibleTpaFirmMessageTemplates(final ServletContext servlet,
			final HttpServletRequest request) throws SystemException {

		UserProfile userProfile = getUserProfile(request);
		 Set<Integer> returnSet = (Set<Integer>) request
				.getSession(false).getAttribute(
						Constants.TPA_FIRM_MESSAGE_ACCESSIBLE_TEMPLATES);
		if ( returnSet == null ) {
			returnSet = MessageServiceFacadeFactory.getInstance(servlet).getAccessibleTpaFirmMessageTemplates(userProfile);
			request.getSession(false).setAttribute(Constants.TPA_FIRM_MESSAGE_ACCESSIBLE_TEMPLATES, returnSet);
		}
		return returnSet;
	}	
	
	public static void setShowEditExternalUserButton(HttpServletRequest request, Boolean flag) {
		request.getSession(false).setAttribute(Constants.SHOW_EDIT_EXT_USER_BUTTON, flag);
	}

	public static void setMCLeftMCFromGlobalContext(HttpServletRequest request, Boolean flag) {
		request.getSession(false).setAttribute(Constants.MESSAGE_CENTER_LEFT_MC_FROM_GLOBAL_CONTEXT, flag);
	}

	public static void unsetShowEditExternalUserButton(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.SHOW_EDIT_EXT_USER_BUTTON);
	}

	public static Boolean getShowEditExternalUserButton(HttpServletRequest request) {
		return (Boolean) request.getSession(false).getAttribute(Constants.SHOW_EDIT_EXT_USER_BUTTON);
	}	
	
	
	public static Boolean getMCLeftMCFromGlobalContext(HttpServletRequest request) {
		return (Boolean) request.getSession(false).getAttribute(Constants.MESSAGE_CENTER_LEFT_MC_FROM_GLOBAL_CONTEXT);
	}
	public static Boolean getMCSelectContract(HttpServletRequest request) {
		return (Boolean) request.getSession(false).getAttribute(Constants.MESSAGE_SELECT_CONTRACT);
	}

	public static String getLastVisitedManageUsersPage(HttpServletRequest request) {
		return (String) request.getSession(false).getAttribute(Constants.LAST_VISITED_MANAGE_USERS_PAGE);
	}

	public static void setLastVisitedManageUsersPage(HttpServletRequest request, String forward) {
		request.getSession(false).setAttribute(Constants.LAST_VISITED_MANAGE_USERS_PAGE, forward);
	}

	public static String getTemporaryPassword(HttpServletRequest request) {
		return (String) request.getSession(false).getAttribute(Constants.TEMP_PASSWORD);
	}
		

	public static void setTemporaryPassword(HttpServletRequest request, String password) {
		request.getSession(false).setAttribute(Constants.TEMP_PASSWORD, password);
	}

	public static void unsetTemporaryPassword(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.TEMP_PASSWORD);
	}

	public static void setFileUploadDetails(HttpServletRequest request, FileUploadDetailBean details) {
		request.getSession(false).setAttribute(Constants.FILE_UPLOAD_DETAIL_DATA, details);
	}

	public static void unsetFileUploadDetails(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.FILE_UPLOAD_DETAIL_DATA);
	}

	public static void unsetMCLeftMCFromGlobalContext(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.MESSAGE_CENTER_LEFT_MC_FROM_GLOBAL_CONTEXT);
	}
	public static void unsetMCSelectContract(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.MESSAGE_SELECT_CONTRACT);
	}

	public static void setMCSelectContract(HttpServletRequest request, Boolean value) {
		request.getSession(false).setAttribute(Constants.MESSAGE_SELECT_CONTRACT, value);
	}

	public static void setSubmissionUploadDetails(HttpServletRequest request, SubmissionUploadDetailBean details) {
		request.getSession(false).setAttribute(Constants.SUBMISSION_UPLOAD_HISTORY_DETAIL_DATA, details);
	}

	public static void unsetSubmissionUploadDetails(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.SUBMISSION_UPLOAD_HISTORY_DETAIL_DATA);
	}

	public static void setPaymentUploadDetails(HttpServletRequest request, SubmissionUploadDetailBean details) {
		request.getSession(false).setAttribute(Constants.PAYMENT_UPLOAD_HISTORY_DETAIL_DATA, details);
	}

	public static void unsetPaymentUploadDetails(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.PAYMENT_UPLOAD_HISTORY_DETAIL_DATA);
	}
	
	public static void setEditContributionDetails(HttpServletRequest request, EditContributionDetailBean details) {
		request.getSession(false).setAttribute(Constants.EDIT_CONTRIBUTION_CONFIRM_DETAIL_DATA, details);
	}
    
    public static void setEditVestingDetails(HttpServletRequest request, EditVestingDetailBean details) {
        request.getSession(false).setAttribute(Constants.EDIT_VESTING_CONFIRM_DETAIL_DATA, details);
    }

    public static void setEditLongTermPartTimeDetails(HttpServletRequest request, EditLongTermPartTimeDetailBean details) {
        request.getSession(false).setAttribute(Constants.EDIT_LONG_TERM_PART_TIME_CONFIRM_DETAIL_DATA, details);
    }
    
	public static void unsetEditContributionDetails(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.EDIT_CONTRIBUTION_CONFIRM_DETAIL_DATA);
	}
	
	/**
	 * Returns the user profile associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The user profile object associated with the request (or null if
	 *         none is found).
	 */
	public static UserProfile getUserProfile(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
		} else {
			return null;
		}
	}

	/**
	 * Remove everything except the UserProfile object from the session.
	 * 
	 * @param request
	 */
	public static void clearSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (logger.isDebugEnabled()) {
			logger.debug("Clearing objects from the session");
		}
		
		if (session != null) {
			Enumeration enumeration = session.getAttributeNames();
			while (enumeration.hasMoreElements()) {
				String attribute = (String) enumeration.nextElement();
				if (!attribute.equals(Constants.USERPROFILE_KEY) &&
					!attribute.equals(BINDING_LISTENER) &&
					!attribute.equals(MCConstants.AttrUserState) &&
					!attribute.equals(Constants.CONTRACT_MESSAGE_ACCESSIBLE_TEMPLATES) &&
					!attribute.equals(Constants.MESSAGE_CENTER_LEFT_MC_FROM_GLOBAL_CONTEXT) &&
					!attribute.equals(Constants.MESSAGE_CENTER_CAR_VIEW_FORM) &&
					!attribute.equals(Constants.CHALLENGE_PASSCODE_IND) &&
					!attribute.equals(Constants.PASSCODE_SESSION_KEY) &&
					!attribute.equals(Constants.USERID) &&
					!attribute.equals(Constants.IS_TRANSITION)) {
					if (logger.isDebugEnabled()) {
						logger.debug(" ... Removing [" + attribute + "]");
					}
					session.removeAttribute(attribute);
				}
			}
		}
	}

	/**
	 * Dump the content of the session to the given writer.
	 * 
	 * @param request
	 *            The request object.
	 * @param writer
	 *            The writer to write to.
	 */
	public static void dumpSession(HttpServletRequest request, PrintWriter writer) {
		HttpSession session = request.getSession(false);
		boolean hasAttribute = false;
		StringBuffer sb = new StringBuffer(256);
		if (session != null) {
			sb.append("Session found! Created on ")
			  .append(new Date(session.getCreationTime()))
			  .append(LINE_BREAK)
			  .append("Attributes:")
			  .append(LINE_BREAK);

			Enumeration enumeration = session.getAttributeNames();
			while (enumeration.hasMoreElements()) {
				hasAttribute = true;
				String attribute = (String) enumeration.nextElement();
				sb.append("    ")
				  .append(attribute)
				  .append("=[")
				  .append(session.getAttribute(attribute))
				  .append("]")
				  .append(LINE_BREAK);
			}

			if (hasAttribute) {
				writer.write(sb.toString());
			} else {
				writer.write("None!");
			}
		} else {
			writer.write("No session!");
		}
		writer.write(LINE_BREAK);
	}

	/**
	 * Dump the size of the session attributes to the given writer.
	 * 
	 * @param request
	 *            The request object.
	 * @param writer
	 *            The writer to write to.
	 */
	public static void dumpSessionSize(HttpServletRequest request, PrintWriter writer) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			writer.write("dumpSessionSize: There is no session.");
		} else {
			long totalSize = 0;
			StringBuffer buffer = new StringBuffer(256);
			buffer.append("dumpSessionSize: attribute sizes ... ");
			Enumeration attributeNames = session.getAttributeNames();
			while (attributeNames.hasMoreElements()) {
				String attributeName = (String) attributeNames.nextElement();
				buffer.append("    sizeof(")
				  .append(attributeName)
				  .append(")=")
				  .append(Sizeof.sizeof(session.getAttribute(attributeName)));
			}
			writer.write("dumpSessionSize: Total Size = " + totalSize);
			writer.write("dumpSessionSize: attributes are " + buffer.toString());
		}
	}
	
	/**
	 * setLastExternalUserWithPayrollEmailPermission
	 */
		public static void setLastExternalUserWithPayrollEmailPermission(HttpServletRequest request, Long user_profile_id) {
			request.getSession(false).setAttribute(Constants.LAST_EXTERNAL_USER_WITH_PAYROLL_EMAIL_PERMISSION, user_profile_id);
		}	

	/**
	 * removeLastExternalUserWithPayrollEmailPermissionFromSession
	 */
		public static void removeLastExternalUserWithPayrollEmailPermission(HttpServletRequest request) {
			request.getSession(false).removeAttribute(Constants.LAST_EXTERNAL_USER_WITH_PAYROLL_EMAIL_PERMISSION);
		}
			
	/**
	 * setLastTPAUserWithPayrollEmailPermission
	 */
	    public static void setLastTPAUserWithPayrollEmailPermission(HttpServletRequest request, Long user_profile_id) {
		   	request.getSession(false).setAttribute(Constants.LAST_TPA_USER_WITH_PAYROLL_EMAIL_PERMISSION, user_profile_id);
		}	

	/**
	 * removeLastTPAUserWithPayrollEmailPermissionFromSession
	 */
	    public static void removeLastTPAUserWithPayrollEmailPermission(HttpServletRequest request) {
			request.getSession(false).removeAttribute(Constants.LAST_TPA_USER_WITH_PAYROLL_EMAIL_PERMISSION);
		}	
	    
	 /**
	  * setShowPayrollEmailPermission
	  */
		public static void setShowPayrollEmailPermission(HttpServletRequest request, boolean showPayrollEmail) {
			request.getSession(false).setAttribute(Constants.SHOW_PAYROLL_EMAIL_PERMISSION, new Boolean(showPayrollEmail));
		}	
     /**
	  * removeShowPayrollEmailPermission
	  */
	    
	    public static void removeShowPayrollEmailPermission(HttpServletRequest request) {
			request.getSession(false).removeAttribute(Constants.SHOW_PAYROLL_EMAIL_PERMISSION);
		}
		
	 
     /**
	  * removeDeleteMessageForLastPayrollUser
	  */
    
        public static void removeDeleteMessageForLastPayrollUser(HttpServletRequest request) {
		    request.getSession(false).removeAttribute(Constants.DELETE_PAYROLL_EMAIL_MESSAGE);
	    }	
    
    /**
     * Sets the filterCriteriaVo object in the session
     * 
     * @param request
     * @param filterCriteriaVo
     */
    public static void setFilterCriteriaVO(HttpServletRequest request,
			FilterCriteriaVo filterCriteriaVo) {
		request.getSession(false).setAttribute(Constants.FILTER_CRITERIA_CENSUS_TABS, filterCriteriaVo);
	}

    /**
     * Gets the filterCriteriaVo object from the session
     * 
     * @param request
     * @return FilterCriteriaVo
     */
	public static FilterCriteriaVo getFilterCriteriaVO(HttpServletRequest request) {
		return (FilterCriteriaVo) request.getSession(false).getAttribute(Constants.FILTER_CRITERIA_CENSUS_TABS);
	}
	
	public static void setLastVisitedManageContactsPage(HttpServletRequest request, String forward) {
		request.getSession(false).setAttribute(Constants.LAST_VISITED_MANAGE_CONTACTS_PAGE, forward);
	}

	public static void setShowPasswordMeterForPSW(HttpServletRequest request, String value) {
		request.getSession(false).setAttribute(Constants.PSW_SHOW_PASSWORD_METER_IND, value);
	}
	
	public static  boolean getBusinessParamFlag(final HttpServletRequest request , final UserInfo userInfo) throws SystemException{
		
		EnvironmentServiceDelegate envservice = null;
		
		String businessParamIndicator = null;
		
		boolean businessParamFlag = Boolean.FALSE;
		
		envservice = EnvironmentServiceDelegate.getInstance();
			
		try{
			
			businessParamIndicator = envservice.getBusinessParam(Constants.PSW_SHOW_PASSWORD_METER_IND);
			
		boolean isExternalUser = userInfo.getRole() instanceof ExternalUser;
		
		boolean isInternalUser = userInfo.getRole() instanceof InternalUser;
		
		businessParamFlag = ("EXT".equals(businessParamIndicator) && isExternalUser);
		   
		businessParamFlag = ("INT".equals(businessParamIndicator) && isInternalUser) || businessParamFlag;
		   
		businessParamFlag = ("ALL".equals(businessParamIndicator)) || businessParamFlag;
		
			}catch (Exception sse) {
				if (logger.isDebugEnabled()) {
					logger.debug(sse.toString());
				}
				throw new SystemException("SessionHelper,getBusinessParamFlag"
						+ "Failed to businessapram indicator for new user "
						+ "due to service not found / down or any other ejb errors" + sse.toString());

			}
		
		return businessParamFlag;
	}
	
	

}