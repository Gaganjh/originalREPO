package com.manulife.pension.ps.web.onlineloans;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.utility.SecurityHelper;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;

public class LoanActionForwardHelper {
	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/onlineloans/review.jsp");
		forwards.put("initiate","redirect:/do/onlineloans/initiate/");
		forwards.put("draft","redirect:/do/onlineloans/draft/");
		forwards.put("pendingApproval","redirect:/do/onlineloans/approve/");
		forwards.put("pendingReview","redirect:/do/onlineloans/review/");
		forwards.put("view","redirect:/do/onlineloans/view/");
		forwards.put("error","/onlineloans/review.jsp");
		forwards.put("lockError","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("toLoanAndWithdrawal","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanList","redirect:/do/withdrawal/loanAndWithdrawalRequests/");
		forwards.put("loanConfirmation","redirect:/do/onlineloans/confirmation/");
	}
	public static UserRole getUserRoleWithPermissions(UserProfile userProfile,
			Integer contractId) throws SystemException {

		UserRole permissionFilledUserRole = userProfile.getRole();

		// Populate contract specific permissions (eg: TPAs and BundledGA Reps have contract level permissions)		
		if (permissionFilledUserRole != null) {
			permissionFilledUserRole = SecurityHelper
					.getUserRoleForContract(userProfile.getPrincipal(),
							permissionFilledUserRole, contractId);
		}

		if (permissionFilledUserRole.getContractServiceFeatureMap() == null) {
			try {
				permissionFilledUserRole
						.setContractServiceFeatureMap(ContractServiceDelegate
								.getInstance().getContractServiceFeatures(
										contractId));
			} catch (ApplicationException e) {
				throw new SystemException(e,
						"failed to getContractServiceFeatures");
			}
		}

		return permissionFilledUserRole;
	}

	public static String getActionForwardIfLoanNotAccessible(
			final String currentForward, 
			final LoanForm form, final HttpServletRequest request,
			final UserProfile userProfile,
			final UserRole userRoleWithPermissions,
			final LoanSettings loanSettings, final Loan loan)
			throws SystemException {

		String statusCode = loan.getStatus();

		Set<Integer> validContracts = new HashSet<Integer>();

		final Contract currentContract = userProfile.getCurrentContract();
		if (currentContract != null) {
			validContracts.add(currentContract.getContractNumber());
		} else if (userProfile.getRole().isTPA()) {
			if (currentContract != null) {
				validContracts.add(currentContract.getContractNumber());
			} else {
				UserInfo userInfo = SecurityServiceDelegate.getInstance()
						.getUserInfo(userProfile.getPrincipal());
				for (TPAFirmInfo firmInfo : userInfo.getTpaFirmsAsCollection()) {
					validContracts.addAll(TPAServiceDelegate.getInstance()
							.getContractsByFirm(firmInfo.getId()));
				}
			}
		}

		Integer contractId = loan.getContractId();

		/*
		 * The contract IDs in the given loan is not valid for this user, we
		 * should forward them back to the Withdrawal request list page.
		 */
		if (!validContracts.contains(contractId)) {
			//return AbstractLoanAction.ACTION_FORWARD_LIST);
			return AbstractLoanController.ACTION_FORWARD_LIST;
		}

		/*
		 * Invalid status
		 */
		if (statusCode.equals(LoanStateEnum.DELETED.getStatusCode())) {
			setErrorInSession(request, LoanContentConstants.LOAN_DELETED);
			//return AbstractLoanAction.ACTION_FORWARD_LIST);
			return AbstractLoanController.ACTION_FORWARD_LIST;
		}

		if (statusCode.equals(LoanStateEnum.EXPIRED.getStatusCode())) {
			setErrorInSession(request, LoanContentConstants.LOAN_EXPIRED);
			//return AbstractLoanAction.ACTION_FORWARD_LIST);
			return AbstractLoanController.ACTION_FORWARD_LIST;
		}
//TODO
		//ActionRedirect redirect = null;
		ControllerRedirect redirect=null;
		if (currentForward.equals(AbstractLoanController.ACTION_FORWARD_VIEW) 
		        || currentForward.equals(AbstractLoanController.ACTION_FORWARD_VIEW_MANAGED_CONTENT)) {
			/*
			 * If we're on the view page, we just check if we have the
			 * permission to view loans. If we don't have View All Loans
			 * permission, we redirect to the list page.
			 */
			boolean hasViewAllLoans = userRoleWithPermissions
					.hasPermission(PermissionType.VIEW_ALL_LOANS);
			if (!hasViewAllLoans) {
				return forwards.get(AbstractLoanController.ACTION_FORWARD_LIST);
				//return AbstractLoanAction.ACTION_FORWARD_LIST;
			}

			if (statusCode.equals(LoanStateEnum.DRAFT.getStatusCode())) {
				if (loan.getCreatedId().intValue() != userProfile
						.getPrincipal().getProfileId()) {

					return forwards.get(AbstractLoanController.ACTION_FORWARD_LIST);
					//return AbstractLoanAction.ACTION_FORWARD_LIST;
				}
			}
			/*
			 * Done with View check, we can return.
			 */
			return null;
		}

		/*
		 * Confirmation page is only applicable if the form is set to be showing
		 * confirmation page.
		 */
		if (currentForward
				.equals(AbstractLoanController.ACTION_FORWARD_CONFIRMATION)) {
			if (form.isShowConfirmation()) {
				return null;
			}
		}

		/*
		 * Now, we need to check the edit mode of a loan request.
		 */
		if (statusCode.equals(LoanStateEnum.PENDING_APPROVAL.getStatusCode())) {
			boolean hasSigningAuthority = userRoleWithPermissions
					.hasPermission(PermissionType.SIGNING_AUTHORITY);

			if (!hasSigningAuthority) {
				/*
				 * Must have signing authority to edit a pending approval loan
				 * request.
				 */
				//TODO
				redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_VIEW));
				//redirect=AbstractLoanAction.ACTION_FORWARD_VIEW;
			} else {
				/*
				 * If segregation of duty and user is the initiator, he cannot approve.
				 */
				if (!loanSettings.isInitiatorCanApproveLoan()
						&& loan.getCreatedId().intValue() == userProfile.getPrincipal()
								.getProfileId()) {
					//TODO
					redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_VIEW));
					//redirect=AbstractLoanAction.ACTION_FORWARD_VIEW;
				}

				/*
				 * If we're not using the pending approval page, we should
				 * redirect to the pending approval page.
				 */
				if (!currentForward
						.equals(AbstractLoanController.ACTION_FORWARD_PENDING_APPROVAL)) {
					//TODO
					redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_PENDING_APPROVAL));
					//redirect=AbstractLoanAction.ACTION_FORWARD_PENDING_APPROVAL;
				}
			}
		} else if (statusCode.equals(LoanStateEnum.PENDING_REVIEW
				.getStatusCode())) {
			boolean hasReviewLoan = userRoleWithPermissions
					.hasPermission(PermissionType.REVIEW_LOANS);
			if (!hasReviewLoan) {
				/*
				 * Must have review loans permission to edit a pending review
				 * loan request.
				 */
				//TODO
				redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_VIEW));
				//redirect=AbstractLoanAction.ACTION_FORWARD_VIEW;
			} else {
				/*
				 * If we're not using the pending review page, we should
				 * redirect to the pending review page.
				 */
				if (!currentForward
						.equals(AbstractLoanController.ACTION_FORWARD_PENDING_REVIEW) ){
					//TODO
					redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_PENDING_REVIEW));
				//redirect=AbstractLoanAction.ACTION_FORWARD_PENDING_REVIEW;
				}
			}
		} else if (statusCode.equals(LoanStateEnum.DRAFT.getStatusCode())) {
			boolean hasInitiateLoan = userRoleWithPermissions
					.hasPermission(PermissionType.INITIATE_LOANS);
			if (!hasInitiateLoan) {
				/*
				 * Must have initiate loans permission to edit/initiate a loan
				 * request.
				 */
				//TODO
				redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_VIEW));
				//redirect=AbstractLoanAction.ACTION_FORWARD_VIEW;
			} else {
				if (loan.getSubmissionId() != null) {
					/*
					 * If the submission ID is not null, we're editing a draft
					 * loan request. 
					 * 
					 * First, make sure the login user is the initiator, if not,
					 * we need to forward back to the list page.
					 */
					if (loan.getCreatedId().intValue() != userProfile
							.getPrincipal().getProfileId()) {
						//TODO
						//return new ActionRedirect(AbstractLoanAction.ACTION_FORWARD_LIST));
						return AbstractLoanController.ACTION_FORWARD_LIST;
					}

					/*
					 * Next, check if the current forward is the draft forward.
					 */
					if (!currentForward
							.equals(AbstractLoanController.ACTION_FORWARD_DRAFT)) {
						//TODO
						redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_DRAFT));
						//redirect=AbstractLoanAction.ACTION_FORWARD_DRAFT;
					}
				} else {
					/*
					 * If the submission ID is null, we're initiating a new loan
					 * request, make sure the current page action is for new
					 * loan request.
					 */
					if (!currentForward
							.equals(AbstractLoanController.ACTION_FORWARD_INITIATE)) {
						//TODO
						redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_INITIATE));
						//redirect=AbstractLoanAction.ACTION_FORWARD_INITIATE;
						//TODO
					if (loan.getParticipantProfileId() != null) {
							redirect
									.addParameter(
											AbstractLoanController.PARAM_PARTICIPANT_PROFILE_ID,
											loan.getParticipantProfileId());
						}
					}
				}
			}
		} else {
			/*
			 * For all other loan statuses, there is no edit page, so redirect
			 * to view page.
			 */
			if (!currentForward.equals(AbstractLoanController.ACTION_FORWARD_VIEW)) {
				//TODO
				redirect = new ControllerRedirect(forwards.get(AbstractLoanController.ACTION_FORWARD_VIEW));
				//redirect=AbstractLoanAction.ACTION_FORWARD_VIEW;
			}
		}

		if (redirect != null) {
			//TODO
			if (loan.getSubmissionId() != null) {
				redirect.addParameter(AbstractLoanController.PARAM_SUBMISSION_ID,
						loan.getSubmissionId());
			}
			if (loan.getContractId() != null) {
				redirect.addParameter(AbstractLoanController.PARAM_CONTRACT_ID,
						loan.getContractId());
			}
			return redirect.getPath();
		}
//TODO should be forward name
		return null;
	}

	private static void setErrorInSession(HttpServletRequest request,
			int errorCode) {
		Collection<GenericException> sessionErrors = SessionHelper
				.getErrorsInSession(request);
		if (sessionErrors == null) {
			sessionErrors = new HashSet<GenericException>();
		}
		GenericException error = new GenericException(errorCode);
		sessionErrors.add((GenericException) error);
		SessionHelper.setErrorsInSession(request, sessionErrors);

	}
}
