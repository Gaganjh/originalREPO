package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.manulife.pension.ps.service.report.transaction.reporthandler.ContributionTransactionReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class will hold the Report Data from Contribution Transaction report.
 * 
 * @author harlomte
 * 
 */
public class ContributionTransactionReportData extends ReportData {

    private static final long serialVersionUID = 6840577004613960957L;

    public static final String REPORT_ID = ContributionTransactionReportHandler.class.getName();

	public static final String REPORT_NAME = "contributionTransactionReport";

	public static final String SORT_FIELD_NAME = "name";

	public static final String SORT_FIELD_SSN = "ssn";

	public static final String SORT_FIELD_EMPLOYEE_CONTRIBUTION = "employeeContrib";

	public static final String SORT_FIELD_EMPLOYER_CONTRIBUTION = "employerContrib";

	public static final String SORT_FIELD_TOTAL_CONTRIBUTION = "totalContrib";

	public static final String FILTER_TRANSACTION_NUMBER = "transactionNumber";

	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	
	public static final String FILTER_REPORT_TYPE = "reportType";
	public static final String FILTER_REPORT_TYPE_DOWNLOAD = "download";
	public static final String FILTER_REPORT_TYPE_PAGE = "page";

	private String transactionType;
	private String contractNumber;
	private BigDecimal totalEmployeeContribution;
	private BigDecimal totalEmployerContribution;
	private Date payrollEndingDate;
	private Date transactionDate;
	private String transactionNumber;
	private int numberOfParticipants;
	private List<MoneyTypeAmount> moneyTypes;
	private boolean hasEmployerContribution;
	private boolean hasEmployeeContribution;

	/**
	 * An inner class for a MoneyType. It contains a description and an amount.
	 *  
	 */
	public static class MoneyTypeAmount implements Serializable {

        private String id;
		private String shortDescription;
		private String longDescription;
        private boolean employeeContribution;
		private BigDecimal amount;

		public MoneyTypeAmount(
		        String id,
		        String shortDescription,
		        String longDescription,
                boolean employeeContribution,
		        BigDecimal amount) {
		    this.id = id;
			this.shortDescription = shortDescription;
			this.longDescription = longDescription;
            this.employeeContribution = employeeContribution;
			this.amount = amount;
		}

		/**
		 * @return Returns the amount.
		 */
		public BigDecimal getAmount() {
			return amount;
		}
		
		public String getId() {
		    return id;
		}

		/**
		 * @return Returns the description.
		 */
		public String getShortDescription() {
			return shortDescription;
		}
		
		public String getLongDescription() {
		    return longDescription;
		}

		/**
		 * @return Returns the employeeContribution.
		 */
		public boolean isEmployeeContribution() {
			return employeeContribution;
		}

		/**
		 * @return Returns the negated employeeContribution.
		 */
		public boolean isEmployerContribution() {
			return !employeeContribution;
		}
		
		public boolean equals(Object other) {
		    MoneyTypeAmount that = (MoneyTypeAmount) other;
		    return
		    this.id.equals(that.id)
		    && this.shortDescription.equals(that.shortDescription)
		    && this.longDescription.equals(that.longDescription)
		    && this.amount.equals(that.amount)
		    && this.employeeContribution == that.employeeContribution;
		}
	}
	
	public static class ParticipantMoneyTypeAllocation implements Serializable {
	    
	    private String id;
	    private boolean isEmployeeContribution;
	    private BigDecimal amount;
	    
	    public ParticipantMoneyTypeAllocation(String id, boolean isEmployeeContribution, BigDecimal amount) {
	        this.id = id;
	        this.isEmployeeContribution = isEmployeeContribution;
	        this.amount = amount;
	    }
	    
	    public String getId() {
	        return id;
	    }
	    
	    public boolean isEmployeeContribution() {
	        return isEmployeeContribution;
	    }
	    
	    public boolean isEmployerContribution() {
	        return ! isEmployeeContribution;
	    }
	    
	    public BigDecimal getAmount() {
	        return amount;
	    }
	    
	}

	/**
	 * Constructor.
	 */
	public ContributionTransactionReportData() {
		super();
		moneyTypes = new ArrayList<MoneyTypeAmount>();
	}

	/**
	 * Constructor.
	 * 
	 * @param criteria
	 * @param totalCount
	 */
	public ContributionTransactionReportData(ReportCriteria criteria,
			int totalCount) {
		super(criteria, totalCount);
		moneyTypes = new ArrayList<MoneyTypeAmount>();
	}

	/**
	 * @return Returns the transactionType.
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            The transactionType to set.
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Gets the total contribution (sum of total employee contribution and total
	 * employee contribution).
	 * 
	 * @return The total contribution.
	 */
	public BigDecimal getTotalContribution() {
	    BigDecimal totalContribution = BigDecimal.ZERO;
	    if (getTotalEmployeeContribution() != null) {
	        if (getTotalEmployerContribution() != null) {
	            totalContribution = getTotalEmployeeContribution().add(getTotalEmployerContribution());
	        } else {
	            totalContribution = getTotalEmployeeContribution();
	        }
	    } else if (getTotalEmployerContribution() != null) {
	        totalContribution = getTotalEmployerContribution();
	    }
	    return totalContribution;
	}

	/**
	 * @return Returns the numberOfParticipants.
	 */
	public int getNumberOfParticipants() {
		return numberOfParticipants;
	}

	/**
	 * @param numberOfParticipants
	 *            The numberOfParticipants to set.
	 */
	public void setNumberOfParticipants(int numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	/**
	 * @return Returns the payrollEndingDate.
	 */
	public Date getPayrollEndingDate() {
		return payrollEndingDate;
	}

	/**
	 * @param payrollEndingDate
	 *            The payrollEndingDate to set.
	 */
	public void setPayrollEndingDate(Date payrollEndingDate) {
		this.payrollEndingDate = payrollEndingDate;
	}

	/**
	 * @return Returns the totalEmployeeContribution.
	 */
	public BigDecimal getTotalEmployeeContribution() {
        return totalEmployeeContribution;
	}

	/**
	 * @param totalEmployeeContribution
	 *            The totalEmployeeContribution to set.
	 */
	public void setTotalEmployeeContribution(
			BigDecimal totalEmployeeContribution) {
		this.totalEmployeeContribution = totalEmployeeContribution;
	}

	/**
	 * @return Returns the totalEmployerContribution.
	 */
	public BigDecimal getTotalEmployerContribution() {
        return totalEmployerContribution;
	}

	/**
	 * @param totalEmployerContribution
	 *            The totalEmployerContribution to set.
	 */
	public void setTotalEmployerContribution(
			BigDecimal totalEmployerContribution) {
		this.totalEmployerContribution = totalEmployerContribution;
	}

	/**
	 * @return Returns the transactionDate.
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            The transactionDate to set.
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return Returns the transactionNumber.
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param transactionNumber
	 *            The transactionNumber to set.
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @return Returns the moneyTypes.
	 */
	public List<MoneyTypeAmount> getMoneyTypes() {
		return moneyTypes;
	}

	/**
	 * @param moneyTypes
	 *            The moneyTypes to set.
	 */
	public void setMoneyTypes(List<MoneyTypeAmount> moneyTypes) {
		this.moneyTypes = moneyTypes;
	}

	/**
	 * @return Returns the hasEmployerContribution.
	 */
	public boolean isHasEmployerContribution() {
		return hasEmployerContribution;
	}

	/**
	 * @param hasEmployerContribution The hasEmployerContribution to set.
	 */
	public void setHasEmployerContribution(boolean hasEmployerContribution) {
		this.hasEmployerContribution = hasEmployerContribution;
	}

	/**
	 * @return Returns the hasEmployeeContribution.
	 */
	public boolean isHasEmployeeContribution() {
		return hasEmployeeContribution;
	}

	/**
	 * @param hasEmployeeContribution The hasEmployeeContribution to set.
	 */
	public void setHasEmployeeContribution(boolean hasEmployeeContribution) {
		this.hasEmployeeContribution = hasEmployeeContribution;
	}
	/**
	 * @return Returns the ContractNumber.
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param ContractNumber The ContractNumber to set.
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
}