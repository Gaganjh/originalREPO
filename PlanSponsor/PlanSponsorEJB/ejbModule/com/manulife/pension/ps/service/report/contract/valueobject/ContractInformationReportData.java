package com.manulife.pension.ps.service.report.contract.valueobject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.contract.reporthandler.ContractInformationReportHandler;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractSnapshotVO;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.contract.valueobject.MoneySourceVO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This is the report data for Contact Information page
 * 
 * @author Siby Thomas
 * 
 */
public class ContractInformationReportData extends ReportData {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(ContractInformationReportData.class);
    
    public static final String REPORT_ID = ContractInformationReportHandler.class.getName();
    public static final String CSV_REPORT_NAME = "ContractInformation";
    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
    public static final String FILTER_AS_OF_DATE = "asOfDate";
    public static final String FILTER_FIRM_IDS = "firmIds";
    public static final String FILTER_CONTRACT_EFFECTIVE_DATE = "contractEffectiveDate";
    
    private ContractSummaryVO contractSummaryVo = null;
    private ContractSnapshotVO contractSnapshotVo = null;
    private ContractProfileVO contractProfileVo = null;

    private boolean isPermittedDisparity;
    private boolean isVestingShownOnStatements;
    
    private boolean hasLoanAssets;
    
    private ContactVO planSponsorContact;
    private String tpaFirmName;
    private String tpaContactName;
    private String[] tpaContactsName;

    private List<MoneyTypeVO> moneyTypes;
    private List<MoneySourceVO> moneySources;
    private List<ContactVO> tpaContactDetails;
    private List<ContactVO> planSponsorContactDetails;

	private List<ContactVO> tpaPrimaryContactDetails;
    private List<ContactVO> planSponsorPrimaryContactDetails;
    
    private BigDecimal blendedAssetCharge;
    private Date assetChargeAsOfDate;
    
    private int participantCount;
    private BigDecimal pendingTransactionAmount;
    
    private boolean hasLifeCycleFunds;
    
    private String managedAccountServiceFeature;


	/**
     * gets the contract summary VO
     * 
     * @return contractSummaryVo ContractSummaryVO
     */
    public ContractSummaryVO getContractSummaryVo() {
        return contractSummaryVo;
    }

    /**
     * sets the contract summary VO
     * 
     * @param contractSummaryVo ContractSummaryVO
     */
    public void setContractSummaryVo(ContractSummaryVO contractSummaryVo) {
        this.contractSummaryVo = contractSummaryVo;
    }

    /**
     * gets the contract snapshot VO
     * 
     * @return contractSnapshotVo ContractSnapshotVO
     */
    public ContractSnapshotVO getContractSnapshotVo() {
        return contractSnapshotVo;
    }

    /**
     * sets the contract snapshot VO
     * 
     * @param contractSnapshotVo ContractSnapshotVO
     */
    public void setContractSnapshotVo(ContractSnapshotVO contractSnapshotVo) {
        this.contractSnapshotVo = contractSnapshotVo;
    }

    /**
     * gets the contract profile VO
     * 
     * @return contractProfileVo ContractProfileVO
     */
    public ContractProfileVO getContractProfileVo() {
        return contractProfileVo;
    }

    /**
     * sets the contract profile VO
     * 
     * @param contractProfileVo ContractProfileVO
     */
    public void setContractProfileVo(ContractProfileVO contractProfileVo) {
        this.contractProfileVo = contractProfileVo;
    }

    /**
     * gets the money types
     * 
     * @return moneyTypes List<MoneyTypeVO>
     */
    public List<MoneyTypeVO> getMoneyTypes() {
        return moneyTypes;
    }

    /**
     * sets the money type
     * 
     * @param moneyTypes List<MoneyTypeVO>
     */
    public void setMoneyTypes(List<MoneyTypeVO> moneyTypes) {
        this.moneyTypes = moneyTypes;
    }

    /**
     * gets the money source
     * 
     * @return moneySources List<MoneySourceVO>
     */
    public List<MoneySourceVO> getMoneySources() {
        return moneySources;
    }

    /**
     * sets the money source
     * 
     * @param moneySources List<MoneySourceVO>
     */
    public void setMoneySources(List<MoneySourceVO> moneySources) {
        this.moneySources = moneySources;
    }
    /**
     * gets the TPA Contact details
     * 
     * @return tpaContactDetails List<ContactVO>
     */
    public List<ContactVO> getTpaContactDetails() {
        return tpaContactDetails;
    }

    /**
     * sets the TPA Contact details
     * 
     * @param tpaContactDetails List<ContactVO>
     */
    public void setTpaContactDetails(List<ContactVO> tpaContactDetails) {
        this.tpaContactDetails = tpaContactDetails;
    }
    /**
     * gets the PS Contact details
     * 
     * @return planSponsorContactDetails List<ContactVO>
     */
    public List<ContactVO> getplanSponsorContactDetails() {
        return planSponsorContactDetails;
    }

    /**
     * sets the PS Contact details
     * 
     * @param planSponsorContactDetails List<ContactVO>
     */
    public void setplanSponsorContactDetails(List<ContactVO> planSponsorContactDetails) {
        this.planSponsorContactDetails = planSponsorContactDetails;
    }
    
    public List<ContactVO> getTpaPrimaryContactDetails() {
		return tpaPrimaryContactDetails;
	}

	public void setTpaPrimaryContactDetails(List<ContactVO> tpaPrimaryContactDetails) {
		this.tpaPrimaryContactDetails = tpaPrimaryContactDetails;
	}

	public List<ContactVO> getPlanSponsorPrimaryContactDetails() {
		return planSponsorPrimaryContactDetails;
	}

	public void setPlanSponsorPrimaryContactDetails(
			List<ContactVO> planSponsorPrimaryContactDetails) {
		this.planSponsorPrimaryContactDetails = planSponsorPrimaryContactDetails;
	}

    /**
     * gets the is permitted disparity indicator
     * 
     * @return isPermittedDisparity String
     */
    public boolean getIsPermittedDisparity() {
        return isPermittedDisparity;
    }

    /**
     * sets the is permitted disparity indicator
     * 
     * @param isPermittedDisparity String
     */
    public void setIsPermittedDisparity(boolean isPermittedDisparity) {
        this.isPermittedDisparity = isPermittedDisparity;
    }

    /**
     * gets the is vesting shown on statement indicator
     * 
     * @return isVestingShownOnStatements boolean
     */
    public boolean getIsVestingShownOnStatements() {
        return isVestingShownOnStatements;
    }

    /**
     * sets the is vesting shown on statement indicator
     * 
     * @param isVestingShownOnStatements boolean
     */
    public void setIsVestingShownOnStatements(boolean isVestingShownOnStatements) {
        this.isVestingShownOnStatements = isVestingShownOnStatements;
    }

    /**
     * gets the has loan assets indicator
     * 
     * @return hasLoanAssets boolean
     */
    public boolean getHasLoanAssets() {
        return hasLoanAssets;
    }

    /**
     * sets the has loan assets indicator
     * 
     * @param hasLoanAssets boolean
     */
    public void setHasLoanAssets(boolean hasLoanAssets) {
        this.hasLoanAssets = hasLoanAssets;
    }

    /**
     * gets the tpa firm name
     * 
     * @return tpaFirmName String
     */
    public String getTpaFirmName() {
        return tpaFirmName;
    }

    /**
     * sets the tpa firm name
     * 
     * @param brokerShareInformations Collection<ContractBrokerShareInformationVO>
     */
    public void setTpaFirmName(String tpaFirmName) {
        this.tpaFirmName = tpaFirmName;
    }

    /**
     * gets the tpa contact names
     * 
     * @return tpacontactsName
     */
    public String[] getTpaContactsName() {
        return tpaContactsName;
    }

    /**
     * sets the tpa contact names
     * 
     * @param tpaContactsName String
     */
    public void setTpaContactsName(String[] tpaContactsName) {
        this.tpaContactsName = tpaContactsName;
    }

    /**
     * gets the tpa contact name
     * 
     * @return brokerShareInformations Collection<ContractBrokerShareInformationVO>
     */
    public String getTpaContactName() {
        return tpaContactName;
    }

    /**
     * sets the tpa contact name
     * 
     * @param tpaContactName String
     */
    public void setTpaContactName(String tpaContactName) {
        this.tpaContactName = tpaContactName;
    }

    /**
     * gets the plan sponsor contact vo
     * 
     * @return tpaContact ContactVO
     */
    public ContactVO getPlanSponsorContact() {
        return planSponsorContact;
    }

    /**
     * sets the plan sponsor contact vo
     * @param tpaContact ContactVO
     */
    public void setPlanSponsorContact(ContactVO planSponsorContact) {
        this.planSponsorContact = planSponsorContact;
    }
    
    /**
     * gets the Plan Sponsor phone number
     * 
     * @return String
     */
    @SuppressWarnings("finally")
    public String getPlanSponsorPhoneNumber() {
        String phoneNumber = StringUtils.trimToEmpty(planSponsorContact.getPhone());
        StringBuilder formattedNumber=new StringBuilder();
        try {
            if (phoneNumber.length() > 9) {
                  formattedNumber.append(phoneNumber.substring(0,3))
                                 .append(Constants.PHONE_NUMBER_SEPERATOR)
                                 .append(phoneNumber.substring(3,6))
                                 .append(Constants.PHONE_NUMBER_SEPERATOR)
                                 .append(phoneNumber.substring(6));
            }
        } catch ( Exception e) {
            logger.error("Exception while formatting the Plan Sponsor phone number");
        } finally {
            return formattedNumber.toString();
        }
    }

    /**
     * gets the blended asset charge
     * 
     * @return blendedAssetCharge String
     */
    public BigDecimal getBlendedAssetCharge() {
        return blendedAssetCharge;
    }

    /**
     * sets the blended asset charge
     * 
     * @param blendedAssetCharge String
     */
    public void setBlendedAssetCharge(BigDecimal blendedAssetCharge) {
        this.blendedAssetCharge = blendedAssetCharge;
    }

    /**
     * gets the asset charge as of date
     * 
     * @return assetChargeAsOfDate String
     */
    public Date getAssetChargeAsOfDate() {
        return assetChargeAsOfDate;
    }

    /**
     * sets the asset charge as of date
     * 
     * @param assetChargeAsOfDate String
     */
    public void setAssetChargeAsOfDate(Date assetChargeAsOfDate) {
        this.assetChargeAsOfDate = assetChargeAsOfDate;
    }
    
    /**
     * gets the participant count
     * 
     * @return participantCount int
     */
    public int getParticipantCount() {
        return participantCount;
    }

    /**
     * sets the participant count
     * 
     * @param participantCount int
     */
    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }
    
    /**
     * gets the pending transaction amount
     * 
     * @return pendingTransactionAmount BigDecimal
     */
    public BigDecimal getPendingTransactionAmount() {
        return pendingTransactionAmount;
    }

    /**
     * sets the pending transaction amount
     * 
     * @param pendingTransactionAmount BigDecimal
     */
    public void setPendingTransactionAmount(BigDecimal pendingTransactionAmount) {
        this.pendingTransactionAmount = pendingTransactionAmount;
    }

    /**
     * gets the hasLifeCycleFunds indicator
     * 
     * @return hasLifeCycleFunds boolean
     */
    public boolean getHasLifeCycleFunds() {
        return hasLifeCycleFunds;
    }

    /**
     * sets the hasLifeCycleFunds indicator
     * 
     * @param hasLifeCycleFunds boolean
     */
    public void setHasLifeCycleFunds(boolean hasLifeCycleFunds) {
        this.hasLifeCycleFunds = hasLifeCycleFunds;
    }
    
    public String getManagedAccountServiceFeature() {
		return managedAccountServiceFeature;
	}

	public void setManagedAccountServiceFeature(String managedAccountServiceFeature) {
		this.managedAccountServiceFeature = managedAccountServiceFeature;
	}
}
