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
import com.manulife.pension.bd.web.messagecenter.BDMessageCenterConstants;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDPublicWizardProcessController;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.PasswordChallengeInputRule;
import com.manulife.pension.delegate.BDUserMigrationServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.validation.rules.NewPasswordRule;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.bd.migration.MigratingUserRegistrationValueObject;
import com.manulife.pension.service.security.bd.valueobject.PasswordChallenge;
import com.manulife.pension.service.security.bd.valueobject.UserSecurityValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject.SiteLocation;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNameIsInUseException;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;


@Controller
@RequestMapping( value = "/migration")
@SessionAttributes({"migrationNewProfileForm"})

public class MigrationStep3Controller extends BDPublicWizardProcessController {
	@ModelAttribute("migrationNewProfileForm") 
	public MigrationNewProfileForm populateForm() 
	{
		return new MigrationNewProfileForm();
		}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/migration/step3.jsp");
		}

	private final MandatoryRule pwdMandatoryRule = new MandatoryRule(
            CommonErrorCodes.EMPTY_PASSWORD);

    private final MandatoryRule confirmedPwdMandatoryRule = new MandatoryRule(
            CommonErrorCodes.CONFIRM_PASSWORD_MANDATORY);

    private final MandatoryRule licenseMandatoryRule = new MandatoryRule(
            BDErrorCodes.LICENSE_VERIFICATION_NOT_SELECTED);

    private final MandatoryRule fundListingMandatoryRule = new MandatoryRule(
            BDErrorCodes.FUND_LISTING_NOT_SELECTED);

    private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_PROFILE_FIRST_NAME);

    private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_PROFILE_LAST_NAME);

    private static final RegularExpressionRule invalidValueRErule = new RegularExpressionRule(
            BDErrorCodes.BROKER_PERSONAL_INFO_INVALID_VALUE,
            BDRuleConstants.FIRST_NAME_LAST_NAME_RE);

    private final List<ValidationError> tempArrayList = new ArrayList<ValidationError>();

    private static final String PROFILE_FIRST_NAME_LABEL = "Profile First Name";

    private static final String PROFILE_LAST_NAME_LABEL = "Profile Last Name";
    
    public MigrationStep3Controller() {
        super(MigrationStep3Controller.class, MigrationProcessContext.class,
                MigrationProcessContext.PROCESS_NAME, MigrationProcessContext.Step3State);
    }
    private static ControllerRedirect HomeRedirect1 = new ControllerRedirect(URLConstants.HomeURL);
	private static String HomeRedirect=HomeRedirect1.getPath();
    @Override
    protected ProcessState doContinue(ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        MigrationProcessContext context = (MigrationProcessContext) getProcessContext(request);
        MigrationNewProfileForm f = (MigrationNewProfileForm) form;
        validate(f, context, errors);

        if (errors.size() == 0) {
            // all front end validation pass
            MigratingUserRegistrationValueObject registration = createMigrationUserRegistration(f,
                    context);
            try {
                BDUserMigrationServiceDelegate.getInstance().migrate(registration);
            } catch (UserNameIsInUseException e) {
                logger.debug("Fail to migrate", e);
                context.setUserIdNeedChange(true);
                context.setPasswordNeedChange(true);
                errors.add(new ValidationError(MigrationNewProfileForm.FIELD_USER_ID,
                        SecurityServiceExceptionHelper.getErrorCode(e)));
            } catch (SecurityServiceException e) {
                logger.debug("Fail to migrate", e);
                errors.add(new ValidationError(MigrationNewProfileForm.FIELD_USER_ID,
                        SecurityServiceExceptionHelper.getErrorCode(e)));

            }
        }
        if (errors.size() == 0) {
            return getState().getNext(BDWizardProcessContext.ACTION_CONTINUE);
        } else {
            setErrorsInSession(request, errors);
            return getState();
        }
    }
    /*@RequestMapping(value = "/step3", method =  {RequestMethod.POST,RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("migrationNewProfileForm") MigrationNewProfileForm form,BindingResult bindingResult,
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
    
    @RequestMapping(value = "/step3", method = { RequestMethod.GET })
	protected String doInput(
			@Valid @ModelAttribute("migrationNewProfileForm") MigrationNewProfileForm form,
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

	@RequestMapping(value = "/step3", params = { "action=continue" }, method = { RequestMethod.POST })
	protected String doContinue(
			@Valid @ModelAttribute("migrationNewProfileForm") MigrationNewProfileForm form,
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

	@RequestMapping(value = "/step3", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(
			@Valid @ModelAttribute("migrationNewProfileForm") MigrationNewProfileForm form,
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
    
    
    /**
     * Create and return the MigratingUserRegisrationValue object from the form and Migration
     * Context
     * 
     * @param f
     * @param context
     * @return
     */
    private MigratingUserRegistrationValueObject createMigrationUserRegistration(
            MigrationNewProfileForm f, MigrationProcessContext context) {
        MigratingUserRegistrationValueObject vo = new MigratingUserRegistrationValueObject();
        vo.setOldUserId(context.getMigratingProfile().getUserId());
        vo.setFirstName(f.getFirstName());
        vo.setLastName(f.getLastName());
        UserSecurityValueObject securityVO = new UserSecurityValueObject();
        securityVO.setUserName(context.getNewUserId());
        securityVO.setPassword(context.getNewPassword());
        PasswordChallenge challenge1 = new PasswordChallenge(f.getChallenge1().getQuestionText(), f
                .getChallenge1().getAnswer());
        PasswordChallenge challenge2 = new PasswordChallenge(f.getChallenge2().getQuestionText(), f
                .getChallenge2().getAnswer());

        securityVO.setChallenges(new PasswordChallenge[] { challenge1, challenge2 });
        vo.setSecurityVO(securityVO);
        UserSiteInfoValueObject siteInfoVO = new UserSiteInfoValueObject();
        siteInfoVO.setAcceptDisclaimer(f.getAcceptDisclaimer());
        siteInfoVO.setDefaultSiteLocation(SiteLocation.valueOf(f.getDefaultSiteLocation()));
        siteInfoVO.setProducerLicence(f.getProducerLicense());
        vo.setSiteInfoVO(siteInfoVO);
        vo.setEmailNotification(f.isEmailNotification());
		vo
				.setSummaryEmailPref(BDMessageCenterConstants.ReceiveSummaryEmailPreference);

        vo.setUpdateBrokerEntityList(context.getUpdateBrokerEntities());
        long primaryPartyId = context.getPrimaryBrokerEntityPartyId();
        if (primaryPartyId == 0) {
            primaryPartyId = vo.getUpdateBrokerEntityList().get(0).getId();
        }
        vo.setPrimaryPartyId(primaryPartyId);
        return vo;
    }

    /**
     * The validation methods for the form
     * 
     * @param form
     * @return
     * @throws SystemException
     */
    private void validate(MigrationNewProfileForm form, MigrationProcessContext context,
            List<ValidationError> errors) throws SystemException {
        boolean isValid = false;
        isValid = firstNameMandatoryRule.validate(MigrationNewProfileForm.FIELD_FIRSTNAME, errors, form
                .getFirstName());
        if (isValid) {
            isValid = invalidValueRErule.validate(
                    MigrationNewProfileForm.FIELD_FIRSTNAME, tempArrayList, form
                            .getFirstName());
            if (!isValid) {
                errors.add(new ValidationError(
                        MigrationNewProfileForm.FIELD_FIRSTNAME,
                        BDErrorCodes.BROKER_PERSONAL_INFO_INVALID_VALUE, new Object[] { "",
                                PROFILE_FIRST_NAME_LABEL }));
            }
        }

        isValid = lastNameMandatoryRule.validate(MigrationNewProfileForm.FIELD_LASTNAME, errors, form
                .getLastName());
        if (isValid) {
            isValid = invalidValueRErule.validate(
                    MigrationNewProfileForm.FIELD_LASTNAME, tempArrayList, form
                            .getLastName());
            if (!isValid) {
                errors.add(new ValidationError(
                        MigrationNewProfileForm.FIELD_LASTNAME,
                        BDErrorCodes.BROKER_PERSONAL_INFO_INVALID_VALUE, new Object[] { "",
                                PROFILE_LAST_NAME_LABEL }));
            }
        }
        if (context.isUserIdNeedChange()) {
            // User Name mandatory and standards must be met
            UserNameRule.getInstance().validate(MigrationNewProfileForm.FIELD_USER_ID,
                    errors, form.getUserCredential().getUserId());
        }
        if (context.isPasswordNeedChange()) {
            // Password mandatory and standards must be met
            pwdMandatoryRule.validate(MigrationNewProfileForm.FIELD_PASSWORD, errors, form
                    .getUserCredential().getPassword());
            // Confirm Password mandatory and standards must be met
            confirmedPwdMandatoryRule.validate(
                    MigrationNewProfileForm.FIELD_CONFIRMED_PASSWORD, errors, form
                            .getUserCredential().getConfirmedPassword());
            if (StringUtils.isNotEmpty(form.getUserCredential().getPassword())
                    && StringUtils.isNotEmpty(form.getUserCredential().getConfirmedPassword())) {
                Pair<String, String> pair = new Pair<String, String>(form
                        .getUserCredential().getPassword(), form
                        .getUserCredential().getConfirmedPassword());
                NewPasswordRule.getInstance().validate(
                        MigrationNewProfileForm.FIELD_PASSWORD, errors, pair);
            }
        }
        Pair<PasswordChallengeInput, PasswordChallengeInput> challengePair = new Pair<PasswordChallengeInput, PasswordChallengeInput>(
                form.getChallenge1(), form.getChallenge2());
        PasswordChallengeInputRule.getInstance().validate(
                MigrationNewProfileForm.FIELD_CHALLENGE, errors, challengePair);
        licenseMandatoryRule.validate(MigrationNewProfileForm.FIELD_PRODUCER_LICENSE, errors,
                form.getProducerLicense());
        fundListingMandatoryRule.validate(
                MigrationNewProfileForm.FIELD_DEFAULT_SITE_LOCATION, errors, form
                        .getDefaultSiteLocation());
        if (form.getAcceptDisclaimer() == null) {
            errors.add(new ValidationError(MigrationNewProfileForm.FIELD_ACCEPT_DISCLAIMER,
                    BDErrorCodes.TERMS_AND_CONDITIONS_NOT_CHECKED));
        }
    }
}
