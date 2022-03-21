package com.manulife.pension.ps.service.report.census.valueobject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.report.census.reporthandler.CensusVestingReportHandler;
import com.manulife.pension.ps.service.report.contract.reporthandler.VestingTemplateReportHandler;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.vesting.VestingConstants;

public class CensusVestingReportData extends ReportData {

    private static final long serialVersionUID = -1590411619352751433L;

    public static final String REPORT_ID = CensusVestingReportHandler.class.getName();
    public static final String REPORT_NAME = "censusVestingReport";
    
    public static final String TEMPLATE_REPORT_ID = VestingTemplateReportHandler.class.getName();
    public static final String TEMPLATE_REPORT_NAME = "vestingTemplateReport";
    
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_STATUS = "status";
	public static final String FILTER_LAST_NAME = "lastName";
	public static final String FILTER_SSN = "ssn";
    public static final String FILTER_DIVISION = "division";
    public static final String FILTER_SEGMENT = "segment";
    public static final String FILTER_ASOFDATE = "asOfDate";
    public static final String FILTER_PRODUCTID = "productId";
    
    
	public static final String DEFAULT_SORT = "lastName";
    
    public static final String SORT_LAST_NAME = "lastName";
    public static final String SORT_BIRTH_DATE = "birthDate";
    public static final String SORT_HIRE_DATE = "hireDate";
    public static final String SORT_STATUS = "status";
    public static final String SORT_DIVISION = "division";
    public static final String SORT_SSN = "ssn";
    public static final String SORT_EMPLOYEE_ID = "empId";
    
    public static final int    PAGE_SIZE = 35; 
    
    public static final String DATE_FORMAT = "MMddyyyy";
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);
    
    
    private int contractNumber;
    private String sortOptionCode = "EE"; 
    private String specialSortIndicator = "Yes";
    private List<MoneyTypeVO> moneyTypes;
    private List<String> columnLabels;
    private Date asOfDate;
    private String transactionNumber="vest.d";
    
    private String vestingServiceFeature;
    private String creditingMethod;

	public CensusVestingReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
    }

    public int getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }


    public String getSortOptionCode() {
        return sortOptionCode;
    }

    public void setSortOptionCode(String sortOptionCode) {
        this.sortOptionCode = sortOptionCode;
    }

    public String getSpecialSortIndicator() {
        return specialSortIndicator;
    }

    public void setSpecialSortIndicator(String specialSortIndicator) {
        this.specialSortIndicator = specialSortIndicator;
    }

    public List<MoneyTypeVO> getMoneyTypes() {
        return moneyTypes;
    }

    public void setMoneyTypes(List<MoneyTypeVO> moneyTypes) {
        this.moneyTypes = moneyTypes;
    }

    public List<String> getColumnLabels() {
        return columnLabels;
    }

    public void setColumnLabels(List<String> columnLabels) {
        this.columnLabels = columnLabels;
    }

    public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
    
    /**
     * @return Returns the date.
     */
    public String getFormattedDate() {
        if(getAsOfDate() == null) return "";
        String dateString = null;
        synchronized (FORMATTER) {
            dateString = FORMATTER.format(getAsOfDate());
        }
        return dateString;
    }

    public String getVestingServiceFeature() {
        return vestingServiceFeature;
    }

    public void setVestingServiceFeature(String vestingServiceFeature) {
        this.vestingServiceFeature = vestingServiceFeature;
    }

    public String getCreditingMethod() {
        return creditingMethod;
    }

    public void setCreditingMethod(String creditingMethod) {
        this.creditingMethod = creditingMethod;
    }
    
    /**
     * This method determines if the crediting method is currently set to unspecified. It also
     * returns true if the crediting method is unset (null/blank).
     * 
     * @return boolean - True if the crediting method is unspecified or blank, false otherwise.
     */
    public boolean getIsCreditingMethodUnspecified() {
        return StringUtils.isEmpty(this.creditingMethod)
                || StringUtils.equals(VestingConstants.CreditingMethod.UNSPECIFIED,
                        this.creditingMethod);
    }
    
}
