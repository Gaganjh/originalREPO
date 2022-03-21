package com.manulife.pension.bd.web.bob.participant;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantDeferralVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.employee.util.DeferralProcessing;

/**
 * Form for the Participant Account page
 * 
 * @author Saravana
 */
public class ParticipantAccountForm extends BaseForm {
	
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private String profileId = null;
	private int contractNumber;
	
    private String selectedAsOfDate = null;
	private String baseAsOfDate = null;
	private String selectedLoan = null;
	@SuppressWarnings("unchecked")
	private ArrayList loanList = null;
	
	private boolean hasInvestments = false;
	private boolean showAge = false;
	private boolean showPba = false;
	private boolean showLoans = false;
	private boolean showLifecycle = false;
	private boolean showManagedAccount = false;
		
	private String deferralContributionText;
	private String nextIncreaseValue;
	private String personalRateLimit;
	
	private boolean isDefinedBenefitContract = false;
	
	private String fundsOrganizedBy = BDConstants.VIEW_BY_ASSET_CLASS;
	
	private static DecimalFormat AMOUNT_FORMATTER = new DecimalFormat("##,###,##0.00");
	private static DecimalFormat PERCENT_FORMATTER = new DecimalFormat("##.###");

	public static final String MANAGING_DEFERRALS = "MD";
    public static final String MED_DEFERRAL_TYPE = "DFT";
    private boolean pdfCapped = false;
    private int cappedRowsInPDF = 0;
    
    private boolean showGiflFootnote = false;
    
    private boolean showNonRothHeader = false;
    private boolean showRothHeader = false;
    private boolean showMultileRothFootnote = false;
    
    //added this Code as part of ME: GWAMRPSBB-34-FRW PDF version of Participant Reports
    private boolean showParticipantNewFooter = false;
    
    //synchronized method to avoid race condition.
    public static synchronized String formatAmountFormatter(Double value) { 
        return AMOUNT_FORMATTER.format(value); 
    }
    //synchronized method to avoid race condition.
    public static synchronized String formatPercentageFormatter(Double value) { 
        return PERCENT_FORMATTER.format(value); 
    }
    
    /**
     * @return Returns the cappedRowsInPDF.
     */
    public int getCappedRowsInPDF() {
        return cappedRowsInPDF;
    }
    
    /**
     * @param cappedRowsInPDF The cappedRowsInPDF to set.
     */
    public void setCappedRowsInPDF(int cappedRowsInPDF) {
        this.cappedRowsInPDF = cappedRowsInPDF;
    }

    /**
     * This method will return true if the PDF has been capped.
     * @return
     */
    public boolean getPdfCapped() {
        return pdfCapped;
    }

    /**
     * 
     * @param pdfCapped
     */
    public void setPdfCapped(boolean pdfCapped) {
        this.pdfCapped = pdfCapped;
    }
    
	/**
	 * Gets the profileId
	 * @return Returns a String
	 */
	public String getProfileId() {
		return profileId;
	}
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
	/**
	 * Gets the selectedAsOfDate
	 * @return Returns a String
	 */
	public String getSelectedAsOfDate() {
		return selectedAsOfDate;
	}
	/**
	 * Sets the selectedAsOfDate
	 * @param selectedAsOfDate The selectedAsOfDate to set
	 */
	public void setSelectedAsOfDate(String selectedAsOfDate) {
		this.selectedAsOfDate = selectedAsOfDate;
	}
	
	/**
	 * Gets the baseAsOfDate
	 * @return Returns a String
	 */
	public String getBaseAsOfDate() {
		return baseAsOfDate;
	}
	/**
	 * Sets the baseAsOfDate
	 * @param baseAsOfDate The baseAsOfDate to set
	 */
	public void setBaseAsOfDate(String baseAsOfDate) {
		this.baseAsOfDate = baseAsOfDate;
	}

	/**
	 * Gets the selectedLoan
	 * @return Returns a String
	 */
	public String getSelectedLoan() {
		return selectedLoan;
	}
	/**
	 * Sets the selectedLoan
	 * @param selectedLoan The selectedLoan to set
	 */
	public void setSelectedLoan(String selectedLoan) {
		this.selectedLoan = selectedLoan;
	}
	
	/**
	 * Gets the showManagedAccount
	 * @return Returns a boolean
	 */
	public boolean getShowManagedAccount() {
		return showManagedAccount;
	}
	/**
	 * Sets the showManagedAccount
	 * @param showManagedAccount The showManagedAccount to set
	 */
	public void setShowManagedAccount(boolean showManagedAccount) {
		this.showManagedAccount = showManagedAccount;
	}
	
	@SuppressWarnings("serial")
	public class Attribute implements Serializable {
 		private String value;
 		private String label;

 		public Attribute(String value, String label)
 		{
 			this.value = value;
 			this.label = label;
 		}

 		public String getValue() {
 			return value;
 		}
 		public void setValue(String value) {
 			this.value = value;
 		}
 		public String getLabel() {
 			return label;
 		}
 		public void setLabel(String label) {
 			this.label = label;
 		}
 	}
	
	/**
	 * Gets the loanList
	 * @return Returns a ArrayList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList getLoanList() {
		return loanList;
	}
	/**
	 * Sets the loanList
	 * @param loanList The loanList to set
	 */
	@SuppressWarnings("unchecked")
	public void setLoanList(ArrayList loanList) {
		this.loanList = loanList;
	}

	/**
	 * 
	 * @param loandDetailsList
	 */
 	@SuppressWarnings("unchecked")
	public void setLoanDetailList(Collection loandDetailsList) {

		DecimalFormat formatter = new DecimalFormat("$#,##0.00");

		loanList = new ArrayList();

		Attribute attVO = new Attribute("-1","View details");
		loanList.add(attVO);

		Iterator iter = loandDetailsList.iterator();

		while (iter.hasNext())	{
			ParticipantLoanDetails loanDetail = (ParticipantLoanDetails) iter.next();
			String loanLabel = "Loan #"+loanDetail.getLoanId()+": "+formatter.format(loanDetail.getOutstandingPrincipalAmount());
			loanList.add(new Attribute(loanDetail.getLoanId(),loanLabel));
		}
 	}
	
 	/**
	 * Gets the hasInvestments
	 * @return Returns a boolean
	 */
	public boolean getHasInvestments() {
		return hasInvestments;
	}
	/**
	 * Sets the hasInvestments
	 * @param hasInvestments The hasInvestments to set
	 */
	public void setHasInvestments(boolean hasInvestments) {
		this.hasInvestments = hasInvestments;
	}

	/**
	 * Gets the showAge
	 * @return Returns a boolean
	 */
	public boolean getShowAge() {
		return showAge;
	}
	/**
	 * Sets the showAge
	 * @param showAge The showAge to set
	 */
	public void setShowAge(boolean showAge) {
		this.showAge = showAge;
	}
	
	/**
	 * Gets the showPba
	 * @return Returns a boolean
	 */
	public boolean getShowPba() {
		return showPba;
	}
	/**
	 * Sets the showPba
	 * @param showPba The showPba to set
	 */
	public void setShowPba(boolean showPba) {
		this.showPba = showPba;
	}
	
	/**
	 * Gets the showLoans
	 * @return Returns a boolean
	 */
	public boolean getShowLoans() {
		return showLoans;
	}
	/**
	 * Sets the showLoans
	 * @param showLoans The showLoans to set
	 */
	public void setShowLoans(boolean showLoans) {
		this.showLoans = showLoans;
	}
	
	/**
	 * Gets the showLifecycle
	 * @return Returns the showLifecycle.
	 */
	public boolean getShowLifecycle() {
		return showLifecycle;
	}
	/**
	 * Sets the showLifecycle
	 * @param showLifecycle The showLifecycle to set.
	 */
	public void setShowLifecycle(boolean showLifecycle) {
		this.showLifecycle = showLifecycle;
	}
		
	/**
	 * Returns TRUE if both the base Date and select date are null or 
	 * if the base Date is equal to select date,
	 * 
	 * Returns FALSE if the base Date is not equal to select date
	 * 
	 * @return boolean
	 */
	public boolean isAsOfDateCurrent() {
		if(getBaseAsOfDate() == null || getSelectedAsOfDate() == null) { 
			return true;
		}
		
		return getBaseAsOfDate().equals(getSelectedAsOfDate());
	}
	
	/**
	 * Gets the deferralContribution Text that would be displayed in the page
	 * @return Returns the deferralContributionText.
	 */
	public String getDeferralContributionText() {
		return deferralContributionText;
	}
	
	/**
	 * Sets the deferralContribution Text that would be displayed in the page
	 * @param deferralContributionText The deferralContributionText to set. 
	 */
	public void setDeferralContributionText(String deferralContributionText) {
		this.deferralContributionText = deferralContributionText;
	}
	
	/**
	 * Gets the EZincrease next increase value 
	 * @return Returns the nextIncreaseValue.
	 */
	public String getNextIncreaseValue() {
		return nextIncreaseValue;
	}
	
	/**
	 * Sets the EZincrease next increase value
	 * @param nextIncreaseValue The nextIncreaseValue to set.
	 */
	public String setNextIncreaseValue(String nextIncreaseValue) {
		return this.nextIncreaseValue = nextIncreaseValue;
	}
	
	/**
	 * Gets the EZincrease Personal Contribution limit value
	 * @return Returns the personalRateLimit.
	 */
	public String getPersonalRateLimit() {
		return personalRateLimit;
	}
	
	/**
	 * Sets the EZincrease Personal Contribution limit value
	 * @param personalRateLimit The personalRateLimit to set.
	 */
	public String setPersonalRateLimit(String personalRateLimit) {
		return this.personalRateLimit = personalRateLimit;
	}
	
	/**
	 * Gets the option for organizing the funds
	 * @return Returns the funds organized by option
	 */
	public String getFundsOrganizedBy() {
		return fundsOrganizedBy;
	}
	
	/**
	 * Sets the option for organizing the funds
	 * @param Sets the funds organized by option
	 */
	public void setFundsOrganizedBy(String fundsOrganizedBy) {
		this.fundsOrganizedBy = fundsOrganizedBy;
	}

    /**
     * Gets is defined benefit contract indicator
     * 
     * @return boolean
     */
    public boolean getIsDefinedBenefitContract() {
        return isDefinedBenefitContract;
    }

    /**
     * Sets the is defined benefit contract indicator
     * 
     * @param isDefinedBenefitContract
     */
    public void setIsDefinedBenefitContract(boolean isDefinedBenefitContract) {
        this.isDefinedBenefitContract = isDefinedBenefitContract;
    }
    
    /**
     * Gets the contract number
     * 
     * @return int
     */
    public int getContractNumber() {
        return contractNumber;
    }

    
    /**
     * Sets the contract number
     * 
     * @param contractNumber
     */
    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }
	
	/**
	 * Calculate text for EE deferrals tab here, don't want this display stuff
	 * in VO but don't want complex logic in jsp. Also not being used in other places
	 * so no use making it a tag. This method is not called unless the Participant Account
	 * pages EE Deferral tab is selected.
	 * 
	 * @param deferralVO 	The ParticipantDeferralVO to set
	 * @param contract 		The Contract to set
	 * 
	 * @throws SystemException
	 */
	private void calculateDeferralsText(ParticipantDeferralVO deferralVO, Contract contract) 
	            throws SystemException {
		// AS per PPC.18.3 - PPC.18.6, ACI2 PPC.28

		StringBuffer contributionText = new StringBuffer();
		boolean beforeTaxPresent = false;
				
		boolean useBTDPCT = false;
		boolean useBTDAMT = false;
		
        // deal with possible null & zero issue;
		if ((deferralVO.getBeforeTaxDeferralPercent() == null) &&
			 (deferralVO.getBeforeTaxDeferralAmount() == null)) {
			// no values
		} else if  ((deferralVO.getBeforeTaxDeferralPercent() != null) &&
			        (deferralVO.getBeforeTaxDeferralAmount() == null)) {
			useBTDPCT = true;
		} else if ((deferralVO.getBeforeTaxDeferralAmount() != null) &&
		           (deferralVO.getBeforeTaxDeferralPercent() == null)) {
			useBTDAMT = true;
		} else { // have two values 
			if (deferralVO.getBeforeTaxDeferralPercent() > deferralVO.getBeforeTaxDeferralAmount()) {
				useBTDPCT = true;
			} else if (deferralVO.getBeforeTaxDeferralAmount() > deferralVO.getBeforeTaxDeferralPercent()) {
				useBTDAMT = true;
			} else { // both have a value and are the same, must both be zero(otherwise it's an error)
				// use same type as the increase amount.
				if ((deferralVO.getIncreaseAmount() != null) && (deferralVO.getIncreaseAmount() > 0)) {
					useBTDAMT = true;
				} else if ((deferralVO.getIncreasePercent() != null) && (deferralVO.getIncreasePercent() > 0)) {
					useBTDPCT = true;
				} else {					
					// final clause, check csf.
					if (isContractDeferralTypeDollar(contract.getContractNumber())) {
						useBTDAMT = true;
					} else {
						useBTDPCT = true;
					}
				}
			}
		}
				
		if (useBTDPCT) {
			beforeTaxPresent = true;
			contributionText.append(" Before tax: "
                    + genFormattedValue(deferralVO.getBeforeTaxDeferralPercent(), false));
		} else if (useBTDAMT) {
			beforeTaxPresent = true;
			contributionText.append(" Before tax: "
                    + genFormattedValue(deferralVO.getBeforeTaxDeferralAmount(), true));
		}

		boolean rothPresent = false;
		if (contract.hasRoth()) {	
			boolean useRPCT = false;
			boolean useRAMT = false;
			
			if ((deferralVO.getRothAmount() == null) &&
				(deferralVO.getRothPercent() == null)) {
				// no values
			} else if ((deferralVO.getRothPercent() != null) &&
					   (deferralVO.getRothAmount() == null)) {
				useRPCT = true;
			} else if ((deferralVO.getRothAmount() != null) &&
			           (deferralVO.getRothPercent() == null)) {
			    useRAMT = true;
			} else { // have two values
				if (deferralVO.getRothPercent() > deferralVO.getRothAmount()) {
					useRPCT = true;
				} else if (deferralVO.getRothAmount() > deferralVO.getRothPercent()) {
					useRAMT = true;
				} else { // both have a value and are the same, must both be zero(otherwise it's an error)
					if ((deferralVO.getIncreaseAmount() != null) && (deferralVO.getIncreaseAmount() > 0)) {
						useRAMT = true;
					} else if ((deferralVO.getIncreasePercent() != null) && (deferralVO.getIncreasePercent() > 0)) {
						useRPCT = true;
					} else {						
						// final clause, check csf.
						if (isContractDeferralTypeDollar(contract.getContractNumber())) {
							useRAMT = true;
						} else {
							useRPCT = true;
						}
					}
				}
			}
						
			if (useRPCT) {
				rothPresent = true;
				if (beforeTaxPresent) contributionText.append(",");
				contributionText.append(" Roth: "
                        + genFormattedValue(deferralVO.getRothPercent(), false));
			} else if (useRAMT) {
				rothPresent = true;
				if (beforeTaxPresent) contributionText.append(",");
				contributionText.append(" Roth: "
                        + genFormattedValue(deferralVO.getRothAmount(), true));
			}
		}

		if (beforeTaxPresent== false && rothPresent==false) {
			contributionText.append(" Not on file");
		}

		this.setDeferralContributionText(contributionText.toString());
	}
	
 	/**
	 * Returns TRUE if contract deferral type '$'
	 * @param contractId
	 * @return boolean
	 * @throws SystemException
	 */
	private static boolean isContractDeferralTypeDollar(Integer contractId) throws SystemException {
		ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();    
		try {
			ContractServiceFeature csf = delegate.getContractServiceFeature(contractId, MANAGING_DEFERRALS);
			if (csf !=null) {
				if (BDConstants.DOLLAR_SIGN.equalsIgnoreCase(csf
                        .getAttributeValue(MED_DEFERRAL_TYPE)))
                    return true;
			}
		} catch(ApplicationException ae) {
			throw new SystemException(ae, DeferralProcessing.class.getName(), "isContractDeferralTypeDollar", "");
		} 
		 
		return false;
	}	

	/**
	 * Gets the link
	 * 
	 * @param value
	 * @param formatDollar
	 * @return String
	 */
	private String genFormattedValue(Double value, boolean formatDollar) {
		String formattedValue = null;
		if (formatDollar) {
			formattedValue = BDConstants.DOLLAR_SIGN + formatAmountFormatter(value);
		} else {
			formattedValue = formatPercentageFormatter(value) + BDConstants.PERCENTAGE_SIGN;
		}
		return formattedValue;
	}
	
	/**
	 * Calculates the deferrals Text, Next increase and Personal contribution limit.
	 * 
	 * @param deferralVO
	 * @param theContract
	 * 
	 * @throws SystemException
	 */
	public void calculateDeferralFields(ParticipantDeferralVO deferralVO, Contract theContract) 
    throws SystemException {
		calculateDeferralsText(deferralVO, theContract);
		calculateNextIncrease(deferralVO);
		calculatePersonalRateLimit(deferralVO);
	}
	
	/**
	 * Calculate the next increase type and value
	 * 
	 * @param ParticipantDeferralVO
	 */
	private void calculateNextIncrease(ParticipantDeferralVO deferralVO) {
		
		// PPC.41 & PPC.42
		// If the CSF deferral Type is '$ only'
		if (ParticipantDeferralVO.DEFERRAL_TYPE_DOLLAR.equalsIgnoreCase(
				deferralVO.getContractDeferralType())) {
			
			// check whether the PPT has a customized dollar value, 
			// if yes then set that customized value 
			// else set the contract default dollar value
			if (deferralVO.getIncreaseAmount() != null) {
				nextIncreaseValue = BDConstants.DOLLAR_SIGN + 
						formatAmountFormatter(deferralVO.getIncreaseAmount());
			} else {
				nextIncreaseValue = BDConstants.DOLLAR_SIGN + 
						deferralVO.getContractDefaultIncreaseAmount();
			}
		
		// If the CSF deferral Type is '% only'
		} else if (ParticipantDeferralVO.DEFERRAL_TYPE_PERCENT.equalsIgnoreCase(
				deferralVO.getContractDeferralType())) {
			
			// check whether the PPT has a customized percent value, 
			// if yes then set that customized value 
			// else set the contract default percent value
			if (deferralVO.getIncreasePercent() != null) {
				nextIncreaseValue = formatPercentageFormatter(deferralVO.getIncreasePercent())
                        + BDConstants.PERCENTAGE_SIGN;
			} else {
				nextIncreaseValue = deferralVO.getContractDefaultIncreasePercent() + 
						BDConstants.PERCENTAGE_SIGN;
			}
		
		// If the CSF deferral Type is 'Either % or $'	
		} else if (ParticipantDeferralVO.DEFERRAL_TYPE_EITHER.equalsIgnoreCase(
				deferralVO.getContractDeferralType())) {
			
			// If dollar value is customized, then set that value
			if (deferralVO.getIncreaseAmount() != null) {
				nextIncreaseValue = BDConstants.DOLLAR_SIGN
                        + formatAmountFormatter(deferralVO.getIncreaseAmount());
			
			// If Percent value is customized, then set that value
			} else if (deferralVO.getIncreasePercent() != null) {
				nextIncreaseValue = formatPercentageFormatter(deferralVO.getIncreasePercent())
                        + BDConstants.PERCENTAGE_SIGN;
				
			// set the value based on the Confirmed deferral type
			} else {
				
				// if the PPT has either confirmed Before Tax or Roth 
				// contribution in dollar, then set the contract default 
				// dollar value
				if ((deferralVO.getBeforeTaxDeferralAmount() != null) ||
						(deferralVO.getRothAmount() != null)) {
					nextIncreaseValue = BDConstants.DOLLAR_SIGN
                            + deferralVO.getContractDefaultIncreaseAmount();
					
				// if the PPT has either confirmed Before Tax or Roth 
				// contribution in percent, then set the contract default 
				// percent value
				} else if ((deferralVO.getBeforeTaxDeferralPercent() != null) ||
						   (deferralVO.getRothPercent() != null)) {
					nextIncreaseValue = deferralVO.getContractDefaultIncreasePercent()
                            + BDConstants.PERCENTAGE_SIGN;
					
				// if none of the above conditions are satisfied, set the 
				// contract default percent value 
				} else { 
                    nextIncreaseValue = deferralVO.getContractDefaultIncreasePercent()
                            + BDConstants.PERCENTAGE_SIGN;
				}
			}
		}
	}
	
	/**
	 * Calculates the personal limit type and value
	 * 
	 * @param ParticipantDeferralVO
	 */
	private void calculatePersonalRateLimit(ParticipantDeferralVO deferralVO) {
		
		// PPC.43 & PPC.44
		// If the CSF deferral Type is '$ only'
		if (ParticipantDeferralVO.DEFERRAL_TYPE_DOLLAR.equalsIgnoreCase(
				deferralVO.getContractDeferralType())) {
			
			// check whether the PPT has a customized dollar value, 
			// if yes then set that customized value 
			// else set the contract default dollar value
			if (deferralVO.getMaxLimitAmount() != null) {
				personalRateLimit = BDConstants.DOLLAR_SIGN + 
						formatAmountFormatter(deferralVO.getMaxLimitAmount());
			} else {
				personalRateLimit = BDConstants.DOLLAR_SIGN + 
							deferralVO.getContractDefaultLimitAmount();
			}
		// If the CSF deferral Type is '% only'
		} else if (ParticipantDeferralVO.DEFERRAL_TYPE_PERCENT.equalsIgnoreCase(
			deferralVO.getContractDeferralType())) {
		
			// check whether the PPT has a customized percent value, 
			// if yes then set that customized value 
			// else set the contract default percent value
			if (deferralVO.getMaxLimitPercent() != null) {
				personalRateLimit = formatPercentageFormatter(deferralVO.getMaxLimitPercent())
                        	+ BDConstants.PERCENTAGE_SIGN;
			} else {
				personalRateLimit = deferralVO.getContractDefaultLimitPercent() 
							+ BDConstants.PERCENTAGE_SIGN;
			}
		// If the CSF deferral Type is 'Either % or $'	
		} else if (ParticipantDeferralVO.DEFERRAL_TYPE_EITHER.equalsIgnoreCase(
			deferralVO.getContractDeferralType())) {
			
			// If dollar value is customized, then set that value
			if (deferralVO.getMaxLimitAmount() != null) {
				personalRateLimit = BDConstants.DOLLAR_SIGN
                        + formatAmountFormatter(deferralVO.getMaxLimitAmount());
			
			// If Percent value is customized, then set that value
			} else if (deferralVO.getMaxLimitPercent() != null) {
				personalRateLimit = formatPercentageFormatter(deferralVO.getMaxLimitPercent())
                        + BDConstants.PERCENTAGE_SIGN;
				
			// set the value based on the Confirmed deferral type
			} else {
				
				// if the PPT has either confirmed Before Tax or Roth 
				// contribution in dollar, then set the contract default 
				// dollar value
				if ((deferralVO.getBeforeTaxDeferralAmount() != null) ||
						(deferralVO.getRothAmount() != null)) {
					personalRateLimit = BDConstants.DOLLAR_SIGN + 
							deferralVO.getContractDefaultLimitAmount();
				
				// if the PPT has either confirmed Before Tax or Roth 
				// contribution in percent, then set the contract default 
				// percent value
				} else if ((deferralVO.getBeforeTaxDeferralPercent() != null) ||
						   (deferralVO.getRothPercent() != null)) {
					personalRateLimit = deferralVO.getContractDefaultLimitPercent() 
							+ BDConstants.PERCENTAGE_SIGN;
				
				// if none of the above conditions are satisfied, set the 
				// contract default percent value 
				} else {
					personalRateLimit = deferralVO.getContractDefaultLimitPercent() 
					+ BDConstants.PERCENTAGE_SIGN;
				}
			}
		}
	}
	
	/**
	 * @return boolean value true or false
	 */
	public boolean isShowGiflFootnote() {
		return showGiflFootnote;
	}
	/**
	 * @param showGiflFootnote the showGiflFootnote to set
	 */
	public void setShowGiflFootnote(boolean showGiflFootnote) {
		this.showGiflFootnote = showGiflFootnote;
	}
	
	public void setShowNonRothHeader(boolean showNonRothHeader)
	{
		this.showNonRothHeader = 	showNonRothHeader;
	}
	public boolean getShowNonRothHeader()
	{
		return showNonRothHeader;
	}
	
	public void setShowRothHeader(boolean showRothHeader)
	{
		this.showRothHeader = showRothHeader;
	}
	
	public boolean getShowRothHeader()
	{
		return showRothHeader;
	}
	/**
	 * @return the showMultileRothFootnote
	 */
	public boolean isShowMultileRothFootnote() {
		return showMultileRothFootnote;
	}
	/**
	 * @param showMultileRothFootnote the showMultileRothFootnote to set
	 */
	public void setShowMultileRothFootnote(boolean showMultileRothFootnote) {
		this.showMultileRothFootnote = showMultileRothFootnote;
	}
	
	/**
	 * @return the showParticipantNewFooter
	 */
	public boolean isShowParticipantNewFooter() {
		return showParticipantNewFooter;
	}
	/**
	 * @param showParticipantNewFooter the showParticipantNewFooter to set
	 */
	public void setShowParticipantNewFooter(boolean showParticipantNewFooter) {
		this.showParticipantNewFooter = showParticipantNewFooter;
	}
	
	
}