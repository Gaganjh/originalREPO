package com.manulife.pension.ps.web.taglib.report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import org.apache.log4j.Logger;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.platform.web.taglib.report.AbstractReportTag;
import com.manulife.pension.ps.service.report.submission.valueobject.LongTermPartTimeDetailsReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection;
import com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.tools.util.CensusDetailsHelper;
import com.manulife.pension.submission.SubmissionError;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * @author parkand
 *
 */
public class SubmissionErrorsTag extends AbstractReportTag {

	private String width;
	private SubmissionErrorCollection errors;
	private String contributionDetailsErrors;
    private String vestingDetailsErrors;
    private String longTermPartTimeDetailsErrors;
	private String warnings;
	private String forceView;
	private String printFriendly;
	private String includeTable;
	private int numErrors;
	private int numWarnings;
    
    /**
     * Instance of Logger
     */
    private static Logger logger = Logger.getLogger(SubmissionErrorsTag.class);
	

	protected static final String PAGE_SCOPE = "page";
	protected static final String SESSION_SCOPE = "session";
	
	// HTML constants
	private static final String DIMENSION_TOKEN = "%";
	
	private static final String SPACER_IMG = "<img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" />";
	
    private static final String TABLE_TOP = 
		"<table width=\"%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
    	  + "<tr>"
		  + "<td><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td width=\"242\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td width=\"84\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td width=\"122\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td width=\"276\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td width=\"4\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td width=\"1\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "</tr>";
    
    private static final String TABLE_BODY_START = 
		    "<tr class=\"datacell1\">"
		  + "<td class=\"databorder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td colspan=\"6\" align=\"left\" valign=\"top\" class=\"datacell1\">";

    private static final String DIV_START = "<div class=\"scroll\">";
    
    private static final String DIV_END = "</div>"; 
    
    private static final String TABLE_BODY_END =
	        "</td>"
	      + "<td class=\"databorder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /> </td>"
	      + "</tr>";
    
    private static final String TABLE_HEAD_START = 
        "<tr class=\"tablehead\">"
	      + "<td class=\"tableheadTD1\" colspan=\"8\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
	          + "<tr>"
			  	+ "<td class=\"tableheadTD\"><strong>";

	private static final String TABLE_HEAD_END =
				  "</strong></td>"
				+ "</tr>"
	      + "</table></td>"
	    + "</tr>";
    	
    private static final String DIVIDER =
    	"<tr class=\"divider\">"
	      + "<td height=\"5\" class=\"databorder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
	      + "<td colspan=\"2\" class=\"whiteborder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
	      + "<td class=\"whiteborder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
	      + "<td colspan=\"2\" class=\"whiteborder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
		  + "<td height=\"5\"  colspan=\"2\" rowspan=\"2\" class=\"whiteborder\"><img src=\"/assets/unmanaged/images/box_lr_corner.gif\" width=\"5\" height=\"5\" /></td>"
	    + "</tr>";

    private static final String TABLE_BOTTOM = 
        "<tr>"
	      + "<td height=\"1\" class=\"databorder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
	      + "<td height=\"1\" colspan=\"2\" class=\"databorder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
	      + "<td height=\"1\" class=\"databorder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
	      + "<td height=\"1\" colspan=\"2\" class=\"databorder\"><img src=\"/assets/unmanaged/images/spacer.gif\" width=\"1\" height=\"1\" /></td>"
	    + "</tr>"
	    + "</table>";
    
    private static final String ERROR_DIV = "<br><DIV id='errordivcs'></DIV>";
    
    private static final String INTRO_TEXT = 
	      	"All errors are <span class=\"highlight\">highlighted</span> with an "+Constants.ERROR_ICON+" next to them.<br />"
	      +	"All warnings are <span class=\"highlight\">highlighted</span> with an "+Constants.WARNING_ICON+" next to them.<br />"
    	  +	ERROR_DIV;
	 
    // TODO: make this CMA managed
    private static final String SYNTAX_ERROR_MESSAGE =
    		"Some entries contained invalid formats and could not be processed. Please discard or contact your client account representative for details.<br />";
    
    private static final String DISCARD_BUTTON =
    		"<br><input name=\"discard\" value=\"Discard unprocessed records\"type=\"submit\" onClick=\"return doDiscard()\"/><br>";
	
	public int doStartTag() throws JspException {
		
		try {
			numErrors = errors.getNumErrors();
			numWarnings = errors.getNumWarnings();
			
			Boolean isViewForced = new Boolean(false);
			if ( forceView != null ) {
				isViewForced = Boolean.valueOf(forceView);
			}
			
			Boolean isPrintFriendly = new Boolean(false);
			if (printFriendly != null) {
				isPrintFriendly = Boolean.valueOf(printFriendly);
			}
			
			Boolean isIncludeTable = new Boolean(true);
			if (includeTable != null) {
				isIncludeTable = Boolean.valueOf(includeTable);
			}
			
			CensusDetailsHelper helper = (CensusDetailsHelper)(pageContext.getSession().getAttribute(Constants.CENSUS_DETAILS_HELPER));
			boolean isEditMode = false;
			if ( helper != null ) {
				isEditMode = helper.isEditMode();
				// set the error flag in the helper to true in case of syntax errors so that the 'edit' button appears on the page
				if (numErrors > 0 || errors.isSyntaxErrorIndicator() ) {
					helper.setErrorFlag(true);
				}
			}
				
			String html = null;
			if ( isViewForced.booleanValue() == true ) {
				// always display if the view is forced
				html = buildErrorPanel(numErrors, numWarnings, isPrintFriendly.booleanValue(), isEditMode, isIncludeTable.booleanValue());
			} else if (numErrors == 0 && numWarnings == 0 && errors.isSyntaxErrorIndicator() == false) {
				// there are no errors or warnings to display - no need to display the error panel
				html = SPACER_IMG + ERROR_DIV; // always need to render the error div because it's looked for in the JS
			} else {
				// there are warnings/errors - display them
				html = buildErrorPanel(numErrors, numWarnings, isPrintFriendly.booleanValue(), isEditMode, isIncludeTable.booleanValue());
			}

			pageContext.getOut().print(html);

		} catch (MalformedURLException e) {
			throw new JspException(e.getMessage());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_BODY_TAG;
	}

	private String buildErrorPanel(int errorCount, int warningCount, boolean isPrintFriendly, boolean isEditMode, boolean isIncludeTable) throws JspException {
		StringBuffer buffer = new StringBuffer();
		StringBuffer tmpBuffer = new StringBuffer();
		
		StringTokenizer st = new StringTokenizer(TABLE_TOP,DIMENSION_TOKEN);
		if (isIncludeTable) {
			buffer.append(st.nextToken());
			buffer.append(width);
			buffer.append(st.nextToken());

		}

		if ( !isPrintFriendly ) {
			if (isIncludeTable) tmpBuffer.append(DIV_START);
			tmpBuffer.append(INTRO_TEXT);  // always show intro text
			tmpBuffer.append(printErrors(false, isEditMode, isIncludeTable));
			if (isIncludeTable) tmpBuffer.append(DIV_END);
		} else {
			tmpBuffer.append(INTRO_TEXT);	// always show intro text		
			tmpBuffer.append(printErrors(true, isEditMode, isIncludeTable));
		}
		
		if (isIncludeTable) {
			tmpBuffer.append(TABLE_BODY_END);
	
			tmpBuffer.append(DIVIDER);
			tmpBuffer.append(TABLE_BOTTOM);
		}

		// add copy warning count if any
		Integer numCopyWarnings = (Integer)pageContext.getSession().getAttribute(Constants.NUM_COPY_WARNINGS);
		if ( numCopyWarnings != null ) {
			warningCount += numCopyWarnings.intValue();
			// remove the copy warning count from session
			pageContext.getSession().removeAttribute(Constants.NUM_COPY_WARNINGS);
		}
		
		// now that all of the errors have been determined, print the error count
		if (isIncludeTable) {
			buffer.append(TABLE_HEAD_START);
			buffer.append("Errors (<DIV id='errorCount' class='inline'>");
			buffer.append(errorCount);
			buffer.append("</DIV>) / Warnings (");
			buffer.append(warningCount);
			buffer.append(")");
			buffer.append(TABLE_HEAD_END);
	
			buffer.append(TABLE_BODY_START);
		}
		buffer.append(tmpBuffer.toString());
		
		return buffer.toString();
	}
	
	/**
	 * @return Returns the width.
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width The width to set.
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return Returns the errors.
	 */
	public SubmissionErrorCollection getErrors() {
		return errors;
	}
	/**
	 * @param errors The errors to set.
	 */
	public void setErrors(SubmissionErrorCollection errors) {
		this.errors = errors;
	}
	/**
	 * @return Returns the forceView.
	 */
	public String getForceView() {
		return forceView;
	}
	/**
	 * @param forceView The forceView to set.
	 */
	public void setForceView(String forceView) {
		this.forceView = forceView;
	}
	/**
	 * @return Returns the printFriendly.
	 */
	public String getPrintFriendly() {
		return printFriendly;
	}
	/**
	 * @param printFriendly The printFriendly to set.
	 */
	public void setPrintFriendly(String printFriendly) {
		this.printFriendly = printFriendly;
	}

	private String printErrors(boolean isPrintFriendly, boolean isEditMode, boolean isIncludeTable) throws JspException {
		
		if ( errors == null || errors.getErrors() == null ) {
			return "";
		}

		StringBuffer html = new StringBuffer("");
		
		// check for syntax errors
		if (errors.isSyntaxErrorIndicator()) {
			html.append("<br/>");
			html.append(SYNTAX_ERROR_MESSAGE);
			if (!isPrintFriendly && isEditMode) {
				html.append(DISCARD_BUTTON);
			}
			html.append("<br/>");
		}
		
		boolean hasWarningsOrContributionVestingErrors = false;
		
		if (warnings != null && warnings.length() > 0) {
			html.append("<br/>");
			html.append(warnings);
			html.append("<br/>");
            hasWarningsOrContributionVestingErrors = true;
		}
		
		// display contribution detail page msgs
		if ("true".equals(contributionDetailsErrors)) {
			html.append(printContributionDetailsErrors());
            hasWarningsOrContributionVestingErrors = true;
		}
        
        // display vesting detail page msgs
        if ("true".equals(vestingDetailsErrors)) {
            html.append(printVestingDetailsErrors());
            hasWarningsOrContributionVestingErrors = true;
        }
        // display long term part time detail page msgs
        if ("true".equals(longTermPartTimeDetailsErrors)) {
            html.append(printLongTermPartTimeDetailsErrors());
            hasWarningsOrContributionVestingErrors = true;
        }

		if ( errors.getErrors().size() == 0 && !hasWarningsOrContributionVestingErrors ) {
			// no data check errors/warnings, return
			return html.toString();
		}

        int cnt = 0;
		// list the errors/warnings
        html.append("<table>");
        // iterate through the errors
        Iterator iter = errors.getErrors().iterator();
        while (iter.hasNext()) {
            SubmissionError error = (SubmissionError) iter.next();
            cnt++;
            html.append("<tr><td width=\"6%\" valign=\"top\" align=\"right\" >");
            html.append(cnt + ".");
            html.append("</td><td width=\"94%\">");
            // display either the error or warning icon
            html.append(displayIcon(error));        
            html.append("&nbsp;");
            // if it's a participant-level error, display the row number
            if (error.isParticipantLevel()) {
                html.append("Row # ").append(error.getRowNumber()).append("&nbsp;");
            } else {
                if (error.getValue() != null) {
                    // display value
                    html.append(error.getValue());
                    html.append("&nbsp;");
                } else {
                    // display the field name
                    // html.append(error.getFieldDisplayLabel());
                }
            }
            
            // display the content
            try {
                Message msg = (Message)ContentCacheManager.getInstance().getContentById(error.getContentId(), ContentTypeManager.instance().MESSAGE);
                MessageFormat msgFormat = new MessageFormat(msg.getText() + "&nbsp;&nbsp;[" + msg.getCode() + "]");
                if (logger.isDebugEnabled()) {
                    logger.debug("Found MessagedFormat:"+msgFormat+" for errorCode:"+error.getCode());
                }
                Object[] param = new Object[1];
                if (error.hasParams()) {
                    param[0] = error.getFieldDisplayLabel();
                } else if (error.getArgument() != null) {
                    param[0] = error.getArgument();
                } else {
                    param[0] = "";
                }
                
                html.append(msgFormat.format( param ));
            } catch (ContentException ce) {
                html.append("unknown error/warning");
            } catch (NullPointerException ne) {
                html.append("unknown error/warning");
            }
            html.append("</td></tr>");
            
        }
        html.append("</table>");
        
		return html.toString();
	}

	private String displayIcon(SubmissionError error) {
		if (error.isWarning()) {
			return Constants.WARNING_ICON;
		} else if (error.isError()) {
			return Constants.ERROR_ICON;
		} else {
			return "";
		}
	}

	/**
	 * @return Returns the includeTable.
	 */
	public String getIncludeTable() {
		return includeTable;
	}
	/**
	 * @param includeTable The includeTable to set.
	 */
	public void setIncludeTable(String includeTable) {
		this.includeTable = includeTable;
	}
 
    private String printVestingDetailsErrors() throws JspException {
        StringBuffer html = new StringBuffer();
        String[] genericErrors = null;
        
        // retrieve the object from the page
        VestingDetailsReportData reportData = (VestingDetailsReportData)pageContext.getAttribute("theReport");
        genericErrors = reportData.getVestingData().getErrorMessage();
        
        if ( genericErrors != null ) {
    
            html.append("<table>");
            // iterate through the errors
            for (int i = 0; i < genericErrors.length; i++) {
                html.append("<tr><td width=\"10%\">");
                html.append("&nbsp;");
                html.append("</td><td width=\"90%\">");
                //html.append(Constants.ERROR_ICON);
                //html.append("&nbsp;");
                html.append(genericErrors[i]);
                html.append("</td></tr>");
                //html.append("<br><br>");
                numErrors++;
            }
            html.append("</table>");
        }
        return html.toString();
    }
    private String printLongTermPartTimeDetailsErrors() throws JspException {
        StringBuffer html = new StringBuffer();
        String[] genericErrors = null;
        
        // retrieve the object from the page
        LongTermPartTimeDetailsReportData reportData = (LongTermPartTimeDetailsReportData)pageContext.getAttribute("theReport");
        genericErrors = reportData.getLongTermPartTimeData().getErrorMessage();
        
        if ( genericErrors != null ) {
    
            html.append("<table>");
            // iterate through the errors
            for (int i = 0; i < genericErrors.length; i++) {
                html.append("<tr><td width=\"10%\">");
                html.append("&nbsp;");
                html.append("</td><td width=\"90%\">");
                html.append(genericErrors[i]);
                html.append("</td></tr>");
                numErrors++;
            }
            html.append("</table>");
        }
        return html.toString();
    }
    
	private String printContributionDetailsErrors() throws JspException {
		StringBuffer html = new StringBuffer();

		// retrieve the errors from the session
		Object obj = pageContext.getSession().getAttribute(PsBaseUtil.ERROR_KEY);
		Collection errorCollection = null;
        String[] errors = null;
        List<ObjectError>  errorList =null;

        if( obj instanceof Collection){
    		errorCollection = (Collection) obj;
    	} else if (obj instanceof BeanPropertyBindingResult){
			errorList=((BeanPropertyBindingResult) obj).getAllErrors();
		}
		
		if ( errorCollection != null ) {

			try {
				errors = MessageProvider.getInstance().getMessages(errorCollection );
			} catch (Exception ex) {
				throw new JspException(ex.toString());
			}}
			
			 else if (errorList != null) {
					errors = new String[errorList.size()];
					for (int i = 0; i < errorList.size(); i++) {
						ObjectError objError = errorList.get(i);
						Object[] arg = objError.getArguments();
						String strMessage = null;
						if (!objError.getCode().equalsIgnoreCase("typeMismatch")) {
							try {
								strMessage = MessageProvider.getInstance().getMessage(Integer.parseInt(objError.getCode()),
										arg);
							} catch (ContentException e) {
								strMessage = "Invalid message format for error code [" + objError.getCode() + "]";

							} catch (Exception ex) {
								logger.info(ex);
							}
						}

						if (strMessage != null && !(strMessage.equals("null"))) {
							errors[i] = strMessage;
							// i++;
						}
					}
				}
	
		if (errors != null && errors.length > 0) {
			html.append("<table>");
			// iterate through the errors
			for (int i = 0; i < errors.length; i++) {
				html.append("<tr><td width=\"10%\">");
				html.append("&nbsp;");
				html.append("</td><td width=\"90%\">");
				html.append(Constants.ERROR_ICON);
				html.append("&nbsp;");
				html.append(errors[i]);
				html.append("</td></tr>");
				//html.append("<br><br>");
				numErrors++;
			}
			html.append("</table>");
			// remove the errors from the session (unfortunately making them disappear on page refresh)
			pageContext.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);

		
		
		
		// display copy warnings
		String copyWarnings = (String)pageContext.getSession().getAttribute(Constants.COPY_WARNINGS);
		if ( copyWarnings != null && copyWarnings.length() > 0) {

			html.append(copyWarnings);
			// remove the copy warnings from session
			pageContext.getSession().removeAttribute(Constants.COPY_WARNINGS);
		}
		}
		return html.toString();
		
	}

	/**
	 * @return Returns the contributionDetailsErrors.
	 */
	public String getContributionDetailsErrors() {
		return contributionDetailsErrors;
	}
	/**
	 * @param contributionDetailsErrors The contributionDetailsErrors to set.
	 */
	public void setContributionDetailsErrors(String contributionDetailsErrors) {
		this.contributionDetailsErrors = contributionDetailsErrors;
	}
	/**
	 * @return Returns the warnings.
	 */
	public String getWarnings() {
		return warnings;
	}
	/**
	 * @param warnings The warnings to set.
	 */
	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

    public String getVestingDetailsErrors() {
        return vestingDetailsErrors;
    }

    public void setVestingDetailsErrors(String vestingDetailsErrors) {
        this.vestingDetailsErrors = vestingDetailsErrors;
    }
    public String getLongTermPartTimeDetailsErrors() {
		return longTermPartTimeDetailsErrors;
	}

	public void setLongTermPartTimeDetailsErrors(String longTermPartTimeDetailsErrors) {
		this.longTermPartTimeDetailsErrors = longTermPartTimeDetailsErrors;
	}
}
