package com.manulife.pension.ireports.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.dao.ReportDataRepository;
import com.manulife.pension.ireports.dao.ReportDataRepositoryFactory;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.util.AssetHouseFundMapping;
import com.manulife.pension.ireports.report.viewbean.AssetHouseMainGroupViewBean;
import com.manulife.pension.ireports.report.viewbean.AssetHouseSubGroupViewBean;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.pension.platform.utility.CommonConstants;
import com.manulife.pension.platform.utility.fap.constants.FapConstants;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.util.pdf.PdfColors;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

public abstract class AbstractAssetClassReport extends StandardReportTemplate {
	protected Date asOfDate;
	protected FundReportData reportData;
	protected ReportDefinition reportDefinition;
	
	public AbstractAssetClassReport(ReportOptions options, ReportDefinition reportDefinition) {
		super(options);
		this.reportDefinition = reportDefinition;
		setLandscape(reportDefinition.isLandscape());
		setLegalSize(reportDefinition.isLegalSize());
	}
	
	protected abstract void definePageSequence(StandardReportBuilder report,PdfFlow flow, FundReportData reportData);

    /**
     * Builds the PDF content for the report
     * 
     * @throws IOException
     */
	public void buildReport() {
	    
		initializeReportData();
		
		if(reportDefinition.containsStandardizedReportColumns()) {
		    Date investmentReturnsAsofDate = reportData.getAsOfDateForContext("ROR");
	        Date quarterEndDate = reportData.getAsOfDateForContext("RORQE");
	        if(investmentReturnsAsofDate.equals(quarterEndDate)) {
	            reportDefinition.hideStandardizedReportColumns();
	        }
		}
		
		createPageLayoutStructure(report, reportDefinition.isHeaderParagraphPresent(), options.getContractShortlistOptions().isContractOrShortlistUsed());

		PdfFlow flow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
		PdfParagraph tableParagraph = report.createParagraph().setMargin("0.0cm").setTextAlignment(
                "end");
		flow.add(tableParagraph);
		
        // Add Intro Disclosure
        String[] introDisclosuresContentIds;
        if(reportData.isMerrillAdvisor()){
        	introDisclosuresContentIds = reportDefinition.getIncludedMerrillDisclosuresContentIds();
        } else {
        	introDisclosuresContentIds = reportDefinition.getIntroDisclosuresContentIds(); 
        }
        if (introDisclosuresContentIds != null && introDisclosuresContentIds.length > 0) {
            for (String introDisclosureContentId : introDisclosuresContentIds) {
                PdfParagraph introDisclosureParagraph = createIntroDisclosures(introDisclosureContentId);
                if (introDisclosureParagraph != null) {
                    introDisclosureParagraph.setPaddingBottom("0.2cm").setTextAlignment("left");
                    tableParagraph.add(introDisclosureParagraph);
                }
            }
        }        
        // Add Contract level Intro Disclosure
        String[] introContractLevelDisclosuresContentIds = null;
        if(options.getContractFundsMap()!=null && options.getContractFundsMap().size()>0){
            introContractLevelDisclosuresContentIds = reportDefinition.getIntroContractLevelDisclosuresContentIds();
        }else {
            introContractLevelDisclosuresContentIds = reportDefinition.getIntroGenericLevelDisclosuresContentIds();
        }
        if (introContractLevelDisclosuresContentIds != null && introContractLevelDisclosuresContentIds.length > 0) {
            for (String introDisclosureContentId : introContractLevelDisclosuresContentIds) {
                PdfParagraph introDisclosureParagraph = createIntroDisclosures(introDisclosureContentId);
                if (introDisclosureParagraph != null) {
                    introDisclosureParagraph.setPaddingBottom("0.2cm").setTextAlignment("left");
                    tableParagraph.add(introDisclosureParagraph);
                }
            }
        } 
        
        addUserModifiedDisclosure(tableParagraph);
        
		PdfTableHeader mainTableHeader = reportDefinition.buildMainTableHeader(report,
                classDisclosureFootnoteNumber, reportData, options);
		
		PdfTableBody mainTableBody = buildMainTableBody(report);
		
		PdfTable mainTable = report.createTable(reportDefinition.getColumnWidths(),
                new PdfTableRegion[] { mainTableHeader, mainTableBody });
		
        tableParagraph.add(mainTable);
		
		definePageSequence(report, flow, reportData);
		
		addISFReport();
	}

	protected void initializeReportData() {
		ReportDataRepository reportDataRepository = 
			ReportDataRepositoryFactory.getRepository();
		
		String[] staticFootnotes = reportDefinition.getStaticFootnoteSymbols();
		
		staticFootnotes = options.getContractShortlistOptions().addToFootnotes(staticFootnotes);
		if(reportDefinition.containsClassDisclosureFootnote()) {
			staticFootnotes = addClassDisclosureFootnote(staticFootnotes, options);
		}
		
		reportData = reportDataRepository.getFundReportData(
				options,
				ReportDataRepository.REPORTFUND_SORT_ORDER_RISKRETURN,
				staticFootnotes
				);
	}
	
	private PdfTableBody buildMainTableBody(StandardReportBuilder report) {
		List funds = reportData.getFunds();
		Map morningstarCategoryPerformances = reportData.getMorningstarCategoryPerformances();
		Map marketIndexIbPerformances = reportData.getMarketIndexIbPerformances();
		// Modified the below method for ACR REWRITE (iReport)
		List assetClasses = collateFundsIntoAssetHouseInvestmentGroups(funds, morningstarCategoryPerformances, marketIndexIbPerformances,options.getReportCode());
	
		int numberOfAssetClasses = assetClasses.size();
		
		// 2 rows/ each Asset Class to incorporate spacing after each Class
		// +1 for Market Index information
        int numberOfRows = numberOfAssetClasses * 2 + 2;

        PdfTableBody mainTableBody = report.createTableBody(numberOfRows, 1);
	
		int currentAssetClassIndex = 0;
		List<List<ReportFund>> allFunds = new ArrayList<List<ReportFund>>();
		
		// Generate sections for Asset Class (main Group)
        for (Iterator assetClassIterator = assetClasses.iterator(); assetClassIterator.hasNext();) {    
		    
			AssetHouseMainGroupViewBean assetClass = (AssetHouseMainGroupViewBean) assetClassIterator.next();
			List styleBoxes = assetClass.getSubGroups();
			
			PdfTableRegion assetClassTableHeader = buildAssetClassHeader(report, assetClass);
			
			int numberOfStyleBoxes = assetClass.getNumberOfSubgroups();
			
			if (shouldBuildGicSection(reportData, assetClass)) {
				numberOfStyleBoxes++;
			}
			
			PdfTableBody assetClassTableBody = report.createTableBody(numberOfStyleBoxes, 1);
			int i = 0;
			
			List gicFunds = new ArrayList();
			
			// Generate sub-section under Asset Class for Style Box (sub Group)
			for (Iterator styleBoxIterator = styleBoxes.iterator(); styleBoxIterator.hasNext();) {
				AssetHouseSubGroupViewBean styleBox = (AssetHouseSubGroupViewBean) styleBoxIterator.next();
				
				if(shouldBuildGicSection(reportData, assetClass) && styleBox.hasGICFunds()) {
					gicFunds.addAll(styleBox.getGICFunds());
				} else {
					PdfTableHeader styleBoxHeading = buildStyleBoxHeading(report,  styleBox);
					PdfTableBody styleBoxTableBody =  buildStyleBoxTableBody(report, styleBox, morningstarCategoryPerformances);
					allFunds.add(styleBox.getFunds());
					PdfTableRegion[] styleBoxTableRegions = new PdfTableRegion[]{
						styleBoxHeading, styleBoxTableBody
					};
					PdfTable styleBoxTable = report.createTable(reportDefinition.getColumnWidths(), styleBoxTableRegions);
					assetClassTableBody.add(i++, 0, styleBoxTable);
				}
			} //end of sub group (style box) iterator
	
			// Add Guaranteed Accounts subgroup to Fixed Income Asset Class. 
			if (shouldBuildGicSection(reportData, assetClass)) {
				
				PdfTable gicTable = buildGicSection(report, reportDefinition, reportData, gicFunds, GIC_ASSETCLASS);
				assetClassTableBody.add(i++, 0, gicTable);
			}
			
			while (i < numberOfStyleBoxes) {
                assetClassTableBody.add(i++, 0, report.createParagraph());
            }
			
			PdfTableRegion[] assetClassTableRegions = new PdfTableRegion[]{assetClassTableHeader, assetClassTableBody};
			String assetClassColumnSize = isLandscape ? "24.5cm" : "19cm";
			PdfTable assetClassTable = report.createTable(new String[]{assetClassColumnSize}, assetClassTableRegions );
			
			mainTableBody.add(currentAssetClassIndex++,0, assetClassTable);
			

			if (assetClassIterator.hasNext()) {
				 mainTableBody.add(currentAssetClassIndex++, 0, buildAssetClassSpacerTable(report));
			}
			
		} //end of mainGroupIterator
        
        //Populating Morningstar Foot Notes
		ReportDataRepository reportDataRepository = ReportDataRepositoryFactory
				.getRepository();
		reportDataRepository.getMorningstarFootNotes(options, reportData,
				allFunds);
		
		// Market Index for FundCheck 
		if(reportDefinition.containsMarketIndexSectionAssetClass()) {
			PdfTable marketIndexTable = buildMarketIndexSection(report, reportData);
			mainTableBody.add(currentAssetClassIndex++, 0, marketIndexTable);
		}
		
		// Market Index for Investment Expense Ration & Standard Deviation 
		if(reportDefinition.containsStandardizedReportColumns()){
			PdfTable marketIndexTable = buildMarketIndexSection(report, reportData);
			mainTableBody.add(currentAssetClassIndex++, 0, marketIndexTable);
		}
		
		PdfTable spacer =  buildSpacerTable(report);
	    mainTableBody.add(currentAssetClassIndex++, 0, spacer);
		PdfTable lastRowTable = reportDefinition.buildLastRow(report, 0, reportData);
		if (lastRowTable != null) {
			mainTableBody.add(currentAssetClassIndex++, 0, lastRowTable);
		}
		
        /*
         * initialize empty cells to fo:block
         */
         while (currentAssetClassIndex < numberOfRows) {
                mainTableBody.add(currentAssetClassIndex++, 0, report.createParagraph());
        }
         
		return mainTableBody;
	}
	
    private PdfTable buildSpacerTable(StandardReportBuilder report) {
        PdfTableRegion spacerBody = report
                .createTableBody(1, reportDefinition.getNumberOfColumns());
        spacerBody.add(0, 0, report.createText(" "));
        spacerBody.setRowHeight(0, "0.75cm");

        /*
         * Initialize empty cells with a fo:block
         */
        for (int i = 1; i < reportDefinition.getNumberOfColumns(); i++) {
            spacerBody.add(0, i, report.createParagraph());
        }

        PdfTableRegion[] spacerRegions = new PdfTableRegion[] { spacerBody };
        PdfTable spacerTable = report
                .createTable(reportDefinition.getColumnWidths(), spacerRegions);
        return spacerTable;
    }
	
	private PdfTable buildAssetClassSpacerTable(StandardReportBuilder report) {
		PdfTableRegion assetClassSpacerBody = report.createTableBody(1, reportDefinition.getNumberOfColumns());
		assetClassSpacerBody.add(0, 0, report.createText(" "));
		assetClassSpacerBody.setRowHeight(0, "0.75cm");
		//changes done as part of ACR REWRITE (ireport)
		if(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT.equals(options.getReportCode())){
			reportDefinition.setCellFormattingForRow(assetClassSpacerBody, 0, false);
		}
		else
		{
			reportDefinition.setCellFormattingForRow(assetClassSpacerBody, 0, true);
		}
		
		/*
         * Initialize empty cells with a fo:block
         */
        for (int i = 1; i < reportDefinition.getNumberOfColumns(); i++) {
            assetClassSpacerBody.add(0, i, report.createParagraph());
        }
	
		PdfTableRegion[] assetClassSpacerRegions = new PdfTableRegion[] { assetClassSpacerBody };
		PdfTable assetClassSpacerTable = report.createTable(reportDefinition.getColumnWidths(), assetClassSpacerRegions);
		return assetClassSpacerTable;
	}

	private PdfTableRegion buildAssetClassHeader(StandardReportBuilder report, AssetHouseMainGroupViewBean assetClass) {
		PdfTableRegion assetClassTableHeader = report.createTableHeader(1, 1);
		PdfTableRegion assetClassDescriptionBody = report.createTableBody(2, reportDefinition.getNumberOfColumns());
		assetClassDescriptionBody.add(0, 0, report.createParagraph());
		//changes done as part of ACR REWRITE (ireport)
		if(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT.equals(options.getReportCode())){
			assetClassDescriptionBody.setRowHeight(0, "1.1pt");
			assetClassDescriptionBody.add(1, 0, report.createText(assetClass.getName()).setFontSize(12).setFontWeight("bold"));
			assetClassDescriptionBody.setRowHeight(1, "1.1cm");
		}
		else
		{
			assetClassDescriptionBody.setRowHeight(0, "1pt");
			assetClassDescriptionBody.add(1, 0, report.createText(assetClass.getName()).setFontSize(12).setFontWeight("bold"));
			assetClassDescriptionBody.setRowHeight(1, "1cm");
		}
        /*
         * Initialize empty cells with a fo:block
         */
        for (int i = 1; i < reportDefinition.getNumberOfColumns(); i++) {
            assetClassDescriptionBody.add(0, i, report.createParagraph());
            assetClassDescriptionBody.add(1, i, report.createParagraph());
        }
		
		assetClassDescriptionBody.setCellVerticalAlign(1, 0, "center");
	    // reportDefinition.setCellFormattingForRow(assetClassDescriptionBody, 0, false);
		reportDefinition.setCellFormattingForRow(assetClassDescriptionBody,1,false);
		PdfTableRegion[] assetClassDescriptionRegions = new PdfTableRegion[]{assetClassDescriptionBody};
		PdfTable assetClassDescriptionTable = report.createTable(reportDefinition.getColumnWidths(), assetClassDescriptionRegions);
		assetClassTableHeader.add(0,0,assetClassDescriptionTable);
		return assetClassTableHeader;
	}

	private PdfTableHeader buildStyleBoxHeading(StandardReportBuilder report, AssetHouseSubGroupViewBean investmentGroup) {
		PdfTableHeader fundsTableHeader = report.createTableHeader(1, reportDefinition.getNumberOfColumns());
		fundsTableHeader.add(0, 0, report.createParagraph()
				.setFontSize(9).setFontWeight("bold")
				.add(report.createText(investmentGroup.getName())
						.setColor(PdfColors.WHITE)));
		fundsTableHeader.setCellVerticalAlign(0, 0, "center");
		fundsTableHeader.setCellBackgroundColor(0, 0, COLOUR_NAVY_BLUE);
		fundsTableHeader.setCellMarginLeft(0, 0, "0.1in");
		fundsTableHeader.setCellBorderBottomStyle(0, 0, "solid");
		fundsTableHeader.setBorderColor(COLOUR_NAVY_BLUE);
		fundsTableHeader.setCellBorderBottomWidth(0, 0, "0.01pt");
		fundsTableHeader.spanCells(0, 0, 1, reportDefinition.getNumberOfColumns());
		
		//to stop the bleeding of shading from first fund row
		fundsTableHeader.setTablePaddingAfter("1.1pt");
		
		return fundsTableHeader;
	}
	protected PdfTableBody buildStyleBoxTableBody(StandardReportBuilder report, AssetHouseSubGroupViewBean styleBox, Map morningstarCategoryPerformances) {
		return buildStyleBoxTableBody( report, styleBox.getFunds(), morningstarCategoryPerformances);
	}

	private PdfTableBody buildStyleBoxTableBody(StandardReportBuilder report,
			List funds, Map morningstarCategoryPerformances) {

		int numberOfFundsInGroup = funds.size();

		ReportFund reportFund = null;
		for (Iterator iter = funds.iterator(); iter.hasNext();) {
			reportFund = (ReportFund) iter.next();
			if (reportFund.getFundStandardDeviation() != null) {
				if (reportFund.getFundStandardDeviation()
						.getFundDisclosureText() != null
						&& reportDefinition.isShowMMFDisclosureText()) {
					numberOfFundsInGroup++;
				}
			}
		}

		PdfTableBody tableBody = report.createTableBody(numberOfFundsInGroup,
				reportDefinition.getNumberOfColumns());

		reportDefinition.justifyReportBodyColumns(tableBody);

		for (int j = 0; j < numberOfFundsInGroup; j++) {
			reportDefinition.setCellFormattingForRow(tableBody, j, false);
		}

		// this logic is for keeping rows together on one page
		int row = 0;
		int rowNumberKeepTogetherFirst = (new Integer(PropertyManager
				.getString("row.number.keep.together"))).intValue();
		int rowNumberKeepTogetherLast = 0;
		if (numberOfFundsInGroup >= rowNumberKeepTogetherFirst) {
			rowNumberKeepTogetherLast = numberOfFundsInGroup
					- rowNumberKeepTogetherFirst;
		}
		String fundDisclosureText = null;
		int ind = 0;
		for (Iterator iter = funds.iterator(); iter.hasNext();) {
			reportFund = (ReportFund) iter.next();

			if (reportFund.getFundStandardDeviation() != null) {
				fundDisclosureText = reportFund.getFundStandardDeviation()
						.getFundDisclosureText();
			}

			reportDefinition.buildFundRow(report, tableBody, row, reportFund, reportData);

			int k = 0;
			if (ind == 0) {
				for (Iterator iterator = funds.iterator(); iterator.hasNext();) {
					ReportFund reportFundForIterate = (ReportFund) iterator
							.next();
					for (int col = 0; col < reportDefinition.getNumberOfColumns(); col++) {
						if (!"MMR".equals(reportFundForIterate.getFund().getInvestmentid())) {
							tableBody.setCellBorderBottomStyle(k, col, "solid");
							tableBody.setCellBorderBottomColor(k, col, PDF_RGB_ROW_BORDER);
							tableBody.setCellBorderBottomWidth(k, col, "0.5pt");
						} else if ("MMR".equals(reportFundForIterate.getFund().getInvestmentid())
								&& !reportDefinition.isShowMMFDisclosureText()) {
							tableBody.setCellBorderBottomStyle(k, col, "solid");
							tableBody.setCellBorderBottomColor(k, col, PDF_RGB_ROW_BORDER);
							tableBody.setCellBorderBottomWidth(k, col, "0.5pt");
						}
					}
					k++;
				}
			}
			ind++;
				
			// formatting / keeping rows together
            tableBody.setRowKeepTogetherWithinPage(row);
            if (row != numberOfFundsInGroup - 1) {
				if (row < rowNumberKeepTogetherFirst-1 || row >= rowNumberKeepTogetherLast) {
					tableBody.setRowKeepWithNext(row);
				}
            }
            

			if (fundDisclosureText != null && reportDefinition.isShowMMFDisclosureText()) {
				tableBody = reportDefinition.buildDisclosureTextRow(report, tableBody, ++row,
						reportDefinition.getNumberOfColumns(), reportFund);
			}

			row++;
		}	
		tableBody.setPaddingBefore("0.1cm");
		
		return tableBody;
		
	}

	protected boolean shouldBuildGicSection(FundReportData reportData, AssetHouseMainGroupViewBean mainGroup) {
		return reportDefinition.containsGicSection() 
			&& isFixedIncomeMainGroup(mainGroup) 
			&& containsGuaranteedAccountRates(reportData);
	}

	protected boolean containsGuaranteedAccountRates(FundReportData reportData) {
		return reportData.getGuaranteedAccountRates() != null
                && !reportData.getGuaranteedAccountRates().isEmpty();
	}

	/**
     * This method checks if the given reportdata contains the retirement living or choices funds or both.
     * 
     * @return String retirementLivingOrChoices
     * The values are 1) "Y"  - contract having both retirement living and choice funds
     * 				  2) "RC" - Retirement Choice funds only
     * 				  3) "RL" - Retirement Living funds only 
     * 				  4) ""   - doesn't have any retirement choice or living funds
     */
    @SuppressWarnings("unchecked")
    protected String containsRetirementLivingOrChoicesFund(FundReportData reportData) {
        boolean isRetirementLiving = false;
        boolean isRetirementChoices = false;
        String retirementLivingOrChoices = StringUtils.EMPTY;
        if (reportData != null && reportData.getFunds() != null) {
            for (ReportFund fundVO : (List<ReportFund>) reportData.getFunds()) {
                if (fundVO.getFund() != null){
                	if(CommonConstants.RETIREMENT_LIVING.equals(fundVO.getFund().getFundFamilyCd())) {
                		isRetirementLiving = true;
                	}else if(CommonConstants.RETIREMENT_CHOICES.equals(fundVO.getFund().getFundFamilyCd())){
                		isRetirementChoices = true;
                	}
                }
                if(isRetirementLiving && isRetirementChoices){
                	break;
                }
            }
            if(isRetirementLiving && isRetirementChoices){
            	retirementLivingOrChoices = CommonConstants.RETIREMENT_LIVING_AND_CHOICES;
            } else if(isRetirementChoices) {
            	retirementLivingOrChoices = CommonConstants.RETIREMENT_CHOICES;
            } else if(isRetirementLiving){
            	retirementLivingOrChoices = CommonConstants.RETIREMENT_LIVING;
            }
        }
        return retirementLivingOrChoices;
    }

    /**
     * This method checks if the given reportdata contains the lifestyle funds or not.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    protected boolean containsLifestyleFunds(FundReportData reportData) {
        boolean islifestyleInd = false;
        if (reportData != null && reportData.getFunds() != null) {
            for (ReportFund fundVO : (List<ReportFund>) reportData.getFunds()) {
                if (fundVO.getFund() != null && fundVO.getFund().isLifestyle()) {
                    islifestyleInd = true;
                    break;
                }
            }
        }
        return islifestyleInd;
    }
    
	private boolean isFixedIncomeMainGroup(AssetHouseMainGroupViewBean mainGroup) {
		return AssetHouseFundMapping.SECTION_TITLE_FIXED_INCOME.equals(mainGroup.getName());
	}

	private PdfTable buildMarketIndexSection(StandardReportBuilder report, FundReportData reportData) {
		PdfTableBody marketIndexTableBody = buildMarketIndexTableBody(report, reportData.getFunds());
		
		PdfTableHeader marketIndexTableHeader = buildMarketIndexTableHeader(report);
		
		PdfTableRegion[] marketIndexTableRegions = new PdfTableRegion[]{
				marketIndexTableHeader, marketIndexTableBody
		};
		PdfTable marketIndexTable = report.createTable(reportDefinition.getColumnWidths(), marketIndexTableRegions);
		return marketIndexTable;
	}

	private PdfTableHeader buildMarketIndexTableHeader(StandardReportBuilder report) {
		PdfTableHeader fundGroupHeading = report.createTableHeader(2, reportDefinition.getNumberOfColumns());
	
		fundGroupHeading.add(0, 0, report.createText("Market Indexes").setFontSize(12).setFontWeight("bold"));
		fundGroupHeading.setRowHeight(0, "1cm");
		fundGroupHeading.setCellVerticalAlign(0, 0, "center");
		reportDefinition.setCellFormattingForRow(fundGroupHeading,0,true);
		
		 /*
         * Initialize empty cells with a fo:block
         */
        for (int i = 1; i < reportDefinition.getNumberOfColumns(); i++) {
            fundGroupHeading.add(0, i, report.createParagraph());
        }
		
		
		fundGroupHeading.add(1, 0, report.createParagraph()
				.setFontSize(9).setFontWeight("bold")
				.add(report.createText("Market Indexes (comparison purposes only)")
						.setColor(PdfColors.WHITE)));
		fundGroupHeading.setCellVerticalAlign(1, 0, "center");
		fundGroupHeading.setCellBackgroundColor(1, 0, COLOUR_NAVY_BLUE);
		fundGroupHeading.setCellMarginLeft(1, 0, "0.1in");
		fundGroupHeading.setCellBorderBottomStyle(1, 0, "solid");
		fundGroupHeading.setBorderColor(COLOUR_NAVY_BLUE);
		fundGroupHeading.setCellBorderBottomWidth(1, 0, "0.01pt");
		fundGroupHeading.spanCells(1, 0, 1, reportDefinition.getNumberOfColumns());
	
		//to stop the bleeding of shading from first fund row
		fundGroupHeading.setTablePaddingAfter("1.1pt");
		
		return fundGroupHeading;
	}

	private PdfTableBody buildMarketIndexTableBody(StandardReportBuilder report, List funds) {
		List indexes = new ArrayList();
		for (Iterator iter = funds.iterator(); iter.hasNext();) {
			ReportFund fund = (ReportFund) iter.next();
			if (fund.getFund().isMarketIndex()) {
				indexes.add(fund);
			}
		}
		
		//don't really need Morningstar info for indexes...
		return  buildStyleBoxTableBody(report, indexes, new HashMap());
	}
	
}
