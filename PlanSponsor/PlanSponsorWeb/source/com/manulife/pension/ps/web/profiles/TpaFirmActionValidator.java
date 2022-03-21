package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.util.content.GenericException;
@Component
public class TpaFirmActionValidator implements Validator {
	
private static Logger logger = Logger.getLogger(TpaFirmActionValidator.class);
	
	@Override
	public boolean supports(Class<?> arg0) {
		return TpaFirm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors error) {
	
		Collection errors = new ArrayList();
		TpaFirm form = (TpaFirm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		/*
		 * Validate the action form only when we save.
		 */
		if (form.isSaveAction()) {
			if (!form.getChanges().isChanged()) {
				GenericException ex = new GenericException(
						ErrorCodes.SAVING_WITH_NO_CHANGES);
				errors.add(ex);
			}

			if(!form.isBundledGaIndicator()){
				if (form.getContractAccess(0).isShowSigningAuthority() && form.getContractAccess(0).getSigningAuthority() && form.getEnableSigningAuthority()
	                    && form.getSelectedTPAUsersAsList().size() == 0) {
	                GenericException ex = new GenericException(ErrorCodes.NO_TPA_USERS_SELECTED);
	                errors.add(ex);
	            }
			}

			/*
	         * Disabled selections are not sent back from the browser. This is a
	         * workaround to clear those flags.
	         */
	        for (Iterator it = form.getContractAccesses().iterator(); it.hasNext();) {
	        	TPAUserContractAccess contractAccess = (TPAUserContractAccess) it.next();

	            if (contractAccess.getDirectDebit() != null
	                    && !contractAccess.getDirectDebit().booleanValue()) {
	                contractAccess.setSelectedDirectDebitAccounts(new String[0]);
	            }
	        }
		}
		
		
		
	}
}
