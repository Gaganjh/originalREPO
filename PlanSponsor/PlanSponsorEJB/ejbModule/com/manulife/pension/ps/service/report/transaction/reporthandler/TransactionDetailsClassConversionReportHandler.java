package com.manulife.pension.ps.service.report.transaction.reporthandler;
 
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.TransactionDetailsClassConversionDAO;
import com.manulife.pension.ps.service.report.transaction.dao.TransactionHistoryHelper;
import com.manulife.pension.ps.service.report.transaction.valueobject.ClassConversionDetailsFund;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsClassConversionReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsItem;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
 
public class TransactionDetailsClassConversionReportHandler extends AbstractTransactionReportHandler {
	
	
	private static Logger logger = Logger.getLogger(TransactionDetailsClassConversionReportHandler.class);

	public ReportData getReportData(ReportCriteria criteria)
			throws ReportServiceException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("enter <- PS TransactionDetailsClassConversionReportHandler.getReportData");
		}
						
  		String contractNumber = (String) criteria.getFilterValue(TransactionDetailsClassConversionReportData.FILTER_CONTRACT_NUMBER);
 		String participantId = (String) criteria.getFilterValue(TransactionDetailsClassConversionReportData.FILTER_PARTICIPANT_ID);
		
		if (participantId == null || contractNumber == null) {
			String errorText = "Missing Participant Id and/or Contract Number";
			logger.error(errorText);
			throw new EJBException(errorText);
		}
					
		TransactionDetailsClassConversionReportData reportData = new TransactionDetailsClassConversionReportData(criteria, 0);
		reportData = TransactionDetailsClassConversionDAO.getReportData(criteria);		
		reportData.setTransferFroms(populateFundGroupInfo(reportData.getTransferFroms(), reportData.getTransactionDate()));
		reportData.setTransferTos(populateFundGroupInfo(reportData.getTransferTos(), reportData.getTransactionDate()));
		reportData.setTransferFromsAndTos(populateFromsAndTos(reportData.getTransferFroms(), reportData.getTransferTos()));
	
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

	
	private List populateFromsAndTos(List transferFroms, List transferTos) {
		List fromsAndTosList = new ArrayList();

		if (transferFroms == null && transferTos == null) {
			return fromsAndTosList;
		}
		if (transferFroms == null && transferTos != null
				|| transferFroms != null && transferTos == null
				|| transferFroms.size() != transferTos.size()) {
			String errorMsg = "Transfer froms and tos groups mismatch";
			logger.error(errorMsg);
			throw new EJBException(errorMsg);
		}
		
		for (int i = 0; i < (transferFroms == null ? 0 : transferFroms.size()); i++ ) {
			FundGroup fromGroup = (FundGroup)transferFroms.get(i);
			Fund[] fromFunds = fromGroup.getFunds();			
			
			FundGroup toGroup = (FundGroup)transferTos.get(i);
			Fund[] toFunds = toGroup.getFunds();

			if (!fromGroup.getGroupName().trim().equals(toGroup.getGroupName().trim())) {
				String errorMsg = "Couldn't find matching to group for from group: " + fromGroup.getGroupName();
				logger.error(errorMsg);
				throw new EJBException(errorMsg);
			}
			if (fromFunds.length != toFunds.length) {
				String errorMsg = "Number of funds in group: " + fromGroup.getGroupName() + " didn't match";
				logger.error(errorMsg);
				throw new EJBException(errorMsg);
			}
			
			FundGroup fromAndToGroup = new FundGroup(fromGroup.getGroupName(), 
					fromGroup.getBackgroundColorCode(), fromGroup.getForegroundColorCode());
			for (int j=0; j < fromFunds.length; j++) {
				TransactionDetailsFund fromFund = (TransactionDetailsFund)fromFunds[j];
				TransactionDetailsFund toFund = (TransactionDetailsFund)toFunds[j];
				
				if (!fromFund.getId().trim().equals(toFund.getId().trim())) {
					String errorMsg = "From and to funds at array index " + j +" didn't match - from: " + fromFund.getId() + " != to: " + toFund.getId();
					logger.error(errorMsg);
					throw new EJBException(errorMsg);
				}
					
				ClassConversionDetailsFund classConversionFund 
					= new ClassConversionDetailsFund(fromFund.getId(),
				            fromFund.getName(),
				            fromFund.getType(),
				            fromFund.getMoneyTypeDescription(),
				            fromFund.getAmount(),
				            fromFund.getPercentage(),
				            fromFund.getUnitValue(),
				            fromFund.getNumberOfUnits(),
				            fromFund.getComments(),
				            fromFund.getSelectedFlag(),
				            0, //fromFund.getSortNumber(), this isn't public; don't need value anyway??
				            fromFund.getRiskCategoryCode(),
				            fromFund.getRatetype(),
				            toFund.getAmount(),
				            toFund.getUnitValue(),
				            toFund.getNumberOfUnits());
				fromAndToGroup.addFund(classConversionFund);
			}	
			fromsAndTosList.add(fromAndToGroup);
		}
		return fromsAndTosList;
	}


}

