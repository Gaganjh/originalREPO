package com.manulife.pension.ireports.report;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

public interface ReportDefinition extends ReportFormattingConstants, StandardReportsConstants {
	
	public String getReportName();
	
	public String[] getAsOfDateContexts();
	
	public String[] getStaticFootnoteSymbols();
	public boolean containsClassDisclosureFootnote();
	
	public boolean isLandscape();
	
	public boolean isLegalSize();
	
	public boolean isHeaderParagraphPresent();
	public String[] getColumnWidths();
	public int getNumberOfColumns();
	
	public boolean containsGicSection();
	public int getGicSectionAnnualRateColumn();
	
	public String[] getIntroDisclosuresContentIds();

    public String[] getIntroGenericLevelDisclosuresContentIds();
    public String[] getIntroContractLevelDisclosuresContentIds();
	
	public String[] getIncludedMerrillDisclosuresContentIds();
	
	public String[] getGeneralDisclosureContentIds();
	
	/**
	 * For Risk/Return report, Market Index information is automatically included in
	 * Investment Groups retrieved by report data. Not all reports contain this
	 * information, so return the appropriate value.
	 * 
	 *  @return boolean - true to generate Market Index information. false otherwise
	 */
	public boolean containsMarketIndexSectionRiskReturn();
	public boolean containsMarketIndexSectionAssetClass();
	
	public boolean containsStandardizedReportColumns();
	public void hideStandardizedReportColumns();
	public boolean isStandardizedReportColumnsSupressed();
	
	public void justifyReportBodyColumns(PdfTableBody tableBody);
	
	public void buildFundRow(StandardReportBuilder report, PdfTableBody fundTableBody, int row, ReportFund reportFund, FundReportData reportData);
	
	public PdfTable buildLastRow(StandardReportBuilder report, int row, FundReportData reportData);
	
	public PdfTableHeader buildMainTableHeader(StandardReportBuilder report, String classDisclosureFootnoteNumber, FundReportData reportData, ReportOptions options);

	public void setCellFormattingForRow(PdfTableRegion tableRegion, int row, boolean setTopBorder);
	
	public boolean isShowMMFDisclosureText();
	
	public PdfTableBody buildDisclosureTextRow(StandardReportBuilder report,  PdfTableBody tableBody, int row, int numberOfColumns, ReportFund reportFund);
	
}
