package com.manulife.pension.bd.web.login;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.manulife.pension.bd.web.password.ForgetPasswordContext;
import com.manulife.pension.bd.web.password.ForgetPasswordForm;
import com.manulife.pension.bd.web.process.BDProcessContextHelper;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.passcode.PasscodeForm;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.util.BaseSessionHelper;

@Controller
@RequestMapping("/forgetPassword/stepUpValidation")
public class ForgetPasswordStepUpValidationController extends BDPublicWizardProcessController {

	private static final Logger LOGGER = Logger.getLogger(ForgetPasswordStepUpValidationController.class);
	
	@Autowired
	private BdForgetPasswordValidatePasscodeController bdForgetPasswordValidatePasscodeController;
	private static final String FORWARD_SUCCESS_KEY = "success";
	private static final Map<String, String> forwards;

	static {
		Map<String, String> map = new HashMap<>();
		for(String forwardType : Arrays.asList("input", "default", "retry", "expired", "cooling", "resend")) {
			map.put(forwardType, "/password/stepUpValidation.jsp");
		}
		for(String forwardType : Arrays.asList("failure", "locked", "cancel")) {
			map.put(forwardType, "/home/public_home.jsp");
		}
		map.put(FORWARD_SUCCESS_KEY, "redirect:/do/forgetPassword/step3");
		map.put("updatePassword", "redirect:/do/updatePassword/");
		
		forwards = Collections.unmodifiableMap(map);
	}

	@ModelAttribute("forgetPasswordForm")
	public ForgetPasswordForm populateForm() {
		return new ForgetPasswordForm();
	}

	public ForgetPasswordStepUpValidationController() {
		super(ForgetPasswordStepUpValidationController.class, ForgetPasswordContext.class,
				ForgetPasswordContext.ProcessName, ForgetPasswordContext.StepUpValidationStep);
	}

	@GetMapping
	public String showForm(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("entry -> showForm");
		}

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("default");
			}
		}

		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doExecuteInput(request);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("super.doExecuteInput(request) output is [ " + forward + " ]");
		}
		
		if (forward == null) {
			String forward1 = super.doInput(forgetPasswordForm, request, response);
			return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1);
		}
		return forward;

	}

	@PostMapping
	public String execute(@Valid @ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("entry -> execute");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("default");
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		//Update the State for next step equivalent to calling doCancelContinue
		updateContext(request, forgetPasswordForm);
		return execute(forgetPasswordForm, request, response);
	}
	
	private void updateContext(HttpServletRequest request, ForgetPasswordForm forgetPasswordForm) throws SystemException {
		BDWizardProcessContext context = (BDWizardProcessContext) getProcessContext(request);
		context.updateContext(forgetPasswordForm);
		getProcessContext(request).setCurrentState(getState().getNext(BDWizardProcessContext.ACTION_CONTINUE));
	}

	private String execute(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		
		String forward = "cancel";
		
		try {
			final ForgetPasswordContext forgetPasswordContext = (ForgetPasswordContext) BDProcessContextHelper.getProcessContext(request, processName);
			PasscodeForm passcodeForm = new PasscodeForm();
			passcodeForm.setPasscode( ((ForgetPasswordForm)actionForm).getPasscode());
			forward = bdForgetPasswordValidatePasscodeController.doExecute(passcodeForm, request, response);
			//Invalidate the session and restore the context 
			if (FORWARD_SUCCESS_KEY.equals(forward)) {
				BaseSessionHelper.invalidateSessionKeepCookie(request);
				BDProcessContextHelper.setProcessContext(request, processName, forgetPasswordContext);
			}			
			//Set this as it was set during Step 1 and is expected by the next controller
			request.getSession().setAttribute("userId", String.valueOf( forgetPasswordContext.getSecurityProfile().getPrincipal().getProfileId() ) );
		} catch (IOException | ServletException e) {
			LOGGER.error("Exception During Passcode Processing", e);
		}
		
		if ( LOGGER.isDebugEnabled() ) {
			LOGGER.debug("Passcode Verification completed and the result is [ " + forward + " ] ");
		}
		
		return forwards.get(forward);
	}

	@Override
	protected ProcessState doContinue(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
	}
	
	@Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
}

