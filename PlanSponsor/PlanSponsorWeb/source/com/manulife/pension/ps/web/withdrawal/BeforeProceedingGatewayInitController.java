/**
 * 
 */
package com.manulife.pension.ps.web.withdrawal;

import static com.manulife.pension.ps.web.withdrawal.WebConstants.CONTRACT_ID_PARAMETER;
import static com.manulife.pension.ps.web.withdrawal.WebConstants.ORIGINATOR_PARAMETER;
import static com.manulife.pension.ps.web.withdrawal.WebConstants.PROFILE_ID_PARAMETER;
import static com.manulife.pension.ps.web.withdrawal.WebConstants.SUBMISSION_ID_PARAMETER;
import static com.manulife.pension.ps.web.withdrawal.WebConstants.WITHDRAWAL_INITIATED_BY_PARAMETER;
import static com.manulife.pension.ps.web.withdrawal.WebConstants.WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE;
import static com.manulife.pension.ps.web.withdrawal.WebConstants.WITHDRAWAL_STATUS_CODE_PARAMETER;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;

/**
 * 
 * This action class act as a gateway for pre-processing of information
 * for the before proceeding page when the withdrawal request is
 * initiated or reviewed.
 * 
 * @author kuthiha
 *
 */
@Controller
@RequestMapping( value = "/withdrawal")
@SessionAttributes({"withdrawalBeforeProceedingForm"})

public class BeforeProceedingGatewayInitController extends BaseWithdrawalController {
	@ModelAttribute("withdrawalBeforeProceedingForm") 
	public BeforeProceedingForm populateForm()
	{
		return new BeforeProceedingForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","redirect:/do/withdrawal/beforeProceedingInitiate/");
		forwards.put("defaultReview","redirect:/do/withdrawal/beforeProceedingReview/");
		}
	

    /* (non-Javadoc)
     * @see com.manulife.pension.ps.web.controller.PsAutoAction#doDefault(org.apache.struts.action.ActionMapping, com.manulife.pension.ps.web.controller.BaseAutoForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	@RequestMapping(value = {"/beforeProceedingGatewayReview/"},  method =  {RequestMethod.GET}) 
	public String doDefaultReview(@Valid @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
	 if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defaultReview");//if input forward not //available, provided default
	       }
		}


    if (logger.isDebugEnabled()) {
        logger.debug("doDefault> Entry.");
    }
     String forward=doDefault(form, request, response,true);
    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value = {"/beforeProceedingGatewayInit/"},  method =  {RequestMethod.GET}) 
	public String doDefaultInitiate(@Valid @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
	 if(bindingResult.hasErrors()){
		 
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}


    if (logger.isDebugEnabled()) {
        logger.debug("doDefault> Entry.");
    }
    String forward=doDefault(form, request, response,false);
    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
    

		public String doDefault(BeforeProceedingForm form,HttpServletRequest request,HttpServletResponse response,boolean review) 
		throws IOException,ServletException, SystemException {
		
        if (logger.isDebugEnabled()) {
            logger.debug("doDefault> Entry.");
        }
        
        // Grab request parameters (submission, profile id, contract id, request status)
        final String statusCode = request.getParameter(WITHDRAWAL_STATUS_CODE_PARAMETER);
        final String originator = request.getParameter(ORIGINATOR_PARAMETER);
        final String profileIdParameter = request.getParameter(PROFILE_ID_PARAMETER);
        final String contractIdParameter = request.getParameter(CONTRACT_ID_PARAMETER);
        final String submissionIdParameter = request.getParameter(SUBMISSION_ID_PARAMETER);
        final String initiatedBy = request.getParameter(WITHDRAWAL_INITIATED_BY_PARAMETER);
        
        final boolean isInitiate = StringUtils.isBlank(statusCode)
                || StringUtils.equals(statusCode, WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        final boolean isNew = StringUtils.isBlank(statusCode);
        
        Integer selectedContract = null;
        
        UserProfile userProfile = PsAutoController.getUserProfile(request);
        if (userProfile != null) {
        	if (userProfile.getCurrentContract() != null) {
        		selectedContract = userProfile.getCurrentContract().getContractNumber();
        	}
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doDefault> Loaded request parameters: statusCode[")
                    .append(statusCode).append("], profileId[").append(profileIdParameter).append(
                            "], contractId[").append(contractIdParameter)
                    .append("], submissionId[").append(submissionIdParameter).append(
                            "], originator[").append(originator).append("].").toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "doDefault> Determined that before proceeding request is [").append(
                    isNew ? "new" : isInitiate ? "initiate" : "review").append("].").toString());
        }

        final Integer contractId = NumberUtils.createInteger(contractIdParameter);
        final Integer profileId = NumberUtils.createInteger(profileIdParameter);
        final Integer submissionId = !isNew ? NumberUtils.createInteger(submissionIdParameter)
                : null;
        
        /*CL #132493 fix: The penetration issue where in wrong contract id and 
         * profile id is passed in the URL and the system did not allow to get through.
         * The fix is by comparing the contract id passed with the one in session,
         * and if not matching, redirecting to home page.*/
        if (contractId != null && selectedContract != null) {
        	if(!contractId.equals(selectedContract)) {
        		return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        	}
        }
 
        // Create appropriate withdrawal request meta data
        final WithdrawalRequestMetaData metaData;
        if (isNew) {
            metaData = new WithdrawalRequestMetaData(contractId, profileId, statusCode);
        } else {
            metaData = new WithdrawalRequestMetaData(submissionId, contractId, profileId,
                    statusCode, initiatedBy);
        }
       
        WithdrawalRequestMetaDataUi metaDataUi = new WithdrawalRequestMetaDataUi(metaData, originator);
        request.getSession(false).setAttribute(WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE, metaDataUi);
        if(review)
        	{
        	return forwards.get("defaultReview");
        	}
        else {
        	return forwards.get(ACTION_FORWARD_DEFAULT);
        	}
    }

    /**
     * Called when the page 'print' link is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward - The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */
	 @RequestMapping(value ={"/beforeProceedingGatewayInit/"}, params= {"action=print"}, method =  {RequestMethod.GET}) 
		public String doPrintInitiate(@Valid @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 
		 if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
		       }
			}


     if (logger.isDebugEnabled()) {
         logger.debug("doPrint> Entry.");
     }
     String forward=doPrint(form, bindingResult, request, response);
     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	 
	 @RequestMapping(value ={"/beforeProceedingGatewayReview/"}, params= {"action=print"}, method =  {RequestMethod.GET}) 
		public String doPrint(@Valid @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 
		 if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defaultReview");//if input forward not //available, provided default
		       }
			}
   

        if (logger.isDebugEnabled()) {
            logger.debug("doPrint> Entry.");
        }

        // Need to reset up the meta data to circumvent the bookmarking in before proceeding
       
        final WithdrawalRequestMetaData metaData = form.getWithdrawalRequestMetaDataForPrint();
        WithdrawalRequestMetaDataUi metaDataUi = new WithdrawalRequestMetaDataUi(metaData, "print");
        request.getSession(false)
                .setAttribute(WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE, metaDataUi);
        request.getSession(false).setAttribute(WebConstants.PRINTFRIENDLY_KEY, "true");

        String forward = ACTION_FORWARD_DEFAULT;
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doPrint> Exit with forward [").append(forward).append(
                    "].").toString());
        }
        return forwards.get(forward);
    }
	
	   @RequestMapping(value ={"/beforeProceedingGatewayReview/"}, params= {"task=printPDF"}, method =  {RequestMethod.GET})
	   public String doPrintPDFReview( @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm form,BindingResult bindingResult,
	             HttpServletRequest request,  HttpServletResponse response)
	            throws IOException, ServletException, SystemException {
		  
	   if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defaultReview");//if input forward not //available, provided default
	       }
		}
	   String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}
	   @RequestMapping(value ={"/beforeProceedingGatewayInit/"}, params= {"task=printPDF"}, method =  {RequestMethod.GET})
		   public String doPrintPDFInitiate( @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm form,BindingResult bindingResult,
		             HttpServletRequest request,  HttpServletResponse response)
		            throws IOException, ServletException, SystemException {
		  
		   if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
		       }
			}
		  String  forward=super.doExecute( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}
}
