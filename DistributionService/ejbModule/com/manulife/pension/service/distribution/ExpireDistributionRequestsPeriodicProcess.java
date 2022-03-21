package com.manulife.pension.service.distribution;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.security.SystemUser;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * Periodic process used to process expired distribution requests.
 */
public class ExpireDistributionRequestsPeriodicProcess implements
		PeriodicProcess {

	private static final Logger logger = Logger
			.getLogger(ExpireDistributionRequestsPeriodicProcess.class);

	private String distributionType;

	/**
	 * {@inheritDoc}
	 */
	public void execute() {

		logger.debug("Starting ExpireDistributionRequestsPeriodicProcess.");

		try {
			Integer profileId = SystemUser.SUBMISSION.getProfileId();

			if (LoanConstants.SUBMISSION_CASE_TYPE_CODE
					.equals(distributionType)) {
				/*
				 * needs to truncate the date because expiration date does not
				 * have a time component.
				 */
				Date checkDate = DateUtils.truncate(new Date(), Calendar.DATE);
				LoanServiceDelegate.getInstance().markExpiredLoans(checkDate,
						profileId);
			} else if (WithdrawalRequest.SUBMISSION_CASE_TYPE_CODE_WITHDRAWAL
                    .equals(distributionType)) {
				WithdrawalServiceDelegate.getInstance().markExpiredWithdrawals(
						new Date(), SystemUser.SUBMISSION.getProfileId());
			}
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} finally {
			logger.debug("Finished ExpireDistributionRequestsPeriodicProcess.");
		} // end try/catch/finally
	}

	/**
     * @param distributionType
     */
	public void setDistributionType(final String distributionType) {
		this.distributionType = distributionType;
	}

}
