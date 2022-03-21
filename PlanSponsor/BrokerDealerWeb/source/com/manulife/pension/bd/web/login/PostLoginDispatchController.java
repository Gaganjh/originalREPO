package com.manulife.pension.bd.web.login;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.LoginStatus;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWError;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.BDUserPasswordStatus;
import com.manulife.pension.service.security.bd.dao.BDProfileDao;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequest.Type;
import com.manulife.pension.service.security.bd.valueobject.BDUserProfileValueObject;

/**
 * A dispatch action to handle after successful login redirect It will check the
 * condition in UserProfile and set the LoginStatus and find the correct
 * redirect
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value ="/postLogin")

public class PostLoginDispatchController extends BDController {
	
	
	
	private static final String CHANGEPASSWORDFLAG = "changePasswordFlag";
	
public static HashMap<String,String> forwards = new HashMap<String,String>();
static{
	forwards.put("error","/password/changeTempPassword.jsp" );
	}

	
	
	private static ControllerRedirect HomeRedirect = new ControllerRedirect(
			URLConstants.HomeURL);

	
	
	private static final ControllerRedirect ChangeTmpPasswordRedirect = new ControllerRedirect(
			URLConstants.ChangeTempPasswordURL);

	private static final ControllerRedirect InternalMyProfileRedirect = new ControllerRedirect(
			URLConstants.MyProfileInternal);

	private static final ControllerRedirect BrokerComfirmationRedirect = new ControllerRedirect(
			URLConstants.MyProfileBrokerPersonalTab + "?activation=y");

	
	
	
	
	public PostLoginDispatchController() {
		super(PostLoginDispatchController.class);
	}
	 @RequestMapping(  method =  {RequestMethod.POST,RequestMethod.GET}) 
		public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return forwards.get("error");//if input forward not //available, provided default
			 }
		 }
	
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		if (profile == null) {
			return "redirect:/do/home/";
		}
		
		boolean changePasswordFlag = Boolean.FALSE;
		if(null != request.getSession(false) && 
				null != request.getSession(false).getAttribute(CHANGEPASSWORDFLAG)){
			changePasswordFlag = (Boolean)request.getSession(false).getAttribute(CHANGEPASSWORDFLAG);
		}	
		
		
		String businessParamIndicator = null;
		if(null != request.getSession(false) && 
				null != request.getSession(false).getAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND)){
			businessParamIndicator = (String)request.getSession(false).getAttribute(BDConstants.FRW_SHOW_PASSWORD_METER_IND);
		}
		
		BDUserProfileValueObject profileVO = BDProfileDao.getInstance()
				.getBDUserByProfileId(Long.valueOf(profile.getBDPrincipal().getProfileId()));
		
		boolean isExternalUser = BDSessionHelper.isExternalUser(profileVO);
		
		request.getSession(false).setAttribute(BDConstants.IS_EXTERNAL_USER, isExternalUser);
	
		
		boolean isInternalUser = BDSessionHelper.isInternalUser(profileVO);
		
		request.getSession(false).setAttribute(BDConstants.IS_INTERNAL_USER, isInternalUser);
		
		boolean showUpdatePasswordPage = Boolean.FALSE;
		if(null != businessParamIndicator){
		 showUpdatePasswordPage = (changePasswordFlag && "INT".equals(businessParamIndicator) && isInternalUser) 
		|| (changePasswordFlag && "EXT".equals(businessParamIndicator) && isExternalUser)
		|| (changePasswordFlag && "ALL".equals(businessParamIndicator));
		}
		
		setLoginStatus(profile, showUpdatePasswordPage);
		if(isExternalUser) {
			request.getSession().setAttribute(BDConstants.USERID_KEY, profile.getBDPrincipal().getUserName());
		}
		
		if (LoginStatus.ResetPassword.equals(profile.getLoginStatus())) {
			return "redirect:"+URLConstants.ChangeTempPasswordURL;
		}
		
		if (showUpdatePasswordPage){
			request.getSession(false).removeAttribute(CHANGEPASSWORDFLAG);
			return "redirect:"+URLConstants.UpdatePasswordURL;
		}
		
		BDUserRoleType roleType = profile.getBDPrincipal().getBDUserRole()
				.getRoleType();
		
		// For direct URL's
		String nextPage = request.getParameter("nextURL");
		
		if (roleType.compareTo(BDUserRoleType.FinancialRep) == 0) {
			Type activationType = profile.getActivationType();
			if (activationType != null
					&& (activationType.compareTo(Type.UserActivation) == 0 || activationType
							.compareTo(Type.UserPartyActivation) == 0)) {
				return "redirect:"+URLConstants.MyProfileBrokerPersonalTab + "?activation=y";
			}
		} else if (profile.getBDPrincipal().getProducerLicense() == null
				&& roleType.isInternal()
				&& roleType.compareTo(BDUserRoleType.CAR) != 0
				&& roleType.compareTo(BDUserRoleType.SuperCAR) != 0) {

			HttpSession session =request.getSession();
			String url=(String)session.getAttribute(BDConstants.DIRECT_URL_ATTR);
			if(null == url || !url.contains("/fundcheck/fundCheckInternal")){
				return "redirect:"+URLConstants.MyProfileInternal;
			}
		} else if ( nextPage != null ){
			return (nextPage);
		}
		return "redirect:/do/home/";
	}

	/**
	 * Set the login status based on various check 1. reset password status 2.
	 * ...
	 * 
	 * @param profile
	 * @param showUpdatePasswordPage 
	 */
	private void setLoginStatus(BDUserProfile profile, boolean showUpdatePasswordPage) {
		if (BDUserPasswordStatus.Reset.compareTo(profile.getPasswordStatus()) == 0) {
			profile.setLoginStatus(LoginStatus.ResetPassword);
		} 
		else if (showUpdatePasswordPage) {
			profile.setLoginStatus(LoginStatus.UpdatePassword);
		}else {
			profile.setLoginStatus(LoginStatus.FullyLogin);
		}
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	@Autowired
	   private BDValidatorFWError  bdValidatorFWError;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWError);
}
}
