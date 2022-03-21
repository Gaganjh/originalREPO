/**
 * 
 */
package com.manulife.pension.ireports.report.isf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;

import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.streamingreport.impl.TextStyle;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.util.pdf.BasePdfReport;
import com.manulife.util.pdf.PdfConstants;
import com.manulife.util.pdf.PdfGraphic;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableRegion;

public class AssetClassBlock {

	private static final String BACKGROUND_COLOR_GRAY = "#D3D3D3";

	private TextStyle assetClassHeadingStyle = new TextStyle(null, null, -1, TextStyle.WEIGHT_BOLD, false);

	String assetClassName;
	private ISFGenerator generator;

	/** List of FundLine */
	List lines;


	public AssetClassBlock(ISFGenerator generator, String heading, List fundsInBlock, List fundsChosen) {
		this.generator = generator;
		this.assetClassName = heading;
		this.lines = calculateLines(fundsInBlock, fundsChosen);
	}
	
	private List calculateLines(List fundsInBlock, List fundsChosen) {
		if (fundsInBlock.isEmpty()) {
			throw new IllegalArgumentException("Asset blocks must be empty");
		}
		List result = new ArrayList();
		Collection<String> fundSuites = new ArrayList<String> ();
		
		for (Iterator iter = fundsInBlock.iterator(); iter.hasNext();) {
			Fund fund = ((ReportFund) iter.next()).getFund();
			boolean fundSelected = fundsChosen.contains(fund.getInvestmentid());

			if (fund.isLifecycle()) {
				boolean hasSuite = CollectionUtils.exists(fundSuites, 
            			PredicateUtils.equalPredicate(fund.getFundFamilyCd()));
				if(!hasSuite){
					fundSuites.add(fund.getFundFamilyCd());
					result.add(FundLine.makeLifecycleFund(fundSelected,
							generator.messages, fund.getFundFamilyCd()));
				}
				
				if(fundSuites.size() == 2){ 
					break;
				}else{
					continue;
				}
			}
			
			FundLine line = FundLine.make(fund, fundSelected, generator.messages);
			result.add(line);
		}
		return result;
	}

	public PdfParagraph makeBlock() {

		PdfParagraph result = getReport().createParagraph();

		PdfTableRegion tableHeader = getReport().createTableHeader(1, 3);
		PdfParagraph header = assetClassHeadingStyle.createParagraph(getReport(), assetClassName);
		tableHeader.add(0, 0, header).spanCells(0, 0, 1, 3);
		tableHeader.setBorderBottom(ISFGenerator.MAIN_BORDER);

		PdfTableBody tableBody = getReport().createTableBody(lines.size(), 3);
		int row = 0;
        int numOfRows = 0;
        if (lines != null) {
            numOfRows = lines.size() - 1;
        }
		
		for (Iterator iter = lines.iterator(); iter.hasNext(); ++row) {
			FundLine line = (FundLine) iter.next();
			String investmentId = line.getInvestmentid();
			tableBody.add(row, 0, investmentId);

			PdfGraphic checkbox = generator.imageBuilder.buildCheckbox(line.isChecked());

			tableBody.add(row, 1, getReport().createParagraph(checkbox)).setColumnPaddingLeft(1, "0.08in");

			PdfParagraph name = getReport().createParagraph(line.getFundLongName());
			
			tableBody.add(row, 2, name);

			PdfParagraph addition = assetClassHeadingStyle.createParagraph(getReport(), "");
			String extraDescription = line.getFundExtraDescription();
			if (extraDescription != null) {
			    name.setKeepWithNext();
			    PdfParagraph description = assetClassHeadingStyle.createParagraph(getReport(), extraDescription);
				addition.add(description);
			}
			
			if (line.isLifecycle) {
				String extraDescriptionLC = line.getFundExtraDescriptionLC();
				if (extraDescriptionLC != null) {
					PdfParagraph LC = assetClassHeadingStyle.createParagraph(getReport(), extraDescriptionLC);
					addition.add(LC);
				}
			}
			if(extraDescription != null || line.isLifecycle) {
				tableBody.add(row, 2, addition).setCellBackgroundColor(row, 2, BACKGROUND_COLOR_GRAY);				
			}
			if (numOfRows != row) {
                tableBody.setRowKeepWithNext(row);
            } else {
                tableBody.setRowKeepWithPrevious(row);
            }
			tableBody.setCellBorderRightStyle(row, 0, PdfConstants.BORDER_STYLE_SOLID);
		}

		if (row > 0) {
			tableBody.setColumnTextAlign(0, PdfConstants.ALIGNMENT_CENTER);
			tableBody.setColumnTextAlign(1, PdfConstants.ALIGNMENT_CENTER);
			tableBody.setElementAttribute("border-top", ISFGenerator.MAIN_BORDER);
			tableBody.setElementAttribute("border-left", ISFGenerator.MAIN_BORDER);
			tableBody.setElementAttribute("border-right", ISFGenerator.MAIN_BORDER);
			tableBody.setBorderBottom(ISFGenerator.MAIN_BORDER);
		}

		PdfTable table = getReport().createTable(new String[] { "0.5in", "0.3in", "proportional-column-width(10)" },
				new PdfTableRegion[] { tableHeader, tableBody });
		table.setElementAttribute("width", "100%");
		//	  So cells don't have borders too wide. It will merge the cell borders.
		table.setElementAttribute("border-collapse", "collapse"); 
		result.add(table);
		result.setPaddingBottom("0.25in");
		return result;
	}

	private BasePdfReport getReport() {
		return generator.report;
	}
}