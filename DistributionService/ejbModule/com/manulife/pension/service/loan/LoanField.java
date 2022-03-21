package com.manulife.pension.service.loan;


/**
 * 
 * @author Ted Matyszczuk
 */

public enum LoanField {

    LEGALLY_MARRIED_IND("legallyMarriedInd", 0, Type.BOOLEAN),
    LOAN_TYPE("loanType", 0, Type.STRING),
    LOAN_REASON("loanReason", 0, Type.STRING),
    EXPIRATION_DATE("expirationDate", 0, Type.DATE),
    PAYROLL_DATE("payrollDate", 0, Type.DATE),
    DEFAULT_PROVISION("defaultProvision", 0, Type.STRING),
    CURRENT_PARTICIPANT_NOTE("currentParticipantNote", 0, Type.STRING),
    CURRENT_ADMINISTRATOR_NOTE("currentAdministratorNote", 0, Type.STRING),
    MONEY_TYPE_VESTING_PERCENTAGE_PREFIX("moneyTypeVestingPercentage", 2, Type.PERCENT),
    MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS("maxBalanceLast12Months", 0, Type.AMOUNT),
    OUTSTANDING_LOANS_COUNT("outstandingLoansCount", 0, Type.NUMBER),
    CURRENT_OUTSTANDING_LOAN_BALANCE("currentOutstandingBalance", 0, Type.AMOUNT),
    LOAN_AMOUNT("loanAmount", 0, Type.AMOUNT),
    AMORTIZATION_MONTHS("amortizationMonths", 0, Type.NUMBER),
    PAYMENT_AMOUNT("paymentAmount", 0, Type.AMOUNT),
    PAYMENT_FREQUENCY("paymentFrequency", 0, Type.STRING),
    INTEREST_RATE("interestRate", 0, Type.PERCENT),
    TPA_LOAN_ISSUE_FEE("tpaLoanFee", 1, Type.AMOUNT),
    ABA_ROUTING_NUMBER("abaRoutingNumber", 13, Type.NUMBER),
    PAYMENT_METHOD("paymentMethod", 3, Type.STRING),
    ADDRESS_LINE1("addressLine1", 4, Type.STRING),
    ADDRESS_LINE2("addressLine2", 5, Type.STRING),
    CITY("city", 6, Type.STRING),
    STATE("state", 7, Type.STRING),
    ZIP_CODE("zipCode", 8, Type.STRING),
    COUNTRY("country", 9, Type.STRING),
    ACCOUNT_TYPE("accountType", 10, Type.STRING),
    ACCOUNT_NUMBER("accountNumber", 11, Type.STRING),
    BANK_NAME("bankName", 12, Type.STRING),
    TRUTH_IN_LENDING_NOTICE("truthInLendingNotice", 0, Type.BOOLEAN),
    PROMISSORY_NOTE("promissoryNote", 0, Type.BOOLEAN),
    AT_RISK_TRANSACTION("atRiskTransaction", 0, Type.BOOLEAN);

    /**
     * Needed to type the fields to do extra operations.
     */
    public enum Type {
        BOOLEAN, DATE, STRING, AMOUNT, PERCENT, NUMBER;
        private Type() {
        }
    }
    
    private String fieldName;

    private Integer activityDetailItemNo;
    
    private Type type;
    
    /**
     * contrstructor.
     */
    private LoanField(String fieldName, int activityDetailItemNo, Type type) {
        this.fieldName = fieldName;
        this.activityDetailItemNo = activityDetailItemNo;
        this.type = type;
    }

    public String toString() {
        return fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getActivityDetailItemNo() {
        return activityDetailItemNo;
    }

    public void setActivityDetailItemNo(int activityDetailItemNo) {
        this.activityDetailItemNo = activityDetailItemNo;
    }
    
    /**
     * Turns an Id into a LoanField instance.
     * 
     * @param itemNumber The itemNo of the field.
     * @return the LoanField for the itemNo
     */
    public static LoanField getFieldFromItemNumber(final Integer itemNumber) {
        for (LoanField field : LoanField.values()) {
            if (field.getActivityDetailItemNo().equals(itemNumber)) {
                return field;
            }
        }
        return null;
    }

    public Type getType() {
        return type;
    }

}
