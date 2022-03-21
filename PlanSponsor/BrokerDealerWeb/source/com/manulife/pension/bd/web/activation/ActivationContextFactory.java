package com.manulife.pension.bd.web.activation;

import com.manulife.pension.bd.web.registration.RegisterBrokerAssistantProcessContext;
import com.manulife.pension.bd.web.registration.RegisterFirmRepProcessContext;
import com.manulife.pension.bd.web.registration.RegisterRiaUserProcessContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;

/**
 * All the activation involves a Process, this factory class creates the
 * ProcessContext for different activation process based on the
 * BDSecurityInteractionRequest's type (and user role of the request)
 * 
 * @author guweigu
 * 
 */
public class ActivationContextFactory {

	private static final ActivationContextFactory instance = new ActivationContextFactory();

	public static ActivationContextFactory getInstance() {
		return instance;
	}

	private ActivationContextFactory() {
	}

	/**
	 * The factory method to create a new process context for different
	 * activation process and start it
	 * 
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	public AbstractActivationProcessContext getProcessContext(
			BDSecurityInteractionRequestEx request) throws SystemException {
		AbstractActivationProcessContext context = null;

		if (request == null) {
			context = new InvalidActivationProcessContext();
		} else {
			switch (request.getType()) {
			case NewPasswordActivation:
				context = new NewPasswordActivationProcessContext();
				break;
			case UserPartyActivation:
				context = new BrokerEntityActivationProcessContext();
				break;
			case UserActivation: {
				switch (request.getUserRoleType()) {
				case FirmRep:
					context = new RegisterFirmRepProcessContext();
					break;
				case RIAUser:
					context = new RegisterRiaUserProcessContext();
					break;
				case FinancialRepAssistant:
					context = new RegisterBrokerAssistantProcessContext();
					break;
				case FinancialRep:
					context = new NewUserActivationProcessContext();
					break;
				case BasicFinancialRep:
					context = new NewUserActivationProcessContext();
					break;
				}
			}
				break;
			}
			if (context == null) {
				throw new SystemException(
						"The activation request is not supported:"
								+ request.getId());
			}
		}
		context.setSecurityRequest(request);
		context.setCurrentState(context.getStartState());
		return context;
	}
}
