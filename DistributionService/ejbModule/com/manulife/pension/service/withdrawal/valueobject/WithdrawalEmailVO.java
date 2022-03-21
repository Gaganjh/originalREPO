package com.manulife.pension.service.withdrawal.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.service.security.Principal;

/**
 * Value object used to hold minimal information about the request so that we can create and
 * populate the email message.
 * 
 * @author Mihai Popa
 */
public class WithdrawalEmailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer submissionId;

    private Date lastStatusUpdateTS;

    private Integer contractId;

    private String firstName;

    private String lastName;

    private String middleInitial;

    private String contractName;

    private Location siteLocation;

    private String requestStatus;

    private String requestType;

    private Date requestDate;

    private Date expirationDate;

    private Principal principal;

    private String carEmail;

    private BigDecimal userIdOfTpaWhoLastUpdatedFee;

    private boolean plansponsorLastPersionToUpdateFee;

    private String cmaSiteCode;

    private int employeeProfileId;

    public String getCmaSiteCode() {
        return cmaSiteCode;
    }

    public void setCmaSiteCode(String cmaSiteCode) {
        this.cmaSiteCode = cmaSiteCode;
    }

    public String getCarEmail() {
        return carEmail;
    }

    public void setCarEmail(String carEmail) {
        this.carEmail = carEmail;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public WithdrawalEmailVO(Integer submissionId, Integer contractId, String contractName,
            Date lastStatusUpdateTS, String requestStatus, String requestType, String firstName,
            String lastName, String middleInitial, Location siteLocation, Date requestDate,
            Date expirationDate, Principal principal, String carEmail,
            BigDecimal userIdOfTpaWhoLastUpdatedFee, boolean wasPlansponsorLastPersionToUpdateFee,
            String cmaSiteCode, int employeeProfileId) {
        this.submissionId = submissionId;
        this.contractId = contractId;
        this.contractName = contractName;
        this.requestStatus = requestStatus;
        this.requestType = requestType;
        this.lastStatusUpdateTS = lastStatusUpdateTS;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.siteLocation = siteLocation;
        this.requestDate = requestDate;
        this.expirationDate = expirationDate;
        this.principal = principal;
        this.carEmail = carEmail;
        this.userIdOfTpaWhoLastUpdatedFee = userIdOfTpaWhoLastUpdatedFee;
        this.plansponsorLastPersionToUpdateFee = wasPlansponsorLastPersionToUpdateFee;
        this.cmaSiteCode = cmaSiteCode;
        this.employeeProfileId = employeeProfileId;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public Date getLastStatusUpdateTS() {
        return lastStatusUpdateTS != null ? lastStatusUpdateTS : new Date();
    }

    public Integer getContractId() {
        return contractId;
    }

    public String getContractName() {
        return contractName.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getRequestType() {
        return requestType;
    }

    public Location getSiteLocation() {
        return siteLocation;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public String getParticipantName() {
        return firstName.trim()
                + " "
                + (middleInitial != null && middleInitial.trim().length() > 0 ? middleInitial
                        .trim()
                        + " " : "") + lastName.trim();
    }

    @Override
    public String toString() {

        return "WithdrawalEmailVO " + " submissionId = " + submissionId + " contractId = "
                + contractId + " contractName = " + contractName + " requestStatus = "
                + requestStatus + " requestType = " + requestType + " lastStatusUpdateTS = "
                + lastStatusUpdateTS + " name = " + getParticipantName() + " siteLocation = "
                + siteLocation;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public BigDecimal getUserIdOfTpaWhoLastUpdatedFee() {
        return userIdOfTpaWhoLastUpdatedFee;
    }

    public void setUserIdOfTpaWhoLastUpdatedFee(BigDecimal userIdOfTpaWhoLastUpdatedFee) {
        this.userIdOfTpaWhoLastUpdatedFee = userIdOfTpaWhoLastUpdatedFee;
    }

    public boolean isPlansponsorLastPersionToUpdateFee() {
        return plansponsorLastPersionToUpdateFee;
    }

    public void setPlansponsorLastPersionToUpdateFee(boolean wasPlansponsorLastPersionToUpdateFee) {
        this.plansponsorLastPersionToUpdateFee = wasPlansponsorLastPersionToUpdateFee;
    }

    public int getEmployeeProfileId() {
        return employeeProfileId;
    }

    public void setEmployeeProfileId(int employeeProfileId) {
        this.employeeProfileId = employeeProfileId;
    }

}
