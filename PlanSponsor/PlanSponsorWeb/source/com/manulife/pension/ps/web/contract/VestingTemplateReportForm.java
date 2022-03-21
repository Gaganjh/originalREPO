package com.manulife.pension.ps.web.contract;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * Report action form class for the Vesting Template report 
 */
public class VestingTemplateReportForm extends ReportForm {
    private String contractSortOptionCode;

    /**
     * Default Constructor
     */
    public VestingTemplateReportForm() {
    }

    /**
     * Clears the report form. Since report form is stored in the session and is reused by many
     * other actions, we must clear the parameters when we switch reports.
     */
    public void clear() {
        super.clear();
        contractSortOptionCode = null;
    }

    /**
     * @return the contractSortOptionCode
     */
    public String getContractSortOptionCode() {
        return contractSortOptionCode;
    }

    /**
     * @param contractSortOptionCode the contractSortOptionCode to set
     */
    public void setContractSortOptionCode(String contractSortOptionCode) {
        this.contractSortOptionCode = contractSortOptionCode;
    }
}
