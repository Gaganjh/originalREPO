package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.platform.web.validation.rules.PrimaryEmailRule;
import com.manulife.pension.platform.web.validation.rules.SecondaryEmailRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.util.content.GenericException;
@Component
public class AddEditTpaUserSaveValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return AddEditUserForm.class.equals(arg0);
	}
	@Override
	public void validate(Object target, Errors error) {
		BindingResult bindingResult = (BindingResult) error;
		if(!bindingResult.hasErrors()){
		Collection errors = new ArrayList();
		AddEditUserForm form = (AddEditUserForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
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
	        }
			if(!errors.isEmpty()){
				setErrorsInSession(request, errors);
			}
			
			}
		
	}
	protected void setErrorsInSession(HttpServletRequest request,
			Collection errors) {
		BaseSessionHelper.setErrorsInSession(request, errors);
		
}
}
