package com.manulife.pension.service.loan.domain;

import java.util.Collection;
import java.util.List;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;

public class ApprovedState extends DefaultLoanState {

	@Override
	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException {
		super.validate(fromState, toState, context);
		LoanPlanData loanPlanData = context.getLoanPlanData();
		Loan loan = context.getLoan();
		LoanParticipantData loanParticipantData = context
				.getLoanParticipantData();
		LoanSettings loanSettings = context.getLoanSettings();
        List<LoanMessage> errors = loan.getErrors();
		
        LoanValidationHelper.validateLoanReason(loan);
		LoanValidationHelper.validateLoanIssueFee(errors, loan,
				loanPlanData);
		
		LoanValidationHelper.validateExpirationDate(loan);
		LoanValidationHelper.validatePayrollDate(context, LoanStateEnum.APPROVED);
		LoanValidationHelper.validateParticipantStatusForApproval(errors,
		        loanParticipantData);
		LoanValidationHelper.validateContractStatusForApproval(
				errors, loanPlanData);
		/*
		 * Validate only if the current state is DRAFT or Pending Review
		 */
		if (LoanStateEnum.DRAFT.equals(fromState)
				|| LoanStateEnum.PENDING_REVIEW.equals(fromState)) {
			LoanValidationHelper.validateCurrentOutstandingBalance(loan,
					loanParticipantData);
			LoanValidationHelper.validateMaxBalanceLast12Months(loan,
					loanParticipantData);
		}
		LoanValidationHelper.validateOutstandingLoansCount(loan,
				loanParticipantData, loanPlanData);
		validateLoanCalculationSection(loan, loanPlanData, 
		        LoanStateEnum.APPROVED);
		validatePaymentSectionInvalidCharacter(loan);
		
		if(!loan.isBundledContract() || (loan.isBundledContract() && !loan.isSigningAuthorityForContractTpaFirm())){
			LoanValidationHelper.validateAddressPaymentInfo(errors, loan);
			LoanValidationHelper.validateLegallyMarriedInd(errors, loan
					.getLegallyMarriedInd(), loanPlanData);
		}
		
			LoanValidationHelper.validateDeclarationsAccepted(errors, loan, true);
			
		/*
		 * Validate Loan Request Submission
		 */
		LoanValidationHelper.validateParticipantLoans(errors, loan,
				loanParticipantData);
		LoanValidationHelper.validateAllowLoans(errors, loanSettings);
		
		// check for participant is applicable to LIA
		LoanValidationHelper.validateLIA(errors, loan);
	}

	

	@Override
	public Loan complete(Loan loan) throws DistributionServiceException {
		loan.setStatus(LoanStateEnum.COMPLETED.getStatusCode());
		LoanStateContext context = getLoanStateContext(loan);
		return save(context);
	}
}
