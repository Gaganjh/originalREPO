package com.manulife.pension.service.withdrawal.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.log.WithdrawalEvent;
import com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * PendingState is common to the pending states (pending review/pending approval).
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public abstract class PendingState extends BeforeEndState {

    private static final Logger logger = Logger.getLogger(PendingState.class);

    /**
     * {@inheritDoc}
     */
    void transitionToState(final Withdrawal withdrawal, final WithdrawalStateEnum newState) {

        switch (newState) {
            case DENIED:
                withdrawal.setWithdrawalState(WithdrawalStateFactory.getState(newState));
                break;

            default:
                super.transitionToState(withdrawal, newState);
        } // end case

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyDefaultDataForEdit(final Withdrawal withdrawal,
            final WithdrawalRequest defaultVo) throws SystemException {

        WithdrawalRequest withdrawalRequest = withdrawal.getWithdrawalRequest();

        withdrawalRequest.setFirstName(defaultVo.getFirstName());
        withdrawalRequest.setLastName(defaultVo.getLastName());
        withdrawalRequest.setParticipantSSN(defaultVo.getParticipantSSN());
        withdrawalRequest.setContractName(defaultVo.getContractName());

        // Refresh until End
        final Recipient recipient = withdrawalRequest.getRecipients().iterator()
                .next();
        recipient.setFirstName(defaultVo.getFirstName());
        recipient.setLastName(defaultVo.getLastName());

        // Refresh until End - show saved otherwise
        withdrawalRequest.setMostRecentPriorContributionDate(defaultVo
                .getMostRecentPriorContributionDate());

        for (WithdrawalRequestLoan csdbLoan : defaultVo.getLoans()) {
            boolean foundLoan = false;
            for (WithdrawalRequestLoan sdbLoan : withdrawalRequest.getLoans()) {
                if (sdbLoan.getLoanNo().equals(csdbLoan.getLoanNo())) {
                    sdbLoan.setOutstandingLoanAmount(csdbLoan.getOutstandingLoanAmount());
                    foundLoan = true;
                    break;
                }
            }
            if (!foundLoan) {
                withdrawalRequest.getLoans().add(csdbLoan);
            }
        }
        // remove any loans that are not in csdb loans
        Collection<WithdrawalRequestLoan> newLoans = new ArrayList<WithdrawalRequestLoan>();
        for (WithdrawalRequestLoan sdbLoan : withdrawalRequest.getLoans()) {
            for (WithdrawalRequestLoan csdbLoan : defaultVo.getLoans()) {
                if (csdbLoan.getLoanNo().equals(sdbLoan.getLoanNo())) {
                    newLoans.add(sdbLoan);
                }
            }
        }
        withdrawalRequest.setLoans(newLoans);

        updateMoneyTypesWithDefaultData(withdrawalRequest, defaultVo);

        updateMoneyTypeNames(withdrawalRequest, defaultVo);

        // set recipient last name/first name or org/trustee name based on Payment to values (not
        // saved until end)
        withdrawalRequest.setParticipantInfo(defaultVo.getParticipantInfo());
        withdrawalRequest.setContractInfo(defaultVo.getContractInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyDefaultDataForView(final Withdrawal withdrawal,
            final WithdrawalRequest defaultVo) throws SystemException {

        final WithdrawalRequest withdrawalRequest = withdrawal.getWithdrawalRequest();

        withdrawalRequest.setFirstName(defaultVo.getFirstName());
        withdrawalRequest.setLastName(defaultVo.getLastName());
        withdrawalRequest.setParticipantSSN(defaultVo.getParticipantSSN());
        withdrawalRequest.setContractName(defaultVo.getContractName());

        // For End states just update the Money type name (custom long name): WIW-128, WIW-256
        for (WithdrawalRequestMoneyType sdbMt : withdrawalRequest.getMoneyTypes()) {
            boolean foundMoneyType = false;

            for (WithdrawalRequestMoneyType csdbMT : defaultVo.getMoneyTypes()) {
                if (sdbMt.getMoneyTypeId().equals(csdbMT.getMoneyTypeId())) {
                    sdbMt.setMoneyTypeName(csdbMT.getMoneyTypeName());
                    sdbMt.setMoneyTypeAliasId(csdbMT.getMoneyTypeAliasId());
                    sdbMt.setIsPre1987MoneyType(csdbMT.getIsPre1987MoneyType());
                    foundMoneyType = true;
                    break;
                }
            }

            if (!foundMoneyType) {
                // If MT alias is missing then fill in from the generic MT alias collection
                sdbMt.setMoneyTypeName(defaultVo.getParticipantInfo().getMoneyTypeAliases().get(
                        sdbMt.getMoneyTypeId()));
            }
        }

        withdrawalRequest.setParticipantInfo(defaultVo.getParticipantInfo());
        withdrawalRequest.setContractInfo(defaultVo.getContractInfo());
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final Withdrawal withdrawal) throws DistributionServiceException {

        // Delete needs to validate differently depending on our origin
        if (BooleanUtils.isTrue(withdrawal.getWithdrawalRequest().getRequestInitiatedFromView())) {
            withdrawal.validateRequestHasExpiredByStateOrTime();
            withdrawal.validateRequestHasNotBeenDeleted();
        } else {
            withdrawal.validateRequestTimestampHasNotChangedOrExpiryTimePassed();
        }

        // We only perform the delete if no errors were found
        if (CollectionUtils.isEmpty(withdrawal.getWithdrawalRequest().getErrorCodes())) {
            transitionToState(withdrawal, WithdrawalStateEnum.DELETED);

            withdrawal.doSave();

            ActivityHistoryHelper.saveSummary(withdrawal, WithdrawalActivitySummary.ACTION_CODE_DELETED);

            final WithdrawalEvent withdrawalEvent;
            if (BooleanUtils
                    .isTrue(withdrawal.getWithdrawalRequest().getRequestInitiatedFromView())) {
                withdrawalEvent = WithdrawalEvent.DELETE_FROM_VIEW;
            } else {
                withdrawalEvent = WithdrawalEvent.DELETE_FROM_POST_DRAFT;
            } // fi

            WithdrawalLoggingHelper.logShort(withdrawal.getWithdrawalRequest().getSubmissionId(),
                    withdrawal.getWithdrawalRequest().getLastUpdatedById(), withdrawalEvent,
                    PendingState.class, "delete");

            withdrawal.afterEnteringDeletedState();
        }
    }
}
