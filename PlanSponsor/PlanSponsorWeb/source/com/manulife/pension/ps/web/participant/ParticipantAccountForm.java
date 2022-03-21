package com.manulife.pension.ps.web.participant;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantDeferralVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsForm;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;

public class ParticipantAccountForm extends PsForm {
	private String selectedAsOfDate = null;
	private String baseAsOfDate = null;
	private String selectedLoan = null;
	private boolean showAge = false;
	private boolean showLoans = false;
	private ArrayList loanList = null;
	private String profileId = null;
	private boolean hasInvestments = false;
	private boolean showPba = false;
	private boolean showLifecycle = false;
	private boolean showNetContribEarnings = false;
	private boolean isDefinedBenefitContract = false;
	private boolean showCreateWithdrawalRequestLink = false;
	private boolean showLoanCreateLink;
	private boolean showManagedAccount = false;
	
	private String deferralContributionText;
	private String nextIncreaseValue;
	private String personalRateLimit;
	private boolean showGiflFootnote = false;

	private static DecimalFormat AMOUNT_FORMATTER = new DecimalFormat("##,###,##0.00");
	private static DecimalFormat PERCENT_FORMATTER = new DecimalFormat("##.###");

	public static final String MANAGING_DEFERRALS = "MD";
    public static final String MED_DEFERRAL_TYPE = "DFT";
    public static final String DEFERRAL_TYPE_DOLLAR="$";
    
    private boolean showNonRothHeader = false;
    private boolean showRothHeader = false;
    private boolean showMultileRothFootnote = false;

    //synchronized method to avoid race condition.
    public static synchronized String formatAmountFormatter(Double value) { 
        return AMOUNT_FORMATTER.format(value); 
    }
    //synchronized method to avoid race condition.
    public static synchronized String formatPercentageFormatter(Double value) { 
        return PERCENT_FORMATTER.format(value); 
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


	public boolean isAsOfDateCurrent() {
		if(getBaseAsOfDate()==null||getSelectedAsOfDate()==null) return true;
		return getBaseAsOfDate().equals(getSelectedAsOfDate());
	}

	public String getColspan() {
	if (isAsOfDateCurrent())
			return "1";
		else if (isDefinedBenefitContract())
			return "3";
		else
			return "5";
	}


	/**
	 * Calculate the next increase type and value
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
				nextIncreaseValue = Constants.DEFERRAL_TYPE_DOLLAR + 
						formatAmountFormatter(deferralVO.getIncreaseAmount());
			} else {
				nextIncreaseValue = Constants.DEFERRAL_TYPE_DOLLAR + 
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
                        + Constants.DEFERRAL_TYPE_PERCENT;
			} else {
				nextIncreaseValue = deferralVO.getContractDefaultIncreasePercent() + 
						Constants.DEFERRAL_TYPE_PERCENT;
			}
		
		// If the CSF deferral Type is 'Either % or $'	
		} else if (ParticipantDeferralVO.DEFERRAL_TYPE_EITHER.equalsIgnoreCase(
				deferralVO.getContractDeferralType())) {
			
			// If dollar value is customized, then set that value
			if (deferralVO.getIncreaseAmount() != null) {
				nextIncreaseValue = Constants.DEFERRAL_TYPE_DOLLAR
                        + formatAmountFormatter(deferralVO.getIncreaseAmount());
			
			// If Percent value is customized, then set that value
			} else if (deferralVO.getIncreasePercent() != null) {
				nextIncreaseValue = formatPercentageFormatter(deferralVO.getIncreasePercent())
                        + Constants.DEFERRAL_TYPE_PERCENT;
				
			// set the value based on the Confirmed deferral type
			} else {
				
				// if the PPT has either confirmed Before Tax or Roth 
				// contribution in dollar, then set the contract default 
				// dollar value
				if ((deferralVO.getBeforeTaxDeferralAmount() != null) ||
						(deferralVO.getRothAmount() != null)) {
					nextIncreaseValue = Constants.DEFERRAL_TYPE_DOLLAR
                            + deferralVO.getContractDefaultIncreaseAmount();
					
				// if the PPT has either confirmed Before Tax or Roth 
				// contribution in percent, then set the contract default 
				// percent value
				} else if ((deferralVO.getBeforeTaxDeferralPercent() != null) ||
						   (deferralVO.getRothPercent() != null)) {
					nextIncreaseValue = deferralVO.getContractDefaultIncreasePercent()
                            + Constants.DEFERRAL_TYPE_PERCENT;
					
				// if none of the above conditions are satisfied, set the 
				// contract default percent value 
				} else { 
                    nextIncreaseValue = deferralVO.getContractDefaultIncreasePercent()
                            + Constants.DEFERRAL_TYPE_PERCENT;
				}
			}
		}
	}


	private void calculatePersonalRateLimit(ParticipantDeferralVO deferralVO) {
		
		// PPC.43 & PPC.44
		// If the CSF deferral Type is '$ only'
		if (ParticipantDeferralVO.DEFERRAL_TYPE_DOLLAR.equalsIgnoreCase(
				deferralVO.getContractDeferralType())) {
			
			// check whether the PPT has a customized dollar value, 
			// if yes then set that customized value 
			// else set the contract default dollar value
			if (deferralVO.getMaxLimitAmount() != null) {
				personalRateLimit = Constants.DEFERRAL_TYPE_DOLLAR + 
						formatAmountFormatter(deferralVO.getMaxLimitAmount());
			} else {
				personalRateLimit = Constants.DEFERRAL_TYPE_DOLLAR + 
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
                        	+ Constants.DEFERRAL_TYPE_PERCENT;
			} else {
				personalRateLimit = deferralVO.getContractDefaultLimitPercent() 
							+ Constants.DEFERRAL_TYPE_PERCENT;
			}
		// If the CSF deferral Type is 'Either % or $'	
		} else if (ParticipantDeferralVO.DEFERRAL_TYPE_EITHER.equalsIgnoreCase(
			deferralVO.getContractDeferralType())) {
			
			// If dollar value is customized, then set that value
			if (deferralVO.getMaxLimitAmount() != null) {
				personalRateLimit = Constants.DEFERRAL_TYPE_DOLLAR
                        + formatAmountFormatter(deferralVO.getMaxLimitAmount());
			
			// If Percent value is customized, then set that value
			} else if (deferralVO.getMaxLimitPercent() != null) {
				personalRateLimit = formatPercentageFormatter(deferralVO.getMaxLimitPercent())
                        + Constants.DEFERRAL_TYPE_PERCENT;
				
			// set the value based on the Confirmed deferral type
			} else {
				
				// if the PPT has either confirmed Before Tax or Roth 
				// contribution in dollar, then set the contract default 
				// dollar value
				if ((deferralVO.getBeforeTaxDeferralAmount() != null) ||
						(deferralVO.getRothAmount() != null)) {
					personalRateLimit = Constants.DEFERRAL_TYPE_DOLLAR + 
							deferralVO.getContractDefaultLimitAmount();
				
				// if the PPT has either confirmed Before Tax or Roth 
				// contribution in percent, then set the contract default 
				// percent value
				} else if ((deferralVO.getBeforeTaxDeferralPercent() != null) ||
						   (deferralVO.getRothPercent() != null)) {
					personalRateLimit = deferralVO.getContractDefaultLimitPercent() 
							+ Constants.DEFERRAL_TYPE_PERCENT;
				
				// if none of the above conditions are satisfied, set the 
				// contract default percent value 
				} else {
					personalRateLimit = deferralVO.getContractDefaultLimitPercent() 
							+ Constants.DEFERRAL_TYPE_PERCENT;
				}
			}
		}
	}


	public void calculateDeferralFields(ParticipantDeferralVO deferralVO, Contract theContract) 
	     throws SystemException {
		calculateDeferralsText(deferralVO, theContract);
		calculateNextIncrease(deferralVO);
		calculatePersonalRateLimit(deferralVO);
	}


	/**
	 * Calculate text for EE deferrals tab here, don't want this display stuff
	 * in VO but don't want complex logic in jsp. Also not being used in other places
	 * so no use making it a tag. This method is not called unless the Participant Account
	 * pages EE Deferral tab is selected.
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
			contributionText.append(" Before tax: "+genLink(deferralVO.getBeforeTaxDeferralPercent(), false));
		} else if (useBTDAMT) {
			beforeTaxPresent = true;
			contributionText.append(" Before tax: "+genLink(deferralVO.getBeforeTaxDeferralAmount(), true));
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
				contributionText.append(" Roth: "+genLink(deferralVO.getRothPercent(), false));
			} else if (useRAMT) {
				rothPresent = true;
				if (beforeTaxPresent) contributionText.append(",");
				contributionText.append(" Roth: "+genLink(deferralVO.getRothAmount(), true));
			}
		}

		if (beforeTaxPresent== false && rothPresent==false) {
			contributionText.append(" Not on file");
		}

		this.setDeferralContributionText(contributionText.toString());
	}

	
    // code more or less lifted from DeferralUtils.java in web.
	private static boolean isContractDeferralTypeDollar(Integer contractId) throws SystemException {
		ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();    
		try {
			ContractServiceFeature csf = delegate.getContractServiceFeature(contractId, MANAGING_DEFERRALS);
			if (csf !=null) {
				if (DEFERRAL_TYPE_DOLLAR.equalsIgnoreCase(csf.getAttributeValue(MED_DEFERRAL_TYPE))) return true;
			}
		} catch(ApplicationException ae) {
			throw new SystemException(ae, "");
		} 
		 
		return false;
	}	
	
	
	private String genLink(Double value, boolean formatDollar) {
		String formattedValue = null;
		if (formatDollar) {
			formattedValue = "$"+formatAmountFormatter(value);
		} else {
			formattedValue = formatPercentageFormatter(value)+"%";
		}
		return "<a href=\"/do/census/deferral\">"+formattedValue+"</a>";
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

	public class Attribute implements Serializable
	 	{
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
	public ArrayList getLoanList() {
		return loanList;
	}
	/**
	 * Sets the loanList
	 * @param loanList The loanList to set
	 */
	public void setLoanList(ArrayList loanList) {
		this.loanList = loanList;
	}


 	public void setLoanDetailList(Collection loandDetailsList) {

		DecimalFormat formatter = new DecimalFormat("$#,##0.00");

		loanList = new ArrayList();

		Attribute attVO = new Attribute("-1","View Loan Details");
		loanList.add(attVO);

		Iterator iter = loandDetailsList.iterator();

		while (iter.hasNext())	{
			ParticipantLoanDetails loanDetail = (ParticipantLoanDetails) iter.next();
			String loanLabel = "Loan #"+loanDetail.getLoanId()+": "+formatter.format(loanDetail.getOutstandingPrincipalAmount());
			loanList.add(new Attribute(loanDetail.getLoanId(),loanLabel));
		}
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
	 * Gets the showNetContribEarnings
	 * @return Returns a boolean
	 */
	public boolean getShowNetContribEarnings() {
		return showNetContribEarnings;
	}
	/**
	 * Sets the showNetContribEarnings
	 * @param showNetContribEarnings The showNetContribEarnings to set
	 */
	public void setShowNetContribEarnings(boolean showNetContribEarnings) {
		this.showNetContribEarnings = showNetContribEarnings;
	}
	/**
	 * @param showLifecycle The showLifecycle to set.
	 */
	public void setShowLifecycle(boolean showLifecycle) {
		this.showLifecycle = showLifecycle;
	}
	/**
	 * @return Returns the showLifecycle.
	 */
	public boolean getShowLifecycle() {
		return showLifecycle;
	}

	 /**
     * @return the showCreateWithdrawalRequestLink
     */
    public boolean getShowCreateWithdrawalRequestLink() {
        return showCreateWithdrawalRequestLink;
    }
    /**
     * @param showCreateWithdrawalRequestLink the showCreateWithdrawalRequestLink to set
     */
    public void setShowCreateWithdrawalRequestLink(final boolean showCreateWithdrawalRequestLink) {
        this.showCreateWithdrawalRequestLink = showCreateWithdrawalRequestLink;
    }

	public boolean isDefinedBenefitContract() {
		return isDefinedBenefitContract;
	}

	public void setDefinedBenefitContract(boolean isDefinedBenefitContract) {
		this.isDefinedBenefitContract = isDefinedBenefitContract;
	}

	public String getDeferralContributionText() {
		return deferralContributionText;
	}
	public void setDeferralContributionText(String deferralContributionText) {
		this.deferralContributionText = deferralContributionText;
	}
	public String getNextIncreaseValue() {
		return nextIncreaseValue;
	}
	public String getPersonalRateLimit() {
		return personalRateLimit;
	}
	public boolean isShowLoanCreateLink() {
		return showLoanCreateLink;
	}
	public void setShowLoanCreateLink(boolean showLoanCreateLink) {
		this.showLoanCreateLink = showLoanCreateLink;
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
	/**
	 * @return the showNonRothHeader
	 */
	public boolean isShowNonRothHeader() {
		return showNonRothHeader;
	}
	/**
	 * @param showNonRothHeader the showNonRothHeader to set
	 */
	public void setShowNonRothHeader(boolean showNonRothHeader) {
		this.showNonRothHeader = showNonRothHeader;
	}
	/**
	 * @return the showRothHeader
	 */
	public boolean isShowRothHeader() {
		return showRothHeader;
	}
	/**
	 * @param showRothHeader the showRothHeader to set
	 */
	public void setShowRothHeader(boolean showRothHeader) {
		this.showRothHeader = showRothHeader;
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
	 * Gets the showManagedAccount
	 * @return Returns a boolean
	 */
	public boolean getShowManagedAccount() {
		return showManagedAccount;
	}
	/**
	 * Sets the showManagedAccount
	 * @param showManagedAccount The selectedLoan to set
	 */
	public void setShowManagedAccount(boolean showManagedAccount) {
		this.showManagedAccount = showManagedAccount;
	}
	
}


