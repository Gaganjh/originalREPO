package com.manulife.pension.ireports.report.streamingreport.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.AbstractRiskReturnReport;
import com.manulife.pension.ireports.report.CustomPageSequence;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.viewbean.AssetHouseSubGroupViewBean;
import com.manulife.pension.ireports.report.viewbean.MarketIndexIbPerformanceViewBean;
import com.manulife.pension.ireports.report.viewbean.MorningstarCategoryPerformanceViewBean;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.pension.platform.utility.CommonConstants;
import com.manulife.pension.platform.utility.ireports.FundReportConstants;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfTableBody;

public class InvestmentReturnsStandardDeviationsByRiskReturnReport extends AbstractRiskReturnReport {
    public InvestmentReturnsStandardDeviationsByRiskReturnReport(ReportOptions options) {
        super(options, new InvestmentReturnsStandardDeviationsReportDefinition());
    }

    /**
     * The initializeReportData() method was overriden to remove few static footnotes, if they are
     * not supposed to be present.
     */
    protected void initializeReportData() {
        super.initializeReportData();

        if (!containsGuaranteedAccountRates(reportData)) {
            reportData.getFootnotes().remove(FOOTNOTE_3Y_5Y_10Y_GIC);
        }
        
        List<String> footNoteSuperScriptList = getNotRequiredFootNoteSuperScripts(reportData);
        if(footNoteSuperScriptList != null && footNoteSuperScriptList.size() > 0 ){
     	   for(String footNoteSuperScript : footNoteSuperScriptList){
     		   if(reportData.getFootnotes().containsKey(footNoteSuperScript)){
     			   reportData.getFootnotes().remove(footNoteSuperScript);
         	   }
            }
        }
    }
    
    
    protected void definePageSequence(StandardReportBuilder report, PdfFlow flow,
            FundReportData reportData) {

        Map<String, PdfParagraph> firstPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        Map<String, PdfParagraph> subsequentPageTitleParagraphMap = new HashMap<String, PdfParagraph>();

        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());

        CustomPageSequence pageSequenceMainReport = createCustomPageSequence(report, null,
                "content", firstPageTitleParagraphMap, subsequentPageTitleParagraphMap);
        pageSequenceMainReport.add(flow);

        PdfFlow footerFlow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
        footerFlow.add(createFootnotesHeader(report, reportData.getFootnotes()));
        footerFlow.add(createFootnotes(report, reportData.getFootnotes()));
        footerFlow.add(createFootnotes(report, reportData.getCompanyIdFootnotes()));
        footerFlow.add(createStandardRiskDisclosure(report));
        footerFlow.add(createStandardFootnoteBlock(report, this.getClass().getName(),
                reportDefinition.getGeneralDisclosureContentIds()));
        footerFlow.add((PdfParagraph) report.createParagraph().setId("end"));

        firstPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());
        subsequentPageTitleParagraphMap.put(StandardReportsConstants.TITLE_2,
                createTitle2HeaderParagraph());

        CustomPageSequence pageSequenceFooter = createCustomPageSequence(report, null,
                "subsequentPageDoubleColumn", firstPageTitleParagraphMap,
                subsequentPageTitleParagraphMap);

        pageSequenceFooter.add(footerFlow);
    }

    /**
     * This method creates the Title 2 Header Paragraph.
     * 
     * @return
     */
    private PdfParagraph createTitle2HeaderParagraph() {
        PdfParagraph title2HeaderParagraph = report.createParagraph(
                report.createText("Investment returns")).add(
                Footnotes.createFootnote(report, FOOTNOTE_PERFORMANCE_HISTORY)).add(
                " and standard deviation").add(
                Footnotes.createFootnote(report, FOOTNOTE_STANDARD_DEVIATION)).setFontFamily(
                FRUTIGER57CN).setFontSize(14).setTextAlignment("right");

        return title2HeaderParagraph;
    }
    
    protected PdfTableBody buildStyleBoxTableBody(StandardReportBuilder report,
            AssetHouseSubGroupViewBean styleBox, Map morningstarCategoryPerformances) {
        List fundsInStyleBox = styleBox.getFunds();
        int numberOfFundsInGroup = fundsInStyleBox.size();

        Set morningstarCategoriesInGroup = styleBox.getMorningstarCategoryPerformances();
        numberOfFundsInGroup += morningstarCategoriesInGroup.size();

        Set marketIndexesInGroup = styleBox.getMarketIndexIbPerformances();
        numberOfFundsInGroup += marketIndexesInGroup.size();

        int tableRowSize = numberOfFundsInGroup;

        ReportFund reportFund = null;
        for (Iterator iter = fundsInStyleBox.iterator(); iter.hasNext();) {
            reportFund = (ReportFund) iter.next();
            if (reportFund.getFundStandardDeviation().getFundDisclosureText() != null) {
                tableRowSize++;
            }
        }
        

        PdfTableBody tableBody = report.createTableBody(tableRowSize, reportDefinition
                .getNumberOfColumns());

        tableBody.setBorderBottomStyle("solid");
        tableBody.setBorderBottomColor(PDF_RGB_ROW_BORDER);
        tableBody.setBorderBottomWidth("0.5pt");
        tableBody.setPaddingBefore("0.1cm");
        
        reportDefinition.justifyReportBodyColumns(tableBody);

        for (int j = 0; j < numberOfFundsInGroup; j++) {
            if (j == 0) {
                reportDefinition.setCellFormattingForRow(tableBody, j, false);
            } else {
                reportDefinition.setCellFormattingForRow(tableBody, j, true);
            }
        }

        // this logic is for keeping rows together on one page
        int row = 0;
        int rowNumberKeepTogetherFirst = (new Integer(PropertyManager
                .getString("row.number.keep.together"))).intValue();
        int rowNumberKeepTogetherLast = 0;
        if (numberOfFundsInGroup >= rowNumberKeepTogetherFirst) {
            rowNumberKeepTogetherLast = numberOfFundsInGroup - rowNumberKeepTogetherFirst;
        }

        for (Iterator iter = fundsInStyleBox.iterator(); iter.hasNext();) {
            reportFund = (ReportFund) iter.next();

            String fundDisclosureText = reportFund.getFundStandardDeviation()
                    .getFundDisclosureText();

            reportDefinition.buildFundRow(report, tableBody, row, reportFund, reportData);
            
            // formatting / keeping rows together
            
             tableBody.setRowKeepTogetherWithinPage(row);
             if(row != numberOfFundsInGroup -1 ) {
                if (row < rowNumberKeepTogetherFirst - 1 || row >= rowNumberKeepTogetherLast) {
                    tableBody.setRowKeepWithNext(row);
                }
             }
            
            if (reportFund.getFundStandardDeviation().getFundDisclosureText() != null) {

                for (int i = 0; i < reportDefinition.getNumberOfColumns(); i++) {
                    tableBody.setCellBorderBottomStyle(row, i, "none");
                }
                
                buildDisclosureTextRow(report, tableBody, ++row, reportDefinition
                        .getNumberOfColumns(), reportFund.getFundStandardDeviation()
                        .getFundDisclosureText());
                tableBody.setRowKeepTogetherWithinPage(row);
                tableBody.setRowKeepWithPrevious(row);
            }
            
            row++;
        }

        for (Iterator iter = marketIndexesInGroup.iterator(); iter.hasNext();) {
            MarketIndexIbPerformanceViewBean marketIndexPerformance = (MarketIndexIbPerformanceViewBean) iter
                    .next();
            String footnote = null;
            
			if (StandardReportsConstants.ASSET_CLASS_LIFECYCLE.equals(marketIndexPerformance.getFundAssetCls())
					|| StandardReportsConstants.ASSET_CLASS_LIFESTYLE.equals(marketIndexPerformance.getFundAssetCls())){
            	footnote = getSuperScriptValue(marketIndexPerformance.getInvestmentid()).toString();
            } 

            if(reportDefinition.isStandardizedReportColumnsSupressed()) {
                buildBeanBasedRowWithOutStandardColumns(report, marketIndexPerformance, tableBody, row, "Market Index",
                        marketIndexPerformance.getMarketIndexName(), footnote);
            } else {
                buildBeanBasedRowWithStandardColumns(report, marketIndexPerformance, tableBody, row, "Market Index",
                        marketIndexPerformance.getMarketIndexName(), footnote);
            }
            

            // formatting / keeping rows together
            tableBody.setRowKeepTogetherWithinPage(row);
            if (row != numberOfFundsInGroup - 1) {
                if (row < rowNumberKeepTogetherFirst - 1 || row >= rowNumberKeepTogetherLast) {
                    tableBody.setRowKeepWithNext(row);
                }
            }
            row++;
        }

        for (Iterator iter = morningstarCategoriesInGroup.iterator(); iter.hasNext();) {
            MorningstarCategoryPerformanceViewBean morningstarCategoryPerformance = (MorningstarCategoryPerformanceViewBean) iter
                    .next();
            
            if(reportDefinition.isStandardizedReportColumnsSupressed()) {
                buildBeanBasedRowWithOutStandardColumns(report, morningstarCategoryPerformance, tableBody, row,
                        "Morningstar Category(Peer Group)", morningstarCategoryPerformance
                        .getMorningstarCategoryName(), null);
            } else {
                buildBeanBasedRowWithStandardColumns(report, morningstarCategoryPerformance, tableBody, row,
                        "Morningstar Category(Peer Group)", morningstarCategoryPerformance
                        .getMorningstarCategoryName(), null);
            }

            // formatting / keeping rows together
            tableBody.setRowKeepTogetherWithinPage(row);
            if (row != numberOfFundsInGroup - 1) {
                if (row < rowNumberKeepTogetherFirst - 1 || row >= rowNumberKeepTogetherLast) {
                    tableBody.setRowKeepWithNext(row);
                }
            }
            row++;
        }
        
        
        return tableBody;
    }

    private void buildBeanBasedRowWithStandardColumns(StandardReportBuilder report, Object bean,
            PdfTableBody tableBody, int row, String title, String rowName, String footnote) {
        PdfParagraph fundNameParagraph = report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                .setFontFamily(FRUTIGER47LIGHT_CN);
        tableBody.add(row, 0, fundNameParagraph.add(report.createText(StandardReportsUtils.getValueorNA(title))));
        if (footnote != null) {
            tableBody.add(row, 1, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(
                            report.createText(StandardReportsUtils.getValueorNA(rowName))).add(
                            report.createText(StandardReportsUtils.getValueorNA(footnote))));
        } else {
            tableBody.add(row, 1, report.createParagraph().setFontSize(FONT_SIZE_FUND_ROW)
                    .setFontFamily(FRUTIGER47LIGHT_CN).add(report.createText(StandardReportsUtils.getValueorNA(rowName))));
        }

        tableBody.add(row, 2, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 3, report.createParagraph(""));
        tableBody.add(row, 4, getBeanValueParagraph(report, bean, "ror_1Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 5, getBeanValueParagraph(report, bean, "ror_3Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 6, getBeanValueParagraph(report, bean, "ror_5Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 7, getBeanValueParagraph(report, bean, "ror_10Year", FONT_SIZE_FUND_ROW));

        // tableBody.add(row, 8, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 8, report.createParagraph(""));

        tableBody.add(row, 9, getBeanValueParagraph(report, bean, "ror_1YearQe", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 10, getBeanValueParagraph(report, bean, "ror_5YearQe", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 11, getBeanValueParagraph(report, bean, "ror_10YearQe", FONT_SIZE_FUND_ROW));

        tableBody.add(row, 12, report.createParagraph(""));
        tableBody.add(row, 13, getBeanValueParagraph(report, bean, "standardDeviation_3YearQe",
                FONT_SIZE_FUND_ROW));
        tableBody.add(row, 14, getBeanValueParagraph(report, bean, "standardDeviation_5YearQe",
                FONT_SIZE_FUND_ROW));
        tableBody.add(row, 15, getBeanValueParagraph(report, bean, "standardDeviation_10YearQe",
                FONT_SIZE_FUND_ROW));

        tableBody.add(row, 16, report.createParagraph(""));
        //tableBody.add(row, 17, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        //tableBody.add(row, 18, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        //tableBody.add(row, 19, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));

        if (bean instanceof MorningstarCategoryPerformanceViewBean) {
			tableBody.add(row, 17, getBeanValueParagraph(report, bean,
					"expenseRatioQe",
                    FONT_SIZE_FUND_ROW));
        } else {
            tableBody.add(row, 17, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        }

        // Shade the fund row
        for (int j = 0; j < reportDefinition.getNumberOfColumns(); j++) {
            tableBody.setCellBackgroundColor(row, j, PDF_RGB_ROW_BACKGROUND_SHADE);
        }
    }

    private void buildBeanBasedRowWithOutStandardColumns(StandardReportBuilder report, Object bean,
            PdfTableBody tableBody, int row, String title, String rowName, String footnote) {
        PdfParagraph fundNameParagraph = report.createParagraph()
                .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN);
        tableBody.add(row, 0, fundNameParagraph.add(report.createText(StandardReportsUtils.getValueorNA(title))));
        if (footnote != null) {
            tableBody.add(row, 1, report.createParagraph()
                    .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
                    .add(report.createText(StandardReportsUtils.getValueorNA(rowName)))
                    .add(report.createText(StandardReportsUtils.getValueorNA(footnote))));
        } else {
            tableBody.add(row, 1, report.createParagraph()
                    .setFontSize(FONT_SIZE_FUND_ROW).setFontFamily(FRUTIGER47LIGHT_CN)
                    .add(report.createText(StandardReportsUtils.getValueorNA(rowName))));
        }

        tableBody.add(row, 2, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 3, report.createParagraph(""));
        
        tableBody.add(row, 4, getBeanValueParagraph(report, bean, "ror_1Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 5, getBeanValueParagraph(report, bean, "ror_3Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 6, getBeanValueParagraph(report, bean, "ror_5Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 7, getBeanValueParagraph(report, bean, "ror_10Year", FONT_SIZE_FUND_ROW));
        
        //tableBody.add(row, 8, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        tableBody.add(row, 8, report.createParagraph(""));
        
        tableBody.add(row, 9, getBeanValueParagraph(report, bean, "standardDeviation_3Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 10, getBeanValueParagraph(report, bean, "standardDeviation_5Year", FONT_SIZE_FUND_ROW));
        tableBody.add(row, 11, getBeanValueParagraph(report, bean, "standardDeviation_10Year", FONT_SIZE_FUND_ROW));
        
        tableBody.add(row, 12, report.createParagraph(""));
        //tableBody.add(row, 13,report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        //tableBody.add(row, 14, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        //tableBody.add(row, 15, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        
        if (bean instanceof MorningstarCategoryPerformanceViewBean) {
			tableBody.add(row, 13, getBeanValueParagraph(report, bean,
					"expenseRatioQe", FONT_SIZE_FUND_ROW));
        } else {
            tableBody.add(row, 13, report.createParagraph("n/a").setFontSize(FONT_SIZE_FUND_ROW));
        }

        // Shade the fund row
        for (int j = 0; j < reportDefinition.getNumberOfColumns(); j++) {
            tableBody.setCellBackgroundColor(row, j, PDF_RGB_ROW_BACKGROUND_SHADE);
        }
    }
    
    private void buildDisclosureTextRow(StandardReportBuilder report, PdfTableBody tableBody,
            int row, int numberOfColumns, String disclosureText) {
        TextStyle normalTextStyle = new TextStyle(report,
                ReportFormattingConstants.FRUTIGER47LIGHT_CN,
                ReportFormattingConstants.FONT_SIZE_FUND_ROW);
        tableBody.add(row, 0, normalTextStyle
                .createParagraph(disclosureText)).setCellPaddingLeft(row, 0, "0.2cm");
        tableBody.setCellBorderTopStyle(row, 0, "none");
        tableBody.spanCells(row, 0, 1, numberOfColumns);
    }
    
    /**
     * Method to retrieve the footNotes super scripts if they are not supposed to be present.
     * 
     * @param reportData
     * @return List<String>
     */
    private List<String> getNotRequiredFootNoteSuperScripts(FundReportData reportData) {

    	String[] dynamicSuperScripts = new String[] { FOOTNOTE_RETIREMENT_CHOICES_MARKET_INDEX, 
                FOOTNOTE_RETIREMENT_LIVING_MARKET_INDEX, FOOTNOTE_LIFESTYLE_MARKET_INDEX,
                FOOTNOTE_SUPERSCRIPT_31, FOOTNOTE_SUPERSCRIPT_32, FOOTNOTE_SUPERSCRIPT_33,
                FOOTNOTE_SUPERSCRIPT_34, FOOTNOTE_SUPERSCRIPT_35};
    	
		List<String> investmentIdList = new ArrayList<String>();
		
		List<String> dynamicSuperScriptsList = new ArrayList<String>(Arrays.asList(dynamicSuperScripts));
		
		if (reportData != null && reportData.getFunds() != null) {
			for (ReportFund fundVO : (List<ReportFund>) reportData.getFunds()) {
				if (fundVO.getFund() != null) {
					if (fundVO.getFund().getInvestmentid() != null) {
						investmentIdList
								.add(fundVO.getFund().getInvestmentid());
					}
				}
			}
		}

		if (investmentIdList != null) {
			for (String investmentId : investmentIdList) {
				String superScripts = getSuperScriptValue(investmentId).toString();
				if(StringUtils.isNotBlank(superScripts)){
					if (StringUtils.contains(superScripts, CommonConstants.COMMA_SYMBOL)) {
						String[] supreScriptArray = StringUtils.split(superScripts,
								CommonConstants.COMMA_SYMBOL);
	
						for (String superScript : supreScriptArray) {
							if (dynamicSuperScriptsList.contains(superScript)) {
								dynamicSuperScriptsList.remove(superScript);
							}
						}
	
					} else {
						if (dynamicSuperScriptsList.contains(superScripts)) {
							dynamicSuperScriptsList.remove(superScripts);
						}
					}
				}
			}
		}
		
		return dynamicSuperScriptsList;
	}
}
