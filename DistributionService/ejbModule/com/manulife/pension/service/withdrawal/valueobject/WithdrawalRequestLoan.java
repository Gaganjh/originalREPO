package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;

public class WithdrawalRequestLoan extends BaseWithdrawal {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private Integer loanNo;

    private BigDecimal outstandingLoanAmount;

    /**
     * @return Returns the loanNo.
     */
    public Integer getLoanNo() {
        return loanNo;
    }

    /**
     * @return Returns the outstandingLoanAmount.
     */
    public BigDecimal getOutstandingLoanAmount() {
        return outstandingLoanAmount;
    }

    /**
     * @param loanNo The loanNo to set.
     */
    public void setLoanNo(final Integer loanNo) {
        this.loanNo = loanNo;
    }

    /**
     * @param outstandingLoanAmount The outstandingLoanAmount to set.
     */
    public void setOutstandingLoanAmount(final BigDecimal outstandingLoanAmount) {
        this.outstandingLoanAmount = outstandingLoanAmount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal#doErrorCodesExist()
     */
    @Override
    public boolean doErrorCodesExist() {

        // Check base error codes
        if (CollectionUtils.isNotEmpty(getErrorCodes())) {
            return true;
        }

        // No errors exist
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal#doWarningCodesExist()
     */
    @Override
    public boolean doWarningCodesExist() {

        // Check base warning codes
        if (CollectionUtils.isNotEmpty(getWarningCodes())) {
            return true;
        }

        // No warnings exist
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal#doAlertCodesExist()
     */
    @Override
    public boolean doAlertCodesExist() {

        // Check base alert codes
        if (CollectionUtils.isNotEmpty(getAlertCodes())) {
            return true;
        }

        // No alerts exist
        return false;
    }
}
