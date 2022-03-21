package com.manulife.pension.bd.web.messagecenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWMcMessage;
import com.manulife.pension.delegate.MessageServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;
import com.manulife.pension.service.message.valueobject.RecipientMessageInfo;

/**
 * The action for Act/Info type message. It first marks the message as visited
 * and then goes to the target URL
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value ="/messagecenter")
public class MCMessageActController extends BDController {

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("homePage","redirect:/do/home/");
		forwards.put("error","/home/public_home.jsp");
		}

	String applicationId=Environment.getInstance().getApplicationId();

private static final Logger log = Logger.getLogger(MCMessageActController.class);

	public MCMessageActController() {
		super(MCMessageActController.class);
	}
	

	@RequestMapping(value ="/actMessage",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		int messageId =getId(request, "messageId");
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("homePage");//if input forward not //available, provided default
			}
		}
		BDUserProfile user = BDSessionHelper.getUserProfile(request);

		RecipientMessageInfo message = getRecipientMessageById(messageId, user);
		return gotoNext(request,response,message);
	}
	
	/**
	 * Save the selected tab and section, then redirect to the parameter URL
	 */

	protected String gotoNext(
			HttpServletRequest request, HttpServletResponse response,
			RecipientMessageInfo message) throws IOException, ServletException,
			SystemException {
		
		boolean action = MessageActionType.ACTION.getValue().equals(
				request.getParameter("INF"));
		String url="";
		if(message!=null){
		url =  action ? message.getActionURL()
				: message.getInfoURL();
		}else{
			log.error("Trying to access a message that does not exist, hence redirecting to home page..");
			//return  mapping.findForward("homePage");
			return forwards.get("homePage");
		}
	
		ControllerRedirect forward = new ControllerRedirect(url);
		
		return forward.getPath();
	}
	
	public RecipientMessageInfo getRecipientMessageById(int messageId,
			BDUserProfile userProfile) throws SystemException {
		return MessageServiceDelegate.getInstance(applicationId)
				.getRecipientMessageById(messageId,
						userProfile.getBDPrincipal().getProfileId(),
						applicationId);
	}
	
	/**
	 * Convert a integer string to an integer. If a invalid integer string,
	 * returns -1
	 * 
	 * @param idStr
	 * @return
	 */
	public static int getId(String idStr) {
		int id = -1;
		if (idStr != null) {
			try {
				id = Integer.parseInt(idStr);
			} catch (NumberFormatException e) {
			}
		}
		return id;
	}
	/**
	 * Utility method to get an Id as integer from Request
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static int getId(HttpServletRequest request, String paramName) {
		String idStr = request.getParameter(paramName);
		return getId(idStr);
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	
	@Autowired
	private BDValidatorFWMcMessage bdValidatorFWMcMessage;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWMcMessage);
	}
}
