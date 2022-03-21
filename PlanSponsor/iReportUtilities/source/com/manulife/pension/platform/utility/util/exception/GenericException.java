package com.manulife.pension.platform.utility.util.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class GenericException extends ServletException {

    protected Object[] params = null; // parameters for error messages

    protected int errorCode = -1; // error code

    protected Object source = null; // source of the exception

    public GenericException() {
        super();
        this.fillInStackTrace();
        params = new Object[0];
    }

    /**
     * Class constructor
     * 
     * @param code - ExceptionCode
     */
    public GenericException(int errorCode) {
        super("errorCode: " + errorCode);
        this.errorCode = errorCode;
        params = new Object[0];
    }

    /**
     * Class constructor
     * 
     * @param code - ExceptionCode
     */
    public GenericException(int errorCode, Object[] params) {
        super("errorCode: " + errorCode);
        this.errorCode = errorCode;
        this.params = params;
    }

    public GenericException(Throwable exception) {
        super();
        source = exception;
        params = new Object[0];
    }

    /**
     * Class constructor
     * 
     * @param code - ExceptionCode
     */
    public GenericException(Object obj) {
        super(obj.toString());
        source = obj;
        params = new Object[0];
    }

    /**
     * Class constructor
     * 
     * @param obj - Class
     * @param code - ExceptionCode
     */
    public GenericException(Object obj, int errorCode) {
        this(obj);
        this.errorCode = errorCode;
        params = new Object[0];
    }

    /**
     * Return tokens that represent parameters for error messages
     * 
     * @return tokens
     */
    public Object[] getParams() {
        return this.params;
    }

    /**
     * Set tokens
     */
    public void setParams(Object[] params) {
        this.params = params;
    }

    /**
     * Return error code
     * 
     * @return int
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * Return a string that presents the error code
     * 
     * @return String
     */
    public String getErrorCodeString() {
        return (new Integer(this.errorCode)).toString();
    }

    /**
     * Set error code
     * 
     * @param code
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (getRootCause() != null) {
            s.print("Root cause is : ");
            getRootCause().printStackTrace(s);
        }
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (getRootCause() != null) {
            System.err.print("Root cause is :");
            getRootCause().printStackTrace();
        }
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (getRootCause() != null) {
            s.print("Root cause is : ");
            getRootCause().printStackTrace(s);
        }
    }

    public Throwable getRootCause() {
        return super.getRootCause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GenericException)) {
            return false;
        }

        final GenericException rhs = (GenericException) obj;

        return new EqualsBuilder().appendSuper(super.equals(obj)).append(params, rhs.params)
                .append(errorCode, rhs.errorCode).append(source, rhs.source).isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(params).append(errorCode)
                .append(source).toHashCode();
    }

}
