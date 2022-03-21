package com.manulife.pension.platform.web.util.exception;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.util.content.GenericException;

/**
 * This class will be used to hold the Exception information (Error / Info / Warning Messages) that
 * will be shown to the user in JSP page.
 * 
 * @author harlomte
 * 
 */
public class GenericExceptionWithContentType extends GenericException {
    
    private static final long serialVersionUID = 1L;

    // content Type of the Message.
    protected ContentType contentType;
    
    // This is indicator telling if the CMACode / CMAKey should be shown or not.
    protected boolean showCMACodeInd;
    
    /**
     * Class Constructor. contentType is default set to MESSAGE.
     */
    public GenericExceptionWithContentType() {
        super();
        contentType = ContentTypeManager.instance().MESSAGE;
        showCMACodeInd = true;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param errorCode - error code of the exception.
     */
    public GenericExceptionWithContentType(int errorCode) {
        super(errorCode);
        contentType = ContentTypeManager.instance().MESSAGE;
        showCMACodeInd = true;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param errorCode - error code of the exception.
     */
    public GenericExceptionWithContentType(int errorCode, boolean showCMACodeInd) {
        super(errorCode);
        contentType = ContentTypeManager.instance().MESSAGE;
        this.showCMACodeInd = showCMACodeInd;
    }

    /**
     * Class Constructor.
     * 
     * @param errorCode - error code of the exception.
     * @param contentType - content type of the exception.
     */
    public GenericExceptionWithContentType(int errorCode, ContentType contentType) {
        super(errorCode);
        this.contentType = contentType;
        showCMACodeInd = true;
    }

    /**
     * Class Constructor.
     * 
     * @param errorCode - error code of the exception.
     * @param contentType - content type of the exception.
     */
    public GenericExceptionWithContentType(int errorCode, ContentType contentType,
            boolean showCMACodeInd) {
        super(errorCode);
        this.contentType = contentType;
        this.showCMACodeInd = showCMACodeInd;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param errorCode - error code of the exception.
     * @param params - params to be sent along with error code to CMA.
     */
    public GenericExceptionWithContentType(int errorCode, Object[] params) {
        super(errorCode, params);
        contentType = ContentTypeManager.instance().MESSAGE;
        showCMACodeInd = true;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param errorCode - error code of the exception.
     * @param params - params to be sent along with error code to CMA.
     */
    public GenericExceptionWithContentType(int errorCode, Object[] params, boolean showCMACodeInd) {
        super(errorCode, params);
        contentType = ContentTypeManager.instance().MESSAGE;
        this.showCMACodeInd = showCMACodeInd;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param errorCode - error code of the exception.
     * @param params - params to be sent along with error code to CMA.
     * @param contentType - content type of the exception.
     */
    public GenericExceptionWithContentType(int errorCode, Object[] params, ContentType contentType) {
        super(errorCode, params);
        this.contentType = contentType;
        showCMACodeInd = true;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param errorCode - error code of the exception.
     * @param params - params to be sent along with error code to CMA.
     * @param contentType - content type of the exception.
     */
    public GenericExceptionWithContentType(int errorCode, Object[] params, ContentType contentType,
            boolean showCMACodeInd) {
        super(errorCode, params);
        this.contentType = contentType;
        this.showCMACodeInd = showCMACodeInd;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param exception
     */
    public GenericExceptionWithContentType(Throwable exception) {
        super(exception);
        contentType = ContentTypeManager.instance().MESSAGE;
        showCMACodeInd = true;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param obj
     */
    public GenericExceptionWithContentType(Object obj) {
        super(obj);
        contentType = ContentTypeManager.instance().MESSAGE;
        showCMACodeInd = true;
    }

    /**
     * Class Constructor. contentType is default set to MESSAGE.
     * 
     * @param obj
     * @param errorCode
     */
    public GenericExceptionWithContentType(Object obj, int errorCode) {
        super(obj, errorCode);
        contentType = ContentTypeManager.instance().MESSAGE;
        showCMACodeInd = true;
    }

    /**
     * Get the content Type.
     * 
     * @return - content type.
     */
    public ContentType getContentType() {
        return contentType;
    }
    
    /**
     * Set the content Type.
     * 
     * @param contentType
     */
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GenericExceptionWithContentType)) {
            return false;
        }

        final GenericExceptionWithContentType rhs = (GenericExceptionWithContentType) obj;

        return new EqualsBuilder().appendSuper(super.equals(obj)).append(contentType,
                rhs.contentType).append(showCMACodeInd, rhs.showCMACodeInd).isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(contentType).append(
                showCMACodeInd).toHashCode();
    }

    public boolean isShowCMACodeInd() {
        return showCMACodeInd;
    }

    public void setShowCMACodeInd(boolean showCMACodeInd) {
        this.showCMACodeInd = showCMACodeInd;
    }

    
}
