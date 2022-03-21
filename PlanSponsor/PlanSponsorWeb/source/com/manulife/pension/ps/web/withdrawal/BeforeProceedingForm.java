package com.manulife.pension.ps.web.withdrawal;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.controller.PsAutoActionLabelForm;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;

/**
 * Defines the action form for the before proceeding page.
 * 
 * @author dickand
 */
public class BeforeProceedingForm extends PsAutoActionLabelForm {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private ContractInfo contractInfo;

    private ParticipantInfo participantInfo;

    private WithdrawalRequestMetaData withdrawalRequestMetaDataForPrint;

    private String skipIndicator;
    
    private boolean initiatedByParticipant;

    public boolean isInitiatedByParticipant() {
        return initiatedByParticipant;
    }

    public void setInitiatedByParticipant(boolean initiatedByParticipant) {
        this.initiatedByParticipant = initiatedByParticipant;
    }

    /**
     * @return the skipIndicator
     */
    public String getSkipIndicator() {
        return skipIndicator;
    }

    /**
     * @param skipIndicator the skipIndicator to set
     */
    public void setSkipIndicator(final String skipIndicator) {
        this.skipIndicator = skipIndicator;
    }

    /**
     * @return the contractInfo
     */
    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    /**
     * @param contractInfo the contractInfo to set
     */
    public void setContractInfo(final ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }

    /**
     * @return the participantInfo
     */
    public ParticipantInfo getParticipantInfo() {
        return participantInfo;
    }

    /**
     * @param participantInfo the participantInfo to set
     */
    public void setParticipantInfo(ParticipantInfo participantInfo) {
        this.participantInfo = participantInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset( final HttpServletRequest httpServletRequest) {

        // Reset the checkbox
        skipIndicator = null;

        super.reset( httpServletRequest);
    }

    /**
     * @return the withdrawalRequestMetaDataForPrint
     */
    public WithdrawalRequestMetaData getWithdrawalRequestMetaDataForPrint() {
        return withdrawalRequestMetaDataForPrint;
    }

    /**
     * @param withdrawalRequestMetaDataForPrint the withdrawalRequestMetaDataForPrint to set
     */
    public void setWithdrawalRequestMetaDataForPrint(
            WithdrawalRequestMetaData withdrawalRequestMetaDataForPrint) {
        this.withdrawalRequestMetaDataForPrint = withdrawalRequestMetaDataForPrint;
    }
}
