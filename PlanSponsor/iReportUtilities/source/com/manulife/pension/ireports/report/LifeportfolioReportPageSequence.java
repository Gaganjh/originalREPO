package com.manulife.pension.ireports.report;

import java.util.Date;

/**
 * Defines the AbstractPageSequence for both Lifecycle and Lifesytle reports.
 */
public class LifeportfolioReportPageSequence extends StandardReportPageSequence {

    public LifeportfolioReportPageSequence(StandardReportBuilder report, String reportName,
            Date asOfDate, ReportOptions options, boolean isLandscape, boolean isLegalSize,
            String headerParagraph, String masterPageReference) {
        super(report, reportName, options, isLandscape, isLegalSize, headerParagraph,
                masterPageReference, null, null);
    }

    protected String lookupReportTitle() {
        return super.getReportName();
    }

    protected String getReportName() {
        return "";
    }
}
