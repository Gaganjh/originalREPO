package com.manulife.pension.service.loan.domain;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.util.BusinessCalendar;

public class LoanStateContext extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	private boolean printLoanDocument;
	
    private boolean saveAndExit;

	private Loan loan;

	private LoanDataHelper loanDataHelper;

	private BusinessCalendar businessCalendar;
	
	public LoanStateContext(Loan loan) {
		this.loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		this.loan = loan;
	}

	public boolean isPrintLoanDocument() {
        return printLoanDocument;
    }

    public void setPrintLoanDocument(boolean printLoanDocument) {
        this.printLoanDocument = printLoanDocument;
    }

    public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public BusinessCalendar getBusinessCalendar() {
		if (businessCalendar == null) {
			businessCalendar = loanDataHelper.getBusinessCalendar();
		}
		return businessCalendar;
	}
	
	/**
	 * @deprecated Kept for compatibility reason.
	 */
	public LoanParticipantData getLoanParticipantData() {
		return loan.getLoanParticipantData();
	}

	/**
	 * @deprecated Kept for compatibility reason.
	 */
	public LoanPlanData getLoanPlanData() {
		return loan.getLoanPlanData();
	}

	/**
	 * @deprecated Kept for compatibility reason.
	 */
	public LoanSettings getLoanSettings() {
		return loan.getLoanSettings();
	}

    public boolean isSaveAndExit() {
        return saveAndExit;
    }

    public void setSaveAndExit(boolean saveAndExit) {
        this.saveAndExit = saveAndExit;
    }

}
