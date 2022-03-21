package com.manulife.pension.ps.web.profiles;


import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.content.GenericException;
@Component
public class TpaFirmValidator extends ValidatorUtil implements Validator,CommonConstants {
	  private static final String REFRESH = "refresh";
	private static Logger logger = Logger.getLogger(TpaFirmValidator.class);
	

	/*@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}*/

	@Override
	public boolean supports(Class<?> clazz) {
		return TpaFirm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Collection error = new ArrayList();
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		TpaFirm form = (TpaFirm) target;
		/*
		 * Prevent user from copy and paste URL.
		 */
		UserProfile userProfile = getUserProfile(request);

		try {
			setShowManageTpaFirmLink(request);

			
		} catch (SystemException e) {
			/*
			 * We should never see this exception.
			 */
			GenericException ex = new GenericException(
					ErrorCodes.TECHNICAL_DIFFICULTIES);
			error.add(ex);
		}

       

		/*
		 * If this is a save action, we should compare the token and make sure
		 * it's still valid. Token is initialized in the doDefault() method and
		 * reset in the doSave() method.
		 */
		if (form.isSaveAction()) {
			/*//TODO if (!isTokenValid(request)) {
				return forwards.get(FORWARD_TPA_CONTACTS);
			}*/
		}

		error.addAll(doValidate(form, request));

		/*
		 * Errors are stored in the session so that our REDIRECT can look up the
		 * errors.
		 */
		
		
	}

	private Collection doValidate(TpaFirm form, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
     * @param request
     * @return
     * @throws SystemException
     */
    public static void setShowManageTpaFirmLink(HttpServletRequest request)
			throws SystemException {
		UserProfile userProfile = getUserProfile(request);

		if (userProfile.getContractProfile().getShowManageTpaFirmLink() == null) {
			boolean showEditTpaFirm = false;
			Contract currentContract = userProfile.getCurrentContract();

			UserRole role = userProfile.getRole();

			if (role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS))
			{

				if ((role.isExternalUser() && !role.hasPermission(PermissionType.SELECTED_ACCESS)) || role.isInternalUser()) {

					TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance()
							.getFirmInfoByContractId(
									currentContract.getContractNumber());
					if (firmInfo != null) {
						showEditTpaFirm = true;
					}
				}
			}
			userProfile.getContractProfile().setShowManageTpaFirmLink(
					showEditTpaFirm ? Boolean.TRUE : Boolean.FALSE);
		}
	}
    

	
	
}
