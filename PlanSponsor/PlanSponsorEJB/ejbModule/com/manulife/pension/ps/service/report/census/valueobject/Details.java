package com.manulife.pension.ps.service.report.census.valueobject;

import java.util.Date;

/**
 * Interface added to allow common processing in custom tags 
 * based on different detail beans
 * 
 * @author patuadr
 *
 */
public interface Details {
    public Date getBirthDate();

    public String getFirstName();

    public String getLastName();

    public String getProfileId();

    public String getSsn();

    public Date getHireDate();

    public boolean isParticipantInd();

    public String getDivision();

    public String getEmployerDesignatedID();

    public void setEmployerDesignatedID(String employerDesignatedID);

    public String getEnrollmentMethod();

    public Date getEnrollmentProcessedDate();

    public void setEnrollmentProcessedDate(Date enrollmentProcessedDate);
    
    public String getEnrollmentStatus();
    
    public void setProfileId(String profileId);


}
