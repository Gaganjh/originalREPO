package com.manulife.pension.service.withdrawal.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.ParticipantDataValueObject;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.PlanDataWithdrawalVO;
import com.manulife.pension.service.environment.util.LookupDataHelper;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.utility.SecurityHelper;
import com.manulife.pension.service.withdrawal.WithdrawalService;
import com.manulife.pension.service.withdrawal.dao.WithdrawalDao;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;

/**
 * Provides a common area for loading and manipulating Withdrawal data that can be used from either
 * the Service Bean or the domain object.
 * 
 * @author dickand
 */
public class WithdrawalDataManager {

    // Application ID (required by the EmployeeService)
    private static final String PS_APPLICATION_ID = "PS";

    private static final Logger logger = Logger.getLogger(WithdrawalDataManager.class);

    /**
     * Default constructor - private for Singleton purposes.
     */
    private WithdrawalDataManager() {
    }

    /**
     * Returns an ordered list of WD reasons. The order in which the codes are returned is the
     * specified display order.
     * 
     * @param participantInfo The paricipant info to filter the payment to options with.
     * @return Collection - An ordered list of withdrawal reasons matching the participant
     *         information.
     */
    public static Collection getParticipantPaymentToOptions(final ParticipantInfo participantInfo) {

        final Collection<String> lookupDataKeys = new ArrayList<String>();
        lookupDataKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
        try {
            // Get base set of payment to options
            final Map map = EnvironmentServiceDelegate.getInstance(PS_APPLICATION_ID)
                    .getLookupData(lookupDataKeys);
            Collection paymentToCollection = (Collection) map.get(CodeLookupCache.PAYMENT_TO_TYPE);

            // Check for after tax money
            if (!participantInfo.getHasAfterTaxContributions()) {

                final Collection<String> filterKeys = new ArrayList<String>();
                filterKeys
                        .add(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
                filterKeys
                        .add(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);

                paymentToCollection = LookupDataHelper.filterLookupData(paymentToCollection,
                        filterKeys.toArray(new String[0]), LookupDataHelper.REMOVE_INDICATOR);
            }

            // Check for check payable to trustee
            if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, participantInfo
                    .getChequePayableToCode())) {

                final Collection<String> filterKeys = new ArrayList<String>();
                filterKeys.add(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
                paymentToCollection = LookupDataHelper.filterLookupData(paymentToCollection,
                        filterKeys.toArray(new String[0]), LookupDataHelper.RETAIN_INDICATOR);
            }

            // Check for MTA request
            if (participantInfo.getIsMTAContract()) {

                final Collection<String> filterKeys = new ArrayList<String>();
                filterKeys.add(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
                paymentToCollection = LookupDataHelper.filterLookupData(paymentToCollection,
                        filterKeys.toArray(new String[0]), LookupDataHelper.REMOVE_INDICATOR);
            }

            // remove Rollover to JH IRA
            final Collection<String> filterKeys = new ArrayList<String>();
            filterKeys.add(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE);
            paymentToCollection = LookupDataHelper.filterLookupData(paymentToCollection, filterKeys
                    .toArray(new String[0]), LookupDataHelper.REMOVE_INDICATOR);

            return paymentToCollection;

        } catch (SystemException se) {
            throw ExceptionHandlerUtility.wrap(se);
        }
    }

    /**
     * lookupContractInfo looks up details for the contract that are used by the various withdrawal
     * service methods.
     * 
     * @param contractId The contract to find information about.
     * @param principal The principal of the logged in user.
     * @return {@link ContractInfo} The contract data.
     */
    public static ContractInfo loadContractInfo(final Integer contractId, final Principal principal) {

        final ContractInfo contractInfo = new ContractInfo();
        contractInfo.setContractId(contractId);

        final ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate
                .getInstance();

        final UserRole userRole = principal.getRole();
        final Contract contract;
        try {
            final int contractDiDuration = EnvironmentServiceDelegate.getInstance()
            		.retrieveContractDiDuration(userRole, contractId,null);
            contract = contractServiceDelegate.getContractDetails(contractId, contractDiDuration);
            contractInfo.setTpaStaffPlanIndicator(contract.isTpaStaffPlanInd());
            contractInfo.setIsBundledGaIndicator(contract.isBundledGaIndicator());
        } catch (ContractNotExistException contractNotExistException) {
            throw new RuntimeException(contractNotExistException);
        } catch (SystemException systemException) {
            throw new RuntimeException(systemException);
        } // end try/catch

        try {
            ContractServiceFeature feature = contractServiceDelegate.getContractServiceFeature(
                    contractId, ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
            contractInfo.setCsfAllowOnlineWithdrawals(ContractServiceFeature
                    .internalToBoolean(feature.getValue()));
            contractInfo.setTwoStepApprovalRequired(ContractServiceFeature
                    .internalToBoolean(feature
                            .getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW)));
            contractInfo.setInitiatorMayApprove(ContractServiceFeature.internalToBoolean(feature
                    .getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_APPROVE)));
            contractInfo.setCsfIRSSpecialTaxNotice(ContractServiceFeature.internalToBoolean(feature
                    .getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_SPECIAL_TAX)));

            // for loans service feature
            LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance().getLoanDataHelper();
            LoanSettings loanSettings = loanDataHelper.getLoanSettings(contractId);
            if (loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()) {
                contractInfo.setCsfAllowOnlineLoans(true);
            }
            contractInfo.setInitiatorMayApproveLoans(loanSettings.isInitiatorCanApproveLoan());

            final PlanDataWithdrawalVO planDataWithdrawalVO = contractServiceDelegate
                    .getPlanDataWithdrawal(contractId);

            contractInfo.setSpousalConsentRequired(planDataWithdrawalVO.getSpouseConsentRequired());
            contractInfo.setNormalRetirementAge(planDataWithdrawalVO.getNormalRetireAge());
            contractInfo.setEarlyAgeRetirement(planDataWithdrawalVO.getEarlyRetireAge());
            contractInfo.setEarlyRetirementAllowed(planDataWithdrawalVO.getEarlyRetireAllowed());
            contractInfo.setTrusteeAddress(planDataWithdrawalVO.getTrusteeAddress());
            contractInfo.setWithdrawalReasons(planDataWithdrawalVO.getWithdrawalReasons());
            contractInfo.setUnvestedMoneyOptions(planDataWithdrawalVO.getUnvestedMoneyOptions());
            contractInfo.setFullyVestedWithdrawalReasons(planDataWithdrawalVO
                    .getFullyVestedWithdrawalReasons());

            final ContactVO contactDetails = contractServiceDelegate.getContactsDetail(contractId);

            contractInfo.setContractHasRothEnabled(contract.hasRothNoExpiryCheck());
            contractInfo.setContractHasPbaEnabled(contract.isPBA());
            contractInfo.setClientAccountRepId(contactDetails.getCarId());
            contractInfo.setClientAccountRepEmail(contactDetails.getEmail());

            final TPAFirmInfo tpaFirmInfo = TPAServiceDelegate.getInstance()
                    .getFirmInfoByContractId(contractId);

            contractInfo.setHasATpaFirm(tpaFirmInfo != null ? Boolean.TRUE : Boolean.FALSE);
            contractInfo.setStatus(contract.getStatus());
            contractInfo.setStatusIsAcOrCf(StringUtils.equals(ContractInfo.CONTRACT_STATUS_ACTIVE,
                    contract.getStatus())
                    || StringUtils
                            .equals(ContractInfo.CONTRACT_STATUS_FROZEN, contract.getStatus()));

            contractInfo.setDefinedBenefits(contract.isDefinedBenefitContract());
            contractInfo.setMailChequeToAddressIndicator(!StringUtils.equals(
                    ContractInfo.CHECK_MAILED_TO_CODE_PAYEE, contract.getCheckMailedToCode()));
            contractInfo.setTeamCode(contactDetails.getTeamCode());

        } catch (final ApplicationException e) {
            throw new RuntimeException(e);
        } catch (final SystemException e) {
            throw new RuntimeException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("loadContractInfo> Loaded contract information [")
                    .append(contractInfo).append("] for contract [").append(contractId).append("]")
                    .toString());
        }

        return contractInfo;
    }

    /**
     * This methods returns the enrolment date of the particpant.
     * 
     * @param request
     * @return date
     */
    public static Date getParticipantEnrollmentDate(final Integer profileId,
            final Integer contractId) {
        ParticipantDataValueObject participant = null;
        Date enrollmentDate = null;
        CustomerServicePrincipal principal = new CustomerServicePrincipal();
        String[] roles = new String[1];
        roles[0] = CustomerServicePrincipal.ROLE_PARTICIPANT;
        principal.setRoles(roles);
        principal.setName(roles[0]);
        try {
            participant = AccountServiceDelegate.getInstance().getParticipantDataValueObject(
                    principal, profileId.toString(), contractId.toString());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "getParticipantEnrollmentDate> Loaded participant data with profile id [")
                        .append(profileId).append("] and contract ID [").append(contractId).append(
                                "].  Participant data is [").append(participant).append("]")
                        .toString());
            }
            if (participant != null) {
                enrollmentDate = participant.getMfcContractEnrollmentDate();
            }
        } catch (AccountException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (Exception e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
        return enrollmentDate;
    }

    /**
     * @see WithdrawalService#getWithdrawalInfo
     * 
     * @param participantId The participant ID to use.
     * @param contractId The contract ID to use.
     * @return WithdrawalInfo The data that was found.
     */
    public static WithdrawalInfo getWithdrawalInfo(final int participantId, final int contractId) {
        try {
            final WithdrawalInfo info = WithdrawalInfoDao.getWithdrawalInfo(participantId,
                    contractId);
            return info;
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
    }

    /**
     * @see WithdrawalService#getParticipantInfo
     * 
     * @param profileId The profile ID to use.
     * @param participantId The participant ID to use.
     * @param contractId The contract ID to use.
     * @return ParticipantInfo The data that was found.
     */
    public static ParticipantInfo getParticipantInfo(final int profileId, final int participantId,
            final int contractId) {
        try {
            final ParticipantInfo participantInfo = WithdrawalInfoDao.getParticipantInfo(
                    (long) participantId, contractId);
            participantInfo.setParticipantEnrollmentDate(WithdrawalDataManager
                    .getParticipantEnrollmentDate(profileId, contractId));

            return participantInfo;
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
    }

    /**
     * This method looks up the meta data for the given submission.
     * 
     * @param submissionId - The submission ID to look up the meta data for.
     * @return {@link WithdrawalRequestMetaData} - The meta data for the given submissionId.
     */
    public static WithdrawalRequestMetaData getWithdrawalRequestMetaData(final Integer submissionId) {
        try {
            return WithdrawalDao.getWithdrawalRequestMetaData(submissionId);
        } catch (DAOException daoException) {
            throw ExceptionHandlerUtility.wrap(daoException);
        } // end try/catch
    }
}
