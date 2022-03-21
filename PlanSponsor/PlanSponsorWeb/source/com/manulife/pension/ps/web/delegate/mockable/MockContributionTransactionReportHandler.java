package com.manulife.pension.ps.web.delegate.mockable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.MoneyTypeAmount;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Mock ReportHandler for the ContributionTransactionReport
 * @author drotele
 * Created on May 4, 2004
 *
 */
public class MockContributionTransactionReportHandler
	implements ReportHandler {

	/**
	 * constructor
	 */
	public MockContributionTransactionReportHandler() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria criteria)
		throws SystemException, ReportServiceException {

		return this.createTestReportData(criteria);
	}

	/**
	 * @param criteria
	 * @return
	 */
	private ContributionTransactionReportData createTestReportData(ReportCriteria criteria) {

		BigDecimal totalEEC = new BigDecimal("0");
		BigDecimal totalERC = new BigDecimal("0");

		int itemCount = 9;

		ContributionTransactionReportData theReport =
			new ContributionTransactionReportData(criteria, itemCount);
		theReport.setTransactionNumber(
			(String) criteria.getFilterValue(
				ContributionTransactionReportData.FILTER_TRANSACTION_NUMBER));
		theReport.setTransactionType("Contribution");
		theReport.setPayrollEndingDate(new Date());
		theReport.setNumberOfParticipants(itemCount);
		theReport.setTransactionDate(new Date());

		List items = new ArrayList();
		for (int i = 0; i < itemCount; i++) {

			ContributionTransactionItem item =
				new ContributionTransactionItem();

			ParticipantVO participant = new ParticipantVO();
			participant.setFirstName("FirstName" + i);
			participant.setLastName("LastName" + i);
			participant.setId(new Integer("10" + i));
			participant.setProfileId("100000000" + i);
			participant.setSsn("12345678" + i);
			participant.setWholeName("LastName" + i + ", FirstName" + i);

			item.setParticipant(participant);

			// get random employee contribution	
			BigDecimal eeContribution =
				new BigDecimal(
					Math.floor(Math.random() * (1000 - 100 + 1)) + 100);

			// get random total contribution	
			BigDecimal totalContribution =
				new BigDecimal(
					Math.floor(Math.random() * (1000 - 100 + 1)) + 100);

			// compute Employer contribution	
			BigDecimal erContribution =
				totalContribution.subtract(eeContribution);
			item.setEmployeeContribution(eeContribution);
			item.setEmployerContribution(erContribution);
			item.setTotalContribution(totalContribution);

			items.add(item);

			totalEEC = totalEEC.add(eeContribution);
			totalERC = totalEEC.add(erContribution);
		}
		theReport.setDetails(items);

		// set totals
		theReport.setTotalEmployeeContribution(totalEEC);
		theReport.setTotalEmployerContribution(totalERC);

		// add Totals by Money Type
		List moneyType = new ArrayList();
		moneyType.add(
			new MoneyTypeAmount(
			    "EEDEF",
			    "EEDEF",
			    "Employee 401k",
			    true,
			    new BigDecimal("500.52")));
		moneyType.add(
			new MoneyTypeAmount(
			    "EERRT",
                "EERRT",
				"Employee rollover contributions",
                true,
				new BigDecimal("825.67")));
		moneyType.add(
			new MoneyTypeAmount(
			    "EEMT1",
                "EEMT1",
			    "Employee 401k",
                true,
			    new BigDecimal("545.23")));
		moneyType.add(
			new MoneyTypeAmount(
				"EERC",
                "EERC",
				"Employee rollover contributions",
                true,
				new BigDecimal("565.87")));
		moneyType.add(
			new MoneyTypeAmount(
			    "EEMT2",
                "EEMT2",
			    "Employee 401k",
                true,
			    new BigDecimal("300.65")));
		moneyType.add(
			new MoneyTypeAmount(
			    "ERMC2",
                "ERMC2",
			    "Employer match",
                false,
			    new BigDecimal("696.12")));
		moneyType.add(
			new MoneyTypeAmount(
			    "ERMC3",
                "ERMC3",
			    "Employer match",
                false,
			    new BigDecimal("505.60")));
		theReport.setMoneyTypes(moneyType);

		return theReport;
	}

}
