/*
 * Created on Nov 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.validation.rules;

import java.util.Collection;
import java.util.Iterator;

import com.manulife.pension.ps.web.profiles.AccessLevelHelper;
import com.manulife.pension.ps.web.profiles.TpaFirm;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TpaAcessLevelRule extends ValidationRule {

	public TpaAcessLevelRule(int errorCode) {
		super(errorCode);
	}

	/**
	 * @see MultiFieldValidateable#validate(int, String[], Collection, Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors,
			Object objectToValidate) {
		boolean isValid = false;

		if (objectToValidate != null) {
			Pair pair = (Pair) objectToValidate;

			Collection tpaFirms = (Collection) pair.getFirst();

			Boolean hiddenFirms = (Boolean) pair.getSecond();
			// if not new TPA user and there is a hidden firm
			if (hiddenFirms!=null && hiddenFirms.booleanValue())
				isValid = true;

			else{
			 for (Iterator it = tpaFirms.iterator(); it.hasNext();) {
			 		TpaFirm tpaFirm= (TpaFirm)it.next();
			 		String role =tpaFirm.getContractAccess(0).getPlanSponsorSiteRole();
					if(!AccessLevelHelper.NO_ACCESS.equalsIgnoreCase(role))
					{
						isValid = true;
						break;
					}

				}
			}
		}

		if (!isValid) {
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
		}
		return isValid;
	}
}