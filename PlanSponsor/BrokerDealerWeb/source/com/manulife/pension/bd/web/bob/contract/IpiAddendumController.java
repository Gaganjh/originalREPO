package com.manulife.pension.bd.web.bob.contract;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails.UserIdType;
import com.manulife.pension.service.fund.valueobject.UserAccess;
import com.manulife.pension.util.content.GenericException;

/**
 * This is the action class for the Plan Information document Generation page. This contains the logic to
 * display the Plan Information document.
 * 
 * @author Rajesh Rajendran
 * 
 */
@Controller
@RequestMapping(value ="/bob")

public class IpiAddendumController extends BaseAutoController {
	@ModelAttribute("autoForm")
	public AutoForm populateForm() 
	{
		return new AutoForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/bob/blockOfBusinessActive.jsp");
		}

	/* Logger */
	private Logger logger = Logger.getLogger(IpiAddendumController.class);
	boolean isDebugEnabled = logger.isDebugEnabled();
	boolean isErrorEnabled = logger.isEnabledFor(Level.ERROR);
	
	/**
	 * Constructor
	 */
	public IpiAddendumController() {
		super(IpiAddendumController.class);
	}


	/**
	 * Method to generate the PIF form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException, ServletException, SystemException
	 */
	@RequestMapping(value ="/investment/ipiAddendum/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("autoForm") AutoForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,forwards.get(BDConstants.BOB_PAGE_FORWARD));
			return forwards.get(BDConstants.BOB_PAGE_FORWARD);
		}

		if (isDebugEnabled) {
			logger.debug("entry -> doExecute");
		}
		
		byte[] downloadData = null;	
		List<GenericException> errors = new ArrayList<GenericException>();
		
		BobContext bobContext = BDSessionHelper.getBobContext(request);
        
		int contractId = bobContext.getCurrentContract().getContractNumber();
		
		FeeDisclosureUserDetails userDetails = getUserDetails(bobContext);
		UserAccess user = UserAccess.FRW;
		
		Access404a5 contractAcc =
		        FundServiceDelegate
		        .getInstance()
		        .get404a5Permissions(
		                EnumSet.of(Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE),
		                contractId,
		                user);

		if (contractAcc.getAccess(Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE) == null) {
			return  forwards.get(BOB_PAGE_FORWARD);
		}
		
		try{
			downloadData =
		        FundServiceDelegate.getInstance().getPIAddendumBinary(
		                contractId,
		                userDetails, user);
		}catch (Exception e) {

				logger.error("Exception Occured while generating addendum " +
						"document:"+contractId+" with reason :: "+e.getMessage(), e);
				errors.add(new GenericException(CommonErrorCodes.REPORT_FILE_NOT_FOUND));
				setErrorsInRequest(request, errors);
				return forwards.get(CommonConstants.SECONDARY_WINDOW_ERROR_FORWARD);
		}
		
		if (downloadData != null && downloadData.length > 0) {
			BaseReportController.streamDownloadData(request, response,
					"application/msword", "ADDENDUM_" + contractId + ".doc", downloadData);
		}		

		if (isDebugEnabled) {
			logger.debug("exit -> doExecute");
		}
		return null;
	}
    
    private FeeDisclosureUserDetails getUserDetails(BobContext context) {
		
    	FeeDisclosureUserDetails userDetails = new FeeDisclosureUserDetails();
		
		BDUserProfile user = context.getUserProfile();
		
		userDetails.setUserRoleCode(user.getRole().getRoleType().getUserRoleCode());
		userDetails.setApplicationId("FRW");
		userDetails.setRequestedTime(new Date().getTime());
		if(context.getUserProfile().isInternalUser()){
			userDetails.setUserIdType(UserIdType.getIdType(UserIdType.INTERNALUSER));
			userDetails.setUserId(String.valueOf(user.getBDPrincipal().getUserName()));
			userDetails.setUserFirstName(user.getBDPrincipal().getFirstName());
            userDetails.setUserLastName(user.getBDPrincipal().getLastName());
		} else {
			userDetails.setUserIdType(UserIdType.getIdType(UserIdType.EXTERNALUSER));
			userDetails.setUserId(String.valueOf(user.getBDPrincipal().getProfileId()));
		}
		
		return userDetails;
	}
    
    /**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	/**
	 * This method calls doValidate for doing validation.
	 * 
	 * @param form
	 *            Form objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * 
	 * @return Collection of errors
	 */
	
    
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
}
