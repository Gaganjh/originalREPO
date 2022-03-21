package com.manulife.pension.ireports.report.streamingreport.impl;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.report.AbstractAssetClassReportGroupedByMarketIndex;
import com.manulife.pension.ireports.report.CustomPageSequence;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableRegion;

/**
 * @author Greg McKhool, Tom McGrady
 */
public class IR_AICByAssetClassReport extends AbstractAssetClassReportGroupedByMarketIndex {
	
    public IR_AICByAssetClassReport(ReportOptions options) {
        super(options, new IR_AICReportDefinition(true));
    }

    /**
     * The initializeReportData() method was overriden to remove few static footnotes, if they are
     * not supposed to be present.
     */
    protected void initializeReportData() {
        super.initializeReportData();

        if (!containsGuaranteedAccountRates(reportData)) {
            reportData.getFootnotes().remove(FOOTNOTE_3Y_5Y_10Y_GIC);
        }
    }   

    protected void definePageSequence(StandardReportBuilder report, PdfFlow flow,
            FundReportData reportData) {

        Map<String, PdfParagraph> firstPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        Map<String, PdfParagraph> subsequentPageTitleParagraphMap = new HashMap<String, PdfParagraph>();

        PdfParagraph firstPageTitle2HeaderParagraph = createTitle2HeaderParagraph();
        PdfParagraph subsequentPageTitle2HeaderParagraph = createTitle2HeaderParagraph();
        
        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                firstPageTitle2HeaderParagraph);
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                subsequentPageTitle2HeaderParagraph);
        

        CustomPageSequence pageSequenceMainReport = createCustomPageSequence(report, null,
                "content", firstPageTitleParagraphMap, subsequentPageTitleParagraphMap);
        
        pageSequenceMainReport.add(flow);

        PdfFlow footerFlow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
        footerFlow.add(createFootnotesHeader(report, reportData.getFootnotes()));
        footerFlow.add(createFootnotes(report, reportData.getFootnotes()));
        footerFlow.add(createFootnotes(report, reportData.getCompanyIdFootnotes()));        footerFlow.add(createStandardRiskDisclosure(report));
        footerFlow.add(createStandardFootnoteBlock(report, this.getClass().getName(),
                reportDefinition.getGeneralDisclosureContentIds()));
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));
        
        PdfParagraph firstPageTitle2HdrParagraph = createTitle2HeaderParagraph();
        PdfParagraph subsequentPageTitle2HdrParagraph = createTitle2HeaderParagraph();
        
        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                firstPageTitle2HdrParagraph);
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                subsequentPageTitle2HdrParagraph);
        
        CustomPageSequence pageSequenceFooter = createCustomPageSequence(report, null,
                "subsequentPageDoubleColumn", firstPageTitleParagraphMap,
                subsequentPageTitleParagraphMap);

        pageSequenceFooter.add(footerFlow);
        
    }

    /**
     * This method creates the Title 2 Header Paragraph
     * 
     * @return
     */
    private PdfParagraph createTitle2HeaderParagraph() {
        return report.createParagraph(report.createText("Investment Returns")).add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).add(
                " and Expense Ratios").add(Footnotes.createFootnote(report, FOOTNOTE_AIC))
                .setFontFamily(FRUTIGER57CN).setFontSize(14).setTextAlignment("right");
    }
    
    @Override
    protected void buildBeanBasedRowWithOutStandardColumns(StandardReportBuilder report, Object bean,
            PdfTableBody tableBody, int row, String title, String rowName, String footnote) {
    	
    	PdfParagraph fundNameParagraph = report.createParagraph()
                .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);
        PdfTableRegion fundNameTableBody = report.createTableBody(1, 2);
		PdfTableRegion[] fundNameTableRegions = new PdfTableRegion[]{fundNameTableBody};
		PdfTable fundNameTable = report.createTable(new String[] {"0.2cm", "7.65cm"}, fundNameTableRegions);
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
        
        tableBody.add(row, 3, getBeanValueParagraph(report, bean, 
        		"ror_1Month", FONT_SIZE_FUND_ROW));  
        tableBody.add(row, 4, getBeanValueParagraph(report, bean,
        		"ror_3Month", FONT_SIZE_FUND_ROW));  
        tableBody.add(row, 5, getBeanValueParagraph(report, bean,
        		"rorYtd", FONT_SIZE_FUND_ROW));  
        tableBody.add(row, 6, getBeanValueParagraph(report, bean,
        		"ror_1Year", FONT_SIZE_FUND_ROW));  
        tableBody.add(row, 7, getBeanValueParagraph(report, bean,
        		"ror_3Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 8, getBeanValueParagraph(report, bean,
        		"ror_5Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 9, getBeanValueParagraph(report,
    			bean, "ror_10Year", FONT_SIZE_FUND_ROW));
        
        tableBody.add(row, 10, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 11, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 12, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));

    }
    
    @Override
    protected void buildBeanBasedRowWithStandardColumns(StandardReportBuilder report, Object bean,
            PdfTableBody tableBody, int row, String title, String rowName, String footnote) {
       
    	PdfParagraph fundNameParagraph = report.createParagraph()
                .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);
        PdfTableRegion fundNameTableBody = report.createTableBody(1, 2);
		PdfTableRegion[] fundNameTableRegions = new PdfTableRegion[]{fundNameTableBody};
		PdfTable fundNameTable = report.createTable(new String[] {"0.2cm", "8.95cm"}, fundNameTableRegions);
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
        
        
        tableBody.add(row, 3, getBeanValueParagraph(report, bean, 
        		"ror_1Month", FONT_SIZE_FUND_ROW));  
        tableBody.add(row, 4, getBeanValueParagraph(report, bean,
        		"ror_3Month", FONT_SIZE_FUND_ROW));  
        tableBody.add(row, 5, getBeanValueParagraph(report, bean,
        		"rorYtd", FONT_SIZE_FUND_ROW));  
        tableBody.add(row, 6, getBeanValueParagraph(report, bean,
        		"ror_1Year", FONT_SIZE_FUND_ROW));  
        tableBody.add(row, 7, getBeanValueParagraph(report, bean,
        		"ror_3Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 8, getBeanValueParagraph(report, bean,
        		"ror_5Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 9, getBeanValueParagraph(report,
    			bean, "ror_10Year", FONT_SIZE_FUND_ROW));
        
        
        tableBody.add(row, 10, getBeanValueParagraph(report,
        		bean, "ror_1YearQe", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 11, getBeanValueParagraph(report,
        		bean, "ror_5YearQe", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 12, getBeanValueParagraph(report,
    			bean, "ror_10YearQe", FONT_SIZE_FUND_ROW));
        

        tableBody.add(row, 13, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW).setTextAlignment("center"));
        tableBody.add(row, 14, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW).setTextAlignment("center"));
        tableBody.add(row, 15, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));

    }
}