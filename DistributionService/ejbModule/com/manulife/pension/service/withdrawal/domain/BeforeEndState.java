package com.manulife.pension.service.withdrawal.domain;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.AnyPredicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.common.WithdrawalRequestMoneyTypeByIdPredicate;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.SystemOfRecordValues;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * BeforeEndState is common to the states before approval.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public abstract class BeforeEndState extends DefaultWithdrawalState {

    private static final Logger logger = Logger.getLogger(BeforeEndState.class);

    /**
     * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#getSystemOfRecordValues(com.manulife.pension.service.withdrawal.domain.Withdrawal)
     */
    protected SystemOfRecordValues getSystemOfRecordValues(final Withdrawal withdrawal)
            throws DistributionServiceException {
        return ActivityHistoryHelper.getSystemOfRecordValues(withdrawal);
    }

    /**
     * {@inheritDoc}
     */
    public void cancel(final Withdrawal withdrawal) {
        transitionToState(withdrawal, getWithdrawalStateEnum());
    }

    /**
     * {@inheritDoc}
     */
    public void expire(final Withdrawal withdrawal) throws DistributionServiceException {
        transitionToState(withdrawal, WithdrawalStateEnum.EXPIRED);

        withdrawal.doSave();

        ActivityHistoryHelper.saveSummary(withdrawal, WithdrawalActivitySummary.ACTION_CODE_EXPIRED);
    }

    /**
     * {@inheritDoc}
     */
    void transitionToState(final Withdrawal withdrawal, final WithdrawalStateEnum newState) {

        switch (newState) {
            case APPROVED:
                // This flows into the case below (as there is no break).

            case EXPIRED:
                // This flows into the case below (as there is no break).

            case DELETED:
                withdrawal.setWithdrawalState(WithdrawalStateFactory.getState(newState));
                break;

            default:
                super.transitionToState(withdrawal, newState);
        } // end case
    }

    /**
     * Updates the given money type object with data from the source system.
     * 
     * @param withdrawalRequestMoneyType The object to update.
     * @param defaultDataMoneyType The data to update it with.
     */
    protected void updateMoneyTypeDataFromSource(
            final WithdrawalRequestMoneyType withdrawalRequestMoneyType,
            final WithdrawalRequestMoneyType defaultDataMoneyType) {
        withdrawalRequestMoneyType.setTotalBalance(defaultDataMoneyType.getTotalBalance());
        withdrawalRequestMoneyType.setIsPre1987MoneyType(defaultDataMoneyType
                .getIsPre1987MoneyType());
        withdrawalRequestMoneyType.setIsRolloverMoneyType(defaultDataMoneyType
                .getIsRolloverMoneyType());
        withdrawalRequestMoneyType.setIsVoluntaryContributionMoneyType(defaultDataMoneyType
                .getIsVoluntaryContributionMoneyType());
        withdrawalRequestMoneyType.setMoneyTypeCategoryCode(defaultDataMoneyType
                .getMoneyTypeCategoryCode());
        withdrawalRequestMoneyType.setMoneyTypeName(defaultDataMoneyType.getMoneyTypeName());
        withdrawalRequestMoneyType.setMoneyTypeAliasId(defaultDataMoneyType.getMoneyTypeAliasId());
    }

    /**
     * Updates the money type names, if they're null.
     * 
     * @param withdrawalRequest The {@link WithdrawalRequest} to update.
     * @param defaultVo The default data to apply.
     */
    protected void updateMoneyTypeNames(final WithdrawalRequest withdrawalRequest,
            final WithdrawalRequest defaultVo) {
        for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {
            // If MT alias is missing then fill in from the contract money type
            // long name
            // if the contract does not have the money type anymore, then use
            // the ID
            if (defaultVo.getParticipantInfo() != null
                    && defaultVo.getParticipantInfo().getMoneyTypeAliases() != null) {
                if (withdrawalRequestMoneyType.getMoneyTypeName() == null) {
                    String moneyTypeLongName = defaultVo.getParticipantInfo().getMoneyTypeAliases()
                            .get(withdrawalRequestMoneyType.getMoneyTypeId());
                    withdrawalRequestMoneyType.setMoneyTypeName(StringUtils
                            .isNotBlank(moneyTypeLongName) ? moneyTypeLongName
                            : withdrawalRequestMoneyType.getMoneyTypeId());
                } // fi
            } // fi
        } // end for
    }

    /**
     * Updates the Money Types with the given data.
     * 
     * @param withdrawalRequest The {@link WithdrawalRequest} to be updated.
     * @param defaultVo The data to update the withdrawal request with.
     */
    protected void updateMoneyTypesWithDefaultData(final WithdrawalRequest withdrawalRequest,
            final WithdrawalRequest defaultVo) {
        final Collection<WithdrawalRequestMoneyType> defaultMoneyTypes = defaultVo.getMoneyTypes();

        final WithdrawalRequestMoneyTypeByIdPredicate[] defaultPredicates = new WithdrawalRequestMoneyTypeByIdPredicate[defaultMoneyTypes
                .size()];

        int i = 0;
        for (WithdrawalRequestMoneyType defaultDataMoneyType : defaultMoneyTypes) {

            // Create a predicate that can match other money types by the ID.
            final WithdrawalRequestMoneyTypeByIdPredicate defaultMoneyTypePredicate = new WithdrawalRequestMoneyTypeByIdPredicate(
                    defaultDataMoneyType);
            // Keep the predicate for later use.
            defaultPredicates[i++] = defaultMoneyTypePredicate;

            // Get the money type from the request that matches the default data.
            final WithdrawalRequestMoneyType withdrawalRequestMoneyType = (WithdrawalRequestMoneyType) CollectionUtils
                    .find(withdrawalRequest.getMoneyTypes(), defaultMoneyTypePredicate);

            // Check if we already have this or if it's new.
            if (withdrawalRequestMoneyType == null) {
                // There is no request money type, for the source system money type, it's new.
                withdrawalRequest.getMoneyTypes().add(defaultDataMoneyType);
            } else {
                // We have a match, so we update the data.
                updateMoneyTypeDataFromSource(withdrawalRequestMoneyType, defaultDataMoneyType);
            } // fi

        } // end for

        // Selects all the money types that no longer have default values. This means that the
        // balance has gone to zero. So we need to update the totals.
        Collection<WithdrawalRequestMoneyType> noLongerExistingMoneyTypes = CollectionUtils
                .selectRejected(withdrawalRequest.getMoneyTypes(), new AnyPredicate(
                        defaultPredicates));

        for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : noLongerExistingMoneyTypes) {
            withdrawalRequestMoneyType.setTotalBalance(BigDecimal.ZERO);
        } // end for
    }
}
