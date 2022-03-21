package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.mimic.StartMimicController;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWSearch;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.UserManagementAccessRules;
import com.manulife.pension.service.security.bd.UserManagementAccessRules.UsermanagementOperation;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * The action to dispatch to specific managing function based on the request
 * parameter. It also checks the authorization of the internal user on the
 * specific operation and external user role
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value = "/usermanagement")

public class UserManagementDispatchController extends BaseAutoController {
	@ModelAttribute("userManagementDispatchForm")
	public UserManagementDispatchForm populateForm() {
		return new UserManagementDispatchForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private static final EnumMap<BDUserRoleType, String> UserManageForwardMap = new EnumMap<BDUserRoleType, String>(
			BDUserRoleType.class);

	static {
		forwards.put("exception", "redirect:/do/usermanagement/search?task=exception");
		forwards.put("search", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("resetPassword", "redirect:/do/usermanagement/resetPassword");
		forwards.put("passcodeView", "redirect:/do/usermanagement/passcodeView");
		forwards.put("exemptPasscode", "redirect:/do/usermanagement/passcodeExemption");
		forwards.put("manageBroker", "redirect:/do/manage/broker");
		forwards.put("manageBasicBroker", "redirect:/do/manage/basicBroker");
		forwards.put("manageAssistant", "redirect:/do/manage/assistant");
		forwards.put("manageFirmrep", "redirect:/do/manage/firmrep");
		forwards.put("manageRia", "redirect:/do/manage/ria");
		
		UserManageForwardMap.put(BDUserRoleType.BasicFinancialRep, "manageBasicBroker");
		UserManageForwardMap.put(BDUserRoleType.FinancialRep, "manageBroker");
		UserManageForwardMap.put(BDUserRoleType.FinancialRepAssistant, "manageAssistant");
		UserManageForwardMap.put(BDUserRoleType.FirmRep, "manageFirmrep");
		UserManageForwardMap.put(BDUserRoleType.RIAUser, "manageRia");
	}

	private static final String ForwardSearch = "search";
	private static final String ForwardResetPassword = "resetPassword";

	@RequestMapping(value = "/dispatch", method = { RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("userManagementDispatchForm") UserManagementDispatchForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("search");// if input forward not //available,
										// provided default
			}
		}
		return forwards.get(ForwardSearch);
	}

	/**
	 * Handles reset password action
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

	@RequestMapping(value = "/dispatch", params = "action=resetPassword", method = { RequestMethod.POST })
	public String doResetPassword(
			@Valid @ModelAttribute("userManagementDispatchForm") UserManagementDispatchForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("search");// if input forward not
												// //available, provided default
			}
		}
		if (actionForm.getUserProfileId() == 0) {
			return noSelectionForward(actionForm, request, response);
		}
		BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
		if (!UserManagementAccessRules.getInstance().isOperationAllowed(principal.getBDUserRole().getRoleType(),
				actionForm.getUserRole(), UsermanagementOperation.ResetPassword)) {
			return operationNotAllowedForward(actionForm, request, response);
		}
		try {
			UserManagementContext.setCurrentUser(request, principal, actionForm.getUserProfileId());
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				UserManagementHelper.setUserManagementException(request, e);
				return forwards.get("exception");
			}
			return forwards.get(ForwardSearch);
		}
		return forwards.get(ForwardResetPassword);
	}

	/**
	 * Handles the manage action
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
	@RequestMapping(value = "/dispatch", params = "action=manage", method = { RequestMethod.POST })
	public String doManage(
			@Valid @ModelAttribute("userManagementDispatchForm") UserManagementDispatchForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("search");// if input forward not
												// //available, provided default
			}
		}
		if (actionForm.getUserProfileId() == 0) {
			return noSelectionForward(actionForm, request, response);
		}
		BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
		if (!UserManagementAccessRules.getInstance().isOperationAllowed(principal.getBDUserRole().getRoleType(),
				actionForm.getUserRole(), UsermanagementOperation.View)) {
			return operationNotAllowedForward(actionForm, request, response);
		}
		try {
			UserManagementContext.setCurrentUser(request, principal, actionForm.getUserProfileId());
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				UserManagementHelper.setUserManagementException(request, e);
				return forwards.get("exception");
			}

			return forwards.get(ForwardSearch);
		}
		return forwards.get(UserManageForwardMap.get(actionForm.getUserRole()));
	}

	/**
	 * Handles the mimic action
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
	@RequestMapping(value = "/dispatch", params = { "action=mimic" }, method = { RequestMethod.POST })
	public String doMimic(
			@Valid @ModelAttribute("userManagementDispatchForm") UserManagementDispatchForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("search");// if input forward not
												// //available, provided default
			}
		}
		if (actionForm.getUserProfileId() == 0) {
			return noSelectionForward(actionForm, request, response);
		}
		BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
		if (!UserManagementAccessRules.getInstance().isOperationAllowed(principal.getBDUserRole().getRoleType(),
				actionForm.getUserRole(), UsermanagementOperation.Mimic)) {
			return operationNotAllowedForward(actionForm, request, response);
		}
		ControllerRedirect forward = new ControllerRedirect(URLConstants.StartMimic);
		forward.addParameter(StartMimicController.PARAM_USER_PROFILE_ID, actionForm.getUserProfileId());
		return forward.getPath();
	}

	private String noSelectionForward(AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		errors.add(new ValidationError("", BDErrorCodes.USER_SEARCH_NO_SELECTION));
		setErrorsInSession(request, errors);
		return forwards.get(ForwardSearch);
	}

	private String operationNotAllowedForward(AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		errors.add(new ValidationError("", BDErrorCodes.USER_SEARCH_INSUFFICIENT_ACCESS));
		setErrorsInSession(request, errors);
		return forwards.get(ForwardSearch);
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */

	@Autowired
	private BDValidatorFWSearch bdValidatorFWSearch;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWSearch);
	}
}
