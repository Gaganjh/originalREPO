package com.manulife.pension.service.loan.domain;

import org.junit.Assert;
import org.junit.Test;

public class TestLoanStateEnum {

	@Test
	public void testIsDraftBefore() throws Exception {
		Assert.assertFalse(LoanStateEnum.DRAFT.isBefore(LoanStateEnum.DRAFT));
		Assert.assertTrue(LoanStateEnum.DRAFT
				.isBefore(LoanStateEnum.PENDING_REVIEW));
		Assert.assertTrue(LoanStateEnum.DRAFT
				.isBefore(LoanStateEnum.PENDING_ACCEPTANCE));
		Assert.assertTrue(LoanStateEnum.DRAFT
				.isBefore(LoanStateEnum.PENDING_APPROVAL));
		Assert.assertTrue(LoanStateEnum.DRAFT.isBefore(LoanStateEnum.APPROVED));
		Assert.assertTrue(LoanStateEnum.DRAFT.isBefore(LoanStateEnum.DECLINED));
		Assert.assertTrue(LoanStateEnum.DRAFT.isBefore(LoanStateEnum.REJECTED));
		Assert
				.assertTrue(LoanStateEnum.DRAFT
						.isBefore(LoanStateEnum.CANCELLED));
		Assert
				.assertTrue(LoanStateEnum.DRAFT
						.isBefore(LoanStateEnum.COMPLETED));
		Assert.assertTrue(LoanStateEnum.DRAFT.isBefore(LoanStateEnum.DELETED));
		Assert.assertTrue(LoanStateEnum.DRAFT.isBefore(LoanStateEnum.EXPIRED));
		Assert.assertTrue(LoanStateEnum.DRAFT
				.isBefore(LoanStateEnum.LOAN_PACKAGE));
	}

	@Test
	public void testIsPendingReviewBefore() throws Exception {
		Assert.assertFalse(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.PENDING_REVIEW));
		Assert.assertFalse(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.DRAFT));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.PENDING_ACCEPTANCE));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.PENDING_APPROVAL));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.APPROVED));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.DECLINED));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.REJECTED));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.CANCELLED));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.COMPLETED));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.DELETED));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.EXPIRED));
		Assert.assertTrue(LoanStateEnum.PENDING_REVIEW
				.isBefore(LoanStateEnum.LOAN_PACKAGE));
	}

	@Test
	public void testIsPendingAcceptanceBefore() throws Exception {
		Assert.assertFalse(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.DRAFT));
		Assert.assertFalse(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.PENDING_REVIEW));
		Assert.assertFalse(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.PENDING_ACCEPTANCE));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.PENDING_APPROVAL));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.APPROVED));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.DECLINED));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.REJECTED));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.CANCELLED));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.COMPLETED));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.DELETED));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.EXPIRED));
		Assert.assertTrue(LoanStateEnum.PENDING_ACCEPTANCE
				.isBefore(LoanStateEnum.LOAN_PACKAGE));
	}

	@Test
	public void testIsPendingApprovalBefore() throws Exception {
		Assert.assertFalse(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.DRAFT));
		Assert.assertFalse(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.PENDING_REVIEW));
		Assert.assertFalse(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.PENDING_ACCEPTANCE));
		Assert.assertFalse(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.PENDING_APPROVAL));
		Assert.assertTrue(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.APPROVED));
		Assert.assertTrue(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.DECLINED));
		Assert.assertTrue(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.REJECTED));
		Assert.assertTrue(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.CANCELLED));
		Assert.assertTrue(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.COMPLETED));
		Assert.assertTrue(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.DELETED));
		Assert.assertTrue(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.EXPIRED));
		Assert.assertTrue(LoanStateEnum.PENDING_APPROVAL
				.isBefore(LoanStateEnum.LOAN_PACKAGE));
	}

	@Test
	public void testIsApprovedBefore() throws Exception {
		Assert
				.assertFalse(LoanStateEnum.APPROVED
						.isBefore(LoanStateEnum.DRAFT));
		Assert.assertFalse(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.PENDING_REVIEW));
		Assert.assertFalse(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.PENDING_ACCEPTANCE));
		Assert.assertFalse(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.PENDING_APPROVAL));
		Assert.assertFalse(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.APPROVED));
		Assert.assertTrue(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.DECLINED));
		Assert.assertTrue(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.REJECTED));
		Assert.assertTrue(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.CANCELLED));
		Assert.assertTrue(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.COMPLETED));
		Assert.assertTrue(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.DELETED));
		Assert.assertTrue(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.EXPIRED));
		Assert.assertTrue(LoanStateEnum.APPROVED
				.isBefore(LoanStateEnum.LOAN_PACKAGE));
	}
}
