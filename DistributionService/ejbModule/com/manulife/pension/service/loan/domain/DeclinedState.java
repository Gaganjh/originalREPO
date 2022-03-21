package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.LoanWarning;
import com.manulife.pension.service.loan.valueobject.Loan;

public class DeclinedState extends DefaultLoanState {

	@Override
	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException {
		super.validate(fromState, toState, context);
		Loan loan = context.getLoan();
		LoanValidationHelper.validateCurrentParticipantNote(loan.getErrors(),
				loan.getCurrentParticipantNote());
		if (loan.getFee() != null && loan.getFee().getValue() != null) {
		    if (loan.getFee().getValue().compareTo(BigDecimal.ZERO) > 0) {
                LoanWarning loanWarning = new LoanWarning(
                        LoanErrorCode.TPA_LOAN_ISSUE_FEE_MAY_BE_PAYABLE_BY_PARTICIPANT,
                        LoanField.TPA_LOAN_ISSUE_FEE);
                loan.getErrors().add(loanWarning);
            }
		}
	}

}
