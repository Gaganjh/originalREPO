package com.manulife.pension.service.loan.domain;

import java.util.List;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;

public class LoanPackageState extends DefaultLoanState {

	@Override
	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException {
		super.validate(fromState, toState, context);
		LoanPlanData loanPlanData = context.getLoanPlanData();
		LoanParticipantData loanParticipantData = context
				.getLoanParticipantData();
		LoanSettings loanSettings = context.getLoanSettings();

		Loan loan = context.getLoan();
        LoanValidationHelper.validateLoanReason(loan);
		LoanValidationHelper.validateLoanIssueFee(loan.getErrors(), loan,
				context.getLoanPlanData());
		LoanValidationHelper.validateLegallyMarriedInd(loan.getErrors(), loan
				.getLegallyMarriedInd(), loanPlanData);
		LoanValidationHelper.validateLoanTypeCode(loan.getErrors(), loan
				.getLoanType(), loanPlanData.getLoanTypeList());
		LoanValidationHelper.validateExpirationDate(loan);
		LoanValidationHelper.validatePayrollDate(context,
				LoanStateEnum.LOAN_PACKAGE);
		LoanValidationHelper.validateDefaultProvision(loan);
		LoanValidationHelper.validateCurrentOutstandingBalance(loan,
				loanParticipantData);
		LoanValidationHelper.validateMaxBalanceLast12Months(loan,
				loanParticipantData);
        LoanValidationHelper.validateOutstandingLoansCount(loan, 
                loanParticipantData, loanPlanData);
		validateLoanCalculationSection(loan, loanPlanData,
		        LoanStateEnum.LOAN_PACKAGE);
		validatePaymentSectionInvalidCharacter(loan);

		/*
		 * Validate Loan Request Submission
		 */
		List<LoanMessage> errors = loan.getErrors();
		LoanValidationHelper.validateParticipantLoans(errors, loan,
				loanParticipantData);
		LoanValidationHelper.validateAllowLoans(errors, loanSettings);
		
	}

}
