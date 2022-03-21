package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.contract.ContractServiceHelper;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.TPAUserManager;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.DefaultRolePermissions;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.util.FormatUtils;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;

/**
 * A helper class for add/edit external user action. The goal is to cut down the
 * amount of non-workflow related codes in the actual action.
 * 
 */
public class TPAUserContractAccessActionHelper {
	private static final String CLASS_NAME = TPAUserContractAccessActionHelper.class.getName(); 
	
    private static final int MAX_ACCOUNT_LABEL_WIDTH = 9999;

    private static final Logger logger = Logger
            .getLogger(TPAUserContractAccessActionHelper.class);

 //   private static final String PSEUM = new ExternalUserManager().toString();
    private static final String TPAUM = new TPAUserManager().toString();
 //   private static final String PA    = new PlanAdministrator().toString();
    private static final String TPA   = new ThirdPartyAdministrator().toString();
    

    /**
     * Constructor.
     */
    private TPAUserContractAccessActionHelper() {
        super();
    }

    public static void populateContractAccess(TPAUserContractAccess accessForm,
            ContractPermission contractPermission) throws SystemException {
        populateContractAccess(accessForm, contractPermission, null);
    }

    /**
     * Populates a contract access object with the given contract permission,
     * filtered by what is accessible by the user info.
     * 
     * @param accessForm
     * @param contractPermission
     * @param filterUserInfo
     * 
     * @throws SystemException
     */
    static void populateContractAccess(TPAUserContractAccess accessForm,
            ContractPermission contractPermission, UserInfo filterUserInfo)
            throws SystemException {
        accessForm.setContractNumber(new Integer(contractPermission
                .getContractNumber()));
        accessForm.setCompanyName(contractPermission.getCompanyName());
        /* 
         *TODO accessForm.setContractAllocated(new Boolean(contractPermission
                .isAllocated()));*/
        
        UserRole role = contractPermission.getRole();
        accessForm.setPlanSponsorSiteRole(role.toString());
        accessForm.setExternalUserManager(role.isExternalUser() && role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS));
        		
        accessForm.setDirectDebit(new Boolean(contractPermission.isDirectDebit()));
        accessForm.setCashAccount(new Boolean(contractPermission.isCashAccount()));
        accessForm.setUploadSubmissions(new Boolean(contractPermission.isUploadSubmissions()));
        accessForm.setParticipantAddressDownloadAccessAvailable(new Boolean(
                contractPermission.isParticipantAddressDownloadAccessAvailable()));
        accessForm.setStatementsAccessAvailable(new Boolean(contractPermission.isStatementsAccessAvailable()));
        accessForm.setReportDownload(new Boolean(contractPermission.isReportDownload()));
        accessForm.setTpaStaffPlanAccess(new Boolean(contractPermission.isTpaStaffPlanAccess()));
        accessForm.setUpdateCensusData(new Boolean(contractPermission.isUpdateCensusData()));
        
        /*Added new attribute for 404a5 permission*/
        accessForm.setFeeAccess404A5(new Boolean(contractPermission.isFeeAccess404A5()));
        accessForm.setViewSalary(new Boolean(contractPermission.isViewSalary()));
        accessForm.setReviewIWithdrawals(new Boolean(contractPermission.isReviewIWithdrawals()));
        accessForm.setEditApprovePlan(new Boolean(contractPermission.isEditPlanData()));
        accessForm.setSubmitUpdateVesting(new Boolean(contractPermission.isSubmitUpdateVesting()));
        accessForm.setManageUsers(contractPermission.isManageTpaUsers());
        accessForm.setSigningAuthority(contractPermission.isSigningAuthority());
        accessForm.setInitiateIWithdrawals(contractPermission.isInitiateWithdrawalsAndViewMine());
        accessForm.setViewAllIWithdrawals(contractPermission.isViewAllWithdrawals());
        accessForm.setInitiateLoans(contractPermission.isInitiateLoans());
        accessForm.setViewAllLoans(contractPermission.isViewAllLoans());
        accessForm.setReviewLoans(contractPermission.isReviewLoans());
        
        populatePermissionDefaults(accessForm, contractPermission.getRole().getDefaultRolePermissions());

        if (filterUserInfo != null) {
            populateDirectDebitAccountsFromUserInfo(accessForm, filterUserInfo, contractPermission.getContractNumber());
            accessForm.setAccountsNotShown(hasDirectDebitAccountNotShown(
                    contractPermission.getContractNumber(), filterUserInfo,
                    contractPermission));
            setSelectedDirectDebitAccounts(contractPermission, accessForm);
            filterContractAccess(filterUserInfo, contractPermission.getContractNumber(), accessForm);
        } else {
            populateDirectDebitAccountsFromContractPermission(accessForm, contractPermission);
            setSelectedDirectDebitAccounts(contractPermission, accessForm);
            accessForm.setAccountsNotShown(false);
        }

        // Set original values for 'parent' permissions for use in .jsp
        accessForm.setOriginalReviewIWithdrawals(accessForm.getReviewIWithdrawals());
        accessForm.setOriginalReviewLoans(accessForm.isReviewLoans());
        
    }
    
    public static void populatePermissionDefaults(TPAUserContractAccess contractAccess, DefaultRolePermissions defaultPermissions) throws SystemException {
        if (defaultPermissions == null) {
            defaultPermissions = SecurityServiceDelegate.getInstance().getDefaultRolePermissions(ThirdPartyAdministrator.ID);
        }
        UserPermissions userPermissions = contractAccess.getUserPermissions();

        userPermissions.setTpaStaffPlanAccessDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.TPA_STAFF_PLAN_ACCESS));
        userPermissions.setManageUsersDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.MANAGE_TPA_USERS));
        userPermissions.setSelectedAccessDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.SELECTED_ACCESS));
        userPermissions.setEditContractServiceFeaturesDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.EDIT_SERVICE_FEATURES));
        userPermissions.setDownloadReportsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.REPORT_DOWNLOAD));
        userPermissions.setEditPlanDataDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.EDIT_PLAN_DATA));
        userPermissions.setSubmitUpdateVestingDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.SUBMIT_UPDATE_VESTING));
        userPermissions.setCreateUploadSubmissionsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.UPLOAD_SUBMISSIONS));
        userPermissions.setCashAccountDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.CASH_ACCOUNT_ACCESS));
        userPermissions.setDirectDebitDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.DIRECT_DEBIT_ACCOUNT));
        userPermissions.setInitiateAndViewMyWithdrawalsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE));
        userPermissions.setViewAllWithdrawalsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_ALL_WITHDRAWALS));
        userPermissions.setReviewWithdrawalsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.REVIEW_WITHDRAWALS));
        userPermissions.setSigningAuthorityDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.SIGNING_AUTHORITY));
        userPermissions.setUpdateCensusDataDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.UPDATE_CENSUS_DATA));
        
        /*Added new attribute for 404a5 permission*/
        userPermissions.setFeeAccess404A5Default(defaultPermissions.getDefaultPermissionValue(PermissionType.FEE_ACCESS_404A5));
        
        userPermissions.setViewSalaryDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_SALARY));
        userPermissions.setInitiateLoansDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.INITIATE_LOANS));
        userPermissions.setViewAllLoansDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_ALL_LOANS));
        userPermissions.setReviewLoansDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.REVIEW_LOANS));
        
    }

    public static void populatePermissionsFromDefaults(TPAUserContractAccess contractAccess) {
        UserPermissions userPermissions = contractAccess.getUserPermissions();
        userPermissions.setTpaStaffPlanAccess(DefaultRolePermissions.TRUE.equals(userPermissions.getTpaStaffPlanAccessDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getTpaStaffPlanAccessDefault()));
        userPermissions.setManageUsers(DefaultRolePermissions.TRUE.equals(userPermissions.getManageUsersDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getManageUsersDefault()));
        userPermissions.setSelectedAccess(DefaultRolePermissions.TRUE.equals(userPermissions.getSelectedAccessDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getSelectedAccessDefault()));
        userPermissions.setEditContractServiceFeatures(DefaultRolePermissions.TRUE.equals(userPermissions.getEditContractServiceFeaturesDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getEditContractServiceFeaturesDefault()));
        userPermissions.setDownloadReports(DefaultRolePermissions.TRUE.equals(userPermissions.getDownloadReportsDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getDownloadReportsDefault()));
        userPermissions.setEditPlanData(DefaultRolePermissions.TRUE.equals(userPermissions.getEditPlanDataDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getEditPlanDataDefault()));
        userPermissions.setSubmitUpdateVesting(DefaultRolePermissions.TRUE.equals(userPermissions.getSubmitUpdateVestingDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getSubmitUpdateVestingDefault()));
        userPermissions.setCreateUploadSubmissions(DefaultRolePermissions.TRUE.equals(userPermissions.getCreateUploadSubmissionsDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getCreateUploadSubmissionsDefault()));
        userPermissions.setCashAccount(DefaultRolePermissions.TRUE.equals(userPermissions.getCashAccountDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getCashAccountDefault()));
        userPermissions.setDirectDebit(DefaultRolePermissions.TRUE.equals(userPermissions.getDirectDebitDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getDirectDebitDefault()));
        userPermissions.setInitiateAndViewMyWithdrawals(DefaultRolePermissions.TRUE.equals(userPermissions.getInitiateAndViewMyWithdrawalsDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getInitiateAndViewMyWithdrawalsDefault()));
        userPermissions.setViewAllWithdrawals(DefaultRolePermissions.TRUE.equals(userPermissions.getViewAllWithdrawalsDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getViewAllWithdrawalsDefault()));
        userPermissions.setReviewWithdrawals(DefaultRolePermissions.TRUE.equals(userPermissions.getReviewWithdrawalsDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getReviewWithdrawalsDefault()));
        userPermissions.setSigningAuthority(DefaultRolePermissions.TRUE.equals(userPermissions.getSigningAuthorityDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getSigningAuthorityDefault()));
        userPermissions.setUpdateCensusData(DefaultRolePermissions.TRUE.equals(userPermissions.getUpdateCensusDataDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getUpdateCensusDataDefault()));
        /*Added new attribute for 404a5 permission*/
        userPermissions.setFeeAccess404A5(DefaultRolePermissions.TRUE.equals(userPermissions.getFeeAccess404A5Default())
                || DefaultRolePermissions.YES.equals(userPermissions.getFeeAccess404A5Default()));
        userPermissions.setViewSalary(DefaultRolePermissions.TRUE.equals(userPermissions.getViewSalaryDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getViewSalaryDefault()));
        userPermissions.setInitiateLoans(DefaultRolePermissions.TRUE.equals(userPermissions.getInitiateLoansDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getInitiateLoansDefault()));
        userPermissions.setViewAllLoans(DefaultRolePermissions.TRUE.equals(userPermissions.getViewAllLoansDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getViewAllLoansDefault()));
        userPermissions.setReviewLoans(DefaultRolePermissions.TRUE.equals(userPermissions.getReviewLoansDefault())
                || DefaultRolePermissions.YES.equals(userPermissions.getReviewLoansDefault()));

    }

    /**
     * Unsets any permission that the logged in user do not have access to. This is important to avoid a user creating another user with more privileges.
     * 
     * @param filterUserInfo The logged in user's UserInfo object.
     * @param contractNumber The contract number of this contract access.
     * @param accessForm The contract access form.
     */
    public static void filterContractAccess(UserInfo filterUserInfo,
            int contractNumber, TPAUserContractAccess accessForm) {

        if (!(filterUserInfo.getRole() instanceof InternalUser)) {

            /*
             * Unset any permission that the login user does not have access to.
             * Once it's unset, JSP will suppressed it.
             */
        	ContractPermission filterContractPermission = filterUserInfo
                    .getContractPermission(contractNumber);
    
            if (filterContractPermission == null) {
                accessForm.setAllShowPermissions(false);
            } else {
                accessForm.setShowDirectDebit(filterContractPermission.isDirectDebit());
                accessForm.setShowCashAccount(filterContractPermission.isCashAccount());
                accessForm.setShowStatementsAccessAvailable(filterContractPermission.isStatementsAccessAvailable());
                accessForm.setShowUploadSubmissions(filterContractPermission.isUploadSubmissions());
                accessForm.setShowParticipantAddressDownloadAccessAvailable(filterContractPermission.isParticipantAddressDownloadAccessAvailable());
                accessForm.setShowReportDownload(filterContractPermission.isReportDownload());
                accessForm.setShowTpaStaffPlanAccess(filterContractPermission.isTpaStaffPlanAccess());
                accessForm.setShowUpdateCensusData(filterContractPermission.isUpdateCensusData());
                /*Added new attribute for 404a5 permission*/
                accessForm.setShowFeeAccess404A5(filterContractPermission.isFeeAccess404A5());
                accessForm.setShowViewSalary(filterContractPermission.isViewSalary());
                accessForm.setShowReviewIWithdrawals(filterContractPermission.isReviewIWithdrawals());
                accessForm.setShowReviewLoans(filterContractPermission.isReviewLoans());
                accessForm.setShowPlanData(filterContractPermission.isEditPlanData() || 
                		filterContractPermission.isSubmitUpdateVesting() || true); // TODO: remove after
            }
        }
    }
    
 
     /**
     * Filters permissions for looged user TPA with Manage users
     * 
     * @param filterUserInfo
     *            The logged in user's UserInfo object.
     * @param firmId
     *            The firmId of this contract access.
     * @param accessForm
     *            The contract access form.
     */
    public static void filterFirmContractAccess(UserInfo filterUserInfo,
            TpaFirm tpaForm) throws SystemException {
    	
        TPAUserContractAccess accessForm = tpaForm.getContractAccess(0);
        TPAServiceDelegate tpaSD = TPAServiceDelegate.getInstance();
    	com.manulife.pension.delegate.ContractServiceDelegate csd = 
    		 com.manulife.pension.delegate.ContractServiceDelegate.getInstance();
    	
    	boolean isInternalUser = filterUserInfo.getRole() instanceof InternalUser; 
    	ContractPermission filterContractPermission = null;
    	
    	if (isInternalUser) {
    		accessForm.setAllShowPermissions(true);
    	} else {
        	filterContractPermission = filterUserInfo.getTpaFirm(tpaForm.getId().intValue()).getContractPermission();

            /*
             * Unset any permission that the login user does not have access to.
             * Once it's unset, JSP will suppressed it.
             */
            if (filterContractPermission == null) {
                accessForm.setAllShowPermissions(false); //no permisssions for all
            } else {            	
                accessForm.setShowTpaStaffPlanAccess(filterContractPermission.isTpaStaffPlanAccess());
                accessForm.setShowReportDownload(filterContractPermission.isReportDownload());
                accessForm.setShowUpdateCensusData(filterContractPermission.isUpdateCensusData());
                
                /*Added new attribute for 404a5 permission*/
                accessForm.setShowFeeAccess404A5(filterContractPermission.isFeeAccess404A5());
               
                accessForm.setShowViewSalary(filterContractPermission.isViewSalary());
                          
                // NOTE!! ONLY internal users can get to the sub-page (which has the rest of the settings)
                //        so there is no need to filter those. 
                          
                 /*
                 * ICE 11.2 Checks if there is any permission which is
                 * suppressed.
                 */
         		boolean showPermissions = filterContractPermission.isSubmissionAccess();
         		// The below is replaced with the above one becasue there is no more iFile Permission
         		//boolean showPermissions = filterContractPermission.isIfileAccessAvailable()||filterContractPermission.isSubmissionAccess();
                accessForm.setPermissionsNotShown(!showPermissions);
 
                /* Checks if there is any permission which is
                * suppressed.
                */
               if (filterContractPermission.isSubmissionAccess()) {
                   boolean permissionsNotShown = false;
                   if ((accessForm.isDirectDebitTrue() && !filterContractPermission
                           .isDirectDebit())
                           || (accessForm.isCashAccountTrue() && !filterContractPermission
                                   .isCashAccount())
//                           || (accessForm.isViewAllSubmissions() && !filterContractPermission
//                                   .isViewAllSubmissions())
                           || (accessForm.isUploadSubmissionsTrue() && !filterContractPermission
                                   .isUploadSubmissions())) {
                       permissionsNotShown = true;
                   }
                   accessForm.setPermissionsNotShown(permissionsNotShown); 
               }
            }
    	}
    	    	
    	boolean oneContractForThisFirmHasWDWSF = false;
    	boolean oneContractIsBusinessConverted = false;
    	boolean oneContractForThisFirmHasVesting = false;
    	boolean tpaFirmHasPlanDataMgnt = false;
        boolean tpaFirmHasSigningAuthority = false;
        boolean firmContractsHaveDirectDebitAccounts = false;
        boolean oneContractForThisFirmHasLoansCSF = false;
    	
		List<Integer> caList = tpaForm.getContractIds();
        if (caList == null) {
            caList = tpaSD.getContractsByFirm(tpaForm.getId().intValue());  // all contracts associated with the firm
            tpaForm.setContractIds(caList);
        }

        ArrayList<String> serviceFeatureList = new ArrayList<String>();
        serviceFeatureList.add(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);

        Iterator<Integer> caIter = caList.iterator();
        Map<Integer, Contract> contractMap = new HashMap<Integer, Contract>();
        
		while(caIter.hasNext()) { 
			int contractNumber = ((Integer)caIter.next()).intValue();
			TPAFirmInfo firmInfo = tpaSD.getFirmInfoByContractId(contractNumber);

            // Since we're already going through the contracts for the firm here, also check if any of the contracts have direct debit accounts selected
            if (!firmInfo.getContractPermission().getDirectDebitAccounts().isEmpty()) {
                firmContractsHaveDirectDebitAccounts = true;
            }

			try {

				/*
				 * Check if one of the contracts associated with the given TPA
				 * firm is business converted or not. If we found one, we don't
				 * have to look at the others.
				 */
				if (!oneContractIsBusinessConverted) {
					Contract contract = contractMap.get(contractNumber);
					if (contract == null) {
						contract = ContractServiceHelper.getInstance()
								.getContractDetails(contractNumber, 6);
						if (contract != null) {
							contractMap.put(contractNumber, contract);
						}
					}

					if (contract != null) {
						if (contract.isBusinessConverted()) {
							oneContractIsBusinessConverted = true;
						}
					}
				}
				
                Map<String, ContractServiceFeature> serviceFeatureMap = csd.getContractServiceFeatures(contractNumber, serviceFeatureList);
                
				ContractServiceFeature csf = serviceFeatureMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);  
		        if (ContractServiceFeature.internalToBoolean(csf.getValue())) {
		        	oneContractForThisFirmHasWDWSF = true;
		        }

				ContractServiceFeature loansCSF = serviceFeatureMap.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
				boolean hasLoanRecordKeepingFeature = false; 
				hasLoanRecordKeepingFeature = LoanServiceDelegate.getInstance().hasLoanRecordKeepingProductFeature(contractNumber);

				if (hasLoanRecordKeepingFeature && ContractServiceFeature.internalToBoolean(loansCSF.getValue())) {
		        	oneContractForThisFirmHasLoansCSF = true;
		        }

                if (firmInfo.getContractPermission().isSigningAuthority()) {
                    tpaFirmHasSigningAuthority = true;
                }
                
		        csf = serviceFeatureMap.get(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE); // STA.34
                if (!com.manulife.pension.service.contract.util.ServiceFeatureConstants.NA.equals(csf.getValue())) {
		        	oneContractForThisFirmHasVesting = true;
		        }
                
                if (firmInfo.getContractPermission().isSubmitUpdateVesting()) { //TODO: check here
                    tpaFirmHasPlanDataMgnt = true;
                }
			} catch(ApplicationException ae) {
				throw new SystemException (ae, CLASS_NAME, "filterFirmContractAccess", ae.getMessage());
			}
		} // endWhile contracts for this firm
		
        tpaForm.setFirmContractsHaveDirectDebitAccounts(firmContractsHaveDirectDebitAccounts);

		// STA.33
		if (oneContractForThisFirmHasWDWSF &&
		    (isInternalUser || filterContractPermission.isReviewIWithdrawals())) {
			accessForm.setShowReviewIWithdrawals(true);
		} else {
			accessForm.setShowReviewIWithdrawals(false);
		}
		
        if (oneContractIsBusinessConverted) {
            accessForm.setShowSigningAuthority(true);
        } else {
            accessForm.setShowSigningAuthority(false);
        }

		if (oneContractForThisFirmHasLoansCSF &&
			    (isInternalUser || filterContractPermission.isReviewLoans())) {
				accessForm.setShowReviewLoans(true);
			} else {
				accessForm.setShowReviewLoans(false);
			}
        
		// STA.34
//		if (true && tpaFirmHasPlanDataMgnt && (isInternalUser || filterContractPermission.isPlanDataManagement())) { //TODO: check this one
//            accessForm.setShowPlanData(true);
//        } else {
//        	//TODO: verify condition here
//            accessForm.setShowPlanData(true);
//        }
   }	
    
    /**
     * Filters permissions for looged user TPA with Manage users
     * 
     * @param filterUserInfo
     *            The logged in user's UserInfo object.
     * @param firmId
     *            The firmId of this contract access.
     * @param accessForm
     *            The contract access form.
     */
    public static void filterFirmContractAccessNonContractLevel(UserInfo filterUserInfo,
            TpaFirm tpaForm) throws SystemException {
    	
        TPAUserContractAccess accessForm = tpaForm.getContractAccess(0);
        TPAServiceDelegate tpaSD = TPAServiceDelegate.getInstance();
    	com.manulife.pension.delegate.ContractServiceDelegate csd = 
    		 com.manulife.pension.delegate.ContractServiceDelegate.getInstance();
    	
    	boolean isInternalUser = filterUserInfo.getRole() instanceof InternalUser; 
    	ContractPermission filterContractPermission = null;
    	
    	if (isInternalUser) {
    		accessForm.setAllShowPermissions(true);
    	} else {
        	filterContractPermission = filterUserInfo.getTpaFirm(tpaForm.getId().intValue()).getContractPermission();

            /*
             * Unset any permission that the login user does not have access to.
             * Once it's unset, JSP will suppressed it.
             */
            if (filterContractPermission == null) {
                accessForm.setAllShowPermissions(false); //no permisssions for all
            } else {            	
                accessForm.setShowTpaStaffPlanAccess(filterContractPermission.isTpaStaffPlanAccess());
                accessForm.setShowReportDownload(filterContractPermission.isReportDownload());
                accessForm.setShowUpdateCensusData(filterContractPermission.isUpdateCensusData());
                
                /*Added new attribute for 404a5 permission*/
                accessForm.setShowFeeAccess404A5(filterContractPermission.isFeeAccess404A5());
               
                accessForm.setShowViewSalary(filterContractPermission.isViewSalary());
                          
                // NOTE!! ONLY internal users can get to the sub-page (which has the rest of the settings)
                //        so there is no need to filter those. 
                          
                 /*
                 * ICE 11.2 Checks if there is any permission which is
                 * suppressed.
                 */
         		boolean showPermissions = filterContractPermission.isSubmissionAccess();
         		// The below is replaced with the above one becasue there is no more iFile Permission
         		//boolean showPermissions = filterContractPermission.isIfileAccessAvailable()||filterContractPermission.isSubmissionAccess();
                accessForm.setPermissionsNotShown(!showPermissions);
 
                /* Checks if there is any permission which is
                * suppressed.
                */
               if (filterContractPermission.isSubmissionAccess()) {
                   boolean permissionsNotShown = false;
                   if ((accessForm.isDirectDebitTrue() && !filterContractPermission
                           .isDirectDebit())
                           || (accessForm.isCashAccountTrue() && !filterContractPermission
                                   .isCashAccount())
//                           || (accessForm.isViewAllSubmissions() && !filterContractPermission
//                                   .isViewAllSubmissions())
                           || (accessForm.isUploadSubmissionsTrue() && !filterContractPermission
                                   .isUploadSubmissions())) {
                       permissionsNotShown = true;
                   }
                   accessForm.setPermissionsNotShown(permissionsNotShown); 
               }
            }
    	}
    	    	
    	boolean oneContractForThisFirmHasWDWSF = true; // changed from false to true for  CL#132387
    	boolean oneContractIsBusinessConverted = true; // changed from false to true for  CL#132387
//    	boolean oneContractForThisFirmHasVesting = false;
//    	boolean tpaFirmHasPlanDataMgnt = false;
//        boolean tpaFirmHasSigningAuthority = false;
        boolean firmContractsHaveDirectDebitAccounts = true; 
        boolean oneContractForThisFirmHasLoansCSF = true; // changed from false to true for  CL#132387
        /*	
		List<Integer> caList = tpaForm.getContractIds();
        if (caList == null) {
        	caList = tpaSD.getContractsByFirm(tpaForm.getId().intValue());  // all contracts associated with the firm
            tpaForm.setContractIds(caList);
        }

       ArrayList<String> serviceFeatureList = new ArrayList<String>();
        serviceFeatureList.add(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
        serviceFeatureList.add(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);

        Iterator<Integer> caIter = caList.iterator();
        Map<Integer, Contract> contractMap = new HashMap<Integer, Contract>();
        
		while(caIter.hasNext()) { 
			int contractNumber = ((Integer)caIter.next()).intValue();
			TPAFirmInfo firmInfo = tpaSD.getFirmInfoByContractId(contractNumber);

            // Since we're already going through the contracts for the firm here, also check if any of the contracts have direct debit accounts selected
            if (!firmInfo.getContractPermission().getDirectDebitAccounts().isEmpty()) {
                firmContractsHaveDirectDebitAccounts = true;
            }

			try {

				//	  Check if one of the contracts associated with the given TPA firm is business converted or not. If we found one, we don't  have to look at the others.
				 
				if (!oneContractIsBusinessConverted) {
					Contract contract = contractMap.get(contractNumber);
					if (contract == null) {
						contract = ContractServiceHelper.getInstance()
								.getContractDetails(contractNumber, 6);
						if (contract != null) {
							contractMap.put(contractNumber, contract);
						}
					}

					if (contract != null) {
						if (contract.isBusinessConverted()) {
							oneContractIsBusinessConverted = true;
						}
					}
				}
				long startContractServiceFeatures = System.currentTimeMillis();
                Map<String, ContractServiceFeature> serviceFeatureMap = csd.getContractServiceFeatures(contractNumber, serviceFeatureList);
                long stopContractServiceFeatures = System.currentTimeMillis();
                logger.error("EDIT TPA Performance Trace -  TPAUserContractAccessActionHelper: calling filterFirmContractAccess() --> getContractServiceFeatures " 
                		+ "Start time: " + startContractServiceFeatures + ", End time: " + stopContractServiceFeatures 
                		+ ", Duration: " + (stopContractServiceFeatures - startContractServiceFeatures) + " ms ");
                
				ContractServiceFeature csf = serviceFeatureMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);  
		        if (ContractServiceFeature.internalToBoolean(csf.getValue())) {
		        	oneContractForThisFirmHasWDWSF = true;
		        }

				ContractServiceFeature loansCSF = serviceFeatureMap.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
				boolean hasLoanRecordKeepingFeature = false;
				long startLoanRecordKeepingProductFeature = System.currentTimeMillis();
				hasLoanRecordKeepingFeature = LoanServiceDelegate.getInstance().hasLoanRecordKeepingProductFeature(contractNumber);
				long stopLoanRecordKeepingProductFeature = System.currentTimeMillis();
                logger.error("EDIT TPA Performance Trace -  TPAUserContractAccessActionHelper: calling filterFirmContractAccess() --> hasLoanRecordKeepingProductFeature " 
                		+ "Start time: " + startLoanRecordKeepingProductFeature + ", End time: " + stopLoanRecordKeepingProductFeature 
                		+ ", Duration: " + (stopLoanRecordKeepingProductFeature - startLoanRecordKeepingProductFeature) + " ms ");

				if (hasLoanRecordKeepingFeature && ContractServiceFeature.internalToBoolean(loansCSF.getValue())) {
		        	oneContractForThisFirmHasLoansCSF = true;
		        }

                if (firmInfo.getContractPermission().isSigningAuthority()) {
                    tpaFirmHasSigningAuthority = true;
                }
                
		        csf = serviceFeatureMap.get(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE); // STA.34
                if (!com.manulife.pension.service.contract.util.ServiceFeatureConstants.NA.equals(csf.getValue())) {
		        	oneContractForThisFirmHasVesting = true;
		        }
                
                if (firmInfo.getContractPermission().isSubmitUpdateVesting()) { //TODO: check here
                    tpaFirmHasPlanDataMgnt = true;
                }
			} catch(ApplicationException ae) {
				throw new SystemException (ae, CLASS_NAME, "filterFirmContractAccess", ae.getMessage());
			}
		} // endWhile contracts for this firm
		*/
        tpaForm.setFirmContractsHaveDirectDebitAccounts(firmContractsHaveDirectDebitAccounts);

		// STA.33
		if (oneContractForThisFirmHasWDWSF &&
		    (isInternalUser || filterContractPermission.isReviewIWithdrawals())) {
			accessForm.setShowReviewIWithdrawals(true);
		} else {
			accessForm.setShowReviewIWithdrawals(false);
		}
		
        if (oneContractIsBusinessConverted) {
            accessForm.setShowSigningAuthority(true);
        } else {
            accessForm.setShowSigningAuthority(false);
        }

		if (oneContractForThisFirmHasLoansCSF &&
			    (isInternalUser || filterContractPermission.isReviewLoans())) {
				accessForm.setShowReviewLoans(true);
			} else {
				accessForm.setShowReviewLoans(false);
			}
        
		// STA.34
//		if (true && tpaFirmHasPlanDataMgnt && (isInternalUser || filterContractPermission.isPlanDataManagement())) { //TODO: check this one
//            accessForm.setShowPlanData(true);
//        } else {
//        	//TODO: verify condition here
//            accessForm.setShowPlanData(true);
//        }
   }
    
    /**-----------------------------------------------------------------LS

    /**
     * Populates the given contract access object with the list of direct debit
     * accounts in the contract permission object. Also sets the
     * selectedAccounts attribute to be all of the accounts. This method is used
     * in DeleteProfileAction and RegisterAction to prepare the list of direct
     * debit accounts for display.
     * 
     * @param contractAccess
     * @param contractPermission
     */
    public static void populateDirectDebitAccountsFromContractPermission(
            TPAUserContractAccess contractAccess, ContractPermission contractPermission) {
        /*
         * Set the list of direct debit accounts for this user.
         */
        String[] selectedAccounts = new String[contractPermission.getRole()
                .getDirectDebitAccounts().size()];
        int index = 0;
        for (Iterator it = contractPermission.getRole()
                .getDirectDebitAccounts().iterator(); it.hasNext();) {
            DirectDebitAccount account = (DirectDebitAccount) it.next();
            BankAccount bankAccount = BankAccount.getBankAccount(account);
            bankAccount.setLabel(FormatUtils.formatAccountName(account,
                    MAX_ACCOUNT_LABEL_WIDTH));

            selectedAccounts[index] = bankAccount.getPrimaryKey();
            index++;
            contractAccess.getDirectDebitAccounts().add(bankAccount);
        }
        contractAccess.setSelectedDirectDebitAccounts(selectedAccounts);
    }

    /**
     * 
     * Populates the given contract access form with direct debit accounts
     * available to the login user for the given contract.
     * 
     * @param accessForm
     * @param userInfo
     * @param contractNumber
     * 
     * @throws SystemException
     */
    private static void populateDirectDebitAccountsFromUserInfo(
            TPAUserContractAccess accessForm, UserInfo userInfo, int contractNumber)
            throws SystemException {

        accessForm.getDirectDebitAccounts().clear();

        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        List directDebitAccounts = service
                .getDirectDebitAccounts(contractNumber);
        List allowedDirectDebitAccounts = null;

        if (!(userInfo.getRole() instanceof InternalUser)) {
            /*
             * If login user is NOT an internal user, we limit the list of
             * direct debit accounts to the ones the current user have access
             * to.
             */
            ContractPermission loginUserPermission = userInfo
                    .getContractPermission(contractNumber);

            if (loginUserPermission != null) {
                allowedDirectDebitAccounts = loginUserPermission
                        .getDirectDebitAccounts();
            } else {
                allowedDirectDebitAccounts = new ArrayList();
            }
        }

        if (directDebitAccounts != null) {
            for (Iterator it = directDebitAccounts.iterator(); it.hasNext();) {
                DirectDebitAccount account = (DirectDebitAccount) it.next();
                BankAccount bankAccount = BankAccount.getBankAccount(account);
                bankAccount.setLabel(FormatUtils.formatAccountName(account,
                        MAX_ACCOUNT_LABEL_WIDTH));
                if (!(userInfo.getRole() instanceof InternalUser)) {
                    if (!allowedDirectDebitAccounts.contains(account)) {
                        bankAccount.setNoAccess(true);
                    }
                }
                accessForm.getDirectDebitAccounts().add(bankAccount);
            }
        }
    }

    /**
     * Set the selected direct debit accounts based on the given contract
     * permission. This method does not expose any additional bank account that
     * the login user does not have access to. Remember that the initial list of
     * accounts (stored in the action form) comes from what the login user have
     * access to.
     * 
     * @param permission
     * @param accessForm
     */
    private static void setSelectedDirectDebitAccounts(
            ContractPermission permission, TPAUserContractAccess accessForm) {

        String[] selectedAccounts = new String[accessForm
                .getDirectDebitAccounts().size()];

        int index = 0;

        /*
         * Goes through each of the direct debit accounts the user have access
         * to, selects only those that are available.
         */
        for (Iterator it = permission.getRole().getDirectDebitAccounts()
                .iterator(); it.hasNext();) {
            DirectDebitAccount account = (DirectDebitAccount) it.next();
            BankAccount bankAccount = BankAccount.getBankAccount(account);
            if (accessForm.getDirectDebitAccounts().contains(bankAccount)) {
                selectedAccounts[index] = bankAccount.getPrimaryKey();
                index++;
            }
        }
        accessForm.setSelectedDirectDebitAccounts(selectedAccounts);
    }

    /**
     * Populates the given contract permission object with the direct debit
     * accounts from the given contract access form.
     * 
     * @param loginUserInfo
     * @param permission
     * @param access
     */
    static void populateDirectDebitAccounts(ContractPermission permission,
            TPAUserContractAccess access) {

        permission.getRole().getDirectDebitAccounts().clear();

        List selectedAccounts = access.getSelectedDirectDebitAccountsAsList();
        for (Iterator it = selectedAccounts.iterator(); it.hasNext();) {
            BankAccount account = (BankAccount) it.next();
            DirectDebitAccount directDebitAccount = account
					.getDirectDebitAccount();
			permission.getRole().getDirectDebitAccounts().add(
					directDebitAccount);
        }

        /*
         * Find out which direct debit accounts are deselected.
         */
        permission.getRole().getRemovedDirectDebitAccounts().clear();
        for (Iterator it = access.getDirectDebitAccounts().iterator(); it
                .hasNext();) {
            BankAccount account = (BankAccount) it.next();
            DirectDebitAccount directDebitAccount = account
					.getDirectDebitAccount();
            if (!permission.getRole().getDirectDebitAccounts().contains(
                    directDebitAccount)) {
                permission.getRole().getRemovedDirectDebitAccounts().add(
                        directDebitAccount);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Contract Access Accounts:\n"
                    + StaticHelperClass.toXML(access.getDirectDebitAccounts()));
            logger.debug("User Info Selected Accounts:\n"
                    + StaticHelperClass.toXML(permission.getRole()
                            .getDirectDebitAccounts()));
            logger.debug("User Info Removed Accounts:\n"
                    + StaticHelperClass.toXML(permission.getRole()
                            .getRemovedDirectDebitAccounts()));
        }
    }

    /**
     * Obtains the list of contracts (in the form of Integer objects) that the
     * user is an external user manager of.
     * 
     * @param userName
     *            The user name to check.
     * @return
     */
    static List getExternalUserManagerContracts(String userName) {
        List contracts = null;
        try {
            UserInfo userInfo = SecurityServiceDelegate.getInstance()
                    .searchByUserName(
                            AbstractAddEditUserController.newPrincipal(new Long(0),
                                    null, null), userName);
            ContractPermission[] editUserContracts = userInfo
                    .getContractPermissions();
            contracts = new ArrayList(editUserContracts.length);
            for (int i = 0; i < editUserContracts.length; i++) {
            	UserRole role = editUserContracts[i].getRole();
                if (role.isExternalUser() && role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
                    contracts.add(new Integer(editUserContracts[i]
                            .getContractNumber()));
                }
            }
        } catch (Exception e) {
            /*
             * This should not happen because we are only retrieving the user
             * info object using a generic principal.
             */
            throw new NestableRuntimeException(e);
        }
        return contracts;
    }

    /**
     * Prepares the request. Specifically, it sets up the contract drop down
     * list if the user is a PSEUM and has multiple contracts.
     * 
     * @param actionForm
     * @param request
     */
    static void populateContractDropDown(String mapping,
            ActionForm actionForm, HttpServletRequest request) {

        UserProfile userProfile = PsController.getUserProfile(request);
        AddEditUserForm form = (AddEditUserForm) actionForm;
        UserRole userRole = userProfile.getRole();

        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				PsController.getUserProfile(request).getPrincipal());

        /*
		 * MPR 20 & MPR 21. Show drop down list of contracts if logged in user
		 * is an PSEUM and has multiple contracts.
		 */
        if (userRole.isExternalUser() && userRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
//                && userProfile.isMultipleContracts()) {

            ContractPermission[] contracts = loginUserInfo
                    .getContractPermissions();

            /*
             * The labels are sorted by contract IDs in ascending order.
             */
            Set contractLabelValueBeans = new TreeSet(new Comparator() {
                public int compare(Object o1, Object o2) {
                    LabelValueBean bean1 = (LabelValueBean) o1;
                    LabelValueBean bean2 = (LabelValueBean) o2;
                    Integer contractNumber1 = new Integer(bean1.getValue());
                    Integer contractNumber2 = new Integer(bean2.getValue());
                    return contractNumber1.compareTo(contractNumber2);
                }
            });

            /*
             * Obtains the list of contracts that the edited user is a PSEUM of.
             */
            List editUserPseumContracts;
            if (!AbstractAddEditUserController.isAddUser(mapping)) {
                editUserPseumContracts = getExternalUserManagerContracts(form
                        .getUserName());
            } else {
                editUserPseumContracts = new ArrayList();
            }

            for (int i = 0; i < contracts.length; i++) {

                /*
                 * Drop down should only contains contracts that the login user
                 * is a PSEUM of.
                 */
            	UserRole role = contracts[i].getRole();

                if (!(role.isExternalUser() && role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS))) {
                    continue;
                }

                int contractId = contracts[i].getContractNumber();

                /*
                 * Also, if the edited user is a PSEUM of any contract, the drop
                 * down should not show the contract.
                 */
                if (editUserPseumContracts.contains(new Integer(contractId)) ||
                		(userProfile.getCurrentContract() != null &&  
                				userProfile.getCurrentContract().getContractNumber() == contractId)) {
                    continue;
                }

                /*
                 * Skip all contracts that is already in the form.
                 */
                boolean contractFound = false;

                for (Iterator it2 = form.getContractAccesses().iterator(); it2
                        .hasNext()
                        && !contractFound;) {
                    ContractAccess accessForm = (ContractAccess) it2.next();
                    if (accessForm.getContractNumber().intValue() == contractId) {
                        contractFound = true;
                    }
                }

                if (contractFound) {
                    continue;
                }

                String label = contractId + " " + contracts[i].getCompanyName();

                contractLabelValueBeans.add(new LabelValueBean(label, String
                        .valueOf(contractId)));
            }

            /*
             * Add an empty label.
             */
            List contractList = new ArrayList();
            if (contractLabelValueBeans.size() > 0) {
                contractList.add(new LabelValueBean("", ""));
                contractList.addAll(contractLabelValueBeans);
	            request.setAttribute(Constants.ADD_EDIT_USER_CONTRACT_LIST,
	                    contractList);
            }
        }
    }

    /**
     * Checks whether there is any direct debit account that is not shown to the
     * editor.
     * 
     * @param contractNumber
     * @param filterUserInfo
     * @param permissionToCheck
     * @return
     */
    static boolean hasDirectDebitAccountNotShown(int contractNumber,
            UserInfo filterUserInfo, ContractPermission permissionToCheck) {

        /*
         * For internal user, all accounts are shown.
         */
        if (filterUserInfo.getRole() instanceof InternalUser) {
            return false;
        }

        /*
         * If the permission to check has no direct debit account, that means no
         * account is filtered.
         */
        if (permissionToCheck.getDirectDebitAccounts().size() == 0) {
            return false;
        }

        ContractPermission filterUserPermission = filterUserInfo
                .getContractPermission(contractNumber);

        /*
         * If filter user has fewer direct debit accounts, we can be sure that
         * some accounts are not shown on the screen.
         */
        if (filterUserPermission.getDirectDebitAccounts().size() < permissionToCheck
                .getDirectDebitAccounts().size()) {
            return true;
        }

        for (Iterator it = permissionToCheck.getDirectDebitAccounts()
                .iterator(); it.hasNext();) {
            DirectDebitAccount account = (DirectDebitAccount) it.next();
            if (!filterUserPermission.getDirectDebitAccounts()
                    .contains(account)) {
                return true;
            }
        }
        return false;
    }

    static void populateContractPermission(ContractPermission permission, TPAUserContractAccess access) {

        permission.setContractNumber(access.getContractNumber() == null ? 0 : access.getContractNumber().intValue());
        permission.setCashAccount(access.isCashAccountTrue());
        permission.setDirectDebit(access.isDirectDebitTrue());
        permission.setUploadSubmissions(access.isUploadSubmissionsTrue());
        permission.setTpaStaffPlanAccess(access.getTpaStaffPlanAccess());
        permission.setReportDownload(access.getReportDownload().booleanValue());

        permission.setUpdateCensusData(access.getUpdateCensusData().booleanValue());
        /*Added new attribute for 404a5 permission*/
        permission.setFeeAccess404A5(access.getFeeAccess404A5().booleanValue());
        permission.setViewSalary(access.getViewSalary().booleanValue());
        permission.setReviewIWithdrawals(access.getReviewIWithdrawals().booleanValue());
        permission.setEditPlanData(access.getEditApprovePlan().booleanValue());
        permission.setSubmitUpdateVesting(access.getSubmitUpdateVesting().booleanValue());
        permission.setManageTpaUsers(access.getManageUsers().booleanValue());
        permission.setSigningAuthority(access.getSigningAuthority());
        permission.setInitiateWithdrawalsAndViewMine(access.getInitiateIWithdrawals());
        permission.setViewAllWithdrawals(access.getViewAllIWithdrawals().booleanValue());
        
        //populate loans related permissions
        permission.setInitiateLoans(access.isInitiateLoans());
        permission.setViewAllLoans(access.isViewAllLoans());
        permission.setReviewLoans(access.isReviewLoans());
        
        populateDirectDebitAccounts(permission, access);
    }
    
    public static void setLastPermissionFlags(TpaFirm tpaForm, long managedUserProfileId) throws SystemException {
        // Check only TPA users for these permissions
        String manageTPAUsersCode = PermissionType.getPermissionCode(PermissionType.MANAGE_TPA_USERS);
        String receiveILoansEmailCode = PermissionType.getPermissionCode(PermissionType.RECEIVE_ILOANS_EMAIL);
        String tpaStaffPlanCode = PermissionType.getPermissionCode(PermissionType.TPA_STAFF_PLAN_ACCESS);

        List<String> permissionList = new ArrayList<String>();
        permissionList.add(manageTPAUsersCode);
        if (!tpaForm.isNewFirm()) {
        permissionList.add(receiveILoansEmailCode);
            if (tpaForm.getContractAccess(0).isShowTpaStaffPlanAccess()) {
            permissionList.add(tpaStaffPlanCode);
        }
        }

        String tpaRoleId = ThirdPartyAdministrator.ID;
        List<String> roleList = new ArrayList<String>();
        roleList.add(tpaRoleId);

        Map<String, List<Long>> firmPermissionMap = SecurityServiceDelegate.getInstance().getTPAUsersWithRolePermission(tpaForm.getId().intValue(), permissionList, roleList);

        if (!tpaForm.isNewFirm()) {
            if (tpaForm.getContractAccess(0).isShowTpaStaffPlanAccess()) {
            // Need to determine if this is the last user with both receive iloans email and staff plan access
                List<Long> usersWithReceiveILoans = firmPermissionMap.get(receiveILoansEmailCode);
                List<Long> usersWithTPAStaffPlan = firmPermissionMap.get(tpaStaffPlanCode);
            List<Long> usersWithBoth = new ArrayList<Long>();
                if (usersWithReceiveILoans != null && !usersWithReceiveILoans.isEmpty() && usersWithTPAStaffPlan != null && !usersWithTPAStaffPlan.isEmpty()) {
                for (Long receiveILoansUser : usersWithReceiveILoans) {
                    if (usersWithTPAStaffPlan.contains(receiveILoansUser)) {
                        usersWithBoth.add(receiveILoansUser);
                    }
                }
            }
            String usersWithBothCode = receiveILoansEmailCode + "&" + tpaStaffPlanCode;
            firmPermissionMap.put(usersWithBothCode, usersWithBoth);
                tpaForm.setLastUserWithReceiveILoansEmailAndTPAStaffPlan(isLastUserWithPermission(firmPermissionMap, usersWithBothCode, managedUserProfileId));
            }
            tpaForm.setLastUserWithReceiveILoansEmail(isLastUserWithPermission(firmPermissionMap, receiveILoansEmailCode, managedUserProfileId));
        }
        tpaForm.setLastUserWithManageUsers(isLastUserWithPermission(firmPermissionMap, manageTPAUsersCode, managedUserProfileId, tpaForm.isNewFirm()));
        tpaForm.setLastRegisteredUser(isLastUserWithPermission(firmPermissionMap, tpaRoleId, managedUserProfileId, tpaForm.isNewFirm()));

        
        // Check client and TPA users for the last users with review withdrawal/signing authority/review loans permissions
        if ((!tpaForm.isNewFirm())
				|| (tpaForm.getContractAccess(0).isShowReviewIWithdrawals())
				|| (tpaForm.getContractAccess(0).isShowReviewLoans())
				|| (tpaForm.getContractAccess(0).isShowSigningAuthority())) {
            String selectedAccessCode = PermissionType.getPermissionCode(PermissionType.SELECTED_ACCESS);
            String reviewWithdrawalsCode = PermissionType.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS);
            String signingAuthorityCode = PermissionType.getPermissionCode(PermissionType.SIGNING_AUTHORITY);
            // Review Loans 
            String reviewLoansCode = PermissionType.getPermissionCode(PermissionType.REVIEW_LOANS);

            permissionList.clear();
            permissionList.add(selectedAccessCode);
            if (!tpaForm.isNewFirm() || tpaForm.getContractAccess(0).isShowReviewIWithdrawals()) {
                permissionList.add(reviewWithdrawalsCode);
            }
            if (!tpaForm.isNewFirm() || tpaForm.getContractAccess(0).isShowSigningAuthority()) {
                permissionList.add(signingAuthorityCode);
            }
            // Loans
            if (!tpaForm.isNewFirm() || tpaForm.getContractAccess(0).isShowReviewLoans()) {
                permissionList.add(reviewLoansCode);
            }

            com.manulife.pension.delegate.ContractServiceDelegate csd = com.manulife.pension.delegate.ContractServiceDelegate.getInstance();
            // Get the list of contracts associated with this firm
            List<Integer> contractIds = tpaForm.getContractIds();
            if (contractIds == null) {
                contractIds = TPAServiceDelegate.getInstance().getContractsByFirm(tpaForm.getId().intValue());
                tpaForm.setContractIds(contractIds);
            }
            
            /*
             * CL # 132387 - Commented the loop for performance improvement. This needs to be done in a better way by passing all firm contracts at a time.
             * try {
            	for (Integer contractId : contractIds) {
            		TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractId);
            		
					Map contractPermissionMap = SecurityServiceDelegate.getInstance().getUsersWithRolePermission(
							contractId, permissionList, null);
            		
					if (tpaForm.getContractAccess(0).isShowReviewIWithdrawals()){
						// First check that the withdrawals service feature is
						// enabled for this contract
						ContractServiceFeature withdrawalsCSF = csd.getContractServiceFeature(contractId.intValue(),
							ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
						if (withdrawalsCSF != null
							&& ContractServiceFeature.internalToBoolean(withdrawalsCSF.getValue()).booleanValue()) {
							
							String whoWillReviewWithdrawalAttribute = withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS);
							if(ServiceFeatureConstants.WHO_WILL_REVIEW_TPA.equals(whoWillReviewWithdrawalAttribute)){
								if (!tpaForm.isNewFirm() || tpaForm.getContractAccess(0).isShowReviewIWithdrawals()) {
									filterUserList(contractPermissionMap, reviewWithdrawalsCode, selectedAccessCode);
									if (isLastUserWithPermission(contractPermissionMap, reviewWithdrawalsCode,
										managedUserProfileId, tpaForm.isNewFirm())) {
										String contractList = tpaForm.getLastUserWithReviewIWithdrawalsContracts();
										tpaForm.setLastUserWithReviewIWithdrawalsContracts((contractList == null || ""
											.equals(contractList)) ? contractId.toString()
											: (contractList + ", " + contractId));
									}
								}
							}
						}
					}
					// Signing authority
					if (tpaForm.getContractAccess(0).isShowSigningAuthority()) {
						filterUserList(contractPermissionMap, signingAuthorityCode, selectedAccessCode);
						if (isLastUserWithPermission(contractPermissionMap, signingAuthorityCode,
								managedUserProfileId, tpaForm.isNewFirm())) {
							String contractList = tpaForm.getLastUserWithSigningAuthorityContracts();
							tpaForm.setLastUserWithSigningAuthorityContracts((contractList == null || ""
									.equals(contractList)) ? contractId.toString()
									: (contractList + ", " + contractId));
						}
					}
					
					// Review Loans
					if (tpaForm.getContractAccess(0).isShowReviewLoans()){
						// Loans related Last User Permission Flags for TPA
						ContractServiceFeature allowLoansCSF = csd.getContractServiceFeature(contractId.intValue(),
							ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
						if (allowLoansCSF != null
							&& ContractServiceFeature.internalToBoolean(allowLoansCSF.getValue()).booleanValue()) {
							String whoWillReviewLoansAttribute = allowLoansCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS);
							if ((ServiceFeatureConstants.WHO_WILL_REVIEW_TPA).equals(whoWillReviewLoansAttribute)) 
							{
								if (!tpaForm.isNewFirm() || tpaForm.getContractAccess(0).isShowReviewLoans()) {
									filterUserList(contractPermissionMap, reviewLoansCode, selectedAccessCode);
									if (isLastUserWithPermission(contractPermissionMap, reviewLoansCode,
										managedUserProfileId, tpaForm.isNewFirm())) {
										String contractList = tpaForm.getLastUserWithReviewLoansContracts();
										tpaForm.setLastUserWithReviewLoansContracts((contractList == null || ""
											.equals(contractList)) ? contractId.toString()
											: (contractList + ", " + contractId));
									}
								}
							}
						}
					}
				} // end of for loop
			} catch (ApplicationException ae) {
				throw new SystemException(ae, CLASS_NAME, "setLastPermissionFlags", ae.getMessage());
			}*/
        }
    }
    
    private static void filterUserList(Map permissionMap, String permissionCode, String filterCode) {
        List userList = (List) permissionMap.get(permissionCode);
        List filterList = (List) permissionMap.get(filterCode);
        if (userList != null && filterList != null) {
            userList.removeAll(filterList);
        }
    }
    
    private static boolean isLastUserWithPermission(Map permissionMap, String permissionCode, long profileId, boolean newContract) throws SystemException {
        boolean isLast = false;
        List userList = (List) permissionMap.get(permissionCode);
        if (userList == null || userList.size() == 0) {
            isLast = newContract;
        } else {
            if (newContract) {
                isLast = !SecurityServiceDelegate.getInstance().checkUserActive(userList);
            } else {
                Long userProfileId = new Long(profileId);
                if (userList.contains(userProfileId)) {
                    if (userList.size() == 1) {
                        isLast = true;
                    } else {
                        userList.remove(userProfileId);
                        // Ensure that at least one of the users in the list is active
                        isLast = !SecurityServiceDelegate.getInstance().checkUserActive(userList);
                    }
                }
            }
        }
        return isLast;
    }
    
    private static boolean isLastUserWithPermission(Map permissionMap, String permissionCode, long profileId) throws SystemException {
        boolean isLast = false;
        List userList = (List) permissionMap.get(permissionCode);
        if (userList == null || userList.isEmpty()) {
            isLast = true;
        } else {
            Long userProfileId = new Long(profileId);
            if (userList.contains(userProfileId)) {
                userList.remove(userProfileId);
            }
            if (userList.isEmpty()) {
                isLast = true;
            } else {
                // Ensure that at least one of the users in the list is active
                isLast = !SecurityServiceDelegate.getInstance().checkUserActive(userList);
            }
        }
        return isLast;
    }

    // public static List<Integer> getLastUserWithPayrollPathEmailContractList(List<TpaFirm>
    // tpaFirms, long profileId) {
    // // We need to check all of the firms and contracts to check that they will all have the
    // payroll path email preference.
    // List<Integer> lastUserWithPayrollPathEmailContracts = new ArrayList<Integer>();
    //
    // String payrollPathEmailCode =
    // PermissionType.getPermissionCode(PermissionType.RECEIVE_PAYROLL_EMAIL);
    // List<String> permissionList = new ArrayList<String>();
    // permissionList.add(payrollPathEmailCode);
    //
    // for (TpaFirm tpaFirm : tpaFirms) {
    // try {
    // Map permissionMap =
    // SecurityServiceDelegate.getInstance().getTPAUsersWithRolePermission(tpaFirm.getId(),
    // permissionList, null);
    // if (isLastUserWithPermission(permissionMap, payrollPathEmailCode, profileId,
    // tpaFirm.isNewFirm())) {
    // List<Integer> contractNumbers = tpaFirm.getContractIds();
    // if (contractNumbers == null) {
    // contractNumbers =
    // TPAServiceDelegate.getInstance().getContractsByFirm(tpaFirm.getId().intValue());
    // tpaFirm.setContractIds(contractNumbers);
    // }
    // for (Integer contractNumber : contractNumbers) {
    // try {
    // // First check that the TPA Firm has payroll path enabled
    // TPAFirmInfo firmInfo =
    // TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractNumber);
    // if (firmInfo.getContractPermission().isPayrollPathEmail()) {
    // // Next we need to check that the service feature is enabled
    // ContractServiceFeature payrollServiceFeature =
    // com.manulife.pension.delegate.ContractServiceDelegate.getInstance().getContractServiceFeature(
    // contractNumber, ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
    // if
    // (ContractServiceFeature.internalToBoolean(payrollServiceFeature.getValue()).booleanValue()) {
    // // Now we check if this contract will have the payroll path email preference
    // lastUserWithPayrollPathEmailContracts.add(contractNumber);
    // }
    // }
    // } catch (ApplicationException ae) {
    // logger.warn("Error getting payroll path service feature for contract " + contractNumber);
    // } catch (SystemException se) {
    // logger.warn("Error getting TPA firm info for contract " + contractNumber);
    // }
    // }
    // }
    // } catch (SystemException se) {
    // logger.warn("Error getting TPA firm info for firm " + tpaFirm.getId());
    // }
    // }
    // return lastUserWithPayrollPathEmailContracts;
    // }

    public static Collection<GenericException> checkForLockOrDelete(UserProfile loggedInUserProfile, long managedUserProfileId, String managedUserName) throws SystemException {
        Collection<GenericException> errors = new ArrayList<GenericException>();

        if (managedUserName != null && !"".equals(managedUserName)) {
            Long deletedByProfileId = SecurityServiceDelegate.getInstance().getDeletedByProfileId(managedUserName);
            if (deletedByProfileId != null) {
                try {
                    UserInfo deletedByUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(AbstractAddEditUserController.newPrincipal(new Long(0), null, null),
                            deletedByProfileId.longValue());
                    if (deletedByUserInfo != null) {
                        errors.add(new GenericException(1058, new String[] { deletedByUserInfo.getFirstName(),
                                deletedByUserInfo.getLastName() + (deletedByUserInfo.getRole().isInternalUser() ? " John Hancock Representative" : "") }));
                    } else {
                        errors.add(new GenericException(1058, new String[] { "John Hancock", "Representative" }));
                    }
                } catch (SecurityServiceException e) {
                    throw new SystemException(e, ClientUserContractAccessActionHelper.class.getName(), "checkForLockOrDelete", "Failed to get user info of deleted by. "
                            + e.toString());
                }
            }
        }

        if (errors.isEmpty() && managedUserProfileId != 0) {
            if (!LockServiceDelegate.getInstance().lock(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + managedUserProfileId,
                    loggedInUserProfile.getPrincipal().getProfileId())) {
                try {
                    Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + managedUserProfileId);
                    UserInfo lockOwnerUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(AbstractAddEditUserController.newPrincipal(new Long(0), null, null),
                            lockInfo.getLockUserProfileId());

                    String lockOwnerDisplayName = LockHelper.getLockOwnerDisplayName(loggedInUserProfile, lockOwnerUserInfo);
                    errors.add(new GenericException(1057, new String[] { lockOwnerDisplayName }));
                } catch (SecurityServiceException e) {
                    throw new SystemException(e, ClientUserContractAccessActionHelper.class.getName(), "checkForLockOrDelete", "Failed to get user info of lock own. "
                            + e.toString());
                }
            }
        }

        return errors;
    }

}