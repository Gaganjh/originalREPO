package com.manulife.pension.ps.web.withdrawal;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.environment.valueobject.StateTaxType;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;
import com.manulife.pension.util.NumberUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * WithdrawalRequestRecipientUi provides String fields for non-String fields in the
 * {@link WithdrawalRequestRecipient} object. To access String fields, just access the
 * {@link WithdrawalRequestRecipient} object directly, as it's a field of this object.
 * 
 * @author Andrew Dick
 */
public class WithdrawalRequestRecipientUi extends BaseWithdrawalUiObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(WithdrawalRequestRecipientUi.class);

    private static final String NAME_SEPARATOR = " ";

    private static final String STATE_PERCENT_PATTERN = "##0.0000";

    private WithdrawalRequestRecipient withdrawalRequestRecipient;

    private transient WithdrawalRequestUi parent;

    private static final String VO_BEAN_NAME = "withdrawalRequestRecipient";

    private static final String[] UI_FIELDS = { "recipientNo", "usCitizenInd", "shareValue",
            "stateTaxPercent" };

    // These are the non-String fields from the WithdrawalRequestRecipient class
    private String recipientNo;

    private String usCitizenInd;

    private String shareValue;

    private String stateTaxPercent;

    private AddressUi addressUi;

    private Collection<WithdrawalRequestPayeeUi> payees;
    
    // attributes for PDF
    private boolean showParticipantUsCitizenRow;
    private boolean nonUsPayeeExists;
    

    /**
     * Default Constructor.
     * 
     * @param withdrawalRequestRecipient The bean to create the data with.
     * @param parent A reference to the parent request object.
     */
    public WithdrawalRequestRecipientUi(
            final WithdrawalRequestRecipient withdrawalRequestRecipient,
            final WithdrawalRequestUi parent) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setWithdrawalRequestRecipient(withdrawalRequestRecipient);
        this.parent = parent;

        convertFromBean();
    }

    /**
     * Converts the matching fields from the {@link WithdrawalRequestRecipient} bean, to this
     * object.
     */
    public final void convertFromBean() {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("convertFromBean> Pre-convert U.S. Citizen UI is [")
                    .append(getUsCitizenInd()).append("] and U.S. Citizen [").append(
                            withdrawalRequestRecipient.getUsCitizenInd()).append("].").toString());
        }

        try {
            BeanUtils.copyProperties(this, withdrawalRequestRecipient);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        } // end try/catch

        final DecimalFormat stateTaxPercentageFormatter = new DecimalFormat(STATE_PERCENT_PATTERN);
        this.stateTaxPercent = (withdrawalRequestRecipient.getStateTaxPercent() == null) ? StringUtils.EMPTY
                : stateTaxPercentageFormatter.format(withdrawalRequestRecipient
                        .getStateTaxPercent());
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("convertFromBean> Converted state tax percent from [")
                    .append(withdrawalRequestRecipient.getStateTaxPercent()).append("] to [")
                    .append(withdrawalRequestRecipient.getStateTaxPercent()).append("].")
                    .toString());
        }

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("convertFromBean> Post-convert U.S. Citizen UI is [")
                    .append(getUsCitizenInd()).append("] and U.S. Citizen [").append(
                            withdrawalRequestRecipient.getUsCitizenInd()).append("].").toString());
        }

        // Convert address.
        this.addressUi = new AddressUi((Address)withdrawalRequestRecipient.getAddress());

        // Convert payees
        if (CollectionUtils.isEmpty(withdrawalRequestRecipient.getPayees())) {
            setPayees(new ArrayList<WithdrawalRequestPayeeUi>(0));
        } else {
            setPayees(new ArrayList<WithdrawalRequestPayeeUi>(withdrawalRequestRecipient
                    .getPayees().size()));

            for (Payee withdrawalRequestPayee : withdrawalRequestRecipient
                    .getPayees()) {

                getPayees().add(new WithdrawalRequestPayeeUi((WithdrawalRequestPayee)withdrawalRequestPayee, this));
            } // end for
        } // fi

    }

    /**
     * Converts the matching fields from this object, to the {@link WithdrawalRequestRecipient}
     * bean.
     */
    public final void convertToBean() {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("convertToBean> Pre-convert U.S. Citizen UI is [")
                    .append(getUsCitizenInd()).append("] and U.S. Citizen [").append(
                            withdrawalRequestRecipient.getUsCitizenInd()).append("].").toString());
        }

        try {
            BeanUtils.copyProperties(withdrawalRequestRecipient, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }

        // Convert Participant U.S. Citizen as BeanUtils converts null to false
        withdrawalRequestRecipient.setUsCitizenInd(BooleanUtils.toBooleanObject(getUsCitizenInd()));

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("convertToBean> Post-convert U.S. Citizen UI is [")
                    .append(getUsCitizenInd()).append("] and U.S. Citizen [").append(
                            withdrawalRequestRecipient.getUsCitizenInd()).append("].").toString());
        }

        // Ensure that we've copied the state of residence value into the recipient.
        // TEMPORARY DEBUGGING TO CATCH SPORADIC NULLPOINTER PROBLEM
        if (getWithdrawalRequestRecipient() == null) {
            logger.error(new StringBuffer(
                    "convertToBean> getWithdrawalRequestRecipient() was NULL [")
                    .append(getParent()).toString());
        }
        if (getParent() == null) {
            logger.error(new StringBuffer("convertToBean> getParent() was NULL [").append(
                    getWithdrawalRequestRecipient()).toString());
        } else {
            if (getParent().getWithdrawalRequest() == null) {
                logger.error(new StringBuffer(
                        "convertToBean> getParent().getWithdrawalRequest() was NULL [").append(
                        getParent()).toString());
            }
        }
        getWithdrawalRequestRecipient().setStateOfResidenceCode(
                getParent().getWithdrawalRequest().getParticipantStateOfResidence());

        // Convert payees
        if (CollectionUtils.isEmpty(getPayees())) {
            withdrawalRequestRecipient.setPayees(new ArrayList<Payee>(0));
        } else {
            final Collection<Payee> convertedPayees = new ArrayList<Payee>(
                    getPayees().size());

            for (WithdrawalRequestPayeeUi withdrawalRequestPayeeUi : getPayees()) {

                withdrawalRequestPayeeUi.convertToBean();

                convertedPayees.add(withdrawalRequestPayeeUi.getWithdrawalRequestPayee());
            } // end for
            withdrawalRequestRecipient.setPayees(convertedPayees);
        } // fi
    }

    /**
     * @return the payees
     */
    public Collection<WithdrawalRequestPayeeUi> getPayees() {
        return payees;
    }

    /**
     * @param payees the payees to set
     */
    public void setPayees(final Collection<WithdrawalRequestPayeeUi> payees) {
        this.payees = payees;
    }

    /**
     * @return the recipientNo
     */
    public String getRecipientNo() {
        return recipientNo;
    }

    /**
     * @param recipientNo the recipientNo to set
     */
    public void setRecipientNo(final String recipientNo) {
        this.recipientNo = recipientNo;
    }

    /**
     * @return the shareValue
     */
    public String getShareValue() {
        return shareValue;
    }

    /**
     * @param shareValue the shareValue to set
     */
    public void setShareValue(final String shareValue) {
        this.shareValue = shareValue;
    }

    /**
     * @return the usCitizenInd
     */
    public String getUsCitizenInd() {
        return usCitizenInd;
    }

    /**
     * @param usCitizenInd the usCitizenInd to set
     */
    public void setUsCitizenInd(final String usCitizenInd) {
        this.usCitizenInd = usCitizenInd;
    }

    /**
     * @return the withdrawalRequestRecipient
     */
    public WithdrawalRequestRecipient getWithdrawalRequestRecipient() {
        return withdrawalRequestRecipient;
    }

    /**
     * @param withdrawalRequestRecipient the withdrawalRequestRecipient to set
     */
    public void setWithdrawalRequestRecipient(
            final WithdrawalRequestRecipient withdrawalRequestRecipient) {
        this.withdrawalRequestRecipient = withdrawalRequestRecipient;
    }

    /**
     * @return the stateTaxPercent
     */
    public String getStateTaxPercent() {
        return stateTaxPercent;
    }

    /**
     * @param stateTaxPercent the stateTaxPercent to set
     */
    public void setStateTaxPercent(final String stateTaxPercent) {
        this.stateTaxPercent = stateTaxPercent;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {
        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getWithdrawalRequestRecipient()));

        // Get the address messages.
        messages.addAll(addressUi.getValidationMessages(new GraphLocation(graphLocation,
                "addressUi")));

        // Get the payee messages.
        int i = 0;
        for (WithdrawalRequestPayeeUi withdrawalRequestPayeeUi : getPayees()) {
            messages.addAll(withdrawalRequestPayeeUi.getValidationMessages(new GraphLocation(
                    graphLocation, "payees", i)));
            i++;
        } // end for

        return messages;
    }

    /**
     * @return the parent
     */
    public WithdrawalRequestUi getParent() {
        return parent;
    }

    /**
     * Retrieves the participant name which is the first name and last name concatenated and
     * separated by a single white space character.
     * 
     * @return String - The participant name constructed from the first and last name.
     */
    public String getParticipantName() {
        return new StringBuffer(StringUtils.defaultString(getWithdrawalRequestRecipient()
                .getFirstName())).append(NAME_SEPARATOR).append(
                StringUtils.defaultString(getWithdrawalRequestRecipient().getLastName()))
                .toString();
    }

    /**
     * Determines whether the participant U.S. citizen row should be displayed or suppressed.
     * Currently the participant U.S. citizen row is shown if the state of residence is outside the
     * US and:
     * <ul>
     * <li> Payment To is Direct to Participant.
     * <li> Payment To is Rollover to IRA.
     * <li> Payment To is Rollover to plan.
     * <li> Payment To is After-tax contributions direct to participant, remainder to IRA (payee 1
     * and 2)
     * <li> Payment To is After-tax contributions direct to participant, remainder to plan (payee 1
     * and 2).
     * </ul>
     * There is an additional dynamic constraint (controlled on page) that the row is only displayed
     * if the payee country (EFT or Check) is non-US.
     * 
     * @return boolean - True if the participant US citizen row should be displayed.
     */
    public boolean getShowParticipantUsCitizenRow() {
        final WithdrawalRequest withdrawalRequest = getParent().getWithdrawalRequest();

        return withdrawalRequestRecipient.getShowParticipantUsCitizenRow(withdrawalRequest);
    }

    private void setShowParticipantUsCitizenRow() {
        showParticipantUsCitizenRow = getShowParticipantUsCitizenRow();
    }    

    /**
     * This method determines if the state tax section is shown or suppressed. A true value returned
     * means that the section should be shown.
     * 
     * @return boolean - True if the state tax row should be displayed, false otherwise.
     */
    public boolean getShowStateTax() {
        return withdrawalRequestRecipient.getShowStateTax();
    }

    /**
     * This method determines if the state tax entry should be shown as a free form text entry field
     * or a drop down (select) input element. This returns true for a free form text entry, and
     * false for a drop down (select) input element.
     * 
     * @return boolean - True if the state tax entry should be a free form text entry, false
     *         otherwise.
     */
    public boolean getUseFreeFormStateTax() {

        final StateTaxVO stateTaxVo = this.getWithdrawalRequestRecipient().getStateTaxVo();
        if (stateTaxVo == null) {
            throw new RuntimeException("State Tax value object was null.");
        } // fi
        if ((!stateTaxVo.getTaxRequiredIndicator())
                && (BigDecimal.ZERO.compareTo(stateTaxVo.getDefaultTaxRatePercentage()) == 0)
                && (BigDecimal.ZERO.compareTo(stateTaxVo.getTaxPercentageMinimum()) == 0)
                && (BigDecimal.ZERO.compareTo(stateTaxVo.getTaxPercentageMaximum()) != 0)) {
            // The tax rate is not required, default and min rates are zero, and the max rate is
            // non-zero.
            return true;
        }// fi
        // CL 131784 March 2016: Added new condition for Puerto Rico State
		else if (("PR").equals(stateTaxVo.getStateCode())
				&& "PR".equals(getParent().getWithdrawalRequest()
						.getContractIssuedStateCode())) {
			//
			return true;
		}

        return false;
    }

    /**
     * This method sets the default state tax values.
     */
    public void setDefaultStateTax() {

        // Check to see if they have picked 'outside US' for a state value.
        if (StringUtils.equals(WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US,
                withdrawalRequestRecipient.getStateOfResidenceCode())) {
            logger.debug("setDefaultStateTax> State of residence outside US.");
            setStateTaxPercent(null);
            withdrawalRequestRecipient.setStateTaxTypeCode(null);
            return;
        }

        // If there is no state tax information, we just set the values to null.
        final StateTaxVO stateTaxVo = withdrawalRequestRecipient.getStateTaxVo();
        if (stateTaxVo == null) {
            logger.debug("setDefaultStateTax> State tax info is null.");
            withdrawalRequestRecipient.setStateTaxTypeCode(null);
            setStateTaxPercent(null);
            return;
        }

        // Set the state tax type code depending on the state information.
        withdrawalRequestRecipient.setStateTaxTypeCode(stateTaxVo.getTaxTypeCode());

        // Check for tax type none
        final StateTaxType stateTaxType = stateTaxVo.getStateTaxType();
        final BigDecimal currentStateTaxPercent = (StringUtils.isBlank(getStateTaxPercent())) ? null
                : new BigDecimal(getStateTaxPercent());
        switch (stateTaxType) {
            case NONE:

                logger.debug("setDefaultStateTax> State tax type is none.");
                withdrawalRequestRecipient.setStateTaxTypeCode(null);
                setStateTaxPercent(null);
                break;

            case VOLUNTARY_FREE_FORM:

                // Check for voluntary freeform (percentage is always retained)
                logger.debug("setDefaultStateTax> State tax type is voluntary free form.");
                if (StringUtils.isBlank(getStateTaxPercent())) {
                    setStateTaxPercent(formatTaxRateString(BigDecimal.ZERO));
                }
                break;

            case MANDATORY:

                // Check for state tax type mandatory
                logger.debug("setDefaultStateTax> State tax is mandatory.");
                setStateTaxPercent(formatTaxRateString(stateTaxVo.getDefaultTaxRatePercentage()));
                break;

            case VOLUNTARY_FIXED:

                // Check for state tax type voluntary fixed
                logger.debug("setDefaultStateTax> State tax is voluntary fixed.");
                // Redefault if null (was NONE) or if non-zero and not an allowed percent
                if ((currentStateTaxPercent == null)
                        || (NumberUtils.isNotZeroValue(currentStateTaxPercent) && !ObjectUtils
                                .equals(currentStateTaxPercent, stateTaxVo
                                        .getDefaultTaxRatePercentage()))) {
                    setStateTaxPercent(formatTaxRateString(BigDecimal.ZERO));
                }
                break;

            case OPT_OUT:

                // Check for state tax type opt out
                logger.debug("setDefaultStateTax> State tax is opt out.");
                if ((currentStateTaxPercent == null)
                        || (NumberUtils.isNotZeroValue(currentStateTaxPercent) && !ObjectUtils
                                .equals(currentStateTaxPercent, stateTaxVo
                                        .getDefaultTaxRatePercentage()))) {
                    setStateTaxPercent(formatTaxRateString(stateTaxVo.getDefaultTaxRatePercentage()));
                }
                break;

            default:
                // Unknown tax type - throw exception
                throw new RuntimeException(new StringBuffer("Unknown tax type [").append(
                        stateTaxVo.getStateTaxType()).append("]").toString());
        }
    }

    /**
     * Formats to the correct String format for taxes.
     * 
     * @param bigDecimal The value to convert and format.
     * @return String The converted formatted string.
     */
    private String formatTaxRateString(final BigDecimal bigDecimal) {
        return bigDecimal.setScale(WithdrawalRequest.TAX_PERCENTAGE_SCALE).toPlainString();
    }

    /**
     * @return the addressUi
     */
    public AddressUi getAddressUi() {
        return addressUi;
    }

    /**
     * @param addressUi the addressUi to set
     */
    public void setAddressUi(final AddressUi addressUi) {
        this.addressUi = addressUi;
    }

    /**
     * Queries if any of the payees has a non-US country.
     */
    public boolean getNonUsPayeeExists() {

        for (Payee payee : getWithdrawalRequestRecipient().getPayees()) {
            if (!StringUtils.equals(Address.USA_COUNTRY_CODE, payee.getAddress().getCountryCode())) {
                return true;
            }
        }

        return false;
    }
    
    private void setNonUsPayeeExists() {
    	nonUsPayeeExists = getNonUsPayeeExists();
    }
    
    /**
     * Sets the required values for PDF
     */
    public void setValuesForPDF() {
    	setShowParticipantUsCitizenRow();
        setNonUsPayeeExists();
    }
}
