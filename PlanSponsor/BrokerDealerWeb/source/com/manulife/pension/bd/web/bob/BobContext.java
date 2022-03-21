package com.manulife.pension.bd.web.bob;

import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportVO;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.role.BDInternalUser;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.util.StaticHelperClass;

/**
 * BobContext class is used to store all the Bob related information. 
 * Both the contract and Participant level information required for the 
 * BOB business will be placed in the BobContext class
 * 
 * @author SAyyalusamy
 */
public class BobContext implements Serializable{

    private static final long serialVersionUID = 1L;

    // This variable will hold the Contract Information for the contract selected by the user in BOB
    // page.
    private ContractProfile contractProfile;
	
    // This variable holds the User Profile Information
	private BDUserProfile userProfile;
	
	// This variable holds the User Profile information of the "Internal user" who is mimicking
    // another user.
	private BDUserProfile mimickingUserProfile;
	
	// This variable holds the profile ID of the participant selected by the user.
	private String pptProfileId;
	
	// This variable holds the GIFL status of the participant
	private String participantGiflInd;

	private final static int DI_DURATION_6_MONTH = 6;  

    private final static int DI_DURATION_24_MONTH = 24;  
    
	/**
	 * Constructor for User
	 */
	public BobContext()	{
		super();
	}
	
	@Override
	public String toString() {
		return StaticHelperClass.toString(this);
	}
	
	/**
	 * @return Returns the contractProfile.
	 */
	public ContractProfile getContractProfile() {
		return this.contractProfile;
	}

	/**
	 * @param contractProfile The contractProfile to set.
	 */
	public void setContractProfile(ContractProfile contractProfile) {
		this.contractProfile = contractProfile;
	}
	
	/**
     * @return Returns the UserProfile object.
     */
	public BDUserProfile getUserProfile() {
        return userProfile;
    }

	/**
     * @param userProfile The UserProfile to set.
     */
    public void setUserProfile(BDUserProfile userProfile) {
        this.userProfile = userProfile;
    }

    /**
     * Returns the UserProfile object of the Mimicking Internal User.
     * 
     * @return
     */
    public BDUserProfile getMimickingUserProfile() {
        return mimickingUserProfile;
    }

    /**
     * Sets the UserProfile object of the Mimicking Internal User.
     * 
     * @param mimickingUserProfile
     */
    public void setMimickingUserProfile(BDUserProfile mimickingUserProfile) {
        this.mimickingUserProfile = mimickingUserProfile;
    }

    /**
     * Gets the contract
     * 
     * @return Returns a the current Contract or null if the current contract is not selected yet.
     */
	public Contract getCurrentContract() {
		return contractProfile == null ? null : contractProfile.getContract();
	}

	public void setCurrentContract(HttpServletRequest request, Contract contract) throws SystemException {
        this.setCurrentContract(request, contract.getContractNumber());
    }

    /**
     * Sets the contract based on contractNumber.
     * 
     * @param contract The contract to set
     * @throws SystemException
     */
	public void setCurrentContract(HttpServletRequest request, Integer contractNumber) throws SystemException {
        Contract contract = null;

        if (contractNumber != null) {
            Contract existingContract = this.contractProfile == null ? null : this.contractProfile
                    .getContract();

            if (existingContract != null) {
                // Check if it is same as the contract passed in as parameter
                if (existingContract.getContractNumber() == contractNumber) {
                    // Don't need to set any contract. Existing contract is the same the
                    // contractNumber passed as parameter.
                    return;
                } else {
                    // Get the new contract details from Database
                    contract = getContractDetails(request, contractNumber);
                }
            } else {
                contract = getContractDetails(request, contractNumber);
            }
        }
	    
        this.contractProfile = new ContractProfile(contract);
	}
	
	/**
     * Load the contract Details for the given contract number.
     * 
     * @param contractNumber
     * @return - Contract Information for the given contract number.
     * @throws SystemException
     */
	private Contract getContractDetails(HttpServletRequest request, Integer contractNumber) 
		throws SystemException {
	    
        Contract contractDetails = null;
        
        boolean isUserAllowedAccessToContract = verifyUserAcessToContract(request, contractNumber);
        ContractSummaryVO contractSummaryVO = null;
        if (isUserAllowedAccessToContract) {
            BDUserRole userRole = userProfile.getRole();
        
            int diDuration = DI_DURATION_6_MONTH;
            if (userRole instanceof BDInternalUser) {
                diDuration = DI_DURATION_24_MONTH;
            }

            try {
                contractDetails = ContractServiceDelegate.getInstance().getContractDetails(
                        contractNumber, diDuration);
                contractSummaryVO = ContractServiceDelegate.getInstance().getContractSummary(contractNumber,false);
                if(contractSummaryVO!=null){
                contractDetails.setLoansTotalAmount(contractSummaryVO.getOutstandingLoans());
                }
            } catch (ContractNotExistException ce) {
                // The contract does not exist. Contract object will remain as null.
            }
            if (contractDetails != null && contractDetails.getContractDates() == null) {
                contractDetails.setContractDates(EnvironmentServiceDelegate.getInstance(
                        BDConstants.BD_APPLICATION_ID).getContractDates(
                        contractDetails.getContractNumber()));
            }
        }
        return contractDetails;
    }

    /**
     * This method would verify if the current User has access to the contract number.
     * 
     * @param contractNumber
     * @return - true if the user has access to the contract, else, return false.
     * @throws SystemException
     */
    private boolean verifyUserAcessToContract(HttpServletRequest request, Integer contractNumber) 
    	throws SystemException {
        boolean isUserAllowedAccessToContract = false;
	    
	    // Call BOB Stored proc for AC contracts
        ReportCriteria criteria = BlockOfBusinessUtility.populateReportCriteria(userProfile,
                mimickingUserProfile, contractNumber, BDConstants.CONTRACT_STATUS_AC, 
                BlockOfBusinessUtility.getDBSessionIDForDefaultAsOfDate(request));
        BlockOfBusinessReportData bobReportData = BlockOfBusinessUtility
				.callBOBStoredProc(criteria, request);
        isUserAllowedAccessToContract = isContractNumberPresentInReportData(contractNumber,
                bobReportData);
        
        if (!isUserAllowedAccessToContract) {
            // Call BOB Stored proc for DI contracts
            criteria = BlockOfBusinessUtility.populateReportCriteria(userProfile,
                    mimickingUserProfile, contractNumber, BDConstants.CONTRACT_STATUS_DI,
                    bobReportData.getDbSessionID());
            isUserAllowedAccessToContract = isContractNumberPresentInReportData(contractNumber,
                    BlockOfBusinessUtility.callBOBStoredProc(criteria, request));           
        }
        if (!isUserAllowedAccessToContract) {
        	// Call BOB Stored proc for Pending/pre-active contracts
        	criteria = BlockOfBusinessUtility.populateReportCriteria(userProfile,
                    mimickingUserProfile, contractNumber, BDConstants.CONTRACT_STATUS_PN,
                    bobReportData.getDbSessionID());
            isUserAllowedAccessToContract = isContractNumberPresentInReportData(contractNumber,
                    BlockOfBusinessUtility.callBOBStoredProc(criteria, request));
        }
        // BOB Stored Proc is not called for contracts which are Oustanding Proposals or Pending
        // status, because the user is not allowed to go to contract specific pages for the
        // contracts having these contract statuses.
        
        return isUserAllowedAccessToContract;
    }

    /**
     * This method checks if the given contractNumber is present in the BlockOfBusiness report
     * details.
     * 
     * @param contractNumber - The Integer number to be checked.
     * @param bobReportData - The BlockOfBusiness report details obtained from a call to BOB stored
     *            proc.
     * @return - true if the contractNumber is present in BlockOfBusiness report details, else,
     *         returns false.
     */
	@SuppressWarnings("unchecked")
    private boolean isContractNumberPresentInReportData(Integer contractNumber,
            BlockOfBusinessReportData bobReportData) {
	    
	    boolean isContractNumberPresentInReportData = false;
	    
        if (bobReportData != null && bobReportData.getDetails() != null
                && !bobReportData.getDetails().isEmpty()) {
            ArrayList<BlockOfBusinessReportVO> bobReportVOList = (ArrayList<BlockOfBusinessReportVO>) bobReportData
                    .getDetails();
            for (BlockOfBusinessReportVO bobReportVO : bobReportVOList) {
                if (contractNumber.equals(bobReportVO.getContractNumber())) {
                    isContractNumberPresentInReportData = true;
                    break;
                }
            }
        }
        
        return isContractNumberPresentInReportData;
    }
	
	/**
	 * This method returns the clientId
	 * 
	 * @return String
	 */
	public String getClientId()	{
		String returnValue ="";
		if (contractProfile != null) {
			returnValue = contractProfile.getContract().getClientId();
		}
		return returnValue;
	}
	

    public String getPptProfileId() {
        return pptProfileId;
    }

    public void setPptProfileId(String pptProfileId) {
        this.pptProfileId = pptProfileId;
    }

    /**
     * This method returns the site location of the contract (usa/ny)
     * 
     * @return String
     */
    public String getContractSiteLocation() {
        String siteLocation = "";
        Contract contract = getCurrentContract();
        if (contract != null) {
            siteLocation = GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA.equals(contract
                    .getCompanyCode()) ? BDConstants.SITEMODE_USA
                    : BDConstants.SITEMODE_NY;
        }
        return siteLocation;
    }

	/**
	 * Returns the participant gifl indicator
	 * 
	 * @return String
	 */
	public String getParticipantGiflInd() {
		return participantGiflInd;
	}

	/**
	 * Sets the participant gifl indicator
	 * 
	 * @param participantGIFLInd
	 */
	public void setParticipantGiflInd(String participantGiflInd) {
		this.participantGiflInd = participantGiflInd;
	}
}
