package com.manulife.pension.ps.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.util.render.SSNRender;

/**
 * class ReportDownloadHelper
 * Various helper methods for CSV report downloads
 * @author sternlu
 *
 */
public class ReportDownloadHelper {
    
	public static final String REPORT_DOWNLOAD_PERMISSION = "REDO" ;
    /**
     * String getDisplaySsn
     * determines if ssn should be masked.
     * called from various report actions
     * @param ssn
     * @param user
     * @param contractId
     * @return
     * @throws SystemException
     */
    public static String getDisplaySsn  (String ssn, UserProfile user, int contractId)throws SystemException{
    
    	String displaySsn = null;
     		displaySsn = SSNRender.format(ssn, null, isMaskedSsn(user, contractId));
        return displaySsn;
    }
        
    /**
     * Determine whether the SSN should be masked in download report.
     * 
     * @param user
     * @param contractId
     * @return
     * @throws SystemException
     */
    public static boolean isMaskedSsn (UserProfile user, int contractId)throws SystemException{
    	
        boolean maskSsn = true;// use masked ssn as a default
    	
    	// As of 13April2009, this permission is always true for the Firm.
    	// TODO:  explicitly assign the Firm permission for safer code
    	//  maintenance and readability going forward.
    	if(user.isAllowedDownloadReport()){ 
    	    maskSsn = false;
        }
    	
        return maskSsn;
        
    }
    
    /**
     * Method to check for Masking SSN By TPAUser And FirmId
     * 
     * @param profileId
     * @param tpaFirmId
     * @return
     * @throws SystemException
     */
    public static boolean isMaskedSsnByTPAUserAndFirmId(Long profileId, Integer tpaFirmId) throws SystemException {
        
        List<String> permissionList = new ArrayList<String>();
        
        permissionList.add(REPORT_DOWNLOAD_PERMISSION);  

        Map<String, List<Long>> userInfoMap = SecurityServiceDelegate
                     .getInstance().getTPAUsersWithRolePermission(tpaFirmId, 
                                   permissionList, null);
        
        List<Long> usersList = userInfoMap.get(REPORT_DOWNLOAD_PERMISSION);
			
        if(usersList!=null && usersList.contains(profileId)){ 
               
               return false; // it means it has SSN Full Access
        }
        
        return true; // means Mask the SSN
 }

}
