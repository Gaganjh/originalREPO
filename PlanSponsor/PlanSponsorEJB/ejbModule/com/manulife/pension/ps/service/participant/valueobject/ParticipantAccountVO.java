package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * ParticipantAccountVO class
 * This class is used as a value object used on the Participant Account page,
 * this is the big VO that will be returned to the presentation layer
 * 
 * @author Simona Stoicescu
 *
 *
 **/

public class ParticipantAccountVO implements Serializable {
	private ParticipantAccountDetailsVO			participantAccountDetailsVO;
	private ParticipantAccountAssetsByRiskVO 	assetsByRisk; 
    private InvestmentOptionVO[] 				participantFundsByRisk;
	private Collection						   	loanDetailsCollection;
	private HashMap 							employeeMoneyTypeTotals;
	private HashMap 							employerMoneyTypeTotals;
	private Collection 							netContribEarningsDetailsCollection;
	private double								totalEmployeeContributionsAssets;
	private double								totalEmployeeContributionsLoanAssets;
	private double								totalEmployerContributionsAssets;	
	private double								totalEmployerContributionsLoanAssets;
	
	private InvestmentOptionVO[] 				organizedParticipantFunds;
	private boolean  rothMoneyTypeInd = false;
	private boolean  nonRothMoneyTypeInd = false;
	private int rothMoneyTypeCount = 0;
	

	
	public ParticipantAccountVO (){
		loanDetailsCollection = new ArrayList();
		employeeMoneyTypeTotals = new HashMap();
		employerMoneyTypeTotals = new HashMap();
		netContribEarningsDetailsCollection = new ArrayList();
	}

		
	
	/**
	 * Adds a ParticipantLoanDetails object to the collection loanDetailsList
	 */

    public void addParticipantLoanDetails(ParticipantLoanDetails loanDetailsItem) {
    	if (loanDetailsItem != null) {
			loanDetailsCollection.add(loanDetailsItem);
    	}
	}

	/**
	 * Adds a ParticipantNetContribEarningsVO object to the collection netContribEarningsDetailsCollection
	 */

    public void addNetContribEarningsDetails(ParticipantNetContribEarningsVO netContribEarningsItem) {
    	if (netContribEarningsItem != null) {
    	    netContribEarningsDetailsCollection.add(netContribEarningsItem);
    	}
	}	
	

	/**
	 * Gets the participantAccountDetailsVO
	 * @return Returns a ParticipantAccountDetailsVO
	 */
	public ParticipantAccountDetailsVO getParticipantAccountDetailsVO() {
		return participantAccountDetailsVO;
	}
	/**
	 * Sets the participantAccountDetailsVO
	 * @param participantAccountDetailsVO The participantAccountDetailsVO to set
	 */
	public void setParticipantAccountDetailsVO(ParticipantAccountDetailsVO participantAccountDetailsVO) {
		this.participantAccountDetailsVO = participantAccountDetailsVO;
	}


	/**
	 * Gets the assetsByRisk
	 * @return Returns a ParticipantAccountAssetsByRiskVO
	 */
	public ParticipantAccountAssetsByRiskVO getAssetsByRisk() {
		return assetsByRisk;
	}
	/**
	 * Sets the assetsByRisk
	 * @param assetsByRisk The assetsByRisk to set
	 */
	public void setAssetsByRisk(ParticipantAccountAssetsByRiskVO assetsByRisk) {
		this.assetsByRisk = assetsByRisk;
	}



	/**
	 * Gets the loanDetailsList
	 * @return Returns a Collection
	 */
	public Collection getLoanDetailsCollection() {
		return loanDetailsCollection;
	}
	/**
	 * Sets the loanDetailsCollection
	 * @param loanDetailsCollection The loanDetailsCollection to set
	 */
	public void setLoanDetailsList(Collection loanDetailsCollection) {
		this.loanDetailsCollection = loanDetailsCollection;
	}

	/**
	 * Gets the netContribEarningsDetailsCollection
	 * @return Returns a Collection
	 */
	public Collection getNetContribEarningsDetailsCollection() {
		return netContribEarningsDetailsCollection;
	}
	

	/**
	 * Gets the participantFundsByRisk
	 * @return Returns an array
	 */
	public InvestmentOptionVO[] getParticipantFundsByRisk() {
		return participantFundsByRisk;
	}
	/**
	 * Sets the participantFundsByRiskCollection
	 * @param participantFundsByRiskCollection The participantFundsByRiskCollection to set
	 */
	public void setParticipantFundsByRisk(InvestmentOptionVO[] participantFundsByRisk) {
		this.participantFundsByRisk = participantFundsByRisk;
	}

	public boolean getHasInvestments()
	{
		int count = 0;
		
		for( int i = 0 ; i < participantFundsByRisk.length; i++)
		{
			// add up the number of funds invested in.
			count += participantFundsByRisk[i].getParticipantFundSummaryArray().length;
		}
		
		return count > 0 ? true:false;
	}

	
	
	/**
	 * @return Returns the employeeMoneyTypeTotals.
	 */
	public ParticipantFundMoneyTypeTotalsVO[] getEmployeeMoneyTypeTotals() {
		TreeSet listedMoneyTypes = new TreeSet(employeeMoneyTypeTotals.values());		
		return (ParticipantFundMoneyTypeTotalsVO[]) listedMoneyTypes.toArray(new ParticipantFundMoneyTypeTotalsVO[]{});
	}
	
	/**
	 * @return Returns the employerMoneyTypeTotals.
	 */
	public ParticipantFundMoneyTypeTotalsVO[] getEmployerMoneyTypeTotals() {
		TreeSet listedMoneyTypes = new TreeSet(employerMoneyTypeTotals.values());
		return (ParticipantFundMoneyTypeTotalsVO[]) listedMoneyTypes.toArray(new ParticipantFundMoneyTypeTotalsVO[]{});
	}


	
	/**
	 * @param moneyTypeName
	 * @param balance
	 * Add asset to the employeeMoneyTypeTotal to set.
	 */
	public void addEmployeeMoneyTypeAsset(String moneyTypeName, double balance) {
		ParticipantFundMoneyTypeTotalsVO ptVO =  null ;
		Object obj = employeeMoneyTypeTotals.get(moneyTypeName);
		if ( obj == null ) {
			ptVO = new ParticipantFundMoneyTypeTotalsVO(moneyTypeName, balance, 0.0);
			employeeMoneyTypeTotals.put(moneyTypeName, ptVO);
		} 
		else {
			ptVO = (ParticipantFundMoneyTypeTotalsVO)obj;
			ptVO.addBalance(balance);
		}
		
		totalEmployeeContributionsAssets += balance;
	}
	/**
	 * @param moneyTypeName
	 * @param loanBalance
	 * Add loan the employeeMoneyTypeTotal to set.
	 */
	public void addEmployeeMoneyTypeLoan(String moneyTypeName, double loanBalance) {
		ParticipantFundMoneyTypeTotalsVO ptVO =  null ;
		Object obj = employeeMoneyTypeTotals.get(moneyTypeName);
		if ( obj == null ) {
			ptVO = new ParticipantFundMoneyTypeTotalsVO(moneyTypeName, 0.0, loanBalance);
			employeeMoneyTypeTotals.put(moneyTypeName, ptVO);
		} 
		else {
			ptVO = (ParticipantFundMoneyTypeTotalsVO)obj;
			ptVO.addLoanBalance(loanBalance);
		}
		
		totalEmployeeContributionsLoanAssets += loanBalance;
	}
	
	
	/**
	 * @param moneyTypeName
	 * @param balance
	 * Add asset to the employerMoneyTypeTotal to set.
	 */
	public void addEmployerMoneyTypeAsset(String moneyTypeName, double balance) {
		ParticipantFundMoneyTypeTotalsVO ptVO =  null ;
		Object obj = employerMoneyTypeTotals.get(moneyTypeName);
		if ( obj == null ) {
			ptVO = new ParticipantFundMoneyTypeTotalsVO(moneyTypeName, balance, 0.0);
			employerMoneyTypeTotals.put(moneyTypeName, ptVO);
		} 
		else {
			ptVO = (ParticipantFundMoneyTypeTotalsVO)obj;
			ptVO.addBalance(balance);
		}
		
		totalEmployerContributionsAssets += balance;
	}
	/**
	 * @param moneyTypeName
	 * @param loanBalance
	 * Add loan the employerMoneyTypeTotal to set.
	 */
	public void addEmployerMoneyTypeLoan(String moneyTypeName, double loanBalance) {
		ParticipantFundMoneyTypeTotalsVO ptVO =  null ;
		Object obj = employerMoneyTypeTotals.get(moneyTypeName);
		if ( obj == null ) {
			ptVO = new ParticipantFundMoneyTypeTotalsVO(moneyTypeName, 0.0, loanBalance);
			employerMoneyTypeTotals.put(moneyTypeName, ptVO);
		} 
		else {
			ptVO = (ParticipantFundMoneyTypeTotalsVO)obj;
			ptVO.addLoanBalance(loanBalance);
		}
		
		totalEmployerContributionsLoanAssets += loanBalance;
	}	
	
	/**
	 * @return Returns the totalEmployeeContributionsAssets.
	 */
	public double getTotalEmployeeContributionsAssets() {
		return totalEmployeeContributionsAssets;
	}

	/**
	 * @return Returns the totalEmployeeContributionsLoanAssets.
	 */
	public double getTotalEmployeeContributionsLoanAssets() {
		return totalEmployeeContributionsLoanAssets;
	}

	/**
	 * @return Returns the totalEmployerContributionsAssets.
	 */
	public double getTotalEmployerContributionsAssets() {
		return totalEmployerContributionsAssets;
	}

	/**
	 * @return Returns the totalEmployerContributionsLoanAssets.
	 */
	public double getTotalEmployerContributionsLoanAssets() {
		return totalEmployerContributionsLoanAssets;
	}

	/**
	 * @return Returns the totalContributionsAssets.
	 */
	public double getTotalContributionsAssets() {
		return totalEmployerContributionsAssets + totalEmployeeContributionsAssets;
	}

	/**
	 * @return Returns the totalContributionsLoanAssets.
	 */
	public double getTotalContributionsLoanAssets() {
		return totalEmployerContributionsLoanAssets + totalEmployeeContributionsLoanAssets;
	}

	/**
	 * Returns the Funds in which the Participant has invested and organized 
	 * either by Investment Category or Asset Class
	 *  
	 * @return InvestmentOptionVO[]
	 */
	public InvestmentOptionVO[] getOrganizedParticipantFunds() {
		return organizedParticipantFunds;
	}

	/**
	 * Sets the Funds in which the Participant has invested and organized 
	 * either by Investment Category or Asset Class
	 *  
	 * @param InvestmentOptionVO[]
	 */
	public void setOrganizedParticipantFunds(InvestmentOptionVO[] organizedParticipantFunds) {
		this.organizedParticipantFunds = organizedParticipantFunds;
	}



	/**
	 * @return the rothMoneyTypeInd
	 */
	public boolean isRothMoneyTypeInd() {
		return rothMoneyTypeInd;
	}



	/**
	 * @param rothMoneyTypeInd the rothMoneyTypeInd to set
	 */
	public void setRothMoneyTypeInd(boolean rothMoneyTypeInd) {
		this.rothMoneyTypeInd = rothMoneyTypeInd;
	}



	/**
	 * @return the nonRothMoneyTypeInd
	 */
	public boolean isNonRothMoneyTypeInd() {
		return nonRothMoneyTypeInd;
	}



	/**
	 * @param nonRothMoneyTypeInd the nonRothMoneyTypeInd to set
	 */
	public void setNonRothMoneyTypeInd(boolean nonRothMoneyTypeInd) {
		this.nonRothMoneyTypeInd = nonRothMoneyTypeInd;
	}



	/**
	 * @return the rothMoneyTypeCount
	 */
	public int getRothMoneyTypeCount() {
		return rothMoneyTypeCount;
	}

	/**
	 * @param rothMoneyTypeCount the rothMoneyTypeCount to set
	 */
	public void setRothMoneyTypeCount(int rothMoneyTypeCount) {
		this.rothMoneyTypeCount = rothMoneyTypeCount;
	}
	



}