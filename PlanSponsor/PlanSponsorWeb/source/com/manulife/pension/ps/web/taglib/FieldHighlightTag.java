package com.manulife.pension.ps.web.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * This tag can be used to hilight a input field based on whether there is a ValidationError for
 * that field (matching the name). It supports hilighting error, warning and alert with PSW's error,
 * warning and alert images. It supports tooltips (requires inclusion of the tooltips.js) through
 * the displayToolTips field (default is suppression of tooltips).
 * 
 * @author guweigu
 */
public class FieldHighlightTag extends TagSupport {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(FieldHighlightTag.class);

    private static final String ERROR_IMAGE_SOURCE = "/assets/unmanaged/images/error.gif";

    private static final String WARNING_IMAGE_SOURCE = "/assets/unmanaged/images/warning2.gif";

    private static final String ACTIVITY_HISTORY_IMAGE_SOURCE = "/assets/unmanaged/images/arrow_green.gif";

    private static final String START_LIST = "<ul>";

    private static final String END_LIST = "</ul>";

    private static final String NEW_LINE = "\n";

    private static final String LIST_ELEMENT_START = "<li>";

    private static final String LIST_ELEMENT_END = "</li>";

    // TODO: image location to be determined
    private static final String ALERT_IMAGE_SOURCE = "/assets/unmanaged/images/warning2.gif";

    private static final EnumMap<ValidationError.Type, String> IMAGE_MAP = new EnumMap<ValidationError.Type, String>(
            Type.class);

    private static final EnumMap<ValidationError.Type, String> STYLE_ID_PREFIX = new EnumMap<ValidationError.Type, String>(
            Type.class); 
    
    static {
        IMAGE_MAP.put(ValidationError.Type.error, ERROR_IMAGE_SOURCE);
        IMAGE_MAP.put(ValidationError.Type.warning, WARNING_IMAGE_SOURCE);
        IMAGE_MAP.put(ValidationError.Type.alert, ALERT_IMAGE_SOURCE);
    }

    static {
        STYLE_ID_PREFIX.put(ValidationError.Type.error, "errorIcon_");
        STYLE_ID_PREFIX.put(ValidationError.Type.warning, "warningIcon_");
        STYLE_ID_PREFIX.put(ValidationError.Type.alert, "alertIcon_");
    }


    private String name;

    private Boolean singleDisplay;

    private Boolean displayToolTip = false;

    private String style;

    private String className;

    private Boolean displayActivityHistory = false;
    
    private StringBuffer toolTip = new StringBuffer();

    /**
     * Suffix used to translated to a generated ID attribute name
     */
    private String styleIdSuffix;
    
    /**
     * {@inheritDoc}
     */
    public int doStartTag() throws JspException {
    	Collection<GenericException> errors = null;
        List<ObjectError>  errorList =null;
        Object obj =  pageContext.findAttribute(PsBaseUtil.ERROR_KEY);
        if( obj instanceof Collection){
        	errors = (Collection) obj;
    	} else if (obj instanceof BeanPropertyBindingResult){
    		errors=new ArrayList<GenericException>();
			errorList=((BeanPropertyBindingResult) obj).getAllErrors();
			for(ObjectError objError:errorList){
				 if (objError != null) {
					 errors.add(new ValidationError(new String[0], Integer.parseInt(objError.getCode()), objError.getArguments(),
	                            ValidationError.Type.error));
               }
				
			}
		}
        boolean hasError = false;
        boolean hasWarning = false;
        boolean hasAlert = false;

        ValidationError fieldError = null;
        final Collection<GenericException> fieldErrors = new ArrayList<GenericException>();
        if (errors != null && errors.size() > 0) {
            for (Iterator it = errors.iterator(); it.hasNext() && fieldError == null;) {
                Object error = it.next();
                if (error instanceof ValidationError) {
                    ValidationError validationError = (ValidationError) error;
                    if (validationError.hasFieldId(name)) {
                        switch (validationError.getType()) {
                            case error:
                                hasError = true;
                                break;
                            case warning:
                                hasWarning = true;
                                break;
                            case alert:
                                hasAlert = true;
                                break;
                            default:
                                throw new NotImplementedException("Validation Type not supported: "
                                        + validationError.getType());
                        } // end switch

                        // If tooltips is on - add to our list of errors
                        if (BooleanUtils.isTrue(displayToolTip)) {
                            fieldErrors.add((GenericException) error);
                        }
                    } // fi
                } // fi
            } // end for
        } // fi

        // If tooltips is on - retrieve the list of error messages and add to our buffer
        if (BooleanUtils.isTrue(displayToolTip)) {
            final Collection<String> errorMessages;
            try {
                errorMessages = MessageProvider.getInstance().getMessageCollection(fieldErrors);
            } catch (ContentException contentException) {
                logger.warn("Got an exception while processing tag", contentException);
                throw new JspException("Got an exception while processing tag", contentException);
            } // end try/catch
            toolTip.append(START_LIST);
            for (String message : errorMessages) {
                toolTip.append(LIST_ELEMENT_START).append(
                        escapeJavaScript(message)).append(LIST_ELEMENT_END);
            }
            toolTip.append(END_LIST);
        }

        // Check the special case where we only display one icon.
        if (BooleanUtils.isTrue(singleDisplay)) {
            // Only one error type will be displayed (if existing), so we set the others
            // values to false.
            if (hasError) {
                hasWarning = false;
                hasAlert = false;
            } else if (hasWarning) {
                hasAlert = false;
            } // fi
        } // fi

        if (hasError || hasWarning || hasAlert || BooleanUtils.isTrue(displayActivityHistory)) {
            final JspWriter jspWriter = pageContext.getOut();
            try {
                if (hasError) {
                    writeImage(jspWriter, getImageSrc(Type.error), getStyleIdPrefix(Type.error), null);
                } // fi
                if (hasWarning) {
                    writeImage(jspWriter, getImageSrc(Type.warning), getStyleIdPrefix(Type.warning), null);
                } // fi
                if (hasAlert) {
                    writeImage(jspWriter, getImageSrc(Type.alert), getStyleIdPrefix(Type.alert), null);
                } // fi
                if (BooleanUtils.isTrue(displayActivityHistory)) {
                    boolean hideActivityIcon = false;
                    if (BooleanUtils.isTrue(singleDisplay) && (hasError
                            || hasWarning || hasAlert)) {
                        hideActivityIcon = true;
                    }
                    if (hideActivityIcon) {
                        writeImage(jspWriter, ACTIVITY_HISTORY_IMAGE_SOURCE, "activityIcon_", "display:none");
                    } else {
                        writeImage(jspWriter, ACTIVITY_HISTORY_IMAGE_SOURCE, "activityIcon_", null);
                    }
                } // fi
            } catch (IOException ioException) {
                throw new JspException(ioException);
            } // end try/catch
            return SKIP_BODY;
        } else {
            return EVAL_PAGE;
        } // fi
    }

    /**
     * This method writes the image to the provided writer.
     * 
     * @param jspWriter The {@link JspWriter} to write to.
     * @param imageSource The 'src' location of the image to use.
     * @throws IOException If an {@link IOException} occurs.
     */
    protected void writeImage(final JspWriter jspWriter, final String imageSource, final String styleIdPrefix, String additionalStyle) throws IOException {
        jspWriter.write("<img src=\"");
        jspWriter.write(imageSource);
        jspWriter.write("\"");

        if (StringUtils.isNotBlank(styleIdSuffix)) {
            jspWriter.write(" id=\"");
            jspWriter.write(styleIdPrefix);
            jspWriter.write(styleIdSuffix);
            jspWriter.write("\"");
        } // fi

        StringBuffer styleString = new StringBuffer();
        if (StringUtils.isNotBlank(style)) {
            styleString.append(style);
            if (StringUtils.isNotBlank(additionalStyle)) {
                styleString.append(";");
            }
        }

        if (StringUtils.isNotBlank(additionalStyle)) {
            styleString.append(additionalStyle);
        }
        
        if (styleString.length() > 0) {
            jspWriter.write(" style=\"");
            jspWriter.write(styleString.toString());
            jspWriter.write("\"");
        } // fi

        if (StringUtils.isNotBlank(className)) {
            jspWriter.write(" class=\"");
            jspWriter.write(className);
            jspWriter.write("\"");
        } // fi

        if (BooleanUtils.isTrue(displayToolTip)) {
            jspWriter.write(" onmouseover=\"Tip('");
            jspWriter.write(toolTip.toString());
            jspWriter.write("')\"");
            jspWriter.write(" onmouseout=\"UnTip()\"");
        }

        jspWriter.write(" />");
    }

    /**
     * Depending on the error's type, return the proper image source.
     * 
     * @param type The type of error.
     * @return String - The image source location.
     */
    protected String getImageSrc(final Type type) {
        return IMAGE_MAP.get(type);
    }

    /**
     * Depending on the error's type, return the proper prefix to be used when 
     * generating the image's styleId.
     * 
     * @param type The type of error.
     * @return String - The styleId name prefix.
     */
    protected String getStyleIdPrefix(final Type type) {
        return STYLE_ID_PREFIX.get(type);
    }

    /**
     * Gets the name.
     * 
     * @return String - The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the singleDisplay
     */
    public Boolean getSingleDisplay() {
        return singleDisplay;
    }

    /**
     * @param singleDisplay the singleDisplay to set
     */
    public void setSingleDisplay(final Boolean singleDisplay) {
        this.singleDisplay = singleDisplay;
    }

    /**
     * @return String - The style.
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style - The style to set.
     */
    public void setStyle(final String style) {
        this.style = style;
    }

    /**
     * @return String - The className.
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className - The className to set.
     */
    public void setClassName(final String className) {
        this.className = className;
    }

    public String getStyleIdSuffix() {
	return styleIdSuffix;
    }

    public void setStyleIdSuffix(String styleIdSuffix) {
	this.styleIdSuffix = styleIdSuffix;
    }

    /**
     * @return the displayToolTip
     */
    public Boolean getDisplayToolTip() {
        return displayToolTip;
    }

    /**
     * @param displayToolTip the displayToolTip to set
     */
    public void setDisplayToolTip(final Boolean displayToolTip) {
        this.displayToolTip = displayToolTip;
    }

    public Boolean getDisplayActivityHistory() {
        return displayActivityHistory;
    }

    public void setDisplayActivityHistory(Boolean displayActivityHistory) {
        this.displayActivityHistory = displayActivityHistory;
    }
    
    protected String escapeJavaScript(String s) {
    	if (!StringUtils.isBlank(s)) {
    		s = s.replace("\"", "&quot;");
    		return StringEscapeUtils.escapeJavaScript(s);
    	}
    	return s; 
    }
    public StringBuffer getToolTip() {
		return toolTip;
	}

}
