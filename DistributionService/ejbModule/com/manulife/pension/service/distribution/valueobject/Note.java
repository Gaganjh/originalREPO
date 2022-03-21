package com.manulife.pension.service.distribution.valueobject;

import java.sql.Timestamp;

public interface Note {

    /**
     * Note type code for participant to administrator notes.
     */
    String PARTICIPANT_TO_ADMIN_TYPE_CODE = "PA";

    /**
     * Note type code for administrator to participant notes.
     */
    String ADMIN_TO_PARTICIPANT_TYPE_CODE = "AP";

    /**
     * Note type code for administrator to administrator notes.
     */
    String ADMIN_TO_ADMIN_TYPE_CODE = "AA";

    /**
     * Maximum length for notes.
     */
    int MAXIMUM_LENGTH = 750;

	public boolean isBlank();
	public String getNoteTypeCode();
	public void setNoteTypeCode(String noteTypeCode);
	
	public String getNote();
	public void setNote(String note);
	
	public Integer getCreatedById();
	public void setCreatedById(final Integer createdById);
    
	public Timestamp getCreated();
	public void setCreated(final Timestamp created);
    
	public Integer getLastUpdatedById();
	public void setLastUpdatedById(final Integer lastUpdatedById);
    
	public Timestamp getLastUpdated();
    public void setLastUpdated(final Timestamp lastUpdated);	
}
