package com.manulife.pension.ireports.report.streamingreport.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.dao.FootnoteSymbolComparator;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.report.AbstractAssetClassReportGroupedByMarketIndex;
import com.manulife.pension.ireports.report.CustomPageSequence;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.util.CMAContentHelper;
import com.manulife.pension.ireports.report.viewbean.MorningstarCategoryPerformanceViewBean;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.platform.utility.fap.constants.FapConstants;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfLeader;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfRGB;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableRegion;

public class InvestmentReturnsStandardDeviationsByAssetClassReport extends AbstractAssetClassReportGroupedByMarketIndex {
    public InvestmentReturnsStandardDeviationsByAssetClassReport(ReportOptions options) {
        super(options, new InvestmentReturnsStandardDeviationsReportDefinition());
    }
    private static final FootnoteSymbolComparator FOOTNOTE_SYMBOL_COMPARATOR = new FootnoteSymbolComparator();
    /**
     * The initializeReportData() method was overriden to remove few static footnotes, if they are
     * not supposed to be present.
     */
    protected void initializeReportData() {
        super.initializeReportData();

        if (!containsGuaranteedAccountRates(reportData)) {
            reportData.getFootnotes().remove(FOOTNOTE_3Y_5Y_10Y_GIC);
        }
        
       List<String> footNoteSuperScriptList = getNotRequiredFootNoteSuperScripts(reportData);
       if(footNoteSuperScriptList != null && footNoteSuperScriptList.size() > 0 ){
    	   for(String footNoteSuperScript : footNoteSuperScriptList){
    		   if(reportData.getFootnotes().containsKey(footNoteSuperScript)){
    			   reportData.getFootnotes().remove(footNoteSuperScript);
        	   }
           }
       }
    }
    
	protected void definePageSequence(StandardReportBuilder report, PdfFlow flow,
            FundReportData reportData) {

        Map<String, PdfParagraph> firstPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        Map<String, PdfParagraph> subsequentPageTitleParagraphMap = new HashMap<String, PdfParagraph>();

        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());

        CustomPageSequence pageSequenceMainReport = createCustomPageSequence(report, null,
                "content", firstPageTitleParagraphMap, subsequentPageTitleParagraphMap);
        pageSequenceMainReport.add(flow);

        PdfFlow footerFlow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
        if(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT.equals(options.getReportCode())){
        	Map footNotes = sortFootnotesOnReport(reportData.getFootnotes());
        	footerFlow.add(createFootnotesHeader(report, footNotes));
        	footerFlow.add(createFootnotes(report, footNotes));
        	footerFlow.add(createFootnotes(report, reportData.getCompanyIdFootnotes()));
        }
        else{
        	footerFlow.add(createFootnotesHeader(report, reportData.getFootnotes()));
        	footerFlow.add(createFootnotes(report, reportData.getFootnotes()));
        	footerFlow.add(createFootnotes(report, reportData.getCompanyIdFootnotes()));
        }
        // suppress the data in I:report
        footerFlow.add(createStandardRiskDisclosure(report));
        if(reportData.isSelectedFromHistoricalReport()){
        footerFlow.add(createStandardEndFootnoteBlock(report, this.getClass().getName(),
                reportDefinition.getGeneralDisclosureContentIds()));
        }
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));

        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());

        CustomPageSequence pageSequenceFooter = createCustomPageSequence(report, null,
                "subsequentPageDoubleColumn", firstPageTitleParagraphMap,
                subsequentPageTitleParagraphMap);

        pageSequenceFooter.add(footerFlow);
    }

    /**
     * This method creates the Title 2 Header Paragraph.
     * 
     * @return
     */
    private PdfParagraph createTitle2HeaderParagraph() {
        PdfParagraph title2HeaderParagraph = report.createParagraph(
                report.createText("Investment returns")).add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).add(
                " and standard deviation").add(
                Footnotes.createFootnote(report, FOOTNOTE_STANDARD_DEVIATION)).setFontFamily(
                FRUTIGER57CN).setFontSize(14).setTextAlignment("right");

        return title2HeaderParagraph;
    }
    
    
    /**
     * This is a work-around for the lack of support of conditional-page-master page-position="last"
     * in the current version of fop.
     * 
     * @param report
     * @param reportDefinition
     * @return
     */
    private PdfParagraph createStandardEndFootnoteBlock(StandardReportBuilder report,
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
                	if("64508".equalsIgnoreCase(generalDisclosureContentId))
                	{
                		tableBody.add(rowCount++, 0, report.createParagraph().add(
                                generalDisclosureParagraph));

                        tableBody.add(rowCount++, 0, report.createParagraph().add(
                                report.createParagraph(report.createText("  ")).setFontSize(12)));
                	}
                	else{
                    tableBody.add(rowCount++, 0, report.createParagraph().add(
                            generalDisclosureParagraph.setFontSize(8)));

                    tableBody.add(rowCount++, 0, report.createParagraph().add(
                            report.createParagraph(report.createText("  ")).setFontSize(8)));
                	}
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
        
    @Override
    protected void buildBeanBasedRowWithStandardColumns(StandardReportBuilder report, Object bean,
            PdfTableBody tableBody, int row, String title, String rowName, String footnote) {
    	
    	PdfParagraph fundNameParagraph = report.createParagraph()
                .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);
        PdfTableRegion fundNameTableBody = report.createTableBody(1, 2);
		PdfTableRegion[] fundNameTableRegions = new PdfTableRegion[]{fundNameTableBody};
		PdfTable fundNameTable = report.createTable(new String[] {"0.2cm", "9.1cm"}, fundNameTableRegions);
		fundNameTableBody.add(0, 0, report.createParagraph(" "));
		fundNameTableBody.add(0, 1, fundNameParagraph.add(report.createText(StandardReportsUtils.getValueorNA(title))));
		tableBody.add(row, 0, fundNameTable);
    	
        if (footnote != null) {
            tableBody.add(row, 1, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.getValueorNA(rowName))).add(
                            report.createText(footnote.trim()).setBaseLineShift("super").setFontSize(5)));
        } else {
            tableBody.add(row, 1, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText(StandardReportsUtils.getValueorNA(rowName))));
        }

        tableBody.add(row, 2, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 3, report.createParagraph(""));
        tableBody.add(row, 4, getBeanValueParagraph(report, bean, "ror_1Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 5, getBeanValueParagraph(report, bean, "ror_3Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 6, getBeanValueParagraph(report, bean, "ror_5Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 7, getBeanValueParagraph(report, bean, "ror_10Year", FONT_SIZE_FUND_ROW));

        // tableBody.add(row, 8, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 8, report.createParagraph(""));

        tableBody.add(row, 9, getBeanValueParagraph(report, bean, "ror_1YearQe", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 10, getBeanValueParagraph(report, bean, "ror_5YearQe", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 11, getBeanValueParagraph(report, bean, "ror_10YearQe", FONT_SIZE_FUND_ROW));

        tableBody.add(row, 12, report.createParagraph(""));
        tableBody.add(row, 13, getBeanValueParagraph(report, bean, "standardDeviation_3YearQe",
                FONT_SIZE_FUND_ROW));
        tableBody.add(row, 14, getBeanValueParagraph(report, bean, "standardDeviation_5YearQe",
                FONT_SIZE_FUND_ROW));
        tableBody.add(row, 15, getBeanValueParagraph(report, bean, "standardDeviation_10YearQe",
                FONT_SIZE_FUND_ROW));

        tableBody.add(row, 16, report.createParagraph(""));
       //tableBody.add(row, 17, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
       // tableBody.add(row, 18, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
       // tableBody.add(row, 19, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));

        if (bean instanceof MorningstarCategoryPerformanceViewBean) {
			tableBody.add(row, 17, getBeanValueParagraph(report, bean,
					"expenseRatioQe",
                    FONT_SIZE_FUND_ROW));
        } else {
            tableBody.add(row, 17, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        }

    }

    @Override
    protected void buildBeanBasedRowWithOutStandardColumns(StandardReportBuilder report, Object bean,
            PdfTableBody tableBody, int row, String title, String rowName, String footnote) {
    	
    	PdfParagraph fundNameParagraph = report.createParagraph()
                .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);
        PdfTableRegion fundNameTableBody = report.createTableBody(1, 2);
		PdfTableRegion[] fundNameTableRegions = new PdfTableRegion[]{fundNameTableBody};
		PdfTable fundNameTable = report.createTable(new String[] {"0.2cm", "9.8cm"}, fundNameTableRegions);
		fundNameTableBody.add(0, 0, report.createParagraph(" "));
		fundNameTableBody.add(0, 1, fundNameParagraph.add(report.createText(StandardReportsUtils.getValueorNA(title))));
		tableBody.add(row, 0, fundNameTable);
		
        if (footnote != null) {
            tableBody.add(row, 1, report.createParagraph()
                    .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
                    .add(report.createText(StandardReportsUtils.getValueorNA(rowName)))
                    .add(report.createText(footnote.trim()).setBaseLineShift("super").setFontSize(5)));
        } else {
            tableBody.add(row, 1, report.createParagraph()
                    .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
                    .add(report.createText(StandardReportsUtils.getValueorNA(rowName))));
        }

        tableBody.add(row, 2, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 3, report.createParagraph(""));
        
        tableBody.add(row, 4, getBeanValueParagraph(report, bean, "ror_1Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 5, getBeanValueParagraph(report, bean, "ror_3Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 6, getBeanValueParagraph(report, bean, "ror_5Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 7, getBeanValueParagraph(report, bean, "ror_10Year", FONT_SIZE_FUND_ROW));
        
        //tableBody.add(row, 8, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 8, report.createParagraph(""));
        
        tableBody.add(row, 9, getBeanValueParagraph(report, bean, "standardDeviation_3Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 10, getBeanValueParagraph(report, bean, "standardDeviation_5Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 11, getBeanValueParagraph(report, bean, "standardDeviation_10Year", FONT_SIZE_FUND_ROW));
        
        tableBody.add(row, 12, report.createParagraph(""));
       // tableBody.add(row, 13,report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
       // tableBody.add(row, 14, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
       // tableBody.add(row, 15, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        
        if (bean instanceof MorningstarCategoryPerformanceViewBean) {
			tableBody.add(row, 13, getBeanValueParagraph(report, bean,
					"expenseRatioQe", FONT_SIZE_FUND_ROW));
        } else {
            tableBody.add(row, 13, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        }

    }
    
    /**
     * Method to retrieve the footNotes if they are not supposed to be present.
     * 
     * @param reportData
     * @return List<String>
     */
    private List<String> getNotRequiredFootNoteSuperScripts(FundReportData reportData) {

    	String[] dynamicSuperScripts = new String[] { FOOTNOTE_RETIREMENT_CHOICES_MARKET_INDEX, 
                FOOTNOTE_RETIREMENT_LIVING_MARKET_INDEX,FOOTNOTE_LIFESTYLE_MARKET_INDEX };
    	
		List<String> dynamicSuperScriptsList = new ArrayList<String>(Arrays.asList(dynamicSuperScripts));
		
		return dynamicSuperScriptsList;
	}
	

}
