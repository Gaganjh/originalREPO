package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SelectContractDetailUtil;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.message.valueobject.Message.MessageStatus;
import com.manulife.pension.service.message.valueobject.MessageDetail;
import com.manulife.pension.service.message.valueobject.RecipientMessageInfo;

/**
 * Direct message action
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping( value = "/mcdirect")

public class MCMessageDirectController extends PsController implements MCConstants {
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("summary","redirect:/do/messagecenter/summary");
		}

	public MCMessageDirectController() {
		super(MCMessageDirectController.class);
	}
	@RequestMapping(value ="/message",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if (!MCEnvironment.isMessageCenterAvailable(request)) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		String messageIdStr = StringUtils.trimToNull(request.getParameter(ParamMessageId));

		int messageId = -1;
		if (messageIdStr != null) {
			try {
				messageId = Integer.parseInt(messageIdStr);
			} catch (NumberFormatException ex) {
				logger.error("Message id is invalid: " + messageIdStr, ex);
			}
		}
		// if the message id is not valid, go to home page
		if (messageId == -1) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		MessageServiceFacade facade = MessageServiceFacadeFactory.getInstance(request.getServletContext());
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		RecipientMessageInfo message = facade.getRecipientMessageById(messageId, userProfile);
		MessageDetail detail = MessageServiceFacadeFactory.getInstance(request.getServletContext()).getMessageDetails(
				messageId, (int) userProfile.getPrincipal().getProfileId(),
				Environment.getInstance().getApplicationId());
		 
		 // if the message is available and active
		if (message != null
				&& MessageStatus.ACTIVE.compareTo(message.getMessageStatus()) == 0) {
			Integer selectedContract = new Integer(message.getContractId());
			if (selectedContract != null) {
				Contract currentContract = userProfile.getCurrentContract();
				if (currentContract == null || currentContract.getContractNumber() != selectedContract) {
					// can access this contract
					if (userProfile.getMessageCenterAccessibleContracts().contains(selectedContract)) {
						// clean up first
						userProfile.setCurrentContract(null);
						SessionHelper.clearSession(request);
						// select the contract
						SelectContractDetailUtil.selectContract(userProfile, selectedContract);
					} else { // can not access the desired contract
						return forwards.get("summary");
					}
				}
				ControllerRedirect f = new ControllerRedirect(DetailTabUrl + "?" + ParamTabId + "=" + detail.getMessageTabId());
				return f.getPath();
			}
		}

		Set<Integer> accessibleContracts = userProfile.getMessageCenterAccessibleContracts();
		// if there is one, go to summary
		if (accessibleContracts != null && !accessibleContracts.isEmpty()) {
			return forwards.get("summary");
		} else {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

	}

}
