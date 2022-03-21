package com.manulife.pension.ps.web.validation.rules;

import java.util.Collection;

import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRule;

/**
 * Validate SEM.45
 * Email address cannot be "@jhancock.com" or "@manulife" if not demo contract
 * 
 * @author marcest
 * Created Dec 21, 2006
 */
public class EmailNotLocalRule extends EmailLocalRule {

	private int contractId ;
	
	/**
	 * @param aContractId
	 */
	public EmailNotLocalRule ( int aContractId ) {
		super( ErrorCodes.EMAIL_MUST_BE_EXTERNAL );
		setContractId( aContractId );
	}
	
	/** 
	 * 
	 * @see com.manulife.pension.validation.Validateable#validate(java.lang.String[], java.util.Collection, java.lang.Object)
	 */
	public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {
		String email = objectToValidate.toString();
		boolean isLocal = false;
		if ("usa".equalsIgnoreCase(Environment.getInstance().getSiteLocation())) {
			// If not demo contract for USA
			if (getContractId() != 70300) {
				isLocal = isEmailLocal(email); 
			}
		} else if ("ny".equalsIgnoreCase(Environment.getInstance().getSiteLocation())) {
			// If not demo contract for NY
			if (getContractId() != 80089) {
				isLocal = isEmailLocal(email); 
			}			
		}
		if (isLocal) {
			validationErrors.add(new ValidationError(fieldIds, getErrorCode()));
            return false;
		}
		return true;
	}

	/**
	 * @return the contractId
	 */
	public int getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

}
