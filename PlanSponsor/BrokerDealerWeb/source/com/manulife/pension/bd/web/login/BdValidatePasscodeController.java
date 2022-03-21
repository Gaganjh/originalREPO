package com.manulife.pension.bd.web.login;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.FrwValidation;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.LoginStatus;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage;
import com.manulife.pension.platform.web.passcode.PasscodeForm;
import com.manulife.pension.platform.web.passcode.ValidatePasscodeController;
import com.manulife.pension.service.security.bd.valueobject.BDLoginValueObject;

@Controller
@RequestMapping( value ="/stepupPasscode")
public class BdValidatePasscodeController extends ValidatePasscodeController<BDLoginValueObject> {

	@ModelAttribute("passcodeForm")
	public PasscodeForm populateForm()
	{
		return new PasscodeForm();
		}
	
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
		forwards.put("default","/home/passcode.jsp");
		forwards.put("success","redirect:/do/postLogin");
		forwards.put("retry","/home/passcode.jsp");
		forwards.put("failure","/home/public_home.jsp");
		forwards.put("expired","/home/passcode.jsp");
		forwards.put("locked","/home/public_home.jsp");
		forwards.put("cooling","/home/passcode.jsp");
		forwards.put("cancel","/home/public_home.jsp");
		forwards.put("resend","/home/passcode.jsp");
		forwards.put("updatePassword", "redirect:/do/updatePassword/");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");
		forwards.put("home", "redirect:/do/home/");
	}
	

	/*
	public BdValidatePasscodeAction(Class<?> clazz, String appIdForAppLog, ValidateCatalogLaunch injectionValidation,
			PasscodeVerification<BDLoginValueObject> verification,
			AuthenticatedSessionInitialization<BDLoginValueObject> initialization, PasscodeErrorMap errorMap) {
		super(clazz, appIdForAppLog, injectionValidation, verification, initialization, errorMap);
		// TODO Auto-generated constructor stub
	}*/
    
    public BdValidatePasscodeController() {
        super(
                BdValidatePasscodeController.class,
                BDConstants.BD_APPLICATION_ID,
                FrwValidation.getInstance(),
                BdPasscodeVerification.INSTANCE,
                BdAuthenticatedSessionInitialization.INSTANCE,
                new PasscodeErrorMessage.PasscodeErrorMap.Builder()
                .add(PasscodeErrorMessage.RETRY, 8121)
                .add(PasscodeErrorMessage.LOCKED, 8122)
                .add(PasscodeErrorMessage.COOLING, 8123)
                .add(PasscodeErrorMessage.EXPIRED, 8120)
                .add(PasscodeErrorMessage.BLANK_PASSCODE, 8124)
                .add(PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN, 8138)
                .add(PasscodeErrorMessage.SYSTEM_ERROR_AT_RESEND, 8126)
                .build());
    }
    
    @RequestMapping(value ="/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doExecute (@Valid @ModelAttribute("passcodeForm") PasscodeForm passcodeForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    		
    		String challengeRequest = (String)request.getSession().getAttribute("challengeRequestFrom");    	
    		String currentTab = (String)request.getSession().getAttribute("myProfileCurrentTab");
    		String action =  getSubmitAction(request, passcodeForm);


		// Defect 8711: Forward to home page when user clicks on cancel button on Passcode Validate page when he is 
    	// challenged on OTP at Manage my profile.	
		if ("Cancel".equals(action) && null != challengeRequest
				&& challengeRequest.equalsIgnoreCase("myprofileDispatch")) {
			request.getSession().setAttribute("challengePasscodeInd", true);
			return forwards.get("home");
		}

    		String forward = super.doExecute(passcodeForm, request, response);
    		
    		if (forward.equals("updatePassword")) {
    			BDUserProfile bdUserProfile = BDSessionHelper.getUserProfile(request);
    			bdUserProfile.setLoginStatus(LoginStatus.UpdatePassword);
    			return forwards.get(forward);
    		}
    		
    		if("success".equals(forward) && "myprofileDispatch".equals(challengeRequest)){
    			request.getSession().setAttribute("challengePasscodeInd", false);
    			request.getSession().setAttribute("myProfileCurrentTab",currentTab);
    			BDUserProfile bdUserProfile = BDSessionHelper.getUserProfile(request);
    			bdUserProfile.setLoginStatus(LoginStatus.FullyLogin); //This is to authorize the request before redirecting to the edit my profile page again.
    			
    			return forwards.get("myprofileDispatch");
    		}
    		
	       return forwards.get(forward);

}
    
}
