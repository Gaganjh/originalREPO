package com.manulife.pension.ireports.report.streamingreport.impl;

import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.content.ContentConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.ReportDefinition;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.StandardReportTemplate;
import com.manulife.pension.ireports.report.util.BottomAlignCellDecoratorFactory;
import com.manulife.pension.ireports.report.util.CMAContentHelper;
import com.manulife.pension.ireports.report.viewbean.FundMetricsViewBean;
import com.manulife.pension.ireports.report.viewbean.FundStandardDeviationViewBean;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundMetrics;
import com.manulife.pension.service.fund.standardreports.valueobject.FundStandardDeviation;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfRGB;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

public class FundCharacteristicsReportDefinition implements ReportDefinition {

	private static final String REPORT_NAME = "Fund Characteristics";
	
	//Landscape: 10.0in = 25.4cm
	private static final String[] COLUMN_WIDTHS = new String[] {
		"6.8cm", // Investment Options
		"3.4cm", // Manager
		"1.1cm", // Alpha
		"1.1cm", // Alpha percent ranking
		"1.1cm", // Beta
		"1.1cm", // Beta percent ranking
		"1.1cm", // R- squared
		"1.1cm", // R squared percent ranking
		"1.1cm", // Sharpe Ratio
		"1.1cm", // Sharpe Ratio percent ranking
		"1.1cm", // Info Ratio
		"1.1cm", // Info Ratio percent ranking
		"1.4cm", // Standard Deviation 3yr
		"3.0cm" // Morningstar Category Peer Group
	};
	
	public String getReportName() {
		return REPORT_NAME;
	}
	
	public String[] getAsOfDateContexts() {
		return new String[] {	StandardReportsConstants.REPORT_CONTEXT_FUND_METRICS, 
                StandardReportsConstants.REPORT_CONTEXT_FUND_STD_DEVIATION };
	}
	
	public String[] getStaticFootnoteSymbols() {
		return  new String[]{
				ReportFormattingConstants.FOOTNOTE_PERFORMANCE_HISTORY,
				ReportFormattingConstants.FOOTNOTE_INVESTMENT_OPTIONS,
				ReportFormattingConstants.FOOTNOTE_FUND_MANAGER,
				ReportFormattingConstants.FOOTNOTE_MORNINGSTAR_CATEGORY,
				};
	}
	public boolean containsClassDisclosureFootnote() { return false; }

	public boolean isLandscape() {return true;}

	public boolean isHeaderParagraphPresent() { return false;}

	public String[] getColumnWidths() {
		return COLUMN_WIDTHS;
	}
	public int getNumberOfColumns() {
		return COLUMN_WIDTHS.length;
	}
	
	public boolean containsGicSection() { return false; }
	public int getGicSectionAnnualRateColumn() { return 2; }
	
	public boolean containsMarketIndexSectionRiskReturn() { return true; }
	public boolean containsMarketIndexSectionAssetClass() { return true; }

	public void justifyReportBodyColumns(PdfTableBody tableBody) {
		for(int j = 2; j < getNumberOfColumns(); j++) {
			tableBody.setColumnTextAlign(j, "center");
		}
	}

	public void buildFundRow(StandardReportBuilder report, PdfTableBody fundTableBody, int row, ReportFund reportFund, FundReportData reportData) {
		
		TextStyle normalTextStyle = new TextStyle(report, ReportFormattingConstants.FRUTIGER47LIGHT_CN, ReportFormattingConstants.FONT_SIZE_FUND_ROW);
		
		Fund fund = reportFund.getFund();
		FundMetrics metrics = reportFund.getFundMetrics();	
		FundMetricsViewBean metricsViewBean = new FundMetricsViewBean(metrics);
		
		Map morningstarCategoryPerformances = reportData.getMorningstarCategoryPerformances();
		
		String morningstarCategoryName = "n/a";
		String fundId = fund.getInvestmentid();	
		if ( fundId != null) {
			MorningstarCategoryPerformance morningstarCategoryPerf = (MorningstarCategoryPerformance) morningstarCategoryPerformances.get(fundId);
			if (morningstarCategoryPerf != null) {
				morningstarCategoryName = morningstarCategoryPerf.getMorningstarCategoryName();
			}
		} 
		PdfParagraph fundNameParagraph = report.createParagraph()
		.setFontSize(ReportFormattingConstants.FONT_SIZE_FUND_ROW).setFontFamily(ReportFormattingConstants.FRUTIGER47LIGHT_CN);

		if(reportData.isMerrillAdvisor() && reportData.getRestrictedFunds().containsKey(fundId) 
				&& !(fundId.equals("XX05") || fundId.equals("XX03") || fundId.equals("XX11") || fundId.equals("XX14"))) {
			fundTableBody.add(row, 0, fundNameParagraph.add(report.createText(MERRILL_RESRICTED_FUND_SYMBOL)));
		} 
		
		fundTableBody.add(row, 0, fundNameParagraph.add(report.createText(StandardReportsUtils
                .getValueorNA(fund.getFundLongName()))));
		StandardReportTemplate.attachFootnotes(fundNameParagraph, reportFund, report);
		fundTableBody.add(row, 1, normalTextStyle
                .createParagraph(StandardReportsUtils.getValueorNA(fund.getInvmanagername())));
        if(!metrics.isRor3yrHypothetical()) {
    		fundTableBody.add(row, 2, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getAlpha())) );
    		fundTableBody.add(row, 3, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getAlphaPercent()) ));
    		fundTableBody.add(row, 4, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getBeta()) ));
    		fundTableBody.add(row, 5, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getBetaPercent()) ));		
    		fundTableBody.add(row, 6, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getRSquared()) ));
    		fundTableBody.add(row, 7, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getRSquaredPercent()) ));
    		fundTableBody.add(row, 8, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getSharpeRatio()) ));
    		fundTableBody.add(row, 9, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getSharpeRatioPercent()) ));
    		fundTableBody.add(row, 10, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getInfoRatio()) ));
    		fundTableBody.add(row, 11, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getInfoRatioPercent()) ));
        } else {
            fundTableBody.add(row, 2, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getAlpha())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
            fundTableBody.add(row, 3, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getAlphaPercent())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
            fundTableBody.add(row, 4, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getBeta())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
            fundTableBody.add(row, 5, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getBetaPercent())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));     
            fundTableBody.add(row, 6, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getRSquared())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
            fundTableBody.add(row, 7, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getRSquaredPercent())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
            fundTableBody.add(row, 8, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getSharpeRatio())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
            fundTableBody.add(row, 9, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getSharpeRatioPercent())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
            fundTableBody.add(row, 10, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getInfoRatio())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
            fundTableBody.add(row, 11, normalTextStyle.createParagraph(StandardReportsUtils.getValueorNA(metricsViewBean.getInfoRatioPercent())).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
        }
		FundStandardDeviation fundStandardDeviation = reportFund.getFundStandardDeviation();
		
		FundStandardDeviationViewBean standardDeviationViewBean = new FundStandardDeviationViewBean(fundStandardDeviation);
		if(!metrics.isRor3yrHypothetical()) {
		    fundTableBody.add(row, 12, StandardReportTemplate.getMetricValueParagraph(report, standardDeviationViewBean, ROR_3YR, FONT_SIZE_FUND_ROW));
        } else {
            fundTableBody.add(row, 12, StandardReportTemplate.getMetricValueParagraph(report, standardDeviationViewBean, ROR_3YR, FONT_SIZE_FUND_ROW).setFontWeight("bold").setFontFamily(FRUTIGER_SR));
        }
		fundTableBody.add(row, 13, normalTextStyle.createParagraph(StandardReportsUtils
                .getValueorNA(morningstarCategoryName)));
	}

	public PdfTableHeader buildMainTableHeader(StandardReportBuilder report, String classDisclosureFootnoteNumber, FundReportData reportData, ReportOptions options) {
		
		TextStyle firstRowHeaderTextStyle = new TextStyle(report, ReportFormattingConstants.FRUTIGER56ITALIC, ReportFormattingConstants.FONT_SIZE_COLUMN_HEADING);
		TextStyle secondRowHeaderTextStyle = new TextStyle(report, ReportFormattingConstants.FRUTIGER67BOLD_CN, ReportFormattingConstants.FONT_SIZE_COLUMN_HEADING);

		PdfTableHeader tableHeader = report.createTableHeader(3, getNumberOfColumns(), new BottomAlignCellDecoratorFactory());
		for(int j = 2; j < getNumberOfColumns(); j++) {
			tableHeader.setColumnTextAlign(j, "center");
		}
		
		
		tableHeader.add(0, 0, report.createParagraph());
        tableHeader.add(0, 1, report.createParagraph());
        PdfParagraph introDisclosureParagraph = createIntroDisclosures(report, ContentConstants.FUND_CHARACTERISTICS_INTRO_DISCLOSURE_KEY_3, options
                .getCompanyId());
        tableHeader.add(0, 2, introDisclosureParagraph);
        tableHeader.spanCells(0, 2, 1, 11);
        tableHeader.add(0, 13, report.createParagraph());
		

        setCellFormattingForRow(tableHeader, 1, false);
        setCellFormattingForRow(tableHeader, 2, false);
        
		tableHeader.add(1, 0, report.createParagraph());
        tableHeader.add(1, 1, report.createParagraph());
        String fundCharacteristicsAsOfDate = DateFormatUtils.format(reportData
                .getAsOfDateForContext(getAsOfDateContexts()[0]),
                ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE);
		tableHeader.add(1, 2, firstRowHeaderTextStyle.createParagraph("Fund Characteristics as of " + fundCharacteristicsAsOfDate ).setTextAlignment("center"));
		tableHeader.spanCells(1, 2, 1, 10);
		
        String stdDeviationAsOfDate = DateFormatUtils.format(reportData
                .getAsOfDateForContext(getAsOfDateContexts()[1]),
                ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE);
		tableHeader.add(1, 12, firstRowHeaderTextStyle.createParagraph("Standard")	.setTextAlignment("center"))
							.add(1, 12, firstRowHeaderTextStyle.createParagraph("Deviation")	.setTextAlignment("center"))
                            .add(
                        1,
                        12,
                        firstRowHeaderTextStyle.createParagraph("as of " + stdDeviationAsOfDate)
                                .setTextAlignment("center"));		
		tableHeader.add(1, 13, firstRowHeaderTextStyle.createParagraph("Morningstar Category")	.setTextAlignment("center"))
							.add(1, 13, firstRowHeaderTextStyle.createParagraph("Peer Group")	.setTextAlignment("center")
									.add(Footnotes.createFootnote(report, ReportFormattingConstants.FOOTNOTE_MORNINGSTAR_CATEGORY)));
		
		tableHeader.add(2, 0, secondRowHeaderTextStyle.createParagraph("Investment Options")
				.add(Footnotes.createFootnote(report, ReportFormattingConstants.FOOTNOTE_INVESTMENT_OPTIONS)));
		tableHeader.add(2, 1, secondRowHeaderTextStyle.createParagraph("Sub-Adviser/Manager")
				.add(Footnotes.createFootnote(report, ReportFormattingConstants.FOOTNOTE_FUND_MANAGER)));
		tableHeader.add(2, 2, secondRowHeaderTextStyle.createParagraph("Alpha"));
		tableHeader.add(2, 3, secondRowHeaderTextStyle.createParagraph("%"))
			.add(2, 3, secondRowHeaderTextStyle.createParagraph("Ranking"));
		tableHeader.add(2, 4, secondRowHeaderTextStyle.createParagraph("Beta"));
		tableHeader.add(2, 5, secondRowHeaderTextStyle.createParagraph("%"))
			.add(2, 5, secondRowHeaderTextStyle.createParagraph("Ranking"));
		tableHeader.add(2, 6, secondRowHeaderTextStyle.createParagraph("R-"))
			.add(2, 6, secondRowHeaderTextStyle.createParagraph("Squared"));
		tableHeader.add(2, 7, secondRowHeaderTextStyle.createParagraph("%"))
			.add(2, 7, secondRowHeaderTextStyle.createParagraph("Ranking"));
		tableHeader.add(2, 8, secondRowHeaderTextStyle.createParagraph("Sharpe Ratio"));
		tableHeader.add(2, 9, secondRowHeaderTextStyle.createParagraph("%"))
			.add(2, 9, secondRowHeaderTextStyle.createParagraph("Ranking"));
		tableHeader.add(2, 10, secondRowHeaderTextStyle.createParagraph("Info Ratio"));
		tableHeader.add(2, 11, secondRowHeaderTextStyle.createParagraph("%"))
			.add(2, 11, secondRowHeaderTextStyle.createParagraph("Ranking"));
		tableHeader.add(2, 12, secondRowHeaderTextStyle.createParagraph("3yr"));
		tableHeader.add(2, 13, report.createParagraph());
		
		tableHeader.setBorderBottomStyleForRegion("solid").setBorderBottomWidthForRegion("0.01cm");
		return tableHeader;
	}

	public void setCellFormattingForRow(PdfTableRegion tableRegion, int row, boolean setTopBorder) {
		//set cell top borders to stop colour bleeding from fund row cells
		if (setTopBorder) {
			for (int i = 0; i < getNumberOfColumns(); i++) {
				tableRegion.setCellBorderTopStyle(row, i, "solid");
				tableRegion.setCellBorderTopWidth(row, i, "0.5pt");
				tableRegion.setCellBorderTopColor(row, i, ReportFormattingConstants.PDF_RGB_ROW_BORDER);
			}
		}
		
		//set shading for certain columns
		for (int i = 2; i < 12; i++) {
			tableRegion.setCellBackgroundColor(row, i, ReportFormattingConstants.PDF_RGB_SHADING_1);
		}
		tableRegion.setCellBackgroundColor(row, 12, ReportFormattingConstants.PDF_RGB_SHADING_2);
		tableRegion.setCellBackgroundColor(row, 13, ReportFormattingConstants.PDF_RGB_SHADING_1);
	}

    public String[] getIntroDisclosuresContentIds() {
        return new String[] { ContentConstants.FUND_CHARACTERISTICS_INTRO_DISCLOSURE_KEY_1,
                ContentConstants.FUND_CHARACTERISTICS_INTRO_DISCLOSURE_KEY_2};
    }
    
    public String[] getIntroGenericLevelDisclosuresContentIds() {
        return new String[] { "97640" };
    }
    
    public String[] getIntroContractLevelDisclosuresContentIds() {
        return new String[] { "97641" };
    }
    
    public String[] getIncludedMerrillDisclosuresContentIds() {
        return new String[] { ContentConstants.FUND_CHARACTERISTICS_INTRO_DISCLOSURE_KEY_1,
                ContentConstants.FUND_CHARACTERISTICS_INTRO_DISCLOSURE_KEY_2,
                ContentConstants.MERRILL_RESRICTED_FUNDS_CONTENT };
    }
    public String[] getGeneralDisclosureContentIds() {
        return new String[] { ContentConstants.GENERAL_DISCLOSURE_KEY_1,
                ContentConstants.GENERAL_DISCLOSURE_KEY_2,
                ContentConstants.GENERAL_DISCLOSURE_KEY_3,
                ContentConstants.GENERAL_DISCLOSURE_KEY_4,
                ContentConstants.GENERAL_DISCLOSURE_FUND_CHARACTERISTICS_REPORT };
    }

    public PdfTable buildLastRow(StandardReportBuilder report, int row, FundReportData reportData) {
    	// TODO Auto-generated method stub
		return null;
		
	}

	public boolean isLegalSize() {
		// TODO Auto-generated method stub
		return false;
	}

    public boolean containsStandardizedReportColumns() {
        // TODO Auto-generated method stub
        return false;
    }

    public void hideStandardizedReportColumns() {
        // TODO Auto-generated method stub
        
    }

	public PdfTableBody buildDisclosureTextRow(StandardReportBuilder report,
			PdfTableBody tableBody, int row, int numberOfColumns,
			ReportFund reportFund) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowMMFDisclosureText() {
		// TODO Auto-generated method stub
		return false;
	}

    public boolean isStandardizedReportColumnsSupressed() {
        // TODO Auto-generated method stub
        return false;
    }

    protected PdfParagraph createIntroDisclosures(StandardReportBuilder report, String contentId,
            String companyId) {
        PdfParagraph contentParagraph = null;

        contentParagraph = CMAContentHelper.createIntroDisclosureParagraph(report, contentId,
                companyId);

        return contentParagraph;
    }
}
