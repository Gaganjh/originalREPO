package com.manulife.pension.ps.web.tpabob;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.InitBinder;		
import org.springframework.web.bind.ServletRequestDataBinder;	
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.ps.web.Constants;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tpabob.util.IpiBlockOfBusinessUtility;
import com.manulife.pension.ps.web.tpabob.util.TPABlockOfBusinessUtility;
import com.manulife.pension.ps.web.tpabob.util.TPACrdQbadTransactionsUtility;
import com.manulife.pension.ps.web.tpabob.util.TPAFeeBoBUtility;
import com.manulife.pension.ps.web.tpabob.util.TPAMissedLoanRepaymentReportBlockOfBusinessUtility;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * 
 * @author Baburaj Ramasamy
 *
 */
@Controller
@RequestMapping(value ="/tpabob")
@SessionAttributes({"tpaBlockOfBusinessEntryForm","tpaBlockOfBusinessForm"})

public class TPABlockOfBusinessEntryController extends BaseAutoController{
	
	@ModelAttribute("tpaBlockOfBusinessEntryForm")
	public TPABlockOfBusinessEntryForm populateForm() {
		return new TPABlockOfBusinessEntryForm();
	}
	
	@ModelAttribute("tpaBlockOfBusinessForm")
	public TPABlockOfBusinessForm populateBusinessForm() {
		return new TPABlockOfBusinessForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tpabob/tpaBlockOfBusinessOverView.jsp"); 
		forwards.put("default","/tpabob/tpaBlockOfBusinessOverView.jsp");}

	/*Since it is getting a post call from SearchTPA Page post has been added to doDefault Method*/
	@RequestMapping(value ="/tpaBlockOfBusinessHome/", method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("tpaBlockOfBusinessEntryForm") TPABlockOfBusinessEntryForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException,SystemException {
		String forward = preExecute(actionForm, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    	}
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		TPABlockOfBusinessEntryForm form = (TPABlockOfBusinessEntryForm) actionForm; 
        UserProfile userProfile = SessionHelper.getUserProfile(request);
		String tpaUserIDSelected = form.getTpaUserIDSelected();

		if (userProfile.getRole().isInternalUser()) {

			if (!StringUtils.isBlank(tpaUserIDSelected)) {
				// Set the TPA UserInfo in the session.
				UserInfo tpaUserInfo = TPABlockOfBusinessUtility
						.getUserInfo(Long.parseLong(tpaUserIDSelected));
				request.getSession(false).setAttribute(Constants.TPA_USER_INFO,
						tpaUserInfo);
				userProfile.setSelectedTpaUserProfileId(tpaUserIDSelected);
			}
		}

		return forwards.get("default");
	}

	@RequestMapping(value ="/tpaBlockOfBusinessHome/" ,params={"action=generateIPIBoBCsv",}   , method =  {RequestMethod.GET}) 
	public String doGenerateIPIBoBCsv (@Valid @ModelAttribute("tpaBlockOfBusinessEntryForm") TPABlockOfBusinessEntryForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
	//TPABlockOfBusinessEntryForm form = (TPABlockOfBusinessEntryForm) actionForm;
		 UserProfile userProfile = SessionHelper.getUserProfile(request);
		 String userProfileId ;
		 // If Mimic
		 if (userProfile.getRole().isInternalUser()) {
			 userProfileId = actionform.getTpaUserIDSelected();
		 } else {
			 userProfileId = String.valueOf(userProfile.getPrincipal().getProfileId());
		 }
			 
		 Map<Integer,String> tpaFirms = ContractServiceDelegate.getInstance().getTpaFirmsByProfileId(userProfileId);
	
		BaseReportController.streamDownloadData(request, response,
				"text/csv", IpiBlockOfBusinessUtility.getCsvFileName(), 
								IpiBlockOfBusinessUtility.getIpiBoBCSVFileData(tpaFirms, userProfileId));

		return null;
	}
	@RequestMapping(value ="/tpaBlockOfBusinessHome/", params={"action=generateTPAFeesCsv"} , method =  {RequestMethod.GET}) 
	public String doGenerateTPAFeesCsv (@Valid @ModelAttribute("tpaBlockOfBusinessEntryForm") TPABlockOfBusinessEntryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		//TPABlockOfBusinessEntryForm form = (TPABlockOfBusinessEntryForm) actionForm;
		 UserProfile userProfile = SessionHelper.getUserProfile(request);
		 String userProfileId ;
		 // If Mimic
		 if (userProfile.getRole().isInternalUser()) {
			 userProfileId = form.getTpaUserIDSelected();
		 } else {
			 userProfileId = String.valueOf(userProfile.getPrincipal().getProfileId());
		 }
			 
		 Map<Integer,String> tpaFirms = ContractServiceDelegate.getInstance().getTpaFirmsByProfileId(userProfileId);
		 List<Integer> tpaIds = new ArrayList<Integer>();
		 for (Map.Entry<Integer, String> tpaFirm : tpaFirms.entrySet()){
			 tpaIds.add(tpaFirm.getKey());
		 }
		 
		 BaseReportController.streamDownloadData(request, response,
					"text/csv", TPAFeeBoBUtility.getCsvFileName(), 
					TPAFeeBoBUtility.getTPAFeeBoBCSVFileData(tpaIds));


		return null ;
	}

	/**
	 * DM 335567 - TPA Missing Loan Payments report
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/tpaBlockOfBusinessHome/", params={"action=generateTPAMissedLoanRepaymentReportCsv"}  , method =  {RequestMethod.GET}) 
	public String doGenerateTPAMissedLoanRepaymentReportCsv (@Valid @ModelAttribute("tpaBlockOfBusinessEntryForm") TPABlockOfBusinessEntryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
	
		//TPABlockOfBusinessEntryForm form = (TPABlockOfBusinessEntryForm) actionForm;
		 UserProfile userProfile = SessionHelper.getUserProfile(request);
		 String userProfileId = null;
		 // If Mimic
		 if (userProfile.getRole().isInternalUser()) {
			 userProfileId = form.getTpaUserIDSelected();
		 } else {
			 userProfileId = String.valueOf(userProfile.getPrincipal().getProfileId());
		 }
		
		 if(StringUtils.isNotBlank(userProfileId)) {
			 
			 Date asOfDate = ContractServiceDelegate.getInstance().getRunDate("PS", "RUNDATE");
			 
			 BaseReportController.streamDownloadData(request, response,
						"text/csv", TPAMissedLoanRepaymentReportBlockOfBusinessUtility.getCsvFileName(asOfDate), 
						TPAMissedLoanRepaymentReportBlockOfBusinessUtility.getTPAMissedLoanRepaymentCSVFileData(Long.parseLong(userProfileId), asOfDate));
		 }

		return null ;
	}
	
	/**
	 * HAT 164 - TPA CRD QBA Transactions Report
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value = "/tpaBlockOfBusinessHome/", params = {"action=generateTPACrdQbaTransactionReportCsv" }, method = { RequestMethod.GET })
	public String doGenerateTPACrdQbaTransactionReportCsv(@Valid @ModelAttribute("tpaBlockOfBusinessEntryForm") TPABlockOfBusinessEntryForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		UserProfile userProfile = SessionHelper.getUserProfile(request);
		String userProfileId = null;
		// If Mimic
		if (userProfile.getRole().isInternalUser()) {
			userProfileId = form.getTpaUserIDSelected();
		} else {
			userProfileId = String.valueOf(userProfile.getPrincipal().getProfileId());
		}

		String companyId = CommonEnvironment.getInstance().getSiteLocation();
		String locationId = StringUtils.equals(companyId, Constants.SITEMODE_USA) ? Constants.COUNTRY_USA_CODE : Constants.COUNTRY_NY_CODE;

		if (StringUtils.isNotBlank(userProfileId)) {

			Date asOfDate = new Date();
			List<Integer> contractNumbers = ContractServiceDelegate.getInstance().getContractsByTpaUser(Long.parseLong(userProfileId), locationId);
			BaseReportController.streamDownloadData(request, response, "text/csv",
					TPACrdQbadTransactionsUtility.getCsvFileName(asOfDate,null),
					TPACrdQbadTransactionsUtility.getCrdQbdTransactionReportCSVFileData(asOfDate, contractNumbers));
		}

		return null;
	}
	
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
