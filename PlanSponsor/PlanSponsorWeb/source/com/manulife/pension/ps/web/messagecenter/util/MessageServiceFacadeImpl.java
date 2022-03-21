package com.manulife.pension.ps.web.messagecenter.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.MessageServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SearchContractController;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.personalization.DisplayableMessageTemplate;
import com.manulife.pension.ps.web.messagecenter.personalization.DisplayableMessageTemplateImpl;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.message.valueobject.EmailPreference;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageContainer;
import com.manulife.pension.service.message.valueobject.MessageContainerSummary;
import com.manulife.pension.service.message.valueobject.MessageDetail;
import com.manulife.pension.service.message.valueobject.MessagePreference;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError;
import com.manulife.pension.service.message.valueobject.MessageTemplateSectionRel;
import com.manulife.pension.service.message.valueobject.RecipientMessageContext;
import com.manulife.pension.service.message.valueobject.RecipientMessageInfo;
import com.manulife.pension.service.message.valueobject.Message.MessageStatus;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent.Type;
import com.manulife.pension.service.message.valueobject.MessageRecipient.RecipientStatus;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.ConversionUtility;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.service.security.valueobject.UserPreferences;

/**
 * The implementation of MessageServiceFacade, which delegates the
 * MessageService.
 * 
 * @author guweigu
 *
 */
public class MessageServiceFacadeImpl implements MessageServiceFacade {
	private String applicationId;

	public MessageServiceFacadeImpl(String applicationId) {
		this.applicationId = applicationId;
	}

	private RecipientMessageContext getRecipientContext(HttpServletRequest request, UserProfile profile) {
		return new RecipientMessageContext(applicationId, profile
				.getPrincipal().getProfileId(), MCUtils
				.getContractList(profile), MCUtils.isInGlobalContext(request) ? profile.getMessageCenterTpaFirms() : null );
	}

	public Set<MessageContainerSummary> getAllSectionSummaries(HttpServletRequest request, 
			UserProfile userProfile, MessageCenterComponentId topId)
			throws SystemException {

		return MessageServiceDelegate
				.getInstance(this.applicationId)
				.getAllSectionSummaries(getRecipientContext(request, userProfile), topId);
	}

	public Set<MessageContainerSummary> getAllTabSummaries(HttpServletRequest request, 
			UserProfile userProfile, MessageCenterComponentId topId,
			boolean includeUrgent) throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId)
				.getAllTabSummaries(getRecipientContext(request, userProfile), topId,
						includeUrgent);
	}

	public List<MessageContainer> getDetails(List<ReportCriteria> criteriaList)
			throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId)
				.getSectionDetails(criteriaList);
	}

	public MessageCenterComponent getMessageCenterComponentTree()
			throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId)
				.getMessageCenterComponentTree();
	}

	public MessageContainer getUrgentMessages(ReportCriteria criteria)
			throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId)
				.getUrgentMessages(criteria);
	}

	public void completeMessage(UserProfile userProfile, int messageId)
			throws SystemException {
		if (userProfile.isInternalUser() && !userProfile.isBundledGACAR()) {
			throw new SystemException(
					"Internal user can not complete a message");
		}

		MessageServiceDelegate.getInstance(applicationId).updateMessageStatus(
				messageId, userProfile.getPrincipal().getProfileId(),
				MessageStatus.DONE);

		// The recipient status should be visited
		MessageServiceDelegate.getInstance(applicationId)
				.updateRecipientMessageStatus(
						userProfile.getPrincipal().getProfileId(), messageId,
						RecipientStatus.VISITED);
	}

	public void undoCompleteMessage(UserProfile userProfile, int messageId)
			throws SystemException {
		if (userProfile.isInternalUser() && !userProfile.isBundledGACAR()) {
			throw new SystemException(
					"Internal user can not undoComplete a message");
		}

		MessageServiceDelegate.getInstance(applicationId).updateMessageStatus(
				messageId, userProfile.getPrincipal().getProfileId(),
				MessageStatus.ACTIVE);

		// The recipient status should be visited
		MessageServiceDelegate.getInstance(applicationId)
				.updateRecipientMessageStatus(
						userProfile.getPrincipal().getProfileId(), messageId,
						RecipientStatus.VISITED);
	}

	public void removeMessage(UserProfile userProfile, int messageId)
			throws SystemException {
		if (userProfile.isInternalUser() && !userProfile.isBundledGACAR()) {
			throw new SystemException("Internal user can not remove a message");
		}
		MessageServiceDelegate.getInstance(applicationId)
				.updateRecipientMessageStatus(
						userProfile.getPrincipal().getProfileId(), messageId,
						RecipientStatus.HIDDEN);
	}

	public void unRemoveMessage(UserProfile userProfile, int messageId)
			throws SystemException {
		if (userProfile.isInternalUser()&& !userProfile.isBundledGACAR()) {
			throw new SystemException(
					"Internal user can not unremove a message");
		}
		MessageServiceDelegate.getInstance(applicationId)
				.updateRecipientMessageStatus(
						userProfile.getPrincipal().getProfileId(), messageId,
						RecipientStatus.VISITED);
	}

	public void visitMessage(UserProfile userProfile, int messageId)
			throws SystemException {
		if (userProfile.isInternalUser() && !userProfile.isBundledGACAR()) {
			throw new SystemException("Internal user can not visit a message");
		}

		MessageServiceDelegate.getInstance(applicationId)
				.updateRecipientMessageStatus(
						userProfile.getPrincipal().getProfileId(), messageId,
						RecipientStatus.VISITED);
	}

	public Map<MessageCenterComponentId, DisplayableMessageTemplate[]> getMessageTemplates(
			MessageCenterComponentId topId) throws SystemException {
		List<MessageTemplateSectionRel> rels = MessageServiceDelegate
				.getInstance(applicationId).getMessageTemplateSectionRel(
						(String) topId.getValue());
		Map<Integer, List<DisplayableMessageTemplateImpl>> results = new HashMap<Integer, List<DisplayableMessageTemplateImpl>>();
		for (MessageTemplateSectionRel rel : rels) {
			int sectionId = rel.getSectionId();
			int tid = rel.getTemplateId();
			List<DisplayableMessageTemplateImpl> list = results.get(sectionId);
			if (list == null) {
				list = new ArrayList<DisplayableMessageTemplateImpl>();
				results.put(sectionId, list);
			}
			try {

				DisplayableMessageTemplateImpl t = new DisplayableMessageTemplateImpl(
						tid, rel.getContentKey(), rel.getType());
				list.add(t);
			} catch (Exception e) {
				throw new SystemException(e, getClass().getName(),
						"getMessageTemplates", "Failed to obtain content.");
			}
		}
		Map<MessageCenterComponentId, DisplayableMessageTemplate[]> map = new HashMap<MessageCenterComponentId, DisplayableMessageTemplate[]>();
		for (Integer sid : results.keySet()) {
			MessageCenterComponentId mcId = new MessageCenterComponentId(
					Type.SECTION, sid);
			List<DisplayableMessageTemplateImpl> tList = results.get(sid);
			DisplayableMessageTemplateImpl[] ts = new DisplayableMessageTemplateImpl[tList
					.size()];
			tList.toArray(ts);
			map.put(mcId, ts);
		}
		return map;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade#getContractMessagePreference(com.manulife.pension.ps.web.controller.UserProfile)
	 */
	public List<MessagePreference> getContractMessagePreference(UserRole role, long userProfileId, int contractId,
			Set<Integer> accessibleTemplates) throws SystemException {

		return MessageServiceDelegate.getInstance(this.applicationId).getMessageContractPreferences(
				userProfileId, contractId,
				ConversionUtility.convertToStoredProcRole(role), accessibleTemplates);
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade#getTpaFirmMessagePreference(com.manulife.pension.ps.web.controller.UserProfile)
	 */
	public List<MessagePreference> getTpaFirmMessagePreference(UserRole userRole, long userProfileId,
			Set<Integer> accessibleTemplates) throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId).getMessagePreferences(
				userProfileId,
				ConversionUtility.convertToStoredProcRole(userRole), accessibleTemplates);
	}	

	public List<MessagePreferenceError> updateContractMessagePreference(
			UserProfile userProfile, long userProfileId, List<MessagePreference> preferences)
			throws SystemException {

		return MessageServiceDelegate.getInstance(applicationId)
				.updateContractMessagePreferences(
						userProfile.getPrincipal(), userProfileId, preferences);
	}
	public List<MessagePreferenceError> updateTpaFirmMessagePreference(
			UserProfile userProfile, long userProfileId, List<MessagePreference> preferences)
			throws SystemException {
		if ( userProfile.getRole().isTPA() &&  userProfile.getMessageCenterTpaFirms().size() == 0 ) {
			throw new SystemException(
			"There are no tpa firms for this user:" + userProfile.getPrincipal().getProfileId());
		}

		return MessageServiceDelegate.getInstance(applicationId).updateTpaFirmMessagePreferences(
				userProfile.getPrincipal(), userProfileId, preferences);
	}	

	public EmailPreference getEmailPreference(UserProfile userProfile)
			throws SystemException {
		if (userProfile.isInternalUser() && !userProfile.isBundledGACAR()) {
			throw new SystemException(
					"Internal user can not have email preference");
		}

		UserPreferences preferences = SecurityServiceDelegate.getInstance()
				.getUserPreferences(userProfile.getPrincipal());

		Map<String, String> map = new HashMap<String, String>(5);

		String urgent = preferences.get(UserPreferenceKeys.URGENT_MESSAGE_PREF,
				"");
		if (StringUtils.isEmpty(urgent)) {
			urgent = MCConstants.DefaultUrgentEmailFrequency;
			map.put(UserPreferenceKeys.URGENT_MESSAGE_PREF, urgent);
		}
		String normal = preferences.get(UserPreferenceKeys.NORMAL_MESSAGE_PREF,
				"");
		if (StringUtils.isEmpty(normal)) {
			normal = MCConstants.DefaultNormalEmailFrequency;
			map.put(UserPreferenceKeys.NORMAL_MESSAGE_PREF, normal);
		}

		String format = preferences.get(UserPreferenceKeys.EMAIL_FORMAT_PREF,
				"");
		if (StringUtils.isEmpty(format)) {
			format = MCConstants.DefaultEmailFormat;
			map.put(UserPreferenceKeys.EMAIL_FORMAT_PREF, format);
		}
		
		
		String emailWithNoMessage = preferences.get(
				UserPreferenceKeys.NOMEESAGE_EMAIL_PREF, "");
		if (StringUtils.isEmpty(emailWithNoMessage)) {
			emailWithNoMessage = MCConstants.DefaultNoMessageOption;
			map
					.put(UserPreferenceKeys.NOMEESAGE_EMAIL_PREF,
							emailWithNoMessage);
		}
		if (!map.isEmpty()) {
			SecurityServiceDelegate.getInstance().updateUserPreferences(
					userProfile.getPrincipal(), map);
		}
		return new EmailPreference(urgent, normal, StringUtils.equals("Y",
				emailWithNoMessage), format);
	}
	public EmailPreference getEmailPreference(UserProfile userProfile, long userProfileId) throws SystemException {
		if (!userProfile.isInternalUser()) {
			throw new SystemException("Only Internal user view other users preferences");
		}

		UserPreferences preferences = SecurityServiceDelegate.getInstance().getUserPreferences(
				userProfileId);

		Map<String, String> map = new HashMap<String, String>(5);

		String urgent = preferences.get(UserPreferenceKeys.URGENT_MESSAGE_PREF, "");
		if (StringUtils.isEmpty(urgent)) {
			urgent = MCConstants.DefaultUrgentEmailFrequency;
			map.put(UserPreferenceKeys.URGENT_MESSAGE_PREF, urgent);
		}
		String normal = preferences.get(UserPreferenceKeys.NORMAL_MESSAGE_PREF, "");
		if (StringUtils.isEmpty(normal)) {
			normal = MCConstants.DefaultNormalEmailFrequency;
			map.put(UserPreferenceKeys.NORMAL_MESSAGE_PREF, normal);
		}

		String format = preferences.get(UserPreferenceKeys.EMAIL_FORMAT_PREF, "");
		if (StringUtils.isEmpty(format)) {
			format = MCConstants.DefaultEmailFormat;
			map.put(UserPreferenceKeys.EMAIL_FORMAT_PREF, format);
		}

		String emailWithNoMessage = preferences.get(UserPreferenceKeys.NOMEESAGE_EMAIL_PREF, "");
		if (StringUtils.isEmpty(emailWithNoMessage)) {
			emailWithNoMessage = MCConstants.DefaultNoMessageOption;
			map.put(UserPreferenceKeys.NOMEESAGE_EMAIL_PREF, emailWithNoMessage);
		}
		return new EmailPreference(urgent, normal, StringUtils.equals("Y", emailWithNoMessage), format);
	}
	public void updateEmailPreference(Principal principal, Long userProfileId,
			EmailPreference preference) throws SystemException {
		Map<String, String> map = new HashMap<String, String>(3);
		map.put(UserPreferenceKeys.URGENT_MESSAGE_PREF, preference
				.getUrgentMessageFrequencyAsString());
		map.put(UserPreferenceKeys.NORMAL_MESSAGE_PREF, preference
				.getNormalMessageFrequencyAsString());
		map.put(UserPreferenceKeys.NOMEESAGE_EMAIL_PREF, preference
				.isEmailWithNoMessage() ? "Y" : "N");
		map.put(UserPreferenceKeys.EMAIL_FORMAT_PREF, preference.getFormatPreference().getValue());
		SecurityServiceDelegate.getInstance().updateUserPreferences(
				principal, userProfileId, map);
	}

	public RecipientMessageInfo getRecipientMessageById(int messageId,
			UserProfile userProfile) throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId)
				.getRecipientMessageById(messageId,
						userProfile.getPrincipal().getProfileId(),
						this.applicationId);
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade#getAccessibleContractMessageTemplates(com.manulife.pension.ps.web.controller.UserProfile)
	 */
	public Set<Integer> getAccessibleContractMessageTemplates(UserProfile userProfile)
			throws SystemException {
		Contract contract = userProfile.getCurrentContract();
		if (contract == null) {
			throw new SystemException(
					"Can not retrieve accessible contract message templates without selecting contract");
		}
		Set<PermissionType> userPermissionTypes = (Set<PermissionType>) 
			userProfile.getPrincipal().getRole().getPermissions();
		
		List<String> userPermissions = new ArrayList<String>();
		for (PermissionType permissions : userPermissionTypes) {
			userPermissions.add(permissions.getPermissionCode(permissions));
        }

		return MessageServiceDelegate.getInstance(this.applicationId)
			.getAccessibleContractMessageTemplates(this.applicationId,
				userProfile.getPrincipal().getProfileId(),
				contract.getContractNumber(),
				userProfile.getPrincipal().getRole().toString(),
				userPermissions,
				contract.isTpaStaffPlanInd());

	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade#getAccessibleTpaFirmMessageTemplates(com.manulife.pension.ps.web.controller.UserProfile)
	 */
	public Set<Integer> getAccessibleTpaFirmMessageTemplates(UserProfile userProfile) throws SystemException {
		Set<PermissionType> userPermissionTypes = (Set<PermissionType>) userProfile.getPrincipal().getRole()
				.getPermissions();

		List<String> userPermissions = new ArrayList<String>();
		for (PermissionType permissions : userPermissionTypes) {
			userPermissions.add(permissions.getPermissionCode(permissions));
		}
		int tpaFirmId = 0;
		if (userProfile.getMessageCenterTpaFirms().size() > 0) {
			tpaFirmId = userProfile.getMessageCenterTpaFirms().iterator().next();
		} else {
			throw new SystemException(
					"Can not retrieve accessible tpa firm message templates because this user does not belong to a firm");

		}

		return MessageServiceDelegate.getInstance(this.applicationId).getAccessibleTpaFirmMessageTemplates(
				this.applicationId, userProfile.getPrincipal().getProfileId(),
				userProfile.getPrincipal().getRole().toString(), userPermissions, tpaFirmId);

	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade#getAccessibleTpaFirmMessageTemplates(com.manulife.pension.ps.web.controller.UserProfile)
	 */
	public Set<Integer> getAccessibleTpaFirmMessageTemplates(Principal principal, UserInfo userInfo) throws SystemException {
		Contract contract = null;
		
		if ( !principal.getRole().isInternalUser() ) {
			throw new SystemException("This method is for internal users only");
		}

		UserRole userRole = userInfo.getRole();
		Set<PermissionType> userPermissionTypes = (Set<PermissionType>) 
			userRole.getPermissions();
		
		List<String> userPermissions = new ArrayList<String>();
		for (PermissionType permissions : userPermissionTypes) {
			userPermissions.add(permissions.getPermissionCode(permissions));
        }

		return MessageServiceDelegate.getInstance(this.applicationId)
			.getAccessibleTpaFirmMessageTemplates(this.applicationId,
				userInfo.getProfileId(),
				userRole.toString(),
				userPermissions,
				userInfo.getTpaFirmsAsCollection().iterator().next().getId());

	}	
	
	public Set<Integer> getActiveMessageTemplatesForUser(UserProfile userProfile)
			throws SystemException {
		Contract contract = userProfile.getCurrentContract();
		int contractNumber = -1;
		if (contract != null) {
			contractNumber = contract.getContractNumber();
		}
		return MessageServiceDelegate.getInstance(this.applicationId)
				.getActiveMessageTemplatesForUser(
						userProfile.getPrincipal().getProfileId(),
						contractNumber);
	}
	
	

	public Map<Integer, int[]> getUserMessageCounts(long profileId, List<Integer> contractIds) throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId).getUserMessageCounts(applicationId, profileId,
				contractIds);
	}

	public MessageDetail getMessageDetails(int messageId,int userProfileId, String applicationId)
			throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId).getMessageDetails(messageId,
				userProfileId, applicationId);
	}

	public int[] getTotalMessageCountForUserContracts(UserProfile userProfile)
			throws SystemException {
		Set<Integer> accessibleContracts = userProfile
				.getMessageCenterAccessibleContracts();
		if (accessibleContracts == null || accessibleContracts.isEmpty()) {
			return new int[] { 0, 0, 0 };
		} else {
			return MessageServiceDelegate.getInstance(this.applicationId)
					.getTotalMessageCountForUserContracts(applicationId,
							userProfile.getPrincipal().getProfileId(),
							accessibleContracts);
		}
	}

	public List<MessagePreference> getMessagePreferenceFromContract(
			UserProfile userProfile, int contractId) throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId)
				.getMessageContractPreferences(
						userProfile.getPrincipal().getProfileId(), contractId);
	}

	public List<MessagePreferenceError> updateContractMessagePreferencesToAllContracts(UserProfile userProfile,
			List<Integer> contracts, List<MessagePreference> preferences) throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId).updateContractMessagePreferencesToAllContracts(
				userProfile.getPrincipal(), userProfile.getPrincipal().getProfileId(), contracts, preferences);
	}

	public Collection<UserInfo> getRecipients(int contractId) throws SystemException {
		return MessageServiceDelegate.getInstance(this.applicationId).getRecipients(contractId);
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade#getTotalMessageCountForUserFirms(com.manulife.pension.ps.web.controller.UserProfile)
	 */
	public int[] getTotalMessageCountForUserFirms(UserProfile profile) throws SystemException {
		try {
			UserInfo userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(profile.getPrincipal(),
					profile.getPrincipal().getProfileId());
			if (userInfo.getTpaFirmsAsCollection().size() == 0 
			        || profile.getMessageCenterTpaFirms() == null 
                    || profile.getMessageCenterTpaFirms().isEmpty()) {
				return new int[] { 0, 0, 0 };
			}

			return MessageServiceDelegate.getInstance(this.applicationId).getTotalMessageCountForUserFirms(
					applicationId, profile.getPrincipal().getProfileId(), profile.getMessageCenterTpaFirms());
		} catch (SecurityServiceException e) {
			throw new SystemException(e, "error trying to get profile id for " + profile.getPrincipal().getProfileId());
		}
	}

	public Set<Integer> getAccessibleContractMessageTemplates(Principal principal, UserInfo userInfo, int contractId)
			throws SystemException {
		Contract contract = null;
		
		if ( !principal.getRole().isInternalUser() ) {
			throw new SystemException("This method is for internal users only");
		}
		try {
			contract = ContractServiceDelegate.getInstance().getContractDetails(contractId,
					EnvironmentServiceDelegate.getInstance()
					.retrieveContractDiDuration(principal.getRole(), 0,null));
		} catch (ContractNotExistException e) {
			throw new SystemException(e, "can't find contract, " + contractId);
		}
		if (contract == null) {
			throw new SystemException(
					"Can not retrieve accessible contract message templates without selecting contract");
		}
		UserRole userRole = userInfo.getRole();
		if (!(userRole.isTPA() || userRole instanceof BundledGaCAR)) {
			userRole = userInfo.getContractPermission(contractId).getRole();
		} else if (userRole.isTPA()) {
			userRole = SecurityServiceDelegate.getInstance().getTpaUserRoleForContract(userInfo, contractId);
		}
		
		Set<PermissionType> userPermissionTypes = (Set<PermissionType>) 
			userRole.getPermissions();
		
		List<String> userPermissions = new ArrayList<String>();
		for (PermissionType permissions : userPermissionTypes) {
			userPermissions.add(permissions.getPermissionCode(permissions));
        }

		return MessageServiceDelegate.getInstance(this.applicationId)
			.getAccessibleContractMessageTemplates(this.applicationId,
				userInfo.getProfileId(),
				contractId,
				userRole.toString(),
				userPermissions,
				contract.isTpaStaffPlanInd());

	}
	
	
	public List<UserNoticeManagerAlertVO> getNoticePreferences(BigDecimal profileId , Integer contractId)
			throws SystemException {
		List<UserNoticeManagerAlertVO> userNoticeManagerAlerts = PlanNoticeDocumentServiceDelegate.getInstance().getUserNoticePreferences(profileId, contractId);
		return userNoticeManagerAlerts;
	}
	
	public int addUserNoticePreferences(List<UserNoticeManagerAlertVO> userNoticePreference)
			throws SystemException {
		Integer alertAddedCount = 0;
		alertAddedCount = PlanNoticeDocumentServiceDelegate.getInstance().addUserNoticePreferences(userNoticePreference);
		return alertAddedCount;
	}
	
	public boolean checkAlertNameExists(String alertName,Integer alertId, Integer contractId)
			throws SystemException {
		boolean alertNameExists =false ;
		alertNameExists = PlanNoticeDocumentServiceDelegate.getInstance().checkAlertNameExists(alertName,alertId,contractId);
		return alertNameExists;
	}
	
	public boolean deleteAlert(Integer alertId)
			throws SystemException {
		boolean deleted =false ;
		deleted = PlanNoticeDocumentServiceDelegate.getInstance().deleteAlert(alertId);
		return deleted;
	}
	
	public boolean userActionLog(Integer contractId,BigDecimal profileId,String userAction)
			throws SystemException {
		boolean logged =false ;
		logged = PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId,profileId,userAction);
		return logged;
	}

	public UserNoticeManagerAlertVO getExistingAlertDetails(int alertId) throws SystemException {
		UserNoticeManagerAlertVO existingAlertVO =  PlanNoticeDocumentServiceDelegate.getInstance().getExistingAlertDetails(alertId);
		return existingAlertVO;
}

}
