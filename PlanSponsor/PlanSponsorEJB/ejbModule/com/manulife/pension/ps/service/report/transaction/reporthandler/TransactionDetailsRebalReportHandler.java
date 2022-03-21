package com.manulife.pension.ps.service.report.transaction.reporthandler;
 
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.TransactionDetailsRebalDAO;
import com.manulife.pension.ps.service.report.transaction.dao.TransactionHistoryHelper;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsRebalReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
 
public class TransactionDetailsRebalReportHandler extends AbstractTransactionReportHandler {
	
	private static Logger logger = Logger.getLogger(TransactionDetailsRebalReportHandler.class);
	private static boolean testMode = true;

	public ReportData getReportData(ReportCriteria criteria)
		throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("enter <- getReportData");
		}
						
 		String participantId = (String) criteria.getFilterValue(TransactionDetailsRebalReportData.FILTER_PARTICIPANT_ID);
 		String contractNumber = (String) criteria.getFilterValue(TransactionDetailsRebalReportData.FILTER_CONTRACT_NUMBER);
		
		if (participantId == null || contractNumber == null) {
			String errorText = "Missing Participant Id and/or Contract Number";
			logger.error(errorText);
			throw new EJBException(errorText);
		}
 				
		TransactionDetailsRebalReportData reportData = new TransactionDetailsRebalReportData(criteria, 0);

		long startMillis = System.currentTimeMillis(); 		
		reportData = TransactionDetailsRebalDAO.getReportData(criteria);		
		
		reportData.setBeforeChange(populateFundGroupInfo(reportData.getBeforeChange(), reportData.getTransactionDate()));
		reportData.setAfterChange(populateFundGroupInfo(reportData.getAfterChange(), reportData.getTransactionDate()));
		
  		BigDecimal totalMVA = new BigDecimal(0.00);
  		BigDecimal totalRedemptionFees = new BigDecimal(0.00);

 		/**
		 * calculate total Redemption fees and MVAs
		 */
		if (reportData.getDetails() != null && reportData.getDetails().size() > 0) {	
			Iterator it = reportData.getDetails().iterator();
			while (it.hasNext()) {
				TransactionDetailsItem item = (TransactionDetailsItem) it.next();
				if (TransactionHistoryHelper.isMva(item.getSubtype()))
					totalMVA = totalMVA.add(item.getAmount());
				else if (TransactionHistoryHelper.isRedemptionFees(item.getSubtype()))
					totalRedemptionFees = totalRedemptionFees.add(item.getAmount());
			}
		}
		// set MVA/redemption fees to absolute value
		reportData.setRedemptionFees(totalRedemptionFees.abs());
		reportData.setMva(totalMVA.abs());
		
 		/**
		 * now replace the details with the fund group info
		 */
 		if (reportData.getDetails() != null && reportData.getDetails().size() > 0) {	
			reportData.setDetails(populateFundGroupInfo(
									(List) reportData.getDetails(), 
									reportData.getTransactionDate()));
  		}
		return reportData;
	}
	

}

