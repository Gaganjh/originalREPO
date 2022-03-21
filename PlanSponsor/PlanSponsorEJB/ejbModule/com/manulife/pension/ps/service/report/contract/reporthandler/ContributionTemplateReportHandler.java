package com.manulife.pension.ps.service.report.contract.reporthandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.contract.valueobject.ContributionTemplateReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantContribution;
import com.manulife.pension.service.contract.dao.ContractDAO;
import com.manulife.pension.service.contract.valueobject.ContractContributionTemplateVO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.ParticipantWithLoansVO;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 *
 * @author Adrian Robitu
 *
 * Contribution template generator.
 *
 */
public class ContributionTemplateReportHandler implements ReportHandler {

	private static final String className = ContributionTemplateReportHandler.class.getName();

	// Labels
	public static final String TRANSACTION_LABEL = "Trans#";
	public static final String CONTRACT_LABEL = "Cont#";
	public static final String SSN_LABEL = "SSN#";
	public static final String EMPLOYEE_ID_LABEL = "Employee#";
	public static final String NAME_LABEL = "\"Participant Name (Lastname, Firstname)\"";
	public static final String DATE_LABEL = "Date (mmddyyyy)";
	public static final String LOAN_ID_LABEL = "LoanID";
	public static final String LOAN_AMOUNT_LABEL = "LoanAmt";

	public static final String TEMPLATE_TRANSACTION = "00000505";

	public static final String EMPLOYEE_ID_INDICATOR = "EE";

	public static final BigDecimal ZERO = new BigDecimal("0");

	/**
	 * Generates a contribution template for a given contract
	 */
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {

		int contractNumber =
				((Integer)criteria.getFilterValue(ContributionTemplateReportData.FILTER_FIELD_1)).intValue();
		ContributionTemplateReportData reportData = null;
		try {
			ContractContributionTemplateVO contributionData =
					ContractDAO.getContractContributionTemplate(contractNumber);

			reportData = setReportHeader(criteria, contractNumber, contributionData);

			SortedMap emptyContributionAmounts = generateEmptyContributions(contributionData);

			List details = new ArrayList();
			for (Iterator i = contributionData.getContributables().iterator(); i.hasNext();) {
				ParticipantWithLoansVO participantData = (ParticipantWithLoansVO) i.next();
				ParticipantContribution item = new ParticipantContribution(participantData.getSsn(),
						participantData.getFirstName(), participantData.getLastName(), emptyContributionAmounts,
						generateZeroLoanAmounts(participantData));
				details.add(item);
			}
			reportData.setDetails(details);

		} catch (DAOException e) {
			throw new SystemException(e, className, "getReportData",
					"DAO exception occurred while retrieving contribution. Input Paramereters are contractNumber: " + contractNumber);
		}
		return 	reportData;
	}

	/**
	 * Generates a map of zero amounts by loan ids, sorted by loan id.
	 *
	 * @param vo participant info from the DAO
	 * @return
	 */
	private SortedMap generateZeroLoanAmounts(ParticipantWithLoansVO participantData) {
		SortedMap zeroLoanAmounts = new TreeMap();
		for (Iterator i = participantData.getLoanIds().iterator(); i.hasNext();) {
			zeroLoanAmounts.put(i.next(), ZERO);
		}
		return zeroLoanAmounts;
	}

	/**
	 * Generates a map of null amounts by money type, sorted by money type.
	 *
	 * @param contributionData information from the DAO.
	 * @return
	 */
	private SortedMap generateEmptyContributions(ContractContributionTemplateVO contributionData) {
		SortedMap emptyContributionAmounts = new TreeMap();
		for (Iterator i = contributionData.getMoneyTypes().iterator(); i.hasNext();) {
			emptyContributionAmounts.put(i.next(), null);
		}
		return emptyContributionAmounts;
	}

	/**
	 * Sets the header information for this report.
	 *
	 * @param criteria
	 * @param contractNumber
	 * @param contributionData
	 * @return
	 */
	private ContributionTemplateReportData setReportHeader(ReportCriteria criteria, int contractNumber,
			ContractContributionTemplateVO contributionData) {

		int totalCount = contributionData.getContributables().size();
		ContributionTemplateReportData reportData = new ContributionTemplateReportData(criteria, totalCount);
		reportData.setContractNumber(contractNumber);
		reportData.setTransactionNumber(TEMPLATE_TRANSACTION);
		reportData.setDate(new Date());

		int maxLoanCount = contributionData.getMaxLoanCount();
		if (contributionData.isHasLoanFeature()) {
			maxLoanCount = Math.max(maxLoanCount, 1);
		}
		reportData.setMaxLoanCount(maxLoanCount);
		reportData.setColumnLabels(generateColumnLabels(contributionData, reportData, maxLoanCount));
		return reportData;
	}

	/**
	 * Generates the list of column labels for this report.
	 *
	 * @param contributionData
	 * @param reportData
	 */
	private List generateColumnLabels(ContractContributionTemplateVO contributionData,
				ContributionTemplateReportData reportData, int maxLoanCount) {

		List columnLabels =  new ArrayList();
		columnLabels.add(TRANSACTION_LABEL);
		columnLabels.add(CONTRACT_LABEL);
		if (EMPLOYEE_ID_INDICATOR.equals(contributionData.getSortOptionCode())) {
			columnLabels.add(EMPLOYEE_ID_LABEL);
		} else {
			columnLabels.add(SSN_LABEL);
		}
		columnLabels.add(NAME_LABEL);
		columnLabels.add(DATE_LABEL);

		// sort according to the rule defined in MoneyTypeVO, which implements Comparable
		Collections.sort(contributionData.getMoneyTypes());
		for (int i=0; i<contributionData.getMoneyTypes().size(); i++) {
			MoneyTypeVO moneyType = (MoneyTypeVO)contributionData.getMoneyTypes().get(i);
			columnLabels.add(moneyType.getContributionName()); // does implicit substitutions
		}

		for (int i=0; i<maxLoanCount; i++) {
			columnLabels.add(LOAN_ID_LABEL);
			columnLabels.add(LOAN_AMOUNT_LABEL);
		}
		return columnLabels;
	}
}