/**
 * 
 * @ author kuthiha
 * Jan 2, 2007
 */
package com.manulife.pension.service.withdrawal.valueobject;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * This DTO contains the counts of the pending review requests and the pending approve requests.
 * 
 * @author kuthiha
 */
public class PendingReviewApproveWithdrawalCount extends BaseSerializableCloneableObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private Integer countPendingReviewRequests;

    private Integer countPendingApproveRequests;

    /**
     * Default Constructor.
     */
    public PendingReviewApproveWithdrawalCount() {
        super();
    }

    /**
     * @return Integer - The countPendingApproveRequests.
     */
    public Integer getCountPendingApproveRequests() {
        return countPendingApproveRequests;
    }

    /**
     * @param countPendingApproveRequests - The countPendingApproveRequests to set.
     */
    public void setCountPendingApproveRequests(final Integer countPendingApproveRequests) {
        this.countPendingApproveRequests = countPendingApproveRequests;
    }

    /**
     * @return Integer - The countPendingReviewRequests.
     */
    public Integer getCountPendingReviewRequests() {
        return countPendingReviewRequests;
    }

    /**
     * @param countPendingReviewRequests - The countPendingReviewRequests to set.
     */
    public void setCountPendingReviewRequests(final Integer countPendingReviewRequests) {
        this.countPendingReviewRequests = countPendingReviewRequests;
    }

}
