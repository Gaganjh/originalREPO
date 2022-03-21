package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;

/**
 * PayeePaymentInstruction is the value object for the payment instructions.
 * 
 * @author Dennis_Snowdon
 */
public class PayeePaymentInstruction extends BaseWithdrawal implements PaymentInstruction {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String bankAccountTypeCode;

    private Integer bankTransitNumber;

    private String bankAccountNumber;

    private String bankName;

    private String attentionName;

    private String creditPartyName;

    private Integer payeeNo;

    private Integer recipientNo;
    
    private BigDecimal paymentAmount;

    /**
     * @return gets the payee number
     */
    public Integer getPayeeNo() {
        return payeeNo;
    }

    /**
     * @param payeeNo sets the payee number
     */
    public void setPayeeNo(final Integer payeeNo) {
        this.payeeNo = payeeNo;
    }

    /**
     * @return Gets the recipient numbers
     */
    public Integer getRecipientNo() {
        return recipientNo;
    }

    /**
     * @param recipientNo sets the recipient number
     */
    public void setRecipientNo(final Integer recipientNo) {
        this.recipientNo = recipientNo;
    }

    /**
     * @return the attention name
     */
    public final String getAttentionName() {
        return attentionName;
    }

    /**
     * @param attentionName the attention name
     */
    public final void setAttentionName(final String attentionName) {
        this.attentionName = attentionName;
    }

    /**
     * @return the bank account number
     */
    public final String getBankAccountNumber() {
        return bankAccountNumber;
    }

    /**
     * @param bankAccountNumber the bank account number
     */
    public final void setBankAccountNumber(final String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    /**
     * @return the bank account type code
     */
    public final String getBankAccountTypeCode() {
        return bankAccountTypeCode;
    }

    /**
     * @param bankAccountTypeCode the bank account type code
     */
    public final void setBankAccountTypeCode(final String bankAccountTypeCode) {
        this.bankAccountTypeCode = bankAccountTypeCode;
    }

    /**
     * @return the bank name
     */
    public final String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bank name
     */
    public final void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the bank transit number
     */
    public final Integer getBankTransitNumber() {
        return bankTransitNumber;
    }

    /**
     * @param bankTransitNumber the bank transit number
     */
    public final void setBankTransitNumber(final Integer bankTransitNumber) {
        this.bankTransitNumber = bankTransitNumber;
    }

    /**
     * @return the credit party name
     */
    public final String getCreditPartyName() {
        return creditPartyName;
    }

    /**
     * @param creditPartyName the credit party name
     */
    public final void setCreditPartyName(final String creditPartyName) {
        this.creditPartyName = creditPartyName;
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

    /**
     * Checks if the default object has been inititalized or not.
     * 
     * @return boolean - True if the object is still blank.
     */
    public boolean isBlank() {
        return (StringUtils.isBlank(getBankAccountTypeCode())
                && StringUtils.isBlank(getBankAccountNumber())
                && StringUtils.isBlank(getBankName()) && StringUtils.isBlank(getAttentionName())
                && StringUtils.isBlank(getCreditPartyName()) && (getBankTransitNumber() == null)
                && (getRecipientNo() == null) && (getPayeeNo() == null));
    }

	@Override
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	@Override
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
}
