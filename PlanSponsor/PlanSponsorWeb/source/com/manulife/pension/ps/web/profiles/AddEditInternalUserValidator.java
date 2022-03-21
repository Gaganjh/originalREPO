package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.platform.web.validation.rules.PrimaryEmailRule;
import com.manulife.pension.platform.web.validation.rules.SecondaryEmailRule;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.validation.rules.EmailLocalRule;
import com.manulife.pension.ps.web.validation.rules.EmployeeNumberRule;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class AddEditInternalUserValidator implements Validator {

private static Logger logger = Logger.getLogger(AddEditInternalUserValidator.class);
	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	@Override
	public void validate(Object target, Errors error) {
		BindingResult bindingResult = (BindingResult) error;
		if(!bindingResult.hasErrors()){
		Collection errors = new ArrayList();
		AddEditUserForm form = (AddEditUserForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		String[] errorCodes = new String[10];
		

		/*
		 * Validate the action form only when we save.
		 */
		if (form.isSaveAction()) {

            NameRule.getFirstNameInstance().validate(AddEditUserForm.FIELD_FIRST_NAME,
                    errors, form.getFirstName());
            NameRule.getLastNameInstance().validate(AddEditUserForm.FIELD_LAST_NAME, errors,
                    form.getLastName());
            
            if(form.isWebAccess() || form.getEmail().length() > 0){
            	// Email field is mandatory only when web access is set to Yes
            	PrimaryEmailRule.getInstance().validate(AddEditUserForm.FIELD_EMAIL, errors,
            			form.getEmail());
            }
            
            if(form.isWebAccess() && form.getSecondaryEmail() != null && form.getSecondaryEmail().length() > 0){
            	// Email field is mandatory only when web access is set to Yes
            	SecondaryEmailRule.getInstance().validate(AddEditUserForm.FIELD_SECONDARY_EMAIL, errors,
            			form.getSecondaryEmail());
            }
            if (!form.getChanges().isChanged()) {
                GenericException ex = new GenericException(ErrorCodes.SAVING_WITH_NO_CHANGES);
                errors.add(ex);
            }
        
            
			EmployeeNumberRule.getInstance().validate(
					AddEditUserForm.FIELD_EMPLOYEE_NUMBER, errors,
					form.getEmployeeNumber());

			// this is a bit of a hack but the validation rule framework is a
			// bit restrictive since everything is a singleton
			if (!UserNameRule.getInstance().validate(
					AddEditUserForm.FIELD_USER_NAME, errors,
					form.getUserName())) {
				for (Iterator it = errors.iterator(); it.hasNext();) {
					ValidationError validationError = (ValidationError) it
							.next();

					if (ErrorCodes.USER_NAME_MANDATORY == validationError
							.getErrorCode()) {
						validationError
								.setErrorCode(ErrorCodes.INTERNAL_USER_NAME_MANDATORY);
					}
				}
			}

			EmailLocalRule.getInstance().validate(
					AddEditUserForm.FIELD_EMAIL, errors, form.getEmail());
			if(form.getSecondaryEmail() != null && form.getSecondaryEmail().length()>0){
				EmailLocalRule.getInstance().validate(
						AddEditUserForm.FIELD_SECONDARY_EMAIL, errors, form.getSecondaryEmail());
				
			}
			if (BDUserRoleType.RVP.getUserRoleCode().equals(
					form.getBrokerDealerSiteRole())
					&& StringUtils.isEmpty(form.getRvpId())) {
				
				errors.add(new ValidationError(AddEditUserForm.FIELD_RVP,
						ErrorCodes.BDW_RVP_NOT_SELETED));
			}
			if (RelationshipManager.ID.equals(form.getPlanSponsorSiteRole())
					&& StringUtils.isEmpty(form.getRmId())) {
				errors.add(new ValidationError(AddEditUserForm.FIELD_RM,
						ErrorCodes.RM_NOT_ASSIGNED));
			}
		}
		if(!errors.isEmpty()){
			for (Object e : errors) {
				if (e instanceof GenericException) {
					GenericException errorEx=(GenericException) e;
					errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
					bindingResult.addError(new ObjectError(error
							                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					
				}
				
			}
			if(request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null){
		    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
		    }
				
				request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
				request.removeAttribute(PsBaseUtil.ERROR_KEY);
				request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
				request.setAttribute(PsBaseUtil.ERROR_KEY, bindingResult);
		}
	}
		
  }
}
