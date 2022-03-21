package com.manulife.pension.bd.web.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.myprofile.WebBrokerEntityProfile;
import com.manulife.pension.bd.web.myprofile.WebBrokerEntityProfileHelper;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.delegate.BDUserMigrationServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile;
import com.manulife.pension.service.broker.valueobject.impl.BrokerEntityExtendedProfileImpl;
import com.manulife.pension.service.security.bd.migration.OBDWUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * BDW user migration process step 1 action.
 * Verify the user id and password and load the user profile information
 * from the old site
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value ="/migration")
@SessionAttributes({"migrationForm"})

public class MigrationStep1Controller extends BDPublicWizardProcessController {
	@ModelAttribute("migrationForm") 
	public MigrationForm populateForm() 
	{
		return new MigrationForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/migration/step1.jsp");
		}

	private final MandatoryRule userNameManadatoryRule = new MandatoryRule(
			BDErrorCodes.EMPTY_USERNAME);
	private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
	private final MandatoryRule passwordManadatoryRule = new MandatoryRule(
			BDErrorCodes.EMPTY_PASSWORD);

	public MigrationStep1Controller() {
		super(MigrationStep1Controller.class, MigrationProcessContext.class,
				MigrationProcessContext.PROCESS_NAME,
				MigrationProcessContext.Step1State);
	}

	/**
	 * Handle the user click continue
	 */
	@Override
	protected ProcessState doContinue( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		MigrationForm f = (MigrationForm) form;

		List<ValidationError> errors = new ArrayList<ValidationError>();
		userNameManadatoryRule.validate("userName", errors, f.getUserName());
		passwordManadatoryRule.validate("password", errors, f.getPassword());
		// only if it passes the front end validation
		if (errors.isEmpty()) {
			try {
				// load the old bdw user profile
				OBDWUserProfile migratingProfile = BDUserMigrationServiceDelegate.getInstance()
						.validateUserId(f.getUserName(), f.getPassword());
				MigrationProcessContext context = (MigrationProcessContext) getProcessContext(request);
				context.setMigratingProfile(migratingProfile);
				context.setOldPassword(f.getPassword());
				// has to call after setMigratingProfile and setOldPassword
				context.checkIdPasswordStandard();
				// populate the profiles into the form
				populateBrokerEntities(f, migratingProfile);
				return getState().getNext(
						BDWizardProcessContext.ACTION_CONTINUE);
			} catch (SecurityServiceException e) {
				logger.debug("Failed to validate the migrating user", e);
				errors
						.add(new ValidationError("",
								SecurityServiceExceptionHelper
										.getErrorCode(e)));
			}
		}
		// there are errors, if it reaches here
		setErrorsInSession(request, errors);
		return getState();
	}
	@RequestMapping(value = "/step1", method = { RequestMethod.GET })
	protected String doInput(
			@Valid @ModelAttribute("migrationForm") MigrationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not //available, provided default
			}
		}

		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doExecuteInput(request);

		if (forward == null) {
			String forward1 = super.doInput(form, request, response);
			return StringUtils.contains(forward1, '/') ? forward1 : forwards.get(forward1);
		}
		return forward;
	}

	@RequestMapping(value = "/step1", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(
			@Valid @ModelAttribute("migrationForm") MigrationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = "/step1", params = { "action=cancel" }, method = { RequestMethod.POST })
	protected String doCancel(
			@Valid @ModelAttribute("migrationForm") MigrationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doInput");
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not //available, provided default
			}
		}
		if (BDSessionHelper.getUserProfile(request) != null) {
			return HomeRedirect;
		}
		String forward = super.doCancelContinue(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	/*@RequestMapping(value = "/step1", method =  {RequestMethod.POST,RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("migrationForm") MigrationForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("input");//if input forward not //available, provided default
			}
		}
		String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}*/
	private void populateBrokerEntities(MigrationForm f,
			OBDWUserProfile migratingProfile) {
		List<BrokerEntityExtendedProfile> brokerProfiles = migratingProfile
				.getBrokerEntities();
		List<WebBrokerEntityProfile> webProfiles = new ArrayList<WebBrokerEntityProfile>(
				brokerProfiles.size());
		for (BrokerEntityExtendedProfile p : brokerProfiles) {
			webProfiles.add(new WebBrokerEntityProfile(
					(BrokerEntityExtendedProfileImpl) p));
		}
		f.setBrokerEntityProfilesList(WebBrokerEntityProfileHelper
				.sortBrokerEntityProfiles(webProfiles));
        f.setPrimaryBrokerPartyId(f.getBrokerEntityProfilesList().get(0).getId());
        f.setAddressFlagMap(WebBrokerEntityProfileHelper.getAddressFlagMap(webProfiles));
	}
}
