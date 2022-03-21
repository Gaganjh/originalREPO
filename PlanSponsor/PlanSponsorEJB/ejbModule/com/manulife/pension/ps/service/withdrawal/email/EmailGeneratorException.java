/*
 * Created on Jan 12, 2007
 */
package com.manulife.pension.ps.service.withdrawal.email;

/**
 * @author Mihai Popa
 * 
 */
public class EmailGeneratorException extends Exception {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor.
     * 
     * @param message The message for this exception.
     * @param cause The exception that caused this exception.
     */
    public EmailGeneratorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Default Constructor.
     * 
     * @param cause The exception that caused this exception.
     */
    public EmailGeneratorException(final Throwable cause) {
        super(cause);
    }

}
