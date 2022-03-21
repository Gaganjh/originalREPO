package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.messagecenter.history.MCCarReportValidator;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.message.valueobject.MessageDetail;
import com.manulife.pension.service.message.valueobject.MessageRecipient;
import com.manulife.pension.service.message.valueobject.Recipient;


/**
 * Action to complete the Message.
 * 
 * @author dsnowdon
 *
 */
@Controller
@RequestMapping( value = {"/messagecenter"})
@SessionAttributes({"messageDetailForm"})


public class MCMessageDetailController extends PsAutoController{

	@ModelAttribute("messageDetailForm") 
	public MCDetailForm populateForm()
	{
		return new MCDetailForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default"," /messagecenter/carmessagedetail.jsp");
		}

	private class RecipientSort implements Comparator<MessageRecipient> {

		public int compare(MessageRecipient o1, MessageRecipient o2) {
			Recipient r1 = o1.getRecipient();
			Recipient r2 = o2.getRecipient();
			
			if ( r1.getLastName() != null && r2.getLastName() != null ) {
				if( r1.getLastName().compareTo(r2.getLastName()) == 0 ) {
					if ( r1.getFirstName() != null && r2.getFirstName() != null ) {
						return r1.getFirstName().toUpperCase().compareTo(r2.getFirstName().toUpperCase());
					} else {
						return 0;
					}
				} else {
					return r1.getLastName().toUpperCase().compareTo(r2.getLastName().toUpperCase());
				}
			}

			return 0;
		}
	}

	public MCMessageDetailController() {
		logger = Logger.getLogger(MCMessageDetailController.class);
	}


	@RequestMapping(value ="/messageDetail", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("messageDetailForm") MCDetailForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
		
		if ( form.getContractId() == null  || form.getMessageId() == null ) {
			forwards.get("homepagefinder");
		}
		form.setCarView( StringUtils.equals(request.getServletPath(), "carView"));

		int messageId = Integer.parseInt(form.getMessageId());
		
		MessageDetail detail = MessageServiceFacadeFactory.getInstance(request.getServletContext()).getMessageDetails(messageId,
				(int) getUserProfile(request).getPrincipal().getProfileId(), Environment.getInstance().getAppId());
		
		List<MessageRecipient> recipients =  new ArrayList<MessageRecipient>();
		recipients.addAll(detail.getMessageRecipients());
		Collections.sort(recipients, new RecipientSort());
		detail.setMessageRecipients(recipients);
		
		form.setMessage(detail);
		return forwards.get("default");
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	@Autowired
	MCCarReportValidator mCCarReportValidator;
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}

}
