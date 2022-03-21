package com.manulife.pension.ps.web.profiles;

import java.util.Iterator;

public class TPAActionHelper {
	
	
	/** 
	 * Used as part of clone
	 * 
	 * @param theTpaFirm
	 */
    public static void syncSettingsToUserPermissions(TpaFirm theTpaFirm) {
   	      	
     	UserPermissions up = theTpaFirm.getContractAccess(0).getUserPermissions();
     	syncSettingsToUserPermissions(up, theTpaFirm);
    }
	
    
    
	/**
	 * setup prior to hitting the sub-page.
	 * 
	 * @param updateMode indicate if we have from a read-only screen or not.
	 * @param actionForm
	 * @param tpaFirmNumber
	 */
    public static void syncSettingsToUserPermissions(AddEditUserForm actionForm, 
    		             int tpaFirmNumber, boolean updateMode) {
    	 
    	TpaFirm theTpaFirm = null;
     	for (Iterator it = actionForm.getTpaFirms().iterator(); it.hasNext();) {
     		theTpaFirm = (TpaFirm) it.next();
            if (theTpaFirm.getId().intValue() == tpaFirmNumber) {
            	break; // found
            }
     	}
     	
        if (updateMode) {
            
            // spf19/20, was it changed? (also first check it was rendered at all, since it may not have been)
            if ((theTpaFirm.getContractAccess(0).getOriginalReviewIWithdrawals() != null) &&
                (!theTpaFirm.getContractAccess(0).getOriginalReviewIWithdrawals()
                     .equals(theTpaFirm.getContractAccess(0).getReviewIWithdrawals()))) { 
                if (theTpaFirm.getContractAccess(0).getReviewIWithdrawals()) {
                    theTpaFirm.getContractAccess(0).setViewAllIWithdrawals(true);
                    theTpaFirm.getContractAccess(0).setInitiateIWithdrawals(true); 
                    //theTpaFirm.getContractAccess(0).setSigningAuthority(false);
                } else {
                    theTpaFirm.getContractAccess(0).setViewAllIWithdrawals(true);
                    theTpaFirm.getContractAccess(0).setInitiateIWithdrawals(false); 
                    //theTpaFirm.getContractAccess(0).setSigningAuthority(false);
                }
            }
            if ((!theTpaFirm.getContractAccess(0).isOriginalReviewLoans() == 
            	(theTpaFirm.getContractAccess(0).isReviewLoans()))) {
            	if(theTpaFirm.getContractAccess(0).isReviewLoans()) {
            		theTpaFirm.getContractAccess(0).setViewAllLoans(true);
            		theTpaFirm.getContractAccess(0).setInitiateLoans(true);
            	} else {
            		theTpaFirm.getContractAccess(0).setViewAllLoans(true);
            		theTpaFirm.getContractAccess(0).setInitiateLoans(false);
            	}
            }

        }

        UserPermissions up = theTpaFirm.getContractAccess(0).getUserPermissions();
     	syncSettingsToUserPermissions(up, theTpaFirm);
    }

    // copy data from TpaFirm to UserPermissions
	private static void syncSettingsToUserPermissions(UserPermissions up, TpaFirm theTpaFirm) {  
		// User management, Reporting, Plan services
     	up.setManageUsers(theTpaFirm.getContractAccess(0).getManageUsers());
     	up.setTpaStaffPlanAccess(theTpaFirm.getContractAccess(0).getTpaStaffPlanAccess());     	
     	up.setDownloadReports(theTpaFirm.getContractAccess(0).getReportDownload());
     	up.setEditPlanData(theTpaFirm.getContractAccess(0).getEditApprovePlan());
     	up.setSubmitUpdateVesting(theTpaFirm.getContractAccess(0).getSubmitUpdateVesting());
     	
     	up.setInitiateAndViewMyWithdrawals(theTpaFirm.getContractAccess(0).getInitiateIWithdrawals());
     	
     	up.setReviewWithdrawals(theTpaFirm.getContractAccess(0).getReviewIWithdrawals());
     	up.setSigningAuthority(theTpaFirm.getContractAccess(0).getSigningAuthority());
     	up.setCreateUploadSubmissions(theTpaFirm.getContractAccess(0).getUploadSubmissions());
     	up.setCashAccount(theTpaFirm.getContractAccess(0).getCashAccount());
     	up.setDirectDebit(theTpaFirm.getContractAccess(0).getDirectDebit());
     	up.setViewAllWithdrawals(theTpaFirm.getContractAccess(0).getViewAllIWithdrawals());
     	
     	// Census
     	up.setUpdateCensusData(theTpaFirm.getContractAccess(0).getUpdateCensusData());
     	up.setViewSalary(theTpaFirm.getContractAccess(0).getViewSalary());
     	
     	// since we have calculated this already, just copy it over 
     	//TODO: remove the below
     	up.setShowEditPlanData(theTpaFirm.getContractAccess(0).isShowPlanData()); // SPF31=STA.34
     	up.setShowSubmitUpdateVesting(theTpaFirm.getContractAccess(0).isShowPlanData() || true);
     	
        up.setShowReviewWithdrawals(theTpaFirm.getContractAccess(0).isShowReviewIWithdrawals()); // SPF33=STA.33
        up.setShowSigningAuthority(theTpaFirm.getContractAccess(0).isShowSigningAuthority());
        
        // Loans permissions
        up.setInitiateLoans(theTpaFirm.getContractAccess(0).isInitiateLoans());
        up.setViewAllLoans(theTpaFirm.getContractAccess(0).isViewAllLoans());
        up.setReviewLoans(theTpaFirm.getContractAccess(0).isReviewLoans());
        up.setShowReviewLoans(theTpaFirm.getContractAccess(0).isShowReviewLoans());
	}
    
    
    
    /**
     * When we leave the sub-page we need to copy attributes back to main form since some are also
     * on that page.
     * 
     */
    public static void copyAttributesToForm(UserPermissions up, int tpaFirmNumber, AddEditUserForm profileForm) {
    
	    TpaFirm theTpaFirm = null;
		for (Iterator it = profileForm.getTpaFirms().iterator(); it.hasNext();) {
			theTpaFirm = (TpaFirm) it.next();
	        if (theTpaFirm.getId().intValue() == tpaFirmNumber) {
	        	break; // found
	        }
		}
		 
		theTpaFirm.getContractAccess(0).setManageUsers(up.isManageUsers());
		theTpaFirm.getContractAccess(0).setTpaStaffPlanAccess(up.isTpaStaffPlanAccess());
		theTpaFirm.getContractAccess(0).setReportDownload(up.isDownloadReports());
		theTpaFirm.getContractAccess(0).setUpdateCensusData(up.isUpdateCensusData());
		theTpaFirm.getContractAccess(0).setViewSalary(up.isViewSalary());
		theTpaFirm.getContractAccess(0).setEditApprovePlan(up.isEditPlanData());
		theTpaFirm.getContractAccess(0).setSubmitUpdateVesting(up.isSubmitUpdateVesting());
		
		// name is a bit different for next two
		theTpaFirm.getContractAccess(0).setReviewIWithdrawals(up.isReviewWithdrawals());
        
        // also re-set the original values for submissions and review i:withdrawals
        theTpaFirm.getContractAccess(0).setOriginalReviewIWithdrawals(theTpaFirm.getContractAccess(0).getReviewIWithdrawals());
		
		// attributes only on the permissions screen.
		theTpaFirm.getContractAccess(0).setUploadSubmissions(up.isCreateUploadSubmissions());
		theTpaFirm.getContractAccess(0).setCashAccount(up.isCashAccount());
		theTpaFirm.getContractAccess(0).setDirectDebit(up.isDirectDebit());
		theTpaFirm.getContractAccess(0).setInitiateIWithdrawals(up.isInitiateAndViewMyWithdrawals());
		theTpaFirm.getContractAccess(0).setSigningAuthority(up.isSigningAuthority());
		theTpaFirm.getContractAccess(0).setViewAllIWithdrawals(up.isViewAllWithdrawals());
		// Loans
		theTpaFirm.getContractAccess(0).setInitiateLoans(up.isInitiateLoans());
		theTpaFirm.getContractAccess(0).setViewAllLoans(up.isViewAllLoans());
		theTpaFirm.getContractAccess(0).setReviewLoans(up.isReviewLoans());
        theTpaFirm.getContractAccess(0).setOriginalReviewLoans(theTpaFirm.getContractAccess(0).isReviewLoans());
		
		theTpaFirm.getContractAccess(0).setDetailedPermissionsSet(true); // completed screen via continue not cancel
    }
    
}
