package com.manulife.pension.ps.service.report.census.reporthandler;

import java.util.List;

import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.CensusSummaryDAO;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryReportData;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.BaseEnvironment;

/**
 * This handler class used to generate report data for beneficiary download report.
 * 
 * @author Vellaisamy S
 *
 */
public class BeneficiaryReportHandler implements ReportHandler {

	
	/**
	 * Method used to get the beneficiary download report data.
	 * 
	 * @param reportCriteria
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
		
		BaseEnvironment environment = new BaseEnvironment();
		BeneficiaryReportData beneficiaryReportData = null;
		
		int contractNumber = (new Integer((String) reportCriteria.getFilterValue(
                CensusSummaryReportData.FILTER_CONTRACT_NUMBER))).intValue();
		
		List<EmployeeData> employeeDataList = CensusSummaryDAO.getEmployeeDetails(reportCriteria, false);
		
		if(employeeDataList != null && employeeDataList.size() > 0) {
			
			beneficiaryReportData = EmployeeServiceDelegate.
			getInstance(environment.getApplicationId()).getBeneficiaryReportData(employeeDataList, contractNumber);
		}
		
		return beneficiaryReportData;
	}

}
