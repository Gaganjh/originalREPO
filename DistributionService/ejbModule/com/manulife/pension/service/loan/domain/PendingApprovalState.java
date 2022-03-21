package com.manulife.pension.service.loan.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.AtRiskHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.log.DistributionEventEnum;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.RequestType;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.entity.SecurityActivity.SecurityActivityTypeCode;
import com.manulife.pension.service.synchronization.SynchronizationConstants;
import com.manulife.pension.service.synchronization.dao.SynchronizationDAO;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.Pair;

public class PendingApprovalState extends DefaultLoanState {

	@Override
	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException {
		super.validate(fromState, toState, context);
		Loan loan = context.getLoan();
		
		LoanParticipantData loanParticipantData = context
				.getLoanParticipantData();
		LoanSettings loanSettings = context.getLoanSettings();
		LoanPlanData loanPlanData = context.getLoanPlanData();
        LoanValidationHelper.validateLoanReason(loan);
		LoanValidationHelper.validateLoanIssueFee(loan.getErrors(), loan,
				loanPlanData);
		LoanValidationHelper.validateExpirationDate(loan);
		LoanValidationHelper.validatePayrollDate(context,
				LoanStateEnum.PENDING_APPROVAL);
		LoanValidationHelper.validateDefaultProvision(loan);
		LoanValidationHelper.validateCurrentOutstandingBalance(loan,
				loanParticipantData);
		LoanValidationHelper.validateMaxBalanceLast12Months(loan,
				loanParticipantData);
		LoanValidationHelper.validateOutstandingLoansCount(loan,
				loanParticipantData, loanPlanData);
		validateLoanCalculationSection(loan, loanPlanData,
                LoanStateEnum.PENDING_APPROVAL);
		validatePaymentSectionInvalidCharacter(loan);

		/*
		 * Validate Loan Request Submission
		 */
		List<LoanMessage> errors = loan.getErrors();
		LoanValidationHelper.validateParticipantLoans(errors, loan,
				loanParticipantData);
		LoanValidationHelper.validateAllowLoans(errors, loanSettings);
		
		if(loan.isBundledContract() && loan.isSigningAuthorityForContractTpaFirm()){
			LoanValidationHelper.validateAddressPaymentInfo(errors, loan);
			LoanValidationHelper.validateLegallyMarriedInd(errors, loan
					.getLegallyMarriedInd(), loanPlanData);
		}

		if (loan.isBundledContract()
				&& loan.isSigningAuthorityForContractTpaFirm()) {
			LoanValidationHelper.validateDeclarationsAccepted(errors, loan,
					false);
		}
		
		// check for participant is applicable to LIA
		LoanValidationHelper.validateLIA(errors, loan);
		
	}

	@Override
	public Loan approve(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		loan.setEffectiveDate(getEffectiveDateForApproval(context));
        loan.setMaturityDate(getEstimatedMaturityDate(loan));

		validateToState(LoanStateEnum.PENDING_APPROVAL, LoanStateEnum.APPROVED,
				context);
		if (!loan.isOK()) {
			return loan;
		}

		loan.setStatus(LoanStateEnum.APPROVED.getStatusCode());
		if (JdbcHelper.INDICATOR_NO.equals(context.getLoanPlanData()
		        .getSpousalConsentReqdInd())) {
		    loan.setLegallyMarriedInd(null);      // Indicates unspecified
		}
		if (!loan.isParticipantInitiated()) {
			setAdditionalPlanAndContractData(loan);
		}

        setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
            saveActivityHistory(LoanStateEnum.PENDING_APPROVAL,
                    LoanStateEnum.APPROVED, context, loan
                            .getLastUpdated());
			logToMrl(DistributionEventEnum.APPROVE_FROM_APPROVAL
					.getEventName(), context);
			
			// Triggering the LoanRequestApprovedEvent event.
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			loanEventGenerator.prepareAndSendApprovedEvent(loan
					.getParticipantProfileId(), loan.getLoginRoleCode(), loan
							.getLastFeeChangedWasPlanSponsorUserInd());
			
			try {
				SynchronizationDAO.addSynchronizationTrail(
						String.valueOf(loan.getSubmissionId()),String.valueOf(loan.getContractId())
						,SynchronizationConstants.SYNCHRONIZATION_TYPE_LOAN);
			} catch (SystemException e) {	
				throw new DistributionServiceDaoException("Unable to insert synchronization trail", e);				
			}
		}
		return loan;

	}

	@Override
	public Loan decline(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		validateToState(LoanStateEnum.PENDING_APPROVAL, LoanStateEnum.DECLINED,
				context);
		if (!loan.isOK()) {
			return loan;
		}
		loan.setStatus(LoanStateEnum.DECLINED.getStatusCode());

        setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
            saveActivityHistory(LoanStateEnum.PENDING_APPROVAL,
                    LoanStateEnum.DECLINED, context, loan
                            .getLastUpdated());
			logToMrl(DistributionEventEnum.DENY_FROM_APPROVAL.getEventName(), 
			        context);

			//Triggering LoanRequestDenied Event.
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			loanEventGenerator.prepareAndSendDeniedEvent(loan
					.getParticipantProfileId(), loan.getPreviousStatus());
		}
		return loan;
	}

	@Override
	public void populate(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
		.getLoanDataHelper();

		Integer contractId = loan.getContractId();
		Integer participantProfileId = loan.getParticipantProfileId();
		Pair<List<LoanMoneyType>, EmployeeVestingInformation> participantMoneyTypes = loanDataHelper
		.getParticipantMoneyTypesForLoans(contractId,
				participantProfileId);
		loan.setEmployeeVestingInformation(participantMoneyTypes.getSecond());
		loan.setEffectiveDate(getEffectiveDateForApproval(context));
		loan.setMaturityDate(getEstimatedMaturityDate(loan));
		populateMessages(context);

		if(loan.isParticipantInitiated()){
			boolean isAtRisk = false;

			try {
				AtRiskDetailsVO atRiskDetailsVO = null;
				AtRiskDetailsInputVO atRiskDetils = new AtRiskDetailsInputVO();
				atRiskDetils.setContractId(loan.getContractId());
				atRiskDetils.setProfileId(loan.getParticipantProfileId());
				atRiskDetils.setSubmissionId(loan.getSubmissionId());
				atRiskDetils.setLoanOrWithdrawalReq(RequestType.LOAN);

				AtRiskAddressChangeVO atRiskAddressChangeVO = null;
				boolean isWebRegAtRisk = AtRiskHandler.getInstance().isRegistrationAtRiskPeriod(atRiskDetils);
				
				if(isWebRegAtRisk ){
					if(atRiskDetailsVO == null){
						atRiskDetailsVO = new AtRiskDetailsVO();
						atRiskDetailsVO.setWebRegistration(AtRiskHandler.getInstance().getWebRigistrationInfo(atRiskDetils));
					}
					//We should have get the WebRegistrationVO from DISTRIBUTION_AT_RISK_DETAIL
					if(atRiskDetailsVO.getWebRegistration() != null){
						atRiskDetailsVO.getWebRegistration().setWebRegAtRiskPeriod(isWebRegAtRisk);
					}
					atRiskAddressChangeVO = AtRiskHandler.getInstance().getCurrentAddress(atRiskDetils);
					
					atRiskDetailsVO.setAddresschange(atRiskAddressChangeVO);
					isAtRisk = true;
				}
				
				ArrayList <AtRiskPasswordResetVO> passwordResetVOList = 
					AtRiskHandler.getInstance().getForgotUserNameAndPassowordFunction(atRiskDetils);

				AtRiskPasswordResetVO atRiskPasswordResetVO = null;
				AtRiskForgetUserName forgetUserNameVO = null;
				
				//If the list size is >= 2 means, Password Reset & Forgot Username functions are at risk
				if(passwordResetVOList.size() >= 2){
					
					forgetUserNameVO = AtRiskHandler.getInstance().getAtRiskActivitiesForgotUserName(atRiskDetils);
					
					for(AtRiskPasswordResetVO passwordResetVo :passwordResetVOList){
						if(SecurityActivityTypeCode.SA_PS_INIT_RESET_USER_PWD_SENT.getValue()
								.equals(passwordResetVo.getActivityTypeCode())){
							atRiskPasswordResetVO = AtRiskHandler.getInstance().getForgotPasswordFunction(atRiskDetils);
						}
					}
				}

				//If they are at risk period then only these Objects will have values. 
				//Hence no need to check again whether they are in risk period or not  
				if(forgetUserNameVO != null && atRiskPasswordResetVO != null){
					if(atRiskDetailsVO == null)
						atRiskDetailsVO = new AtRiskDetailsVO();
					
					atRiskDetailsVO.setForgetUserName(forgetUserNameVO);
					atRiskDetailsVO.setPasswordReset(atRiskPasswordResetVO);
					isAtRisk = true;
				}

				if(isAtRisk){
					atRiskDetailsVO.setSubmissionId(loan.getSubmissionId());
					loan.setAtRiskInd(JdbcHelper.INDICATOR_YES); 
					loan.setAtRiskDetailsVO(atRiskDetailsVO);
				}

			} catch (SystemException e) {
				throw new RuntimeException(e);
			}
		}
		super.populate(loan);
	}
	
	/**
	 * Special state change provided for NavSmart and Bundled GA Loans.
	 * In NavSmart an internal BGA CAR (Reviewer) is allowed to change a loan that is
	 * in Pending Approval state, back to Pending Review state.  All that is required
	 * is the state change.  No validations.
	 */
    @Override
    public Loan sendForReview(Loan loan) throws DistributionServiceException {
        LoanStateContext context = new LoanStateContext(loan);
              
        /**
         * Prepare the loan to go back to pending review stage.
         */
        loan.setStatus(LoanStateEnum.PENDING_REVIEW.getStatusCode());
        loan.getReviewedParameter().setStatusCode(LoanStateEnum.PENDING_REVIEW.getStatusCode());
        
        loan = save(context);

        if (loan.isOK()) {
            saveActivityHistory(LoanStateEnum.PENDING_APPROVAL,
                    LoanStateEnum.PENDING_REVIEW, context, loan
                            .getLastUpdated());
        }
        return loan;
    }	
}
