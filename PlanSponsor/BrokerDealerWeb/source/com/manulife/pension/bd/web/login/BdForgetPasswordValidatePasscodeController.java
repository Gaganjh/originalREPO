package com.manulife.pension.bd.web.login;

import org.springframework.stereotype.Component;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.FrwValidation;
import com.manulife.pension.platform.web.passcode.PasscodeErrorMessage;
import com.manulife.pension.platform.web.passcode.ValidatePasscodeController;
import com.manulife.pension.service.security.bd.valueobject.BDLoginValueObject;

@Component
public class BdForgetPasswordValidatePasscodeController extends ValidatePasscodeController<BDLoginValueObject> {

	public BdForgetPasswordValidatePasscodeController() {
		super(ForgetPasswordStepUpValidationController.class, BDConstants.BD_APPLICATION_ID,
				FrwValidation.getInstance(), BdPasscodeVerification.INSTANCE,
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

}
