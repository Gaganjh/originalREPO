package com.manulife.pension.ireports.report.streamingreport.impl;

import java.math.BigDecimal;
import java.util.Map;

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
import com.manulife.pension.ireports.report.viewbean.FundExpensesViewBean;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfRGB;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

public class AICReportDefinition implements ReportDefinition {
	private static final String REPORT_NAME = "Expense Ratios";

	//Landscape: 10.0in = 25.4cm
	private static final String[] COLUMN_WIDTHS = new String[] {
	        "6.9cm",
            "3.9cm",
            "2cm",
            "1.8cm",
            "0.3cm",
            "1.8cm",
            "0.5cm",
            "1.8cm",
			"1.8cm",
			"3.1cm", 
			"1.5cm"
	};
	
		
	public String getReportName() { return REPORT_NAME; }

	public String[] getAsOfDateContexts() {
		return new String[] {	StandardReportsConstants.REPORT_CONTEXT_AICS, 
				StandardReportsConstants.REPORT_CONTEXT_MORNINGSTAR_CAT_PERFORMANCE,
				StandardReportsConstants.REPORT_CONTEXT_FUND_EXPENSE_RATIOS};
	}

	public String[] getStaticFootnoteSymbols() {
		return new String[]{
				FOOTNOTE_INVESTMENT_OPTIONS,
				FOOTNOTE_FUND_MANAGER,
				//FOOTNOTE_EXPENSES,
				//FOOTNOTE_MAINTAINENANCE_CHARGE,
				FOOTNOTE_AIC,
				FOOTNOTE_MORNINGSTAR_CATEGORY,
				FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK,
				FOOTNOTE_UFNC,
			    FOOTNOTE_RFUF ,
			    FOOTNOTE_RFSA ,
			    FOOTNOTE_TRUTPC
		};
	}
	
	public String[] getIntroDisclosuresContentIds() {
        return new String[] { "64589", "64590", "64591" ,"90936"};
    }
    
    public String[] getIntroGenericLevelDisclosuresContentIds() {
        return new String[] { "97640" };
    }
    
    public String[] getIntroContractLevelDisclosuresContentIds() {
        return new String[] { "97641" };
    }
	
	public String[] getIncludedMerrillDisclosuresContentIds() {
        return new String[] { "64589", "64590", "64591" ,"90936", "96181" };
    }

    /**
     * This method returns the content Ids for General Disclosure.
     */
    public String[] getGeneralDisclosureContentIds() {
        return new String[] { "64507", "64508", "64509", "64510", "64588" };
    }
	
	public boolean containsClassDisclosureFootnote() { return false; }
	
	public boolean isLandscape() { return true; }

	public boolean isHeaderParagraphPresent() { return false;}
	
	public String[] getColumnWidths() {
		return COLUMN_WIDTHS;
	}

	public int getNumberOfColumns() { return getColumnWidths().length; }
	
	public boolean containsGicSection() { return false; }
	public int getGicSectionAnnualRateColumn() { return 1; }
	
	public boolean containsMarketIndexSectionRiskReturn() { return false; }
	public boolean containsMarketIndexSectionAssetClass() { return true; }
	
	public void justifyReportBodyColumns(PdfTableBody tableBody) {
		for (int j = 2; j < (getNumberOfColumns()-1); j++) {
			tableBody.setColumnTextAlign(j, "center");
		}
		tableBody.setColumnTextAlign(getNumberOfColumns()-1, "right");
	}
	
	public void buildFundRow(StandardReportBuilder report, PdfTableBody fundTableBody, int row, ReportFund reportFund, FundReportData reportData) {
		Fund fund = reportFund.getFund();
		FundExpensesViewBean fundExpenses = new FundExpensesViewBean(reportFund.getFundExpenses());
		PdfParagraph fundNameParagraph = report.createParagraph()
			.setFontSize(8).setFontFamily(FRUTIGER47LIGHT_CN);
		
		Map morningstarCategoryPerformances = reportData.getMorningstarCategoryPerformances();
		String morningstarName = "n/a";
		BigDecimal morningstarExpenseRatio = null;
		String fundId = fund.getInvestmentid();	
		if ( fundId != null && morningstarCategoryPerformances.get(fundId) != null) {
			MorningstarCategoryPerformance morningstarCategory = (MorningstarCategoryPerformance) morningstarCategoryPerformances.get(fundId);
			morningstarName = morningstarCategory.getMorningstarCategoryName();
			morningstarExpenseRatio = morningstarCategory.getExpenseRatio();
		} 
		
		/* draw table rows */				
		int column = 0;
		
		PdfTableRegion fundNameTableBody = report.createTableBody(1, 2);
		PdfTableRegion[] fundNameTableRegions = new PdfTableRegion[]{fundNameTableBody};
		PdfTable fundNameTable = report.createTable(new String[] {"0.2cm", "6.7cm"}, fundNameTableRegions);

		if (reportData.getFeeWaiverFundIds().contains(fundId) && (reportData.isMerrillAdvisor()
				&& reportData.getRestrictedFunds().containsKey(fundId))) {
			fundNameTableBody.add(0, 0, report.createParagraph(DOT_INDICATOR + "\n" + MERRILL_RESRICTED_FUND_SYMBOL));
		} else if (reportData.getFeeWaiverFundIds().contains(fundId)) {
			fundNameTableBody.add(0, 0, report.createParagraph(DOT_INDICATOR));
		} else if (reportData.isMerrillAdvisor() && reportData.getRestrictedFunds().containsKey(fundId) 
				&& !(fundId.equals("XX05") || fundId.equals("XX03") || fundId.equals("XX11") || fundId.equals("XX14"))) {
			fundNameTableBody.add(0, 0, report.createParagraph(MERRILL_RESRICTED_FUND_SYMBOL));
		} else {
			fundNameTableBody.add(0, 0, report.createParagraph(" "));
		}
			
		
		
		fundNameTableBody.add(0, 1, fundNameParagraph.add(report.createText(StandardReportsUtils
                .getValueorNA(fund.getFundLongName()))));
		StandardReportTemplate.attachFootnotes(fundNameParagraph, reportFund, report);
		fundTableBody.add(row, column, fundNameTable);
		
		
		column++;
	
		fundTableBody.add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
				.add(report.createText(StandardReportsUtils.getValueorNA(fund.getInvmanagername()))));
		column++;
		
		fundTableBody.setCellBackgroundColor(row, column, new
		 PdfRGB(247,241,220)).add(row, column,
		 report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
		 .setFontFamily(FRUTIGER65BOLD).add(
		 report.createText(StandardReportsUtils.getValueorNA(fundExpenses
		 .getUnderlyingFundNetCost()))));
		 column++;
        
		fundTableBody.setCellBackgroundColor(row, column, new
		 PdfRGB(247,241,220)).add(row, column,
		 report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
		 .setFontFamily(FRUTIGER65BOLD).add(
		 report.createText(StandardReportsUtils.getValueorNA(fundExpenses
		 .getRevenueFromUnderlyingFund()))));
		 column++;

	     fundTableBody.setCellBackgroundColor(row, column, new
             PdfRGB(247,241,220)).add(row, column,
             report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
             .setFontFamily(FRUTIGER65BOLD).add(
             report.createText("+")));
        column++;

		fundTableBody.setCellBackgroundColor(row, column, new
		 PdfRGB(247,241,220)).add(row, column,
		 report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
		 .setFontFamily(FRUTIGER65BOLD).add(
		 report.createText(StandardReportsUtils.getValueorNA(fundExpenses
		 .getRevenueFromSubAccount()))));
		 column++;
		 
		 fundTableBody.setCellBackgroundColor(row, column, new
		         PdfRGB(247,241,220)).add(row, column,
		         report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
		         .setFontFamily(FRUTIGER65BOLD).add(
		         report.createText("=")));
		 column++;
		 
	    fundTableBody.setCellBackgroundColor(row, column, new
	             PdfRGB(247,241,220)).add(row, column,
	             report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
	             .setFontFamily(FRUTIGER65BOLD).add(
	             report.createText(StandardReportsUtils.getValueorNA(fundExpenses
	             .getTotalRevenueUsedTowardsPlanCosts()))));
	    column++;
		
		fundTableBody.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER65BOLD)
				.add(
                        report.createText(StandardReportsUtils.getValueorNA(fundExpenses
                                .getAnnualInvestmentCharge()))));
		column++;
		fundTableBody.add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
				.add(
                        report.createText(StandardReportsUtils.getValueorNA(morningstarName))));
		column++;
		
		if (morningstarExpenseRatio!=null){
			fundTableBody.add(row, column, report.createParagraph()
					.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
					.add(report.createText(StandardReportsUtils.formatPercentage(morningstarExpenseRatio))));
		} else {
			fundTableBody.add(row, column, report.createParagraph()
					.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
					.add(report.createText(StandardReportsConstants.NA)));
		}
	}
	
	/**
     * This method returns the table for the last row.
     */
    public PdfTable buildLastRow(StandardReportBuilder report, int row, FundReportData reportData) {
		return null;
	}
	
	public PdfTableHeader buildMainTableHeader(StandardReportBuilder report, String classDisclosureFootnoteNumber, FundReportData reportData, ReportOptions options) {
		PdfTableHeader tableHeader = report.createTableHeader(4,
				getNumberOfColumns(), new BottomAlignCellDecoratorFactory());
		
		for (int j = 2; j < getNumberOfColumns(); j++) {
			tableHeader.setColumnTextAlign(j, "center");
		}
		int column = 0;
		int row=0;
		//Row Zero
		tableHeader.add(row, column, report.createParagraph());
		tableHeader.spanCells(row, column, 1, 2);
		column++;
		column++;
		tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
				.add(
                        report.createText("As of "
                                + DateFormatUtils.format(reportData.getAsOfDateForContext("FER"),
                                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE)))).setPaddingAfter("0.1cm");
        tableHeader.spanCells(row, column, 1, 7);
		column++;
        column++;
        column++;
        column++;
        column++;
        column++;
        column++;
		tableHeader.add(row, column, report.createParagraph());
		tableHeader.spanCells(row, column, 1, 2);

		//Row 1
		row=1;
		column = 0;
	    tableHeader.add(row, column, report.createParagraph());
	    tableHeader.spanCells(row, column, 1, 2);
	    column++;
	    column++;
	    tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
	            row,
	            column,
	            report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
	            FRUTIGER67BOLD_CN).add(report.createText("Investment"))).add(
	            row,
	            column,
	            report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
	            FRUTIGER67BOLD_CN).add(report.createText("Services"))
	            );
	    tableHeader.spanCells(row, column, 1, 1);
	    column++;
	    tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
                FRUTIGER67BOLD_CN).add(report.createText("Plan Services")));
        tableHeader.spanCells(row, column, 1, 5);
	    column++;
	    column++;
	    column++;
        column++;
        column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph());
	    tableHeader.spanCells(row, column, 1, 1);
	    column++;
	    tableHeader.add(row, column, report.createParagraph());
	    tableHeader.spanCells(row, column, 1, 2);
	    
	  //Row 2
        column = 0;
        row=2;
        tableHeader.add(row, column, report.createParagraph());
        tableHeader.spanCells(row, column, 1, 2);
        column++;
        column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
                FRUTIGER67BOLD_CN).add(report.createText("(1)")));
        tableHeader.spanCells(row, column, 1, 1);
        column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
                FRUTIGER67BOLD_CN).add(report.createText("(2)")));
        tableHeader.spanCells(row, column, 1, 1);
        column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph());
        tableHeader.spanCells(row, column, 1, 1);
        column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
                FRUTIGER67BOLD_CN).add(report.createText("(3)")));
        tableHeader.spanCells(row, column, 1, 1);
        column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph());
        tableHeader.spanCells(row, column, 1, 1);
        column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
                FRUTIGER67BOLD_CN).add(report.createText("(4)")));
        tableHeader.spanCells(row, column, 1, 1);
        column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
                row,
                column,
                report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
                FRUTIGER67BOLD_CN).add(report.createText("(5)")));
        tableHeader.spanCells(row, column, 1, 1);
        column++;

        tableHeader.add(row, column, report.createParagraph());
        tableHeader.spanCells(row, column, 1, 2);
        
	    //Row 3
		column = 0;
		row=3;
		tableHeader.add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Investment Options"))
				.add(Footnotes.createFootnote(report, FOOTNOTE_INVESTMENT_OPTIONS)));
		
		column++;
		tableHeader.add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
 				.add(report.createText("Sub-Adviser/Manager"))
				.add(Footnotes.createFootnote(report, FOOTNOTE_FUND_MANAGER)));

	    column++;
        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220)).add(
         row,
         column,
         report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
         FRUTIGER67BOLD_CN).add(report.createText("Underlying"))).add(
         row,
         column,
         report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
         FRUTIGER67BOLD_CN).add(report.createText("Fund Net Cost"))
         .add(Footnotes.createFootnote(report, FOOTNOTE_UFNC))
         );
        
         column++;
         tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("Revenue")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("From")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("Underlying")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN).add(report.createText("Fund")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER57CN).add(report.createText("(12b-1, STA,")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER57CN).add(report.createText("Other)"))
         .add(Footnotes.createFootnote(report, FOOTNOTE_RFUF).setFontFamily(FRUTIGER67BOLD_CN))
         );
        
         column++;
        
         tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220))
         .add(row, column, report.createParagraph()
                 .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
                 .add(report.createText("+")));
         
         column++;

        tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220))
        .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Revenue")))
        .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("From")))
        .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Sub-")))
        .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Account"))
        .add( Footnotes.createFootnote(report, FOOTNOTE_RFSA))
        );
         column++;

         tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220))
         .add(row, column, report.createParagraph()
                 .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
                 .add(report.createText("=")));
         
         column++;
         
         tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247,241,220))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Total")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Revenue")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Used")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Towards")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Plan")))
         .add( row, column, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily( FRUTIGER67BOLD_CN).add(report.createText("Cost"))
         .add( Footnotes.createFootnote(report, FOOTNOTE_TRUTPC))
         );
         column++;

		PdfParagraph ratioHeader = report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Ratio"))
				.add(Footnotes.createFootnote(report, FOOTNOTE_AIC));

		tableHeader.setCellBackgroundColor(row, column, new PdfRGB(247, 241, 220)).add(
		        row,
                column,
                report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(
                        FRUTIGER67BOLD_CN).add(report.createText("Expense"))).add(row, column,
                ratioHeader);

		column++;
		tableHeader.add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Morningstar Benchmark Category"))
				/* Changed the FOOTNOTE_MORNINGSTAR_CATEGORY constant name*/
				.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_CATEGORY)))
				
				.add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("and Average Expense Ratio"))
				.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK)))
				.add(row, column, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("as of "))
				.add(DateFormatUtils.format(reportData.getAsOfDateForContext("MSP"),
                        ReportFormattingConstants.DATE_PATTERN_AS_OF_DATE)));

		tableHeader.spanCells(row	, column, 1, 2);
		
		tableHeader.setBorderBottomStyleForRegion("solid").setBorderBottomWidthForRegion("0.01cm");
		
		return tableHeader;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ireports.report.ReportDefinition#setCellFormattingForRow(com.manulife.util.pdf.PdfTableRegion, int, boolean)
	 */
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
        for (int i = 2; i <= 8; i++) {
			tableRegion.setCellBackgroundColor(row, i, new PdfRGB(247,241,220));
		}

	}

	public boolean isLegalSize() {
		return false;
	}


    public boolean containsStandardizedReportColumns() {
        return false;
    }

    public void hideStandardizedReportColumns() {
        
    }


	public PdfTableBody buildDisclosureTextRow(StandardReportBuilder report,
			PdfTableBody tableBody, int row, int numberOfColumns,
			ReportFund reportFund) {
		return null;
	}

	public boolean isShowMMFDisclosureText() {
		return false;
	}

    public boolean isStandardizedReportColumnsSupressed() {
        return false;
    }

}
