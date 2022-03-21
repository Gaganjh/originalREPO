package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.ParticipantMoneyTypeAllocation;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;

/**
 * @author Charles Chan
 */
public class ContributionTransactionItem implements Serializable {

    private ParticipantVO participant;
    private List<ParticipantMoneyTypeAllocation> allocations;
    
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	private BigDecimal totalContribution;

	public ContributionTransactionItem() {
		participant = new ParticipantVO();
		allocations = new ArrayList<ParticipantMoneyTypeAllocation>();
	}
	
	/**
	 * @return Returns the employeeContribution.
	 */
	public BigDecimal getEmployeeContribution() {
        if (employeeContribution == null) {
            BigDecimal sum = BigDecimal.ZERO;
            for (ParticipantMoneyTypeAllocation allocation : getAllocations()) {
                if (allocation.isEmployeeContribution()) {
                    sum = sum.add(allocation.getAmount());
                }
            }
            employeeContribution = sum;
        }
        return employeeContribution;
	}

	/**
	 * @param employeeContribution The employeeContribution to set.
	 */
	public void setEmployeeContribution(BigDecimal employeeContribution) {
		this.employeeContribution = employeeContribution;
	}

	/**
	 * @return Returns the employerContribution.
	 */
	public BigDecimal getEmployerContribution() {
        if (employerContribution == null) {
            BigDecimal sum = BigDecimal.ZERO;
            for (ParticipantMoneyTypeAllocation allocation : getAllocations()) {
                if (allocation.isEmployerContribution()) {
                    sum = sum.add(allocation.getAmount());
                }
            }
            employerContribution = sum;
        }
        return employerContribution;
	}

	/**
	 * @param employerContribution The employerContribution to set.
	 */
	public void setEmployerContribution(BigDecimal employerContribution) {
		this.employerContribution = employerContribution;
	}

	/**
	 * @return Returns the participant.
	 */
	public ParticipantVO getParticipant() {
		return participant;
	}

	/**
	 * @param participant The participant to set.
	 */
	public void setParticipant(ParticipantVO participant) {
		this.participant = participant;
	}

	/**
	 * @return Returns the totalContribution.
	 */
	public BigDecimal getTotalContribution() {
        if (totalContribution == null) {
            totalContribution = getEmployeeContribution().add(getEmployerContribution());
        }
	    return totalContribution;
	}

	/**
	 * @param totalContribution The totalContribution to set.
	 */
	public void setTotalContribution(BigDecimal totalContribution) {
		this.totalContribution = totalContribution;
	}
	
	public void addAllocation(
	        String moneyTypeId,
	        boolean isEmployeeContribution,
	        BigDecimal amount) {
	    
	    allocations.add(
	            new ParticipantMoneyTypeAllocation(
	                    moneyTypeId,
	                    isEmployeeContribution,
	                    amount));
	    
	}
	
	public List<ParticipantMoneyTypeAllocation> getAllocations() {
	    return Collections.unmodifiableList(allocations);
	}
	
	public void setAllocations(List<ParticipantMoneyTypeAllocation> allocations) {
	    this.allocations = allocations;
	}
}

