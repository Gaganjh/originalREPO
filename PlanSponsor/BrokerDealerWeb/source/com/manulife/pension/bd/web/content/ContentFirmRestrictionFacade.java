package com.manulife.pension.bd.web.content;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.manulife.pension.delegate.BDSystemSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRule;
import com.manulife.pension.service.security.exception.SecurityServiceException;

/**
 * The facade to the content firm restriction rules.
 * 
 * make it possible for caching...
 * 
 * @author guweigu
 * 
 */
public class ContentFirmRestrictionFacade {
	private static ContentFirmRestrictionFacade instance = new ContentFirmRestrictionFacade();

	protected ContentFirmRestrictionFacade() {
	}

	static public ContentFirmRestrictionFacade getInstance() {
		return instance;
	}

	public Set<ContentFirmRestrictionRule> getContentRestictionRules()
			throws SystemException, SecurityServiceException {
		return BDSystemSecurityServiceDelegate.getInstance()
				.getContentRestrictionRules();
	}

	/**
	 * Returns the content firm restriction rule for one content
	 */
	public ContentFirmRestrictionRule getContentRestictionRule(int contentId)
			throws SystemException {
		Set<Integer> ids = new HashSet<Integer>(1);
		ids.add(contentId);
		Map<Integer, ContentFirmRestrictionRule> values = BDSystemSecurityServiceDelegate
				.getInstance().getContentRestrictionRules(ids);
		return values.get(contentId);
	}

}
