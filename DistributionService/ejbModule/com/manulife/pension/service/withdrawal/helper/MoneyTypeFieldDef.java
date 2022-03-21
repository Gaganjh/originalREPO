package com.manulife.pension.service.withdrawal.helper;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * @author Dennis
 * 
 */
public enum MoneyTypeFieldDef {
    MT_VESTING_PERCENT(1, "vestingPercentage", true, "Vesting %"), MT_WITHDRAWAL_AMOUNT(2,
            "withdrawalAmount", false, "Amount");

    private Integer id;

    private String fieldAccessor;

    private String name;

    private Boolean systemOfRecord = false;

    /**
     * @param id The id of the field
     * @param fieldAccessor The accessor fo the field
     * @param systemOfRecord True of the field has a system of record value
     * @param name the name of the field
     */
    private MoneyTypeFieldDef(final Integer id, final String fieldAccessor,
            final Boolean systemOfRecord, final String name) {
        this.systemOfRecord = systemOfRecord;
        this.id = id;
        this.fieldAccessor = fieldAccessor;
        this.name = name;
    }

    /**
     * @return the field accessor
     */
    public final String getFieldAccessor() {
        return fieldAccessor;
    }

    /**
     * @return the id of the field
     */
    public final Integer getId() {
        return id;
    }

    /**
     * @param withdrawalRequest the current withdrawal request used for context
     * @param moneyType the current money type
     * @return the value from the money type representd by the this field
     */
    public final String getValue(final WithdrawalRequest withdrawalRequest,
            final WithdrawalRequestMoneyType moneyType) {
        String returnVal = "";

        if (this == MT_WITHDRAWAL_AMOUNT) {
            if (withdrawalRequest.getAmountTypeCode().equals(
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
                returnVal = moneyType.getWithdrawalAmount().toString();
            } else if (moneyType.getWithdrawalPercentage() != null) {
                returnVal = moneyType.getWithdrawalPercentage().toString();
            }
        } else {
            try {
                returnVal = BeanUtils.getProperty(moneyType, getFieldAccessor());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(returnVal)) {
            if (this == MT_WITHDRAWAL_AMOUNT) {
                if (withdrawalRequest.getAmountTypeCode().equals(
                        WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
                    returnVal = NumberFormat.getCurrencyInstance()
                            .format(Double.valueOf(returnVal));
                } else {
                    returnVal = new DecimalFormat("#0.00%").format(Double.valueOf(returnVal) / 100);
                }
            }
            if (this == MT_VESTING_PERCENT) {
                returnVal = new DecimalFormat("#0.000%").format(Double.valueOf(returnVal) / 100);
            }
        }
        return returnVal;
    }

    /**
     * @param itemNumber The id of the field to find.
     * @return the {@link MoneyTypeFieldDef} instance
     */
    public static MoneyTypeFieldDef getFieldFromItemNumber(final Integer itemNumber) {
        for (MoneyTypeFieldDef field : MoneyTypeFieldDef.values()) {
            if (field.getId().equals(itemNumber)) {
                return field;
            }
        }
        return null;
    }

    /**
     * @return The name of the field
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name The name of the field
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @return true if the field has a system of record value
     */
    public final Boolean getSystemOfRecord() {
        return systemOfRecord;
    }

    /**
     * @param systemOfRecord true if the field has a system of record value
     */
    public final void setSystemOfRecord(final Boolean systemOfRecord) {
        this.systemOfRecord = systemOfRecord;
    }

    /**
     * @param fieldAccessor the field accessor
     */
    public final void setFieldAccessor(final String fieldAccessor) {
        this.fieldAccessor = fieldAccessor;
    }

    /**
     * @param id the field id
     */
    public final void setId(final Integer id) {
        this.id = id;
    }

}
