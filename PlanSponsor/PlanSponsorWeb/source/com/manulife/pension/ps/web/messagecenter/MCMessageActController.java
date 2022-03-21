package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;

import java.util.HashMap;

import javax.servlet.ServletContext;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SelectContractDetailUtil;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWSummary;
import com.manulife.pension.service.message.valueobject.Message.MessageStatus;
import com.manulife.pension.service.message.valueobject.MessageRecipient.RecipientStatus;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;
import com.manulife.pension.service.message.valueobject.RecipientMessageInfo;

/**
 * The action for Act/Info type message. It first marks the message as visited
 * and then goes to the target URL
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping( value ="/messagecenter")
public class MCMessageActController extends MCMessageController implements MCConstants {

	public MCMessageActController() {
		logger = Logger.getLogger(MCMessageActController.class);
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("summary","redirect:/do/messagecenter/summary");
		}
	

	@Override
	protected void doAction(HttpServletRequest request,
			RecipientMessageInfo message) throws SystemException {
		
		RecipientMessageDetail detail = (RecipientMessageDetail)message;
		if (!detail.getMessageRecipientStatus().getValue().equals(RecipientStatus.HIDDEN.getValue())) {
			getMessageServiceFacade().visitMessage(SessionHelper.getUserProfile(request), message.getId());
		}
	}

	/**
	 * Save the selected tab and section, then redirect to the parameter URL
	 */
	@Override
	protected String gotoNext(
			HttpServletRequest request, HttpServletResponse response,
			RecipientMessageInfo message,ServletContext servlet) throws IOException, ServletException,
			SystemException {
		
		boolean action = MessageActionType.ACTION.getValue().equals(
				request.getParameter(ParamUrlType));
		String url =  action ? message.getActionURL()
				: message.getInfoURL();
		
		if ( action && isArchived(message) ) {
			return MCConstants.ArchiveUrl;
		}

		UserProfile user = SessionHelper.getUserProfile(request);
		// before forwarding, check if the needs to switch context
		boolean firmMessage = message.getContractId() == null && message.getTpaFirmId() != null;
		if (!firmMessage) {
			int contractId = new Integer(message.getContractId());
			if (MCUtils.isInGlobalContext(request) || user.getCurrentContract().getContractNumber() != contractId) {
				// clean up first
				user.setContractProfile(null);
				SessionHelper.clearSession(request);

				if ( MCUtils.isInGlobalContext(request) ) {
					SessionHelper.setMCLeftMCFromGlobalContext(request, true);
				} else {
					SessionHelper.unsetMCLeftMCFromGlobalContext(request);
				}
				
				// select the contract
				SelectContractDetailUtil.selectContract(user, contractId);
			
			}
		}
		String forward=url;
		return forward;
	}

	private boolean isArchived(RecipientMessageInfo message) {
		RecipientMessageDetail detail = (RecipientMessageDetail) message;

		return !MessageStatus.ACTIVE.equals(detail.getMessageStatus())
				|| RecipientStatus.HIDDEN.equals(detail.getMessageRecipientStatus());
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	
	@RequestMapping(value ="/actMessage", method ={RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return 	forwards.get("detail");//if input forward not //available, provided default
			}
		}
		String forward=super.doExecute( form, request, response);
		return "redirect:"+forward; 
	}
	
	
	@Autowired
    private PSValidatorFWSummary psValidatorFWSummary;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWSummary);
	}
}
