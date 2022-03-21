package com.manulife.pension.service.withdrawal.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.util.WithdrawalReasonCodeEqualityPredicate;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.environment.valueobject.CodeEqualityPredicate;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * This class retrieves and manages the Look up data.
 * 
 * @author kuthiha
 * 
 */
public class WithdrawalLookupDataManager {

    private static final Logger logger = Logger.getLogger(WithdrawalLookupDataManager.class);

    private static final String PS_APPLICATION_ID = "PS";

    private final ContractInfo contractInfo;

    private final String participantStatusCode;

    private Collection<String> keys;

    public WithdrawalLookupDataManager(final ContractInfo contractInfo,
            final String participantStatusCode, final Collection<String> lookupKeys) {
        this.contractInfo = contractInfo;
        this.participantStatusCode = participantStatusCode;
        if (CollectionUtils.isEmpty(lookupKeys)) {
            keys = getAllKeys();
        } else {
            keys = lookupKeys;
        }

    }

    /**
     * 
     * Returns the look up data as a Map after applying the information available from the Contract
     * Service, if required.
     * 
     * @return Map
     */
    public Map getLookupData() {
        Map data;
        if (contractInfo == null || StringUtils.equals(StringUtils.EMPTY, participantStatusCode)) {
            data = lookupDataForView();
        } else {

            try {
                data = EnvironmentServiceDelegate.getInstance(PS_APPLICATION_ID)
                        .getLookupData(keys);
                if (!isCodeAvailable(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS)) {
                    data.put(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS,
                            getParticipantWithdrawalReasons());
                }

            } catch (final SystemException e) {

                throw ExceptionHandlerUtility.wrap(e);
            }
            final Collection<DeCodeVO> withdrawalReasons = filteredOnlineWithdrawalReasonsList(
                    contractInfo.getWithdrawalReasons(), (Collection<DeCodeVO>) data
                            .get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS));
            if (CollectionUtils.isNotEmpty(withdrawalReasons)) {
                data.put(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS, withdrawalReasons);
            }

            // Perform unvested option filtering if key was loaded
            if (keys.contains(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS)) {
                final Collection<DeCodeVO> unvestedOptions = filteredOnlineUnvestedMoneyOptions(
                        contractInfo.getUnvestedMoneyOptions(), (Collection<DeCodeVO>) data
                                .get(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS));
                if (CollectionUtils.isNotEmpty(unvestedOptions)) {
                    data.put(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS, unvestedOptions);
                }
                contractInfo
                        .setEffectiveDefaultUnvestedMoneyOptionCode(filterDefaultUnvestedMoneyOption(
                                contractInfo, (Collection<DeCodeVO>) data
                                        .get(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS)));
            }
        }

        return data;
    }

    /**
     * Returns the default unvested money option that should be used after filtering against the
     * filtered unvested money option list.
     * 
     * @return Map
     */
    public String filterDefaultUnvestedMoneyOption(final ContractInfo contractInfo,
            final Collection<DeCodeVO> unvestedOptions) {

        // Check if a default plan option exists and is an allowed option
        if (StringUtils.isNotBlank(contractInfo.getDefaultUnvestedMoneyOptionCode())
                && CollectionUtils.exists(unvestedOptions, new CodeEqualityPredicate(contractInfo
                        .getDefaultUnvestedMoneyOptionCode()))) {
            return contractInfo.getDefaultUnvestedMoneyOptionCode();
        } else if (CollectionUtils.exists(unvestedOptions, new CodeEqualityPredicate(
                WithdrawalRequest.UNVESTED_TRANSFER_TO_CASH_ACCOUNT_CODE))) {
            // Check if normal default (transfer to cash account) is an option
            return WithdrawalRequest.UNVESTED_TRANSFER_TO_CASH_ACCOUNT_CODE;
        } else {
            // Use blank as default
            return StringUtils.EMPTY;
        }
    }

    /**
     * This method is invoked to get data when no filter needs to be applied on the look up data.
     * Used for View only pages
     * 
     * @return
     */
    private Map lookupDataForView() {

        Map data;
        try {
            data = EnvironmentServiceDelegate.getInstance(PS_APPLICATION_ID).getLookupData(keys);
        } catch (final SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
        return data;
    }

    /**
     * Returns a collection of withdrawal reasons which are intersection of Online withdrawal
     * reasons and the withdrawal reasons available for the Contract.
     * 
     * @param contractReasons
     * @param lookupReasons
     * @return collection of withdrawal reasons
     */
    protected Collection<DeCodeVO> filteredOnlineWithdrawalReasonsList(
            final Collection<WithdrawalReason> contractReasons,
            final Collection<DeCodeVO> lookupReasons) {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "filteredOnlineWithdrawalReasonsList> Loaded contract reasons [").append(
                    contractReasons).append("] and online reasons [").append(lookupReasons).append(
                    "].").toString());
        }
        final Collection<DeCodeVO> reasons = new ArrayList<DeCodeVO>();
        if (CollectionUtils.isEmpty(contractReasons) || CollectionUtils.isEmpty(lookupReasons)) {
            return reasons;
        }
        for (final DeCodeVO reason : lookupReasons) {
            if (CollectionUtils.exists(contractReasons, new WithdrawalReasonCodeEqualityPredicate(
                    reason.getCode()))) {
                reasons.add(reason);
            }
        }

        return reasons;
    }

    private boolean isMandatoryTerminationWithdrawalReason(final String code) {
        if (StringUtils.equals(
                WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE, code)) {
            return true;
        }

        return false;
    }

    /**
     * Returns a collection of Unvested Money Options which are intersection of Online Unvested
     * Money Options and the Unvested money options available for the Contract.
     * 
     * @param fromContract
     * @param lookup
     * @return collection of unvested money options
     */
    private Collection<DeCodeVO> filteredOnlineUnvestedMoneyOptions(
            final Collection<DeCodeVO> optionsFromContract,
            final Collection<DeCodeVO> optionsFromLookup) {

        final Collection<DeCodeVO> filteredOptions = new ArrayList<DeCodeVO>();
        for (DeCodeVO option : optionsFromLookup) {
            if (CollectionUtils.exists(optionsFromContract, new CodeEqualityPredicate(option
                    .getCode()))) {
                filteredOptions.add(option);
            }
        }

        // If intersection is empty - use lookup options
        if (CollectionUtils.isEmpty(filteredOptions)) {
            return optionsFromLookup;
        } else {
            return filteredOptions;
        }
    }

    private Collection<String> getAllKeys() {
        final Collection<String> lookupKeys = new ArrayList<String>();

        // Step One
        lookupKeys.add(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS);
        lookupKeys.add(CodeLookupCache.LOAN_OPTION_TYPE);
        lookupKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
        lookupKeys.add(CodeLookupCache.HARDSHIP_REASONS);
        lookupKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
        lookupKeys.add(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE);

        // Step Two
        lookupKeys.add(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE);
        lookupKeys.add(CodeLookupCache.COUNTRY_COLLECTION_TYPE);
        lookupKeys.add(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE_BY_DESC);
        lookupKeys.add(CodeLookupCache.TPA_TRANSACTION_FEE_TYPE);
        lookupKeys.add(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS);
        lookupKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS);
        lookupKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);

        // added for the back button
        lookupKeys.add(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE);
        lookupKeys.add(CodeLookupCache.WITHDRAWAL_REQUEST_STATUS_ORDERED);
        return lookupKeys;

    }

    private boolean isCodeAvailable(final String code) {
        final Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            final String element = (String) iter.next();
            if (StringUtils.equals(code, element)) {
                return true;
            }
        }
        return false;

    }

    private Collection<DeCodeVO> getParticipantWithdrawalReasons() throws SystemException {
        final Collection<DeCodeVO> reasons = WithdrawalInfoDao.getParticipantWithdrawalReasons(
                contractInfo.getStatus(), participantStatusCode);
        return reasons;
    }

}
