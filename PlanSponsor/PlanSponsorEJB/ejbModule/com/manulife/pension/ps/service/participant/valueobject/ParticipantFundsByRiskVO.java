package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ParticipantFundsByRiskVO class
 * This class is used as a value object used on the Participant Account page,
 * @author Simona Stoicescu
 **/
public class ParticipantFundsByRiskVO implements Serializable {
	
	private String riskCategoryId;
	private String riskCategoryName;
	
	private Collection participantFundsSummaries;

    /**
     * Contructors
     */
	public ParticipantFundsByRiskVO() {
	} 
    
	public ParticipantFundsByRiskVO(String riskCategoryId, String riskCategoryName) {
		this.riskCategoryId = riskCategoryId;
		this.riskCategoryName = riskCategoryName;
		participantFundsSummaries = new ArrayList();
	} 
	
	/**
	 * Adds a ParticipantFund object to the collection of participantFunds
	 */
	public void addParticipantFundSummary(ParticipantFundSummaryVO participantFundSummaryVO){
		if (participantFundSummaryVO != null) {
			participantFundsSummaries.add(participantFundSummaryVO);
		}
	}

	public void addParticipantFundSummary(String fundId, 
										  String fundName,
										  double fundTotalNumberOfUnitsHeld,
										  double fundTotalCompositeRate,
											double fundTotalBalance,
											double fundTotalPercentageOfTotal,
											double fundTotalOngoingContributions,
											double employeeNumberOfUnitsHeld,
											double employeeCompositeRate,
											double employeeBalance,
											double employerNumberOfUnitsHeld,
											double employerCompositeRate,
											double employerBalance) {

		participantFundsSummaries.add( new ParticipantFundSummaryVO(fundId, 
																	fundName, 
																	fundTotalNumberOfUnitsHeld, 
																	fundTotalCompositeRate,
																	fundTotalBalance, 
																	fundTotalPercentageOfTotal,
																	fundTotalOngoingContributions, 
																	employeeNumberOfUnitsHeld, 
																	employeeCompositeRate, 
																	employeeBalance, 
																	employerNumberOfUnitsHeld, 
																	employerCompositeRate,
																	employerBalance));
	}

	/**
	 * Gets the participantFundsSummaries
	 * @return Returns a Collection
	 */
	public Collection getParticipantFundsSummaries() {
		return participantFundsSummaries;
	}

	/**
	 * Sets the participantFundsSummaries
	 * @param participantFundsSummaries The participantFundsSummaries to set
	 */
	public void setParticipantFundsSummaries(Collection participantFundsSummaries) {
		this.participantFundsSummaries = participantFundsSummaries;
	}
}
