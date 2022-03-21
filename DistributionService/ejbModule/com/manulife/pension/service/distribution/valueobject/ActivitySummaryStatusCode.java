package com.manulife.pension.service.distribution.valueobject;

/**
 * This contains the list of available activity summary status codes.
 * 
 * @author glennpa
 */
public enum ActivitySummaryStatusCode {

    SENT_FOR_REVIEW("SU"), SENT_FOR_APPROVAL("RE"), APPROVED("AP"), DENIED("DN"), DELETED("DL"),
    EXPIRED("EX"), SENT_FOR_ACCEPTANCE("SA"), LOAN_PACKAGE_REQUESTED("LB");

    private String statusCode;

    /**
     * Default Constructor.
     * 
     * @param statusCode The status code for this value.
     */
    private ActivitySummaryStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return String - The statusCode.
     */
    public String getStatusCode() {
        return statusCode;
    }

}
