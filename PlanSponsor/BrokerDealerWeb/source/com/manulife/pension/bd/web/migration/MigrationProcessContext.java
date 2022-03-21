package com.manulife.pension.bd.web.migration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.manulife.pension.ezk.web.ActionForm;

import com.manulife.pension.bd.web.myprofile.WebBrokerEntityProfile;
import com.manulife.pension.bd.web.myprofile.WebBrokerEntityProfileHelper;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.process.BDWizardProcessContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.process.ProcessState;
import com.manulife.pension.platform.web.process.SimpleProcessState;
import com.manulife.pension.platform.web.validation.rules.UserNameRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile;
import com.manulife.pension.service.security.bd.migration.OBDWUserProfile;
import com.manulife.pension.validator.ValidationError;

public class MigrationProcessContext extends BDWizardProcessContext {
	public static final String PROCESS_NAME = "security.migration";

	private static RegularExpressionRule PasswordRule = new RegularExpressionRule(
			CommonErrorCodes.PASSWORD_FAILS_STANDARDS, "^[a-zA-Z0-9]{5,32}$");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static SimpleProcessState Step1State = new SimpleProcessState(
			"step1", "/do/migration/step1");

	public final static SimpleProcessState Step2State = new SimpleProcessState(
			"step2", "/do/migration/step2");

	public final static SimpleProcessState Step3State = new SimpleProcessState(
			"step3", "/do/migration/step3");

	public final static SimpleProcessState CompleteState = new SimpleProcessState(
			"complete", "/do/migration/complete");

	public final static SimpleProcessState Login = new SimpleProcessState(
			"login", URLConstants.PostLogin, true);

	public final static SimpleProcessState CancelState = new SimpleProcessState(
			"cancel", URLConstants.HomeURL, true);

	static {
		Step1State.addNext(ACTION_CONTINUE, Step2State);
		Step2State.addNext(ACTION_CONTINUE, Step3State);
		Step3State.addNext(ACTION_CONTINUE, CompleteState);
		CompleteState.addNext(ACTION_CONTINUE, Login);
		Step1State.addNext(ACTION_CANCEL, CancelState);
		Step2State.addNext(ACTION_CANCEL, CancelState);
		Step3State.addNext(ACTION_CANCEL, CancelState);
	}

	private OBDWUserProfile migratingProfile;
	private String oldPassword;
	private boolean passwordNeedChange;
	private boolean userIdNeedChange;
	private List<BrokerEntityExtendedProfile> updateBrokerEntities;
	private long primaryBrokerEntityPartyId;
	private MigrationNewProfileForm registrationForm = new MigrationNewProfileForm();

	/**
	 * Populate the Action Form before the corresponding page is rendering
	 */
	@Override
	public void populateForm(ActionForm form) throws SystemException {
		if (Step3State.isSameState(getCurrentState())) {
			MigrationNewProfileForm mnpf = (MigrationNewProfileForm) form;
			mnpf.copyFrom(registrationForm);
			mnpf.setUserIdNeedChange(isUserIdNeedChange());
			mnpf.setPasswordNeedChange(isPasswordNeedChange());
			if (isUserIdNeedChange()) {
				mnpf.getUserCredential().setUserId(registrationForm.getUserCredential().getUserId());
			} else {
				mnpf.getUserCredential().setUserId(getMigratingProfile().getUserId());
			}
		}
	}

	@Override
	public void updateContext(ActionForm form) throws SystemException {
		if (Step3State.isSameState(getCurrentState())) {
			MigrationNewProfileForm mnpf = (MigrationNewProfileForm) form;
			registrationForm.copyFrom(mnpf);
			// these two fields should come from context
			registrationForm.setUserIdNeedChange(isUserIdNeedChange());
			registrationForm.setPasswordNeedChange(isPasswordNeedChange());
		}
	}

	@Override
	public ProcessState getStartState() {
		return Step1State;
	}

	public OBDWUserProfile getMigratingProfile() {
		return migratingProfile;
	}

	public void setMigratingProfile(OBDWUserProfile migratingProfile) {
		this.migratingProfile = migratingProfile;
		// populate the context
		setUserIdNeedChange(migratingProfile.isUserIdNeedChange());
		registrationForm.setFirstName(migratingProfile.getFirstName());
		registrationForm.setLastName(migratingProfile.getLastName());
		registrationForm.setPasswordNeedChange(passwordNeedChange);
	}

	public List<BrokerEntityExtendedProfile> getUpdateBrokerEntities() {
		return updateBrokerEntities;
	}

	public void setUpdateBrokerEntities(
			List<WebBrokerEntityProfile> updateBrokerEntities) {
		this.updateBrokerEntities = WebBrokerEntityProfileHelper
				.getBrokerExtendedProfile(updateBrokerEntities);
	}

	public long getPrimaryBrokerEntityPartyId() {
		return primaryBrokerEntityPartyId;
	}

	public void setPrimaryBrokerEntityPartyId(long primaryBrokerEntityPartyId) {
		this.primaryBrokerEntityPartyId = primaryBrokerEntityPartyId;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public boolean isPasswordNeedChange() {
		return passwordNeedChange;
	}

	public void setPasswordNeedChange(boolean passwordNeedChange) {
		this.passwordNeedChange = passwordNeedChange;
		registrationForm.setPasswordNeedChange(passwordNeedChange);
	}

	public boolean isUserIdNeedChange() {
		return userIdNeedChange;
	}

	public void setUserIdNeedChange(boolean userIdNeedChange) {
		this.userIdNeedChange = userIdNeedChange;
		if (userIdNeedChange) {
			setPasswordNeedChange(true);
		}
		registrationForm.setUserIdNeedChange(userIdNeedChange);
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = StringUtils.trimToEmpty(oldPassword);
	}

	/**
	 * Check whether oldPassword and UserId need to be changed
	 */
	public void checkIdPasswordStandard() {
		List<ValidationError> errors = new ArrayList<ValidationError>(2);
		if (migratingProfile != null) {
			userIdNeedChange = migratingProfile.isUserIdNeedChange();
			if (!userIdNeedChange) {
				if (!UserNameRule.getInstance().validate("", errors,
						migratingProfile.getUserId()))
					userIdNeedChange = true;
			}
		}
		// if id need to be changed, password has to change as well
		passwordNeedChange = userIdNeedChange;
		if (!passwordNeedChange
				&& !PasswordRule.validate("", errors, oldPassword)) {
			passwordNeedChange = true;
		}
	}

	/**
	 * Helper method to return the new user id
	 * 
	 * @return
	 */
	public String getNewUserId() {
		if (isUserIdNeedChange() || isPasswordNeedChange()) {
			return registrationForm.getUserCredential().getUserId();
		} else {
			return getMigratingProfile().getUserId();
		}
	}

	/**
	 * Helper method to return the new user password
	 * 
	 * @return
	 */
	public String getNewPassword() {
		if (isPasswordNeedChange()) {
			return registrationForm.getUserCredential().getPassword();
		} else {
			return getOldPassword();
		}
	}
}
