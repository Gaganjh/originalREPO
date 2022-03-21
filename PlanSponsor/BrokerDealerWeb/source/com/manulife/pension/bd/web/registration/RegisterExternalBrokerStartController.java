package com.manulife.pension.bd.web.registration;

import java.io.IOException;
import java.util.HashMap;

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

import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDProcessContextHelper;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWExternalBroker;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.process.SimpleProcessState;

/**
 * This is the common action for the initial step of both Broker/Basic Broker Registration
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/registerExternalBroker")

public class RegisterExternalBrokerStartController extends BaseAutoController {
	@ModelAttribute("registerExternalBrokerStartForm") 
	public RegisterExternalBrokerStartForm populateForm() 
	{
		return new RegisterExternalBrokerStartForm();
		}
	public static HashMap<String,String>forwards= new HashMap<String,String>();
	static{
		forwards.put("getRegistered","/registration/registerExternalBrokerStart.jsp");
		forwards.put("startRegistration","/registration/registerExternalBrokerStep1.jsp");
		forwards.put("step1","/registration/registerExternalBrokerStep1.jsp");
		}
	public static final String REDIRECT="redirect:";
    public static final String GET_REGISTERED_FORWARD = "getRegistered";

    public static final String START_REGISTRATION_FORWARD = "startRegistration";

    public static final String STEP1_FORWARD = "step1";

    public static final String YES = "Yes";

    public static final String NO = "No";

	private static ControllerRedirect HomeRedirect = new ControllerRedirect(URLConstants.HomeURL);
	
	
	 

	/**
     * The method will be called when the action parameter is null or default. This will forward the
     * user to the External Broker Registration start page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value = "/start", method =  {RequestMethod.GET})
	 public String doDefault(@Valid @ModelAttribute("registerExternalBrokerStartForm") RegisterExternalBrokerStartForm form,BindingResult bindingResult,
				HttpServletRequest request, HttpServletResponse response)
						throws IOException, ServletException, SystemException {
	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> doDefault");
	        }
	        
	        if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		         return    forwards.get("getRegistered");//if input forward not //available, provided default
		       }
			}
			// protect in case the user already log in
			if (BDSessionHelper.getUserProfile(request) != null) {
				return HomeRedirect.getPath();	
			}
	        return forwards.get(GET_REGISTERED_FORWARD);
	    }
	


    /**
     * The method will be called when the action parameter is start. This will forward the user to
     * the External Broker Registration step 1 page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value ="/start",params= {"action=start"}, method =  {RequestMethod.POST,RequestMethod.GET})
	public String doStart(@ModelAttribute("registerExternalBrokerStartForm") RegisterExternalBrokerStartForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doStart");
        }
        if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	          return   forwards.get("getRegistered");//if input forward not //available, provided default
	       }
		}
		// protect in case the user already log in
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect.getPath();	
		}
        return forwards.get(START_REGISTRATION_FORWARD);
    }
    /**
     * The method will be called when the action parameter is step1. This will forward the user to
     * the specific External User (Broker / Basic Broker) Registration step 1 page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	
	@RequestMapping(value = "/start", params= {"action=step1"}, method =  {RequestMethod.POST,RequestMethod.GET})
	public String doStep1(@Valid @ModelAttribute("registerExternalBrokerStartForm") RegisterExternalBrokerStartForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		
		if (logger.isDebugEnabled()) {
            logger.debug("entry -> doStep1");
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	            return  forwards.get("getRegistered");//if input forward not //available, provided default
	       }
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect.getPath();	
		}
        if (logger.isDebugEnabled()) {
            logger.debug("User Has Contract --> " + form.getUserHasContract());
        }
        BDWizardProcessContext context = null;
        String processName = null;
        SimpleProcessState step1 = null;
        if (StringUtils.equalsIgnoreCase(YES, form.getUserHasContract())) {
            context = new RegisterBrokerProcessContext();
            processName = RegisterBrokerProcessContext.ProcessName;
            step1 = RegisterBrokerProcessContext.Step1;
        } else if (StringUtils.equalsIgnoreCase(NO, form.getUserHasContract())) {
            context = new RegisterBasicBrokerProcessContext();
            processName = RegisterBasicBrokerProcessContext.ProcessName;
            step1 = RegisterBasicBrokerProcessContext.Step1;
        } else {
            logger.error("No selection is made for contract, should not happen");
            throw new SystemException("No selection is made for contract, should not happen");
        }
        context.setCurrentState(step1);
        BDProcessContextHelper.setProcessContext(request, processName, context);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doStep1");
        }
        ControllerRedirect f=    new ControllerRedirect(context.getCurrentState().getUrl());
        return REDIRECT+f.getPath();
    }

    /**
     * The method will be called when the action parameter is canel. This will forward the user to
     * the public home page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value = "/start", params= {"action=cancel"}, method =  {RequestMethod.GET})
	public String doCancel(@Valid @ModelAttribute("registerExternalBrokerStartForm") RegisterExternalBrokerStartForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		 if (logger.isDebugEnabled()) {
	            logger.debug("entry -> doCancel");
	        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	          return   forwards.get("getRegistered");//if input forward not //available, provided default
	       }
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect.getPath();	
		}
       
        ControllerRedirect f= new ControllerRedirect(URLConstants.HomeURL);
        return REDIRECT+ f.getPath();
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	
	@Autowired
	private BDValidatorFWExternalBroker bdValidatorFWExternalBroker;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWExternalBroker);
	}
	
}
