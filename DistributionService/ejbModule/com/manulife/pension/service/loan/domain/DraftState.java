package com.manulife.pension.service.loan.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.intware.dao.DAOException;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.distribution.AtRiskHandler;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.log.DistributionEventEnum;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.RequestType;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.util.LoanContentConstants;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.EjbLoanSupportDataRetriever;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanFee;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanTypeVO;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.Pair;

public class DraftState extends DefaultLoanState {

	@Override
	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException {
		super.validate(fromState, toState, context);
	}

	@Override
	public Loan loanPackage(Loan loan) throws DistributionServiceException {

		LoanStateContext context = getLoanStateContext(loan);
		validateToState(LoanStateEnum.DRAFT, LoanStateEnum.LOAN_PACKAGE,
				context);
		if (!loan.isOK()) {
			return loan;
		}

		/**
		 * Prepare the loan to go to loan package stage.
		 */
		loan.setReviewedParameter((LoanParameter) loan.getOriginalParameter()
				.clone());
		loan.getReviewedParameter().setStatusCode(
				LoanStateEnum.PENDING_REVIEW.getStatusCode());
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
	        saveActivityHistory(LoanStateEnum.DRAFT, LoanStateEnum.LOAN_PACKAGE,
	                context, loan.getLastUpdated());
	        logToMrl(DistributionEventEnum.LOAN_PACKAGE.getEventName(), context);

			// Trigger Loan Package Event
			LoanEventGenerator loanEventGenerator = EventFactory
				.getInstance().getLoanEventGenerator(
					loan.getContractId(), loan.getSubmissionId(),
					loan.getLoginUserProfileId());
			loanEventGenerator
					.prepareAndSendLoanPackageEvent(loan
						.getParticipantProfileId());
		}
		return loan;
	}

	@Override
	public Loan sendForReview(Loan loan) throws DistributionServiceException {
		LoanStateContext context = new LoanStateContext(loan);
		validateToState(LoanStateEnum.DRAFT, LoanStateEnum.PENDING_REVIEW,
				context);

		if (!loan.isOK()) {
			return loan;
		}

		/**
		 * Prepare the loan to go to pending review stage.
		 */
		loan.setStatus(LoanStateEnum.PENDING_REVIEW.getStatusCode());
		loan.setReviewedParameter((LoanParameter) loan.getOriginalParameter()
				.clone());
		loan.getReviewedParameter().setStatusCode(
				LoanStateEnum.PENDING_REVIEW.getStatusCode());

		/**
		 * Save atRisk information for participant initiated request.
		 */
		if (loan.isParticipantInitiated()) {
			
			AtRiskDetailsInputVO atRiskDetils = new AtRiskDetailsInputVO();
			atRiskDetils.setContractId(loan.getContractId());
			atRiskDetils.setProfileId(loan.getParticipantProfileId());
			atRiskDetils.setSubmissionId(loan.getSubmissionId());
			atRiskDetils.setLoanOrWithdrawalReq(RequestType.LOAN);

			try {

				AtRiskWebRegistrationVO atRiskWebRegistrationVO = null;

				if(AtRiskHandler.getInstance().isRegistrationAtRiskPeriod(atRiskDetils))
					atRiskWebRegistrationVO = AtRiskHandler.getInstance().getWebRigistrationInfo(atRiskDetils);

				AtRiskDetailsVO atRiskDetailsVO = new AtRiskDetailsVO();

				if(atRiskWebRegistrationVO !=  null){
					atRiskDetailsVO.setWebRegistration(atRiskWebRegistrationVO);

					atRiskDetailsVO.setSubmissionId(loan.getSubmissionId());
					
					loan.setAtRiskAddress(atRiskWebRegistrationVO.getAddress());
					loan.setAtRiskDetailsVO(atRiskDetailsVO);
					loan.setAtRiskInd(JdbcHelper.INDICATOR_YES);
				}

			} catch (SystemException e) {
				//TODO : Properly handling exception ?
				throw new RuntimeException(e);
			}
		}		
		setCommonLoanValues(context);
		loan = save(context);

		if (loan.isOK()) {
			saveActivityHistory(LoanStateEnum.DRAFT,
					LoanStateEnum.PENDING_REVIEW, context, loan
							.getLastUpdated());
			logToMrl(DistributionEventEnum.SEND_FOR_REVIEW.getEventName(), context);

			// Trigger the pending review event
			LoanEventGenerator loanEventGenerator = EventFactory
			.getInstance().getLoanEventGenerator(
					loan.getContractId(), loan.getSubmissionId(),
					loan.getLoginUserProfileId());
			if (LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(loan
					.getCreatedByRoleCode())) {
				loanEventGenerator
						.prepareAndSendParticipantPendingReviewEvent(loan
								.getParticipantProfileId());
			} else {
				loanEventGenerator.prepareAndSendPendingReviewEvent(loan
						.getParticipantProfileId());
			}
		}
		return loan;
	}

	@Override
	public Loan initiate(Integer participanProfileId, Integer contractId,
			Integer userProfileId) throws DistributionServiceException {
		Loan returnLoan = new Loan();
		/*
		 * By default, use the EJB data retriever. The web container should
		 * reset this to its own implementation.
		 */
		returnLoan.setDataRetriever(new EjbLoanSupportDataRetriever());
		returnLoan.setContractId(contractId);
		returnLoan.setParticipantProfileId(participanProfileId);
		returnLoan.setLoginUserProfileId(userProfileId);
		returnLoan.setRequestDate(new Date());
		returnLoan.setStatus(LoanStateEnum.DRAFT.getStatusCode());
		returnLoan.setLastFeeChangedWasPlanSponsorUserInd(false);
		returnLoan.setApplyIrs10KDollarRuleInd(false);
		LoanParameter parameter = new LoanParameter();
		parameter.setStatusCode(LoanStateEnum.DRAFT.getStatusCode());
		parameter.setReadyToSave(true);
		returnLoan.setOriginalParameter(parameter);

		try {
			LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
					.getLoanDataHelper();
			LoanPlanData loanPlanData = returnLoan.getLoanPlanData();
			returnLoan.setLoanType(getDefaultLoanType(loanPlanData));
			Location location = Location.USA;
			if (GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY.equals(loanPlanData
					.getManulifeCompanyId())) {
				location = Location.NEW_YORK;
			}
			ContentText defaultProvision = loanDataHelper.getContentTextByKey(
					GlobalConstants.PSW_APPLICATION_ID, location,
					LoanContentConstants.DEFAULT_PROVISION);
			if (defaultProvision != null) {
				returnLoan.setDefaultProvision(StringUtils
						.trimToEmpty(defaultProvision.getText()));
			}

			LoanSupportDao loanSupportDao = new LoanSupportDao(
					BaseDatabaseDAO
							.getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));
			returnLoan.setParticipantId(loanSupportDao
					.getParticipantIdByProfileId(participanProfileId));
		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Failed to instantiate loan support dao", e);
		}
		
		populate(returnLoan);
		
		// check for participant is applicable to LIA.
		LoanValidationHelper.validateLIA(returnLoan.getMessages(), returnLoan);

		return returnLoan;
	}

	@Override
	public Loan sendForApproval(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		validateToState(LoanStateEnum.DRAFT, LoanStateEnum.PENDING_APPROVAL,
				context);
		if (!loan.isOK()) {
			return loan;
		}

		/**
		 * Prepare the loan to go to pending approval stage.
		 */
		loan.setStatus(LoanStateEnum.PENDING_APPROVAL.getStatusCode());
		loan.setReviewedParameter((LoanParameter) loan.getOriginalParameter()
				.clone());
		loan.getReviewedParameter().setStatusCode(
				LoanStateEnum.PENDING_REVIEW.getStatusCode());

        setCommonLoanValues(context);
		loan = save(context);
		if (loan.isOK()) {
			saveActivityHistory(LoanStateEnum.DRAFT,
					LoanStateEnum.PENDING_APPROVAL, context, loan
							.getLastUpdated());
			logToMrl(DistributionEventEnum.SEND_FOR_APPROVAL_FROM_DRAFT
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
	public void populate(Loan loan) throws DistributionServiceException {
		LoanStateContext context = getLoanStateContext(loan);
		LoanParticipantData participantData = context.getLoanParticipantData();
		LoanPlanData loanPlanData = context.getLoanPlanData();

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

		String loanType = loan.getLoanType();
		if (loan.getCurrentLoanParameter().getAmortizationMonths() == null) {
			loan.getCurrentLoanParameter().setAmortizationMonths(
					loanPlanData.getMaximumAmortizationYears(loanType) * 12);
		}

		if (loan.getMaximumAmortizationYears() == null) {
			loan.setMaximumAmortizationYears(loanPlanData
					.getMaximumAmortizationYears(loanType));
		}

		if (loan.getFee() == null) {
			Fee fee = new LoanFee();
			fee.setTypeCode(Fee.DOLLAR_TYPE_CODE);
			fee.setValue(loanPlanData.getContractLoanSetupFeeAmount());
			loan.setFee(fee);
		}

		loan.setEffectiveDate(getEstimatedEffectiveDate(context));
		loan.setMaturityDate(getEstimatedMaturityDate(loan));
		loan.setExpirationDate(getEstimatedExpirationDate());
		populateMessages(context);

		super.populate(loan);
	}
	
	private String getDefaultLoanType(LoanPlanData loanPlanData) {
		
		String defaultLoanType = StringUtils.EMPTY;
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
		
		// return the default loan type based on the first loan type available 
		// for the Plan in the order of the Loan Types 
		for(LoanTypeVO loanType : loanPlanData.getLoanTypeList()) {
			
			if(!loanType.isDisabled()) {
				defaultLoanType = loanType.getLoanTypeCode();
				break;
			}
		}
		
		return defaultLoanType;
	}
}
