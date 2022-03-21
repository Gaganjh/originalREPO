package com.manulife.pension.service.loan.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.exception.LoanValidationException;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.util.BusinessCalendar;
import com.manulife.pension.util.Pair;

public class PendingAcceptanceState extends DefaultLoanState {

	@Override
	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException {
		super.validate(fromState, toState, context);
		Loan loan = context.getLoan();
		LoanParticipantData loanParticipantData = context
				.getLoanParticipantData();
		LoanPlanData loanPlanData = context.getLoanPlanData();
		LoanSettings loanSettings = context.getLoanSettings();

		LoanValidationHelper.validateLoanIssueFee(loan.getErrors(), loan,
				context.getLoanPlanData());
		LoanValidationHelper.validateExpirationDate(loan);
		LoanValidationHelper.validatePayrollDate(context,
				LoanStateEnum.PENDING_ACCEPTANCE);
		LoanValidationHelper.validateDefaultProvision(loan);
		LoanValidationHelper.validateCurrentOutstandingBalance(loan,
				loanParticipantData);
		LoanValidationHelper.validateMaxBalanceLast12Months(loan,
				loanParticipantData);
        LoanValidationHelper.validateOutstandingLoansCount(loan, 
                loanParticipantData, loanPlanData);
		validateLoanCalculationSection(loan, loanPlanData,
                LoanStateEnum.PENDING_ACCEPTANCE);
		validatePaymentSectionInvalidCharacter(loan);

		/*
		 * Validate Loan Request Submission
		 */
		List<LoanMessage> errors = loan.getErrors();
		LoanValidationHelper.validateParticipantLoans(errors, loan,
				loanParticipantData);
		LoanValidationHelper.validateAllowLoans(errors, loanSettings);
		
		// check for participant is applicable to LIA
		LoanValidationHelper.validateLIA(errors, loan);
	}

	@Override
	public Loan sendForApproval(Loan loan) throws DistributionServiceException {
	    
	    if ( ! LoanValidationHelper.isParticipantAllowedToSendForApproval(loan)) {
	        throw new LoanValidationException("Employee function shutdown is in effect."
	                + " Send for approval stopped. Contract=" + loan.getContractId()
	                + " ProfileId=" + loan.getParticipantProfileId()
	                );
	    };
        
		loan.setStatus(LoanStateEnum.PENDING_APPROVAL.getStatusCode());
		LoanStateContext context = getLoanStateContext(loan);
        setCommonLoanValues(context);
		loan = save(context);
		
		if (loan.isOK()) {
			
			// Triggering LoanRequestPendingApproval Event
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			if (LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(loan
					.getCreatedByRoleCode())) {
				loanEventGenerator
						.prepareAndSendParticipantPendingApprovalEvent(loan
								.getParticipantProfileId());
			} else {
				loanEventGenerator.prepareAndSendPendingApprovalEvent(loan
						.getParticipantProfileId());
			}
		}
		return loan;
	}

	public Loan reject(Loan loan) throws DistributionServiceException {
		loan.setStatus(LoanStateEnum.REJECTED.getStatusCode());
		LoanStateContext context = getLoanStateContext(loan);
        setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
			// Triggering LoanRequestRejected Event
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			loanEventGenerator
					.prepareAndSendParticipantRejectedEvent(loan
							.getParticipantProfileId());
		}
		return loan;
	}

	@Override
	public void populate(Loan loan) throws DistributionServiceException {
	    LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
	        .getLoanDataHelper();
		LoanStateContext context = getLoanStateContext(loan);

	    Integer contractId = loan.getContractId();
	    Integer participantProfileId = loan.getParticipantProfileId();
	    Pair<List<LoanMoneyType>, EmployeeVestingInformation> participantMoneyTypes = 
	        loanDataHelper.getParticipantMoneyTypesForLoans(contractId,
	            participantProfileId);
	    loan.setEmployeeVestingInformation(participantMoneyTypes.getSecond());
		loan.setEffectiveDate(getEstimatedEffectiveDate(context));
        loan.setMaturityDate(getEstimatedMaturityDate(loan));
		super.populate(loan);
	}

}
