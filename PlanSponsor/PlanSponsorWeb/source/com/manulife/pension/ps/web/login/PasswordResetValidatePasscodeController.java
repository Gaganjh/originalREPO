package com.manulife.pension.ps.web.login;

import java.io.IOException;
import java.util.HashMap;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage;
import com.manulife.pension.platform.web.passcode.PasscodeForm;
import com.manulife.pension.platform.web.passcode.ValidatePasscodeController;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;
import com.manulife.pension.service.security.valueobject.LoginPSValueObject;

@Controller
@RequestMapping( value ="/login/forgotPasswordPasscodeValidation")
@SessionAttributes({"passwordResetAuthenticationForm"})

public class PasswordResetValidatePasscodeController extends ValidatePasscodeController<LoginPSValueObject> {
	@ModelAttribute("passwordResetAuthenticationForm")
	public PasswordResetAuthenticationForm populateForm()
	{
		return new PasswordResetAuthenticationForm();
		}
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
		forwards.put("default" ,"/login/forgotPasswordPasscode.jsp");
		forwards.put("success" ,"redirect:/do/login/passwordReset/");
		forwards.put("phoneCollection" ,"redirect:/do/phoneCollection/");
		forwards.put("retry" ,"/login/forgotPasswordPasscode.jsp");
		forwards.put("failure" ,"/login/login.jsp");
		forwards.put("expired" ,"/login/forgotPasswordPasscode.jsp");
		forwards.put("locked" ,"/login/login.jsp");
		forwards.put("cooling" ,"/login/forgotPasswordPasscode.jsp");
		forwards.put("cancel" ,"/login/login.jsp");
		forwards.put("resend" ,"/login/forgotPasswordPasscode.jsp");
		forwards.put("expired_sms" ,"/login/forgotPasswordPasscode.jsp");
		forwards.put("retry_sms" ,"/login/forgotPasswordPasscode.jsp");
		forwards.put("sms_switch_on" ,"/login/login.jsp");
	    
			}
	 public PasswordResetValidatePasscodeController() {
	        super(
	                PasswordResetValidatePasscodeController.class,
	                Constants.PS_APPLICATION_ID,
	                PsValidator1.getInstance(),
	                PsPasscodeVerification.INSTANCE,
	                PsAuthenticatedSessionInitialization.INSTANCE,
	                new PasscodeErrorMessage.PasscodeErrorMap.Builder()
	                .add(PasscodeErrorMessage.RETRY, 3551)
	                .add(PasscodeErrorMessage.LOCKED, 3552, 3555)
	                .add(PasscodeErrorMessage.COOLING, 3553, 3556)
	                .add(PasscodeErrorMessage.EXPIRED, 3550)
	                .add(PasscodeErrorMessage.BLANK_PASSCODE, 3554)
	                .add(PasscodeErrorMessage.SYSTEM_ERROR_AT_LOGIN, 3557, 3559)
	                .add(PasscodeErrorMessage.SYSTEM_ERROR_AT_RESEND, 3558, 3560)
	                .add(PasscodeErrorMessage.EXPIRED_SMS, 8142)
	                .add(PasscodeErrorMessage.RETRY_SMS, 8143)
	                .add(PasscodeErrorMessage.SMS_SWITCH_ON, 8140)
	                .build());

	    }
	
	@RequestMapping(value ="/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("passwordResetAuthenticationForm") PasswordResetAuthenticationForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException, SystemException {
		
		PasscodeForm passcodeForm = new PasscodeForm();
		
		passcodeForm.setPasscode(form.getPasscode());
		
		final String action = super.doExecute(passcodeForm, request, response);
		
		if (action != "success") {
			return forwards.get(action);
		}
		
		if("success".equalsIgnoreCase(action))
			BaseSessionHelper.invalidateSessionKeepCookie(request);
		
		request.getSession().setAttribute(Constants.CHALLENGE_PASSCODE_IND, false);
		return forwards.get(action);

	}
	
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	} 
}