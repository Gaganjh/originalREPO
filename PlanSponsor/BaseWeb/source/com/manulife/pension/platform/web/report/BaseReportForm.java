package com.manulife.pension.platform.web.report;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * Base action form class for all report forms
 *
 * @see		BaseForm
 * @see		com.manulife.pension.ps.web.controller.PsForm
 */
public class BaseReportForm extends BaseForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sortField;
	private String sortDirection;
	private int pageNumber = 1;
	private String reportId;
	private boolean pdfCapped = false;
    private int cappedRowsInPDF = 0;
    
    /**
     * This method will return capped number of rows in PDF.
     * @return Returns the cappedRowsInPDF.
     */
    public int getCappedRowsInPDF() {
        return cappedRowsInPDF;
    }
    
    /**
     * This method sets capped number of rows in PDF.
     * @param cappedRowsInPDF The cappedRowsInPDF to set.
     */
    public void setCappedRowsInPDF(int cappedRowsInPDF) {
        this.cappedRowsInPDF = cappedRowsInPDF;
    }

    /**
     * This method will return true if the PDF has been capped.
     * @return boolean
     */
    public boolean getPdfCapped() {
        return pdfCapped;
    }
    
    /**
	 * This method sets if the PDF has been capped.
	 * @param pdfCapped
	 */
	public void setPdfCapped(boolean pdfCapped) {
		this.pdfCapped = pdfCapped;
	}

    /**
 	* Default Constructor
 	*/
	public BaseReportForm() {
	}

	/**
	 * @return Returns the reportId.
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * @param reportId The reportId to set.
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

 	/**
	 * Gets the pageNumber
	 * @return Returns a int
	 */
	public int getPageNumber() {
		return pageNumber;
	}
	/**
	 * Sets the pageNumber
	 * @param pageNumber The pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * Gets the sortField
	 * @return Returns a String
	 */
	public String getSortField() {
		return sortField;
	}
	/**
	 * Sets the sortField
	 * @param sortField The sortField to set
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}


	/**
	 * Gets the sortDirection
	 * @return Returns a String
	 */
	public String getSortDirection() {
		return sortDirection;
	}
	/**
	 * Sets the sortDirection
	 * @param sortDirection The sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	
	/**
	 * Clears the report form. Since report form is stored in the session and
	 * is reused by many other actions, we must clear the parameters when
	 * we switch reports. 
	 */
	public void clear() {
		pageNumber = 1;
		sortField = null;
		sortDirection = null;
		reportId = null;
	}

	public void reset(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
}
