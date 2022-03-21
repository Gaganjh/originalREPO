package com.manulife.pension.ireports.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.report.util.CMAContentHelper;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;

/**
 * This class reads the pdf_reports.properties file and gets the content ID for a given footnote
 * symbol from this file. The content text is retrieved for the content ID and a Footnote object is
 * created.
 * 
 * @author HArlomte
 * 
 */
public class StaticFootnoteDAO {

	private Properties footNoteProperties;
	private String companyPropertyPart;

	public StaticFootnoteDAO(String companyId) {
		this.companyPropertyPart = StandardReportsConstants.COMPANY_ID_NY.equals(companyId) ? "NY" : "US";
		footNoteProperties = PropertyManager.getProperties("standardReports.staticFootnote." + companyPropertyPart);
	}

    /**
     * This method retrieves the Footnote symbol, footnote text from CMA, and retuns a Map of the
     * symbol, footnote.
     * 
     * @param staticFootnoteSymbols
     * @return
     * @throws SystemException 
     */
	public Map<String, Footnote> retrieveFootnote(String[] staticFootnoteSymbols) {
		if (staticFootnoteSymbols == null) {
			return Collections.emptyMap();
		}
		Map<String, Footnote> result = new HashMap<String, Footnote>();
		String companyId;
        if (StandardReportsConstants.STANDARDREPORTS_COMPANY_NY.equals(companyPropertyPart)) {
            companyId = StandardReportsConstants.COMPANY_ID_NY;
        } else {
            companyId = StandardReportsConstants.COMPANY_ID_USA;
        }
        try {
			result = CMAContentHelper.getPageFootnoteTextByDisplayName(staticFootnoteSymbols, companyId);
		} catch (SystemException e) {
			throw new NestableRuntimeException("Problem retrieving footnotes in getPageFootnoteTextByDisplayName() in "
                    + getClass().getName(), e);
		}
		return result;
	}
	
	/**
	 * This method retrieves the foonote content ID from the .properties file
	 * and retrieves the content for that content Id from CMA with params. This
	 * method creates a Footnote object out of the content ID, content text and
	 * returns it back.
	 * 
	 * @param footnoteSymbol
	 * @return Footnote
	 */
	public Footnote retrieveFootnoteWithParams(String contentId,
			String... contentParams) {
		if (contentId == null) {
			return null;
		}
		/*
		 * String footnoteContentId = footNoteProperties
		 * .getProperty(StandardReportsConstants.STANDARD_REPORTS +
		 * companyPropertyPart + "." + footnoteSymbol);
		 */

		String companyId;
		if (StandardReportsConstants.STANDARDREPORTS_COMPANY_NY
				.equals(companyPropertyPart)) {
			companyId = StandardReportsConstants.COMPANY_ID_NY;
		} else {
			companyId = StandardReportsConstants.COMPANY_ID_USA;
		}
		String footnoteText = null;
		// Retrieve the content for footnote..
		if (contentParams != null && contentParams.length != 0) {
			footnoteText = CMAContentHelper.getPageFootnoteTextWithParams(
					contentId, companyId, contentParams);
		} else {
			footnoteText = CMAContentHelper.getPageFootnoteText(contentId,
					companyId);
		}
		
		if (footnoteText != null) {
			return new Footnote(StandardReportsConstants.SPACE, footnoteText);
		}

		return null;
	}
}
