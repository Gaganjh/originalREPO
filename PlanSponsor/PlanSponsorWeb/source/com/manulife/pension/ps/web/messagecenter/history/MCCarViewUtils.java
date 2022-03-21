package com.manulife.pension.ps.web.messagecenter.history;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.contract.valueobject.ContractEnrollmentVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.message.report.valueobject.MessageReportData;
import com.manulife.pension.service.message.report.valueobject.MessageReportDetails;
import com.manulife.pension.service.message.valueobject.MessageRecipient.RecipientStatus;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;
import com.manulife.pension.service.message.valueobject.MessageTemplate.Priority;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.utility.ConversionUtility;
import com.manulife.pension.service.security.valueobject.UserInfo;

public class MCCarViewUtils {

	private static LabelValueBean ClientUserLabel = new LabelValueBean(
			"===== Client user =====", "=");
	private static LabelValueBean TPAUserLabel = new LabelValueBean(
			"======= TPA ========", "==");
	private static EnumMap<RecipientStatus, String> RecipientStatusMap = new EnumMap<RecipientStatus, String>(
			RecipientStatus.class);
	private static EnumMap<Priority, String> PriorityMap = new EnumMap<Priority, String>(
			Priority.class);
	static {
		RecipientStatusMap.put(RecipientStatus.NEW, "New");
		RecipientStatusMap.put(RecipientStatus.HIDDEN, "Removed");
		RecipientStatusMap.put(RecipientStatus.VISITED, "Visited");
		PriorityMap.put(Priority.NORMAL, "Normal");
		PriorityMap.put(Priority.IMPORTANT_INFORMATION, "Important");
		PriorityMap.put(Priority.URGENT, "Urgent");
	}

	public enum MessageStatus {
		// \u25CF is a bullet. These displays get dumped out into javascript, 
		// which is why there are \u25CF instead of &#x25CF ( which is used for html )
		// What we really need is an encoder that will encode for javascript
		// and encode for html. 
		ALL("ALL", "All"),
		ACTIVE("ACTIVE", "Active"),
		ARCHIVED("ARCHIVED", "Archived"),
		ESCALATED("ESCALATED", "    \u25CF Escalated"),
		EXPIRED("EXPIRED", "    \u25CF Expired"),
		REPLACED("REPLACED", "    \u25CF Replaced"),
		OBSOLETED("OBSOLETED", "    \u25CF Obsoleted"),
		COMPLETEDONLINE("COMPONLINE", "    \u25CF Completed Online"),
		DECLAREDCOMPLETE("DECCOMP", "    \u25CF Declared Complete"),
		REMOVEDFROMVIEW("REMFROMVIEW", "    \u25CF Removed"),
		REMOVEDBYSTILLACTIVE("REMSTILLACTIVE", "    \u25CF Removed But Still Active");
	
	    private static final Map<String,MessageStatus> lookup 
     	= new HashMap<String,MessageStatus>();

	    static {
	    	for(MessageStatus s : EnumSet.allOf(MessageStatus.class))
	    		lookup.put(s.getValue(), s);
	    }
		
		private String value;
		private String text;
	
		private MessageStatus(String value, String text) {
			this.value = value;
			this.text = text;
		}
		
		public String getValue() {
			return value;
		}
		
		public static MessageStatus get(String value) {
			return lookup.get(value);
		}

		public String getText() {
			
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}	

	/**
	 * Get recipients for a list of contract ids
	 * 
	 * 1. get all PS users for contract id
	 * 2. get the FIRM for the contract id
	 * 3. get all TPAs for the FIRM
	 * 4. for each user, get all contracts associated to 
	 * 
	 * @param contractId
	 * @return
	 * @throws SystemException
	 * @throws SecurityServiceException 
	 */
	public static String getRecipientList(ServletContext servlet, int contractId) throws SystemException {
		StringBuffer returnString = new StringBuffer();
		try {
			
			//validate the contract.  this throws an exception if not found, so just need to call it.
			InternalUser role = null;
			ContractServiceDelegate.getInstance().getContractDetails(contractId, EnvironmentServiceDelegate.getInstance()
					.retrieveContractDiDuration((UserRole)role, 0,
							null));
			List<UserInfo> users = new ArrayList<UserInfo>();
			users.addAll(MessageServiceFacadeFactory.getInstance(servlet).getRecipients(contractId));
			
			Collections.sort(users, new Comparator<UserInfo>() {
				public int compare(UserInfo u1, UserInfo u2) {
					UserRole r1 = u1.getRole();
					UserRole r2 = u2.getRole();
					// r1 and r2 are the same, then compare the name
					if ((r1.isTPA() && r2.isTPA()) || (!r1.isTPA() && !r2.isTPA())) {
						int r = u1.getLastName().compareTo(u2.getLastName());
						return r == 0 ? u1.getFirstName().compareTo(u2.getFirstName()) : r;
					} else if (r1.isTPA()) { // u1 is tpa and u2 is client
						// user
						return 1;
					} else { // u1 is client user and u2 is tpa
						return -1;
					}
				}
			});
			StringBuffer ps = new StringBuffer();
			StringBuffer tpa = new StringBuffer();
			List<UserInfo> psUsers = new ArrayList<UserInfo>();
			List<UserInfo> tpaUsers = new ArrayList<UserInfo>();
			for (UserInfo u : users) {
				if (u.getRole().isTPA()) {
					tpaUsers.add(u);
				} else {
					psUsers.add(u);
				}
			}
			Iterator<UserInfo> it = tpaUsers.iterator();
			while (it.hasNext()) {
				UserInfo u = it.next();
				tpa.append(Long.toString(u.getProfileId())).append("|").append(u.getFirstName()).append("|").append(
						u.getLastName()).append("|").append(ConversionUtility.convertToStoredProcRole(u.getRole()));
				if (it.hasNext())
					tpa.append("~~");
			}
			it = psUsers.iterator();
			while (it.hasNext()) {
				UserInfo u = it.next();
				ps.append(Long.toString(u.getProfileId())).append("|").append(u.getFirstName()).append("|").append(
						u.getLastName()).append("|").append(ConversionUtility.convertToStoredProcRole(u.getRole()));
				if (it.hasNext())
					ps.append("~~");
			}
			StringBuffer internalUser = new StringBuffer();
			ContractEnrollmentVO contractEnrollmentVO = ContractServiceDelegate.getInstance().getContractInfoForEnrollment(contractId);
				if(contractEnrollmentVO != null && contractEnrollmentVO.getCarId() != null){
					// get user from LDAP
					UserInfo u = SecurityServiceDelegate.getInstance().
									searchBgaCarByUserName(contractEnrollmentVO.getCarId());
					if(u != null){
						internalUser.append(Long.toString(u.getProfileId())).append("|").append(u.getFirstName()).append("|").append(
								u.getLastName()).append("|").append(ConversionUtility.convertToStoredProcRole(u.getRole()));
					}
				}
			returnString.append("GetRecipientsResult:").append(tpa).append("{}").append(ps).append("{}").append(internalUser);
			return returnString.toString();
		} catch (ContractNotExistException e) {
			return "no results";
		}

	}

	static public String getMessageStatusText(HttpServletRequest request,
			MessageReportDetails detail) {
		StringBuffer buf = new StringBuffer("Message ID: ");
		buf.append(detail.getMessageId()).append("<BR>");
		
		buf.append("Status: ");
		if (StringUtils.equals(detail.getMessageActionType().getValue(),MessageActionType.FYI.getValue())
				&& StringUtils.equals(detail.getRecipientStatus(), "Y")) {
			buf.append(MCHistoryUtils.MessageStatus.REMOVEDFROMVIEW.getText()); // can
																				// use
																				// MCCarUtils
																				// cause
																				// they
																				// have
																				// bullets!
		} else if ((StringUtils.equals(detail.getMessageActionType().getValue(), MessageActionType.DECLARE_COMPLETE
				.getValue()) || StringUtils.equals(detail.getMessageActionType().getValue(), MessageActionType.ACTION
				.getValue()))
				&& StringUtils
						.equals(
								detail.getMessageStatus(),
								MessageReportData
										.getMessageStatusDisplay(com.manulife.pension.service.message.valueobject.Message.MessageStatus.ACTIVE))
				&& StringUtils.equals(detail.getRecipientStatus(), "Y")) {
			buf.append(MCHistoryUtils.MessageStatus.REMOVEDBYSTILLACTIVE.getText());
		} else {
			buf.append(detail.getMessageStatus());
		}
		buf.append("<br>");
		
		
		buf.append("Last modified: ");
		buf.append(new SimpleDateFormat(MCConstants.LastModifiedFormat).format(detail
				.getStatusTimestamp()));
		buf.append("<br>");
		buf.append("By: ");
	if ( detail.getStatusUserProfileId() < MCConstants.SystemProfileIdMax ) {
			buf.append("System");
		} else {
			buf.append(detail.getStatusUserLastName());
			buf.append(", ");
			buf.append(detail.getStatusUserFirstName());
		}
		return buf.toString();
	}

	static public String getMessageStatusAsJavascript(HttpServlet servlet) {
		StringBuffer returnString = new StringBuffer();
    	for(MCCarViewUtils.MessageStatus s : EnumSet.allOf(MCCarViewUtils.MessageStatus .class)) {
    		if (s != MCCarViewUtils.MessageStatus.REMOVEDFROMVIEW
					&& s != MCCarViewUtils.MessageStatus.REMOVEDBYSTILLACTIVE) {
				returnString.append("messageStatus.push(").append("new option('").append(s.getValue()).append("','")
						.append(s.getText()).append("'))\n");
			}
    	}
    	for (MCCarViewUtils.MessageStatus s : EnumSet.allOf(MCCarViewUtils.MessageStatus.class)) {
			returnString.append("messageStatusWithRecipient.push(").append("new option('").append(s.getValue())
					.append("','").append(s.getText()).append("'))\n");
		}
    	
    	return returnString.toString();
	}
	
	static public String getRecipientStatusDisplay(RecipientStatus status) {
		String value = RecipientStatusMap.get(status);
		return value == null ? "" : value;
	}
	static public String getPriorityDisplay(Priority priority) {
		String value = PriorityMap.get(priority);
		return value == null ? "" : value;
	}

	public static Collection<Integer> getUserContractIds(Integer userId, String role) throws SystemException {
		ReportCriteria reportCriteria = new ReportCriteria(SelectContractReportData.REPORT_ID);
		// default sort criteria
		reportCriteria.setPageNumber(1);
		reportCriteria.setPageSize(9999); // no restrictions
		reportCriteria.addFilter(SelectContractReportData.FILTER_CLIENT_ID, String.valueOf(userId));
		reportCriteria.addFilter(SelectContractReportData.FILTER_SITE_LOCATION, Environment.getInstance()
				.getDBSiteLocation());
		reportCriteria.addFilter(SelectContractReportData.FILTER_USER_ROLE, role);
		reportCriteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, null);
		reportCriteria.insertSort("contractName", ReportSort.ASC_DIRECTION);
		Collection<SelectContract> contracts;
		try {
			contracts = (Collection<SelectContract>)ReportServiceDelegate.getInstance().getReportData(reportCriteria).getDetails();
		} catch (ReportServiceException e) {
			throw new SystemException(e, "failure during call to report service in getMultipleContracts");
		} 
		Set<Integer> returnContracts = new HashSet<Integer>();
		for ( SelectContract selectContract : contracts ) {
			returnContracts.add(selectContract.getContractNumber());
		}
        return returnContracts;
		
	}

	/**
	 * Given a list of contract Ids, get the tpa firms associated with them.
	 * 
	 * @param contractIds
	 * @return
	 */
	public static Collection<Integer> getFirmIdsForContracts(Collection<Integer> contractIds) throws SystemException {
		Collection<Integer> returnCollection = new ArrayList<Integer>();
		for ( Integer contractId : contractIds) {
			TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractId);
			if ( firmInfo != null ) {
				returnCollection.add(firmInfo.getId());
			}
		}
		return returnCollection;
	}
}
