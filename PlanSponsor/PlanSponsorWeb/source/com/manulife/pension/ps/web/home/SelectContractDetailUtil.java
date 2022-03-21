package com.manulife.pension.ps.web.home;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.utility.SecurityHelper;
import com.manulife.pension.service.security.valueobject.ContractPermission;

/**
 * Common logic for selecting a contrac into a user profile
 * 
 * @author guweigu
 * 
 */
public class SelectContractDetailUtil {
	private static Logger log = Logger
			.getLogger(SelectContractDetailUtil.class);

	private static final String DEFAULT_SORT_FIELD = "contractNumber";
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static ReportSortList sortList = new ReportSortList();
	private final static int MAX_CONTRACT = 9999;
	
	static {
		sortList
				.add(new ReportSort(DEFAULT_SORT_FIELD, DEFAULT_SORT_DIRECTION));
	}

	static public void selectContract(UserProfile profile, int contractNumber)
			throws SystemException {
		UserRole role = profile.getRole();
		int diDuration = EnvironmentServiceDelegate.getInstance()
				.retrieveContractDiDuration(role, contractNumber,
						null);
		Contract contractDetails = null;
		
		try {
			contractDetails = ContractServiceDelegate.getInstance()
					.getContractDetails(contractNumber, diDuration);
		} catch (ContractNotExistException ce) {
			SystemException se = new SystemException(ce,
					SelectContractDetailUtil.class.getName(), "doExecute", ce
							.getMessage());
			throw se;
		}

		// lazy load the contract dates for the current contract
		if (contractDetails.getContractDates() == null)
			contractDetails.setContractDates(EnvironmentServiceDelegate
					.getInstance(Constants.PS_APPLICATION_ID).getContractDates(
							contractDetails.getContractNumber()));

		profile.setCurrentContract(contractDetails);
		profile.setPrincipal(SecurityServiceDelegate.getInstance()
				.getRoleForContract(profile.getPrincipal(),
						contractDetails.getContractNumber()));
		if (profile.getRole().isTPA()) {
			TPAFirmInfo tpaFirm = TPAServiceDelegate.getInstance()
					.getFirmInfoByContractId(
							contractDetails.getContractNumber());
			profile.getRole().setTpafirmContractPermission(
					tpaFirm.getContractPermission().getRole().getPermissions());
			ContractPermission tpaContractPermission = tpaFirm
					.getUseridToContractPermission().get((int) profile.getPrincipal().getProfileId());
			if ( tpaContractPermission != null ) {
				profile.getRole().setTpaUserContractPermissions(tpaContractPermission.getRole().getPermissions());
			}
        } else {
            // This is called to add additional dynamic permissions to a user based on Contract & Role for other roles.
            // Added for Bundled GA - Sept 2012
            SecurityHelper.getUserRoleForContract(profile.getPrincipal(), profile.getRole(), contractDetails.getContractNumber());
        }

		initFundsForContract(profile, contractDetails);
		
	}
	/**
	 * The homepage action sets these 2 properties, and they are required for the
	 * census pages.  (this is a bad setup, since calling homepageAction is therefore required 
	 * before accessing other pages )
	 * Therefore, whenever we change the contract, we must setup any extra properties.
	 * 
	 * TODO This code should be removed, and refactored into the various census actions that require it.
	 * 
	 * @param userProfile
	 * @param contract
	 * @throws SystemException
	 */
	static public void initFundsForContract(UserProfile userProfile, Contract contract) throws SystemException {

		// lastly, setup 2 properties on the currentContract that may be
		// required by some of the pages:

		ContractSummaryVO contractSummaryVO = ContractServiceDelegate.getInstance().getContractSummary(
				contract.getContractNumber(), userProfile.getRole().isExternalUser());
		contract.setLoansTotalAmount(new Double(contractSummaryVO.getOutstandingLoans()));
		boolean hasLifecycle = false;
		// LS set hasLifecycle indicator
		// to do - should be replaced with a field in contract_cs
		List selectedFunds = contractSummaryVO.getSelectedFunds();
		Iterator fundsIteraror = selectedFunds.iterator();
		while (fundsIteraror.hasNext()) {
			String fundId = (String) fundsIteraror.next();
			if (FundVO.RISK_LIFECYCLE.equals(FundInfoCache.getRiskCategoryCode(fundId))) {
				hasLifecycle = true;
				break;
			}

		}
		contract.setHasLifecycle(hasLifecycle);
	}
}
