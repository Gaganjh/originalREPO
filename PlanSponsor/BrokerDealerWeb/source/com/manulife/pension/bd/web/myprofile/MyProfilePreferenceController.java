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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.messagecenter.BDMessageCenterFacade;
import com.manulife.pension.bd.web.navigation.UserNavigationFactory;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.BDUserMessageCenterPreferences;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * The action to handle my profile preference
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value ="/myprofile")
@SessionAttributes({"myprofilePreferenceForm"})
public class MyProfilePreferenceController extends BaseAutoController {
	
	@ModelAttribute("myprofilePreferenceForm")
	public MyProfilePreferenceForm populateForm() 
	{
		return new MyProfilePreferenceForm();
		}
	public static HashMap<String,String> forwards =new HashMap<String,String>();
	static{
		forwards.put("input","/myprofile/preference.jsp");
		forwards.put("cancel","redirect:/do/home/");
		forwards.put("fail","/myprofile/preference.jsp");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");}
	

	private static final String CANCEL_FORWARD = "cancel";

	private final MandatoryRule fundListingMandatoryRule = new MandatoryRule(
			BDErrorCodes.FUND_LISTING_NOT_SELECTED);

	public MyProfilePreferenceController() {
		super(MyProfilePreferenceController.class);
	} 
	//Note:1. Removed execute Method since its was handling only the penTest .moved pent test to seperate validator
		// 2.doExecute method is called in all the method in order to handle GET/POST Method
	
	
	
	
	@RequestMapping(value ="/preference",  method = {RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("myprofilePreferenceForm") MyProfilePreferenceForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward1=null;
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forward1="redirect:/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return forward1;
			}
		}
		//if user is bookmarked the URL, we still need to challenge.
    	if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
    		if((boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND)) {
    			request.getSession().setAttribute("myProfileCurrentTab", MyProfileNavigation.PreferenceTabId);
    			return forwards.get("myprofileDispatch");
    		}
    	}
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		BDUserRoleType role = profile.getBDPrincipal().getBDUserRole()
				.getRoleType();
		// only broker and broker assistant can have message preferences
		switch (role) {
		case FinancialRep:
		case FinancialRepAssistant:
			form.setHasMessagePreference(true);
			break;
		default:
			form.setHasMessagePreference(false);
			break;
		}
		MyProfileContext context = MyProfileUtil.getContext(request
				.getServletContext(), request);
		context.getNavigation().setCurrentTabId(
				MyProfileNavigation.PreferenceTabId);
		if (form.isHasMessagePreference()) {
			//BDUserProfile profile = BDSessionHelper.getUserProfile(request);
			form.setMessageCenterPreferences(BDMessageCenterFacade
					.getInstance().getMCPreferences(
							profile.getBDPrincipal().getProfileId()));
		}
		SiteLocation location = BDSessionHelper.getUserProfile(request)
				.getDefaultFundListing();
		form.setDefaultSiteLocation(location == null ? "" : location.toString());
		return forwards.get("input");
	}


	/**
	 * This method will be called if the action parameter is save. This will
	 * save the license information to the database.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value ="/preference",params={ "action=save" },  method = {RequestMethod.POST })
	public String doSave(
			@Valid @ModelAttribute("myprofilePreferenceForm") MyProfilePreferenceForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward1=null;
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forward1="redirect:/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return forward1;
			}
		}
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		BDUserRoleType role = profile.getBDPrincipal().getBDUserRole()
				.getRoleType();
		// only broker and broker assistant can have message preferences
		switch (role) {
		case FinancialRep:
		case FinancialRepAssistant:
			form.setHasMessagePreference(true);
			break;
		default:
			form.setHasMessagePreference(false);
			break;
		}
		MyProfileContext context = MyProfileUtil.getContext(request
				.getServletContext(), request);
		context.getNavigation().setCurrentTabId(
				MyProfileNavigation.PreferenceTabId);
		
		
		BDUserMessageCenterPreferences messagePreferences = null;
		if (form.isHasMessagePreference()) {
			messagePreferences = form.getMessageCenterPreferences();
		}
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		fundListingMandatoryRule.validate("defaultFundListing", errors,
				form.getDefaultSiteLocation());

		if (errors.isEmpty()) {
			try {
				BDUserProfile userProfile = BDSessionHelper
						.getUserProfile(request);
				SiteLocation defaultFundListing = SiteLocation.valueOf(form
						.getDefaultSiteLocation());
				BDUserSecurityServiceDelegate.getInstance().updatePreference(
						userProfile.getBDPrincipal(), defaultFundListing,
						messagePreferences);
				// update the current session
				userProfile.setDefaultFundListing(defaultFundListing);
				ServletContext contexta = request.getServletContext();
				UserNavigationFactory.getInstance(contexta).updateNavigation(
						request, contexta);
			} catch (SecurityServiceException e) {
				logger.debug("Save preference fails", e);
				errors.add(new ValidationError(
						"",
						SecurityServiceExceptionHelper
								.getErrorCode(
										e,
										MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
			}
		}
		if (errors.isEmpty()) {
			form.setChanged(false);
			form.setSuccess(true);
		} else {
			setErrorsInRequest(request, errors);
			form.setChanged(true);
			form.setSuccess(false);
		}

		// update the forms using the message preferences
		if (form.isHasMessagePreference()) {
			form.setMessageCenterPreferences(messagePreferences);
		}
		return forwards.get("input");
	}
	/**
	 * This method will be called when the action parameter is cancel. This will
	 * forward the user to secure home page.
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
	@RequestMapping(value ="/preference", params={ "action=cancel" }, method = {RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("myprofilePreferenceForm") MyProfilePreferenceForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward1=null;
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forward1="redirect:/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return forward1;
			}
		}
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		BDUserRoleType role = profile.getBDPrincipal().getBDUserRole()
				.getRoleType();
		// only broker and broker assistant can have message preferences
		switch (role) {
		case FinancialRep:
		case FinancialRepAssistant:
			form.setHasMessagePreference(true);
			break;
		default:
			form.setHasMessagePreference(false);
			break;
		}
		MyProfileContext context = MyProfileUtil.getContext(request
				.getServletContext(), request);
		context.getNavigation().setCurrentTabId(
				MyProfileNavigation.PreferenceTabId);
		return forwards.get(CANCEL_FORWARD);
	}
	
	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}
	

}
