package com.manulife.pension.service.loan.domain;

import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.log.DistributionEventEnum;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.LoanTypeVO;
import com.manulife.pension.service.synchronization.SynchronizationConstants;
import com.manulife.pension.service.synchronization.dao.SynchronizationDAO;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.Pair;

public class PendingReviewState extends DefaultLoanState {

	@Override
	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException {
		super.validate(fromState, toState, context);
		Loan loan = context.getLoan();
        if ( (fromState.equals(LoanStateEnum.PENDING_APPROVAL) && 
                toState.equals(LoanStateEnum.PENDING_REVIEW))  && 
                LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE.equals(loan.getLoginRoleCode())) {
              // The only role allowed to do this state transition without typical validations
              // is internal staff - As of Bundled GA project Sept 2012.
              // In this case we want to revert from Pending Approval to Pending Review without
              // any of the typical validations.

        } else {  
            LoanParticipantData loanParticipantData = context.getLoanParticipantData();
    		LoanPlanData loanPlanData = context.getLoanPlanData();
    		LoanSettings loanSettings = context.getLoanSettings();
    		LoanValidationHelper.validateLoanIssueFee(loan.getErrors(), loan,
    				context.getLoanPlanData());
    		LoanValidationHelper.validateLoanTypeCode(loan.getErrors(), loan
    				.getLoanType(), loanPlanData.getLoanTypeList());
    		LoanValidationHelper.validateLoanReason(loan);
    		LoanValidationHelper.validatePayrollDate(context,
    				LoanStateEnum.PENDING_REVIEW);
    		LoanValidationHelper.validateDefaultProvision(loan);
    		LoanValidationHelper.validateCurrentOutstandingBalance(loan,
    				loanParticipantData);
    		LoanValidationHelper.validateMaxBalanceLast12Months(loan,
    				loanParticipantData);
    		LoanValidationHelper.validateOutstandingLoansCount(loan,
    				loanParticipantData, loanPlanData);
    		validateLoanCalculationSection(loan, loanPlanData,
                    LoanStateEnum.PENDING_REVIEW);
    		validatePaymentSectionInvalidCharacter(loan);
    		/*
    		 * Validate Loan Request Submission
    		 */
    		List<LoanMessage> errors = loan.getErrors();
    		LoanValidationHelper.validateParticipantLoans(errors, loan,
    				loanParticipantData);
    		LoanValidationHelper.validateAllowLoans(errors, loanSettings);
    		
    		// check for participant is applicable to LIA
    		LoanValidationHelper.validateLIA(errors, loan);
    		
        }
	}

	@Override
	public Loan sendForAcceptance(Loan loan)
			throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);

		validateToState(LoanStateEnum.PENDING_REVIEW,
				LoanStateEnum.PENDING_ACCEPTANCE, context);
		if (!loan.isOK()) {
			return loan;
		}
		/**
		 * Prepare the loan to go to pending acceptance. We need to replicate
		 * the loan parameter because the participant can change the loan
		 * parameter.
		 */
		loan.setStatus(LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode());
		loan.setAcceptedParameter((LoanParameter) loan.getReviewedParameter()
				.clone());
		loan.getAcceptedParameter().setStatusCode(
				LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode());

        setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
            saveActivityHistory(LoanStateEnum.PENDING_REVIEW,
                    LoanStateEnum.PENDING_ACCEPTANCE, context, loan
                            .getLastUpdated());
			logToMrl(DistributionEventEnum.SEND_FOR_ACCEPTANCE.getEventName(),
			        context);

			// Triggering LoanRequestPendingAcceptance Event
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			loanEventGenerator.prepareAndSendPendingAcceptanceEvent(loan
					.getParticipantProfileId());			
		}
		return loan;
	}

	@Override
	public Loan sendForApproval(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		validateToState(LoanStateEnum.PENDING_REVIEW,
				LoanStateEnum.PENDING_APPROVAL, context);
		if (!loan.isOK()) {
			return loan;
		}

		/**
		 * Prepare the loan to go to pending approval. We do not need to
		 * duplicate the loan parameter because approval cannot update the loan
		 * parameters.
		 */
		loan.setStatus(LoanStateEnum.PENDING_APPROVAL.getStatusCode());
        setCommonLoanValues(context);
		loan = save(context);

		if (loan.isOK()) {
            saveActivityHistory(LoanStateEnum.PENDING_REVIEW,
                    LoanStateEnum.PENDING_APPROVAL, context, loan
                            .getLastUpdated());
			logToMrl(DistributionEventEnum.SEND_FOR_APPROVAL_FROM_REVIEWED
					.getEventName(), context);

						// Triggering LoanRequestPendingApproval Event
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			if (LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(loan
					.getCreatedByRoleCode())) {
				loanEventGenerator
						.prepareAndSendParticipantPendingApprovalEvent(loan
								.getParticipantProfileId());
			} else {
				loanEventGenerator
						.prepareAndSendPendingApprovalEvent(loan
								.getParticipantProfileId());
			}
		}
		return loan;
	}

	@Override
	public Loan loanPackage(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		validateToState(LoanStateEnum.PENDING_REVIEW,
				LoanStateEnum.LOAN_PACKAGE, context);
		if (!loan.isOK()) {
			return loan;
		}

		loan.setStatus(LoanStateEnum.LOAN_PACKAGE.getStatusCode());
		setLoanPackageDates(loan);
        if (JdbcHelper.INDICATOR_NO.equals(context.getLoanPlanData()
                .getSpousalConsentReqdInd())) {
            loan.setLegallyMarriedInd(null);      // Indicates unspecified
        }
		setAdditionalPlanAndContractData(loan);

        setCommonLoanValues(context);
		loan = save(context);
        if (loan.isOK()) {
            saveActivityHistory(LoanStateEnum.PENDING_REVIEW,
                    LoanStateEnum.LOAN_PACKAGE, context, loan
                            .getLastUpdated());
            logToMrl(DistributionEventEnum.LOAN_PACKAGE.getEventName(), 
                    context);
            
            // Trigger Loan Package Event
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			loanEventGenerator.prepareAndSendLoanPackageEvent(loan
					.getParticipantProfileId());
        }
		return loan;
	}

	@Override
	public Loan decline(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);

		validateToState(LoanStateEnum.PENDING_REVIEW, LoanStateEnum.DECLINED,
				context);
		if (!loan.isOK()) {
			return loan;
		}
		loan.setStatus(LoanStateEnum.DECLINED.getStatusCode());

        setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
            saveActivityHistory(LoanStateEnum.PENDING_REVIEW,
                    LoanStateEnum.DECLINED, context, loan
                            .getLastUpdated());
			logToMrl(DistributionEventEnum.DENY_FROM_REVIEWED.getEventName(), 
			        context);

			// Triggering LoanRequestDenied Event.
			LoanEventGenerator loanEventGenerator = EventFactory.getInstance()
					.getLoanEventGenerator(loan.getContractId(),
							loan.getSubmissionId(),
							loan.getLoginUserProfileId());
			loanEventGenerator
					.prepareAndSendDeniedEvent(loan.getParticipantProfileId(), loan.getPreviousStatus());
		}
		return loan;
	}

	@Override
	public void populate(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		LoanParticipantData participantData = context.getLoanParticipantData();
		LoanPlanData loanPlanData = context.getLoanPlanData();

		checkIfDisableAnyLoanTypes(loanPlanData);
		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();

		Integer contractId = loan.getContractId();
		Integer participantProfileId = loan.getParticipantProfileId();

		Pair<List<LoanMoneyType>, EmployeeVestingInformation> participantMoneyTypes = loanDataHelper
				.getParticipantMoneyTypesForLoans(contractId,
						participantProfileId);
		loan.setEmployeeVestingInformation(participantMoneyTypes.getSecond());

		refreshLoanMoneyTypes(loanPlanData, loan.getMoneyTypes(),
				participantMoneyTypes.getFirst());

		if (loan.getMaxBalanceLast12Months() == null) {
			loan.setMaxBalanceLast12Months(participantData
					.getMaxBalanceLast12Months());
		}
		if (loan.getCurrentOutstandingBalance() == null) {
			loan.setCurrentOutstandingBalance(participantData
					.getCurrentOutstandingBalance());
		}
		if (loan.getOutstandingLoansCount() == null) {
			loan.setOutstandingLoansCount(participantData
					.getOutstandingLoansCount());
		}

		loan.setEffectiveDate(getEstimatedEffectiveDate(context));
        loan.setMaturityDate(getEstimatedMaturityDate(loan));
        populateMessages(context);
		super.populate(loan);
	}

	@Override
	public Loan approve(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		loan.setEffectiveDate(getEffectiveDateForApproval(context));
        loan.setMaturityDate(getEstimatedMaturityDate(loan));

		validateToState(LoanStateEnum.PENDING_REVIEW, LoanStateEnum.APPROVED,
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
            saveActivityHistory(LoanStateEnum.PENDING_REVIEW,
                    LoanStateEnum.APPROVED, context, loan
                            .getLastUpdated());
			logToMrl(DistributionEventEnum.APPROVE_FROM_REVIEWED
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
	
	private void checkIfDisableAnyLoanTypes(LoanPlanData loanPlanData) {
			
			boolean flag = true;
			
			if(flag) {
				//check if all loan type amortization years are null
				for(LoanTypeVO loanType : loanPlanData.getLoanTypeList()) {
					if(loanType.getMaxAmortizationYear() != null) {
						flag = false;
						break;
					}
				}
			}
			
			if(!flag) {
				//check if all loan type amortization years are having values
				flag = true;
				for(LoanTypeVO loanType : loanPlanData.getLoanTypeList()) {
					if(loanType.getMaxAmortizationYear() == null) {
						flag = false;
						break;
					}
				}
			}
			
			if(!flag) {
			
				//Disable the loan type which is not having Max AmortizationYear
				for(LoanTypeVO loanType : loanPlanData.getLoanTypeList()) {
					
					if(loanType.getMaxAmortizationYear() == null) {
						loanType.setDisabled(true);
					}
				}
			}
	}

}
