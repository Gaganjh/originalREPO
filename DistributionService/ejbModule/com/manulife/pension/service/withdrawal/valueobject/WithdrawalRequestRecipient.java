package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;

/**
 * The Recipient Value Object.
 * 
 */
public class WithdrawalRequestRecipient extends BaseWithdrawal implements Recipient {

    /**
     * FIELDS_TO_EXCLUDE_FROM_LOGGING.
     */
    private static final String[] FIELDS_TO_EXCLUDE_FROM_LOGGING = { "stateTaxVo" };

    /**
     * Defines the field limit for the state tax percentage.
     */
    public static final BigDecimal PERCENTAGE_FIELD_LIMIT_MAXIMUM = new BigDecimal("100.0000");

    /**
     * Defines the permitted number of payees per recipient.
     */
    public static final IntRange PAYEE_RANGE = new IntRange(1, 4);

    /**
     * Defines the state of residence for outside of the US.
     */
    public static final String STATE_OF_RESIDENCE_OUTSIDE_US = "ZZ";

    /**
     * Defines the state code for Puerto Rico state
     */
    public static final String PR_STATE = "PR";
    /**
     * Defines the state code for Connect Cut state
     */
    public static final String CT_STATE = "CT";
    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private int recipientNo;

    private String firstName;

    private String lastName;

    private String organizationName;

    private Boolean usCitizenInd;

    private String stateOfResidenceCode;

    private String shareTypeCode;

    private BigDecimal shareValue;

    private BigDecimal federalTaxPercent;

    private BigDecimal stateTaxPercent;

    private String stateTaxTypeCode;

    private String taxpayerIdentTypeCode;

    private String taxpayerIdentNo;

    private DistributionAddress address = new Address();

    private Collection<Payee> payees = new ArrayList<Payee>(0);

    private StateTaxVO stateTaxVo;

    /**
     * @return the address
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
     * @return the payees
     */
    public Collection<Payee> getPayees() {
        return payees;
    }

    /**
     * @param payees the payee to set
     */
    public void setPayees(final Collection<Payee> payees) {
        this.payees = (payees == null) ? new ArrayList<Payee>() : payees;
    }

    /**
     * @return the recipientNo
     */
    public int getRecipientNo() {
        return recipientNo;
    }

    /**
     * @param recipientNo the recipientNo to set
     */
    public void setRecipientNo(final int recipientNo) {
        this.recipientNo = recipientNo;
    }

    /**
     * @return the shareTypeCode
     */
    public String getShareTypeCode() {
        return shareTypeCode;
    }

    /**
     * @param shareTypeCode the shareTypeCode to set
     */
    public void setShareTypeCode(final String shareTypeCode) {
        this.shareTypeCode = shareTypeCode;
    }

    /**
     * @return the shareValue
     */
    public BigDecimal getShareValue() {
        return shareValue;
    }

    /**
     * @param shareValue the shareValue to set
     */
    public void setShareValue(final BigDecimal shareValue) {
        this.shareValue = shareValue;
    }

    /**
     * @return the stateOfResidenceCode
     */
    public String getStateOfResidenceCode() {
        return stateOfResidenceCode;
    }

    /**
     * @param stateOfResidenceCode the stateOfResidenceCode to set
     */
    public void setStateOfResidenceCode(final String stateOfResidenceCode) {
        this.stateOfResidenceCode = stateOfResidenceCode;
    }

    /**
     * @return the usCitizenInd
     */
    public Boolean getUsCitizenInd() {
        return usCitizenInd;
    }

    /**
     * @param usCitizenInd the usCitizenInd to set
     */
    public void setUsCitizenInd(final Boolean usCitizenInd) {
        this.usCitizenInd = usCitizenInd;
    }

    /**
     * @return the federal tax percent
     */
    public BigDecimal getFederalTaxPercent() {
        return federalTaxPercent;
    }

    /**
     * @param federalTaxPercent the federal tax percent
     */
    public void setFederalTaxPercent(final BigDecimal federalTaxPercent) {
        this.federalTaxPercent = federalTaxPercent;
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
     * @return the state tax percent
     */
    public BigDecimal getStateTaxPercent() {
        return stateTaxPercent;
    }

    /**
     * @param stateTaxPercent the state tax percent
     */
    public void setStateTaxPercent(final BigDecimal stateTaxPercent) {
        this.stateTaxPercent = stateTaxPercent;
    }

    /**
     * @return the state tax type code
     */
    public String getStateTaxTypeCode() {
        return stateTaxTypeCode;
    }

    /**
     * @param stateTaxTypeCode the state tax type code
     */
    public void setStateTaxTypeCode(final String stateTaxTypeCode) {
        this.stateTaxTypeCode = stateTaxTypeCode;
    }

    /**
     * @return the taxpayer ident number
     */
    public String getTaxpayerIdentNo() {
        return taxpayerIdentNo;
    }

    /**
     * @param taxpayerIdentNo the taxpayer ident number
     */
    public void setTaxpayerIdentNo(final String taxpayerIdentNo) {
        this.taxpayerIdentNo = taxpayerIdentNo;
    }

    /**
     * @return the taxpayer ident type code
     */
    public String getTaxpayerIdentTypeCode() {
        return taxpayerIdentTypeCode;
    }

    /**
     * @param taxpayerIdentTypeCode the taxpayer ident type code
     */
    public void setTaxpayerIdentTypeCode(final String taxpayerIdentTypeCode) {
        this.taxpayerIdentTypeCode = taxpayerIdentTypeCode;
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

        // Check payees
        for (final Payee payee : getPayees()) {
            if (((WithdrawalRequestPayee) payee).doErrorCodesExist()) {
                return true;
            }
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

        // Check payees
        for (final Payee payee : getPayees()) {
            if (((WithdrawalRequestPayee) payee).doWarningCodesExist()) {
                return true;
            }
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

        // Check payees
        for (final Payee payee : getPayees()) {
            if (((WithdrawalRequestPayee) payee).doAlertCodesExist()) {
                return true;
            }
        }

        // No alerts exist
        return false;
    }

    /**
     * @return the stateTaxVo
     */
    public StateTaxVO getStateTaxVo() {
        return stateTaxVo;
    }

    /**
     * @param stateTaxVo the stateTaxVo to set
     */
    public void setStateTaxVo(final StateTaxVO stateTaxVo) {
        this.stateTaxVo = stateTaxVo;
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

        // Check payees
        for (final Payee withdrawalRequestPayee : getPayees()) {
            ((WithdrawalRequestPayee) withdrawalRequestPayee).removeMessages();
        } // end for
    }

    /**
     * Determines whether the participant U.S. citizen row should be displayed or suppressed.
     * Currently the participant U.S. citizen row is shown if the state of residence is outside the
     * US and:
     * <ul>
     * <li>Payment To is Direct to Participant.
     * <li>Payment To is Rollover to IRA.
     * <li>Payment To is Rollover to plan.
     * <li>Payment To is After-tax contributions direct to participant, remainder to IRA (payee 1
     * and 2)
     * <li>Payment To is After-tax contributions direct to participant, remainder to plan (payee 1
     * and 2).
     * </ul>
     * There is an additional dynamic constraint (controlled on page) that the row is only displayed
     * if the payee country (EFT or Check) is non-US.
     * 
     * @param withdrawalRequest - The {@link WithdrawalRequest} to use.
     * @return boolean - True if the participant US citizen row should be displayed.
     */
    public boolean getShowParticipantUsCitizenRow(final WithdrawalRequest withdrawalRequest) {

        final String paymentTo = withdrawalRequest.getPaymentTo();

        // Ensure that the payment to field is appropriate.
        if (!(StringUtils
                .equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, paymentTo)
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, paymentTo)
                || StringUtils
                        .equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, paymentTo)
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                        paymentTo) || StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                paymentTo)|| StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION,
                        paymentTo))) {
            return false;
        } // fi

        return true;
    }

    /**
     * Determines whether the participant U.S. citizen field should be displayed or suppressed.
     * Currently the participant U.S. citizen field is shown if the state of residence is outside
     * the US and Payment To is one of:
     * <ul>
     * <li>Direct to Participant.
     * <li>Rollover to IRA.
     * <li>Rollover to plan.
     * <li>After-tax contributions direct to participant, remainder to IRA (payee 1 and 2)
     * <li>After-tax contributions direct to participant, remainder to plan (payee 1 and 2).
     * </ul>
     * There is an additional constraint that the field is only displayed if any payee country (EFT
     * or Check) is non-US.
     * 
     * @param withdrawalRequest - The {@link WithdrawalRequest} to use.
     * @return boolean - True if the participant US citizen field should be displayed.
     */
    public boolean getShowParticipantUsCitizenField(final WithdrawalRequest withdrawalRequest) {

        final String paymentTo = withdrawalRequest.getPaymentTo();

        // Ensure that the payment to field is appropriate.
        if (!(StringUtils
                .equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, paymentTo)
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, paymentTo)
                || StringUtils
                        .equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, paymentTo)
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                        paymentTo) || StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                paymentTo))) {
            return false;
        } // fi

        // Check the payee addresses for country != USA.
        boolean payeeCountryIsNotUsa = false;
        for (final Payee withdrawalRequestPayee : getPayees()) {
            final DistributionAddress myAddress = withdrawalRequestPayee.getAddress();
            if (myAddress != null) {
                if (StringUtils.isNotBlank(myAddress.getCountryCode())
                        && !StringUtils.equals(myAddress.getCountryCode(),
                                GlobalConstants.COUNTRY_CODE_USA)) {
                    payeeCountryIsNotUsa = true;
                    break;
                } // fi
            } // fi
        } // end for

        // True if at least one payee address is not USA.
        return payeeCountryIsNotUsa;
    }

    /**
     * This method determines if the state tax section is shown or suppressed. A true value returned
     * means that the section should be shown.
     * 
     * @return boolean - True if the state tax row should be displayed, false otherwise.
     */
    public boolean getShowStateTax() {
        if (StringUtils.equals(WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US,
                getStateOfResidenceCode())) {
            // The state of residence is set to 'outside of the US'.
            return false;
        } // fi

        if (stateTaxVo == null) {
            return false;
        } // fi

        if ((BigDecimal.ZERO.compareTo(stateTaxVo.getDefaultTaxRatePercentage()) == 0)
                && (BigDecimal.ZERO.compareTo(stateTaxVo.getTaxPercentageMinimum()) == 0)
                && (BigDecimal.ZERO.compareTo(stateTaxVo.getTaxPercentageMaximum()) == 0)) {
            // The default, min and max rates are all zero.
            return false;
        } // fi

        // Otherwise return true;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> fieldNamesToExcludeFromLogging() {

        final Collection<String> toExclude = new ArrayList<String>(
                FIELDS_TO_EXCLUDE_FROM_LOGGING.length);

        CollectionUtils.addAll(toExclude, FIELDS_TO_EXCLUDE_FROM_LOGGING);

        toExclude.addAll(super.fieldNamesToExcludeFromLogging());

        return toExclude;
    }

}
