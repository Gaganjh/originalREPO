package com.manulife.pension.ps.web.password;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.util.PasswordMeterUtility;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.EmployeeNumberDoesNotMatchException;
import com.manulife.pension.service.security.exception.FailedAlmostNTimesException;
import com.manulife.pension.service.security.exception.FailedNTimesException;
import com.manulife.pension.service.security.exception.IncorrectPasswordException;
import com.manulife.pension.service.security.exception.LockedUserException;
import com.manulife.pension.service.security.exception.SSNDoesNotMatchException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * @author Chris Shin
 * @version CS 1.0
 */
@Controller
@SessionAttributes({"changePasswordForm"})
@RequestMapping(value = "/password")
public class ChangePasswordController extends PsController {
	
	private UserInfo userInfo;
	@ModelAttribute("changePasswordForm")
	public ChangePasswordForm populateForm()
	{
		return new ChangePasswordForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	private static final String DEFAULT = "default";
    private static final String CONTINUE = "continue";
	private static final String SAVE = "save";
	private static final String SIGNOUT = "signout";
    private static final String CANCEL = "cancel";
    private static final String LOGIN_FLOW = "loginFlow";
    private static final String INPUT = "input";
    
    private static final String CONTINUEINT = "continueInt";
    private static final String CANCELINT = "cancelInt";
    private static final String DEFAULTINT = "defaultInt";
    private static final String SIGNOUTINT = "signoutInt";
    private static final String INPUTINT = "inputInt";
    
	static{
		forwards.put(INPUT,"/password/changePassword.jsp");
		forwards.put(CONTINUE,"redirect:/do/password/changePasswordConfirmation/");
		forwards.put(CANCEL,"redirect:/do/home/homePageFinder/");
		forwards.put(DEFAULT,"/password/changePassword.jsp");
		forwards.put(SIGNOUT,"redirect:/do/home/Signout/");
		
		forwards.put(CONTINUEINT,"redirect:/do/password/changePasswordConfirmationInternal/");
		forwards.put(CANCELINT,"redirect:/do/profiles/manageUsers/");
		forwards.put(DEFAULTINT,"/password/changePasswordInternal.jsp");
		forwards.put(SIGNOUTINT,"redirect:/do/home/Signout/");
		forwards.put(INPUTINT,"/password/changePasswordInternal.jsp");
		
		}

	
	/**
	 * Constructor.
	 */
	public ChangePasswordController() {
		super(ChangePasswordController.class);
	}
	
	
	/*
	 */
	@RequestMapping(value ="/changePassword/", method =  { RequestMethod.GET,RequestMethod.POST }) 
	public String doExecute(@Valid @ModelAttribute("changePasswordForm") ChangePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(INPUT);
			}
		}
		String forward = forwards.get(DEFAULT);
		
		String doExecute = "doExecute";
		
		String exceptionNotHandled = "Exception not handled ";
		
		// Changes for US 44837
		
		UserProfile userProfile = getUserProfile(request);
		
		userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());
		
		//boolean businessIndicator = SessionHelper.getBusinessParamFlag(request,userInfo);
		
		boolean changePasswordFlag = Boolean.FALSE;
		 
		if (null!= request.getSession(false) && 
				null != request.getSession(false).getAttribute(Constants.PASSWORD_CHANGE_NEW_USER)) {
				changePasswordFlag = (Boolean) request.getSession(false).getAttribute(Constants.PASSWORD_CHANGE_NEW_USER);
			}
		
		// End changes for US 44837
		
		Collection errors = new ArrayList<GenericException>();
		
		
        //if(!businessIndicator){
		     errors = doValidate(form,request);
       // }
        
        // End changes for US 44837

		if (!errors.isEmpty()) {
			  SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(INPUT);
		} 
    	
		
		
		
		if (!userProfile.isInternalUser() && !SecurityConstants.RESET_PASSWORD_STATUS.equals(userInfo.getPasswordState())) {
            request.getSession(false).removeAttribute("changePasswordForm");
			forward = forwards.get(SIGNOUT);
        } else if (form.getButton() == null && (!StringUtils.isEmpty(form.getUserFullName()) || !StringUtils.isEmpty(form.getEmailAddress()))) {
            if (!SecurityConstants.RESET_PASSWORD_STATUS.equals(userInfo.getPasswordState())) {
                request.getSession(false).removeAttribute("changePasswordForm");
                forward = forwards.get(CANCEL);
            }
		} else {
	    	if(StringUtils.isNotEmpty(request.getParameter(LOGIN_FLOW))){
	    		form.setLoginFlow(Boolean.TRUE);
	    	}
            populateForm(userInfo, form);

			if (form.getButton() != null) {
                if (form.getButton().equals(CONTINUE) || form.getButton().equals(SAVE)) {

                    if (userInfo.getRole() instanceof ExternalUser) {
                        userInfo.setSsn(form.getSsnValue());
                        userInfo.setEmployeeNumber(null);
                    } else {
                        userInfo.setSsn(null);
                    }

                    userInfo.setNewPassword(form.getNewPassword());
                    userInfo.setPassword(form.getOldPassword());

                    SecurityServiceDelegate delegate = SecurityServiceDelegate.getInstance();
                    

                    try {
                        delegate.changePassword(getUserProfile(request).getPrincipal(), userInfo, IPAddressUtils.getRemoteIpAddress(request));
                        
                        
                        // start changes for password US PSW
                        String userflow = "ChangePassword";
                        
                       delegate.updateDBTransactionPassword(userProfile.getPrincipal(),userInfo,userflow,
                    		   IPAddressUtils.getRemoteIpAddress(request),request.getSession(false).getId());
        				
            		   
        				
        			    // end changes for password US PSW
                        UserProfile user = getUserProfile(request);
                        user.setPasswordStatus(SecurityConstants.ACTIVE_PASSWORD_STATUS);
                        request.getSession(false).setAttribute(Constants.USERPROFILE_KEY, user);

                    } catch (SecurityServiceException e) {
                        errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
                        e.printStackTrace();
                        if (e instanceof IncorrectPasswordException) {
                            // nothing special
                        } else if (e instanceof FailedNTimesException) {
                            forward =forwards.get(SIGNOUT);
                        } else if (e instanceof FailedAlmostNTimesException) {
                            // nothing special
                        } else if (e instanceof LockedUserException) {
                            forward = forwards.get(SIGNOUT);
                        } else if (e instanceof DisabledUserException) {
                            forward = forwards.get(SIGNOUT);
                        } else if (e instanceof UserNotFoundException) {
                            forward =forwards.get(SIGNOUT);
                        } else if (e instanceof SSNDoesNotMatchException) {
                            // nothing special
                        } else if (e instanceof EmployeeNumberDoesNotMatchException) {
                            // nothing special

                        } else {
                            // unhandled exception
                            //       
                            throw new SystemException(e, "com.manulife.pension.ps.web.password.ChangePasswordAction", doExecute, exceptionNotHandled + e.toString());
                        }

                    }catch(RemoteException e){
                    	throw new SystemException(e, "com.manulife.pension.ps.web.password.ChangePasswordController", doExecute, exceptionNotHandled + e.toString());
                    }

                    if (errors == null || errors.size() == 0) {
                    	forward = forwards.get(CONTINUE);
                    } else {

                        SessionHelper.setErrorsInSession(request, errors);
                        form.setButton(null);
                    }
                } else if (form.getButton().equals(CANCEL)) {
                	ChangePasswordForm changePasswordForm1 =  (ChangePasswordForm)request.getSession(false).getAttribute("changePasswordForm");
                    request.getSession(false).removeAttribute("changePasswordForm");
                    ChangePasswordForm changePasswordForm2 =  (ChangePasswordForm)request.getSession(false).getAttribute("changePasswordForm");
                    if(form.isLoginFlow()) {
                    	forward = CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
                    }
                    else {
                    	forward = forwards.get(CANCEL);
                    }	
                    form.setButton("");
                }
            }
		}

		return forward;
	}
	
	@RequestMapping(value ="/changePasswordInternal/", method =   { RequestMethod.GET,RequestMethod.POST}) 
	public String doExecuteInternal(@Valid @ModelAttribute("changePasswordForm") ChangePasswordForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException, RemoteException {
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(INPUTINT);
			}
		}
		String forward = forwards.get(DEFAULTINT);
		
		String doExecute = "doExecute";
		
		String exceptionNotHandled = "Exception not handled";
		
		// Changes for US 44837
		
				
				
				UserProfile userProfile = getUserProfile(request); 
				
				userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
						userProfile.getPrincipal());
				
				 boolean changePasswordFlag = Boolean.FALSE;
				 
				if (null != request.getSession(false).getAttribute(Constants.PASSWORD_CHANGE_NEW_USER)) {
						changePasswordFlag = (Boolean) request.getSession(false).getAttribute(Constants.PASSWORD_CHANGE_NEW_USER);
					}
					
				//boolean businessIndicator = SessionHelper.getBusinessParamFlag(request, userInfo);
				
				Collection   errors = doValidate(form,request);
		        
		        // End changes for US 44837
		
		

		if (!errors.isEmpty() || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))) {
			  SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(INPUTINT);
		} 
    	
		 
		if (!userProfile.isInternalUser() && !SecurityConstants.RESET_PASSWORD_STATUS.equals(userInfo.getPasswordState())) {
            request.getSession(false).removeAttribute("changePasswordForm");
			forward = forwards.get(SIGNOUTINT);
        } else if (form.getButton() == null && (!StringUtils.isEmpty(form.getUserFullName()) || !StringUtils.isEmpty(form.getEmailAddress()))) {
            if (!SecurityConstants.RESET_PASSWORD_STATUS.equals(userInfo.getPasswordState())) {
                request.getSession(false).removeAttribute("changePasswordForm");
                forward = forwards.get(CANCELINT);
            }
		} else {
	    	if(StringUtils.isNotEmpty(request.getParameter(LOGIN_FLOW))){
	    		form.setLoginFlow(Boolean.TRUE);
	    	}
            populateForm(userInfo, form);

			if (form.getButton() != null) {
                if (form.getButton().equals(CONTINUE) || form.getButton().equals(SAVE)) {

                    if (userInfo.getRole() instanceof ExternalUser) {
                        userInfo.setSsn(form.getSsnValue());
                        userInfo.setEmployeeNumber(null);
                    } else {
                        userInfo.setSsn(null);
                    }

                    userInfo.setNewPassword(form.getNewPassword());
                    userInfo.setPassword(form.getOldPassword());

                    SecurityServiceDelegate delegate = SecurityServiceDelegate.getInstance();

                    try {
                        delegate.changePassword(getUserProfile(request).getPrincipal(), userInfo,IPAddressUtils.getRemoteIpAddress(request));
                     // start changes for password US PSW
                        String userflow = "ChangePasswordInternal";
            			
            			delegate.updateDBTransactionPassword(userProfile.getPrincipal(),userInfo,userflow,
                        		IPAddressUtils.getRemoteIpAddress(request),request.getSession(false).getId());
        				
            		   
        			    // end changes for password US PSW
                        UserProfile user = getUserProfile(request);
                        user.setPasswordStatus(SecurityConstants.ACTIVE_PASSWORD_STATUS);
                        request.getSession(false).setAttribute(Constants.USERPROFILE_KEY, user);

                    } catch (SecurityServiceException e) {
                        errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
                        e.printStackTrace();
                        if (e instanceof IncorrectPasswordException) {
                            // nothing special
                        } else if (e instanceof FailedNTimesException) {
                            forward =forwards.get(SIGNOUTINT);
                        } else if (e instanceof FailedAlmostNTimesException) {
                            // nothing special
                        } else if (e instanceof LockedUserException) {
                            forward = forwards.get(SIGNOUTINT);
                        } else if (e instanceof DisabledUserException) {
                            forward = forwards.get(SIGNOUTINT);
                        } else if (e instanceof UserNotFoundException) {
                            forward =forwards.get(SIGNOUTINT);
                        } else if (e instanceof SSNDoesNotMatchException) {
                            // nothing special
                        } else if (e instanceof EmployeeNumberDoesNotMatchException) {
                            // nothing special

                        } else {
                            // unhandled exception
                            //       
                            throw new SystemException(e, "com.manulife.pension.ps.web.password.ChangePasswordAction", doExecute, exceptionNotHandled + e.toString());
                        }

                    }

                    if (errors == null || errors.size() == 0) {
                    	forward = forwards.get(CONTINUEINT);
                    } else {

                        SessionHelper.setErrorsInSession(request, errors);
                        form.setButton(null);
                    }
                } else if (form.getButton().equals(CANCEL)) {
                    request.getSession(false).removeAttribute("changePasswordForm");
                    if(form.isLoginFlow()) {
                    	forward = CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
                    }
                    else {
                    	forward = forwards.get(CANCELINT);
                    }	
                    form.setButton("");
                }
            }
		}

		return forward;
	}
	
	private void populateForm(UserInfo userInfo, ChangePasswordForm form) {

		StringBuffer buf = new StringBuffer();
		buf.append(userInfo.getFirstName());
		buf.append(" ");
		buf.append(userInfo.getLastName());
		form.setUserFullName(buf.toString());
		form.setEmailAddress(userInfo.getEmail());
        form.setPasswordReset(SecurityConstants.RESET_PASSWORD_STATUS.equals(userInfo.getPasswordState()));

	}

	/**
	 * Validate the input action form.
	 *  
	 */
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);

	}
	
		
	  protected Collection doValidate( ActionForm form,HttpServletRequest request) {
			

			Collection errors = new ArrayList();
			ChangePasswordForm actionForm = (ChangePasswordForm) form;


			if (actionForm.getButton() != null
					&& (actionForm.getButton().equals(CONTINUE) || actionForm.getButton().equals(SAVE))) {

			if (getUserProfile(request).getRole() instanceof ExternalUser) {
				SsnRule.getInstance().validate(
					ChangePasswordForm.FIELD_SSN,errors, actionForm.getSsnValue());
			}
			
			// new password mandatory 
			if (actionForm.getNewPassword().length() == 0) {
				errors.add(new ValidationError(ChangePasswordForm.FIELD_NEW_PASSWORD ,ErrorCodes.PASSWORD_MANDATORY));
			}

				// confirm new password mandatory 
				if (actionForm.getConfirmPassword().length() == 0) {
					errors.add(new ValidationError(ChangePasswordForm.FIELD_CONFIRM_PASSWORD ,ErrorCodes.CONFIRM_PASSWORD_MANDATORY));
				}

				// old password mandatory 
				if (actionForm.getOldPassword().length() == 0) {
					errors.add(new ValidationError(ChangePasswordForm.FIELD_OLD_PASSWORD ,ErrorCodes.CURRENT_PASSWORD_MANDATORY));
				}
				
				Pair pair = new Pair(actionForm.getNewPassword(), actionForm
						.getConfirmPassword());

				// New password mandatory and standards must be met
//				NewPasswordRule.getInstance().validate(
//						ChangePasswordForm.FIELD_NEW_PASSWORD, errors, pair);
//				
				//TODO: see if you can encapsulate the following if needed to
				// be so
				
				
				if (actionForm.getNewPassword().length() > 0) {
					
					NewPasswordRule
							.getInstance()
							.validate(
									new String[] {
											ChangePasswordForm.FIELD_NEW_PASSWORD,
											ChangePasswordForm.FIELD_CONFIRM_PASSWORD },
									errors, pair);
					
					
					// changes for defect 8589 , 8590
					 if(null != errors && errors.isEmpty()) {
						 SecurityServiceDelegate serviceInstance = null;
				         String responseText = null;
					     serviceInstance = SecurityServiceDelegate.getInstance();
	                try{
	                	responseText = serviceInstance.passwordStrengthValidation(actionForm.getNewPassword(),
	                			userInfo.getUserName(),Boolean.FALSE);
	                	getPasswordScore(responseText,errors,request);
	                }catch(Exception e){
	                	if (logger.isDebugEnabled()) {
	                        logger.debug("exception occured while calling "
	                        		+ "passwordStrengthValidation service call" +e.getMessage());
	                    }
	                   }
					 }
					
					// end changes for defect 8589 , 8590

				}
				
			}

			return errors;
		}
	  
		@RequestMapping(value ="/changePassword/ajaxvalidator/" ,method =  {RequestMethod.POST})
		@ResponseBody
		public String doDeApiAjaxCall(@Valid @ModelAttribute("changePasswordForm") ChangePasswordForm form, BindingResult bindingResult,
	           HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException,SecurityServiceException, IOException{
			
	      //Password Meter DeApi Call
		  //Passing Boolean value as False for only FRW External Users to validate
			PasswordMeterUtility.validatePasswordMeter(request, response, userInfo.getUserName().toString(),Boolean.FALSE); 
			return null;
		}
		
	private void getPasswordScore(String responseText, Collection errors,HttpServletRequest request) {
		int score = 0;
		String deApiStatus = null;
		JsonElement jsonElement = null;
		final int passwordScore = 2;
		if (null != responseText) {
			jsonElement = new JsonParser().parse(responseText);
		}
		JsonObject jsonObject = null;
		if (null != jsonElement) {
			jsonObject = jsonElement.getAsJsonObject();
		}
		if (null != jsonObject && null != jsonObject.get("Deapi")) {
			deApiStatus = jsonObject.get("Deapi").getAsString();
		}

		if (null != deApiStatus && !deApiStatus.isEmpty() && deApiStatus.equalsIgnoreCase("down")) {
			request.getSession(false).setAttribute("Deapi", "down");
		} else {
			if (null != jsonObject && null != jsonObject.get("score")) {
				score = jsonObject.get("score").getAsInt();
			}
			request.getSession(false).setAttribute("Deapi", "up");
			if (score < passwordScore) {
				errors.add(new ValidationError(ChangePasswordForm.FIELD_NEW_PASSWORD,
						CommonErrorCodes.PASSWORD_FAILS_STANDARDS));
			}
		}
	}
	   
}