package com.manulife.pension.service.loan.valueobject.document;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class LoanDocumentBundle extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	private TruthInLendingNotice truthInLendingNotice;

	private PromissoryNote promissoryNote;

	private AmortizationSchedule amortizationSchedule;

	private Instructions instructionsForParticipant;

	private Instructions instructionsForAdministrator;

	private LoanForm loanForm;

	public TruthInLendingNotice getTruthInLendingNotice() {
		return truthInLendingNotice;
	}

	public void setTruthInLendingNotice(
			TruthInLendingNotice truthInLendingNotice) {
		this.truthInLendingNotice = truthInLendingNotice;
	}

	public PromissoryNote getPromissoryNote() {
		return promissoryNote;
	}

	public void setPromissoryNote(PromissoryNote promissoryNote) {
		this.promissoryNote = promissoryNote;
	}

	public AmortizationSchedule getAmortizationSchedule() {
		return amortizationSchedule;
	}

	public void setAmortizationSchedule(
			AmortizationSchedule amortizationSchedule) {
		this.amortizationSchedule = amortizationSchedule;
	}

	public Instructions getInstructionsForParticipant() {
		return instructionsForParticipant;
	}

	public void setInstructionsForParticipant(
			Instructions instructionsForParticipant) {
		this.instructionsForParticipant = instructionsForParticipant;
	}

	public Instructions getInstructionsForAdministrator() {
		return instructionsForAdministrator;
	}

	public void setInstructionsForAdministrator(
			Instructions instructionsForAdministrator) {
		this.instructionsForAdministrator = instructionsForAdministrator;
	}

	public LoanForm getLoanForm() {
		return loanForm;
	}

	public void setLoanForm(LoanForm loanForm) {
		this.loanForm = loanForm;
	}
}
