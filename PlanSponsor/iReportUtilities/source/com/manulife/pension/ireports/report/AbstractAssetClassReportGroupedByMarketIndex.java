package com.manulife.pension.ireports.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.LinkedMap;

import com.manulife.pension.content.view.MutableFootnote;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.dao.FootnoteSymbolComparator;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.streamingreport.impl.TextStyle;
import com.manulife.pension.ireports.report.viewbean.AssetHouseSubGroupViewBean;
import com.manulife.pension.ireports.report.viewbean.MarketIndexIbPerformanceViewBean;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableRegion;

public abstract class AbstractAssetClassReportGroupedByMarketIndex extends AbstractAssetClassReport {
	
    public AbstractAssetClassReportGroupedByMarketIndex(ReportOptions options,
			ReportDefinition reportDefinition) {
		super(options, reportDefinition);
	}

	private static final FootnoteSymbolComparator FOOTNOTE_SYMBOL_COMPARATOR = new FootnoteSymbolComparator();
    
    
    /**
     * Method is used to display the solid and dashed lines in the iReport PDF.
     * @param tableRegion
     * @param row
     * @param setTopBorder
     * @param columnNumber
     */
    private  void setCellFormattingBottom(PdfTableRegion tableRegion, int row, boolean setTopBorder,int columnNumber)
    {
    	if (setTopBorder) {
    		for (int i = 0; i < columnNumber; i++) {
    			tableRegion.setCellBorderBottomStyle(row, i, "solid");
    			tableRegion.setCellBorderBottomWidth(row, i, "0.5pt");
    			tableRegion.setPaddingBefore("0.1cm");
    			tableRegion.setCellBorderBottomColor(row, i, PDF_RGB_ROW_BORDER_3);
    		}
    	}
    	else{
    		for (int i = 0; i < columnNumber; i++) {
    			tableRegion.setCellBorderBottomStyle(row, i, "dashed");
    			tableRegion.setCellBorderBottomWidth(row, i, "0.5pt");
    			tableRegion.setPaddingBefore("0.1cm");
    			tableRegion.setCellBorderBottomColor(row, i, PDF_RGB_ROW_BORDER);
    		}
    	}
    }
    
   protected PdfTableBody buildStyleBoxTableBody(StandardReportBuilder report,
            AssetHouseSubGroupViewBean styleBox, Map morningstarCategoryPerformances) {
        List fundsInStyleBox = styleBox.getFunds();
        int numberOfFundsInGroup = fundsInStyleBox.size();

        Set marketIndexesInGroup = styleBox.getMarketIndexIbPerformances();
       // numberOfFundsInGroup += marketIndexesInGroup.size();

		// Make declaration we can access globally
        Map<String, String> marketIndexFootnote = reportData.getMarketIndexFootnote();
        
        List<String> marketIndexFootnoteSymbols = new ArrayList<String>();
        int tableRowSize = numberOfFundsInGroup;

        ReportFund reportFund = null;
        for (Iterator iter = fundsInStyleBox.iterator(); iter.hasNext();) {
            reportFund = (ReportFund) iter.next();
            if (reportFund.getFundStandardDeviation().getFundDisclosureText() != null) {
                tableRowSize++;
            }
        }
        //This logic is implemented for Funds that share same primary index make up a grouping 
       // List<ReportFund> sortedFunds = new ArrayList<ReportFund>();
/*		for (Iterator itre1 = marketIndexesInGroup.iterator(); itre1.hasNext();) {
			MarketIndexIbPerformanceViewBean marketIndexPerformance = (MarketIndexIbPerformanceViewBean) itre1
					.next();
			String footnote = marketIndexFootnote.get(marketIndexPerformance.getMarketIndexId());
			marketIndexFootnoteSymbols.add(footnote);
			for (Iterator it = fundsInStyleBox.iterator(); it.hasNext();) {

				ReportFund tempreportFund = (ReportFund) it.next();
			if (marketIndexPerformance.getMarketIndexId().equals(tempreportFund.getFund().getMarketIndexId())) {
					sortedFunds.add(tempreportFund);
				}
			}
		}*/

        PdfTableBody tableBody = report.createTableBody(tableRowSize, reportDefinition
                .getNumberOfColumns());
        
        reportDefinition.justifyReportBodyColumns(tableBody);

        for (int j = 0; j < numberOfFundsInGroup; j++) {
            if (j == 0) {
                reportDefinition.setCellFormattingForRow(tableBody, j, false);
            } else {
                reportDefinition.setCellFormattingForRow(tableBody, j, false);
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
       
        String[] marketIndex = new String[1];
        String marketindexId = null;
      //  for (Iterator itre1 = marketIndexesInGroup.iterator(); itre1.hasNext();) {
        	//MarketIndexIbPerformanceViewBean marketIndexPerformance = (MarketIndexIbPerformanceViewBean) itre1.next();
        	for (Iterator iter = fundsInStyleBox.iterator(); iter.hasNext();) {
        		reportFund = (ReportFund) iter.next();
        		String fundDisclosureText = reportFund
        				.getFundStandardDeviation().getFundDisclosureText();
        		//if (marketIndexPerformance.getMarketIndexId().equalsIgnoreCase(reportFund.getFund().getMarketIndexId())) {
        			 
        			reportDefinition.buildFundRow(report, tableBody, row, reportFund, reportData);
        			setCellFormattingBottom(tableBody, row, false, reportDefinition.getNumberOfColumns());
        			if (row != numberOfFundsInGroup - 1) {
        				if (row < rowNumberKeepTogetherFirst - 1
        						|| row >= rowNumberKeepTogetherLast) {
        					tableBody.setRowKeepWithNext(row);
        				}
        			}
        			if (reportFund.getFundStandardDeviation().getFundDisclosureText() != null) {
        				for (int i = 0; i < reportDefinition.getNumberOfColumns(); i++) {
        					tableBody.setCellBorderBottomStyle(row, i, "none");
        				}
        				buildDisclosureTextRow(report, tableBody, ++row,
        						reportDefinition.getNumberOfColumns(),
        						reportFund.getFundStandardDeviation()
        						.getFundDisclosureText());
        				tableBody.setRowKeepTogetherWithinPage(row);
        				tableBody.setRowKeepWithPrevious(row);
        			}
        			
        			row++;
        			//marketindexId = reportFund.getFund().getMarketIndexId();
        			//row++;
        		//} else if (marketIndex[0].toString().equalsIgnoreCase(marketIndexPerformance.getMarketIndexId())) {
        			/*if (marketindexId.toString().equalsIgnoreCase(marketIndexPerformance.getMarketIndexId())) {
        				String footnote = null;
        				footnote = marketIndexFootnote.get(marketIndexPerformance.getMarketIndexId());
        				marketIndexFootnoteSymbols.add(footnote);*/
        				
        				//if (reportDefinition.isStandardizedReportColumnsSupressed()) {
        					/*reportDefinition.setCellFormattingForRow(tableBody, row, true);
        					setCellFormattingBottom(tableBody, row, true, reportDefinition.getNumberOfColumns());
        					
        					buildBeanBasedRowWithOutStandardColumns(
        							report,
        							marketIndexPerformance,
        							tableBody,
        							row,
        							" Market Index ",
        							marketIndexPerformance.getMarketIndexName(),
        							footnote);*/
        				//} else {
        					/*reportDefinition.setCellFormattingForRow(tableBody, row, true);
        					setCellFormattingBottom(tableBody, row, true, reportDefinition.getNumberOfColumns());
        					buildBeanBasedRowWithStandardColumns(
        							report,
        							marketIndexPerformance,
        							tableBody,
        							row,
        							" Market Index ",
        							marketIndexPerformance.getMarketIndexName(),
        							footnote);*/
        				//}

        				/*if (row != numberOfFundsInGroup - 1) {
        					row++;
        				}*/
        			}
        		//}
        		//marketIndex[0] = reportFund.getFund().getMarketIndexId();
        	

        	/*if ((!itre1.hasNext())) {
        		String footnote = null;
        		 footnote = marketIndexFootnote.get(marketIndexPerformance.getMarketIndexId());
        		marketIndexFootnoteSymbols.add(footnote);
        		
        		if (reportDefinition.isStandardizedReportColumnsSupressed()) {
        			reportDefinition.setCellFormattingForRow(tableBody, row, true);
        			setCellFormattingBottom(tableBody, row, true, reportDefinition.getNumberOfColumns());
        			
					buildBeanBasedRowWithOutStandardColumns(report,
							marketIndexPerformance, tableBody, row,
							" Market Index",
							marketIndexPerformance.getMarketIndexName(),
							footnote);
        		} else {
        			reportDefinition.setCellFormattingForRow(tableBody, row, true);
        			setCellFormattingBottom(tableBody, row, true, reportDefinition.getNumberOfColumns());
        			buildBeanBasedRowWithStandardColumns(report,
        					marketIndexPerformance, tableBody, row,
        					" Market Index ",
        					marketIndexPerformance.getMarketIndexName(),
        					footnote);
        		}
        		if (row != numberOfFundsInGroup - 1) {
        			row++;
        		}
        	}*/
        //}

      Map<String, String> footnotesOnReport = reportData.getFootnotes();
       // Map<String, String> marketIndexFootNotes = getMarketIndexFootNotes(marketIndexFootnoteSymbols);
        
        Map<String, String> marketfootnotesOnReport =new HashMap<String,String>();
      marketfootnotesOnReport.putAll(footnotesOnReport);
       // marketfootnotesOnReport.putAll(marketIndexFootNotes);
       Map<String, String> orderedFootnotes = sortFootnotesOnReport(marketfootnotesOnReport);
       reportData.setFootnotes(orderedFootnotes);
		
        return tableBody;

    }
    
    /**
     * Method is used to arranged the footnotes in order
     * 
     * @param unorderedFootnotes
     * @return MAP
     */
   protected  Map sortFootnotesOnReport(Map unorderedFootnotes) {
		List allFootnoteSymbolsOnReport = new ArrayList(unorderedFootnotes.keySet());
		Collections.sort(allFootnoteSymbolsOnReport, FOOTNOTE_SYMBOL_COMPARATOR);

		Map result = new LinkedMap();
		for (Iterator iter = allFootnoteSymbolsOnReport.iterator(); iter.hasNext();) {
			String symbol = (String) iter.next();
			result.put(symbol, unorderedFootnotes.get(symbol));
		}
		return result;
	}
   
   /**
    * Method  to return the MarketIndexFootNotes Text for available marketIndexFootNoteSymbol 
    * @param marketIndexFootnotes
    * @return Map<String ,String>
    */
   
    private Map<String, String>  getMarketIndexFootNotes(List<String> marketIndexFootnotes){
    	
    	if (marketIndexFootnotes == null) {
			return Collections.emptyMap();
		}
		Map<String, String> result = new HashMap<String, String>();
		Map footenotesymbols = reportData.getFootnotesByCompany();
		Map<String, MutableFootnote> footnotesMap = null;
		if(StandardReportsConstants.COMPANY_ID_NY.equals(options.getFundOffering().getCompanyId())){
			footnotesMap = (Map<String, MutableFootnote>)footenotesymbols.get(StandardReportsConstants.COMPANY_ID_NY);
		}else{
			footnotesMap = (Map<String, MutableFootnote>) footenotesymbols.get(StandardReportsConstants.COMPANY_ID_USA);
		}
		
		MutableFootnote content = null;
		String footnoteText = null;
		for (int i = 0; i < marketIndexFootnotes.size(); i++) {
			String symbol = marketIndexFootnotes.get(i);
			content = (MutableFootnote) footnotesMap.get(symbol);
			if (content != null) {
				if (options.getFundOffering().getCompanyId().equalsIgnoreCase(StandardReportsConstants.COMPANY_ID_USA)) {
					footnoteText = content.getText();
				} else {
					footnoteText = content.getNyText();
					if (footnoteText == null) {
						footnoteText = content.getText();
					}
				}
				if (footnoteText != null) {
					result.put(symbol, footnoteText);
				}
			}
		}
		return result;
    }

    protected abstract void buildBeanBasedRowWithStandardColumns(StandardReportBuilder report, Object bean,
            PdfTableBody tableBody, int row, String title, String rowName, String footnote);

    protected abstract void buildBeanBasedRowWithOutStandardColumns(StandardReportBuilder report, Object bean,
            PdfTableBody tableBody, int row, String title, String rowName, String footnote);
    
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
}
