package com.manulife.pension.ireports.report.streamingreport.impl;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.report.AbstractAssetClassReport;
import com.manulife.pension.ireports.report.CustomPageSequence;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;

public class MorningstarRatingsAndTickersByAssetClassReport extends AbstractAssetClassReport {
    public MorningstarRatingsAndTickersByAssetClassReport(ReportOptions options) {
        super(options, new MorningstarRatingsAndTickersReportDefinition());
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
                "content", firstPageTitleParagraphMap, subsequentPageTitleParagraphMap, 
                StandardReportsConstants.MORNINGSTAR_TICKER_REPORT_FOOTNOTE_DISCLOSURE_ID);
        
        pageSequenceMainReport.add(flow);

        PdfFlow footerFlow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
        footerFlow.add(createFootnotesHeader(report, reportData.getFootnotes()));
		footerFlow.add(createMorningstarFootnotes(report, reportData
				.getMorningstarStaticFootNotes()));
        footerFlow.add(createFootnotes(report, reportData.getFootnotes()));
		footerFlow.add(createMorningstarFootnotes(report, reportData
				.getMorningstarFootNotes()));
		footerFlow.add(createFootnotes(report, reportData.getCompanyIdFootnotes()));
        footerFlow.add(createStandardFootnoteBlock(report, this.getClass().getName(),
                reportDefinition.getGeneralDisclosureContentIds()));
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));

        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());

        CustomPageSequence pageSequenceFooter = createCustomPageSequence(report, null,
                "subsequentPageDoubleColumn", firstPageTitleParagraphMap,
                subsequentPageTitleParagraphMap,
                StandardReportsConstants.MORNINGSTAR_TICKER_REPORT_FOOTNOTE_DISCLOSURE_ID);

        pageSequenceFooter.add(footerFlow);
    }

    /**
     * This method creates the Title2 Header paragraph.
     * 
     * @return
     */
    private PdfParagraph createTitle2HeaderParagraph() {
        return report.createParagraph(report.createText("Morningstar Ratings")).add(
                report.createText(FOOTNOTE_PERFORMANCE_HISTORY).setBaseLineShift("super")
                        .setFontSize(5)).add(" and Ticker Symbols").setFontFamily(FRUTIGER57CN)
                .setFontSize(14).setTextAlignment("right");
    }
}
