package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;	
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.bind.annotation.InitBinder;	
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import com.intware.dao.DAOException;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.CensusVestingReportDataUi.CensusVestingReportDataRowUi;
import com.manulife.pension.ps.web.census.CensusVestingReportDataUi.CensusVestingReportDataRowValueUi;
import com.manulife.pension.ps.web.census.util.CensusInfoFilterCriteriaHelper;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.census.util.CensusSummaryUtils;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.census.util.CensusValidationErrors;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ProtectedStringBuffer;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ContractStatementInfoVO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.ReportFrequencyType;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.employee.valueobject.PlanYearEndInfo;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.VestingEngine;
import com.manulife.pension.service.vesting.VestingException;
import com.manulife.pension.service.vesting.VestingInputDescription;
import com.manulife.pension.service.vesting.VestingRetrievalDetails;
import com.manulife.pension.service.vesting.dao.Db2VestingDAO;
import com.manulife.pension.service.vesting.transfer.MoneyTypeVestingPercentageVo;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action handles the creation of the CensusVestingReport. It will also create the census
 * vesting download.
 * 
 * @author Diana Macean
 * @see ReportController for details
 */
@Controller
@RequestMapping(value = "/census")
@SessionAttributes({"censusVestingReportForm"})

public final class CensusVestingReportController extends ReportController {

	@ModelAttribute("censusVestingReportForm") 
	public CensusVestingReportForm populateForm()
	{
		return new CensusVestingReportForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{
		forwards.put("input","/census/censusVestingReport.jsp");
		forwards.put("default","/census/censusVestingReport.jsp");
		forwards.put("sort","/census/censusVestingReport.jsp");
		forwards.put("filter","/census/censusVestingReport.jsp");
		forwards.put("print","/census/censusVestingReport.jsp");
		forwards.put("edit","/census/censusVestingReport.jsp");
		forwards.put("save","/census/censusVestingReport.jsp"); 
		forwards.put("saveError","/census/censusVestingReport.jsp");
		forwards.put("cancel","/census/censusVestingReport.jsp"); 
		forwards.put("reset","/census/censusVestingReport.jsp");
		forwards.put("page","/census/censusVestingReport.jsp");
	}

	
    public static final Logger logger = Logger.getLogger(CensusVestingReportController.class);

    protected static final String TOTAL_COUNT_COLUMN_HEADING = "Total count of employees,";

    protected static final int SSN_LENGTH = 9;

    protected static final String DOWNLOAD_COLUMN_HEADING = "cens.h10,Cont#,SSN#,FirstName,LastName,Initial,NamePrefix,EEID#,Address1,Address2,City,State,ZipCode,Country,"
            + "StateRes,ERProvEmail,Division,BirthDate,HireDate,EmplStat,EmplStatDate,EligInd,EligDate,OptOutInd,YTDHrs,PlanYTDComp,YTDHrsWkCompDt,"
            + "BaseSalary,BfTxDefPct,DesigRothPct,BfTxFltDoDef,DesigRothAmt";

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

    public static final String REPORT_TYPE = "reportType";

    public static final String CENSUS_REPORT = "census";

    public static final String EMPLOYEE_ID_INDICATOR = "EE";

    public static final String FILE_LINE_BREAK_SEPARATOR = "|";

    public static final int FILE_LINE_BREAK_LENGTH = 100;

    /**
     * Constructor for CensusVestingReportAction.
     */
    public CensusVestingReportController() {
        super(CensusVestingReportController.class);
    }

    /**
     * @see ReportController#getDownloadData(PrintWriter, BaseReportForm, ReportData)
     */
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) {

        byte[] bytes = null;

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDownloadData");
        }

        // Identify the type of report
        // if(CENSUS_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))){
        // bytes = getCensusDownloadData(reportForm, report, request);
        // } else {
        bytes = getVestingDownloadData(reportForm, report, request);
        // }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getDownloadData");
        }

        return bytes;
    }

    /**
     * Populate Census Download Data
     */
    protected byte[] getCensusDownloadData(final BaseReportForm reportForm,
            final ReportData report, final HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getCensusDownloadData");
        }

        CensusVestingReportForm form = (CensusVestingReportForm) reportForm;
        UserProfile user = getUserProfile(request);
        UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(user.getPrincipal());
        Contract currentContract = user.getCurrentContract();

        // find the contract sort code
        String sortCode = currentContract.getParticipantSortOptionCode();

        String todayDate = DateRender.formatByPattern(Calendar.getInstance().getTime(), "",
                RenderConstants.MEDIUM_MDY_SLASHED);

        StringBuffer buffer = new StringBuffer();

        buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(
                COMMA).append(currentContract.getCompanyName()).append(LINE_BREAK);

        // As of and total count
        buffer.append("As of,").append(todayDate).append(LINE_BREAK);
        buffer.append(TOTAL_COUNT_COLUMN_HEADING).append(report.getTotalCount()).append(LINE_BREAK);

        // filters used
        if (!StringUtils.isEmpty(form.getStatus())) {
            buffer.append("Status,").append(
                    CensusLookups.getInstance().getLookupValue(CensusLookups.EmploymentStatus,
                            form.getStatus())).append(LINE_BREAK);
        }
        if (!StringUtils.isEmpty(form.getNamePhrase())) {
            buffer.append("Last name starts with,").append(form.getNamePhrase()).append(LINE_BREAK);
        }
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }
        if (!form.getSsn().isEmpty()) {
            if (form.getSsn().toString().length() == SSN_LENGTH) {
                //SSE S024 determine wheather the ssn should be masked on the csv report
               buffer.append("SSN is,").append(SSNRender.format(form.getSsn(), null, maskSsnFlag)).append(
              LINE_BREAK);
            } else {
                buffer.append("SSN is,").append(form.getSsn().toString()).append(LINE_BREAK);
            }
        }

        if (!StringUtils.isEmpty(form.getDivision())) {
            buffer.append("Division,").append(form.getDivision()).append(LINE_BREAK);
        }

        if (!StringUtils.isEmpty(form.getSegment())) {
            if (form.getSegment().equals("1")) {
                buffer.append("Segment,").append("Account Holders").append(LINE_BREAK);
            } else {
                buffer.append("Segment,").append("Non-Account Holders").append(LINE_BREAK);
            }
        }

        // heading and records
        buffer.append(DOWNLOAD_COLUMN_HEADING);
    
        Iterator iterator = report.getDetails().iterator();
        while (iterator.hasNext()) {
            buffer.append(LINE_BREAK);
            CensusVestingDetails theItem = (CensusVestingDetails) iterator.next();

            buffer.append("cens.d").append(COMMA);
            buffer.append(currentContract.getContractNumber()).append(COMMA);
            //SSE S024 determine wheather the ssn should be masked on the csv report

           buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA);

            buffer.append(escapeField(theItem.getFirstName().trim())).append(COMMA);
            buffer.append(escapeField(theItem.getLastName().trim())).append(COMMA);

            if (theItem.getMiddleInitial() != null)
                buffer.append(theItem.getMiddleInitial());
            buffer.append(COMMA);

            if (theItem.getNamePrefix() != null)
                buffer.append(theItem.getNamePrefix());
            buffer.append(COMMA);

            if (theItem.getEmployeeNumber() != null
                    && theItem.getEmployeeNumber().trim().length() > 0) {
                buffer.append(StringUtils.leftPad(theItem.getEmployeeNumber().trim(), 9, "0"));
            }
            buffer.append(COMMA);

            if (theItem.getAddressLine1() != null)
                buffer.append(escapeField(theItem.getAddressLine1().trim()));
            buffer.append(COMMA);

            if (theItem.getAddressLine2() != null)
                buffer.append(escapeField(theItem.getAddressLine2().trim()));
            buffer.append(COMMA);

            if (theItem.getCity() != null)
                buffer.append(escapeField(theItem.getCity().trim()));
            buffer.append(COMMA);

            if (theItem.getStateCode() != null)
                buffer.append(escapeField(theItem.getStateCode().trim()));
            buffer.append(COMMA);

            if (theItem.getZipCode() != null) {
                String zipCode = StringUtils.trim(theItem.getZipCode());
                if (zipCode.length() > 0) {
                	buffer.append(zipCode.toUpperCase());
                }
            }
            buffer.append(COMMA);

            if (theItem.getCountry() != null)
                buffer.append(escapeField(theItem.getCountry().trim()));
            buffer.append(COMMA);

            if (theItem.getStateOfResidence() != null)
                buffer.append(escapeField(theItem.getStateOfResidence().trim()));
            buffer.append(COMMA);

            if (theItem.getEmployeeProvidedEmail() != null)
                buffer.append(escapeField(theItem.getEmployeeProvidedEmail().trim()));
            buffer.append(COMMA);

            if (theItem.getDivision() != null)
                buffer.append(escapeField(theItem.getDivision().trim()));
            buffer.append(COMMA);

            buffer.append(
                    DateRender.formatByPattern(theItem.getBirthDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
            buffer.append(
                    DateRender.formatByPattern(theItem.getHireDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            if (theItem.getStatus() != null)
                buffer.append(escapeField(theItem.getStatus().trim()));
            buffer.append(COMMA);

            buffer.append(
                    DateRender.formatByPattern(theItem.getEmployeeStatusDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            if (theItem.getEligibleToDeferInd() != null)
                buffer.append(escapeField(theItem.getEligibleToDeferInd().trim()));
            buffer.append(COMMA);

            buffer.append(
                    DateRender.formatByPattern(theItem.getEligibilityDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            if (theItem.getOptOut() != null)
                buffer.append(escapeField(theItem.getOptOut().trim()));
            buffer.append(COMMA);

            if (theItem.getPlanYTDHoursWorked() != null)
                buffer.append(escapeField(NumberRender.formatByType(
                        theItem.getPlanYTDHoursWorked(), "", RenderConstants.INTEGER_TYPE)));
            buffer.append(COMMA);

            buffer.append(
                    escapeField(CensusSummaryUtils.getMaskedValue(theItem.getPlanYTDCompensation(),
                            theItem, user, userInfo, false))).append(COMMA);
            buffer.append(
                    DateRender.formatByPattern(theItem.getPlanYTDHoursWorkedEffDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            buffer.append(
                    escapeField(CensusSummaryUtils.getMaskedValue(theItem.getAnnualBaseSalary(),
                            theItem, user, userInfo, false))).append(COMMA);
            buffer.append(
                    escapeField(NumberRender.formatByPattern(theItem
                            .getBeforeTaxDeferralPercentage(), "", "###.###", 3,
                            BigDecimal.ROUND_HALF_DOWN))).append(COMMA);
            buffer.append(
                    escapeField(NumberRender.formatByPattern(theItem
                            .getDesigRothDeferralPercentage(), "", "###.###", 3,
                            BigDecimal.ROUND_HALF_DOWN))).append(COMMA);
            buffer.append(
                    escapeField(NumberRender.formatByType(theItem.getBeforeTaxDeferralAmount(), "",
                            RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
            buffer.append(
                    escapeField(NumberRender.formatByType(theItem.getDesigRothDeferralAmount(), "",
                            RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getCensusDownloadData");
        }

        return buffer.toString().getBytes();
    }

    /**
     * Populate Vesting Download Data
     */
    protected byte[] getVestingDownloadData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getVestingDownloadData");
        }

        ProtectedStringBuffer buffer = new ProtectedStringBuffer();
        CensusVestingReportData vestingData = (CensusVestingReportData) report;
        CensusVestingReportForm form = (CensusVestingReportForm) reportForm;

        UserProfile user = getUserProfile(request);
        Contract currentContract = user.getCurrentContract();

        // get As of Date from the form
        String asOfDate = form.getAsOfDate();
        Date selectedAsOfDate = parseAsOfDate(asOfDate);
        asOfDate = DateRender.formatByPattern(selectedAsOfDate, "",
                RenderConstants.MEDIUM_MDY_SLASHED);

        // find the contract sort code
        String sortCode = currentContract.getParticipantSortOptionCode();
        
        // Indicator to determine if the division column is shown
        final boolean showDivisionColumn = currentContract.hasSpecialSortCategoryInd();

        // Get content
        Message titleMessage = null;
        Message disclaimerMessage = null;
        final boolean vestingServiceFeatureIsCalculate = VestingConstants.VestingServiceFeature.CALCULATION
                .equals(vestingData.getVestingServiceFeature());
        int titleContentId;
        int disclaimerContentId;
        if (vestingServiceFeatureIsCalculate) {
            titleContentId = ContentConstants.DOWNLOAD_REPORT_TITLE_FOR_CALCULATED;
            disclaimerContentId = ContentConstants.DOWNLOAD_REPORT_DISCLAIMER_FOR_CALCULATED;
        } else {
            titleContentId = ContentConstants.DOWNLOAD_REPORT_TITLE_FOR_COLLECTED;
            disclaimerContentId = ContentConstants.DOWNLOAD_REPORT_DISCLAIMER_FOR_COLLECTED;
        }
        try {
            titleMessage = (Message) ContentCacheManager.getInstance().getContentById(
                    titleContentId, ContentTypeManager.instance().MESSAGE);
            disclaimerMessage = (Message) ContentCacheManager.getInstance().getContentById(
                    disclaimerContentId, ContentTypeManager.instance().MESSAGE);
        } catch (ContentException ce) {
        } catch (NullPointerException ne) {
        }

        // Report Header
        buffer.append(escapeField(getTextFromContent(titleMessage))).append(LINE_BREAK);

        // Wrap the disclaimer text.
        final String disclaimerText = StringUtils.chomp(getTextFromContent(disclaimerMessage));
        final String disclaimerTextWithSeparator = WordUtils.wrap(disclaimerText,
                FILE_LINE_BREAK_LENGTH, FILE_LINE_BREAK_SEPARATOR, false);
        final String[] disclaimerLines = StringUtils.split(disclaimerTextWithSeparator,
                FILE_LINE_BREAK_SEPARATOR);
        for (String disclaimerLine : disclaimerLines) {
            buffer.append(escapeField(disclaimerLine)).append(LINE_BREAK);
        }
        // buffer.append(escapeField(disclaimerText)).append(LINE_BREAK);

        buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(
                COMMA).append(escapeField(currentContract.getCompanyName())).append(LINE_BREAK);

        // Report run date
        final Date now = new Date();
        final String reportRunDateString = DateRender.formatByPattern(now, "",
                RenderConstants.MEDIUM_MDY_SLASHED);
        buffer.append("Report run date,").append(reportRunDateString).append(LINE_BREAK);

        // As of and total count
        buffer.append("Reporting period end date,").append(asOfDate).append(LINE_BREAK);
        buffer.append(TOTAL_COUNT_COLUMN_HEADING).append(report.getTotalCount()).append(LINE_BREAK);

        // filters used
        if (!StringUtils.isEmpty(form.getStatus())) {
            buffer.append("Status,").append(
                    CensusLookups.getInstance().getLookupValue(CensusLookups.EmploymentStatus,
                            form.getStatus())).append(LINE_BREAK);
        }
        if (!StringUtils.isEmpty(form.getNamePhrase())) {
            buffer.append("Last name starts with,").append(form.getNamePhrase()).append(LINE_BREAK);
        }
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }
        if (!form.getSsn().isEmpty()) {
            if (form.getSsn().toString().length() == SSN_LENGTH) {	
            	buffer.append("SSN is,").append(SSNRender.format(form.getSsn(), null, maskSsnFlag)).append(
               LINE_BREAK);
            } else {
                buffer.append("SSN is,").append(form.getSsn().toString()).append(LINE_BREAK);
            }
        }

        if (!StringUtils.isEmpty(form.getDivision())) {
            buffer.append("Division,").append(form.getDivision()).append(LINE_BREAK);
        }

        if (!StringUtils.isEmpty(form.getSegment())) {
            if (form.getSegment().equals("1")) {
                buffer.append("Segment,").append("Account Holders").append(LINE_BREAK);
            } else {
                buffer.append("Segment,").append("Non-Account Holders").append(LINE_BREAK);
            }
        }

        // Fill in the header
        Iterator columnLabels = vestingData.getColumnLabels().iterator();
        while (columnLabels.hasNext()) {
            buffer.append(columnLabels.next());
            if (columnLabels.hasNext()) {
                buffer.append(COMMA);
            }
        }

        Iterator items = report.getDetails().iterator();
        while (items.hasNext()) {
            buffer.append(LINE_BREAK);
            // buffer.append(vestingData.getTransactionNumber()).append(COMMA);
            // buffer.append(vestingData.getContractNumber()).append(COMMA);

            CensusVestingDetails vesting = (CensusVestingDetails) items.next();

            // SSN
          buffer.append(SSNRender.format(vesting.getSsn(), null, maskSsnFlag)).append(COMMA);

            // Names (Last, First and Middle Initial)
            buffer.append(escapeField(StringUtils.trimToEmpty(vesting.getLastName())))
                    .append(COMMA);
            buffer.append(escapeField(StringUtils.trimToEmpty(vesting.getFirstName()))).append(
                    COMMA);
            buffer.append(escapeField(StringUtils.trimToEmpty(vesting.getMiddleInitial()))).append(
                    COMMA);

            // Employee ID (may not display)
            if (sortCode.equals(EMPLOYEE_ID_INDICATOR)) {
                if (vesting.getEmployeeNumber() != null
                        && vesting.getEmployeeNumber().trim().length() > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < (9 - vesting.getEmployeeNumber().trim().length()); i++) {
                        sb.append("0");
                    }
                    buffer.append(sb.toString());
                    buffer.append(vesting.getEmployeeNumber().trim());
                }
                buffer.append(COMMA);
            }

            // Employment Status
            if (vesting.getStatus() != null) {
                buffer.append(escapeField(escapeField(vesting.getStatus().trim())));
            } // fi
            buffer.append(COMMA);

            // Hire Date
            buffer.append(
                    DateRender.formatByPattern(vesting.getHireDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            // Division
            if (showDivisionColumn) {
                if (vesting.getDivision() != null) {
                    buffer.append(escapeField(vesting.getDivision().trim()));
                } // fi
                buffer.append(COMMA);
            } // fi

            // Vesting Effective Date
            buffer.append(
                    DateRender.formatByPattern(vesting.getVestingEffDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            // Vesting Percentages by Money Type
            // sort according to the rule defined in the comparator
            List<MoneyTypeVO> contractMoneyTypes = vestingData.getMoneyTypes();
            Collections.sort(contractMoneyTypes, new VestingMoneyTypeComparator());
            for (int i = 0; i < contractMoneyTypes.size(); i++) {
                MoneyTypeVO vo = (MoneyTypeVO) contractMoneyTypes.get(i);
                MoneyTypeVestingPercentage mtvp = (MoneyTypeVestingPercentage) vesting
                        .getPercentages().get(vo.getId());
                if (mtvp != null) {
                    BigDecimal perc = mtvp.getPercentage();
                    buffer.append(NumberRender.formatByPattern(perc, "", "###.###", 3,
                            BigDecimal.ROUND_HALF_DOWN));
                }
                buffer.append(COMMA);
            }

            // Only displayed for calculate contracts.
            if (vestingServiceFeatureIsCalculate) {

                final boolean creditingMethodIsHoursOfService = VestingConstants.CreditingMethod.HOURS_OF_SERVICE
                        .equals(vesting.getCalculationFact().getCreditingMethod());
                final boolean creditingMethodIsElapsedTime = VestingConstants.CreditingMethod.ELAPSED_TIME
                        .equals(vesting.getCalculationFact().getCreditingMethod());

                final VestingRetrievalDetails fact = vesting.getCalculationFact();

                // if crediting method is Hours of Service
                // YTD Hours Worked
                if (creditingMethodIsHoursOfService) {

                    VestingInputDescription hos = fact
                            .getEffectiveInput(VestingRetrievalDetails.PARAMETER_RAW_PLAN_YTD_HOURS_OF_SERVICE);
                    if (hos != null) {
                        buffer.append(escapeField(NumberRender.formatByType(hos.getIntegerValue(),
                                "", RenderConstants.INTEGER_TYPE)));
                    }
                    buffer.append(COMMA);

                    buffer.append(
                            DateRender.formatByPattern(hos == null ? null : hos.getEffectiveDate(),
                                    "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                }

                // if crediting method is Elapsed Time
                // YTD Hours Worked
                if (creditingMethodIsElapsedTime) {
                    buffer.append(COMMA);
                    buffer.append(COMMA);
                }

                // Service Credited This Year
                final VestingInputDescription vestingInputDescriptionServiceCreditedThisYear = fact
                        .getEffectiveInput(VestingRetrievalDetails.PARAMETER_CALCULATED_SERVICE_CREDITED_CURRENT_YEAR);
                if (vestingInputDescriptionServiceCreditedThisYear != null) {
                    buffer.append(escapeField(vestingInputDescriptionServiceCreditedThisYear
                            .getBooleanValue() ? "Y" : "N"));
                }
                buffer.append(COMMA);

                // Completed Years of Service
                final VestingInputDescription vestingInputDescriptionCompletedYearsOfService = fact
                        .getEffectiveInput(VestingRetrievalDetails.PARAMETER_CALCULATED_COMPLETED_YEARS_OF_SERVICE);
                if (vestingInputDescriptionCompletedYearsOfService != null) {
                    buffer.append(escapeField(NumberRender.formatByType(
                            vestingInputDescriptionCompletedYearsOfService.getIntegerValue(), "",
                            RenderConstants.INTEGER_TYPE)));
                }
                buffer.append(COMMA);

                // Provided VYOS
                if (creditingMethodIsHoursOfService) {

                    VestingInputDescription info = fact
                            .getEffectiveInput(VestingRetrievalDetails.PARAMETER_RAW_VESTED_YEARS_OF_SERVICE);
                    if (info != null) {
                        buffer.append(
                                escapeField(NumberRender.formatByType(info.getIntegerValue(), "",
                                        RenderConstants.DECIMAL_TYPE))).append(COMMA);

                        buffer.append(
                                DateRender.formatByPattern(info.getEffectiveDate(), "",
                                        RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                    } else {
                        buffer.append(COMMA);
                        buffer.append(COMMA);
                    }
                }

                // Provided VYOS
                if (creditingMethodIsElapsedTime) {
                    buffer.append(COMMA);
                    buffer.append(COMMA);
                }

                // Fully Vested Indicator
                VestingInputDescription fullyVestedInd = fact
                        .getEffectiveInput(VestingRetrievalDetails.PARAMETER_RAW_FULLY_VESTED);
                if (fullyVestedInd != null) {
                    buffer.append(escapeField(fullyVestedInd.getBooleanValue() ? "Y" : "N"));
                }
                buffer.append(COMMA);

                buffer.append(
                        DateRender.formatByPattern(fullyVestedInd == null ? null : fullyVestedInd
                                .getEffectiveDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
                        .append(COMMA);

            } // end if - calculate contracts

        } // end while - looping over rows

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getVestingDownloadData");
        }

        return buffer.toString().getBytes();
    }

    private Date parseAsOfDate(String asOfDate) {
        Date selectedAsOfDate;
        if (asOfDate != null && asOfDate.trim().length() != 0)
            selectedAsOfDate = new Date(Long.parseLong(asOfDate));
        else
            selectedAsOfDate = new Date();
        return selectedAsOfDate;
    }

    /**
     * @see ReportController#getDefaultSortDirection()
     * @return String
     */
    protected String getDefaultSortDirection() {
        return ReportSort.ASC_DIRECTION;
    }

    /**
     * @see ReportController#getDefaultSort()
     * @return String
     */
    protected String getDefaultSort() {
        return CensusVestingReportData.DEFAULT_SORT;
    }

    /**
     * @see ReportController#getReportId()
     * @return String
     */
    protected String getReportId() {
        return CensusVestingReportData.REPORT_ID;
    }

    /**
     * @see ReportController#getReportName()
     * 
     * @return String
     */
    protected String getReportName() {
        return CensusVestingReportData.REPORT_NAME;
    }

    /**
     * Given a report ID, returns the downloaded CSV file name.
     * 
     * @param request
     * 
     * @return The file name used for the downloaded CSV.
     */
    protected String getFileName(HttpServletRequest request) {
        // defaults to .csv extension
        String dateString = null;
        synchronized (DATE_FORMATTER) {
            dateString = DATE_FORMATTER.format(new Date());
        }

        String name = "";
        // Identify the type of report
        if (CENSUS_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))) {
            name = "Census_Summary_Report_for_";
        } else {
            name = "Vesting_Report_for_";
        }

        return name + getUserProfile(request).getCurrentContract().getContractNumber() + "_for_"
                + dateString + CSV_EXTENSION;
    }

    /**
     * 
     * @see ReportController#populateReportCriteria(ReportCriteria, BaseReportForm,
     *      HttpServletRequest)
     * 
     * @param criteria ReportCriteria
     * @param form BaseReportForm
     * @param request HttpServletRequest
     */
    protected void populateReportCriteria(final ReportCriteria criteria,
            final BaseReportForm form, final HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria");
        }

        // default sort criteria
        // this is already set in the super

        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();

        criteria.addFilter(CensusVestingReportData.FILTER_CONTRACT_NUMBER, Integer
                .toString(currentContract.getContractNumber()));

        CensusVestingReportForm psform = (CensusVestingReportForm) form;
        
        // Get the filter object from session.
        FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		String task = getTask(request);
        
		if(filterCriteriaVo == null ){
			filterCriteriaVo = new FilterCriteriaVo();
		}
		
		// If the task is default then reset the page no and sort detains that
		// are cached in eligibility tab and deferral tab.
		if (task.equals(DEFAULT_TASK)) {
			filterCriteriaVo.clearDeferralSortDetails();
			filterCriteriaVo.clearEligibilitySortDetails();
		}
		
		// Populate the filter criterias
		CensusInfoFilterCriteriaHelper.populateVestingTabFilterCriteria(task, filterCriteriaVo, psform, criteria);
		
		//set filterCriteriaVo back to session
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
        
        // if external user, don't display Canceled employees
        criteria.setExternalUser(userProfile.getRole().isExternalUser());

        if (logger.isDebugEnabled()) {
            logger.debug("criteria= " + criteria);
            logger.debug("exit <- populateReportCriteria");
        }
    }

    /**
     * @param mapping ActionMapping
     * @param reportForm BaseReportForm
     * @param request HttpServletRequest
     */
    protected void populateReportForm(
            final BaseReportForm reportForm, final HttpServletRequest request) {
        super.populateReportForm( reportForm, request);

        String task = getTask(request);
        if (task.equals(FILTER_TASK)) {
            reportForm.setSortField(getDefaultSort());
            reportForm.setSortDirection(getDefaultSortDirection());
        }
    }

    /**
     * Populate sort criteria in the criteria object using the given FORM. Default sort: - by last
     * name, first name, middle initial
     * 
     * @param criteria The criteria to populate
     * @param form The Form to populate from.
     */
    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
        if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
            if (!form.getSortField().equals(getDefaultSort())) {
                criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
            }
        }
    }

    /**
     * Validate the fields in the filter.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     * 
     * @param mapping ActionMapping
     * @param form Form
     * @param request HttpServletRequest
     * @return Collection
     */

    @Autowired
    private CensusVestingReportValidator censusVestingReportValidator;
    
    @RequestMapping(value = "/censusVesting", method = { RequestMethod.POST })
	public String execute(@ModelAttribute("censusVestingReportForm")  CensusVestingReportForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException {
		
    	
    	 UserProfile userProfile = SessionHelper.getUserProfile(request);

         // check for selected access
         if (userProfile.isSelectedAccess()) {
             return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
         }

         // check if contract is discontinued
         if (userProfile.getCurrentContract().isDiscontinued()) {
             return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
         }

             // do a refresh so that there's no problem using tha back button
        	 ControllerForward forward = new ControllerForward("refresh",
     				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
     		return "redirect:" + forward.getPath();

	}

    /**
     * Check for permissions
     * 
     * @see PsController#execute(ActionMapping, Form, HttpServletRequest, HttpServletResponse)
     * 
     * @param mapping ActionMapping
     * @param form Form
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    @RequestMapping(value="/censusVesting",method = {RequestMethod.GET})
	public String doDefualt(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              ControllerForward forward = new ControllerForward("refresh",
	      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
	      		return "redirect:" + forward.getPath();
	       }
		}
    
        UserProfile userProfile = SessionHelper.getUserProfile(request);

        // check for selected access
        if (userProfile.isSelectedAccess()) {
            return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }

        // check if contract is discontinued
        if (userProfile.getCurrentContract().isDiscontinued()) {
            return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        String forward =null;
        request.setAttribute("censusVestingReportForm",form);
        forward = super.doDefault(form, request, response);
       return StringUtils.contains(forward,"/")?forward:forwards.get(forward);
    }

    /**
     * @see ReportController#doCommon(ActionMapping, Form, HttpServletRequest,
     *      HttpServletResponse)
     * 
     * @param mapping ActionMapping
     * @param form BaseReportForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    
    
	public String doCommon(final BaseReportForm reportForm,HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
    	
   
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCommon");
        }

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        final CensusVestingReportForm form = (CensusVestingReportForm) reportForm;

        form.setIsEditMode(StringUtils.equals(getTask(request), "edit"));
        boolean inViewMode = !form.getIsEditMode();
        boolean inEditMode = form.getIsEditMode();

        if (inViewMode) {
            form.setDirty("false");
        } // fi

        // check if asOfDate is a request parameter, otherwise set the default to today.
        String asOfDateValue = request.getParameter(Constants.AS_OF_DATE_PARAMETER);
        if (asOfDateValue != null) {
            form.setAsOfDate(asOfDateValue);
        } else if (StringUtils.isEmpty(form.getAsOfDate())) {
            form.setAsOfDate(Long.toString(today.getTime()));
        } // fi

        String forward = super.doCommon( reportForm, request, response);

        UserProfile userProfile = getUserProfile(request);
        int contractId = userProfile.getCurrentContract().getContractNumber();

        // get the report details and check for warnings
        CensusVestingReportData report = (CensusVestingReportData) request
                .getAttribute(Constants.REPORT_BEAN);
        List<CensusVestingDetails> details = (List<CensusVestingDetails>) report.getDetails();
        applyBusinessRulesForWarnings(details, request);

        // set permission flag for editing
        final boolean vestingIsCalculated = CensusUtils.isVestingCalculated(contractId);

        // Lookup plan data, to get the plan end date and service crediting method.
        //final PlanData planData = ContractServiceDelegate.getInstance().readPlanData(
          //      new Integer(contractId), true);
        final PlanDataLite planDataLite = ContractServiceDelegate.getInstance().getPlanDataLight(
                      new Integer(contractId));
        final String creditingMethod = planDataLite.getVestingServiceCreditMethod();

        form.setServiceCreditingMethod(creditingMethod);

        final boolean creditingMethodIsSpecified = form.getIsCreditingMethodSpecified();

        final boolean contractIsNotDiscontinued = (!(Contract.STATUS_CONTRACT_DISCONTINUED
                .equals(userProfile.getCurrentContract().getStatus())));

        form.setAllowedToEdit(userProfile.isAllowedSubmitUpdateVesting()
                && contractIsNotDiscontinued && vestingIsCalculated && creditingMethodIsSpecified);

        // There exists at least one non-fully vested money type.
        final boolean atLeastOneNotFullyVested = !MoneyTypeVO.getAreAllMoneyTypesFullyVested(report
                .getMoneyTypes());

        form.setAllowedToViewVestingInformation(vestingIsCalculated && creditingMethodIsSpecified
                && atLeastOneNotFullyVested);

        // set permission flag for adding a new employee
        form.setAllowedToAdd(userProfile.isAllowedUpdateCensusData() && contractIsNotDiscontinued);

        // set permission flag for eligibility tab
        boolean isEnabled = userProfile.isInternalUser()
                || CensusUtils.isAutoEnrollmentEnabled(contractId);
        form.setAllowedToAccessEligibTab(isEnabled);

        // set permission flag for deferral tab
        boolean allowedToAccessDeferrals = DeferralUtils.isAllowedToAccessDeferrals(userProfile);
        form.setAllowedToAccessDeferralTab(allowedToAccessDeferrals);

        // set permission flag for How To ... auto enrollment
        form.setAllowedToAutoEnrollment(CensusUtils.isAutoEnrollmentEnabled(contractId));

        // set permission flag for download census report
//      SSE S024 allow to download report, but mask the ssn if required
        form.setAllowedToDownloadCensus((userProfile.isInternalUser() && 
        		userProfile.isAllowedUpdateCensusData())
               || (userProfile.getRole().isExternalUser() ));
         //&& userProfile.isAllowedDownloadReport()));

        // set permission flag for download vesting report
//      SSE S024 allow to download report, but mask the ssn if required
        form.setAllowedToDownloadVesting(inViewMode
        && ((CensusUtils.isVestingProvidedByTPA(contractId) || (vestingIsCalculated && creditingMethodIsSpecified)) && (userProfile
        .isInternalUser() || (userProfile.getRole().isExternalUser()))));
        //&& userProfile.isAllowedDownloadReport()))));

        // populate list of employee statuses for the dropdown
        List employeeStatusList = null;
        // if external user, do not display Cancelled status in the dropdown
        if (userProfile.isInternalUser()) {
            employeeStatusList = CensusLookups.getInstance().getEmploymentStatuses();
        } else {
            employeeStatusList = CensusLookups.getInstance().getEmploymentStatusesWithoutC();
        }
        form.setStatusList(employeeStatusList);

        // populate list of segments for the dropdown
        form.setSegmentList(CensusLookups.getInstance().getSegments());

        // populate asOfDate list
        List<Date> dateList = new ArrayList<Date>();

        // Get the statement info to get the statement frequency.
        final String contractStatementReportFrequency = ContractServiceDelegate.getInstance()
                .getContractStatementFrequency(new Integer(contractId));
        ReportFrequencyType reportFrequencyType = ContractStatementInfoVO
                .getReportFrequencyType(contractStatementReportFrequency);
        if (reportFrequencyType == null) { // set default
            reportFrequencyType = ReportFrequencyType.QUARTERLY;
        }
        
        // Get statement dates list for the last 2 years and into the next future period.
        final PlanYearEndInfo planYearEndInfo = new PlanYearEndInfo(planDataLite.getPlanYearEnd(),
                today);

        // Gets all the old and future statement dates for the provided 'as of' date.
        final Collection<Date> statementDates = planYearEndInfo
                .getSelectableStatementDates(reportFrequencyType);

        // If today is already in the list, we don't add it. Otherwise, we add it to the start of
        // the list.
        final boolean todayIsNotAReportingPeriodEndDate = !(statementDates.contains(today));
        if (todayIsNotAReportingPeriodEndDate) {
            dateList.add(today);
        } // fi

        // Add the rest of the statement dates to the list.
        if (StringUtils.equals(ReportFrequencyType.SEMI_ANNUAL.getCode(), reportFrequencyType
                .getCode())) {

            // Add all the dates, but only one future date.
            for (Date date : statementDates) {
                dateList.add(date);

                // If a future date is found, stop adding dates.
                if (date.after(today)) {
                    break;
                } // fi
            } // end for
        } else {
            dateList.addAll(statementDates);
        } // fi

        // Save the list into the form, where it's used from.
        form.setAsOfDateList(dateList);

        // populate Vesting Service Feature
        UserProfile user = getUserProfile(request);
        // UserInfo userInfo =
        // SecurityServiceDelegate.getInstance().getUserInfo(user.getPrincipal());
        Contract currentContract = user.getCurrentContract();

        // Don't read the vesting service feature from the vesting engine, as it could be
        // using a historical value. We read it from the contract data.
        final ContractServiceFeature vestingCsf = currentContract.getServiceFeatureMap().get(
                ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);

        final String vestingServiceFeature = StringUtils.trim(vestingCsf.getValue());
        form.setVestingServiceFeature(vestingServiceFeature);

        // Look up the data to set the flags in the form that control the button display.
        boolean isCollect = StringUtils.equals(vestingServiceFeature,
                VestingConstants.VestingServiceFeature.COLLECTION);

        boolean hasUpdatePermission = userProfile.isAllowedSubmitUpdateVesting();

        // Get the selected 'as of' date (also called reporting period end date).
        final Date asOf = new Date(Long.parseLong(form.getAsOfDate()));
        // The as of date is either 'today', or today is reporting period date.
        // boolean validDate = ((!(DateUtils.isSameDay(today, asOf))) &&
        // todayIsNotAReportingPeriodEndDate);
        boolean validDate = ((!(DateUtils.isSameDay(today, asOf))) || (!(todayIsNotAReportingPeriodEndDate)));

        // Lookup data for the last changed user.
        final boolean loggedInUserIsInternal = userProfile.isInternalUser();
        for (CensusVestingDetails censusVestingDetails : details) {
            censusVestingDetails.fetchTipChangedByDisplay(loggedInUserIsInternal);
        } // end for

        // Set a flag indicating if the page is NOT being displayed for printing.
        boolean isNotPrintFriendly = StringUtils.isBlank(request.getParameter("printFriendly"));

        // Set button permissions
        final CensusVestingReportDataUi censusVestingReportDataUi = form
                .getCensusVestingReportDataUi();
        censusVestingReportDataUi.setDisplayEditButton(isNotPrintFriendly && isCollect && validDate
                && hasUpdatePermission && atLeastOneNotFullyVested && inViewMode
                && contractIsNotDiscontinued);

        censusVestingReportDataUi.setDisplaySaveButton(isNotPrintFriendly && isCollect && validDate
                && hasUpdatePermission && inEditMode);

        censusVestingReportDataUi.setDisplayCancelButton(isNotPrintFriendly && isCollect
                && validDate && hasUpdatePermission && inEditMode);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doCommon");
        }
        return forward;
    }
    
    /**
	 * This method is called when reset button is clicked
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 */
    @RequestMapping(value="/censusVesting", params = {"task=reset"}, method = {RequestMethod.GET})
	public String doReset(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              ControllerForward forward = new ControllerForward("refresh",
		      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
		      		return "redirect:" + forward.getPath();
	       }
		}
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReset");
		}
		
		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
		
		//Reset the session object for remebering filter criteria
		if(filterCriteriaVo != null){
			filterCriteriaVo = new FilterCriteriaVo();
		}
		
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
		
		//Reset the form bean
		super.resetForm(form, request);

		String forward = doCommon(form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doReset");
		}
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

    /**
     * Allows the 'task' to be overridden by a submit button.
     * 
     * @param request The {@link HttpServletRequest} for this request.
     * 
     * @see com.manulife.pension.ps.web.report.ReportController#getTask(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected String getTask(HttpServletRequest request) {
        final String taskButton = request.getParameter("taskButton");
        if (StringUtils.isNotBlank(taskButton)) {
            return taskButton;
        } else {
            return super.getTask(request);
        } // fi
    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value="/censusVesting",params = {"task=edit"}, method = {RequestMethod.GET})
	public String doEdit(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              ControllerForward forward = new ControllerForward("refresh",
		      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
		      		return "redirect:" + forward.getPath();
	       }
		}
         if (logger.isDebugEnabled()) {
            logger.debug("entry -> doEdit");
        }

        String forward;
        try {
            forward = doCommon(form, request, response);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

        // ActionForward forward = super.doCommon( reportForm, request, response);

       

        // censusVestingReportForm.cl
        // get the report details and check for warnings
        final CensusVestingReportData censusVestingReportData = (CensusVestingReportData) request
                .getAttribute(Constants.REPORT_BEAN);

        final CensusVestingReportDataUi censusVestingReportDataUi = new CensusVestingReportDataUi(
                censusVestingReportData);

        for (CensusVestingDetails censusVestingDetails : (Collection<CensusVestingDetails>) censusVestingReportData
                .getDetails()) {
            final Map<String, MoneyTypeVestingPercentage> percentagesMap = censusVestingDetails
                    .getPercentages();
            final List<MoneyTypeVO> moneyTypes = censusVestingReportData.getMoneyTypes();
            for (MoneyTypeVO moneyTypeVO : moneyTypes) {
                if (percentagesMap.get(moneyTypeVO.getId()) == null) {
                    // Initialize an object.
                    percentagesMap.put(moneyTypeVO.getId(), new MoneyTypeVestingPercentageVo(
                            moneyTypeVO.getId()));
                }
            }
        }

        // Ensure the data is on the form.
        form.getCensusVestingReportDataUi().setCensusVestingReportData(
                censusVestingReportData);

        // Ensure the form isn't dirty.
        form.setDirty("false");

        List<CensusVestingDetails> censusVestingDetailsList = (List<CensusVestingDetails>) censusVestingReportData
                .getDetails();

        // final String forward;
        try {
            forward = doCommon(form, request, response);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doEdit");
        }
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

    }

    /**
     * {@inheritDoc}
     */
    @RequestMapping(value="/censusVesting",params = {"task=save"}, method = {RequestMethod.GET})
	public String doSave(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			ControllerForward forward = new ControllerForward("refresh",
	      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
	      		return "redirect:" + forward.getPath();
    		}
    	}
      if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSave");
        } // fi

        
        final CensusVestingReportDataUi censusVestingReportDataUi = form
                .getCensusVestingReportDataUi();
        final List<CensusVestingReportDataRowUi> uiRows = censusVestingReportDataUi.getRows();
        final List<CensusVestingDetails> rows = (List<CensusVestingDetails>) censusVestingReportDataUi
                .getCensusVestingReportData().getDetails();

        Date effectiveDate = parseAsOfDate(form.getAsOfDate());
        Date lastUpdated = new Date();
        final UserProfile userProfile = getUserProfile(request);
        String lastUserId = Long.toString(userProfile.getPrincipal().getProfileId());
        String lastUserType = "";// userProfile.getRole();
        String sourceChannelCode = "PC";

        // Find all the rows in the form that have changed from when we loaded the page (in
        // doEdit).
        int rowIndex = 0;
        final ArrayList<MoneyTypeVestingPercentageVo> toSave = new ArrayList<MoneyTypeVestingPercentageVo>();
        for (CensusVestingDetails censusVestingDetails : rows) {
            // }
            // for (CensusVestingReportDataRowUi censusVestingReportDataRowUi : uiRows) {
            final CensusVestingReportDataRowUi censusVestingReportDataRowUi = uiRows
                    .get(rowIndex++);

            final Map<String, MoneyTypeVestingPercentage> percentagesMap = censusVestingDetails
                    .getPercentages();

            // Collection to hold row values.
            boolean rowDataHasChanged = false;

            int moneyTypeIndex = 0;
            for (MoneyTypeVO moneyTypeVO : censusVestingReportDataUi.getCensusVestingReportData()
                    .getMoneyTypes()) {
                if (!(isMoneyTypeFullyVested(moneyTypeVO))) {
                    // Has the value changed?
                    CensusVestingReportDataRowValueUi censusVestingReportDataRowValueUi = censusVestingReportDataRowUi
                            .getMoneyTypeValues().get(moneyTypeIndex);
                    final BigDecimal originalValue = censusVestingReportDataRowValueUi
                            .getOriginalValue();
                    final BigDecimal newValue = censusVestingReportDataRowValueUi
                            .getBigDecimalValue();
                    if (newValue != null) {
                        if ((originalValue == null) || (originalValue.compareTo(newValue) != 0)) {
                            // Save this entire row.
                            rowDataHasChanged = true;
                            // break;
                        } // fi
                    } // fi

                    if ((rowDataHasChanged) && (newValue != null)) {
                        // Save it, only if they've provided a value.
                        MoneyTypeVestingPercentage moneyTypeVestingPercentage = percentagesMap
                                .get(moneyTypeVO.getId());

                        MoneyTypeVestingPercentageVo savePercentage = new MoneyTypeVestingPercentageVo(
                                moneyTypeVestingPercentage);

                        savePercentage.setEffectiveDate(effectiveDate);
                        savePercentage.setLastUpdated(lastUpdated);
                        savePercentage.setLastUserId(lastUserId);
                        savePercentage.setLastUserType(lastUserType);
                        savePercentage.setPercentage(newValue);
                        savePercentage.setSourceChannelCode(sourceChannelCode);
                        savePercentage.setProfileId(Long.parseLong(censusVestingDetails
                                .getProfileId()));

                        toSave.add(savePercentage);
                    } // fi
                } // fi
                moneyTypeIndex++;
            } // end for
        }

        // Validate the changes.
        boolean errorsFound = false;

        String forward;
        // If error found, redisplay the page with the errors.
        if (errorsFound) {
            forward = forwards.get("saveError");
        } else {

            // Now save all the changed rows to the database.
            final VestingEngine vestingEngine;

            Db2VestingDAO vestingDAO;
            try {
                vestingDAO = new Db2VestingDAO(BaseDatabaseDAO
                        .getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));
            } catch (DAOException daoException) {
                throw new RuntimeException(daoException);
            } // end try/catch

            // Db2VestingDAO vestingDAO = new Db2VestingDAO(
            //                    
            // BaseDAO.getCSDataSource());
            // VestingEngine
            vestingEngine = new VestingEngine(vestingDAO);

            // Store the Percentage on CSDB
            for (MoneyTypeVestingPercentageVo moneyTypeVestingPercentage : toSave) {
                try {
                    vestingEngine.storeEmployeeMoneyTypeVestingPercentage(form
                            .getCensusVestingReportDataUi().getCensusVestingReportData()
                            .getContractNumber(), moneyTypeVestingPercentage.getProfileId(),
                            moneyTypeVestingPercentage);
                } catch (VestingException vestingException) {
                    throw new RuntimeException(vestingException);
                } // end try/catch
            } // end for

            // Save successful, so just send to the view.
            try {
                forward = doCommon(form, request, response);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }

        } // fi

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doSave");
        } // fi
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

    private boolean isMoneyTypeFullyVested(MoneyTypeVO moneyTypeVO) {
        return StringUtils.equals(moneyTypeVO.getFullyVested(), MoneyTypeVO.FULLY_VESTED_VALUE_YES);
    }

    /**
     * @see Action#execute(ActionMapping, Form, HttpServletRequest, HttpServletResponse)
     */
    @RequestMapping(value="/censusVesting",params = {"task=cancel" }, method = {RequestMethod.GET})
	public String doCancel(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              ControllerForward forward = new ControllerForward("refresh",
		      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
		      		return "redirect:" + forward.getPath();
	       }
		}
    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCancel");
        }

        final String forward;
        try {
            forward = doCommon( form, request, response);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doCancel");
        }
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

    }

    
    @RequestMapping(value = "/censusVesting", params = {"task=filter"}, method = {RequestMethod.GET})
   	public String doFilter(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
   			throws IOException, ServletException, SystemException {
   		if (bindingResult.hasErrors()) {
   			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   			if (errDirect != null) {
   				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   				ControllerForward forward = new ControllerForward("refresh",
	      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
	      		return "redirect:" + forward.getPath();
   			}
   		}
   		String forward = super.doFilter(form, request, response);
   		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
   	}

   	@RequestMapping(value = "/censusVesting", params = {"task=page"}, method = {RequestMethod.GET})
   	public String doPage(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
   			throws IOException, ServletException, SystemException {
   		if (bindingResult.hasErrors()) {
   			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   			if (errDirect != null) {
   				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   				ControllerForward forward = new ControllerForward("refresh",
	      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
	      		return "redirect:" + forward.getPath();
   			}
   		}
   		String forward = super.doPage(form, request, response);
   		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
   	}

   	@RequestMapping(value = "/censusVesting", params = {"task=sort"}, method = {RequestMethod.GET})
   	public String doSort(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
   			throws IOException, ServletException, SystemException {
   		if (bindingResult.hasErrors()) {
   			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   			if (errDirect != null) {
   				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   				ControllerForward forward = new ControllerForward("refresh",
	      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
	      		return "redirect:" + forward.getPath();
   			}
   		}
   		String forward = super.doSort(form, request, response);
   		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
   	}

   	@RequestMapping(value = "/censusVesting", params = {"task=download"}, method = {RequestMethod.GET})
   	public String doDownload(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
   			throws IOException, ServletException, SystemException {
   		if (bindingResult.hasErrors()) {
   			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   			if (errDirect != null) {
   				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   				ControllerForward forward = new ControllerForward("refresh",
	      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
	      		return "redirect:" + forward.getPath();
   			}
   		}
   		String forward = super.doDownload(form, request, response);
   		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
   	}

   	@RequestMapping(value = "/censusVesting", params = {"task=downloadAll"}, method = {RequestMethod.GET})
   	public String doDownloadAll(@Valid @ModelAttribute("censusVestingReportForm") CensusVestingReportForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
   			throws IOException, ServletException, SystemException {
   		if (bindingResult.hasErrors()) {
   			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   			if (errDirect != null) {
   				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   				ControllerForward forward = new ControllerForward("refresh",
	      				"/do" + new UrlPathHelper().getPathWithinServletMapping(request), true);
	      		return "redirect:" + forward.getPath();
   			}
   		}
   		String forward = super.doDownloadAll(form, request, response);
   		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
   	}
    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getPageSize(javax.servlet.http.HttpServletRequest)
     */
    protected int getPageSize(HttpServletRequest request) {
        UserProfile profile = getUserProfile(request);
        return profile.getPreferences().getInt(UserPreferenceKeys.REPORT_PAGE_SIZE,
                super.getPageSize(request));
    }

    private String escapeField(String field) {
        if (field.indexOf(",") != -1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append("\"").append(field).append("\"");
            return newField.toString();
        } else {
            return field;
        }
    }

    private String getTextFromContent(Message msg) {
        String result = "no content found";
        if (msg == null || StringUtils.isEmpty(msg.getText())) {
            return result;
        } else {
            return msg.getText();
        }
    }

    /**
     * Utility method that performs a warning assesment per each element
     * 
     * @param details
     */
    private void applyBusinessRulesForWarnings(List<CensusVestingDetails> details,
            HttpServletRequest request) throws SystemException {
        Map contractLevelErrors = new HashMap();
        for (CensusVestingDetails element : details) {
            CensusValidationErrors.processVestingWarnings(element, contractLevelErrors);
        }
        setErrors(request, new ArrayList(contractLevelErrors.values()));
    }

    /**
     * Utility method used to save into the request the errors and warnings returned from the
     * vesting engine
     * 
     * @param request
     * @param errors
     */
    protected void setErrors(HttpServletRequest request, List<ValidationError> errors) {
        if (errors != null && errors.size() > 0) {
            request.setAttribute("vestingErrors", new CensusValidationErrors(errors));
        }
        // super.setErrorsInRequest(request, errors);
    }

    /**
     * Building Plan Year End, based on day/month retrieved from the database Year is 8888 and has
     * to be replaced with previous year
     */
    private Date buildPlanYearEnd(Date inDate) {

        // get current year
        GregorianCalendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);

        // build plan year end using day/month and previous year
        cal.setTime(inDate);
        cal.set(Calendar.YEAR, year - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }
    
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
    @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(censusVestingReportValidator);
	}
    
}