package com.manulife.pension.ireports.report;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.report.util.BottomAlignCellDecoratorFactory;
import com.manulife.pension.ireports.report.util.CMAContentHelper;
import com.manulife.pension.ireports.report.util.ImageBuilder;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.platform.utility.util.ContentHelper;
import com.manulife.util.pdf.PdfElement;
import com.manulife.util.pdf.PdfElementContainer;
import com.manulife.util.pdf.PdfFlow;
import com.manulife.util.pdf.PdfPageSequence;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfStaticContent;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableBody;
import com.manulife.util.pdf.PdfTableRegion;
import com.manulife.util.pdf.PdfText;

public abstract class AbstractPageSequence extends PdfElementContainer implements
		ReportFormattingConstants{

	protected PdfPageSequence pageSequence;
	protected ReportOptions options;
    protected int footnoteDisclosureContentID; // THis will hold the Content ID to be shown along with footer.
	private boolean isLandscape;
	private boolean isLegalSize;
	private ImageBuilder imageBuilder;

	public AbstractPageSequence(StandardReportBuilder report, ReportOptions options,
            boolean isLandscape,
			boolean isLegalSize) {
		super(report);
		this.options = options;
		this.isLandscape = isLandscape;
		this.isLegalSize = isLegalSize;
		this.imageBuilder = new ImageBuilder(report);
		this.footnoteDisclosureContentID = StandardReportsConstants.GLOBAL_FOOTNOTE_DISCLOSURE_ID;
	}
	
	public AbstractPageSequence(StandardReportBuilder report, ReportOptions options,
            boolean isLandscape, boolean isLegalSize, int footnoteDisclosureContentID) {
		super(report);
		this.options = options;
		this.isLandscape = isLandscape;
		this.isLegalSize = isLegalSize;
		this.imageBuilder = new ImageBuilder(report);
		this.footnoteDisclosureContentID = footnoteDisclosureContentID;
	}
	
	public void init() {
		pageSequence = report.createPageSequence("content");
		addElement((PdfElement) pageSequence);
		setContainerRootElement(pageSequence);
		pageSequence.setLanguage("en");
		// This attribute set is required to get rid of blank page.
        pageSequence.setForcePageCount("no-force");
		pageSequence.add(createFirstPageStaticContent((StandardReportBuilder) report));
		pageSequence.add(createSubsequentPagesStaticContent((StandardReportBuilder) report));
		pageSequence.add(createFooterStaticContent((StandardReportBuilder) report));
	}
	
	protected abstract PdfStaticContent createFirstPageStaticContent(StandardReportBuilder report );
	protected abstract PdfStaticContent createSubsequentPagesStaticContent(StandardReportBuilder report );

	protected PdfStaticContent createFooterStaticContent(StandardReportBuilder report) {
		PdfStaticContent footerStaticContent = report.createStaticContent("footer");
		PdfParagraph footerTableParagraph = report.createParagraph();
		
		PdfTableBody footerTableBody = report.createTableBody(3, 2, new BottomAlignCellDecoratorFactory());

		String[] tableColumnSizes = null;
		if (this.isLandscape) {
			tableColumnSizes = new String[] { "9.5in", "1.0in" };
		} else if (this.isLegalSize) {
            tableColumnSizes = new String[] { "12.5in", "1.0in" };
		} else {
			tableColumnSizes = new String[]{"7.0in", "1.0in"};
		}
		
		PdfTable footerTable = report.createTable(tableColumnSizes, new PdfTableRegion[] {footerTableBody});
		
		footerTableBody.setRowHeight(0, "0.1in");
		footerTableBody.setRowHeight(1, "0.3in");
		footerTableBody.setRowHeight(2, "0.2in");
		
		footerTableBody.add(0, 0, "");
        footerTableBody.add(0, 1, "");
        
		if (options.isSelectedFromHistoricalReport() && StandardReportsUtils.isNewYork(options.getCompanyId())){
			footerTableBody.add(1, 0, report.createParagraph("Group annuity contracts are issued by John Hancock Life Insurance Company of New York (John Hancock New York).")
					.setTextAlignment("left")
					.setFontFamily(FRUTIGER55ROMAN)
					.setFontSize(10));
			footerTableBody.add(1, 1, report.createParagraph(
					report.createGraphic(this.getClass().getResource(
                                    "/images/New_York_TAB.jpg"))
													.setContentHeight("13px")
													.setContentWidth("130px"))
					.setTextAlignment("right"));
		} else {
            footerTableBody.add(1, 0, "");
            footerTableBody.add(1, 1, "");
        }
		// This is used to suppress the Page no as part of ACR REWRITE (iReport)
		if (options.isSelectedFromHistoricalReport()) {
		footerTableBody.add(2,0,report.createParagraph()
				.add(report.createText("Page "))
				.add(report.createPageNumber())
				.add(report.createText(" of "))
				.add(report.createPageNumberCitation("end"))
				.add(report.createText(" NOT VALID WITHOUT ALL PAGES."))
				.setFontFamily(FRUTIGER67BOLD_CN)
				.setFontSize(8)
				.setTextAlignment("left")
				.add(report.createText(" "))
				.add(getFootnoteDisclosureText(report)
						.setFontFamily(FRUTIGER67BOLD_CN)
						.setFontSize(8)))
			.spanCells(2,0,1,2);
		}
		else
		{
			footerTableBody.add(2,0,"").spanCells(2, 0, 1, 2);
		}
		footerTableParagraph.add(footerTable);
		footerStaticContent.add(footerTableParagraph);
		
		return footerStaticContent;
			
	}

	/**
	 * Get the Footnote Disclosure text to be shown besides the Page Numbering text.
	 * @param report
	 * @return - Footnote Disclosure text
	 */
	protected PdfText getFootnoteDisclosureText(StandardReportBuilder report) {
		String footnoteDisclosureText = ContentHelper.getContentText(getFootnoteDisclosureContentID(), 
					ContentTypeManager.instance().FOOTNOTE, 
					CMAContentHelper.getLocation(options.getCompanyId()));
		
		PdfText disclosureText = CMAContentHelper.createInlineForCMAText(report, footnoteDisclosureText);
		
		return disclosureText;
	}
	
	/**
	 * This method returns the content ID for showing the footnote disclosure.
	 * @return
	 */
	protected int getFootnoteDisclosureContentID() {
		return footnoteDisclosureContentID;
	}
	
	/**
	 * This method sets the content ID for showing the footnote disclosure.
	 * @param contentID
	 */
	protected void setFootnoteDisclosureContentID(int contentID) {
		footnoteDisclosureContentID = contentID;
	}
	
    /**
     * This method returns back the PdfParagraph which will be printed as the title header. There
     * are totally 5 titles. The pageTitleStaticParagraphMap may contain the "title id" as the key
     * and the value as the PdfParagraph which should be printed. If this Map does not contain the
     * title PdfParagraph, then we create the PdfParagraph by getting the title information from
     * ReportOptions object.
     * 
     * @param title - title id = can contain values title1 / title2 / title3 / title4 / title5
     * @param pageTitleStaticParagraphMap - Map that contains the title id as the key and
     *            PdfParagraph to be printed as the value.
     * @return - PdfParagraph to be printed as the title.
     */
    protected PdfParagraph getTitleParagraph(String title,
            Map<String, PdfParagraph> pageTitleStaticParagraphMap) {
        PdfParagraph pgSubTitleParagraph = null;

        if (pageTitleStaticParagraphMap != null && pageTitleStaticParagraphMap.get(title) != null) {
            pgSubTitleParagraph = pageTitleStaticParagraphMap.get(title);
        } else {
            Map<String, String> reportTitleMap = options.getReportTitle();
            if (reportTitleMap != null && !StringUtils.isBlank(reportTitleMap.get(title))) {
                pgSubTitleParagraph = report.createParagraph(reportTitleMap.get(title));
                applyDefaultAttributes(title, pgSubTitleParagraph);
            }
        }

        return pgSubTitleParagraph;
    }

    /**
     * This method sets the attributes of the paragraph to their default values.
     * 
     * @param title
     * @param pgSubTitleParagraph
     */
    protected void applyDefaultAttributes(String title, PdfParagraph pgSubTitleParagraph) {
        if (StandardReportsConstants.TITLE_1.equals(title)) {
            pgSubTitleParagraph.setFontFamily(FRUTIGER67BOLD_CN).setFontSize(
                    getHeadingFontSize(title))
                    .setTextAlignment("right");
        } 
        else if (StandardReportsConstants.TITLE_2.equals(title)) {
            pgSubTitleParagraph.setFontFamily(FRUTIGER57CN).setFontSize(14).setTextAlignment(
                    "right");
        } 
        else if (StandardReportsConstants.TITLE_3.equals(title)) {
            pgSubTitleParagraph.setFontFamily(FRUTIGER57CN).setFontSize(
                    FONT_SIZE_CONTRACT_OR_SHORTLIST_DESC).setTextAlignment("right");
        } else if (StandardReportsConstants.TITLE_4.equals(title)) {
            pgSubTitleParagraph.setFontFamily(FRUTIGER57CN).setFontSize(
                    FONT_SIZE_CONTRACT_OR_SHORTLIST_DESC).setTextAlignment("right");
        } 
        else if (StandardReportsConstants.TITLE_5.equals(title)) {
            pgSubTitleParagraph.setFontFamily(FRUTIGER57CN).setFontSize(
                    FONT_SIZE_CONTRACT_OR_SHORTLIST_DESC).setTextAlignment("right");

            String footnoteReference = options.getContractShortlistOptions().getFootnoteReference();
            if (StringUtils.isNotBlank(footnoteReference)) {
                pgSubTitleParagraph.add(report.createText(footnoteReference + " ")
                        .setBaseLineShift("super").setFontSize(FONT_SIZE_FOOTNOTE_SYMBOL));
            }
        } 
        else if (StandardReportsConstants.TITLE_6.equals(title)) {
            pgSubTitleParagraph.setFontFamily(FRUTIGER47LIGHT_CN).setFontSize(11).setTextAlignment(
                    "right");
        }
    }
    
    protected int getHeadingFontSize(String title) {
        return 16;
    }
    
	public boolean isLandscape() {
		return isLandscape;
	}
	
	public boolean isLegalSize() {
		return isLegalSize;
	}

	public PdfPageSequence add(PdfStaticContent content) {
		return pageSequence.add(content);
	}

	public PdfPageSequence add(PdfFlow content) {
		return pageSequence.add(content);
	}

	public ImageBuilder getImageBuilder() {
		return imageBuilder;
	}
}
