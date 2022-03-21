package com.manulife.pension.bd.web.bob.blockOfBusiness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.exception.SystemException;

@Component
public class BlockOfBusinessValidator implements Validator {
	private static Logger logger = Logger.getLogger(BlockOfBusinessValidator.class);
	@Override
	public boolean supports(Class arg0) {
		return BlockOfBusinessForm.class.equals(arg0);
	}

	@Override
	public void validate(Object target, Errors errors) {
				
		BlockOfBusinessForm form = (BlockOfBusinessForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		 if (logger.isDebugEnabled()) {
	            logger.debug("entry -> doValidate()");
	        }
	       
	        
	        // All the validations to check Error conditions are being done in
	        // checkForInfoMessageConditions() method instead of here. This is because, if the
	        // error conditions are being tested here, all the things that need to be done to load the
	        // page properly are not being carried out.
	        
	        
	        // This is just a check to make sure the user can view the "Outstanding Proposals Tab",
	        // "Pending Tab" only for latest asOfDate.
	        if (new UrlPathHelper().getPathWithinApplication(request).contains(BDConstants.OUTSTANDING_PROPOSALS_TAB_URL)
	                || new UrlPathHelper().getPathWithinApplication(request).contains(BDConstants.PENDING_TAB_URL)) {
	            String asofDateSelected = ((BlockOfBusinessForm) form).getAsOfDateSelected();
	            List<Date> asOfDatesList = null;
	            try {
	                asOfDatesList = BlockOfBusinessUtility.getMonthEndDates();
	            } catch (SystemException se) {
	                // Do nothing.
	            }
	            
	            if (asOfDatesList != null && !asOfDatesList.isEmpty()) {
	                String asOfDate = String.valueOf(asOfDatesList.get(0).getTime());

	                if (!asOfDate.equals(asofDateSelected)) {
	                    // Ideally, we should never come inside this loop.
	                    ((BlockOfBusinessForm) form).setAsOfDateSelected(asOfDate);
	                }
	            }
	        }
	        if (logger.isDebugEnabled()) {
	            logger.debug("exit -> doValidate()");
	        }
		
		
	}


}
