package com.manulife.pension.bd.web.myprofile;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * This is the form object for MyProfileBrokerPersonalInfoAction
 * 
 * @author Ilamparithi
 * 
 */
public class MyProfileBrokerPersonalInfoForm extends BrokerEntitiesForm {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_PROFILE_FIRST_NAME = "profileFirstName";

    public static final String FIELD_PROFILE_LAST_NAME = "profileLastName";

    private String profileFirstName;

    private String profileLastName;

    private boolean changed;

    private boolean success = false;

    private List<WebBrokerEntityProfile> lastUpdatedBrokerEntityProfilesList;
    
    private HashMap<String, String> addressFlagMap;

    /**
     * Returns the profileFirstName
     * 
     * @return the profileFirstName
     */
    public String getProfileFirstName() {
        return profileFirstName;
    }

    /**
     * Sets the profileFirstName
     * 
     * @param profileFirstName the profileFirstName to set
     */
    public void setProfileFirstName(String profileFirstName) {
        this.profileFirstName = StringUtils.trimToEmpty(profileFirstName);
    }

    /**
     * Returns the profileLastName
     * 
     * @return the profileLastName
     */
    public String getProfileLastName() {
        return profileLastName;
    }

    /**
     * Sets the profileLastName
     * 
     * @param profileLastName the profileLastName to set
     */
    public void setProfileLastName(String profileLastName) {
        this.profileLastName = StringUtils.trimToEmpty(profileLastName);
    }

    /**
     * Returns a flag that indicates whether the form is dirty or not
     * 
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets a flag that indicates whether the form is dirty or not
     * 
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * @return the lastUpdatedBrokerEntityProfilesList
     */
    public List<WebBrokerEntityProfile> getLastUpdatedBrokerEntityProfilesList() {
        return lastUpdatedBrokerEntityProfilesList;
    }

    /**
     * @param lastUpdatedBrokerEntityProfilesList the lastUpdatedBrokerEntityProfilesList to set
     */
    public void setLastUpdatedBrokerEntityProfilesList(
            List<WebBrokerEntityProfile> lastUpdatedBrokerEntityProfilesList) {
        this.lastUpdatedBrokerEntityProfilesList = lastUpdatedBrokerEntityProfilesList;
    }

    /**
     * Return a flag to indicate whether the save action is success or not
     * 
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success flag
     * 
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns a map for profile entitys and the address flags (to indicate which address is
     * used.(home/mailing))
     * 
     * @return
     */
    public HashMap<String, String> getAddressFlagMap() {
        return addressFlagMap;
    }

    /**
     * Sets the address flag map
     * 
     * @param addressFlagMap
     */
    public void setAddressFlagMap(HashMap<String, String> addressFlagMap) {
        this.addressFlagMap = addressFlagMap;
    }

}
