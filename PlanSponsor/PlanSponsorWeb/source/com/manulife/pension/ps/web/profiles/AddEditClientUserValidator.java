package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SearchTpaValidator;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.ps.web.validation.rules.AddContractNumberRule;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class AddEditClientUserValidator extends ValidatorUtil implements Validator{
	
	private static Logger logger = Logger.getLogger(AddEditClientUserValidator.class);
	   private static final String ADD_CONTRACT_ACTION = "addContract";
	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	@Override
	public void validate(Object target, Errors error) {
		BindingResult bindingResult = (BindingResult) error;
		AddEditClientUserForm form = new AddEditClientUserForm();
		if(!bindingResult.hasErrors()){
			if(target instanceof AddEditClientUserForm ){
				 form = (AddEditClientUserForm) target;
			}
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		UserProfile loginUserProfile = SessionHelper.getUserProfile(request);
		Collection errors = new ArrayList();
		
	
		  if (form.isSaveAction()) {
	                boolean atLeastOneContractAccess = false;

	                for (Iterator it = form.getContractAccesses().iterator(); it.hasNext()
	                        && !atLeastOneContractAccess;) {
	                    ClientUserContractAccess contractAccess = (ClientUserContractAccess) it.next();
	                    if (!contractAccess.getPlanSponsorSiteRole()
	                            .equals(AccessLevelHelper.NO_ACCESS)) {
	                        atLeastOneContractAccess = true;
	                    }
	                }
	                if (!atLeastOneContractAccess) {
	                    String fieldId = AddEditClientUserForm.FIELD_CONTRACT_ACCESS
	                         + ClientUserContractAccess.FIELD_PLANSPONSORSITE_ROLE;
	                    
	                    String[] errorCode = new String[]{Integer.toString(ErrorCodes.PROFILE_MUST_HAVE_ONE_CONTRACT)};
	                    bindingResult.addError(new ObjectError(fieldId,errorCode,new String[]{}, "default"));
	                }
	            
	        } else if (form.getAction().equals(ADD_CONTRACT_ACTION)) {
	           
	            AddContractNumberRule.getInstance().validate(AddEditClientUserForm.FIELD_CONTRACT_TO_ADD, errors, new Pair(form.getContractAccesses(), form.getContractToAdd()));
	            if (errors.isEmpty()) {
	               
	                UserProfile userProfile = getUserProfile(request);
	                Integer contractNumber = Integer.valueOf(form.getContractToAdd());
	                try {
	                    Contract contract = ClientUserContractAccessActionHelper.getContract(userProfile.getRole(), contractNumber);
	                } catch (SystemException e) {
	                    if (e.getCause() instanceof ContractNotExistException) {
	                    	String[] errorCode = new String[]{Integer.toString(ErrorCodes.CONTRACT_NUMBER_INVALID)};
		                    bindingResult.addError(new ObjectError(AddEditClientUserForm.FIELD_CONTRACT_TO_ADD,errorCode,new String[]{}, "default"));
	                        
	                    } else {
	                        logger.error(e);
	                        throw new RuntimeException(e);
	                    }
	                }
	            }
	        }else if(form.getAction().equals("changeRole")){
	        	for (int i = 0; i < form.getContractAccesses().size(); i++) {
	                ClientUserContractAccess contractAccess = (ClientUserContractAccess) form.getContractAccesses().get(i);
	                    
	                String[] params = new String[] { contractAccess.getContractNumber().toString() };
	              //SCC.204
	        		if(contractAccess.isIccDesignate() && !Trustee.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
	        				&& !AuthorizedSignor.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
	        				&& !AdministrativeContact.stringID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
	                    GenericException ex = new GenericException(1365, params);
	                    String[] errorCode = new String[]{"1365"};
	                    bindingResult.addError(new ObjectError(error.getObjectName(),errorCode,params, "default"));
	                  
	        		}
	        		if(contractAccess.isSendServiceDesignate() && !Trustee.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
	        				&& !AuthorizedSignor.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
	        				&& !AdministrativeContact.stringID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
	        			String[] errorCode = new String[]{"3155"};
	                    bindingResult.addError(new ObjectError(error.getObjectName(),errorCode,params, "default"));
	                    
	        		}
	            }
	        }
		  if(bindingResult.hasErrors()){
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
