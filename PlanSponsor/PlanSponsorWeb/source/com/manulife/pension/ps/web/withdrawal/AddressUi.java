/*
 * AddressUi.java,v 1.1.2.1 2007/01/18 14:25:10 Paul_Glenn Exp
 * AddressUi.java,v
 * Revision 1.1.2.1  2007/01/18 14:25:10  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.ps.web.withdrawal;

import java.util.ArrayList;
import java.util.Collection;

import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.validator.ValidationError;

/**
 * This class contains the UI representation of the {@link Address} object.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.1 2007/01/18 14:25:10
 */
public class AddressUi extends BaseWithdrawalUiObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private static final String[] UI_FIELDS = {};

    private static final String VO_BEAN_NAME = "address";

    private Address address;

    /**
     * Default Constructor.
     * 
     * @param address The {@link Address} to use.
     */
    public AddressUi(final Address address) {
        super(UI_FIELDS, VO_BEAN_NAME);
        this.address = address;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, address));

        return messages;

    }
}
