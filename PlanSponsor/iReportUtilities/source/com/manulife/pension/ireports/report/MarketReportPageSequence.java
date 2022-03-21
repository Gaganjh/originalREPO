package com.manulife.pension.ireports.report;

import java.util.Map;

import com.manulife.util.pdf.PdfParagraph;


public class MarketReportPageSequence extends StandardReportPageSequence {
    public MarketReportPageSequence(StandardReportBuilder report, String reportName,
            ReportOptions options, boolean isLandscape, boolean isLegalSize,
            String headerParagraph,
            String masterReference,Map<String, PdfParagraph> firstPageTitleStaticParagraph,
            Map<String, PdfParagraph> subsequentPageTitleStaticParagraph) {
        super(report, reportName, options, isLandscape, isLegalSize, headerParagraph,
                masterReference, firstPageTitleStaticParagraph, subsequentPageTitleStaticParagraph);
    }

    protected String lookupReportTitle() {
        return super.getReportName();
    }

    protected String getReportName() {
        return "Returns of Major Investment Benchmarks";
    }
}
