/**
 * 
 */
package com.manulife.pension.ps.web.psr;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWPasswordResetConfirmation;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.psr.valueobject.PasswordResetEmailVO;

/**
 * Responsible for Reset Password Confirmation Page handling .
 * @author gazulra
 *
 */
@Controller
@RequestMapping( value ="/pwdemail")

public class PasswordResetConfirmController extends PsAutoController{
	
	@ModelAttribute("passwordResetForm") 
	public PasswordResetForm populateForm()
	{
		return new PasswordResetForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("resetPasswordConfirmation","/pwdemail/resetPptPasswordConfirmation.jsp");
		forwards.put("censusSummary","redirect:/do/census/censusSummary/");
		}

	
	/**
	 * Constructor
	 */
	public PasswordResetConfirmController() {
		super(PasswordResetConfirmController.class);
	}

	/**
     * doDefault is for the default access of the action 
	 * @param actionForm
	 * @param request
	 * @param response		
     *
     * @throws IOException, ServletException, SystemException
     * @return ActionForward
     */
	@RequestMapping(value ="/ResetPasswordConfirm/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("passwordResetForm") PasswordResetForm passwordResetForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("resetPasswordConfirmation");//if input forward not //available, provided default
	       }
		}
	
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> PasswordResetConfirmAction -> doDefault");
		}
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);		
		String profileId = passwordResetForm.getProfileId();
		
		Employee employee = new EmployeeServiceFacade().getEmployee(Long.parseLong(profileId), userProfile, new Date(), false);
		
		String dateString  = null;
		if (employee != null && employee.getEmployeeDetailVO() != null) {
			passwordResetForm.setFirstName(employee.getEmployeeDetailVO().getFirstName());
			passwordResetForm.setLastName(employee.getEmployeeDetailVO().getLastName());
			passwordResetForm.setEmployerProvidedEmailAddress(employee.getEmployeeDetailVO().getEmployerProvidedEmailAddress()); 
			
			PasswordResetEmailVO passwordResetEmailVO = SecurityServiceDelegate.getInstance().selectActiveEmpEmailPwdResetRequest(employee.getEmployeeDetailVO().getContractId(), new BigDecimal(employee.getEmployeeDetailVO().getProfileId().longValue()));
			if(passwordResetEmailVO != null){
				SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");//displays as Jul 25, 2014 01:58 PM 
				dateString = format.format(passwordResetEmailVO.getRequestedTs());
				passwordResetForm.setRequestId(passwordResetEmailVO.getRequestId());
				passwordResetForm.setRequestedTs(dateString);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exits -> PasswordResetConfirmAction -> doDefault");
		}
		
		return forwards.get(Constants.RESET_PWD_CONFIRMATION);
	}
	
	/**
     * doFinish is to return back to Employee Snapshot page
	 * @param actionForm
	 * @param request
	 * @param response		
     *
     * @throws IOException, ServletException, SystemException
     * @return ActionForward
     */
	@RequestMapping(value ="/ResetPasswordConfirm/", params={"action=finish"}, method ={RequestMethod.POST}) 
	public String doFinish(@Valid @ModelAttribute("passwordResetForm") PasswordResetForm PasswordResetForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("resetPasswordConfirmation");//if input forward not //available, provided default
	       }
		}
		return forwards.get(PasswordResetForm.getSource());
	}
	
	
	
	@Autowired
	private PSValidatorFWPasswordResetConfirmation psValidatorFWPasswordResetConfirmation;

	@InitBinder
	public void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWPasswordResetConfirmation);
	}
}
