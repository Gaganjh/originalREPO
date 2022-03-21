package com.manulife.pension.ps.web.messagecenter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.model.MCHomePageBoxModel;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.model.MCPreferencesHolder;
import com.manulife.pension.ps.web.messagecenter.model.MessageTemplateRepository;
import com.manulife.pension.ps.web.messagecenter.personalization.DisplayableMessageTemplate;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.message.report.valueobject.RecipientMessageReportData;
import com.manulife.pension.service.message.valueobject.MessageCategory;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentImpl;
import com.manulife.pension.service.message.valueobject.MessageContainer;
import com.manulife.pension.service.message.valueobject.MessageContainerSummary;
import com.manulife.pension.service.message.valueobject.MessageContainerSummaryImpl;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError.ErrorCode;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;
import com.manulife.pension.service.message.valueobject.MessageTemplate.Priority;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.BaseEnvironment;

/**
 * Utility class for MessageCenter
 * 
 * @author guweigu
 * 
 */
public class MCUtils implements MCConstants {
	private static Pattern alphaPattern = Pattern.compile("[a-zA-Z]");
	private static long HEARTBEAT_USER_ID;
	private static final String HEARTBEAT_USER_ID_NAME = "heartbeatUserId";
	private static int HEARTBEAT_SECTION_ID = 14;
	final static Logger logger = Logger.getLogger(MCUtils.class);
	
	 static {
	        try {
	            BaseEnvironment environment = new BaseEnvironment();
	            HEARTBEAT_USER_ID = Long.valueOf(environment.getNamingVariable(HEARTBEAT_USER_ID_NAME, null));
	           	           
	        } catch (Exception e) {
	        	SystemException se =  new SystemException("Could not retrieve heartbeat user id from naming");
	        	throw ExceptionHandlerUtility.wrap(se);
        }
	    }


	/**
	 * Retrieve the MessageCenter tree from the Application scope context If it
	 * is not there, call the Service Layer to retrieve
	 * 
	 * @param servlet
	 * @return
	 * @throws SystemException
	 */
	public static MessageCenterComponent getMessageCenterTree(
			ServletContext servlet) throws SystemException {
		MessageCenterComponent top = (MessageCenterComponent) servlet.getAttribute(AttrMessageCenterTree);
		top = MessageServiceFacadeFactory.getInstance(servlet)
				.getMessageCenterComponentTree();
		servlet.setAttribute(AttrMessageCenterTree, top);
		return top;
	}

	/**
	 * Get Application-wise Message-Template repository
	 * 
	 * @param servlet
	 * @param topId
	 * @return
	 * @throws SystemException
	 */
	public static MessageTemplateRepository getMessageTemplateRepository(
			ServletContext servlet, MessageCenterComponentId topId)
			throws SystemException {
		MessageTemplateRepository repository = (MessageTemplateRepository) servlet
				.getAttribute(AttrMessageTemplateRepository);
		if (repository == null) {

			Map<MessageCenterComponentId, DisplayableMessageTemplate[]> templateMap = MessageServiceFacadeFactory
					.getInstance(servlet).getMessageTemplates(topId);
			repository = new MessageTemplateRepository(templateMap);

			servlet.setAttribute(
					AttrMessageTemplateRepository, repository);
		}
		return repository;
	}

	/**
	 * Returns if a tab is a summary tab
	 * 
	 * @param tab
	 * @return
	 */
	public static boolean isSummaryTab(MessageCenterComponent tab) {
		return tab == null || (Integer) tab.getId().getValue() == 0;
	}

	/**
	 * Returns if a tabid is a summary tabid
	 * 
	 * @param tab
	 * @return
	 */
	public static boolean isSummaryTab(MessageCenterComponentId tabId) {
		return tabId == null || (Integer) tabId.getValue() == 0;
	}

	/**
	 * Utility method to get an Id as integer from Request
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static int getId(HttpServletRequest request, String paramName) {
		String idStr = request.getParameter(paramName);
		return getId(idStr);
	}

	/**
	 * Convert a integer string to an integer. If a invalid integer string,
	 * returns -1
	 * 
	 * @param idStr
	 * @return
	 */
	public static int getId(String idStr) {
		int id = -1;
		if (idStr != null) {
			try {
				id = Integer.parseInt(idStr);
			} catch (NumberFormatException e) {
			}
		}
		return id;
	}

	/**
	 * Return if the user is in the global context (without selecting a
	 * contract)
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isInGlobalContext(HttpServletRequest request) {
		UserProfile profile = SessionHelper.getUserProfile(request);
		return profile == null || profile.getCurrentContract() == null;
	}

	/**
	 * Returns the User's preference holder in HTTPSession.
	 * 
	 * @param request
	 * @return
	 */
	public static MCPreferencesHolder getPreferencesHolder(
			HttpServletRequest request) {
		MCPreferencesHolder userState = (MCPreferencesHolder) request
				.getSession().getAttribute(AttrUserState);
		if (userState == null) {
			userState = new MCPreferencesHolder();
			request.getSession().setAttribute(AttrUserState, userState);
		}
		return userState;
	}

	/**
	 * Returns the Preference from session depending on the context
	 * (selected contract or global)
	 * 
	 * @param request
	 * @return
	 */
	public static MCPreference getPreference(HttpServletRequest request) {
		MCPreferencesHolder userState = getPreferencesHolder(request);
		UserProfile profile = SessionHelper.getUserProfile(request);
		if (profile != null && profile.getCurrentContract() != null) {
			// return the contract preference
			return userState.getContractPreference(profile.getCurrentContract()
					.getContractNumber());
		} else {
			return userState.getGlobalPreference();
		}
	}

	/**
	 * Convenient method to get a MessaageContainerSummary object
	 * 
	 * @param id
	 * @param urgentCount
	 * @param actionCount
	 * @param fyiCount
	 * @return
	 */
	public static MessageContainerSummary getSummary(
			MessageCenterComponentId id, int urgentCount, int actionCount,
			int fyiCount) {
		EnumMap<MessageCategory, Integer> countMap = new EnumMap<MessageCategory, Integer>(
				MessageCategory.class);
		countMap.put(MessageCategory.URGENT, urgentCount);
		countMap.put(MessageCategory.ACTION, actionCount);
		countMap.put(MessageCategory.FYI, fyiCount);
		MessageContainerSummaryImpl res = new MessageContainerSummaryImpl(id);
		res.setCountMap(countMap);
		return res;
	}

	/**
	 * Return a string representation of an integer, if 0 returns ""
	 * 
	 * @param value
	 * @return
	 */
	public static String getCountDisplay(int value) {
		return value == 0 ? "" : Integer.toString(value);
	}

	/**
	 * Checked whether a message is urgnet
	 * @param message
	 * @return
	 */
	public static boolean isUrgent(RecipientMessageDetail message) {
		return Priority.URGENT.compareTo(message.getPriority()) == 0;
	}

	public static boolean isUrgent(Priority p) {
		return Priority.URGENT.compareTo(p) == 0;
	}

	public static String getPriorityDisplay(Priority priority) {
		if(Priority.URGENT.compareTo(priority) == 0) {
			return "Urgent";
		} else if (Priority.IMPORTANT_INFORMATION.compareTo(priority) == 0) {
			return "Important";
		} else {
			return "Normal";
		}
	}

	public static Date convertDate(String dateStr) {
		if (StringUtils.isEmpty(dateStr) || isAlphaPresent(dateStr)) {
			return null;
		}
		SimpleDateFormat f = new SimpleDateFormat(DateFormat);
		f.setLenient(false);
		try {
			Date date = f.parse(dateStr);
			if(isValidYear(date)){
				return date;
			}else{
				return null;
			}
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static boolean isValidYear(Date date){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
	    if(calendar.get(Calendar.YEAR) > 9999){
	    	return false;
	    }else{
	    	return true;
	    }
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat f = new SimpleDateFormat(DateFormat);
			return f.format(date);
		}
	}
	
	/**
	 * Convenient method to check if a message is eligible for done in a
	 * specific user view
	 * 
	 * @param role
	 * @param message
	 * @return
	 */
	public static boolean isEligbileForDone(UserRole role,
			RecipientMessageDetail message) {
		MessageActionType actionType = message.getMessageActionType();
		return !(role.isInternalUser()&& !(role instanceof BundledGaCAR))
				&& actionType != null
				&& MessageActionType.DECLARE_COMPLETE.compareTo(actionType) == 0;
	}
	
	/**
	 * Convenient method to check if a section is to be displayed
	 * 
	 * @param request
	 * @param section
	 * @return
	 */
	public static boolean isSectionDisplayed(HttpServletRequest request,
			MessageCenterComponent section) {
		Integer sectionId= (Integer)(section.getId().getValue());
		if(sectionId != HEARTBEAT_SECTION_ID || SessionHelper.getUserProfile(request).getPrincipal().getProfileId() == HEARTBEAT_USER_ID){
			 return true;
		 }
		else{
		 	 return false;
		}
	}

	/**
	 * Convenient method to check if a message is eligible for act in a specific
	 * user view
	 * 
	 * @param role
	 * @param message
	 * @return
	 */
	public static boolean isEligbileForAct(UserRole role,
			RecipientMessageDetail message) {
		MessageActionType actionType = message.getMessageActionType();
		return !(role.isInternalUser()&& !(role instanceof BundledGaCAR)) && actionType != null
				&& MessageActionType.ACTION.compareTo(actionType) == 0;
	}

	/**
	 * Based on whether the user is in a contract context, return either the
	 * current contract (as a single item list); or the accessible contracts as
	 * a list
	 * 
	 * @param userProfile
	 * @return
	 */
	public static Collection<Integer> getContractList(UserProfile userProfile) {
		Set<Integer> accessibleContracts = userProfile
				.getMessageCenterAccessibleContracts();
		Contract contract = userProfile.getCurrentContract();
		if (contract == null) {
			return accessibleContracts;
		} else {
			List<Integer> contracts = new ArrayList<Integer>(1);
			if (accessibleContracts != null
					&& accessibleContracts.contains(contract
							.getContractNumber())) {
				contracts.add(contract.getContractNumber());
			}
			return contracts;
		}
	}

	/**
	 * Retrieve a HomePage MCBox model
	 */
	public static MCHomePageBoxModel getHomePageBoxModel(HttpServletRequest request, ServletContext servlet,
			UserProfile userProfile) throws SystemException {
		MessageServiceFacade facade = MessageServiceFacadeFactory
				.getInstance(servlet);
		MessageCenterComponent top = getMessageCenterTree(servlet);
		Collection<Integer> accessibleContracts = getContractList(userProfile);
		if (!accessibleContracts.isEmpty()) {

			Set<MessageContainerSummary> summary = facade.getAllTabSummaries(request,
					userProfile, top.getId(), false);

			MessageContainer urgent = null;
			if (!userProfile.isInternalUser()) {
				ReportCriteria criteria = new ReportCriteria(
						RecipientMessageReportData.REPORT_ID);
				criteria.addFilter(
						RecipientMessageReportData.FILTER_APPLICATION_ID, top
								.getId().getValue());
				criteria.addFilter(
						RecipientMessageReportData.FILTER_CONTRACT_LIST,
						accessibleContracts);

				criteria.addFilter(
						RecipientMessageReportData.FILTER_USER_PROFILE_ID,
						userProfile.getPrincipal().getProfileId());
				criteria.addFilter(RecipientMessageReportData.FILTER_PRIORITY,
						Priority.URGENT);
				criteria.setPageNumber(1);
				criteria.setPageSize(MCEnvironment.getHomePageUrgentMessageCount());
				criteria.setSorts(MCHomePageBoxModel.SortList);
				urgent = facade.getUrgentMessages(criteria);
			}

			return new MCHomePageBoxModel(top, summary, urgent);
		} else {
			return null;
		}
	}

	private static Pattern pattern = Pattern.compile("\\{([^\\}]*)\\}");

	public static String[] parseParameterValues(String valueString) {
		List<String> vList = new ArrayList<String>();
		Matcher m = pattern.matcher(valueString);
		while (m.find()) {
			vList.add("<span class='parameter'>{" + m.group(1) + "}</span>");
		}
		String[] result = new String[vList.size()];
		vList.toArray(result);
		return result;
	}

	public static MessageTemplateRepository getContractMessageTemplateRepository(ServletContext servlet,
			HttpServletRequest request, MessageCenterComponentId topId, Set<Integer> accessibleTemplates)
			throws SystemException {
		HttpSession session = request.getSession();
		MessageTemplateRepository userRepository = (MessageTemplateRepository) session
				.getAttribute(AttrUserMessageTemplateRepository);
		if (userRepository == null) {
			MessageTemplateRepository globalRepository = MCUtils.getMessageTemplateRepository(servlet, topId);
			userRepository = globalRepository.filter(accessibleTemplates);
		}
		return userRepository;
	}
	
	
	public static MessageTemplateRepository getTpaFirmMessageTemplateRepository(
			ServletContext servlet, HttpServletRequest request,
			MessageCenterComponentId topId, Set<Integer> accessibleTemplates) throws SystemException {
		HttpSession session = request.getSession();
		MessageTemplateRepository userRepository = (MessageTemplateRepository) session
				.getAttribute(AttrUserMessageTemplateRepository);
		if (userRepository == null) {
			MessageTemplateRepository globalRepository = MCUtils
					.getMessageTemplateRepository(servlet, topId);
			userRepository = globalRepository.filter(accessibleTemplates);
		}
		return userRepository;
	}	

	private static boolean isAlphaPresent(String expression) {
		Matcher m = alphaPattern.matcher(expression);
		return m.find();
	}

	/**
	 * Check if there is can't turn off error for a message template
	 * 
	 * @param errors
	 * @param templateId
	 * @return
	 */
	public static boolean hasPreferenceDisplayIndError(
			List<MessagePreferenceError> errors, int templateId) {
		if (errors != null) {
			for (MessagePreferenceError e : errors) {
				if (e.getCode().compareTo(ErrorCode.CANNOT_TURN_OFF) == 0
						&& e.getMessageTemplateId() == templateId) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if there is can't turn off error for a message template
	 * 
	 * @param errors
	 * @param templateId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasPreferenceDisplayIndError(ServletRequest request,
			int templateId) {
		return hasPreferenceDisplayIndError(
				(List<MessagePreferenceError>) request
						.getAttribute(AttrPreferenceError), templateId);
	}

	/**
	 * Filter out none AE contract for Phase I
	 * 
	 * @param contracts
	 * @return
	 * @throws SystemException
	 */
/*	public static Set<Integer> getAccessibleContracts(Set<Integer> contracts)
			throws SystemException {
        
	    //    MessageCenterVersion is obsolete
//		if (MCEnvironment.getMessageCenterVersion() == PhaseIVersion) {
        
			// only AE contract works
			Set<Integer> results = new HashSet<Integer>();
			ContractServiceDelegate csd = ContractServiceDelegate.getInstance();
			for (Integer cId : contracts) {
				ContractServiceFeature feature;
				try {
					feature = csd.getContractServiceFeature(cId,
							ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
				} catch (ApplicationException e) {
					throw new SystemException(e,
							"Fail to check AE CSF for contract: " + cId);
				}
				if (ContractServiceFeature
						.internalToBoolean(feature.getValue())) {
					results.add(cId);
				}
			}
			return Collections.unmodifiableSet(results);
//		} else {
//			return contracts;
//		}
	}
*/
    
	/**
	 * Return whether the user has more than one message center
	 * accessible contracts
	 * @param profile
	 * @return
	 */
	public static boolean hasMoreThanOneMCContracts(UserProfile profile) {
		Set<Integer> contracts = profile.getMessageCenterAccessibleContracts();
		return contracts != null && contracts.size() > 1;
	}
	
	/**
	 * Return the filtered tab for display in message center summary page
	 * @param request
	 * @param userprofile
	 * @param top
	 * @return
	 * @throws SystemException
	 */
	public static MessageCenterComponent deleteNoticeManagerTab(HttpServletRequest request ,UserProfile userprofile,MessageCenterComponent top) throws SystemException {
		UserProfile userProfile  = SessionHelper.getUserProfile(request);
		try {
			//validate the conditions to show or hide to the tab
			if(!(MCUtils.isInGlobalContext(request))&& !(userProfile.getCurrentContract()==null)) {
				if  (userprofile.getRole()instanceof PayrollAdministrator 
						|| NoticeManagerUtility.validateProductRestriction(userProfile.getCurrentContract())
						|| NoticeManagerUtility.validateContractRestriction(userProfile.getCurrentContract())
						|| NoticeManagerUtility.validateDIStatus(userProfile.getCurrentContract(), userprofile.getRole()))
				{
					MessageCenterComponent[] tabs = top.getChildren();
					MessageCenterComponent component = top.getChild(new Integer(10));
					List<MessageCenterComponent> list = new ArrayList<MessageCenterComponent>(Arrays.asList(tabs));
					list.remove(component);
					MessageCenterComponentImpl finalTabList = (MessageCenterComponentImpl) top ;
					finalTabList.setChildren(list);
					top = finalTabList;
				}
				
			}
		} catch (ContractDoesNotExistException e) {
			logger.error("Exception happened for the validation with ",e);
		} 
		return top;
	}
}
