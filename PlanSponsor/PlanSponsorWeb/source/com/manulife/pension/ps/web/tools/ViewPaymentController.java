package com.manulife.pension.ps.web.tools;

import java.util.ArrayList;
import java.util.Collection;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionPaymentItem;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.util.content.GenericException;

/**
 * 
 * Handles the submission upload process. This is a modified version of the 
 * FileUploadAction.
 * 
 * @author Tony Tomasone
 * 
 */
@Controller
@RequestMapping( value ="/tools")
@SessionAttributes({"viewPaymentForm"})

public class ViewPaymentController extends PsAutoController {

	@ModelAttribute("viewPaymentForm") 
	public ViewPaymentForm populateForm() 
	{
		return new ViewPaymentForm();
		}

	public static final String INPUT="input";
	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put(INPUT,"/tools/viewPayment.jsp");
		forwards.put("tools","redirect:/do/tools/toolsMenu/");
		forwards.put("history","redirect:/do/tools/submissionHistory/");
	}

	
	private static char SUB_CHAR = 'x';
	protected static final String TOOLS = "tools";
	protected static final String HISTORY = "history";
	
	/**
	 * Constructor.
	 */
	public ViewPaymentController() {
		super(ViewPaymentController.class);
	}

	
	@RequestMapping(value ="/viewPayment/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("viewPaymentForm") ViewPaymentForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(INPUT);
        	}
        }  
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}

		UserProfile userProfile = getUserProfile(request);

		// lets check the permissions
		if(
				!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get(TOOLS);
		}
		
		int contractNumber = userProfile.getCurrentContract().getContractNumber();
		
		SubmissionPaymentItem paymentItem = 
			SubmissionServiceDelegate.getInstance().getPaymentOnlySubmission(Integer.valueOf(actionForm.getSubNo()).intValue(), contractNumber);

		// check detailed permissions
		if (paymentItem == null) {
			actionForm.setAllowedView(false);
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
            setErrorsInSession(request, errors);
            return forwards.get(HISTORY);
 		}
		if (!SubmissionHistoryItemActionHelper.getInstance().isViewAllowed(paymentItem,userProfile)) {
			actionForm.setAllowedView(false);
			return forwards.get(INPUT);
		} else {
			actionForm.setAllowedView(true);
		}
        
		if (!userProfile.isAllowedToViewAllSubmissions()) {
			long submitterId = 0;
		    try {
		    	submitterId = Long.parseLong(paymentItem.getSubmitterID());
		    } catch (NumberFormatException e) {
				return  forwards.get(HISTORY);
		    }
		    if (userProfile.getPrincipal().getProfileId() != submitterId) {
		    	return forwards.get( HISTORY);
		    }	
		}
		
		GFTUploadDetail result = new GFTUploadDetail();
		result.setContractName(paymentItem.getContractName());
		result.setContractNumber(paymentItem.getContractId().toString());
		result.setReceivedDate(paymentItem.getSubmissionDate());
		result.setRequestedPaymentEffectiveDate(paymentItem.getRequestedPaymentEffectiveDate());
		result.setSubmissionId(paymentItem.getSubmissionId().toString());
		result.setUserName(paymentItem.getSubmitterName());
		result.setUserSSN(paymentItem.getSubmitterID());

		result.setPaymentInstructions(paymentItem.getPaymentInstructions());
		
		SubmissionUploadDetailBean det = new SubmissionUploadDetailBean(result);
		det.setStatus(SubmissionHistoryItemActionHelper.getInstance().getDisplayStatus(paymentItem));
		det.setSystemStatus(paymentItem.getSystemStatus());
		
		SessionHelper.setSubmissionUploadDetails(request, det);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}

		return forwards.get(INPUT);
	}
		
	/**
	 * This code has been changed and added to validate form and request against
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