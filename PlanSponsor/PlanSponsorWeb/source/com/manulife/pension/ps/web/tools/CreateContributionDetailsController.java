package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

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
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.submission.valueobject.CopiedSubmissionHistoryItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCancel;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.util.content.GenericException;

/**
 * CreateContributionDetailsAction Action class 
 * This class is used to create contribution submission
 * with skeleton data based on a contract id
 * 
 * @author Jim Adamthwaite
 */
@Controller
@RequestMapping(value ="/tools")


public class CreateContributionDetailsController extends PsController 
{
	
	@ModelAttribute("createContributionDetailsForm") 
	public CreateContributionDetailsForm populateForm()
	{
		return new CreateContributionDetailsForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("editContribution","redirect:/do/tools/editContribution/");
		forwards.put("copyContributionError","/tools/copyContributionError.jsp");
		forwards.put("cancel","redirect:/do/tools/submissionHistory/");
		}
   
	
	//	note: don't change this value, as it is what the edit action is looking for
	public static String CREATED_ITEM_KEY = "copiedItem"; 
	protected static final String TOOLS = "tools";
	protected static final String CANCEL = "cancel";
	public static final String COPY_SIZE_LIMIT_KEY = "copy.size.limit";
	protected static final String COPY_SIZE_LIMIT = System.getProperty(COPY_SIZE_LIMIT_KEY, "1500"); 

	
	public CreateContributionDetailsController()
	{
		super(CreateContributionDetailsController.class);
	} 
	
	@RequestMapping(value ="/createContribution/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("createContributionDetailsForm") CreateContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
	     	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	     	if(errDirect!=null){
	     		SubmissionHistoryReportData report = new SubmissionHistoryReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, report);
	          request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	          forwards.get("cancel");//if input forward not //available, provided default
	     	}
	     }  
		

		Date startDate = new Date();
		long startTime = 0; 
		if (logger.isDebugEnabled()) {
			startTime = new GregorianCalendar().getTime().getTime();
			logger.debug("entry -> doExecute");
		}
		
		//reset editContribution form
		request.getSession(false).removeAttribute("editContributionDetailsForm");
		request.getSession(false).removeAttribute(PsBaseUtil.ERROR_KEY);
		request.getSession(false).removeAttribute(Constants.COPY_WARNINGS);
		request.getSession(false).removeAttribute(Constants.NUM_COPY_WARNINGS);
		
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
			request.getSession().setAttribute(CREATED_ITEM_KEY, copiedItem);
			return forwards.get("editContribution");
		}

		int contractNumber = currentContract.getContractNumber();
		
		GFTUploadDetail gftUploadDetail = new GFTUploadDetail();
		gftUploadDetail.setContractNumber(String.valueOf(contractNumber));
		
		gftUploadDetail.setUserSSN(String.valueOf(userProfile.getPrincipal().getProfileId()));
		gftUploadDetail.setUserName(userProfile.getPrincipal().getFirstName() + " " + userProfile.getPrincipal().getLastName());

		gftUploadDetail.setUserType(isTPARole(userProfile)? GFTUploadDetail.USER_TYPE_TPA : GFTUploadDetail.USER_TYPE_CLIENT);
		if (userProfile.isInternalUser()) {
			gftUploadDetail.setUserType(EditContributionDetailsController.USER_TYPE_INTERNAL);
		}
		gftUploadDetail.setUserTypeID(userProfile.getClientId());

		//TODO: this should be client name
		gftUploadDetail.setUserTypeName("");
		//CL 128338 - Submitter Email missing in online contribution submissions
		gftUploadDetail.setNotificationEmailAddress(userProfile.getEmail());
		//End of CL 128338
		
		CopiedSubmissionHistoryItem newItem = 
			SubmissionServiceDelegate.getInstance().createContributionDetails(gftUploadDetail);

		if (newItem != null) {
			request.getSession().setAttribute(CREATED_ITEM_KEY, newItem);
		} else {
			Collection errors = new ArrayList();
			errors.add(new GenericException(
					ErrorCodes.CONTRIBUTION_ABOVE_SIZE_LIMIT,
					new String[] { COPY_SIZE_LIMIT }));
			/*
			 * Errors are stored in the session so that our REDIRECT can look up the
			 * errors.
			 */
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(CANCEL);
		}

		// this is for displaying the errors/warnings section
		generateCopyWarnings(newItem, request);

		//	log the event
		Date endDate = new Date();
		logEvent(newItem, userProfile, startDate, endDate, "doExecute");

		if (logger.isDebugEnabled()) {
			System.out.println("create contribution action total time: " + (new GregorianCalendar().getTime().getTime() - startTime));
			logger.debug("exit <- doExecute");
		}

		return forwards.get("editContribution");
	}						                
	
	
	/**
	 * @return boolean
	 */
	protected static boolean isTPARole(UserProfile userProfile) {
		 return userProfile.getPrincipal().getRole() instanceof ThirdPartyAdministrator;
	}

	private void generateCopyWarnings(CopiedSubmissionHistoryItem copiedItem, HttpServletRequest request) {
		StringBuffer html = new StringBuffer("");

		int numCopyWarnings = 0;
		if ( copiedItem != null ) {
			if (copiedItem.getParticipantsNotCopiedNonUniqueId() != null && !copiedItem.getParticipantsNotCopiedNonUniqueId().isEmpty()) {
				html.append("<table>");
				html.append("<tr><td width=\"10%\" height=\"10\">");
				html.append("&nbsp;");
				html.append("</td><td width=\"90%\">");
				html.append("&nbsp;");
				html.append("</td></tr>");

				html.append("<tr><td width=\"10%\">");
				html.append("&nbsp;");
				html.append("</td><td width=\"90%\">");
				html.append(Constants.WARNING_ICON);
				html.append("The following ");
				html.append(copiedItem.getParticipantsNotCopiedNonUniqueId().size());
				html.append(" participant(s) have been removed from the submission because they do not have a unique Employee number.");
				html.append("<ul>");
				Iterator iter = copiedItem.getParticipantsNotCopiedNonUniqueId().values().iterator();
				while (iter.hasNext()) {
					html.append("<li>");
					html.append((String)iter.next());
				}
				html.append("</ul>");
				html.append("</td></tr>");
				html.append("</table>");
				numCopyWarnings++;
			}
		}
		request.getSession().setAttribute(Constants.COPY_WARNINGS,html.toString());
		request.getSession().setAttribute(Constants.NUM_COPY_WARNINGS,new Integer(numCopyWarnings));
	}

	protected void logEvent(CopiedSubmissionHistoryItem copiedItem, UserProfile userProfile, Date startTime, 
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
		.append("Created tracking number: ")
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
	private PSValidatorFWCancel psValidatorFWCancel;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWCancel);
	}
}

