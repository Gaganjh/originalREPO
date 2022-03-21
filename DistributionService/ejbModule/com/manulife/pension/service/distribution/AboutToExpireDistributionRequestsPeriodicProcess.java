package com.manulife.pension.service.distribution;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.security.SystemUser;

public class AboutToExpireDistributionRequestsPeriodicProcess implements
		PeriodicProcess {

	private static final Logger logger = Logger
			.getLogger(AboutToExpireDistributionRequestsPeriodicProcess.class);

	private String distributionType;

	/**
	 * {@inheritDoc}
	 */
	public void execute() {

		logger
				.debug("Starting AboutToExpireDistributionRequestsPeriodicProcess.");

		try {
			Integer profileId = SystemUser.SUBMISSION.getProfileId();

			if (LoanConstants.SUBMISSION_CASE_TYPE_CODE
					.equals(distributionType)) {
				int aboutToExpireReminderDays = LoanDefaults
						.getAboutToExpireReminderDays();
				List<Loan> loanRequests = (List<Loan>) LoanServiceDelegate
						.getInstance().getAboutToExpireLoanRequests(
								aboutToExpireReminderDays);
				if (loanRequests.size() > 0) {
					for (Loan loan : loanRequests) {
						if (loan != null) {
							LoanEventGenerator eventGenerator = EventFactory
									.getInstance().getLoanEventGenerator(
											loan.getContractId(),
											loan.getSubmissionId(), profileId);
							eventGenerator.prepareAndSendAboutToExpireEvent(
									loan.getParticipantProfileId(), loan
											.getStatus(), aboutToExpireReminderDays);
							logger.log(Level.DEBUG,
									"Successfully triggered AboutToExpireEvent for : "
											+ loan.getSubmissionId());
						}
					}
				}
			} else if ("W".equals(distributionType)) {
				// place holder for withdrawal about expire event event
				// generation
			}
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} finally {
			logger
					.debug("Finished AboutToExpireDistributionRequestsPeriodicProcess.");
		} // end try/catch/finally
	}

	public void setDistributionType(String distributionType) {
		this.distributionType = distributionType;
	}

}
