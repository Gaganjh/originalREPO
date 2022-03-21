package com.manulife.pension.bd.web.bob.transaction;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsACIReportData;

/**
 * This form bean class will store the transaction hisotry ezincrease page values
 * 
 * @author aambrose
 * 
 */
public class EziReportForm extends BaseReportForm {

    private static final long serialVersionUID = 1150324724505426322L;

    private TransactionDetailsACIReportData report;

    private String profileId;

    private String transactionDate;

    private String contractNumber;

    public EziReportForm() {
    }

    /**
     * @return TransactionDetailsACIReportData report
     */
    public TransactionDetailsACIReportData getReport() {
        return report;
    }

    /**
     * @param TransactionDetailsACIReportData report
     */
    public void setReport(TransactionDetailsACIReportData report) {
        this.report = report;
    }

    /**
     * @return String profileId
     */
    public String getProfileId() {
        return profileId;
    }

    /**
     * @param String profileId
     */
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /**
     * @return String transactionDate
     */
    public String getTransactionDate() {
        return transactionDate;
    }

    /**
     * format we have is yyyy-mm-dd need mm/dd/yyyy (string)
     * 
     * @return String transactionDate
     */
    public String getTransactionDateFormatted() {
        return transactionDate.substring(5, 7) + BDConstants.SLASH_SYMBOL
                + transactionDate.substring(8) + BDConstants.SLASH_SYMBOL
                + transactionDate.substring(0, 4);
    }

    /**
     * @param String transactionDate
     */
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return String contractNumber
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * @param String contractNumber
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

}
