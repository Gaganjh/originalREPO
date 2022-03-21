package com.manulife.pension.ps.web.onlineloans;

import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;

/**
 * Helper class to hold the common methods that are used for display and validation 
 * 
 * @author thangjo
 *
 */
public class LoanDisplayHelper {

	public static boolean isShowDeclarationsSection(Loan loan, UserRole userRoleWithPermissions) {
		
		// Draft status
		if (LoanStateEnum.DRAFT.getStatusCode().equals(loan.getStatus())) {
			return false;
		}

		// New status
		if (LoanStateEnum.DRAFT.getStatusCode().equals(loan.getStatus())
				&& loan.getSubmissionId() == null) {
			return false;
		}
		
		// The loan request is in pending review status and is a Participant
		// initiated request
		if (loan.isParticipantInitiated()
				&& LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(
						loan.getStatus())) {
			return false;
		}
		
		if (!loan.isBundledContract() || !loan.isSigningAuthorityForContractTpaFirm()) {

			// Logged in user does not have the signing authority permission
			
			if (!userRoleWithPermissions
					.hasPermission(PermissionType.SIGNING_AUTHORITY)) {
				return false;
			}
		}
		
		return true;
	}

}
