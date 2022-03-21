package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.AbstractIPSAndReviewDetailsController;
import com.manulife.pension.platform.web.investment.IPSViewParticiapantNotificationForm;
import com.manulife.pension.platform.web.util.PhoneRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;
/**
 * This Action class handles the requests from ParticipantNotification link in 
 * FRW IPS Landing & Results page.
 *   
 * @author Vellaisamy S
 *
 */
@Controller
@RequestMapping( value = "/investment")

public class IPSViewParticipantNotificationController extends AbstractIPSAndReviewDetailsController {
	
	@ModelAttribute("ipsViewParticiapantNotificationForm")
	public  IPSViewParticiapantNotificationForm  populateForm()
	{
		return new  IPSViewParticiapantNotificationForm ();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("input","redirect:/do/investment/ipsManager/"); 
		forwards.put("default","redirect:/do/investment/ipsManager/"); 
		forwards.put("viewIPSReviewResults","redirect:/investment/viewIPSReviewResults.jsp");
		forwards.put("additionalParameterOverlay","/WEB-INF/investment/ipsParticipantOverlay.jsp");
	}
   
	/**
	 * This method is used to get the Participant Notification PDF
	 * 
	 * @param mapping        /viewParticipantNotification/

	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value= "/viewParticipantNotification/", params= {"action=viewParticipantNotificationPDF"}, method =  {RequestMethod.POST}) 
	public String doViewParticipantNotificationPDF(@Valid @ModelAttribute("ipsViewParticiapantNotificationForm") IPSViewParticiapantNotificationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
        	}
        }
	
	

		UserProfile userProfile = SessionHelper.getUserProfile(request);

		Contract contract = userProfile.getCurrentContract();

		String reviewRequestId = request.getParameter("reviewRequestId");
		String isFromLandingPage = request.getParameter("isFromLandingPage");
		
		IPSViewParticiapantNotificationForm ipsParticipantNotificationForm = (IPSViewParticiapantNotificationForm) actionForm;
		
		Collection<GenericException> errorMessages = new ArrayList<GenericException>();
		
		// To generate Participant Notification PDF
		populateParticipantNotificationDetails(request, response, contract,
				ipsParticipantNotificationForm, Integer.parseInt(reviewRequestId),
				errorMessages,true);
		
		if (!errorMessages.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errorMessages);
			if (StringUtils.isNotBlank(isFromLandingPage)
					&& StringUtils.equals(TRUE, isFromLandingPage)) {
				return forwards.get(DEFAULT_ACTION);
			} else {
				return forwards.get(VIEW_REVIEW_ACTION);
			}
		}

		return null;
	}

	@RequestMapping(value= "/viewParticipantNotification/",  method =  {RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("ipsViewParticiapantNotificationForm") IPSViewParticiapantNotificationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
        	}
        }
		return null;
	}
	

	@RequestMapping(value= "/viewParticipantNotification/", params = {"action=validateOverlayFields"} ,method =  {RequestMethod.POST}) 
	public String doValidateOverlayFields(@Valid @ModelAttribute("ipsViewParticiapantNotificationForm") IPSViewParticiapantNotificationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
        	}
        }
    	
       
        String contactName = actionForm.getContactName();
        String zip = actionForm.getZipCode();
        Collection existingErrors = (Collection) request.getAttribute(PsBaseUtil.ERROR_KEY);
        if (existingErrors != null) {
            request.removeAttribute(PsBaseUtil.ERROR_KEY);
        }

        Collection errors = new ArrayList();
        StringBuffer errorString = new StringBuffer();
        try {
            if (StringUtils.isBlank(contactName) || StringUtils.isEmpty(actionForm.getTelephoneNumber().getValue())){
                
                Content message = ContentCacheManager.getInstance().getContentByName(
                        String.valueOf(ErrorCodes.ERROR_MANDATORY), ContentTypeManager.instance().MESSAGE);
                errorString.append((new StringBuilder()).append("<li>")
                        .append(ContentUtility.getContentAttribute(message, "text"))
                        .append(" </li>").toString());
            }
            
            if (StringUtils.isNotBlank(zip) && !StringUtils.isNumeric(zip)){
                
                Content message = ContentCacheManager.getInstance().getContentByName(
                        String.valueOf(ErrorCodes.ERROR_ZIP_CODE), ContentTypeManager.instance().MESSAGE);
                errorString.append((new StringBuilder()).append("<li>")
                        .append(ContentUtility.getContentAttribute(message, "text"))
                        .append(" </li>").toString());
            }
               
            PhoneRule.getInstance().validate("telephoneNumber", errors,
            		actionForm.getTelephoneNumber().getValue());
            if (errors.size() > 0) {
                for (Iterator i = errors.iterator(); i.hasNext();) {
                    Object errorstring = (GenericException) i.next();
                    Content message = ContentCacheManager.getInstance().getContentByName(
                            String.valueOf(((ValidationError) errorstring).getErrorCodeString()),
                            ContentTypeManager.instance().MESSAGE);
                    errorString.append((new StringBuilder()).append("<li>")
                            .append(ContentUtility.getContentAttribute(message, "text"))
                            .append(" </li>").toString());
                }

            }
            
            if(StringUtils.isNotEmpty(actionForm.getTelephoneNumber().getValue())){
                
                if (StringUtils.isEmpty(actionForm.getTelephoneNumber()
                        .getAreaCode())
                        || StringUtils.isEmpty(actionForm.getTelephoneNumber()
                                .getPhonePrefix())
                        || StringUtils.isEmpty(actionForm.getTelephoneNumber()
                                .getPhoneSuffix())
                        || actionForm.getTelephoneNumber().getValue().length() < 10) {
                    Content message = ContentCacheManager.getInstance().getContentByName(
                            String.valueOf(ErrorCodes.PHONE_NOT_COMPLETE), ContentTypeManager.instance().MESSAGE);
                    errorString.append((new StringBuilder()).append("<li>")
                            .append(ContentUtility.getContentAttribute(message, "text"))
                            .append(" </li>").toString());
                }
                
                if (StringUtils.isNotEmpty(actionForm.getTelephoneNumber()
                        .getAreaCode())
                        && StringUtils.isNotEmpty(actionForm
                                .getTelephoneNumber().getPhonePrefix())) {
                    String areaCode = null;
                    String phonePrefix = null;
                    areaCode = actionForm.getTelephoneNumber().getAreaCode();
                    phonePrefix = actionForm.getTelephoneNumber()
                            .getPhonePrefix();
                    if (areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1'
                            || phonePrefix.charAt(0) == '0' || phonePrefix.charAt(0) == '1') {
                        Content message = ContentCacheManager.getInstance().getContentByName(
                                String.valueOf(ErrorCodes.PHONE_NOT_NUMERIC), ContentTypeManager.instance().MESSAGE);
                        errorString.append((new StringBuilder()).append("<li>")
                                .append(ContentUtility.getContentAttribute(message, "text"))
                                .append(" </li>").toString());
                    }
                }
                
            }

            if (errorString.length() > 0) {
                response.setContentType("text");
                PrintWriter out = response.getWriter();
                errorString.append("</ul>");
                out.print((new StringBuilder()).append("{ \"error\" : \"")
                        .append(errorString.toString().trim()).append("\"}").toString());
                out.flush();
            }
            
        } catch (IOException exception) {
            try {
                throw new SystemException(exception,
                        "doValidateOverlayFields() - Exception while writting the output to Output Stream.");
            } catch (SystemException e) {
                e.printStackTrace();
            }
        } catch (ContentException e) {
            throw new SystemException(e, "Something wrong with CMA");
        }
        return null;
    }
	
	@RequestMapping(value= "/viewParticipantNotification/", params = {"action=change"}, method =  {RequestMethod.POST}) 
	public String doOverlay(@Valid @ModelAttribute("ipsViewParticiapantNotificationForm") IPSViewParticiapantNotificationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
        	}
        }
		String forward=super.doChange(actionForm, request, response);
		return  forwards.get(forward)!=null?forwards.get(forward):forwards.get("default");
	}
    
    /**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	  @Autowired
	   private PSValidatorFWDefault  psValidatorFWDefault;
	  @InitBinder
	    public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWDefault);
	}
    
}
