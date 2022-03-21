package com.manulife.pension.ps.service.report.tpadownload.valueobject;

import java.io.Serializable;
import java.util.Date;

import com.manulife.pension.util.StaticHelperClass;
import com.manulife.util.render.DateRender;

public class EStatementsItem implements Serializable {
	
	public final static String STATUS_YES = "Y";
	public final static String STATUS_NO = "N";
	
	public final static String CORRECTED_STATUS= "C";
	public final static String REGULAR_STATUS = "R";	
	
	public static final String STATUS_YES_DESC = "Yes";
	public static final String STATUS_NO_DESC = "No";
	
	private static final String UNDER_SCORE = "_";
	private static final String KEY_DATE_FORMAT = "yyyyMMdd";
	private static final String PDF_FILENAME_EXTENSION = ".pdf";		
	
	public final static String STATEMENT_TYPE_PLAN_ADMINISTRATOR = "PA";
	public final static String STATEMENT_TYPE_EMPLOYER_FINANCIAL= "EF";	
	public final static String STATEMENT_TYPE_SCHEDULE_A = "SA";	
	public final static String STATEMENT_TYPE_SCHEDULE_C = "SC"; // Schedule C (Added for FeeDisclosure)
	/* Audit on Web */
	public final static String STATEMENT_TYPE_AUDIT_SUMMARY = "AS";	
	public final static String STATEMENT_TYPE_AUDIT_CERTIFICATION = "CL";
    public final static String STATEMENT_TYPE_BENEFIT_PAYMENT = "BP";    
	public static final String STATEMENT_TYPE_EMPLOYER_FINANCIAL_TEXT = "Employer Financial";
	public static final String STATEMENT_TYPE_PLAN_ADMINISTRATOR_TEXT = "Plan Administrator";
	public static final String STATEMENT_TYPE_SCHEDULE_A_TEXT = "Schedule A Report";	
	public static final String STATEMENT_TYPE_SCHEDULE_C_TEXT = "Schedule C Report";// Schedule C (Added for FeeDisclosure)
	/* Audit on Web */
	public static final String STATEMENT_TYPE_AUDIT_SUMMARY_TEXT = "Audit Summary Report";	
	public static final String STATEMENT_TYPE_AUDIT_CERTIFICATION_TEXT = "Audit Certification";
    public static final String STATEMENT_TYPE_BENEFIT_PAYMENT_TEXT = "Benefit Payment";           
	public static final String STATEMENT_TYPE_ALL_TEXT = "All";	
    public static final String CONTRACT_TYPE_DEFINED_BENEFIT = "Defined Benefit"; 
    public static final String CONTRACT_TYPE_DEFINED_CONTRIBUTION = "Defined Contribution"; 

	public static final String SORT_FIELD_CONTRACT_NAME = "contractName";
	public static final String SORT_FIELD_CONTRACT_NUMBER = "contractNumber";
	public static final String SORT_FIELD_STATEMENT_TYPE = "statementType";	
	public static final String SORT_FIELD_PERIOD_END_DATE = "periodEndDate";
	public static final String SORT_FIELD_PRODUCED_DATE = "producedDate";
	public static final String SORT_FIELD_CORRECTED = "corrected";	
	public static final String SORT_FIELD_YEAR_END = "yearEnd";
    public static final String SORT_FIELD_CONTRACT_TYPE = "contractType";
	
	private String contractNumber;
	private String contractName;
	private String statementType;
	private String statementTypeCode;	
	private Date periodEndDate;
	private Date producedDate;
	private String corrected;
	private String correctedCode;	
	private String yearEnd;
	private String clientId;
    private String contractType = "DC";
	
	public EStatementsItem(String contractNumber,
						   String contractName,
						   String statementTypeCode,
						   Date periodEndDate,
						   Date producedDate,
						   String correctedCode,
						   String yearEnd,
						   String clientId,
                           String contractType)
	{
		this.contractNumber = contractNumber;
		this.contractName = contractName;
		setStatementTypeCode(statementTypeCode.trim());
		this.periodEndDate = periodEndDate;
		this.producedDate = producedDate;
		setCorrectedCode(correctedCode);
		setYearEnd(yearEnd);
		this.clientId = clientId;
        this.contractType = contractType;
	}
	
	/**
	 * @return Returns the contractName.
	 */
	public String getContractName() {
		return contractName;
	}
	/**
	 * @param contractName The contractName to set.
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	/**
	 * @return Returns the contractNumber.
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param contractNumber The contractNumber to set.
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	/**
	 * @return Returns the corrected.
	 */
	public String getCorrected() {
		return corrected;
	}
	/**
	 * @param correctedCode The correctedCode to set.
	 */
	public void setCorrectedCode(String correctedCode) {
		this.correctedCode = correctedCode;
		if (correctedCode.equalsIgnoreCase(CORRECTED_STATUS))
			this.corrected = STATUS_YES_DESC;
		else if (correctedCode.equalsIgnoreCase(REGULAR_STATUS))
			this.corrected= STATUS_NO_DESC;
	}

	/**
	 * @return Returns the periodEndDate.
	 */
	public Date getPeriodEndDate() {
		return periodEndDate;
	}
	/**
	 * @param periodEndDate The periodEndDate to set.
	 */
	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}
	/**
	 * @return Returns the producedDate.
	 */
	public Date getProducedDate() {
		return producedDate;
	}
	/**
	 * @param producedDate The producedDate to set.
	 */
	public void setProducedDate(Date producedDate) {
		this.producedDate = producedDate;
	}
	/**
	 * @return Returns the statementType.
	 */
	public String getStatementType() {
		return statementType;
	}
	
	/**
	 * @return Returns the statementTypeCode.
	 */
	public String getStatementTypeCode() {
		return statementTypeCode;
	}
	
	/**
	 * @param statementType The statementType to set.
	 */
	public void setStatementTypeCode(String statementTypeCode) {
		this.statementTypeCode = statementTypeCode;
		if ( STATEMENT_TYPE_EMPLOYER_FINANCIAL.equals(statementTypeCode) )
			this.statementType = STATEMENT_TYPE_EMPLOYER_FINANCIAL_TEXT;
		else if ( STATEMENT_TYPE_PLAN_ADMINISTRATOR.equals(statementTypeCode) )
			this.statementType = STATEMENT_TYPE_PLAN_ADMINISTRATOR_TEXT;
		else if ( STATEMENT_TYPE_SCHEDULE_A.equals(statementTypeCode) )
			this.statementType = STATEMENT_TYPE_SCHEDULE_A_TEXT;
		else if ( STATEMENT_TYPE_SCHEDULE_C.equals(statementTypeCode) )
			this.statementType = STATEMENT_TYPE_SCHEDULE_C_TEXT;
        else if ( STATEMENT_TYPE_BENEFIT_PAYMENT.equals(statementTypeCode) )
            this.statementType = STATEMENT_TYPE_BENEFIT_PAYMENT_TEXT;
        else if ( STATEMENT_TYPE_AUDIT_SUMMARY.equals(statementTypeCode) )
            this.statementType = STATEMENT_TYPE_AUDIT_SUMMARY_TEXT;
        else if ( STATEMENT_TYPE_AUDIT_CERTIFICATION.equals(statementTypeCode) )
            this.statementType = STATEMENT_TYPE_AUDIT_CERTIFICATION_TEXT;
	}
	/**
	 * @return Returns the yearEnd.
	 */
	public String getYearEnd() {
		return yearEnd;
	}
	/**
	 * @param yearEnd The yearEnd to set.
	 */
	public void setYearEnd(String yearEnd) {
		if (yearEnd.equalsIgnoreCase(STATUS_YES))
			this.yearEnd = STATUS_YES_DESC;
		else 
			this.yearEnd= STATUS_NO_DESC;
	}
	
	public String getClientIdAndStatementFileName() {
		StringBuffer clientIdAndfileName = new StringBuffer();
		clientIdAndfileName.append(clientId).append(":");
		clientIdAndfileName.append(contractNumber).append(UNDER_SCORE);
		clientIdAndfileName.append(DateRender.formatByPattern(periodEndDate, "", KEY_DATE_FORMAT));
		if ( STATUS_YES_DESC.equals(yearEnd) ) 
			clientIdAndfileName.append(STATUS_YES);
		
		clientIdAndfileName.append(UNDER_SCORE);
		
		clientIdAndfileName.append(statementTypeCode).append(UNDER_SCORE);
		clientIdAndfileName.append(DateRender.formatByPattern(producedDate, "", KEY_DATE_FORMAT)).append(UNDER_SCORE);		
		clientIdAndfileName.append(correctedCode).append(PDF_FILENAME_EXTENSION);
		return clientIdAndfileName.toString();
	}
	
	public String toString()
	{
		return StaticHelperClass.toString(this);
	}
	
	
	/**
	 * @return Returns the clientId.
	 */
	public String getClientId() {
		return clientId;
	}
	/**
	 * @param clientId The clientId to set.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

    public String getContractType() {
        return contractType;
    }
    
    public String getContractTypeText() {
        return (contractType.equalsIgnoreCase("DC")?  CONTRACT_TYPE_DEFINED_CONTRIBUTION : CONTRACT_TYPE_DEFINED_BENEFIT);
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
}

