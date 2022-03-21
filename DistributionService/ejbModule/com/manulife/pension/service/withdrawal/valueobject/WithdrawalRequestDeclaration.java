package com.manulife.pension.service.withdrawal.valueobject;

import org.apache.commons.collections.CollectionUtils;

import com.manulife.pension.service.distribution.valueobject.Declaration;

/**
 * WithdrawalRequestDeclaration is the value object for the withdrawal declaration.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.4 2006/09/08 13:48:29
 */
public class WithdrawalRequestDeclaration extends BaseWithdrawal implements Declaration {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String typeCode;

    /**
     * @return Returns the typeCode.
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode The typeCode to set.
     */
    public void setTypeCode(final String typeCode) {
        this.typeCode = typeCode;
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
}
