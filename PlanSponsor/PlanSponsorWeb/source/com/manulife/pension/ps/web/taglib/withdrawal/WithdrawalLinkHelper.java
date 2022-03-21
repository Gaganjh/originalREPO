package com.manulife.pension.ps.web.taglib.withdrawal;

import java.util.Date;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.SecurityHelper;

public class WithdrawalLinkHelper {
	   /**
     * Determine whether the link to the list page for Withdrawals should be displayed based on
     * Service Features contract provided.
     * 
     * @param userProfile
     * @return boolean
     * @throws NumberFormatException
     * @throws SystemException
     */
    public static boolean validateListAccessOnPSWView(UserProfile userProfile)
            throws NumberFormatException, SystemException, ApplicationException {

        boolean displayLink = true;

        Contract contract = getContract(userProfile);

        // Check CSFs
        displayLink = validateContractServiceFeatures(userProfile, contract);

        String contractStatus = contract.getStatus();
        // 2b) Contract must be in AC, CF, or DI status for a list link
        if (displayLink) {
            if (Contract.STATUS_ACTIVE_CONTRACT.equals(contractStatus)
                    || Contract.STATUS_CONTRACT_FROZEN.equals(contractStatus)
                    || Contract.STATUS_CONTRACT_DISCONTINUED.equals(contractStatus)) {
                // all okay
            } else {
                displayLink = false;
            }
        }

        // 3) User must be authorized:
        // internal users can only have access to the list
        // todo: Permissions and Roles will be changed.
        // b) User is a PSW user and has initiate request
        // c) user is a TPA user and has access to Initiate requests for the FIRM and the
        // FIRM has access to initiate requests for the contract
        // todo: add permission restrictions when available
        if (displayLink) {
            UserRole role = userProfile.getPrincipal().getRole();
            // TODO: FIXME Check permissions
            if ((role.isInternalUser() || (role.isPlanSponsor()) || (role.isTPA()))) {
                displayLink = true;
            } else {
                displayLink = false;
            }
        }

        return displayLink;
    }

    /**
     * Determine whether the link to the Create page for Withdrawals should be displayed based on
     * Service Features contract provided.
     * 
     * @param userProfile
     * @return boolean
     * @throws NumberFormatException
     * @throws SystemException
     */
    public static boolean validateCreateAccessOnPSWView(UserProfile userProfile)
            throws NumberFormatException, SystemException, ApplicationException {

        boolean displayLink = true;

        Contract contract = getContract(userProfile);
        String contractStatus = contract.getStatus();
        displayLink = validateContractServiceFeatures(userProfile, contract);

        // 2) Contract must be in AC or CF status for a create link
        if (displayLink) {
            if (Contract.STATUS_ACTIVE_CONTRACT.equals(contractStatus)
                    || Contract.STATUS_CONTRACT_FROZEN.equals(contractStatus)) {
                // all okay
            } else {
                displayLink = false;
            }
        }

        if (displayLink) {
            UserRole role = userProfile.getPrincipal().getRole();
            // TODO: FIXME Check permissions
            if (role.isPlanSponsor() || role.isTPA()) {
                displayLink = true;
            } else {
                displayLink = false;
            }
        }

        return displayLink;
    }

    /**
     * Determine whether the link to the create page for Withdrawals should be displayed on the TPA
     * view since it is contract-less.
     * 
     * @param userProfile
     * @return boolean
     * @throws NumberFormatException
     * @throws SystemException
     */
    public static boolean validateCreateAccessOnTPAView(UserProfile userProfile)
            throws NumberFormatException, SystemException {

        boolean displayLink = true;

        // Restrictions
        // TODO: FIXME waiting for Permissions
        // Must have initiate access for the user and TPA firm for any of their contracts
        if (displayLink) {
            UserRole role = userProfile.getPrincipal().getRole();
            // TODO: FIXME Check permissions
            if (role.isTPA()) {
                displayLink = true;
            } else {
                displayLink = false;
            }
        }
        return displayLink;
    }

    /**
     * Determine whether the link to the list page for Withdrawals should be displayed on the TPA
     * view since it is contract-less.
     * 
     * @param userProfile The user profile to check.
     * @return boolean True if the TPA is allowed access, otherwise false.
     * @throws SystemException If an exception occurs.
     */
    public static boolean validateListAccessOnTPAView(final UserProfile userProfile)
            throws SystemException {

        final UserRole role = userProfile.getPrincipal().getRole();
        if (!(role.isTPA())) {
            return false;
        } // fi

        // Must have initiate access for the user and TPA firm for any of their contracts
        return SecurityHelper.checkForAnyTpaFirmWithPermission(userProfile.getPrincipal(),
                PermissionType.VIEW_ALL_WITHDRAWALS);
    }

    /**
     * Validate the Contract Service Feature for the given Contract.
     * 
     * @param userProfile
     * @param contract
     * @return boolean
     * @throws NumberFormatException
     * @throws SystemException
     */
    private static boolean validateContractServiceFeatures(UserProfile userProfile, Contract contract)
            throws NumberFormatException, SystemException, ApplicationException {

        boolean displayLink = true;
        // Restrictions

        ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate
                .getInstance().getContractServiceFeature(contract.getContractNumber(),
                        ServiceFeatureConstants.IWITHDRAWALS_FEATURE);

        // 1) Contract must have Online Withdrawals turned on.
        if (csf == null) {
            displayLink = false;
        } else {

            boolean hasOnlineWithdrawal = ContractServiceFeature.internalToBoolean(csf.getValue());
            if (!hasOnlineWithdrawal) {
                displayLink = false;
            }
        }
        return displayLink;
    }

    /**
     * Get the Contract Details information.
     * 
     * @param userProfile
     * @return Contract
     * @throws NumberFormatException
     * @throws SystemException
     */
    private static Contract getContract(UserProfile userProfile) throws NumberFormatException,
            SystemException {
        ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
        Contract contract = userProfile.getCurrentContract();
		Date contractTerminationDate = contract.getContractStatusEffectiveDate();
		int contractNumber = contract.getContractNumber();
		UserRole role = userProfile.getRole();
		int diDuration = EnvironmentServiceDelegate.getInstance()
				.retrieveContractDiDuration(role, contractNumber,
						contractTerminationDate);
        try {
            contract = delegate.getContractDetails(contractNumber, diDuration);
        } catch (ContractNotExistException ce) {
            SystemException se = new SystemException(ce, "WithdrawalLinkHelper", "getContract",
                    ce.getMessage());
            throw se;
        }
        return contract;
    }
}
