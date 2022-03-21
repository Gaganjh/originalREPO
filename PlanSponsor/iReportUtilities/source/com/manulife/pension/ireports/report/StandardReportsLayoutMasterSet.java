package com.manulife.pension.ireports.report;

import com.manulife.util.pdf.PdfConditionalPageMasterReference;
import com.manulife.util.pdf.PdfElement;
import com.manulife.util.pdf.PdfElementContainer;
import com.manulife.util.pdf.PdfLayoutMasterSet;
import com.manulife.util.pdf.PdfPageSequenceMaster;
import com.manulife.util.pdf.PdfRegion;
import com.manulife.util.pdf.PdfRegionBody;
import com.manulife.util.pdf.PdfRepeatablePageMasterAlternatives;
import com.manulife.util.pdf.PdfSideRegion;
import com.manulife.util.pdf.PdfSimplePageMaster;

/**
 * Generates the master layout for the report and optionally the investment selection form (ISF).
 * 
 * The page sequence is:
 * <ol>
 * <li>firstPageSingleColumn
 * <li>firstPageDoubleColumn
 * <li>subseqentPageSingleColumn
 * <li>subseqentPageDoubleColumn
 * <li>blank (optional spacer page)
 * <li>first ISF page
 * <li>subseqent ISF pages
 * </ol>
 * The firstPage, subsequentPage, and blank PageMasters comprise the "content" PageSequenceMaster.
 * The rest make up the "ISFcontent" PageSequenceMaster
 */
public class StandardReportsLayoutMasterSet extends PdfElementContainer {
	private static final String FOOTER_HEIGHT = "0.6in";
	private boolean isLandscape = false;
	private boolean isLegalSize = false;
	private boolean includeHeaderParagraph = false;
	private boolean containsContractOrShortlistDescription = false;
	private String headerHeight ;
	
	/**
	 * Build the standard page sequence.
	 * 
	 * @param report
	 * @param isLandscape is the main report lanscape or portrait.  The ISF is always portrait
	 * @param includeHeaderParagraph
	 * @param containsContractOrShortlistDescription
	 */
	public StandardReportsLayoutMasterSet(StandardReportBuilder report, boolean isLandscape, boolean isLegalSize, boolean includeHeaderParagraph, boolean containsContractOrShortlistDescription) {
		super(report);
		this.isLandscape = isLandscape;
		this.isLegalSize = isLegalSize;
		this.includeHeaderParagraph = includeHeaderParagraph;
		this.containsContractOrShortlistDescription = containsContractOrShortlistDescription;
	}
	
	/**
     * Build the standard page sequence.
     * 
     * @param report
     * @param isLandscape is the main report lanscape or portrait.  The ISF is always portrait
     * @param includeHeaderParagraph
     * @param containsContractOrShortlistDescription
     */
    public StandardReportsLayoutMasterSet(StandardReportBuilder report, boolean isLandscape, boolean isLegalSize, boolean includeHeaderParagraph, boolean containsContractOrShortlistDescription, String headerHeight) {
        super(report);
        this.isLandscape = isLandscape;
        this.isLegalSize = isLegalSize;
        this.includeHeaderParagraph = includeHeaderParagraph;
        this.containsContractOrShortlistDescription = containsContractOrShortlistDescription;
        this.headerHeight = headerHeight;
    }
	

	public void init() {
		PdfLayoutMasterSet layoutMasterSet = ((StandardReportBuilder)getReport()).createLayoutMasterSet();
		addElement((PdfElement) layoutMasterSet);
		setContainerRootElement(layoutMasterSet);
		/*
         * add single column first page master
         */
        layoutMasterSet.add(buildFirstPageMaster(isLandscape, isLegalSize, includeHeaderParagraph, 1 , headerHeight));
        /*
         * add double column first page master
         */
        layoutMasterSet.add(buildFirstPageMaster(isLandscape, isLegalSize, includeHeaderParagraph, 2, headerHeight));
        /*
         * add single column subsequent page master
         */
        layoutMasterSet.add(buildSubsequentPageMaster(isLandscape, isLegalSize, 1));
        /*
         * add double column subsequent page master
         */
        layoutMasterSet.add(buildSubsequentPageMaster(isLandscape, isLegalSize, 2));
		layoutMasterSet.add(buildBlankPageMaster());
		layoutMasterSet.add(buildPageSequenceMaster());
		
		addISFLayout(layoutMasterSet);
	}

	/**
	 *  A blank page to fill empty even pages between the report and the ISF
	 */
	private PdfSimplePageMaster buildBlankPageMaster() {
		PdfSimplePageMaster blankPageMaster = makePageMaster("blank", isLandscape, isLegalSize);
		blankPageMaster.add(report.createRegionBody(PDFConstants.MASTER_SEQUENCE_FLOW_BODY));
		return blankPageMaster;
	}

	public void addISFLayout(PdfLayoutMasterSet layoutMasterSet) {
		layoutMasterSet.add(makeThreePartPage("ISFFirstPage", PDFConstants.MASTER_REGION_FIRST_PAGE_HEADER, "3.8in", false, false, 1));
		layoutMasterSet.add(makeThreePartPage("ISFSubsequentPage", PDFConstants.MASTER_REGION_SUBSEQUENT_PAGES_HEADER, "1.5in", false, false, 1 ));
		
		PdfPageSequenceMaster pageSequenceMaster = report.createPageSequenceMaster(PDFConstants.MASTER_SEQUENCE_ISFCONTENT);
		pageSequenceMaster.add(report.createSinglePageMasterReference("ISFFirstPage"));
		pageSequenceMaster.add(report.createRepeatablePageMasterReference("ISFSubsequentPage"));
		layoutMasterSet.add(pageSequenceMaster);
	}

	public PdfLayoutMasterSet add(PdfSimplePageMaster simplePageMaster) {
		return (PdfLayoutMasterSet) super.addElement(simplePageMaster);
	}

	public PdfLayoutMasterSet add(PdfPageSequenceMaster pageSequenceMaster) {
		return (PdfLayoutMasterSet) super.addElement(pageSequenceMaster);
	}

    /**
     * Builds the simple page master for first page
     * 
     * @param isLandscape
     * @param includeHeaderParagraph
     * @param columns
     * 
     * @return PdfSimplePageMaster
     */
	private PdfSimplePageMaster buildFirstPageMaster(boolean isLandscape, boolean isLegalSize,
            boolean includeHeaderParagraph, int columns, String headerHeight) {
		String regionBeforeExtent;
		
		if (headerHeight != null) {
		    regionBeforeExtent = headerHeight;
        } else {
            if (includeHeaderParagraph) {
                if (containsContractOrShortlistDescription) {
                    regionBeforeExtent = "2.20in";
                } else {
                    regionBeforeExtent = "2.00in";
                }
            } else {
                regionBeforeExtent = "1.4in";
            }
        }
		
		PdfSimplePageMaster firstPageMaster = null;
		
		switch (columns) {
            case 1:
                firstPageMaster = makeThreePartPage("firstPageSingleColumn",
                        PDFConstants.MASTER_REGION_FIRST_PAGE_HEADER, regionBeforeExtent,
                        isLandscape, isLegalSize, columns);
                break;
            case 2:
                firstPageMaster = makeThreePartPage("firstPageDoubleColumn",
                        PDFConstants.MASTER_REGION_FIRST_PAGE_HEADER, regionBeforeExtent,
                        isLandscape, isLegalSize, columns);
                break;
        }
		
		return firstPageMaster;
	}

    /**
     * Builds the simple page master for subsequent page
     * 
     * @param isLandscape
     * @param columns
     * 
     * @return PdfSimplePageMaster
     */
	private PdfSimplePageMaster buildSubsequentPageMaster(boolean isLandscape, boolean isLegalSize, int columns) {
		String regionBeforeExtent;
		if (containsContractOrShortlistDescription) {
			regionBeforeExtent = "1.50in";
		} else {
			regionBeforeExtent = "1.45in";
		}
		
        PdfSimplePageMaster subsequentPageMaster = null;

        switch (columns) {
            case 1:
                subsequentPageMaster = makeThreePartPage("subsequentPageSingleColumn",
                        PDFConstants.MASTER_REGION_SUBSEQUENT_PAGES_HEADER, regionBeforeExtent,
                        isLandscape, isLegalSize, columns);
                break;
            case 2:
                subsequentPageMaster = makeThreePartPage("subsequentPageDoubleColumn",
                        PDFConstants.MASTER_REGION_SUBSEQUENT_PAGES_HEADER, regionBeforeExtent,
                        isLandscape, isLegalSize, columns);
                break;
        }
		return subsequentPageMaster;
	}

	private PdfSimplePageMaster makeThreePartPage(String masterName, String headerFlowName, String headerHeight, boolean isLandscape, 
			boolean isLegalSize, int columnCount) {
		PdfSimplePageMaster pageMaster = makePageMaster(masterName, isLandscape, isLegalSize);
		pageMaster.add(makeBody(headerHeight, columnCount));
		pageMaster.add(report.createRegionBefore(headerFlowName).setExtent(headerHeight));
		pageMaster.add(makeFooter());
		return pageMaster;
	}

	private PdfSideRegion makeFooter() {
		PdfSideRegion regionAfter = report.createRegionAfter("footer").setExtent(FOOTER_HEIGHT);
		regionAfter.setMarginRight("0.0in");
		return regionAfter;
	}

	private PdfRegionBody makeBody(String regionBeforeExtent, int columnCount) {
		PdfRegionBody regionBody = report.createRegionBody(PDFConstants.MASTER_SEQUENCE_FLOW_BODY);
		regionBody.setMarginTop(regionBeforeExtent);
		regionBody.setMarginBottom(FOOTER_HEIGHT);
		regionBody.setColumnCount(columnCount);
		return regionBody;
	}

	/**
	 * Setup basic printable margins
	 * @param masterName name for master.  Used to reference later in layout-master-set
	 * @param isLandscape
	 * @return
	 */
	private PdfSimplePageMaster makePageMaster(String masterName, boolean isLandscape, boolean isLegalSize) {
		PdfSimplePageMaster master = 
			report.createSimplePageMaster(masterName,new PdfRegion[]{});
		master.setMarginBottom("0.4in");
		master.setMarginLeft("0.5in");
		master.setMarginRight("0.5in");
		master.setMarginTop("0.5in");
		if (isLegalSize) {
			master.setPageHeight("8.5in");
			master.setPageWidth("14.0in");
		}
		else if (isLandscape) {
			master.setPageHeight("8.5in");
			master.setPageWidth("11.0in");
		} else {
			master.setPageHeight("11.0in");
			master.setPageWidth("8.5in");
		}
		
		return master;
	}

	/**
	 * Build a "case statment" page sequence: firstPage, blank page, with a default of subsequentPage
	 */
	private PdfPageSequenceMaster buildPageSequenceMaster() {
		PdfPageSequenceMaster pageSequenceMaster = report.createPageSequenceMaster(PDFConstants.MASTER_SEQUENCE_CONTENT);
		PdfRepeatablePageMasterAlternatives alternatives = report.createRepeatablePageMasterAlternatives();
		pageSequenceMaster.add(alternatives);

		{
			PdfConditionalPageMasterReference blank = report.createConditionalPageMasterReference("blank");
			blank.setElementAttribute("blank-or-not-blank","blank");
			alternatives.add(blank);
		}
		{
			PdfConditionalPageMasterReference first = report
                    .createConditionalPageMasterReference("firstPageSingleColumn");
			first.setElementAttribute("page-position","first");
			alternatives.add(first);
		}
		{
			PdfConditionalPageMasterReference first = report
                    .createConditionalPageMasterReference("subsequentPageSingleColumn");
			alternatives.add(first);
		}
		return pageSequenceMaster;
	}

}
