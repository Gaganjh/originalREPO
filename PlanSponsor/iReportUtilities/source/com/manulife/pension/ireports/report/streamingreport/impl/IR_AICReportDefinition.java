package com.manulife.pension.ireports.report.streamingreport.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.ReportDefinition;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.StandardReportTemplate;
import com.manulife.pension.ireports.report.util.BottomAlignCellDecoratorFactory;
import com.manulife.pension.ireports.report.util.CMAContentHelper;
import com.manulife.pension.ireports.report.viewbean.FundExpensesViewBean;
import com.manulife.pension.ireports.report.viewbean.FundMetricsViewBean;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundMetrics;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

public class IR_AICReportDefinition implements ReportDefinition {

	private static final String REPORT_NAME = "Investment Returns and Expense Ratios";
	
	private boolean isAssetClassSorting;
	    
	public IR_AICReportDefinition(boolean isAssetClassSorting) {
		this.isAssetClassSorting = isAssetClassSorting;
	}
	
	public boolean isAssetClassSorting() {
		return this.isAssetClassSorting;
	}

	//Landscape: 14in = 35.56cm
	private static final String[] COLUMN_WIDTHS_EXPANDED = new String[] {
            "7.85cm", // Investment Options
            "5.45cm", // Sub-Adviser
			"1.2cm", // Inception Date
			"1.1cm", // Annualized returns 1mo
			"1.1cm", // Annualized returns 3mo
			"1.1cm", // Annualized returns ytd
			"1.1cm", // Annualized returns 1yr
			"1.05cm", // Annualized returns 3yr
			"1.05cm", // Annualized returns 5yr
			"1.6cm", // Annualized returns 10yr/Since inception
			"1.05cm", // Quarterly returns 1yr
			"1.05cm", // Quarterly returns 5yr
			"1.6cm", // Quarterly returns 10yr/Since inception
			"2.7cm", // ER (previously labelled as "AIC")
			"3.0cm", // Mstar cat
			"1.0cm" // Mstar avg exp ratio
			};
	
	private static final String[] COLUMN_WIDTHS_NORMAL = new String[] {
        "9.15cm", // Investment Options
        "7.45cm", // Sub-Adviser
		"1.2cm", // Inception Date
		"1.2cm", // Annualized returns 1mo
		"1.2cm", // Annualized returns 3mo
		"1.2cm", // Annualized returns ytd
		"1.2cm", // Annualized returns 1yr
		"1.05cm", // Annualized returns 3yr
		"1.05cm", // Annualized returns 5yr
		"1.6cm", // Annualized returns 10yr/Since inception
		"2.7cm", // ER (previously labelled as "AIC")
		"3.0cm", // Mstar cat
		"1.0cm" // Mstar avg exp ratio
		};
	
	private static  String[] COLUMN_WIDTHS = COLUMN_WIDTHS_EXPANDED;
	
	private static boolean isStandarizedColumnsSuppresed = false ;
	
	public String getReportName() {
		return REPORT_NAME;
	}
	
	public String[] getAsOfDateContexts() {
		return new String[] {StandardReportsConstants.REPORT_CONTEXT_FUND_RETURNS};
	}
	public String[] getStaticFootnoteSymbols() {
		return  new String[]{
				FOOTNOTE_PERFORMANCE_HISTORY,
				FOOTNOTE_INVESTMENT_OPTIONS,
				FOOTNOTE_FUND_MANAGER,
				FOOTNOTE_AIC,
				FOOTNOTE_MORNINGSTAR_CATEGORY,
				FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK,
				FOOTNOTE_INCEPTION_DATE,
                FOOTNOTE_3Y_5Y_10Y_GIC };
	}
	
	public boolean containsClassDisclosureFootnote() { return false; }

	public boolean isLandscape() {return false;}
	
	public boolean isLegalSize() { return true; }

	public boolean isHeaderParagraphPresent() { return false;}

	public String[] getColumnWidths() {
		return COLUMN_WIDTHS;
	}
	public int getNumberOfColumns() {
		return COLUMN_WIDTHS.length;
	}
	
	public boolean containsGicSection() { return true; }
	public int getGicSectionAnnualRateColumn() { return 5; }
	
	public boolean containsMarketIndexSectionRiskReturn() { return false; }
	public boolean containsMarketIndexSectionAssetClass() { return false; }

	public void justifyReportBodyColumns(PdfTableBody tableBody) {
		for(int j = 3; j < getNumberOfColumns(); j++) {
			tableBody.setColumnTextAlign(j, "right");
		}
	}

	public void buildFundRow(StandardReportBuilder report, PdfTableBody fundTableBody, int row, ReportFund reportFund, FundReportData reportData) {
		Fund fund = reportFund.getFund();
		FundExpensesViewBean expensesViewBean = 
			new FundExpensesViewBean(reportFund.getFundExpenses());
		
		Map morningstarCategoryPerformances  = reportData.getMorningstarCategoryPerformances();
		String morningstarCategoryName = "n/a";
		BigDecimal morningstarCategoryExpenseRatio = null;
		String fundId = fund.getInvestmentid();	
		if ( fundId != null) {
			MorningstarCategoryPerformance morningstarCategoryPerf = (MorningstarCategoryPerformance) morningstarCategoryPerformances.get(fundId);
			if (morningstarCategoryPerf != null) {
				morningstarCategoryName = morningstarCategoryPerf.getMorningstarCategoryName();
				morningstarCategoryExpenseRatio = morningstarCategoryPerf.getExpenseRatio();
			}
		} 
		
		PdfParagraph fundNameParagraph = report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);
        PdfTableRegion fundNameTableBody = report.createTableBody(1, 2);
		PdfTableRegion[] fundNameTableRegions = new PdfTableRegion[]{fundNameTableBody};
		PdfTable fundNameTable = null;
		if (isStandardizedReportColumnsSupressed()) {
			fundNameTable = report.createTable(new String[] {"0.2cm", "8.95cm"}, fundNameTableRegions);
		} else {
			fundNameTable = report.createTable(new String[] {"0.2cm", "7.65cm"}, fundNameTableRegions);
		}

		if(reportData.getFeeWaiverFundIds().contains(fund.getInvestmentid()) && (reportData.isMerrillAdvisor() && reportData.getRestrictedFunds().containsKey(fund.getInvestmentid()))){
		    fundNameTableBody.add(0, 0, report.createParagraph(DOT_INDICATOR+"\n"+MERRILL_RESRICTED_FUND_SYMBOL));
		} else if(reportData.getFeeWaiverFundIds().contains(fund.getInvestmentid())) {
			fundNameTableBody.add(0, 0, report.createParagraph(DOT_INDICATOR));
		} else if(reportData.isMerrillAdvisor() && reportData.getRestrictedFunds().containsKey(fund.getInvestmentid())
				&& !(fund.getInvestmentid().equals("XX05") || fund.getInvestmentid().equals("XX03") || fund.getInvestmentid().equals("XX11") || fund.getInvestmentid().equals("XX14"))) {
			fundNameTableBody.add(0, 0, report.createParagraph(MERRILL_RESRICTED_FUND_SYMBOL));
		} else {
			fundNameTableBody.add(0, 0, report.createParagraph(" "));
		}
		
		fundNameTableBody.add(0, 1, fundNameParagraph
				.add(report.createText(StandardReportsUtils.getValueorNA(fund.getFundLongName()))));
		StandardReportTemplate.attachFootnotes(fundNameParagraph, reportFund, report);
		fundTableBody.add(row, 0, fundNameTable);
		
		fundTableBody.add(row, 1, report.createParagraph()
			.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
			.add(report.createText(StandardReportsUtils.getValueorNA(fund.getInvmanagername()))));

		FundMetrics metrics = reportFund.getFundMetrics();

		fundTableBody.add(row, 2, report.createParagraph()
			.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
			.add(report.createText(metrics.getInceptionDate() == null ? 
			        StandardReportsConstants.NA : 
			            DateFormatUtils.format(metrics.getInceptionDate(), DATE_PATTERN_FUND_DATA))));
		
		FundMetricsViewBean metricsViewBean = new FundMetricsViewBean(metrics);
		
		fundTableBody.add(row, 3, StandardReportTemplate.getMetricValueParagraph(report, metricsViewBean, ROR_1MTH, FONT_SIZE_FUND_ROW));  
		fundTableBody.add(row, 4, StandardReportTemplate.getMetricValueParagraph(report, metricsViewBean, ROR_3MTH, FONT_SIZE_FUND_ROW));  
		fundTableBody.add(row, 5, StandardReportTemplate.getMetricValueParagraph(report, metricsViewBean, ROR_YTD, FONT_SIZE_FUND_ROW));  
		fundTableBody.add(row, 6, StandardReportTemplate.getMetricValueParagraph(report, metricsViewBean, ROR_1YR, FONT_SIZE_FUND_ROW));  
		fundTableBody.add(row, 7, StandardReportTemplate.getMetricValueParagraph(report, metricsViewBean, ROR_3YR, FONT_SIZE_FUND_ROW));
		fundTableBody.add(row, 8, StandardReportTemplate.getMetricValueParagraph(report, metricsViewBean, ROR_5YR, FONT_SIZE_FUND_ROW));
		
		if (checkIf10YearRorExists(metricsViewBean, ROR_10YR)) {
		       fundTableBody.add(row, 9, StandardReportTemplate.getMetricValueParagraph(report,
		               metricsViewBean, ROR_10YR, FONT_SIZE_FUND_ROW));
		} else {
            fundTableBody.add(row, 9, StandardReportTemplate.getMetricValueParagraphItalicied(report,
                    metricsViewBean, ROR_SINCEINCEPTION, FONT_SIZE_FUND_ROW));
		}
		
		if (isStandardizedReportColumnsSupressed()) {
	        
	        if (fund.isMarketIndex() || fund.isGuaranteedAccount()) {
				fundTableBody.add(row, 10, report.createParagraph()
						.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).setTextAlignment("center")
						.add(report.createText("n/a")));
				
			} else {
				fundTableBody.add(row, 10, report.createParagraph()
						.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).setTextAlignment("center")
						.add(report.createText(StandardReportsUtils.getValueorNA(expensesViewBean.getAnnualInvestmentCharge()))));
			}
			
			fundTableBody.add(row, 11, report.createParagraph()
					.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).setTextAlignment("center")
					.add(report.createText(StandardReportsUtils.getValueorNA(morningstarCategoryName))));
			
			if (morningstarCategoryExpenseRatio == null){
				fundTableBody.add(row, 12, report.createParagraph()
						.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
						.add(report.createText("n/a")));
			} else {
				fundTableBody.add(row, 12, report.createParagraph()
						.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
						.add(report.createText(StandardReportsUtils.formatPercentage(morningstarCategoryExpenseRatio))));
			}
            
        } else {
        	
        	 fundTableBody.add(row, 10, StandardReportTemplate.getMetricValueParagraph(report,
                     metricsViewBean, ROR_1YR_QE, FONT_SIZE_FUND_ROW));
             fundTableBody.add(row, 11, StandardReportTemplate.getMetricValueParagraph(report,
                     metricsViewBean, ROR_5YR_QE, FONT_SIZE_FUND_ROW));

             if (checkIf10YearRorExists(metricsViewBean, ROR_10YR_QE)) {
                 fundTableBody.add(row, 12, StandardReportTemplate.getMetricValueParagraph(report,
                         metricsViewBean, ROR_10YR_QE, FONT_SIZE_FUND_ROW));
             } else {
                 fundTableBody.add(row, 12, StandardReportTemplate.getMetricValueParagraphItalicied(
                         report, metricsViewBean, ROR_SINCEINCEPTION_QE, FONT_SIZE_FUND_ROW));
             }

			if (fund.isMarketIndex() || fund.isGuaranteedAccount()) {
				fundTableBody.add(row, 13, report.createParagraph()
						.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).setTextAlignment("center")
						.add(report.createText("n/a")));
				
			} else {
				fundTableBody.add(row, 13, report.createParagraph()
						.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).setTextAlignment("center")
						.add(report.createText(StandardReportsUtils.getValueorNA(expensesViewBean.getAnnualInvestmentCharge()))));
			}
			
			fundTableBody.add(row, 14, report.createParagraph()
					.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN).setTextAlignment("center")
					.add(
                            report.createText(StandardReportsUtils
                                    .getValueorNA(morningstarCategoryName))));
			
			if (morningstarCategoryExpenseRatio == null){
				fundTableBody.add(row, 15, report.createParagraph()
						.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
						.add(report.createText("n/a")));
			} else {
				fundTableBody.add(row, 15, report.createParagraph()
						.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
						.add(report.createText(StandardReportsUtils.formatPercentage(morningstarCategoryExpenseRatio))));
			}
		
        }
		
	}

	public PdfTableHeader buildMainTableHeader(StandardReportBuilder report, String classDisclosureFootnoteNumber, FundReportData reportData, ReportOptions options) {
		PdfTableHeader tableHeader = report.createTableHeader(3, getNumberOfColumns(), new BottomAlignCellDecoratorFactory());
		for(int j = 3; j < getNumberOfColumns()-1; j++) {
			tableHeader.setColumnTextAlign(j, "center");
		}
		setCellFormattingForRow(tableHeader, 1, false);
		setCellFormattingForRow(tableHeader, 2, false);
	
		tableHeader.add(0, 0, report.createParagraph());
		tableHeader.add(0, 1, report.createParagraph());
        tableHeader.add(0, 2, report.createParagraph());
		
        PdfParagraph introParagraph = CMAContentHelper.createIntroDisclosureParagraph(report, "64925",
                options.getCompanyId());
        
		tableHeader.add(0, 3, introParagraph.setTextAlignment("center"));
		if (isStandardizedReportColumnsSupressed()) {
			tableHeader.spanCells(0, 3, 1, 7);
		} else {
			tableHeader.spanCells(0, 3, 1, 10);
		}
		
		if (isStandardizedReportColumnsSupressed()) {
			for (int i = 10; i < getNumberOfColumns(); i++) {
	            tableHeader.add(0, i, report.createParagraph());
	        }
		} else {
			for (int i = 13; i < getNumberOfColumns(); i++) {
	            tableHeader.add(0, i, report.createParagraph());
	        }
		}
		
		tableHeader.add(1, 0, report.createParagraph());
		tableHeader.add(1, 1, report.createParagraph());
        tableHeader.add(1, 2, report.createParagraph());
        
        tableHeader.add(1, 3, report.createParagraph()
				.add(
                report.createText(
                        "Returns as of  "
                                + DateFormatUtils.format(reportData.getAsOfDateForContext("ROR"),
                                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE))
                        .setFontFamily(FRUTIGER56ITALIC))
				.setTextAlignment("center").setFontSize(FONT_SIZE_COLUMN_HEADING)
				);
		tableHeader.spanCells(1, 3, 1, 7);

		if (isStandardizedReportColumnsSupressed()) {
			tableHeader.add(1, 10, report.createParagraph()
						.add(
	                report.createText(
	                        "As of  "
	                                + DateFormatUtils.format(reportData.getAsOfDateForContext("FER"),
	                                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE))
	                        .setFontFamily(FRUTIGER56ITALIC))
						.setTextAlignment("center").setFontSize(FONT_SIZE_COLUMN_HEADING)
						);
            tableHeader.spanCells(1, 10, 1, 1);
            tableHeader.add(1, 11, report.createParagraph());
            tableHeader.add(1, 12, report.createParagraph());
		} else {
			tableHeader.add(1, 10, report.createParagraph()
						.add(
		                report.createText(
		                        "Returns as of  "
		                                + DateFormatUtils.format(reportData.getAsOfDateForContext("RORQE"),
		                                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE))
		                        .setFontFamily(FRUTIGER56ITALIC))
						.setTextAlignment("center").setFontSize(FONT_SIZE_COLUMN_HEADING)
						);
			tableHeader.spanCells(1, 10, 1, 3);
			
			tableHeader.add(1, 13, report.createParagraph()
						.add(
	                report.createText(
	                        "As of  "
	                                + DateFormatUtils.format(reportData.getAsOfDateForContext("FER"),
	                                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE))
	                        .setFontFamily(FRUTIGER56ITALIC))
						.setTextAlignment("center").setFontSize(FONT_SIZE_COLUMN_HEADING)
						);
            tableHeader.spanCells(1, 13, 1, 1);
            tableHeader.add(1, 14, report.createParagraph());
            tableHeader.add(1, 15, report.createParagraph());
		}
		
		tableHeader.add(2, 0, createHeaderText(report, "Investment Options")
				.add(Footnotes.createFootnote(report, FOOTNOTE_INVESTMENT_OPTIONS)));
		
		if(isAssetClassSorting()) {
			tableHeader.add(2, 1, createHeaderText(report, "Manager Name/Benchmark")
					.add(Footnotes.createFootnote(report, FOOTNOTE_FUND_MANAGER)));
		} else {
			tableHeader.add(2, 1, createHeaderText(report, "Manager Name")
					.add(Footnotes.createFootnote(report, FOOTNOTE_FUND_MANAGER)));
		}
		
		
		tableHeader.add(2, 2, createHeaderText(report, "Inception"))
				.add(2, 2, createHeaderText(report,"Date")
				.add(Footnotes.createFootnote(report, FOOTNOTE_INCEPTION_DATE)));
		tableHeader.add(2, 3, createHeaderText(report,"1mo"));
		tableHeader.add(2, 4, createHeaderText(report,"3mo"));
		tableHeader.add(2, 5, createHeaderText(report,"YTD"));
		tableHeader.add(2, 6, createHeaderText(report,"1yr"));
		tableHeader.add(2, 7, createHeaderText(report,"3yr"));
		tableHeader.add(2, 8, createHeaderText(report,"5yr"));
		tableHeader.add(2, 9, createHeaderText(report,"10yr/"))
				.add(2, 9, createHeaderText(report,"Inception").setFontFamily(FRUTIGER66BOLD_ITALIC)
				.add(Footnotes.createFootnote(report, FOOTNOTE_INCEPTION_DATE)));
		if (isStandardizedReportColumnsSupressed()) {

		    PdfParagraph erHeader = createHeaderText(report,"Expense Ratio").add(Footnotes.createFootnote(report, FOOTNOTE_AIC));
            tableHeader.add(2, 10, erHeader);
            tableHeader
                    .add(2, 11, createHeaderText(report, "Morningstar Benchmark"))
                    .add(2,11,createHeaderText(report, "Category")
					.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_CATEGORY)))	
                    .add(2,11,createHeaderText(report, "and Average Expense Ratio")
					.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK)))
                    .add(2,11,report.createParagraph()
					.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
					.add(report.createText("as of "))
					.add(DateFormatUtils.format(reportData.getAsOfDateForContext("MSP"),
	                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE)));
            tableHeader.spanCells(2, 11, 1, 2);
		} else {
			tableHeader.add(2, 10, createHeaderText(report,"1yr"));
			tableHeader.add(2, 11, createHeaderText(report,"5yr"));
			tableHeader.add(2, 12, createHeaderText(report,"10yr/"))
					.add(2, 12, createHeaderText(report,"Inception").setFontFamily(FRUTIGER66BOLD_ITALIC)
					.add(Footnotes.createFootnote(report, FOOTNOTE_INCEPTION_DATE)));
			
			
			PdfParagraph erHeader = createHeaderText(report,"Expense Ratio").add(Footnotes.createFootnote(report, FOOTNOTE_AIC));
            tableHeader.add(2, 13, erHeader);
            tableHeader.add(2, 14, createHeaderText(report, "Morningstar Benchmark"))                    
                .add(2,14,createHeaderText(report, "Category")
				.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_CATEGORY)))	
                .add(2,14,createHeaderText(report, "and Average Expense Ratio")
				.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK)))
                .add(2,14,report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("as of "))
				.add(DateFormatUtils.format(reportData.getAsOfDateForContext("MSP"),
	                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE)));
            tableHeader.spanCells(2, 14, 1, 2);
		}
		
		tableHeader.setBorderBottomStyleForRegion("solid").setBorderBottomWidthForRegion("0.01cm");
	
		return tableHeader;
	}

	public void setCellFormattingForRow(PdfTableRegion tableRegion, int row, boolean setTopBorder) {
		//set cell top borders to stop colour bleeding from fund row cells
		if (setTopBorder) {
			for (int i = 0; i < getNumberOfColumns(); i++) {
				tableRegion.setCellBorderTopStyle(row, i, "solid");
				tableRegion.setCellBorderTopWidth(row, i, "0.5pt");
				tableRegion.setCellBorderTopColor(row, i, PDF_RGB_ROW_BORDER);
			}
		}
		
		if (isStandardizedReportColumnsSupressed()) {
            for (int i = 3; i < 10; i++) {
				tableRegion.setCellBackgroundColor(row, i, PDF_RGB_SHADING_1);
			}
			tableRegion.setCellBackgroundColor(row, 10, PDF_RGB_SHADING_2);
			
			//other formatting
            tableRegion.setCellPaddingRight(row, 11, "0.1cm");
			tableRegion.setCellPaddingRight(row, 12, "0.1cm");
		} else {
			//set shading for certain columns
            for (int i = 3; i < 13; i++) {
				tableRegion.setCellBackgroundColor(row, i, PDF_RGB_SHADING_1);
			}
			tableRegion.setCellBackgroundColor(row, 13, PDF_RGB_SHADING_2);
			
			//other formatting
            tableRegion.setCellPaddingRight(row, 14, "0.1cm");
			tableRegion.setCellPaddingRight(row, 15, "0.1cm");
		}
	}

	private PdfParagraph createHeaderText(StandardReportBuilder report, String text) {
		return report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING)
				.setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText(text));
	}
	
	/**
     * This method returns the table for the last row.
     */
	public PdfTable buildLastRow(StandardReportBuilder report, int row, FundReportData reportData) {
		return null;
	}

	public String[] getIntroDisclosuresContentIds() {
        return new String[] { "64996", "64995", "90936"};
    }
    
    public String[] getIntroGenericLevelDisclosuresContentIds() {
        return new String[] { "97640" };
    }
    
    public String[] getIntroContractLevelDisclosuresContentIds() {
        return new String[] { "97641" };
    }
	public String[] getIncludedMerrillDisclosuresContentIds() {
        return new String[] { "64996", "64995", "90936", "96181"};
    }
	
	/**
     * This method returns the content Ids for General Disclosure.
     */
    public String[] getGeneralDisclosureContentIds() {
        return new String[] { "64507", "64508", "64509", "64510", "64999" };
    }

    public boolean containsStandardizedReportColumns() {
        return true;
    }

    public void  hideStandardizedReportColumns() {
        COLUMN_WIDTHS =  COLUMN_WIDTHS_NORMAL;
        isStandarizedColumnsSuppresed = true;
    }

    public boolean isStandardizedReportColumnsSupressed() {
        return isStandarizedColumnsSuppresed;
    }
	
	public PdfTableBody buildDisclosureTextRow( StandardReportBuilder report,  PdfTableBody tableBody, int row, int numberOfColumns, ReportFund reportFund) {
	    TextStyle normalTextStyle = new TextStyle(report, ReportFormattingConstants.FRUTIGER47LIGHT_CN,ReportFormattingConstants.FONT_SIZE_FUND_ROW);
	    String fundDisclosureText = reportFund.getFundStandardDeviation().getFundDisclosureText();
	    tableBody.add(row, 0, normalTextStyle
                .createParagraph(fundDisclosureText)).setCellPaddingLeft(row, 0, "0.2cm");
	    tableBody.spanCells(row, 0, 1, numberOfColumns);
	    tableBody.setRowKeepWithPrevious(row);
	    return tableBody;
    }

	private boolean checkIf10YearRorExists(FundMetricsViewBean metricsViewBean, String attribute10yr) {
        String ror10year = null;
        try {
            ror10year = (String) PropertyUtils.getSimpleProperty(metricsViewBean, attribute10yr);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new NestableRuntimeException(e);
        }
        if (StandardReportsConstants.NA.equals(ror10year))
            return false;
        else
            return true;
    }
	
	public boolean isShowMMFDisclosureText() {
		return true;
	}
}
