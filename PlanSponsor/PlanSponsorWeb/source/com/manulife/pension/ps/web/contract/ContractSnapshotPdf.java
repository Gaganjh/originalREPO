package com.manulife.pension.ps.web.contract;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.mail.URLName;

import org.apache.log4j.Logger;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.valueobject.ContractSnapshotVO;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.barchart.DataSeries;
import com.manulife.util.barchart.taglib.BarChartBean;
import com.manulife.util.barchart.taglib.ImageUrl;
import com.manulife.util.pdf.PdfConstants;
import com.manulife.util.pdf.PdfGraphic;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfReport;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableRegion;
import com.manulife.util.pdf.PdfText;
import com.manulife.util.piechart.PieChartBean;
import com.manulife.util.piechart.PieChartUtil;
import com.manulife.util.render.NumberRender;

/**
 * This class generates the contract snapshot PDF using the PDF framework.
 * 
 * @author Charles Chan
 */
public class ContractSnapshotPdf {

	private static final Logger logger = Logger
			.getLogger(ContractSnapshotPdf.class);

    private static final String DEFAULT_VALUE = "0.000";
    
    private static final String AMT_PATTERN = "##0.000";

    private String stringDate;
	private boolean displayLoan;
	private boolean displayPba;
	private boolean displayLifecycle;
	private boolean isRecentDate;
	private boolean showBlendedAssetCharge;
	private URLName requestUrlName;
	private ContractSnapshotVO vo;
	private BarChartBean assetGrowthBarChartBean;
	private BarChartBean contributionWithdrawalsBarChartBean;
	private PieChartBean participantStatusPieChartBean;
	private PieChartBean assetAllocByRiskPieChartBean;
	private PieChartBean assetAllocBelow30PieChartBean;
	private PieChartBean assetAllocBetween30And39PieChartBean;
	private PieChartBean assetAllocBetween40And49PieChartBean;
	private PieChartBean assetAllocBetween50And59PieChartBean;
	private PieChartBean assetAllocBetween60And69PieChartBean;
	private LayoutBean layoutBean;
	private LayoutPage layoutPageBean;
	private UserProfile userProfile;
	private Content contractSnapshotLayoutBean;
	private Content generalPBAFootnoteBean;
	private Content generalNoPBAFootnoteBean;
	private Content generalDefinedBenefitFootnoteBean;
	private Content giflMessageBean;
	

	private static boolean httpsFactoryDefined;
	private PdfReport report;

	public ContractSnapshotPdf(String stringDate, boolean displayLoan,
			boolean displayPba, boolean displayLifecycle, boolean isRecentDate,boolean showBlendedAssetCharge,
			UserProfile userProfile, URLName requestUrlName,
			LayoutBean layoutBean, ContractSnapshotVO vo,
			BarChartBean assetGrowthBarChartBean,
			BarChartBean contributionWithdrawalsBarChartBean,
			PieChartBean participantStatusPieChartBean,
			PieChartBean assetAllocByRiskPieChartBean,
			PieChartBean assetAllocBelow30PieChartBean,
			PieChartBean assetAllocBetween30And39PieChartBean,
			PieChartBean assetAllocBetween40And49PieChartBean,
			PieChartBean assetAllocBetween50And59PieChartBean,
			PieChartBean assetAllocBetween60And69PieChartBean)
			throws ContentException {

		this.stringDate = stringDate;
		this.displayLoan = displayLoan;
		this.displayPba = displayPba;
		this.displayLifecycle = displayLifecycle;
		this.isRecentDate = isRecentDate;
		this.showBlendedAssetCharge = showBlendedAssetCharge;
		this.userProfile = userProfile;
		this.requestUrlName = requestUrlName;
		this.layoutBean = layoutBean;
		this.layoutPageBean = layoutBean.getLayoutPageBean();
		this.vo = vo;
		this.assetGrowthBarChartBean = assetGrowthBarChartBean;
		this.contributionWithdrawalsBarChartBean = contributionWithdrawalsBarChartBean;
		this.participantStatusPieChartBean = participantStatusPieChartBean;
		this.assetAllocByRiskPieChartBean = assetAllocByRiskPieChartBean;
		this.assetAllocBelow30PieChartBean = assetAllocBelow30PieChartBean;
		this.assetAllocBetween30And39PieChartBean = assetAllocBetween30And39PieChartBean;
		this.assetAllocBetween40And49PieChartBean = assetAllocBetween40And49PieChartBean;
		this.assetAllocBetween50And59PieChartBean = assetAllocBetween50And59PieChartBean;
		this.assetAllocBetween60And69PieChartBean = assetAllocBetween60And69PieChartBean;

		// This is the parent of all the PdfElements
		report = new PdfReport().setFontFamily("sans-serif").setFontSize(8);
		contractSnapshotLayoutBean = ContentCacheManager.getInstance()
				.getContentById(ContentConstants.CONTRACT_SNAPSHOT_LAYOUT_PAGE,
						ContentTypeManager.instance().LAYOUT_PAGE);

		generalPBAFootnoteBean = ContentCacheManager
				.getInstance()
				.getContentById(
						ContentConstants.FIXED_FOOTNOTE_CONTRACT_SNAPSHOT_PBA_PDF,
						ContentTypeManager.instance().PAGE_FOOTNOTE);

		generalNoPBAFootnoteBean = ContentCacheManager
				.getInstance()
				.getContentById(
						ContentConstants.FIXED_FOOTNOTE_CONTRACT_SNAPSHOT_NOPBA_PDF,
						ContentTypeManager.instance().PAGE_FOOTNOTE);

		generalDefinedBenefitFootnoteBean = ContentCacheManager
				.getInstance()
				.getContentById(
						ContentConstants.FIXED_FOOTNOTE_CONTRACT_SNAPSHOT_DEFINED_BENEFIT_PDF,
						ContentTypeManager.instance().PAGE_FOOTNOTE);
		
		giflMessageBean = ContentCacheManager
			.getInstance()
			.getContentById(ContentConstants.CONTRACT_SNAPSHOT_GIFL_MESSAGE,
							ContentTypeManager.instance().MISCELLANEOUS);

	}

	/**
	 * Writes the PDF content to the output stream.
	 * 
	 * @param out
	 *            The output stream to write to.
	 * @throws IOException
	 */
	public void writeTo(OutputStream out) throws IOException {

		boolean definedBenefit = userProfile.getCurrentContract()
				.isDefinedBenefitContract();

		/*
		 * Manulife Logo Picture.
		 */
		PdfGraphic logoGraph = getStaticGraphic(Constants.UNMANAGED_IMAGE_FILE_PREFIX
				+ (Constants.SITEMODE_USA.equals(Environment.getInstance()
						.getSiteLocation()) ? "JH_blue_resized.gif"
						: "JH_blue_resized_NY.gif"));
		report.add(report.createParagraph().add(logoGraph));

		/*
		 * Company Name and Contract Number.
		 */
		PdfParagraph contractName = report.createParagraph().setMarginLeft(
				"0.0cm").setPaddingBottom("0.37cm");
		contractName.add(report.createText(
				userProfile.getCurrentContract().getCompanyName().trim()
						+ " | ").setFontWeight("bold"));
		contractName.add("Contract number: "
				+ userProfile.getCurrentContract().getContractNumber());

		report.add(contractName);

		report.add(report.createParagraph().add(
				getStaticGraphic(
						Constants.UNMANAGED_IMAGE_FILE_PREFIX
								+ "contract_snapshot.gif").setHeight("0.8cm")));

		/*
		 * An overview of your plan's participant & asset growth over the last
		 * 24 months
		 */
		report.add(report.createParagraph().setMargin("0.0cm").setFontSize(9)
				.setPaddingTop("0.3cm").setPaddingBottom("0.3cm").add(
						ContentUtility.getContentAttribute(layoutPageBean,
								"introduction2")));
		/*
		 * Intro 1
		 */
		String intro1 = ContentUtility.getPageIntroduction(layoutPageBean);
		if (intro1 != null && intro1.length() > 0)
			report.add(report.createParagraph().setMargin("0.0cm").setFontSize(
					9).setPaddingTop("0.3cm").setPaddingBottom("0.3cm").add(
					intro1));

		if (userProfile.getContractProfile().getContract()
				.hasRothNoExpiryCheck()) {

			report
					.add(report
							.createParagraph()
							.setMargin("0.0cm")
							.setFontSize(9)
							.setPaddingTop("0.3cm")
							.setPaddingBottom("0.3cm")
							.add(
									"Assets and contributions below include the value of the participant's Roth 401(k) account."));

		}

		// GIFL-1C
		// To show GIFL message. The text is hardcoded since the text in content
		// database contains
		// tags like <p> and </p>
		if (userProfile.getCurrentContract().getHasContractGatewayInd()) {
			report
					.add(report
							.createParagraph()
							.setMargin("0.0cm")
							.setFontSize(9)
							.setPaddingTop("0.3cm")
							.setPaddingBottom("0.3cm")
							.add( 
									ContentUtility.getContentAttribute(giflMessageBean,"text")));
		}

		PdfParagraph greyBorderParagraph = report.createParagraph()
				.setBorderColor("#002D62").setBorderStyle(
						PdfConstants.BORDER_STYLE_SOLID).setMargin("0.0cm");
		report.add(greyBorderParagraph);

		PdfTableBody tableBody = null;
		if (!definedBenefit) {
			tableBody = report.createTableBody(9, 2);
		} else {
			tableBody = report.createTableBody(7, 2);
		}
		tableBody.setBorderColor("#CCCCCC").setBorderWidth("1px");
		PdfTable mainTable = report.createTable(
				new String[] { "9.5cm", "9.5cm" },
				new PdfTableRegion[] { tableBody });

		greyBorderParagraph.add(mainTable);

		/*
		 * Contract snapshot as of [date]
		 */
		Date asOfDateDate = new Date();
		asOfDateDate.setTime(Long.parseLong(stringDate));

		DateFormat df1 = new SimpleDateFormat("MMMM dd, yyyy");
		DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
		String asOfDateString = df1.format(asOfDateDate);

		tableBody
				.add(0, 0, report.createParagraph().setBackgroundColor(
						"#002D62").setTextIndent("0.1cm").setFontSize(9).add(
						report.createText(
								ContentUtility.getContentAttribute(
										contractSnapshotLayoutBean,
										"body1Header")).setFontWeight("bold").setColor("#ffffff"))
						.add(report.createText(" as of " + asOfDateString).setColor("#ffffff")));
		tableBody.spanCells(0, 0, 1, 2);

		/*
		 * Plan assets section.
		 */
        PdfText supScriptContractAssets = report.createText("1");
        supScriptContractAssets.setElementAttribute("baseline-shift", "super");
        supScriptContractAssets.setElementAttribute("font-weight", "normal");
        supScriptContractAssets.setElementAttribute("font-size", "5pt");
        PdfParagraph contractAssetsHeader = report.createParagraph();
        contractAssetsHeader.add("Contract Assets");
        contractAssetsHeader.add(supScriptContractAssets);
        contractAssetsHeader.setBackgroundColor("#CCCCCC").setTextAlignment("0.1cm").setFontSize(9).setFontWeight(
        "bold");
        tableBody.add(1, 0, contractAssetsHeader);
		tableBody.spanCells(1, 0, 1, 2);
		
		if(!showBlendedAssetCharge){
			tableBody.spanCells(2, 0, 1, 2);
		}else{

			PdfTableBody assetChargeTableBody = report.createTableBody(1, 2);
			PdfTable assetCargeTable = report.createTable(new String[] {
					"6.0cm", "3.4cm" },
					new PdfTableRegion[] { assetChargeTableBody });
			assetChargeTableBody.setColumnTextAlign(1, "right")
			.setColumnPaddingLeft(0, "0.1cm")
			.setColumnMarginRight(1, "0cm");
			tableBody.add(2, 1, assetCargeTable);
			assetChargeTableBody.add(0, 0, report
					.createText("Blended Asset Charge "));
			String assetChargeAsOfDate = df2.format(vo.getPlanAssets().getAssetChargeAsOfDate());
			
			assetChargeTableBody.add(0, 1, report.createText(NumberRender
					.formatByPattern(vo.getPlanAssets().getBlendedAssetCharge(), DEFAULT_VALUE, AMT_PATTERN)
					+"%"+"    As of  "+assetChargeAsOfDate));
		}

		PdfTableBody tableBodyPlanAssets = report.createTableBody(7, 2);
		PdfTable contractAssetsTable = report.createTable(new String[] {
				"6.0cm", "3.4cm" },
				new PdfTableRegion[] { tableBodyPlanAssets });
		tableBodyPlanAssets.setColumnTextAlign(1, "right")
				.setColumnPaddingLeft(0, "0.1cm")
				.setColumnMarginRight(1, "0cm");
		tableBody.add(2, 0, contractAssetsTable);
		// tableBody.add(2, 0, new PdfParagraph("  "));
		// tableBody.add(2, 0, new
		// PdfParagraph("*Asset figures current as of close of previous business day"));

		tableBodyPlanAssets.add(0, 0, report
				.createText("Total contract assets").setFontWeight("bold").setColor("#002D62"));
		tableBodyPlanAssets.add(0, 1, report.createText(
				'$' + getFormattedCurrency(vo.getPlanAssets()
						.getTotalPlanAssetsAmount().toString(), false)).setFontWeight("bold")
				.setColor("#002D62"));
		tableBodyPlanAssets.add(1, 0, "Allocated assets");
		tableBodyPlanAssets.add(1, 1, vo.getPlanAssets()
				.getAllocatedAssetsAmount() != null ? getFormattedCurrency(vo
				.getPlanAssets().getAllocatedAssetsAmount().toString(), false)
				: "");

		boolean show = !(vo.getPlanAssets().getCashAccountAmount()
				.equals(new BigDecimal("0.00")));
		tableBodyPlanAssets.add(2, 0, show ? "Cash account" : "");
		tableBodyPlanAssets
				.add(
						2,
						1,
						show
								&& vo.getPlanAssets().getCashAccountAmount() != null ? getFormattedCurrency(
								vo.getPlanAssets().getCashAccountAmount()
										.toString(), false)
								: "");

		show = !(vo.getPlanAssets().getUninvestedAssetsAmount()
				.equals(new BigDecimal("0.00")));
		tableBodyPlanAssets.add(3, 0, show ? "Pending transactions" : "");
		tableBodyPlanAssets
				.add(
						3,
						1,
						show
								&& vo.getPlanAssets()
										.getUninvestedAssetsAmount() != null ? getFormattedCurrency(
								vo.getPlanAssets().getUninvestedAssetsAmount()
										.toString(), false)
								: "");

		show = displayLoan;
		tableBodyPlanAssets.add(4, 0, show ? "Loan assets" : "");
		tableBodyPlanAssets
				.add(
						4,
						1,
						show && vo.getPlanAssets().getLoanAssets() != null ? getFormattedCurrency(
								vo.getPlanAssets().getLoanAssets().toString(),
								false)
								: "");

		show = displayPba;
		tableBodyPlanAssets.add(5, 0, show ? "Personal brokerage account(†)"
				: "");
		tableBodyPlanAssets
				.add(
						5,
						1,
						show
								&& vo.getPlanAssets()
										.getPersonalBrokerageAccountAmount() != null ? getFormattedCurrency(
								vo.getPlanAssets()
										.getPersonalBrokerageAccountAmount()
										.toString(), false)
								: "");
		tableBodyPlanAssets.add(6, 0,
				"Asset figures current as of close of previous business day.");
		tableBodyPlanAssets.spanCells(6, 0, 1, 2);

		/*
		 * Assets growth section
		 */
		tableBody.add(3, 0, report.createParagraph().setBackgroundColor(
				"#CCCCCC").setTextIndent("0.1cm").setFontSize(9).setFontWeight(
				"bold").add(report.createText("Asset growth")));
		tableBody.setCellMarginRight(3, 0, "0.02cm");
		tableBody.add(3, 1, report.createParagraph().setBackgroundColor(
				"#CCCCCC").setTextIndent("0.1cm").setFontSize(9).setFontWeight(
				"bold").add(report.createText("Contributions & withdrawals")));
		tableBody.setCellMarginLeft(3, 1, "0.02cm");

		// Asset growth bar chart
		Iterator itSeries = assetGrowthBarChartBean.getAllSeries().iterator();
		while (itSeries.hasNext()) {
			DataSeries serie = (DataSeries) itSeries.next();

			PdfTableBody t = report.createTableBody(1, 2);
			PdfTable assetGrowthTable = report.createTable(new String[] {
					"0.4cm", "9cm" }, new PdfTableRegion[] { t });
			PdfParagraph legend = generateRectangle(getHexColor(serie
					.getColor()));
			t.add(0, 0, legend).setCellMargin(0, 0, "0cm");
			t.add(0, 1, "  " + serie.getSeriesTitle()).setCellMargin(0, 1,
					"0cm");
			tableBody.add(4, 0, assetGrowthTable);
		}
		assetGrowthBarChartBean.setWidth(360);
		tableBody.add(4, 0, report.createParagraph().add(
				getDynamicGraphic(ImageUrl.getChartImageUrl(
						assetGrowthBarChartBean, false))));

		tableBody.setCellPaddingLeft(4, 0, "0.2cm").setCellPaddingTop(4, 0,
				"0.1cm").setCellMargin(4, 0, "0cm").setCellBorderRightStyle(4,
				0, "solid");

		// Contributions and Withdrawals bar chart
		itSeries = contributionWithdrawalsBarChartBean.getAllSeries()
				.iterator();
		while (itSeries.hasNext()) {
			DataSeries serie = (DataSeries) itSeries.next();
			PdfTableBody t = report.createTableBody(1, 2);
			PdfTable contributionWithdrawals = report.createTable(new String[] {
					"0.4cm", "9cm" }, new PdfTableRegion[] { t });
			PdfParagraph legend = generateRectangle(getHexColor(serie
					.getColor()));
			t.add(0, 0, legend).setCellMargin(0, 0, "0cm");
			t.add(0, 1, "  " + serie.getSeriesTitle()).setCellMargin(0, 1,
					"0cm");
			tableBody.add(4, 1, contributionWithdrawals);
		}

		tableBody.add(4, 1, report.createParagraph().add(
				getDynamicGraphic(ImageUrl.getChartImageUrl(
						contributionWithdrawalsBarChartBean, false))));

		tableBody.setCellPaddingLeft(4, 1, "0.1cm").setCellPaddingRight(4, 1,
				"0.1cm").setCellPaddingTop(4, 1, "0.1cm").setCellMargin(4, 1,
				"0cm").setCellBorderLeftStyle(4, 1, "solid");

		/*
		 * Assets allocation by risk category section
		 */
		tableBody.add(5, 0, report.createParagraph().setBackgroundColor(
				"#CCCCCC").setTextIndent("0.1cm").setFontSize(9).setFontWeight(
				"bold").add(
				report.createText("Asset Allocation by investment category")));
		tableBody.spanCells(5, 0, 1, 2);

		PdfTableBody assetsAllocByRiskMainTableBody = report.createTableBody(2,
				2);

		PdfTable assetsAllocByRiskMainTable = report.createTable(new String[] {
				"7.2cm", "11.8cm" },
				new PdfTableRegion[] { assetsAllocByRiskMainTableBody });
		PdfText superScriptAllocatedContractAssets = report.createText("3");
		superScriptAllocatedContractAssets.setElementAttribute("baseline-shift", "super");
		superScriptAllocatedContractAssets.setElementAttribute("font-weight", "normal");
		superScriptAllocatedContractAssets.setElementAttribute("font-size", "5pt");

		PdfParagraph allocatedContractAssetsHeader = report.createParagraph();
		allocatedContractAssetsHeader.add("Allocated Contract Assets").setFontWeight("bold");
		allocatedContractAssetsHeader.add(superScriptAllocatedContractAssets);
		assetsAllocByRiskMainTableBody.add(0, 0, allocatedContractAssetsHeader);
		assetsAllocByRiskMainTableBody.setCellMargin(0, 0, "0cm");

		if (!definedBenefit) {
			assetsAllocByRiskMainTableBody.add(0, 1, report.createText(
					"Asset allocation by age group").setFontWeight("bold"));
			assetsAllocByRiskMainTableBody.setCellMargin(0, 1, "0cm");
		} else {
			assetsAllocByRiskMainTableBody.add(0, 1, report.createText(" "));
		}

		tableBody.add(6, 0, assetsAllocByRiskMainTable);
		tableBody.spanCells(6, 0, 1, 2);
		tableBody.setCellMargin(6, 0, "0cm").setCellPaddingLeft(6, 0, "0.2cm")
				.setCellPaddingRight(6, 0, "0.1cm");

		tableBody.setCellPaddingTop(6, 0, "0.1cm").setCellPaddingBottom(6, 0,
				"0.1cm");
		PdfGraphic graphic = null;

		// Allocated assets table
		int rowCount = 5;
		if (displayPba) {
			rowCount++;
		}
		if (displayLifecycle) {
			rowCount++;
		}
		PdfTableBody allocAssetsTableBody = report.createTableBody(rowCount, 4);
		PdfTable allocAssetsTable = report.createTable(new String[] { "0.4cm",
				"1.8cm", "2.0cm", "2.9cm" },
				new PdfTableRegion[] { allocAssetsTableBody });
		allocAssetsTableBody
				.setColumnTextAlign(2, PdfConstants.ALIGNMENT_RIGHT);
		assetsAllocByRiskMainTableBody.add(1, 0, allocAssetsTable);

		// LS - add lifecycle. make it dynamic
		int x = 0;
		if (displayLifecycle) {
			allocAssetsTableBody.setCellMargin(x, 0, "0cm");
			PdfParagraph legend = generateRectangle(Constants.AssetAllocationPieChart.COLOR_LIFECYCLE);
			allocAssetsTableBody.add(x, 0, legend);
			allocAssetsTableBody.add(x, 1, "  Target Date").setCellMargin(0, 1,
					"0cm");
			allocAssetsTableBody.add(x, 2, '$' + getFormattedCurrency(String
					.valueOf(vo.getContractAssetsByRisk().getTotalAssetsByRisk(
							"LC")), false));
			allocAssetsTableBody.setCellMargin(x, 2, "0cm");
			x++;
		}
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		PdfParagraph legend = generateRectangle(Constants.AssetAllocationPieChart.COLOR_AGRESSIVE);
		allocAssetsTableBody.add(x, 0, legend);
		allocAssetsTableBody.add(x, 1, "  Agressive Growth").setCellMargin(x,
				1, "0cm");
		allocAssetsTableBody.add(x, 2, '$' + getFormattedCurrency(String
				.valueOf(vo.getContractAssetsByRisk()
						.getTotalAssetsByRisk("AG")), false));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		x++;
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		legend = generateRectangle(Constants.AssetAllocationPieChart.COLOR_GROWTH);
		allocAssetsTableBody.add(x, 0, legend);
		allocAssetsTableBody.add(x, 1, "  Growth").setCellMargin(x, 1, "0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("GR")), false));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		x++;
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		legend = generateRectangle(Constants.AssetAllocationPieChart.COLOR_GROWTH_INCOME);
		allocAssetsTableBody.add(x, 0, legend);
		allocAssetsTableBody.add(x, 1, "  Growth & Income").setCellMargin(x, 1,
				"0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("GI")), false));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		x++;
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		legend = generateRectangle(Constants.AssetAllocationPieChart.COLOR_INCOME);
		allocAssetsTableBody
				.add(
						x,
						0,
						generateRectangle(Constants.AssetAllocationPieChart.COLOR_INCOME));
		allocAssetsTableBody.add(x, 1, "  Income").setCellMargin(x, 1, "0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("IN")), false));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		x++;
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		legend = generateRectangle(Constants.AssetAllocationPieChart.COLOR_CONSERVATIVE);
		allocAssetsTableBody.add(x, 0, legend);
		allocAssetsTableBody.add(x, 1, "  Conservative").setCellMargin(x, 1,
				"0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("CN")), false));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");

		if (displayPba) {
			x++;
			allocAssetsTableBody.setCellMargin(x, 0, "0cm");
			legend = generateRectangle(Constants.AssetAllocationPieChart.COLOR_PBA);
			allocAssetsTableBody.add(x, 0, legend);
			allocAssetsTableBody.add(x, 1, "  Personal Brokerage Account")
					.setCellMargin(x, 1, "0cm");
			allocAssetsTableBody.add(x, 2, getFormattedCurrency(String
					.valueOf(vo.getContractAssetsByRisk().getTotalAssetsByRisk(
							"PB")), false));
			allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		}

		graphic = getDynamicGraphic(PieChartUtil
				.createURLString(assetAllocByRiskPieChartBean));
		allocAssetsTableBody.add(0, 3, report.createParagraph().add(graphic));
		allocAssetsTableBody.spanCells(0, 3, rowCount, 1);
		allocAssetsTableBody.setCellMargin(0, 3, "0cm");
		allocAssetsTableBody.setCellPaddingLeft(0, 3, "0.2cm");
		allocAssetsTableBody.setCellPaddingRight(0, 3, "0.1cm");
		allocAssetsTableBody.setCellPaddingBottom(0, 3, "0.1cm");

		if (definedBenefit) {
			assetsAllocByRiskMainTableBody.add(1, 1, report.createText(" "));
		} else {
			// asset allocation by age group and participant status do not apply to
			// defined benefit contracts

			// Asset Allocation by age group
			int counter = 0;
			if (vo.getContractAssetsByRisk().getAgeGroup("<30")
					.getNumberOfParticipants() != 0)
				counter++;
			if (vo.getContractAssetsByRisk().getAgeGroup("30-39")
					.getNumberOfParticipants() != 0)
				counter++;
			if (vo.getContractAssetsByRisk().getAgeGroup("40-49")
					.getNumberOfParticipants() != 0)
				counter++;
			if (vo.getContractAssetsByRisk().getAgeGroup("50-59")
					.getNumberOfParticipants() != 0)
				counter++;
			if (vo.getContractAssetsByRisk().getAgeGroup("60+")
					.getNumberOfParticipants() != 0)
				counter++;
			
			if (counter != 0) { 
			String[] sa = new String[counter];
			PdfTableBody assetAllocAgeGroupTableBody = report.createTableBody(
					1, sa.length);

			for (int i = 0; i < counter; i++) {
				sa[i] = "2.1cm";
				assetAllocAgeGroupTableBody.setColumnTextAlign(i,
						PdfConstants.ALIGNMENT_CENTER);
			}

			PdfTable assetAllocAgeGroupTable = report.createTable(sa,
					new PdfTableRegion[] { assetAllocAgeGroupTableBody });
			assetAllocAgeGroupTableBody.setBorderStyle(
					PdfConstants.BORDER_STYLE_SOLID).setBorderColor("#002D62");

			assetsAllocByRiskMainTableBody.add(1, 1, assetAllocAgeGroupTable);
			assetsAllocByRiskMainTableBody.setCellMargin(1, 1, "0cm");

			counter = 0;
			if (vo.getContractAssetsByRisk().getAgeGroup("<30")
					.getNumberOfParticipants() != 0) {
				graphic = getDynamicGraphic(PieChartUtil
						.createURLString(assetAllocBelow30PieChartBean));
				assetAllocAgeGroupTableBody.add(0, counter, report
						.createParagraph().add(graphic));
				assetAllocAgeGroupTableBody.add(0, counter, "<30 years old");
				assetAllocAgeGroupTableBody.add(0, counter, vo
						.getContractAssetsByRisk().getAgeGroup("<30")
						.getNumberOfParticipants()
						+ " Participant(s)");
				counter++;
			}

			if (vo.getContractAssetsByRisk().getAgeGroup("30-39")
					.getNumberOfParticipants() != 0) {
				graphic = getDynamicGraphic(PieChartUtil
						.createURLString(assetAllocBetween30And39PieChartBean));
				assetAllocAgeGroupTableBody.add(0, counter, report
						.createParagraph().add(graphic));
				assetAllocAgeGroupTableBody.add(0, counter, "30-39 years old");
				assetAllocAgeGroupTableBody.add(0, counter, vo
						.getContractAssetsByRisk().getAgeGroup("30-39")
						.getNumberOfParticipants()
						+ " Participant(s)");
				counter++;
			}

			if (vo.getContractAssetsByRisk().getAgeGroup("40-49")
					.getNumberOfParticipants() != 0) {
				graphic = getDynamicGraphic(PieChartUtil
						.createURLString(assetAllocBetween40And49PieChartBean));
				assetAllocAgeGroupTableBody.add(0, counter, report
						.createParagraph().add(graphic));
				assetAllocAgeGroupTableBody.add(0, counter, "40-49 years old");
				assetAllocAgeGroupTableBody.add(0, counter, vo
						.getContractAssetsByRisk().getAgeGroup("40-49")
						.getNumberOfParticipants()
						+ " Participant(s)");
				counter++;
			}

			if (vo.getContractAssetsByRisk().getAgeGroup("50-59")
					.getNumberOfParticipants() != 0) {
				graphic = getDynamicGraphic(PieChartUtil
						.createURLString(assetAllocBetween50And59PieChartBean));
				assetAllocAgeGroupTableBody.add(0, counter, report
						.createParagraph().add(graphic));
				assetAllocAgeGroupTableBody.add(0, counter, "50-59 years old");
				assetAllocAgeGroupTableBody.add(0, counter, vo
						.getContractAssetsByRisk().getAgeGroup("50-59")
						.getNumberOfParticipants()
						+ " Participant(s)");
				counter++;
			}

			if (vo.getContractAssetsByRisk().getAgeGroup("60+")
					.getNumberOfParticipants() != 0) {
				graphic = getDynamicGraphic(PieChartUtil
						.createURLString(assetAllocBetween60And69PieChartBean));
				assetAllocAgeGroupTableBody.add(0, counter, report
						.createParagraph().add(graphic));
				assetAllocAgeGroupTableBody.add(0, counter, "60+ years old");
				assetAllocAgeGroupTableBody.add(0, counter, vo
						.getContractAssetsByRisk().getAgeGroup("60+")
						.getNumberOfParticipants()
						+ " Participant(s)");
				counter++;
			}
			}else{
				PdfTableBody assetAllocAgeGroupTableBody = report.createTableBody(
						1, 1);
				PdfTable assetAllocAgeGroupTable = report.createTable(new String[]{"2.1cm"},
						new PdfTableRegion[] { assetAllocAgeGroupTableBody });

				assetsAllocByRiskMainTableBody.add(1, 1, assetAllocAgeGroupTable);
				assetsAllocByRiskMainTableBody.setCellMargin(1, 1, "0cm");
				assetAllocAgeGroupTableBody.add(0, 0, "");
			}
			
			if (vo.getContractAssetsByRisk()
					.getNumberOfDefaultBirthDateParticipants() > 0) {
				StringBuffer text = new StringBuffer();

				// assetsAllocByRiskMainTable.add
				// (1, 1,
				text.append("Allocations by age group will not be "
						+ "accurate if complete and "
						+ "correct birth dates have not been "
						+ "provided. In these instances, "
						+ "default birth dates are assumed. ");
				// );
				if (vo.getContractAssetsByRisk().getAgeGroup("DEFAULT1980")
						.getNumberOfParticipants() > 0) {
					String s = "Your contract currently has "
							+ vo.getContractAssetsByRisk().getAgeGroup(
									"DEFAULT1980").getNumberOfParticipants()
							+ " participant(s) with a default birth date of January 1, 1980.";
					text.append(s);
				}

				assetsAllocByRiskMainTableBody.add(1, 1, text.toString());
				assetsAllocByRiskMainTableBody
						.setCellMarginRight(1, 1, "0.1cm");
			}

			/*
			 * Participant status
			 */

			tableBody.add(7, 0, report.createParagraph().setBackgroundColor(
					"#CCCCCC").setTextIndent("0.1cm").setFontSize(9)
					.setFontWeight("bold").add(
							report.createText("Participant Status")));
			tableBody.setCellMarginRight(7, 0, "0.02cm");
			tableBody.setCellMarginLeft(7, 0, "0.02cm");
			tableBody.add(7, 1, report.createParagraph().setBackgroundColor(
					"#CCCCCC").setTextIndent("0.1cm").setFontSize(9)
					.setFontWeight("bold").add(report.createText(" ")));

			if (isRecentDate) {
				/*
				 * Total line is always there.
				 */
				rowCount = 1;
				if (!vo.getContractAssetsByRisk().getHasEmployeeMoneyType()) {
					if (vo.getContractAssetsByRisk()
							.getActiveContributingParticipantsNumber() != 0) {
						rowCount++;
					}
					if (vo.getContractAssetsByRisk()
							.getActiveNoBalanceParticipantsNumber() != 0)
						rowCount++;
					if (vo.getContractAssetsByRisk()
							.getActiveNotContributingParticipantsNumber() != 0)
						rowCount++;
					if (vo.getContractAssetsByRisk()
							.getActiveOptedOutParticipantsNumber() != 0)
						rowCount++;
					if (vo.getContractAssetsByRisk()
							.getInactiveWithBalanceParticipantsNumber() != 0)
						rowCount++;
					if (vo.getContractAssetsByRisk()
							.getInactiveWithUMParticipantsNumber() != 0)
						rowCount++;
					if (vo.getContractAssetsByRisk()
							.getOptedOutNotVestedParticipantsNumber() != 0)
						rowCount++;
					if (vo.getContractAssetsByRisk()
							.getOptedOutZeroBalanceParticipantsNumber() != 0)
						rowCount++;
				} else {
					if (vo.getContractAssetsByRisk()
							.getActiveParticipantsNumber() != 0) {
						rowCount++;
					}
					if (vo.getContractAssetsByRisk()
							.getInactiveWithBalanceParticipantsNumber() != 0)
						rowCount++;
					if (vo.getContractAssetsByRisk()
							.getInactiveWithUMParticipantsNumber() != 0)
						rowCount++;
				}

				PdfTableBody partStatusTableBody = report.createTableBody(
						rowCount, 4);
				PdfTable partStatusTable = report.createTable(new String[] {
						"0.4cm", "4.1cm", "1.0cm", "4.0cm" },
						new PdfTableRegion[] { partStatusTableBody });
				partStatusTableBody.setColumnTextAlign(2,
						PdfConstants.ALIGNMENT_RIGHT);

				if (!vo.getContractAssetsByRisk().getHasEmployeeMoneyType()) {
					x = 0;
					if (vo.getContractAssetsByRisk()
							.getActiveContributingParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						legend = generateRectangle(Constants.ParticipantStatusPieChart.COLOR_ACTIVE_AND_CONTRIBUTING);
						partStatusTableBody.add(x, 0, legend);
						partStatusTableBody.add(x, 1,
								"  Active and contributing").setCellMargin(x,
								1, "0cm");
						int activeContributingParticipants = vo
								.getContractAssetsByRisk()
								.getActiveContributingParticipantsNumber();
						partStatusTableBody.add(x, 2,
								String.valueOf(activeContributingParticipants))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getActiveNoBalanceParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						legend = generateRectangle(Constants.ParticipantStatusPieChart.COLOR_ACTIVE_NO_BALANCE);
						partStatusTableBody.add(x, 0, legend);
						partStatusTableBody.add(x, 1,
								"  Active with no Balance").setCellMargin(x, 1,
								"0cm");
						int activeNoBalanceParticipants = vo
								.getContractAssetsByRisk()
								.getActiveNoBalanceParticipantsNumber();
						partStatusTableBody.add(x, 2,
								String.valueOf(activeNoBalanceParticipants))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getActiveNotContributingParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						legend = generateRectangle(Constants.ParticipantStatusPieChart.COLOR_ACTIVE_NOT_CONTRIBUTING);
						partStatusTableBody.add(x, 0, legend);
						partStatusTableBody.add(x, 1,
								"  Active but not contributing").setCellMargin(
								x, 1, "0cm");
						int activeNotContributingPrts = vo
								.getContractAssetsByRisk()
								.getActiveNotContributingParticipantsNumber();
						partStatusTableBody.add(x, 2,
								String.valueOf(activeNotContributingPrts))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getActiveOptedOutParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						partStatusTableBody
								.add(
										x,
										0,
										generateRectangle(Constants.ParticipantStatusPieChart.COLOR_ACTIVE_OPTED_OUT));
						partStatusTableBody.add(x, 1, "  Active opted out")
								.setCellMargin(x, 1, "0cm");
						partStatusTableBody
								.add(
										x,
										2,
										String
												.valueOf(vo
														.getContractAssetsByRisk()
														.getActiveOptedOutParticipantsNumber()))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getInactiveWithBalanceParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						partStatusTableBody
								.add(
										x,
										0,
										generateRectangle(Constants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE));
						partStatusTableBody
								.add(x, 1, "  Inactive with balance")
								.setCellMargin(x, 1, "0cm");
						partStatusTableBody
								.add(
										x,
										2,
										String
												.valueOf(vo
														.getContractAssetsByRisk()
														.getInactiveWithBalanceParticipantsNumber()))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getInactiveWithUMParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						partStatusTableBody
								.add(
										x,
										0,
										generateRectangle(Constants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY));
						partStatusTableBody.add(x, 1,
								"  Inactive Uninvested Money only")
								.setCellMargin(x, 1, "0cm");
						partStatusTableBody
								.add(
										x,
										2,
										String
												.valueOf(vo
														.getContractAssetsByRisk()
														.getInactiveWithUMParticipantsNumber()))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getOptedOutNotVestedParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						partStatusTableBody
								.add(
										x,
										0,
										generateRectangle(Constants.ParticipantStatusPieChart.COLOR_OPTED_OUT_NOT_VESTED));
						partStatusTableBody.add(x, 1, "  Opted out not vested")
								.setCellMargin(x, 1, "0cm");
						partStatusTableBody
								.add(
										x,
										2,
										String
												.valueOf(vo
														.getContractAssetsByRisk()
														.getOptedOutNotVestedParticipantsNumber()))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getOptedOutZeroBalanceParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						partStatusTableBody
								.add(
										x,
										0,
										generateRectangle(Constants.ParticipantStatusPieChart.COLOR_OPTED_OUT_ZERO_BALANCE));
						partStatusTableBody.add(x, 1, "  Opted out not vested")
								.setCellMargin(x, 1, "0cm");
						partStatusTableBody
								.add(
										x,
										2,
										String
												.valueOf(vo
														.getContractAssetsByRisk()
														.getOptedOutZeroBalanceParticipantsNumber()))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					partStatusTableBody.setCellMargin(x, 0, "0cm");
					partStatusTableBody.add(x, 0, "");
					partStatusTableBody.add(
							x,
							1,
							report.createText("  Total").setFontWeight("bold")
									.setColor("#002D62")).setCellMargin(x, 1,
							"0cm");
					partStatusTableBody.add(
							x,
							2,
							report.createText(
									String.valueOf(vo.getContractAssetsByRisk()
											.getTotalParticipantsNumber()))
									.setFontWeight("bold").setColor("#002D62"))
							.setCellMargin(x, 2, "0cm");
					x++;
				} else {
					x = 0;
					if (vo.getContractAssetsByRisk()
							.getActiveParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						partStatusTableBody
								.add(
										x,
										0,
										generateRectangle(Constants.ParticipantStatusPieChart.COLOR_ACTIVE));
						partStatusTableBody.add(x, 1, "  Active")
								.setCellMargin(x, 1, "0cm");
						partStatusTableBody.add(
								x,
								2,
								String.valueOf(vo.getContractAssetsByRisk()
										.getActiveParticipantsNumber()))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getInactiveWithBalanceParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						partStatusTableBody
								.add(
										x,
										0,
										generateRectangle(Constants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE));
						partStatusTableBody
								.add(x, 1, "  Inactive with balance")
								.setCellMargin(x, 1, "0cm");
						partStatusTableBody
								.add(
										x,
										2,
										String
												.valueOf(vo
														.getContractAssetsByRisk()
														.getInactiveWithBalanceParticipantsNumber()))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					if (vo.getContractAssetsByRisk()
							.getInactiveWithUMParticipantsNumber() != 0) {
						partStatusTableBody.setCellMargin(x, 0, "0cm");
						partStatusTableBody
								.add(
										x,
										0,
										generateRectangle(Constants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY));
						partStatusTableBody.add(x, 1,
								"  Inactive Uninvested Money only")
								.setCellMargin(x, 1, "0cm");
						partStatusTableBody
								.add(
										x,
										2,
										String
												.valueOf(vo
														.getContractAssetsByRisk()
														.getInactiveWithUMParticipantsNumber()))
								.setCellMargin(x, 2, "0cm");
						x++;
					}

					partStatusTableBody.setCellMargin(x, 0, "0cm");
					partStatusTableBody.add(x, 0, "");
					partStatusTableBody.add(
							x,
							1,
							report.createText("  Total").setFontWeight("bold")
									.setColor("#002D62")).setCellMargin(x, 1,
							"0cm");
					partStatusTableBody.add(
							x,
							2,
							report.createText(
									String.valueOf(vo.getContractAssetsByRisk()
											.getTotalParticipantsNumber()))
									.setFontWeight("bold").setColor("#002D62"))
							.setCellMargin(x, 2, "0cm");
					x++;
				}

				graphic = getDynamicGraphic(PieChartUtil
						.createURLString(participantStatusPieChartBean));
				partStatusTableBody.add(0, 3, report.createParagraph().add(
						graphic));
				partStatusTableBody.spanCells(0, 3, rowCount, 1);
				partStatusTableBody.setCellMargin(0, 3, "0cm");
				partStatusTableBody.setCellPaddingLeft(0, 3, "0.2cm");
				partStatusTableBody.setCellPaddingRight(0, 3, "0.5cm");
				partStatusTableBody.setCellPaddingBottom(0, 3, "0.1cm");

				tableBody.add(8, 0, partStatusTable);
				tableBody.add(8, 1, " ");

				tableBody.setCellPaddingLeft(8, 0, "0.2cm")
						.setCellPaddingRight(8, 0, "1cm").setCellPaddingTop(8,
								0, "0.1cm").setCellMargin(8, 0, "0cm")
						.setCellBorderRightStyle(8, 0, "solid");

			} else {
				tableBody
						.add(
								8,
								0,
								"  Participant Status and Participation rate are not available for the date selected.");
				tableBody.spanCells(8, 0, 1, 2);

			}
		} // end if (! definedBenefit asset allocation by age group)

		if (!definedBenefit) {
			if (displayPba) {
				StringBuffer contentAttribute = new StringBuffer(ContentUtility.getContentAttribute(generalPBAFootnoteBean, "text"));
				int startIndex = contentAttribute.indexOf("&#134;");
				if(startIndex >= 0){
					contentAttribute.replace(startIndex, startIndex+6, "†");
				}

				report.add(contentAttribute.toString());
			} else {
				StringBuffer contentAttribute = new StringBuffer(ContentUtility.getContentAttribute(generalNoPBAFootnoteBean, "text"));
				int startIndex = contentAttribute.indexOf("&#134;");
				if(startIndex >= 0){
					contentAttribute.replace(startIndex, startIndex+6, "†");
				}

				report.add(contentAttribute.toString());
			}
		} else {
			report.add(ContentUtility.getContentAttribute(
					generalDefinedBenefitFootnoteBean, "text"));
		}

		report.printAsPdf(out);
	}

	/**
	 * Returns a URL that points to an image.
	 * 
	 * @param fileName
	 *            The filename of the image.
	 * @return A URL that points to an image. If no image is found, this method
	 *         returns null.
	 */
	private PdfGraphic getStaticGraphic(String fileName) {
		return report.createGraphic(this.getClass().getResource(fileName));
	}

	/**
	 * Returns a URL that points to the given file name. This URL is generated
	 * using the request URL's protocol, host, and port.
	 * 
	 * @param fileName
	 *            The file name portion of the URL.
	 * @return A URL that points to the given file name.
	 * @throws MalformedURLException
	 *             If the URL cannot be constructed.
	 */
	private PdfGraphic getDynamicGraphic(String fileName)
			throws MalformedURLException {

		String portNumber = System.getProperty("webcontainer.http.port") == null ? "9081"
				: System.getProperty("webcontainer.http.port");

		if (logger.isDebugEnabled()) {
			logger.debug("About to create URL with http://localhost:"
					+ portNumber + "/" + fileName);
		}
		URL url = null;
		try {

			// THIS WOULD have been nice to have, but since this URL scraping
			// happens within the same server the protocol and servername are
			// hardcoded,
			// and are not going to cause any problems with DSMart.
			// url = new URL(requestUrlName.getProtocol(), requestUrlName
			// .getHost(), requestUrlName.getPort(), fileName);
			url = new URL("http", "localhost", Integer.parseInt(portNumber),
					fileName);

			if (logger.isDebugEnabled()) {
				logger.debug("URL: " + url.toExternalForm());
			}
		} catch (MalformedURLException e) {
			// TODO: revisit this
			logger.error("Exception in creating URL", e);
			throw e;
		}

		PdfGraphic graphic = report.createGraphic(url);
		graphic.setContentHeight("100%");
		graphic.setContentWidth("scale-to-fit");
		graphic.setWidth("100%");
		return graphic;
	}

	/**
	 * A utility method to convert a Color to an CSS hex color
	 * 
	 * @param color
	 *            is the color to convert
	 * @return the CSS string color
	 */
	public static String getHexColor(Color color) {
		return "#" + Integer.toHexString(color.getRGB()).substring(2, 8);
	}

	private PdfParagraph generateRectangle(String color) {
		return report.createParagraph("A").setFontSize(2).setColor(color)
				.setBackgroundColor(color).setPadding("2px").setMargin("2px");
	}

	public static String getFormattedCurrency(Object value,
			boolean showCurrencySign) {
		try {
			return NumberRender.format(value, null, null, "c", 2,
					BigDecimal.ROUND_HALF_DOWN, 1, showCurrencySign);
		} catch (Exception e) {
			SystemException se = new SystemException(e,
					"com.manulife.pension.ps.web.contract.ContractSnapshotPdf",
					"getFormattedCurrency", "Input parameters are value:"
							+ value + ", showCurrencySign:" + showCurrencySign);
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
			return null;
		}
	}

}
