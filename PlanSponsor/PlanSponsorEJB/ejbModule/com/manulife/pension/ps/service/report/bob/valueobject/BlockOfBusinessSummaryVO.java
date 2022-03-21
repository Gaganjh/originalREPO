package com.manulife.pension.ps.service.report.bob.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * This VO will hold the summary information to be shown in BOB page.
 * 
 * @author harlomte
 * 
 */
public class BlockOfBusinessSummaryVO implements Serializable {

    private static final long serialVersionUID = 2401287682503421362L;
    
    // Holds the Broker Information to be shown in the Summary section.
    private ArrayList<BrokerInfoVO> brokerInfoVO;
    
    // Holds the Internal user name to be shown in the Summary section.
    private String internalUserName;
    
    private BigDecimal activeContractAssets;

    private BigDecimal numOfActiveContracts;

    private BigDecimal numOfLives; // Number of Lives Active + Pending -> Shown in BOB page
    
    private BigDecimal numOfActiveLives; // Number of Active Lives -> Shown in Home Page.

    private BigDecimal numOfOutstandingProposals;
    
    private BigDecimal numOfOutstandingProposalsLives;
    
    private BigDecimal outstandingProposalsAssets;
    
    private BigDecimal numOfPendingContracts;

    private BigDecimal numOfPendingContractsLives;
    
    private BigDecimal pendingContractsAssets;
    
    private BigDecimal numOfDiscontinuedContracts;
    
	private boolean hasPresigContract = false;
    
    private boolean hasAllPinpointOrPresigContract = true;
    
	private boolean hasContractWithRiaAssocaited = false;
    
    private boolean hasContractWithMulipleRiaAssociated = false;
    
    private boolean hasContractWithRiaPaidByJH = false;
    
    private boolean hasContractWithABAllYearCompensation = false;
    
    private boolean hasRiaFees = false;
    
    private boolean has338Designation = false;
    
    private boolean hasContractsWithCofidSelected = false;

    public BigDecimal getActiveContractAssets() {
        return (activeContractAssets == null)?BigDecimal.valueOf(0.0):activeContractAssets;
    }

    public void setActiveContractAssets(BigDecimal activeContractAssets) {
        this.activeContractAssets = activeContractAssets;
    }

    public BigDecimal getNumOfActiveContracts() {
        return (numOfActiveContracts == null)?BigDecimal.valueOf(0.0):numOfActiveContracts;
    }

    public void setNumOfActiveContracts(BigDecimal numOfActiveContracts) {
        this.numOfActiveContracts = numOfActiveContracts;
    }

    public BigDecimal getNumOfLives() {
        return numOfLives;
    }

    public void setNumOfLives(BigDecimal numOfLives) {
        this.numOfLives = numOfLives;
    }
    
    public BigDecimal getNumOfActiveLives() {    	
        return (numOfActiveLives == null) ? BigDecimal.valueOf(0.0) : numOfActiveLives;
    }

    public void setNumOfActiveLives(BigDecimal numOfActiveLives) {
        this.numOfActiveLives = numOfActiveLives;
    }

    public BigDecimal getNumOfOutstandingProposals() {
        return (numOfOutstandingProposals == null)?BigDecimal.valueOf(0.0):numOfOutstandingProposals;
    }

    public void setNumOfOutstandingProposals(BigDecimal numOfOutstandingProposals) {
        this.numOfOutstandingProposals = numOfOutstandingProposals;
    }

    public BigDecimal getNumOfPendingContracts() {
        return (numOfPendingContracts == null)?BigDecimal.valueOf(0.0):numOfPendingContracts;
    }

    public void setNumOfPendingContracts(BigDecimal numOfPendingContracts) {
        this.numOfPendingContracts = numOfPendingContracts;
    }

    public BigDecimal getNumOfDiscontinuedContracts() {
        return (numOfDiscontinuedContracts == null)?BigDecimal.valueOf(0.0):numOfDiscontinuedContracts;
    }

    public void setNumOfDiscontinuedContracts(BigDecimal numOfDiscontinuedContracts) {
        this.numOfDiscontinuedContracts = numOfDiscontinuedContracts;
    }

    public ArrayList<BrokerInfoVO> getBrokerInfoVO() {
        return brokerInfoVO;
    }

    public void setBrokerInfoVO(ArrayList<BrokerInfoVO> brokerInfoVO) {
        this.brokerInfoVO = brokerInfoVO;
    }

    /**
     * This method is used to return a Map containing Unique Firm names.
     * 
     * @return
     */
    public Map<String, String> getUniqueFirmNames() {
        Map<String, String> uniqueFirmNames = new HashMap<String, String>();
        for (BrokerInfoVO brokerInfo : brokerInfoVO) {
            if (!StringUtils.isEmpty(brokerInfo.getBdFirmName())) {
                uniqueFirmNames.put(brokerInfo.getBdFirmName(), brokerInfo.getBdFirmName());
            }
        }
        return uniqueFirmNames;
    }
    
    public String getInternalUserName() {
        return internalUserName;
    }

    public void setInternalUserName(String internalUserName) {
        this.internalUserName = internalUserName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the numOfOutstandingProposalsLives
     */
    public BigDecimal getNumOfOutstandingProposalsLives() {
        return (numOfOutstandingProposalsLives == null)?BigDecimal.valueOf(0.0):numOfOutstandingProposalsLives;
    }

    /**
     * @param numOfOutstandingProposalsLives the numOfOutstandingProposalsLives to set
     */
    public void setNumOfOutstandingProposalsLives(BigDecimal numOfOutstandingProposalsLives) {
        this.numOfOutstandingProposalsLives = numOfOutstandingProposalsLives;
    }

    /**
     * @return the numOfPendingContractsLives
     */
    public BigDecimal getNumOfPendingContractsLives() {
        return (numOfPendingContractsLives == null)?BigDecimal.valueOf(0.0):numOfPendingContractsLives;
    }

    /**
     * @param numOfPendingContractsLives the numOfPendingContractsLives to set
     */
    public void setNumOfPendingContractsLives(BigDecimal numOfPendingContractsLives) {
        this.numOfPendingContractsLives = numOfPendingContractsLives;
    }

    /**
     * @return the outstandingProposalsAssets
     */
    public BigDecimal getOutstandingProposalsAssets() {
        return (outstandingProposalsAssets == null)?BigDecimal.valueOf(0.0):outstandingProposalsAssets;
    }

    /**
     * @param outstandingProposalsAssets the outstandingProposalsAssets to set
     */
    public void setOutstandingProposalsAssets(BigDecimal outstandingProposalsAssets) {
        this.outstandingProposalsAssets = outstandingProposalsAssets;
    }

    /**
     * @return the pendingContractsAssets
     */
    public BigDecimal getPendingContractsAssets() {
        return (pendingContractsAssets == null)?BigDecimal.valueOf(0.0):pendingContractsAssets;
    }

    /**
     * @param pendingContractsAssets the pendingContractsAssets to set
     */
    public void setPendingContractsAssets(BigDecimal pendingContractsAssets) {
        this.pendingContractsAssets = pendingContractsAssets;
    }

    /**
     * Returns the total contracts
     * 
     * @return BigDecimal
     */
    public BigDecimal getTotalContracts() {
        return getNumOfActiveContracts().add(getNumOfOutstandingProposals()).add(getNumOfPendingContracts());
    }

    /**
     * Returns the total participants
     * 
     * @return BigDecimal
     */
    public BigDecimal getTotalLives() {
        return getNumOfActiveLives().add(getNumOfOutstandingProposalsLives()).add(getNumOfPendingContractsLives());
    }

    /**
     * Returns the total assets
     * 
     * @return BigDecimal
     */
    public BigDecimal getTotalAssets() {
        return getActiveContractAssets().add(getOutstandingProposalsAssets()).add(getPendingContractsAssets());
    }
    
    public boolean getHasPresigContract() {
		return hasPresigContract;
	}

	public void setHasPresigContract(boolean hasPresigContract) {
		this.hasPresigContract = hasPresigContract;
	}

	public boolean getHasAllPinpointOrPresigContract() {
		return hasAllPinpointOrPresigContract;
	}

	public void setHasAllPinpointOrPresigContract(
			boolean hasAllPinpointOrPresigContract) {
		this.hasAllPinpointOrPresigContract = hasAllPinpointOrPresigContract;
	}

	public boolean getHasContractWithRiaAssocaited() {
		return hasContractWithRiaAssocaited;
	}

	public void setHasContractWithRiaAssocaited(boolean hasContractWithRiaAssocaited) {
		this.hasContractWithRiaAssocaited = hasContractWithRiaAssocaited;
	}

	public boolean getHasContractWithMulipleRiaAssociated() {
		return hasContractWithMulipleRiaAssociated;
	}

	public void setHasContractWithMulipleRiaAssociated(
			boolean hasContractWithMulipleRiaAssociated) {
		this.hasContractWithMulipleRiaAssociated = hasContractWithMulipleRiaAssociated;
	}

	public boolean getHasContractWithRiaPaidByJH() {
		return hasContractWithRiaPaidByJH;
	}

	public void setHasContractWithRiaPaidByJH(boolean hasContractWithRiaPaidByJH) {
		this.hasContractWithRiaPaidByJH = hasContractWithRiaPaidByJH;
	}
	
	public boolean getHasContractWithABAllYearCompensation() {
		return hasContractWithABAllYearCompensation;
	}

	public void setHasContractWithABAllYearCompensation(
			boolean hasContractWithABAllYearCompensation) {
		this.hasContractWithABAllYearCompensation = hasContractWithABAllYearCompensation;
	}

	public boolean getHasRiaFees() {
		return hasRiaFees;
	}

	public void setHasRiaFees(boolean hasRiaFees) {
		this.hasRiaFees = hasRiaFees;
	}

	public boolean getHas338Designation() {
		return has338Designation;
	}

	public void setHas338Designation(boolean has338Designation) {
		this.has338Designation = has338Designation;
	}
	
	public boolean getHasContractsWithCofidSelected() {
		return hasContractsWithCofidSelected;
	}

	public void setHasContractsWithCofidSelected(boolean hasContractsWithCofidSelected) {
		this.hasContractsWithCofidSelected = hasContractsWithCofidSelected;
	}
}
