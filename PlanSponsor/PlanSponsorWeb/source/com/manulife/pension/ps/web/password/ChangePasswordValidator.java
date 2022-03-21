package com.manulife.pension.ps.web.password;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.util.Pair;

@Component
public class ChangePasswordValidator implements Validator {

	private static final String CONTINUE = "continue";
	private static final String SAVE = "save";

	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePasswordForm.class.isAssignableFrom(clazz);
	}
	
    

	public void validate(Object target, Errors errors) {


		try{
		BindingResult bindingResult = (BindingResult)errors;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		HttpSession session = request.getSession(false);
		
		//Collection errors = super.doValidate(mapping, form, request);
		ChangePasswordForm actionForm = (ChangePasswordForm) target;
		Collection errorList=new ArrayList();//((BeanPropertyBindingResult) bindingResult).getAllErrors();
		
		String[] errorCodes = new String[]{Integer.toString(ErrorCodes.CURRENT_PASSWORD_MANDATORY),
											Integer.toString(ErrorCodes.CONFIRM_PASSWORD_MANDATORY),
											Integer.toString(ErrorCodes.PASSWORD_MANDATORY)};	
		Set<Integer> errorCodeSet = new HashSet<Integer>();
		if(!bindingResult.hasErrors()){
		if (actionForm.getButton() != null
				&& (actionForm.getButton().equals(CONTINUE) || actionForm.getButton().equals(SAVE))) {

			if (getUserProfile(request).getRole() instanceof ExternalUser) {
				SsnRule.getInstance().validate(
					ChangePasswordForm.FIELD_SSN,errorList, actionForm.getSsnValue());
			}
			
			// new password mandatory 
			if (actionForm.getNewPassword().length() == 0) {
				bindingResult.addError(new ObjectError(ChangePasswordForm.FIELD_NEW_PASSWORD ,ErrorCodes.PASSWORD_MANDATORY+""));
			}

			// confirm new password mandatory 
			if (actionForm.getConfirmPassword().length() == 0) {
				bindingResult.addError(new ObjectError("pswErrors",errorCodes ,null, ""));
				errorCodeSet.add(ErrorCodes.CONFIRM_PASSWORD_MANDATORY);
				//errors.add(new ValidationError(ChangePasswordForm.FIELD_CONFIRM_PASSWORD ,ErrorCodes.CONFIRM_PASSWORD_MANDATORY));
			}

			// old password mandatory 
			if (actionForm.getOldPassword().length() == 0) {
				bindingResult.addError(new ObjectError("pswErrors",errorCodes ,null, ""));
				errorCodeSet.add(ErrorCodes.CURRENT_PASSWORD_MANDATORY);
				
				//errors.add(new ValidationError(ChangePasswordForm.FIELD_OLD_PASSWORD ,ErrorCodes.CURRENT_PASSWORD_MANDATORY));
			}
			
			Pair pair = new Pair(actionForm.getNewPassword(), actionForm
					.getConfirmPassword());

			// New password mandatory and standards must be met
//			NewPasswordRule.getInstance().validate(
//					ChangePasswordForm.FIELD_NEW_PASSWORD, errors, pair);
//			
			//TODO: see if you can encapsulate the following if needed to
			// be so
			if (actionForm.getNewPassword().length() > 0) {
				
				NewPasswordRule
						.getInstance()
						.validate(
								new String[] {
										ChangePasswordForm.FIELD_NEW_PASSWORD,
										ChangePasswordForm.FIELD_CONFIRM_PASSWORD },
								errorList, pair);

				/*
				new RegularExpressionRule(ErrorCodes.PASSWORD_FAILS_STANDARDS,
						"^[a-zA-Z0-9]{5,32}$").validate(
						ChangePasswordForm.FIELD_NEW_PASSWORD, errors,
						actionForm.getNewPassword());
				new MatchRule(ErrorCodes.PASSWORDS_DO_NOT_MATCH)
					.validate(
							new String[] {
									ChangePasswordForm.FIELD_NEW_PASSWORD,
									ChangePasswordForm.FIELD_CONFIRM_PASSWORD },
							errors, pair);
							*/
			}
			
			if (!errorList.isEmpty()) {
				SessionHelper.setErrorsInSession(request, errorList);
			}

		}
		}
		
		}catch(Exception ex){
			System.out.println("Exception in Change password Validator");
}

	
		
	}

	public static UserProfile getUserProfile(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
		} else {
			return null;
		}
	}

}
