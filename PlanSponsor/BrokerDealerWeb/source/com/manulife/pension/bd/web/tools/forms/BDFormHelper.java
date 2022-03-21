package com.manulife.pension.bd.web.tools.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.content.view.MutableBDForm;
import com.manulife.pension.delegate.BDSystemSecurityServiceDelegate;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRule;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;

/**
 * A helper class for validating the bd forms content - related to firm
 * Inclusion content list.
 * 
 * @author AAmbrose
 */
public class BDFormHelper {

	private ArrayList<MutableBDForm> validBDForms = new ArrayList<MutableBDForm>();

	private ArrayList<MutableBDForm> firmInclusionBDForms = new ArrayList<MutableBDForm>();

	private Map<Integer, ContentFirmRestrictionRule> values = new HashMap<Integer, ContentFirmRestrictionRule>();

	private Set<Integer> bdFormsContentIds = new HashSet<Integer>();

	public BDFormHelper() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param BDUserProfile
	 *            userProfile
	 * @param BDUserProfile
	 *            bdFormList
	 */
	public BDFormHelper(BDUserProfile userProfile, Collection bdFormList)
			throws SecurityServiceException, SystemException {

		populateValidBDForms(userProfile, bdFormList);
	}

	/**
	 * To populate the valid bdforms for the current user
	 * 
	 * @param BDUserProfile
	 *            userProfile
	 * @param Collection
	 *            bdFormList
	 */
	public void populateValidBDForms(BDUserProfile bdUserProfile,
			Collection bdFormList) throws SecurityServiceException,
			SystemException {

		Iterator iter = bdFormList.iterator();
		// For internal user no validation otherwise we have to validate Content
		// Firm Restriction Rule
		if (bdUserProfile.isInternalUser()) {
			while (iter.hasNext()) {
				MutableBDForm bdFormContent = (MutableBDForm) iter.next();
				validBDForms.add(bdFormContent);
			}
		} else {
			while (iter.hasNext()) {
				MutableBDForm bdFormContent = (MutableBDForm) iter.next();
				if (BDConstants.BD_FIRM_RESTRICTION_CODE.equals(bdFormContent
						.getRestrictionCode())) {
					bdFormsContentIds.add(bdFormContent.getKey());
					firmInclusionBDForms.add(bdFormContent);
				} else {
					validBDForms.add(bdFormContent);
				}
			}
			if (bdFormsContentIds != null && !bdFormsContentIds.isEmpty()) {
				this.values = BDSystemSecurityServiceDelegate.getInstance()
						.getContentRestrictionRules(bdFormsContentIds);
			}
			populateFirmInclusionBDForms(bdUserProfile);
		}
	}

	/**
	 * @param BDUserProfile
	 *            bdUserProfile
	 */
	public void populateFirmInclusionBDForms(BDUserProfile bdUserProfile)
			throws SecurityServiceException, SystemException {

		List<Long> bdFirmIds = new ArrayList<Long>();
		List<BrokerDealerFirm> brokerDealerFirms = new ArrayList<BrokerDealerFirm>();
		ExtendedBDExtUserProfile extUser;

		BDPrincipal user = bdUserProfile.getBDPrincipal();
		extUser = BDUserSecurityServiceDelegate.getInstance()
				.getExtendedBDExtUserProfile(user);
		brokerDealerFirms = extUser.getBrokerDealerFirms();
		bdFirmIds = getBrokerDealerFirmIds(brokerDealerFirms);

		if (values != null && !values.isEmpty() && firmInclusionBDForms != null
				&& !firmInclusionBDForms.isEmpty()) {
			for (MutableBDForm mutableBDForm : firmInclusionBDForms) {
				ContentFirmRestrictionRule contentFirmRestrictionRule = values
						.get(mutableBDForm.getKey());
				if (contentFirmRestrictionRule != null
						&& contentFirmRestrictionRule.isAllowed(bdFirmIds)) {
					validBDForms.add(mutableBDForm);
				} else if (contentFirmRestrictionRule == null) {
					// No Firm Restriction rule and Firm Inclusion Rule for the
					// given ids
					validBDForms.add(mutableBDForm);
				}
			}
		} else if (values == null || values.isEmpty()) {
			// No Firm Restriction rule and Firm Inclusion Rule for the list of
			// ids
			if (firmInclusionBDForms != null && !firmInclusionBDForms.isEmpty()) {
				for (MutableBDForm mutableBDForm : firmInclusionBDForms) {
					validBDForms.add(mutableBDForm);
				}
			}
		}
	}

	/**
	 * @return the MutableBDForm validBDForms
	 */
	public ArrayList<MutableBDForm> getValidBDForms() {
		return this.validBDForms;
	}

	/**
	 * 
	 * @return Set<String> to build the first tab name.
	 */
	public String getBDFormTabNavigationList() {
		String strTabName = "";
		Set<String> bdFormTabNavigationList = new TreeSet<String>();
		if (validBDForms != null && !validBDForms.isEmpty()) {
			for (MutableBDForm mutableBDFormItem : validBDForms) {
				bdFormTabNavigationList.add(mutableBDFormItem.getCategory());
			}
			strTabName = bdFormTabNavigationList.iterator().next();
		}
		return strTabName;
	}

	/**
	 * return the list of broker Dealer Firms ids
	 * 
	 * @param List
	 *            <BrokerDealerFirm> brokerDealerFirms
	 * @return List<Long>
	 */
	public List<Long> getBrokerDealerFirmIds(
			List<BrokerDealerFirm> brokerDealerFirms) {
		List<Long> ids = new ArrayList<Long>(brokerDealerFirms.size());
		for (BrokerDealerFirm bf : brokerDealerFirms) {
			ids.add(bf.getId());
		}
		return ids;
	}
}
