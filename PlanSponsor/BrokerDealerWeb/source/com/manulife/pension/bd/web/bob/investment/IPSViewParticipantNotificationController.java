package com.manulife.pension.bd.web.bob.investment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.AbstractIPSAndReviewDetailsController;
import com.manulife.pension.platform.web.investment.IPSViewParticiapantNotificationForm;
import com.manulife.pension.platform.web.util.PhoneRule;
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
@RequestMapping(value ="/bob")
@SessionAttributes({"ipsViewParticiapantNotificationForm"})

public class IPSViewParticipantNotificationController extends
						AbstractIPSAndReviewDetailsController {

	@ModelAttribute("ipsViewParticiapantNotificationForm") 
	public IPSViewParticiapantNotificationForm populateForm() 
	{
		return new IPSViewParticiapantNotificationForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
	forwards.put("input","/investment/ipsAndReviewDetails.jsp"); 
	forwards.put("default","/investment/ipsAndReviewDetails.jsp");
	forwards.put("viewIPSReviewResults","/investment/viewIPSReviewResults.jsp");
	forwards.put("additionalParameterOverlay","/WEB-INF/investment/ipsParticipantOverlay.jsp");
	}

	/**
	 * This method is used to get the Participant Notification PDF
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/investment/viewParticipantNotification/",params= {"action=viewParticipantNotificationPDF"},method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doViewParticipantNotificationPDF(@Valid @ModelAttribute("ipsViewParticiapantNotificationForm") IPSViewParticiapantNotificationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
		Contract contract = getBobContext(request).getCurrentContract();

		String reviewRequestId = request.getParameter("reviewRequestId");
		String isFromLandingPage = request.getParameter("isFromLandingPage");
		
		
		
		Collection<GenericException> errorMessages = new ArrayList<GenericException>();
		
		// To generate Participant Notification PDF
		populateParticipantNotificationDetails(request, response, contract,
				actionForm, Integer.parseInt(reviewRequestId),
				errorMessages,true);
		
		if (!errorMessages.isEmpty()) {
			setErrorsInRequest(request, errorMessages);
			if (StringUtils.isNotBlank(isFromLandingPage)
					&& StringUtils.equals(TRUE, isFromLandingPage)) {
				return forwards.get(DEFAULT_ACTION);
			} else {
				return forwards.get(VIEW_REVIEW_ACTION);
			}
		}

		return null;
	}
	@RequestMapping(value ="/investment/viewParticipantNotification/",method =  {RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("ipsViewParticiapantNotificationForm") IPSViewParticiapantNotificationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");
        	}
        }
		return null;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/investment/viewParticipantNotification/",params= {"action=validateOverlayFields"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doValidateOverlayFields(@Valid @ModelAttribute("ipsViewParticiapantNotificationForm") IPSViewParticiapantNotificationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
	
	
        IPSViewParticiapantNotificationForm ipsParticipantNotificationForm = (IPSViewParticiapantNotificationForm) actionForm;
        String contactName = ipsParticipantNotificationForm.getContactName();
        String zip = ipsParticipantNotificationForm.getZipCode();
        Collection existingErrors = (Collection) request.getAttribute(BdBaseController.ERROR_KEY);
        if (existingErrors != null) {
            request.removeAttribute(BdBaseController.ERROR_KEY);
        }

        Collection errors = new ArrayList();
        StringBuffer errorString = new StringBuffer();
        try {
            if (StringUtils.isBlank(contactName) || StringUtils.isEmpty(ipsParticipantNotificationForm.getTelephoneNumber().getValue())){
                
                Content message = ContentCacheManager.getInstance().getContentByName(
                        String.valueOf(BDErrorCodes.ERROR_MANDATORY), ContentTypeManager.instance().MESSAGE);
                errorString.append((new StringBuilder()).append("<li>")
                        .append(ContentUtility.getContentAttribute(message, "text"))
                        .append(" </li>").toString());
            }
            
            if (StringUtils.isNotBlank(zip) && !StringUtils.isNumeric(zip)){
                
                Content message = ContentCacheManager.getInstance().getContentByName(
                        String.valueOf(BDErrorCodes.ERROR_ZIP_CODE), ContentTypeManager.instance().MESSAGE);
                errorString.append((new StringBuilder()).append("<li>")
                        .append(ContentUtility.getContentAttribute(message, "text"))
                        .append(" </li>").toString());
            }
               
            PhoneRule.getInstance().validate("telephoneNumber", errors,
                    ipsParticipantNotificationForm.getTelephoneNumber().getValue());
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
            
            if(StringUtils.isNotEmpty(ipsParticipantNotificationForm.getTelephoneNumber().getValue())){
                
                if (StringUtils.isEmpty(ipsParticipantNotificationForm.getTelephoneNumber()
                        .getAreaCode())
                        || StringUtils.isEmpty(ipsParticipantNotificationForm.getTelephoneNumber()
                                .getPhonePrefix())
                        || StringUtils.isEmpty(ipsParticipantNotificationForm.getTelephoneNumber()
                                .getPhoneSuffix())
                        || ipsParticipantNotificationForm.getTelephoneNumber().getValue().length() < 10) {
                    Content message = ContentCacheManager.getInstance().getContentByName(
                            String.valueOf(BDErrorCodes.PHONE_NOT_COMPLETE), ContentTypeManager.instance().MESSAGE);
                    errorString.append((new StringBuilder()).append("<li>")
                            .append(ContentUtility.getContentAttribute(message, "text"))
                            .append(" </li>").toString());
                }
                
                if (StringUtils.isNotEmpty(ipsParticipantNotificationForm.getTelephoneNumber()
                        .getAreaCode())
                        && StringUtils.isNotEmpty(ipsParticipantNotificationForm
                                .getTelephoneNumber().getPhonePrefix())) {
                    String areaCode = null;
                    String phonePrefix = null;
                    areaCode = ipsParticipantNotificationForm.getTelephoneNumber().getAreaCode();
                    phonePrefix = ipsParticipantNotificationForm.getTelephoneNumber()
                            .getPhonePrefix();
                    if (areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1'
                            || phonePrefix.charAt(0) == '0' || phonePrefix.charAt(0) == '1') {
                        Content message = ContentCacheManager.getInstance().getContentByName(
                                String.valueOf(BDErrorCodes.PHONE_NOT_NUMERIC), ContentTypeManager.instance().MESSAGE);
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
	
	@RequestMapping(value ="/investment/viewParticipantNotification/",params = {"action=change"},method =  {RequestMethod.POST}) 
	public String doOverlay(@Valid @ModelAttribute("ipsViewParticiapantNotificationForm") IPSViewParticiapantNotificationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");
        	}
        }
		String forward=super.doChange(actionForm, request, response);
		return  forwards.get(forward)!=null?forwards.get(forward):forwards.get("default");
	}

	/**
	 * Returns the BOB Context associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The BOBContext object associated with the request (or null if
	 *         none is found).
	 */
	public static BobContext getBobContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session == null ? null : (BobContext) session
				.getAttribute(BDConstants.BOBCONTEXT_KEY);
	}
	
	/**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
	
	
}
