package com.manulife.pension.ps.web.tools.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.service.report.transaction.handler.MoneySourceDescription;
import com.manulife.pension.ps.service.submission.valueobject.MoneySource;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.BundledGaApprover;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.BasicInternalUser;
import com.manulife.pension.util.log.LogUtility;

/**
 * @author Andrew Park
 *
 * This helper class is used to determine action rules for a given
 * SubmissionHistoryItem
 * Implemented as a singleton.
 */
public class SubmissionHistoryItemActionHelper {

	// singleton instance
	private static SubmissionHistoryItemActionHelper instance = new SubmissionHistoryItemActionHelper();
	private static Logger logger = Logger.getLogger(SubmissionHistoryItemActionHelper.class);
	public static final String SUBMISSION_TYPE_LTPT = "Q";

	// collections
	private static Map typeMap;

	// private classes representing collection elements
	private static class TypeMapElement {
		private Map statusMap;
		private Map permissionMap;
		private TypeMapElement(Map status, Map permission) {
			statusMap = status;
			permissionMap = permission;
		}
	}


	// constants defining literals used in XML descriptor
	private static final String TYPE_ELEMENT_NAME = "type";
	private static final String TYPE_CODE_ATTRIBUTE = "code";
	private static final String TYPE_DESCRIPTION_ATTRIBUTE = "desc";
	private static final String STATUS_ELEMENT_NAME = "status";
	private static final String STATUS_CODE_ATTRIBUTE = "code";
	private static final String STATUS_DESCRIPTION_ATTRIBUTE = "desc";
	private static final String STATUS_DISPLAY_STATUS_ATTRIBUTE = "displayStatus";
	private static final String STATUS_ICON_INDICATOR_ATTRIBUTE = "icon";

	private static final String ACTIONS_ELEMENT_NAME = "actions";
	private static final String ACTIONS_VIEW_ATTRIBUTE = "view";
	private static final String ACTIONS_COPY_ATTRIBUTE = "copy";
	private static final String ACTIONS_EDIT_ATTRIBUTE = "edit";
	private static final String ACTIONS_DELETE_ATTRIBUTE = "delete";
	private static final String PERMISSION_ELEMENT_NAME = "permission";
	private static final String PERMISSION_TYPE_ATTRIBUTE = "type";

	// permission types
	private static final String PERMISSION_VIEW_SUBMISSION = "ViewSubmission";
	private static final String PERMISSION_UPLOAD_CREATE = "Upload/Create";
	private static final String PERMISSION_INTERNAL_USER = "InternalUser";
    private static final String PERMISSION_SUBMIT_UPDATE_VESTING = "SubmitUpdateVesting";

	// actions
	private static final short VIEW = 0;
	private static final short COPY = 1;
	private static final short EDIT = 2;
	private static final short DELETE = 3;
	private static final short DISPLAY_STATUS = 4;
	private static final short STATUS_DESCRIPTION = 5;
	private static final short ICON_INDICATOR = 6;

	// XML action values
	public static final String NO = "no";
	public static final String YES = "yes";
	public static final String NA = "na";
	public static final String CASH = "CASH";
	public static final String DEBIT = "DEBIT";
	public static final String ICON_INDICATOR_WARNING = "warning";
	public static final String ICON_INDICATOR_ERROR = "error";
	
	// private constructor
	private SubmissionHistoryItemActionHelper() {
		logger = Logger.getLogger(this.getClass());
		InputStream xmlFileStream = null;
		try {
			xmlFileStream = getClass().getClassLoader().getResourceAsStream("./actionStates.xml");
			DOMParser parser = new DOMParser();
	        try {
	        	parser.parse(new InputSource(xmlFileStream));
	            Document document = parser.getDocument();
	            loadData(document);
	        } catch (IOException e) {
	        	e.printStackTrace();
	  			throw new SystemException(e, "SubmissionHistoryItemActionHelper", "constructor" , "IOException occurred.");
	        } catch (SAXException e) {
	        	e.printStackTrace();
	  			throw new SystemException(e, "SubmissionHistoryItemActionHelper", "constructor" , "SAXException occurred.");
	        }
		} catch (SystemException e) {
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);
		}finally{
			try {
				if(xmlFileStream!= null)
					xmlFileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static SubmissionHistoryItemActionHelper getInstance() {
		return instance;
	}

	private static void loadData(Document document) {
        NodeList list = document.getElementsByTagName(TYPE_ELEMENT_NAME);
        int len = list.getLength();
        typeMap = new HashMap(len+1);
        for (int i=0; i < len; i++)
        {
        	Element type = (Element)list.item(i);
        	String typeCode = type.getAttribute(TYPE_CODE_ATTRIBUTE);
        	String description = type.getAttribute(TYPE_DESCRIPTION_ATTRIBUTE);

        	// load status rules
        	NodeList statuses = type.getElementsByTagName(STATUS_ELEMENT_NAME);
        	int lenStatuses = statuses.getLength();
        	Map statusMap = new HashMap(lenStatuses+1);
        	for (int j=0; j < lenStatuses; j++)
        	{
        		Element status = (Element)statuses.item(j);
        		String statusCode = status.getAttribute(STATUS_CODE_ATTRIBUTE);
        		String statusDesc = status.getAttribute(STATUS_DESCRIPTION_ATTRIBUTE);
        		String displayStatus = status.getAttribute(STATUS_DISPLAY_STATUS_ATTRIBUTE);
        		String iconIndicator = status.getAttribute(STATUS_ICON_INDICATOR_ATTRIBUTE);
        		if ( ICON_INDICATOR_ERROR.equals(iconIndicator) ) {
        			iconIndicator = Constants.ERROR_ICON;
        		} else if ( ICON_INDICATOR_ERROR.equals(iconIndicator) ) {
        			iconIndicator = Constants.WARNING_ICON;
        		} else {
        			iconIndicator = "";
        		}

        		// there should only be one action element
        		NodeList actions = status.getElementsByTagName(ACTIONS_ELEMENT_NAME);
       			Element action = (Element)actions.item(0);
       			ArrayList al = new ArrayList(7);
       			al.add(VIEW,action.getAttribute(ACTIONS_VIEW_ATTRIBUTE));
        		al.add(COPY,action.getAttribute(ACTIONS_COPY_ATTRIBUTE));
        		al.add(EDIT,action.getAttribute(ACTIONS_EDIT_ATTRIBUTE));
        		al.add(DELETE,action.getAttribute(ACTIONS_DELETE_ATTRIBUTE));
        		al.add(DISPLAY_STATUS,displayStatus);
        		al.add(STATUS_DESCRIPTION, statusDesc);
        		al.add(ICON_INDICATOR,iconIndicator);
        		statusMap.put(statusCode,al);
        	}

        	// load permission rules
        	NodeList permissions = type.getElementsByTagName(PERMISSION_ELEMENT_NAME);
        	int lenPermissions = permissions.getLength();
    		Map permissionMap = new HashMap(lenPermissions+1);
        	for (int j=0; j < lenPermissions; j++)
        	{
        		Element permission = (Element)permissions.item(j);
        		String permissionType = permission.getAttribute(PERMISSION_TYPE_ATTRIBUTE);

        		// there should only be one action element
        		NodeList actions = permission.getElementsByTagName(ACTIONS_ELEMENT_NAME);
       			Element action = (Element)actions.item(0);
       			ArrayList al = new ArrayList(4);
       			al.add(VIEW,action.getAttribute(ACTIONS_VIEW_ATTRIBUTE));
        		al.add(COPY,action.getAttribute(ACTIONS_COPY_ATTRIBUTE));
        		al.add(EDIT,action.getAttribute(ACTIONS_EDIT_ATTRIBUTE));
        		al.add(DELETE,action.getAttribute(ACTIONS_DELETE_ATTRIBUTE));
        		permissionMap.put(permissionType,al);
        	}

        	// add the maps to the type collection
        	TypeMapElement tme = new TypeMapElement(statusMap,permissionMap);
        	typeMap.put(typeCode,tme);
        }
	}

	/**
	 * @param item SubmissionHistoryItem
	 * @param profile UserProfile
	 * @return boolean indicating whether the item can be viewed
	 */
	public boolean isViewAllowed(SubmissionHistoryItem item, UserProfile profile) {
		boolean rc = false;

		try {
			TypeMapElement tme = (TypeMapElement)typeMap.get(item.getType());

			// determine if viewable based on status
			ArrayList actions = (ArrayList)tme.statusMap.get(item.getSystemStatus());
			if ( actions == null ) {
				// cannot find allowable actions for given system status
				return false;
			}
			boolean statusState = isAllowed((String)actions.get(VIEW),profile.getRole().toString());

			// determine if viewable based on permission rules
			boolean permissionState = isAllowed(tme.permissionMap,profile,VIEW);

			// viewable only if both status and permission rules are TRUE
			rc = statusState && permissionState;
		} catch (Exception e) {
  			logger.error("Error: ", new SystemException(e, this.getClass().getName(), "isViewAllowed" , "Unable to determine view access"));
  			throw new NestableRuntimeException(e);
		}

		return rc;
	}

	/**
	 * @param item SubmissionHistoryItem
	 * @param profile UserProfile
	 * @return boolean indicating whether the item can be copied
	 */
	public boolean isCopyAllowed(SubmissionHistoryItem item, UserProfile profile){
		boolean rc = false;

		try {
			TypeMapElement tme = (TypeMapElement)typeMap.get(item.getType());

			// determine if copyable based on status
			ArrayList actions = (ArrayList)tme.statusMap.get(item.getSystemStatus());
			if ( actions == null ) {
				// cannot find allowable actions for given system status
				return false;
			}
			boolean statusState = isAllowed((String)actions.get(COPY),profile.getRole().toString());

			// determine if copyable based on permission rules
			boolean permissionState = isAllowed(tme.permissionMap,profile,COPY);

			// requirement 6.1.34 - Contribution Regular or Transfer case with invalid money source
			if (GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR.equals(item.getType()) || GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER.equals(item.getType())) {
				// check the money source
				MoneySource ms = item.getMoneySource();
				if ( ms != null ) {
					if ( !(MoneySourceDescription.REGULAR_CODE.equals(ms.getSourceCode()) || MoneySourceDescription.EXTERNAL_TRANSFER_CODE.equals(ms.getSourceCode())) ) {
						return false;
					}
				}
			}

			// determine if copyable based on contract status
//			boolean contractState = !profile.getCurrentContract().getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED);
			boolean contractState = profile.getCurrentContract() == null ? false : !profile.getCurrentContract().getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED);

			// viewable only if both status and permission rules are TRUE
			rc = statusState && permissionState && contractState;
		} catch (Exception e) {
  			logger.error("Error: ", new SystemException(e, this.getClass().getName(), "isCopyAllowed" , "Unable to determine copy access"));
		}

		return rc;
	}

	/**
	 * @param item SubmissionHistoryItem
	 * @param profile UserProfile
	 * @return boolean indicating whether the item can be edited
	 */
	public boolean isEditAllowed(SubmissionHistoryItem item, UserProfile profile) {
		return isEditAllowed(item.getType(), item.getSystemStatus(), item.isLockAvailable(String.valueOf(profile.getPrincipal().getProfileId())), profile);
	}

	/**
	 * @param type
	 * @param systemStatus
	 * @param profile UserProfile
	 * @return boolean indicating whether a case with the given parameters can be edited
	 */
	public boolean isEditAllowed(String type, String systemStatus, boolean isLockAvailable, UserProfile profile) {
		boolean rc = false;
		try {
			TypeMapElement tme = (TypeMapElement)typeMap.get(type);

			// determine if editable based on status
			ArrayList actions = (ArrayList)tme.statusMap.get(systemStatus);
			if ( actions == null ) {
				// cannot find allowable actions for given system status
				return false;
			}
			boolean statusState = isAllowed((String)actions.get(EDIT),profile.getRole().toString());

			// determine if editable based on permission rules
			boolean permissionState = isAllowed(tme.permissionMap,profile,EDIT);

			// determine if editable based on contract status
			//boolean contractState = !profile.getCurrentContract().getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED);
			boolean contractState = profile.getCurrentContract() == null ? false : !profile.getCurrentContract().getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED);
            /*
             * TODO: Use the framework properly.
             */
			if ((type.equals(GFTUploadDetail.SUBMISSION_TYPE_CENSUS)
					|| type.equals(SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT))
					&& (profile.getRole() instanceof RelationshipManager
							|| profile.getRole() instanceof BasicInternalUser
							|| profile.getRole() instanceof BundledGaApprover)) {
				return false;
			}

			// editable only if both status and permission rules are TRUE
			rc = statusState && permissionState && contractState;
		} catch (Exception e) {
  			logger.error("Error: ", new SystemException(e, this.getClass().getName(), "isEditAllowed" , "Unable to determine edit access"));
		}
		return rc;
	}

	/**
	 * @param item SubmissionHistoryItem
	 * @param profile UserProfile
	 * @return boolean indicating whether the item can be deleted
	 */
	public boolean isDeleteAllowed(SubmissionHistoryItem item, UserProfile profile) {
		boolean rc = false;
		try {
			TypeMapElement tme = (TypeMapElement)typeMap.get(item.getType());

			// determine if deletable based on status
			ArrayList actions = (ArrayList)tme.statusMap.get(item.getSystemStatus());
			if ( actions == null ) {
				// cannot find allowable actions for given system status
				return false;
			}
			boolean statusState = isAllowed((String)actions.get(DELETE),profile.getRole().toString());

			// determine if deletable based on permission rules
			boolean permissionState = isAllowed(tme.permissionMap,profile,DELETE);

			// determine if deletable based on contract status
			//boolean contractState = !profile.getCurrentContract().getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED);
			boolean contractState = profile.getCurrentContract() == null ? false : !profile.getCurrentContract().getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED);

			// no longer need to check lock
			// boolean lockAvailable = item.isLockAvailable(profile.getPrincipal().getUserName());

			// viewable only if both status and permission rules are TRUE
			rc = statusState && permissionState && contractState;
		} catch (Exception e) {
  			logger.error("Error: ", new SystemException(e, this.getClass().getName(), "isDeleteAllowed" , "Unable to determine delete access"));
		}
		return rc;
	}

	/**
	 * @param profile
	 * @param typeCode
	 * @param systemStatus
	 * @return
	 */
	public boolean isDeleteAllowed(UserProfile profile, String typeCode, String systemStatus) {
		boolean rc = false;
		try {
			TypeMapElement tme = (TypeMapElement)typeMap.get(typeCode);

			// determine if deletable based on status
			ArrayList actions = (ArrayList)tme.statusMap.get(systemStatus);
			if ( actions == null ) {
				// cannot find allowable actions for given system status
				return false;
			}
			boolean statusState = isAllowed((String)actions.get(DELETE),profile.getRole().toString());

			// determine if deletabe based on permission rules
			boolean permissionState = isAllowed(tme.permissionMap,profile,DELETE);
			// viewable only if both status and permission rules are TRUE
			rc = statusState && permissionState;
		} catch (Exception e) {
  			logger.error("Error: ", new SystemException(e, this.getClass().getName(), "isDeleteAllowed" , "Unable to determine delete access"));
		}
		return rc;
	}


	/**
	 * @param code String retrieved from actionStates.xml
	 * @param profile UserProfile
	 * @return boolean indicating whether or not the particular action is allowed
	 */
	private boolean isAllowed(String code, String role) {
		boolean rc = false;
		if ( YES.equals(code) ) {
			rc = true;
		} else if ( !NO.equals(code) ){
			// check role rules
			StringTokenizer st = new StringTokenizer(code, ",");
			while (st.hasMoreElements()) {
				String allowableRole = st.nextToken();
				if ( role.equals(allowableRole) ) {
					rc = true;
					break;
				}
			}
		}
		return rc;
	}

	private boolean isAllowed(Map permissionMap, UserProfile profile, short action) {

		// load the permissions into local Strings for convenience
		String viewSubmission = (String)((ArrayList)permissionMap.get(PERMISSION_VIEW_SUBMISSION)).get(action);
		String uploadCreateSubmission = (String)((ArrayList)permissionMap.get(PERMISSION_UPLOAD_CREATE)).get(action);
		String internalUser = (String)((ArrayList)permissionMap.get(PERMISSION_INTERNAL_USER)).get(action);
        String submitUpdateVesting = (String)((ArrayList)permissionMap.get(PERMISSION_SUBMIT_UPDATE_VESTING)).get(action);

		/*
		 * check each type of permission
		 * both the rule and the profile must be YES/true for at least one of these conditions
		 * we can return true as soon as we find a permission match
		 */
		if ( YES.equals(viewSubmission) && profile.isSubmissionAccess() ) {
			return true;
		}
		if ( YES.equals(uploadCreateSubmission) && profile.isAllowedUploadSubmissions() ) {
			return true;
		}
		if ( YES.equals(internalUser) && profile.isInternalUser() ) {
			return true;
		}
        if ( YES.equals(submitUpdateVesting) && profile.isAllowedSubmitUpdateVesting() ) {
            return true;
        }
        
		/*
		 * made it to the end
		 * this means that all none of the allowable permission rules have passed
		 * return false
		 */
		return false;
	}

	public String getDisplayStatus(SubmissionHistoryItem item) {
		StringBuffer displayStatus = new StringBuffer();
		try {
			String systemStatus = item.getSystemStatus();
			if (systemStatus == null || systemStatus.length() == 0) {
				// return empty string
				return displayStatus.toString();
			}
			TypeMapElement tme = (TypeMapElement)typeMap.get(item.getType());
			ArrayList actions = (ArrayList)tme.statusMap.get(systemStatus);
			if ( actions == null ) {
				// cannot find allowable actions for given system status
				return displayStatus.toString();
			}
			if ((String)actions.get(ICON_INDICATOR) != "") {
				displayStatus.append((String)actions.get(ICON_INDICATOR));
			}
			displayStatus.append((String)actions.get(DISPLAY_STATUS));
		} catch (Exception e) {
			// cannot determine the status of this submission history item
			// return an empty string
  			logger.error("Error: ", new SystemException(e, this.getClass().getName(), "getDisplayStatus" , "Unable to determine display status for item "+item.getSubmissionId()));
		}
		return displayStatus.toString();
	}

	public String getSystemStatusDescription(SubmissionHistoryItem item) {
		String statusDescription = "";
		try {
			String systemStatus = item.getSystemStatus();
			if (systemStatus == null || systemStatus.length() == 0) {
				// return empty string
				return statusDescription;
			}
			TypeMapElement tme = (TypeMapElement)typeMap.get(item.getType());
			ArrayList actions = (ArrayList)tme.statusMap.get(systemStatus);
			if ( actions == null ) {
				// cannot find allowable actions for given system status
				return statusDescription;
			}
			statusDescription = (String)actions.get(STATUS_DESCRIPTION);
		} catch (Exception e) {
			// cannot determine the status of this submission history item
			// return an empty string
  			logger.error("Error: ", new SystemException(e, this.getClass().getName(), "getSystemStatusDescription" , "Unable to determine display status for item "+item.getSubmissionId()));
		}
		return statusDescription;
	}

	public String getDisplayType(SubmissionHistoryItem item) {
		String displayType = "";
		boolean isComboFile = StringUtils.equals(item.getComboFileInd(), "Y") ? true : false ;
		try {
			if(isComboFile) {
				if(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR.equals(item.getType()) || 
						GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER.equals(item.getType())) {
					displayType = "Regular Contribution<sup>#</sup>";
				} else {
					displayType = "Census<sup>#</sup>";
				}
			} else if ((GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR.equals(item.getType()) || GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER.equals(item.getType())) && item.getMoneySource() != null ) {
				// the item is either a regular or transfer contribution - determine real display type based on money type
				displayType = item.getMoneySource().getDisplayName();
			} else if (GFTUploadDetail.SUBMISSION_TYPE_MISCELLANEOUS.equals(item.getType())) {
				// translate misc submission types to 'Sample'
				displayType = "Sample";
			} else {
				displayType = SubmissionTypeTranslater.translate(item.getType());
			}
		} catch (Exception e) {
			// cannot determine the type of this submission history item
			// return an empty string
			logger.error("Error: ", new SystemException(e, this.getClass().getName(), "getDisplayType" , "Unable to determine display type for item"+item.getSubmissionId()));
		}
		return displayType;
	}
}
