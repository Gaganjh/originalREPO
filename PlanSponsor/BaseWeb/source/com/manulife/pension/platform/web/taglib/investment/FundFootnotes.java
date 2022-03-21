package com.manulife.pension.platform.web.taglib.investment;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.platform.web.cache.FootnoteCacheImpl;

/**
 * This class is the tag that will build the footnotes section of the contract
 * funds page.  These footnotes coincide with the footnote symbols associated
 * with the fund in the fundTable
 *
 * Copied from PlanSponsorWeb
 * 
 * @author SAyyalusamy
 * 
 **/

public class FundFootnotes extends TagSupport {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String arrayName;
	private String companyId;
	
	/**
	 * set the symbols
	 * @param value
	 */
	public void setSymbols(String value){
		arrayName = value;
	}	

	/**
	 * Returns the symbols
	 * @return return the symbol
	 */
	public String getSymbols(){
		return (arrayName);
	}

	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * doStartTag is called by the JSP container when the tag is encountered
	 */
	public int doStartTag()throws JspException {
		try {
			
			JspWriter out = pageContext.getOut();
			
			/**
			 * Get the name of the footnote symbol array from the pageContext 
			 * attribute "symbolsArray". This object passed into the handler 
			 * class from the JSP page as "symbols"
			 */
			String[] footnoteSymbolsArray = null;
			if (pageContext.getAttribute(
					arrayName,PageContext.REQUEST_SCOPE) != null) {
			
				footnoteSymbolsArray = (String [])pageContext.getAttribute(
						arrayName, PageContext.REQUEST_SCOPE);
				
			}else if (pageContext.getAttribute(
					arrayName,PageContext.SESSION_SCOPE) != null) {
				
				footnoteSymbolsArray = (String [])pageContext.getAttribute(
						arrayName, PageContext.SESSION_SCOPE);
			}
			
			if ( footnoteSymbolsArray != null  ) {
				Footnote[] sortedSymbolsArray = 
					FootnoteCacheImpl.getInstance().sortFootnotes(
							footnoteSymbolsArray, companyId);		

				/**
				 * loop through the footnoteSymbolsArray, print the symbols in 
				 * order - *'s, #'s, ^'s, +'s, and numbers 1 to 18 Text for 
				 * footnotes currently hard-coded, waiting for getContent 
				 * method to be developed
				 */
				for(int i = 0; i < sortedSymbolsArray.length; i++){
					if (sortedSymbolsArray[i] != null){
						String returnText = sortedSymbolsArray[i].getText();
						String returnSymbol = sortedSymbolsArray[i].getSymbol();
						out.println("<P><SUP>" + returnSymbol + "</SUP>" + returnText + "</P>");
					}
				}				
			}
						
		} catch (Exception ex){
			throw new JspException(ex.getMessage());
		}
		
		return SKIP_BODY;
	}
}
