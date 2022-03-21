package com.manulife.pension.ps.service.withdrawal.email;

/**
 * Enumeration of all the available email types.
 * 
 * @author glennpa
 */
public enum EmailType {

    /**
     * The number of business days that need to pass since the request status was set to pending
     * review, before the email needs to be generated and sent.
     */
    REVIEW_MATURITY_DAYS("reviewMaturity"),
    /**
     * The number of business days that need to pass since the request status was set to pending
     * approval, before the email needs to be generated and sent.
     */
    APPROVE_MATURITY_DAYS("approveMaturity"),
    /**
     * The number of business days that need to pass since the request status was set to pending
     * review, before the email for car needs to be generated and sent.
     */
    CAR_REVIEW_MATURITY_DAYS("carReviewMaturity"),
    /**
     * The number of business days that need to pass since the request status was set to pending
     * approval, before the email for car needs to be generated and sent.
     */
    CAR_APPROVE_MATURITY_DAYS("carApproveMaturity"),
    /**
     * The number of business days before the request is expiring that a message has to be sent to
     * the reviewer.
     */
    REVIEW_EXPIRY_DAYS("reviewExpiry"),
    /**
     * The number of business days before the request is expiring that a message has to be sent to
     * the approver.
     */
    APPROVE_EXPIRY_DAYS("approveExpiry"),
    /**
     * The number of business days before the request is expiring that a message has to be sent to
     * the creator.
     */
    CREATOR_EXPIRY_DAYS("creatorExpiry");

    private String code;

    /**
     * Default Constructor.
     * 
     * @param code The code to use to lookup the field from the environment property.
     */
    private EmailType(final String code) {
        this.code = code;
    }

    /**
     * @return String - The code.
     */
    public String getCode() {
        return code;
    }

}
