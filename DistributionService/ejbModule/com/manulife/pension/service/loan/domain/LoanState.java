package com.manulife.pension.service.loan.domain;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.loan.valueobject.Loan;

public interface LoanState {

	public void populate(Loan loan) throws DistributionServiceException;

	public void validate(LoanStateEnum fromState, LoanStateEnum toState, LoanStateContext context)
			throws DistributionServiceException;

	public Loan saveAndExit(Loan loan) throws DistributionServiceException;

    public Loan printLoanDocument(Loan loan) throws DistributionServiceException; 
    
    public Loan printLoanDocumentReview(Loan loan) throws DistributionServiceException; 
	
	public Loan sendForReview(Loan loan) throws DistributionServiceException;

	public Loan sendForAcceptance(Loan loan)
			throws DistributionServiceException;

	public Loan decline(Loan loan) throws DistributionServiceException;

	public Loan sendForApproval(Loan loan) throws DistributionServiceException;

	public Loan approve(Loan loan) throws DistributionServiceException;

	public Loan expire(Loan loan) throws DistributionServiceException;

	public Loan complete(Loan loan) throws DistributionServiceException;

	public Loan reject(Loan loan) throws DistributionServiceException;

	public Loan delete(Loan loan) throws DistributionServiceException;

	public Loan initiate(Integer participantProfileId, Integer contractId,
			Integer userProfileId) throws DistributionServiceException;

	public Loan loanPackage(Loan loan) throws DistributionServiceException;

}
