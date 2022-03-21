package com.manulife.pension.ps.cache;

/*
  File: CofidProperties.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2016-12-27   natarde       		Initial version.
  
*/
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;


/**
 * This "static" utility class provides access to all of the application properties
 * in the Cofid properties file. (Copied from ezk)
 *
 * @author   natarde
 * @version  CS1.0  (Dec 27, 2016)
 * @see com.manulife.pension.ezk.web.ezkProperties.java
 **/

public class CofidProperties {

	public static final String APPNAME = "cofid";

		
	private static CofidProperties theInstance;
	
	private static final String PROPERTIES_FILE_NAME= "/cofid.properties";
		
	/** runtime properties */
	public static final Properties properties = new Properties();
	
	public static final String PLAN_REVIEW_FILE_NAME = "MF.{0}_planReviewFileName";
	public static final String PARTICIPANT_NOTICE_FILE_NAME_PREFIX = "{0}_ParticipantNoticeFileNamePrefix";
	
	public static final String NY_SubSectionHeading = "Cofid338FundReplacementForm.NY.SubSectionHeading";
	public static final String Form_MonthYear = "Cofid338FundReplacementForm.MonthYear";

            // Below static block to initialize property file
            static {
            	InputStream propertyFileStream = null;
                try {
                	
                	 Class cofidPropertiesClass= Class.forName(CofidProperties.class.getName());
                	 ClassLoader cofidClassLoader = null;
                	 if(cofidPropertiesClass!=null)
                		 cofidClassLoader=cofidPropertiesClass.getClassLoader();
                	if(cofidClassLoader!=null)
                	{
                    propertyFileStream = cofidClassLoader.getResourceAsStream(PROPERTIES_FILE_NAME);
                    properties.load(propertyFileStream);
                	}
                } catch (Throwable e) {
                    SystemException se = new SystemException(e,
                            "CofidProperties - static: Static block failed for cofid.properties");
                    throw ExceptionHandlerUtility.wrap(se);
                }finally{
                		if(propertyFileStream != null)
							try {
								propertyFileStream.close();
							} catch (IOException e) {
								 SystemException se = new SystemException(e,
				                            "CofidProperties - static: Static block failed for cofid.properties");
							}
        			
                }
            }
	
		/**
	 * Return the singleton instance.
	 **/
	
	
	public static CofidProperties getInstance() throws SystemException {

		if (theInstance == null) {
			theInstance = new CofidProperties();
		}
		return theInstance;
	}
	

	/**
	 * Provides the normal getProperty() interface
	 * by delegating to the internal Properties object
	 * NOTE: null will be returned if the key cannot be found.
	 *
	 * @param	java.lang.String - the property name to lookup
	 * @return	java.lang.String - value of the requested property
	 **/
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	}
