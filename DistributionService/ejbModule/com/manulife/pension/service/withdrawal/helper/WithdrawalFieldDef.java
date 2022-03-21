package com.manulife.pension.service.withdrawal.helper;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.exception.WithdrawalActivityHistoryException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * 
 * @author Dennis
 */
public enum WithdrawalFieldDef {

    STATE_OF_RESIDENCE(1, "participantStateOfResidence", "State of residence", true, Type.STRING),
    DATE_OF_BIRTH(2, "birthDate", "Date of birth", true, Type.DATE),
    EXPIRATION_DATE(3, "expirationDate", "Expiration date", false, Type.DATE),
    HARDSHIP_REASON(4, "reasonDetailCode", "Hardship reason", false, Type.STRING),
    // HARDSHIP_REASON_EXPLANATION(5, "reasonDescription", "Hardship Explanation", false,
    // Type.STRING),
    IRA_PROVIDER(6, "iraServiceProviderCode", "IRA provider", false, Type.STRING), EVENT_DATE(7,
            "", "", true, Type.DATE), FINAL_CONTRIBUTION_DATE(8, "finalContributionDate",
            "Final contribution date", false, Type.DATE), OUTSTANDING_LOANS_OPTION(9, "loanOption",
            "What should be done with all outstanding loans?", false, Type.STRING),
    IRS_DIST_CODE_FOR_LOANS(10, "irsDistributionCodeLoanClosure",
            "IRS distribution code for loans", false, Type.STRING), AMOUNT_TYPE_CODE(11,
            "amountTypeCode", "Enter amount as", false, Type.STRING), AMOUNT_VALUE(12,
            "withdrawalAmount", "Dollar amount", false, Type.STRING), TPA_FEE_AMOUNT(13,
            "fees[0].value", "TPA withdrawal fee amount", false, Type.STRING), TPA_FEE_TYPE(14,
            "fees[0].typeCode", "TPA withdrawal fee type", false, Type.STRING),
    OPTION_FOR_UNVESTED_AMOUNTS(15, "unvestedAmountOptionCode", "Option for unvested amounts",
            false, Type.STRING), FED_TAX_RATE(16, "recipients[0].federalTaxPercent",
            "Federal tax rate", false, Type.STRING), STATE_TAX_RATE(17,
            "recipients[0].stateTaxPercent", "State tax rate", false, Type.STRING),
    TEN99R_ADDRESS_LINE1(18, "recipients[0].address.addressLine1", "1099R Address line 1", false,
            Type.STRING), TEN99R_ADDRESS_LINE2(19, "recipients[0].address.addressLine2",
            "1099R Address line 2", false, Type.STRING), TEN99R_CITY(20,
            "recipients[0].address.city", "1099R City", false, Type.STRING), TEN99R_STATE(21,
            "recipients[0].address.stateCode", "1099R State", false, Type.STRING), TEN99R_ZIP(22,
            "recipients[0].address.zipCode1", "1099R Zip Code", false, Type.STRING),
    TEN99R_COUNTRY(23, "recipients[0].address.countryCode", "1099R Country", false, Type.STRING),
    TEN99R_US_CITIZEN_IND(24, "recipients[0].usCitizenInd", "US Citizen", false, Type.BOOLEAN);

    /**
     * Needed to type the fields to do extra operations.
     * 
     * @author Dennis
     */
    public enum Type {
        BOOLEAN, DATE, STRING;
        /**
         * contrstructor.
         */
        private Type() {
        }
    }

    private Integer id;

    private String fieldAccessor;

    private String name;

    private Type type;

    private Boolean systemOfRecord = false;

    public static final Integer DYN_MONEY_TYPE = 100;

    public static final Integer DYN_PAYEE_TYPE = 101;

    public static final Integer DYN_DECLARATION_TYPE = 102;

    public static final String CLASS_NAME = WithdrawalFieldDef.class.getName();

    /**
     * @param id The id of the field
     * @param fieldAccessor The field accessor
     * @param name The name of the field
     * @param systemOfRecord true if the field has a system of record value
     * @param type The Type of the field
     */
    private WithdrawalFieldDef(final Integer id, final String fieldAccessor, final String name,
            final Boolean systemOfRecord, final Type type) {
        this.id = id;
        this.fieldAccessor = fieldAccessor;
        this.name = name;
        this.systemOfRecord = systemOfRecord;
        this.type = type;
    }

    /**
     * @return the field accessor
     */
    public final String getFieldAccessor() {
        return fieldAccessor;
    }

    /**
     * @return the field id
     */
    public final Integer getId() {
        return id;
    }

    /**
     * Returns the value of the field represeted by this WithdrawalFieldDef.
     * 
     * @param obj The object being accessed
     * @return The value of the field represented by this WithdrawalFieldDef from the input object
     * @throws DistributionServiceException Thrown if an error is encountered
     */
    public final String getValue(final Object obj) throws DistributionServiceException {
        String returnVal = "";

        WithdrawalRequest wr = (WithdrawalRequest) obj;
        try {

            if (this == EVENT_DATE) {
                // have to call BeanUtils so that
                // it will run the date through the convetor
                if (StringUtils.isNotEmpty(wr.getReasonCode())) {
                    String withdrawalType = wr.getReasonCode();
                    if (WithdrawalReason.isTermination(withdrawalType)) {
                        returnVal = BeanUtils.getProperty(obj, "terminationDate");
                    } else if (withdrawalType.equals(WithdrawalReason.DISABILITY)) {
                        returnVal = BeanUtils.getProperty(obj, "disabilityDate");
                    } else if (withdrawalType.equals(WithdrawalReason.RETIREMENT)) {
                        returnVal = BeanUtils.getProperty(obj, "retirementDate");
                    } else if (withdrawalType.equals(WithdrawalReason.DEATH)) {
                        returnVal = BeanUtils.getProperty(obj, "deathDate");
                    }
                }
            } else if (this == TEN99R_ZIP) {
                String zip1 = StringUtils.trim(BeanUtils.getProperty(obj, getFieldAccessor()));
                String zip2 = StringUtils.trim(BeanUtils.getProperty(obj,
                        "recipients[0].address.zipCode2"));
                returnVal = StringUtils.isNotBlank(zip1) ? zip1
                        + (StringUtils.isNotBlank(zip2) ? " - " + zip2 : "") : "";
            } else if ((this == TPA_FEE_AMOUNT || this == TPA_FEE_TYPE) && wr.getFees().size() == 0) {
                returnVal = null;
            } else if ((this == TEN99R_US_CITIZEN_IND)
                    && (!((WithdrawalRequestRecipient)wr.getRecipients().iterator().next()).getShowParticipantUsCitizenField(wr))) {
                // Citizen indicator is suppressed so show blank
                returnVal = StringUtils.EMPTY;

            } else {
                returnVal = BeanUtils.getProperty(obj, getFieldAccessor());
            }
            if (this.getType() == Type.BOOLEAN) {
                if (returnVal == null) {
                    returnVal = Boolean.FALSE.toString();
                }
            }
        } catch (IllegalAccessException e) {
            throw new WithdrawalActivityHistoryException(e, CLASS_NAME, "getValue",
                    "failed to get Value from bean for field number" + this.getId().toString());
        } catch (InvocationTargetException e) {
            throw new WithdrawalActivityHistoryException(e, CLASS_NAME, "getValue",
                    "failed to get Value from bean for field number" + this.getId().toString());
        } catch (NoSuchMethodException e) {
            throw new WithdrawalActivityHistoryException(e, CLASS_NAME, "getValue",
                    "failed to get Value from bean for field number" + this.getId().toString());
        }
        if (StringUtils.isNotBlank(returnVal)) {
            if (this == AMOUNT_VALUE
                    || (this == TPA_FEE_AMOUNT && wr.getFees().iterator().next().getTypeCode()
                            .equals(WithdrawalRequestFee.DOLLAR_TYPE_CODE))) {
                returnVal = NumberFormat.getCurrencyInstance().format(Double.valueOf(returnVal));
            } else if (this == TPA_FEE_AMOUNT
                    && wr.getFees().iterator().next().getTypeCode().equals(
                            WithdrawalRequestFee.PERCENT_TYPE_CODE)) {
                returnVal = new DecimalFormat("#0.00%").format(Double.valueOf(returnVal) / 100);
            } else if (this == STATE_TAX_RATE || this == FED_TAX_RATE) {
                returnVal = new DecimalFormat("#0.0000%").format(Double.valueOf(returnVal) / 100);
            }
        }
        if (this == TEN99R_COUNTRY) {
            if (wr.getRecipients().iterator().next().getAddress().isBlank()) {
                returnVal = "";
            }
        }
        return returnVal;
    }

    /**
     * Turns an Id into a WithdrawalFieldDef instance.
     * 
     * @param itemNumber The id of the field.
     * @return the WithdrawalField def for the id
     */
    public static WithdrawalFieldDef getFieldFromItemNumber(final Integer itemNumber) {
        for (WithdrawalFieldDef field : WithdrawalFieldDef.values()) {
            if (field.getId().equals(itemNumber)) {
                return field;
            }
        }
        return null;
    }

    /**
     * @return the name of the field
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name the name of the field
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
     * @return the field type
     */
    public final Type getType() {
        return type;
    }

    /**
     * @param type the field type
     */
    public final void setType(final Type type) {
        this.type = type;
    }

    /**
     * Retuns the display name of the event field. Since this is a multiplexed field, it is a
     * special case.
     * 
     * @param wr The withdrawal request used to determine the withdrawal reason
     * @return The display name of the event date
     */
    public final String getEventName(final WithdrawalRequest wr) {
        String returnVal = "";
        if (StringUtils.isNotEmpty(wr.getReasonCode())) {
            String withdrawalType = wr.getReasonCode();
            if (WithdrawalReason.isTermination(withdrawalType)) {
                returnVal = "Termination date";
            } else if (withdrawalType.equals(WithdrawalReason.DISABILITY)) {
                returnVal = "Disability date";
            } else if (withdrawalType.equals(WithdrawalReason.RETIREMENT)) {
                returnVal = "Retirement date";
            } else if (withdrawalType.equals(WithdrawalReason.DEATH)) {
                returnVal = "Death date";
            }
        }
        return returnVal;
    }

    /**
     * Returns true if the field represents a dynamic field.
     * 
     * @param itemNumber The id of the field
     * @return true if the field represents a dynamic field.
     */
    public static boolean isDynamicField(final Integer itemNumber) {

        return itemNumber.intValue() == DYN_DECLARATION_TYPE.intValue()
                || itemNumber.intValue() == DYN_MONEY_TYPE.intValue()
                || itemNumber.intValue() == DYN_PAYEE_TYPE.intValue();
    }

}
