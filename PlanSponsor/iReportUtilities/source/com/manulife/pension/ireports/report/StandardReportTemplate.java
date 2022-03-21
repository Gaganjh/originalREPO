package com.manulife.pension.ireports.report;

import java.awt.Color;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.dao.ContractDAO;
import com.manulife.pension.ireports.dao.DAOFactory;
import com.manulife.pension.ireports.model.Contract;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.isf.ISFGenerator;
import com.manulife.pension.ireports.report.streamingreport.StreamingReport;
import com.manulife.pension.ireports.report.streamingreport.impl.TextStyle;
import com.manulife.pension.ireports.report.util.AssetHouseFundCollator;
import com.manulife.pension.ireports.report.util.AssetHouseFundMapping;
import com.manulife.pension.ireports.report.util.CMAContentHelper;
import com.manulife.pension.ireports.report.util.RiskReturnCollator;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.platform.utility.util.ContentHelper;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.GARates;
import com.manulife.util.pdf.PdfColors;
import com.manulife.util.pdf.PdfLeader;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfRGB;
import com.manulife.util.pdf.PdfReportPrinter;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;
import com.manulife.util.pdf.PdfText;
import com.manulife.util.pdf.ReportPrinter;
import com.manulife.util.render.NumberRender;

/**
 * This class represents the standard report template
 * 
 * @author Siby Thomas
 */
public abstract class StandardReportTemplate implements StreamingReport, ReportFormattingConstants {
    
    public static final int GIC_ASSETCLASS = 0;

    public static final int GIC_RISKRETURN = 1;

    public static Map<String, String> GIC_HEADERS = new HashMap<String, String>();

    public ReportOptions options = null;

    protected boolean isLandscape = false;

    protected boolean isLegalSize = false;

    protected StandardReportBuilder report = new StandardReportBuilder();

    protected ReportPrinter reportPrinter = new PdfReportPrinter();

    protected boolean isNewYork;

    protected String classDisclosureFootnoteNumber;

    static {
        GIC_HEADERS.put("3YC", "3-year");
        GIC_HEADERS.put("5YC", "5-year");
        GIC_HEADERS.put("10YC", "10-year");
    }

    public StandardReportTemplate(ReportOptions options) {
        this.options = options;
        this.isNewYork = StandardReportsUtils.isNewYork(options.getCompanyId());
        this.classDisclosureFootnoteNumber = determineClassDisclosureFootnoteNumber(options);
    }

    public void writeTo(OutputStream out) {
        this.report.print(this.reportPrinter, out);
        // this.report.printAsFO(System.out);
    }

    public void writeToFO(OutputStream out) {
        this.report.printAsFO(out);
    }

    protected StandardReportPageSequence createPageSequence(StandardReportBuilder report,
            String reportName, String headerParagraph, String masterRefernce,
            Map<String, PdfParagraph> firstPageTitleStaticParagraph,
            Map<String, PdfParagraph> subsequentPageTitleStaticParagraph) {
        StandardReportPageSequence pageSequence = new StandardReportPageSequence(report,
                reportName, options, isLandscape(), isLegalSize(), headerParagraph, masterRefernce,
                firstPageTitleStaticParagraph, subsequentPageTitleStaticParagraph);
        report.addPageSequence(pageSequence);
        return pageSequence;
    }

    protected CustomPageSequence createCustomPageSequence(StandardReportBuilder report,
            String headerParagraph, String masterRefernce,
            Map<String, PdfParagraph> firstPageTitleStaticParagraph,
            Map<String, PdfParagraph> subsequentPageTitleStaticParagraph) {
        CustomPageSequence pageSequence = new CustomPageSequence(report, options, 
                isLandscape(), isLegalSize(), headerParagraph, masterRefernce,
                firstPageTitleStaticParagraph,
                subsequentPageTitleStaticParagraph);
        report.addPageSequence(pageSequence);
        return pageSequence;
    }

    protected CustomPageSequence createCustomPageSequence(StandardReportBuilder report,
            String headerParagraph, String masterRefernce,
            Map<String, PdfParagraph> firstPageTitleStaticParagraph,
            Map<String, PdfParagraph> subsequentPageTitleStaticParagraph, 
            int footnoteDisclosureContentID) {
        CustomPageSequence pageSequence = new CustomPageSequence(report, options, 
                isLandscape(), isLegalSize(), headerParagraph, masterRefernce,
                firstPageTitleStaticParagraph,
                subsequentPageTitleStaticParagraph, footnoteDisclosureContentID);
        report.addPageSequence(pageSequence);
        return pageSequence;
    }

    protected void createPageLayoutStructure(StandardReportBuilder report,
            boolean includeHeaderParagraph, boolean containsContractOrShortlistParagraph) {
        StandardReportsLayoutMasterSet masterSet = new StandardReportsLayoutMasterSet(report, this
                .isLandscape(), this.isLegalSize(), includeHeaderParagraph,
                containsContractOrShortlistParagraph);
        report.setLayoutMasterSet(masterSet);
    }

    public static PdfParagraph getMetricValueParagraph(StandardReportBuilder report,
            Object viewBean, String propertyName, int fontSize) {
        return getMetricValueParagraph(report, viewBean, propertyName, fontSize, FRUTIGER_SR);
    }

    public static PdfParagraph getMetricValueParagraph(StandardReportBuilder report,
            Object viewBean, String propertyName, int fontSize, String fontFamily) {
        PdfParagraph result = null;
        result = getBeanValueParagraph(report, viewBean, propertyName, fontSize);
        boolean isHypothetical = false;
        try {
            isHypothetical = ((Boolean) PropertyUtils.getSimpleProperty(viewBean, propertyName
                    + "Hypothetical")).booleanValue();
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NestableRuntimeException(e);
        }
        if (isHypothetical) {
            result.setFontSize(fontSize).setFontWeight("bold").setFontFamily(fontFamily);
        }
        return result;
    }

    public static PdfParagraph getMetricValueParagraphItalicied(StandardReportBuilder report,
            Object viewBean, String propertyName, int fontSize) {
        return getMetricValueParagraphItalicied(report, viewBean, propertyName, fontSize, FRUTIGER_SR);
    }

    public static PdfParagraph getMetricValueParagraphItalicied(StandardReportBuilder report,
            Object viewBean, String propertyName, int fontSize, String fontFamily) {
        PdfParagraph result = null;
        result = getBeanValueParagraph(report, viewBean, propertyName, fontSize);
        result.setFontStyle("italic");
        boolean isHypothetical = false;
        try {
            isHypothetical = ((Boolean)PropertyUtils.getSimpleProperty(viewBean, propertyName + "Hypothetical")).booleanValue();
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NestableRuntimeException(e);
        }
        if (isHypothetical) {
            result
            .setFontSize(fontSize)
            .setFontWeight("bold")
            .setFontFamily(fontFamily);
        } 
        return result;
    }

    public static PdfParagraph getBeanValueParagraph(StandardReportBuilder report, Object bean,
            String propertyName, int fontSize) {
        PdfParagraph result = null;
        String value = null;
        try {
            value = (String) PropertyUtils.getSimpleProperty(bean, propertyName);
            // Double checking if the value is null. If yes, changing it to "n/a".
            if (value == null) {
                value = StandardReportsConstants.NA;
            }
            result = report.createParagraph().setFontSize(fontSize).setFontFamily(
                    FRUTIGER47LIGHT_CN).add(report.createText(value));
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NestableRuntimeException(e);
        }
        return result;
    }

    protected PdfParagraph createFootnotesHeader(StandardReportBuilder report, Map footnotes) {
        PdfParagraph paragraph = report.createParagraph().setSpan("all");
        paragraph.setBreakBefore("page");
        if (footnotes != null) {
            Set footnoteEntries = footnotes.entrySet();
            if (!footnoteEntries.isEmpty()) {
                PdfLeader leader = report.createLeader();
                leader.setColor("black");
                if (this.isLegalSize) {
                    leader.setLeaderLength("12.9in");
                } else if (this.isLandscape()) {
                    leader.setLeaderLength("10.0in");
                } else {
                    leader.setLeaderLength("7.5in");
                }
                leader.setRuleThickness("0.5pt");
                leader.setLeaderPattern("rule");
                paragraph.add(report.createParagraph().add(leader));
                paragraph.add(report.createParagraph("Footnotes").setFontFamily(FRUTIGER67BOLD_CN)
                        .setFontSize(10));
                paragraph.add(report.createParagraph("  "));
            }
        }
        return paragraph;
    }

    protected PdfParagraph createFootnotes(StandardReportBuilder report, Map footnotes) {
        PdfParagraph paragraph = report.createParagraph().setSpan("none");
        if (footnotes != null) {
            Set footnoteEntries = footnotes.entrySet();
            for (Iterator iter = footnoteEntries.iterator(); iter.hasNext();) {
                Entry footnoteEntry = (Entry) iter.next();
                String symbol = (String) footnoteEntry.getKey();
                String footnoteText = (String) footnoteEntry.getValue();
                PdfParagraph footnoteLine = report.createParagraph();
                if (symbol != null) {
                    footnoteLine.add(report.createText(symbol).setBaseLineShift("super")
                            .setFontSize(FONT_SIZE_FOOTNOTE_SYMBOL));
                    footnoteLine.add(report.createText(" "));
                }
                if (symbol != null && footnoteText != null) {
                    PdfText footnoteLineInlineText = CMAContentHelper.createInlineForCMAText(
                            report, footnoteText);
                    if (footnoteLineInlineText != null) {
                        footnoteLine.add(footnoteLineInlineText
                                .setFontSize(FONT_SIZE_FOOTNOTE_TEXT));
                    }
                }
                paragraph.add(footnoteLine);
            }
        }
        return paragraph;
    }
    
    /**
     * Method to Create Morningstar Footnotes
     * 
     * @param report
     * @param footnotes
     * 
     * @return PdfParagraph
     */
    protected PdfParagraph createMorningstarFootnotes(StandardReportBuilder report, Map footnotes) {
        PdfParagraph paragraph = report.createParagraph().setSpan(NONE);
        if (footnotes != null) {
            Set footnoteEntries = footnotes.entrySet();
            for (Iterator iter = footnoteEntries.iterator(); iter.hasNext();) {
                Entry footnoteEntry = (Entry) iter.next();
                String symbol = (String) footnoteEntry.getKey();
                String footnoteText = (String) footnoteEntry.getValue();
                PdfParagraph footnoteLine = report.createParagraph();
                if (symbol != null && footnoteText != null) {
                    PdfText footnoteLineInlineText = CMAContentHelper.createInlineForCMAText(
                            report, footnoteText);
                    if (footnoteLineInlineText != null) {
                        footnoteLine.add(footnoteLineInlineText
                                .setFontSize(FONT_SIZE_FOOTNOTE_TEXT));
                    }
                }
                paragraph.add(footnoteLine);
            }
        }
        return paragraph;
    }
    
    protected PdfParagraph createStandardRiskDisclosure(StandardReportBuilder report) {
    	PdfParagraph disclosureParagraph = report.createParagraph();
		String contentText = ContentHelper.getContentText(65816, ContentTypeManager.instance().DISCLAIMER,
				CMAContentHelper.getLocation(options.getCompanyId()));
		disclosureParagraph = CMAContentHelper.createParagraphForCMAText(report, contentText)
								.setPaddingTop("0.2cm").setFontSize(FONT_SIZE_FOOTNOTE_TEXT).setSpan("none");
        return disclosureParagraph;
    }

    /**
     * This is a work-around for the lack of support of conditional-page-master page-position="last"
     * in the current version of fop.
     * 
     * @param report
     * @param reportDefinition
     * @return
     */
    protected PdfParagraph createStandardFootnoteBlock(StandardReportBuilder report,
            String reportName, String[] generalDisclosureContentIds) {
        PdfParagraph standardFootnoteBlock = report.createParagraph().setSpan("all");
        PdfLeader rule = report.createLeader();
        rule.setRuleThickness("0.5pt");
        rule.setLeaderPattern("rule");
        if (this.isLegalSize) {
            rule.setLeaderLength("12.9in");
        } else if (this.isLandscape()) {
            rule.setLeaderLength("10.0in");
        } else {
            rule.setLeaderLength("7.4in");
        }
        rule.setColor(new PdfRGB(188, 190, 192));// PY: 30% black.

        // need to put static footnotes in a table, because this is the only way for FOP to keep
        // text together, without breaking across pages
        String[] columnWidths;// = new String[] {"19.0cm"};
        if (this.isLegalSize) {
        	 columnWidths = new String[] { "33.0cm" };
        } else if (isLandscape()) {
            columnWidths = new String[] { "25.4cm" };
        } else {
            columnWidths = new String[] { "19.0cm" };
        }

        int numberOfRows = 2;
        if (generalDisclosureContentIds != null) {
            numberOfRows += generalDisclosureContentIds.length * 2;
        }
        
        PdfTableBody tableBody = report.createTableBody(numberOfRows, 1);

        tableBody.add(0, 0, report.createParagraph().add(rule));
        tableBody.add(1, 0, report.createParagraph().setHeight("8pt").add(report.createText("  ")));

        int rowCount = 2;
        if (generalDisclosureContentIds != null && generalDisclosureContentIds.length > 0) {
            for (String generalDisclosureContentId : generalDisclosureContentIds) {
                PdfParagraph generalDisclosureParagraph = CMAContentHelper
                        .createIntroDisclosureParagraph(report, generalDisclosureContentId, options
                                .getCompanyId());

                if (generalDisclosureParagraph != null) {
                    tableBody.add(rowCount++, 0, report.createParagraph().add(
                            generalDisclosureParagraph.setFontFamily(FRUTIGER67BOLD_CN).setFontSize(8)));

                    tableBody.add(rowCount++, 0, report.createParagraph().add(
                            report.createParagraph(report.createText("  ")).setFontSize(8)));
                }
            }
        }
        
        // add formatting to keep rows together
        for (int i = 0; i < numberOfRows; i++) {
            tableBody.setRowKeepTogetherWithinPage(i);
          //  tableBody.setRowKeepTogether(i);
            tableBody.setRowKeepWithNext(i);
        }
        
        PdfTable footnoteTable = report.createTable(columnWidths,
                new PdfTableRegion[] { tableBody });
        standardFootnoteBlock.add(footnoteTable);

        return standardFootnoteBlock;
    }

    public static void attachFootnotes(PdfParagraph fundNameParagraph, ReportFund reportFund,
            StandardReportBuilder report) {
        String[] footnoteSymbols = reportFund.getFootnoteSymbols();
        StandardReportTemplate.attachFootnotes(fundNameParagraph, footnoteSymbols, report);
    }

    public static void attachFootnotes(PdfParagraph fundNameParagraph, String[] footnoteSymbols,
            StandardReportBuilder report) {
        if (footnoteSymbols != null) {
            List footnoteList = new ArrayList();
            for (int i = 0; i < footnoteSymbols.length; i++) {
                String symbol = footnoteSymbols[i];
                if (symbol != null && symbol.trim().length() > 0) {
                    footnoteList.add(symbol);
                }
            }
            for (Iterator iter = footnoteList.iterator(); iter.hasNext();) {
                String symbol = (String) iter.next();
                fundNameParagraph.add(report.createText(symbol.trim()).setBaseLineShift("super")
                        .setFontSize(5));
                if (iter.hasNext()) {
                    fundNameParagraph.add(report.createText(",").setBaseLineShift("super")
                            .setFontSize(5));
                }
            }
        }
    }

    protected List collateFundsIntoInvestmentGroups(Collection funds, Map investmentGroupLookupMap) {
        return new RiskReturnCollator(funds, investmentGroupLookupMap).collate();
    }
    // changes added as part of ACR REWRITE (iReport changes)
    protected List collateFundsIntoAssetHouseInvestmentGroups(List funds,
            Map morningstarCategoryPerformances, Map marketIndexPerformances,String reportName) {
        return new AssetHouseFundCollator(funds, AssetHouseFundMapping.getInstance(),
                morningstarCategoryPerformances, marketIndexPerformances,reportName).collate();
    }

    /**
     * A utility method to convert a Color to an CSS hex color
     * 
     * @param color is the color to convert
     * @return the CSS string color
     */
    public static String getHexColor(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2, 8);
    }

    public static String getFormattedCurrency(Object value, boolean showCurrencySign) {
        try {
            return NumberRender.format(value, null, null, "c", 2, BigDecimal.ROUND_HALF_DOWN, 1,
                    showCurrencySign);
        } catch (Exception e) {
            return null;
        }
    }

    protected boolean isLandscape() {
        return this.isLandscape;
    }

    protected void setLandscape(boolean isLandscape) {
        this.isLandscape = isLandscape;
    }

    protected boolean isLegalSize() {
        return this.isLegalSize;
    }

    protected void setLegalSize(boolean isLegalSize) {
        this.isLegalSize = isLegalSize;
    }

    protected void setReportPrinter(ReportPrinter reportPrinter) {
        this.reportPrinter = reportPrinter;
    }

	protected void addISFReport() {
		// appending Contract investment administration form instead ISF :ME127244 change
		if (options.isIncludeISF()) {
			// ISFGenerator.makeISFGenerator(options, report).add();
		}
	}

    protected PdfTable buildGicSection(StandardReportBuilder report,
            ReportDefinition reportDefinition, FundReportData reportData, List gicFunds,
            int reportType) {
        PdfTableHeader fundGroupHeading = buildGicSectionTableHeader(report, reportDefinition,
                gicFunds, reportType);

        PdfTableBody fundTableBody = buildGicSectionTableBody(report, reportDefinition, reportData
                .getGuaranteedAccountRates(), gicFunds, reportType);

        PdfTableRegion[] fundTableRegions = new PdfTableRegion[] { fundGroupHeading, fundTableBody };
        PdfTable fundTable = report.createTable(reportDefinition.getColumnWidths(),
                fundTableRegions);
        return fundTable;
    }

    private PdfTableHeader buildGicSectionTableHeader(StandardReportBuilder report,
            ReportDefinition reportDefinition, List gicFunds, int reportType) {
        // TODO use StyleText for formatting
        PdfTableHeader gicSectionTableHeader = report.createTableHeader(3, reportDefinition
                .getNumberOfColumns());

        int gicAnnualRateColumn = reportDefinition.getGicSectionAnnualRateColumn();
        for (int j = gicAnnualRateColumn + 1; j < reportDefinition.getNumberOfColumns(); j++) {
            gicSectionTableHeader.setColumnTextAlign(j, "right");
        }
        gicSectionTableHeader.setColumnTextAlign(gicAnnualRateColumn, "left");

        int headerRow = 0;
        if (reportType == GIC_ASSETCLASS) {
            gicSectionTableHeader.add(0, 0, report.createParagraph().setFontSize(
                    FONT_SIZE_STYLE_BOX_HEADING).setFontWeight("bold").add(
                    report.createText(AssetHouseFundMapping.SECTION_TITLE_GUARANTEED_ACCOUNTS)
                            .setColor(PdfColors.WHITE)));
            gicSectionTableHeader.setCellVerticalAlign(0, 0, "center");
            gicSectionTableHeader.setCellBackgroundColor(0, 0, COLOUR_NAVY_BLUE);
            gicSectionTableHeader.setCellMarginLeft(0, 0, "0.1in");
            gicSectionTableHeader.spanCells(0, 0, 1, reportDefinition.getNumberOfColumns());
            headerRow = 2;
            gicSectionTableHeader.add(1, 0, report.createParagraph()).setRowHeight(1, "1.1pt"); // stops
                                                                                                // color
                                                                                                // from
                                                                                                // row
                                                                                                // 2
                                                                                                // from
                                                                                                // overlapping
            for (int i = 1; i < reportDefinition.getNumberOfColumns(); i++) {
                gicSectionTableHeader.add(1, i, report.createParagraph());
            }
            for (int i = 0; i < gicAnnualRateColumn; i++) {
                gicSectionTableHeader.add(headerRow, i, report.createParagraph());
            }
        } else {
            // put company name in header
            String fundManagerName;
            if (isNewYork) {
                fundManagerName = "John Hancock New York";
            } else {
                fundManagerName = "John Hancock USA";
            }
            PdfParagraph annualRateOfHeader = report.createParagraph().setFontSize(
                    FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).add(
                    report.createText(fundManagerName));
            gicSectionTableHeader.add(0, 0, report.createParagraph(
                    AssetHouseFundMapping.SECTION_TITLE_GUARANTEED_ACCOUNTS).setFontSize(
                    FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN));
            gicSectionTableHeader.add(0, 1, annualRateOfHeader);

            for (int i = 2; i < gicAnnualRateColumn; i++) {
                gicSectionTableHeader.add(headerRow, i, report.createParagraph());
            }
            
        }

        PdfParagraph annualRateOfHeader = report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("annual rate of"));
        gicSectionTableHeader.add(headerRow, gicAnnualRateColumn, annualRateOfHeader).spanCells(
                headerRow, gicAnnualRateColumn, 1, 2);

        int hdrCol = gicAnnualRateColumn + 2;
        for (Iterator iter = gicFunds.iterator(); iter.hasNext();) {
            Fund fund = (Fund) ((ReportFund) iter.next()).getFund();

            PdfParagraph gic3YearHeader = report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText((String) GIC_HEADERS.get(fund.getInvestmentid())))
                    .add(
                            report.createText(FOOTNOTE_3Y_5Y_10Y_GIC + " ").setBaseLineShift(
                                    "super").setFontSize(5));
            gicSectionTableHeader.add(headerRow, hdrCol, gic3YearHeader);
            ++hdrCol;
        }

        for (int i = hdrCol; i < reportDefinition.getNumberOfColumns(); i++) {
            gicSectionTableHeader.add(headerRow, i, report.createParagraph());
        }
        
        if (reportType == GIC_ASSETCLASS) {
            underlineCells(gicSectionTableHeader, headerRow, 0, reportDefinition
                    .getNumberOfColumns());
        }

        for (int j = 0; j < reportDefinition.getNumberOfColumns(); j++) {
            gicSectionTableHeader.setCellPaddingTop(headerRow, j, "0.1cm");
        }

        reportDefinition.setCellFormattingForRow(gicSectionTableHeader, headerRow, false);

        return gicSectionTableHeader;
    }

    private PdfTableBody buildGicSectionTableBody(StandardReportBuilder report,
            ReportDefinition reportDefinition, Map guaranteedAccountRates, List gicFunds,
            int reportType) {
        PdfTableBody gicSectionTableBody = report.createTableBody(2, reportDefinition
                .getNumberOfColumns());

        TextStyle textStyle = new TextStyle(report, ReportFormattingConstants.FRUTIGER47LIGHT_CN,
                FONT_SIZE_FUND_ROW);

        int gicAnnualRateColumn = reportDefinition.getGicSectionAnnualRateColumn();
        for (int i = gicAnnualRateColumn; i < reportDefinition.getNumberOfColumns(); i++) {
            gicSectionTableBody.setColumnTextAlign(i, "right");
        }

        // Fund Manager for Guaranteed Account Funds will be shown as n/a.
        String fundManagerName = null; 

        boolean isFirst = true;
        int column = gicAnnualRateColumn + 2;

        for (int i = 0; i < column; i++) {
            gicSectionTableBody.add(0, i, report.createParagraph());
            gicSectionTableBody.add(1, i, report.createParagraph());
        }
        
        for (Iterator iter = gicFunds.iterator(); iter.hasNext();) {
            Fund fund = (Fund) ((ReportFund) iter.next()).getFund();
            GARates rates = (GARates) guaranteedAccountRates.get(fund.getInvestmentid());
            if (isFirst) {
                isFirst = false;
                gicSectionTableBody.add(0, 0, textStyle.createParagraph(rates == null
                        || rates
                        .getPreviouseffectivedate() == null ? " " : DateFormatUtils.format(
                        rates
                        .getPreviouseffectivedate(), "MMMM")));
                gicSectionTableBody.add(0, 1, textStyle.createParagraph(StandardReportsUtils
                        .formatFundDataString(fundManagerName)));
                gicSectionTableBody.add(1, 0, textStyle.createParagraph(rates == null
                        || rates
                        .getCurrenteffectivedate() == null ? " " : DateFormatUtils.format(
                        rates
                        .getCurrenteffectivedate(), "MMMM")));
                gicSectionTableBody.add(1, 1, textStyle.createParagraph(StandardReportsUtils
                        .formatFundDataString(fundManagerName)));
            }
            gicSectionTableBody.add(0, column, textStyle.createParagraph(rates == null
                    || rates
                    .getPreviousinterestrate() == null ? "n/a" : StandardReportsUtils
                    .formatPercentage(rates.getPreviousinterestrate())));
            gicSectionTableBody.add(1, column, textStyle.createParagraph(rates == null
                    || rates
                    .getCurrentinterestrate() == null ? "n/a" : StandardReportsUtils
                    .formatPercentage(rates.getCurrentinterestrate())));
            ++column;
        }

        for (int i = column; i < reportDefinition.getNumberOfColumns(); i++) {
            gicSectionTableBody.add(0, i, report.createParagraph());
            gicSectionTableBody.add(1, i, report.createParagraph());
        }
        
        gicSectionTableBody.setBorderBottomStyle("solid");
        gicSectionTableBody.setBorderBottomColor(PDF_RGB_ROW_BORDER);
        gicSectionTableBody.setBorderBottomWidth("0.5pt");
        gicSectionTableBody.setPaddingBefore("0.1cm");

        reportDefinition.setCellFormattingForRow(gicSectionTableBody, 0, true);
        reportDefinition.setCellFormattingForRow(gicSectionTableBody, 1, true);

        underlineCells(gicSectionTableBody, 1, 0, reportDefinition.getNumberOfColumns());

        return gicSectionTableBody;
    }

    private void underlineCells(PdfTableRegion tableRegion, int rowIndex, int startColumn,
            int endColumn) {
        if (startColumn <= endColumn) {
            for (int col = startColumn; col < endColumn; col++) {
                tableRegion.setCellBorderBottomStyle(rowIndex, col, "solid");
                tableRegion.setCellBorderBottomColor(rowIndex, col, PDF_RGB_ROW_BORDER);
                tableRegion.setCellBorderBottomWidth(rowIndex, col, "0.5pt");
            }
        }
    }

    public String[] addClassDisclosureFootnote(String[] staticFootnotes, ReportOptions options) {
        return (String[]) ArrayUtils.add(staticFootnotes, this.classDisclosureFootnoteNumber);
    }

    private String determineClassDisclosureFootnoteNumber(ReportOptions options) {
        String classDisclosureFootnoteNumber = "";
        if (options.getContractShortlistOptions().isContract()) {
            Contract contract = ((ContractDAO) DAOFactory.create(ContractDAO.class.getName()))
                    .retrieveContractByContractNumber(options.getContractShortlistOptions()
                            .getContractNumber());

            if (StandardReportsConstants.PACKAGE_SERIES_MULTI_CLASS.equals(contract
                    .getFundPackageSeries().trim())) {
                classDisclosureFootnoteNumber = ReportFormattingConstants.FOOTNOTE_CLASS_DISCLOSURE_PLAN_ADMIN;
            } else {
                classDisclosureFootnoteNumber = ReportFormattingConstants.FOOTNOTE_CLASS_DISCLOSURE_GENERIC_INFORCE;
            }
        } else {
            classDisclosureFootnoteNumber = ReportFormattingConstants.FOOTNOTE_CLASS_DISCLOSURE_PLAN_ADMIN;
        }
        return classDisclosureFootnoteNumber;
    }
    
    /**
     * This method creates the PdfParagraph for Intro Disclosure.
     * 
     * @param reportDefinition
     * @return
     */
    protected PdfParagraph createIntroDisclosures(String contentId) {
        PdfParagraph contentParagraph = null;

        contentParagraph = CMAContentHelper.createIntroDisclosureParagraph(report, contentId,
                options.getCompanyId());

        return contentParagraph;
    }
    
	public static List<String> getMarketIndexFootnoteSymbol(String investmentId) {
		List<String> footNoteSymbol = new ArrayList<String>();
		try {
			footNoteSymbol = FundServiceDelegate.getInstance().getMarketIndexFootnoteForFund(investmentId);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving market index footnotes in getFootnoteSymbol for "
							+ investmentId, e);
		}
		return footNoteSymbol;
	}

	public static String getSuperScriptValue(List<String> footnoteSymbol) {
		StringBuilder superScripts = new StringBuilder();
		for (String symbol : footnoteSymbol) {
			superScripts.append(symbol).append(",");
		}
		if (superScripts.length() > 0) {
			superScripts.deleteCharAt(superScripts.length() - 1);
		}
		return superScripts.toString();
	}

	public static String getSuperScriptValue(String investmentId) {
		List<String> footnoteSymbol = new ArrayList<String>();
		footnoteSymbol = getMarketIndexFootnoteSymbol(investmentId);
		return getSuperScriptValue(footnoteSymbol);
	}
	
	protected void addUserModifiedDisclosure(PdfParagraph tableParagraph) {
		if (options.getContractShortlistOptions().isUserModifiedContract()) {
			PdfParagraph introDisclosureParagraph = createIntroDisclosures("92398");
			if (introDisclosureParagraph != null) {
				introDisclosureParagraph.setPaddingBottom("0.2cm").setTextAlignment("left");
				tableParagraph.add(introDisclosureParagraph);
			}
		} else if (options.getContractShortlistOptions().isUserModified()) {
			PdfParagraph introDisclosureParagraph = createIntroDisclosures("92397");
			if (introDisclosureParagraph != null) {
				introDisclosureParagraph.setPaddingBottom("0.2cm").setTextAlignment("left");
				tableParagraph.add(introDisclosureParagraph);
			}
		}
	}
}