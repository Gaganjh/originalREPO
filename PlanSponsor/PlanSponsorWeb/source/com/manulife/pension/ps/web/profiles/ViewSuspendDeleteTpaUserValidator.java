package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.ps.web.validation.rules.TpaAcessLevelRule;
import com.manulife.pension.ps.web.validation.rules.TpaFirmRule;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class ViewSuspendDeleteTpaUserValidator extends ValidatorUtil implements Validator {
	protected static final String ADD_TPA_FIRM_ACTION = "addTpaFirm";
	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	@Override
	public void validate(Object target, Errors error) {
		BindingResult bindingResult = (BindingResult) error;
		if(!bindingResult.hasErrors()){
		AddEditUserForm form = (AddEditUserForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
	
		Collection errorList  = new ArrayList();
		UserProfile userProfile = getUserProfile(request);

		/*
		 * Different validation rules when we save and when we add tpa firm.
		 */
		if (form.isSaveAction()) {

			SsnRule.getInstance().validate(AddEditUserForm.FIELD_SSN,
					errorList, form.getSsn());

			// MPR 575, section 3.3.8 email address.
			if (form.getEmail() !=null && 
					(form.getEmail().toLowerCase().indexOf("@jhancock") != -1 || form.getEmail().toLowerCase().indexOf("@manulife") != -1)) {
				
				
				// check if TPA firm id = 52801 has been selected or not
				boolean specialFirmFound = false;
				for (Iterator it = form.getTpaFirms().iterator(); it.hasNext();) {
					TpaFirm tpaFirmForm = (TpaFirm) it.next();
					if (tpaFirmForm.getId().intValue() == 52801) {
						specialFirmFound = true;
					}
				}
				
				if (!specialFirmFound){
					 errorList.add(new ValidationError(AddEditUserForm.FIELD_EMAIL, ErrorCodes.EMAIL_INVALID));
				}
			}	
			
			if (form.getSecondaryEmail() !=null && 
					(form.getSecondaryEmail().toLowerCase().indexOf("@jhancock") != -1 || form.getSecondaryEmail().toLowerCase().indexOf("@manulife") != -1)) {
				
				
				// check if TPA firm id = 52801 has been selected or not
				boolean specialFirmFound = false;
				for (Iterator it = form.getTpaFirms().iterator(); it.hasNext();) {
					TpaFirm tpaFirmForm = (TpaFirm) it.next();
					if (tpaFirmForm.getId().intValue() == 52801) {
						specialFirmFound = true;
					}
				}
				
				if (!specialFirmFound){
					
					
					 errorList.add(new ValidationError(AddEditUserForm.FIELD_SECONDARY_EMAIL, ErrorCodes.SECONDARY_EMAIL_INVALID));
				}
			}	
			
			
			TpaAcessLevelRule accessRule = new TpaAcessLevelRule(ErrorCodes.BAD_TPA_ACCESS_LEVEL);
		    accessRule.validate("", errorList, new Pair(form.getUndeletedTpaFirms(), form.isHiddenFirmExist()));

		    resetTpaDropdowns(form, request);

		} else if (form.getAction().equals(ADD_TPA_FIRM_ACTION)) {

			TpaFirmRule.getInstance().validate(
					AddEditUserForm.FIELD_TPA_FIRM_ID_TO_ADD, errorList,
					new Pair(form.getTpaFirms(), form.getTpaFirmId()));
			resetTpaDropdowns( form, request);
		} else if (!userProfile.isInternalUser() && (form.isSuspendAction() || form.isUnsuspendAction() || form.isDeleteAction())) {
		    // STV30 & STV43 from DFS25, TTP.68
		    // check if you have manage user permissions on ALL firms that the user you are
		    // attempting to access has himself access to.
		    if (form.isHiddenFirmExist().booleanValue()) {
		        errorList.add(new GenericException(1084));
		    }
		}
		if (!errorList.isEmpty()) {
			request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, errorList);
 			request.setAttribute(PsBaseUtil.ERROR_KEY, errorList);
		}
	
		}	
		
		
	}
	protected void resetTpaDropdowns(
			AddEditUserForm form, HttpServletRequest request) {

		if (getUserProfile(request).getRole().isTPA() &&
				 getUserProfile(request).getRole().hasPermission(PermissionType.MANAGE_TPA_USERS)) {
			TpaumHelper.populateTpaFirmDropDown( form, request);
		}
	}
}
