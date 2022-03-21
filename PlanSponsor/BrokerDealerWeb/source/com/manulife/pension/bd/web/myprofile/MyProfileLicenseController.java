package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletContext;
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

import com.manulife.pension.bd.web.navigation.UserNavigationFactory;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the Action class for My Profile License tab.
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping(value ="/myprofile")

public class MyProfileLicenseController extends BaseAutoController {
	@ModelAttribute("myprofileLicenseForm") 
	public MyProfileLicenseForm populateForm() 
	{
		return new MyProfileLicenseForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/myprofile/license.jsp");
		forwards.put("cancel","redirect:/do/home/");
		forwards.put("fail","/myprofile/license.jsp");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");
	}

    private static final String CANCEL_FORWARD = "cancel";

    /**
     * Constructor
     */
    public MyProfileLicenseController() {
        super(MyProfileLicenseController.class);
    } 

    @RequestMapping(value = "/license", method =  {RequestMethod.GET})
    public String doDefault( @Valid @ModelAttribute("myprofileLicenseForm") MyProfileLicenseForm licenseForm,BindingResult bindingResult,
   			HttpServletRequest request, HttpServletResponse response)
   					throws IOException, ServletException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }
        if(bindingResult.hasErrors()){
       		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       		if(errDirect!=null){
       			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
       		return	forwards.get("fail");//if input forward not //available, provided default
       		}
       	}
      //if user is bookmarked the URL, we still need to challenge.
    	if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
    		if((boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND)) {
    			request.getSession().setAttribute("myProfileCurrentTab", MyProfileNavigation.LicenseTabId);
    			return forwards.get("myprofileDispatch");
    		}
    	}
        MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(),request);
        context.getNavigation().setCurrentTabId(MyProfileNavigation.LicenseTabId);
       
        licenseForm.setProducerLicense(BDSessionHelper.getUserProfile(request).getBDPrincipal()
                .getProducerLicense());
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doDefault");
        }
        return forwards.get("input");
    }
    
   	/*public String doExecute(@Valid @ModelAttribute("myprofileLicenseForm") MyProfileLicenseForm actionForm,BindingResult bindingResult,
   			HttpServletRequest request, HttpServletResponse response)
   					throws IOException, ServletException, SystemException {
    	if(bindingResult.hasErrors()){
       		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       		if(errDirect!=null){
       			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
       		return	forwards.get("fail");//if input forward not //available, provided default
       		}
       	}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doExecute");
        }
        MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(),request);
        context.getNavigation().setCurrentTabId(MyProfileNavigation.LicenseTabId);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doExecute");
        }
        String forward=super.doExecute( actionForm, request, response);
   		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}*/

    /**
     * This method will be called if the action parameter is save. This will save the license
     * information to the database.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
    @RequestMapping(value = "/license",params={ "action=save" }, method =  {RequestMethod.POST})
    public String doSave( @Valid @ModelAttribute("myprofileLicenseForm") MyProfileLicenseForm licenseForm,BindingResult bindingResult,
   			HttpServletRequest request, HttpServletResponse response)
   					throws IOException, ServletException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSave");
        }
        if(bindingResult.hasErrors()){
       		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       		if(errDirect!=null){
       			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
       		return	forwards.get("fail");//if input forward not //available, provided default
       		}
       	}
        MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(),request);
        context.getNavigation().setCurrentTabId(MyProfileNavigation.LicenseTabId);
        List<ValidationError> errors = new ArrayList<ValidationError>();
        try {
            BDUserSecurityServiceDelegate.getInstance().updateExtUserLicense(
                    BDSessionHelper.getUserProfile(request).getBDPrincipal(),
                    licenseForm.getProducerLicense());
        } catch (SecurityServiceException sse) {
            logger.debug("Saving License Info Failed. ", sse);
            errors
					.add(new ValidationError(
							"",
							SecurityServiceExceptionHelper
									.getErrorCode(
											sse,
											MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
        }
        if (errors.size() == 0) {
            BDSessionHelper.getUserProfile(request).getBDPrincipal().setProducerLicense(
                    licenseForm.getProducerLicense());
            ServletContext contexta = request.getServletContext();
            UserNavigationFactory.getInstance(contexta).updateNavigation(request, contexta);
            licenseForm.setSuccess(true);
            licenseForm.setChanged(false);
        } else {
            licenseForm.setSuccess(false);
            setErrorsInRequest(request, errors);
            licenseForm.setChanged(true);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doSave");
        }
        return forwards.get("input");
    }

    /**
     * This method will be called when the action parameter is cancel. This will forward the user to
     * secure home page.
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
    @RequestMapping(value = "/license",params={ "action=cancel" }, method =  {RequestMethod.POST})
    public String doCancel( @Valid @ModelAttribute("myprofileLicenseForm") MyProfileLicenseForm licenseForm,BindingResult bindingResult,
   			HttpServletRequest request, HttpServletResponse response)
   					throws IOException, ServletException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCancel");
        }
        if(bindingResult.hasErrors()){
       		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       		if(errDirect!=null){
       			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
       		return	forwards.get("fail");//if input forward not //available, provided default
       		}
       	}
        MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(),request);
        context.getNavigation().setCurrentTabId(MyProfileNavigation.LicenseTabId);
        return forwards.get(CANCEL_FORWARD);
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
    @Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
	
	/* avoids token generation as this class acts as intermediate for many
	 * transactions.
	 * 
     * (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
     */
   /* @Override
	protected boolean isTokenRequired(String action) {
		return true;
	}*/
    
    /*
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenValidatorEnabled(java.lang.String)
	 */
    @Override
   	protected boolean isTokenValidatorEnabled(String action) {
       	// avoids methods from validation which ever is not required
       	return StringUtils.isNotEmpty(action)
   				&& (StringUtils.equalsIgnoreCase(action, "Save") || (StringUtils.equalsIgnoreCase(action, "Cancel")))?true:false;
   	}
	
}
