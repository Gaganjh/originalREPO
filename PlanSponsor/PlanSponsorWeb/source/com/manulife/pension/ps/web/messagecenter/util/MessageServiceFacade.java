package com.manulife.pension.ps.web.messagecenter.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.personalization.DisplayableMessageTemplate;
import com.manulife.pension.service.message.MessageService;
import com.manulife.pension.service.message.valueobject.EmailPreference;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageContainer;
import com.manulife.pension.service.message.valueobject.MessageContainerSummary;
import com.manulife.pension.service.message.valueobject.MessageDetail;
import com.manulife.pension.service.message.valueobject.MessagePreference;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError;
import com.manulife.pension.service.message.valueobject.Recipient;
import com.manulife.pension.service.message.valueobject.RecipientMessageInfo;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Facade to access MessageService for MessageCenter UI
 * 
 * @author guweigu
 * 
 */
public interface MessageServiceFacade {
	public Set<MessageContainerSummary> getAllSectionSummaries(HttpServletRequest request, 
			UserProfile userProfile, MessageCenterComponentId topId)
			throws SystemException;

	/**
	 * @param includeUrgent
	 *            
	 * @see MessageService#getAllTabSummaries(Recipient,
	 *      MessageCenterComponentId)
	 */
	public Set<MessageContainerSummary> getAllTabSummaries(HttpServletRequest request, 
			UserProfile userProfile, MessageCenterComponentId topId,
			boolean includeUrgent) throws SystemException;

	public List<MessageContainer> getDetails(List<ReportCriteria> criteria)
			throws SystemException;

	/**
	 * @see MessageService#getMessageCenterTree(String)
	 * 
	 */
	public MessageCenterComponent getMessageCenterComponentTree()
			throws SystemException;

	public MessageContainer getUrgentMessages(ReportCriteria criteria)
			throws SystemException;

	/**
	 * Remove the message for the user
	 * 
	 * @param userProfile
	 * @param messageId
	 */
	public void removeMessage(UserProfile userProfile, int messageId)
			throws SystemException;

	/**
	 * Remove the message for the user
	 * 
	 * @param userProfile
	 * @param messageId
	 */
	public void unRemoveMessage(UserProfile userProfile, int messageId)
			throws SystemException;

	/**
	 * Complete the message
	 * 
	 * @param userProfile
	 * @param messageId
	 */
	public void completeMessage(UserProfile userProfile, int messageId)
			throws SystemException;

	/**
	 * Undo Complete the message
	 * 
	 * @param userProfile
	 * @param messageId
	 */
	public void undoCompleteMessage(UserProfile userProfile, int messageId)
			throws SystemException;

	/**
	 * Set the recipient message status as Visited
	 * 
	 * @param userProfile
	 * @param messageId
	 */
	public void visitMessage(UserProfile userProfile, int messageId)
			throws SystemException;

	/**
	 * Retrieve the MessageTemplates in the section id map
	 * 
	 * @param topId
	 * @return
	 */
	public Map<MessageCenterComponentId, DisplayableMessageTemplate[]> getMessageTemplates(
			MessageCenterComponentId topId) throws SystemException;

	/**
	 * Retrieve the message preference for a user
	 * 
	 * @param role The role user's prefs.
	 * @param userProfileId The id of the user's prefs
	 * @param contractId The contract id for the prefs
	 * @param accessibleTemplates The list of accessible templates
	 * @return
	 * @throws SystemException
	 */
	public List<MessagePreference> getContractMessagePreference(UserRole role, long userProfileId, int contractId,
			Set<Integer> accessibleTemplates) throws SystemException;
	/**
	 * Retrieve the message preference for a user
	 * @param role The Users's role. ( the user we are tyring to retrieve the prefs for )
	 * @param userProfileId The user id of the user we are retrieving prefs for
	 * @param accessibleTemplates The accessible templates for the user
	 * @return
	 * @throws SystemException
	 */
	public List<MessagePreference> getTpaFirmMessagePreference(UserRole role, long userProfileId, Set<Integer> accessibleTemplates)
			throws SystemException;

	/**
	 * Retrieve the message preference for a user
	 * 
	 * @param userProfile
	 * @return
	 */
	public List<MessagePreference> getMessagePreferenceFromContract(UserProfile userProfile, int contractId)
			throws SystemException;

	/**
	 * Update u users message preferences 
	 * 
	 * @param userProfile The user making the update
	 * @param userProfileId The user being updated
	 * @param preferences The list of preferences
	 * @return
	 * @throws SystemException
	 */
	public List<MessagePreferenceError> updateContractMessagePreference(UserProfile userProfile, long userProfileId,
			List<MessagePreference> preferences) throws SystemException;
	
	/**
	 * Update the message preference for a user
	 * 
	 * @param userProfile The user making the update
	 * @param userProfileId The user being updated
	 * @param preferences The list of preferences
	 * @return
	 * @throws SystemException
	 */
	public List<MessagePreferenceError> updateTpaFirmMessagePreference(UserProfile userProfile, long userProfileId,
			List<MessagePreference> preferences) throws SystemException;	

	/**
	 * Update the message preference for a user
	 * 
	 * @param userProfile
	 * @return
	 */
	public List<MessagePreferenceError> updateContractMessagePreferencesToAllContracts(UserProfile userProfile,
			List<Integer> contracts, List<MessagePreference> preferences) throws SystemException;
	
	/**
	 * Retrieves email preference for a user profile
	 * 
	 * @param userProfile
	 * @return
	 */
	public EmailPreference getEmailPreference(UserProfile userProfile)
			throws SystemException;
	

	/**
	 * @param userProfile logged in users profile
	 * @param userProfileId the user whose prefs we are retreiving
	 * @return
	 * @throws SystemException
	 */
	public EmailPreference getEmailPreference(UserProfile userProfile, long userProfileId)
			throws SystemException;	

	/**
	 * Update email preference for a user
	 * 
	 * @param principal The user doing the updating
	 * @param userProfile The user being updated
	 * @param preference The map of preferences
	 */
	public void updateEmailPreference(Principal principal, Long userProfileId, 
			EmailPreference preference) throws SystemException;

	/**
	 * Retrieve a Recipient message for a message id and UserProfile
	 * @param userProfile 
	 */
	public RecipientMessageInfo getRecipientMessageById(int messageId, UserProfile userProfile)
			throws SystemException;
	
	/**
	 * Retrieve all the accessible message template ids for a user for a 
	 * selected contract.
	 * 
	 * @param userProfile
	 * @return
	 * @throws SystemException If the user hasn't select a contract
	 */
	public Set<Integer> getAccessibleContractMessageTemplates(UserProfile userProfile)
			throws SystemException;
	
	/**
	 * Retrieve all the accessible message template ids for a user for a 
	 * given contract.  this is used by internal users for viewing
	 * an external users preferences
	 * 
	 * @param userProfile
	 * @return
	 * @throws SystemException If the user hasn't select a contract
	 */
	public Set<Integer> getAccessibleContractMessageTemplates(Principal principal, UserInfo userInfo, int contractId)
			throws SystemException;	

	/**
	 * Retrieve all the accessible message template ids for a tpa user for
	 * all of their firms
	 * 
	 * @param userProfile
	 * @return
	 * @throws SystemException If the user hasn't select a contract
	 */
	public Set<Integer> getAccessibleTpaFirmMessageTemplates(UserProfile userProfile)
			throws SystemException;
	
	/**
	 * Retrieve all the accessible message template ids for a tpa user for
	 * all of their firms.  This is used by internal users for viewing external users prefs.
	 * 
	 * @param userProfile
	 * @return
	 * @throws SystemException If the user hasn't select a contract
	 */
	public Set<Integer> getAccessibleTpaFirmMessageTemplates(Principal principal, UserInfo userInfo)
			throws SystemException;

	/**
	 * Returns the message counts (urgent, action, fyi) for a user
	 * within a list of contracts
	 * @param profileId
	 * @param contractIds
	 * @return
	 * @throws SystemException
	 */
	public Map<Integer, int[]> getUserMessageCounts(long profileId,
			List<Integer> contractIds) throws SystemException;

	/**
	 * Returns the message counts (urgent, action, fyi) for a user within current site
	 * @param profileId
	 * @param contractIds
	 * @return
	 * @throws SystemException
	 */
	public int[] getTotalMessageCountForUserContracts(UserProfile userProfile) throws SystemException;

	/**
	 * Returns the message details for a given message
	 * @param messageId The id of the message
	 * @param contractId The contract id for the message 
	 * @param userProfileId The logged in user profile id
	 * @param applicationId the application id
	 * @return The {@link MessageDetail} value object
	 * @throws SystemException
	 */
	public MessageDetail getMessageDetails(int messageId, int userProfileId, String applicationId)
			throws SystemException ;
	
	/**
	 * Retrieve a list of possible recipients for a contract.
	 * 
	 * @param contractId The contract the recipients have access to
	 * @return The list recipients
	 * @throws SystemException
	 */
	public Collection<UserInfo> getRecipients(int contractId) throws SystemException;

	/**
	 * Returns a set of message template ids for the logged in user, 
	 * for the current contract ( which is retrieved from the userProfile object)
	 * 
	 * @param userProfile  The user profile of the logged in user
	 * @return A set of message template id's currently in use by the user for the current contract
	 * @throws SystemException thrown by the underlying system
	 */
	public Set<Integer> getActiveMessageTemplatesForUser(UserProfile userProfile) throws SystemException;

	/**
	 * Returns the message counts (urgent, action, fyi) for a user's tpa firm messages
	 * @param profile
	 * @return
	 * @throws SecurityServiceException
	 * @throws SystemException
	 */
	public int[] getTotalMessageCountForUserFirms(UserProfile profile) throws SystemException;
	
	/**
	 * Retrieve Notice preference for the user profile 
	 * @param userProfile
	 * @return
	 * @throws SystemException
	 */
	public List<UserNoticeManagerAlertVO> getNoticePreferences(BigDecimal profileId,Integer contactId) throws SystemException;
	
	/**
	 * Add the user Notice preference added by user
	 * @param userProfile
	 * @return
	 * @throws SystemException
	 */
	public int addUserNoticePreferences(List<UserNoticeManagerAlertVO> userNoticePreference) throws SystemException;
	
	/**
	 * Check if alert name already exists
	 * @param alertname
	 * @return
	 * @throws SystemException
	 */
	public boolean checkAlertNameExists(String alertName, Integer alertId, Integer contractId) throws SystemException;
	/**
	 * delete the alert
	 * @param alertname
	 * @return
	 * @throws SystemException
	 */
	public boolean deleteAlert(Integer alertId) throws SystemException;
	/**
	 * Add WebPageVisitLog
	 * @param alertname
	 * @return
	 * @throws SystemException
	 */
	public boolean userActionLog(Integer contractId,BigDecimal profileId,String userAction) throws SystemException;
	/**
	 * Get the existing notice manager alert details
	 * @param alertId
	 * @return UserNoticeManagerAlertVO
	 * @throws SystemException
	 */
	public UserNoticeManagerAlertVO getExistingAlertDetails(int alertId) throws SystemException;
	
}
