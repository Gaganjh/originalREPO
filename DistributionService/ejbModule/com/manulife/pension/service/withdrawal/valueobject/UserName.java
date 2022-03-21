package com.manulife.pension.service.withdrawal.valueobject;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * User name and profile ID.
 * 
 * @author penciau
 */
public class UserName extends BaseSerializableCloneableObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private Integer profileId;

    private String lastName;

    private String firstName;

    private String middleName;

    private String role; // Values: Participant, Plan Sponsor, TPA

    // public UserName(Integer profileId, String lastName, String firstName, String middleName) {
    // this.profileId = profileId;
    // this.lastName = lastName;
    // this.firstName = firstName;
    // this.middleName = middleName;
    // }

    /**
     * Default Constructor.
     * 
     */
    public UserName() {
        super();
    }

    /**
     * Constructor with user data.
     * 
     * @param profileId The profile ID of the user.
     * @param lastName The last name of the user.
     * @param firstName The first name of the user.
     */
    public UserName(final Integer profileId, final String lastName, final String firstName) {
        super();
        this.profileId = profileId;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * @return String - The firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName - The firstName to set.
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return String - The lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName - The lastName to set.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return String - The middleName.
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName - The middleName to set.
     */
    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return Integer - The profileId.
     */
    public Integer getProfileId() {
        return profileId;
    }

    /**
     * @param profileId - The profileId to set.
     */
    public void setProfileId(final Integer profileId) {
        this.profileId = profileId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
