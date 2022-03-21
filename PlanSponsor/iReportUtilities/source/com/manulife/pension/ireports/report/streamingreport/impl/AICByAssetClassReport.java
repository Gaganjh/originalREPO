package com.manulife.pension.ireports.report.streamingreport.impl;

import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.report.AbstractAssetClassReport;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.StandardReportPageSequence;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;

public class AICByAssetClassReport extends AbstractAssetClassReport {
    public AICByAssetClassReport(ReportOptions options) {
        super(options, new AICReportDefinition());
    }

    protected void definePageSequence(StandardReportBuilder report, PdfFlow flow,
            FundReportData reportData) {

        StandardReportPageSequence pageSequenceMainReport = createPageSequence(report,
                reportDefinition.getReportName(), null, "content", null, null);
        pageSequenceMainReport.add(flow);

        PdfFlow footerFlow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
        footerFlow.add(createFootnotesHeader(report, reportData.getFootnotes()));
        footerFlow.add(createFootnotes(report, reportData.getFootnotes()));
		footerFlow.add(createFootnotes(report, reportData.getCompanyIdFootnotes()));
        footerFlow.add(createStandardFootnoteBlock(report, this.getClass().getName(),
                reportDefinition.getGeneralDisclosureContentIds()));
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));

        StandardReportPageSequence pageSequenceFooter = createPageSequence(report, reportDefinition
                .getReportName(), null, "subsequentPageDoubleColumn", null, null);
        pageSequenceFooter.add(footerFlow);
    }
    
}
