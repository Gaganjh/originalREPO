package com.manulife.pension.ireports.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ireports.dao.Footnote;
import com.manulife.pension.ireports.dao.ReportDataRepository;
import com.manulife.pension.ireports.dao.ReportDataRepositoryFactory;
import com.manulife.pension.ireports.dao.StaticFootnoteDAO;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.viewbean.InvestmentGroupViewBean;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.pension.platform.utility.CommonConstants;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableHeader;
import com.manulife.util.pdf.PdfTableRegion;

public abstract class AbstractRiskReturnReport  extends StandardReportTemplate {
	protected Date asOfDate;
	protected FundReportData reportData;
	protected ReportDefinition reportDefinition;
	
	public AbstractRiskReturnReport(ReportOptions options, ReportDefinition reportDefinition) {
        super(options);
		this.reportDefinition = reportDefinition;
		setLandscape(reportDefinition.isLandscape());
		setLegalSize(reportDefinition.isLegalSize());
	}

	protected abstract void definePageSequence(StandardReportBuilder report,  PdfFlow flow, FundReportData reportData);
	
	/**
	 * Build the PDF content
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
		PdfParagraph tableParagraph = report.createParagraph().setMargin("0.0cm").setTextAlignment("end").setSpan("all");
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
		
		PdfTable mainTable = report.createTable(reportDefinition.getColumnWidths(), new PdfTableRegion[]{mainTableHeader, mainTableBody});
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
	
	protected PdfTableBody buildMainTableBody(StandardReportBuilder report ) {
	    
		int numberOfBodyRows = options.getFundsChosen().length+1;
		int rows = numberOfBodyRows * 2 + 1; // We have a lastRow where we show the Average Expense
                                         // Ratio.
        int cols = reportDefinition.getNumberOfColumns();
		
		PdfTableBody mainTableBody = report.createTableBody(rows, cols);

		Collection funds = reportData.getFunds();
		
		List investmentGroupsWithMarketIndex = collateFundsIntoInvestmentGroups(funds, reportData.getInvestmentGroups());		

		List investmentGroups = new ArrayList();
        for (Iterator groupIterator = investmentGroupsWithMarketIndex.iterator(); groupIterator.hasNext();) {
            InvestmentGroupViewBean investmentGroup = (InvestmentGroupViewBean) groupIterator.next();
            // If report doesn't have Market Index information, and we're in the Market Index Group,
            // dont show that group.
            if (!reportDefinition.containsMarketIndexSectionRiskReturn()
                    && investmentGroup.isMarketIndex()) {
                continue;
            }
            investmentGroups.add(investmentGroup);
        }

        Map<String, String> morningstarFootNoteMap = new LinkedMap();
        int i = 0;
		for (Iterator groupIterator = investmentGroups.iterator(); groupIterator.hasNext();) {
			
			InvestmentGroupViewBean investmentGroup = (InvestmentGroupViewBean) groupIterator.next();
			// If report doesn't have Market Index information, and we're in the Market Index Group,
			// go to the next Group
			if(!reportDefinition.containsMarketIndexSectionRiskReturn() && investmentGroup.isMarketIndex()) { 
				continue;
			}

			List fundsInGroup = new ArrayList(investmentGroup.getFunds());
			if(shouldAddGICFunds(investmentGroup)) {
				fundsInGroup.addAll(investmentGroup.getGICFunds());
			}
			
			int numberOfFundsInGroup = fundsInGroup.size();
			
			ReportFund reportFund = null;
			for (Iterator iter = fundsInGroup.iterator(); iter.hasNext();) {
	            reportFund = (ReportFund) iter.next();
	            if (reportFund.getFundStandardDeviation() != null) {
		            if (reportFund.getFundStandardDeviation().getFundDisclosureText() != null &&
		            	reportDefinition.isShowMMFDisclosureText()) {
		            	numberOfFundsInGroup++;
		            }
	            }
			}
			
			if (numberOfFundsInGroup == 0) {
                continue;
            }
			
			PdfTableBody fundTableBody = report.createTableBody(numberOfFundsInGroup, cols);
			reportDefinition.justifyReportBodyColumns(fundTableBody);
		
			for(int j = 0; j < numberOfFundsInGroup; j++) {
				reportDefinition.setCellFormattingForRow(fundTableBody, j, false);
			}
			
			PdfTableHeader fundGroupHeading = buildFundGroupHeading(report, reportData, investmentGroup, isNewYork);

			//this logic is for keeping rows together on one page
			int row = 0;
			int rowNumberKeepTogetherFirst = (new Integer(PropertyManager.getString("row.number.keep.together"))).intValue();
			int rowNumberKeepTogetherLast = 0;
			if (numberOfFundsInGroup >= rowNumberKeepTogetherFirst) {
				rowNumberKeepTogetherLast = numberOfFundsInGroup - rowNumberKeepTogetherFirst;
			}
			String fundDisclosureText = null;
			int ind = 0;
			for(Iterator iter = fundsInGroup.iterator(); iter.hasNext();) {
				reportFund = (ReportFund) iter.next();
				
				if (reportFund.getFundStandardDeviation() != null) {
					fundDisclosureText = reportFund.getFundStandardDeviation().getFundDisclosureText();
				}

				reportDefinition.buildFundRow(report, fundTableBody, row, reportFund, reportData);
				
				int k=0;
				if (ind == 0) {
					for(Iterator iterator = fundsInGroup.iterator(); iterator.hasNext();) {	
						ReportFund reportFundForIterate = (ReportFund) iterator.next();
						for (int col=0; col<reportDefinition.getNumberOfColumns(); col++) {
							if (!"MMR".equals(reportFundForIterate.getFund().getInvestmentid())) {
								fundTableBody.setCellBorderBottomStyle(k, col, "solid");
								fundTableBody.setCellBorderBottomColor(k, col, PDF_RGB_ROW_BORDER);
								fundTableBody.setCellBorderBottomWidth(k, col, "0.5pt");
								
							}
							else if ("MMR".equals(reportFundForIterate.getFund().getInvestmentid()) &&
									!reportDefinition.isShowMMFDisclosureText()) {
								fundTableBody.setCellBorderBottomStyle(k, col, "solid");
								fundTableBody.setCellBorderBottomColor(k, col, PDF_RGB_ROW_BORDER);
								fundTableBody.setCellBorderBottomWidth(k, col, "0.5pt");
							}
						}
						k++;
					}
				}
				ind++;
				
				//formatting / keeping rows together
				fundTableBody.setRowKeepTogetherWithinPage(row);

				if (row < rowNumberKeepTogetherFirst-1 || row >= rowNumberKeepTogetherLast) {
					fundTableBody.setRowKeepWithNext(row);
				}
				
				if (fundDisclosureText != null && reportDefinition.isShowMMFDisclosureText()) {
					fundTableBody = reportDefinition.buildDisclosureTextRow(report, fundTableBody, ++row,
							reportDefinition.getNumberOfColumns(), reportFund);
				}
				
				Footnote footNote = buildMorningstarFootNotes(reportFund,
						reportData, options);

				if (footNote != null) {
					morningstarFootNoteMap.put(reportFund.getFund()
							.getInvestmentid(), footNote.getText());
				}

				row++;
			}

			fundTableBody.setPaddingBefore("0.1cm");
			
			PdfTableRegion[] fundTableRegions = new PdfTableRegion[]{
				fundGroupHeading, fundTableBody
			};
			PdfTable fundTable = report.createTable(reportDefinition.getColumnWidths(), fundTableRegions);
			mainTableBody.spanCells(i, 0, 1, cols);
			mainTableBody.add(i++, 0, fundTable);
			if (groupIterator.hasNext()) {
                mainTableBody.add(i, 0, report.createParagraph()).setRowHeight(i, "0.3in");
                reportDefinition.setCellFormattingForRow(mainTableBody, i, true);
				for (int j = 0; j < cols; j++) {
                    mainTableBody.add(i, j, "");
                }
				i++;
			}
		}

		reportData.setMorningstarFootNotes(morningstarFootNoteMap);
		
	    PdfTable spacer = buildSpacerTable(report);
        mainTableBody.add(i, 0, spacer);
        for (int col = 1; col < reportDefinition.getNumberOfColumns(); col++) {
            mainTableBody.add(i, col, report.createParagraph());
        }
        i++;
        PdfTable lastRowTable = reportDefinition.buildLastRow(report, 0, reportData);
        if (lastRowTable != null) {
            mainTableBody.add(i, 0, lastRowTable);
            for (int col = 1; col < reportDefinition.getNumberOfColumns(); col++) {
                mainTableBody.add(i, col, report.createParagraph());
            }
        }
		
        /*
         * initializing the unused table cells
         */
        for (; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mainTableBody.add(i, j, report.createParagraph());
            }
        }
		
		return mainTableBody;
	}
	
	/**
	 * Builds Morningstar Foot Notes
	 * 
	 * @param reportFund
	 * @param reportData
	 * @param options
	 * 
	 * @return Footnote
	 */
	@SuppressWarnings("unchecked")
	private static Footnote buildMorningstarFootNotes(ReportFund reportFund,
			FundReportData reportData, ReportOptions options) {

		Map<String, MorningstarCategoryPerformance> morningstarCategoryPerformances = reportData
				.getMorningstarCategoryPerformances();
		String companyId = options.getCompanyId();
		Footnote footNote = null;
		if (morningstarCategoryPerformances != null) {
			StaticFootnoteDAO staticFootnoteDAO = null;
			MorningstarCategoryPerformance morningstar = (MorningstarCategoryPerformance) morningstarCategoryPerformances
					.get(reportFund.getFund().getInvestmentid());

			if (morningstar != null
					&& StringUtils.isNotBlank(reportFund.getFund()
							.getMorningstarRating())) {
				Map<String, List<String>> morningstarFootNoteMap = morningstar
						.getMorningstarFootNoteMap();
				if (morningstarFootNoteMap != null) {
					List<String> morningstarFootNoteList = morningstarFootNoteMap
							.get(reportFund.getFund().getInvestmentid());
					staticFootnoteDAO = new StaticFootnoteDAO(companyId);
					if (morningstarFootNoteList != null) {
						String[] contentParams = (String[]) morningstarFootNoteList
								.toArray(new String[0]);
						// Replacing the fund name with long name to show in i:reports
						contentParams[0] = reportFund.getFund().getFundLongName();
						footNote = staticFootnoteDAO
								.retrieveFootnoteWithParams(
										Integer.toString(morningstar
												.getMorningstarFootNoteCMAId()),
										contentParams);
					}
				}
			}
		}
		return footNote;
	}

	protected PdfTableHeader buildFundGroupHeading(StandardReportBuilder report, FundReportData reportData, InvestmentGroupViewBean investmentGroup, boolean isNewYork) {
		int numberOfHeaderRows = 3;

		PdfTableHeader fundsTableHeader;
		
		int numberOfColumns = reportDefinition.getNumberOfColumns();
		
		if (shouldBuildGicRows(investmentGroup)) {
			// +1 to numberOfHeaderRows for GIC information
            fundsTableHeader = report.createTableHeader(numberOfHeaderRows + 2, numberOfColumns);
            for (int j = 1; j < numberOfColumns; j++) {
                fundsTableHeader.add(3, j, report.createParagraph());
            }
		} else {
			fundsTableHeader = report.createTableHeader(numberOfHeaderRows, numberOfColumns);
		}

        /*
         * Initialize the table cells to empty
         */
		if (shouldBuildGicRows(investmentGroup)) {
            for (int i = 0; i < numberOfHeaderRows + 2; i++) {
                for (int j = 1; j < numberOfColumns; j++) {
                    fundsTableHeader.add(i, j, report.createParagraph());
                }
            }
        } else {
            for (int i = 0; i < numberOfHeaderRows; i++) {
                for (int j = 1; j < numberOfColumns; j++) {
                    fundsTableHeader.add(i, j, report.createParagraph());
                }
            }
        }
		
		if (shouldBuildGicRows(investmentGroup)) {
			int annualRateColumn = reportDefinition.getGicSectionAnnualRateColumn();
			
			for(int j = annualRateColumn; j < reportDefinition.getNumberOfColumns(); j++) {
				fundsTableHeader.setColumnTextAlign(j, "right");
			}
		}
		
		fundsTableHeader.add(0, 0, report.createParagraph()).setRowHeight(0, "1.1pt"); //stops color from row 1 from overlapping
		fundsTableHeader.add(1, 0, report.createParagraph("  "));
		fundsTableHeader.add(2, 0, report.createParagraph()
				.setFontSize(FONT_SIZE_FUND_GROUP_HEADING).setFontWeight("bold")
				.add(report.createText(investmentGroup.getGroupname()).setColor(investmentGroup.getFontcolor().toLowerCase())));
		fundsTableHeader.setCellVerticalAlign(2, 0, "center");
		fundsTableHeader.setCellBackgroundColor(2,0,investmentGroup.getColorcode());
		fundsTableHeader.setCellMarginLeft(2, 0, "0.1in");
		fundsTableHeader.setCellBorderBottomStyle(2, 0, "solid");
		fundsTableHeader.setBorderColor(investmentGroup.getColorcode());
		fundsTableHeader.setCellBorderBottomWidth(2	, 0, "0.01pt");
		fundsTableHeader.spanCells(2, 0, 1, reportDefinition.getNumberOfColumns());
		
		for (int i = 1; i < numberOfHeaderRows; i++) {
			reportDefinition.setCellFormattingForRow(fundsTableHeader, i, false);
		}
		
		if (shouldBuildGicRows(investmentGroup)) {
			fundsTableHeader.add(3, 0, report.createParagraph()).setRowHeight(3, "1.1pt"); //stops color from row 4 from overlapping
			PdfTable gicTable = buildGicSection(report, reportDefinition, reportData, investmentGroup.getGICFunds(), GIC_RISKRETURN);
			fundsTableHeader.add(4,0,gicTable);
			fundsTableHeader.spanCells(4, 0, 1, reportDefinition.getNumberOfColumns());
		} else {
			fundsTableHeader.setTablePaddingAfter("1.1pt");
		}
		
		return fundsTableHeader;
	}

	// Risk/Return needs to 'manually' add GIC Funds to list of funds in Conservative group if the report doesn't contain
	// the specially formatted Guaranteed Accounts section
	protected boolean shouldAddGICFunds(InvestmentGroupViewBean investmentGroup) {
		return !reportDefinition.containsGicSection()   
		&& investmentGroup.isConservative()
		&& investmentGroup.hasGICs();
	}
	
	protected boolean shouldBuildGicRows(InvestmentGroupViewBean investmentGroup) {
		return reportDefinition.containsGicSection()   
		&& investmentGroup.isConservative()
		&& investmentGroup.hasGICs();
	}

    protected boolean containsGuaranteedAccountRates(FundReportData reportData) {
        return reportData.getGuaranteedAccountRates() != null
                && !reportData.getGuaranteedAccountRates().isEmpty();
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
	
}
