/*
 * WithdrawalMessage.java,v 1.2 2006/11/06 15:57:55 Paul_Glenn Exp
 * WithdrawalMessage.java,v
 * Revision 1.2  2006/11/06 15:57:55  Paul_Glenn
 * Update to messages.
 *
 */
package com.manulife.pension.service.withdrawal.common;

import java.util.ArrayList;
import java.util.Collection;

import com.manulife.pension.common.Message;

/**
 * WithdrawalMessage contains the business tier message that is translated at the web level into a
 * ValidationError.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.2 2006/11/06 15:57:55
 */
public class WithdrawalMessage extends Message {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private WithdrawalMessageType withdrawalMessageType;

    /**
     * Default Constructor.
     * 
     * @param withdrawalMessageType The withdrawal message type to create the message with.
     */
    public WithdrawalMessage(final WithdrawalMessageType withdrawalMessageType) {
        super();
        this.withdrawalMessageType = withdrawalMessageType;
        setParameters(new ArrayList(0));
    }

    /**
     * Default Constructor.
     * 
     * @param withdrawalMessageType The withdrawal message type to create the message with.
     * @param propertyNames the propertyNames to set
     */
    public WithdrawalMessage(final WithdrawalMessageType withdrawalMessageType,
            final Collection<String> propertyNames) {
        super();
        this.withdrawalMessageType = withdrawalMessageType;
        setPropertyNames(propertyNames);
        setParameters(new ArrayList(0));
    }

    /**
     * Default Constructor.
     * 
     * @param withdrawalMessageType The withdrawal message type to create the message with.
     * @param propertyName the propertyName to set
     */
    public WithdrawalMessage(final WithdrawalMessageType withdrawalMessageType,
            final String propertyName) {
        super();
        this.withdrawalMessageType = withdrawalMessageType;
        // setPropertyNames(new ArrayList<String>(1) {
        // {
        // add(propertyName);
        // }
        // });

        final Collection<String> properties = new ArrayList<String>(1);
        properties.add(propertyName);
        setPropertyNames(properties);
        setParameters(new ArrayList(0));
    }

    /**
     * Default Constructor.
     * 
     * @param withdrawalMessageType The withdrawal message type to create the message with.
     * @param propertyName the propertyName to set
     * @param parameters A Collection of parameters to be used by the message.
     */
    public WithdrawalMessage(final WithdrawalMessageType withdrawalMessageType,
            final String propertyName, final Collection parameters) {
        super();
        this.withdrawalMessageType = withdrawalMessageType;

        final Collection<String> properties = new ArrayList<String>(1);
        properties.add(propertyName);
        setPropertyNames(properties);
        setParameters(parameters);
    }

    /**
     * Default Constructor.
     * 
     * @param withdrawalMessageType The withdrawal message type to create the message with.
     * @param propertyNames the propertyNames to set
     * @param parameters A Collection of parameters to be used by the message.
     */
    public WithdrawalMessage(final WithdrawalMessageType withdrawalMessageType,
            final Collection<String> propertyNames, final Collection parameters) {
        super();
        this.withdrawalMessageType = withdrawalMessageType;
        setPropertyNames(propertyNames);
        setParameters(parameters);
    }

    /**
     * @return the withdrawalMessageType
     */
    public WithdrawalMessageType getWithdrawalMessageType() {
        return withdrawalMessageType;
    }

    /**
     * @param withdrawalMessageType the withdrawalMessageType to set
     */
    public void setWithdrawalMessageType(final WithdrawalMessageType withdrawalMessageType) {
        this.withdrawalMessageType = withdrawalMessageType;
    }

}
