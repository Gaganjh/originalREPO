/*
 * WithdrawalRequestMoneyTypeUi.java,v 1.1.2.2 2006/08/29 13:25:27 Paul_Glenn Exp
 * WithdrawalRequestMoneyTypeUi.java,v
 * Revision 1.1.2.2  2006/08/29 13:25:27  Paul_Glenn
 * Update imports.
 *
 * Revision 1.1.2.1  2006/08/24 13:54:59  Paul_Glenn
 * Updates for step2.
 *
 */
package com.manulife.pension.ps.web.withdrawal;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.validator.ValidationError;

/**
 * WithdrawalRequestMoneyTypeUi provides String fields for non-String fields in the
 * {@link WithdrawalRequestMoneyType} object. To access String fields, just access the
 * {@link WithdrawalRequestMoneyType} object directly, as it's a field of this object.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.2 2006/08/29 13:25:27
 */
public class WithdrawalRequestMoneyTypeUi extends BaseWithdrawalUiObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(WithdrawalRequestMoneyTypeUi.class);

    private WithdrawalRequestMoneyType withdrawalRequestMoneyType;

    private transient WithdrawalRequestUi parent;

    // These are the non-String fields from the WithdrawalRequestMoneyType
    // class.
    private String vestingPercentage;

    private String withdrawalAmount;

    private String withdrawalPercentage;

    private static final String DEFAULT_MAXIMUM_PERCENTAGE = "100.00";

    private static final String VESTING_PATTERN = "##0.000";

    private static final String REQUESTED_PERCENT_PATTERN = "##0.00";

    private static final String VO_BEAN_NAME = "withdrawalRequestMoneyType";

    private static final String[] UI_FIELDS = { "vestingPercentage", "withdrawalAmount",
            "withdrawalPercentage" };

    /**
     * Default Constructor.
     * 
     * @param withdrawalRequestMoneyType The bean to create the data with.
     * @param parent A reference to the parent request object.
     */
    public WithdrawalRequestMoneyTypeUi(
            final WithdrawalRequestMoneyType withdrawalRequestMoneyType,
            final WithdrawalRequestUi parent) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setWithdrawalRequestMoneyType(withdrawalRequestMoneyType);
        this.parent = parent;

        convertFromBean();
    }

    /**
     * Converts the matching fields from the {@link WithdrawalRequestMoneyType} bean, to this
     * object.
     */
    public final void convertFromBean() {
        try {
            BeanUtils.copyProperties(this, withdrawalRequestMoneyType);

            // Special copy of numbers to format
            if (BooleanUtils.toBooleanDefaultIfNull(withdrawalRequestMoneyType
                    .getVestingPercentageUpdateable(), false)) {
                final DecimalFormat vestingFormatter = new DecimalFormat(VESTING_PATTERN);
                this.vestingPercentage = (withdrawalRequestMoneyType.getVestingPercentage() == null) ? StringUtils.EMPTY
                        : vestingFormatter
                                .format(withdrawalRequestMoneyType.getVestingPercentage());
            } else {
                this.vestingPercentage = null;
            }
            final DecimalFormat withdrawalPercentageFormatter = new DecimalFormat(
                    REQUESTED_PERCENT_PATTERN);
            this.withdrawalPercentage = (withdrawalRequestMoneyType.getWithdrawalPercentage() == null) ? StringUtils.EMPTY
                    : withdrawalPercentageFormatter.format(withdrawalRequestMoneyType
                            .getWithdrawalPercentage());
            final DecimalFormat currencyFormatter = new DecimalFormat(
                    WithdrawalRequestUi.CURRENCY_FORMAT_PATTERN);
            this.withdrawalAmount = (withdrawalRequestMoneyType.getWithdrawalAmount() == null) ? StringUtils.EMPTY
                    : currencyFormatter.format(withdrawalRequestMoneyType.getWithdrawalAmount());
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }

    }

    /**
     * Converts the matching fields from this object, to the {@link WithdrawalRequestMoneyType}
     * bean.
     */
    public final void convertToBean() {
        try {
            if (BooleanUtils.isTrue(withdrawalRequestMoneyType.getVestingPercentageUpdateable())) {
                BeanUtils.copyProperty(withdrawalRequestMoneyType, "vestingPercentage",
                        this.vestingPercentage);
            } // fi
            BeanUtils.copyProperty(withdrawalRequestMoneyType, "withdrawalAmount",
                    this.withdrawalAmount);
            BeanUtils.copyProperty(withdrawalRequestMoneyType, "withdrawalPercentage",
                    this.withdrawalPercentage);
            // BeanUtils.copyProperties(withdrawalRequestMoneyType, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * @return the vestingPercentage
     */
    public String getVestingPercentage() {
        return vestingPercentage;
    }

    /**
     * @param vestingPercentage the vestingPercentage to set
     */
    public void setVestingPercentage(final String vestingPercentage) {
        this.vestingPercentage = vestingPercentage;
    }

    /**
     * @return the withdrawalAmount
     */
    public String getWithdrawalAmount() {
        return withdrawalAmount;
    }

    /**
     * @param withdrawalAmount the withdrawalAmount to set
     */
    public void setWithdrawalAmount(final String withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    /**
     * @return the withdrawalPercentage
     */
    public String getWithdrawalPercentage() {
        return withdrawalPercentage;
    }

    /**
     * @param withdrawalPercentage the withdrawalPercentage to set
     */
    public void setWithdrawalPercentage(final String withdrawalPercentage) {
        this.withdrawalPercentage = withdrawalPercentage;
    }

    /**
     * @return the withdrawalRequestMoneyType
     */
    public WithdrawalRequestMoneyType getWithdrawalRequestMoneyType() {
        return withdrawalRequestMoneyType;
    }

    /**
     * @param withdrawalRequestMoneyType the withdrawalRequestMoneyType to set
     */
    public void setWithdrawalRequestMoneyType(
            final WithdrawalRequestMoneyType withdrawalRequestMoneyType) {
        this.withdrawalRequestMoneyType = withdrawalRequestMoneyType;
    }

    /**
     * @see com.manulife.pension.ps.web.withdrawal.WithdrawalMessageToValidationErrorTranslator#getValidationMessages(com.manulife.pension.ps.web.withdrawal.GraphLocation)
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getWithdrawalRequestMoneyType()));

        return messages;
    }

    public WithdrawalRequestUi getParent() {
        return parent;
    }

    public void setParent(WithdrawalRequestUi parent) {
        this.parent = parent;
    }
}
