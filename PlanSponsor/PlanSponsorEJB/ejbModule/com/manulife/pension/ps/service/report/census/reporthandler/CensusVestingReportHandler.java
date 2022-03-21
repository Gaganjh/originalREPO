package com.manulife.pension.ps.service.report.census.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.CensusVestingDAO;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.vesting.VestingConstants;

public class CensusVestingReportHandler implements ReportHandler {

	private static final Logger logger = Logger
			.getLogger(CensusVestingReportHandler.class);

	// Labels
    private static final String SSN_LABEL = "Employee SSN";
    private static final String EMPLOYEE_ID_LABEL = "Employee ID";
    private static final String FIRST_NAME_LABEL = "First Name";
    private static final String LAST_NAME_LABEL = "Last Name";
    private static final String INITIAL_LABEL = "Middle Initial";
    private static final String VESTING_DATE_LABEL = "Vesting Effective Date";
    private static final String EMPLOYMENT_STATUS_LABEL = "Employment Status";
    private static final String HIRE_DATE_LABEL = "Date of Hire";
    private static final String DIVISION_LABEL = "Division";
    private static final String YTD_HOURS_LABEL = "YTD Hours Worked";
    private static final String YTD_HOURS_EFF_DATE_LABEL = "YTD Hours Worked Effective Date";
    private static final String SERVICE_CREDITED_THIS_YEAR_LABEL = "Service Credited This Year";
    private static final String CYOS_LABEL = "Completed Years of Service";
    private static final String VYOS_LABEL = "Provided Vested Years of Service";
    private static final String VYOS_EFF_DATE_LABEL = "Provided Vested Years of Service Effective";
    private static final String FULLY_VESTED_LABEL = "100% Vesting Applied";
    private static final String FULLY_VESTED_EFF_DATE_LABEL = "100% Vesting Applied Effective";



    public static final String EMPLOYEE_ID_INDICATOR = "EE";

	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {

        // display all money types
		CensusVestingReportData censusVestingReportData = (CensusVestingReportData) CensusVestingDAO
				.getReportData(reportCriteria,true);

        censusVestingReportData.setColumnLabels(generateColumnLabels(censusVestingReportData));

		return censusVestingReportData;
	}

    /**
     * Generates the list of column labels for this report.
     *
     * @param censusVestingReportData
     * @param forDownload
     */
    protected List<String> generateColumnLabels(CensusVestingReportData censusVestingReportData) {

        List<String> columnLabels =  new ArrayList<String>();

        columnLabels.add(SSN_LABEL);
        columnLabels.add(LAST_NAME_LABEL);
        columnLabels.add(FIRST_NAME_LABEL);
        columnLabels.add(INITIAL_LABEL);
        if (EMPLOYEE_ID_INDICATOR.equals(censusVestingReportData.getSortOptionCode())) {
            columnLabels.add(EMPLOYEE_ID_LABEL);
        }

        columnLabels.add(EMPLOYMENT_STATUS_LABEL);
        columnLabels.add(HIRE_DATE_LABEL);
        if (StringUtils.equals("Y", censusVestingReportData.getSpecialSortIndicator())) {
            columnLabels.add(DIVISION_LABEL);
        } // fi

        columnLabels.add(VESTING_DATE_LABEL);

        List<MoneyTypeVO> contractMoneyTypes = censusVestingReportData.getMoneyTypes();
        // sort according to the rule defined in the comparator
        Collections.sort(contractMoneyTypes,new VestingMoneyTypeComparator());
        for (MoneyTypeVO moneyTypeVO : contractMoneyTypes) {
            columnLabels.add(moneyTypeVO.getVestingName());
        } // end for

        if (VestingConstants.VestingServiceFeature.CALCULATION.equals(censusVestingReportData.getVestingServiceFeature())) {
            columnLabels.add(YTD_HOURS_LABEL);
            columnLabels.add(YTD_HOURS_EFF_DATE_LABEL);
            columnLabels.add(SERVICE_CREDITED_THIS_YEAR_LABEL);
            columnLabels.add(CYOS_LABEL);
            columnLabels.add(VYOS_LABEL);
            columnLabels.add(VYOS_EFF_DATE_LABEL);
            columnLabels.add(FULLY_VESTED_LABEL);
            columnLabels.add(FULLY_VESTED_EFF_DATE_LABEL);
        }
        return columnLabels;
    }

}

