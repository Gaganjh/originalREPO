package com.manulife.pension.ps.web.taglib.investment;

/*
  File: FundFootnotes.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-01-01   Chris Shin       Initial version.
*/

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.util.content.Footnotes;

/**
 * This class is the tag that will build the footnotes section of the contract
 * funds page.  These footnotes coincide with the footnote symbols associated
 * with the fund in the fundTable
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 **/

public class FundFootnotes extends TagSupport {

	private String arrayName;
	
	public void setSymbols(String value){
		arrayName = value;
	}	

	public String getSymbols(){
		return (arrayName);
	}

/**
  *doStartTag is called by the JSP container when the tag is encountered
*/

	public int doStartTag()throws JspException {
		try {
			
			JspWriter out = pageContext.getOut();
			
			/**
			 * Get the name of the footnote symbol array from the pageContext attribute "symbolsArray"
			 * This object passed into the handler class from the JSP page as "symbols"
			 */
			String[] footnoteSymbolsArray = null;
			if (pageContext.getAttribute(arrayName,PageContext.REQUEST_SCOPE) != null)
				footnoteSymbolsArray = (String [])pageContext.getAttribute(arrayName, PageContext.REQUEST_SCOPE);
			else if (pageContext.getAttribute(arrayName,PageContext.SESSION_SCOPE) != null)
				footnoteSymbolsArray = (String [])pageContext.getAttribute(arrayName, PageContext.SESSION_SCOPE);
			
			if ( footnoteSymbolsArray != null  ) {
				Footnote[] sortedSymbolsArray = Footnotes.getInstance().sortFootnotes(footnoteSymbolsArray);		

				/**
				 * loop through the footnoteSymbolsArray, print the symbols in order - *'s, #'s, ^'s, +'s, and numbers 1 to 18
				 * Text for footnotes currently hard-coded, waiting for getContent method to be developed
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
