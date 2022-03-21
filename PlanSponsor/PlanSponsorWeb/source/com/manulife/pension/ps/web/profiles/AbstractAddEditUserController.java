package com.manulife.pension.ps.web.profiles;

import static com.manulife.pension.ps.web.Constants.FORWARD_TPA_CONTACTS;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.platform.web.validation.rules.PrimaryEmailRule;
import com.manulife.pension.platform.web.validation.rules.SecondaryEmailRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.bd.UserPartyStatus;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNameIsInUseException;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.PilotCAR;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.transaction.UserNameIsNotUniqueException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the base class of all add/edit/view user actions.
 * 
 * @author Charles Chan
 */
public abstract class AbstractAddEditUserController extends PsAutoController {

    private static final String ADD_PARAM = "add";

    private static final String VIEW_PARAM = "view";
    
    private static final String EDIT_PARAM = "edit";

    private static final String CONFIRMATION = "confirm";

    private static final String CONFIRMATION_PAGE = "confirmPage";

    protected static final String MANAGE_USERS = "manageUsers";

    protected static final String REFRESH = "refresh";

    protected static final String CHANGE_PERMISSIONS = "changePermissions";

    protected static final String FLOW_TOKEN = "flowToken";

    protected static final String EDIT_USER = "editUser";

    protected static final String DEFAULT_EMAIL_NEWSLETTER = Constants.YES;
    
    protected Map securityExceptionFormFieldMap = new HashMap();

    /**
     * Constructor.
     * 
     * @param clazz
     */
    public AbstractAddEditUserController(Class clazz) {
        super(clazz);
        securityExceptionFormFieldMap.put(UserNameIsInUseException.class,
                AddEditUserForm.FIELD_USER_NAME);
        securityExceptionFormFieldMap.put(UserNameIsNotUniqueException.class,
                AddEditUserForm.FIELD_USER_NAME);
    }

    /**
     * Populates a UserInfo object with the given action form.
     * 
     * @param userInfo The user info object to populate.
     * @param actionForm The action form to obtain values from.
     * @return
     */
    protected abstract void populateUserInfo(HttpServletRequest request, UserInfo userInfo,
            AddEditUserForm actionForm);

    /**
     * Populates an action form with the given user info.
     * 
     * @param actionForm The action form to populate.
     * @param userInfo The user info object to obtain values from.
     */
    protected abstract void populateForm(HttpServletRequest request,
            AddEditUserForm actionForm, UserInfo userInfo) throws SystemException;

    /**
     * Creates an arbitrary principal object.
     * 
     * @param profileId
     * @param userName
     * @param role
     * @return A newly created principal object or null if any exception occurs.
     */
    public static Principal newPrincipal(Long profileId, String userName, UserRole role) {
        Constructor theConstructor = Principal.class.getDeclaredConstructors()[0];
        theConstructor.setAccessible(true);
        try {
            return (Principal) theConstructor.newInstance(new Object[] { profileId, role, userName,
                    null, null });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Populates the common attributes of a user info. These includes user name, first name, last
     * name, and email address.
     * 
     * @param actionForm
     * @return
     */
    private void populateCommonUserInfo(UserInfo userInfo, AutoForm actionForm) {
        AddEditUserForm form = (AddEditUserForm) actionForm;
        userInfo.setProfileId(form.getProfileId());
        userInfo.setUserName(form.getUserName());
        userInfo.setEmail(form.getEmail());
        userInfo.setSecondaryEmail(form.getSecondaryEmail());
        userInfo.setFirstName(form.getFirstName());
        userInfo.setLastName(form.getLastName());
        userInfo.setUpdatedInformation(form.getChanges().toString());
        userInfo.setModifications(form.getChanges().getModifications());
        
        if (logger.isDebugEnabled()) {
            logger.debug(userInfo.getUpdatedInformation());
        }
    }

    /**
     * Populates the given action form with the specified UserInfo object.
     * 
     * @param form The action form to populate.
     * @param userInfo The user info object to obtain data from.
     */
    private void populateCommonForm(AddEditUserForm form, UserInfo userInfo) {
        form.setProfileId(userInfo.getProfileId());
        form.setUserName(userInfo.getUserName());
        form.setFirstName(userInfo.getFirstName());
        form.setLastName(userInfo.getLastName());
        form.setEmail(userInfo.getEmail());
        form.setSecondaryEmail(userInfo.getSecondaryEmail());
        form.setProfileStatus(userInfo.getProfileStatus());
        // form.setEmailNewsletter(userInfo.getEmailNewsletter());
    }

    /**
     * Checks whether the current action invoked is an add action.
     * 
     * @param form The current action form.
     * @return true if the current action is an add action, false if otherwise.
     */
    static boolean isAddUser(String mapping) {
        //return mapping.getParameter().equals(ADD_PARAM);
    	return mapping.equals(ADD_PARAM);
    }

    /**
     * Checks whether the current action invoked is an view action.
     * 
     * @param form The current action form.
     * @return true if the current action is an view action, false if otherwise.
     */
    static boolean isViewUser(String mapping) {
        //return mapping.getParameter().equals(VIEW_PARAM);
    	return mapping.equals(VIEW_PARAM);
    }

    /**
     * Checks whether the current action invoked is an edit action.
     * 
     * @param form The current action form.
     * @return true if the current action is an edit action, false if otherwise.
     */
    static boolean isEditUser(String mapping) {
        //return mapping.getParameter().equals(EDIT_PARAM);
    	return mapping.equals(EDIT_PARAM);
    }
    
    /**
     * Checks whether we're in the right state.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     */
    protected String validate( ActionForm actionForm,ModelMap model,
            HttpServletRequest request) {

        UserProfile userProfile = getUserProfile(request);
       String mapping=(String) model.get("mapping");
        //Contact Management Changes for Login Pages - While Internal User Manager Adds or Edits the User Profile the contract can be null
        AddEditUserForm form = (AddEditUserForm) actionForm;
        if (form == null) {
            //return mapping.findForward(MANAGE_USERS);
        	return MANAGE_USERS;
        }

        Collection errors = doValidate( form, request);

        /*
         * Errors are stored in the session so that our REDIRECT can look up the errors.
         */
        if (!errors.isEmpty()) {
           // SessionHelper.setErrorsInSession(request, errors);
            request.getSession(false).setAttribute(FLOW_TOKEN, "addEdit");
            //return mapping.findForward(REFRESH);
            return REFRESH;
        }
        /*
         * If we're not adding a user and the username is empty, we should forward back to the
         * Manage Users page. Notice that this is possible when user hit back button on the
         * confirmation page.
         */

        if (!isAddUser(mapping) || form.isConfirmAction()) {
            if (form.getUserName() == null || form.getUserName().length() == 0) {
                //return mapping.findForward(MANAGE_USERS);
            	return MANAGE_USERS;
            }
        }

        /*
         * If this is a save action, we should compare the token and make sure it's still valid.
         * Token is initialized in the doDefault() method and reset in the doSave() method.
         */
        if (form.isSaveAction()) {
           /* if (!isTokenValid(request)) {
                return  forwards.get(MANAGE_USERS);
            }
*/        }

        

        return null;
    }

    /**
     * Perform validation that is common to all add/edit user actions. Specifically, this method
     * validates first name, last name, and email address.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#doValidate(org.apache.struts.action.Form,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected Collection doValidate( ActionForm actionForm,
            HttpServletRequest request) {

        Collection errors = new ArrayList();

        AddEditUserForm form = (AddEditUserForm) actionForm;

        /*
         * Validate the action form only when we save.
         */
        if (form.isSaveAction()) {
            NameRule.getFirstNameInstance().validate(AddEditUserForm.FIELD_FIRST_NAME,
                    errors, form.getFirstName());
            NameRule.getLastNameInstance().validate(AddEditUserForm.FIELD_LAST_NAME, errors,
                    form.getLastName());
            
            if(form.isWebAccess() || form.getEmail().length() > 0){
            	// Email field is mandatory only when web access is set to Yes
            	PrimaryEmailRule.getInstance().validate(AddEditUserForm.FIELD_EMAIL, errors,
            			form.getEmail());
            }
            
            if(form.isWebAccess() && form.getSecondaryEmail() != null && form.getSecondaryEmail().length() > 0){
            	// Email field is mandatory only when web access is set to Yes
            	SecondaryEmailRule.getInstance().validate(AddEditUserForm.FIELD_SECONDARY_EMAIL, errors,
            			form.getSecondaryEmail());
            }
            if (!form.getChanges().isChanged()) {
                GenericException ex = new GenericException(ErrorCodes.SAVING_WITH_NO_CHANGES);
                errors.add(ex);
            }
        }

        return errors;
    }

    /**
     * Cancel the current add/edit operation. The form in the session is cleared.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     */
    public String doCancel( AutoForm actionForm,ModelMap model,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        clearSession( model,request);
        AddEditUserForm form = (AddEditUserForm)actionForm;
        
        // if user navigates through TPA Contacts tab, then redirect to TPA contacts tab
        if(form.isFromTPAContactsTab()){
        	//return mapping.findForward(FORWARD_TPA_CONTACTS);
        	return FORWARD_TPA_CONTACTS;
        }
        //return mapping.findForward(MANAGE_USERS);
        return MANAGE_USERS;
    }

    /**
     * Saves the user input form.
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
    public String doSave(AutoForm actionForm,ModelMap model,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {

        AddEditUserForm form = (AddEditUserForm) actionForm;
      //TODO-Adding string variable mapping to avoid error(check with previos code)
        String mapping=(String) model.get("mapping");
        if (logger.isDebugEnabled()) {
            logger.debug("entry --> doSave");
            logger.debug(form);
        }

        //ActionForward forward = null;
        String forward=null;

        UserProfile userProfile = getUserProfile(request);
        Principal principal = userProfile.getPrincipal();

        UserInfo userInfo = new UserInfo();
        populateCommonUserInfo(userInfo, form);
        populateUserInfo(request, userInfo, form);

        // if (logger.isDebugEnabled()) {
        // logger.debug("\nNew User's Info:");
        // logger.debug("\n" + StaticHelperClass.toXML(userInfo));
        // logger.debug("\nFrom Form:");
        // logger.debug("\n" + StaticHelperClass.toXML(form));
        // }
        String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
        userInfo.setIpAddress(ipAddress);
        SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();

        try {
            UserInfo newUserInfo = null;
			UserInfo oldUserInfo = null;
			
            if (isAddUser(mapping)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("addUser");
                }
                if (RelationshipManager.ID.equalsIgnoreCase(form.getPlanSponsorSiteRole())&& StringUtils.isNotEmpty(form.getRmId())) {
                	if(StringUtils.isNotBlank(form.getRmId()) && !"UA".equals(form.getRmId())){
                	  service.validateUserParty(form.getRmId());
                	}
                }
                newUserInfo = service.addUser(principal, userInfo, Environment.getInstance()
                        .getSiteLocation());
                form.setRmIdSaved(form.getRmId()); 
                form.setUserName(newUserInfo.getUserName());
				if (RelationshipManager.ID.equalsIgnoreCase(form.getPlanSponsorSiteRole())&& StringUtils.isNotEmpty(form.getRmId())) {
					if(StringUtils.isNotBlank(form.getRmId()) && !"UA".equals(form.getRmId())){
						service.validateUserParty(form.getRmId());
						SecurityServiceDelegate.getInstance().addUserParty(newUserInfo.getProfileId(), Long.parseLong(form.getRmId()),
							UserPartyStatus.Active, principal.getProfileId());
					}
				  }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("updateUser");
                }
                oldUserInfo = SecurityServiceDelegate.getInstance()
						.searchByUserName(userProfile.getPrincipal(),
								userInfo.getUserName());
                
				if (principal.getRole() instanceof PilotCAR) {
					oldUserInfo.setPhoneNumber(form.getPhone());
					oldUserInfo.setPhoneExtension(form.getExt());
					service.updateUser(principal, Environment.getInstance().getSiteLocation(), oldUserInfo);
					forward = CONFIRMATION;
					return forward;

				}
                
                if (RelationshipManager.ID.equalsIgnoreCase(form.getPlanSponsorSiteRole())&& StringUtils.isNotEmpty(form.getRmId())) {
                	if((StringUtils.isNotBlank(form.getRmIdSaved()) && !form.getRmIdSaved().equals(form.getRmId())) && StringUtils.isNotBlank(form.getRmId()) && !"UA".equals(form.getRmId())){
                	service.validateUserParty(form.getRmId());
                	}
                }
                
                service
                        .updateUser(principal, Environment.getInstance().getSiteLocation(),
                                userInfo);
	            if(StringUtils.isNotBlank(form.getRmId()) && !"UA".equals(form.getRmId())){
	               //service.validateUserParty(form.getRmId());
	              	if( StringUtils.isNotBlank(form.getRmIdSaved()) && !"UA".equals(form.getRmIdSaved())){
	              		SecurityServiceDelegate.getInstance().updateUserParty(oldUserInfo.getProfileId(), Long.valueOf(form.getRmIdSaved()), principal.getProfileId());
		            }
		            SecurityServiceDelegate.getInstance().addUserParty(oldUserInfo.getProfileId(), Long.parseLong(form.getRmId()),
								UserPartyStatus.Active, principal.getProfileId());
	            }else if(StringUtils.isNotBlank(form.getRmId()) && "UA".equals(form.getRmId())){
	                if( StringUtils.isNotBlank(form.getRmIdSaved()) && !"UA".equals(form.getRmIdSaved())){
	            	   SecurityServiceDelegate.getInstance().updateUserParty(oldUserInfo.getProfileId(), Long.valueOf(form.getRmIdSaved()), principal.getProfileId());
	            	}
	            }
             }

            // If some of this stuff is needed in the end, at least it belongs in
            // AbstractTpaUserAction
            // /*
            // * Checking for plansponsor user payroll path option
            // * turned from "YES" to "NO"
            // */
            // boolean lastPayrollEmailValue=false;
            // boolean lastTPAUserPayrollEmailValue = false;
            // boolean isPayrollEnabledFirm = false;
            // TPAFirmInfo firminfo = TPAServiceDelegate.getInstance()
            // .getFirmInfoByContractId(userProfile.getCurrentContract().getContractNumber());
            // isPayrollEnabledFirm=firminfo.getContractPermission().isPayrollEmail();
            //				
            // String user_Name="";
            // user_Name = userInfo.getUserName();
            // long profile_id = userInfo.getProfileId();
            //									
            // /*
            // * Checking for TPA user payroll path option
            // * turned from "YES" to "NO"
            // */
            // for (Iterator it = form.getTpaFirms().iterator(); it.hasNext();) {
            // TpaFirm tpaFirmForm = (TpaFirm) it.next();
            // Iterator ait = tpaFirmForm.getContractAccesses().iterator();
            // if(ait.hasNext()){
            // TPAUserContractAccess access = (TPAUserContractAccess) ait.next();
            // UserPermissions up = access.getUserPermissions();
            // if(access.getPayrollEmail()!=null && access.isShowPayrollEmail()){
            // lastTPAUserPayrollEmailValue=access.getPayrollEmail().booleanValue();
            //													
            // }
            // }
            // }
            // 				
            // /*
            // * Getting firmId
            // */
            // for (Iterator it = userInfo.getTpaFirmsAsCollection().iterator(); it.hasNext();) {
            // TPAFirmInfo firmInfo = (TPAFirmInfo) it.next();
            // tpaFirmId = firmInfo.getId();
            // break;
            // }
            //												
            // /*
            // * Checking Last TPA user who have to receive payroll path email permission
            // */
            // if(isPayrollEnabledFirm && userInfo.getRole() instanceof ThirdPartyAdministrator){
            // if(!lastTPAUserPayrollEmailValue){
            // //List of TPA users for an firm
            // List tpaUserForFirm
            // =ProfileDAO.getTPAUsersWithPayrollEmailPermissionForFirm(tpaFirmId);
            // if(tpaUserForFirm.size() ==
            // SecurityConstants.MIN_NO_OF_TPA_USERS_WITH_PAYROLL_EMAIL_PERMISSION ){
            // Iterator iterator = tpaUserForFirm.iterator();
            // Long tpaProfileId = (Long)iterator.next();
            // if(tpaProfileId.longValue()==profile_id){
            // //setting the error code
            // List errors = new ArrayList();
            // GenericException ex = new GenericException(ErrorCodes.AUTOPAYROLL_TPA_USER_EDIT);
            // errors.add(ex);
            // SessionHelper.setErrorsInSession(request, errors);
            // return mapping.findForward(EDIT_USER);
            // }
            // }
            // }
            // }
            // /*
            // * Checking for Last user who have to receive payroll path email permission
            // */
            // else {
            // for (Iterator it = form.getContractAccesses().iterator(); it.hasNext();) {
            // TPAUserContractAccess contractAccess = (TPAUserContractAccess) it.next();
            // if (contractAccess.getPayrollEmail() != null){
            // lastPayrollEmailValue=contractAccess.getPayrollEmail().booleanValue();
            // if(!lastPayrollEmailValue){
            // //List of plansponsor users for contract
            // List
            // userListForContract=ProfileDAO.getUsersWithPayrollEmailPermissionForContract(contractAccess.getContractNumber().intValue());
            //		            		 
            //		            		
            // // List of TPA users for contract
            // List
            // tpaListForContract=ProfileDAO.getTPAUsersWithPayrollEmailPermissionForContract(userProfile.getCurrentContract().getContractNumber());
            // if(contractAccess.isShowPayrollEmail()){
            // if(tpaListForContract.size()==0 && userListForContract.size()==1){
            // Iterator iterator = userListForContract.iterator();
            // Long userProfileId = (Long)iterator.next();
            // if(userProfileId.longValue() == profile_id){
            // //setting the error code
            // List errors = new ArrayList();
            // GenericException ex = new GenericException(ErrorCodes.AUTOPAYROLL_PS_USER_EDIT);
            // errors.add(ex);
            // SessionHelper.setErrorsInSession(request, errors);
            // return mapping.findForward(REFRESH);
            // }
            //		            			 
            // }
            // }
            // }
            // }
            // }
            // }
            // service.updateUser(principal, userInfo);
            //				
            // }

            /*
             * Resets the token after the form is cleared.
             */

           // //TODO resetToken(request);
            request.getSession(false).removeAttribute(FLOW_TOKEN);
            //forward = mapping.findForward(CONFIRMATION);
            forward=CONFIRMATION;
        } catch (SecurityServiceException e) {

            List errors = new ArrayList();
            String fieldId = (String) securityExceptionFormFieldMap.get(e.getClass());

            if (fieldId == null) {
                errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
            } else {
                errors.add(new ValidationError(fieldId, Integer.parseInt(e.getErrorCode())));
            }

            SessionHelper.setErrorsInSession(request, errors);
//TODO
            //forward =forwards.get("input");
            forward="input";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doSave");
        }

        return forward;
    }

    /**
     * Forward to the confirmation page. This action is needed because it's the result of a REDIRECT
     * after a POST.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public String doConfirm( AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        //return mapping.findForward(CONFIRMATION_PAGE);
    	return CONFIRMATION_PAGE;
    }

    /**
     * Simply refresh the page. This action is used when we try to perform a REDIRECT after a POST.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return The input forward.
     */
    public String doRefresh( AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
    	//TODO
       // return mapping.getInputForward();
    	return "input";
    }

    /**
     * Removes the form from the session and forward user to the manage users page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public String doFinish( AutoForm actionForm,ModelMap model,
            HttpServletRequest request, HttpServletResponse response) {
    	clearSession(model,request);
        AddEditUserForm form = (AddEditUserForm)actionForm;
        
        // if user navigates through TPA Contacts tab, then redirect to TPA contacts tab
        if(form.isFromTPAContactsTab()){
        	//return mapping.findForward(FORWARD_TPA_CONTACTS);
         return FORWARD_TPA_CONTACTS;
        }
        //return mapping.findForward(MANAGE_USERS);
        return MANAGE_USERS;
    }

    /**
     * Allow the AddEditInternalUserAction to override this behavior
     * 
     * @param p
     * @param userName
     * @return
     * @throws SecurityServiceException 
     */
    protected UserInfo getUserInfo(Principal p, String userName) throws SystemException, SecurityServiceException {
    	return SecurityServiceDelegate.getInstance().searchByUserName(p, userName);
    }
    
    /**
     * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping,
     *      AutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String doDefault( AutoForm actionForm,ModelMap model,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
    	//TODO-Adding string variable mapping to avoid error(check with previos code)
		String mapping=(String) model.get("mapping");
        if (logger.isDebugEnabled()) {
            logger.debug("entry <-- doDefault");
        }
        AddEditUserForm form = (AddEditUserForm) actionForm;
        UserProfile userProfile = getUserProfile(request);
        /*
         * Save the token for this form. We have to validate this token when we save so that
         * duplicate submits are avoided.
         */
        ////TODO saveToken(request);
        /*
         * If we're on an edit page, populate the action form with the user info object.
         */
        String userName = form.getUserName();
        String rmId = form.getRmId();
        form.clear( request);
        
        if (!isAddUser(mapping)) {
            /*
             * If we're in edit mode and the form is empty, we forward back to
             * the manage users page.
             */
            if (userName == null || userName.length() == 0) {
                //return mapping.findForward(MANAGE_USERS);
            	return MANAGE_USERS;
            }

            try {
            	long beforeMethodCall = System.currentTimeMillis();
                UserInfo userInfo = getUserInfo(
                        newPrincipal(new Long(0), null, null), userName);
                
                long afterMethodCall = System.currentTimeMillis();
                logger.error("EDIT TPA Performance Trace - Calling ***getUserInfo() - Security service.searchByUserName*** from super.doDefault() " 
                		+ "Start time: " + beforeMethodCall + ", End time: " + afterMethodCall 
                		+ ", Duration: " + (afterMethodCall - beforeMethodCall) + " ms for "
                		+ " userName #" + userName);
                
                  if (!isViewUser(mapping)) {
                    long profileId = userInfo == null ? form.getProfileId() : userInfo
                            .getProfileId();
                    Collection<GenericException> errors = TPAUserContractAccessActionHelper
                            .checkForLockOrDelete(getUserProfile(request), profileId, userName);
                    if (!errors.isEmpty()) {
                        SessionHelper.setErrorsInSession(request, errors);
                        if(form.isFromTPAContactsTab()){
                        	//return mapping.findForward(FORWARD_TPA_CONTACTS);
                        	return FORWARD_TPA_CONTACTS;
                        }
                        //return mapping.findForward(MANAGE_USERS);
                        return MANAGE_USERS;
                    }
                }

                /*
                 * Handle the case when a user registeres himself while the IUM
                 * session still has the old username.
                 */
                if (userInfo == null) {
                    //return mapping.findForward(MANAGE_USERS);
                	return MANAGE_USERS;
                }

                /*
                 * If we're editing a user whose contract we don't have access
                 * to, we return to the Manage Users page. This is possible if
                 * someone removes all the contract access in the edit user page
                 * and hit the back button in the confirmation page.
                 */
                if (!(userInfo.getRole() instanceof InternalUser || userInfo.getRole() instanceof ThirdPartyAdministrator)
                        && userInfo.getContractPermissions().length == 0) {

                    //return mapping.findForward(MANAGE_USERS);
                	return MANAGE_USERS;
                }

                populateCommonForm(form, userInfo);
                populateForm(request, form, userInfo);

            } catch (SecurityServiceException e) {
                List errors = new ArrayList();
                errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
                SessionHelper.setErrorsInSession(request, errors);
                //return mapping.findForward(MANAGE_USERS);
                return MANAGE_USERS;
            }
        } else {
            // add mode
            populateFormWithEmailPreference(form);
        }
       
        form.storeClonedForm();

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doDefault");
        }
//TODO
        //return mapping.getInputForward();
        return "input";
    }

    /**
     * Populates the default email preference
     * 
     * @param actionForm
     * @return
     */
    private void populateFormWithEmailPreference(AutoForm actionForm) {
        AddEditUserForm form = (AddEditUserForm) actionForm;
        //  form.setEmailNewsletter(DEFAULT_EMAIL_NEWSLETTER);
    }

    protected void clearSession(ModelMap model,HttpServletRequest request) {
    	String mapping=(String)model.get("mapping");
        request.getSession(false).removeAttribute(mapping);
        request.getSession(false).removeAttribute(FLOW_TOKEN);
        request.getSession(false).removeAttribute("userPermissionsForm");
    }
}