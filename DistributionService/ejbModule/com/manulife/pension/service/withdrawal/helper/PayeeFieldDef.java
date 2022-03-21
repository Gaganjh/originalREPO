package com.manulife.pension.service.withdrawal.helper;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;

public enum PayeeFieldDef {
    P_ROLLOVER_ACCOUNT_NO(1, "rolloverAccountNo", "Account number for rollover",
            WithdrawalFieldDef.Type.STRING),
    P_ROLLOVER_PLAN_NAME(2, "rolloverPlanName", "Name of new plan", WithdrawalFieldDef.Type.STRING),
    P_IRS_DIST_CODE(3, "irsDistCode", "IRS distribution code for withdrawal",
            WithdrawalFieldDef.Type.STRING),
    P_PAYMENT_METHOD_CODE(4, "paymentMethodCode", "Payment method", WithdrawalFieldDef.Type.STRING),
    P_BANKACCOUNT_TYPE_CODE(5, "paymentInstruction.bankAccountTypeCode", "Bank account type",
            WithdrawalFieldDef.Type.STRING), P_ORGANIZATION_NAME(23, "organizationName", "Name",
            WithdrawalFieldDef.Type.STRING),

    P_LAST_NAME(6, "lastName", "Last name", WithdrawalFieldDef.Type.STRING), P_FIRST_NAME(7,
            "firstName", "First name", WithdrawalFieldDef.Type.STRING), P_LINE1(8,
            "address.addressLine1", "Address line 1", WithdrawalFieldDef.Type.STRING), P_LINE2(9,
            "address.addressLine2", "Address line 2", WithdrawalFieldDef.Type.STRING), P_CITY(10,
            "address.city", "City", WithdrawalFieldDef.Type.STRING), P_STATE(11,
            "address.stateCode", "State", WithdrawalFieldDef.Type.STRING), P_ZIP(12,
            "address.zipCode1", "Zip Code", WithdrawalFieldDef.Type.STRING), P_COUNTRY(14,
            "address.countryCode", "Country", WithdrawalFieldDef.Type.STRING),

    P_BANKNAME(15, "paymentInstruction.bankName", "Bank / Branch Name",
            WithdrawalFieldDef.Type.STRING), P_BANK_TRANSIT_NUMBER(16,
            "paymentInstruction.bankTransitNumber", "ABA / Routing number",
            WithdrawalFieldDef.Type.STRING), P_BANK_ACCOUNT_NUMBER(17,
            "paymentInstruction.bankAccountNumber", "Account number",
            WithdrawalFieldDef.Type.STRING), P_CREDIT_PARTY_NAME(18,
            "paymentInstruction.creditPartyName", "Credit party name",
            WithdrawalFieldDef.Type.STRING),

    P_SEND_CHECK_COURIER(19, "sendCheckByCourier", "Send check by courier?",
            WithdrawalFieldDef.Type.BOOLEAN), P_COURIER_COMPANY_CODE(20, "courierCompanyCode",
            "Courier", WithdrawalFieldDef.Type.STRING), P_COURIER_NUMBER(21, "courierNo",
            "Your courier account number", WithdrawalFieldDef.Type.STRING),
    P_MAIL_CHECK_TO_ADDRESS(22, "mailCheckToAddress", "Mail to indicated address",
            WithdrawalFieldDef.Type.BOOLEAN);

    private Integer id;

    private String fieldAccessor;

    private String name;

    private WithdrawalFieldDef.Type type;

    /**
     * @return the field name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name the field name
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @param id the id of the field
     * @param fieldAccessor the accessor of the field
     * @param name the name of the field
     * @param type the type of field
     */
    private PayeeFieldDef(final Integer id, final String fieldAccessor, final String name,
            final WithdrawalFieldDef.Type type) {
        this.id = id;
        this.fieldAccessor = fieldAccessor;
        this.name = name;
        this.type = type;
    }

    /**
     * @return the field accessor
     */
    public final String getFieldAccessor() {
        return fieldAccessor;
    }

    /**
     * @return The id
     */
    public final Integer getId() {
        return id;
    }

    /**
     * Uses the current field instance to lookup a value on an object and return it.
     * 
     * @param wr the current withdrawal request
     * @param payee the current payee
     * @return the value from the payee represesnt by this field instance
     */
    public final String getValue(final WithdrawalRequest wr, final WithdrawalRequestPayee payee) {
        String returnVal = "";
        try {
            if (this == P_ZIP) {
                String zip1 = StringUtils.trim(BeanUtils.getProperty(payee, getFieldAccessor()));
                String zip2 = StringUtils.trim(BeanUtils.getProperty(payee, "address.zipCode2"));
                returnVal = StringUtils.isNotBlank(zip1) ? zip1
                        + (StringUtils.isNotBlank(zip2) ? " - " + zip2 : "") : "";
            } else {
                returnVal = BeanUtils.getProperty(payee, getFieldAccessor());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (this == P_ORGANIZATION_NAME) {

            // Need to handle case where organization name is check and display only
            final String paymentMethod = payee.getPaymentMethodCode();
            if (StringUtils.equals(paymentMethod, WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE)
                    && !payee.isCheckPayeeNameEditable(wr)) {

                returnVal = StringUtils.EMPTY;
            }
        }
        if (this == P_LINE1 || this == P_LINE2 || this == P_CITY || this == P_STATE
                || this == P_ZIP || this == P_COUNTRY) {

            if (payee.getAddress().isBlank()) {
                return StringUtils.EMPTY;
            }

        }
        return returnVal;
    }

    /**
     * gets an instance of this class using the Id as a key.
     * 
     * @param itemNumber the id of the field
     * @return the {@link PayeeFieldDef} reference
     */
    public static PayeeFieldDef getFieldFromItemNumber(final Integer itemNumber) {
        for (PayeeFieldDef field : PayeeFieldDef.values()) {
            if (field.getId().equals(itemNumber)) {
                return field;
            }
        }
        throw null;
    }

    /**
     * @return the field type
     */
    public final WithdrawalFieldDef.Type getType() {
        return type;
    }

    /**
     * @param type the field type
     */
    public final void setType(final WithdrawalFieldDef.Type type) {
        this.type = type;
    }
}
