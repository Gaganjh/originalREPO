package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.distribution.valueobject.Fee;

/**
 * WithdrawalRequestFee is the value object for the withdrawal fee.
 * 
 * @author Paul_Glenn
 * @author Chris_Shin
 * @version 1.3 2006/09/08 13:48:29
 */
public class WithdrawalRequestFee extends BaseWithdrawal implements Fee {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String typeCode;

    private BigDecimal value;

    /**
     * @return Returns the typeCode.
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @return Returns the value.
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param typeCode The typeCode to set.
     */
    public void setTypeCode(final String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(final BigDecimal value) {
        this.value = value;
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

        // No alerts exist
        return false;
    }

    /**
     * Determines if the fee has either a value or typeCode set.
     * 
     * @return boolean - True if the value and typeCode are not set, false otherwise.
     */
    public boolean isBlank() {
        if ((value == null) && (StringUtils.isBlank(typeCode))) {
            return true;
        } // fi

        return false;
    }
}
