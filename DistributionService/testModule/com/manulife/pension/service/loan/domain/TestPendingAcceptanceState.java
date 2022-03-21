package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Assert;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.LoanDocumentServiceDelegate;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.util.LoanContentConstants;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanAddress;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;

public class TestPendingAcceptanceState extends LoanStateBase {

	private Integer contractId = 11847;
	private Integer participantProfileId = 130747405;
	private Integer reviewerProfileId = 1631092; // TPA
	
	//Constants for Bundled Contracts
	private Integer bgaContractId = 10007;
	private Integer bgaContractParticipantProfileId = 100004464;
	private Integer bgaCARUserProfileId = 8026064; //BGARep01
	private Integer bgaApproverUserProfileId = 8026007; //BGAppr01
	

    public void setUp() throws Exception {
        super.setUp();
        insertOnlineLoanCSF(contractId);
        expireAllLoanRequestsForParticipant(contractId, participantProfileId);
        
        //For Bundled GA
        insertOnlineLoanCSF(bgaContractId);
        expireAllLoanRequestsForParticipant(bgaContractId, bgaContractParticipantProfileId);
    }    
	
	public void testParticipantSendForApproval() throws Exception {
		Loan loan = initiate(participantProfileId, contractId,
				participantProfileId, LoanConstants.USER_ROLE_PARTICIPANT_CODE);
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan.getMessages().clear();
		loan.setLoanReason("To buy a car");
		loan.getOriginalParameter().setLoanAmount(new BigDecimal(2000));
		loan.getOriginalParameter().setPaymentFrequency(
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		loan.getOriginalParameter().setInterestRate(new BigDecimal(12.5));
		loan = state.sendForReview(loan);
        discard(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());

		/*
		 * After the loan is successfully sent for review, we send it for
		 * acceptance.
		 */
		 
		state = LoanStateFactory.getState(LoanStateEnum.PENDING_REVIEW);
		loan.setLoginUserProfileId(reviewerProfileId);
		loan.setStatus(LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode());
		loan.getReviewedParameter().setLoanAmount(new BigDecimal(1500));
		loan = state.sendForAcceptance(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});

		/*
		 * Participant then sends it back for approval.
		 */
		state = LoanStateFactory.getState(LoanStateEnum.PENDING_ACCEPTANCE);
		loan.setLoginUserProfileId(participantProfileId);
		loan.setStatus(LoanStateEnum.PENDING_APPROVAL.getStatusCode());
		loan.getAcceptedParameter().setLoanAmount(new BigDecimal(2000));

		Calendar firstPayrollDate = Calendar.getInstance();
		firstPayrollDate.add(Calendar.MONTH, 1);

		LoanDataHelper dataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		LoanPlanData loanPlanData = dataHelper.getLoanPlanData(contractId);

		loan.setFirstPayrollDate(firstPayrollDate.getTime());
		loan.setContractLoanExpenseMarginPct(loanPlanData
				.getContractLoanExpenseMarginPct());
		loan.setContractLoanMonthlyFlatFee(loanPlanData
				.getContractLoanMonthlyFlatFee());
		loan.setSpousalConsentReqdInd(loanPlanData.getSpousalConsentReqdInd());
		loan.setMaximumLoanAmount(loanPlanData.getMaximumLoanAmount());
		loan.setMinimumLoanAmount(loanPlanData.getMinimumLoanAmount());
		loan.setMaximumLoanPercentage(loanPlanData.getMaximumLoanPercentage());

		String paymentMethod = Payee.ACH_PAYMENT_METHOD_CODE;

		/*
		 * Payment information
		 */
		AddressVO addressVo = EmployeeServiceDelegate.getInstance(
				GlobalConstants.EZK_APPLICATION_ID).getAddressByProfileId(
				participantProfileId.longValue(), contractId);

		DistributionAddress address = new LoanAddress();
		address.setAddressLine1(addressVo.getAddressLine1());
		address.setAddressLine2(addressVo.getAddressLine2());
		address.setStateCode(addressVo.getStateCode());
		address.setCity(addressVo.getCity());
		address.setCountryCode(addressVo.getCountry());
		address.setZipCode(addressVo.getZipCode());

		Payee payee = new LoanPayee();
		payee.setAddress(address);
		payee.setPaymentMethodCode(paymentMethod);
		payee.setFirstName(addressVo.getFirstName());
		payee.setLastName(addressVo.getLastName());
		payee.setTypeCode(Payee.TYPE_CODE_PARTICIPANT);
		payee.setReasonCode(Payee.REASON_CODE_PAYMENT);

		if (Payee.ACH_PAYMENT_METHOD_CODE.equals(paymentMethod)
				|| Payee.WIRE_PAYMENT_METHOD_CODE.equals(paymentMethod)) {

			PaymentInstruction paymentInstruction = new LoanPaymentInstruction();
			paymentInstruction.setBankName("BANK OF AMERICA");
			paymentInstruction.setBankTransitNumber(new Integer("123456789"));
			if (!Payee.WIRE_PAYMENT_METHOD_CODE.equals(paymentMethod)) {
				paymentInstruction
						.setBankAccountTypeCode(Payee.CHECKING_ACCOUNT_TYPE_CODE);
			}
			paymentInstruction.setBankAccountNumber("123456789");
			payee.setPaymentInstruction(paymentInstruction);
		}

		LoanRecipient recipient = new LoanRecipient();
		recipient.setAddress((DistributionAddress) ((LoanAddress) address)
				.clone());
		recipient.setFirstName(addressVo.getFirstName());
		recipient.setLastName(addressVo.getLastName());
		recipient.getPayees().add(payee);

		loan.setRecipient(recipient);

		/*
		 * Declarations.
		 */
		LoanDeclaration declaration = new LoanDeclaration();
		declaration.setTypeCode(Declaration.TRUTH_IN_LENDING_NOTICE);
		loan.getDeclarations().add(declaration);
		declaration = new LoanDeclaration();
		declaration
				.setTypeCode(Declaration.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE);
		loan.getDeclarations().add(declaration);
		declaration = new LoanDeclaration();
		declaration.setTypeCode(Declaration.LOAN_PARTICIPANT_AUTHORIZATION);
		loan.getDeclarations().add(declaration);

		LoanDocumentServiceDelegate loanDocumentServiceDelegate = LoanDocumentServiceDelegate
				.getInstance();
		ContentText content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.TRUTH_IN_LENDING_NOTICE_HTML);
		addManagedContent(loan, ManagedContent.TRUTH_IN_LENDING_NOTICE_HTML,
				LoanContentConstants.TRUTH_IN_LENDING_NOTICE_HTML, content
						.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF);
		addManagedContent(loan, ManagedContent.TRUTH_IN_LENDING_NOTICE_PDF,
				LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF, content
						.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_HTML);
		addManagedContent(loan,
				ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_HTML,
				LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_HTML,
				content.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF);
		addManagedContent(loan,
				ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_PDF,
				LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF,
				content.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.PARTICIPANT_AUTHORIZATION);
		addManagedContent(loan, ManagedContent.LOAN_PARTICIPANT_AUTHORIZATION,
				LoanContentConstants.PARTICIPANT_AUTHORIZATION, content.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.AMORTIZATION_SCHEDULE_HTML);
		addManagedContent(loan, ManagedContent.AMORTIZATION_SCHEDULE_HTML,
				LoanContentConstants.AMORTIZATION_SCHEDULE_HTML, content
						.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.AMORTIZATION_SCHEDULE_PDF);
		addManagedContent(loan, ManagedContent.AMORTIZATION_SCHEDULE_PDF,
				LoanContentConstants.AMORTIZATION_SCHEDULE_PDF, content.getId());

		loan = state.sendForApproval(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});

	}

	private void addManagedContent(Loan loan, String contentTypeCode,
			Integer contentKey, Integer contentId) {
		ManagedContent content = new ManagedContent();
		content.setCmaSiteCode(ManagedContent.CMA_SITE_CODE_PSW);
		content.setContentId(contentId);
		content.setContentKey(contentKey);
		content.setContentTypeCode(contentTypeCode);
		loan.getManagedContents().add(content);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testBundledContractParticipantSendForApproval() throws Exception {
		Loan loan = initiate(bgaContractParticipantProfileId, bgaContractId,
				bgaCARUserProfileId, LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE);
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan.getMessages().clear();
		loan.setLoanReason("To buy a car");
		loan.getOriginalParameter().setLoanAmount(new BigDecimal(2000));
		loan.getOriginalParameter().setPaymentFrequency(
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		loan.getOriginalParameter().setInterestRate(new BigDecimal(12.5));
		loan = state.sendForReview(loan);
        discard(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});
		Assert.assertNotNull(loan.getSubmissionId());

		/*
		 * After the loan is successfully sent for review, we send it for
		 * acceptance.
		 */
		state = LoanStateFactory.getState(LoanStateEnum.PENDING_REVIEW);
		loan.setLoginUserProfileId(bgaApproverUserProfileId);
		loan.setStatus(LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode());
		loan.getReviewedParameter().setLoanAmount(new BigDecimal(1500));
		loan = state.sendForAcceptance(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});

		/*
		 * Participant then sends it back for approval.
		 */
		state = LoanStateFactory.getState(LoanStateEnum.PENDING_ACCEPTANCE);
		loan.setLoginUserProfileId(participantProfileId);
		loan.setStatus(LoanStateEnum.PENDING_APPROVAL.getStatusCode());
		loan.getAcceptedParameter().setLoanAmount(new BigDecimal(2000));

		Calendar firstPayrollDate = Calendar.getInstance();
		firstPayrollDate.add(Calendar.MONTH, 1);

		LoanDataHelper dataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		LoanPlanData loanPlanData = dataHelper.getLoanPlanData(contractId);

		loan.setFirstPayrollDate(firstPayrollDate.getTime());
		loan.setContractLoanExpenseMarginPct(loanPlanData
				.getContractLoanExpenseMarginPct());
		loan.setContractLoanMonthlyFlatFee(loanPlanData
				.getContractLoanMonthlyFlatFee());
		loan.setSpousalConsentReqdInd(loanPlanData.getSpousalConsentReqdInd());
		loan.setMaximumLoanAmount(loanPlanData.getMaximumLoanAmount());
		loan.setMinimumLoanAmount(loanPlanData.getMinimumLoanAmount());
		loan.setMaximumLoanPercentage(loanPlanData.getMaximumLoanPercentage());

		String paymentMethod = Payee.ACH_PAYMENT_METHOD_CODE;

		/*
		 * Payment information
		 */
		AddressVO addressVo = EmployeeServiceDelegate.getInstance(
				GlobalConstants.EZK_APPLICATION_ID).getAddressByProfileId(
				participantProfileId.longValue(), contractId);

		DistributionAddress address = new LoanAddress();
		address.setAddressLine1(addressVo.getAddressLine1());
		address.setAddressLine2(addressVo.getAddressLine2());
		address.setStateCode(addressVo.getStateCode());
		address.setCity(addressVo.getCity());
		address.setCountryCode(addressVo.getCountry());
		address.setZipCode(addressVo.getZipCode());

		Payee payee = new LoanPayee();
		payee.setAddress(address);
		payee.setPaymentMethodCode(paymentMethod);
		payee.setFirstName(addressVo.getFirstName());
		payee.setLastName(addressVo.getLastName());
		payee.setTypeCode(Payee.TYPE_CODE_PARTICIPANT);
		payee.setReasonCode(Payee.REASON_CODE_PAYMENT);

		if (Payee.ACH_PAYMENT_METHOD_CODE.equals(paymentMethod)
				|| Payee.WIRE_PAYMENT_METHOD_CODE.equals(paymentMethod)) {

			PaymentInstruction paymentInstruction = new LoanPaymentInstruction();
			paymentInstruction.setBankName("BANK OF AMERICA");
			paymentInstruction.setBankTransitNumber(new Integer("123456789"));
			if (!Payee.WIRE_PAYMENT_METHOD_CODE.equals(paymentMethod)) {
				paymentInstruction
						.setBankAccountTypeCode(Payee.CHECKING_ACCOUNT_TYPE_CODE);
			}
			paymentInstruction.setBankAccountNumber("123456789");
			payee.setPaymentInstruction(paymentInstruction);
		}

		LoanRecipient recipient = new LoanRecipient();
		recipient.setAddress((DistributionAddress) ((LoanAddress) address)
				.clone());
		recipient.setFirstName(addressVo.getFirstName());
		recipient.setLastName(addressVo.getLastName());
		recipient.getPayees().add(payee);

		loan.setRecipient(recipient);

		/*
		 * Declarations.
		 */
		LoanDeclaration declaration = new LoanDeclaration();
		declaration.setTypeCode(Declaration.TRUTH_IN_LENDING_NOTICE);
		loan.getDeclarations().add(declaration);
		declaration = new LoanDeclaration();
		declaration
				.setTypeCode(Declaration.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE);
		loan.getDeclarations().add(declaration);
		declaration = new LoanDeclaration();
		declaration.setTypeCode(Declaration.LOAN_PARTICIPANT_AUTHORIZATION);
		loan.getDeclarations().add(declaration);

		LoanDocumentServiceDelegate loanDocumentServiceDelegate = LoanDocumentServiceDelegate
				.getInstance();
		ContentText content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.TRUTH_IN_LENDING_NOTICE_HTML);
		addManagedContent(loan, ManagedContent.TRUTH_IN_LENDING_NOTICE_HTML,
				LoanContentConstants.TRUTH_IN_LENDING_NOTICE_HTML, content
						.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF);
		addManagedContent(loan, ManagedContent.TRUTH_IN_LENDING_NOTICE_PDF,
				LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF, content
						.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_HTML);
		addManagedContent(loan,
				ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_HTML,
				LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_HTML,
				content.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF);
		addManagedContent(loan,
				ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_PDF,
				LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF,
				content.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.PARTICIPANT_AUTHORIZATION);
		addManagedContent(loan, ManagedContent.LOAN_PARTICIPANT_AUTHORIZATION,
				LoanContentConstants.PARTICIPANT_AUTHORIZATION, content.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.AMORTIZATION_SCHEDULE_HTML);
		addManagedContent(loan, ManagedContent.AMORTIZATION_SCHEDULE_HTML,
				LoanContentConstants.AMORTIZATION_SCHEDULE_HTML, content
						.getId());

		content = loanDocumentServiceDelegate
				.getContentTextByKey(LoanContentConstants.AMORTIZATION_SCHEDULE_PDF);
		addManagedContent(loan, ManagedContent.AMORTIZATION_SCHEDULE_PDF,
				LoanContentConstants.AMORTIZATION_SCHEDULE_PDF, content.getId());

		loan = state.sendForApproval(loan);
		assertLoanMessages(loan, new LoanErrorCode[] {});

	}
	
	
}
