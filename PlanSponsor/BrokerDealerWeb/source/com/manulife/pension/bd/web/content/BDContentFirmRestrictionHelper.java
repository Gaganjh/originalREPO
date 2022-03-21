package com.manulife.pension.bd.web.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.view.MutableContent;
import com.manulife.pension.delegate.BDSystemSecurityServiceDelegate;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRule;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * This is a helper class to perform content firm restriction related tasks.
 * 
 * @author Ilamparithi
 * 
 */
public class BDContentFirmRestrictionHelper {
    
    private static final String ORDER_NUMBER_PROPERTY = "orderNumber";

    /**
     * Returns the allowed current contents of the given content group for the given user
     * 
     * @param userProfile
     * @param contentGroupId
     * @return List<Content>
     * @throws ContentException
     * @throws SystemException
     * @throws SecurityServiceException
     */
    public static List<Content> getCurrentAllowedContents(BDUserProfile userProfile,
            int contentGroupId) throws ContentException, SystemException, SecurityServiceException {
        return getAllowedContents(userProfile, getCurrentUpdatesByContentGroup(contentGroupId));
    }

    /**
     * Returns the allowed all contents of the given content group for the given user
     * 
     * @param userProfile
     * @param contentGroupId
     * @return List<Content>
     * @throws ContentException
     * @throws SystemException
     * @throws SecurityServiceException
     */
    public static List<Content> getAllAllowedContents(BDUserProfile userProfile, int contentGroupId)
            throws ContentException, SystemException, SecurityServiceException {
        return getAllowedContents(userProfile, getAllUpdatesByContentGroup(contentGroupId));
    }

    /**
     * Returns a list of allowed contents for the current user
     * 
     * @param userProfile
     * @param contents
     * @return List<Content>
     * @throws SecurityServiceException
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    public static List<Content> getAllowedContents(BDUserProfile userProfile, List<Content> contents)
            throws SecurityServiceException, SystemException {
        List<Content> allowedContents = new ArrayList<Content>();
        List<Content> contentsWithRestriction = new ArrayList<Content>();
        Set<Integer> contentIdsWithRestriction = new TreeSet<Integer>();
        if (userProfile.isInternalUser()) { // No restriction for internal users
            allowedContents.addAll(contents);
        } else { // For external users
            for (Content content : contents) {
                MutableContent mutableContent = (MutableContent) content;
                if (StringUtils.equals(mutableContent.getRestrictionCode(),
                        BDConstants.BD_FIRM_RESTRICTION_CODE)) {
                    // contents with restriction
                    contentIdsWithRestriction.add(content.getKey());
                    contentsWithRestriction.add(content);

                } else {
                    // contents with no restriction
                    allowedContents.add(content);
                }
            }
            // Retrieving existing rules for the restricted content ids
            Map<Integer, ContentFirmRestrictionRule> rules = BDSystemSecurityServiceDelegate
                    .getInstance().getContentRestrictionRules(contentIdsWithRestriction);
            // Finding allowed contents for the current user out of the restricted contents, by
            // passing the associated firms of the current user
            for (Content content : contentsWithRestriction) {
                if (rules.containsKey(content.getKey())) { // Rule exists for the restricted
                    // content
                    ContentFirmRestrictionRule rule = rules.get(content.getKey());
                    if (rule.isAllowed(getBDFirmIds(userProfile))) {
                        allowedContents.add(content);
                    }
                } else { // No rule exists
                    allowedContents.add(content);
                }
            }
        }
        // Reordering the contents as the order will be changed while filtering the allowed contents
        BeanComparator comparator = new BeanComparator(ORDER_NUMBER_PROPERTY);
        Collections.sort(allowedContents, comparator);
        return allowedContents;
    }

    /**
     * Returns the list of broker Dealer Firms ids associated with the given external user
     * 
     * @param userProfile
     * @return List<Long>
     */
    public static List<Long> getBDFirmIds(BDUserProfile userProfile)
            throws SecurityServiceException, SystemException {
        List<BrokerDealerFirm> brokerDealerFirms = new ArrayList<BrokerDealerFirm>();
        BDPrincipal user = userProfile.getBDPrincipal();
        ExtendedBDExtUserProfile extUser = BDUserSecurityServiceDelegate.getInstance()
                .getExtendedBDExtUserProfile(user);
        brokerDealerFirms = extUser.getBrokerDealerFirms();
        List<Long> firmIds = new ArrayList<Long>(brokerDealerFirms.size());
        for (BrokerDealerFirm bf : brokerDealerFirms) {
            firmIds.add(bf.getId());
        }
        return firmIds;
    }

    /**
     * Returns a list of Current Updates based on the Content Group
     * 
     * @param contentGroupId
     * @return List<Content>
     * @throws ContentException
     */
    public static List<Content> getCurrentUpdatesByContentGroup(int contentGroupId)
            throws ContentException {
        List<Content> currentUpdates = Arrays.asList((BrowseServiceDelegate.getInstance())
                .findContent(false, ContentTypeManager.instance().UPDATE));
        List<Content> currentUpdatesByGroup = new ArrayList<Content>();
        for (Content content : currentUpdates) {
            MutableContent mutableContent = (MutableContent) content;
            if (mutableContent.getParentId() == contentGroupId) {
                currentUpdatesByGroup.add(content);
            }
        }
        return currentUpdatesByGroup;
    }

    /**
     * Returns all updates available based on the Content Group
     * 
     * @param contentGroupId
     * @return List<Content>
     * @throws ContentException
     */
    public static List<Content> getAllUpdatesByContentGroup(int contentGroupId)
            throws ContentException {
        List<Content> allUpdates = Arrays.asList((ContentCacheManager.getInstance())
                .getContentByType(ContentTypeManager.instance().UPDATE));
        List<Content> allContentsByGroup = new ArrayList<Content>();
        for (Content content : allUpdates) {
            MutableContent mutableContent = (MutableContent) content;
            if (mutableContent.getParentId() == contentGroupId) {
                allContentsByGroup.add(content);
            }
        }
        return allContentsByGroup;
    }
    
    /**
     * This method checks whether a particular content exists in the list of contents retrieved from
     * CMA by looking for the given content key in the list
     * 
     * @param contentId
     * @param contents
     * @return boolean
     */
    public static boolean isRestrictedContentExists(int contentId, List<Content> contents) {
        for (Content content : contents) {
            if (content.getKey() == contentId) {
                return true;
            }
        }
        return false;
    }

}
