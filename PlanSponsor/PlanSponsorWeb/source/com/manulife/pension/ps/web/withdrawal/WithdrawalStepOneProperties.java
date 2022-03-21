/**
 * 
 * @ author kuthiha
 * Oct 27, 2006
 */
package com.manulife.pension.ps.web.withdrawal;

//import static com.manulife.pension.service.withdrawal.valueobject.WithdrawalReason.DISABILITY;


/**
 * @author kuthiha
 *
 */
public interface WithdrawalStepOneProperties {
    
    static final String BirthDate = "withdrawalRequestUi.birthDate";
    static final String RetirementDate = "withdrawalRequestUi.retirementDate";
    static final String DisabilityDate = "withdrawalRequestUi.disabilityDate";
    static final String LastContributionDate = "lastContributionDate";
    static final String LastContributionProcessedDate = "lastContributionProcessedDate";
    static final String TerminationDate = "withdrawalRequestUi.terminationDate";
    static final String StateOfResidence = "stateOfResidence";
    static final String LoanOption = "withdrawalRequestUi.withdrawalRequest.loanOption";
    
    static final String PaymentTo = "paymentTo";
    static final String HardshipReasonCode = "withdrawalRequestUi.withdrawalRequest.hardshipReasonCode";
    static final String HardshipReasonExplanation = "withdrawalRequestUi.withdrawalRequest.hardshipReasonExplanation";
    
    static final String LoanOptionIrsCodeSelection = "withdrawalRequestUi.withdrawalRequest.loanOptionIrsCode";
}
