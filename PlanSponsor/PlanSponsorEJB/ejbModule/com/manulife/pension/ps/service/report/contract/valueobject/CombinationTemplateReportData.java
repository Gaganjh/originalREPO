package com.manulife.pension.ps.service.report.contract.valueobject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.manulife.pension.ps.service.report.contract.reporthandler.CombinationTemplateReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class is used for setting and getting the Report Data in CSV file Download.
 * @author Vishnu
 *
 */
public class CombinationTemplateReportData extends ReportData {

	public static String REPORT_ID = CombinationTemplateReportHandler.class.getName();
	public static String REPORT_NAME = "contributionTemplateReport"; 
	public static final String FILTER_FIELD_1 = "contractNumber";
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_STATUS = "status";
	public static final String FILTER_LAST_NAME = "lastName";
	public static final String FILTER_SSN = "ssn";
    public static final String FILTER_DIVISION = "division";
    public static final String FILTER_SEGMENT = "segment";
    public static final String DEFAULT_SORT = "lastName";
    public static final String SORT_LAST_NAME = "lastName";
    public static final String SORT_BIRTH_DATE = "birthDate";
    public static final String SORT_HIRE_DATE = "hireDate";
    public static final String SORT_STATUS = "status";
    public static final String SORT_DIVISION = "division";
    public static final int    PAGE_SIZE = 35; 
	public static final String DATE_FORMAT = "MMddyyyy";
	public static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	private int contractNumber;
	private String transactionNumber;
	private Date date;
	private int maxLoanCount;
	private List columnLabels;
    private String EDTSortOption = "EE"; 
	private String specialSortIndicator = "Yes";
	
	public CombinationTemplateReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * Gets the contractNumber
	 * @return Returns a String
	 */
	public int getContractNumber() {
		return contractNumber;
	}

	/**
	 * Sets the contractNumber
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}
	/**
	 * @return Returns the date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return Returns the date.
	 */
	public String getFormattedDate() {
		if(getDate() == null) return "";
		String dateString = null;
		synchronized (FORMATTER) {
			dateString = FORMATTER.format(getDate());
		}
		return dateString;
	}

	/**
	 * @param date The date to set.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return Returns the transactionNumber.
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param transactionNumber The transactionNumber to set.
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @return Returns the columnLabels.
	 */
	public List getColumnLabels() {
		return columnLabels;
	}

	/**
	 * @param columnLabels The columnLabels to set.
	 */
	public void setColumnLabels(List columnLabels) {
		this.columnLabels = columnLabels;
	}

	/**
	 * @return Returns the maxLoanCount.
	 */
	public int getMaxLoanCount() {
		return maxLoanCount;
	}

	/**
	 * @param maxLoanCount The maxLoanCount to set.
	 */
	public void setMaxLoanCount(int maxLoanCount) {
		this.maxLoanCount = maxLoanCount;
	}
	/**
	 * 
	 * @return EDTSortOption;
	 */
    public String getEDTSortOption() {
        return EDTSortOption;
    }

    /**
     * 
     * @param sortOption 
     */
    public void setEDTSortOption(String sortOption) {
        EDTSortOption = sortOption;
    }
    
    /**
     * 
     * @return Special Sort Indicator.
     */
    public String getSpecialSortIndicator() {
        return specialSortIndicator;
    }

    /**
     * Set the Special SOrt Indicator.
     * @param specialSortIndicator
     */
    public void setSpecialSortIndicator(String specialSortIndicator) {
        this.specialSortIndicator = specialSortIndicator;
    }

    /*
     * (non-Javadoc)
     * @see com.manulife.pension.service.report.valueobject.ReportData#toString()
     */
	public String toString() {
		return super.toString() + "\ncontractNumber: " + getContractNumber() + "\ntransactionNumber: " +
				getTransactionNumber() + "\ndate: " + getFormattedDate() + "\nmaxLoanCount: " + getMaxLoanCount();
	}	
}
