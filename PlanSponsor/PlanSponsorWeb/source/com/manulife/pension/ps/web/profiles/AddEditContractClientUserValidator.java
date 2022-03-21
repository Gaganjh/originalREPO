package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import com.manulife.pension.service.contract.valueobject.Contract;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.ExtensionRule;
import com.manulife.pension.platform.web.util.FaxRule;
import com.manulife.pension.platform.web.util.PhoneRule;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.EmailRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.ps.web.validation.rules.AddContractNumberUserRule;
import com.manulife.pension.ps.web.validation.rules.EmailWithoutMandatoryRule;
import com.manulife.pension.ps.web.validation.rules.SsnWithoutMandatoryRule;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.PlanSponsorUser;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class AddEditContractClientUserValidator extends ValidatorUtil implements Validator {

private static Logger logger = Logger.getLogger(AddEditContractClientUserValidator.class);
private static final String ADD_CONTRACT_ACTION = "addContract";
private static final String ADD_PARAM = "add";
protected static final String JHANCOCK = "@jhancock";

protected static final String MANULIFE = "@manulife";
	@Override
	public boolean supports(Class<?> clazz) {
		return AddEditClientUserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors error) {
		BindingResult bindingResult = (BindingResult) error;
		if(!bindingResult.hasErrors()){
		Collection errors = new ArrayList();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		String[] errorCodes = new String[10];
			
		AddEditClientUserForm form = (AddEditClientUserForm) target;
		 List<Integer> contractIdList = new ArrayList<Integer>();
	        
	        UserProfile userProfile = getUserProfile(request);
	        
	        UserInfo managedUserInfo = (UserInfo) request.getSession(false).getAttribute("managedUserInfo");
	        
	        /*
	         * Validate the action form only when we save.
	         */
	        if (form.isSaveAction()) {

	            /*
	             * Make sure users have at least one contract access.
	             */
	            if (form.getAction().equals(ADD_PARAM)) {
	                boolean atLeastOneContractAccess = false;
	                ContractPermission[] permissions = managedUserInfo.getContractPermissions(); 
	                for (int i= 0; i < permissions.length && !atLeastOneContractAccess; i++) {
	                	RoleValueLabelBean role = new RoleValueLabelBean(permissions[i].getRole().toString(), permissions[i].getRole().getDisplayName());
	                    if (!role.equals(AccessLevelHelper.NO_ACCESS)) {
	                    	atLeastOneContractAccess = true;
	                    }
	                }
	                if (!atLeastOneContractAccess) {
	                    String fieldId = AddEditClientUserForm.FIELD_CONTRACT_ACCESS
	                    // commented out to avoid highlighting of the label
	                            // + "[0]."
	                            + ClientUserContractAccess.FIELD_PLANSPONSORSITE_ROLE;
	                    errors.add(new ValidationError(fieldId,
	                            ErrorCodes.PROFILE_MUST_HAVE_ONE_CONTRACT));
	                    errors.add(error);
	                }
	            }
	        } else if (form.getAction().equals(ADD_CONTRACT_ACTION)) {
	            // Validate contract number DFS11 ICE9
	            try {
	            	ContractPermission[] permissions = managedUserInfo.getContractPermissions(); 
	            	contractIdList = ClientUserContractAccessActionHelper.getContractIdList( form, request, userProfile, permissions);
					AddContractNumberUserRule.getInstance().validate(AddEditClientUserForm.FIELD_CONTRACT_TO_ADD, errors, 
							new Pair(contractIdList, form.getContractToAdd()));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            if (errors.isEmpty()) {
	                // Validate contract actually exists
	                Integer contractNumber = Integer.valueOf(form.getContractToAdd());
	                try {
	                    Contract contract = ClientUserContractAccessActionHelper.getContract(userProfile.getRole(), contractNumber);
	                } catch (SystemException e) {
	                    if (e.getCause() instanceof ContractNotExistException) {
	                    	errors.add(new ValidationError(AddEditClientUserForm.FIELD_CONTRACT_TO_ADD, ErrorCodes.CONTRACT_NUMBER_INVALID));
	                    } else {
	                        logger.error(e);
	                        throw new RuntimeException(e);
	                    }
	                }
	            }
	        }else if(form.getAction().equals("changeRole")){
	        	ContractPermission[] permissions = managedUserInfo.getContractPermissions(); 
	            // build contract access list
	            for (int i = 0; i < permissions.length; i++) {
	              //SCC.204
	        		RoleValueLabelBean role = new RoleValueLabelBean(permissions[i].getRole().toString(), permissions[i].getRole().getDisplayName());
	        		try {
						boolean isIccDesignate = SecurityServiceDelegate.getInstance().isICCDesignateEligible(permissions[i].getContractNumber());
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		if(permissions[i].isIccDesignate() && !Trustee.ID.equals(role.getValue())
	        				&& !AuthorizedSignor.ID.equals(role.getValue())
	        				&& !AdministrativeContact.stringID.equals(role.getValue())) {
	                    GenericException ex = new GenericException(1365, new String[] {String.valueOf(permissions[i].getContractNumber())});
	                    errors.add(ex);
	        		}
	        		
	        		if(permissions[i].isSendServiceDesignate() && !Trustee.ID.equals(role.getValue())
	        				&& !AuthorizedSignor.ID.equals(role.getValue())
	        				&& !AdministrativeContact.stringID.equals(role.getValue())) {
	                    GenericException ex = new GenericException(3155, new String[] {String.valueOf(permissions[i].getContractNumber())});
	                    errors.add(ex);
	        		}
	            }
	        }


		/*
		 * Validate the action form only when we save.
		 */
		// MPR213 first name & last name
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
	
	protected boolean emailContainCompanyName(String email) {
		return email.contains(JHANCOCK) || email.contains(MANULIFE);
	}

}
