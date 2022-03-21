package com.manulife.pension.ireports.report.streamingreport.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.dao.Footnote;
import com.manulife.pension.ireports.dao.FootnoteSymbolComparator;
import com.manulife.pension.ireports.dao.ReportDataRepository;
import com.manulife.pension.ireports.dao.ReportDataRepositoryFactory;
import com.manulife.pension.ireports.dao.StaticFootnoteDAO;
import com.manulife.pension.ireports.model.report.MarketReportData;
import com.manulife.pension.ireports.report.MarketReportPageSequence;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.StandardReportPageSequence;
import com.manulife.pension.ireports.report.StandardReportTemplate;
import com.manulife.pension.ireports.report.util.AssetCategoriesCollator;
import com.manulife.pension.ireports.report.util.BottomAlignCellDecoratorFactory;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.pension.platform.utility.ireports.utilities.FundReportUtil;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetCategory;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetCategoryGroup;
import com.manulife.pension.service.fund.standardreports.valueobject.CurrentAsOfDate;
import com.manulife.pension.service.fund.valueobject.MarketIndexIbPerformance;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfRGB;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

/**
 * @author Siby Thomas
 */
public class MarketReport extends StandardReportTemplate {
    private static final int NUMBER_OF_COLUMNS_ON_REPORT = 9;

    public static final String REPORT_NAME = "Market Index Report";

    public static final String[] GENERAL_DISCLOSURE_CONTENT_IDS = { "64507", "64508", "64509",
            "64510", "64982" };

    public static final String[] INTRO_CONTENT_IDS = { "64973", "64974", "64975" };
    public static final String[] INTRO_GENERIC_LEVEL_CONTENT_IDS = { "97640" };
    
    private static String[] staticFootnoteSymbols = new String[] { FOOTNOTE_ASSET_CLASS_LSFLCF,
            FOOTNOTE_MARKET_INDEX, FOOTNOTE_RETURNS_MAJOR_INVESTMENT };

    public MarketReport(ReportOptions options) {
        super(options);
    }

    /**
     * Build the PDF content
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void buildReport() {
        createPageLayoutStructure(report, false, false);

        ReportDataRepository reportDataRepository = ReportDataRepositoryFactory.getRepository();

        MarketReportData reportData = reportDataRepository.getMarketReportData();

        Map<String, CurrentAsOfDate> asOfDates = reportDataRepository.getCurrentAsOfDates();

        
        PdfFlow flow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
        PdfParagraph tableParagraph = report.createParagraph().setMargin("0.0cm").setTextAlignment(
                "end").setSpan("all");
        flow.add(tableParagraph);

        if (INTRO_CONTENT_IDS != null && INTRO_CONTENT_IDS.length > 0) {
            for (String introDisclosureContentId : INTRO_CONTENT_IDS) {
                PdfParagraph introDisclosureParagraph = createIntroDisclosures(introDisclosureContentId);
                if (introDisclosureParagraph != null) {
                    introDisclosureParagraph.setPaddingBottom("0.2cm").setTextAlignment("left");
                    tableParagraph.add(introDisclosureParagraph);
                }
            }
        }
        if (INTRO_GENERIC_LEVEL_CONTENT_IDS != null && INTRO_GENERIC_LEVEL_CONTENT_IDS.length > 0) {
            for (String introDisclosureContentId : INTRO_GENERIC_LEVEL_CONTENT_IDS) {
                PdfParagraph introDisclosureParagraph = createIntroDisclosures(introDisclosureContentId);
                if (introDisclosureParagraph != null) {
                    introDisclosureParagraph.setPaddingBottom("0.2cm").setTextAlignment("left");
                    tableParagraph.add(introDisclosureParagraph);
                }
            }
        }

        // Portrait: 7.5in = 19cm
        String[] columnWidths = new String[] { "5.0cm", "4.9cm", "1.3cm", "1.3cm", "1.3cm",
                "1.3cm", "1.3cm", "1.3cm", "1.3cm" };

        Collection assetCategories = reportData.getAssetCategories().values();
        Map marketIndexIbPerformances = reportData.getMarketIndexIbPerformances();
        Map assetCategoriesByGroup = collateAssetCategoriesIntoGroups(assetCategories, reportData
                .getAssetCategoryGroups());

        PdfTableHeader mainTableHeader = buildTableHeader(report, columnWidths.length, asOfDates);

        int size = assetCategoriesByGroup.size() * 2 - 1;
        PdfTableBody mainTableBody = report.createTableBody(size, columnWidths.length);

        int i = 0;
        for (Iterator groupIterator = assetCategoriesByGroup.entrySet().iterator(); groupIterator
                .hasNext();) {
            Map.Entry entry = (Map.Entry) groupIterator.next();
            String groupId = (String) entry.getKey();
            AssetCategoryGroup assetCategoryGroup = (AssetCategoryGroup) reportData
                    .getAssetCategoryGroups().get(groupId);
            List assetCategoriesForGroup = (List) entry.getValue();

            PdfTable assetCategoryGroupTable = buildAssetCategoryGroupTable(report,
                    assetCategoryGroup.getAssetCategoryGroupName(), assetCategoriesForGroup,
                    columnWidths, marketIndexIbPerformances);

            mainTableBody.spanCells(i, 0, 1, columnWidths.length);
            mainTableBody.add(i++, 0, assetCategoryGroupTable);
            if (groupIterator.hasNext()) {
                setCellFormattingForRow(mainTableBody, i, false);
                mainTableBody.add(i, 0, report.createParagraph()).setRowHeight(i, "0.3in");
                for (int j = 1; j < columnWidths.length; j++) {
                    mainTableBody.add(i, j, report.createParagraph());
                }
                i++;
            }
        }

        PdfTable mainTable = report.createTable(columnWidths, new PdfTableRegion[] {
                mainTableHeader, mainTableBody });
        tableParagraph.add(mainTable);

        /** * get static footnotes ** */
        StaticFootnoteDAO staticFootnoteDAO = new StaticFootnoteDAO(options.getCompanyId());
        Map footnotesMap = staticFootnoteDAO.retrieveFootnote(staticFootnoteSymbols); // returns a
        // map of
        // Footnote
        // entries
        Map staticFootnotes = new LinkedMap();
        // sort and create map of footnote symbols/values
        List allFootnoteSymbolsOnReport = new ArrayList(footnotesMap.keySet());
        Collections.sort(allFootnoteSymbolsOnReport, new FootnoteSymbolComparator());

        for (Iterator iter = allFootnoteSymbolsOnReport.iterator(); iter.hasNext();) {
            String symbol = (String) iter.next();
            staticFootnotes.put(symbol, ((Footnote) footnotesMap.get(symbol)).getText());
        }

        Map<String, PdfParagraph> firstPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        Map<String, PdfParagraph> subsequentPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        
        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_5,
                createTitle4HeaderParagraph());
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_5,
                createTitle4HeaderParagraph());
        
        StandardReportPageSequence pageSequenceMainReport = createPageSequence(report, REPORT_NAME,
                null, "content", firstPageTitleParagraphMap , subsequentPageTitleParagraphMap );
        
        pageSequenceMainReport.add(flow);

        PdfFlow footerFlow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
        footerFlow.add(createFootnotesHeader(report, staticFootnotes));
        footerFlow.add(createFootnotes(report, staticFootnotes));

        footerFlow.add(createStandardFootnoteBlock(report, this.getClass().getName(),
                GENERAL_DISCLOSURE_CONTENT_IDS));
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));
        
        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_5,
                createTitle4HeaderParagraph());
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_5,
                createTitle4HeaderParagraph());

        StandardReportPageSequence pageSequenceFooter = createPageSequence(report, REPORT_NAME,
                null, "subsequentPageDoubleColumn",  firstPageTitleParagraphMap , subsequentPageTitleParagraphMap );

        pageSequenceFooter.add(footerFlow);
    }

    private PdfTable buildAssetCategoryGroupTable(StandardReportBuilder report,
            String assetCategoryGroupName, List assetCategoriesForGroup, String[] columnWidths,
            Map marketIndexIbPerformances) {
        int numberOfAssetCategoriesForGroup = assetCategoriesForGroup.size();

        PdfTableBody tableBody = report.createTableBody(numberOfAssetCategoriesForGroup,
                columnWidths.length);
        for (int j = 2; j < NUMBER_OF_COLUMNS_ON_REPORT; j++) {
            tableBody.setColumnTextAlign(j, "right");
        }

        PdfTableHeader tableHeader = buildAssetCategoryGroupHeader(report,
                NUMBER_OF_COLUMNS_ON_REPORT, assetCategoryGroupName);

        // this logic is for keeping rows together on one page
        int row = 0;
        int rowNumberKeepTogetherFirst = (new Integer(PropertyManager
                .getString("row.number.keep.together"))).intValue();
        int rowNumberKeepTogetherLast = 0;
        if (numberOfAssetCategoriesForGroup >= rowNumberKeepTogetherFirst) {
            rowNumberKeepTogetherLast = numberOfAssetCategoriesForGroup
                    - rowNumberKeepTogetherFirst;
        }

        for (Iterator iter = assetCategoriesForGroup.iterator(); iter.hasNext();) {
            AssetCategory assetCategory = (AssetCategory) iter.next();

            buildAssetCategoryRow(report, assetCategory, tableBody, row, marketIndexIbPerformances);

            // formatting / keeping rows together
            // tableBody.setRowKeepTogether(row);
            tableBody.setRowKeepTogetherWithinPage(row);
            if (row < rowNumberKeepTogetherFirst - 1 || row >= rowNumberKeepTogetherLast) {
                tableBody.setRowKeepWithNext(row);
            }
            row++;
        }
        tableBody.setBorderBottomStyle("solid");
        tableBody.setBorderBottomColor(new PdfRGB(188, 190, 192));
        tableBody.setBorderBottomWidth("0.5pt");
        tableBody.setPaddingBefore("0.1cm");

        PdfTableRegion[] tableRegions = new PdfTableRegion[] { tableHeader, tableBody };

        return report.createTable(columnWidths, tableRegions);
    }

    private void buildAssetCategoryRow(StandardReportBuilder report, AssetCategory assetCategory,
            PdfTableBody tableBody, int row, Map marketIndexIbPerformances) {

        String marketIndexId = assetCategory.getMarketIndexId();
        MarketIndexIbPerformance marketIndexPerf = null;
        if (marketIndexId != null) {
            marketIndexPerf = (MarketIndexIbPerformance) marketIndexIbPerformances
                    .get(marketIndexId);
        }

        PdfParagraph assetCategoryNameParagraph = report.createParagraph().setFontSize(
                FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);
        tableBody.add(row, 0, assetCategoryNameParagraph.add(report.createText(StandardReportsUtils
                .getValueorNA(assetCategory.getAssetCategoryName()))));

        if (marketIndexPerf == null) {
            tableBody.add(row, 1, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("n/a")));
            tableBody.add(row, 2, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("n/a")));
            tableBody.add(row, 3, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("n/a")));
            tableBody.add(row, 4, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("n/a")));
            tableBody.add(row, 5, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("n/a")));
            tableBody.add(row, 6, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("n/a")));
            tableBody.add(row, 7, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("n/a")));
            tableBody.add(row, 8, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText("n/a")));
        } else {
            tableBody.add(row, 1, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.getValueorNA(marketIndexPerf
                                    .getMarketIndexName()))));
            tableBody.add(row, 2, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.formatPercentage(marketIndexPerf
                                    .getRor_1Month()))));
            tableBody.add(row, 3, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.formatPercentage(marketIndexPerf
                                    .getRor_3Month()))));
            tableBody.add(row, 4, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.formatPercentage(marketIndexPerf
                                    .getRorYtd()))));
            tableBody.add(row, 5, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.formatPercentage(marketIndexPerf
                                    .getRor_1Year()))));
            tableBody.add(row, 6, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.formatPercentage(marketIndexPerf
                                    .getRor_3Year()))));
            tableBody.add(row, 7, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.formatPercentage(marketIndexPerf
                                    .getRor_5Year()))));
            tableBody.add(row, 8, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.formatPercentage(marketIndexPerf
                                    .getRor_10Year()))));
        }

        setCellFormattingForRow(tableBody, row, false);
    }

    private PdfTableHeader buildAssetCategoryGroupHeader(StandardReportBuilder report,
            int numberOfColumns, String assetCategoryGroupName) {
        int numberOfHeaderRows = 2;

        PdfTableHeader categoryGroupTableHeader = report.createTableHeader(numberOfHeaderRows,
                numberOfColumns);
        for (int j = 2; j < numberOfColumns; j++) {
            categoryGroupTableHeader.setColumnTextAlign(j, "right");
        }

        categoryGroupTableHeader.add(0, 0, report.createParagraph("  "));

        for (int i = 1; i < numberOfColumns; i++) {
            categoryGroupTableHeader.add(0, i, report.createParagraph());
        }

        setCellFormattingForRow(categoryGroupTableHeader, 0, false);

        categoryGroupTableHeader.add(1, 0, report.createParagraph().setFontSize(
                FONT_SIZE_FUND_GROUP_HEADING).setFontWeight("bold").add(
                report.createText(assetCategoryGroupName).setColor("white")));
        categoryGroupTableHeader.setCellVerticalAlign(1, 0, "center");
        categoryGroupTableHeader.setCellBackgroundColor(1, 0, PDF_RGB_GROUP_HEADING);
        categoryGroupTableHeader.setCellMarginLeft(1, 0, "0.1in");
        categoryGroupTableHeader.setCellBorderBottomStyle(1, 0, "solid");
        categoryGroupTableHeader.setBorderColor(PDF_RGB_GROUP_HEADING);
        categoryGroupTableHeader.setCellBorderBottomWidth(1, 0, "0.01pt");
        categoryGroupTableHeader.spanCells(1, 0, 1, numberOfColumns);

        return categoryGroupTableHeader;
    }

    private PdfTableHeader buildTableHeader(StandardReportBuilder report, int numberOfColumns,
            Map<String, CurrentAsOfDate> asOfDates) {
        PdfTableHeader tableHeader = report.createTableHeader(2, numberOfColumns,
                new BottomAlignCellDecoratorFactory());

        for (int j = 2; j < numberOfColumns; j++) {
            tableHeader.setColumnTextAlign(j, "right");
        }

        tableHeader.add(0, 0, report.createParagraph());
        tableHeader.add(0, 1, report.createParagraph());
        tableHeader.add(0, 2, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(
                        report.createText("Returns as of "
                                + DateFormatUtils.format(FundReportUtil.getAsOfDateForContext(
                                        asOfDates, "MKP"),
                                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE)))
                .setTextAlignment("center"));
        tableHeader.spanCells(0, 2, 1, 7);

        tableHeader.add(1, 0, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("Category")).add(
                        report.createText(staticFootnoteSymbols[0]).setBaseLineShift("super")
                                .setFontSize(5)));
        tableHeader.add(1, 1, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("Index")).add(
                        report.createText(staticFootnoteSymbols[1]).setBaseLineShift("super")
                                .setFontSize(5)));
        tableHeader.add(1, 2, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("1mo")));
        tableHeader.add(1, 3, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("3mo")));
        tableHeader.add(1, 4, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("YTD")));
        tableHeader.add(1, 5, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("1yr")));
        tableHeader.add(1, 6, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("3yr")));
        tableHeader.add(1, 7, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("5yr")));
        tableHeader.add(1, 8, report.createParagraph().setFontSize(FONT_SIZE_COLUMN_HEADING)
                .setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("10yr")));

        tableHeader.setBorderBottomStyleForRegion("solid").setBorderBottomWidthForRegion("0.01cm");

        setCellFormattingForRow(tableHeader, 0, false);
        setCellFormattingForRow(tableHeader, 1, false);

        return tableHeader;
    }

    private Map collateAssetCategoriesIntoGroups(Collection assetCategories, Map assetCategoryGroups) {
        return new AssetCategoriesCollator(assetCategories, assetCategoryGroups).collate();
    }

    protected StandardReportPageSequence createPageSequence(StandardReportBuilder report,
            String reportName, String headerParagraph, String masterReference,
            Map<String, PdfParagraph> firstPageTitleStaticParagraph,
            Map<String, PdfParagraph> subsequentPageTitleStaticParagraph) {
        StandardReportPageSequence pageSequence = new MarketReportPageSequence(report, reportName,
                options, isLandscape(), isLegalSize(), headerParagraph, masterReference,
                firstPageTitleStaticParagraph, subsequentPageTitleStaticParagraph);
        pageSequence.init();
        report.addPageSequence(pageSequence);
        return pageSequence;
    }

    public void setCellFormattingForRow(PdfTableRegion tableRegion, int row, boolean setTopBorder) {
        if (setTopBorder) {
            for (int i = 0; i < NUMBER_OF_COLUMNS_ON_REPORT; i++) {
                tableRegion.setCellBorderTopStyle(row, i, "solid");
                tableRegion.setCellBorderTopWidth(row, i, "0.5pt");
                tableRegion.setCellBorderTopColor(row, i, PDF_RGB_ROW_BORDER);
            }
        }

        // set shading for certain columns
        for (int i = 2; i < NUMBER_OF_COLUMNS_ON_REPORT; i++) {
            tableRegion.setCellBackgroundColor(row, i, ReportFormattingConstants.PDF_RGB_SHADING_1);
        }
    }

    /**
     * This method creates the Title 2 Header Paragraph.
     * 
     * @return
     */
    private PdfParagraph createTitle4HeaderParagraph() {
        PdfParagraph title4HeaderParagraph = report.createParagraph(
                report.createText("Returns of Major Investment Benchmarks")).add(
                Footnotes.createFootnote(report, FOOTNOTE_RETURNS_MAJOR_INVESTMENT)).setFontFamily(
                FRUTIGER57CN).setFontSize(FONT_SIZE_CONTRACT_OR_SHORTLIST_DESC).setTextAlignment(
                "right");

        return title4HeaderParagraph;
    }

    public static void main(String[] args) throws Exception {
        MarketReport report = new MarketReport(null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        report.buildReport();
        report.writeTo(bos);
        File reportFile = new File("C:\\intelliware\\simpleReport.pdf");
        FileOutputStream fos = new FileOutputStream(reportFile);
        fos.write(bos.toByteArray());
        fos.close();
    }
}