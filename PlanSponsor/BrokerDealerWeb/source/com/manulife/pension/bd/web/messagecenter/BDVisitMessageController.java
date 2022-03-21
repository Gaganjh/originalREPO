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
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

/**
 * The action that set the RecipientMessage status to Visited.
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value ="/messagecenter")

public class BDVisitMessageController extends BDController {
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm()
	{
		return new DynaForm();
				}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("fail","/messagecenter/messagecenter.jsp ");
		forwards.put("error","/home/public_home.jsp");
		}

	private static final Logger log = Logger.getLogger(BDVisitMessageController.class);
	
	public BDVisitMessageController() {
		super(BDVisitMessageController.class);
	}
	
	@RequestMapping(value ="/visitMessage",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("fail");//if input forward not //available, provided default
	       }
		}
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		byte[] jsonResultBytes;
		// if in mimic mode, do not apply this
		if (!BDMessageCenterUtils.isUnderMimic(userProfile)) {
			Integer messageId = null;
			messageId = BDWebCommonUtils.getRequestParameterAsInt(request,
					"messageId");
			try {
			BDMessageCenterFacade.getInstance().visitMessage(
					userProfile, messageId);
			} catch (SystemException e) {
				log.error("Fail to set the message as visited, messageId="
						+ messageId + ",userProfileId = "
						+ userProfile.getBDPrincipal().getProfileId(), e);
			}
			response.setHeader("Cache-Control", "must-revalidate");
			response.setContentType("text/plain");
			jsonResultBytes = "success".getBytes();
		} else {
			jsonResultBytes = "fail".getBytes();
		}
		response.setContentLength(jsonResultBytes.length);

		try {
			response.getOutputStream().write(jsonResultBytes);
		} catch (IOException ioException) {
			throw new SystemException(ioException, "Exception writing result.");
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException ioException) {
				throw new SystemException(ioException,
						"Exception closing output stream.");
			}
		}
		return null;
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	
	@Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
	
}
