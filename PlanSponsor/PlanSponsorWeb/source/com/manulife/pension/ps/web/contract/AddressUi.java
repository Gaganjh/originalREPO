package com.manulife.pension.ps.web.contract;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.contract.valueobject.Address;

public class AddressUi extends Address {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    public static final String LEGAL_HEADER = "Legal";

    public static final String MAILING_HEADER = "Mailing";

    public static final String COURIER_HEADER = "Courier";

    public static final String TRUSTEE_HEADER = "Trustee";

    public static final String AND = "and";

    public static final String SEPARATOR = ",";

    public static final String SPACE = " ";

    private String header;

    /**
     * Default constructor - constructs an address of the specified type.
     * 
     * @param type The address type to instantiate.
     */
    public AddressUi(final Address.Type type) {
        super(type);
    }

    /**
     * Default constructor - constructs an address of the specified type.
     * 
     * @param address The address to instantiate with.
     */
    public AddressUi(final Address address) {
        super(address.getType());
        setLine1(address.getLine1());
        setLine2(address.getLine2());
        setCity(address.getCity());
        setStateCode(address.getStateCode());
        setStateName(address.getStateName());
        setZipCode(address.getZipCode());
        setAttention(address.getAttention());
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Determines the header for a particular address based on the type.
     * 
     * @param type The type of address to determine a header for.
     * @return String - The header for the specified address type.
     */
    public static String getHeaderForType(final Address.Type type) {
        switch (type) {
            case LEGAL:
                return LEGAL_HEADER;
            case MAILING:
                return MAILING_HEADER;
            case COURIER:
                return COURIER_HEADER;
            case TRUSTEE:
                return TRUSTEE_HEADER;
            default:
                return StringUtils.EMPTY;
        }
    }
   
}
