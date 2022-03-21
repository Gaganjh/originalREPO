package com.manulife.pension.bd.web.bob.contract;

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
import java.util.regex.Pattern;

import javax.mail.URLName;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
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
import com.manulife.util.piechart.PieChartBean;
import com.manulife.util.piechart.PieChartUtil;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * This class generates the contract snapshot PDF using the mrl PDF framework.
 * 
 * @author aambrose
 */

public class ContractSnapshotPdf {

	private static final Logger logger = Logger
			.getLogger(ContractSnapshotPdf.class);

	private String stringDate;

	private boolean displayLoan;

	private boolean displayPba;

	private boolean displayLifecycle;

	private boolean isRecentDate;
	
	private boolean isContractHasRothInfo;

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

	private BDLayoutBean bdLayoutBean;

	private LayoutPage layoutPageBean;

	private Contract currentContract;

	private Location location;
	
	private Content contractSnapshotLayoutBean;
	
	private PdfReport report;

	private Content pbaFootnoteBean;

	private Content assetsAllocatedActiveParticipantFootnoteBean;

	private Content selectedReportingDateFootnoteBean;

	private Content globalFootnoteText1FootnoteBean;

	private Content globalFootnoteText2FootnoteBean;

	private Content globalFootnoteText3FootnoteBean;

    private Content globalFootnoteText4FootnoteBean;

    private Content globalFootnoteText5FootnoteBean;

	private static final String FOOTER_TEXT = " NOT VALID WITHOUT ALL PAGES";

	private static final Pattern PATTERN_STRONG_TAG = Pattern
			.compile("<strong[^>]*>|<STRONG[^>]*>");

	private static final String BOLD_TAG = "<b>";

	private static final Pattern PATTERN_STRONG_END_TAG = Pattern
			.compile("</strong[^>]*>|</STRONG[^>]*>");

	private static final String BOLD_END_TAG = "</b>";

	private static final String INFORMATION_MESSAGE = "Information Message";

	private static String[] errors = null;

	public ContractSnapshotPdf(String stringDate, boolean displayLoan,
			boolean displayPba, boolean displayLifecycle, boolean isRecentDate, 
			boolean isContractHasRothInfo, Contract currentContract, 
			URLName requestUrlName, Location location,
            BDLayoutBean bdLayoutBean, ContractSnapshotVO vo,
			BarChartBean assetGrowthBarChartBean,
			BarChartBean contributionWithdrawalsBarChartBean,
			PieChartBean participantStatusPieChartBean,
			PieChartBean assetAllocByRiskPieChartBean,
			PieChartBean assetAllocBelow30PieChartBean,
			PieChartBean assetAllocBetween30And39PieChartBean,
			PieChartBean assetAllocBetween40And49PieChartBean,
			PieChartBean assetAllocBetween50And59PieChartBean,
			PieChartBean assetAllocBetween60And69PieChartBean, String[] errors)
			throws ContentException {

		this.stringDate = stringDate;
		this.displayLoan = displayLoan;
		this.displayPba = displayPba;
		this.displayLifecycle = displayLifecycle;
		this.isRecentDate = isRecentDate;
		this.isContractHasRothInfo = isContractHasRothInfo;
		this.currentContract = currentContract;
		this.requestUrlName = requestUrlName;
		this.location = location;
		this.bdLayoutBean = bdLayoutBean;
		this.layoutPageBean = bdLayoutBean.getLayoutPageBean();
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
		this.errors = errors;

		report = new PdfReport(FOOTER_TEXT).setFontFamily("sans-serif")
				.setFontSize(8);

		if (currentContract.isDefinedBenefitContract()) {
            contractSnapshotLayoutBean = ContentCacheManager.getInstance().getContentById(
                    BDContentConstants.BD_CONTRACT_SNAPSHOT_LAYOUT_PAGE_DB,
                    ContentTypeManager.instance().LAYOUT_PAGE);

		} else {
            contractSnapshotLayoutBean = ContentCacheManager.getInstance().getContentById(
                    BDContentConstants.BD_CONTRACT_SNAPSHOT_LAYOUT_PAGE,
                    ContentTypeManager.instance().LAYOUT_PAGE);

        }

		pbaFootnoteBean = ContentCacheManager.getInstance().getContentById(
				BDContentConstants.PDF_PERSONAL_BROKERAGE_ACCOUNT,
				ContentTypeManager.instance().PAGE_FOOTNOTE);

		assetsAllocatedActiveParticipantFootnoteBean = ContentCacheManager
				.getInstance()
				.getContentById(
						BDContentConstants.PDF_ASSETS_ALLOCATED_TO_ACTIVE_PARTICIPANT,
						ContentTypeManager.instance().PAGE_FOOTNOTE);

		selectedReportingDateFootnoteBean = ContentCacheManager
				.getInstance()
				.getContentById(
						BDContentConstants.PDF_INFORMATION_FOR_REPORTING_DATE_SELECTED,
						ContentTypeManager.instance().PAGE_FOOTNOTE);

		globalFootnoteText1FootnoteBean = ContentCacheManager.getInstance()
				.getContentById(BDContentConstants.PDF_GLOBAL_FOOTNOTE_TEXT1,
						ContentTypeManager.instance().PAGE_FOOTNOTE);

		globalFootnoteText2FootnoteBean = ContentCacheManager.getInstance()
				.getContentById(BDContentConstants.PDF_GLOBAL_FOOTNOTE_TEXT2,
						ContentTypeManager.instance().PAGE_FOOTNOTE);

		globalFootnoteText3FootnoteBean = ContentCacheManager.getInstance()
				.getContentById(BDContentConstants.PDF_GLOBAL_FOOTNOTE_TEXT3,
						ContentTypeManager.instance().PAGE_FOOTNOTE);

        globalFootnoteText4FootnoteBean = ContentCacheManager.getInstance()
        .getContentById(BDContentConstants.PDF_GLOBAL_FOOTNOTE_TEXT4,
                ContentTypeManager.instance().PAGE_FOOTNOTE);

        globalFootnoteText5FootnoteBean = ContentCacheManager.getInstance()
        .getContentById(BDContentConstants.PDF_GLOBAL_FOOTNOTE_TEXT5,
                ContentTypeManager.instance().PAGE_FOOTNOTE);

	}

	/**
	 * Writes the PDF content to the output stream.
	 * 
	 * @param out
	 *            The output stream to write to.
	 * @throws IOException
	 */
	public void writeTo(OutputStream out) throws IOException {

		boolean definedBenefit = currentContract.isDefinedBenefitContract();

		/*
		 * Manulife Logo Picture.
		 */
		PdfGraphic logoGraph = getStaticGraphic(BDConstants.UNMANAGED_IMAGE_FILE_PREFIX
				+ CommonConstants.JHRPS_LOGO_FILE);

		/*
		 * Company Name and Contract Number.
		 */
		PdfParagraph contractName = report.createParagraph().setMarginLeft(
				"0.0cm").setPaddingBottom("0.0cm").setFontWeight("bold")
				.setFontFamily("Arial").setFontSize(12);
		contractName.add(report.createText(currentContract.getCompanyName()
				.trim()));
		contractName.add(" (" + currentContract.getContractNumber() + ")");

		/*
		 * Contract snapshot as of [date]
		 */
		Date asOfDateDate = new Date();
		asOfDateDate.setTime(Long.parseLong(stringDate));

		DateFormat df1 = new SimpleDateFormat(
				RenderConstants.MEDIUM_MDY_SLASHED);
		String asOfDateString = df1.format(asOfDateDate);

		PdfTableBody headerPdfTableBody = report.createTableBody(1, 2);
		PdfTable headerTable = report.createTable(
				new String[] { "40%", "60%" },
				new PdfTableRegion[] { headerPdfTableBody });
		headerPdfTableBody.add(0, 0, report.createParagraph().add(logoGraph.setContentHeight("70px")
				.setContentWidth("170px")))
				.setCellMargin(0, 0, "0cm");
		headerPdfTableBody.add(
				0,
				1,
				report.createParagraph().setBorderBeforeColor("#FFFFFF")
						.setBorderBeforeStyle("solid").setBorderBeforeWidth(
								"0.5cm").add(
								report.createText(
										ContentUtility.getContentAttribute(
												contractSnapshotLayoutBean,
												"name")).setFontFamily(
										"Georgia").setFontSize(24)).add(
								contractName).add(
								report.createText(" as of " + asOfDateString))
						.setTextAlignment("right")).setPaddingBefore("0.5cm")
				.setCellMargin(0, 1, "0cm");
		report.add(headerTable);

		// This variable is added to make sure we are not adding top-padding, bottom-padding for
        // every element. We add the top-padding, bottom-padding for the first element and the rest
        // of the elements will only add the bottom-padding.
		boolean topPaddingAdded = false;
		/*
		 * Intro 1
		 */
		String intro1 = ContentUtility.getPageIntroduction(layoutPageBean);
		String contentWithFo = replaceContentWithFO(intro1);
		if (!StringUtils.isBlank(intro1)) {
			report.add(report.createParagraph().setMargin("0.0cm").setFontSize(
					9).setPaddingTop("0.3cm").setPaddingBottom("0.3cm").add(
					intro1));
			topPaddingAdded = true;
		}
		
        /*
         * An overview of your plan's participant & asset growth over the last 24 months
         */
        // To remove the html tag and add the relevant FO tag.
        String introduction2Text = ContentUtility.getContentAttribute(layoutPageBean,
                "introduction2");
        contentWithFo = replaceContentWithFO(introduction2Text);
        if (!StringUtils.isBlank(contentWithFo)) {
	        PdfParagraph intro2Para = report.createParagraph(
	                ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)).setMargin(
	                "0.0cm").setFontSize(9).setPaddingBottom("0.3cm");
	        if (!topPaddingAdded) {
	            intro2Para.setPaddingTop("0.3cm");
	            topPaddingAdded = true;
	        }
	        report.add(intro2Para);
        }

       /* if (isContractHasRothInfo) {
            String rothText = ContentHelper.getContentText(
                    BDContentConstants.MISCELLANEOUS_ROTH_INFO,
                    ContentTypeManager.instance().MISCELLANEOUS, location);
            if (!StringUtils.isBlank(rothText)) {
                contentWithFo = replaceContentWithFO(rothText);
                PdfParagraph rothPara = report.createParagraph(
                        ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)).setMargin(
                        "0.0cm").setFontSize(9).setPaddingBottom("0.3cm");
                if (!topPaddingAdded) {
                    rothPara.setPaddingTop("0.3cm");
                    topPaddingAdded = true;
                }
                report.add(rothPara);
            }
        }*/
		
		// Contract having the Roth and GIFL were validated in the action class
		// and the same
		// validations have removed from here. The values passed as String
		// array.
		if (errors != null && errors.length > 0) {
			report.add(report.createParagraph().setMargin("0.0cm").setFontSize(
					9).setPaddingTop("0.03cm").setPaddingBottom("0.06cm")
					.setFontWeight("bold").add(INFORMATION_MESSAGE));

			for (int i = 0; i < errors.length; i++) {

				report.add(report.createParagraph().setMargin("0.0cm")
						.setFontSize(9).setPaddingTop("0.03cm")
						.setPaddingBottom("0.06cm").add(
								i
										+ 1
										+ ". "
										+ StringUtils.replace(errors[i],
												"&nbsp;", " ")));
			}
		}
		PdfParagraph greyBorderParagraph = report.createParagraph().setMargin(
				"0.0cm");
		report.add(greyBorderParagraph);

		PdfTableBody tableBody = null;
		tableBody = report.createTableBody(5, 2);

		tableBody.setBorderColor("#CCCCCC").setBorderWidth("1px");
		PdfTable mainTable = report.createTable(
				new String[] { "9.5cm", "9.5cm" },
				new PdfTableRegion[] { tableBody });

		greyBorderParagraph.add(mainTable);

		tableBody.add(0, 0, report.createParagraph().setBackgroundColor(
				"#CCCCCC").setTextIndent("0.1cm").setFontSize(9)
				.setBorderBeforeColor("#febe10").setBorderBeforeStyle("solid")
				.setBorderBeforeWidth("0.06cm").setBorderBottomColor("#CCCCCC")
				.setBorderBottomStyle("solid").setBorderBottomWidth("0.10cm")
				.add(
						report.createText(
								ContentUtility.getContentAttribute(
										contractSnapshotLayoutBean,
										"body1Header")).setFontWeight("bold")));
		tableBody.spanCells(0, 0, 1, 2);

		tableBody
				.add(1, 0, report.createParagraph().add(report.createText("")))
				.setPaddingBefore("0.04cm");
		tableBody.spanCells(1, 0, 1, 2);

		PdfTableBody leftSideSectionsTableBody = report.createTableBody(4, 1);
		PdfTableBody rightSideSectionsTableBody = report.createTableBody(4, 1);

		PdfTable leftSideSectionsTable = report.createTable(new String[] {"100%"}, new PdfTableRegion[]{leftSideSectionsTableBody});
		
		PdfTable rightSideSectionsTable = report.createTable(new String[] {"100%"}, new PdfTableRegion[]{rightSideSectionsTableBody});

		tableBody.add(2, 0, leftSideSectionsTable);
		
		tableBody.add(2, 1, rightSideSectionsTable);
		
		/*
		 * Plan assets section.
		 */
		if (!definedBenefit) {
			leftSideSectionsTableBody.add(
					0,
					0,
					report.createParagraph().setBackgroundColor("#CCCCCC")
							.setTextIndent("0.1cm").setFontSize(9)
							.setFontWeight("bold").setBorderBeforeColor(
									"#febe10").setBorderBeforeStyle("solid")
							.setBorderBeforeWidth("0.06cm").add(
									report.createText("Contract Assets")).add(
									report.createText("1").setBaseLineShift(
											"super").setFontSize(6)))
					.setCellMarginRight(0, 0, "0.04cm");
			rightSideSectionsTableBody.add(
					0,
					0,
					report.createParagraph().setBackgroundColor("#CCCCCC")
							.setTextIndent("0.1cm").setFontSize(9)
							.setFontWeight("bold").setBorderBeforeColor(
									"#febe10").setBorderBeforeStyle("solid")
							.setBorderBeforeWidth("0.06cm")
							.setBorderBottomColor("#CCCCCC")
							.setBorderBottomStyle("solid")
							.setBorderBottomWidth("0.10cm").add(
									report.createText("Participant Status")))
					.setCellMarginLeft(0, 0, "0.04cm");
			// tableBody.spanCells(2, 0, 1, 2);
		} else {
			leftSideSectionsTableBody
					.add(0, 0, report.createParagraph().setBackgroundColor(
							"#CCCCCC").setTextIndent("0.1cm").setFontSize(9)
							.setFontWeight("bold").setBorderBeforeColor(
									"#febe10").setBorderBeforeStyle("solid")
							.setBorderBeforeWidth("0.06cm")
							.setBorderBottomColor("#CCCCCC")
							.setBorderBottomStyle("solid")
							.setBorderBottomWidth("0.10cm").add(
									report.createText("Contract Assets")).add(
									report.createText("1").setBaseLineShift(
											"super").setFontSize(6))).setCellMarginRight(0, 0, "0.04cm");
			rightSideSectionsTableBody.add(0, 0, report.createParagraph().setBackgroundColor(
			"#CCCCCC").setTextIndent("0.1cm").setFontSize(9).setFontWeight(
			"bold").setBorderBeforeColor("#febe10").setBorderBeforeStyle(
			"solid").setBorderBeforeWidth("0.06cm").setBorderBottomColor(
			"#CCCCCC").setBorderBottomStyle("solid").setBorderBottomWidth(
			"0.10cm").setBorderBottomColor("#CCCCCC").setBorderBottomStyle(
			"solid").setBorderBottomWidth("0.10cm").add(
			report.createText("Contributions & Withdrawals")));
			rightSideSectionsTableBody.setCellMarginLeft(0, 0, "0.04cm");
		}

		PdfTableBody tableBodyPlanAssets = report.createTableBody(7, 2);
		PdfTable contractAssetsTable = report.createTable(new String[] {
				"6.0cm", "3.4cm" },
				new PdfTableRegion[] { tableBodyPlanAssets });
		tableBodyPlanAssets.setColumnTextAlign(1, "left")
				.setColumnPaddingLeft(0, "0.1cm");
		leftSideSectionsTableBody.add(1, 0, contractAssetsTable);

		tableBodyPlanAssets.add(0, 0, report
				.createText("Total Contract Assets").setFontWeight("bold"))
				.setCellPaddingTop(0, 0, "0.1cm");
		tableBodyPlanAssets.add(0, 1, report.createText(
				getFormattedCurrency(vo.getPlanAssets()
						.getTotalPlanAssetsAmount().toString(), true))
				.setFontWeight("bold")).setCellPaddingTop(0, 1, "0.1cm");
		tableBodyPlanAssets.add(1, 0, "Allocated Assets").setCellPaddingTop(1, 0, "0.1cm");
		tableBodyPlanAssets.add(1, 1, vo.getPlanAssets()
				.getAllocatedAssetsAmount() != null ? getFormattedCurrency(vo
				.getPlanAssets().getAllocatedAssetsAmount().toString(), true)
				: "").setCellPaddingTop(1, 1, "0.1cm");

		boolean show = !(vo.getPlanAssets().getCashAccountAmount()
				.equals(new BigDecimal("0.00")));
		if (show) {
			tableBodyPlanAssets.add(2, 0, "Cash account").setCellPaddingTop(2, 0, "0.1cm");
			tableBodyPlanAssets
					.add(
							2,
							1,
							getFormattedCurrency(
									vo.getPlanAssets().getCashAccountAmount()
											.toString(), true)).setCellPaddingTop(2, 1, "0.1cm");
		} else {
			tableBodyPlanAssets.add(2, 0, "");
			tableBodyPlanAssets.add(2, 1, "");
		}

		show = !(vo.getPlanAssets().getUninvestedAssetsAmount()
				.equals(new BigDecimal("0.00")));
		if (show) {
			tableBodyPlanAssets.add(3, 0, "Pending transactions").setCellPaddingTop(3, 0, "0.1cm");
			tableBodyPlanAssets
					.add(
							3,
							1,
							getFormattedCurrency(
									vo.getPlanAssets().getUninvestedAssetsAmount()
											.toString(), true)).setCellPaddingTop(3, 1, "0.1cm");
		} else {
			tableBodyPlanAssets.add(3, 0, "");
			tableBodyPlanAssets.add(3, 1, "");
		}
		
		show = displayPba;
		if (show) {
			tableBodyPlanAssets.add(
					4,
					0,
					report.createParagraph().add(
							report.createText("Personal Brokerage Account"))
							.add(
									report.createText("†").setBaseLineShift(
											"super").setFontSize(6)))
					.setCellMarginTop(4, 0, "0.0cm").setCellPaddingTop(4, 0, "0.1cm");
			tableBodyPlanAssets
			.add(
					4,
					1,
					getFormattedCurrency(
							vo.getPlanAssets()
									.getPersonalBrokerageAccountAmount()
									.toString(), true)).setCellPaddingTop(4, 1, "0.1cm");
		} else {
			tableBodyPlanAssets.add(4, 0, "");
			tableBodyPlanAssets.add(4, 1, "");
		}

		show = displayLoan;
		if (show) {
			tableBodyPlanAssets.add(5, 0, "Loan Assets").setCellPaddingTop(5, 0, "0.1cm");
			tableBodyPlanAssets
					.add(
							5,
							1,vo.getPlanAssets().getLoanAssets() != null?
							getFormattedCurrency(
									vo.getPlanAssets().getLoanAssets().toString(),
									true):"$0.00").setCellPaddingTop(5, 1, "0.1cm");
		} else {
			tableBodyPlanAssets.add(5, 0, "");
			tableBodyPlanAssets.add(5, 1, "");
		}
		
		tableBodyPlanAssets.add(6, 0,
				"Asset figures current as of market close on the selected \"as of\" date.")
                .setCellPaddingTop(6, 0, "0.1cm");
		tableBodyPlanAssets.spanCells(6, 0, 1, 2);

		if (!definedBenefit && isRecentDate) {
			/*
			 * Total line is always there.
			 */
			int rowCount = 1;
			PdfGraphic participantStatusPdfGraphic = null;
			PdfParagraph participantStatusLegend = null;
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
				if (vo.getContractAssetsByRisk().getActiveParticipantsNumber() != 0) {
					rowCount++;
				}
				if (vo.getContractAssetsByRisk()
						.getInactiveWithBalanceParticipantsNumber() != 0)
					rowCount++;
				if (vo.getContractAssetsByRisk()
						.getInactiveWithUMParticipantsNumber() != 0)
					rowCount++;
			}

			PdfTableBody partStatusTableBody = report.createTableBody(rowCount,
					4);
			PdfTable partStatusTable = report.createTable(new String[] {
					"0.4cm", "4.1cm", "1.0cm", "4.0cm" },
					new PdfTableRegion[] { partStatusTableBody });
			partStatusTableBody.setColumnTextAlign(2,
					PdfConstants.ALIGNMENT_RIGHT);

			if (!vo.getContractAssetsByRisk().getHasEmployeeMoneyType()) {
				int x = 0;
				if (vo.getContractAssetsByRisk()
						.getActiveContributingParticipantsNumber() != 0) {
					partStatusTableBody.setCellMargin(x, 0, "0cm");
					participantStatusLegend = generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_AND_CONTRIBUTING);
					partStatusTableBody.add(x, 0, participantStatusLegend);
					partStatusTableBody.add(x, 1, "  Active").setCellMargin(x,
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
					participantStatusLegend = generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_NO_BALANCE);
					partStatusTableBody.add(x, 0, participantStatusLegend);
					partStatusTableBody.add(x, 1, "  Active with no Balance")
							.setCellMargin(x, 1, "0cm");
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
					participantStatusLegend = generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_NOT_CONTRIBUTING);
					partStatusTableBody.add(x, 0, participantStatusLegend);
					partStatusTableBody.add(x, 1,
							"  Active but not contributing").setCellMargin(x,
							1, "0cm");
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
									generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_OPTED_OUT));
					partStatusTableBody.add(x, 1, "  Active opted out")
							.setCellMargin(x, 1, "0cm");
					partStatusTableBody.add(
							x,
							2,
							String.valueOf(vo.getContractAssetsByRisk()
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
									generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE));
					partStatusTableBody.add(x, 1, "  Inactive with balance")
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
									generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY));
					partStatusTableBody.add(x, 1,
							"  Inactive unvested money only").setCellMargin(x,
							1, "0cm");
					partStatusTableBody.add(
							x,
							2,
							String.valueOf(vo.getContractAssetsByRisk()
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
									generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_OPTED_OUT_NOT_VESTED));
					partStatusTableBody.add(x, 1, "  Opted out not vested")
							.setCellMargin(x, 1, "0cm");
					partStatusTableBody.add(
							x,
							2,
							String.valueOf(vo.getContractAssetsByRisk()
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
									generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_OPTED_OUT_ZERO_BALANCE));
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
				partStatusTableBody.add(x, 1,
						report.createText("  Total").setFontWeight("bold"))
						.setCellMargin(x, 1, "0cm");
				partStatusTableBody.add(
						x,
						2,
						report.createText(
								String.valueOf(vo.getContractAssetsByRisk()
										.getTotalParticipantsNumber()))
								.setFontWeight("bold")).setCellMargin(x, 2,
						"0cm");
				x++;
			} else {
				int x = 0;
				if (vo.getContractAssetsByRisk().getActiveParticipantsNumber() != 0) {
					partStatusTableBody.setCellMargin(x, 0, "0cm");
					partStatusTableBody
							.add(
									x,
									0,
									generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE));
					partStatusTableBody.add(x, 1, "  Active").setCellMargin(x,
							1, "0cm");
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
									generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE));
					partStatusTableBody.add(x, 1, "  Inactive with balance")
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
									generateRectangle(BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY));
					partStatusTableBody.add(x, 1,
							"  Inactive Uninvested Money only").setCellMargin(
							x, 1, "0cm");
					partStatusTableBody.add(
							x,
							2,
							String.valueOf(vo.getContractAssetsByRisk()
									.getInactiveWithUMParticipantsNumber()))
							.setCellMargin(x, 2, "0cm");
					x++;
				}

				partStatusTableBody.setCellMargin(x, 0, "0cm");
				partStatusTableBody.add(x, 0, "");
				partStatusTableBody.add(x, 1,
						report.createText("  Total").setFontWeight("bold"))
						.setCellMargin(x, 1, "0cm");
				partStatusTableBody.add(
						x,
						2,
						report.createText(
								String.valueOf(vo.getContractAssetsByRisk()
										.getTotalParticipantsNumber()))
								.setFontWeight("bold")).setCellMargin(x, 2,
						"0cm");
				x++;
			}

			participantStatusPdfGraphic = getDynamicGraphic(PieChartUtil
					.createURLString(participantStatusPieChartBean));
			partStatusTableBody.add(0, 3, report.createParagraph().add(
					participantStatusPdfGraphic));
			partStatusTableBody.spanCells(0, 3, rowCount, 1);
			partStatusTableBody.setCellMargin(0, 3, "0cm");
			partStatusTableBody.setCellPaddingLeft(0, 3, "0.2cm");
			partStatusTableBody.setCellPaddingRight(0, 3, "0.5cm");
			partStatusTableBody.setCellPaddingBottom(0, 3, "0.1cm");

			rightSideSectionsTableBody.add(1, 0, partStatusTable);
			// tableBody.add(8, 1, " ");

			rightSideSectionsTableBody.setCellPaddingLeft(1, 0, "0.2cm").setCellPaddingRight(1,
					0, "1cm").setCellPaddingTop(1, 0, "0.1cm").setCellMargin(1,
					0, "0cm");
			//////////////////
			rightSideSectionsTableBody.add(2, 0, report.createParagraph().setBackgroundColor(
			"#CCCCCC").setTextIndent("0.1cm").setFontSize(9).setFontWeight(
			"bold").setBorderBeforeColor("#febe10").setBorderBeforeStyle(
			"solid").setBorderBeforeWidth("0.06cm").setBorderBottomColor(
			"#CCCCCC").setBorderBottomStyle("solid").setBorderBottomWidth(
			"0.10cm").setBorderBottomColor("#CCCCCC").setBorderBottomStyle(
			"solid").setBorderBottomWidth("0.10cm").add(
			report.createText("Contributions & Withdrawals")));
			rightSideSectionsTableBody.setCellMarginLeft(2, 0, "0.04cm");
			
			// Contributions and Withdrawals bar chart
			Iterator itSeries = contributionWithdrawalsBarChartBean.getAllSeries()
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
				rightSideSectionsTableBody.add(3, 0, contributionWithdrawals);
			}

			rightSideSectionsTableBody.add(3, 0, report.createParagraph().add(
					getDynamicGraphic(ImageUrl.getChartImageUrl(
							contributionWithdrawalsBarChartBean, false))));

			rightSideSectionsTableBody.setCellPaddingLeft(3, 0, "0.1cm").setCellPaddingRight(3, 0,
					"0.1cm").setCellPaddingTop(3, 0, "0.1cm").setCellMargin(3, 0,
					"0cm");

		} else {
			if (definedBenefit) {
				// Contributions and Withdrawals bar chart
				Iterator itSeries = contributionWithdrawalsBarChartBean.getAllSeries()
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
					rightSideSectionsTableBody.add(1, 0, contributionWithdrawals);
				}

				rightSideSectionsTableBody.add(1, 0, report.createParagraph().add(
						getDynamicGraphic(ImageUrl.getChartImageUrl(
								contributionWithdrawalsBarChartBean, false))));

				rightSideSectionsTableBody.setCellPaddingLeft(1, 0, "0.1cm").setCellPaddingRight(1, 0,
						"0.1cm").setCellPaddingTop(1, 0, "0.1cm").setCellMargin(1, 0,
						"0cm");
				//////////////////
				rightSideSectionsTableBody.add(2, 0, report.createParagraph());
				rightSideSectionsTableBody.spanCells(2, 0, 1, 1);
				rightSideSectionsTableBody.add(3, 0, report.createParagraph());
				rightSideSectionsTableBody.spanCells(3, 0, 1, 1);
				
			}
			else {
				String infoMsg = ContentHelper.getContentText(
						BDContentConstants.PARTICIPANT_STATUS_NOT_AVAILABLE,
			                ContentTypeManager.instance().MISCELLANEOUS, null);
				rightSideSectionsTableBody
						.add(
								1,
								0,
								report.createParagraph("Information Message").setFontWeight("bold"))
										.setCellPaddingLeft(1, 0, "0.4cm")
										.setCellPaddingTop(1, 0, "0.6cm")
										.setCellPaddingBottom(1, 0, "0.6cm");
				rightSideSectionsTableBody
				.add(
						1,
						0,
						report.createParagraph("1. " +infoMsg)).setCellPaddingLeft(1, 0, "0.3cm")
								.setCellPaddingTop(1, 0, "0.3cm");
				// tableBody.spanCells(2, 1, 1, 2);
				rightSideSectionsTableBody.add(2, 0, report.createParagraph().setBackgroundColor(
				"#CCCCCC").setTextIndent("0.1cm").setFontSize(9).setFontWeight(
				"bold").setBorderBeforeColor("#febe10").setBorderBeforeStyle(
				"solid").setBorderBeforeWidth("0.06cm").setBorderBottomColor(
				"#CCCCCC").setBorderBottomStyle("solid").setBorderBottomWidth(
				"0.10cm").setBorderBottomColor("#CCCCCC").setBorderBottomStyle(
				"solid").setBorderBottomWidth("0.10cm").add(
				report.createText("Contributions & Withdrawals")));
				rightSideSectionsTableBody.setCellMarginLeft(2, 0, "0.04cm");
				
				// Contributions and Withdrawals bar chart
				Iterator itSeries = contributionWithdrawalsBarChartBean.getAllSeries()
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
					rightSideSectionsTableBody.add(3, 0, contributionWithdrawals);
				}

				rightSideSectionsTableBody.add(3, 0, report.createParagraph().add(
						getDynamicGraphic(ImageUrl.getChartImageUrl(
								contributionWithdrawalsBarChartBean, false))));

				rightSideSectionsTableBody.setCellPaddingLeft(3, 0, "0.1cm").setCellPaddingRight(3, 0,
						"0.1cm").setCellPaddingTop(3, 0, "0.1cm").setCellMargin(3, 0,
						"0cm");
			}
		}

		/*
		 * Assets growth section
		 */
		leftSideSectionsTableBody.add(2, 0, report.createParagraph().setBackgroundColor(
				"#CCCCCC").setTextIndent("0.1cm").setFontSize(9).setFontWeight(
				"bold").setBorderBeforeColor("#febe10").setBorderBeforeStyle(
				"solid").setBorderBeforeWidth("0.06cm").setBorderBottomColor(
				"#CCCCCC").setBorderBottomStyle("solid").setBorderBottomWidth(
				"0.10cm").setBorderBottomColor("#CCCCCC").setBorderBottomStyle(
				"solid").setBorderBottomWidth("0.10cm").add(
				report.createText("Asset Growth")));
		leftSideSectionsTableBody.setCellMarginRight(2, 0, "0.04cm");
		

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
			leftSideSectionsTableBody.add(3, 0, assetGrowthTable);
		}
		assetGrowthBarChartBean.setWidth(360);
		leftSideSectionsTableBody.add(3, 0, report.createParagraph().add(
				getDynamicGraphic(ImageUrl.getChartImageUrl(
						assetGrowthBarChartBean, false))));

		leftSideSectionsTableBody.setCellPaddingLeft(3, 0, "0.2cm").setCellPaddingTop(3, 0,
				"0.1cm").setCellMargin(3, 0, "0cm");

		

		/*
		 * Assets allocation by risk category section
		 */
		tableBody.add(3, 0, report.createParagraph().setBackgroundColor(
				"#CCCCCC").setTextIndent("0.1cm").setFontSize(9).setFontWeight(
				"bold").setBorderBeforeColor("#febe10").setBorderBeforeStyle(
				"solid").setBorderBeforeWidth("0.06cm").setBorderBottomColor(
				"#CCCCCC").setBorderBottomStyle("solid").setBorderBottomWidth(
				"0.10cm").add(
				report.createText("Asset Allocation by Risk/Return Category")));
		tableBody.spanCells(3, 0, 1, 2);

		PdfTableBody assetsAllocByRiskMainTableBody = report.createTableBody(2,
				2);

		PdfTable assetsAllocByRiskMainTable = report.createTable(new String[] {
				"7.2cm", "11.8cm" },
				new PdfTableRegion[] { assetsAllocByRiskMainTableBody });
		assetsAllocByRiskMainTableBody.add(0, 0, report.createParagraph().add(
				report.createText("Allocated Contract Assets").setFontWeight(
						"bold")));
		assetsAllocByRiskMainTableBody.setCellMargin(0, 0, "0cm");

		if (!definedBenefit) {
			assetsAllocByRiskMainTableBody.add(0, 1, report.createText(
					"Asset Allocation by Age Group").setFontWeight("bold"));
			assetsAllocByRiskMainTableBody.setCellMargin(0, 1, "0cm");
		} else {
			assetsAllocByRiskMainTableBody.add(0, 1, report.createText(" "));
		}

		tableBody.add(4, 0, assetsAllocByRiskMainTable);
		tableBody.spanCells(4, 0, 1, 2);
		tableBody.setCellMargin(4, 0, "0cm").setCellPaddingLeft(4, 0, "0.2cm")
				.setCellPaddingRight(4, 0, "0.1cm");

		tableBody.setCellPaddingTop(4, 0, "0.1cm").setCellPaddingBottom(4, 0,
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
			PdfParagraph legend = generateRectangle(BDConstants.AssetAllocationPieChart.COLOR_LIFECYCLE);
			allocAssetsTableBody.add(x, 0, legend);
			allocAssetsTableBody.add(x, 1, "  Target Date").setCellMargin(0, 1,
					"0cm");
			allocAssetsTableBody.add(x, 2, getFormattedCurrency(String
					.valueOf(vo.getContractAssetsByRisk().getTotalAssetsByRisk(
							"LC")), true));
			allocAssetsTableBody.setCellMargin(x, 2, "0cm");
			x++;
		}
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		PdfParagraph legend = generateRectangle(BDConstants.AssetAllocationPieChart.COLOR_AGRESSIVE);
		allocAssetsTableBody.add(x, 0, legend);
		allocAssetsTableBody.add(x, 1, "  Aggressive Growth").setCellMargin(x,
				1, "0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("AG")), true));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		x++;
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		legend = generateRectangle(BDConstants.AssetAllocationPieChart.COLOR_GROWTH);
		allocAssetsTableBody.add(x, 0, legend);
		allocAssetsTableBody.add(x, 1, "  Growth").setCellMargin(x, 1, "0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("GR")), true));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		x++;
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		legend = generateRectangle(BDConstants.AssetAllocationPieChart.COLOR_GROWTH_INCOME);
		allocAssetsTableBody.add(x, 0, legend);
		allocAssetsTableBody.add(x, 1, "  Growth & Income").setCellMargin(x, 1,
				"0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("GI")), true));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		x++;
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		legend = generateRectangle(BDConstants.AssetAllocationPieChart.COLOR_INCOME);
		allocAssetsTableBody
				.add(
						x,
						0,
						generateRectangle(BDConstants.AssetAllocationPieChart.COLOR_INCOME));
		allocAssetsTableBody.add(x, 1, "  Income").setCellMargin(x, 1, "0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("IN")), true));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");
		x++;
		allocAssetsTableBody.setCellMargin(x, 0, "0cm");
		legend = generateRectangle(BDConstants.AssetAllocationPieChart.COLOR_CONSERVATIVE);
		allocAssetsTableBody.add(x, 0, legend);
		allocAssetsTableBody.add(x, 1, "  Conservative").setCellMargin(x, 1,
				"0cm");
		allocAssetsTableBody.add(x, 2, getFormattedCurrency(String.valueOf(vo
				.getContractAssetsByRisk().getTotalAssetsByRisk("CN")), true));
		allocAssetsTableBody.setCellMargin(x, 2, "0cm");

		if (displayPba) {
			x++;
			allocAssetsTableBody.setCellMargin(x, 0, "0cm");
			legend = generateRectangle(BDConstants.AssetAllocationPieChart.COLOR_PBA);
			allocAssetsTableBody.add(x, 0, legend);
			allocAssetsTableBody.add(x, 1, "  Personal Brokerage Account")
					.setCellMargin(x, 1, "0cm");
			allocAssetsTableBody.add(x, 2, getFormattedCurrency(String
					.valueOf(vo.getContractAssetsByRisk().getTotalAssetsByRisk(
							"PB")), true));
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
			// asset allocation by age group and participant status do not apply
			// to
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

			Double totalWidth = 10.5;
            Double widthPerAgeGroup = 2.1; // By default, this is the width per age Group. This
                                           // number is derived from 10.5 / 5 = 2.1cms.
            
                widthPerAgeGroup = totalWidth / counter;
            
			
			for (int i = 0; i < counter; i++) {
				sa[i] = widthPerAgeGroup + "cm";
				assetAllocAgeGroupTableBody.setColumnTextAlign(i,
						PdfConstants.ALIGNMENT_CENTER);
			}

			PdfTable assetAllocAgeGroupTable = report.createTable(sa,
					new PdfTableRegion[] { assetAllocAgeGroupTableBody });

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

				assetsAllocByRiskMainTableBody.add(1, 1, report
						.createParagraph().setBorderBeforeColor("#FFFFFF")
						.setBorderBeforeStyle("solid").setBorderBeforeWidth(
								"0.4cm")
						.add(report.createText(text.toString())));
				assetsAllocByRiskMainTableBody
						.setCellMarginRight(1, 1, "0.1cm");
			}

			/*
			 * Participant status
			 */

		} // end if (! definedBenefit asset allocation by age group)

		contentWithFo = replaceContentWithFO(ContentUtility.getPageFooter(layoutPageBean, null));

		if (StringUtils.isNotBlank(contentWithFo)) {
			report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
					.setBorderBeforeStyle("solid").setBorderBeforeWidth("0.7cm").add(
											ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)));
		}
		
		contentWithFo = replaceContentWithFO(ContentUtility.getContentAttribute(
				selectedReportingDateFootnoteBean, "text"));
		
		if (StringUtils.isNotBlank(contentWithFo)) {
			report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
					.setBorderBeforeStyle("solid").setBorderBeforeWidth("0.1cm")
					.add(
							report.createText("1").setBaseLineShift("super")
									.setFontSize(6)).add(
											ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)));
		}
	
		contentWithFo = replaceContentWithFO(ContentUtility.getContentAttribute(
				assetsAllocatedActiveParticipantFootnoteBean, "text"));
		
		if (StringUtils.isNotBlank(contentWithFo)) {	
			report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
					.setBorderBeforeStyle("solid").setBorderBeforeWidth("0.1cm")
					.add(
                            report.createText(ContentUtility
                                    .createContentDOMNodeFromXslFOString(contentWithFo))));
		}

		if (!definedBenefit && displayPba) {
			contentWithFo = replaceContentWithFO(ContentUtility.getContentAttribute(
					pbaFootnoteBean, "text"));
			if (StringUtils.isNotBlank(contentWithFo)) {
				report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
						.setBorderBeforeStyle("solid").setBorderBeforeWidth("0.1cm")
						.add(
								report.createText("†").setBaseLineShift("super")
										.setFontSize(6)).add(
												ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)));
			}
		}

		contentWithFo = replaceContentWithFO(ContentUtility.getContentAttribute(
				globalFootnoteText1FootnoteBean, "text"));
		
		if (StringUtils.isNotBlank(contentWithFo)) {
			report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
					.setBorderBeforeStyle("solid").setBorderBeforeWidth("0.4cm")
					.add(ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)));
		}

		contentWithFo = replaceContentWithFO(ContentUtility.getContentAttribute(
				globalFootnoteText2FootnoteBean, "text"));
		
		if (StringUtils.isNotBlank(contentWithFo)) {
			report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
					.setBorderBeforeStyle("solid").setBorderBeforeWidth("0.1cm")
					.add(ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)));
		}
		
		contentWithFo = replaceContentWithFO(ContentUtility.getContentAttribute(
				globalFootnoteText3FootnoteBean, "text"));
		
		if (StringUtils.isNotBlank(contentWithFo)) {
			report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
					.setBorderBeforeStyle("solid").setBorderBeforeWidth("0.1cm")
					.add(ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)));
		}

		contentWithFo = replaceContentWithFO(ContentUtility.getContentAttribute(
				globalFootnoteText4FootnoteBean, "text"));
		
		if (StringUtils.isNotBlank(contentWithFo)) {
	        report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
	                .setBorderBeforeStyle("solid").setBorderBeforeWidth("0.1cm")
	                .add(ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)));
		}

		contentWithFo = replaceContentWithFO(ContentUtility.getContentAttribute(
				globalFootnoteText5FootnoteBean, "text"));
		
		if (StringUtils.isNotBlank(contentWithFo)) {
	        report.add(report.createParagraph().setBorderBeforeColor("#FFFFFF")
	                .setBorderBeforeStyle("solid").setBorderBeforeWidth("0.1cm")
	                .add(ContentUtility.createContentDOMNodeFromXslFOString(contentWithFo)));
		}

		report.add((PdfParagraph) report.createParagraph().setId("end"));

		// report.add(ContentUtility.getPageDisclaimer(layoutPageBean, new
		// String[]{}, -1));
		// Print as XSL:FO. This is for debugging only.
		// report.printAsFO(System.out);
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
		Environment env = Environment.getInstance();
		
        String portNumber = System.getProperty("webcontainer.http.port") == null ? "9081" : System.getProperty("webcontainer.http.port");
		if (logger.isDebugEnabled()) {
		    logger.debug("About to create URL with http://localhost:" + portNumber + "/" + fileName);
		}
		URL url = null;
		try {
         
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

	/**
	 * @param Object
	 *            value
	 * @param boolean
	 *            showCurrencySign
	 * @return String or null
	 */
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
			LogUtility.logSystemException(BDConstants.PS_APPLICATION_ID, se);
			return null;
		}
	}

	/**
     * This method will replace the text with the FO-content.
     * 
     * @param text
     * @return
     */
	public static String replaceContentWithFO(String text) {
		String contentWithFo = null;
		if (!StringUtils.isBlank(text)) {
	        text = PATTERN_STRONG_TAG.matcher(text).replaceAll(BOLD_TAG);
	        text = PATTERN_STRONG_END_TAG.matcher(text).replaceAll(BOLD_END_TAG);
	        contentWithFo = ContentUtility.convertCMAContentToXSLFO(text);
		}
        return contentWithFo;
    }
}
