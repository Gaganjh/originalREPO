/**
 * Created on August 23, 2006
 */
package com.manulife.pension.ps.service.withdrawal.valueobject;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemContractNameComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemContractNumberComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemInitiatedByComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemParticipantNameComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemReferenceNumberComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemRequestDateComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemRequestFromDateComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemRequestReasonComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemRequestTypeComparator;
import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemStatusComparator;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.security.role.BundledGaApprover;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;

/**
 * One item in the Loan and Withdrawal report screen.
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalItem implements Serializable {

    private static final long serialVersionUID = 2L;

    private static final Logger logger = Logger.getLogger(LoanAndWithdrawalItem.class);

    private static final String DESCENDING_INDICATOR = "DESC";

    public static final String SORT_REQUEST_TYPE = "requestType";

    public static final String SORT_REQUEST_REASON = "requestReason";

    public static final String SORT_CONTRACT_NAME = "contractName";

    public static final String SORT_CONTRACT_NUMBER = "contractNumber";

    public static final String SORT_PARTICIPANT_NAME = "partcipantName";

    public static final String SORT_SSN = "ssn";

    public static final String SORT_REQUEST_DATE = "requestDate";

    public static final String SORT_REFERENCE_NUMBER = "referenceNumber";

    public static final String SORT_STATUS = "status";

    public static final String SORT_INITIATED_BY = "initiatedBy";

    private static Map comparators = new HashMap();
    static {
        comparators.put(LoanAndWithdrawalReportData.SORT_REQUEST_FROM_DATE,
                new LoanAndWithdrawalItemRequestFromDateComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_REQUEST_DATE,
                new LoanAndWithdrawalItemRequestDateComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_REQUEST_TYPE,
                new LoanAndWithdrawalItemRequestTypeComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_REQUEST_REASON,
                new LoanAndWithdrawalItemRequestReasonComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_CONTRACT_NAME,
                new LoanAndWithdrawalItemContractNameComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_CONTRACT_NUMBER,
                new LoanAndWithdrawalItemContractNumberComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_PARTICIPANT_NAME,
                new LoanAndWithdrawalItemParticipantNameComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_REFERENCE_NUMBER,
                new LoanAndWithdrawalItemReferenceNumberComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_STATUS,
                new LoanAndWithdrawalItemStatusComparator());
        comparators.put(LoanAndWithdrawalReportData.SORT_INITIATED_BY,
                new LoanAndWithdrawalItemInitiatedByComparator());
    }

    private Date requestFromDate;

    private Date requestToDate;

    private String requestType;

    private String requestReason;

    private String contractName;

    private Integer contractNumber;

    private String participantName;

    private String ssn;

    private Date requestDate;

    private Integer referenceNumber;

    private String status;

    private String statusCode;

    private String initiatedBy;

    private int profileId;

    private String firstName;

    private String lastName;

    private String middleInitial;

    public boolean draft;

    public boolean approved;

    public boolean readyForEntry;

    public boolean denied;

    private UserRole userRole;

    private ContractInfo contractInformation;

    private Integer userProfileId;

    private Integer createdByUserProfileId;

    public Integer getCreatedByUserProfileId() {
        return createdByUserProfileId;
    }

    public void setCreatedByUserProfileId(Integer createdByUserProfileId) {
        this.createdByUserProfileId = createdByUserProfileId;
    }

    public Integer getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Integer userProfileId) {
        this.userProfileId = userProfileId;
    }

    public ContractInfo getContractInformation() {
        return contractInformation;
    }

    public void setContractInformation(ContractInfo contractInformation) {
        this.contractInformation = contractInformation;
    }

    public LoanAndWithdrawalItem(final Date requestFromDate, final Date requestToDate,
            final String requestType, final String requestReason, final String contractName,
            final Integer contractNumber, final String participantName, final String ssn,
            final Date requestDate, final Integer referenceNumber, final String status,
            final String initiatedBy, final Integer profileId) {

        this.requestFromDate = requestFromDate;
        this.requestToDate = requestToDate;
        this.requestType = requestType;
        this.requestReason = requestReason;
        this.contractName = contractName;
        this.contractNumber = contractNumber;
        this.participantName = participantName;
        this.ssn = ssn;
        this.requestDate = requestDate;
        this.referenceNumber = referenceNumber;
        this.status = status;
        this.initiatedBy = initiatedBy;
        this.profileId = profileId;
    }

    public String getContractName() {
        return contractName;
    }

    public Integer getContractNumber() {
        return contractNumber;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public String getParticipantName() {
        return StringUtils.trim(lastName)
                + ", "
                + StringUtils.trim(firstName)
                + " "
                + ((StringUtils.equals(middleInitial, null) || StringUtils.equals(middleInitial,
                        StringUtils.EMPTY)) ? "" : middleInitial.trim());
    }

    public Integer getReferenceNumber() {
        return referenceNumber;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public Date getRequestFromDate() {
        return requestFromDate;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public Date getRequestToDate() {
        return requestToDate;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getSsn() {
        return ssn;
    }

    public String getStatus() {
        return status;
    }

    public LoanAndWithdrawalItem() {
        super();
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public void setContractNumber(Integer contractNumber) {
        this.contractNumber = contractNumber;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public void setReferenceNumber(Integer referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns a compartor for the given sort field and direction
     * 
     * @param sortField the sort field
     * @param sortDirection the sort direction ASC or DESC
     * @return the Compartor
     */
    public static Comparator getComparator(String sortField, String sortDirection) {
        LoanAndWithdrawalItemComparator comparator = comparators.containsKey(sortField) ? (LoanAndWithdrawalItemComparator) comparators
                .get(sortField)
                : (LoanAndWithdrawalItemComparator) comparators
                        .get(LoanAndWithdrawalReportData.SORT_REQUEST_DATE);
        comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection));
        return comparator;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    private boolean isPendingApproval() {
        return statusCode.equalsIgnoreCase(WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode());
    }

    private boolean isPendingReview() {
        return statusCode.equalsIgnoreCase(WithdrawalStateEnum.PENDING_REVIEW.getStatusCode());
    }

    private boolean isDraft() {
        return statusCode.equalsIgnoreCase(WithdrawalStateEnum.DRAFT.getStatusCode());
    }
    

    private boolean isApproved() {
        return statusCode.equalsIgnoreCase(WithdrawalStateEnum.APPROVED.getStatusCode());
    }

    private boolean isReadyForEntry() {
        return statusCode.equalsIgnoreCase(WithdrawalStateEnum.READY_FOR_ENTRY.getStatusCode());
    }

    private boolean isDenied() {
        return statusCode.equalsIgnoreCase(WithdrawalStateEnum.DENIED.getStatusCode());
    }

    private boolean isExpired() {
        return statusCode.equalsIgnoreCase(WithdrawalStateEnum.EXPIRED.getStatusCode());
    }

    private boolean isDeleted() {
        return statusCode.equalsIgnoreCase(WithdrawalStateEnum.DELETED.getStatusCode());
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public boolean getShowEditRequestLink() {

    	boolean hasSigningAuthorityPermission = getUserRole().hasPermission(PermissionType.SIGNING_AUTHORITY);
    	
    	if (getUserRole() instanceof BundledGaApprover) {
    		return false;
    	} else if (isPendingReview() && isActiveOrFrozen()
                && getUserRole().hasPermission(PermissionType.REVIEW_WITHDRAWALS)) {
            return true;
        } else if (isPendingApproval() && isActiveOrFrozen() && !isSegregationOfDuties()
                && hasSigningAuthorityPermission) {
            return true;
        } else if (isPendingApproval() && isActiveOrFrozen() && isSegregationOfDuties()
                && hasSigningAuthorityPermission && !isUserCreator()) {
            return true;
        } else if (isDraft() && isActiveOrFrozen() && isUserCreator()
                && getUserRole().hasPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE)) {
            return true;
        } else if (isDraft() && isActiveOrFrozen() && getUserRole().isInternalUser() && getContractInformation().isBundledGaIndicator()
                && getUserRole().hasPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE)) {
            // Internal User + INITIATE privilege is allowed (not nec. limited to BGA Rep) 
            return true;
        }

        return false;
    }
    

    private boolean isUserCreator() {
        return getCreatedByUserProfileId().intValue() == getUserProfileId().intValue();
    }

    private boolean isSegregationOfDuties() {
        return !getContractInformation().getInitiatorMayApprove();
    }

    private boolean isActiveOrFrozen() {
        return StringUtils.equals(ContractInfo.CONTRACT_STATUS_ACTIVE, getContractInformation()
                .getStatus())
                || StringUtils.equals(ContractInfo.CONTRACT_STATUS_FROZEN, getContractInformation()
                        .getStatus());
    }

    private boolean isDiscontinued() {
        return StringUtils.equals(ContractInfo.CONTRACT_STATUS_DISCONTINUED,
                getContractInformation().getStatus());
    }

    public boolean getShowViewRequestLink() {
        boolean show = getUserRole().hasPermission(PermissionType.VIEW_ALL_WITHDRAWALS);
        if (!isActiveOrFrozen() && !isDiscontinued()) {
            show = false;
        } else if (isDraft() || isExpired() || isDeleted()) {
            show = false;
        }

        return show;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    
    /** This method is used to check the permissions for dispalaying the edit button for loan request
     * @return boolean
     */
    public boolean getShowEditLoanRequestLink() {

    	boolean hasSigningAuthorityPermission = getUserRole().hasPermission(PermissionType.SIGNING_AUTHORITY);
    	
    	if (getUserRole() instanceof BundledGaApprover) {
    		return false;
    	} else if (isLoanPendingReview() && isActiveOrFrozen()
                && getUserRole().hasPermission(PermissionType.REVIEW_LOANS)) {
            return true;
        } else if (isLoanPendingApproval() && isActiveOrFrozen() && !isSegregationOfDutiesForLoans()
                && hasSigningAuthorityPermission) {
            return true;
        } else if (isLoanPendingApproval() && isActiveOrFrozen() && isSegregationOfDutiesForLoans()
                && hasSigningAuthorityPermission && !isUserCreator()) {
            return true;
        } else if (isLoanDraft() && isActiveOrFrozen() && isUserCreator()
                && getUserRole().hasPermission(PermissionType.INITIATE_LOANS)) {
            return true;
        } else if (isLoanDraft() && isActiveOrFrozen() && getUserRole().isInternalUser() && getContractInformation().isBundledGaIndicator()
        		&& getUserRole().hasPermission(PermissionType.INITIATE_LOANS)) {
            // Internal User + INITIATE privilege is allowed (not nec. limited to BGA Rep) 
        	return true;
        }

        return false;
    }
    /** This method is used to check the permissions for dispalaying the view button for loan request
     * @return boolean
     */
    public boolean getShowViewLoanRequestLink() {
        boolean show = getUserRole().hasPermission(PermissionType.VIEW_ALL_LOANS);
        if (!isActiveOrFrozen() && !isDiscontinued()) {
            show = false;
        } else if (isLoanDraft() || isLoanExpired() || isLoanDeleted()) {
            show = false;
        }

        return show;
    }    
    private boolean isLoanDraft(){
    	return statusCode.equalsIgnoreCase(LoanStateEnum.DRAFT.getStatusCode());
    }
    private boolean isLoanExpired() {
        return statusCode.equalsIgnoreCase(LoanStateEnum.EXPIRED.getStatusCode());
    }

    private boolean isLoanDeleted() {
        return statusCode.equalsIgnoreCase(LoanStateEnum.DELETED.getStatusCode());
    }    
    
    private boolean isLoanPendingReview() {
        return statusCode.equalsIgnoreCase(LoanStateEnum.PENDING_REVIEW.getStatusCode());
    }
    
    private boolean isLoanPendingApproval() {
        return statusCode.equalsIgnoreCase(LoanStateEnum.PENDING_APPROVAL.getStatusCode());
    }    
    
    private boolean isSegregationOfDutiesForLoans() {
        return !getContractInformation().getInitiatorMayApproveLoans();
    }    


}	