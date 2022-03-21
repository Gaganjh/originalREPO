package com.manulife.pension.service.withdrawal.valueobject;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.withdrawal.helper.MoneyTypeFieldDef;
import com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef;

/**
 * @author Dennis
 * 
 */
public class SystemOfRecordValues extends BaseSerializableCloneableObject {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * map fields to values.
     */
    private Map<WithdrawalFieldDef, String> withdrawalValues = new HashMap<WithdrawalFieldDef, String>();

    // money type key mapped to a map field/values
    private Map<String, Map<MoneyTypeFieldDef, String>> moneyTypeValues = new HashMap<String, Map<MoneyTypeFieldDef, String>>();

    /**
     * @return the money type values
     */
    public final Map<String, Map<MoneyTypeFieldDef, String>> getMoneyTypeValues() {
        return moneyTypeValues;
    }

    /**
     * @param moneyTypeValues the money type value
     */
    public final void setMoneyTypeValues(
            final Map<String, Map<MoneyTypeFieldDef, String>> moneyTypeValues) {
        this.moneyTypeValues = moneyTypeValues;
    }

    /**
     * @return the withdrawal values
     */
    public final Map<WithdrawalFieldDef, String> getWithdrawalValues() {
        return withdrawalValues;
    }

    /**
     * @param withdrawalValues the withdrawal values
     */
    public final void setWithdrawalValues(final Map<WithdrawalFieldDef, String> withdrawalValues) {
        this.withdrawalValues = withdrawalValues;
    }

}
