package com.manulife.pension.ps.web.withdrawal;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.validator.ValidationError;

/**
 * WithdrawalRequestFeeUi provides String fields for non-String fields in the
 * {@link WithdrawalRequestFee} object. To access String fields, just access the
 * {@link WithdrawalRequestFee} object directly, as it's a field of this object.
 * 
 * @author Andrew Dick
 */
public class WithdrawalRequestFeeUi extends BaseWithdrawalUiObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private WithdrawalRequestFee withdrawalRequestFee;

    private static final String VO_BEAN_NAME = "withdrawalRequestFee";

    private static final String[] UI_FIELDS = { "submissionId", "partyId", "value" };

    /**
     * Fee value pattern.
     */
    private static final String FEE_VALUE_FORMAT_PATTERN = "#,##0."
            + StringUtils.rightPad("", WithdrawalRequestFee.FEE_VALUE_SCALE, "0");

    // These are the non-String fields from the WithdrawalRequestFee
    // class.

    private String submissionId;

    private String partyId;

    private String value;

    /**
     * Default Constructor.
     * 
     * @param withdrawalRequestFee The bean to create the data with.
     */
    public WithdrawalRequestFeeUi(final WithdrawalRequestFee withdrawalRequestFee) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setWithdrawalRequestFee(withdrawalRequestFee);

        convertFromBean();
    }

    /**
     * Converts the matching fields from the {@link WithdrawalRequestFee} bean, to this object.
     */
    public final void convertFromBean() {
        try {
            BeanUtils.copyProperties(this, withdrawalRequestFee);

            // Handle value formatting
            final DecimalFormat formatter = new DecimalFormat(FEE_VALUE_FORMAT_PATTERN);
            this.value = (withdrawalRequestFee.getValue() == null) ? StringUtils.EMPTY : formatter
                    .format(withdrawalRequestFee.getValue());

        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * Converts the matching fields from this object, to the {@link WithdrawalRequestFee} bean.
     */
    public final void convertToBean() {
        try {
            BeanUtils.copyProperties(withdrawalRequestFee, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * @return the partyId
     */
    public String getPartyId() {
        return partyId;
    }

    /**
     * @param partyId the partyId to set
     */
    public void setPartyId(final String partyId) {
        this.partyId = partyId;
    }

    /**
     * @return the submissionId
     */
    public String getSubmissionId() {
        return submissionId;
    }

    /**
     * @param submissionId the submissionId to set
     */
    public void setSubmissionId(final String submissionId) {
        this.submissionId = submissionId;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return the withdrawalRequestFee
     */
    public WithdrawalRequestFee getWithdrawalRequestFee() {
        return withdrawalRequestFee;
    }

    /**
     * @param withdrawalRequestFee the withdrawalRequestFee to set
     */
    public void setWithdrawalRequestFee(final WithdrawalRequestFee withdrawalRequestFee) {
        this.withdrawalRequestFee = withdrawalRequestFee;
    }

    /**
     * @see com.manulife.pension.ps.web.withdrawal.WithdrawalMessageToValidationErrorTranslator#getValidationMessages(com.manulife.pension.ps.web.withdrawal.GraphLocation)
     */
    public Collection<ValidationError> getValidationMessages(GraphLocation graphLocation) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getWithdrawalRequestFee()));

        return messages;
    }
}
