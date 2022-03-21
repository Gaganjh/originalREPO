package com.manulife.pension.ireports.report.streamingreport.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.report.AbstractRiskReturnReport;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.StandardReportPageSequence;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfLeader;
import com.manulife.util.pdf.PdfParagraph;

/**
 * @author Greg McKhool, Tom McGrady
 */
public class FundCharacteristicsByRiskReturnReport extends AbstractRiskReturnReport {
    public FundCharacteristicsByRiskReturnReport(ReportOptions options) {
        super(options, new FundCharacteristicsReportDefinition());
    }

    protected void definePageSequence(StandardReportBuilder report, PdfFlow flow,
            FundReportData reportData) {

        StandardReportPageSequence pageSequenceMainReport = createPageSequence(report,
                reportDefinition.getReportName(), null, "content", null, null);
        pageSequenceMainReport.getFirstPageSubTitleParagraph().add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY));

        pageSequenceMainReport.getSubsequentPageSubTitleParagraph().add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY));

        pageSequenceMainReport.add(flow);

        PdfFlow footerFlow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
        Map<String, String> glossaryEntries = createGlossaryEntries();
        footerFlow.add(createGlossaryHeader(report, glossaryEntries));
        footerFlow.add(createGlossary(report, glossaryEntries));
        footerFlow.add(report.createParagraph()); // needed to make the footnote
        footerFlow.add(createFootnotesHeader(report, reportData.getFootnotes()));
        footerFlow.add(createFootnotes(report, reportData.getFootnotes()));
        footerFlow.add(createFootnotes(report, reportData.getCompanyIdFootnotes()));
        footerFlow.add(createStandardFootnoteBlock(report, this.getClass().getName(),
                reportDefinition.getGeneralDisclosureContentIds()));
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));
        
        StandardReportPageSequence pageSequenceFooter = createPageSequence(report, reportDefinition
                .getReportName(), null, "subsequentPageDoubleColumn", null, null);
        pageSequenceFooter.getFirstPageSubTitleParagraph().add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY));
        pageSequenceFooter.getSubsequentPageSubTitleParagraph().add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY));

        pageSequenceFooter.add(footerFlow);
    }

    private Map<String, String> createGlossaryEntries() {
        Map<String, String> glossaryEntries = new LinkedHashMap<String, String>();
        String termPrefix = "glossary.entry.term.";
        String defPrefix = "glossary.entry.definition.";
        String[] glossaryEntrySuffix;
        if (StandardReportsUtils.isNewYork(options.getCompanyId())) {
            glossaryEntrySuffix = new String[] { "alpha", "benchmark.NY", "beta",
                    "informationRatio", "rSquared", "sharpeRatio", "standardDeviation" };
        } else {
            glossaryEntrySuffix = new String[] { "alpha", "benchmark.USA", "beta",
                    "informationRatio", "rSquared", "sharpeRatio", "standardDeviation" };
        }
        for (int i = 0; i < glossaryEntrySuffix.length; i++) {
            glossaryEntries.put(PropertyManager.getString(termPrefix + glossaryEntrySuffix[i]),
                    PropertyManager.getString(defPrefix + glossaryEntrySuffix[i]));
        }

        return glossaryEntries;
    }

    protected PdfParagraph createGlossaryHeader(StandardReportBuilder report,
            Map<String, String> glossaryEntries) {
        PdfParagraph paragraph = report.createParagraph().setSpan("all");
        paragraph.setBreakBefore("page");
        if (glossaryEntries != null) {
            Set<Entry<String, String>> footnoteEntries = glossaryEntries.entrySet();
            if (!footnoteEntries.isEmpty()) {
                PdfLeader leader = report.createLeader();
                leader.setColor("black");
                if (this.isLandscape()) {
                    leader.setLeaderLength("10.0in");
                } else {
                    leader.setLeaderLength("7.5in");
                }
                leader.setRuleThickness("0.5pt");
                leader.setLeaderPattern("rule");
                paragraph.add(report.createParagraph().add(leader));
                paragraph.add(report.createParagraph("Glossary").setFontFamily(FRUTIGER67BOLD_CN)
                        .setFontSize(10));
                paragraph.add(report.createParagraph("  "));
            }
        }
        return paragraph;
    }

    protected PdfParagraph createGlossary(StandardReportBuilder report,
            Map<String, String> glossaryEntries) {
        PdfParagraph paragraph = report.createParagraph().setSpan("none");
        if (glossaryEntries != null) {
            Set<Entry<String, String>> glossaryEntrySet = glossaryEntries.entrySet();
            for (Entry<String, String> glossaryEntry : glossaryEntrySet) {
                String glossaryTerm = glossaryEntry.getKey();
                String glossaryDefinition = glossaryEntry.getValue();
                PdfParagraph glossaryEntryLine = report.createParagraph();
                if (glossaryTerm != null) {
                    glossaryEntryLine.add(report.createText(glossaryTerm).setFontSize(8)
                            .setFontFamily(FRUTIGER67BOLD_CN));
                }
                if (glossaryTerm != null && glossaryDefinition != null) {
                    glossaryEntryLine.add(report.createText(" - " + glossaryDefinition)
                            .setFontSize(8));
                }
                paragraph.add(glossaryEntryLine);
                paragraph.add(report.createParagraph("   "));
            }
        }
        return paragraph;
    }

}