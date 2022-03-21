package com.manulife.pension.ireports.report;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.manulife.util.pdf.PdfElement;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfStaticContent;
import com.manulife.util.pdf.PdfTable;
import com.manulife.util.pdf.PdfTableRegion;

public class CustomPageSequence extends AbstractPageSequence {

	private String headerParagraph;

	private PdfTable firstPageHeaderTable;
	private PdfTable subsequentPageHeaderTable;
	
	private String masterReferenceName;

    // The below 2 maps hold the title information for all the title1 thru title5. This will be
    // later used if the Reports want to make changes to these titles.
    Map<String, PdfParagraph> firstPageTitleParagraphMap;
    Map<String, PdfParagraph> subsequentPageTitleParagraphMap;
    
    // The below 2 maps are those which the user may fill with the title information and pass it to
    // the constructor of this class. If there is some title information in these 2 maps, then they
    // will be used, else, the title information from the ReportOptions object will be shown.
	Map<String, PdfParagraph> firstPageTitleStaticParagraph;
	Map<String, PdfParagraph> subsequentPageTitleStaticParagraph;

	public CustomPageSequence(
			StandardReportBuilder report,
			ReportOptions options,
			boolean isLandscape,
			boolean isLegalSize,
			String headerParagraph,
            String masterReferenceName, Map<String, PdfParagraph> firstPageTitleStaticParagraph,
            Map<String, PdfParagraph> subsequentPageTitleStaticParagraph) {
		super(report, options, isLandscape, isLegalSize);
		this.headerParagraph = headerParagraph;
		this.masterReferenceName = masterReferenceName;
		this.firstPageTitleStaticParagraph = firstPageTitleStaticParagraph;
        this.subsequentPageTitleStaticParagraph = subsequentPageTitleStaticParagraph;

        firstPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        subsequentPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
	}
	
	public CustomPageSequence(
			StandardReportBuilder report,
			ReportOptions options,
			boolean isLandscape,
			boolean isLegalSize,
			String headerParagraph,
            String masterReferenceName, Map<String, PdfParagraph> firstPageTitleStaticParagraph,
            Map<String, PdfParagraph> subsequentPageTitleStaticParagraph, int footnoteDisclosureContentID) {
		super(report, options, isLandscape, isLegalSize, footnoteDisclosureContentID);
		this.headerParagraph = headerParagraph;
		this.masterReferenceName = masterReferenceName;
		this.firstPageTitleStaticParagraph = firstPageTitleStaticParagraph;
        this.subsequentPageTitleStaticParagraph = subsequentPageTitleStaticParagraph;

        firstPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
        subsequentPageTitleParagraphMap = new HashMap<String, PdfParagraph>();
	}
	
	public void init() {
	    pageSequence = report.createPageSequence(this.masterReferenceName);
        addElement((PdfElement) pageSequence);
        setContainerRootElement(pageSequence);
        pageSequence.setLanguage("en");
        // This attribute set is required to get rid of blank page.
        pageSequence.setForcePageCount("no-force");
        pageSequence.add(createFirstPageStaticContent((StandardReportBuilder) report));
        pageSequence.add(createSubsequentPagesStaticContent((StandardReportBuilder) report));
        pageSequence.add(createFooterStaticContent((StandardReportBuilder) report));
    }
	
	protected PdfStaticContent createFirstPageStaticContent(StandardReportBuilder report) {
		PdfStaticContent staticContent = report.createStaticContent(PDFConstants.MASTER_REGION_FIRST_PAGE_HEADER);
		
		String[] columnSizes = null;
		if (isLegalSize()) {
			columnSizes = new String[]{"2.0in", "11.0in"};
		} else if (isLandscape()) {
			columnSizes = new String[]{"2.0in", "8.0in"};
		} else {
			columnSizes = new String[]{"2.0in", "5.5in"};
		}
		
		int tableSize = headerParagraph == null ? 2 : 3;
		 
		firstPageHeaderTable = report.createTable(columnSizes, new PdfTableRegion[]{report.createTableBody(tableSize, columnSizes.length)});
		firstPageHeaderTable.getTableBody().add(0, 0, report.createParagraph(getImageBuilder().buildLogo()));

        PdfParagraph firstPgSubTitleParagraph;
        
        // Get the title Id's.
        Set<String> titleKeySet = options.getReportTitle().keySet();
        
        if (titleKeySet != null) {
            // Iterate thru each title ID and get the PdfParagraph to be printed for each titleid.
            for (String titleKey : titleKeySet) {
                firstPgSubTitleParagraph = getTitleParagraph(titleKey, firstPageTitleStaticParagraph);
                if (firstPgSubTitleParagraph != null) {
                    firstPageTitleParagraphMap.put(titleKey, firstPgSubTitleParagraph);
                    firstPageHeaderTable.getTableBody().add(0, 1, firstPgSubTitleParagraph);
                }
            }
        }
        
		firstPageHeaderTable.getTableBody().add(1, 0, report.createParagraph()).spanCells(1, 0, 1,
                2).setRowHeight(1, "0.25in");
			
		if (headerParagraph != null) {
			firstPageHeaderTable.getTableBody().add(2, 0, 
			        (PdfParagraph)report.createParagraph(report.createText(headerParagraph))
			        .setFontFamily(FRUTIGER47LIGHT_CN).setFontSize(9).setWrapOption("wrap"))
			        .spanCells(2, 0, 1, 2);
		}
		
		staticContent.add(report.createParagraph().add(firstPageHeaderTable));
		return staticContent;
	}

	protected PdfStaticContent createSubsequentPagesStaticContent(StandardReportBuilder report) {
		PdfStaticContent staticContent = report.createStaticContent(PDFConstants.MASTER_REGION_SUBSEQUENT_PAGES_HEADER);
		
		String[] columnSizes = null;
		if (isLegalSize()) {
			columnSizes = new String[]{"2.0in", "11.0in"};
		} else if (isLandscape()) {
			columnSizes = new String[]{"2.0in", "8.0in"};
		} else {
			columnSizes = new String[]{"2.0in", "5.5in"};
		}
		
		subsequentPageHeaderTable = report.createTable(columnSizes, new PdfTableRegion[]{report.createTableBody(1, columnSizes.length)});
		subsequentPageHeaderTable.getTableBody().add(0, 0, report.createParagraph(getImageBuilder().buildLogo()));

        PdfParagraph subsequentPgSubTitleParagraph;
        
        // Get the title Id's.
        Set<String> titleKeySet = options.getReportTitle().keySet();
        
        if (titleKeySet != null) {
            // Iterate thru each title ID and get the PdfParagraph to be printed for each titleid.
            for (String titleKey : titleKeySet) {
                subsequentPgSubTitleParagraph = getTitleParagraph(titleKey, subsequentPageTitleStaticParagraph);
                if (subsequentPgSubTitleParagraph != null) {
                    subsequentPageTitleParagraphMap.put(titleKey, subsequentPgSubTitleParagraph);
                    subsequentPageHeaderTable.getTableBody().add(0, 1, subsequentPgSubTitleParagraph);
                }
            }
        }
        
		staticContent.add(report.createParagraph().add(subsequentPageHeaderTable));
		return staticContent;
	}

    /**
     * Get the First Page Title Paragraph for a specific title id
     * 
     * @param titleid - the title Id for which the First page title Paragraph will be returned
     * @return - First page title Paragraph.
     */
    public PdfParagraph getFirstPageTitleParagraph(String titleid) {
        return firstPageTitleParagraphMap.get(titleid);
    }

    /**
     * Get the Subsequent Page Title Paragraph for a specific title id
     * 
     * @param titleid - the title Id for which the Subsequent page title Paragraph will be returned
     * @return - Subsequent page title Paragraph.
     */
    public PdfParagraph getSubsequentPageTitleParagraph(String titleid) {
        return subsequentPageTitleParagraphMap.get(titleid);
    }
    
	protected int getHeadingFontSize(String headingTitle) {
		int headingFontSize = 16;
		
		// estimated max line length at 16pt. in portrait mode
        if (!isLandscape() && headingTitle != null && headingTitle.length() >= 60) { 
			headingFontSize = 14;
		}
		return headingFontSize;
	}
	
	public String getMasterReferenceName() {
        return masterReferenceName;
    }

    public void setMasterReferenceName(String masterReferenceName) {
        this.masterReferenceName = masterReferenceName;
    }
}
