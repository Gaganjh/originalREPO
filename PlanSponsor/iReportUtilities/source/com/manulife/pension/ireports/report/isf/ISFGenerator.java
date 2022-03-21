package com.manulife.pension.ireports.report.isf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.dao.ReportDataRepository;
import com.manulife.pension.ireports.dao.ReportDataRepositoryFactory;
import com.manulife.pension.ireports.dao.ReportDataTransformer;
import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.ireports.model.report.AssetClassReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.PDFConstants;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.ireports.report.streamingreport.impl.TextStyle;
import com.manulife.pension.ireports.report.util.ImageBuilder;
import com.manulife.pension.ireports.report.util.ReportStrings;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetClass;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.util.pdf.PdfConstants;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfGraphic;
import com.manulife.util.pdf.PdfPageSequence;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfStaticContent;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableRegion;

public class ISFGenerator {
	// formatting constants
	static final String MAIN_BORDER = "2pt solid #DFDFDF";
	private static final String SMALL_BORDER_BLACK = "1pt solid black";
	private static final String AUTH_PADDING = "0.02in";

	// First page only Selection Form
	private static final String SEL_INITIAL = "Initial Fund Selection - ";
	private static final String SEL_ENROL = "Enrollment forms giving participant investment direction must accompany the initial contribution.";
	private static final String SEL_CHECK = "Check (";
	private static final String SEL_INV_OPTIONS1 = ") investment options to be included in the group annuity contract.";
	private static final String SEL_ADD = "Add Funds Later - ";
	private static final String SEL_INV_OPTIONS2 = ") investment options to be added later.";
	private static final String SEL_PLEASE_NOTE = "Please Note:";

	// Authorization Section
	private static final String AUTH_AUTHORIZATION = "Authorization";
	private static final String AUTH_SIGNED_AT = "Signed at";
	private static final String AUTH_CITY = "City";
	private static final String AUTH_STATE = "State";
	private static final String AUTH_THIS = "This";
	private static final String AUTH_DAY_OF = "Day of";
	private static final String AUTH_YEAR = "Year";
	private static final String AUTH_SIGNATURE = "Signature of trustee/authorized named fiduciary";
	private static final String AUTH_NAME = "Name";

	private ReportDataRepository repository = ReportDataRepositoryFactory.getRepository();
	StandardReportBuilder report;
	
	TextStyle assetClassHeadingStyle;
	private TextStyle authorizationTextStyle;
	private TextStyle selectDescriptionStyle;
	
	FundOffering fundOffering;
	ImageBuilder imageBuilder;
	ReportStrings messages;
	private String[] fundsChosen;
	private String contractNumber;
	
	public static ISFGenerator makeISFGenerator(ReportOptions options, StandardReportBuilder report) {
		String contractNumber = "";
		if (options.getContractShortlistOptions().isContract()) {
			contractNumber = options.getContractShortlistOptions().getContractNumber();
		}
		return new ISFGenerator(options.getFundOffering(), options.getFundsChosen(),
                contractNumber, report, new ImageBuilder(report));
	}
	
	private ISFGenerator(FundOffering fundOffering, String[] fundsChosen, String contractNumber,
            StandardReportBuilder report, ImageBuilder imageBuilder) {
		this.report = report;
		this.fundsChosen = new String[]{};  //fundsChosen; TODO Until the business decides how this will be handled, no funds will be pre-selected on the form
		this.contractNumber = contractNumber;
		this.fundOffering = makeISFOffering(fundOffering);
		this.imageBuilder = imageBuilder;

		this.assetClassHeadingStyle = new TextStyle(report, null, -1, TextStyle.WEIGHT_BOLD, false);
		this.authorizationTextStyle = new TextStyle(report, "Frutiger", 7, TextStyle.WEIGHT_NORMAL, true);
		this.selectDescriptionStyle = new TextStyle(report, "Frutiger", 8, TextStyle.WEIGHT_BOLD, false);
		this.messages	= new ReportStrings("isf.", StandardReportsUtils.companyIdToSitecode(fundOffering.getCompanyId()));
}

	/**
	 * Set the fund_menu to ALL.  The ISF always includes all funds.  The only choices are NML and USA/NY
	 */
	private static FundOffering makeISFOffering(FundOffering selectedOffering) {
		return new FundOffering(selectedOffering.getCompanyId(), StandardReportsConstants.FUND_MENU_ALL, selectedOffering.isIncludeNML());
	}

	public void add() {
		PdfPageSequence pageSequence = report.createPageSequence(PDFConstants.MASTER_SEQUENCE_ISFCONTENT);
		pageSequence.setElementAttribute("initial-page-number", "1");
		report.add(pageSequence);
		
		// Create headers and footers
		pageSequence.add(createFirstHeaderContent(report));
		pageSequence.add(createSubsequentHeaderContent(report));
		pageSequence.add(createFooterContent(report));
		pageSequence.add(createISFBody(report));
	}
	
	private PdfFlow createISFBody(StandardReportBuilder report) {
		PdfFlow flow = report.createFlow(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
		
		AssetClassReportData assetClassFunds = repository.getAssetClassReportData();
		Map assetClasses = assetClassFunds.getAssetClasses();
		
		for (Iterator iter = assetClasses.entrySet().iterator(); iter.hasNext();) {
			Entry element = (Entry) iter.next();
			AssetClass assetClass = (AssetClass) element.getValue();
			Map fundsInAssetClass = null;
			//TODO remove this code when GIFL funds are required to display in ISF 
			if(StandardReportsConstants.ASSET_CLASS_GIFL.equals(assetClass.getAssetcls())){
				continue;
			}
			//FFL 2012: Skip the GA section, no need to include it in ISF
			if("GA3".equals(assetClass.getAssetcls())	) {
				// retrieve all guaranteed accounts
				// fundsInAssetClass = repository.getFunds(fundOffering, (String)element.getKey());
				
				// Add GIC 5YR
				element = (Entry) iter.next();
				// assetClass = (AssetClass) element.getValue();
				// fundsInAssetClass.putAll(repository.getFunds(fundOffering, (String)element.getKey()));
				
				// Add GIC 10YR
				element = (Entry) iter.next();
				// assetClass = (AssetClass) element.getValue();
				// fundsInAssetClass.putAll(repository.getFunds(fundOffering, (String)element.getKey()));
				// assetClass.setAssetclsDesc("Guaranteed Accounts");
			} else {
				fundsInAssetClass = repository.getFunds(fundOffering, (String)element.getKey());
			}
			if(fundsInAssetClass != null && !fundsInAssetClass.isEmpty()) {
				List sortedFundsInAssetClass = new ArrayList();
				for (Iterator iterator = fundsInAssetClass.values().iterator(); iterator.hasNext();) {
					sortedFundsInAssetClass.add(new ReportFund(((Fund) iterator.next())));
				}
				Collections.sort(sortedFundsInAssetClass, (Comparator) ReportDataTransformer.getFundComparator(ReportDataRepository.REPORTFUND_SORT_ORDER_RISKRETURN));
	
				AssetClassBlock assetClassBlock = new AssetClassBlock(this, assetClass.getAssetclsDesc(),sortedFundsInAssetClass, Arrays.asList(fundsChosen));
				flow.add(assetClassBlock.makeBlock());
			}
		}
		
		flow.add(makeAuthorizationSection(report));
		return flow;
	}

	private PdfParagraph makeAuthorizationSection(StandardReportBuilder report) {
		PdfParagraph result = report.createParagraph();
		
		// Authorization title box
		PdfTableBody tableBody = report.createTableBody(1, 1);
		tableBody.add(0, 0, assetClassHeadingStyle.createParagraph(report, AUTH_AUTHORIZATION).setFontFamily("Frutiger").setFontSize(9));
		tableBody.setBorderStyle(PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setColumnTextAlign(0, PdfConstants.ALIGNMENT_CENTER);

		PdfTable table = report.createTable(new String[] {"proportional-column-width(10)"}, new PdfTableRegion[] {tableBody});
		table.setElementAttribute("width", "100%");
		table.setElementAttribute("border-collapse", "collapse"); // So cells don't have borders too wide. It will merge the cell borders.

		result.add(table);
		
		// Authorization paragraph
		PdfParagraph authorParagraph = authorizationTextStyle.createParagraph(messages.getString("authorization"));
		authorParagraph.setPaddingTop("0.1in");
		authorParagraph.setPaddingBottom("0.1in");
		 
		result.add(authorParagraph);
		
		// Authorization form fields
		
		// Authorization form fields, row 1 of 2
		PdfParagraph formRow1 = report.createParagraph(); // need paragraph to set padding between tables
		formRow1.setPaddingBottom("0.1in");
		
		tableBody = report.createTableBody(1, 9);
		tableBody.add(0, 0, authorizationTextStyle.createParagraph(AUTH_SIGNED_AT));
		tableBody.add(0, 1, authorizationTextStyle.createParagraph(AUTH_CITY));
		tableBody.add(0, 2, authorizationTextStyle.createParagraph(AUTH_STATE));
        tableBody.add(0, 3, report.createParagraph());
		tableBody.add(0, 4, authorizationTextStyle.createParagraph(AUTH_THIS));
        tableBody.add(0, 5, report.createParagraph());
		tableBody.add(0, 6, authorizationTextStyle.createParagraph(AUTH_DAY_OF));
        tableBody.add(0, 7, report.createParagraph());
		tableBody.add(0, 8, authorizationTextStyle.createParagraph(AUTH_YEAR));
		tableBody.setRowHeight(0, ".3in");
		
		tableBody.setCellBorderLeftStyle(0, 0, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellBorderTopStyle(0, 0, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellBorderTopStyle(0, 1, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellBorderTopStyle(0, 2, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellBorderRightStyle(0, 2, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellBorderBottomStyle(0, 0, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellBorderBottomStyle(0, 1, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellBorderBottomStyle(0, 2, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellPaddingLeft(0, 0, AUTH_PADDING);
		
		tableBody.setCellBorderStyle(0, 4, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellPaddingLeft(0, 4, AUTH_PADDING);
		
		tableBody.setCellBorderStyle(0, 6, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellPaddingLeft(0, 6, AUTH_PADDING);
		
		tableBody.setCellBorderStyle(0, 8, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellPaddingLeft(0, 8, AUTH_PADDING);
		
		table = report.createTable(new String[] {"0.625in", "0.9375in", "0.9375in", "0.3125in", "0.625in", "0.3125in", "2.25in", "0.3125in", "1.1875in" }, new PdfTableRegion[] {tableBody});
		table.setElementAttribute("width", "100%");
		table.setElementAttribute("border-collapse", "collapse"); // So cells don't have borders too wide. It will merge the cell borders.

		formRow1.add(table);

		result.add(formRow1);
		
		// Authorization form fields, row 2 of 2
		tableBody = report.createTableBody(1, 3);
		tableBody.add(0, 0, authorizationTextStyle.createParagraph(AUTH_SIGNATURE));
        tableBody.add(0, 1, report.createParagraph());
		tableBody.add(0, 2, authorizationTextStyle.createParagraph(AUTH_NAME));
		tableBody.setRowHeight(0, ".3in");
		
		tableBody.setCellBorderStyle(0, 0, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellPaddingLeft(0, 0, AUTH_PADDING);
		
		tableBody.setCellBorderStyle(0, 2, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellPaddingLeft(0, 2, AUTH_PADDING);
		
		table = report.createTable(new String[] {"3.4375in", "0.3125in", "3.75in" }, new PdfTableRegion[] {tableBody});
		table.setElementAttribute("width", "100%");
		table.setElementAttribute("border-collapse", "collapse"); // So cells don't have borders too wide. It will merge the cell borders.
		table.setElementAttribute("keep-together.within-page","always");

		result.add(table);

		return result;
	}

	private PdfStaticContent createFirstHeaderContent(StandardReportBuilder report) {
		PdfStaticContent header = createCommonHeader(report, PDFConstants.MASTER_REGION_FIRST_PAGE_HEADER);
		
		PdfTableBody tableBody = report.createTableBody(4, 2);

		// Row 0: Initial Fund Selection
		PdfGraphic checkbox = imageBuilder.buildCheckbox(false);
		tableBody.add(0, 0, report.createParagraph(checkbox));
		
		PdfParagraph addition = selectDescriptionStyle.createParagraph(SEL_INITIAL);
		addition.add(authorizationTextStyle.createText(SEL_ENROL));
		tableBody.add(0, 1, addition);
		
		// Row 1: Check (X) Investment options
		PdfGraphic checkmark = imageBuilder.buildCheckmark();
		tableBody.add(1, 0, report.createParagraph());
		tableBody.add(1, 1, selectDescriptionStyle.createParagraph(SEL_CHECK).add(checkmark).add (SEL_INV_OPTIONS1).setPaddingBottom("0.03in"));

		// Row 2: Add Funds Later
		tableBody.add(2, 0, report.createParagraph(imageBuilder.buildCheckbox(false)));
		addition = selectDescriptionStyle.createParagraph(SEL_ADD);
		addition.add(authorizationTextStyle.createText(messages.getString("header.select_additional")));
		tableBody.add(2, 1, addition);
		
		// Row 3: Check (X) Investment options to be added later
		checkmark = imageBuilder.buildCheckmark();
        tableBody.add(3, 0, report.createParagraph());
		tableBody.add(3, 1, selectDescriptionStyle.createParagraph(SEL_CHECK).add(checkmark).add (SEL_INV_OPTIONS2).setPaddingBottom("0.03in"));
		tableBody.setBorderBottom(MAIN_BORDER);
		
		PdfTable table = report.createTable(new String[] {"0.25in", "7in"}, new PdfTableRegion[] {tableBody});
		addition = report.createParagraph().add(table);
		addition.setPaddingTop("0.03in");
		header.add(addition);

		// Please note paragraph
		header.add(selectDescriptionStyle.createParagraph(SEL_PLEASE_NOTE));
		header.add(authorizationTextStyle.createParagraph(messages.getString("header.please_note_body")));
		
		return header;
	}

	private PdfStaticContent createSubsequentHeaderContent(StandardReportBuilder report) {
		PdfStaticContent header = createCommonHeader(report, PDFConstants.MASTER_REGION_SUBSEQUENT_PAGES_HEADER);
		
		return header;
	}
	
	private PdfStaticContent createCommonHeader(StandardReportBuilder report, String region) {
		PdfStaticContent header = report.createStaticContent(region);

		return createCommonHeader(report, header, contractNumber);
	}

	private PdfStaticContent createCommonHeader(StandardReportBuilder report, PdfStaticContent header, String contractNumber) {
		// Image and Heading table
		PdfTableBody tableBody = report.createTableBody(1, 3);
		tableBody.add(0, 0, report.createParagraph(imageBuilder.buildLogo()));
        tableBody.add(0, 1, report.createParagraph());

		TextStyle headerTitleStyle = new TextStyle(report, "Frutiger", 14, TextStyle.WEIGHT_BOLD, false);
		String headerText = messages.getString(fundOffering.isIncludeNML()?"header.titleNML":"header.title");
		tableBody.add(0, 2, headerTitleStyle.createParagraph(headerText).setTextAlignment(PdfConstants.ALIGNMENT_LEFT));
		
		PdfTable table = report.createTable(new String[] {"2.0in", "0.5in", "4.5in"}, new PdfTableRegion[] {tableBody});
		header.add(report.createParagraph().add(table).setPaddingBottom("0.05in"));
		
		// Add Contract table
		tableBody = report.createTableBody(1, 2);
		
		tableBody.add(0, 0, authorizationTextStyle.createParagraph(messages.getString("header.contract.holder")));
		tableBody.add(0, 1, authorizationTextStyle.createParagraph(messages.getString("header.contract.number")));
		tableBody.add(0, 1, authorizationTextStyle.createParagraph(contractNumber));
		tableBody.setRowHeight(0, ".3in");
		
		tableBody.setCellBorderStyle(0, 0, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellPaddingLeft(0, 0, AUTH_PADDING);
		
		tableBody.setCellBorderStyle(0, 1, PdfConstants.BORDER_STYLE_SOLID);
		tableBody.setCellPaddingLeft(0, 1, AUTH_PADDING);
		
		table = report.createTable(new String[] {"5in", "2.5in" }, new PdfTableRegion[] {tableBody});
		table.setElementAttribute("width", "100%");
		table.setElementAttribute("border-collapse", "collapse"); // So cells don't have borders too wide. It will merge the cell borders.

		header.add(report.createParagraph().add(table).setPaddingBottom(AUTH_PADDING));

		return header;
	}
	
	private PdfStaticContent createFooterContent(StandardReportBuilder report) {
		PdfStaticContent footer = report.createStaticContent("footer");

		PdfParagraph pagePara = report.createParagraph();
		pagePara.add(report.createText("Page "));
		pagePara.add(report.createPageNumber());
		pagePara.setTextAlignment(PdfConstants.ALIGNMENT_RIGHT);
		pagePara.setElementAttribute("border-bottom", SMALL_BORDER_BLACK);
		pagePara.setMarginBottom("0.2in");
		footer.add(pagePara);

		footer.add(pagePara = authorizationTextStyle.createParagraph(getCopyright() + (messages.getString("footer.copyright")))); 

		return footer;
	}
	
	private String getCopyright() {
		Calendar cal = Calendar.getInstance();
		String copyright = "©  " + cal.get(Calendar.YEAR);
		return copyright;
	}
}
