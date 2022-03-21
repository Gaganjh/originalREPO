package com.manulife.pension.service.loan.util;

public class LoanObjectFactory {

    private LoanDataHelper loanDataHelper;

    private static LoanObjectFactory instance = new LoanObjectFactory();

    public static LoanObjectFactory getInstance() {
        return instance;
    }

    public LoanDataHelper getLoanDataHelper() {
        if (loanDataHelper == null) {
            loanDataHelper = new LoanDataHelper();
        }
        return loanDataHelper;
    }

    public void setLoanDataHelper(LoanDataHelper loanDataHelper) {
        this.loanDataHelper = loanDataHelper;
    }

}
