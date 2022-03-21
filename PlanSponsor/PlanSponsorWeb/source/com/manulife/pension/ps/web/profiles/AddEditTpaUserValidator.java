package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.util.ExtensionRule;
import com.manulife.pension.platform.web.util.FaxRule;
import com.manulife.pension.platform.web.util.MobileRule;
import com.manulife.pension.platform.web.util.PhoneRule;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.ps.web.validation.rules.TpaAcessLevelRule;
import com.manulife.pension.ps.web.validation.rules.TpaFirmRule;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;
@Component
public class AddEditTpaUserValidator extends ValidatorUtil implements Validator  {
	
	protected static final String ADD_TPA_FIRM_ACTION = "addTpaFirm";
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
				// SSN field is mandatory only when web access is Yes
				if(form.isWebAccess() || !form.getSsn().isEmpty()){ 
					SsnRule.getInstance().validate(AddEditUserForm.FIELD_SSN,	errors, form.getSsn());
				  	
				}
				
				PhoneRule.getInstance().validate(AddEditUserForm.FIELD_PHONE_NUMBER,	errors, form.getPhone().getValue());
			  
				if(StringUtils.isNotEmpty(form.getPhone().getValue()))
				{
					if(StringUtils.isEmpty(form.getPhone().getAreaCode()) || StringUtils.isEmpty(form.getPhone().getPhonePrefix())
								|| StringUtils.isEmpty(form.getPhone().getPhoneSuffix()) || form.getPhone().getValue().length() < 10)
					{
						errors.add(new ValidationError(AddEditUserForm.FIELD_PHONE_NUMBER, ErrorCodes.PHONE_NOT_COMPLETE));
					}
					if(StringUtils.isNotEmpty(form.getPhone().getAreaCode()) && StringUtils.isNotEmpty(form.getPhone().getPhonePrefix()))
					{
						String areaCode = null,phonePrefix = null;
						areaCode = form.getPhone().getAreaCode();
						phonePrefix = form.getPhone().getPhonePrefix();
						if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || phonePrefix.charAt(0) == '0' || phonePrefix.charAt(0) == '1')
						{	
							errors.add(new ValidationError(AddEditUserForm.FIELD_PHONE_NUMBER, ErrorCodes.PHONE_INVALID));
						}
					}
				}
				FaxRule.getInstance().validate(AddEditUserForm.FIELD_FAX_NUMBER,	errors, form.getFax().getValueTPA());
			  
				if(StringUtils.isNotEmpty(form.getFax().getValue()))
				{
					if(StringUtils.isEmpty(form.getFax().getAreaCode()) || StringUtils.isEmpty(form.getFax().getFaxPrefix())
								|| StringUtils.isEmpty(form.getFax().getFaxSuffix()) || form.getFax().getValueTPA().length() < 10)
					{
						errors.add(new ValidationError(AddEditUserForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_NOT_COMPLETE));
					}
					if(StringUtils.isNotEmpty(form.getFax().getAreaCode()) && StringUtils.isNotEmpty(form.getFax().getFaxPrefix()))
					{
						String areaCode = null,faxPrefix = null;
						areaCode = form.getFax().getAreaCode();
						faxPrefix = form.getFax().getFaxPrefix();
						if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || faxPrefix.charAt(0) == '0' || faxPrefix.charAt(0) == '1')
						{
							errors.add(new ValidationError(AddEditUserForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_INVALID));
							
						}
					}
				}
				
				if (StringUtils.isNotEmpty(form.getExt())) {
					ExtensionRule.getInstance().validate(AddEditUserForm.FIELD_EXTENSION_NUMBER, errors, form.getExt());
					if (StringUtils.isEmpty(form.getPhone().getValue()) && StringUtils.isNotEmpty(form.getExt())) {
						errors.add(new ValidationError(AddEditUserForm.FIELD_EXTENSION_NUMBER,
								ErrorCodes.PH_NOTENTERED_EXT_ENTERED));
					}
				}

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
						
						errors.add(new ValidationError(AddEditUserForm.FIELD_EMAIL, ErrorCodes.EMAIL_INVALID));
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
						
						errors.add(new ValidationError(AddEditUserForm.FIELD_SECONDARY_EMAIL, ErrorCodes.SECONDARY_EMAIL_INVALID));
					}
				}	
				
				
				TpaAcessLevelRule accessRule = new TpaAcessLevelRule(ErrorCodes.BAD_TPA_ACCESS_LEVEL);
	            accessRule.validate("", errors, new Pair(form.getUndeletedTpaFirms(), form.isHiddenFirmExist()));
	            
	           resetTpaDropdowns( form, request);
			} else if (form.getAction().equals(ADD_TPA_FIRM_ACTION)) {

				TpaFirmRule.getInstance().validate(
						AddEditUserForm.FIELD_TPA_FIRM_ID_TO_ADD, errors,
						new Pair(form.getTpaFirms(), form.getTpaFirmId()));

	           resetTpaDropdowns( form, request);
			}
			

				if (!errors.isEmpty()) {
					request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
					request.removeAttribute(PsBaseUtil.ERROR_KEY);
					request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, errors);
		 			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
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
