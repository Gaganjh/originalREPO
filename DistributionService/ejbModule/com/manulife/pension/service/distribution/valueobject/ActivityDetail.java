package com.manulife.pension.service.distribution.valueobject;

import java.sql.Timestamp;

public interface ActivityDetail {

    String TYPE_ORIGINAL = "O";
    
    String TYPE_SAVED = "S";
    
    String TYPE_SYSTEM_OF_RECORD = "Y";
    
	String getTypeCode();
	
	void setTypeCode(String type);
	
	/**
	 * @return the item number
	 */
	Integer getItemNumber();

	/**
	 * @param itemNumber the item number
	 */
	void setItemNumber(final Integer itemNumber);

	/**
	 * @return the last updated by id
	 */
	Integer getLastUpdatedById();

	/**
	 * @param lastUpdatedById the last updated by id
	 */
	void setLastUpdatedById(final Integer lastUpdatedById);

	/**
	 * @return the last updated time stamp
	 */
	Timestamp getLastUpdated();

	/**
	 * @param lastUpdated the last updated time stamp
	 */
	void setLastUpdated(final Timestamp lastUpdated);

	/**
	 * @return the value
	 */
	String getValue();

	/**
	 * @param value the value
	 */
	void setValue(final String value);

}