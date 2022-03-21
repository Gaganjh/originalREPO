/*
 * Created on Nov 3, 2005
 *
 */
package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author sternlu
 *
 */
public class TpaumHelper {
	
	
	// Add/EditTpUser page
	
	public static boolean isTPAUM(UserProfile userProfile) {
		return isTPAWithManageUsers(userProfile.getPrincipal().getRole()); 
	}
	
	public static boolean isTPAUMForAllFirms (Collection tpaFirms)
	{
		
		for(Iterator it = tpaFirms.iterator();it.hasNext();)
		{
			TPAFirmInfo firmInfo = (TPAFirmInfo)it.next();
			if (!isTPAWithManageUsers(firmInfo.getContractPermission().getRole())) 	return false;
		}
		
		return true;
	}
	
	
	static List getTpaFirmsForTpaum(Collection allFirms)		
	{
		List tpaFirms = new ArrayList();
		UserRole role = null;
		for(Iterator it = allFirms.iterator();it.hasNext();)
		{
			TPAFirmInfo firmInfo = (TPAFirmInfo)it.next();
			role =firmInfo.getContractPermission().getRole();
			if (isTPAWithManageUsers(role)) tpaFirms.add(firmInfo); 			
		}

		return tpaFirms;
		
	}
	
	static void populateTpaFirmDropDown(
			ActionForm actionForm, HttpServletRequest request) {
		
		AddEditUserForm form = (AddEditUserForm) actionForm;
		
		// if only one firm don't make a drop down, since it will be auto-added to the 
		// form for editing(this is done in doDefault)
		if (form.getTpaumFirms().size() == 1) return;
		
        List firmList = new ArrayList();
		List firms = form.getTpaumFirms();
		List formFirms =form.getUndeletedTpaFirms(); 
		
		Iterator it = firms.iterator();
		while(it.hasNext()) {
			TPAFirmInfo firmInfo = (TPAFirmInfo)it.next();
			int firmId = firmInfo.getId();
			boolean found = false;
			if(formFirms !=null && formFirms.size()>0) {
				for(Iterator init=formFirms.iterator();init.hasNext();)	{
					TpaFirm firm =(TpaFirm)init.next();
					if(firm.getId().intValue()==firmId) {
						found = true;
						break;
					}
				}
			}
			if(!found) {
				String label = firmId + " " + firmInfo.getName();
				firmList.add(new LabelValueBean( label, String.valueOf(firmId)));
			}
		}

		// set where jsp is going to pick it up from
        if (!firmList.isEmpty()) {
            firmList.add(0, new LabelValueBean("Select one", ""));     
    		request.setAttribute(Constants.ADD_EDIT_USER_TPA_FIRM_LIST, firmList);
        }
	}

	   static List getEditUserTUMFirms(String userName) {
        List firms = null;
        try {
            UserInfo userInfo = SecurityServiceDelegate.getInstance()
                    .searchByUserName(
                            AbstractAddEditUserController.newPrincipal(new Long(0),
                                    null, null), userName);
            TPAFirmInfo[] tpaFirms = userInfo.getTpaFirms();
            firms = new ArrayList(tpaFirms.length);
            for (int i = 0; i < tpaFirms.length; i++) {
            	if (isTPAWithManageUsers(tpaFirms[i].getContractPermission().getRole())) {
                    firms.add(new Integer(tpaFirms[i].getId()));
                }
            }
        } catch (Exception e) {
            /*
             * This should not happen because we are only retrieving the user
             * info object using a generic principal.
             */
            throw new NestableRuntimeException(e);
        }
        return firms;
    }
	   
	/**
	 * Check if user is tpa with manage users.
	 * 
	 * @param role
	 * @return
	 */   
	private static boolean isTPAWithManageUsers(UserRole role) {

		return role.isTPA() && role.hasPermission(PermissionType.MANAGE_TPA_USERS);
	}
	
}
