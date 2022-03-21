package com.manulife.pension.ps.service.report.bob.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.contract.util.SmallPlanFeature;

/**
 * This class holds VO information to be shown as a row in the Main report of BOB.
 * 
 * @author harlomte
 * 
 */
public class BlockOfBusinessReportVO implements Serializable{

    private static final long serialVersionUID = 385934334577626810L;

    private String contractName;

    private int contractNumber;
    
    private String contractStatusCode;

    private String proposalName;

    private int proposalNumber;

    private Date contractEffectiveDate;

    private Date proposalDate;

    private Date contractPlanYearEnd;

    private String contractState;

    private Integer numOfLives;

    private BigDecimal totalAssets;

    private BigDecimal transferredAssetsPriorToCharges;

    private BigDecimal assetCharge;

    private BigDecimal expectedFirstYearAssets;

    private BigDecimal commissionsDepositTR;

    private BigDecimal commissionsDepositReg;

    private BigDecimal commissionsAssetAB;
    
    private BigDecimal commissionsPriceCredit;
    
    private String producerCodes;
    
    private String rvpName;

    private String fundClass;

    private String productType;

    private String usOrNy; // manulifeCompany.

    private String manulifeCompanyID;

    private Date discontinuanceDate;
    
    private String financialRepNameAndFirmName;
    
    private String commissionAsset1Year;
    
    private String commissionAssetAllYrs;
    
    private String commissionDepositReg1Yr;
    
    private String commissionDepositRegRen;
    
    private String commissionDepositTr1yr;
    
    private String commissionDepositTrRen;
    
    private String commissionAssetRen;
    
    private String riaFlatFeeProrata;

	private String riaFlatFeePerHead;

	private String riaBps;

	private String riaBpsMax;

	private String riaAcBlend;

	private String riaAcTiered;

	private String des338Ind;
	
    private String frContractShareList;
    
    private String trusteeName;
    
    private String productId;
    
    private boolean pinPointContract;
    
    private String riaFirmName;
    
    private BigDecimal riaFeePaidByJH;
    
	private BigDecimal riaFeePaidByPlan;
    
    private BigDecimal riaTotalFee;
    
    private static List<String>  preSignatureProductIds = Arrays.asList(new String[]{"ARA85", "10K", "AIP"});

    private BigDecimal riaBpsAnnualMaxAmount;
    
    private BigDecimal riaBpsMin;
    
    private BigDecimal riaBpsMonthlyMinAmount;
    
	private String cofidFeeFeatureCode;
    
	private BigDecimal cofid321ABFee;
    
	private BigDecimal cofid321DBFee;
    
    private BigDecimal cofid338ABFee;
    
    private BigDecimal cofid338DBFee;
    
    private BigDecimal cofidBPSFeeMonthlyMinAmt;
    
    private SmallPlanFeature smallPlanOptionCode;    
    
	public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public int getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractStatusCode() {
        return contractStatusCode;
    }

    public void setContractStatusCode(String contractStatusCode) {
        this.contractStatusCode = contractStatusCode;
    }

    public String getProposalName() {
		return proposalName;
	}

	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}

	public int getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(int proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public Date getContractEffectiveDate() {
        return contractEffectiveDate;
    }

    public void setContractEffectiveDate(Date contractEffectiveDate) {
        this.contractEffectiveDate = contractEffectiveDate;
    }

    public Date getProposalDate() {
        return proposalDate;
    }

    public void setProposalDate(Date proposalDate) {
        this.proposalDate = proposalDate;
    }

    public Date getContractPlanYearEnd() {
        return contractPlanYearEnd;
    }

    public void setContractPlanYearEnd(Date contractPlanYearEnd) {
        this.contractPlanYearEnd = contractPlanYearEnd;
    }

    public String getContractState() {
        return contractState;
    }

    public void setContractState(String contractState) {
        this.contractState = contractState;
    }

    public Integer getNumOfLives() {
        return numOfLives;
    }

    public void setNumOfLives(Integer numOfLives) {
        this.numOfLives = numOfLives;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getTransferredAssetsPriorToCharges() {
        return transferredAssetsPriorToCharges;
    }

    public void setTransferredAssetsPriorToCharges(BigDecimal transferredAssetsPriorToCharges) {
        this.transferredAssetsPriorToCharges = transferredAssetsPriorToCharges;
    }

    public BigDecimal getAssetCharge() {
        return assetCharge;
    }

    public void setAssetCharge(BigDecimal assetCharge) {
        this.assetCharge = assetCharge;
    }

    public BigDecimal getExpectedFirstYearAssets() {
        return expectedFirstYearAssets;
    }

    public void setExpectedFirstYearAssets(BigDecimal expectedFirstYearAssets) {
        this.expectedFirstYearAssets = expectedFirstYearAssets;
    }

    public BigDecimal getCommissionsDepositTR() {
        return commissionsDepositTR;
    }

    public void setCommissionsDepositTR(BigDecimal commissionsDepositTR) {
        this.commissionsDepositTR = commissionsDepositTR;
    }

    public BigDecimal getCommissionsDepositReg() {
        return commissionsDepositReg;
    }

    public void setCommissionsDepositReg(BigDecimal commissionsDepositReg) {
        this.commissionsDepositReg = commissionsDepositReg;
    }

    public BigDecimal getCommissionsAssetAB() {
        return commissionsAssetAB;
    }

    public void setCommissionsAssetAB(BigDecimal commissionsAssetAB) {
        this.commissionsAssetAB = commissionsAssetAB;
    }

    //CL 122165 fix - Report displays 0.000 rather HYPHON
    public String getCommissionsPriceCredit() {
        return (commissionsPriceCredit == null || (commissionsPriceCredit.doubleValue() == 0)) ? "" : commissionsPriceCredit.toString();
    }

    public void setCommissionsPriceCredit(BigDecimal commissionsPriceCredit) {
        this.commissionsPriceCredit = commissionsPriceCredit;
    }

    public String getProducerCodes() {
        return producerCodes;
    }

    public void setProducerCodes(String producerCodes) {
        this.producerCodes = producerCodes;
    }

    public String getFinancialRepNameAndFirmName() {
        return financialRepNameAndFirmName;
    }

    public void setFinancialRepNameAndFirmName(String financialRepNameAndFirmName) {
        this.financialRepNameAndFirmName = financialRepNameAndFirmName;
    }

    public String getRvpName() {
        return rvpName;
    }

    public void setRvpName(String rvpName) {
        this.rvpName = rvpName;
    }

    public String getFundClass() {
        return fundClass;
    }

    public void setFundClass(String fundClass) {
        this.fundClass = fundClass;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getUsOrNy() {
        return usOrNy;
    }

    public void setUsOrNy(String usOrNy) {
        this.usOrNy = usOrNy;
    }

    public String getManulifeCompanyID() {
        return manulifeCompanyID;
    }

    public void setManulifeCompanyID(String manulifeCompanyID) {
        this.manulifeCompanyID = manulifeCompanyID;
    }

    public Date getDiscontinuanceDate() {
        return discontinuanceDate;
    }

    public void setDiscontinuanceDate(Date discontinuanceDate) {
        this.discontinuanceDate = discontinuanceDate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

	public String getCommissionAsset1Year() {
		return commissionAsset1Year;
	}

	public void setCommissionAsset1Year(String commissionAsset1Year) {
		this.commissionAsset1Year = commissionAsset1Year;
	}

	public String getCommissionAssetRen() {
		return commissionAssetRen;
	}

	public void setCommissionAssetRen(String commissionAssetRen) {
		this.commissionAssetRen = commissionAssetRen;
	}

	public String getRiaFlatFeeProrata() {
		return riaFlatFeeProrata;
	}

	public void setRiaFlatFeeProrata(String riaFlatFeeProrata) {
		this.riaFlatFeeProrata = riaFlatFeeProrata;
	}

	public String getRiaFlatFeePerHead() {
		return riaFlatFeePerHead;
	}

	public void setRiaFlatFeePerHead(String riaFlatFeePerHead) {
		this.riaFlatFeePerHead = riaFlatFeePerHead;
	}

	public String getRiaBps() {
		return riaBps;
	}

	public void setRiaBps(String riaBps) {
		this.riaBps = riaBps;
	}
	
	public boolean isRiaBpsAvailable() {
		boolean riaBpsAvailable = false;
		if(StringUtils.isNotBlank(riaBps)) {
			try {
				double value = Double.parseDouble(riaBps);
				if(value != 0.0d) {
					riaBpsAvailable = true;
				}
			} catch (NumberFormatException ex){
				riaBpsAvailable = false;
			}
		}
		return riaBpsAvailable;
	} 

	public String getRiaBpsMax() {
		return riaBpsMax;
	}

	public void setRiaBpsMax(String riaBpsMax) {
		this.riaBpsMax = riaBpsMax;
	}

	public String getRiaAcBlend() {
		return riaAcBlend;
	}

	public void setRiaAcBlend(String riaAcBlend) {
		this.riaAcBlend = riaAcBlend;
	}

	public String getRiaAcTiered() {
		return riaAcTiered;
	}

	public void setRiaAcTiered(String riaAcTiered) {
		this.riaAcTiered = riaAcTiered;
	}

	public String getDes338Ind() {
		return des338Ind;
	}

	public void setDes338Ind(String des338Ind) {
		this.des338Ind = des338Ind;
	}

	public String getCommissionAssetAllYrs() {
		return commissionAssetAllYrs;
	}

	public void setCommissionAssetAllYrs(String commissionAssetAllYrs) {
		this.commissionAssetAllYrs = commissionAssetAllYrs;
	}

	public String getCommissionDepositReg1Yr() {
		return commissionDepositReg1Yr;
	}

	public void setCommissionDepositReg1Yr(String commissionDepositReg1Yr) {
		this.commissionDepositReg1Yr = commissionDepositReg1Yr;
	}

	public String getCommissionDepositRegRen() {
		return commissionDepositRegRen;
	}

	public void setCommissionDepositRegRen(String commissionDepositRegRen) {
		this.commissionDepositRegRen = commissionDepositRegRen;
	}

	public String getCommissionDepositTr1yr() {
		return commissionDepositTr1yr;
	}

	public void setCommissionDepositTr1yr(String commissionDepositTr1yr) {
		this.commissionDepositTr1yr = commissionDepositTr1yr;
	}

	public String getTrusteeName() {
		return trusteeName;
	}

	public void setTrusteeName(String trusteeName) {
		this.trusteeName = trusteeName;
	}

	public String getCommissionDepositTrRen() {
		return commissionDepositTrRen;
	}

	public void setCommissionDepositTrRen(String commissionDepositTrRen) {
		this.commissionDepositTrRen = commissionDepositTrRen;
	}

	public String getFrContractShareList() {
		return frContractShareList;
	}

	public void setFrContractShareList(String frContractShareList) {
		this.frContractShareList = frContractShareList;
	}
    
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public boolean isPreSignatureContract() {
		return preSignatureProductIds.contains(getProductId());
	}
    
	public boolean isPinPointContract() {
		return pinPointContract;
	}

	public void setPinPointContract(boolean pinPointContract) {
		this.pinPointContract = pinPointContract;
	}

	public String getRiaFirmName() {
		return riaFirmName;
	}

	public void setRiaFirmName(String riaFirmName) {
		this.riaFirmName = riaFirmName;
	}

	public BigDecimal getRiaFeePaidByJH() {
		return riaFeePaidByJH;
	}

	public void setRiaFeePaidByJH(BigDecimal riaFeePaidByJH) {
		this.riaFeePaidByJH = riaFeePaidByJH;
	}

	public BigDecimal getRiaFeePaidByPlan() {
		return riaFeePaidByPlan;
	}

	public void setRiaFeePaidByPlan(BigDecimal riaFeePaidByPlan) {
		this.riaFeePaidByPlan = riaFeePaidByPlan;
	}

	public BigDecimal getRiaTotalFee() {
		return riaTotalFee;
	}

	public BigDecimal getRiaBpsMin() {
		return this.riaBpsMin;
	}

	public void setRiaBpsMin(BigDecimal riaBpsMin) {
		this.riaBpsMin = riaBpsMin;
	}
	
	public void setRiaTotalFee(BigDecimal riaTotalFee) {
		this.riaTotalFee = riaTotalFee;
	}
	
	public BigDecimal getRiaBpsAnnualMaxAmount() {
		return this.riaBpsAnnualMaxAmount;
	}

	public void setRiaBpsAnnualMaxAmount(BigDecimal riaBpsAnnualMaxAmount) {
		this.riaBpsAnnualMaxAmount = riaBpsAnnualMaxAmount;
	}

	public BigDecimal getRiaBpsMonthlyMinAmount() {
		return this.riaBpsMonthlyMinAmount;
	}

	public void setRiaBpsMonthlyMinAmount(BigDecimal riaBpsMonthlyMinAmount) {
		this.riaBpsMonthlyMinAmount = riaBpsMonthlyMinAmount;
	}

	public String getCofidFeeFeatureCode() {
		return cofidFeeFeatureCode;
	}

	public void setCofidFeeFeatureCode(String cofidFeeFeatureCode) {
		this.cofidFeeFeatureCode = cofidFeeFeatureCode;
	}
	
	public BigDecimal getCofid321ABFee() {
		return this.cofid321ABFee;
	}

	public void setCofid321ABFee(BigDecimal cofid321abFee) {
		this.cofid321ABFee = cofid321abFee;
	}

	public BigDecimal getCofid321DBFee() {
		return this.cofid321DBFee;
	}

	public void setCofid321DBFee(BigDecimal cofid321dbFee) {
		this.cofid321DBFee = cofid321dbFee;
	}

	public BigDecimal getCofid338ABFee() {
		return this.cofid338ABFee;
	}

	public void setCofid338ABFee(BigDecimal cofid338abFee) {
		this.cofid338ABFee = cofid338abFee;
	}

	public BigDecimal getCofid338DBFee() {
		return this.cofid338DBFee;
	}

	public void setCofid338DBFee(BigDecimal cofid338dbFee) {
		this.cofid338DBFee = cofid338dbFee;
	}
	
	public BigDecimal getCofidBPSFeeMonthlyMinAmt() {
		return this.cofidBPSFeeMonthlyMinAmt;
	}

	public void setCofidBPSFeeMonthlyMinAmt(BigDecimal cofidBPSFeeMonthlyMinAmt) {
		this.cofidBPSFeeMonthlyMinAmt = cofidBPSFeeMonthlyMinAmt;
	}

	public SmallPlanFeature getSmallPlanOptionCode() {
		return smallPlanOptionCode;
	}

	public void setSmallPlanOptionCode(SmallPlanFeature smallPlanOptionCode) {
		this.smallPlanOptionCode = smallPlanOptionCode;
	}	
}
