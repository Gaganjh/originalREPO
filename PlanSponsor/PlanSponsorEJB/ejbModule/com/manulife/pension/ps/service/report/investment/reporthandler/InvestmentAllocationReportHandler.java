package com.manulife.pension.ps.service.report.investment.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.ps.service.report.investment.dao.InvestmentAllocationDAO;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class InvestmentAllocationReportHandler implements ReportHandler{
	
	public ReportData getReportData(ReportCriteria criteria) throws SystemException { 

		int contractNumber = 0;
		contractNumber = ((Integer)criteria.getFilterValue(InvestmentAllocationReportData.FILTER_CONTRACT_NO)).intValue();
   		InvestmentAllocationDAO dao = new InvestmentAllocationDAO(); 
       	InvestmentAllocationReportData data =  (InvestmentAllocationReportData)dao.getReportData(criteria); 
       	data.setContractDates(EnvironmentServiceHelper.getInstance().getContractDates(contractNumber));
       	return data;
	} 

}

