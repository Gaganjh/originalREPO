package com.manulife.pension.ps.web.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.common.util.log.OnlineSubmissionEventLog;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * @author parkand
 *
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"submissionDeleteForm"})

public class SubmissionDeleteController extends PsController {
	@ModelAttribute("submissionDeleteForm")
	public SubmissionDeleteForm populateForm() 
	{
		return new SubmissionDeleteForm();
		}

	public static Map<String,String> forwards = new HashMap<>();
	public static final String DEFAULT = "default";
	static{
		forwards.put("input","/tools/submissionHistory.jsp");
		forwards.put(DEFAULT,"redirect:/do/tools/submissionHistory/");
		forwards.put("tools","redirect:/do/tools/toolsMenu/");
		}
	
	public SubmissionDeleteController() {
		super(SubmissionDeleteController.class);
	}

		
	@RequestMapping(value ="/submissionDelete/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("submissionDeleteForm") SubmissionDeleteForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
			SubmissionHistoryReportData report = new SubmissionHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("input");
	       }
		}
	     

		Date startTime = new Date();
		UserProfile profile = (UserProfile)request.getSession(false).getAttribute(Constants.USERPROFILE_KEY);

		// ensure that the user has permissions to delete this submission case
		Collection errors = new ArrayList();
		if (!SubmissionHistoryItemActionHelper.getInstance().isDeleteAllowed(profile, actionForm.getType(), actionForm.getStatus())) {
			errors.add(new GenericException(ErrorCodes.SUBMISSION_INVALID_PERMISSION));
			setErrorsInSession(request, errors);
			return forwards.get(DEFAULT);
		}
		
		int subId = Integer.parseInt(actionForm.getSubNo());
		//CL89281 fix	
		int contractId = profile.getCurrentContract().getContractNumber();
		long profileId = profile.getPrincipal().getProfileId();

		if(!isSubmissionInDraft(subId, contractId)){
			logger.error("Submission record which is in non-draft status can not be deleted.\n"
					+ "profileId="+profileId+", contractId=" + contractId+", submissionId="+subId);
			errors.add(new GenericException(ErrorCodes.INVALID_PAGE));
			setErrorsInSession(request, errors);
			return forwards.get(DEFAULT);
		}
		
		// lock the submission prior to deletion (don't go through the LockManager because we don't need this added to the session)
		Lock lock = SubmissionServiceDelegate.getInstance().acquireLock(new Integer(subId),new Integer(contractId),actionForm.getType(),String.valueOf(profileId));
		if (lock == null) {
			// cannot obtain a lock, generate an error and return the user to the submission history page
			Collection lockError = new ArrayList(1);
			lockError.add(new ValidationError("LOCKED", ErrorCodes.SUBMISSION_CASE_LOCKED));
			setErrorsInSession(request,lockError);
			return forwards.get(DEFAULT);
		}

		try {
			SubmissionServiceDelegate.getInstance().deleteSubmission(subId, contractId, actionForm.getType());
		} catch (SystemException e) {
			// before re-throwing the exception, release the lock
			try {
				SubmissionServiceDelegate.getInstance().releaseLock(lock);
			} catch (Exception e2) {
				// don't worry if the lock cannot be released - do nothing
			}
			throw e;
		}
		//	log the event
		Date endTime = new Date();
		logEvent(subId, profile, startTime, endTime, "doExecute");

		return forwards.get(DEFAULT); 
	}

	protected void logEvent(int sjNum, UserProfile userProfile, Date startTime, Date endTime, 
			String methodName) {
			
		String logData = "";
		try {
			logData = prepareLogData(sjNum, userProfile, startTime, endTime);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}	
		AbstractSubmitController.logEvent(userProfile.getPrincipal(), this.getClass().getName(), methodName, logData,
				OnlineSubmissionEventLog.class);
	}
	
	protected String prepareLogData(int sjNum, UserProfile userProfile, Date startTime, 
			Date endTime) throws SystemException {
		
		StringBuffer logData = new StringBuffer()
		.append("Deleted tracking number: ")
		.append(sjNum)
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
	
    /**Method to delete submission in draft status records but not others
     * @param submissionId
     * @param contractId
     */
	private static boolean isSubmissionInDraft(int submissionId, int contractId) throws SystemException{
		return SubmissionServiceDelegate.getInstance().isSubmissionInDraft(submissionId,contractId);
	}
	
	/**
	 * This code has been changed and added to validate form and request against
	 * penetration attack, prior to other validations 
	 */
	
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}