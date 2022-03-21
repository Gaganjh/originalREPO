package com.manulife.pension.ps.service.report.census.valueobject;
import java.util.Date;

import com.manulife.pension.ps.service.report.census.reporthandler.EmployeeOptOutSummaryReportHandler;
import com.manulife.pension.ps.service.report.census.reporthandler.EmployeeStatusHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author patuadr
 *
 */
public class EmployeeStatusHistoryReportData extends ReportData {

    private static final long serialVersionUID = 6410615538967753611L;

    public static final String REPORT_ID = EmployeeStatusHistoryReportHandler.class.getName();
    
    public static final String OPT_OUT_REPORT_ID = EmployeeOptOutSummaryReportHandler.class.getName();

    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
    public static final String FILTER_PROFILE_ID= "profileId";
    public static final String FILTER_EXTERNAL= "external";
    
    public static final String DEFAULT_SORT = "effectiveDate";    
    public static final String EFFECTIVE_DATE_COLUMN = "EFFECTIVE_DATE";   
    public static final String STATUS_COLUMN = "STATUS";
    public static final String LAST_UPDATED_USER_ID_COLUMN = "LAST_UPDATED_USER_ID";
    public static final String LAST_UPDATED_TS_COLUMN = "LAST_UPDATED_TS";
    public static final String LAST_UPDATED_USER_ID_TYPE_COLUMN = "LAST_UPDATED_USER_ID_TYPE";
    public static final String SOURCE_CHANNEL_CODE_COLUMN = "SOURCE_CHANNEL_CODE";

    public static final String EFFECTIVE_DATE_FIELD = "effectiveDate";   
    public static final String STATUS_FIELD = "status";
    public static final String LAST_UPDATED_USER_ID_FIELD = "lastUpdatedUserId";
    public static final String LAST_UPDATED_TS_FIELD = "lastUpdatedTs";
    public static final String LAST_UPDATED_USER_ID_TYPE_FIELD = "lastUpdatedUserType";
    public static final String SOURCE_CHANNEL_CODE_FIELD ="sourceChannelCode";
    public static final String STATUS_CANCELLED ="C";
  
    private int numberOfRecords;
    private int numberOfCancelled;
    private int contractNumber;
    private long profileId;
    private Date employeeHireDate;
    private Date employeeBirthDate;
 
    
    public EmployeeStatusHistoryReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);
    }
    
  
	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfCancelled(int numberOfCancelled) {
		this.numberOfCancelled = numberOfCancelled;
	}

	public int getNumberOfCancelled() {
		return numberOfCancelled;
	}


	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}


	public int getContractNumber() {
		return contractNumber;
	}


	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}


	public long getProfileId() {
		return profileId;
	}


	public void setEmployeeHireDate(Date employeeHireDate) {
		this.employeeHireDate = employeeHireDate;
	}


	public Date getEmployeeHireDate() {
		return employeeHireDate;
	}


	public void setEmployeeBirthDate(Date employeeBirthDate) {
		this.employeeBirthDate = employeeBirthDate;
	}


	public Date getEmployeeBirthDate() {
		return employeeBirthDate;
	}


}

