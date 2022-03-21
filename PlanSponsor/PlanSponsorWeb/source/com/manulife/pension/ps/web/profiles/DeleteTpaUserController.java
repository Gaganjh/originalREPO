package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;

/**
 * Code in support of the View/Suspend/Unsuspend/Delete (tpa) user
 *
 */
@Controller
@RequestMapping(value = "/profiles")
@SessionAttributes({ "addEditUserForm" })

public class DeleteTpaUserController extends AbstractTpaUserController {
	@ModelAttribute("addEditUserForm")
	public AddEditUserForm populateForm() {
		return new AddEditUserForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("confirm", "/profiles/deleteUserConfirm.jsp");
		forwards.put("input", "/profiles/deleteTpaProfile.jsp");
		forwards.put("refresh", "redirect:/do/profiles/deleteTpaProfile/?action=refresh");
		forwards.put("confirmPage", "/profiles/editTpaUserConfirmation.jsp");
		forwards.put("manageUsers", "redirect:/do/profiles/manageTpaUsers/");
		forwards.put("viewPermissions", "redirect:/do/profiles/tpaFirmUserPermissions/?action=view&source=delete");
		forwards.put("tpaContactsTab", "redirect:/do/contacts/thirdPartyAdministrator/");
	}

	private static final String REFRESH_ACTION = "refresh";

	private static final String RESET_ACTION = "reset"; // confirmation
														// processing
	private static final String RESET_PAGE = "resetPage"; // confirmation
															// processing
	private static final String UNLOCK_PASSWORD_ACTION = "unlock"; // confirmation
																	// processing
	private static final String UNLOCK_PASSWORD_PAGE = "unlockPage"; // confirmation
																		// processing

	/**
	 * Constructor.
	 */
	public DeleteTpaUserController() {
		super(DeleteTpaUserController.class);
	}

	protected UserInfo populateUserInfo(AddEditUserForm form) {

		UserInfo userInfo = new UserInfo();

		userInfo.setProfileId(form.getProfileId());
		userInfo.setUserName(form.getUserName());
		userInfo.setRole(form.getUserRole());
		userInfo.setFirstName(form.getFirstName());
		userInfo.setLastName(form.getLastName());
		userInfo.setEmail(form.getEmail());
		userInfo.setSecondaryEmail(form.getSecondaryEmail());

		return userInfo;
	}

	protected void populateUserInfo(HttpServletRequest request, UserInfo userInfo, AddEditUserForm actionForm) {

	}

	protected void populateForm(HttpServletRequest request, AddEditUserForm form, UserInfo userInfo)
			throws SystemException {

		super.populateForm(request, form, userInfo);

		// TODO: (only need this for view op)
		form.setShowUnlock(isProfileLocked()); // MPR173
	}

	private boolean isProfileLocked() {
		boolean x = true;
		return x; // FIXME: service not ready yet(as of Feb 1)
	}

	/**
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;
	@Autowired
	private ViewSuspendDeleteTpaUserValidator viewSuspendDeleteTpaUserValidator;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(viewSuspendDeleteTpaUserValidator);
	}

	/*
	 * Different validation rules when we save and when we add tpa firm.
	 */

	protected String doWarningsValidation(AddEditUserForm form, HttpServletRequest request) {

		if (!form.isSaveAction())
			return null; // load of page

		return null;
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=refresh" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doRefresh(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry --> doRefresh");
		}

		String forward = forwards.get("input");

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doRefresh");
		}
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forward;
	}

	/**
	 * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping,
	 *      AutoForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value = "/deleteTpaProfile/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,
			ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		if(form.getProfileId() > 0){
			form.setUserName(null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry --> doDefault");
		}

		model.addAttribute("mapping", "delete");
		/*
		 * Call super's doDefault. If it returns any forward other than the
		 * input forward, we just return.
		 */
		String forward = super.doDefault(form, model, request, response);

		if (!forward.equals("input")) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}

		UserProfile userProfile = getUserProfile(request);
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(getUserProfile(request).getPrincipal());
		// added TPUM logic
		if (TpaumHelper.isTPAUM(userProfile)) {
			List tpaumFirms = TpaumHelper.getTpaFirmsForTpaum(loginUserInfo.getTpaFirmsAsCollection());
			// populate tpaumFirms list for TPAUM
			form.setTpaumFirms(tpaumFirms);

			/*
			 * TTP.34 Default 1 Permission Section for single-firm TPAUMs Only
			 * [a] If the logged in user is a TPAUM and has only one firm where
			 * their access level is TPAUM, then the initial display of the page
			 * will include one permission section showing that one firm with
			 * access level and permissions displayed and defaulted as per
			 * requirements in section 3.3.4.2.
			 */
			String mapping = (String) model.get("mapping");
			if (isAddUser(mapping) && tpaumFirms.size() == 1) {

				TPAFirmInfo tpaFirm = (TPAFirmInfo) tpaumFirms.iterator().next();
				TpaFirm tpaForm = new TpaFirm(new Integer(tpaFirm.getId()), tpaFirm.getName(), new Integer(9999));
				tpaForm.setNewFirm(true);
				if (tpaForm != null) {

					tpaForm.getContractAccesses().add(getDefaultContractAccess());
					TPAUserContractAccessActionHelper.filterFirmContractAccess(loginUserInfo, tpaForm);

					setTpaFirmCommon(tpaForm, tpaFirm);
					form.getTpaFirms().add(0, tpaForm);

					((AddEditUserForm) form.getClonedForm()).getTpaFirms().add(0, new TpaFirm());

				}
			}
		}
		resetTpaDropdowns(form, request);

		form.storeClonedForm();

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doDefault");
		}
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forwards.get("input");
	}

	// code lifted from ManagePasswordAction.
	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=resetPassword" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doResetPassword(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doReset");
		}

		UserProfile userProfile = getUserProfile(request);
		Principal principal = userProfile.getPrincipal();

		long lockedUserProfileId = form.getProfileId();

		Collection<GenericException> errors = TPAUserContractAccessActionHelper.checkForLockOrDelete(userProfile,
				lockedUserProfileId, form.getUserName());
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH_ACTION);
		}

		String forward = (RESET_ACTION);
		try {
			SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
			UserInfo userInfo = populateUserInfo(form);

			/**
			 * resetPassword method of SecurityService returns decrypted temp.
			 * password if the profile manager is CAR or SCAR this string will
			 * be the returns null otherwise.
			 * 
			 * UPDATE: resetPassword no longer required by the .jsp
			 */
			service.resetPassword(principal, userInfo, Environment.getInstance().getSiteLocation());

		} catch (SecurityServiceException e) {
			errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH_ACTION);
		} finally {
			LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + lockedUserProfileId);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doReset");
		}
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forward;
	}

	// code lifted from managePasswordAction.
	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=unlockPassword" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doUnlockPassword(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doUnlock");
		}

		UserProfile userProfile = getUserProfile(request);
		UserInfo userInfo = null;
		Principal principal = userProfile.getPrincipal();

		long lockedUserProfileId = form.getProfileId();

		Collection<GenericException> errors = TPAUserContractAccessActionHelper.checkForLockOrDelete(userProfile,
				lockedUserProfileId, form.getUserName());
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH_ACTION);
		}

		try {
			userInfo = populateUserInfo(form);
			SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
			service.unlockPassword(principal, userInfo);

		} catch (SecurityServiceException e) {
			errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH_ACTION);
		} finally {
			LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + lockedUserProfileId);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doUnlock");
		}
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forwards.get(UNLOCK_PASSWORD_ACTION);
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=suspend" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doSuspend(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		UserInfo userInfo = populateUserInfo(form);

		Principal principal = getUserProfile(request).getPrincipal();
		String forward = null;
		try {
			SecurityServiceDelegate.getInstance().suspendUser(principal, userInfo,
					Environment.getInstance().getSiteLocation());

			// setup for confirm screen.
			UserProfileForm upaf = new UserProfileForm();
			upaf.setFirstName(form.getFirstName());
			upaf.setLastName(form.getLastName());

			request.getSession().setAttribute("userProfileForm", upaf);

			forward = forwards.get("confirm");
			LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + ((AddEditUserForm) form).getProfileId());
		} catch (SecurityServiceException e) {
			Collection errors = new ArrayList();
			errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			forward = forwards.get("input");

			if (logger.isDebugEnabled()) {
				logger.debug("security exception on doSuspend" + e);
			}
		}
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forward;
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=unsuspend" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doUnsuspend(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		UserInfo userInfo = populateUserInfo(form);

		Principal principal = getUserProfile(request).getPrincipal();
		String forward = null;

		try {
			SecurityServiceDelegate.getInstance().unsuspendUser(principal, userInfo,
					Environment.getInstance().getSiteLocation());

			// setup for confirm screen.
			UserProfileForm upaf = new UserProfileForm();
			upaf.setFirstName(form.getFirstName());
			upaf.setLastName(form.getLastName());

			request.getSession().setAttribute("userProfileForm", upaf);

			forward = forwards.get("confirm");
			LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + ((AddEditUserForm) form).getProfileId());
		} catch (SecurityServiceException e) {
			Collection errors = new ArrayList();
			errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			forward = forwards.get("input");

			if (logger.isDebugEnabled()) {
				logger.debug("security exception on doUnsuspend" + e);
			}
		}
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forward;
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=delete" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doDelete(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		UserInfo userInfo = populateUserInfo(form);

		Principal principal = getUserProfile(request).getPrincipal();
		Collection errors = new ArrayList();
		String forward = null;
		try {
			SecurityServiceDelegate.getInstance().deleteUser(principal, userInfo,
					Environment.getInstance().getSiteLocation());

			// setup for confirm screen.
			DeleteProfileForm dpaf = new DeleteProfileForm();
			dpaf.setFirstName(form.getFirstName());
			dpaf.setLastName(form.getLastName());

			request.getSession().setAttribute("deleteProfileForm", dpaf);

			forward = forwards.get("confirm");
			LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + ((AddEditUserForm) form).getProfileId());
		} catch (SecurityServiceException e) {
			errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			forward = forwards.get("input");

			if (logger.isDebugEnabled()) {
				logger.debug("security exception" + e);
			}
		}
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forward;
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = "action=view permissions", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doViewPermissions(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		// support permissions button on view/delete/suspend/unsuspend
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		/*
		 * Prior to navigation to the permissions sub-page we need to setup the
		 * defaults and also some of the settings on the sub-page are also on
		 * the main page, so those need to be copied over.
		 */
		TPAActionHelper.syncSettingsToUserPermissions(form, Integer.parseInt(form.getTpaFirmId()), false);
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forwards.get(VIEW_PERMISSIONS);
	}

	//// Note: In support of the view's reset/unlock, not sure why it's done
	//// like this but since it works
	//// in the managePasswordAction this way(and we want to re-use the
	//// confirmation pages) I will
	//// just copy this stuff directly.

	/**
	 * Simply refresh the page. This action is used when we try to perform a
	 * REDIRECT after a POST.(see note above)
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return The input forward.
	 */
	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=refreshReset" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doRefreshReset(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,
			BindingResult bindingResult, ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		// if (form.getUserName() == null) { wtf
		// return mapping.findForward(MANAGE_USERS_ACTION);
		// }
		model.addAttribute("mapping", "delete");
		UserInfo userInfo = populateUserInfo(form);
		request.setAttribute(Constants.USERINFO_KEY, userInfo);
		clearSession(model, request);
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forwards.get(RESET_PAGE);
	}

	/**
	 * Simply refresh the page. This action is used when we try to perform a
	 * REDIRECT after a POST. (see note above)
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return The input forward.
	 */
	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=refreshUnlock" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doRefreshUnlock(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,
			BindingResult bindingResult, ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		
		model.addAttribute("mapping", "delete");
		request.setAttribute(Constants.USERINFO_KEY, form);
		clearSession(model, request);
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return forwards.get(UNLOCK_PASSWORD_PAGE);
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=confirm" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doConfirm(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doConfirm(form, request, response);
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=finish" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doFinish(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,
			ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		model.addAttribute("mapping", "delete");
		String forward = super.doFinish(form, model, request, response);
		model.remove("mapping");
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=back" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doCancel(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,
			ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		model.addAttribute("mapping", "delete");
		String forward = super.doCancel(form, model, request, response);
		model.remove("mapping");
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=reload" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doReload(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doReload(form, request, response);
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/deleteTpaProfile/", params = { "action=printPDF" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doPrintPDF(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doPrintPDF(form, request, response);
		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

}
