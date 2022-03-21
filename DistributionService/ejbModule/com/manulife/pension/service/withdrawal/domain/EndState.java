package com.manulife.pension.service.withdrawal.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.dao.ActivityDetailDao;
import com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.withdrawal.helper.MoneyTypeFieldDef;
import com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivityDetail;
import com.manulife.pension.service.withdrawal.valueobject.SystemOfRecordValues;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * EndState is common to all the final states (approved, denied, deleted, expired, etc).
 * 
 * @author Aurelian_Penciu
 * @version 1.1 2007/01/10 11:20:00
 */
public abstract class EndState extends DefaultWithdrawalState {

    private static final Logger logger = Logger.getLogger(EndState.class);

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
     * (non-Javadoc).
     * 
     * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#getSystemOfRecordValues(com.manulife.pension.service.withdrawal.domain.Withdrawal)
     */
    protected SystemOfRecordValues getSystemOfRecordValues(Withdrawal withdrawal)
            throws DistributionServiceException {
        ActivityDetailDao detailDao = new ActivityDetailDao();
        ActivityDynamicDetailDao dynDetailDao = new ActivityDynamicDetailDao();
        Integer contractId = withdrawal.getWithdrawalRequest().getContractId();
        Integer submissionId = withdrawal.getWithdrawalRequest().getSubmissionId();
        Integer userProfileId = (int) withdrawal.getWithdrawalRequest().getPrincipal()
                .getProfileId();
        Collection<WithdrawalActivityDetail> details = (List<WithdrawalActivityDetail>)detailDao.selectSystemOfRecord(contractId,
                submissionId, userProfileId, WithdrawalActivityDetail.class);
        Collection<ActivityDynamicDetail> dynDetails = dynDetailDao.selectSystemOfRecord(
                contractId, submissionId, userProfileId);
        SystemOfRecordValues returnValues = new SystemOfRecordValues();
        Map<WithdrawalFieldDef, String> withdrawalValues = new HashMap<WithdrawalFieldDef, String>();
        Map<String, Map<MoneyTypeFieldDef, String>> moneyTypeValues = new HashMap<String, Map<MoneyTypeFieldDef, String>>();

        for (ActivityDetail detail : details) {
            if (WithdrawalFieldDef.isDynamicField(detail.getItemNumber())) {
                continue;
            }
            WithdrawalFieldDef field = WithdrawalFieldDef.getFieldFromItemNumber(detail
                    .getItemNumber());
            withdrawalValues.put(field, detail.getValue());
        }
        for (ActivityDynamicDetail dynDetail : dynDetails) {
            if (dynDetail.getItemNumber().equals(WithdrawalFieldDef.DYN_MONEY_TYPE)) {
                Map<MoneyTypeFieldDef, String> fieldValueMap;
                if (moneyTypeValues.get(dynDetail.getSecondaryName()) == null) {
                    moneyTypeValues.put(dynDetail.getSecondaryName(),
                            new HashMap<MoneyTypeFieldDef, String>());
                }
                fieldValueMap = moneyTypeValues.get(dynDetail.getSecondaryName());
                fieldValueMap.put(MoneyTypeFieldDef.getFieldFromItemNumber(dynDetail
                        .getSecondaryNumber()), dynDetail.getValue());
            }
        }

        returnValues.setMoneyTypeValues(moneyTypeValues);
        returnValues.setWithdrawalValues(withdrawalValues);

        return returnValues;
    }
}
