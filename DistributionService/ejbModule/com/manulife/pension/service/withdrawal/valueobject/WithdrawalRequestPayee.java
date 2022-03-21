package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;

/**
 * WithdrawalRequestPayee is the value object for the withdrawal request payee.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.4 2006/09/11 18:45:51
 */
public class WithdrawalRequestPayee extends BaseWithdrawal implements Payee {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private Integer recipientNo;

    private Integer payeeNo;

    private String firstName;

    private String lastName;

    private String organizationName;

    private String typeCode;

    private String reasonCode;

    private String paymentMethodCode;

    private String shareTypeCode;

    private BigDecimal shareValue;

    private String rolloverAccountNo;

    private String rolloverPlanName;

    private String irsDistCode;

    private Boolean mailCheckToAddress;

    private Boolean sendCheckByCourier;

    private String courierCompanyCode;

    private String courierNo;

    private DistributionAddress address = new Address();

    private DistributionAddress defaultAddress = new Address();

    private PaymentInstruction paymentInstruction = new PayeePaymentInstruction();
    
  
    private BigDecimal WithdrawalAmount;
    private String taxes;
    private String participant;
    private String rolloverType;
    public static final String DISPLAY_EMP_PLAN = "Employer Sponsored Qualified Plan";

    /**
     * @return the IRS distribution code
     */
    public String getIrsDistCode() {
        return irsDistCode;
    }

    /**
     * @param irsDistCode the IRS distribution code
     */
    public void setIrsDistCode(final String irsDistCode) {
        this.irsDistCode = irsDistCode;
    }

    /**
     * @return Returns the reasonCode.
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * @param reasonCode The reasonCode to set.
     */
    public void setReasonCode(final String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @return Returns the recipientNo.
     */
    public Integer getRecipientNo() {
        return recipientNo;
    }

    /**
     * @param recipientNo The recipientNo to set.
     */
    public void setRecipientNo(final Integer recipientNo) {
        this.recipientNo = recipientNo;
    }

    /**
     * @return Returns the rolloverAccountNo.
     */
    public String getRolloverAccountNo() {
        return rolloverAccountNo;
    }

    /**
     * @param rolloverAccountNo The rolloverAccountNo to set.
     */
    public void setRolloverAccountNo(final String rolloverAccountNo) {
        this.rolloverAccountNo = rolloverAccountNo;
    }

    /**
     * @return Returns the rolloverPlanName.
     */
    public String getRolloverPlanName() {
        return rolloverPlanName;
    }

    /**
     * @param rolloverPlanName The rolloverPlanName to set.
     */
    public void setRolloverPlanName(final String rolloverPlanName) {
        this.rolloverPlanName = rolloverPlanName;
    }

    /**
     * @return Returns the shareTypeCode.
     */
    public String getShareTypeCode() {
        return shareTypeCode;
    }

    /**
     * @param shareTypeCode The shareTypeCode to set.
     */
    public void setShareTypeCode(final String shareTypeCode) {
        this.shareTypeCode = shareTypeCode;
    }

    /**
     * @return Returns the shareValue.
     */
    public BigDecimal getShareValue() {
        return shareValue;
    }

    /**
     * @param shareValue The shareValue to set.
     */
    public void setShareValue(final BigDecimal shareValue) {
        this.shareValue = shareValue;
    }

    /**
     * @return the courier company code
     */
    public String getCourierCompanyCode() {
        return courierCompanyCode;
    }

    /**
     * @param courierCompanyCode the courier company code
     */
    public void setCourierCompanyCode(final String courierCompanyCode) {
        this.courierCompanyCode = courierCompanyCode;
    }

    /**
     * @return the courrier number
     */
    public String getCourierNo() {
        return courierNo;
    }

    /**
     * @param courierNo the courrier number
     */
    public void setCourierNo(final String courierNo) {
        this.courierNo = courierNo;
    }

    /**
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the first name
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the last name
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return whether or not to mail check to the address
     */
    public Boolean getMailCheckToAddress() {
        return mailCheckToAddress;
    }

    /**
     * @param mailCheckToAddress whether or not to mail check to the address
     */
    public void setMailCheckToAddress(final Boolean mailCheckToAddress) {
        this.mailCheckToAddress = mailCheckToAddress;
    }

    /**
     * @return the organization name
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organization name
     */
    public void setOrganizationName(final String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return The payment instruction value object
     */
    public PaymentInstruction getPaymentInstruction() {
        return paymentInstruction;
    }

    /**
     * @param paymentInstruction the payment instruction value object
     */
    public void setPaymentInstruction(final PaymentInstruction paymentInstruction) {
        this.paymentInstruction = (paymentInstruction == null) ? new PayeePaymentInstruction()
                : paymentInstruction;
    }

    /**
     * @return the payment method code
     */
    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    /**
     * @param paymentMethodCode the payment method code
     */
    public void setPaymentMethodCode(final String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    /**
     * @return wether or not to send the check by courier
     */
    public Boolean getSendCheckByCourier() {
        return sendCheckByCourier;
    }

    /**
     * @param sendCheckByCourier wether or not to send the check by courier
     */
    public void setSendCheckByCourier(final Boolean sendCheckByCourier) {
        this.sendCheckByCourier = sendCheckByCourier;
    }

    /**
     * @return the payee type code
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode the payee type code
     */
    public void setTypeCode(final String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * @return the address the recipient number
     */
    public DistributionAddress getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(final DistributionAddress address) {
        this.address = (address == null) ? new Address() : address;
    }

    /**
     * @return the payee Number
     */
    public Integer getPayeeNo() {
        return payeeNo;
    }

    /**
     * @param payeeNo the payee Number
     */
    public void setPayeeNo(final Integer payeeNo) {
        this.payeeNo = payeeNo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doErrorCodesExist() {

        // Check base error codes
        if (CollectionUtils.isNotEmpty(getErrorCodes())) {
            return true;
        }

        // Check for address errors.
        if (((Address) getAddress()).doErrorCodesExist()) {
            return true;
        } // fi

        // Check instruction
        if (((PayeePaymentInstruction) getPaymentInstruction()).doErrorCodesExist()) {
            return true;
        }

        // No errors exist
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doWarningCodesExist() {

        // Check base warning codes
        if (CollectionUtils.isNotEmpty(getWarningCodes())) {
            return true;
        }

        // Check for address warnings.
        if (((Address) getAddress()).doWarningCodesExist()) {
            return true;
        } // fi

        // Check instruction
        if (((PayeePaymentInstruction) getPaymentInstruction()).doWarningCodesExist()) {
            return true;
        }

        // No warnings exist
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doAlertCodesExist() {

        // Check base alert codes
        if (CollectionUtils.isNotEmpty(getAlertCodes())) {
            return true;
        }

        // Check for address alerts.
        if (((Address) getAddress()).doAlertCodesExist()) {
            return true;
        } // fi

        // Check instruction
        if (((PayeePaymentInstruction) getPaymentInstruction()).doAlertCodesExist()) {
            return true;
        }

        // No alerts exist
        return false;
    }

    /**
     * @return the defaultAddress
     */
    public DistributionAddress getDefaultAddress() {
        return defaultAddress;
    }

    /**
     * @param defaultAddress the defaultAddress to set
     */
    public void setDefaultAddress(final DistributionAddress defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessages() {
        super.removeMessages();

        if (address != null) {
            ((Address) address).removeMessages();
        } // fi
        if (defaultAddress != null) {
            ((Address) defaultAddress).removeMessages();
        } // fi
        if (paymentInstruction != null) {
            ((PayeePaymentInstruction) paymentInstruction).removeMessages();

        } // fi
    }

    /**
     * Determines if the bank account number for rollover row is shown.
     * 
     * @param withdrawalRequest The withdrawal request being used.
     * @return boolean - True if shown, false otherwise.
     */
    public boolean getShowAccountNumber(final WithdrawalRequest withdrawalRequest) {
    	
    	final String paymentTo = withdrawalRequest.getPaymentTo();
    	if(StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION) && this.getParticipant()!=null && 
    			this.getParticipant().indexOf("Participant") !=-1 ){
    		return false;
   		}
    	else return (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                || StringUtils
                        .equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE) || ((StringUtils
                .equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE) || StringUtils
                .equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE)) && isFirstPayee(withdrawalRequest))
               || StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)
               
               );
    }

    /**
     * Determines if the payee is the first payee in the recipient collection.
     * 
     * @param withdrawalRequest The withdrawal request being used.
     * @return boolean - True if it's the first payee, false otherwise.
     */
    private boolean isFirstPayee(final WithdrawalRequest withdrawalRequest) {
    	
    	
        return (this == withdrawalRequest.getRecipients().iterator().next().getPayees().iterator()
                .next());
    }
        

    /**
     * Determines if the Trustee of _(name of plan)_ for rollover is shown.
     * 
     * @param withdrawalRequest The withdrawal request being used.
     * @return boolean - True if shown, false otherwise.
     */
    public boolean getShowTrusteeForRollover(final WithdrawalRequest withdrawalRequest) {
        final String paymentTo = withdrawalRequest.getPaymentTo();
        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)) {
            return true;
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {

            return isFirstPayee(withdrawalRequest);
        } else if(StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION) && this.getParticipant()!=null && 
    			this.getParticipant().indexOf(DISPLAY_EMP_PLAN) !=-1 ){
    		return true;
   		} else {
            return false;
        } // fi
    }

    /**
     * Determines if the IRS Code For Withdrawal field is shown.
     * 
     * @param withdrawalRequest The withdrawal request being used.
     * @return boolean - True if shown, false otherwise.
     */
    public boolean getShowIrsCodeForWithdrawal(final WithdrawalRequest withdrawalRequest) {
        return (!(StringUtils.equals(withdrawalRequest.getPaymentTo(),
                WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)));
    }

    /**
     * Determines if the bank account number field is shown.
     * 
     * @return boolean - True if the bank account number is shown, false otherwise.
     */
    public boolean getShowBankAccountNumber() {
        return ((StringUtils.equals(paymentMethodCode,
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE)) || (StringUtils.equals(
                paymentMethodCode, WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE)));

    }

    /**
     * Determines whether the check payee name should be editable or not. Currently the check payee
     * name is editable if:
     * <ul>
     * <li>Payment To is Rollover to IRA.
     * <li>Payment To is Rollover to plan.
     * <li>Payment To is After-tax contributions direct to participant, remainder to IRA (payee 1
     * only).
     * <li>Payment To is After-tax contributions direct to participant, remainder to plan (payee 1
     * only).
     * </ul>
     * 
     * @return the checkPayeeNameEditable
     */
    public boolean isCheckPayeeNameEditable(final WithdrawalRequest withdrawalRequest) {

        final String paymentTo = withdrawalRequest.getPaymentTo();
        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)
                || StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            return false;
        } else if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                || StringUtils
                        .equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)) {
            return true;
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)
                )  {
        	
            // Determine if we are the first payee or the second payee
            return isFirstPayee(withdrawalRequest);

        }else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	if(this.getParticipant()!=null && 
        			this.getParticipant().indexOf("Participant") !=-1 ){
        		return false;
        	}else{
        		return true;
        	}
        	
        } else {
            return false;
        }
    }
    
    /**
     * Determines whether the payee/provider name should be editable or not. Currently the payee/provider
     * name is editable if:
     * <ul>
     * <li> Payment To is Rollover to IRA.
     * <li> Payment To is Rollover to plan.
     * <li> Payment To is After-tax contributions direct to participant, remainder to IRA (payee 1
     * only).
     * <li> Payment To is After-tax contributions direct to participant, remainder to plan (payee 1
     * only).
     * </ul>
     * 
     * @return the eftPayeePartyNameEditable
     */    
    public boolean isEftPayeeNameEditable(final WithdrawalRequest withdrawalRequest) {

        final String paymentTo = withdrawalRequest.getPaymentTo();
        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)
                || StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            return false;
        } else if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                || StringUtils
                        .equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)) {
            return true;
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {

            // Determine if we are the first payee or the second payee
            return isFirstPayee(withdrawalRequest);

        }else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	if(this.getParticipant()!=null && 
        			this.getParticipant().indexOf("Participant") !=-1 ){
        		return false;
        	}else{
        		return true;
        	}
        	
        } else {
            return false;
        }

    }
    /**
     * Determines whether the payee/provider name should be editable or not. Currently the payee/provider
     * name is editable if:
     * <ul>
     * <li> Payment To is Rollover to IRA.
     * <li> Payment To is Rollover to plan.
     * <li> Payment To is After-tax contributions direct to participant, remainder to IRA (payee 1
     * only).
     * <li> Payment To is After-tax contributions direct to participant, remainder to plan (payee 1
     * only).
     * </ul>
     * 
     * @return the payeeAddressEditable
     */    
    public boolean isPayeeAddressEditable(final WithdrawalRequest withdrawalRequest) {

        final String paymentTo = withdrawalRequest.getPaymentTo();
        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            return false;
        } else if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)
                || StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)) {
            return true;
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {

            // Determine if we are the first payee or the second payee
            //return isFirstPayee(withdrawalRequest);
        	return true;

        }else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	if(this.getParticipant()!=null){
        		return true;
        	}else{
        		return false;
        	}
        	
        }else {
            return false;
        }
    }


	public BigDecimal getWithdrawalAmount() {
		return WithdrawalAmount;
	}

	public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
		WithdrawalAmount = withdrawalAmount;
	}

	public String getTaxes() {
		return taxes;
	}

	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}
	
	public String getRolloverType() {
		return rolloverType;
	}

	public void setRolloverType(String rolloverType) {
		this.rolloverType = rolloverType;
	}

}
