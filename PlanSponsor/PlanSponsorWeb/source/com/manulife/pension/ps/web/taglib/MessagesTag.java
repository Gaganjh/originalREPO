package com.manulife.pension.ps.web.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.manulife.pension.common.MessageCategory;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.util.content.manager.ContentCacheConstants;
import com.manulife.pension.util.content.manager.ContentProperties;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationErrorCodeAndParamEqualityPredicate;

/**
 * Displays the messages (errors, warnings, and alerts) in a formatted table. If no errors exist,
 * and warnings do exist, the user is shown a dialog with the warnings.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.1 2007/03/01 22:44:08
 */
public class MessagesTag extends TagSupport {

    /**
     * Default serialVersion UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * MESSAGE_TITLE_SEPARATOR.
     */
    private static final String MESSAGE_TITLE_SEPARATOR = "/";

    /**
     * SPACE.
     */
    private static final String SPACE = " ";

    public static final int DEFAULT_PAGE_CONTEXT_SCOPE = PageContext.SESSION_SCOPE;

    /**
     * DEFAULT_WIDTH.
     */
    public static final String DEFAULT_WIDTH = "500px";

    public static final String DEFAULT_MAX_HEIGHT = "100px";

    private static final String LIST_ITEM_OPEN_TAG = "<li>";

    private static final String LIST_ITEM_CLOSE_TAG = "</li>";

    private static final String SESSION_TEXT = "session";

    private static final String PAGE_TEXT = "page";

    private static final String REQUEST_TEXT = "request";

    private static final String APPLICATION_TEXT = "application";

    /**
     * WARNING_POPUP_TRAILING_TEXT.
     */
    private static final String WARNING_POPUP_TRAILING_TEXT = "Do you want to continue?";

    private static final String TABLE_START_BEGIN_BEFORE_WIDTH = "\n"
            + "    <table class=\"box\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" "
            + "width=\"";

    private static final String TABLE_START_BEGIN_AFTER_WIDTH = "\">"
            + "    <tr class=\"tablehead\">" + "      <td  colspan=\"3\" class=\"tableheadTD1\">"
            + "        <b>" + "          &nbsp; ";

    private static final String TABLE_START_END = "\n" + "    </b>" + "  </td>" + "</tr>" + "<tr>"
            + "  <td class=\"boxborder\" width=\"1\">"
            + "    <img src=\"/assets/unmanaged/images/s.gif\" height=\"1\" width=\"1\">"
            + "  </td>" + "  <td>" + "";

    private static final String TABLE_END = "\n" + "        </td>"
            + "        <td width=\"1\" class=\"boxborder\">"
            + "          <img src=\"/assets/unmanaged/images/s.gif\" height=\"1\" width=\"1\">"
            + "        </td>" + "      </tr>" + "<TR class=\"boxborder\">"
            + "  <TD height=\"1\" colspan=\"3\" class=\"boxborder\"><IMG height=\"1\" "
            + "    src=\"/assets/unmanaged/images/s.gif\" " + " width=\"1\"></TD>" + "</TR>"
            + " </table>";

    private static final String ERROR_ICON = "<img src=\"/assets/unmanaged/images/error.gif\"/>";

    private static final String WARNING_ICON = "<img "
            + "src=\"/assets/unmanaged/images/warning2.gif\"/>";

    private static final String ALERT_ICON = "";

    private static final String ERROR_HEADER_TEXT = "All errors have " + ERROR_ICON
            + " next to them.";

    private static final String WARNING_HEADER_TEXT = "All warnings have " + WARNING_ICON
            + " next to them.";

    private static final String BEGIN_LIST = "<ol style=\"padding-left: 10px;\">";

    private static final String END_LIST = "</ol>\n";

    private static final String END_DIV = "</div>\n";

    private boolean isPageContextScopeRequest = false;

    private String scope = null;

    private String maxHeight = null;

    private String showEmptyErrorsAndWarningsInHeader = Boolean.TRUE.toString();

    private String showWarningAsAnAlert = Boolean.FALSE.toString();

    private String disableWarningPopup = Boolean.FALSE.toString();

    private String width = DEFAULT_WIDTH;

    private int pageContextScope = DEFAULT_PAGE_CONTEXT_SCOPE;

    private Boolean suppressDuplicateMessages = false;

    private Boolean showOnlyWarningContent = false;
    
    private Logger logger = Logger.getLogger(MessagesTag.class);

    // Uses an anonymous inner class to initialize the HashMap inline.
    private static Map<String, Integer> scopes = new HashMap<String, Integer>() {
        /**
         * Default Serial Version UID.
         */
        private static final long serialVersionUID = 1L;

        {
            put(PAGE_TEXT, new Integer(PageContext.PAGE_SCOPE));
            put(REQUEST_TEXT, new Integer(PageContext.REQUEST_SCOPE));
            put(SESSION_TEXT, new Integer(PageContext.SESSION_SCOPE));
            put(APPLICATION_TEXT, new Integer(PageContext.APPLICATION_SCOPE));
        }
    };

    /**
     * This method shows the errors in the collection.
     * 
     * @return int - The value to determine how the body of this tag is processed.
     * @throws JspException If a Jsp exception occurs.
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {

        final String errorKey = getErrorKeyAttribute();

        Collection<GenericException> errorCollection = null;
        List<ObjectError>  errorList =null;
        Object obj =null;
        
        obj = TagUtils.getInstance().lookup(pageContext, errorKey, getScope());
        if( obj instanceof Collection){
    		errorCollection = (Collection) obj;
    	} else if (obj instanceof BeanPropertyBindingResult){
    		errorCollection=new ArrayList<GenericException>();
			errorList=((BeanPropertyBindingResult) obj).getAllErrors();
			for(ObjectError objError:errorList){
				 if (objError != null) {
					 errorCollection.add(new ValidationError(new String[0], Integer.parseInt(objError.getCode()), objError.getArguments(),
	                            ValidationError.Type.error));
               }
				
			}
		}
        if (StringUtils.isNotBlank(scope)) {

            if (scope.equals(SESSION_TEXT)) {
                pageContext.setAttribute(errorKey, errorCollection, PageContext.REQUEST_SCOPE);
            } // fi
            pageContext.removeAttribute(errorKey, scopes.get(scope).intValue());
        } else {
            // support the current implementation for ezk
            errorCollection = (Collection<GenericException>) pageContext.getAttribute(errorKey,
                    pageContextScope);
            pageContext.removeAttribute(errorKey);
        } // fi

        if (CollectionUtils.isNotEmpty(errorCollection)) {

            final Collection<String> errorMessages;
            try {
                errorMessages = MessageProvider.getInstance().getMessageCollection(errorCollection);
            } catch (ContentException contentException) {
                logger.warn("Got an exception while processing tag", contentException);
                throw new JspException("Got an exception while processing tag", contentException);
            } // end try/catch

            final Collection<ValidationError> validationErrors = new ArrayList<ValidationError>(
                    errorCollection.size());

            for (GenericException genericException : errorCollection) {

                if (genericException instanceof ValidationError) {
                    // It's a ValidationError.
                    validationErrors.add((ValidationError) genericException);
                } else {
                    // Not a ValidationError. Force the type to be ERROR.
                    validationErrors.add(new ValidationError(new String[0], genericException
                            .getErrorCode(), genericException.getParams(),
                            ValidationError.Type.error));
                } // fi
            } // end for

            return renderTag(errorMessages, validationErrors);

        } // fi
        return SKIP_BODY;
    }

    /**
     * This method produces the message display section.
     * 
     * @param validationErrorCollection The collection of {@link ValidationError} objects.
     * @param errorMessages The collection of {@link String} error messages.
     * 
     * @return int - The processing method for the body of this tag.
     * @throws JspException If a JSP exception occurs.
     */
    private int renderTag(final Collection<String> errorMessages,
            final Collection<ValidationError> validationErrorCollection) throws JspException {

        final Map<ValidationError, String> messageMap = new HashMap<ValidationError, String>();
        final Iterator<String> messageIterator = errorMessages.iterator();
        for (ValidationError validationError : validationErrorCollection) {
            if (messageIterator.hasNext()) {
                messageMap.put(validationError, messageIterator.next());
            } // fi
        } // end for

        final Collection<ValidationError> errors = getMessasgeByCategory(validationErrorCollection,
                MessageCategory.ERROR);
        final Collection<ValidationError> warnings = getMessasgeByCategory(
                validationErrorCollection, MessageCategory.WARNING);
        final Collection<ValidationError> alerts = getMessasgeByCategory(validationErrorCollection,
                MessageCategory.ALERT);

        final StringBuffer stringBuffer = new StringBuffer();

        // Render the start of the table.
        generateTableStart(stringBuffer, errors, warnings, alerts);

        // Suppress error info if this is a warning only message header
        if(null!=errors&&errors.size()>0)
        {
        if (!BooleanUtils.isTrue(getShowOnlyWarningContent())) {
            stringBuffer.append(ERROR_HEADER_TEXT);
            stringBuffer.append(SPACE);
        }
        }
        if(null!=warnings&&warnings.size()>0)
        {
        stringBuffer.append(WARNING_HEADER_TEXT);
        }
        generateDivStart(stringBuffer);
        stringBuffer.append(BEGIN_LIST);

        renderMessages(stringBuffer, messageMap, errors, ERROR_ICON);
        renderMessages(stringBuffer, messageMap, warnings, WARNING_ICON);
        renderMessages(stringBuffer, messageMap, alerts, ALERT_ICON);

        stringBuffer.append(END_LIST);
        stringBuffer.append(END_DIV);

        // Render the end of the table.
        stringBuffer.append(TABLE_END);

        processWarningPopup(stringBuffer, messageMap, errors, warnings);

        try {
            pageContext.getOut().print(stringBuffer.toString());
        } catch (IOException ioException) {
            logger.warn("Got an exception while processing tag", ioException);
            throw new JspException(ioException);
        } // end try/catch

        return SKIP_BODY;
    }

    /**
     * Generates the start of the table.
     * 
     * @param stringBuffer The buffer to append to.
     * @param errors The error collection.
     * @param warnings The warning collection.
     * @param alerts The alert collection.
     */
    private void generateTableStart(final StringBuffer stringBuffer,
            final Collection<ValidationError> errors, final Collection<ValidationError> warnings,
            final Collection<ValidationError> alerts) {

        final String messageTitle = getMessageTitle(errors, warnings, alerts);

        stringBuffer.append(TABLE_START_BEGIN_BEFORE_WIDTH);
        stringBuffer.append(getWidthValue());
        stringBuffer.append(TABLE_START_BEGIN_AFTER_WIDTH);
        stringBuffer.append(messageTitle);
        stringBuffer.append(TABLE_START_END);

    }

    /**
     * Creates the start of the DIV tag, for scrolling.
     * 
     * @param stringBuffer The buffer to append to.
     */
    private void generateDivStart(final StringBuffer stringBuffer) {

        stringBuffer.append("\n<div style=\"overflow-y: auto;");
        stringBuffer.append("height: expression( this.scrollHeight > ");
        stringBuffer.append(stripValue(getMaxHeightValue()));
        stringBuffer.append(" ? '");
        stringBuffer.append(getMaxHeightValue());
        stringBuffer.append("' : 'auto' ); /* sets max-height for IE */ \n");
        stringBuffer.append("max-height: ");
        stringBuffer.append(getMaxHeightValue());
        stringBuffer.append("; /* sets max-height value for all standards-compliant browsers */ ");
        stringBuffer.append("\">\n");
    }

    /**
     * This removes all non-digits from the String.
     * 
     * @param value The input.
     * @return String The result.
     */
    private String stripValue(final String value) {

        if (StringUtils.isNumeric(value)) {
            return value;
        } // fi

        return StringUtils.stripEnd(value, "pxPX");
    }

    /**
     * Render the Messages.
     * 
     * @param stringBuffer The buffer to append to.
     * @param messageMap The map of the messages/errors.
     * @param messages The messages to display.
     * @param icon The icon to display with the message.
     */
    private void renderMessages(final StringBuffer stringBuffer,
            final Map<ValidationError, String> messageMap,
            final Collection<ValidationError> messages, final String icon) {

        final Collection<ValidationError> types = new ArrayList<ValidationError>();
        for (ValidationError validationError : messages) {

            // Check if we should suppress duplicate messages
            boolean showMessage = true;
            if (BooleanUtils.isTrue(getSuppressDuplicateMessages())) {
                if (CollectionUtils.exists(types, new ValidationErrorCodeAndParamEqualityPredicate(
                        validationError))) {
                    showMessage = false;
                } else {
                    types.add(validationError);
                }
            }

            if (showMessage) {
                stringBuffer.append(LIST_ITEM_OPEN_TAG);
                stringBuffer.append(icon);
                stringBuffer.append(SPACE);
                stringBuffer.append(messageMap.get(validationError));
                stringBuffer.append(LIST_ITEM_CLOSE_TAG);
            }
        } // end for
    }

    /**
     * Gets the message table title.
     * 
     * @param errors The error collection.
     * @param warnings The warning collection.
     * @param alerts The alert collection.
     * @return String The title.
     */
    private String getMessageTitle(final Collection<ValidationError> errors,
            final Collection<ValidationError> warnings, final Collection<ValidationError> alerts) {

        final StringBuffer titleBuffer = new StringBuffer();

        if (CollectionUtils.isNotEmpty(errors)
                || (BooleanUtils.toBoolean(getShowEmptyErrorsAndWarningsInHeader()) && (!BooleanUtils
                        .isTrue(getShowOnlyWarningContent())))) {
            // We have errors
            titleBuffer.append("Errors");
            titleBuffer.append(SPACE);
            appendTitleCount(errors, titleBuffer);

            if (CollectionUtils.isNotEmpty(warnings) || CollectionUtils.isNotEmpty(alerts)
                    || (BooleanUtils.toBoolean(getShowEmptyErrorsAndWarningsInHeader()))) {
                // Add separator.
                titleBuffer.append(SPACE).append(MESSAGE_TITLE_SEPARATOR).append(SPACE);
            } // fi
        } // fi
        if (CollectionUtils.isNotEmpty(warnings)
                || (BooleanUtils.toBoolean(getShowEmptyErrorsAndWarningsInHeader()))) {
            // We have warnings
            titleBuffer.append("Warnings");
            titleBuffer.append(SPACE);
            appendTitleCount(warnings, titleBuffer);

            if (CollectionUtils.isNotEmpty(alerts)) {
                // Add separator.
                titleBuffer.append(SPACE).append(MESSAGE_TITLE_SEPARATOR).append(SPACE);
            } // fi
        } // fi
        if (CollectionUtils.isNotEmpty(alerts)) {
            // We have alerts
            titleBuffer.append("Alerts");
            titleBuffer.append(SPACE);
            appendTitleCount(alerts, titleBuffer);
        } // fi

        return titleBuffer.toString();
    }

    /**
     * Appends the count of the messages, for the title.
     * 
     * @param messages The messages to count.
     * @param stringBuffer The buffer to append to.
     */
    private void appendTitleCount(final Collection<ValidationError> messages,
            final StringBuffer stringBuffer) {
        stringBuffer.append("(");
        stringBuffer.append(messages.size());
        stringBuffer.append(")");
    }

    /**
     * Gets the messages from the given collection that match the given category.
     * 
     * @param validationErrorCollection The message to search.
     * @param messageCategory The category to filter by.
     * @return Collection - The messages that match.
     */
    private Collection<ValidationError> getMessasgeByCategory(
            final Collection<ValidationError> validationErrorCollection,
            final MessageCategory messageCategory) {

        final Collection<ValidationError> result = new ArrayList<ValidationError>();

        ValidationError.Type validationErrorType;

        switch (messageCategory) {
            case ERROR:
                validationErrorType = ValidationError.Type.error;
                break;
            case WARNING:
                validationErrorType = ValidationError.Type.warning;
                break;
            case ALERT:
                validationErrorType = ValidationError.Type.alert;
                break;
            default:
                throw new RuntimeException("Unable to find a mapping for category: "
                        + messageCategory);
        } // end switch

        for (ValidationError validationError : validationErrorCollection) {
            if (validationError.getType().equals(validationErrorType)) {
                result.add(validationError);
            } // fi
        } // end for

        return result;
    }

    /**
     * This processes the warning behaviour of displaying a popup message.
     * 
     * @param stringBuffer The buffer to append to.
     * @param messageMap The map of {@link ValidationError} objects to messages.
     * @param errors The collection of errors.
     * @param warnings The collection of warnings.
     */
    private void processWarningPopup(final StringBuffer stringBuffer,
            final Map<ValidationError, String> messageMap,
            final Collection<ValidationError> errors, final Collection<ValidationError> warnings) {

        if (!(warningPopupConditionMet(errors, warnings))) {
            return;
        } // fi

        stringBuffer.append("\n\n");
        stringBuffer.append("<script language=\"javascript\" type=\"text/javascript\">\n");

        stringBuffer.append("\n");
        stringBuffer.append("function confirmWarnings() {\n");

        if (BooleanUtils.toBoolean(getShowWarningAsAnAlert())) {
            stringBuffer.append("  alert(\n");
        } else {
            stringBuffer.append("  return confirm(\n");
        } // fi

        for (ValidationError validationError : warnings) {
            stringBuffer.append("'WARNING: ");
            stringBuffer.append(StringEscapeUtils.escapeJavaScript(StringEscapeUtils
                    .unescapeHtml(messageMap.get(validationError))));
            stringBuffer.append("\\n' + \n");
        } // end for

        if (BooleanUtils.toBoolean(getShowWarningAsAnAlert())) {
            stringBuffer.append("'');\n");
            stringBuffer.append("return false;\n");
        } else {
            stringBuffer.append("'\\n");
            stringBuffer.append(WARNING_POPUP_TRAILING_TEXT);
            stringBuffer.append("');\n");
        } // fi

        stringBuffer.append("}\n");

        stringBuffer.append("\n");
        stringBuffer.append("function processWarnings() {\n");
        stringBuffer.append("  if (confirmWarnings()) {\n");

        stringBuffer.append("    if (typeof(processForm) == \"function\") {\n");
        stringBuffer.append("      processForm();\n");
        stringBuffer.append("    }\n");

        stringBuffer.append("  }\n");
        stringBuffer.append("}\n");

        stringBuffer.append("\n");

        stringBuffer.append("if (typeof(runOnLoad) == \"function\") {\n");
        stringBuffer.append("  runOnLoad( processWarnings );\n");
        stringBuffer.append("}\n");

        stringBuffer.append("\n");
        stringBuffer.append("</script>\n");
    }

    /**
     * Checks to see if the warning popup message dialog should be displayed or not.
     * 
     * @param errors The collection of errors.
     * @param warnings The collection of warnings.
     * 
     * @return boolean - True if it should be displayed, false otherwise.
     */
    private boolean warningPopupConditionMet(final Collection<ValidationError> errors,
            final Collection<ValidationError> warnings) {

        // Check to see if the warning popup is disabled or not.
        if (BooleanUtils.isTrue(BooleanUtils.toBoolean(getDisableWarningPopup()))) {
            return false;
        } // fi

        // Errors must be empty and warnings must be not empty.
        if (CollectionUtils.isEmpty(errors)) {
            return CollectionUtils.isNotEmpty(warnings);
        } // fi

        return false;
    }

    /**
     * Sets the pageContextScope to REQUEST scope if true and to SESSION scope if false.
     * 
     * @param isPageContextScopeRequest The isPageContextScopeRequest to set
     */
    public void setIsPageContextScopeRequest(final boolean isPageContextScopeRequest) {
        this.isPageContextScopeRequest = isPageContextScopeRequest;
        if (this.isPageContextScopeRequest) {
            pageContextScope = PageContext.REQUEST_SCOPE;
        } else {
            pageContextScope = DEFAULT_PAGE_CONTEXT_SCOPE;
        } // fi
    }

    /**
     * Gets the max height, and uses the default if none is specified.
     * 
     * @return String - The max height value.
     */
    private String getMaxHeightValue() {
        if (StringUtils.isBlank(maxHeight)) {
            return DEFAULT_MAX_HEIGHT;
        } else {
            return maxHeight;
        } // fi
    }

    /**
     * Gets the width, and uses the default if none is specified.
     * 
     * @return String - The width value.
     */
    private String getWidthValue() {
        if (StringUtils.isBlank(width)) {
            return DEFAULT_WIDTH;
        } else {
            return width;
        } // fi
    }

    /**
     * Gets the ErrorKeyAttribute.
     * 
     * @return String - The error key attribute.
     */
    private String getErrorKeyAttribute() {
        return ContentProperties.getInstance().getProperty(ContentCacheConstants.ERROR_KEY);
    }

    /**
     * Sets the Scope.
     * 
     * @param scope The scope to set
     */
    public void setScope(final String scope) {
        this.scope = scope;
    }

    /**
     * Gets the Scope.
     * 
     * @return string
     */
    public String getScope() {
        return this.scope;
    }

    /**
     * @return the maxHeight
     */
    public String getMaxHeight() {
        return maxHeight;
    }

    /**
     * @param maxHeight the maxHeight to set
     */
    public void setMaxHeight(final String maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * @return the showEmptyErrorsAndWarningsInHeader
     */
    public String getShowEmptyErrorsAndWarningsInHeader() {
        return showEmptyErrorsAndWarningsInHeader;
    }

    /**
     * @param showEmptyErrorsAndWarningsInHeader the showEmptyErrorsAndWarningsInHeader to set
     */
    public void setShowEmptyErrorsAndWarningsInHeader(
            final String showEmptyErrorsAndWarningsInHeader) {
        this.showEmptyErrorsAndWarningsInHeader = showEmptyErrorsAndWarningsInHeader;
    }

    /**
     * @return String - The showWarningAsAnAlert.
     */
    public String getShowWarningAsAnAlert() {
        return showWarningAsAnAlert;
    }

    /**
     * @param showWarningAsAnAlert - The showWarningAsAnAlert to set.
     */
    public void setShowWarningAsAnAlert(final String showWarningAsAnAlert) {
        this.showWarningAsAnAlert = showWarningAsAnAlert;
    }

    /**
     * @return String - The width.
     */
    public String getWidth() {
        return width;
    }

    /**
     * @param width - The width to set.
     */
    public void setWidth(final String width) {
        this.width = width;
    }

    /**
     * @return String - The disableWarningPopup.
     */
    public String getDisableWarningPopup() {
        return disableWarningPopup;
    }

    /**
     * @param disableWarningPopup - The disableWarningPopup to set.
     */
    public void setDisableWarningPopup(final String disableWarningPopup) {
        this.disableWarningPopup = disableWarningPopup;
    }

    /**
     * @return the suppressDuplicateMessages
     */
    public Boolean getSuppressDuplicateMessages() {
        return suppressDuplicateMessages;
    }

    /**
     * @param suppressDuplicateMessages the suppressDuplicateMessages to set
     */
    public void setSuppressDuplicateMessages(final Boolean suppressDuplicateMessages) {
        this.suppressDuplicateMessages = suppressDuplicateMessages;
    }

    /**
     * @return the showOnlyWarningContent
     */
    public Boolean getShowOnlyWarningContent() {
        return showOnlyWarningContent;
    }

    /**
     * @param showOnlyWarningContent the showOnlyWarningContent to set
     */
    public void setShowOnlyWarningContent(final Boolean showOnlyWarningContent) {
        this.showOnlyWarningContent = showOnlyWarningContent;
    }
}
