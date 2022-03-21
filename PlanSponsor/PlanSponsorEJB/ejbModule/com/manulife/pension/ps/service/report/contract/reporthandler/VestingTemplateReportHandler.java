package com.manulife.pension.ps.service.report.contract.reporthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.CensusVestingDAO;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.vesting.VestingConstants;

public class VestingTemplateReportHandler implements ReportHandler {

    private static final Logger logger = Logger
            .getLogger(VestingTemplateReportHandler.class);

    // Labels
    private static final String TRANSACTION_LABEL = "vest.h10";
    private static final String CONTRACT_LABEL = "Cont#";
    private static final String SSN_LABEL = "SSN#";
    private static final String EMPLOYEE_ID_LABEL = "EEID#";
    private static final String FIRST_NAME_LABEL = "FirstName";
    private static final String LAST_NAME_LABEL = "LastName";
    private static final String INITIAL_LABEL = "Initial";
    private static final String VESTING_PERC_DATE_LABEL = "VestingEffDate";
    private static final String VESTING_VYOS_DATE_LABEL = "VYOSDate";
    private static final String VYOS_LABEL = "VYOS";
    private static final String APPLYLTPTCREDITING = "ApplyLTPTCrediting";

    private static final String EMPLOYEE_ID_INDICATOR = "EE";
    
    public static final String PRODUCT_RA457="RA457";


    public ReportData getReportData(ReportCriteria reportCriteria)
            throws SystemException, ReportServiceException {

        // display only not fully vested money types
        CensusVestingReportData censusVestingReportData = (CensusVestingReportData) CensusVestingDAO
                .getReportData(reportCriteria,false);

        String productId = (String)reportCriteria.getFilterValue(
                CensusVestingReportData.FILTER_PRODUCTID);
        
        censusVestingReportData.setColumnLabels(generateColumnLabels(censusVestingReportData, productId));

        return censusVestingReportData;
    }

    /**
     * Generates the list of column labels for this report.
     *
     * @param censusVestingReportData
     * @param forDownload
     */
    protected List<String> generateColumnLabels(CensusVestingReportData censusVestingReportData, String productId) {

        List<String> columnLabels =  new ArrayList<String>();

        columnLabels.add(TRANSACTION_LABEL);
        columnLabels.add(CONTRACT_LABEL);

        columnLabels.add(SSN_LABEL);
        columnLabels.add(FIRST_NAME_LABEL);
        columnLabels.add(LAST_NAME_LABEL);
        columnLabels.add(INITIAL_LABEL);
        columnLabels.add(EMPLOYEE_ID_LABEL);
        
        if (VestingConstants.VestingServiceFeature.CALCULATION.equals(censusVestingReportData.getVestingServiceFeature())) {
        	List details = (List) censusVestingReportData.getDetails();
        	CensusVestingDetails vesting = (CensusVestingDetails) details.get(0);
        	if(VestingConstants.CreditingMethod.HOURS_OF_SERVICE.equalsIgnoreCase(vesting.getCalculationFact()
					.getCreditingMethod())) {
        		if(!PRODUCT_RA457.equalsIgnoreCase(productId)) {
        			columnLabels.add(APPLYLTPTCREDITING);
        		}
        	}
            columnLabels.add(VESTING_VYOS_DATE_LABEL);
            columnLabels.add(VYOS_LABEL);
        } else {
            columnLabels.add(VESTING_PERC_DATE_LABEL);
            List<MoneyTypeVO> contractMoneyTypes = censusVestingReportData.getMoneyTypes();
            // sort according to the rule defined in the comparator
            Collections.sort(contractMoneyTypes,new VestingMoneyTypeComparator());
            for (MoneyTypeVO moneyTypeVO : contractMoneyTypes) {
                columnLabels.add(moneyTypeVO.getVestingName());
            } // end for
        }
        
        return columnLabels;
    }
}

