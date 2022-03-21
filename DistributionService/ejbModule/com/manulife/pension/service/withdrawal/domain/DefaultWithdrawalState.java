/*
 * DefaultWithdrawalState.java,v 1.1 2006/09/25 19:15:28 Paul_Glenn Exp
 * DefaultWithdrawalState.java,v
 * Revision 1.1  2006/09/25 19:15:28  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.dao.ActivityDetailDao;
import com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao;
import com.manulife.pension.service.distribution.dao.ActivitySummaryDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.helper.ActivityComparator;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.util.WithdrawalSecurityHelper;
import com.manulife.pension.service.withdrawal.valueobject.Activity;
import com.manulife.pension.service.withdrawal.valueobject.ActivityHistory;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.SystemOfRecordValues;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivityDetail;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * DefaultWithdrawalState contains the default implementation of the state's methods. This should
 * never be instatiated, so it's created as abstract.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public abstract class DefaultWithdrawalState implements WithdrawalState {

    private final static String CLASS_NAME = DefaultWithdrawalState.class.toString();

    /**
     * (non-Javadoc).
     * 
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#readActivityHistory(com.manulife.pension.service.withdrawal.domain.Withdrawal)
     */
    public ActivityHistory readActivityHistory(Withdrawal withdrawal)
            throws DistributionServiceException {
        ActivityHistory returnHistory = new ActivityHistory();
        Collection<Activity> activities = new TreeSet<Activity>(new ActivityComparator());

        ActivitySummaryDao asDao = new ActivitySummaryDao();
        ActivityDetailDao adDao = new ActivityDetailDao();
        ActivityDynamicDetailDao addDao = new ActivityDynamicDetailDao();
        WithdrawalRequest wr = withdrawal.getWithdrawalRequest();
        Integer submissionId = wr.getSubmissionId();
        Integer contractId = wr.getContractId();
        Integer userProfileId = (int) wr.getPrincipal().getProfileId();
        
        // Added to get the user permissions
        try {
			ContractInfo contractInfo = WithdrawalServiceDelegate.getInstance().
						getContractInfo(wr.getContractId(), 
								wr.getPrincipal());
			boolean hasSigningAuthority = wr.getPrincipal().getRole()
					.hasPermission(PermissionType.SIGNING_AUTHORITY);
			contractInfo.setHasApprovePermission(hasSigningAuthority);
			withdrawal.getWithdrawalRequest().setContractInfo(contractInfo);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
        
        List<WithdrawalActivitySummary> summaries = (List<WithdrawalActivitySummary>)asDao.select(submissionId, contractId, userProfileId, WithdrawalActivitySummary.class);
        List<WithdrawalActivitySummary> filteredSummaries = new ArrayList<WithdrawalActivitySummary>();
        List<WithdrawalActivityDetail> details = (List<WithdrawalActivityDetail>)adDao.select(submissionId, contractId, userProfileId, WithdrawalActivityDetail.class);

        List<ActivityDynamicDetail> dynDetails = addDao.select(submissionId, contractId,
                userProfileId);
        SystemOfRecordValues systemOfRecordValues = getSystemOfRecordValues(withdrawal);

        activities.addAll(ActivityHistoryHelper.getWithdrawalActivities(withdrawal, details,
                systemOfRecordValues.getWithdrawalValues(), getWithdrawalStateEnum()));

        activities.addAll(ActivityHistoryHelper.getMoneyTypeActivities(withdrawal, dynDetails,
                systemOfRecordValues.getMoneyTypeValues(), getWithdrawalStateEnum()));

        activities.addAll(ActivityHistoryHelper.getPayeeActivities(withdrawal, dynDetails,
                getWithdrawalStateEnum()));

        activities.addAll(ActivityHistoryHelper.getDelarationActivities(withdrawal, dynDetails,
                getWithdrawalStateEnum()));

        for (WithdrawalActivitySummary summary : summaries) {
            if (!summary.getActionCode().trim()
                    .equals(WithdrawalActivitySummary.ACTION_CODE_SYSTEM_OF_RECORD)) {
                filteredSummaries.add(summary);
            }
        }
        returnHistory.setSummaries(filteredSummaries);
        returnHistory.setActivities(activities);
        return returnHistory;

    }

    /**
     * Gets the system of records value object to be used for comparison. This class is abstract
     * since the system of record is either 1. obtained form the current system of record for
     * non-end state requests 2. obtained from the database for end state requests.
     * 
     * @param withdrawal the current withdrawal request
     * @return the value object
     * @throws DistributionServiceException thrown if there is an error
     */
    protected abstract SystemOfRecordValues getSystemOfRecordValues(final Withdrawal withdrawal)
            throws DistributionServiceException;

    /**
     * Gets the {@link WithdrawalStateEnum} value for this state.
     * 
     * @return WithdrawalStateEnum The enum state value.
     */
    public abstract WithdrawalStateEnum getWithdrawalStateEnum();

    /**
     * {@inheritDoc}
     */
    public void save(final Withdrawal withdrawal) throws DistributionServiceException {
        throw new IllegalStateException("DefaultWithdrawalState#save");
    }

    /**
     * {@inheritDoc}
     */
    public void deny(final Withdrawal withdrawal) throws DistributionServiceException {
        throw new IllegalStateException("DefaultWithdrawalState#deny");

    }

    /**
     * {@inheritDoc}
     */
    public void sendForApproval(final Withdrawal withdrawal) throws DistributionServiceException {
        throw new IllegalStateException("DefaultWithdrawalState#sendForApproval");

    }

    /**
     * {@inheritDoc}
     */
    public void sendForReview(final Withdrawal withdrawal) throws DistributionServiceException {
        throw new IllegalStateException("DefaultWithdrawalState#sendForReview");

    }

    /**
     * {@inheritDoc}
     */
    public void approve(final Withdrawal withdrawal) throws DistributionServiceException {
        throw new IllegalStateException("DefaultWithdrawalState#approve");
    }

    /**
     * {@inheritDoc}
     */
    public void processApproved(final Withdrawal withdrawal) throws DistributionServiceException {
        throw new IllegalStateException("DefaultWithdrawalState#processApproved");

    }

    /**
     * {@inheritDoc}
     */
    public void cancel(final Withdrawal withdrawal) {
        throw new IllegalStateException("DefaultWithdrawalState#cancel");

    }

    /**
     * {@inheritDoc}
     */
    public void delete(final Withdrawal withdrawal) throws DistributionServiceException {
        throw new IllegalStateException("DefaultWithdrawalState#delete");

    }

    /**
     * {@inheritDoc}
     */
    public void expire(final Withdrawal withdrawal) throws DistributionServiceException {
        throw new IllegalStateException("DefaultWithdrawalState#expire");

    }

    /**
     * {@inheritDoc}
     */
    public void applyDefaultDataForEdit(final Withdrawal withdrawal,
            final WithdrawalRequest withdrawalRequest) throws SystemException {
        throw new IllegalStateException("DefaultWithdrawalState#applyDefaultData");
    }

    /**
     * {@inheritDoc}
     */
    public void applyDefaultDataForView(final Withdrawal withdrawal,
            final WithdrawalRequest withdrawalRequest) throws SystemException {
        throw new IllegalStateException("DefaultWithdrawalState#applyDefaultData");
    }

    /**
     * transitionToState moves to a new State.
     * 
     * @param withdrawal The withdrawal to transition to the new state.
     * @param newState The state to move to.
     */
    void transitionToState(final Withdrawal withdrawal, final WithdrawalStateEnum newState) {
        throw new IllegalStateException(new StringBuffer().append(
                "Invalid state transition: From State: ").append(this.getWithdrawalStateEnum())
                .append(" to State: ").append(newState).toString());
    }

    /**
     * {@inheritDoc}
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
