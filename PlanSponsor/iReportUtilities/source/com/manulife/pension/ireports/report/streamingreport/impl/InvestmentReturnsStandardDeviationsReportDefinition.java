package com.manulife.pension.ireports.report.streamingreport.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.ReportDefinition;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.StandardReportTemplate;
import com.manulife.pension.ireports.report.util.BottomAlignCellDecoratorFactory;
import com.manulife.pension.ireports.report.util.CMAContentHelper;
import com.manulife.pension.ireports.report.viewbean.FundExpensesViewBean;
import com.manulife.pension.ireports.report.viewbean.FundMetricsViewBean;
import com.manulife.pension.ireports.report.viewbean.FundStandardDeviationViewBean;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundMetrics;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfRGB;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

public class InvestmentReturnsStandardDeviationsReportDefinition implements ReportDefinition {

    private static final String REPORT_NAME = "Investment returns and standard deviation";

    private static final String[] COLUMN_WIDTHS_EXPANDED = new String[] { "9.3cm", // Investment
            "5cm", // Asset Manager
            "1.4cm", // Date Introduced
            "0.2cm", // spacer
            "1.2cm", // Annualized investment returns 1yr
            "1.3cm", // Annualized investment returns 3yr
            "1.3cm", // Annualized investment returns 5yr
            "1.7cm", // Annualized investment returns 10yr/inception
            "0.2cm", // spacer
            "1.3cm", // returns 1yr
            "1.3cm", // returns 5yr
            "1.6cm", // returns 10yr/inception
            "0.2cm", // spacer
            "1.4cm", // Standard deviations 3yr
            "1.4cm", // Standard deviations 5yr
            "1.4cm", // Standard deviations 10yr
            "0.2cm", // spacer
            "2.7cm" // ER
    };

    private static final String[] COLUMN_WIDTHS_NORMAL = new String[] { "10cm", // Investment
            // Options
            "8.5cm", // Asset Manager
            "1.4cm", // Date Introduced
            "0.2cm", // spacer
            "1.4cm", // Annualized returns 1yr
            "1.5cm", // Annualized returns 3yr
            "1.5cm", // Annualized returns 5yr
            "1.5cm", // Annualized returns 10yr/inception
            "0.2cm", // spacer
            "1.5cm", // Standard deviations 3yr
            "1.5cm", // Standard deviations 5yr
            "1.5cm", // Standard deviations 10yr
            "0.2cm", // spacer
            "2.7cm" // ER
    };

    private String[] COLUMN_WIDTHS = COLUMN_WIDTHS_EXPANDED;

    private boolean isStardizedColumnsSuppresed = false;

    public String getReportName() {
        return REPORT_NAME;
    }

    public String[] getAsOfDateContexts() {
        return new String[] { StandardReportsConstants.REPORT_CONTEXT_FUND_RETURNS,
                StandardReportsConstants.REPORT_CONTEXT_MORNINGSTAR_CAT_PERFORMANCE,
                StandardReportsConstants.REPORT_CONTEXT_MARKET_INDEX_IB_PERFORMANCE,
                StandardReportsConstants.REPORT_CONTEXT_FUND_STD_DEVIATIONS };
    }

    public String[] getStaticFootnoteSymbols() {
        return new String[] { FOOTNOTE_PERFORMANCE_HISTORY, FOOTNOTE_INVESTMENT_OPTIONS,
                FOOTNOTE_FUND_MANAGER, // FOOTNOTE_EXPENSES, FOOTNOTE_MAINTAINENANCE_CHARGE,
                FOOTNOTE_AIC, FOOTNOTE_INCEPTION_DATE, FOOTNOTE_STANDARD_DEVIATION,
                FOOTNOTE_3Y_5Y_10Y_GIC, FOOTNOTE_RETIREMENT_CHOICES_MARKET_INDEX, 
                FOOTNOTE_RETIREMENT_LIVING_MARKET_INDEX, FOOTNOTE_LIFESTYLE_MARKET_INDEX,
                FOOTNOTE_SUPERSCRIPT_31, FOOTNOTE_SUPERSCRIPT_32, FOOTNOTE_SUPERSCRIPT_33,
                FOOTNOTE_SUPERSCRIPT_34, FOOTNOTE_SUPERSCRIPT_35};
    }

    public boolean containsClassDisclosureFootnote() {
        return false;
    }

    public boolean isLandscape() {
        return false;
    }

    public boolean isLegalSize() {
        return true;
    }

    public boolean isHeaderParagraphPresent() {
        return false;
    }

    public String[] getColumnWidths() {
        return COLUMN_WIDTHS;
    }

    public int getNumberOfColumns() {
        return COLUMN_WIDTHS.length;
    }

    public boolean containsGicSection() {
        return true;
    }

    public int getGicSectionAnnualRateColumn() {
        return 2;
    }

    public boolean containsMarketIndexSectionRiskReturn() {
        return false;
    }

    public boolean containsMarketIndexSectionAssetClass() {
        return false;
    }

    public void justifyReportBodyColumns(PdfTableBody tableBody) {
        for (int j = 4; j < getNumberOfColumns(); j++) {
            tableBody.setColumnTextAlign(j, "right");
        }
    }

    public void buildFundRow(StandardReportBuilder report, PdfTableBody fundTableBody, int row,
            ReportFund reportFund, FundReportData reportData) {
        TextStyle normalTextStyle = new TextStyle(report,
                ReportFormattingConstants.FRUTIGER47LIGHT_CN,
                ReportFormattingConstants.FONT_SIZE_FUND_ROW);

        Fund fund = reportFund.getFund();

        
        PdfParagraph fundNameParagraph = normalTextStyle.createParagraph(StandardReportsUtils
                .getValueorNA(fund.getFundLongName()));
        PdfTableRegion fundNameTableBody = report.createTableBody(1, 2);
		PdfTableRegion[] fundNameTableRegions = new PdfTableRegion[]{fundNameTableBody};
		PdfTable fundNameTable = null;
		if (isStandardizedReportColumnsSupressed()) {
			fundNameTable = report.createTable(new String[] {"0.2cm", "9.1cm"}, fundNameTableRegions);
		} else {
			fundNameTable = report.createTable(new String[] {"0.2cm", "9.8cm"}, fundNameTableRegions);
		}
		
		if (reportData.getFeeWaiverFundIds().contains(fund.getInvestmentid()) && (reportData.isMerrillAdvisor() && reportData.getRestrictedFunds().containsKey(fund.getInvestmentid()))) {
			fundNameTableBody.add(0, 0, report.createParagraph(DOT_INDICATOR+"\n"+MERRILL_RESRICTED_FUND_SYMBOL));
		} else if (reportData.getFeeWaiverFundIds().contains(fund.getInvestmentid())) {
			fundNameTableBody.add(0, 0, report.createParagraph(DOT_INDICATOR));
		} else if (reportData.isMerrillAdvisor() && reportData.getRestrictedFunds().containsKey(fund.getInvestmentid()) 
				&& !(fund.getInvestmentid().equals("XX05") || fund.getInvestmentid().equals("XX03") || fund.getInvestmentid().equals("XX11") || fund.getInvestmentid().equals("XX14"))) {
			fundNameTableBody.add(0, 0, report.createParagraph(MERRILL_RESRICTED_FUND_SYMBOL));
		} else {
			fundNameTableBody.add(0, 0, report.createParagraph(" "));
		}
		
		fundNameTableBody.add(0, 1, fundNameParagraph);
		StandardReportTemplate.attachFootnotes(fundNameParagraph, reportFund, report);
		fundTableBody.add(row, 0, fundNameTable);
        
        fundTableBody.add(row, 1, normalTextStyle
                .createParagraph(StandardReportsUtils.getValueorNA(fund.getInvmanagername())));
        FundMetrics metrics = reportFund.getFundMetrics();
        FundMetricsViewBean metricsViewBean = new FundMetricsViewBean(metrics);
        fundTableBody.add(row, 2, normalTextStyle
                .createParagraph(metrics.getInceptionDate() == null ? StandardReportsConstants.NA : DateFormatUtils.format(
                        metrics.getInceptionDate(), DATE_PATTERN_FUND_DATA)));

        fundTableBody.add(row, 3, report.createParagraph());

        fundTableBody.add(row, 4, StandardReportTemplate.getMetricValueParagraph(report,
                metricsViewBean, ROR_1YR, FONT_SIZE_FUND_ROW));
        fundTableBody.add(row, 5, StandardReportTemplate.getMetricValueParagraph(report,
                metricsViewBean, ROR_3YR, FONT_SIZE_FUND_ROW));
        fundTableBody.add(row, 6, StandardReportTemplate.getMetricValueParagraph(report,
                metricsViewBean, ROR_5YR, FONT_SIZE_FUND_ROW));

        if (checkIf10YearRorExists(metricsViewBean, ROR_10YR))
            fundTableBody.add(row, 7, StandardReportTemplate.getMetricValueParagraph(report,
                    metricsViewBean, ROR_10YR, FONT_SIZE_FUND_ROW));
        else
            fundTableBody.add(row, 7, StandardReportTemplate.getMetricValueParagraphItalicied(
                    report, metricsViewBean, ROR_SINCEINCEPTION, FONT_SIZE_FUND_ROW));

        fundTableBody.add(row, 8, report.createParagraph());

        if (isStandardizedReportColumnsSupressed()) {
            FundStandardDeviationViewBean standardDeviations = new FundStandardDeviationViewBean(
                    reportFund.getFundStandardDeviation());
            fundTableBody.add(row, 9, StandardReportTemplate.getMetricValueParagraph(report,
                    standardDeviations, ROR_3YR, FONT_SIZE_FUND_ROW));
            fundTableBody.add(row, 10, StandardReportTemplate.getMetricValueParagraph(report,
                    standardDeviations, ROR_5YR, FONT_SIZE_FUND_ROW));
            fundTableBody.add(row, 11, StandardReportTemplate.getMetricValueParagraph(report,
                    standardDeviations, ROR_10YR, FONT_SIZE_FUND_ROW));

            FundExpensesViewBean fundExpenses = new FundExpensesViewBean(reportFund
                    .getFundExpenses());

            fundTableBody.add(row, 12, report.createParagraph());

            fundTableBody.add(row, 13, StandardReportTemplate.getBeanValueParagraph(report,
                    fundExpenses, "annualInvestmentCharge", FONT_SIZE_FUND_ROW));

            setCellFormattingForRow(fundTableBody, row, false);
        } else {

            fundTableBody.add(row, 9, StandardReportTemplate.getMetricValueParagraph(report,
                    metricsViewBean, ROR_1YR_QE, FONT_SIZE_FUND_ROW));
            fundTableBody.add(row, 10, StandardReportTemplate.getMetricValueParagraph(report,
                    metricsViewBean, ROR_5YR_QE, FONT_SIZE_FUND_ROW));

            if (checkIf10YearRorExists(metricsViewBean, ROR_10YR_QE)) {
                fundTableBody.add(row, 11, StandardReportTemplate.getMetricValueParagraph(report,
                        metricsViewBean, ROR_10YR_QE, FONT_SIZE_FUND_ROW));
            } else {
                fundTableBody.add(row, 11, StandardReportTemplate.getMetricValueParagraphItalicied(
                        report, metricsViewBean, ROR_SINCEINCEPTION_QE, FONT_SIZE_FUND_ROW));
            }

            fundTableBody.add(row, 12, report.createParagraph());

            FundStandardDeviationViewBean standardDeviations = new FundStandardDeviationViewBean(
                    reportFund.getFundStandardDeviation());
            fundTableBody.add(row, 13, StandardReportTemplate.getMetricValueParagraph(report,
                    standardDeviations, ROR_3YR, FONT_SIZE_FUND_ROW));
            fundTableBody.add(row, 14, StandardReportTemplate.getMetricValueParagraph(report,
                    standardDeviations, ROR_5YR, FONT_SIZE_FUND_ROW));
            fundTableBody.add(row, 15, StandardReportTemplate.getMetricValueParagraph(report,
                    standardDeviations, ROR_10YR, FONT_SIZE_FUND_ROW));

            FundExpensesViewBean fundExpenses = new FundExpensesViewBean(reportFund
                    .getFundExpenses());

            fundTableBody.add(row, 16, report.createParagraph());

            fundTableBody.add(row, 17, StandardReportTemplate.getBeanValueParagraph(report,
                    fundExpenses, "annualInvestmentCharge", FONT_SIZE_FUND_ROW));

            setCellFormattingForRow(fundTableBody, row, false);

        }
    }

    public PdfTableHeader buildMainTableHeader(StandardReportBuilder report,
            String classDisclosureFootnoteNumber, FundReportData reportData, ReportOptions options) {

        PdfTableHeader tableHeader = report.createTableHeader(3, getNumberOfColumns(),
                new BottomAlignCellDecoratorFactory());
        // This logic for aligning the Headers       
        if (isStandardizedReportColumnsSupressed()) {
        	for (int j = 4; j < 12; j++) {
        		tableHeader.setColumnTextAlign(j, "center");
        	}
        	for (int j = 13; j < 14; j++) {
        		tableHeader.setColumnTextAlign(j, "right");
        	}
        }
        else
        {
        	for (int j = 4; j < 16; j++) {
        		tableHeader.setColumnTextAlign(j, "center");
        	}
        	for (int j = 17; j < 18; j++) {
        		tableHeader.setColumnTextAlign(j, "right");
        	}
        }

        tableHeader.add(0, 0, report.createParagraph());
        tableHeader.add(0, 1, report.createParagraph());
        tableHeader.add(0, 2, report.createParagraph());
        tableHeader.add(0, 3, report.createParagraph());

        PdfParagraph introDisclosureParagraph = createIntroDisclosures(report, "64925", options
                .getCompanyId());
        tableHeader.add(0, 4, introDisclosureParagraph);
        tableHeader.setCellPaddingBottom(0, 3, "0.2cm");

        if (!isStandardizedReportColumnsSupressed()) {
            tableHeader.spanCells(0, 4, 1, 12);
            tableHeader.add(0, 16, report.createParagraph());
            tableHeader.add(0, 17, report.createParagraph());
        } else {
            tableHeader.spanCells(0, 4, 1, 8);
            tableHeader.add(0, 12, report.createParagraph());
            tableHeader.add(0, 13, report.createParagraph());
        }

        tableHeader.add(1, 0, createHeaderText(report, "Investment")).add(
                1,
                0,
                createHeaderText(report, "Options").add(
                        Footnotes.createFootnote(report, FOOTNOTE_INVESTMENT_OPTIONS)));
        tableHeader.spanCells(1, 0, 2, 1);
       // Modifications are done for ACR REWRITE (iReport) 
        if(ReportFormattingConstants.SELECTED_VALUE.equalsIgnoreCase(options.getSelectedValue())){
        	tableHeader.add(1, 1, createHeaderText(report, "Manager Name/")).add(
        			1,
        			1,
        			createHeaderText(report, "Benchmark").add(
        					Footnotes.createFootnote(report, FOOTNOTE_FUND_MANAGER)));
        }
        else
        {
        	tableHeader.add(1, 1, createHeaderText(report, "Manager Name").add(
        			Footnotes.createFootnote(report, FOOTNOTE_FUND_MANAGER)));
        }
        tableHeader.spanCells(1, 1, 2, 1);
        tableHeader.add(1, 2, createHeaderText(report, "Inception")).add(
                1,
                2,
                createHeaderText(report, "Date").add(
                        Footnotes.createFootnote(report, FOOTNOTE_INCEPTION_DATE)));
        tableHeader.spanCells(1, 2, 2, 1);

        tableHeader.add(1, 3, createHeaderText(report, ""));
        tableHeader.spanCells(1, 3, 2, 1);

        tableHeader.add(1, 4, createHeaderText(
                report,
                "Returns as of "
                        + DateFormatUtils.format(reportData.getAsOfDateForContext("ROR"),
                                ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE)).add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).setTextAlignment(
                "center"));

        underlineCells(tableHeader, 1, 4, 7);
        tableHeader.spanCells(1, 4, 1, 4);

        tableHeader.add(2, 4, createHeaderText(report, "1yr"));
        tableHeader.add(2, 5, createHeaderText(report, "3yr"));
        tableHeader.add(2, 6, createHeaderText(report, "5yr"));
        tableHeader.add(
                2,
                7,
                report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(
                        FRUTIGER67BOLD_CN).add(report.createText("10yr/"))).add(
                2,
                7,
                report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(
                        FRUTIGER67BOLD_CN).add(
                        report.createText("Inception").setFontFamily(FRUTIGER66BOLD_ITALIC)).add(
                        Footnotes.createFootnote(report, FOOTNOTE_INCEPTION_DATE)));

        // spacer
        tableHeader.add(2, 8, createHeaderText(report, "  "));
        tableHeader.add(1, 8, report.createParagraph());

        if (isStandardizedReportColumnsSupressed()) {

            tableHeader.add(1, 9, createHeaderText(
                    report,
                    "Standard Deviation as of "
                            + DateFormatUtils.format(reportData.getAsOfDateForContext("DEVQE"),
                                    ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE)).add(
                    Footnotes.createFootnote(report, FOOTNOTE_STANDARD_DEVIATION))
                    .setTextAlignment("center"));
            underlineCells(tableHeader, 1, 9, 11);
            tableHeader.spanCells(1, 9, 1, 3);

            tableHeader.add(1, 12, report.createParagraph());

            tableHeader.add(1, 13, createHeaderText(
                    report,
                    "As of  "
                            + DateFormatUtils.format(reportData.getAsOfDateForContext("FER"),
                                    ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE))
                    .setTextAlignment("center"));
            underlineCells(tableHeader, 1, 13, 14);
            tableHeader.spanCells(1, 13, 1, 1);

            tableHeader.add(2, 9, createHeaderText(report, "3yr"));
            tableHeader.add(2, 10, createHeaderText(report, "5yr"));
            tableHeader.add(2, 11, createHeaderText(report, "10yr"));

            // spacer
            tableHeader.add(2, 12, createHeaderText(report, "  "));

            PdfParagraph erHeader = createHeaderText(report, "Expense Ratio").add(Footnotes.createFootnote(report, FOOTNOTE_AIC));
            tableHeader.add(2, 13, erHeader);
            tableHeader.setBorderBottom("solid");

        } else {
            tableHeader.add(2, 9, createHeaderText(report, "1yr"));
            tableHeader.add(2, 10, createHeaderText(report, "5yr"));
            tableHeader.add(
                    2,
                    11,
                    report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(
                            FRUTIGER67BOLD_CN).add(report.createText("10yr/"))).add(
                    2,
                    11,
                    report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(
                            FRUTIGER67BOLD_CN).add(
                            report.createText("Inception").setFontFamily(FRUTIGER66BOLD_ITALIC)).add(
                            Footnotes.createFootnote(report, FOOTNOTE_INCEPTION_DATE)));
            // spacer
            tableHeader.add(2, 12, createHeaderText(report, "  "));
            tableHeader.add(1, 12, report.createParagraph());

            tableHeader.add(1, 9, createHeaderText(
                    report,
                    "Returns as of  "
                            + DateFormatUtils.format(reportData.getAsOfDateForContext("RORQE"),
                                    ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE))
                    .setTextAlignment("center"));
            underlineCells(tableHeader, 1, 9, 11);
            tableHeader.spanCells(1, 9, 1, 3);

            tableHeader.add(1, 13, createHeaderText(
                    report,
                    "Standard Deviation as of "
                            + DateFormatUtils.format(reportData.getAsOfDateForContext("DEVQE"),
                                    ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE)).add(
                    Footnotes.createFootnote(report, FOOTNOTE_STANDARD_DEVIATION))
                    .setTextAlignment("center"));
            underlineCells(tableHeader, 1, 13, 15);
            tableHeader.spanCells(1, 13, 1, 3);

            tableHeader.add(1, 16, report.createParagraph());

            tableHeader.add(1, 17, createHeaderText(
                    report,
                    "As of  "
                            + DateFormatUtils.format(reportData.getAsOfDateForContext("FER"),
                                    ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE))
                    .setTextAlignment("center"));
            underlineCells(tableHeader, 1, 17, 18);
            tableHeader.spanCells(1, 17, 1, 1);

            tableHeader.add(2, 13, createHeaderText(report, "3yr"));
            tableHeader.add(2, 14, createHeaderText(report, "5yr"));
            tableHeader.add(2, 15, createHeaderText(report, "10yr"));

            // spacer
            tableHeader.add(2, 16, createHeaderText(report, "  "));

            PdfParagraph erHeader = createHeaderText(report, "Expense Ratio").add(Footnotes.createFootnote(report, FOOTNOTE_AIC));

            tableHeader.add(2, 17, erHeader);
            tableHeader.setBorderBottom("solid");

        }

        setCellFormattingForRow(tableHeader, 1, false);
        setCellFormattingForRow(tableHeader, 2, false);

        return tableHeader;
    }

    public void setCellFormattingForRow(PdfTableRegion tableRegion, int row, boolean setTopBorder) {
        if (setTopBorder) {
            for (int i = 0; i < getNumberOfColumns(); i++) {
                tableRegion.setCellBorderTopStyle(row, i, "solid");
                tableRegion.setCellBorderTopWidth(row, i, "0.5pt");
                tableRegion.setCellBorderTopColor(row, i, PDF_RGB_ROW_BORDER);
            }
        }

        // set shading for certain columns
        for (int i = 4; i < 8; i++) {
            tableRegion.setCellBackgroundColor(row, i, ReportFormattingConstants.PDF_RGB_SHADING_1);
        }
        if (!isStandardizedReportColumnsSupressed()) {

            for (int i = 9; i < 12; i++) {
                tableRegion.setCellBackgroundColor(row, i,
                        ReportFormattingConstants.PDF_RGB_SHADING_2);
            }
            // set shading for certain columns
            for (int i = 13; i < 16; i++) {
                tableRegion.setCellBackgroundColor(row, i,
                        ReportFormattingConstants.PDF_RGB_SHADING_1);
            }

            // set shading for certain columns
            for (int i = 17; i < 18; i++) {
                tableRegion.setCellBackgroundColor(row, i,
                        ReportFormattingConstants.PDF_RGB_SHADING_2);
            }
        } else {

            // set shading for certain columns
            for (int i = 9; i < 12; i++) {
                tableRegion.setCellBackgroundColor(row, i,
                        ReportFormattingConstants.PDF_RGB_SHADING_2);
            }

            // set shading for certain columns
            for (int i = 13; i < 14; i++) {
                tableRegion.setCellBackgroundColor(row, i,
                        ReportFormattingConstants.PDF_RGB_SHADING_1);
            }
        }
    }

    private PdfParagraph createHeaderText(StandardReportBuilder report, String text) {
        return report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(
                FRUTIGER67BOLD_CN).add(report.createText(text));
    }

    private void underlineCells(PdfTableRegion tableRegion, int rowIndex, int startColumn,
            int endColumn) {
        if (startColumn <= endColumn) {
            for (int col = startColumn; col < endColumn; col++) {
                tableRegion.setCellBorderBottomStyle(rowIndex, col, "solid");
                tableRegion.setCellBorderBottomColor(rowIndex, col, new PdfRGB(188, 190, 192));
                tableRegion.setCellBorderBottomWidth(rowIndex, col, "0.5pt");
            }
        }
    }

    public String[] getIntroDisclosuresContentIds() {
        return new String[] { "64996", "65000","90936"};
    }
    
    public String[] getIntroGenericLevelDisclosuresContentIds() {
        return new String[] { "97640" };
    }
    
    public String[] getIntroContractLevelDisclosuresContentIds() {
        return new String[] { "97641" };
    }
    
    public String[] getIncludedMerrillDisclosuresContentIds() {
        return new String[] { "64996", "65000","90936", "96181" };
    }
    //removed the key as part of ACR REWRITE (iReport changes)
    public String[] getGeneralDisclosureContentIds() {
        return new String[] { "64507", "64508", "64509", "64510", "65002" ,"87446" };
    }

    public PdfTable buildLastRow(StandardReportBuilder report, int row, FundReportData reportData) {
        return null;
    }

    private boolean checkIf10YearRorExists(FundMetricsViewBean metricsViewBean, String attribute10yr) {
        String ror10year = null;
        try {
            ror10year = (String) PropertyUtils.getSimpleProperty(metricsViewBean, attribute10yr);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NestableRuntimeException(e);
        }
        if (StandardReportsConstants.NA.equals(ror10year))
            return false;
        else
            return true;
    }

    public void hideStandardizedReportColumns() {
        COLUMN_WIDTHS = COLUMN_WIDTHS_NORMAL;
        isStardizedColumnsSuppresed = true;
    }

    public boolean isStandardizedReportColumnsSupressed() {
        return isStardizedColumnsSuppresed;
    }

    public boolean containsStandardizedReportColumns() {
        // TODO Auto-generated method stub
        return true;
    }

    protected PdfParagraph createIntroDisclosures(StandardReportBuilder report, String contentId,
            String companyId) {
        PdfParagraph contentParagraph = null;

        contentParagraph = CMAContentHelper.createIntroDisclosureParagraph(report, contentId,
                companyId);

        return contentParagraph;
    }

    public PdfTable buildLastRow(StandardReportBuilder report, int row) {
        // TODO Auto-generated method stub
        return null;
    }

    public PdfTableBody buildDisclosureTextRow(StandardReportBuilder report,
            PdfTableBody tableBody, int row, int numberOfColumns, ReportFund reportFund) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isShowMMFDisclosureText() {
        // TODO Auto-generated method stub
        return false;
    }

}
