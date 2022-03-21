package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWDefault;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.service.security.passcode.PasscodeSecurity;
import com.manulife.pension.util.IPAddressUtils;

/**
 * Dispatch to the first available tab
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value ="/myprofileDispatch")

public class MyProfileDispatchController extends BDController {
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/home/home.jsp");
		forwards.put("challengePasscode", "redirect:/do/stepupTransition/");
		forwards.put("brokerPersonalInfo", "/do/myprofile/brokerPersonalInfo/");
		forwards.put("securityInfo", "/do/myprofile/security/");
		forwards.put("assistants", "/do/myprofile/assistants/");
		forwards.put("license", "/do/myprofile/license/");
		forwards.put("preference", "/do/myprofile/preference/");
		forwards.put("addBOB", "/do/myprofile/addBOB/");
		forwards.put("createBOB", "/do/myprofile/createBOB/");
		forwards.put("personalInfo", "/do/myprofile/personalInfo/");
		
		}

	public MyProfileDispatchController() {
		super(MyProfileDispatchController.class);
	}
	@RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST })
	   	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,BindingResult bindingResult,
	   			HttpServletRequest request, HttpServletResponse response)
	   					throws IOException, ServletException, SystemException {
		 if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		              return forwards.get("default");
		       }
			}
		MyProfileContext context = MyProfileUtil.getContext(request
				.getServletContext(), request);
		List<UserMenuItem> tabs = context.getNavigation().getTabs();
		if (tabs.size() == 0) {
			// no my profile tab is accessible
			ControllerRedirect f=	 new ControllerRedirect(URLConstants.HomeURL);
		return	"redirect:"+f.getPath();
		} else {
			ControllerRedirect b= new  ControllerRedirect(tabs.get(0).getActionURL());
			if(Objects.nonNull( request.getSession(false).getAttribute("myProfileCurrentTab") )){
				String redirectFrom = (String)request.getSession(false).getAttribute("myProfileCurrentTab");
				b=new ControllerRedirect(forwards.get(redirectFrom));
			}
			HttpSession session = request.getSession(false);
			if (session != null && session.getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND) != null ) {
				boolean challengeUserInd = (boolean) session.getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND);

				BDUserProfile profile = BDSessionHelper.getUserProfile(request);
				if(Objects.nonNull(profile)) {
					if(challengeUserInd && !isUserExempted(profile.getBDPrincipal().getProfileId(), IPAddressUtils.getRemoteIpAddress(request))) {
						request.getSession().setAttribute(BDConstants.IS_TRANSITION, true);
						request.getSession().setAttribute("challengeRequestFrom", "myprofileDispatch");
		
						return forwards.get("challengePasscode");
					}
				}	
			}		
			request.getSession(false).removeAttribute("myProfileCurrentTab");
			return "redirect:"+b.getPath();
		}
	}
	 @Autowired
	   private BDValidatorFWDefault  bdValidatorFWDefault;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWDefault);
	}
	 
	 private boolean isUserExempted(final long profileId, final String ipAddress) {
		 return PasscodeSecurity.BD.isExemptUserProfile(profileId, ipAddress);
	 }
}
