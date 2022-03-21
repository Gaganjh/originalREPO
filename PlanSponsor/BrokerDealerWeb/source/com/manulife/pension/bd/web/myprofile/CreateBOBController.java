package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.messagecenter.BDMessageCenterConstants;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWCreate;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.bd.valueobject.BrokerEntityVerificationKey;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value = "/myprofile")
@SessionAttributes({ "createBOBForm" })

public class CreateBOBController extends CommonBOB {
	@ModelAttribute("createBOBForm")
	public CreateBOBForm populateForm() {
		return new CreateBOBForm();
	}

	public static final String DEFAULT = "default";
	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/myprofile/securityInfo.jsp");
		forwards.put("cancel", "redirect:/do/home/");
		forwards.put("step1", "/myprofile/createBOBStep1.jsp");
		forwards.put("step2", "/myprofile/createBOBStep2.jsp");
		forwards.put("finish", "redirect:/do/home/");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");
	}

	private final MandatoryRule licenseMandatoryRule = new MandatoryRule(
			BDErrorCodes.LICENSE_VERIFICATION_NOT_SELECTED);

	protected void validateForm(AutoForm actionForm, List<ValidationError> errors) {
		super.validateBOBForm((CreateBOBForm) actionForm, errors);
		CreateBOBForm form = (CreateBOBForm) actionForm;
		licenseMandatoryRule.validate("producerLicense", errors, form.getProducerLicense());
	}

	/**
	 * Handle add action
	 */

	public String doAdd(AutoForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		CreateBOBForm form = (CreateBOBForm) actionForm;
		BrokerEntityVerificationKey key = getBrokerEntityVerificationKey(form);
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		try {
			BDUserSecurityServiceDelegate.getInstance().upgradeToBroker(
					BDSessionHelper.getUserProfile(request).getBDPrincipal(), key, form.getProducerLicense(),
					form.getEmailNotification(), BDMessageCenterConstants.ReceiveSummaryEmailPreference,
					IPAddressUtils.getRemoteIpAddress(request));
		} catch (SecurityServiceException e) {
			logger.debug("Upgrade Failed. ", e);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e,
					MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
		}
		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			return forwards.get(gotoPage(request, ForwardStep1));
		} else {
			// log the user out
			BDSessionHelper.invalidateSession(request, response);
			return forwards.get(ForwardFinish);
		}
	}

	protected String getTabId() {
		return MyProfileNavigation.CreateBOBTabId;
	}

	/**
	 * Perform PenTest validation before core business processing.
	 */
	@RequestMapping(value = "/createBOB", method = { RequestMethod.POST, RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("createBOBForm") CreateBOBForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				actionForm.setChanged(true);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(gotoPage(request, ForwardStep1));
			}
		}

		//if user is bookmarked the URL, we still need to challenge.
    	if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
    		if((boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND)) {
    			request.getSession().setAttribute("myProfileCurrentTab", MyProfileNavigation.CreateBOBTabId);
				return forwards.get("myprofileDispatch");
    		}
    	}
    	
    	String forward = super.doExecute(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/createBOB", params = { "action=cancel" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doCancel(@Valid  @ModelAttribute("createBOBForm") CreateBOBForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			actionForm.setChanged(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(gotoPage(request, ForwardStep1));
			}
		}

		String forward = super.doCancel(actionForm, request, response);
		actionForm.setAction(DEFAULT);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@Autowired
	private BDValidatorFWCreate bdValidatorFWCreate;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWCreate);
	}

}
