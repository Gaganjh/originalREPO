package com.manulife.pension.ireports.report.streamingreport.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.ReportDefinition;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.StandardReportTemplate;
import com.manulife.pension.ireports.report.util.BottomAlignCellDecoratorFactory;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

public class MorningstarRatingsAndTickersReportDefinition implements ReportDefinition {
	private static final String REPORT_NAME = "Morningstar Ratings and Ticker Symbols";
	private static final String[] COLUMN_WIDTHS= new String[] {
		"6.0cm", // Investment Options 
		"6.0cm", "1.2cm", // Underlying Fund and Ticker Symbols
		"2.0cm",  // Morningstar Category
		"2.0cm",  // Morningstar Rating
		"1.8cm"  // Morningstar # Funds in Cat
	};
	
	public String getReportName() { return REPORT_NAME; }

	public String[] getAsOfDateContexts() {
		// TM 2006/08/04 - spoke with Bill W and Greg M and yes, Fund Metrics is proper for this report
		return new String[] {StandardReportsConstants.REPORT_CONTEXT_FUND_METRICS};
	}

	public boolean isHeaderParagraphPresent() { return false;}

	public String[] getStaticFootnoteSymbols() {
		return new String[]{
				FOOTNOTE_PERFORMANCE_HISTORY,	
				FOOTNOTE_INVESTMENT_OPTIONS,	
				FOOTNOTE_TICKER_SYMBOL,	
				FOOTNOTE_MORNINGSTART_RATING_AND_CATEGORY	
			};
	}
	public boolean containsClassDisclosureFootnote() { return false; }

	//portrait: 7.5in = 19cm
	public boolean isLandscape() { return false; }

	public String[] getColumnWidths() { return COLUMN_WIDTHS; }

	public int getNumberOfColumns() { return COLUMN_WIDTHS.length; }

	public boolean containsGicSection() { return false; }
	public int getGicSectionAnnualRateColumn() { return -1; }
	
	public boolean containsMarketIndexSectionRiskReturn() { return false; }
	public boolean containsMarketIndexSectionAssetClass() { return true; }

	public void justifyReportBodyColumns(PdfTableBody tableBody) {
		tableBody.setColumnTextAlign(3, ReportFormattingConstants.SHIFT_CENTER);
		tableBody.setColumnTextAlign(4, ReportFormattingConstants.SHIFT_CENTER);
		tableBody.setColumnTextAlign(5, ReportFormattingConstants.SHIFT_CENTER);
	}

	public void buildFundRow(StandardReportBuilder report, PdfTableBody fundTableBody, int row, ReportFund reportFund, FundReportData reportData) {
		Fund fund = reportFund.getFund();
		
		Map morningstarCategoryPerformances = reportData.getMorningstarCategoryPerformances();
		String morningstarCategoryName = StandardReportsConstants.NA;
		String threeYearNumberofFundsInCategory = StandardReportsConstants.NA;
		String fundId = fund.getInvestmentid();	
		if (fundId != null) {
			MorningstarCategoryPerformance morningstarCategoryPerf = (MorningstarCategoryPerformance) morningstarCategoryPerformances
					.get(fundId);
			if (morningstarCategoryPerf != null) {
				morningstarCategoryName = morningstarCategoryPerf
						.getMorningstarCategoryName();
				threeYearNumberofFundsInCategory = (morningstarCategoryPerf
						.getThreeYearNumberofFundsInCategory() != null && StringUtils
						.isNotBlank(morningstarCategoryPerf
								.getThreeYearNumberofFundsInCategory().toString().trim()) && morningstarCategoryPerf
								.getThreeYearNumberofFundsInCategory() != 0) ? morningstarCategoryPerf
						.getThreeYearNumberofFundsInCategory().toString()
						: StandardReportsConstants.NA;
			}
		}

		
		PdfParagraph fundNameParagraph = report.createParagraph()
			.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);

		if(reportData.isMerrillAdvisor() && reportData.getRestrictedFunds().containsKey(fundId) 
				&& !(fundId.equals("XX05") || fundId.equals("XX03") || fundId.equals("XX11") || fundId.equals("XX14"))) {
			fundTableBody.add(row, 0, fundNameParagraph.add(report.createText(MERRILL_RESRICTED_FUND_SYMBOL)));
		} 
		
		fundTableBody.add(row, 0, fundNameParagraph.add(report
				.createText(StandardReportsUtils.getValueorNA(fund
						.getFundLongName()))));
		StandardReportTemplate.attachFootnotes(fundNameParagraph, reportFund,
				report);
		fundTableBody.add(row, 1, report.createParagraph().setFontSize(
				FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).add(
				report.createText(StandardReportsUtils.getValueorNA(fund
						.getUnderlyingFundName()))));
		fundTableBody.add(row, 2, report.createParagraph().setFontSize(
				FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).add(
				report.createText(StandardReportsUtils.getValueorNA(fund
						.getTicker()))));
		fundTableBody.add(row, 3, report.createParagraph().setFontSize(
				FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).add(
				report.createText(StandardReportsUtils
						.getValueorNA(morningstarCategoryName))));
		fundTableBody.add(row, 4, report.createParagraph().setFontSize(
				FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).add(
				report.createText(StandardReportsUtils.getValueorNA(fund
						.getMorningstarRating()))));
		fundTableBody.add(row, 5, report.createParagraph().setFontSize(
				FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).add(
				report.createText(StandardReportsUtils
						.getValueorNA(threeYearNumberofFundsInCategory))));
		
		setCellFormattingForRow(fundTableBody, row, false);
	}

	public PdfTableHeader buildMainTableHeader(StandardReportBuilder report, String classDisclosureFootnoteNumber, FundReportData reportData,
			ReportOptions options) {
		PdfTableHeader tableHeader = report.createTableHeader(2, getNumberOfColumns(), new BottomAlignCellDecoratorFactory());
		for(int j = 1; j < getNumberOfColumns(); j++) {
			tableHeader.setColumnTextAlign(j, ReportFormattingConstants.SHIFT_CENTER);
		}

		tableHeader.add(0, 0, report.createParagraph());
		tableHeader.add(0, 1, report.createParagraph());
		tableHeader.add(0, 2, report.createParagraph());
		tableHeader.add(0, 3,  report.createParagraph()
                .setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
                .add(report.createText(" As of  "
                        + DateFormatUtils.format(reportData.getAsOfDateForContext(StandardReportsConstants.REPORT_CONTEXT_MORNINGSTAR_CAT_PERFORMANCE), ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE))));
		tableHeader.spanCells(0, 3, 1, 3);
		setCellFormattingForRow(tableHeader, 0, false);
		setCellFormattingForRow(tableHeader, 1, false);
		
		tableHeader.add(1, 0, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Investment Options"))
				.add(report.createText(FOOTNOTE_INVESTMENT_OPTIONS)
						.setBaseLineShift(ReportFormattingConstants.BASE_LINE_SHIFT_SUPER).setFontSize(FONT_SIZE_FOOTNOTE_SYMBOL)));

		tableHeader.add(1, 1, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
 				.add(report.createText("Underlying Fund and Ticker Symbols"))
				.add(report.createText(FOOTNOTE_TICKER_SYMBOL)
						.setBaseLineShift(ReportFormattingConstants.BASE_LINE_SHIFT_SUPER).setFontSize(FONT_SIZE_FOOTNOTE_SYMBOL)));
		tableHeader.spanCells(1, 1, 1, 2);
		
		tableHeader.add(1, 3, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Morningstar")))
				.add(1, 3, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Category"))
				.add(report.createText(FOOTNOTE_MORNINGSTART_RATING_AND_CATEGORY)
						.setBaseLineShift(ReportFormattingConstants.BASE_LINE_SHIFT_SUPER).setFontSize(FONT_SIZE_FOOTNOTE_SYMBOL)));
		
		tableHeader.add(1, 4, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Morningstar")))
				.add(1, 4, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Overall Rating"))
				.add(report.createText(FOOTNOTE_MORNINGSTART_RATING_AND_CATEGORY)
						.setBaseLineShift(ReportFormattingConstants.BASE_LINE_SHIFT_SUPER).setFontSize(FONT_SIZE_FOOTNOTE_SYMBOL)));
		
		tableHeader.add(1, 5, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Morningstar #")))
				.add(1, 5, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Funds in Cat"))
				.add(report.createText(FOOTNOTE_MORNINGSTART_RATING_AND_CATEGORY)
						.setBaseLineShift(ReportFormattingConstants.BASE_LINE_SHIFT_SUPER).setFontSize(FONT_SIZE_FOOTNOTE_SYMBOL)));

		tableHeader.setBorderBottomStyleForRegion(ReportFormattingConstants.STYLE_SOLID).setBorderBottomWidthForRegion("0.01cm");
		return tableHeader;
	}


    public String[] getIntroDisclosuresContentIds() {
        return new String[] { "64947", "64948", "64949" };
    }
    
    public String[] getIntroGenericLevelDisclosuresContentIds() {
        return new String[] { "97640" };
    }
    
    public String[] getIntroContractLevelDisclosuresContentIds() {
        return new String[] { "97641" };
    }

    public String[] getIncludedMerrillDisclosuresContentIds() {
    	return new String[] { "64947", "64948", "64949", "96181" };
    }
    public String[] getGeneralDisclosureContentIds() {
        return new String[] { "64507", "64508", "64509", "64952", "64953"};
    }
    
    public void setCellFormattingForRow(PdfTableRegion tableRegion, int row, boolean setTopBorder) {
        //set cell top borders to stop colour bleeding from fund row cells
        if (setTopBorder) {
            for (int i = 0; i < getNumberOfColumns(); i++) {
                tableRegion.setCellBorderTopStyle(row, i, ReportFormattingConstants.STYLE_SOLID);
                tableRegion.setCellBorderTopWidth(row, i, "0.5pt");
                tableRegion.setCellBorderTopColor(row, i, ReportFormattingConstants.PDF_RGB_ROW_BORDER);
            }
        }
        
        //set shading for certain columns
        for (int i = 3; i < 6; i++) {
            tableRegion.setCellBackgroundColor(row, i, ReportFormattingConstants.PDF_RGB_SHADING_1);
        }
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

    public boolean isStandardizedReportColumnsSupressed() {
        // TODO Auto-generated method stub
        return false;
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
}