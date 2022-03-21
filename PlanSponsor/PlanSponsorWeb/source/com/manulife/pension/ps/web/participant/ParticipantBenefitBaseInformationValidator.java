package com.manulife.pension.ps.web.participant;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantBenefitBaseDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.account.valueobject.ParticipantGiflData;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
@Component
public class ParticipantBenefitBaseInformationValidator extends ValidatorUtil  implements Validator{
	public static final String YES = "YES";
private static Logger logger = Logger.getLogger(ParticipantBenefitBaseInformationValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ParticipantBenefitBaseInformationForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ArrayList<GenericException> transactionErrors = new ArrayList<GenericException>();
		Collection error = new ArrayList();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm = (ParticipantBenefitBaseInformationForm) target;
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate()");
		}
	
		

		// Get the task for From date and To date in Transaction history
		if (getTask(request).equals(FILTER_TASK)) {
			//transactionErrors = (ArrayList<GenericException>) validateTransactionHistory(participantBenefitBaseInformationForm, request);
		}

		error.addAll(transactionErrors);

		/*try {*/

			if (request
					.getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID) != null) {
				participantBenefitBaseInformationForm
						.setProfileId((String) request
								.getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID));
			}
			//retrieveParticipantDetails(participantBenefitBaseInformationForm.getProfileId(), request);
			//getParticipantBenefitBaseAccountDetails((BaseReportForm) form,request);
			//getParticipantLIADetails(participantBenefitBaseInformationForm.getProfileId(), request);
			if (transactionErrors.size() == 0) {
			//TODO code need to move to controller	doCommon( participantBenefitBaseInformationForm,  request, null);
			}

		/*TODO need to move to controller} catch (SystemException se) {
			request.setAttribute(Constants.BENEFIT_DETAILS,
					new ParticipantBenefitBaseDetails());
			request.setAttribute(Constants.ACCOUNT_DETAILS,
					new ParticipantGiflData());
			request.setAttribute(Constants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
			
			// Log the system exception.
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);

			// Show user friendly message.
			error.clear();
			error.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, YES);
			
		}
	*/
	}

	
	
	
	
}
