package com.manulife.pension.ireports.report.streamingreport.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.report.AbstractRiskReturnReport;
import com.manulife.pension.ireports.report.CustomPageSequence;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;

public class IR_AICByRiskReturnReport extends AbstractRiskReturnReport {
    public IR_AICByRiskReturnReport(ReportOptions options) {
        super(options, new IR_AICReportDefinition(false));
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
        footerFlow.add(createFootnotes(report, reportData.getCompanyIdFootnotes()));
        footerFlow.add(createStandardRiskDisclosure(report));
        footerFlow.add(createStandardFootnoteBlock(report, this.getClass().getName(),
                reportDefinition.getGeneralDisclosureContentIds()));
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));
        
        PdfParagraph firstPageTitle2HdrParagraph = createTitle2HeaderParagraph();
        PdfParagraph subsequentPageTitle2HdrParagraph = createTitle2HeaderParagraph();
        
        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                firstPageTitle2HdrParagraph);
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                subsequentPageTitle2HdrParagraph);
        
        CustomPageSequence pageSequenceFooter = createCustomPageSequence(report, "",
                "subsequentPageDoubleColumn", firstPageTitleParagraphMap,
                subsequentPageTitleParagraphMap);

        pageSequenceFooter.add(footerFlow);
    }

    private PdfParagraph createTitle2HeaderParagraph() {
        return report.createParagraph(report.createText("Investment Returns")).add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).add(
                " and Expense Ratios").add(Footnotes.createFootnote(report, FOOTNOTE_AIC))
                .setFontFamily(FRUTIGER57CN).setFontSize(14).setTextAlignment("right");
    }
    
    protected boolean containsGuaranteedAccountRates(FundReportData reportData) {
        return false;
    }
}
