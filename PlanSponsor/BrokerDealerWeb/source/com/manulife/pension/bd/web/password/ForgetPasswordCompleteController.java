package com.manulife.pension.bd.web.password;

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
import com.manulife.pension.bd.web.process.BDWizardProcessController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;

/**
 * The last step of forget password process. It merely display a confirmation
 * page. 
 * 
 * But it ends the process by doing cleaning up
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value ="/forgetPassword")

public class ForgetPasswordCompleteController extends BDWizardProcessController {
	@ModelAttribute("forgetPasswordForm") 
	public ForgetPasswordForm populateForm()
	{
		return new ForgetPasswordForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/password/forgetPasswordStep4.jsp");
		forwards.put("fail","/password/forgetPasswordStep3.jsp");
		}

	public ForgetPasswordCompleteController() {
		super(ForgetPasswordCompleteController.class, ForgetPasswordContext.class,
				ForgetPasswordContext.ProcessName, ForgetPasswordContext.StepComplete);
	}
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static final String HomeRedirect=HomeRedirect1.getPath();
	@Override
	protected ProcessState doContinue( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		return null;
	}
	@RequestMapping(value = "/step4", method =  {RequestMethod.GET}) 
	public String doInput(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");//if input forward not //available, provided default
			}
		}
		String forward = super.doInput( actionForm, request, response);
		endProcess(request);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/step4", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}

	
}
