package com.manulife.pension.bd.web.myprofile;

import java.util.List;

import com.manulife.pension.platform.web.controller.AutoForm;

public class BrokerEntitiesForm extends AutoForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ADDRESS = "address";

    public static final String FIELD_PROFILE_FIRST_NAME = "profileFirstName";

    public static final String FIELD_PROFILE_LAST_NAME = "profileLastName";

    public static final String FIELD_CONTACT_FIRST_NAME = "firstName";

    public static final String FIELD_CONTACT_MIDDLE_INITIAL = "middleInit";

    public static final String FIELD_CONTACT_LAST_NAME = "lastName";

    public static final String FIELD_CONTACT_COMPANY_NAME = "orgName";

    public static final String FIELD_CONTACT_EMAIL = "emailAddress";

    public static final String FIELD_TELEPHONE_NUMBER = "phoneNum";

    public static final String FIELD_MOBILE_NUMBER = "cellPhoneNum";

    public static final String FIELD_FAX_NUMBER = "faxNum";

    private List<WebBrokerEntityProfile> brokerEntityProfilesList;

    private long primaryBrokerPartyId;

    /**
     * Returns a list of WebBrokerEntityProfile objects
     * 
     * @return the brokerEntityProfilesList
     */
    public List<WebBrokerEntityProfile> getBrokerEntityProfilesList() {
        return brokerEntityProfilesList;
    }

    /**
     * Sets the brokerEntityProfilesList
     * 
     * @param brokerEntityProfilesList the brokerEntityProfilesList to set
     */
    public void setBrokerEntityProfilesList(List<WebBrokerEntityProfile> brokerEntityProfilesList) {
        this.brokerEntityProfilesList = brokerEntityProfilesList;
    }

    /**
     * This method is used by struts to populate the indexed properties to the
     * brokerEntityProfilesList
     * 
     * @param profile
     */
    public void setBrokerEntityProfile(int index, WebBrokerEntityProfile profile) {
        this.brokerEntityProfilesList.add(index,profile);
    }

    /**
     * This method is used by struts to retrieve the WebBrokerEntityProfile object from the list and
     * then populate with the indexed properties
     * 
     * @param index
     * @return WebBrokerEntityProfile
     */
    public WebBrokerEntityProfile getBrokerEntityProfile(int index) {
        return brokerEntityProfilesList.get(index);
    }

    public long getPrimaryBrokerPartyId() {
        return primaryBrokerPartyId;
    }

    public void setPrimaryBrokerPartyId(long primaryBrokerPartyId) {
        this.primaryBrokerPartyId = primaryBrokerPartyId;
    }
}
