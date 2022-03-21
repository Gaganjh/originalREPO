package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;
import java.util.Calendar;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanAddress;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;

public class TestBGALoanStates extends LoanStateBase {
    
    private Integer contractId = 10042;   // BGA Contract (AKA Total Care)
    
    private Integer participantProfileId = 100014190;   // Participant on BGA Contract

    private Integer initiatorReviewerProfileId = 8026064; // Bundled GA Rep

    public void setUp() throws Exception {
        super.setUp();
        insertOnlineLoanCSF(contractId);
        expireAllLoanRequestsForParticipant(contractId, participantProfileId);
    }

    /**
     * BGA CAR Initiated loan, sends directly to approval from draft, then uses new method for Bundled GA Sept 2012 to pull
     * the loan from PENDING_APPROVAL back to PENDING_REVIEW.  Then sends it back to PENDING_APPROVAL. 
     * 
     * @throws Exception
     */
    public void testBgaSendForApprovalThenBackToReview() throws Exception {
        Loan loan = initiate(participantProfileId, contractId, initiatorReviewerProfileId, LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE);
        loan.setLoginUserProfileId(initiatorReviewerProfileId);
        loan.setLoginRoleCode("JH");
        LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
        loan.getMessages().clear();
        loan.setLoanReason("To buy a car");
        loan.getOriginalParameter().setLoanAmount(new BigDecimal(2000));
        loan.getOriginalParameter().setPaymentFrequency(GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
        loan.getOriginalParameter().setInterestRate(new BigDecimal(12.5));
        discard(loan);

        Calendar firstPayrollDate = Calendar.getInstance();
        firstPayrollDate.add(Calendar.MONTH, 1);

        LoanDataHelper dataHelper = LoanObjectFactory.getInstance().getLoanDataHelper();
        LoanPlanData loanPlanData = dataHelper.getLoanPlanData(contractId);

        loan.setFirstPayrollDate(firstPayrollDate.getTime());
        loan.setContractLoanExpenseMarginPct(loanPlanData.getContractLoanExpenseMarginPct());
        loan.setContractLoanMonthlyFlatFee(loanPlanData.getContractLoanMonthlyFlatFee());
        loan.setSpousalConsentReqdInd(loanPlanData.getSpousalConsentReqdInd());
        loan.setMaximumLoanAmount(loanPlanData.getMaximumLoanAmount());
        loan.setMinimumLoanAmount(loanPlanData.getMinimumLoanAmount());
        loan.setMaximumLoanPercentage(loanPlanData.getMaximumLoanPercentage());

        String paymentMethod = Payee.ACH_PAYMENT_METHOD_CODE;

        /*
         * Payment information
         */
        AddressVO addressVo = EmployeeServiceDelegate.getInstance(GlobalConstants.EZK_APPLICATION_ID).getAddressByProfileId(participantProfileId.longValue(),
                contractId);

        DistributionAddress address = new LoanAddress();
        address.setAddressLine1(addressVo.getAddressLine1());
        address.setAddressLine2(addressVo.getAddressLine2());
        address.setStateCode(addressVo.getStateCode());
        address.setCity(addressVo.getCity());
        address.setCountryCode(addressVo.getCountry());
        address.setZipCode(addressVo.getZipCode().trim());

        Payee payee = new LoanPayee();
        payee.setAddress(address);
        payee.setPaymentMethodCode(paymentMethod);
        payee.setFirstName(addressVo.getFirstName());
        payee.setLastName(addressVo.getLastName());
        payee.setTypeCode(Payee.TYPE_CODE_PARTICIPANT);
        payee.setReasonCode(Payee.REASON_CODE_PAYMENT);

        if (Payee.ACH_PAYMENT_METHOD_CODE.equals(paymentMethod) || Payee.WIRE_PAYMENT_METHOD_CODE.equals(paymentMethod)) {

            PaymentInstruction paymentInstruction = new LoanPaymentInstruction();
            paymentInstruction.setBankName("BANK OF AMERICA");
            paymentInstruction.setBankTransitNumber(new Integer("123456789"));
            if (!Payee.WIRE_PAYMENT_METHOD_CODE.equals(paymentMethod)) {
                paymentInstruction.setBankAccountTypeCode(Payee.CHECKING_ACCOUNT_TYPE_CODE);
            }
            paymentInstruction.setBankAccountNumber("123456789");
            payee.setPaymentInstruction(paymentInstruction);
        }

        LoanRecipient recipient = new LoanRecipient();
        recipient.setAddress((DistributionAddress) ((LoanAddress) address).clone());
        recipient.setFirstName(addressVo.getFirstName());
        recipient.setLastName(addressVo.getLastName());
        recipient.getPayees().add(payee);

        loan.setRecipient(recipient);

        state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
        loan.setStatus(LoanStateEnum.PENDING_APPROVAL.getStatusCode());
        loan = state.sendForApproval(loan);
        assertLoanMessages(loan, new LoanErrorCode[] {});

        
        // Testing send back to review when user is internal. Bundled GA requirement.
        // Only the reviewer should be doing this (pulling it back from approve to review)
        loan.setLoginUserProfileId(initiatorReviewerProfileId);
        state = LoanStateFactory.getState(LoanStateEnum.PENDING_APPROVAL);
        loan.setStatus(LoanStateEnum.PENDING_REVIEW.getStatusCode());
        loan = state.sendForReview(loan);
        assertLoanMessages(loan, new LoanErrorCode[] {});
        
        // Assume they are happy with it, and send it back to approval from review.
        loan.setLoginUserProfileId(initiatorReviewerProfileId);
        state = LoanStateFactory.getState(LoanStateEnum.PENDING_REVIEW);
        loan.setStatus(LoanStateEnum.PENDING_APPROVAL.getStatusCode());
        loan = state.sendForApproval(loan);
        assertLoanMessages(loan, new LoanErrorCode[] {});
        
    }

}
