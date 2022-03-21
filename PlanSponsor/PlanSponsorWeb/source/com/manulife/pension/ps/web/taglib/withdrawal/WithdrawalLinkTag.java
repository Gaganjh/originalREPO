	package com.manulife.pension.ps.web.taglib.withdrawal;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.SecurityHelper;

/**
 * @author shinchr
 * 
 */
public class WithdrawalLinkTag extends BodyTagSupport {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private int contractId;

    private String linkType;

    public int doStartTag() throws JspException {

        UserProfile userProfile = SessionHelper.getUserProfile((HttpServletRequest) pageContext
                .getRequest());
        try {

            boolean displayLink = true;

            if (Constants.LINKTYPE_CREATE_PSW.equals(getLinkType())) {
                displayLink = validateCreateAccessOnPSWView(userProfile);
            } else if (Constants.LINKTYPE_LIST_PSW.equals(getLinkType())) {
                displayLink = validateListAccessOnPSWView(userProfile);
            } else if (Constants.LINKTYPE_CREATE_TPA.equals(getLinkType())) {
                displayLink = validateCreateAccessOnTPAView(userProfile);
            } else if (Constants.LINKTYPE_LIST_TPA.equals(getLinkType())) {
                displayLink = validateListAccessOnTPAView(userProfile);
            } else if (Constants.LINKTYPE_CREATE_LOAN_PSW.equals(getLinkType())) {
                displayLink = validateCreateLoanAccessOnPSWView(userProfile);
            } else if (Constants.LINKTYPE_CREATE_LOAN_TPA.equals(getLinkType())) {
                displayLink = validateCreateLoanAccessOnTPAView(userProfile);
            } 

            if (displayLink) {
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }

        } catch (SystemException systemException) {
            throw new JspException("System Exception found.", systemException);
        } catch (Exception exception) {
            throw new JspException(exception);
        } // end try/catch
    }

    /**
     * Determine whether the link to the list page for Withdrawals should be displayed based on
     * Service Features contract provided.
     * 
     * @param userProfile
     * @return boolean
     * @throws NumberFormatException
     * @throws SystemException
     */
    private boolean validateListAccessOnPSWView(UserProfile userProfile)
            throws NumberFormatException, SystemException, ApplicationException {

        boolean displayLink = true;

        Contract contract = getContract(userProfile);

        // Check CSFs
        boolean checkWithdrawalCSF = validateContractServiceFeatures(userProfile, contract);
        boolean checkLoansCSF = validateLoansContractServiceFeature(userProfile, contract);
        
        if ( checkWithdrawalCSF || checkLoansCSF ) {
        	displayLink = true; 
        } else {
        	displayLink = false;
        }

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
        // b) User is a PSW user and has initiate request
        // c) user is a TPA user and has access to Initiate requests for the FIRM and the
        // FIRM has access to initiate requests for the contract
        if (displayLink) {
            UserRole role = userProfile.getPrincipal().getRole();           
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
    private boolean validateCreateAccessOnPSWView(UserProfile userProfile)
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
            if (role.isPlanSponsor() || role.isTPA()) {
                displayLink = true;
            } else if (contract.isBundledGaIndicator() && 
            		userProfile.getRole().hasPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE)){
					
				// Bundled GA CAR should be able to initiate withdrawal request
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
    private boolean validateCreateAccessOnTPAView(UserProfile userProfile)
            throws NumberFormatException, SystemException {

        boolean displayLink = true;

      
       
        // Must have initiate access for the user and TPA firm for any of their contracts
        if (displayLink) {
            UserRole role = userProfile.getPrincipal().getRole();
       
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
    private boolean validateListAccessOnTPAView(final UserProfile userProfile)
			throws SystemException, ApplicationException {

        final UserRole role = userProfile.getPrincipal().getRole();
        if (!(role.isTPA())) {
            return false;
        } // fi

        boolean checkViewAllWithdrawals = SecurityHelper.checkForAnyTpaFirmWithPermission(userProfile.getPrincipal(),
                PermissionType.VIEW_ALL_WITHDRAWALS);
        boolean checkViewAllLoans = SecurityHelper.checkForAnyTpaFirmWithPermission(userProfile.getPrincipal(),
                PermissionType.VIEW_ALL_LOANS);
        
        // Must have initiate access for the user and TPA firm for any of their contracts
        return (checkViewAllWithdrawals || checkViewAllLoans);
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
    private boolean validateContractServiceFeatures(UserProfile userProfile, Contract contract)
            throws NumberFormatException, SystemException, ApplicationException {

        boolean displayLink = true;
        // Restrictions

        ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate
                .getInstance().getContractServiceFeature(getContractId(),
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
	 * Validate the Loans Contract Service Features for the given Contract.
	 * 
	 * @param userProfile
	 * @param contract
	 * @return boolean
	 * @throws NumberFormatException
	 * @throws SystemException
	 */
    private boolean validateLoansContractServiceFeature(UserProfile userProfile, Contract contract)
            throws NumberFormatException, SystemException, ApplicationException {

        boolean displayLink = false;
 
        LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(getContractId()) ;
        if(loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()){
        	displayLink = true; 
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
    private boolean validateCreateLoanAccessOnPSWView(UserProfile userProfile)
            throws NumberFormatException, SystemException, ApplicationException {

        boolean displayLink = true;

        Contract contract = getContract(userProfile);
        String contractStatus = contract.getStatus();
        displayLink = validateLoansContractServiceFeature(userProfile, contract);

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
            if (role.isPlanSponsor() || role.isTPA()) {
                displayLink = true;
			} else if (contract.isBundledGaIndicator() &&
					userProfile.getRole().hasPermission(PermissionType.INITIATE_LOANS)){
				// Bundled GA CAR should be able to initiate loan request
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
    private boolean validateCreateLoanAccessOnTPAView(UserProfile userProfile)
            throws NumberFormatException, SystemException {

        boolean displayLink = true;

        // Restrictions
    
        // Must have initiate access for the user and TPA firm for any of their contracts
        if (displayLink) {
            UserRole role = userProfile.getPrincipal().getRole();
       
            if (role.isTPA()) {
                displayLink = true;
            } else {
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
    private Contract getContract(UserProfile userProfile) throws NumberFormatException,
            SystemException {
        ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
        Contract contract = userProfile.getCurrentContract();
        Date contractTerminationDate =null;
        int contractNumber = 0;
        if(contract != null){
        	contractTerminationDate = contract.getContractStatusEffectiveDate();
        	contractNumber = contract.getContractNumber();
        }
		UserRole role = userProfile.getRole();
		int diDuration = EnvironmentServiceDelegate.getInstance()
				.retrieveContractDiDuration(role, contractNumber,
						contractTerminationDate);
        try {
            contract = delegate.getContractDetails(getContractId(), diDuration);
        } catch (ContractNotExistException ce) {
            SystemException se = new SystemException(ce, this.getClass().getName(), "getContract",
                    ce.getMessage());
            throw se;
        }
        return contract;
    }

    public int doEndTag() throws JspTagException {
        return EVAL_PAGE;
    }

    /**
     * @return Returns the contractId.
     */
    public int getContractId() {
        return contractId;
    }

    /**
     * @param contractId The contractId to set.
     */
    public void setContractId(final int contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the linkType
     */
    public String getLinkType() {
        return linkType;
    }

    /**
     * @param linkType the linkType to set
     */
    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

}
