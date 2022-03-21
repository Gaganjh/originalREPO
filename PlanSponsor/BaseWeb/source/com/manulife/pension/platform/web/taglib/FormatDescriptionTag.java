package com.manulife.pension.platform.web.taglib;

import java.awt.FontMetrics;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.Stylesheet;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;
import com.manulife.util.render.SSNRender;

/**
 * This tag is used for the Transaction History page. It augments the 3
 * description lines with a link to the Participant Account page when the first
 * 3 charcters are the xxx of the masked ssn.
 * 
 * @author: Maria Lee
 *  
 */
public class FormatDescriptionTag extends TagSupport {

	private static final String FONT_METRICS = "fontMetrics";
	
	private static final String TOKEN_DELIMITER = ",";

	private String item;
	
	private int line;

	private boolean linkParticipant;

	private String length;
	
	private String lineLength;

	private String width;
	
	private boolean showBobParticipantAccountLink;
	
	private boolean hideTransactionInProgress;
	
	public int doStartTag() throws JspException {
		try {
			TransactionHistoryItem historyItem = (TransactionHistoryItem) pageContext
					.findAttribute(getItem());
			
			int descriptionLine = 1;
			StringBuffer buffer = new StringBuffer();
			for (Iterator it = historyItem.getDescriptions().iterator(); it
					.hasNext();) {
				String description = (String) it.next();
				buffer.append(buildLine(description, historyItem,descriptionLine));
				if (it.hasNext()) {
					buffer.append("<br />");
					descriptionLine++;
				}
			}

			pageContext.getOut().print(buffer.toString());

		} catch (IOException ex) {
			throw new JspException(ex.getMessage());
		}
		return SKIP_BODY;
	}
	
	public String buildLine(String description, TransactionHistoryItem item,int descriptionLine) {

		String formattedDescription = description;

		if (length != null) {
			int intLength = Integer.valueOf(length).intValue();
			if (intLength < description.length()) {
				formattedDescription = description.substring(0, intLength);
			}
		}
		
		if (lineLength != null && descriptionLine == line) {
			int intLength = Integer.valueOf(lineLength).intValue();
			if (intLength < description.length()) {
				formattedDescription = description.substring(0, intLength);
			}
		}
		if (width != null) {
			int intWidth = Integer.valueOf(width).intValue();
			while (getFontMetrics().stringWidth(formattedDescription) > intWidth) {
				formattedDescription = formattedDescription.substring(0,
						formattedDescription.length() - 1);
			}
		}

		if(hideTransactionInProgress && StringUtils.equals(description, CommonConstants.IN_PROGRESS)){
			return "";
		}

		String descLine = formattedDescription;

		/*
		 * if (PSTagHelper.isPrintFriendly(pageContext)) { return description; }
		 */

		if (linkParticipant && !BaseTagHelper.isPrintFriendly(pageContext) && description.startsWith(SSNRender.MASK)) {
			descLine = getLink(formattedDescription, item.getParticipant().getId()
					.toString());
		}

		/*
		 * // detect if description starts with masked ssn if (description !=
		 * null && description.length() > 4) { if (description.substring(0,
		 * 4).equalsIgnoreCase("XXX-")) { return getLink(description,
		 * item.getParticipant().getId() .toString()); } } }
		 */

		if (formattedDescription.length() < description.length()) {
			return "<span title=\"" + description + "\">" + descLine + "</span>";
		}
		return descLine;
	}

	/**
	 * Consructs a html string as follows: xxx-xx-xxxx, <a
	 * href="do/participant/ParticipantAccount?participantId=xxxxx"> Lastname,
	 * Firstname </a>
	 *  
	 */
	public String getLink(String description, String participantId) {

		StringBuffer buffer = new StringBuffer();
		int delimPos = description.indexOf(TOKEN_DELIMITER, 0);
		buffer.append(description.substring(0, delimPos + 2));
		if (showBobParticipantAccountLink) {
            buffer.append("<a href=\"").append(CommonConstants.PARTICIPANT_ACCOUNT_URL_BD);
        } else {
            buffer.append("<a href=\"").append(CommonConstants.PARTICIPANT_ACCOUNT_URL_PS);
        }
		buffer.append("?participantId=");
		buffer.append(participantId);
		buffer.append("\">");
		buffer.append(description.substring(delimPos + 2));
		buffer.append("</a>");
		return buffer.toString();
	}

	/**
	 * Gets the item
	 * 
	 * @return Returns a String
	 */
	public String getItem() {
		return item;
	}

	/**
	 * Sets the item
	 * 
	 * @param item
	 *            The item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}

	/**
	 * @return
	 */
	public String getLinkParticipant() {
		return String.valueOf(linkParticipant);
	}

	/**
	 * @param link
	 */
	public void setLinkParticipant(String link) {
		this.linkParticipant = Boolean.valueOf(link).booleanValue();
	}
	
	/**
	 * @return the hideTransactionInProgress
	 */
	public String getHideTransactionInProgress() {
		return String.valueOf(hideTransactionInProgress);
	}

	/**
	 * @param hideTransactionInProgress the hideTransactionInProgress to set
	 */
	public void setHideTransactionInProgress(String hideTransactionInProgress) {
		this.hideTransactionInProgress = Boolean.valueOf(hideTransactionInProgress).booleanValue();
	}	

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * @return Returns the width.
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * 
	 * @return line number
	 */
	public int getLine() {
		return line;
	}

	/**
	 * 
	 * @param line
	 *         The line number to set
	 */
	public void setLine(int line) {
		this.line = line;
	}
	
	/**
	 * 
	 * @return line length
	 */
	public String getLineLength() {
		return lineLength;
	}
	
	/**
	 * 
	 * @param lineLength
	 * 				line max length to set
	 */
	public void setLineLength(String lineLength) {
		this.lineLength = lineLength;
	}
    /**
     * @return Returns the isBDApplication.
     */
    public String getShowBobParticipantAccountLink() {
        return String.valueOf(showBobParticipantAccountLink);
    }

    /**
     * @param isBDApplication The isBDApplication to set.
     */
    public void setShowBobParticipantAccountLink(String showBobParticipantAccountLink) {
        this.showBobParticipantAccountLink = Boolean.valueOf(showBobParticipantAccountLink)
                .booleanValue();
    }
    
	public FontMetrics getFontMetrics() {
		FontMetrics fontMetrics = (FontMetrics)pageContext.getAttribute(FONT_METRICS);
		if (fontMetrics == null) {
			fontMetrics = Stylesheet.getFontMetrics(Stylesheet.getBodyFont());
			pageContext.setAttribute(FONT_METRICS, fontMetrics);
		}
		return fontMetrics;
	}

}

