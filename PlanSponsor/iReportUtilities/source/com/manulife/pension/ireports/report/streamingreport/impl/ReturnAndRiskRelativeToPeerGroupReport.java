package com.manulife.pension.ireports.report.streamingreport.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.dao.ReportDataRepository;
import com.manulife.pension.ireports.dao.ReportDataRepositoryFactory;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.CustomPageSequence;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.StandardReportTemplate;
import com.manulife.pension.ireports.report.util.BottomAlignCellDecoratorFactory;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundRor;
import com.manulife.pension.service.fund.standardreports.valueobject.FundStandardDeviation;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

/**
 * @author Greg McKhool, Tom McGrady
 */
public class ReturnAndRiskRelativeToPeerGroupReport extends StandardReportTemplate {
	public static final String REPORT_NAME = "Return and Risk Relative to Peer Group";
	
	public ReturnAndRiskRelativeToPeerGroupReport(ReportOptions options) {
        super(options);
	}

	/**
	 * Build the PDF content
	 *
	 * @throws IOException
	 */
	public void buildReport() {
		createPageLayoutStructure(report, false, options.getContractShortlistOptions().isContractOrShortlistUsed());
		
		ReportDataRepository reportDataRepository = ReportDataRepositoryFactory.getRepository();
		
        Map<String, PdfParagraph> firstPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        Map<String, PdfParagraph> subsequentPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        
        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2, report.createParagraph(report.createText("Return")).add(
                        Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).add(
                        " and Risk Relative to Peer Group").add(
                        Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_CATEGORY)).add(
                        Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_PEERGROUP))
                        .setFontFamily(FRUTIGER57CN).setFontSize(14).setTextAlignment("right"));
        
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2, report
                .createParagraph(report.createText("Return")).add(
                        Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).add(
                        " and Risk Relative to Peer Group").add(
                        Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_PEERGROUP))
                        .setFontFamily(FRUTIGER57CN).setFontSize(14).setTextAlignment("right"));
        
		CustomPageSequence pageSequenceMainReport = createCustomPageSequence(report, null,
                "content", firstPageTitleParagraphMap, subsequentPageTitleParagraphMap);
		
		PdfFlow flow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
		PdfParagraph tableParagraph = report.createParagraph().setMargin("0.0cm").setTextAlignment("end").setSpan("all");
		flow.add(tableParagraph);

		//portrait: 7.5in = 19cm
        // String[] mainTableColumnWidths = new String[] {"9.5", "9.5"};
		String[] fundTableColumnWidths = new String[] {"6.2cm", "1.4cm","1.4cm"};
		
//		PdfTableHeader mainTableHeader = report.createTableHeader(1, 2);
		PdfTableBody mainTableBody = report.createTableBody(3, 2);

		String[] staticFootnotes = new String[]{
				FOOTNOTE_PERFORMANCE_HISTORY,	
				FOOTNOTE_INVESTMENT_OPTIONS,	
				FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK,	
				FOOTNOTE_MORNINGSTAR_CATEGORY,
				FOOTNOTE_MORNINGSTAR_PEERGROUP,
		};
		staticFootnotes = options.getContractShortlistOptions().addToFootnotes(staticFootnotes);
		staticFootnotes = addClassDisclosureFootnote(staticFootnotes, options);
		
		FundReportData reportData = reportDataRepository.getFundReportData(
				options,
				ReportDataRepository.REPORTFUND_SORT_ORDER_RISKRETURN,
				staticFootnotes
				);

		//assign funds to quartile
		List funds = reportData.getFunds();
		Map morningstarCategoryPerformances = reportData.getMorningstarCategoryPerformances();
		
		List fundsOutperformLessRisk = new ArrayList();
		List fundsOutperformHigherRisk = new ArrayList();
		List fundsUnderperformLessRisk = new ArrayList();
		List fundsUnderperformHigherRisk = new ArrayList();
		List fundsMissing5yearData = new ArrayList();
		
		for (Iterator fundsIterator = funds.iterator(); fundsIterator.hasNext();) {
			BigDecimal fundStdDev5year = null;
			BigDecimal fundRor5year = null;
			BigDecimal morningstarCategoryRor5Year = null;
			BigDecimal morningstarCategoryStdDev5Year = null;
			ReportFund reportFund = (ReportFund) fundsIterator.next();
			FundStandardDeviation fundStandardDeviation = reportFund.getFundStandardDeviation();
			if (fundStandardDeviation != null) {
				fundStdDev5year = fundStandardDeviation.getRor5yr();
			}
			FundRor fundMetrics = reportFund.getFundMetrics();
			if (fundMetrics != null) {
				fundRor5year = fundMetrics.getRor5yr();
			}
			
			if (isFundToBeDisplayed(fundRor5year, fundStdDev5year)) {
				
				String fundId = reportFund.getFund().getInvestmentid();
				if (fundId != null) {
					MorningstarCategoryPerformance morningstarCategoryPerf = (MorningstarCategoryPerformance) morningstarCategoryPerformances.get(fundId);
					if (morningstarCategoryPerf != null) {
						morningstarCategoryRor5Year = morningstarCategoryPerf.getRor_5Year();
						morningstarCategoryStdDev5Year = morningstarCategoryPerf.getStandardDeviation_5Year();
					}
				}
				
				if ( fundRor5year == null || fundStdDev5year == null || morningstarCategoryRor5Year == null || morningstarCategoryStdDev5Year == null) {
					FundVersusPeerGroupData fundVersusPeerGroupData = new FundVersusPeerGroupData(reportFund, null, null);
					fundsMissing5yearData.add(fundVersusPeerGroupData);
				} else {
					BigDecimal fundRorVsPeerGroup = fundRor5year.subtract(morningstarCategoryRor5Year);
					BigDecimal fundStdDevVsPeerGroup = fundStdDev5year.subtract(morningstarCategoryStdDev5Year);
					
					FundVersusPeerGroupData fundVersusPeerGroupData = new FundVersusPeerGroupData(reportFund, fundRorVsPeerGroup, fundStdDevVsPeerGroup);
					if (fundRorVsPeerGroup.compareTo(new BigDecimal(0)) >= 0) {
						if (fundStdDevVsPeerGroup.compareTo(new BigDecimal(0)) >= 0) {
							fundsOutperformHigherRisk.add(fundVersusPeerGroupData);
						} else {
							fundsOutperformLessRisk.add(fundVersusPeerGroupData);
						}
					} else {
						if (fundStdDevVsPeerGroup.compareTo(new BigDecimal(0)) >= 0) {
							fundsUnderperformHigherRisk.add(fundVersusPeerGroupData);
						} else {
							fundsUnderperformLessRisk.add(fundVersusPeerGroupData);
						}
					}
				}
			} 
		}
		
		Collections.sort(fundsOutperformLessRisk, new FundVersusPeerGroupDataComparator());
		Collections.sort(fundsOutperformHigherRisk, new FundVersusPeerGroupDataComparator());
		Collections.sort(fundsUnderperformLessRisk, new FundVersusPeerGroupDataComparator());
		Collections.sort(fundsUnderperformHigherRisk, new FundVersusPeerGroupDataComparator());
		
		String[] fundsOutperformLessRiskHeading = new String[]{"Funds outperforming Peer Group", "with less risk"};
		String[] fundsOutperformHigherRiskHeading = new String[]{"Funds outperforming Peer Group", " with higher risk"};
		String[] fundsUnderperformLessRiskHeading = new String[]{"Funds underperforming Peer Group", " with less risk"};
		String[] fundsUnderperformHigherRiskHeading = new String[]{"Funds underperforming Peer Group", " with higher risk"};
		
		PdfTable fundTable1 = buildFundTable(report, fundTableColumnWidths, fundsOutperformLessRisk, fundsOutperformLessRiskHeading);
		PdfTable fundTable2 = buildFundTable(report, fundTableColumnWidths, fundsOutperformHigherRisk, fundsOutperformHigherRiskHeading);
		
		PdfTable fundTable3 = buildFundTable(report, fundTableColumnWidths, fundsUnderperformLessRisk, fundsUnderperformLessRiskHeading);
		PdfTable fundTable4 = buildFundTable(report, fundTableColumnWidths, fundsUnderperformHigherRisk, fundsUnderperformHigherRiskHeading);
		
		//add 4 tables
		mainTableBody.add(0, 0, fundTable1);
		mainTableBody.add(0, 1, fundTable2);
		mainTableBody.add(1, 0, fundTable3);
		mainTableBody.add(1, 1, fundTable4);
		mainTableBody.add(2, 0, report.createParagraph());
        mainTableBody.add(2, 1, report.createParagraph());
	
		//add padding and set borders
		String paddingValue = "0.5cm";
		
		mainTableBody.setBorderColor(PDF_RGB_ROW_BORDER);

		mainTableBody.setCellPaddingRight(0, 0, paddingValue);
		mainTableBody.setCellBorderRightStyle(0, 0, "solid");
		mainTableBody.setCellPaddingBottom(0, 0, paddingValue);
		mainTableBody.setCellBorderBottomStyle(0, 0, "solid");
		
		mainTableBody.setCellPaddingLeft(0, 1, paddingValue);
		mainTableBody.setCellPaddingBottom(0, 1,paddingValue);
		mainTableBody.setCellBorderBottomStyle(0, 1,"solid");
		
		mainTableBody.setCellPaddingRight(1, 0, paddingValue);
		mainTableBody.setCellBorderRightStyle(1, 0, "solid");
		mainTableBody.setCellPaddingTop(1, 0, paddingValue);

		mainTableBody.setCellPaddingLeft(1, 1, paddingValue);
		mainTableBody.setCellPaddingTop(1, 1, paddingValue);
		
		PdfTable mainTable = report.createTable(null, new PdfTableRegion[] { mainTableBody });
		tableParagraph.add(mainTable);
		
		pageSequenceMainReport.add(flow);

        PdfFlow footerFlow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);

        footerFlow.add(createFootnotesHeader(report, reportData.getFootnotes()));
        footerFlow.add(createFootnotes(report, reportData.getFootnotes()));
        // TODO: Hari - send the general Disclosure ID's.
        footerFlow.add(createStandardFootnoteBlock(report, this.getClass().getName(), null));
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));

        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2, report.createParagraph(
                report.createText("Return")).add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).add(
                " and Risk Relative to Peer Group").add(
                Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_CATEGORY)).add(
                Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_PEERGROUP)).setFontFamily(
                FRUTIGER57CN).setFontSize(14).setTextAlignment("right"));

        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2, report
                .createParagraph(report.createText("Return")).add(
                        Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).add(
                        " and Risk Relative to Peer Group").add(
                        Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_PEERGROUP))
                        .setFontFamily(FRUTIGER57CN).setFontSize(14).setTextAlignment("right"));

        
        CustomPageSequence pageSequenceFooter = createCustomPageSequence(report, null,
                "subsequentPageDoubleColumn", firstPageTitleParagraphMap,
                subsequentPageTitleParagraphMap);
   
	    pageSequenceFooter.add(footerFlow);
		
		addISFReport();
	}

	private PdfTable buildFundTable(StandardReportBuilder report, String[] columnWidths, List fundsInGroup, String[] fundTableHeading) {
		int numberOfFundsInGroup = (fundsInGroup == null) ? 0
                : fundsInGroup.size();
		
		PdfTableBody fundTableBody = null;
		PdfTableHeader fundTableHeader = buildFundTableHeader(report, columnWidths.length, fundTableHeading);

		if (numberOfFundsInGroup > 0) {
		    
		    fundTableBody = report.createTableBody(numberOfFundsInGroup, columnWidths.length);
		    
			fundTableBody.setColumnTextAlign(1, "right");
			fundTableBody.setColumnTextAlign(2, "right");
			fundTableBody.setBorderBottomStyle("solid");
			fundTableBody.setBorderBottomColor(PDF_RGB_ROW_BORDER);
			fundTableBody.setBorderBottomWidth("0.5pt");
			fundTableBody.setPaddingBefore("0.1cm");
	
			//	this logic is for keeping rows together on one page
			int row = 0;
			int rowNumberKeepTogetherFirst = (new Integer(PropertyManager.getString("row.number.keep.together"))).intValue();
			if (rowNumberKeepTogetherFirst > numberOfFundsInGroup){
				rowNumberKeepTogetherFirst = numberOfFundsInGroup;
			}
			int rowNumberKeepTogetherLast = 0;
			if (numberOfFundsInGroup >= rowNumberKeepTogetherFirst) {
				rowNumberKeepTogetherLast = numberOfFundsInGroup - rowNumberKeepTogetherFirst;
			}
			for (Iterator fundsInGroupIterator = fundsInGroup.iterator(); fundsInGroupIterator.hasNext();) {
				FundVersusPeerGroupData fundVersusPeerGroupData = (FundVersusPeerGroupData) fundsInGroupIterator.next();
				buildFundRow(report, fundTableBody, row, fundVersusPeerGroupData);
				
//				formatting / keeping rows together
				fundTableBody.setRowKeepTogether(row);
				if (row < rowNumberKeepTogetherFirst-1 || row >= rowNumberKeepTogetherLast) {
					fundTableBody.setRowKeepWithNext(row);
				}
				row++;
			}
		} else {
		    
		    fundTableBody = report.createTableBody(1, columnWidths.length);
		    
		    for (int i = 0; i < columnWidths.length; i++) {
            fundTableBody.add(0, i, report.createParagraph()); 
		    }
		}
		
		PdfTableRegion[] fundTableRegions = new PdfTableRegion[]{fundTableHeader, fundTableBody};
		PdfTable fundTable = report.createTable(columnWidths, fundTableRegions);
		
		return fundTable;
	}

	private void buildFundRow(StandardReportBuilder report, PdfTableBody fundTableBody, int row, FundVersusPeerGroupData fundVersusPeerGroupData) {
		ReportFund reportFund = fundVersusPeerGroupData.getReportFund();
		
		Fund fund = reportFund.getFund();
		BigDecimal fundRor5Year = fundVersusPeerGroupData.getFundRor5YearVsPeerGroup();
		BigDecimal fundStdDev5Year = fundVersusPeerGroupData.getFundStdDev5YearVsPeerGroup();
		
		PdfParagraph fundNameParagraph = report.createParagraph()
			.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);
		fundTableBody.add(row, 0, fundNameParagraph
			.add(report.createText(fund.getFundLongName().trim())));
		attachFootnotes(fundNameParagraph, reportFund, report);
		fundTableBody.add(row, 1, report.createParagraph()
			.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
			.add(report.createText(StandardReportsUtils.formatPercentage(fundRor5Year))));
		fundTableBody.add(row, 2, report.createParagraph()
			.setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
			.add(report.createText(StandardReportsUtils.formatPercentage(fundStdDev5Year))));
	
	}

	private boolean isFundToBeDisplayed(BigDecimal fundRor5Year, BigDecimal fundStdDev5Year) {
		return (fundRor5Year!=null && fundStdDev5Year!=null);
	}

	private PdfTableHeader buildFundTableHeader(StandardReportBuilder report, int numberOfColumns, String fundTableHeading[]) {
		PdfTableHeader tableHeader = report.createTableHeader(2, numberOfColumns, new BottomAlignCellDecoratorFactory());
		tableHeader.setColumnTextAlign(1, "right");
		tableHeader.setColumnTextAlign(2, "right");

		tableHeader
			.add(0,0, report.createParagraph()
				.setFontSize(12).setFontWeight("bold")
				.add(report.createText(fundTableHeading[0])))
			.add(0,0, report.createParagraph()
				.setFontSize(12).setFontWeight("bold")
				.add(report.createText(fundTableHeading[1]))
				.add(Footnotes.createFootnote(report, FOOTNOTE_INVESTMENT_OPTIONS)));

		tableHeader.spanCells(0,0,1,3);
		tableHeader.add(1, 0, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("  ")));
		tableHeader.add(1, 1, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Fund return")))
				.add(1, 1, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("minus Peer")))
				.add(1, 1, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Group Return"))
				.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_CATEGORY))
				.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK))
		);
		tableHeader.add(1, 2, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Fund risk")))
				.add(1, 2, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("minus Peer")))
				.add(1, 2, report.createParagraph()
				.setFontSize(FONT_SIZE_COLUMN_HEADING).setFontFamily(FRUTIGER67BOLD_CN)
				.add(report.createText("Group   Risk"))
				.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_CATEGORY))
				.add(Footnotes.createFootnote(report, FOOTNOTE_MORNINGSTAR_DATA_EXPENSE_RISK))
		);			
		tableHeader.setBorderBottom("solid");
		return tableHeader;
	}

	private class FundVersusPeerGroupData {
		private ReportFund reportFund = null;
		private BigDecimal fundRor5YearVsPeerGroup = null;
		private BigDecimal fundStdDev5YearVsPeerGroup = null;
		
		FundVersusPeerGroupData(ReportFund reportFund, BigDecimal fundRor5YearVsPeerGroup, BigDecimal fundStdDev5YearVsPeerGroup) {
			this.reportFund = reportFund;
			this.fundRor5YearVsPeerGroup = fundRor5YearVsPeerGroup;
			this.fundStdDev5YearVsPeerGroup = fundStdDev5YearVsPeerGroup;
		}

		public ReportFund getReportFund() {
			return reportFund;
		}
		public BigDecimal getFundRor5YearVsPeerGroup() {
			return fundRor5YearVsPeerGroup;
		}
		public BigDecimal getFundStdDev5YearVsPeerGroup() {
			return fundStdDev5YearVsPeerGroup;
		}
	}
	
	private class FundVersusPeerGroupDataComparator implements Comparator {
		public int compare (Object p1, Object p2){
			FundVersusPeerGroupData data1 = (FundVersusPeerGroupData)p1;
			FundVersusPeerGroupData data2 = (FundVersusPeerGroupData)p2;
			BigDecimal ror1 = data1.getFundRor5YearVsPeerGroup();
			BigDecimal ror2 = data2.getFundRor5YearVsPeerGroup();
			return ror2.compareTo(ror1);
		}
	}
	
	public static void main(String[] args) throws Exception{
		ReturnAndRiskRelativeToPeerGroupReport report = new ReturnAndRiskRelativeToPeerGroupReport(
                null);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		report.buildReport();
		report.writeTo(bos);
		File reportFile = new File("C:\\intelliware\\simpleReport.pdf");
		FileOutputStream fos = new FileOutputStream(reportFile);
		fos.write(bos.toByteArray());
		fos.close();
	}
}