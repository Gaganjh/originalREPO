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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.registration.RegisterBrokerValidationForm;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWCreate;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.bd.web.validation.rules.SsnRule;
import com.manulife.pension.bd.web.validation.rules.TaxIdRule;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.bd.valueobject.BrokerEntityVerificationKey;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value ="/myprofile")
@SessionAttributes({"addBOBForm"})

public class AddBOBController extends BaseAutoController {
	
	@ModelAttribute("addBOBForm")
	public AddBOBForm populateForm() {
		return new AddBOBForm();
	}
	
	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/myprofile/securityInfo.jsp");
		forwards.put("cancel","redirect:/do/home/");
		forwards.put("step1","/myprofile/addBOBStep1.jsp");
		forwards.put("step2","/myprofile/addBOBStep2.jsp");
		forwards.put("finish","redirect:/do/home/");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");
	}

	
	protected static final String ForwardStep1 = "step1";
	protected static final String ForwardStep2 = "step2";
	protected static final String ForwardCancel = "cancel";
	protected static final String ForwardFinish = "finish";
	
	
	private final MandatoryRule contractNumberMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_CONTRACT_NUMBER);

	@RequestMapping(value ="/addBOB",  method = { RequestMethod.GET })	
	public String doDefault(@Valid @ModelAttribute("addBOBForm") AddBOBForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			actionForm.setChanged(true);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("step1");//if input forward not //available, provided default
	       }
		}
		//if user is bookmarked the URL, we still need to challenge.
		if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
			if((boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND)) {
				request.getSession().setAttribute("myProfileCurrentTab", MyProfileNavigation.AddBOBTabId);
				return forwards.get("myprofileDispatch");
			}
		}
		// clear the form
		actionForm.clear();
		return gotoPage(request, ForwardStep1);
	}

	/**
	 * Validate the input
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
	@RequestMapping(value ="/addBOB", params= {"action=validate"},  method = { RequestMethod.GET,RequestMethod.POST })
	public String doValidate(
			AddBOBForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		//AddBOBForm form = (AddBOBForm) actionForm;
		validateBOBForm(form, errors);
		if (errors.size() == 0) {
			BrokerEntityVerificationKey key = getBrokerEntityVerificationKey(form);
			try {
				BDPublicSecurityServiceDelegate.getInstance()
						.getBrokerByKeyContract(key, IPAddressUtils.getRemoteIpAddress(request));
			} catch (SecurityServiceException e) {
				logger.debug("Broker Verification Failed. ", e);
				errors
						.add(new ValidationError("",
								SecurityServiceExceptionHelper
										.getErrorCode(e)));
			}
		}
		String forward = ForwardStep2;
		if (errors.size() != 0) {
			setErrorsInRequest(request, errors);
			form.setChanged(true);
			forward = ForwardStep1;
		}
		
		return gotoPage( request, forward);
	}

	/**
	 * Handle the add action
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
	@RequestMapping(value ="/addBOB", params= {"action=add"},  method = { RequestMethod.GET,RequestMethod.POST })
	public String doAdd(
			@Valid @ModelAttribute("addBOBForm") AddBOBForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		BrokerEntityVerificationKey key = getBrokerEntityVerificationKey(form);
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		try {
			BDUserSecurityServiceDelegate.getInstance().addBrokerEntity(
					BDSessionHelper.getUserProfile(request).getBDPrincipal(),
					key, IPAddressUtils.getRemoteIpAddress(request));
		} catch (SecurityServiceException e) {
			logger.error("Broker Verification Failed. ", e);
			errors
					.add(new ValidationError(
							"",
							SecurityServiceExceptionHelper
									.getErrorCode(
											e,
											MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
		}
		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			return gotoPage(request, ForwardStep1);
		} else {
			clearContext( request);
			return forwards.get(ForwardFinish);
		}
	}

	/**
	 * Handle cancel action
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
	@RequestMapping(value ="/addBOB", params= {"action=cancel"},  method = { RequestMethod.GET,RequestMethod.POST })
	public String doCancel(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		clearContext( request);
		return forwards.get(ForwardCancel);
	}

	protected void validateForm(AutoForm actionForm,
			List<ValidationError> errors) {
		validateBOBForm((AddBOBForm) actionForm, errors);
	}

	protected void validateBOBForm(AddBOBForm form,
			List<ValidationError> errors) {
		EmailAddressRule.getInstance().validate("emailAddress", errors,
				form.getEmailAddress());
		boolean isValid = contractNumberMandatoryRule.validate("contractNum",
				errors, form.getContractNumber());
		if (isValid) {
            ContractNumberRule.getInstance().validate(
                    RegisterBrokerValidationForm.FIELD_CONTRACT_NUMBER, errors,
                    form.getContractNumber());
		}
        
		if (StringUtils.isEmpty(form.getSsn().getValue())
				&& StringUtils.isEmpty(form.getTaxId().getValue())) {
			errors.add(new ValidationError("",
					BDErrorCodes.BOTH_SSN_AND_TAX_ID_BLANK));
		} else if (!StringUtils.isEmpty(form.getSsn().getValue())) {
			SsnRule.getInstance().validate(
					RegisterBrokerValidationForm.FIELD_SSN, errors,
					form.getSsn());
		} else if (!StringUtils.isEmpty(form.getTaxId().getValue())) {
			TaxIdRule.getInstance().validate(
					RegisterBrokerValidationForm.FIELD_TAX_ID, errors,
					form.getTaxId());
		}
		return;
	}

	protected String gotoPage(
			HttpServletRequest request, String forward) throws SystemException {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest req = attr.getRequest();
		MyProfileContext context = MyProfileUtil.getContext(req.getServletContext(), request);
		context.getNavigation().setCurrentTabId(getTabId());
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	protected String getTabId() {
		return MyProfileNavigation.AddBOBTabId;
	}

	/**
	 * Helper method to get the BrokerEntityVerificationKey object
	 * 
	 * @param form
	 * @return BrokerEntityVerificationKey
	 */
	protected BrokerEntityVerificationKey getBrokerEntityVerificationKey(
			AddBOBForm form) {
		BrokerEntityVerificationKey key = new BrokerEntityVerificationKey();
		key.setContractNum(Integer.parseInt(form.getContractNumber()));
		key.setEmailAddress(form.getEmailAddress());
		key.setSsn(BDWebCommonUtils.getHyphenatedString(form.getSsn()
				.getValue(), BDConstants.ID_TYPE_SSN));
		key.setTaxId(BDWebCommonUtils.getHyphenatedString(form.getTaxId()
				.getValue(), BDConstants.ID_TYPE_TAX));
		return key;
	}

	/**
	 * The flow ends, needs to clear up all the related session attributes
	 * 
	 * @param request
	 */
	protected void clearContext(
			HttpServletRequest request) {
		MyProfileUtil.clearContext(request);
		request.getSession().removeAttribute("addBOBForm");
	}

	//both GET?POST is required for execution&CSRF is working as excepted in baseline
	/*@RequestMapping(value ="/addBOB",  method = {RequestMethod.POST, RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("addBOBForm") AddBOBForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			actionForm.setChanged(true);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("step1");//if input forward not //available, provided default
	       }
		}
		String forward=super.doExecute(actionForm, request, response);
		//return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}*/

	@Autowired
	private BDValidatorFWCreate bdValidatorFWCreate;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWCreate);
	}
	
}
