package com.manulife.pension.ps.web.taglib;

/*
  File: SearchPageTag.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-07-16   Chris Shin       Initial version
*/

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Category;

import com.manulife.pension.ps.web.Constants;

/**
 * 
 * This class will simulate the paging requirements for the Search page.
 * 
 * @author 	Chris Shin
 */

public class SearchPagingTag extends TagSupport {

	protected Category generalLog = Category.getInstance(getClass());	

	private int currentPageNumber;
	private String parameter;
	private int numberOfResults;
	private int numberOfResultsPerPage;
	private String url;
	private int groupInterval;
	private int startAfterResultNumber;
	

	/**
	 * @return Returns SKIP_BODY
	 */	
	public int doStartTag() throws JspException {
		
		try {

			StringBuffer buff = new StringBuffer();
			double doubleIndex = getCurrentPageNumber();
			double doubleGroupInterval = getGroupInterval();
		
			Double totalNumberOfPages = new Double(Math.ceil((getNumberOfResults()*1.0d-getStartAfterResultNumber())/getNumberOfResultsPerPage()));
			Double startIndex = new Double(Math.floor(doubleIndex/doubleGroupInterval) * getGroupInterval() +1);

			buildPaging(startIndex,getCurrentPageNumber(),totalNumberOfPages);
		
		} catch (IOException ex) {
			throw new JspException("doStartTag on SearchPageTag failed: " + ex.getMessage());
		}
		
		return SKIP_BODY;
	}	
	
	
    /**
     * @return int
     */
    public int getCurrentPageNumber() {
        return this.currentPageNumber;
    }

    /**
     * @param int
     */
    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

	
    /**
     * @return String
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @param string
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * @return String
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param string
     */
    public void setUrl(String url) {
        this.url = url;
    }


   /**
     * @return int
     */
    public int getNumberOfResults() {
        return numberOfResults;
    }

    /**
     * @param int
     */
    public void setNumberOfResults(int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }


   /**
     * @return int
     */
    public int getGroupInterval() {
        return groupInterval;
    }

    /**
     * @param int
     */
    public void setGroupInterval(int groupInterval) {
        this.groupInterval = groupInterval;
    }

   /**
     * @return int
     */
    public int getStartAfterResultNumber() {
        return startAfterResultNumber;
    }

    /**
     * @param int
     */
    public void setStartAfterResultNumber(int startAfterResultNumber) {
        this.startAfterResultNumber = startAfterResultNumber;
    }

	/**
     * @return int
     */
    public int getNumberOfResultsPerPage() {
        return numberOfResultsPerPage;
    }

    /**
     * @param int
     */
    public void setNumberOfResultsPerPage(int numberOfResultsPerPage) {
        this.numberOfResultsPerPage = numberOfResultsPerPage;
    }

	private void buildPaging (Double startIndex, int selectedPageNumber, Double totalNumberOfPages) throws IOException {
		JspWriter out = pageContext.getOut();

		int pageCounter = startIndex.intValue();

		StringBuffer buf = new StringBuffer();
		buf.append("Page ");

		StringBuffer constructURL = new StringBuffer(getUrl());
		constructURL.append("?");
		constructURL.append(getParameter());
		constructURL.append("&newIndex=");

		
		if (pageCounter > getGroupInterval()) {
			buf.append("<a href=\"");
			buf.append(constructURL.toString());
			buf.append(pageCounter - 2);
			buf.append("\">");
			buf.append("<img src=\"");
			buf.append(Constants.BLACK_LEFT_ARROW_IMAGE_URL);
			buf.append("\" border=\"0\"></a>&nbsp;");
		}
	
		for (int i=0; i<getGroupInterval() ;i++) {

			if (pageCounter != selectedPageNumber+1) {
				buf.append("<a href=\"");
				buf.append(constructURL.toString());
				buf.append(pageCounter-1);
				buf.append("\">");
				buf.append(pageCounter);
				buf.append("</a>");
			} else {
				buf.append("<b>");
				buf.append(pageCounter);
				buf.append("</b>");
			}

			if (pageCounter >= totalNumberOfPages.intValue()) {
				break;
			}
			
			buf.append(" ");
			pageCounter +=1;
		}

		if (pageCounter < totalNumberOfPages.intValue()) {
			buf.append("<a href=\"");
			buf.append(constructURL.toString());
			buf.append(pageCounter-1);
			buf.append("\">");
			buf.append("<img src=\"");
			buf.append(Constants.BLACK_RIGHT_ARROW_IMAGE_URL);
			buf.append("\" border=\"0\"></a>");
		}		
		out.println(buf.toString());
	}
}



