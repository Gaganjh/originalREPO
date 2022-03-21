package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.util.ExtensionRule;
import com.manulife.pension.platform.web.util.FaxRule;
import com.manulife.pension.platform.web.util.PhoneRule;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.platform.web.validation.rules.PrimaryEmailRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.rules.EmailWithoutMandatoryRule;
import com.manulife.pension.ps.web.validation.rules.SsnWithoutMandatoryRule;
import com.manulife.pension.service.contract.valueobject.ContactCommentVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.SiteLevelDuplicateSSNException;
import com.manulife.pension.service.security.exception.UserNameIsInUseException;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.PilotCAR;
import com.manulife.pension.service.security.role.PlanSponsorUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.transaction.UserNameIsNotUniqueException;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.SSNRender;

import org.owasp.encoder.Encode;

/**
 * This is the base class of all add/edit/view user actions.
 * 
 * @author Charles Chan
 * @author Steven Wang
 */
public abstract class AbstractAddEditClientUserController extends PsAutoController {

    private static final String ADD_PARAM = "add";

    private static final String VIEW_PARAM = "view";

    private static final String CONFIRMATION = "confirm";

    private static final String CONFIRMATION_PAGE = "confirmPage";

    //protected static final String MANAGE_USERS = "manageUsers";
    
    protected static final String REFRESH = "refresh";

    protected static final String RESET = "reset";

    protected static final String UNLOCK = "unlock";

    protected static final String FLOW_TOKEN = "flowToken";

    protected static final String DEFAULT_EMAIL_NEWSLETTER = Constants.YES;

    protected static final String JHANCOCK = "@jhancock";

    protected static final String MANULIFE = "@manulife";
    
    protected static final String OVERRIDE_DUPLICATE_SSN_WARNING = "overrideDuplicateSSNWarning";
    
    protected static final String DUPLICATE_SSN_WARNING = "duplicateSSNWarning";
    
    protected static final int MAX_PROFILES_IN_SSN_WARNING = 25;
    protected static final int MAX_CONTRACT_LENGTH_IN_SSN_WARNING = 7;
    protected static final int MAX_FIRST_NAME_LENGTH_IN_SSN_WARNING = 18;
    protected static final int MAX_LAST_NAME_LENGTH_IN_SSN_WARNING = 20;
    protected static final int MAX_ROLE_NAME_LENGTH_IN_SSN_WARNING = 30;

    protected Map securityExceptionFormFieldMap = new HashMap();

    /**
     * Constructor.
     * 
     * @param clazz
     */
    public AbstractAddEditClientUserController(Class clazz) {
        super(clazz);
        securityExceptionFormFieldMap.put(UserNameIsInUseException.class,
                AddEditClientUserForm.FIELD_USER_NAME);
        securityExceptionFormFieldMap.put(UserNameIsNotUniqueException.class,
                AddEditClientUserForm.FIELD_USER_NAME);
    }

    /**
     * Populates a UserInfo object with the given action form.
     * 
     * @param userInfo The user info object to populate.
     * @param actionForm The action form to obtain values from.
     * @return
     */
    protected abstract void populateUserInfo(HttpServletRequest request, UserInfo userInfo,
            AddEditClientUserForm actionForm) throws SystemException;

    /**
     * Populates an action form with the given user info.
     * 
     * @param actionForm The action form to populate.
     * @param userInfo The user info object to obtain values from.
     */
    protected abstract void populateForm(HttpServletRequest request,
            AddEditClientUserForm actionForm, UserInfo userInfo) throws SystemException;

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
        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
        userInfo.setProfileId(form.getProfileId());
        userInfo.setUserName(form.getUserName());
        userInfo.setEmail(form.getEmail());
        userInfo.setSecondaryEmail(form.getSecondaryEmail());
        userInfo.setFirstName(form.getFirstName());
        userInfo.setLastName(form.getLastName());
        userInfo.setUpdatedInformation(form.getChanges().toString());
        userInfo.setPhoneNumber(form.getTelephoneNumber());
        userInfo.setFax(form.getFaxNumber());
        userInfo.setPhoneExtension(form.getTelephoneExtension());
        userInfo.setContactCommentText(form.getComments()); // TODO - to be removed once property is removed from the UserInfo class
        ContactCommentVO contactComment = new ContactCommentVO();
        contactComment.setCommentText(form.getComments());
        userInfo.setContactComment(contactComment);
        userInfo.setWebAccessInd(AddEditClientUserForm.FORM_YES_CONSTANT.equals(form.getWebAccess()));

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
    private void populateCommonForm(AddEditClientUserForm form, UserInfo userInfo) {
        form.setProfileId(userInfo.getProfileId());
        form.setFirstName(userInfo.getFirstName());
        form.setLastName(userInfo.getLastName());
        form.setEmail(userInfo.getEmail());
        form.setSecondaryEmail(userInfo.getSecondaryEmail());
        form.setProfileStatus(userInfo.getProfileStatus());
        form.setUserName(userInfo.getUserName());
        form.setPasswordState(userInfo.getPasswordState());
        form.setWebAccess(userInfo.isWebAccessInd() ? AddEditClientUserForm.FORM_YES_CONSTANT : AddEditClientUserForm.FORM_NO_CONSTANT);
        form.setOriginalWebAccess(form.getWebAccess());
        form.setTelephoneNumber(userInfo.getPhoneNumber());
		form.setMobileNumber(MobileMask.maskPhone(userInfo.getMobileNumber().toString()));
        form.setTelephoneExtension(userInfo.getPhoneExtension());
        form.setFaxNumber(userInfo.getFax());
        if(userInfo.getContactComment() != null) {
        	form.setComments(userInfo.getContactComment().getCommentText());
        }
    }

    /**
     * Checks whether the current action invoked is an add action.
     * 
     * @param form The current action form.
     * @return true if the current action is an add action, false if otherwise.
     */
    static boolean isAddUser(String mapping) {
       // return mapping.getParameter().equals(ADD_PARAM);
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
     * Checks whether we're in the right state.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     */

    protected String validate( ActionForm actionForm,ModelMap model,
            HttpServletRequest request) {
    	String mapping=(String)model.get("mapping");

        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;

        /*
         * If we're not adding a user and the username is empty, we should forward back to the
         * Manage Users page. Notice that this is possible when user hit back button on the
         * confirmation page.
         */

        if (!isAddUser(mapping) || form.isConfirmAction()) {
            if (form.getUserName() == null || form.getUserName().length() == 0) {
                // return mapping.findForward(MANAGE_USERS);
            	//return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
            	return Constants.FORWARD_PLANSPONSOR_CONTACTS;
            }
        }

        /*
         * If this is a save action, we should compare the token and make sure it's still valid.
         * Token is initialized in the doDefault() method and reset in the doSave() method.
         */
        if (form.isSaveAction()) {
           /*TODO  if (!isTokenValid(request)) {
                // return mapping.findForward(MANAGE_USERS);
            	//return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
            	return Constants.FORWARD_PLANSPONSOR_CONTACTS;
            }*/
        }

        Collection errors = doValidate( form,model,request);

        /*
         * Errors are stored in the session so that our REDIRECT can look up the errors.
         */
        if (!errors.isEmpty()) {
            SessionHelper.setErrorsInSession(request, errors);
            request.getSession(false).setAttribute(FLOW_TOKEN, "addEdit");
           // return mapping.findForward(REFRESH);
            return REFRESH;
        }

        return null;
    }

    /**
     * Perform validation that is common to all add/edit user actions. Specifically, this method
     * validates first name, last name, email address and web access field
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#doValidate(org.apache.struts.action.Form,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected Collection doValidate( ActionForm actionForm,ModelMap model,
            HttpServletRequest request) {

        Collection errors = new ArrayList();

        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
        
        if(form.getAction().equals("changeRole")){
        	FormChanges.setRoleChanged(true);
        }

        /*
         * Validate the action form only when we save.
         */
        // MPR213 first name & last name
        if (form.isSaveAction()) {
            NameRule.getFirstNameInstance().validate(AddEditClientUserForm.FIELD_FIRST_NAME,
                    errors, form.getFirstName());
            NameRule.getLastNameInstance().validate(AddEditClientUserForm.FIELD_LAST_NAME,
                    errors, form.getLastName());

            // check email & ssn base on web access field
            if (AddEditClientUserForm.FORM_NO_CONSTANT.equals(form.getWebAccess())) {
                // The following rules will fail if the email or ssn is empty so we need to check that here
                if (form.getEmail() != null && form.getEmail().length() > 0) {
                    EmailWithoutMandatoryRule.getInstance().validate(AddEditClientUserForm.FIELD_EMAIL, errors, form.getEmail());
                }
                if (form.getSsn().toString().length() > 0) {
                    SsnWithoutMandatoryRule.getInstance().validate(AddEditClientUserForm.FIELD_SSN, errors, form.getSsn());
                }
            } else {
                // web access Yes need mandatory
                PrimaryEmailRule.getInstance().validate(AddEditClientUserForm.FIELD_EMAIL, errors,
                        form.getEmail());
                SsnRule.getInstance().validate(AddEditClientUserForm.FIELD_SSN, errors,
                        form.getSsn());
            }
            
           
            PhoneRule.getInstance().validate(AddEditClientUserForm.FIELD_TELEPHONE_NUMBER,	errors, form.getTelephoneNumber().getValue());
			if(StringUtils.isNotEmpty(form.getTelephoneNumber().getValue()))
			{
				if(StringUtils.isEmpty(form.getTelephoneNumber().getAreaCode()) || StringUtils.isEmpty(form.getTelephoneNumber().getPhonePrefix())
							|| StringUtils.isEmpty(form.getTelephoneNumber().getPhoneSuffix()) || form.getTelephoneNumber().getValue().length() < 10)
				{
					errors.add(new ValidationError(AddEditClientUserForm.FIELD_TELEPHONE_NUMBER, ErrorCodes.PHONE_NOT_COMPLETE));
				}
				if(StringUtils.isNotEmpty(form.getTelephoneNumber().getAreaCode()) && StringUtils.isNotEmpty(form.getTelephoneNumber().getPhonePrefix()))
				{
					String areaCode = null,phonePrefix = null;
					areaCode = form.getTelephoneNumber().getAreaCode();
					phonePrefix = form.getTelephoneNumber().getPhonePrefix();
					if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || phonePrefix.charAt(0) == '0' || phonePrefix.charAt(0) == '1')
					{
						errors.add(new ValidationError(AddEditClientUserForm.FIELD_TELEPHONE_NUMBER, ErrorCodes.PHONE_INVALID));
					}
				}
			}
			
			FaxRule.getInstance().validate(AddEditClientUserForm.FIELD_FAX_NUMBER,	errors, form.getFaxNumber().getValue());
			if(StringUtils.isNotEmpty(form.getFaxNumber().getValue()))
			{
				if(StringUtils.isEmpty(form.getFaxNumber().getAreaCode()) || StringUtils.isEmpty(form.getFaxNumber().getFaxPrefix())
							|| StringUtils.isEmpty(form.getFaxNumber().getFaxSuffix()) || form.getFaxNumber().getValue().length() < 10)
				{
					errors.add(new ValidationError(AddEditClientUserForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_NOT_COMPLETE));
				}
				if(StringUtils.isNotEmpty(form.getFaxNumber().getAreaCode()) && StringUtils.isNotEmpty(form.getFaxNumber().getFaxPrefix()))
				{
					String areaCode = null,faxPrefix = null;
					areaCode = form.getFaxNumber().getAreaCode();
					faxPrefix = form.getFaxNumber().getFaxPrefix();
					if( areaCode.charAt(0) == '0' || areaCode.charAt(0) == '1' || faxPrefix.charAt(0) == '0' || faxPrefix.charAt(0) == '1')
					{
						errors.add(new ValidationError(AddEditClientUserForm.FIELD_FAX_NUMBER, ErrorCodes.FAX_INVALID));
					}
				}
			}
			
			if(StringUtils.isNotEmpty(form.getTelephoneExtension())){
			
				ExtensionRule.getInstance().validate(AddEditClientUserForm.FIELD_EXTENSION_NUMBER,	errors, form.getTelephoneExtension());
				if( StringUtils.isEmpty(form.getTelephoneNumber().getValue()))
				{
					errors.add(new ValidationError(AddEditClientUserForm.FIELD_EXTENSION_NUMBER, ErrorCodes.PH_NOTENTERED_EXT_ENTERED));
				}
			}

            // MPR514 error if no changes
            if (!form.getClientUserAction().equals("addUser") && !form.getChanges().isChanged()) {
                GenericException ex = new GenericException(ErrorCodes.SAVING_WITH_NO_CHANGES);
                errors.add(ex);
            }
            
            boolean emailContainsCompanyName = false;
            
            for (int i = 0; i < form.getContractAccesses().size(); i++) {
                ClientUserContractAccess contractAccess = (ClientUserContractAccess) form.getContractAccesses().get(i);
                    
                String[] params = new String[] { contractAccess.getContractNumber().toString() };

                // scc86
                if (!emailContainsCompanyName && contractAccess.getContractNumber().intValue() != 70300 && contractAccess.getContractNumber().intValue() != 80089) {
                    if (emailContainCompanyName(form.getEmail())) {
                        emailContainsCompanyName = true;
                        errors.add(new GenericException(ErrorCodes.EMAIL_CONTAIN_COMPANY_NAME));
                    }
                }

                // MPR38
                if (contractAccess.getPlanSponsorSiteRole().equals(null) || contractAccess.getPlanSponsorSiteRole().equals("")) {
                    errors.add(new ValidationError("contractAccesses[" + i + "]." + ClientUserContractAccess.FIELD_PLANSPONSORSITE_ROLE, ErrorCodes.CHANGE_PERMISSION_WITHOUT_ROLE, params));
                } else {
                    if (contractAccess.getPlanSponsorSiteRole().equals(IntermediaryContact.ID) && (contractAccess.getRoleType() == null || "".equals(contractAccess.getRoleType()))) {
                        errors.add(new ValidationError("contractAccesses[" + i + "]." + ClientUserContractAccess.FIELD_ROLE_TYPE, 2537, params));
                    }
                }

                // Edit SCC87 & Add SAC37
                if (contractAccess.isNewContract() &&
                	!UserProfileForm.FORM_NO_CONSTANT.equals(form.getWebAccess()) &&
                	!contractAccess.isCbcIndicator() &&
                	!contractAccess.isSelectedAccess() &&
                	!contractAccess.isChangePermissionsClicked())
                {
                    errors.add(new GenericException(ErrorCodes.NEW_CONTRACT_WITHOUT_PERMISSIONS, params));
                }

                Map<String, Boolean> lastUserFlagMap = ClientUserContractAccessActionHelper.getLastUserFlags(contractAccess.getContractNumber().intValue(), form.getProfileId(),
                        contractAccess.isNewContract());
                if (contractAccess.isCbcIndicator()) {
                	//TODO-Adding string variable mapping to avoid error(check with previos code)
            		String mapping=(String) model.get("mapping");
                    if (isAddUser(mapping)) {
                        // Add SAC.57
                        if (!contractAccess.isPrimaryContact() && contractAccess.getPrimaryContactProfileId() == 0) {
                            GenericException ex = new GenericException(1055, params);
                            errors.add(ex);
                        }
                        // Add SAC.59
                        if (!contractAccess.isMailRecepient() && contractAccess.getMailRecipientProfileId() == 0) {
                            GenericException ex = new GenericException(1056, params);
                            errors.add(ex);
                        }

                    } else {
                        // Edit SCC.88
                        if (!Trustee.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
                            if (lastUserFlagMap.get(Trustee.ID).booleanValue()) {
                                GenericException ex = new GenericException(1066, params);
                                errors.add(ex);
                            }
                        }
                        // Edit SCC.89
                        if (!contractAccess.isPrimaryContact()
                                && (contractAccess.getPrimaryContactProfileId() == form.getProfileId() || contractAccess.getPrimaryContactProfileId() == 0)) {
                            GenericException ex = new GenericException(1055, params);
                            errors.add(ex);
                        }
                    
                        // Edit SCC.91
                        if (!contractAccess.isMailRecepient()
                                && (contractAccess.getMailRecipientProfileId() == form.getProfileId() || contractAccess.getMailRecipientProfileId() == 0)) {
                            GenericException ex = new GenericException(1056, params);
                            errors.add(ex);
                        }
                    	//SCC.193
                		if(contractAccess.isTrusteeMailRecepient() && !Trustee.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
                				&& !PlanSponsorUser.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
                            GenericException ex = new GenericException(1353, params);
                            errors.add(ex);
                		}
                		//SCC.194
                		if(contractAccess.isSignatureReceivedAuthSigner() && !AuthorizedSignor.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue()) 
                				&& !PlanSponsorUser.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
                            GenericException ex = new GenericException(1354, params);
                            errors.add(ex);
                		}
                		//SCC.195
                		if(contractAccess.isPrimaryContact() && PayrollAdministrator.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
                			GenericException ex = new GenericException(1356, params);
                            errors.add(ex);
                		}
                		//SCC.196
                		if(contractAccess.isPrimaryContact() &&  IntermediaryContact.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
                			GenericException ex = new GenericException(1357, params);
                            errors.add(ex);
                		}
                		//SCC.197
                		if(contractAccess.isSignatureReceivedTrustee() && !AuthorizedSignor.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue()) 
                				&& !Trustee.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
                				&& !PlanSponsorUser.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
                			GenericException ex = new GenericException(1355, params);
                            errors.add(ex);
                		}
                    }
               	}
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
    public String doCancel( AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
    	AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
    	LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + form.getProfileId());
        clearSession( request);
        // return mapping.findForward(MANAGE_USERS);
        //return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
        return Constants.FORWARD_PLANSPONSOR_CONTACTS;
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
    public String doSave( AutoForm actionForm,ModelMap model,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {

        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;

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
        
        boolean checkDuplicateSSN = getCheckDuplicateSSN(principal, form);
        
        String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
        userInfo.setIpAddress(ipAddress);

        SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();

        try {
			if (!(principal.getRole() instanceof PilotCAR)) {
				updateContractContacts(principal, userInfo);
			}
            
          //TODO-Adding string variable mapping to avoid error(check with previos code)
    		String mapping=(String)model.get("mapping");
            if (isAddUser(mapping)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("addUser");
                }
                UserInfo newUserInfo = service.addUser(principal, userInfo, 
                		Environment.getInstance().getSiteLocation(), checkDuplicateSSN);
                form.setUserName(newUserInfo.getUserName());
                if(form.isAddStaggingContact()){
                	long loggedInUserProfileId = userProfile.getPrincipal().getProfileId();
                	service.updateStagingContact(principal, userProfile.getCurrentContract().getContactId(), 
                			Environment.getInstance().getSiteLocation(), form.getStaggingContactId(), BigDecimal.valueOf(loggedInUserProfileId));
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("updateUser");
                }
                service.updateUser(principal, userInfo, Environment.getInstance().getSiteLocation(), 
                		false, checkDuplicateSSN);

                LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());
            }
            
            userProfile.resetAccess404a5();
            
            /*
             * Resets the token after the form is cleared.
             */
           // //TODO resetToken(request);
            request.getSession(false).removeAttribute(FLOW_TOKEN);
            //forward = mapping.findForward(CONFIRMATION);
            forward=CONFIRMATION;
            
        } catch (SiteLevelDuplicateSSNException e) {
        	String user = userProfile.getRole().getRoleId();
			String ssn = "";
			if(StringUtils.isNotEmpty(user) && user.equalsIgnoreCase("ICC")){
				 ssn = SSNRender.ssnFullMaskFormat(Encode.forHtmlContent(userInfo.getSsn()), null, true);
			}
			else if(StringUtils.isNotEmpty(user)){
				 ssn = SSNRender.ssnFullMaskFormat(Encode.forHtmlContent(userInfo.getSsn()), null, false);
			}
          	String duplicateSSNWarningMessage = generateDuplicateSSNWarningMessage(
        			e.getSsnMatches(), ssn);
        	request.setAttribute(DUPLICATE_SSN_WARNING, duplicateSSNWarningMessage);
        	//TODO
        	//forward =forwards.get("input");
        	forward="input";
        	
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
     * Determines if an application wide check for duplicate SSNs should be performed.
     * 
     * This check is only performed if the add/edit user request is initiated by an
     * authorized internal user and the user has not already dismissed the warning.
     * 
     * @param principal The logged in user performing an add/edit user request
     * @param form
     * @return Whether the add/edit user request should include a duplicate SSN check
     */
    private boolean getCheckDuplicateSSN(Principal principal, AddEditClientUserForm form) {
    	
    	boolean duplicateSSNCheck = false;
    	
    	if (!form.isIgnoreSSNWarning()) {
	    	UserRole role = principal.getRole();
	    	if (role.isInternalUser() &&
	    		role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS) && 
	    		!role.hasPermission(PermissionType.SELECTED_ACCESS) &&
	    		form.getSsn() != null && !form.getSsn().isEmpty()){
	    		
	    		duplicateSSNCheck = true;
    		}
    	}
    	return duplicateSSNCheck;
    }
    
    /**
     * Creates the duplicate SSN warning message to display in the popup box.
     * 
     * @param ssnMatches The list of users with matching SSNs
     * @return The duplicate SSN warning message to display
     */
    private String generateDuplicateSSNWarningMessage(List<UserInfo> ssnMatches, String ssn)
    		throws SystemException {
    	
    	StringBuffer warningMessage = new StringBuffer();
    	
    	try {
			Message message = (Message)ContentCacheManager.getInstance().getContentById(
					ContentConstants.WARNING_DUPLICATE_SSN_WITHIN_PSW,
					ContentTypeManager.instance().MESSAGE);
			
			int totalProfiles = ssnMatches.size();
			if (totalProfiles > MAX_PROFILES_IN_SSN_WARNING) {
				ssnMatches = ssnMatches.subList(0, MAX_PROFILES_IN_SSN_WARNING);
			}
			int numProfilesDisplayed = ssnMatches.size();
			
			String[] params = { ssn, Integer.toString(numProfilesDisplayed), 
					Integer.toString(totalProfiles) };
			
			warningMessage.append(ContentUtility.getContentAttribute(message, "text", null, params));
			 
		} catch (ContentException e) {
			throw new SystemException(e, "Failed to call content service.");
		}
		
		warningMessage.append("\\n\\nCN, Name, Role");

		for (UserInfo user : ssnMatches) {
			String contractNum = Integer.toString(user.getContractNumber());
			warningMessage.append("\\n");
			warningMessage.append(StringUtils.substring(contractNum, 0, 
					MAX_CONTRACT_LENGTH_IN_SSN_WARNING));
			warningMessage.append(", ");
			warningMessage.append(StringUtils.substring(user.getLastName(), 0,
					MAX_LAST_NAME_LENGTH_IN_SSN_WARNING));
			warningMessage.append(" ");
			warningMessage.append(StringUtils.substring(user.getFirstName(), 0,
					MAX_FIRST_NAME_LENGTH_IN_SSN_WARNING));
			warningMessage.append(", ");
			warningMessage.append(StringUtils.substring(user.getRole().getDisplayName(), 0, 
					MAX_ROLE_NAME_LENGTH_IN_SSN_WARNING));
		}
		return warningMessage.toString();
    }

    /**
     * Method to remove any previous primary contact or mail recipient on the contracts
     * 
     * @param principal
     * @param userInfo
     * @throws SecurityServiceException
     * @throws SystemException
     */
    private void updateContractContacts(Principal principal, UserInfo userInfo) throws SecurityServiceException, SystemException {
        SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
        for (ContractPermission contractPermission : userInfo.getContractPermissions()) {
            if (contractPermission.isPrimaryContact() || contractPermission.isMailRecipient() || contractPermission.isTrusteeMailRecepient() || contractPermission.isStatementIndicator()|| contractPermission.isIccDesignate() || contractPermission.isSendServiceDesignate()) {
                int contractNumber = contractPermission.getContractNumber();
                // Get the contract info since the cached contract info is stale
                Contract contract = ClientUserContractAccessActionHelper.getContract(contractPermission.getRole(), contractNumber);
                if (contract != null) {
                    UserInfo primaryContactUserInfo = null;
                    UserInfo mailRecipientUserInfo = null;
                    UserInfo trusteeMailRecipientUserInfo = null;
                    UserInfo participantStatConsultantUserInfo = null;
                    UserInfo iccDesignateUserInfo = null;
                    UserInfo sendServiceDesignateUserInfo = null;
                    // If this user has been designated as the primary contact we need to change the existing primary contact to no
                    if (contractPermission.isPrimaryContact() && contract.getPrimaryContact() != null) {
                        long primaryContactProfileId = contract.getPrimaryContact().getProfileId();
                        if (primaryContactProfileId != 0 && primaryContactProfileId != userInfo.getProfileId()) {
                            primaryContactUserInfo = service.searchByProfileId(newPrincipal(new Long(0), null, null), primaryContactProfileId);
                            primaryContactUserInfo.getContractPermission(contractNumber).setPrimaryContact(false);
                        }
                    }
                    // If this user has been designated as the mail recipient we need to change the existing mail recipient to no
                    if (contractPermission.isMailRecipient() && contract.getMailRecipient() != null) {
                        long mailRecipientProfileId = contract.getMailRecipient().getProfileId();
                        if (mailRecipientProfileId != 0 && mailRecipientProfileId != userInfo.getProfileId()) {
                            if (primaryContactUserInfo != null && primaryContactUserInfo.getProfileId() == mailRecipientProfileId) {
                                primaryContactUserInfo.getContractPermission(contractNumber).setMailRecipient(false);
                            } else {
                                mailRecipientUserInfo = service.searchByProfileId(newPrincipal(new Long(0), null, null), mailRecipientProfileId);
                                mailRecipientUserInfo.getContractPermission(contractNumber).setMailRecipient(false);
                            }
                        }
                    }
                    
                    // If this user has been designated as the trustee mail recipient we need to change the existing trustee mail recipient to no
                    if (contractPermission.isTrusteeMailRecepient() && contract.getTrusteeMailRecipient() != null) {
                        long trusteeMailRecipientProfileId = contract.getTrusteeMailRecipient().getProfileId();
                        if (trusteeMailRecipientProfileId != 0 && trusteeMailRecipientProfileId != userInfo.getProfileId()) {
	                        if (primaryContactUserInfo != null && primaryContactUserInfo.getProfileId() == trusteeMailRecipientProfileId) {
	                            primaryContactUserInfo.getContractPermission(contractNumber).setTrusteeMailRecepient(false);
	                        } 
	                        else if(mailRecipientUserInfo != null && mailRecipientUserInfo.getProfileId() == trusteeMailRecipientProfileId) {
	                        	mailRecipientUserInfo.getContractPermission(contractNumber).setTrusteeMailRecepient(false);
	                        }
	                        else {
	                        	trusteeMailRecipientUserInfo = service.searchByProfileId(newPrincipal(new Long(0), null, null), trusteeMailRecipientProfileId);
	                        	trusteeMailRecipientUserInfo.getContractPermission(contractNumber).setTrusteeMailRecepient(false);
	                        }
                        } 
                    }
                    
                    // If this user has been designated as the Participant statement consultant we need to change the existing participant statement consultant to no
                    if (contractPermission.isStatementIndicator() && contract.getParticipantStatementConsultant() != null) {
                        long participantStatConsProfileId = contract.getParticipantStatementConsultant().getProfileId();
                        if (participantStatConsProfileId != 0 && participantStatConsProfileId != userInfo.getProfileId()) {
	                        if (primaryContactUserInfo != null && primaryContactUserInfo.getProfileId() == participantStatConsProfileId) {
	                            primaryContactUserInfo.getContractPermission(contractNumber).setStatementIndicator(false);
	                        } 
	                        else if(mailRecipientUserInfo != null && mailRecipientUserInfo.getProfileId() == participantStatConsProfileId) {
	                        	mailRecipientUserInfo.getContractPermission(contractNumber).setStatementIndicator(false);
	                        }
	                        else if(trusteeMailRecipientUserInfo != null && trusteeMailRecipientUserInfo.getProfileId() == participantStatConsProfileId) {
	                        	trusteeMailRecipientUserInfo.getContractPermission(contractNumber).setStatementIndicator(false);
	                        }	
	                        else {
	                        	participantStatConsultantUserInfo = service.searchByProfileId(newPrincipal(new Long(0), null, null), participantStatConsProfileId);
	                        	participantStatConsultantUserInfo.getContractPermission(contractNumber).setStatementIndicator(false);
	                        }
                        } 
                    }
                   
                   /* CL 126187 - User attributes assigned to more than one user */ 
                    
                 // If this user has been designated as the Icc Designate we need to change the existing Icc Designate to no
                    if (contractPermission.isIccDesignate() && contract.getIccDesignate() != null) {
                        long iccDesignateProfileId = contract.getIccDesignate().getProfileId();
                        if (iccDesignateProfileId != 0 && iccDesignateProfileId != userInfo.getProfileId()) {
                        	if (primaryContactUserInfo != null && primaryContactUserInfo.getProfileId() == iccDesignateProfileId) {
	                            primaryContactUserInfo.getContractPermission(contractNumber).setIccDesignate(false);
	                        } 
	                        else if(mailRecipientUserInfo != null && mailRecipientUserInfo.getProfileId() == iccDesignateProfileId) {
	                        	mailRecipientUserInfo.getContractPermission(contractNumber).setIccDesignate(false);
	                        }
	                        else if(trusteeMailRecipientUserInfo != null && trusteeMailRecipientUserInfo.getProfileId() == iccDesignateProfileId) {
	                        	trusteeMailRecipientUserInfo.getContractPermission(contractNumber).setIccDesignate(false);
	                        }	
	                        else if (participantStatConsultantUserInfo != null && participantStatConsultantUserInfo.getProfileId() == iccDesignateProfileId){	                        	
	                        	participantStatConsultantUserInfo.getContractPermission(contractNumber).setIccDesignate(false);
	                        }
                        	else{
	                        	iccDesignateUserInfo = service.searchByProfileId(newPrincipal(new Long(0), null, null), iccDesignateProfileId);
	                        	iccDesignateUserInfo.getContractPermission(contractNumber).setIccDesignate(false);
                        	}
                        }
                    }
                    /* End of CL 126187 change */
                    
                    // If this user has been designated as the SEND Service Designate we need to change the existing SEND Service Designate to NO
                   if (contractPermission.isSendServiceDesignate() && contract.getSendServiceDesignate() != null) {
                       long sendServiceDesignateProfileId = contract.getSendServiceDesignate().getProfileId();
                       if (sendServiceDesignateProfileId != 0 && sendServiceDesignateProfileId != userInfo.getProfileId()) {
                       	if (primaryContactUserInfo != null && primaryContactUserInfo.getProfileId() == sendServiceDesignateProfileId) {
                            primaryContactUserInfo.getContractPermission(contractNumber).setSendServiceDesignate(false);
                        } 
                        else if(mailRecipientUserInfo != null && mailRecipientUserInfo.getProfileId() == sendServiceDesignateProfileId) {
                        	mailRecipientUserInfo.getContractPermission(contractNumber).setSendServiceDesignate(false);
                        }
                        else if(trusteeMailRecipientUserInfo != null && trusteeMailRecipientUserInfo.getProfileId() == sendServiceDesignateProfileId) {
                        	trusteeMailRecipientUserInfo.getContractPermission(contractNumber).setSendServiceDesignate(false);
                        }	
                        else if (participantStatConsultantUserInfo != null && participantStatConsultantUserInfo.getProfileId() == sendServiceDesignateProfileId){	                        	
                        	participantStatConsultantUserInfo.getContractPermission(contractNumber).setSendServiceDesignate(false);
                        }
                       	else{
                        	sendServiceDesignateUserInfo = service.searchByProfileId(newPrincipal(new Long(0), null, null), sendServiceDesignateProfileId);
                        	sendServiceDesignateUserInfo.getContractPermission(contractNumber).setSendServiceDesignate(false);
                       	}
                       }
                   }
                    
                    if (primaryContactUserInfo != null) {
                        service.updateUser(principal, Environment.getInstance().getSiteLocation(), primaryContactUserInfo);
                    }
                    if (mailRecipientUserInfo != null) {
                        service.updateUser(principal, Environment.getInstance().getSiteLocation(), mailRecipientUserInfo);
                    }
                    if(trusteeMailRecipientUserInfo != null) {
                    	service.updateUser(principal, Environment.getInstance().getSiteLocation(), trusteeMailRecipientUserInfo);
                    }
                    if(participantStatConsultantUserInfo != null) {
                    	service.updateUser(principal, Environment.getInstance().getSiteLocation(), participantStatConsultantUserInfo);
                    }
                    if(iccDesignateUserInfo != null) {  
                    	if(iccDesignateUserInfo.getProfileStatus().equals(SecurityConstants.SUSPENDED_PROFILE_STATUS)){
                    		service.updateUser(principal, iccDesignateUserInfo, Environment.getInstance().getSiteLocation(),true);
                    	} else{
                    		service.updateUser(principal, Environment.getInstance().getSiteLocation(), iccDesignateUserInfo);
                    	}
                    }
                    if(sendServiceDesignateUserInfo != null){
                    	if(sendServiceDesignateUserInfo.getProfileStatus().equals(SecurityConstants.SUSPENDED_PROFILE_STATUS)){
                    		service.updateUser(principal, sendServiceDesignateUserInfo, Environment.getInstance().getSiteLocation(),true);
                    	} else{
                    		service.updateUser(principal, Environment.getInstance().getSiteLocation(), sendServiceDesignateUserInfo);
                    	}
                    }
                    
                }
            }
        }
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
    	AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
    	form.setClientUserAction("confirm");
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
    public String doRefresh( AutoForm actionForm,ModelMap model, HttpServletRequest request, HttpServletResponse response) throws SystemException {

        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
        UserProfile userProfile = getUserProfile(request);
        String userName = form.getUserName();
      //TODO-Adding string variable mapping to avoid error(check with previos code)
      		String mapping=(String)model.get("mapping");
        UserInfo userInfo = ClientUserContractAccessActionHelper.getManagedUserInfo(userProfile.getPrincipal(), userName);
        if (userInfo != null && !isViewUser(mapping)) {
            Collection<GenericException> errors = ClientUserContractAccessActionHelper.checkForLockOrDelete(userProfile, userInfo.getProfileId(), userName);
            if (!errors.isEmpty()) {
                SessionHelper.setErrorsInSession(request, errors);
                // return mapping.findForward(MANAGE_USERS);
                //return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
                return Constants.FORWARD_PLANSPONSOR_CONTACTS;
            }
        }
        
        // fix browser back button bug
        
        if (isAddUser(mapping) && form.getAllContractAccesses().size() == 0) {

            /*
             * MPR 17. Add one empty contract for the current contract.
             */
            ClientUserContractAccess accessForm = new ClientUserContractAccess();

            if (userProfile.getCurrentContract() != null) {
                UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(getUserProfile(request).getPrincipal());

                int contractNumber = userProfile.getCurrentContract().getContractNumber();

                Contract loginContract = ClientUserContractAccessActionHelper.getContract(userProfile.getRole(), new Integer(contractNumber));

                if (loginUserInfo.getRole().isInternalUser()) {
                    form.setFieldsEnableForInternalUser(loginUserInfo.getRole().hasPermission(PermissionType.MANAGE_EXTERNAL_USERS_TRUSTEE_AND_AUTH_SIGNOR));
                }

                if (loginUserInfo.getRole().isExternalUser()) {
                    // For external users we need the contract-level role
                    ContractPermission contractPermission = loginUserInfo.getContractPermission(contractNumber);
                    ClientUserContractAccessActionHelper.populateNewContractAccess(contractPermission.getRole(), accessForm, loginContract);
                } else {
                    ClientUserContractAccessActionHelper.populateNewContractAccess(loginUserInfo.getRole(), accessForm, loginContract);
                }
                form.getAllContractAccesses().add(accessForm);

                // Since this is a new profile the logged in user can manage all of this profile's contracts
                form.setCanManageAllContracts(true);

                /*
                 * Clone the form after the dummy contract access object is created in the add user case.
                 */
                form.storeClonedForm();
            }
        }
        
        ClientUserContractAccessActionHelper.populateContractDropDown( form, request);
        
        for(ClientUserContractAccess contractAccess : form.getContractAccesses()){
        	if(contractAccess.isIccDesignate() && !Trustee.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
    				&& !AuthorizedSignor.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
    				&& !AdministrativeContact.stringID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
        		contractAccess.setIccDesignate(false);
    			break;
    		}
        	if(contractAccess.isSendServiceDesignate() && !Trustee.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
    				&& !AuthorizedSignor.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())
    				&& !AdministrativeContact.stringID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
        		contractAccess.setIccDesignate(false);
    			break;
    		}
        }
//TODO
      //  return mapping.getInputForward();
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
    public String doFinish( AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        clearSession( request);
        // return mapping.findForward(MANAGE_USERS);
        //return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
        return Constants.FORWARD_PLANSPONSOR_CONTACTS;
    }

    /**
     * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping,
     *      AutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String doDefault( AutoForm actionForm,ModelMap model, HttpServletRequest request, HttpServletResponse response) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry <-- doDefault");
        }
        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;

        /*
         * Save the token for this form. We have to validate this token when we save so that duplicate submits are avoided.
         */
       // //TODO saveToken(request);
        /*
         * If we're on an edit page, populate the action form with the user info object.
         */
        //Get user name by using the profile id
        String userName =  null;
		String sessionProfileId =  (String) request.getParameter( "profileId");
		if(! StringUtils.isBlank(sessionProfileId) && Long.valueOf(sessionProfileId) > 0){
			userName = SecurityServiceDelegate.getInstance().getLDAPUserNameByProfileId(Long.valueOf(sessionProfileId));
		}
		
		if (StringUtils.isBlank(userName) ){
	        userName = form.getUserName();
		}
		       
        form.clear( request);
      //TODO-Adding string variable mapping to avoid error(check with previos code)
      		String mapping=(String)model.get("mapping");

        if (!isAddUser(mapping)) {
            // set userAction

            if (!"confirm".equalsIgnoreCase(form.getClientUserAction())) {
                form.setClientUserAction("editUser");
            }

            /*
             * If we're in edit mode and the form is empty, we forward back to the manage users page.
             */
            if (userName == null || userName.length() == 0) {
                // return mapping.findForward(MANAGE_USERS);
            	//return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
            	return Constants.FORWARD_PLANSPONSOR_CONTACTS; 
            }

            UserProfile userProfile = getUserProfile(request);

            UserInfo userInfo = ClientUserContractAccessActionHelper.getManagedUserInfo(userProfile.getPrincipal(), userName);
                        
            long profileId = 0;
            if (userInfo != null) {
                profileId = userInfo.getProfileId();
            } else {
                profileId = form.getProfileId();
            }

            if (!isViewUser(mapping) && !"confirm".equalsIgnoreCase(form.getClientUserAction())) {
                Collection<GenericException> errors = ClientUserContractAccessActionHelper.checkForLockOrDelete(userProfile, profileId, userName);
                if (!errors.isEmpty()) {
                    SessionHelper.setErrorsInSession(request, errors);
                    // return mapping.findForward(MANAGE_USERS);
                    //return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
                    return Constants.FORWARD_PLANSPONSOR_CONTACTS;
                }
            }

            /*
             * Handle the case when a user registeres himself while the IUM session still has the old username.
             */
            if (userInfo == null) {
                // return mapping.findForward(MANAGE_USERS);
            	//return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
            	return Constants.FORWARD_PLANSPONSOR_CONTACTS;
            }

            /*
             * If we're editing a user whose contract we don't have access to, we return to the Manage Users page. This is possible if someone removes all the contract access in
             * the edit user page and hit the back button in the confirmation page.
             */
            if (!(userInfo.getRole() instanceof InternalUser || userInfo.getRole() instanceof ThirdPartyAdministrator) && userInfo.getContractPermissions().length == 0) {

                // return mapping.findForward(MANAGE_USERS);
            	//return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
            	return Constants.FORWARD_PLANSPONSOR_CONTACTS;
            }

            populateCommonForm(form, userInfo);
            populateForm(request, form, userInfo);

            form.storeClonedForm();

        } else {
            // add mode
            form.setClientUserAction("addUser");
            UserProfile loginUserProfile = getUserProfile(request);
            form.setLoginUserRole(loginUserProfile.getRole().getDisplayName());
            // populateFormWithEmailPreference(form);
           // populateFormForAddUser(request, form);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doDefault");
        }
//TODO
        //return mapping.getInputForward();
        return "input";
    }

    protected boolean emailContainCompanyName(String email) {
        return email.contains(JHANCOCK) || email.contains(MANULIFE);
    }

    /**
     * Invokes the reset task. Calls SecurityServiceDelegate to reset the
     * password. Receives temp password as a return and stores it in the
     * session.
     */
    public String doReset( AutoForm actionForm, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry <-- doReset");
        }

        UserProfile userProfile = getUserProfile(request);
        Principal principal = userProfile.getPrincipal();

        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
        Collection<GenericException> errors = ClientUserContractAccessActionHelper.checkForLockOrDelete(userProfile, form.getProfileId(), form.getUserName());
        if (!errors.isEmpty()) {
            SessionHelper.setErrorsInSession(request, errors);
           // return mapping.findForward(MANAGE_USERS);
            //return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
            return Constants.FORWARD_PLANSPONSOR_CONTACTS;
        }

        try {
            UserInfo userInfo = new UserInfo();
            populateCommonUserInfo(userInfo, actionForm);

            SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
            service.resetPassword(principal, userInfo, Environment.getInstance().getSiteLocation());

            request.setAttribute(Constants.USERINFO_KEY, userInfo);

            clearSession( request);
        } catch (SecurityServiceException e) {
            errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
            SessionHelper.setErrorsInSession(request, errors);
            //return mapping.findForward(REFRESH);
            return REFRESH;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doReset");
        }
//TODO
       // return findForward( RESET);
        return findForward(RESET);
    }

    /**
     * Invokes the print task. It uses the common workflow with validateForm set
     * to false.
     */
    public String doUnlock(AutoForm actionForm, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry <-- doUnlock");
        }

        UserProfile userProfile = getUserProfile(request);
        Principal principal = userProfile.getPrincipal();

        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
        Collection<GenericException> errors = ClientUserContractAccessActionHelper.checkForLockOrDelete(userProfile, form.getProfileId(), form.getUserName());
        if (!errors.isEmpty()) {
            SessionHelper.setErrorsInSession(request, errors);
            // return mapping.findForward(MANAGE_USERS);
            //return mapping.findForward(Constants.FORWARD_PLANSPONSOR_CONTACTS);
            return Constants.FORWARD_PLANSPONSOR_CONTACTS;
        }

        try {
            UserInfo userInfo = new UserInfo();
            populateCommonUserInfo(userInfo, actionForm);

            SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
            service.unlockPassword(principal, userInfo);

            request.setAttribute(Constants.USERINFO_KEY, userInfo);

            clearSession( request);
        } catch (SecurityServiceException e) {
            errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
            SessionHelper.setErrorsInSession(request, errors);
            //return mapping.findForward(REFRESH);
            return REFRESH;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-- doUnlock");
        }
//TODO
        //return findForward( UNLOCK);
        return findForward( UNLOCK);
    }
    
    protected void clearSession( HttpServletRequest request) {
       // request.getSession(false).removeAttribute(mapping.getName());
        request.getSession(false).removeAttribute(FLOW_TOKEN);
        request.getSession(false).removeAttribute("userProfileForm");
        request.getSession(false).removeAttribute("userPermissionsForm");
    }
    
}