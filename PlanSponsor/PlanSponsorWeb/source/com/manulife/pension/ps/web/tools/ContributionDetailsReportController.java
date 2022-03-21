package com.manulife.pension.ps.web.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.contract.valueobject.ContributionTemplateReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.NumberRender;

/**
 * This class models the action of retrieving and presenting a contribution 
 * template for a contract.
 * 
 * @author Jim Adamthwaite
 */
public class ContributionDetailsReportController extends ReportController {

	public static final NumberFormat FORMATTER = new DecimalFormat("00");
	public static final Integer ZERO = new Integer(0);
	public static final Integer NINETY_NINE = new Integer(99);
	
	
	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(ContributionTemplateReportData.FILTER_FIELD_1, new Integer(userProfile.getCurrentContract().getContractNumber()));
		criteria.addFilter(ContributionDetailsReportData.FILTER_FIELD_2, new Integer(request.getParameter("submissionId")));
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}	
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ContributionDetailsReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ContributionDetailsReportData.REPORT_NAME;
	}

	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		UserProfile userProfile = getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		//getting formatted date from request scope and appending as part of file name.
		//to fix the common log : 102699 
		Object attribute = request.getAttribute(Constants.FORMATTED_DATE);
		String formattedDate = attribute!=null? attribute.toString():"";
		//end common log fix
		return "Contribution_Template_for_" + String.valueOf(contractId) 
				+ "_for_" + formattedDate + CSV_EXTENSION;
	}

	protected String getDefaultSort() {
		return "name";
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#getPageSize(HttpServletRequest)
	 */
	protected int getPageSize(HttpServletRequest request) {
		return 35;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		
		FunctionalLogger.INSTANCE.log("Download from the tools page - Contribution File Template", request, getClass(), "getDownloadData");
		
		StringBuffer buffer = new StringBuffer();
		ContributionDetailsReportData reportData = 
				(ContributionDetailsReportData) report;
		
		// Fill in the header
		Iterator columnLabels = reportData.getColumnLabels().iterator();
		while (columnLabels.hasNext()) {
			buffer.append(columnLabels.next());
			if (columnLabels.hasNext()) {
				buffer.append(COMMA);
			}
		}

		String formattedDate = reportData.getFormattedDate();
		request.setAttribute(Constants.FORMATTED_DATE, formattedDate);
        Iterator items = report.getDetails().iterator();
		while (items.hasNext()) {
			buffer.append(LINE_BREAK);
			buffer.append(reportData.getTransactionNumber()).append(COMMA);
			buffer.append(reportData.getContractNumber()).append(COMMA);
			
			SubmissionParticipant participant = (SubmissionParticipant) items.next();
			buffer.append(participant.getIdentifier()).append(COMMA);
			buffer.append(QUOTE).append(participant.getName()).append(QUOTE).append(COMMA);

			buffer.append(formattedDate).append(COMMA);

			Map contributions = participant.getMoneyTypeAmounts();
			for (Iterator itr = reportData.getContributionData().getAllocationMoneyTypes().iterator();  itr.hasNext(); ){
				MoneyTypeHeader moneyType = (MoneyTypeHeader)itr.next();
				String moneyTypeKey = moneyType.getKey();
				BigDecimal contributionAmount = (BigDecimal)contributions.get(moneyTypeKey);
				if (null != contributionAmount) {
					String displayAmount = NumberRender.formatByType(contributionAmount, 
							"0.00", "d", BigDecimal.ROUND_UNNECESSARY);
					buffer.append(displayAmount);
				} 
				if (itr.hasNext()) {
					buffer.append(COMMA);
				}
			}	
			
			// Loans (commas pre-pended)
			Iterator loans = participant.getLoanAmounts().keySet().iterator();
			Map loanMap = participant.getLoanAmounts();
			while (loans.hasNext()) {
				String loanKey = (String)loans.next();
				int endIndex = loanKey.indexOf("/");
				Integer loanId = new Integer(loanKey.substring(0, endIndex));
				String formattedLoanId = "";
				if (loanId.compareTo(ZERO) != 0 && loanId.compareTo(NINETY_NINE) != 0) {
					synchronized (FORMATTER) {
						formattedLoanId = FORMATTER.format(loanId.longValue());
					}
				}	
				BigDecimal repaymentAmount = (BigDecimal)loanMap.get(loanKey);
				String displayAmount = NumberRender.formatByType(repaymentAmount, 
						"0.00", "d", BigDecimal.ROUND_UNNECESSARY);
				buffer.append(COMMA).append(formattedLoanId)
						.append(COMMA).append(displayAmount);
			}
			
			// Fill-in the rest of the columns until max no. of loans per contract
			int actualLoanCount = participant.getLoanAmounts().size();
			if (actualLoanCount < reportData.getMaxLoanCount()) {
				for (int i = 0; i < (reportData.getMaxLoanCount() - actualLoanCount); i++) {
					buffer.append(COMMA).append(COMMA);
				}
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}
				
		return buffer.toString().getBytes();
	}
}