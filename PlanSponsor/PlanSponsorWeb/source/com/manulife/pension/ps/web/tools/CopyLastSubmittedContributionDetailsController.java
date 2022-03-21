package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.common.util.log.OnlineSubmissionEventLog;
import com.manulife.pension.ps.service.submission.valueobject.CopiedSubmissionHistoryItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCopyLastSubmit;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * CopyContributionDetailsAction Action class 
 * This class is used to copy a contribution submission
 */
@Controller
@RequestMapping(value ="/tools")

public class CopyLastSubmittedContributionDetailsController extends CopyContributionDetailsController 
{
	@ModelAttribute("copyContributionDetailsForm") 
	public CopyContributionDetailsForm populateForm()
	{
		return new CopyContributionDetailsForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put( "editContribution","redirect:/do/tools/editContribution/");
		forwards.put( "copyContributionError","/tools/copyContributionError.jsp");
		forwards.put( "tools","redirect:/do/tools/toolsMenu/");
	
	}

	 
	public static String COPIED_ITEM_KEY = "copiedItem";
	
	@RequestMapping(value ="/copyLastContribution/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("copyContributionDetailsForm") CopyContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
          return   forwards.get("copyContributionError");//if input forward not //available, provided default
        	}
        }
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doExecute");
		}

		Date startTime = new Date();

		UserProfile userProfile = getUserProfile(request);
		// lets check the permissions
		if(
				!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get(TOOLS);
		}

		Contract currentContract = userProfile.getCurrentContract();
		
		if(
				!userProfile.isSubmissionAccess() || 
				!userProfile.isAllowedUploadSubmissions() ||
				Contract.STATUS_CONTRACT_DISCONTINUED.equals(currentContract.getStatus()) ||
                userProfile.isBeforeCAStatusAccessOnly()
		) {
			CopiedSubmissionHistoryItem copiedItem 
				= new CopiedSubmissionHistoryItem();
			request.getSession().setAttribute(COPIED_ITEM_KEY, copiedItem);
			return forwards.get("editContribution");
		}

		// tracking number 
		int contractNumber = currentContract.getContractNumber();
		String userId = Long.toString(userProfile.getPrincipal().getProfileId());
		String userName = userProfile.getPrincipal().getFirstName() + " " + userProfile.getPrincipal().getLastName();
		String userTypeCode = isTPARole(userProfile)? GFTUploadDetail.USER_TYPE_TPA : GFTUploadDetail.USER_TYPE_CLIENT;
		if (userProfile.isInternalUser()) {
			userTypeCode = EditContributionDetailsController.USER_TYPE_INTERNAL;
		}
		BigDecimal userTypeId = new BigDecimal(userProfile.getClientId());
		String userTypeName = "";  // should be client name

		//CL 128338 - Submitter Email missing in online contribution submissions
		String notificationEmailAddress = userProfile.getEmail();
		CopiedSubmissionHistoryItem copiedItem = 
			SubmissionServiceDelegate.getInstance().copyLastSubmittedContributionDetails(contractNumber,
					userId, userName, userTypeCode, userTypeId, userTypeName, notificationEmailAddress);
		//End of CL 128338
		if (copiedItem != null && copiedItem.getErrorCode() == CopiedSubmissionHistoryItem.NO_ERROR) {
			// this is for the edit page to get the new tracking number
			request.getSession(false).setAttribute(Constants.COPIED_ITEM_KEY, copiedItem);
		} else {
			Collection errors = new ArrayList();

            if (copiedItem == null) {
    			errors.add(new ValidationError("NO_CONTRIBUTION_TO_COPY", ErrorCodes.NO_CONTRIBUTION_TO_COPY));
            } else if (copiedItem.getErrorCode() == CopiedSubmissionHistoryItem.ERROR_COPY_SIZE_LIMIT_REACHED) {
                errors.add(new GenericException(
                		ErrorCodes.CONTRIBUTION_ABOVE_SIZE_LIMIT,
    					new String[] { COPY_SIZE_LIMIT }));
            } else {
                errors.add(new GenericException(
                        ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
            }

			setErrorsInRequest(request, errors);

			return forwards.get("copyContributionError");
		}

		// this is for displaying the errors/warnings section
		generateCopyWarnings(copiedItem, request);

		//	log the event
		Date endTime = new Date();
		logUploadEvent(copiedItem, userProfile, startTime, endTime, "doExecute");

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doExecute");
		}

		return forwards.get("editContribution");
	}						                

	protected void logUploadEvent(CopiedSubmissionHistoryItem copiedItem, UserProfile userProfile, Date startTime, 
			Date endTime, String methodName) {
			
		String logData = "";
		try {
			logData = prepareLogData(copiedItem, userProfile, startTime, endTime);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		AbstractSubmitController.logEvent(userProfile.getPrincipal(), this.getClass().getName(), methodName, logData,
				OnlineSubmissionEventLog.class);	
	}	
	
	protected String prepareLogData(CopiedSubmissionHistoryItem copiedItem, UserProfile userProfile, Date startTime, 
			Date endTime) throws SystemException {
		
		StringBuffer logData = new StringBuffer()
		.append("Copied from tracking number: ")
		.append(copiedItem.getOldSubmissionId())
		.append(" Copied to tracking number: ")
		.append(copiedItem.getNewSubmissionId())
		.append(" Contract number: ")
		.append(userProfile.getCurrentContract().getContractNumber())
		.append(" Username : ")
		.append(userProfile.getPrincipal().getLastName())
		.append(", ")
		.append(userProfile.getPrincipal().getFirstName())
		.append(" Action start time : ")
		.append(startTime.toString())
		.append(" Action end time: ")
		.append(endTime.toString());
		 
		return logData.toString();
	}

	/**
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations .
	 * 
	 */
	@Autowired
	private PSValidatorFWCopyLastSubmit psValidatorFWCopyLastSubmit;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWCopyLastSubmit);
	}
}
