package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.util.content.GenericException;
@Component
public class ManageUsersSelectValidator implements Validator {


	@Override
	public void validate(Object target, Errors errors) {
		
		ActionForm form = (ActionForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
			BindingResult bindingResult = (BindingResult)errors;
			String[] errorCodes = new String[10];
			UserRole role = getUserProfile(request).getPrincipal().getRole();
			String forwardValue = "";
			if (role.hasPermission(PermissionType.MANAGE_TPA_USERS))
			{
				forwardValue = "tpa";
			}
			else if (role.hasPermission(PermissionType.MANAGE_INTERNAL_USERS))
			{
				forwardValue = "internal";
			}
			ArrayList<GenericException> error= (ArrayList<GenericException>) PsValidator1.doValidatePenTestAction(form,request,forwardValue);
			if ( !error.isEmpty()) {

				for(GenericException errorEx :error){
					errorEx.getMessage();
					errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
					bindingResult.addError(new ObjectError(errors
							                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					
				}
				request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
				request.removeAttribute(PsBaseUtil.ERROR_KEY);
				request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, errors);
	 			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
		
			}	
	}

	@Override
	public boolean supports(Class paramClass) {
		return true;
	}
	public static UserProfile getUserProfile(final HttpServletRequest request) {
		return SessionHelper.getUserProfile(request);
	}
	
}